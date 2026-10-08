package bloco11_aulas106a111_wrappers_strings.aula106;

public class Produto {

    private String nome;
    private Integer quantidade;
    private Double preco;

    public Produto(String nome, Integer quantidade, Double preco, Boolean ativo) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.ativo = ativo;
    }

    private Boolean ativo;

    @Override
    public String toString() {
        return "Produto{" +
                "nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", ativo=" + ativo +
                '}';
    }
}
