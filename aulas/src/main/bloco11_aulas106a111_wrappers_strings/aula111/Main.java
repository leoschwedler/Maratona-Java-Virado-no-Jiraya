package bloco11_aulas106a111_wrappers_strings.aula111;

public class Main {
    public static void main(String[] args) {

        // Ex01
//        StringBuilder stringBuilder = new StringBuilder();
//        String string  = stringBuilder.append("Meu nome é ")
//                .append("Leonardo e eu ")
//                .append("estou estudando java")
//                .toString();
//        System.out.println(string);

        // Ex02
        StringBuilder sb = new StringBuilder("0123456789");
        sb.append("JAVA").reverse();
        System.out.println(sb);
        sb.delete(0,4);
        System.out.println(sb);

    }
}
