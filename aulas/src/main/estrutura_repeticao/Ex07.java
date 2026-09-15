package estrutura_repeticao;

public class Ex07 {
    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            System.out.println(i);
            if (i == 37){
                System.out.println("Finalizando laço");
                break;
            }
        }
    }
}
