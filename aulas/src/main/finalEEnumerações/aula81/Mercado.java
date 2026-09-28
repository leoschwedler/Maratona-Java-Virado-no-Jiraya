package finalEEnumerações.aula81;

public enum Mercado {
    FRUTA("fruta,",1),
    ARROZ("arroz",2),
    FEIJAO("feijao",3);

    private String comida;
    private int codigo;

    Mercado(String comida, int codigo) {
        this.comida = comida;
        this.codigo = codigo;
    }

    public String getComida() {
        return comida;
    }

    public int getCodigo() {
        return codigo;
    }
}
