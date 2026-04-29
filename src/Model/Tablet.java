package Model;

public class Tablet extends Device {

    public Tablet(String nama, double harga) {
        super(nama, harga);
    }
    
    @Override
    public String getKategori() {
        return "Tablet";
    }

    @Override
    public void tampilkanInfo(boolean withKategori) {
        if (withKategori) {
            System.out.println("Kategori : Tablet");
        }
        System.out.println("Nama : " + getNama());
        System.out.println("Harga : " + getHarga());
    }
}