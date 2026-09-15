package estrutura_condicionais.aula021;

public class Ex08 {
    public static void main(String[] args) {
        int idade = 17;
        boolean possuiDocumento = true;

        if (idade >= 18){
            System.out.println("Documento Necessario");
            if (possuiDocumento){
                System.out.println("Entrada autorizada");
            }else {
                System.out.println("Entrada não autorizada");
            }
        }else {
            System.out.println("Entrada não autorizada");
        }
    }
}
