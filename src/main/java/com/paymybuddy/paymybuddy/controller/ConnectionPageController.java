package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConnectionPageController {

    private final UserService userService;

    public ConnectionPageController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add-connection")
    public String addConnection(@RequestParam("email") String friendEmail, HttpSession session) {

        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/login";
        }
        User friend = userService.getByEmail(friendEmail);
        userService.addConnection(currentUserId, friend.getId());
        return "redirect:/transfer";
    }
}


