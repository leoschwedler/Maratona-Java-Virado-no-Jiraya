package herancaESobrescrita;

public class Funcionario extends Pessoa{
    private String trabalho;
    private Empresa empresa;

    public Funcionario(String nome, String idade, String trabalho) {
        super(nome, idade);
        this.trabalho = trabalho;
    }

    @Override
    public void imprimir() {
        super.imprimir();

    }
}
