package finalEEnumerações.aula81;

public class Main {

    public static void main(String[] args) {
        Bairro bairro = new Bairro("Bacacheri", Mercado.FEIJAO);
        System.out.println(bairro.getMercado().getCodigo() + " " + bairro.getMercado().getComida());
    }

}
