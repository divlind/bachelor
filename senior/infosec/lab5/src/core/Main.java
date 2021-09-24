package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EllepticCurve curve;
        String text;
        Point pB, g;
        List<Integer> ks;

        // reading alphabet and variant info from files
	    HashMap<String, Point> alphabet = TaskData.getAlphabet();
        HashMap<String, Object> var = TaskData.getVar();


        text = (String) var.get("Text");
        pB = new Point(
                Integer.parseInt((String)var.get("Bx")),
                Integer.parseInt((String)var.get("By"))
        );

        ks = (ArrayList) var.get("k");

        curve = new EllepticCurve(-1, 1, 751); // E(-1, 1) mod 751
        g = new Point(0, 1); // G = (0, 1)

        // encrypting text
        List<Point> result = new ArrayList<>();

        for(int i = 0; i < text.length(); i++){
            Point pm = alphabet.get(text.charAt(i)+"");
            int char_k = ks.get(i);

            // Cm = {kG, Pm + kPb}
            Point kG = curve.mul(g,char_k);
            Point kPb = curve.mul(pB,char_k);
            Point pmkpb = curve.sum(kPb, pm); // Pm + kPb

            System.out.println(text.charAt(i)+": k= "+ char_k+"; kG= "+kG.toString()+"; Pm= "+pm.toString()+"; kPb= "+ kPb.toString());
            System.out.println("Cm = (kg, Pm+kpB) = "+kG.toString()+" "+pmkpb.toString());
            System.out.println("-----------------------------------");

            result.add(kG);
            result.add(pmkpb);
        }

        System.out.println("\nResult: ");
        for(int i = 0; i < result.size(); i=i+2) {
            System.out.print(text.charAt(i/2)+": [");
            for (int j = 0; j < 2; j++)
                System.out.print(result.get(i+j).toString());
            System.out.println("];");
        }
    }
}
