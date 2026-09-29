package ListaExercicios.ex03;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.soma(2,2);
    }


    public void soma(int a, int b){
        if (a == b){
            System.out.println("Valores iguais");
            System.out.println("Resultado: " + (a + b));
            return;
        }
        System.out.println("Os valores nao sao iguais");
    }
}
