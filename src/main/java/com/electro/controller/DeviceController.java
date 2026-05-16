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
        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        addStatistics(model, devices);
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String nama, Model model) {
        List<Device> devices = deviceService.searchDevices(nama);
        model.addAttribute("devices", devices);
        addStatistics(model, devices);
        return "index";
    }

    @GetMapping("/sort")
    public String sort(@RequestParam(required = false, defaultValue = "asc") String order, Model model) {
        List<Device> devices = "desc".equalsIgnoreCase(order)
                ? deviceService.sortByHargaDesc()
                : deviceService.sortByHargaAsc();
        model.addAttribute("devices", devices);
        addStatistics(model, devices);
        model.addAttribute("sortOrder", order);
        return "index";
    }

    private void addStatistics(Model model, List<Device> devices) {
        long totalHp = devices.stream().filter(d -> "Hp".equals(d.getKategori())).count();
        long totalLaptop = devices.stream().filter(d -> "Laptop".equals(d.getKategori())).count();
        long totalTablet = devices.stream().filter(d -> "Tablet".equals(d.getKategori())).count();

        model.addAttribute("countHp", totalHp);
        model.addAttribute("countLaptop", totalLaptop);
        model.addAttribute("countTablet", totalTablet);
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