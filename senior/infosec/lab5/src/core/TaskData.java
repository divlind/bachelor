package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaskData {

    public static HashMap<String, Point> getAlphabet(){
        HashMap<String, Point> alphabet = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader("alphabet.txt"))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] arr = line.split(" , ");
                alphabet.put(arr[0], new Point(Integer.parseInt(arr[1]), Integer.parseInt(arr[2])));
            }
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        return alphabet;
    }

    public static HashMap<String, Object> getVar(){
        HashMap<String, Object> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader("task.txt"))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] arr = line.split(":");

                if(!arr[0].equals("k"))
                    map.put(arr[0], arr[1]);
                else {
                    String[] str = arr[1].split(",");
                    for(String ks: str)
                        list.add(Integer.parseInt(ks));

                    map.put(arr[0], list);
                }
            }
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        return map;
    }
}
