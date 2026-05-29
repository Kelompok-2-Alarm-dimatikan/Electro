package com.electro.controller;
import org.springframework.security.core.Authentication;
import com.electro.service.ElectroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/electronic")
public class ElectroController {

    @Autowired
    private ElectroService electroService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "redirect:/admin";
        }
        model.addAttribute("electronic", electroService.getAllElectro());
        return "index";
    }

    /** Searching: Linear Search by nama produk */
    @GetMapping("/search")
    public String search(@RequestParam String nama, Model model) {
        model.addAttribute("electronic", electroService.searchByNama(nama));
        model.addAttribute("keyword", nama);
        return "index";
    }

    /** Sorting: Bubble Sort by harga ascending */
    @GetMapping("/sort")
    public String sort(Model model) {
        model.addAttribute("electronic", electroService.sortByHarga());
        model.addAttribute("sorted", true);
        return "index";
    }

    @PostMapping("/tambah")
    public String tambah(@RequestParam String kategori,
                         @RequestParam String nama,
                         @RequestParam double harga,
                         @RequestParam int stok,
                         @RequestParam String merk) {
        electroService.tambahElectro(kategori, nama, harga, stok, merk);
        return "redirect:/electronic";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
                       @RequestParam String nama,
                       @RequestParam double harga,
                       @RequestParam int stok,
                       @RequestParam String merk) {
        electroService.editElectro(id, nama, harga, stok, merk);
        return "redirect:/electronic";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        electroService.hapusElectro(id);
        return "redirect:/electronic";
    }
}
