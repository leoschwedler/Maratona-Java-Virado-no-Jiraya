package ListaExerciciosLogica.ex10;

public class Main {
    public static void main(String[] args) {
        media(6,7,8);
    }

    public static void media(double nota1, double nota2, double nota3){
        double media = (nota1 + nota2 + nota3) / 3;
        System.out.println(media);
    }
}
