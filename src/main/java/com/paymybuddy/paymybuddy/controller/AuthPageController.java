package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur MVC des écrans d'authentification (login/register/logout).
 */
@Controller
public class AuthPageController {

    private final UserService userService;

    public AuthPageController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Affiche la page de connexion.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Affiche la page d'inscription.
     */
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    /**
     * Traite l'inscription via formulaire HTML.
     */
    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        try {
            User user = userService.register(username, email, password);
            model.addAttribute("successMessage", "Compte créé, vous pouvez vous connecter.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    /**
     * Traite la connexion et stocke les informations minimales en session.
     */
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        try {
            User user = userService.authenticateByEmail(email, password);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            return "redirect:/transfer";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Identifiants invalides");
            return "login";
        }
    }

    /**
     * Déconnecte l'utilisateur en invalidant la session courante.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
