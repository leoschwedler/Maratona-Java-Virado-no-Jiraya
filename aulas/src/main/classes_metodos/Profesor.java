package classes_metodos;

public class Profesor {
    private String name;
    private int age;
    private char sexo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public Profesor(String name, int age, char sexo) {
        this.name = name;
        this.age = age;
        this.sexo = sexo;
    }

    public Profesor() {
    }
}
