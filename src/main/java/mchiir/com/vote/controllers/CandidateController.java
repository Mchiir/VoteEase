package mchiir.com.vote.controllers;

import lombok.AllArgsConstructor;
import mchiir.com.vote.dtos.CandidateDTO;
import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.services.CandidateService;
import mchiir.com.vote.services.ElectionService;
import mchiir.com.vote.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/api/candidate")
public class CandidateController {
    @Autowired
    private final CandidateService candidateService;
    @Autowired
    private final ElectionService electionService;
    @Autowired
    private final UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{electionId}")
    public String showCandidateManagement(
            @PathVariable("electionId") String electionId,
            @RequestParam(required = false) String message,
            @RequestParam(required = false, defaultValue = "info") String messageType,
            RedirectAttributes redirectAttributes,
            Model model) {

        model.addAttribute("candidateDTO", new CandidateDTO());
        if(model.getAttribute("electionId") != null && model.getAttribute("electionId") != "") {
            electionId = model.getAttribute("electionId").toString();
        }

        if (electionId != null && !electionId.isEmpty()) {
            try {
                UUID uuid = UUID.fromString(electionId);
                Election election = electionService.getElectionById(uuid);
                List<Candidate> candidates = candidateService.getCandidatesByElection(election);

                model.addAttribute("candidates", candidates);
                model.addAttribute("currentElection", election);

                if (message == null && candidates.isEmpty()) {
                    message = "No candidates added to the election";
                    messageType = "info";
                }
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("message", e.getMessage());
                redirectAttributes.addFlashAttribute("messageType", "danger");
                return "redirect:/api/elections/dashboard";
            }
        }

        model.addAttribute("message", model.containsAttribute("message") ?
                model.getAttribute("message") : message);
        model.addAttribute("messageType", model.containsAttribute("messageType") ?
                model.getAttribute("messageType") : messageType);

        return "util/candidates";
    }

    @PostMapping("/{electionId}/add")
    public String createCandidate(
            @ModelAttribute CandidateDTO candidateDTO,
            @PathVariable String electionId,
            RedirectAttributes redirectAttributes) {

        try {
            Candidate candidate = modelMapper.map(candidateDTO, Candidate.class);
            candidate.setName(candidateDTO.getName());
            candidate.setEmail(candidateDTO.getEmail());
            candidate.setParty(candidateDTO.getParty());
            candidate.setPost(candidateDTO.getPost());
            candidate.setElection(electionService.getElectionById(UUID.fromString(electionId)));
            candidateService.createCandidate(candidate);

            redirectAttributes.addAttribute("message", "Candidate created sucessfully");
            redirectAttributes.addAttribute("messageType", "success");
            redirectAttributes.addAttribute("electionId", electionId);

        } catch (Exception e) {
            redirectAttributes.addAttribute("message",
                    "Error creating candidate: " + e.getMessage());
            redirectAttributes.addAttribute("messageType", "error");
            redirectAttributes.addAttribute("electionId", electionId);
        }

        return "redirect:/api/candidate/{electionId}";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        CandidateDTO candidateDTO = modelMapper.map(candidateService.getCandidateById(id), CandidateDTO.class);

        model.addAttribute("candidateDTO", candidateDTO);
        return "util/editCandidate";
    }

    @PostMapping("/update")
    public String updateCandidate(@ModelAttribute Candidate candidate,
                                  RedirectAttributes redirectAttributes) {
       candidateService.updateCandidate(candidate.getId(), candidate);
        redirectAttributes.addFlashAttribute("message",
                "Candidate updated successfully!");
        return "redirect:/api/candidate/";
    }


    @PostMapping("/delete_candidate")
    public String deleteCandidate(@RequestParam UUID candidateId,
                                  @RequestParam UUID electionId,
                                  RedirectAttributes redirectAttributes) {
        candidateService.deleteCandidate(candidateId);
        redirectAttributes.addAttribute("message",
                "Candidate deleted successfully!");
        redirectAttributes.addAttribute("electionId", electionId);
        redirectAttributes.addAttribute("messageType", "info");

        return "redirect:/api/candidate/{electionId}";
    }
}