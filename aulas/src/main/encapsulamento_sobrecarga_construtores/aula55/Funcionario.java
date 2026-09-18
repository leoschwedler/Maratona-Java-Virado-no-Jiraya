package encapsulamento_sobrecarga_construtores.aula55;

public class Funcionario {
    private String nome;
    private double salario = 1000;
    private int nivel;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalario() {
        return salario;
    }

    public int getNivel() {
        return nivel;
    }

    public void aumentarSalario(double percentual){
        double divide = percentual / 100;
        double resultado = this.salario * divide;
        this.salario += resultado;
    }
}
