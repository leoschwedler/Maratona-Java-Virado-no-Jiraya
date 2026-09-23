package herancaESobrescrita;

public class Pessoa {
    protected String nome;
    private String idade;

    public void imprimir(){
        System.out.println(this.nome);
        System.out.println(this.idade);
    }
    public Pessoa(String nome, String idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }
}
