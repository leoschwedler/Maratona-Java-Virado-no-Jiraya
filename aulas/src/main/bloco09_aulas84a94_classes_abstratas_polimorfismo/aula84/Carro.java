package bloco09_aulas84a94_classes_abstratas_polimorfismo.aula84;

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
