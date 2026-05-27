package com.electro.controller;
import com.electro.model.User;
import com.electro.repository.UserRepository;
import com.electro.repository.ComplaintRepository;
import com.electro.service.ElectroService;
import com.electro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired private ElectroService    electroService;
    @Autowired private UserService       userService;
    @Autowired private PasswordEncoder   passwordEncoder;
    @Autowired private ComplaintRepository complaintRepository;

    AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // HALAMAN UTAMA
    @GetMapping
    public String adminPage(Model model, Authentication auth) {
        model.addAttribute("devices",     electroService.getAllElectro());
        model.addAttribute("users",       userRepository.findAll());
        model.addAttribute("currentUser", userRepository.findByUsername(auth.getName()).orElse(null));
        long newUserCount = userRepository.findAll().stream()
                .filter(u -> !"ADMIN".equals(u.getRole())).count();
        model.addAttribute("newUserCount", newUserCount);
        model.addAttribute("complaints",   complaintRepository.findAllByOrderByTanggalDesc());
        return "admin";
    }

    // STOK
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

    // PRODUK CRUD
    @PostMapping("/tambah")
    public String tambah(@RequestParam String  kategori,
                         @RequestParam String  nama,
                         @RequestParam double  harga,
                         @RequestParam String  merk,
                         @RequestParam(defaultValue = "0")  int    stok,
                         @RequestParam(defaultValue = "")   String imageUrl,
                         @RequestParam(defaultValue = "")   String deskripsi,
                         @RequestParam(defaultValue = "")   String spesifikasi) {
        electroService.tambahElectroFull(kategori, nama, harga, stok, merk, imageUrl, deskripsi, spesifikasi);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long   id,
                       @RequestParam String nama,
                       @RequestParam double harga,
                       @RequestParam int    stok,
                       @RequestParam String merk,
                       @RequestParam(defaultValue = "") String imageUrl,
                       @RequestParam(defaultValue = "") String deskripsi,
                       @RequestParam(defaultValue = "") String spesifikasi) {
        electroService.editElectroFull(id, nama, harga, stok, merk, imageUrl, deskripsi, spesifikasi);
        return "redirect:/admin";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        electroService.hapusElectro(id);
        return "redirect:/admin";
    }

    // USER CRUD
    @PostMapping("/user/tambah")
    public String tambahUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(defaultValue = "USER") String role,
                             RedirectAttributes ra) {
        try {
            userService.register(username, email, password);
            if ("ADMIN".equals(role)) {
                userRepository.findByUsername(username).ifPresent(u -> {
                    u.setRole("ADMIN");
                    userRepository.save(u);
                });
            }
            ra.addFlashAttribute("userSuccess", "User berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("userError", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/user/edit")
    public String editUser(@RequestParam Long   id,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String role,
                           RedirectAttributes ra) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) { ra.addFlashAttribute("userError", "User tidak ditemukan."); return "redirect:/admin"; }
        User u = opt.get();
        u.setUsername(username);
        u.setEmail(email);
        u.setRole(role);
        userRepository.save(u);
        ra.addFlashAttribute("userSuccess", "User berhasil diperbarui.");
        return "redirect:/admin";
    }

    @GetMapping("/user/hapus/{id}")
    public String hapusUser(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        userRepository.findById(id).ifPresent(u -> {
            if (!u.getUsername().equals(auth.getName())) {
                userRepository.deleteById(id);
            }
        });
        return "redirect:/admin";
    }

    // UBAH PASSWORD
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        var userOpt = userRepository.findByUsername(auth.getName());
        if (userOpt.isEmpty()) { ra.addFlashAttribute("pwError", "User tidak ditemukan."); return "redirect:/admin"; }
        var user = userOpt.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) { ra.addFlashAttribute("pwError", "Password lama tidak sesuai."); return "redirect:/admin"; }
        if (!newPassword.equals(confirmPassword)) { ra.addFlashAttribute("pwError", "Konfirmasi password tidak cocok."); return "redirect:/admin"; }
        if (newPassword.length() < 6) { ra.addFlashAttribute("pwError", "Password baru minimal 6 karakter."); return "redirect:/admin"; }
        userService.updatePassword(user, newPassword);
        ra.addFlashAttribute("pwSuccess", "Password berhasil diubah.");
        return "redirect:/admin";
    }

    // PENGADUAN ACTIONS
    @PostMapping("/complaints/update-status")
    public String updateComplaintStatus(@RequestParam Long id, @RequestParam String status, RedirectAttributes ra) {
        complaintRepository.findById(id).ifPresent(c -> {
            c.setStatus(status);
            complaintRepository.save(c);
            ra.addFlashAttribute("userSuccess", "Status pengaduan berhasil diperbarui.");
        });
        return "redirect:/admin";
    }

    @GetMapping("/complaints/delete/{id}")
    public String deleteComplaint(@PathVariable Long id, RedirectAttributes ra) {
        complaintRepository.deleteById(id);
        ra.addFlashAttribute("userSuccess", "Pengaduan berhasil dihapus.");
        return "redirect:/admin";
    }
}