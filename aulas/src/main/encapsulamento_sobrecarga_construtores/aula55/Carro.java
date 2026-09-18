package encapsulamento_sobrecarga_construtores.aula55;

public class Carro {
    private String modelo;

    private int velocidade;

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getModelo() {
        return modelo;
    }

    public int getVelocidade() {
        return velocidade;
    }

   public void acelerar(){
        this.velocidade += 10;
   }

    public void frear(){
        if(this.velocidade < 0){
            System.out.println("A velocidade nao pode Ficar negativa");
            return;
        }
        this.velocidade -= 10;
    }
}
