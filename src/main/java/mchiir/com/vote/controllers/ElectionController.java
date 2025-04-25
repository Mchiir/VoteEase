package mchiir.com.vote.controllers;

import lombok.AllArgsConstructor;
import mchiir.com.vote.models.enums.ElectionStatus;
import mchiir.com.vote.services.DateFormatingService;
import mchiir.com.vote.services.UserService;
import mchiir.com.vote.dtos.ElectionDTO;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/elections")
@AllArgsConstructor
public class ElectionController {
    private final UserService userService;
    private final DateFormatingService dateFormatingServise;
    private final ElectionService electionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Guider guider = userService.findByEmail(email);

        if (guider == null) {
            return "redirect:/api/auth/login?error=true&message=Please login first";
        }

        try {
            List<Election> elections = electionService.getAllByGuider(guider);

            if(elections.isEmpty()){
                model.addAttribute("message", "No Elections created yet.");
                model.addAttribute("messageType", "info");
                return "dashboard";
            }
            // Sort by startTime descending
            elections.sort((e1, e2) -> e2.getStartTime().compareTo(e1.getStartTime()));

            // Format and set formatted time as Strings
            elections.forEach(election -> {
                election.setFormatedStartTime(dateFormatingServise.getFormattedDate("EEE dd/MM/yyyy HH:mm:ss", election.getStartTime()));
                election.setFormatedEndTime(dateFormatingServise.getFormattedDate("EEE dd/MM/yyyy HH:mm:ss", election.getEndTime()));
            });

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
    public String submitElection(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {
        String email = authentication.getName();
        Guider guider = userService.findByEmail(email);

        if (guider == null) {
            return "redirect:/api/auth/login?error=true&message=Please login first";
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            var election = new Election();
            election.setGuider(guider);
            election.setTitle(title);
            election.setDescription(description);
            election.setStartTime(start);
            election.setEndTime(end);

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
}