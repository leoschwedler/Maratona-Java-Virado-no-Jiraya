package finalEEnumerações.aula78;

public class Main {
    public static void main(String[] args) {
        Carrinho carrinho = new Carrinho();
        carrinho.getCLIENTE().setNome("Leozinho");
        System.out.println(carrinho.getCLIENTE().toString());
    }
}
