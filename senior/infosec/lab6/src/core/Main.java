package core;

import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EllepticCurve curve;
        int nB;
        List<Point[]> cipherText;

        // reading alphabet and variant info from files
        HashMap<Point, String> alphabet = TaskData.getAlphabet();
        HashMap<String, Object> var = TaskData.getVar();
        nB = (int)var.get("NB");
        cipherText = (List) var.get("Text");

        curve = new EllepticCurve(-1, 1, 751); // E(-1, 1) mod 751

        //decrypting text
        System.out.println("n = " + nB);
        String res = "";

        for (int i = 0; i < cipherText.size(); i++){
            Point[] points = cipherText.get(i);

            //Pm + kPb - nB(kG) = alphabet point
            Point invKg = new Point(points[0].getX(),-points[0].getY()); // -kG
            Point invNKg = curve.mul(invKg, nB); // -nB(kG)
            Point resP = curve.sum(points[1], invNKg); // Pm + kPb - nB(kG)

            res += alphabet.get(resP);


            System.out.println("Symbol ["+points[0].toString()+""+points[1].toString()+"]; -nkG = "+
                    invNKg.toString()+"; Pm + kPb - nkg ="+resP.toString()+"; char: "+alphabet.get(resP));
            System.out.println("--------------------------------------------------------------------------");
        }
        System.out.println("Result: "+res.replace("null","_"));

    }
}
