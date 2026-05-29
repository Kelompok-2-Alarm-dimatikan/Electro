package com.electro.controller;
import com.electro.model.User;
import com.electro.repository.ComplaintRepository;
import com.electro.repository.OrderRepository;
import com.electro.repository.UserRepository;
import com.electro.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @GetMapping
    public String settingsPage() {
        return "settings"; 
    }

    @PostMapping("/update-profile")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails currentUser,
                                 @RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam(required = false) String avatar,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {
        
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        // Validasi Username baru jika diubah
        if (!username.equals(user.getUsername())) {
            if (userRepository.existsByUsername(username)) {
                model.addAttribute("error", "Username sudah digunakan!");
                return "settings";
            }
            user.setUsername(username);
        }

        // Validasi Email baru 
        if (!email.equals(user.getEmail())) {
            if ("GOOGLE".equals(user.getProvider())) {
                model.addAttribute("error", "Email tidak dapat diubah untuk akun Google!");
                return "settings";
            }
            var existingUser = userRepository.findByUsernameOrEmail(email, email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                model.addAttribute("error", "Email sudah digunakan!");
                return "settings";
            }
            user.setEmail(email);
        }

        // Simpan avatar baru jika diunggah
        if (avatar != null && !avatar.trim().isEmpty()) {
            user.setAvatar(avatar);
        }
        userRepository.save(user);

        // Perbarui SecurityContext 
        CustomUserDetails newPrincipal = new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                currentUser.getAuthorities(),
                user.getEmail(),
                user.getAvatar(),
                user.getProvider()
        );
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                newPrincipal,
                auth.getCredentials(),
                auth.getAuthorities()
        );
        
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

        model.addAttribute("success", "Profil berhasil diperbarui!");
        return "settings";
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal CustomUserDetails currentUser,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 Model model) {
        
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        if ("GOOGLE".equals(user.getProvider())) {
            model.addAttribute("error", "Password tidak dapat diubah untuk akun Google!");
            return "settings";
        }

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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Transactional
    @PostMapping("/delete")
    public String deleteAccount(@AuthenticationPrincipal CustomUserDetails currentUser,
                                 @RequestParam(required = false) String password,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {
        
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        if (!"GOOGLE".equals(user.getProvider()) && !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Password konfirmasi salah!");
            return "settings";
        }

        // Hapus relasi FK dulu sebelum hapus user
        orderRepository.deleteByUser(user);
        complaintRepository.deleteByUser(user);

        userRepository.delete(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?deleted=true";
    }
}