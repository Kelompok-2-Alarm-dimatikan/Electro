package com.electro.controller;

import com.electro.repository.UserRepository;
import com.electro.service.ElectroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    @Autowired
    private ElectroService electroService;

    AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("electronic", electroService.getAllElectro());
        return "admin"; // → templates/admin.html
    }

    @PostMapping("/tambahStok")
    public String tambahStok(@RequestParam Long id,
                             @RequestParam int jumlah) {
        electroService.tambahStok(id, jumlah);
        return "redirect:/admin";
    }

    @PostMapping("/kurangiStok")
    public String kurangiStok(@RequestParam Long id,
                              @RequestParam int jumlah) {
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
