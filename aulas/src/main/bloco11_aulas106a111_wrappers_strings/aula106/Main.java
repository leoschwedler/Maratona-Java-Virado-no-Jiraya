package bloco11_aulas106a111_wrappers_strings.aula106;

public class Main {
    public static void main(String[] args) {
        Produto produto = new Produto("Abacaxi",15,4.50,true);
        System.out.println(produto);
        imprimirNumero(new Integer(1));
        imprimirNumero(new Double(2.50));
        imprimirNumero(new Long(4));
        imprimirNumero(new Float(4));
    }


    public static void imprimirNumero(Number numero){
        System.out.println(numero);
    }



}

