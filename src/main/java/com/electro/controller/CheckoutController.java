package com.electro.controller;

import com.electro.model.Order;
import com.electro.repository.OrderRepository;
import com.electro.repository.UserRepository;
import com.electro.service.ElectroService;
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

    @Autowired
    private OrderRepository orderRepository;

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
    public ResponseEntity<?> submitCheckout(@RequestBody CheckoutRequest request, Authentication auth) { 
        try {
            // Validasi & kurangi stok
            for (CheckoutRequest.Item item : request.getItems()) {
                electroService.kurangiStok(item.getId(), item.getQty());
            }

            // SIMPAN ORDER BARU
            Order order = new Order();
            order.setNamaUser(request.getFullName());
            order.setEmailUser(request.getEmail());
            order.setNomerUser(request.getPhone());
            order.setAlamatUser(request.getAddress());
            order.setViaPembayaran(request.getPaymentMethod());

            // Link ke user jika login
            if (auth != null) {
                userRepository.findByUsername(auth.getName())
                    .ifPresent(order::setUser);
            }

            orderRepository.save(order);
            // ← END SIMPAN ORDER

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
    private String fullName;   
    private String email;      
    private String phone;      

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public static class Item {
        private Long id;
        private int qty;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
    }
}