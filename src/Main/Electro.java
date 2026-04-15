package Main;
import java.util.ArrayList;
import java.util.Scanner;
import Model.*;
import Service.*;
public class Electro {

    public static void main(String[] args) {
        ArrayList<Device> deviceList = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        //Data HP
        deviceList.add(new Hp("Samsung", "S", "Samsung S23 Ultra", 10000));
        deviceList.add(new Hp("Xiaomi", "13", "Xiaomi 13", 10000));
        deviceList.add(new Hp("Apple", "14", "iPhone 14", 10000));
        //Data Laptop
        deviceList.add(new Laptop("Asus", "Asus ROG", 30000));
        deviceList.add(new Laptop("Acer", "Acer Predator", 25000));
        deviceList.add(new Laptop("Lenovo","Lenovo Legion", 35000));
        int pilih;

        do {

            System.out.println("\n=== Demon God Device Store ===");
            System.out.println("1. Daftar Device");
            System.out.println("2. Sort by Harga");
            System.out.println("3. Search by Harga");
            System.out.println("4. Tambah Device");
            System.out.println("5. Edit Device");
            System.out.println("6. Hapus Device");
            System.out.println("7. Keluar");
            System.out.println("==============================");
            System.out.print("Pilih : ");
            pilih = input.nextInt();

            switch (pilih) {

                case 1:
                    System.out.println("\n1. Hp");
                    System.out.println("2. Laptop");
                    System.out.print("Pilih: ");
                    int kategori = input.nextInt();
                    int no = 1;

                    if (kategori == 1) {
                        System.out.println("\n=== DAFTAR HP ===");
                        for (Device d : deviceList) {
                            if (d instanceof Hp) {
                                System.out.println("No: " + no++);
                                d.tampilkanInfo(true);
                                System.out.println("----------------");
                            }
                        }
                    } else if (kategori == 2) {
                        System.out.println("\n=== DAFTAR LAPTOP ===");
                        for (Device d : deviceList) {
                            if (d instanceof Laptop) {
                                System.out.println("No: " + no++);
                                d.tampilkanInfo(true);
                                System.out.println("----------------");
                            }
                        }
                    } else {
                        System.out.println("Pilihan tidak valid");
                    }
                    break;

                case 2:
                    SortByHarga.sortByHarga(deviceList);
                    System.out.println("\n1. Hp");
                    System.out.println("2. Laptop");
                    System.out.print("Pilih: ");
                    int kategoriSort = input.nextInt();

                    if (kategoriSort == 1) {
                        for (Device d : deviceList) {
                            if (d instanceof Hp) {
                                d.tampilkanInfo(true);
                                System.out.println("----------------");
                            }
                        }
                    } else if (kategoriSort == 2) {
                        for (Device d : deviceList) {
                            if (d instanceof Laptop) {
                                d.tampilkanInfo(true);
                                System.out.println("----------------");
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.println("\n1. Hp");
                    System.out.println("2. Laptop");
                    System.out.print("Pilih kategori: ");
                    int kategoriSearch = input.nextInt();
                    System.out.print("Masukkan harga: ");
                    double cariHarga = input.nextDouble();
                    SortByHarga.sortByHarga(deviceList);
                    System.out.println("\nHasil pencarian:");
                    SearchDevice.search(deviceList, cariHarga, kategoriSearch);
                    break;

                case 4:
                    System.out.println("\n1. Hp");
                    System.out.println("2. Laptop");
                    System.out.print("Pilih kategori: ");
                    int jenis = input.nextInt();
                    input.nextLine();
                    

                    if (jenis == 1) {
                    System.out.print("Brand: ");
                    String brand = input.nextLine();
                    System.out.print("Seri: ");
                    String seri = input.nextLine();
                    System.out.print("Nama: ");
                    String nama = input.nextLine();
                    System.out.print("Harga: ");
                    double harga = input.nextDouble();
                    deviceList.add(new Hp(brand, seri, nama, harga));
                    } else if (jenis == 2) {
                    System.out.print("Brand: ");
                    String brand = input.nextLine();
                    System.out.print("Nama: ");
                    String nama = input.nextLine();
                    System.out.print("Harga: ");
                    double harga = input.nextDouble();
                    deviceList.add(new Laptop(brand, nama, harga));
                    } else {
                        System.out.println("Kategori tidak valid");
                    }
                    System.out.println("Data berhasil ditambahkan!");
                    break;

               case 5:
                    System.out.println("\n1. Hp");
                    System.out.println("2. Laptop");
                    System.out.print("Pilih kategori: ");
                    int kategoriEdit = input.nextInt();

                    int index = 0;
                    ArrayList<Device> tempList = new ArrayList<>();

                    System.out.println("\n=== DATA ===");
                    for (Device d : deviceList) {
                        if ((kategoriEdit == 1 && d instanceof Hp) ||
                            (kategoriEdit == 2 && d instanceof Laptop)) {

                            tempList.add(d);
                            System.out.println("No: " + (++index));
                            d.tampilkanInfo(true);
                            System.out.println("----------------");
                        }
                    }

                    System.out.print("Pilih nomor: ");
                    int pilihEdit = input.nextInt() - 1;
                    input.nextLine(); // Membersihkan buffer newline

                    if (pilihEdit >= 0 && pilihEdit < tempList.size()) {
                        Device d = tempList.get(pilihEdit);
                        
                        if (kategoriEdit == 1 && d instanceof Hp) {
                            Hp hpEdit = (Hp) d;
                            System.out.print("Brand baru: ");
                            String newBrand = input.nextLine();
                            System.out.print("Seri baru: ");
                            String newSeri = input.nextLine();
                            System.out.print("Nama baru: ");
                            String newNama = input.nextLine();
                            System.out.print("Harga baru: ");
                            double newHarga = input.nextDouble();
                            hpEdit.setBrand(newBrand);
                            hpEdit.setSeri(newSeri);
                            d.setNama(newNama);
                            d.setHarga(newHarga);
                        } else if (kategoriEdit == 2 && d instanceof Laptop) {
                            Laptop laptopEdit = (Laptop) d; 
                            System.out.print("Brand baru: ");
                            String newBrand = input.nextLine();
                            System.out.print("Nama baru: ");
                            String newNama = input.nextLine();
                            System.out.print("Harga baru: ");
                            double newHarga = input.nextDouble();
                            laptopEdit.setBrand(newBrand);
                            d.setNama(newNama);
                            d.setHarga(newHarga);
                        }
                        System.out.println("Data berhasil diupdate!");
                    } else {
                        System.out.println("Data tidak valid!");
                    }
                    break;

                case 6:
                    System.out.println("\n1. Hp");
                    System.out.println("2. Laptop");
                    System.out.print("Pilih kategori: ");
                    int kategoriHapus = input.nextInt();

                    ArrayList<Device> hapusList = new ArrayList<>();
                    int noHapus = 0;

                    for (Device d : deviceList) {
                        if ((kategoriHapus == 1 && d instanceof Hp) ||
                            (kategoriHapus == 2 && d instanceof Laptop)) {

                            hapusList.add(d);
                            System.out.println("No: " + (++noHapus));
                            d.tampilkanInfo(true);
                            System.out.println("----------------");
                        }
                    }

                    System.out.print("Pilih nomor: ");
                    int pilihHapus = input.nextInt() - 1;

                    if (pilihHapus >= 0 && pilihHapus < hapusList.size()) {
                        deviceList.remove(hapusList.get(pilihHapus));
                        System.out.println("Data berhasil dihapus!");
                    } else {
                        System.out.println("Data tidak valid!");
                    }
                    break;

                case 7:
                    System.out.println("Created by : Syaa");
                    System.out.println("Program selesai.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid");
            }
        } while (pilih != 7);
        input.close();
    }
}