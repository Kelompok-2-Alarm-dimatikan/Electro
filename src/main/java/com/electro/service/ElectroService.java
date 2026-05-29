package com.electro.service;
import com.electro.model.*;
import com.electro.repository.ElectroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElectroService {

    @Autowired
    private ElectroRepository electroRepository;

    public List<Electronic> getAllElectro() {
        return electroRepository.findAll();
    }

    public java.util.Optional<Electronic> getElectroById(Long id) {
        return electroRepository.findById(id);
    }

    /**
     * Searching: Linear Search — cari berdasarkan nama, merk, kategori.
     * Mendukung multi-kata (AND) + optional filter kategori spesifik.
     */
    public List<Electronic> searchByNama(String keyword) {
        return searchByNamaAndKategori(keyword, null);
    }

    public List<Electronic> searchByNamaAndKategori(String keyword, String kategori) {
        List<Electronic> semua = electroRepository.findAll();
        List<Electronic> hasil = new ArrayList<>();
        String[] kata = (keyword == null || keyword.isBlank())
                ? new String[]{}
                : keyword.toLowerCase().trim().split("\\s+");

        for (Electronic e : semua) {
            // Filter kategori dulu jika dipilih
            if (kategori != null && !kategori.isBlank() && !kategori.equals("all")) {
                if (!kategori.equals(e.getKategori())) continue;
            }

            // Jika tidak ada keyword, langsung masuk (hanya filter kategori)
            if (kata.length == 0) {
                hasil.add(e);
                continue;
            }

            String nama     = e.getNama()     != null ? e.getNama().toLowerCase()     : "";
            String merk     = e.getMerk()     != null ? e.getMerk().toLowerCase()     : "";
            String kat      = e.getKategori() != null ? e.getKategori().toLowerCase() : "";
            String alias    = kat.equals("hp") ? "smartphone handphone" : "";
            String haystack = nama + " " + merk + " " + kat + " " + alias;

            // Semua kata harus ada (AND)
            boolean cocok = true;
            for (String k : kata) {
                if (!haystack.contains(k)) { cocok = false; break; }
            }
            if (cocok) hasil.add(e);
        }
        return hasil;
    }

    /**
     * Sorting: Bubble Sort manual — harga kecil ke besar.
     */
    public List<Electronic> sortByHargaAsc(List<Electronic> source) {
        List<Electronic> list = new ArrayList<>(source);
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getHarga() > list.get(j + 1).getHarga()) {
                    Electronic temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }

    /**
     * Sorting: Bubble Sort manual — harga besar ke kecil.
     */
    public List<Electronic> sortByHargaDesc(List<Electronic> source) {
        List<Electronic> list = new ArrayList<>(source);
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getHarga() < list.get(j + 1).getHarga()) {
                    Electronic temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }

    /** @deprecated Gunakan sortByHargaAsc(list) */
    public List<Electronic> sortByHarga() {
        return sortByHargaAsc(electroRepository.findAll());
    }

    public void tambahStok(Long id, int jumlah) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        electro.setStok(electro.getStok() + jumlah);
        electroRepository.save(electro);
    }

    public void kurangiStok(Long id, int jumlah) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        int stokBaru = electro.getStok() - jumlah;
        if (stokBaru < 0) throw new IllegalArgumentException("Stok tidak mencukupi");
        electro.setStok(stokBaru);
        electro.setTerjual(electro.getTerjual() + jumlah);
        electroRepository.save(electro);
    }

    public void tambahElectro(String kategori, String nama, double harga, int stok, String merk) {
        Electronic electro;
        switch (kategori) {
            case "Hp"        -> electro = new Hp(nama, harga, stok, merk);
            case "Laptop"    -> electro = new Laptop(nama, harga, stok, merk);
            case "Tablet"    -> electro = new Tablet(nama, harga, stok, merk);
            case "Tv"        -> electro = new Tv(nama, harga, stok, merk);
            case "Ac"        -> electro = new Ac(nama, harga, stok, merk);
            case "Blender"   -> electro = new Blender(nama, harga, stok, merk);
            case "Kulkas"    -> electro = new Kulkas(nama, harga, stok, merk);
            case "Headphone" -> electro = new Headphone(nama, harga, stok, merk);
            default          -> electro = new Hp(nama, harga, stok, merk);
        }
        electroRepository.save(electro);
    }

    public void tambahElectroFull(String kategori, String nama, double harga, int stok, String merk,
                                  String imageUrl, String deskripsi, String spesifikasi) {
        Electronic electro;
        switch (kategori) {
            case "Hp"        -> electro = new Hp(nama, harga, stok, merk);
            case "Laptop"    -> electro = new Laptop(nama, harga, stok, merk);
            case "Tablet"    -> electro = new Tablet(nama, harga, stok, merk);
            case "Tv"        -> electro = new Tv(nama, harga, stok, merk);
            case "Ac"        -> electro = new Ac(nama, harga, stok, merk);
            case "Blender"   -> electro = new Blender(nama, harga, stok, merk);
            case "Kulkas"    -> electro = new Kulkas(nama, harga, stok, merk);
            case "Headphone" -> electro = new Headphone(nama, harga, stok, merk);
            default          -> electro = new Hp(nama, harga, stok, merk);
        }
        if (imageUrl    != null && !imageUrl.isBlank())    electro.setImageUrl(imageUrl);
        if (deskripsi   != null && !deskripsi.isBlank())   electro.setDeskripsi(deskripsi);
        if (spesifikasi != null && !spesifikasi.isBlank()) electro.setSpesifikasi(spesifikasi);
        electroRepository.save(electro);
    }

    public void editElectroFull(Long id, String nama, double harga, int stok, String merk,
                                String imageUrl, String deskripsi, String spesifikasi) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        electro.setNama(nama);
        electro.setHarga(harga);
        electro.setStok(stok);
        electro.setMerk(merk);
        electro.setImageUrl(imageUrl != null && !imageUrl.isBlank() ? imageUrl : null);
        electro.setDeskripsi(deskripsi != null && !deskripsi.isBlank() ? deskripsi : null);
        electro.setSpesifikasi(spesifikasi != null && !spesifikasi.isBlank() ? spesifikasi : null);
        electroRepository.save(electro);
    }

    public void editElectro(Long id, String nama, double harga, int stok, String merk) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        electro.setNama(nama);
        electro.setHarga(harga);
        electro.setStok(stok);
        electro.setMerk(merk);
        electroRepository.save(electro);
    }

    public void hapusElectro(Long id) {
        electroRepository.deleteById(id);
    }
}