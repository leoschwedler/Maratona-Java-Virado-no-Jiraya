package bloco10_aulas95a105_excecoes;

public class Main {

    public static void main(String[] args) {
       try {
           multiplicar(1,0);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
        System.out.println("Deu certo");
    }


    private static void multiplicar(int a, int b){

            if (b == 0){
                throw new RuntimeException("Erro");
            }
        System.out.println(a / b);
    }
}
