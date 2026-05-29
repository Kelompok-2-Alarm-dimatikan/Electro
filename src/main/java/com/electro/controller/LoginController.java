package com.electro.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {

        if (error != null) {
            if ("account_not_found".equals(error)) {
                model.addAttribute("error", "Akun Google Anda tidak ditemukan. Silakan daftar terlebih dahulu.");
            } else {
                model.addAttribute("error", "Username atau password salah!");
            }
        }
        return "login";
    }
}