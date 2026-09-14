package aula020;

public class Desafio {
    public static void main(String[] args) {
        int idade = 20;
        boolean autorizado;

        if (idade >= 18){
            autorizado = true;
        }else {
            autorizado = false;
        }

        if(autorizado){
            System.out.println("Usuario Autorizado");
        }else {
            System.out.println("Usuari nao autorizado");
        }

    }
}
