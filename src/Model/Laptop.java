package Model;
public class Laptop extends Device {
    private String brand;
    public Laptop(String brand,String nama, double harga) {
        super(nama, harga);
        this.brand = brand;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    @Override
    public void tampilkanInfo(boolean withKategori) {
        if (withKategori) {
            System.out.println("Kategori : Laptop");
        }
        System.out.println("Brand : " + this.brand);
        System.out.println("Nama : " + getNama());
        System.out.println("Harga : " + getHarga());
        
    }
}