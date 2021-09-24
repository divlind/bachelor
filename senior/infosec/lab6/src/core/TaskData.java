package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaskData {

    public static HashMap<Point, String> getAlphabet(){
        HashMap<Point, String> alphabet = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader("alphabet.txt"))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] arr = line.split(" , ");
                alphabet.put(new Point(Integer.parseInt(arr[1]), Integer.parseInt(arr[2])),arr[0]);
            }
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        return alphabet;
    }

    public static HashMap<String, Object> getVar(){
        HashMap<String, Object> map = new HashMap<>();
        List<Point[]> list = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader("task.txt"))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] arr = line.split(":");

                if(arr[0].equals("NB"))
                    map.put(arr[0], Integer.parseInt(arr[1]));
                else {

                    String[] str = arr[1].split("[;,]");
                    for (int i = 0; i < str.length; i += 4) {
                        Point[] points = new Point[2];
                        points[0] = new Point(Integer.parseInt(str[i]), Integer.parseInt(str[i+1]));
                        points[1] = new Point(Integer.parseInt(str[i+2]), Integer.parseInt(str[i+3]));
                        list.add(points);
                    }

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
