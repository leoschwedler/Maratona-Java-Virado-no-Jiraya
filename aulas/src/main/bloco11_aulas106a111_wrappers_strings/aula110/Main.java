package bloco11_aulas106a111_wrappers_strings.aula110;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Long inicio = System.currentTimeMillis();
        medidor(new ArrayList(Arrays.asList("Leo", "maria", "jose", "Ayrton")));
        Long fim = System.currentTimeMillis();
        System.out.println("Tempo:" + (fim - inicio));
    }

    public static void medidor(ArrayList array){
        StringBuilder sb = new StringBuilder();
        //String string = "";
        for (int i = 0; i < array.size(); i++) {


         sb.append(array.get(i));
         if (i < array.size() -1){
             sb.append("-");
         }

            //string += i;
        }
        System.out.println("StringBuilder " + sb);
    }
}
