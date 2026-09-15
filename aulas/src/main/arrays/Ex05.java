package arrays;

public class Ex05 {
    public static void main(String[] args) {
        int[] numeros = {2,4,6,8,10,12};

        for (int i = 0; i < numeros.length; i++) {
            if (numeros[i] > 6){
                System.out.println(numeros[i]);
            }
        }
    }
}
