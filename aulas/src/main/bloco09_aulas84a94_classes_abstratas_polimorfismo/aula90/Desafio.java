package bloco09_aulas84a94_classes_abstratas_polimorfismo.aula90;

public class Desafio {
    Produto produto;

    @Override
    public String toString() {
        return "Desafio{" +
                "produto=" + produto +
                '}';
    }

    public void setProduto(Produto produto) {
        if(produto instanceof Computador){
            System.out.println("Computador");
        }else if (produto instanceof Livro){
            System.out.println("Livro");
        }else if(produto instanceof  Alimento){
            System.out.println("Alimento");
        }else {
            System.out.println("Nao é de nenhuma classe cadastrada");
        }


        this.produto = produto;
    }
}
