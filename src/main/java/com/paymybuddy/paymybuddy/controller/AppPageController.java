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

/**
 * Contrôleur MVC des pages applicatives après connexion.
 */
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

    /**
     * Affiche la page de transfert avec relations et historique envoyé.
     */
    @GetMapping("/transfer")
    public String showTransferPage(HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User currentUser = userService.getById(userId);
        model.addAttribute("relations", currentUser.getConnections());
        model.addAttribute("currentUserId", userId);

        List<Transfer> history = transferRepository
                .findBySenderIdOrderByCreatedAtDesc(userId);

        model.addAttribute("history", history);

        return "transfer";
    }

    /**
     * Soumet un transfert depuis le formulaire web.
     */
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
            transferService.createTransfer(userId, connectionId, amount, description);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Transfert effectué avec succès.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/transfer";
    }

    /**
     * Affiche la page d'ajout de relation.
     */
    @GetMapping("/add-connection")
    public String showAddConnectionPage(HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        return "add-connection";
    }

    /**
     * Affiche la page profil avec les données courantes.
     */
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

    /**
     * Met à jour username/email du profil connecté.
     */
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
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }


    /**
     * Met à jour le mot de passe du profil connecté.
     */
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
