package ListaExerciciosBatismoJava.ex04;

public class ContaCorrente extends ContaBancaria{

    public ContaCorrente(double saldo) {
        super(saldo);
    }

    @Override
    public double consultarSaldo() {
        return this.saldo;
    }

    @Override
    public void depositar(double valor) {
        double taxa = valor * 0.10;
        this.saldo = valor - taxa;
    }

    @Override
    public boolean sacar(double valor) {
        if(valor > 0 && this.saldo >= valor){
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    public boolean transferir(Conta contaDestino, double valor) {
        // Tenta sacar da conta atual (this)
        if (this.sacar(valor)) {
            // Se o saque funcionou, deposita na conta de destino
            contaDestino.depositar(valor);
            return true; // Transferência realizada com sucesso
        }
        return false; // Falha na transferência (ex: falta de saldo)
    }



}
