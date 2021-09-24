package lab1;

import java.util.Random;

public class Util {
    public static String getRandomString(int length){
        String res = "";

        Random r = new Random();
        for(int i =0; i<length; i++) {
            char c = (char) (r.nextInt(26) + 'a');
            c = r.nextInt(10) < 2 ? ' ' : c;
            res = new StringBuilder(res).append(c).toString();
        }
        return res;
    }
}
