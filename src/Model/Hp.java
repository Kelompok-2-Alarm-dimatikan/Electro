package Model;

public class Hp extends Device {

    public Hp(String nama, double harga) {
        super(nama, harga);
    }
    
    @Override
    public String getKategori() {
        return "Hp";
    }

    @Override
    public void tampilkanInfo(boolean withKategori) {
        if (withKategori) {
            System.out.println("Kategori : Hp");
        }
        System.out.println("Nama : " + getNama());
        System.out.println("Harga : " + getHarga());
    }
}