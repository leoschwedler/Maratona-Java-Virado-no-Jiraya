package ListaExerciciosBatismoJava.ex02;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        Ninja[] ninjas = new Ninja[5];
        boolean parar = false;

        while (!parar){
            System.out.println("\n===== Menu Ninja =====");
            System.out.println("1. Cadastrar Ninja");
            System.out.println("2. Listar Ninjas");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            int resposta = entrada.nextInt();
            entrada.nextLine();



            switch (resposta){
                case 1:

                    System.out.println("Digite o nome do ninja:");
                    String nome = entrada.nextLine(); // Lê uma linha de texto

                    for (int i = 0; i < ninjas.length; i++) {
                        if (ninjas[i] == null){
                            ninjas[i] = new Ninja(nome);
                            break;
                        }
                    }
                 break;
                case 2:
                    for (int i = 0; i < ninjas.length; i++) {
                        if (ninjas[i] != null){
                            System.out.println("Ninja: " + i + " nome: " + ninjas[i].getNome());

                        }else {
                            break;
                        }
                    }
                    break;
                case 3:
                    parar = true;
                    break;
                default:
                    System.out.println("Escolha uma opcao valida!");
            }
        }
    }
}
