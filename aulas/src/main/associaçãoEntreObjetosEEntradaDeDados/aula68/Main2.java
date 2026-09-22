package associaçãoEntreObjetosEEntradaDeDados.aula68;

import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {

        String[] respostas = {"Bom dia", "Boa tarde", "Boa noite"};

        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite uma pergunta");
        String entrada = scanner.nextLine();
        char primeiroCaractere = entrada.charAt(0);

        if (primeiroCaractere == 'a'){
            System.out.println(respostas[0]);
        }else if(primeiroCaractere == 'b'){
            System.out.println(respostas[1]);
        }else if(primeiroCaractere == 'c'){
            System.out.println(respostas[2]);
        }else {
            System.out.println("Pergunta incorreta");
        }
    }
}
