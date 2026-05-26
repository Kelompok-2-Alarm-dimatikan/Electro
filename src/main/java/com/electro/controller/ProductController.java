package com.electro.controller;
import com.electro.service.ElectroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ElectroService electroService;

    @GetMapping
    public String product(Model model, Authentication authentication) {
        model.addAttribute("electronic", electroService.getAllElectro());
        return "product"; // → templates/product.html
    }
}