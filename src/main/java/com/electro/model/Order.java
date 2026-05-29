package com.electro.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaUser;
    private String emailUser;
    private String nomerUser;
    private String alamatUser;
    private String viaPembayaran;
    private LocalDateTime tanggal;
    private String status;
    private Double totalHarga = 0.0;
    private Integer totalBarang = 0;
    private Integer jumlahItem = 0;
    private boolean notified = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public Order() {
        this.status = "Menunggu";
        this.tanggal = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getNomerUser() {
        return nomerUser;
    }

    public void setNomerUser(String nomerUser) {
        this.nomerUser = nomerUser;
    }

    public String getAlamatUser() {
        return alamatUser;
    }

    public void setAlamatUser(String alamatUser) {
        this.alamatUser = alamatUser;
    }

    public String getViaPembayaran() {
        return viaPembayaran;
    }

    public void setViaPembayaran(String viaPembayaran) {
        this.viaPembayaran = viaPembayaran;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public Integer getTotalBarang() {
        return totalBarang;
    }

    public void setTotalBarang(Integer totalBarang) {
        this.totalBarang = totalBarang;
    }

    public Integer getJumlahItem() {
        return jumlahItem;
    }

    public void setJumlahItem(Integer jumlahItem) {
        this.jumlahItem = jumlahItem;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}