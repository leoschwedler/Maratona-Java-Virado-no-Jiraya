package ListaExerciciosBatismoJava.ex03;

public class Main {

    public static void main(String[] args) {
        Uchiha sasuke = new Uchiha("Sasuke",16,"Matar o naruto", 'S', Status.NAOCONCLUIDA);
        sasuke.setHabilidadeEspecial("Charingan");
        System.out.println(sasuke.toString());
        sasuke.mostrarHabilidadeEspecial();
    }
}
