package associaçãoEntreObjetosEEntradaDeDados.aula68;

import java.util.Scanner;

public class Main1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char primeiraLetra;
        int quantidadeCaracter;
        boolean temEspaco;
        String caixaBaixaCaixaAlta;
        boolean flag;

        System.out.println("Digite uma frase");
        
        String entrada = scanner.nextLine();

        primeiraLetra = entrada.charAt(0);
        quantidadeCaracter = entrada.length();
        temEspaco = entrada.contains(" ");
        if(primeiraLetra == 'a'){
            caixaBaixaCaixaAlta = "a";
            flag = true;

        }else {
            caixaBaixaCaixaAlta = "A";
            flag = false;
        }

        System.out.println("Primeira letra " + primeiraLetra);
        System.out.println("Quantidade de caractere " + quantidadeCaracter);
        System.out.println("Tem espaco? " + temEspaco);
        System.out.println("letra caixa baixa?  " + flag + "Letra: " + caixaBaixaCaixaAlta);
    }
}
