package bloco12_aulas112a118_date_calendar_locale.aula112;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        // Ex01
//        Date date1 = new Date();
//        System.out.println(date1);
//        Date date2 = new Date(0);
//        System.out.println(date2);
//        Date date3 = new Date(86_400_000L);
//        System.out.println(date3);
//        Date date4 = new Date(System.currentTimeMillis());
//        System.out.println(date4);

        // Ex02
        Date date = new Date();
        Date horaAgora = somarHoras(date, 2);
        System.out.println(horaAgora);

    }

    public static Date somarHoras(Date data, int horas) {
        Date date = new Date();
        data.setTime(data.getTime() + horas);
        return data;
    }
}
