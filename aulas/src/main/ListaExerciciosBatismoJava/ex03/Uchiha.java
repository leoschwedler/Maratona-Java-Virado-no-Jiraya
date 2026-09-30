package ListaExerciciosBatismoJava.ex03;

public class Uchiha extends Ninja{

    private String habilidadeEspecial;

    public Uchiha(String nome, int idade, String missa, char nivelDificuldade, Status status) {
        super(nome, idade, missa, nivelDificuldade, status);
    }


    public String getHabilidadeEspecial() {
        return habilidadeEspecial;
    }

    public void setHabilidadeEspecial(String habilidadeEspecial) {
        this.habilidadeEspecial = habilidadeEspecial;
    }

    public void mostrarHabilidadeEspecial(){
        System.out.println("A habilidade dos UCHIHA é: " + habilidadeEspecial);
    }

    @Override
    public String toString() {
        return super.toString() + " " + "E olha a habilidade do UCHIHA: " + habilidadeEspecial;

    }
}
