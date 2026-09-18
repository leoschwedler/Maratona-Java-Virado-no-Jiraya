package encapsulamento_sobrecarga_construtores.aula54;

public class Ex01Test {
    public static void main(String[] args) {
        Produto produto = new Produto();
        produto.setNome("Leo");
        produto.setPreco(50);
        System.out.println(produto.getNome());
        System.out.println(produto.getPreco());

    }
}
