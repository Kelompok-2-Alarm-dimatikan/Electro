package com.electro.controller;
import com.electro.model.Electronic;
import com.electro.service.ElectroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    @Autowired
    private ElectroService electroService;

    /**
     * Endpoint AJAX untuk search + filter kategori + sort.
     * Backend Java tetap melakukan:
     *   - Linear Search  → ElectroService.searchByNamaAndKategori()
     *   - Bubble Sort    → ElectroService.sortByHargaAsc() / sortByHargaDesc()
     *
     * GET /api/product?nama=samsung&kategori=Hp&sort=low-high
     * → returns JSON array of products
     */
    @GetMapping
    public List<Map<String, Object>> searchProducts(
            @RequestParam(name = "nama",     required = false, defaultValue = "") String nama,
            @RequestParam(name = "sort",     required = false, defaultValue = "default") String sort,
            @RequestParam(name = "kategori", required = false, defaultValue = "all") String kategori) {

        // Linear Search 
        List<Electronic> hasil = electroService.searchByNamaAndKategori(
                nama.isBlank() ? null : nama,
                kategori);

        // Bubble Sort 
        if ("low-high".equals(sort)) {
            hasil = electroService.sortByHargaAsc(hasil);
        } else if ("high-low".equals(sort)) {
            hasil = electroService.sortByHargaDesc(hasil);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Electronic e : hasil) {
            Map<String, Object> item = new HashMap<>();
            item.put("id",          e.getId());
            item.put("nama",        e.getNama()        != null ? e.getNama()        : "");
            item.put("merk",        e.getMerk()        != null ? e.getMerk()        : "");
            item.put("kategori",    e.getKategori()    != null ? e.getKategori()    : "");
            item.put("harga",       e.getHarga());
            item.put("stok",        e.getStok());
            item.put("imageUrl",    e.getImageUrl()    != null ? e.getImageUrl()    : "");
            item.put("deskripsi",   e.getDeskripsi()   != null ? e.getDeskripsi()   : "");
            item.put("spesifikasi", e.getSpesifikasi() != null ? e.getSpesifikasi() : "");
            response.add(item);
        }
        return response;
    }
}