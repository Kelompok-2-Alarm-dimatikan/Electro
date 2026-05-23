package com.electro.controller;

import com.electro.model.User;
import com.electro.repository.UserRepository;
import com.electro.service.ElectroService;
import com.electro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired
    private ElectroService electroService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminPage(Model model, Authentication auth) {
        model.addAttribute("electronic", electroService.getAllElectro());

        // Menampilkan data user di panel profil
        if (auth != null) {
            User currentUser = userRepository.findByUsername(auth.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }

        // Notifikasi role user
        List<User> allUsers = (List<User>) userRepository.findAll();
        long newUserCount = allUsers.stream()
                .filter(u -> "USER".equals(u.getRole()))
                .count();
        model.addAttribute("newUserCount", newUserCount);
        model.addAttribute("users", allUsers);
        model.addAttribute("devices", electroService.getAllElectro());

        return "admin";
    }

    // Ganti Password 
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        if (auth == null) return "redirect:/login";

        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null) return "redirect:/login";

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            ra.addFlashAttribute("pwError", "Password lama tidak cocok!");
            return "redirect:/admin";
        }
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("pwError", "Konfirmasi password tidak cocok!");
            return "redirect:/admin";
        }
        if (newPassword.length() < 6) {
            ra.addFlashAttribute("pwError", "Password minimal 6 karakter!");
            return "redirect:/admin";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        ra.addFlashAttribute("pwSuccess", "Password berhasil diubah!");
        return "redirect:/admin";
    }

    // Stok & Produk 
    @PostMapping("/tambahStok")
    public String tambahStok(@RequestParam Long id, @RequestParam int jumlah) {
        electroService.tambahStok(id, jumlah);
        return "redirect:/admin";
    }

    @PostMapping("/kurangiStok")
    public String kurangiStok(@RequestParam Long id, @RequestParam int jumlah) {
        electroService.kurangiStok(id, jumlah);
        return "redirect:/admin";
    }

    @PostMapping("/tambah")
    public String tambah(@RequestParam String kategori,
                         @RequestParam String nama,
                         @RequestParam double harga,
                         @RequestParam String merk,
                         @RequestParam(defaultValue = "0") int stok) {
        electroService.tambahElectro(kategori, nama, harga, stok, merk);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
                       @RequestParam String nama,
                       @RequestParam double harga,
                       @RequestParam int stok,
                       @RequestParam String merk) {
        electroService.editElectro(id, nama, harga, stok, merk);
        return "redirect:/admin";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        electroService.hapusElectro(id);
        return "redirect:/admin";
    }
}