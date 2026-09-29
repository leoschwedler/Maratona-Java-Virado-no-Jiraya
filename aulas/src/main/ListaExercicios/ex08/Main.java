package ListaExercicios.ex08;

public class Main {
    public static void main(String[] args) {
        ordenacao(15,3,2);
    }


    public static void ordenacao(int a, int b, int c){
        int[] numeros = {a,b,c};
        for (int i = numeros.length; i >= 0; i--) {
            System.out.println(numeros[i]);
        }
    }
}
