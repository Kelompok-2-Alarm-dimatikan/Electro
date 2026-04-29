package Main;

import java.util.ArrayList;
import javax.swing.SwingUtilities;
import Model.*;
import View.MainWindows;

public class Electro {
    public static void main(String[] args) {
        ArrayList<Device> deviceList = new ArrayList<>();
        
        // Data HP
        deviceList.add(new Hp("Samsung S23 Ultra", 10000));
        deviceList.add(new Hp("Samsung J2 Prime", 10000));
        deviceList.add(new Hp("Xiaomi 13", 10000));
        deviceList.add(new Hp("iPhone 14", 10000));
        
        // Data Laptop
        deviceList.add(new Laptop("Asus ROG", 30000));
        deviceList.add(new Laptop("Acer Predator", 25000));
        deviceList.add(new Laptop("Lenovo Legion", 35000));
        
        // Data Tablet
        deviceList.add(new Tablet("iPad Pro", 15000));
        deviceList.add(new Tablet("Samsung Galaxy Tab", 12000));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindows main = new MainWindows(deviceList);
                main.setVisible(true);
            }
        });
    }
}