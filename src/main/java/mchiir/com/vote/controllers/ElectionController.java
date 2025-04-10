package mchiir.com.vote.controllers;

import mchiir.com.vote.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import mchiir.com.vote.dtos.ElectionDTO;
import mchiir.com.vote.dtos.UserDTO;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/elections")
public class ElectionController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    private final ElectionService electionService;
    public ElectionController(ElectionService electionService, UserService userService) {
        this.electionService = electionService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User user) {
        try {
            if (user == null) {
                return "redirect:/auth/login?error=true";
            }

            Guider guider = userService.findByEmail(user.getUsername()); // use email to get Guider
            List<Election> elections = electionService.getAllByGuider(guider);

            model.addAttribute("elections", elections);
            model.addAttribute("userDTO", modelMapper.map(guider, UserDTO.class));
            model.addAttribute("message", "Elections retrieved successfully");
            model.addAttribute("messageType", "success");
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "danger");
            return "dashboard";
        }
    }

    @GetMapping("/create")
    public String showForm(Model model, Principal principal) {
        if(principal == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("electionDTO", new ElectionDTO());
        return "util/election";
    }
    @PostMapping("/create")
    public String submitElection(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime,
                                 Model model) {
        try {
            // Decode URL-encoded dates (optional, only if required)
            // String decodedStartTime = URLDecoder.decode(startTime, "UTF-8");
            // String decodedEndTime = URLDecoder.decode(endTime, "UTF-8");

            // Convert decoded strings to Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date start = dateFormat.parse(startTime);  // Parsing the start time
            Date end = dateFormat.parse(endTime);      // Parsing the end time

            // Create ElectionDTO and populate it
            ElectionDTO electionDTO = new ElectionDTO();
            electionDTO.setTitle(title);
            electionDTO.setDescription(description);
            electionDTO.setStartTime(start);
            electionDTO.setEndTime(end);

            // Map DTO to entity
            Election election = modelMapper.map(electionDTO, Election.class);

            // Save election to the database
            electionService.createElection(election);

            // Format the start and end time to display in the desired format
            String formattedStartTime = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss").format(start);
            String formattedEndTime = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss").format(end);

            // Add formatted start and end time to the model for display
            model.addAttribute("formattedStartTime", formattedStartTime);
            model.addAttribute("formattedEndTime", formattedEndTime);

            model.addAttribute("message", "Election created successfully!");
            model.addAttribute("electionDTO", electionDTO);

            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "util/election";
        }
    }
}
