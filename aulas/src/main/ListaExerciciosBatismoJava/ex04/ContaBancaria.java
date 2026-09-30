package ListaExerciciosBatismoJava.ex04;

public abstract class ContaBancaria implements Conta{

    protected double saldo;

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public ContaBancaria(double saldo) {
        this.saldo = saldo;
    }




    public void transferir(double valor, TipoConta tipoConta, Conta conta) {
        switch (tipoConta){
            case POUPANCA:

                break;
            case CORRENTE:
                break;
            default:
        }
    }
}
