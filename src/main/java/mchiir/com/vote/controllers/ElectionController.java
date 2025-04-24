package mchiir.com.vote.controllers;

import lombok.AllArgsConstructor;
import mchiir.com.vote.content.MyAppUserService;
import mchiir.com.vote.services.UserService;
import mchiir.com.vote.dtos.ElectionDTO;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/elections")
@AllArgsConstructor
public class ElectionController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final MyAppUserService myAppUserService;
    private final ElectionService electionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Guider guider = userService.findByEmail(myAppUserService.getCurrentUserEmail());

        if (guider == null) {
            return "redirect:/api/auth/login?error=true";
        }

        try {
            List<Election> elections = electionService.getAllByGuider(guider);

            model.addAttribute("elections", elections);
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
    public String showForm(Model model) {
        model.addAttribute("electionDTO", new ElectionDTO());
        return "util/election";
    }

    @PostMapping("/create")
    public String submitElection(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime,
                                 Model model) {
        Guider guider = userService.findByEmail(myAppUserService.getCurrentUserEmail());

        if (guider == null) {
            return "redirect:/api/auth/login";
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            ElectionDTO electionDTO = new ElectionDTO();
            electionDTO.setTitle(title);
            electionDTO.setDescription(description);
            electionDTO.setStartTime(start);
            electionDTO.setEndTime(end);

            Election election = modelMapper.map(electionDTO, Election.class);

            // Optionally associate with user:
            election.setGuider(guider);

            electionService.createElection(election);

            String formattedStartTime = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss").format(start);
            String formattedEndTime = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss").format(end);

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
