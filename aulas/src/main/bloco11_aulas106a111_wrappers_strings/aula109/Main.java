package bloco11_aulas106a111_wrappers_strings.aula109;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {

//        // EX1
//        String texto = "Java";
//        System.out.println("Tamanhao da String " + texto.length());
//        char primeiroCaracter = texto.charAt(0);
//        System.out.println("Primeiro caractere " + primeiroCaracter);
//        char ultimoCaractere = texto.charAt(texto.length() -1);
//        System.out.println("Ultimo caractere " + ultimoCaractere);
//        char segundoCaractere = texto.charAt(1);
//        System.out.println("Segundo caractere " + segundoCaractere);

//        // Ex2
//        String nome = "   Leonardo   ";
//        String normalizada = nome.trim().toUpperCase();
//        System.out.println(normalizada);

        // EX3
        String codigo = "PROD-2026-JAVA";
        String primeiro = codigo.substring(0,4);
        String segundo = codigo.substring(5,9);
        String terceiro = codigo.substring(10,14);
        System.out.println(primeiro);
        System.out.println(segundo);
        System.out.println(terceiro);
        String codigo2 = "CLIENTE-9876-CURITIBA";
        String quarto = codigo2.substring(0,7);
        System.out.println(quarto);
        String quinto = codigo2.substring(8,12);
        System.out.println(quinto);
        String sexto = codigo2.substring(13);
        System.out.println(sexto);

    }
}
