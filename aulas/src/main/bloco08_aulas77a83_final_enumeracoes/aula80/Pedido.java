package bloco08_aulas77a83_final_enumeracoes.aula80;

public class Pedido {
    private String nome;
    private StatusPedido statusPedido;

    @Override
    public String toString() {
        return "Pedido{" +
                "nome='" + nome + '\'' +
                ", statusPedido=" + statusPedido +
                '}';
    }

    public Pedido(String nome, StatusPedido statusPedido) {
        this.nome = nome;
        this.statusPedido = statusPedido;
    }
}
