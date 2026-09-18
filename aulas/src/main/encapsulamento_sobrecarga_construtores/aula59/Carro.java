package encapsulamento_sobrecarga_construtores.aula59;

public class Carro {
    private String modelo;
    private double ano;
    private double preco;

    public String getModelo() {
        return modelo;
    }

    public Carro() {
    }

    public Carro(String modelo) {
        this.modelo = modelo;
    }

    public Carro(String modelo, double ano, double preco) {
        this(modelo);
        this.ano = ano;
        this.preco = preco;
    }


}
