package Service;
import java.util.ArrayList;
import Model.Device;
import Model.Hp;
import Model.Laptop;
import Model.Tablet;


public class CRUD {
    
    // Method tambah
    public static void tambahDevice(ArrayList<Device> deviceList, String kategori, String nama, double harga){
        
        if (kategori.equals("Hp")){
            deviceList.add(new Hp(nama, harga));
        }
        else if (kategori.equals("Laptop")){
            deviceList.add(new Laptop(nama, harga));
        }
        else if (kategori.equals("Tablet")){
            deviceList.add(new Tablet(nama, harga));
        }
    }

    // Method hapus
    public static void hapusDevice(ArrayList<Device> deviceList, Device device){

        deviceList.remove(device);

    }

    // Method edit
    public static void editDevice(Device device, String namaBaru, double hargaBaru){

        device.setNama(namaBaru);
        device.setHarga(hargaBaru);

    }

}
    
