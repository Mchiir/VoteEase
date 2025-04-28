package mchiir.com.vote.controllers;

import lombok.AllArgsConstructor;
import mchiir.com.vote.dtos.ElectionDTOFinal;
import mchiir.com.vote.models.enums.ElectionStatus;
import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/vote")
@AllArgsConstructor
public class VoteController {
    @Autowired
    private final ElectionService electionService;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping("/enroll")
    public String enroll(Model model) {

        model.addAttribute("message", model.containsAttribute("message") ?
                model.getAttribute("message") : "Please enter the shared election code");
        model.addAttribute("messageType", model.containsAttribute("messageType") ?
                model.getAttribute("messageType") : "info");

        return "util/vote_init";
    }

    @PostMapping("/enroll")
    public String enrollToVote(@RequestParam("otc") String otc, Model model) {
        try {
            Election election = electionService.getElectionByOtc(otc);
            if (election == null) {
                model.addAttribute("message", "Election not found with the provided code!");
                model.addAttribute("messageType", "danger");
                return "redirect:/api/vote/enroll";  // back to enroll page
            }
            if(election.getStatus() == ElectionStatus.CLOSED) {
                model.addAttribute("message", "Election has ended");
                model.addAttribute("messageType", "danger");
                return "redirect:/api/vote/enroll";
            }

            ElectionDTOFinal cleanElection = modelMapper.map(election, ElectionDTOFinal.class);
            // Group candidates by post
            Map<String, List<Candidate>> postsWithCandidates = cleanElection.getCandidates().stream()
                    .collect(Collectors.groupingBy(Candidate::getPost));

            model.addAttribute("election", cleanElection);
            model.addAttribute("postsWithCandidates", postsWithCandidates);
            model.addAttribute("message", "Successfully enrolled!");
            model.addAttribute("messageType", "success");

            return "util/election";

        } catch (Exception e) {
            model.addAttribute("message", "An unexpected error occurred. Please try again later.");
            model.addAttribute("messageType", "danger");
            return "redirect:/api/vote/enroll";  // fallback
        }
    }

}
