package mchiir.com.vote.controllers;

import lombok.AllArgsConstructor;
import mchiir.com.vote.services.DateFormatingService;
import mchiir.com.vote.services.DateFormatingService;
import mchiir.com.vote.services.UserService;
import mchiir.com.vote.dtos.ElectionDTO;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/elections")
@AllArgsConstructor
public class ElectionController {
    private final ModelMapper modelMapper;
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

            model.addAttribute("message", "Election created successfully!");
            model.addAttribute("messageType", "success");

            return "redirect:/api/elections/dashboard";
        } catch (Exception e) {
            model.addAttribute("message", "Error creating election: " + e.getMessage());
            model.addAttribute("messageType", "danger");
            return "util/election";
        }
    }
}