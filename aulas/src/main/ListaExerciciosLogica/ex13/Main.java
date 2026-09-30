package ListaExerciciosLogica.ex13;

public class Main {
    public static void main(String[] args) {
        String[] numeros = new String[4];
        numeros[0] = "1";
        numeros[1] = "2";
        numeros[2] = "3";
        numeros[3] = "4";
        System.out.println(numeros);
        for (int i = 0; i < numeros.length; i++) {
            System.out.println(numeros[i]);
        }
        numeros = new String[3];
        numeros[0] = "10";
        numeros[1] = "9";
        numeros[2] = "8";
        System.out.println(numeros);

    }
}
