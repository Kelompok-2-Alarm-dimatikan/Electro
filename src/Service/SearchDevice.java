package Service;
import java.util.ArrayList;
import Model.*;

public class SearchDevice {
    public static void search(ArrayList<Device> deviceList, double searchHarga, int kategori) {
        int kiri = 0;
        int kanan = deviceList.size() - 1;
        boolean ditemukan = false;

        while (kiri <= kanan) {
            int tengah = kiri + (kanan - kiri) / 2;

            if (deviceList.get(tengah).getHarga() == searchHarga) {
                int i = tengah;
                while (i >= 0 && deviceList.get(i).getHarga() == searchHarga) {
                    if ((kategori == 1 && deviceList.get(i) instanceof Hp) ||
                        (kategori == 2 && deviceList.get(i) instanceof Laptop) ||
                        (kategori == 3 && deviceList.get(i) instanceof Tablet)) {

                        deviceList.get(i).tampilkanInfo(true);
                        System.out.println("----------------");
                        ditemukan = true;
                    }
                    i--;
                }
                i = tengah + 1;
                while (i < deviceList.size() && deviceList.get(i).getHarga() == searchHarga) {
                    if ((kategori == 1 && deviceList.get(i) instanceof Hp) ||
                        (kategori == 2 && deviceList.get(i) instanceof Laptop) ||
                        (kategori == 3 && deviceList.get(i) instanceof Tablet)) {

                        deviceList.get(i).tampilkanInfo(true);
                        System.out.println("----------------");
                        ditemukan = true;
                    }
                    i++;
                }
                break;
            }

            if (deviceList.get(tengah).getHarga() < searchHarga) {
                kiri = tengah + 1;
            } else {
                kanan = tengah - 1;
            }
        }
        if (!ditemukan) {
            System.out.println("Device tidak ditemukan");
        }
    }
}