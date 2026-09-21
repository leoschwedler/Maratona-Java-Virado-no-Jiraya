package associaçãoEntreObjetosEEntradaDeDados.aula64;

public class Jogador {
    private String nome;

    public void imprimir(){
        System.out.println(this.nome);
    }

    public void imprimeNome(){
        System.out.println(this.nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Jogador(String nome) {
        this.nome = nome;
    }
}
