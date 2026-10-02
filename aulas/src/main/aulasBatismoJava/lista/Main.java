package aulasBatismoJava.lista;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Iniciar uma lista tipada (RECOMENDADO)
        List<String> lista = new ArrayList<>();
        // Iniciar uma lista nao tipando (NAO RECOMENDADO)
        List ninjas = new ArrayList();
        // Iniciando uma lista com valores TIPADA (RECOMENDADO)
        List produtos = new ArrayList(Arrays.asList("Tv", "Celular"));
        // Iniciando uma lista com valores NAO TIPADA (NAO RECOMENDADO)
    }
}
