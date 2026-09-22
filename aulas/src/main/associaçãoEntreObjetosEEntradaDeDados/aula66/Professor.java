package associaçãoEntreObjetosEEntradaDeDados.aula66;

public class Professor {
    private String nome;
    private Escola escola;

    public void imprimir(){
        System.out.println("Nome: " + this.nome);
        if(escola == null) return;
        System.out.println("Escola: " + this.escola.getNome());
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Professor(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
