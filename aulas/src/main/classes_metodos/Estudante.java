package classes_metodos;

public class Estudante {
    private String nome;
    private int idade;
    private char sexo;
    private double[] notas;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public double[] getNota() {
        return notas;
    }

    public void setNota(double[] nota) {
        this.notas = nota;
    }

    public Estudante(String nome, int idade, char sexo, double[] nota) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.notas = nota;

    }

    public Estudante() {
    }

    public void imprimirNome(){
        System.out.println(this.nome);
    }

    public void calcularMedia(){
        double quantidade = notas.length;
        double contador = 0;
        for (double nota : notas){
            contador += nota;
        }
        System.out.println(contador / quantidade);
    }

    public void maiorNota(){
        int maiorNota = 0;
        for (double nota : this.notas){
            if(nota > maiorNota){
                maiorNota = (int) nota;
            }
        }
        System.out.println("Maior Nota: " + maiorNota);
    }
}
