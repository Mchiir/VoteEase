package mchiir.com.vote.controllers;

import lombok.AllArgsConstructor;
import mchiir.com.vote.models.enums.ElectionStatus;
import mchiir.com.vote.services.DateFormatingService;
import mchiir.com.vote.services.UserService;
import mchiir.com.vote.dtos.ElectionDTO;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/elections")
@AllArgsConstructor
public class ElectionController {
    private final UserService userService;
    private final DateFormatingService dateFormatingServise;
    private final ElectionService electionService;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Guider guider = userService.findByEmail(email);

        if (guider == null) {
            redirectAttributes.addAttribute("message", "Please login first");
            redirectAttributes.addAttribute("messageType", "warning");
            return "redirect:/api/auth/login?error";
        }

        try {
            List<Election> elections = electionService.getAllByGuider(guider);

            if(elections.isEmpty()){
                model.addAttribute("message", "No Elections created yet.");
                model.addAttribute("messageType", "info");
                return "dashboard";
            }

            // Sort by startTime descending
            elections.sort((e1, e2) -> {
                // Handle null cases first
                if (e1.getStartTime() == null && e2.getStartTime() == null) return 0;
                if (e1.getStartTime() == null) return 1;  // Put nulls at end
                if (e2.getStartTime() == null) return -1; // Put nulls at end

                // Both dates exist - compare in reverse order (newest first)
                return e2.getStartTime().compareTo(e1.getStartTime());
            });

            // Format and set formatted time as Strings
            elections.forEach(election -> {
                if (election.getStartTime() != null) {
                    election.setFormatedStartTime(
                            dateFormatingServise.getFormattedDate("EEE dd/MM/yyyy HH:mm:ss", election.getStartTime())
                    );
                }
                if (election.getEndTime() != null) {
                    election.setFormatedEndTime(
                            dateFormatingServise.getFormattedDate("EEE dd/MM/yyyy HH:mm:ss", election.getEndTime())
                    );
                }
            });

            elections = elections.stream()
                    .filter(election -> !election.getIsDeleted())
                    .collect(Collectors.toList());

            model.addAttribute("elections", elections);
            model.addAttribute("message", model.containsAttribute("message") ?
                    model.getAttribute("message") : "Elections retrieved successfully");
            model.addAttribute("messageType", model.containsAttribute("messageType") ?
                    model.getAttribute("messageType") : "info");

            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "danger");
            return "dashboard";
        }
    }

    @GetMapping("/create")
    public String showForm(Model model, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/api/auth/login?error=true&message=Please login first";
        }

        model.addAttribute("electionDTO", new ElectionDTO());
        return "util/election";
    }

    @PostMapping("/create")
    public String submitElection(@ModelAttribute ElectionDTO electionDTO,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {
        String email = authentication.getName();
        Guider guider = userService.findByEmail(email);

        if (guider == null) {
            redirectAttributes.addFlashAttribute("message", "Please login first");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/api/auth/login?error";
        }

        try {
            var election = new Election();
            election.setGuider(guider);
            election.setTitle(electionDTO.getTitle());
            election.setDescription(electionDTO.getDescription());

            electionService.createElection(election);

            redirectAttributes.addFlashAttribute("message", "Election created successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/api/elections/dashboard";
        } catch (Exception e) {
            model.addAttribute("message", "Error creating election: " + e.getMessage());
            model.addAttribute("messageType", "danger");
            return "util/election";
        }
    }

    @PostMapping("/start_election")
    public String startElection(
            @RequestParam String electionId,
            RedirectAttributes redirectAttributes,
            Model model) {
        UUID uuid = UUID.fromString(electionId);
        Election election = electionService.getElectionById(uuid);

        String otc = electionService.generateOtc();

        election.setOtc(otc);
        election.setStatus(ElectionStatus.ONGOING);
        election.setStartTime(new Date());
        electionService.updateElection(uuid, election);

        model.addAttribute("electionTitle", election.getTitle());
        model.addAttribute("otc", otc);

        return "util/otc";
    }

    @PostMapping("/end_election")
    public String endElection(
            @RequestParam String electionId,
            RedirectAttributes redirectAttributes) {
        UUID uuid = UUID.fromString(electionId);
        Election election = electionService.getElectionById(uuid);

        election.setStatus(ElectionStatus.CLOSED);
        election.setEndTime(new Date());
        electionService.updateElection(uuid, election);

        redirectAttributes.addFlashAttribute("message", "Election ended successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/api/elections/dashboard";  // Redirect to dashboard
    }

    @GetMapping("/results")
    public String viewResults(
            @RequestParam String electionId,
            Model model) {
        UUID uuid = UUID.fromString(electionId);
        Election election = electionService.getElectionById(uuid);

        if (election.getStatus() != ElectionStatus.CLOSED) {
            model.addAttribute("message", "The election has not ended yet.");
            model.addAttribute("messageType", "danger");
            return "redirect:/api/elections/dashboard";  // Redirect if election is still ongoing or upcoming
        }

        // Fetch election results
//        List<Result> results = resultService.getResultsForElection(uuid);  // Assuming you have a result service
//        model.addAttribute("election", election);
//        model.addAttribute("results", results);

        return "util/election_results";  // View to display results
    }

    @PostMapping("/delete_election")
    public String deleteElection(@RequestParam("electionId") UUID electionId,
                                 RedirectAttributes redirectAttributes) {
        try {
            Election election = electionService.getElectionById(electionId);
            if (election != null && !election.getIsDeleted()) {
                election.setDeleted(true);
                electionService.updateElection(electionId, election);
                redirectAttributes.addFlashAttribute("message", "Election deleted successfully.");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Election not found or already deleted.");
                redirectAttributes.addFlashAttribute("messageType", "warning");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to delete election.");
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/api/elections/dashboard";
    }
}