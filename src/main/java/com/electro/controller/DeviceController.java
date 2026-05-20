package com.electro.controller;

import org.springframework.security.core.Authentication;
import com.electro.model.Device;
import com.electro.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        if(authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin";
        }
        model.addAttribute("devices", deviceService.getAllDevices());
        return "index"; // → templates/index.html
    }

    @GetMapping("/search")
    public String search(@RequestParam double harga, Model model) {
        model.addAttribute("devices", deviceService.searchByHarga(harga));
        return "index";
    }

    @GetMapping("/sort")
    public String sort(Model model) {
        model.addAttribute("devices", deviceService.sortByHarga());
        return "index";
    }

    @PostMapping("/tambah")
    public String tambah(@RequestParam String kategori,
                         @RequestParam String nama,
                         @RequestParam double harga,
                         @RequestParam int stok,
                         @RequestParam String merk) {
        deviceService.tambahDevice(kategori, nama, harga, stok, merk);
        return "redirect:/devices";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
                       @RequestParam String nama,
                       @RequestParam double harga,
                    @RequestParam int stok,
                       @RequestParam String merk) {
        deviceService.editDevice(id, nama, harga, stok, merk);
        return "redirect:/devices";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        deviceService.hapusDevice(id);
        return "redirect:/devices";
    }
}