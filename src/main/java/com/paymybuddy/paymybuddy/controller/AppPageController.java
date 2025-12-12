package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.service.TransferService;
import com.paymybuddy.paymybuddy.service.UserService;
import com.paymybuddy.paymybuddy.domain.Transfer;
import com.paymybuddy.paymybuddy.repository.TransferRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.math.BigDecimal;

@Controller
public class AppPageController {

    private final UserService userService;
    private final TransferService transferService;
    private final TransferRepository transferRepository;

    public AppPageController(UserService userService,
                             TransferService transferService, TransferRepository transferRepository) {
        this.userService = userService;
        this.transferService = transferService;
        this.transferRepository = transferRepository;
    }

    @GetMapping("/transfer")
    public String showTransferPage(HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User currentUser = userService.getById(userId);
        model.addAttribute("relations", currentUser.getConnections());
        model.addAttribute("currentUserId", userId);
        // on remplira l'historique de transactions plus tard

        List<Transfer> history = transferRepository
                .findBySenderIdOrderByCreatedAtDesc(userId);

        model.addAttribute("history", history);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String createTransfer(@RequestParam("connectionId") Integer connectionId,
                                 @RequestParam("description") String description,
                                 @RequestParam("amount") BigDecimal amount,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        if (connectionId == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Veuillez sélectionner une relation.");
            return "redirect:/transfer";
        }

        try {
            transferService.createTransfer(
                    userId,
                    connectionId,
                    amount,
                    description
            );
            redirectAttributes.addFlashAttribute("successMessage",
                    "Transfert effectué avec succès.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/transfer";
    }

    @GetMapping("/add-connection")
    public String showAddConnectionPage(HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        return "add-connection";
    }

    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User currentUser = userService.getById(userId);
        model.addAttribute("user", currentUser);

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @ModelAttribute("user") User formUser,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            userService.updateProfile(userId, formUser.getUsername(), formUser.getEmail());
            redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour.");
        } catch (IllegalArgumentException e) {
            // 👉 ça peut être "Aucune modification détectée." ou "Username déjà utilisé", etc.
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }


    @PostMapping("/profile/password")
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Les nouveaux mots de passe ne correspondent pas.");
            return "redirect:/profile";
        }

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Mot de passe mis à jour.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
}
