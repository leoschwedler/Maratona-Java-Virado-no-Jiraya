package estrutura_repeticao;

public class Ex10 {
    public static void main(String[] args) {
        int acumulador = 0;

        for (int i = 1; i < 100; i++) {
            acumulador += i;
            if (acumulador > 500){
                System.out.println("Finalizando o Laco");
                break;
            }

            System.out.println(acumulador);
        }
    }
}
