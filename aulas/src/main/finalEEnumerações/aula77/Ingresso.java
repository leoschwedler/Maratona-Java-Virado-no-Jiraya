package finalEEnumerações.aula77;

public class Ingresso {
    private static final double valor;
    private final double horario = 9.30;
    private final String tipo;

    public Ingresso(String tipo) {
        this.tipo = tipo;
    }

    private String nome;

    static {
        valor = 15.90;
    }


}
