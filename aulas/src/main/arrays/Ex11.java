package arrays;

import java.util.Arrays;

public class Ex11 {
    public static void main(String[] args) {
        int numerosIguais = 0;
        int[] numeros = {2, 5, 2, 8, 2, 10, 5};

        for (int i = 0; i < numeros.length; i++) {
            for (int j = 0; j < numeros.length; j++) {
                if (numeros[i] == numeros[j]) {
                    numerosIguais++;
                    break;
                }
            }
        }
        System.out.println(numerosIguais);
    }
}
