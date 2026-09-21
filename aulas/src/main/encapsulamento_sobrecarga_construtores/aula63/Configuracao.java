package encapsulamento_sobrecarga_construtores.aula63;

public class Configuracao {
    private static String ambiente;

    static {
        Configuracao.ambiente = "Windows";
    }

    public static String getAmbiente() {
        return Configuracao.ambiente;
    }

    public static void setAmbiente(String ambiente) {
        Configuracao.ambiente = ambiente;
    }
}
