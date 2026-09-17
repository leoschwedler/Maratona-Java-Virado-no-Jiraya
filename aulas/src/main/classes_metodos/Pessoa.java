package classes_metodos;

public class Pessoa {
    private String nome;
    private int age;
    private char sexo;

    public Pessoa(String nome, int age, char sexo) {
        this.nome = nome;
        this.age = age;
        this.sexo = sexo;
    }

    public Pessoa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
