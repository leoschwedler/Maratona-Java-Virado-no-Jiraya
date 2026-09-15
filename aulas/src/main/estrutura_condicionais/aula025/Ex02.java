package estrutura_condicionais.aula025;

public class Ex02 {
    public static void main(String[] args) {
        int opcao = 4;

        switch (opcao){
            case 1:
                System.out.println("Cadastrar");
            break;
            case 2:
                System.out.println("Consultar");
                break;
            case 3:
                System.out.println("Atualizar");
                break;
            case 4:
                System.out.println("Excluir");
                break;
            default:
                System.out.println("Opção inválida");
        }
    }
}
