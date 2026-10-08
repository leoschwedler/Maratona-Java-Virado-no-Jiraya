package bloco08_aulas77a83_final_enumeracoes.aula81;

public class Bairro {
    private String nome;
    private Mercado mercado;

    public Mercado getMercado() {
        return mercado;
    }

    public Bairro(String nome, Mercado mercado) {
        this.nome = nome;
        this.mercado = mercado;
    }
}
