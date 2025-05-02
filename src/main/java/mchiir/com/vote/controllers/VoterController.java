package mchiir.com.vote.controllers;

import lombok.AllArgsConstructor;
import mchiir.com.vote.dtos.ElectionResultDTO;
import mchiir.com.vote.exceptions.ResourceNotFoundException;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.repositories.ElectionRepository;
import mchiir.com.vote.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/api/voter")
@AllArgsConstructor
public class VoterController {
    @Autowired
    private final ElectionService electionService;
    @Autowired
    private final ElectionRepository electionRepository;

    @GetMapping("/")
    public String join(Model model) {

        model.addAttribute("message", model.containsAttribute("message") ?
                model.getAttribute("message") : "Make sure you have election code!");
        model.addAttribute("messageType", model.containsAttribute("messageType") ?
                model.getAttribute("messageType") : "info");

        return "voter/voter_landing_page";
    }

    @GetMapping("/enroll")
    public String enroll(Model model) {

        model.addAttribute("message", model.containsAttribute("message") ?
                model.getAttribute("message") : "Please enter the shared election code");
        model.addAttribute("messageType", model.containsAttribute("messageType") ?
                model.getAttribute("messageType") : "info");

        return "voter/vote_init";
    }

    @GetMapping("/get_results_view")
    public String getResults(Model model) {

        model.addAttribute("message", model.containsAttribute("message") ?
                model.getAttribute("message") : "Make sure you have election code!");
        model.addAttribute("messageType", model.containsAttribute("messageType") ?
                model.getAttribute("messageType") : "info");

        return "voter/election_results_init";
    }

    @GetMapping("/{electionId}/get_results")
    public String viewResults(
            @PathVariable("electionId") UUID electionId,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            ElectionResultDTO electionResults = electionService.getElectionResultById(electionId);

            model.addAttribute("electionResult", electionResults);
            model.addAttribute("message", "Election results retrieved successfully");
            model.addAttribute("messageType", "success");

            return "voter/election_results";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", (e.getMessage() != null) ? e.getMessage() : "System error occurred");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/api/voter/get_results_view";
        }
    }

    @PostMapping("/get_results")
    public String viewResults(
            @RequestParam String electionCode,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            Election election = electionRepository.findByOtc(electionCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Election", "otc", electionCode));


            model.addAttribute("message", "Election results retrieved successfully");
            model.addAttribute("messageType", "success");

            return "redirect:/api/voter/"+ election.getId() +"/get_results";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", (e.getMessage() != null) ? e.getMessage() : "System error occurred");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/api/voter/get_results_view";
        }
    }
}
