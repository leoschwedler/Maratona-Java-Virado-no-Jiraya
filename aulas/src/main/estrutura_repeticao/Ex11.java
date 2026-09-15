package estrutura_repeticao;

public class Ex11 {
    public static void main(String[] args) {
        double valor = 30000;

        for (int i = 1; i < 100; i++) {
           double resultado = valor / i;
            System.out.println("Numero de parcelas: " + i);
            System.out.println("Valor da parcela: " + resultado);
           if (resultado < 1000){
               System.out.println("Acabou o laco");
               break;
           }
        }
    }
}
