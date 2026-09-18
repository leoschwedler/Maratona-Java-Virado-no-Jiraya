package encapsulamento_sobrecarga_construtores.aula54;

public class Usuario {
    private String nome;
    private String email;
    private int idade;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if(idade <= 0 || idade > 150){
            System.out.println("Coloque uma idade pertimida");
            return;
        }
        System.out.println("Nova idade recebida: " + idade);
        this.idade = idade;
    }
}
