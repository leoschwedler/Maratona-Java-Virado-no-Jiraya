package bloco06_aulas64a70_associacao_entrada_dados.aula68;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite espaco para sim, e algum caractere para nao");
        String entrada = scanner.nextLine();
        if (!entrada.isEmpty()){
            char caractere = entrada.charAt(0);

            if (caractere == ' '){
                System.out.println("Sim");
            }else {
                System.out.println("Nao");
            }
        }else {
            System.out.println("Nao digiou nada");
        }
    }
}
