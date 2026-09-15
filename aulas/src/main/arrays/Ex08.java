package arrays;

import java.util.Arrays;

public class Ex08 {
    public static void main(String[] args) {
        String[] array = {"leo","leo1","leo2", "leo3","leo4"};
        Arrays.stream(array).forEach(nome -> System.out.println(nome));
    }
}
