package bloco05_aulas54a63_encapsulamento_sobrecarga_construtores.aula62;

import bloco05_aulas54a63_encapsulamento_sobrecarga_construtores.aula56.Pedido;

public class Main {
    public static void main(String[] args) {
        Pessoa pessoa = new Pessoa("Leo");
        pessoa.imprimirNome();
        Pessoa.imprimirNome(pessoa.getNome());
    }
}
