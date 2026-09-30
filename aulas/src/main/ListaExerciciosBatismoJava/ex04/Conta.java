package ListaExerciciosBatismoJava.ex04;

public interface Conta {

    public double consultarSaldo();

    public void depositar(double valor);

    public boolean sacar(double valor);

    public boolean transferir(Conta contaDestino, double valor);
}
