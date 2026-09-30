package ListaExerciciosBatismoJava.ex01;

public class Main {
    public static void main(String[] args) {

        Ninja[] ninjas = {new Ninja("Naruto", 15, "Pegar cachorro", 'A'),new Ninja("Sasuke", 17, "Pegar cachorro", 'A'), new Ninja("Hinata", 14, "Pegar cachorro", 'B')};
        validacao(ninjas);
        for (Ninja ninja: ninjas){
            System.out.println(ninja.toString());
        }
    }


    public static void validacao(Ninja[] ninjas){
        for (Ninja ninja : ninjas){
            if(ninja.getIdade() <= 15){
                System.out.println("Ninja: " + ninja.getNome() + " so pode concluir missao de nivel C ou D");
                if (ninja.getNivelMissao() == 'A' || ninja.getNivelMissao() == 'B'){
                    ninja.setNivelMissao('F');
                    ninja.setStatus(Status.NAOCONCLUIDA);
                    continue;
                }
                ninja.setStatus(Status.CONCLUIDA);
            }
            ninja.setStatus(Status.CONCLUIDA);
        }
    }
}
