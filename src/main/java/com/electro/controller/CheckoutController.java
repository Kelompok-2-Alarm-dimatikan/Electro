package com.electro.controller;

import com.electro.service.ElectroService;
import com.electro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private ElectroService electroService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String checkoutPage(Model model, Authentication auth) {
        if (auth != null) {
            userRepository.findByUsername(auth.getName()).ifPresent(user -> {
                model.addAttribute("currentUser", user);
            });
        }
        return "checkout";
    }

    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<?> submitCheckout(@RequestBody CheckoutRequest request) {
        try {
            // Validasi stok terlebih dahulu sebelum mengurangi agar konsisten
            for (CheckoutRequest.Item item : request.getItems()) {
                // Sederhana: kurangiStok sendiri memvalidasi stok < 0 dan melempar IllegalArgumentException
                electroService.kurangiStok(item.getId(), item.getQty());
            }
            return ResponseEntity.ok(Map.of("success", true, "message", "Checkout berhasil!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "Terjadi kesalahan pada server."));
        }
    }
}

class CheckoutRequest {
    private List<Item> items;
    private String address;
    private String paymentMethod;

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public static class Item {
        private Long id;
        private int qty;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
    }
}
