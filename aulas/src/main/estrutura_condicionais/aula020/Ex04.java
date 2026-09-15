package estrutura_condicionais.aula020;

public class Ex04 {
    public static void main(String[] args) {
        int idade = 25;

        boolean autorizado;

        if(idade >= 18){
            autorizado = true;
        }else {
            autorizado = false;
        }

        if(autorizado){
            System.out.println("Compra autorizada");
        }
    }
}
