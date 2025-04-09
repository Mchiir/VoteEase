package mchiir.com.vote.controllers;

import mchiir.com.vote.dtos.ElectionDTO;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/elections")
public class ElectionController {
    @Autowired
    private ModelMapper modelMapper;

    private final ElectionService electionService;
    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @PostMapping("/add")
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
            return "election";
        }
    }
    @GetMapping("/create")
    public String showForm(Model model) {
        model.addAttribute("electionDTO", new ElectionDTO());
        return "election";
    }
}
