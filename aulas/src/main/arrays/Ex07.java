package arrays;

public class Ex07 {
    public static void main(String[] args) {
        int[] array = {5,10,15,3,12,11,5,7,3,4,5};
        int soma = 0;

        for (int i = 0; i < array.length; i++) {
            soma += array[i];
        }
        System.out.println(soma);
    }
}
