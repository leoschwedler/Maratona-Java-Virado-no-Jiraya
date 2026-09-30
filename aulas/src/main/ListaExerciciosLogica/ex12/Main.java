package ListaExerciciosLogica.ex12;

public class Main {
    public static void main(String[] args) {
        calcuculo(Opcoes.PARCELADOCARTAOTRESVEZES, 1000);
    }


    public static void calcuculo(Opcoes opcao, double valor){
        double desconto;

        switch (opcao){
            case AVISTADINHEIRO:
                desconto = valor * 0.15;
                System.out.println("Recebeu um desconto de: " + desconto + " Valor com desconto: " + (valor - desconto));
                break;
            case AVISTACARTAOCREDITO:
                desconto = valor * 0.10;
                System.out.println("Recebeu um desconto de: " + desconto + " Valor com desconto: " + (valor - desconto));
                break;
            case PARCELADOCARTAODUASVEZES:
                System.out.println("Voce vai pagar o valor de: " + valor + " em 2 vezes de: " + (valor /2));
                break;
            case PARCELADOCARTAOTRESVEZES:
                double juro = valor * 0.10;
                double resultado = (valor + juro) / 3;
                System.out.println("Voce vai pagar o valor de: " + valor + " em 3 vezes de: " + resultado);
                break;
            default:
        }
    }
}
