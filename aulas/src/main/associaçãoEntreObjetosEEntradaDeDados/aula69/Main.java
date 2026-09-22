package associaçãoEntreObjetosEEntradaDeDados.aula69;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite sua pergunta e eu responderei sim ou não");
        String pergunta = scanner.nextLine();
        if(!pergunta.isEmpty()){
            if (pergunta.charAt(0) == ' '){
                System.out.println("Sim, tem espaco");
            }else {
                System.out.println("Nao, nao tem espaco");
            }
        }else {
            System.out.println("Digite alguma coisa");
        }

    }
}
