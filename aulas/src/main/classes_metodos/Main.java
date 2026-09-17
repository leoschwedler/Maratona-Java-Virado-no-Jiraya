package classes_metodos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

       Estudante estudante = new Estudante();
       ArrayList<Double> notas = new ArrayList<>(Arrays.asList(10.0,20.0));

       estudante.setNota(notas.stream().mapToDouble(Double::doubleValue).toArray());
       estudante.calcularMedia();
       estudante.maiorNota();
    }
}
