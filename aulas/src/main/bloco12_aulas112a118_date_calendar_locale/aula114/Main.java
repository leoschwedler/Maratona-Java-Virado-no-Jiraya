package bloco12_aulas112a118_date_calendar_locale.aula114;

import java.text.DateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Date date = new Date();
        //DATA
        System.out.println(DateFormat.getDateInstance().format(date));
        //HORA
        System.out.println(DateFormat.getTimeInstance().format(date));
        // DATA E HORA
        System.out.println(DateFormat.getDateTimeInstance().format(date));
    }
}
