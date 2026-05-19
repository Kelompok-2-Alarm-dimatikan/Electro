package com.electro.model;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Kulkas {

    // ── Enum Merk ─────────────────────────────────────────────────────────────
    public enum MerkKulkas {
        SAMSUNG("Samsung"),
        LG("LG"),
        SHARP("Sharp"),
        PANASONIC("Panasonic"),
        POLYTRON("Polytron"),
        AQUA("Aqua"),
        TOSHIBA("Toshiba"),
        HITACHI("Hitachi"),
        ELECTROLUX("Electrolux"),
        MITSUBISHI("Mitsubishi");

        private final String label;
        MerkKulkas(String label) { this.label = label; }
        public String getLabel()  { return label; }

        @Override
        public String toString()  { return label; }
    }

    // ── Field ─────────────────────────────────────────────────────────────────
    private Long id;
    private String namaProduk;
    private MerkKulkas merk;
    private BigDecimal harga;

    public Kulkas(Long id, String namaProduk, MerkKulkas merk, BigDecimal harga) {
        this.id         = id;
        this.namaProduk = namaProduk;
        this.merk       = merk;
        this.harga      = harga;
    }

    public Long getId()                { return id; }
    public String getNamaProduk()      { return namaProduk; }
    public MerkKulkas getMerk()        { return merk; }
    public BigDecimal getHarga()       { return harga; }
    public void setHarga(BigDecimal h) { this.harga = h; }
    public void setMerk(MerkKulkas m)  { this.merk = m; }

    @Override
    public String toString() {
        return String.format("#%-3d | %-25s | %-12s | Rp %,d",
                id, namaProduk, merk, harga.longValue());
    }

    // ── Storage ───────────────────────────────────────────────────────────────
    private static final Map<Long, Kulkas> data = new LinkedHashMap<>();
    private static final AtomicLong counter = new AtomicLong(1);

    // ── Tambah ────────────────────────────────────────────────────────────────
    public static Kulkas tambah(String nama, MerkKulkas merk, BigDecimal harga) {
        Long id = counter.getAndIncrement();
        Kulkas k = new Kulkas(id, nama, merk, harga);
        data.put(id, k);
        System.out.println("  Ditambahkan: " + k);
        return k;
    }

    // ── Tampil Semua ──────────────────────────────────────────────────────────
    public static void tampilSemua() {
        System.out.println("\n=== DAFTAR KULKAS & HARGA ===");
        System.out.printf("  %-4s | %-25s | %-12s | %s%n",
                "ID", "Nama Produk", "Merk", "Harga");
        System.out.println("  " + "-".repeat(62));
        data.values().forEach(k -> System.out.println("  " + k));
    }

    // ── Filter by Merk ────────────────────────────────────────────────────────
    public static void tampilByMerk(MerkKulkas merk) {
        System.out.println("\n=== MERK: " + merk + " ===");
        data.values().stream()
            .filter(k -> k.getMerk() == merk)
            .forEach(k -> System.out.println("  " + k));
    }

    // ── Update Harga ──────────────────────────────────────────────────────────
    public static void updateHarga(Long id, BigDecimal hargaBaru) {
        Kulkas k = data.get(id);
        if (k == null) { System.out.println("  ID tidak ditemukan!"); return; }
        System.out.printf("%n=== UPDATE HARGA ===%n  %s : Rp %,d -> Rp %,d%n",
                k.getNamaProduk(), k.getHarga().longValue(), hargaBaru.longValue());
        k.setHarga(hargaBaru);
    }

    // ── Hapus ─────────────────────────────────────────────────────────────────
    public static void hapus(Long id) {
        Kulkas k = data.remove(id);
        System.out.println("\n=== HAPUS ===");
        System.out.println(k != null ? "  Terhapus: " + k.getNamaProduk()
                                     : "  ID tidak ditemukan!");
    }

    // ── Main ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("=== TAMBAH DATA KULKAS ===");
        tambah("Samsung RT38K501JS8",   MerkKulkas.SAMSUNG,    new BigDecimal("5200000"));
        tambah("LG GN-B202SQBB",        MerkKulkas.LG,         new BigDecimal("3100000"));
        tambah("Sharp SJ-296MD-SS",     MerkKulkas.SHARP,      new BigDecimal("3800000"));
        tambah("Panasonic NR-B27SW2",   MerkKulkas.PANASONIC,  new BigDecimal("4100000"));
        tambah("Polytron PRG 25B",      MerkKulkas.POLYTRON,   new BigDecimal("2300000"));
        tambah("Aqua AQRB6111MG",       MerkKulkas.AQUA,       new BigDecimal("1900000"));
        tambah("Toshiba GR-A28MS",      MerkKulkas.TOSHIBA,    new BigDecimal("3500000"));
        tambah("Hitachi R-B270PH7",     MerkKulkas.HITACHI,    new BigDecimal("6800000"));

        tampilSemua();
        tampilByMerk(MerkKulkas.SAMSUNG);
        updateHarga(5L, new BigDecimal("2500000"));
        hapus(2L);
        tampilSemua();
    }
}