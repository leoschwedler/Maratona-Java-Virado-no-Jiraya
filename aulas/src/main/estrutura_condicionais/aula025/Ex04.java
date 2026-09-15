package estrutura_condicionais.aula025;

public class Ex04 {
    public static void main(String[] args) {
        double numero1 = 10;
        double numero2 = 15;
        char operador = '-';
        double resultado = 0;

        switch (operador){
            case '+':
                resultado = numero1 + numero2;
                break;
            case '-':
                resultado = numero1 - numero2;
                break;
            case '*':
                resultado = numero1 * numero2;
                break;
            case '/':
                resultado = numero1 / numero2;
                break;
            default:
                System.out.println("Opcao Invalida");
        }
        System.out.println("Resultado: " + resultado);
    }
}
