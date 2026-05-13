package Service;
import java.util.ArrayList;
import Model.*;

public class Search {

    public static ArrayList<Device> search(ArrayList<Device> deviceList, double targetHarga){

        ArrayList<Device> hasilSearch = new ArrayList<>();

        for (Device list : deviceList){
            if (list.getHarga() == targetHarga){
                hasilSearch.add(list);
            }
        }

        return hasilSearch;
       
    }
}


