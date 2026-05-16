package com.electro.controller;

import com.electro.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("devices", deviceService.getAllDevices());
        return "admin"; // → templates/admin.html
    }

    @PostMapping("/tambah")
    public String tambah(@RequestParam String kategori,
                         @RequestParam String nama,
                         @RequestParam double harga) {
        deviceService.tambahDevice(kategori, nama, harga);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
                       @RequestParam String nama,
                       @RequestParam double harga) {
        deviceService.editDevice(id, nama, harga);
        return "redirect:/admin";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        deviceService.hapusDevice(id);
        return "redirect:/admin";
    }
}
