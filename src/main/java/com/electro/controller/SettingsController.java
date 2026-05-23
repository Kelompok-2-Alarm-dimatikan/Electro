package com.electro.controller;
import com.electro.model.User;
import com.electro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String settingsPage() {
        return "settings"; 
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal UserDetails currentUser,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 Model model) {
        
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Password saat ini tidak sesuai!");
            return "settings";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "Konfirmasi password baru tidak cocok!");
            return "settings";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("success", "Password berhasil diperbarui!");
        return "settings";
    }
}