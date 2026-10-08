package bloco08_aulas77a83_final_enumeracoes.aula80;

public class Sistema {
    private Tipo tipo;
//    public static final String ERP = "erp";
//    public static final String SAAS = "saas";
//    public static final String CONSTANTE = "constante";

    @Override
    public String toString() {
        return "Sistema{" +
                "tipo='" + tipo + '\'' +
                '}';
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }


}
