package bloco12_aulas112a118_date_calendar_locale.aula113;

import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        calculaEntrega( Calendar.getInstance(),100);
    }

    public static void calculaEntrega(Calendar diaDaCompra, int dias) {

        int diaDaSemana = diaDaCompra.get(Calendar.DAY_OF_WEEK);

        while (dias > 0) {
            diaDaCompra.add(Calendar.DAY_OF_MONTH, 1);

            if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY) {
                dias--;
            }
        }
        System.out.println("Dia: " + diaDaSemana);
    }
}
