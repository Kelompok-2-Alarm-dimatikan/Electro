package com.electro.controller;

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
    public String index(Model model) {
        model.addAttribute("devices", deviceService.getAllDevices());
        return "index";
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
                         @RequestParam double harga) {
        deviceService.tambahDevice(kategori, nama, harga);
        return "redirect:/devices";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
                       @RequestParam String nama,
                       @RequestParam double harga) {
        deviceService.editDevice(id, nama, harga);
        return "redirect:/devices";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        deviceService.hapusDevice(id);
        return "redirect:/devices";
    }
}