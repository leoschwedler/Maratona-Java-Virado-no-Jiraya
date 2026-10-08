package bloco11_aulas106a111_wrappers_strings.aula108;

public class Main {
    public static void main(String[] args) {

//        String nome1 = "Java";
//        String nome2 = "Java";
//
//        if (nome1.equals(nome2)) {
//            System.out.println("EQUALS");
//        }
//        if (nome1 == nome2) {
//            System.out.println("==");
//        }

//        // EX2
//        String texto = "Java";
//        String novoTexto = texto.concat(" è muito chato").concat(" Misericordia").concat(" Quero ir embora");
//        System.out.println(texto);
//        System.out.println(novoTexto);

        // EX3

        String a = "Javali";
        String b = "Java";
        String c = new String("Javali");

        String d = c.intern();

        if (a == d){
            System.out.println("sim");
        }
    }
}
