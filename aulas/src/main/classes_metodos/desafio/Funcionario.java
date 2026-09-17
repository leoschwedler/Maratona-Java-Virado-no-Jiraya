package classes_metodos.desafio;

import java.util.ArrayList;

public class Funcionario {
    private String nome;
    private int age;
    private ArrayList<Double> salarios;

    public Funcionario(String nome, int age, ArrayList<Double> salarios) {
        this.nome = nome;
        this.age = age;
        this.salarios = salarios;
    }


    public void imprimirDados(){
        System.out.println("Nome: " + this.nome);
        System.out.println("Idade: " + this.age);
        System.out.println("Salarios: " + this.salarios);
    }

    public void calcularMedia(){
        int quantidade = this.salarios.size();
        double somador = 0;
        for (Double salario : this.salarios){
            somador += salario;
        }
        System.out.println(somador / quantidade);
    }

    public  void maiorSalario(){
        double maiorSalario = 0;
        for (Double salario : this.salarios){
            if (salario > maiorSalario){
                maiorSalario = salario;
            }
        }
        System.out.println("Maior Salario");
    }

    public  void menorSalario(){
        double maiorSalario = 0;
        for (Double salario : this.salarios){
            if (salario < maiorSalario){
                maiorSalario = salario;
            }
        }
        System.out.println("Menor Salario");
    }

    public void imprimirNovoFuncionario(Funcionario funcionario){
        System.out.println("Novo Funcionario: " + funcionario.nome);
    }



}
