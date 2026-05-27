package com.electro.controller;

import com.electro.model.Complaint;
import com.electro.model.Electronic;
import com.electro.model.User;
import com.electro.repository.ComplaintRepository;
import com.electro.repository.ElectroRepository;
import com.electro.repository.UserRepository;
import com.electro.service.ElectroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/laporan")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElectroRepository electroRepository;

    @Autowired
    private ElectroService electroService;

    @GetMapping
    public String laporanPage(Model model, Authentication auth) {
        if (auth != null) {
            userRepository.findByUsername(auth.getName()).ifPresent(user -> {
                model.addAttribute("currentUser", user);
                model.addAttribute("complaints", complaintRepository.findByUserOrderByTanggalDesc(user));
            });
        }
        model.addAttribute("electronics", electroService.getAllElectro());
        return "laporan";
    }

    @PostMapping("/submit")
    public String submitComplaint(
            @RequestParam String kategori,
            @RequestParam String judul,
            @RequestParam String deskripsi,
            @RequestParam(required = false) Long productId,
            Authentication auth,
            RedirectAttributes ra) {
        if (auth == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        Electronic electronic = null;
        if (productId != null && productId > 0) {
            electronic = electroRepository.findById(productId).orElse(null);
        }
        Complaint complaint = new Complaint(user, electronic, kategori, judul, deskripsi);
        complaintRepository.save(complaint);
        ra.addFlashAttribute("success", "Pengaduan berhasil diajukan dan sedang menunggu tanggapan.");
        return "redirect:/laporan";
    }
}
