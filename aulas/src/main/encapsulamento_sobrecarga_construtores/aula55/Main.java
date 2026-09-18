package encapsulamento_sobrecarga_construtores.aula55;

public class Main {
    public static void main(String[] args) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("leo");
        System.out.println(funcionario.getNome());
        System.out.println(funcionario.getSalario());
        funcionario.aumentarSalario(20);
        System.out.println(funcionario.getSalario());
    }
}
