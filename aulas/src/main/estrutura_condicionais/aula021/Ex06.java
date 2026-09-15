package estrutura_condicionais.aula021;

public class Ex06 {
    public static void main(String[] args) {
        double temperatura = 25;
        
        if(temperatura < 10){
            System.out.println("Muito frio");
        } else if (temperatura >= 10 && temperatura < 20) {
            System.out.println("Frio");
        } else if (temperatura >= 20 && temperatura < 30) {
            System.out.println("Agradável");
        }else  {
            System.out.println("Muito quente");
        }
    }
}
