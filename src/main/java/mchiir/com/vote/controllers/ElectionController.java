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

    @PostMapping("/create")
    public String submitElection(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime,
                                 Model model) {
        try {
            // Decode URL-encoded dates
            String decodedStartTime = URLDecoder.decode(startTime, StandardCharsets.UTF_8);
            String decodedEndTime = URLDecoder.decode(endTime, StandardCharsets.UTF_8);

            // Convert decoded strings to Date objects
            Date start = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(decodedStartTime);
            Date end = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(decodedEndTime);

            ElectionDTO electionDTO = new ElectionDTO();
            electionDTO.setTitle(title);
            electionDTO.setDescription(description);
            electionDTO.setStartTime(start);
            electionDTO.setEndTime(end);

            // Map DTO to entity and save
            Election election = modelMapper.map(electionDTO, Election.class);
            electionService.createElection(election);

            model.addAttribute("message", "Election created successfully!");
            model.addAttribute("electionDTO", electionDTO);
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "election";
        }
    }
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("electionDTO", new ElectionDTO());
        return "election";
    }
}
