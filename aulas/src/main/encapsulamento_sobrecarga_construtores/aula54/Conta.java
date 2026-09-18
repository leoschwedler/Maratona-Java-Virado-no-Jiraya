package encapsulamento_sobrecarga_construtores.aula54;

public class Conta {
    private String titular;
    private double saldo;

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double valor){
        if (valor < 0){
            System.out.println("Voce nao pode depositar um valor Negativp");
            return;
        }
        this.saldo += valor;
    }

    public void sacar(double valor){
        if(valor > this.saldo){
            System.out.println("Voce nao pode sacar um valor maior do que tem na conta");
            return;
        }
        saldo -= valor;
    }
}
