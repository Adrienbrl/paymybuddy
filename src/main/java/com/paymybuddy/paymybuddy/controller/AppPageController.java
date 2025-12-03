package com.paymybuddy.paymybuddy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppPageController {

    @GetMapping("/transfer")
    public String showTransferPage(HttpSession session, Model model) {
        return "transfer";
    }

    @GetMapping("/add-connection")
    public String showAddConnectionPage(HttpSession session, Model model) {
        return "add-connection";
    }

    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        return "profile";
    }
}

