package com.electro.controller;

import com.electro.service.ElectroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ElectroService electroService;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("electronic", electroService.getAllElectro());
        return "admin";
    }

    @PostMapping("/tambah")
    public String tambah(@RequestParam String kategori,
                         @RequestParam String nama,
                         @RequestParam double harga) {
        electroService.tambahElectro(kategori, nama, harga);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
                       @RequestParam String nama,
                       @RequestParam double harga) {
        electroService.editElectro(id, nama, harga);
        return "redirect:/admin";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        electroService.hapusElectro(id);
        return "redirect:/admin";
    }
}
