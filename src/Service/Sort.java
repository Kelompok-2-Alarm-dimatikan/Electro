package Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Model.Device;

public class Sort {
    public static ArrayList<Device> sort(ArrayList<Device> deviceList, boolean ascending){

        ArrayList<Device> sortedList = new ArrayList<>(deviceList);

        if (ascending){
            Collections.sort(sortedList, Comparator.comparingDouble(Device::getHarga));
        }
        else{
            Collections.sort(sortedList, Comparator.comparingDouble(Device::getHarga ).reversed());
        }

        return sortedList;
    }
}

  