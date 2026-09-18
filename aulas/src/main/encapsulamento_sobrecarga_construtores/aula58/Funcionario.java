package encapsulamento_sobrecarga_construtores.aula58;

public class Funcionario {
    private String nome;
    private double salario;

    public Funcionario(String nome, double salario) throws IllegalAccessException {
        if(nome.trim().isEmpty() || nome == null){
            throw new IllegalAccessException("O nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
        if(salario < 0){
            throw new IllegalAccessException("O Salario nao pode ser negativo.");
        }
        this.salario = salario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
