package arrays;

public class Ex06 {
    public static void main(String[] args) {
        int maiorNumero;
        int[] array = {5,10,15,3,12,11,5,7};

        maiorNumero = array[0];
        for (int i = 0; i < array.length; i++) {
            if(maiorNumero < array[i]){
                maiorNumero = array[i];
            }
        }

        System.out.println(maiorNumero);

    }
}
