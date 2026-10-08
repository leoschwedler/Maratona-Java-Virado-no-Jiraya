package bloco08_aulas77a83_final_enumeracoes.aula76;

public class Anime {
    private String nome;
    private int episodios;
    private static final String genero;

    public Anime(String nome) {
        this.nome = nome;
    }

    static  {
        genero = "Aventura";
    }

    @Override
    public String toString() {
        return "Anime{" +
                "nome='" + nome + '\'' +
                ", episodios=" + episodios +
                ", genero='" + genero + '\'' +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
