package Service;
import java.util.ArrayList;
import Model.Device;

public class SortByHarga {
    public static void sortByHarga(ArrayList<Device> deviceList) {
        int n = deviceList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (deviceList.get(j).getHarga() > deviceList.get(j + 1).getHarga()) {
                    Device temp = deviceList.get(j);
                    deviceList.set(j, deviceList.get(j + 1));
                    deviceList.set(j + 1, temp);
                }
            }
        }
    }
}