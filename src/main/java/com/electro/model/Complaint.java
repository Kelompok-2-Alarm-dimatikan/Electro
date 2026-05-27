package com.electro.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "electronic_id", nullable = true)
    private Electronic electronic;

    @Column(nullable = false)
    private String kategori;

    @Column(nullable = false)
    private String judul;

    @Column(nullable = false, length = 3000)
    private String deskripsi;

    @Column(nullable = false)
    private String status; // PENDING, DIPROSES, SELESAI

    @Column(nullable = false)
    private LocalDateTime tanggal;

    public Complaint() {
        this.status = "PENDING";
        this.tanggal = LocalDateTime.now();
    }

    public Complaint(User user, Electronic electronic, String kategori, String judul, String deskripsi) {
        this.user = user;
        this.electronic = electronic;
        this.kategori = kategori;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.status = "PENDING";
        this.tanggal = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Electronic getElectronic() {
        return electronic;
    }

    public void setElectronic(Electronic electronic) {
        this.electronic = electronic;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }
}
