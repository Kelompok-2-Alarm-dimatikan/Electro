package com.electro.controller;

import com.electro.repository.UserRepository;
import com.electro.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final UserRepository userRepository;
    @Autowired
    private DeviceService deviceService;

    AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("devices", deviceService.getAllDevices());
        model.addAttribute("user", userRepository.findAll());
        return "admin"; //templates/admin.html
    }

    @PostMapping("/tambahStok")
    public String tambahStok(@RequestParam Long id,
                            @RequestParam int jumlah) {
        deviceService.tambahStok(id, jumlah);
        return "redirect:/admin";
    }

    @PostMapping("/kurangiStok")
    public String kurangiStok(@RequestParam Long id,
                            @RequestParam int jumlah) {
        deviceService.kurangiStok(id, jumlah);
        return "redirect:/admin";
    }

    @PostMapping("/tambah")
    public String tambah(@RequestParam String kategori,
                         @RequestParam String nama,
                         @RequestParam double harga,
                         @RequestParam String merk,
                         @RequestParam (defaultValue="0")int stok) {
        deviceService.tambahDevice(kategori, nama, harga, stok, merk);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
                       @RequestParam String nama,
                       @RequestParam double harga,
                       @RequestParam int stok,
                       @RequestParam String merk) {
        deviceService.editDevice(id, nama, harga, stok, merk);
        return "redirect:/admin";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        deviceService.hapusDevice(id);
        return "redirect:/admin";
    }
}
