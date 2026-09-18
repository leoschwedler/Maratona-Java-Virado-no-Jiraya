package encapsulamento_sobrecarga_construtores.aula54;

public class Ex02Test {
    public static void main(String[] args) {

        Conta conta = new Conta();
        conta.setTitular("Leonardo");
        conta.depositar(-50);
        conta.depositar(300.0);
        System.out.println(conta.getSaldo());
        conta.sacar(301.0);
        conta.sacar(250.0);
        System.out.println(conta.getSaldo());
    }
}
