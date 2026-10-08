package bloco08_aulas77a83_final_enumeracoes.aula81;

public class Main {

    public static void main(String[] args) {
        Bairro bairro = new Bairro("Bacacheri", Mercado.FEIJAO);
        System.out.println(bairro.getMercado().getCodigo() + " " + bairro.getMercado().getComida());
    }

}
