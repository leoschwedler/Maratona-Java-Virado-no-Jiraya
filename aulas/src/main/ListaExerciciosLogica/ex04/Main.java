package ListaExerciciosLogica.ex04;

public class Main {
    public static void main(String[] args) {
        imprime(4);
    }

    static void imprime(int numero){
        System.out.println("Numero Sucessor " + (numero + 1));
        System.out.println("Numero Antecessor " + (numero - 1));
    }
}
