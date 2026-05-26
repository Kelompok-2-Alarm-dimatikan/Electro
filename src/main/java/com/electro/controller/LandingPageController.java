package com.electro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPageController {

    @GetMapping("/")
    public String landingPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/electronic";
        }
        return "LandingPage"; 
    }
}
