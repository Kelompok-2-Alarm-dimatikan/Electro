package Model;

public class Hp extends Device {
    private String brand;
    private String seri;
    public Hp(String brand, String seri, String nama, double harga) {
        super(nama, harga);
        this.brand = brand;
        this.seri = seri;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getSeri() {
        return seri;
    }
    public void setSeri(String seri) {
        this.seri = seri;
    }

    @Override
    public void tampilkanInfo(boolean withKategori) {
        if (withKategori) {
            System.out.println("Kategori : Hp");
        }
        System.out.println("Brand : " + this.brand);
        System.out.println("Seri  : " + this.seri);
        System.out.println("Nama  : " + getNama());
        System.out.println("Harga : " + getHarga());
    }
}