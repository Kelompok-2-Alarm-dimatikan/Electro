package Model;
public class Device {
    private String nama;
    private double harga;
    public Device(String nama, double harga) {
        this.nama = nama;
        this.harga = harga;
    }
    public String getNama() {
        return nama;
    }
    public double getHarga() {
        return harga;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setHarga(double harga) {
        this.harga = harga;
    }
    public void tampilkanInfo() {
        System.out.println("Nama : " + nama);
        System.out.println("Harga : " + harga);
    }
    public void tampilkanInfo(boolean withKategori) {
        if (withKategori) {
            System.out.println("Kategori : Device");
        }
        tampilkanInfo();
    }
}