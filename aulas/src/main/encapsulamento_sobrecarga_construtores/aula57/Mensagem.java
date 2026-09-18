package encapsulamento_sobrecarga_construtores.aula57;

public class Mensagem {

    public void enviar(String texto){
        System.out.println(texto);
    }

    public void enviar(String texto, String destinario){
        System.out.println(texto + " " + destinario);
    }

    public void enviar(String texto, String destinario, int prioridade){
        System.out.println(texto + " " + destinario + " " + prioridade);
    }
}
