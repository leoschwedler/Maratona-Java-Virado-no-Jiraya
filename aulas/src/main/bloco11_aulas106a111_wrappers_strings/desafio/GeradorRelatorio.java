package bloco11_aulas106a111_wrappers_strings.desafio;

import java.util.ArrayList;
import java.util.Arrays;

public class GeradorRelatorio {
    public static void main(String[] args) {
        Produto produto = new Produto();
        produto.setCodigo("PROD-2026-JAVA");
        ArrayList produtos = extrairProduto(produto.getCodigo());
        produtos.stream().map( p -> p.toString()).forEach(
                a -> System.out.println(a)
        );
        produto.setNome("   curso java   ".trim());

        System.out.println(produto);
    }


    public static ArrayList<StringBuilder> extrairProduto(String frase){
        StringBuilder primeiro = new StringBuilder();
        StringBuilder segundo = new StringBuilder();
        StringBuilder terceiro = new StringBuilder();

        int contador = 0;
        for (int i = 0; i < frase.length(); i++) {
            char letra = frase.charAt(i);

            if (letra == '-'){
                contador++;
                continue;
            }

            switch (contador){
                case 0:
                    primeiro.append(letra);
                    break;
                case 1:
                    segundo.append(letra);
                    break;
                case 2:
                    terceiro.append(letra);
                    break;
            }
        }

        return new ArrayList<StringBuilder>(Arrays.asList(primeiro,segundo,terceiro));
    }
}
