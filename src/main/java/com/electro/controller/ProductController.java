package com.electro.controller;

import com.electro.service.ElectroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ElectroService electroService;

    @GetMapping
    public String product(
            @RequestParam(name = "nama",     required = false, defaultValue = "") String nama,
            @RequestParam(name = "sort",     required = false, defaultValue = "default") String sort,
            @RequestParam(name = "kategori", required = false, defaultValue = "all") String kategori,
            Model model,
            Authentication authentication) {

        // Linear Search 
        var hasil = electroService.searchByNamaAndKategori(
                nama.isBlank() ? null : nama,
                kategori);

        // Bubble Sort 
        if ("low-high".equals(sort)) {
            hasil = electroService.sortByHargaAsc(hasil);
        } else if ("high-low".equals(sort)) {
            hasil = electroService.sortByHargaDesc(hasil);
        }

        model.addAttribute("electronic", hasil);
        model.addAttribute("searchNama", nama);
        model.addAttribute("sortValue",  sort);
        model.addAttribute("activeKat",  kategori);
        return "product";
    }
}