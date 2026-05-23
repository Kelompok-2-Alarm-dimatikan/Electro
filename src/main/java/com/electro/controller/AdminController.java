package com.electro.controller;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired private ElectroService electroService;
    @Autowired private UserService    userService;
    @Autowired private PasswordEncoder passwordEncoder;

    AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("electronic", electroService.getAllElectro());
        return "admin";
    }

    /* STOK */
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

    /* PRODUK */
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

    /* UBAH PASSWORD */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {

        String username = auth.getName();
        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("pwError", "User tidak ditemukan.");
            return "redirect:/admin";
        }

        var user = userOpt.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("pwError", "Password lama tidak sesuai.");
            return "redirect:/admin";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("pwError", "Konfirmasi password tidak cocok.");
            return "redirect:/admin";
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("pwError", "Password baru minimal 6 karakter.");
            return "redirect:/admin";
        }

        userService.updatePassword(user, newPassword);
        redirectAttributes.addFlashAttribute("pwSuccess", "Password berhasil diubah.");
        return "redirect:/admin";
    }
}