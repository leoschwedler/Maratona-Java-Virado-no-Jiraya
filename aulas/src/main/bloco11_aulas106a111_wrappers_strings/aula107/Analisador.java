package bloco11_aulas106a111_wrappers_strings.aula107;

public class Analisador {

    public static void main(String[] args) {
        analisar("Leozinho15");
    }

    public static void analisar(String caracteres){
        int digitos = 0;
        int letras = 0;
        int maiuscula = 0;
        int minuscula = 0;
        for (int i = 0; i < caracteres.length(); i++) {
            Character letra = caracteres.charAt(i);
            System.out.println(letra);
            if (Character.isDigit(letra)){
                digitos++;
            }else {
                letras++;
            }
            if(Character.isAlphabetic(letra)){
                if (Character.isLowerCase(letra)){
                    minuscula++;
                }else {
                    maiuscula++;
                }
            }else {
                System.out.println("Nao é uma letra");
            }


        }

        System.out.println("===================");
        System.out.println("O Texto: " + caracteres + " tem " + digitos + " digitos e " + letras + " letras");
        System.out.println("E o Texto: " + caracteres + " tem " + minuscula + " Letras minusculas e " + maiuscula + " Letras Maiusculas");
    }
}
