package classes_metodos;

public class Calculadora {
    private int numero1;
    private int numero2;

    public int getNumero1() {
        return numero1;
    }

    public void setNumero1(int numero1) {
        this.numero1 = numero1;
    }

    public int getNumero2() {
        return numero2;
    }

    public void setNumero2(int numero2) {
        this.numero2 = numero2;
    }

    public Calculadora() {
    }

    public Calculadora(int numero1, int numero2) {
        this.numero1 = numero1;
        this.numero2 = numero2;
    }


    public void somar(){
        System.out.println(this.numero1 + this.numero2);
    }

    public void subtrair(){
        System.out.println(this.numero1 - this.numero2);
    }

    public void multiplicar(){
        System.out.println(this.numero1 * this.numero2);
    }

    public void dividir(){
        System.out.println(this.numero1 + this.numero2);
    }

    public void calcularMedia(int... numeros){
        int quantidade = numeros.length;
        int somador = 0;
        for (int numero : numeros){
            somador += numero;
        }
        System.out.println(somador / quantidade);
    }
}
