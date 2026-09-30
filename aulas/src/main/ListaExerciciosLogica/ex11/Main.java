package ListaExerciciosLogica.ex11;

public class Main {
    public static void main(String[] args) {

    }

    public void media(String aluno, double nota1,double nota2, double nota3, double nota4){
        double media = (nota1 + nota2 + nota3 + nota4) / 4;
        if (media >= 7){
            System.out.println("Aluno: " + aluno + " APROVADO");
            return;
        }
        System.out.println("Aluno: " + aluno + " REPROVADO");
    }
}
