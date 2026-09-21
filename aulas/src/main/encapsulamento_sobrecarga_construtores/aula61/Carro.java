package encapsulamento_sobrecarga_construtores.aula61;

public class Carro {
    private String modelo;
    public static double velocidadeLimite;

    public Carro(String modelo) {
        this.modelo = modelo;
    }

    public void imprime(){
        System.out.println("Modelo: " + this.modelo);
        System.out.println("Velocidade Limite: " + this.velocidadeLimite);
    }
}
