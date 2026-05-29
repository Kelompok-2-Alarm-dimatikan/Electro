package com.electro.controller;
import com.electro.model.User;
import com.electro.repository.UserRepository;
import com.electro.repository.ComplaintRepository;
import com.electro.service.ElectroService;
import com.electro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.electro.repository.OrderRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired
    private ElectroService electroService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private OrderRepository orderRepository;

    AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // HALAMAN UTAMA
    @GetMapping
    public String adminPage(Model model, Authentication auth) {
        var devices = electroService.getAllElectro();
        var users = userRepository.findAll();
        var complaints = complaintRepository.findAllByOrderByTanggalDesc();
        var orders = orderRepository.findAllByOrderByTanggalDesc();

        model.addAttribute("devices", devices);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", userRepository.findByUsername(auth.getName()).orElse(null));

        long newUserCount = users.stream().filter(u -> !"ADMIN".equals(u.getRole())).count();
        model.addAttribute("newUserCount", newUserCount);

        model.addAttribute("complaints", complaints);
        model.addAttribute("orders", orders);

        long pendingOrderCount = orders.stream().filter(o -> "MENUNGGU".equalsIgnoreCase(o.getStatus())).count();
        model.addAttribute("pendingOrderCount", pendingOrderCount);

        long pendingComplaintCount = complaints.stream().filter(c -> "PENDING".equalsIgnoreCase(c.getStatus())).count();
        model.addAttribute("pendingComplaintCount", pendingComplaintCount);

        // Dashboard Stats
        double totalPenjualan = orders.stream().mapToDouble(o -> o.getTotalHarga() != null ? o.getTotalHarga() : 0.0)
                .sum();
        long totalOrder = orders.size();
        int produkTerjual = orders.stream().mapToInt(o -> o.getTotalBarang() != null ? o.getTotalBarang() : 0).sum();
        int stokTersisa = devices.stream().mapToInt(d -> d.getStok()).sum();

        model.addAttribute("totalPenjualan", totalPenjualan);
        model.addAttribute("totalOrder", totalOrder);
        model.addAttribute("produkTerjual", produkTerjual);
        model.addAttribute("stokTersisa", stokTersisa);

        // Stok Per Kategori
        Map<String, String> katColors = Map.of(
            "Hp", "#4f8ef7", "Laptop", "#f7c44f", "Tablet", "#4fc78a",
            "Blender", "#f7654f", "Kulkas", "#a078ff", "Ac", "#4fdcdc",
            "Tv", "#f7a04f", "Headphone", "#4fc78a"
        );
        List<Map<String, Object>> stockData = devices.stream()
            .collect(Collectors.groupingBy(d -> d.getKategori(), Collectors.summingInt(d -> d.getStok())))
            .entrySet().stream()
            .map(e -> Map.<String, Object>of(
                "label", e.getKey().equalsIgnoreCase("Hp") ? "HP" : (e.getKey().equalsIgnoreCase("Ac") ? "AC" : e.getKey().equalsIgnoreCase("Tv") ? "TV" : e.getKey()),
                "count", e.getValue(),
                "color", katColors.getOrDefault(e.getKey(), "#ffffff")
            )).collect(Collectors.toList());

        // Popular Products
        List<Map<String, Object>> popularProducts = devices.stream()
            .sorted((a, b) -> Integer.compare(b.getTerjual(), a.getTerjual()))
            .limit(5)
            .map(d -> Map.<String, Object>of(
                "nama", d.getNama(),
                "kat", d.getKategori(),
                "terjual", d.getTerjual(),
                "harga", d.getHarga(),
                "stok", d.getStok()
            )).collect(Collectors.toList());

        // Monthly Chart Data per Kategori (Jan-Des)
        // Warna per kategori
        Map<String, String> katChartColors = Map.of(
            "Hp", "rgba(79,142,247,1)", "Laptop", "rgba(247,196,79,1)",
            "Tablet", "rgba(79,199,138,1)", "Blender", "rgba(247,101,79,1)",
            "Kulkas", "rgba(160,120,255,1)", "Ac", "rgba(79,220,220,1)",
            "Tv", "rgba(247,160,79,1)", "Headphone", "rgba(79,199,138,1)"
        );
        Map<String, String> katChartBg = Map.of(
            "Hp", "rgba(79,142,247,0.08)", "Laptop", "rgba(247,196,79,0.08)",
            "Tablet", "rgba(79,199,138,0.08)", "Blender", "rgba(247,101,79,0.08)",
            "Kulkas", "rgba(160,120,255,0.08)", "Ac", "rgba(79,220,220,0.08)",
            "Tv", "rgba(247,160,79,0.08)", "Headphone", "rgba(79,199,138,0.08)"
        );
        // Kelompokkan orders per bulan per kategori
        // Karena Order tidak punya kategori langsung, kita pakai totalHarga per bulan per kategori dari produk
        // Fallback: bagi totalHarga merata ke semua kategori yang ada di devices
        // Build monthly revenue per kategori dari products (terjual * harga / 12 bulan distribusi)
        java.util.LinkedHashMap<String, java.util.LinkedHashMap<Integer, Double>> katMonthlyRevenue = new java.util.LinkedHashMap<>();
        // Inisialisasi semua kategori dengan 0 per bulan
        List<String> allKats = devices.stream().map(d -> d.getKategori()).distinct().collect(Collectors.toList());
        for (String kat : allKats) {
            java.util.LinkedHashMap<Integer, Double> months = new java.util.LinkedHashMap<>();
            for (int m = 1; m <= 12; m++) months.put(m, 0.0);
            katMonthlyRevenue.put(kat, months);
        }
        // Isi dari orders berdasarkan bulan; bagi rata ke kategori berdasarkan proporsi stok terjual
        // Untuk data real: order tidak simpan kategori, jadi distribusikan berdasarkan proporsi terjual per kategori
        double totalTerjual = devices.stream().mapToDouble(d -> d.getTerjual()).sum();
        Map<String, Double> katProportion = devices.stream()
            .collect(Collectors.groupingBy(d -> d.getKategori(), Collectors.summingDouble(d -> d.getTerjual())));
        for (var o : orders) {
            if (o.getTanggal() == null || o.getTotalHarga() == null) continue;
            int bulan = o.getTanggal().getMonthValue();
            double harga = o.getTotalHarga();
            double tot = totalTerjual > 0 ? totalTerjual : 1.0;
            for (String kat : allKats) {
                double prop = katProportion.getOrDefault(kat, 0.0) / tot;
                katMonthlyRevenue.get(kat).merge(bulan, harga * prop / 1_000_000.0, Double::sum);
            }
        }
        // Build datasets untuk Chart.js
        List<Map<String, Object>> chartDatasets = new java.util.ArrayList<>();
        for (String kat : allKats) {
            java.util.LinkedHashMap<Integer, Double> months = katMonthlyRevenue.get(kat);
            List<Double> dataArr = new java.util.ArrayList<>();
            for (int m = 1; m <= 12; m++) {
                double val = months.getOrDefault(m, 0.0);
                dataArr.add(Math.round(val * 10.0) / 10.0);
            }
            String displayLabel = kat.equalsIgnoreCase("Hp") ? "HP" : kat.equalsIgnoreCase("Ac") ? "AC" : kat.equalsIgnoreCase("Tv") ? "TV" : kat;
            chartDatasets.add(Map.<String, Object>of(
                "label", displayLabel,
                "data", dataArr,
                "borderColor", katChartColors.getOrDefault(kat, "rgba(255,255,255,1)"),
                "backgroundColor", katChartBg.getOrDefault(kat, "rgba(255,255,255,0.08)"),
                "fill", true, "tension", 0.4, "borderWidth", 2, "pointRadius", 4,
                "pointHoverRadius", 6,
                "pointBackgroundColor", katChartColors.getOrDefault(kat, "rgba(255,255,255,1)")
            ));
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            model.addAttribute("stockDataJson", mapper.writeValueAsString(stockData));
            model.addAttribute("popularProductsJson", mapper.writeValueAsString(popularProducts));
            model.addAttribute("monthlyChartJson", mapper.writeValueAsString(chartDatasets));
        } catch (Exception e) {
            model.addAttribute("stockDataJson", "[]");
            model.addAttribute("popularProductsJson", "[]");
            model.addAttribute("monthlyChartJson", "[]");
        }

        return "admin";
    }

    // STOK
    @PostMapping("/tambahStok")
    public String tambahStok(@RequestParam Long id, @RequestParam int jumlah) {
        electroService.tambahStok(id, jumlah);
        return "redirect:/admin";
    }

    @PostMapping("/kurangiStok")
    public String kurangiStok(@RequestParam Long id, @RequestParam int jumlah) {
        electroService.kurangiStok(id, jumlah);
        return "redirect:/admin";
    }

    // PRODUK CRUD
    @PostMapping("/tambah")
    public String tambah(@RequestParam String kategori,
            @RequestParam String nama,
            @RequestParam double harga,
            @RequestParam String merk,
            @RequestParam(defaultValue = "0") int stok,
            @RequestParam(defaultValue = "") String imageUrl,
            @RequestParam(defaultValue = "") String deskripsi,
            @RequestParam(defaultValue = "") String spesifikasi) {
        electroService.tambahElectroFull(kategori, nama, harga, stok, merk, imageUrl, deskripsi, spesifikasi);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam Long id,
            @RequestParam String nama,
            @RequestParam double harga,
            @RequestParam int stok,
            @RequestParam String merk,
            @RequestParam(defaultValue = "") String imageUrl,
            @RequestParam(defaultValue = "") String deskripsi,
            @RequestParam(defaultValue = "") String spesifikasi) {
        electroService.editElectroFull(id, nama, harga, stok, merk, imageUrl, deskripsi, spesifikasi);
        return "redirect:/admin";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id) {
        electroService.hapusElectro(id);
        return "redirect:/admin";
    }

    // USER CRUD
    @PostMapping("/user/tambah")
    public String tambahUser(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "USER") String role,
            RedirectAttributes ra) {
        try {
            if (username == null || username.isBlank()) throw new IllegalArgumentException("Username tidak boleh kosong.");
            if (email == null || email.isBlank()) throw new IllegalArgumentException("Email tidak boleh kosong.");
            if (password == null || password.isBlank()) throw new IllegalArgumentException("Password tidak boleh kosong.");
            if (password.length() < 6) throw new IllegalArgumentException("Password minimal 6 karakter.");
            userService.register(username, email, password);
            if ("ADMIN".equals(role)) {
                userRepository.findByUsername(username).ifPresent(u -> {
                    u.setRole("ADMIN");
                    userRepository.save(u);
                });
            }
            ra.addFlashAttribute("userSuccess", "User berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("userError", e.getMessage() != null ? e.getMessage() : "Gagal menambahkan user.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/user/edit")
    public String editUser(@RequestParam Long id,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String role,
            RedirectAttributes ra) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("userError", "User tidak ditemukan.");
            return "redirect:/admin";
        }
        User u = opt.get();
        u.setUsername(username);
        u.setEmail(email);
        u.setRole(role);
        userRepository.save(u);
        ra.addFlashAttribute("userSuccess", "User berhasil diperbarui.");
        return "redirect:/admin";
    }

    // FIX: Hapus user dengan nullify FK di orders & complaints terlebih dahulu
    @Transactional
    @GetMapping("/user/hapus/{id}")
    public String hapusUser(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        userRepository.findById(id).ifPresent(u -> {
            if (!u.getUsername().equals(auth.getName())) {
                // Nullify user reference in orders (FK constraint fix)
                orderRepository.findByUser(u).forEach(o -> {
                    o.setUser(null);
                    orderRepository.save(o);
                });
                // Delete complaints that belong to this user
                complaintRepository.deleteByUser(u);
                // Now safe to delete user
                userRepository.delete(u);
                ra.addFlashAttribute("userSuccess", "User berhasil dihapus.");
            }
        });
        return "redirect:/admin";
    }

    // UBAH PASSWORD
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Authentication auth,
            RedirectAttributes ra) {
        var userOpt = userRepository.findByUsername(auth.getName());
        if (userOpt.isEmpty()) {
            ra.addFlashAttribute("pwError", "User tidak ditemukan.");
            return "redirect:/admin";
        }
        var user = userOpt.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            ra.addFlashAttribute("pwError", "Password lama tidak sesuai.");
            return "redirect:/admin";
        }
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("pwError", "Konfirmasi password tidak cocok.");
            return "redirect:/admin";
        }
        if (newPassword.length() < 6) {
            ra.addFlashAttribute("pwError", "Password baru minimal 6 karakter.");
            return "redirect:/admin";
        }
        userService.updatePassword(user, newPassword);
        ra.addFlashAttribute("pwSuccess", "Password berhasil diubah.");
        return "redirect:/admin";
    }

    // PENGADUAN ACTIONS
    @PostMapping("/complaints/update-status")
    public String updateComplaintStatus(@RequestParam Long id, @RequestParam String status, RedirectAttributes ra) {
        complaintRepository.findById(id).ifPresent(c -> {
            c.setStatus(status);
            complaintRepository.save(c);
            ra.addFlashAttribute("userSuccess", "Status pengaduan berhasil diperbarui.");
        });
        return "redirect:/admin";
    }

    @GetMapping("/complaints/delete/{id}")
    public String deleteComplaint(@PathVariable Long id, RedirectAttributes ra) {
        complaintRepository.deleteById(id);
        ra.addFlashAttribute("userSuccess", "Pengaduan berhasil dihapus.");
        return "redirect:/admin";
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam Long id,
            @RequestParam String status,
            RedirectAttributes ra) {
        orderRepository.findById(id).ifPresent(o -> {
            o.setStatus(status);
            orderRepository.save(o);
            ra.addFlashAttribute("userSuccess", "Status pengiriman berhasil diperbarui.");
        });
        return "redirect:/admin";
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes ra) {
        orderRepository.deleteById(id);
        ra.addFlashAttribute("userSuccess", "Data pesanan berhasil dihapus.");
        return "redirect:/admin";
    }
}