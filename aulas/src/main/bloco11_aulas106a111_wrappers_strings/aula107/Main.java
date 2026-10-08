package bloco11_aulas106a111_wrappers_strings.aula107;

public class Main {

    public static void main(String[] args) {

        int numero = 100;

        Integer wrapper = numero;
        int primitivo = wrapper;


        String quantidade = "25";
        Integer parseInt = Integer.parseInt(quantidade);
        String preco = "49.90";
        Double parseDouble = Double.parseDouble(preco);
        String ativo = "true";
        Boolean parseBoolean = Boolean.parseBoolean(ativo);


    }
}
