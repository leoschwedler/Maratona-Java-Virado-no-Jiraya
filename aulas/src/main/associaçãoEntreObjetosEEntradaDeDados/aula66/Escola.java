package associaçãoEntreObjetosEEntradaDeDados.aula66;

public class Escola {
    private String nome;
    private Professor[] professores;

    public String getNome() {
        return nome;
    }

    public Professor[] getProfessores() {
        return professores;
    }

    public void imprimir(){
        System.out.println("Escola: " + this.nome);
        if (professores == null){
            return;
        }else {
            for (Professor professor : this.professores){
                if(professor == null){
                    System.out.println("Professor faltou");
                    continue;
                }
                System.out.println("Professor: " + professor.getNome());
            }
        }
    }

    public void setProfessores(Professor[] professores) {
        this.professores = professores;
    }

    public Escola(String nome) {
        this.nome = nome;
    }
}
