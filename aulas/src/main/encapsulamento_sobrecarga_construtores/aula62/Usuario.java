package encapsulamento_sobrecarga_construtores.aula62;

public class Usuario {
    private static String nome;

    public void imprimeNome(){
        System.out.println(this.nome);
    }

    public static void mostrarMensagem(){
        System.out.println(Usuario.nome);
    }
}
