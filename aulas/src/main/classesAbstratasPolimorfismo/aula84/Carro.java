package classesAbstratasPolimorfismo.aula84;

public class Carro extends Veiculo{
    public Carro(String marca, String modelo) {
        super(marca, modelo);
    }

    @Override
    public String toString() {
        return "Carro{" +
                "marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                '}';
    }
}
