package mchiir.com.vote.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mchiir.com.vote.dtos.ElectionResultDTO;
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
import org.springframework.validation.BindingResult;
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

            if(elections.isEmpty()){
                model.addAttribute("message", "No Elections created yet");
                model.addAttribute("messageType", "info");
                return "dashboard";
            }

            model.addAttribute("elections", elections);
            model.addAttribute("message", model.containsAttribute("message") ?
                    model.getAttribute("message") : "Elections retrieved successfully, please start an election to get election code");
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
        return "util/new_election";
    }

    @PostMapping("/create")
    public String submitElection(@Valid @ModelAttribute ElectionDTO electionDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication,
                                 Model model) {
        String email = authentication.getName();
        Guider guider = userService.findByEmail(email);

        if (guider == null) {
            redirectAttributes.addFlashAttribute("message", "Please login first");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/api/auth/login?error";
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("message", "Please correct the errors in the form.");
            model.addAttribute("messageType", "danger");
            return "util/new_election";
        }

        try {
            var election = new Election();
            election.setGuider(guider);
            election.setTitle(electionDTO.getTitle());
            election.setDescription(electionDTO.getDescription());
            election.setMax_voters_count(electionDTO.getMaxVotersCount());

            electionService.createElection(election);

            redirectAttributes.addFlashAttribute("message", "Election created successfully!, please consider adding candidates");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/api/elections/dashboard";
        } catch (Exception e) {
            model.addAttribute("message", "Error creating election: " + e.getMessage());
            model.addAttribute("messageType", "danger");
            return "util/new_election";
        }
    }

    @PostMapping("/start_election")
    public String startElection(
            @RequestParam String electionId,
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

    @GetMapping("/{electionId}/results")
    public String viewResults(
            @PathVariable("electionId") UUID electionId,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            ElectionResultDTO electionResults = electionService.getElectionResult(electionId);
            model.addAttribute("electionResult", electionResults);
            model.addAttribute("message", "Election results retrieved successfully");
            model.addAttribute("messageType", "success");

            return "util/election_results";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", (e.getMessage() != null) ? e.getMessage() : "System error occurred");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/api/elections/dashboard";
        }
    }

    @PostMapping("/toggle_election_hide")
    public String toggleElectionHide(@RequestParam("electionId") UUID electionId,
                                 RedirectAttributes redirectAttributes) {
        try {
            Election election = electionService.getElectionById(electionId);
            if (election != null) {
                election.setIsHidden(!election.getIsHidden());
                electionService.updateElection(electionId, election);
//                redirectAttributes.addFlashAttribute("message", "Election hidden successfully.");
//                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Election not found or already hidden.");
                redirectAttributes.addFlashAttribute("messageType", "warning");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to hide the election.");
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/api/elections/dashboard";
    }
}