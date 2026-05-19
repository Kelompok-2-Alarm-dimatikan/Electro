package com.electro.model;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class blender {

    // ── Enum Merk ─────────────────────────────────────────────────────────────
    public enum MerkBlender {
        PHILIPS("Philips"),
        SHARP("Sharp"),
        MIYAKO("Miyako"),
        OXONE("Oxone"),
        COSMOS("Cosmos"),
        PANASONIC("Panasonic"),
        NATIONAL("National"),
        TEFAL("Tefal"),
        KENWOOD("Kenwood"),
        VITAMIX("Vitamix");

        private final String label;
        MerkBlender(String label) { this.label = label; }
        public String getLabel()  { return label; }

        @Override
        public String toString()  { return label; }
    }

    // ── Field ─────────────────────────────────────────────────────────────────
    private Long id;
    private String namaProduk;
    private MerkBlender merk;
    private BigDecimal harga;

    public blender(Long id, String namaProduk, MerkBlender merk, BigDecimal harga) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.merk = merk;
        this.harga = harga;
    }

    public Long getId()                    { return id; }
    public String getNamaProduk()          { return namaProduk; }
    public MerkBlender getMerk()           { return merk; }
    public BigDecimal getHarga()           { return harga; }
    public void setHarga(BigDecimal h)     { this.harga = h; }
    public void setMerk(MerkBlender m)     { this.merk = m; }

    @Override
    public String toString() {
        return String.format("#%-3d | %-22s | %-10s | Rp %,d",
                id, namaProduk, merk, harga.longValue());
    }

    // ── Storage ───────────────────────────────────────────────────────────────
    private static final Map<Long, blender> data = new LinkedHashMap<>();
    private static final AtomicLong counter = new AtomicLong(1);

    public static blender tambah(String nama, MerkBlender merk, BigDecimal harga) {
        Long id = counter.getAndIncrement();
        blender b = new blender(id, nama, merk, harga);
        data.put(id, b);
        System.out.println("  Ditambahkan: " + b);
        return b;
    }

    public static void tampilSemua() {
        System.out.println("\n=== DAFTAR BLENDER & HARGA ===");
        System.out.printf("  %-4s | %-22s | %-10s | %s%n",
                "ID", "Nama Produk", "Merk", "Harga");
        System.out.println("  " + "-".repeat(58));
        data.values().forEach(b -> System.out.println("  " + b));
    }

    public static void tampilByMerk(MerkBlender merk) {
        System.out.println("\n=== MERK: " + merk + " ===");
        data.values().stream()
            .filter(b -> b.getMerk() == merk)
            .forEach(b -> System.out.println("  " + b));
    }

    public static void updateHarga(Long id, BigDecimal hargaBaru) {
        blender b = data.get(id);
        if (b == null) { System.out.println("ID tidak ditemukan!"); return; }
        System.out.printf("%n=== UPDATE HARGA ===%n  %s : Rp %,d -> Rp %,d%n",
                b.getNamaProduk(), b.getHarga().longValue(), hargaBaru.longValue());
        b.setHarga(hargaBaru);
    }

    public static void hapus(Long id) {
        blender b = data.remove(id);
        System.out.println("\n=== HAPUS ===");
        System.out.println(b != null ? "  Terhapus: " + b.getNamaProduk()
                                     : "  ID tidak ditemukan!");
    }

    // ── Main ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== TAMBAH DATA ===");
        tambah("Philips HR2041",      MerkBlender.PHILIPS,   new BigDecimal("350000"));
        tambah("Sharp EM-11L",        MerkBlender.SHARP,     new BigDecimal("280000"));
        tambah("Miyako BL-101 PF",    MerkBlender.MIYAKO,    new BigDecimal("150000"));
        tambah("Panasonic MX-GM1011", MerkBlender.PANASONIC, new BigDecimal("450000"));
        tambah("Tefal BL2A0166",      MerkBlender.TEFAL,     new BigDecimal("620000"));
        tambah("Vitamix E310",        MerkBlender.VITAMIX,   new BigDecimal("7500000"));

        tampilSemua();
        tampilByMerk(MerkBlender.PHILIPS);
        updateHarga(3L, new BigDecimal("165000"));
        hapus(2L);
        tampilSemua();
    }
}