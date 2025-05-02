package mchiir.com.vote.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mchiir.com.vote.dtos.ElectionDTOFinal;
import mchiir.com.vote.dtos.VoteDTOFinal;
import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.ElectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String enrollToVote(@RequestParam("otc") String otc,
                               RedirectAttributes redirectAttributes) {
        try {
            Election election = electionService.getElectionByOtc(otc.trim());

            return "redirect:/api/vote/" + election.getId() + "/election";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", (e.getMessage() != null) ? e.getMessage() : "A system error occurred");
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/api/vote/enroll";
    }

    @GetMapping("/{electionId}/election")
    public String getElection(@PathVariable("electionId") UUID electionId,
                              Model model) {
        Election election = electionService.getElectionById(electionId);

        if (election == null) {
            model.addAttribute("message", "Election not found.");
            model.addAttribute("messageType", "danger");
            return "redirect:/api/vote/enroll";
        }

        ElectionDTOFinal cleanElection = modelMapper.map(election, ElectionDTOFinal.class);
        cleanElection.setCandidateName(election.getGuider().getName());

        Map<String, List<Candidate>> postsWithCandidates = election.getCandidates().stream()
                .collect(Collectors.groupingBy(Candidate::getPost));

        // Only create a new VoteDTOFinal if not already present (important when redirecting after validation errors)
        if (!model.containsAttribute("voteDTO")) {
            VoteDTOFinal voteDTO = new VoteDTOFinal();
            voteDTO.setElectionId(electionId);
            model.addAttribute("voteDTO", voteDTO);
        }

        model.addAttribute("election", cleanElection);
        model.addAttribute("postsWithCandidates", postsWithCandidates);

        // Default message if no message is already set
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", "Successfully enrolled, cast your vote and make your voice count");
            model.addAttribute("messageType", "info");
        }

        return "util/election";
    }


    @PostMapping("/cast")
    public String castVote(@Valid @ModelAttribute VoteDTOFinal voteDTO,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Please select a candidate for each post.");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            redirectAttributes.addFlashAttribute("voteDTO", voteDTO);  // preserve submitted data
            return "redirect:/api/vote/" + voteDTO.getElectionId() + "/election";
        }

        try {
            electionService.saveVote(voteDTO);
            redirectAttributes.addFlashAttribute("message", "Vote submitted successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/api/vote/confirmation";  // Better to redirect after POST (PRG pattern)
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",  (e.getMessage() != null) ? e.getMessage() : "A system error occurred");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            redirectAttributes.addFlashAttribute("voteDTO", voteDTO);  // preserve submitted data
            return "redirect:/api/vote/" + voteDTO.getElectionId() + "/election";
        }
    }

    @GetMapping("/confirmation")
    public String voteConfirmation(Model model) {

        model.addAttribute("message", model.containsAttribute("message") ?
                model.getAttribute("message") : "");
        model.addAttribute("messageType", model.containsAttribute("messageType") ?
                model.getAttribute("messageType") : "");

        return "util/vote_confirmation";  // separate GET page after voting
    }
}
