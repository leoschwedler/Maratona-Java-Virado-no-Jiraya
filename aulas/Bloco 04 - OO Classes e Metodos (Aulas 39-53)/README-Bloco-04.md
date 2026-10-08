# Bloco 04 — Orientação a Objetos: Classes e Métodos

## Aulas
- 39 — Object Orientation pt 01 — Introduction to classes pt 01
- 40 — Object Orientation — Introduction to classes pt 02
- 41 — Orientação Objetos — Coesão
- 42 — Orientação Objetos — Exercício Classes
- 43 — Orientação Objetos — Referência de objetos
- 44 — Orientação Objetos — Métodos pt 01
- 45 — Orientação Objetos — Métodos pt 02 — Parâmetros
- 46 — Object Orientation — Methods pt 03 — Return pt 01
- 47 — Orientação Objetos — Métodos pt 04 — Retorno pt 02
- 48 — Object Orientation — Methods pt 05 — Primitive type parameters
- 49 — Object Orientation — Methods pt 06 — Reference type parameters
- 50 — Object Orientation — Methods pt 07 — Reference type parameters pt 02
- 51 — Orientação Objetos — Métodos pt 08 — Referência this
- 52 — Object Orientation — Methods pt 09 — Varargs
- 53 — Object Orientation — Methods pt 10 — Exercise

> Este bloco reúne as aulas 39–53 porque elas constroem uma sequência única: primeiro classes e objetos, depois referência de objetos e, em seguida, métodos e passagem de parâmetros.

---

## 1. O que é uma classe?

Uma classe funciona como uma definição/modelo para representar algo.

Exemplo:

```java
public class Estudante {
    String nome;
    int idade;
    char sexo;
}
```

A classe `Aluno` define quais informações um estudante terá.

## 2. O que é um objeto?

O objeto é criado a partir da classe.

```java
Estudante estudante = new Estudante();
```

Aqui:

- `Aluno` → tipo da variável
- `estudante` → variável de referência
- `new Estudante()` → criação do objeto

O objeto possui os atributos definidos pela classe.

```java
estudante.nome = "João";
estudante.idade = 20;
estudante.sexo = 'M';
```

## 3. Atributos

Atributos representam os dados/características do objeto.

```java
public class Carro {
    String nome;
    String modelo;
    int ano;
}
```

Um `Carro` possui `nome`, `modelo` e `ano`.

## 4. Classe x objeto

Pense assim:

```text
Classe
  ↓
define a estrutura

Objeto
  ↓
é uma instância criada a partir dessa estrutura
```

Uma classe pode gerar vários objetos:

```java
Carro carro1 = new Carro();
Carro carro2 = new Carro();
```

Os dois são objetos diferentes e podem possuir valores diferentes.

---

## 5. Coesão

A aula 41 apresenta coesão como algo relacionado ao **propósito da classe**.

Uma classe deve ter responsabilidades relacionadas ao propósito para o qual ela existe.

Exemplo ruim:

```java
public class Estudante {
    String nome;
    int idade;

    String nomeProfessor;
    int idadeProfessor;
}
```

Aqui estamos misturando responsabilidades de estudante e professor.

Melhor:

```java
public class Estudante {
    String nome;
    int idade;
}
```

```java
public class Professor {
    String nome;
    int idade;
}
```

A ideia é manter as classes com responsabilidades relacionadas.

---

## 6. Referência de objetos

Uma variável de uma classe é uma **variável de referência**.

```java
Carro carro1 = new Carro();
Carro carro2 = new Carro();
```

As variáveis fazem referência aos objetos criados.

Uma referência pode passar a apontar para outro objeto:

```java
carro1 = carro2;
```

Nesse caso, `carro1` passa a referenciar o mesmo objeto que `carro2`.

Isso é importante porque duas referências podem apontar para o mesmo objeto.

---

## 7. Métodos

Métodos representam comportamentos/operações que podem ser executados.

Exemplo:

```java
public void imprime() {
    System.out.println("Executando método");
}
```

Para chamar:

```java
objeto.imprime();
```

A diferença básica:

```java
objeto.nome = "Leo";  // atributo
objeto.imprime();     // método
```

Atributos representam dados. Métodos representam comportamentos.

---

## 8. `void`

Quando um método não retorna um valor, usamos `void`.

```java
public void imprime() {
    System.out.println("Olá");
}
```

O método pode produzir uma saída no console e ainda assim não retornar um valor para quem o chamou.

---

## 9. Parâmetros

Um método pode receber informações.

```java
public void multiplica(int numero1, int numero2) {
    System.out.println(numero1 * numero2);
}
```

Na chamada:

```java
calculadora.multiplica(10, 20);
```

- `numero1` e `numero2` são **parâmetros**
- `10` e `20` são **argumentos**

Regra mental:

```text
Declaração do método → parâmetro
Chamada do método    → argumento
```

Um parâmetro possui um tipo e um identificador:

```java
int numero
```

---

## 10. Retorno

Um método pode devolver um valor.

```java
public int soma(int a, int b) {
    return a + b;
}
```

Quem chama pode armazenar o resultado:

```java
int resultado = calculadora.soma(10, 20);
```

O tipo antes do nome do método indica o tipo retornado:

```java
public int soma(...)
```

Neste caso, o método deve retornar um `int`.

### `return`

O `return` devolve o valor para quem chamou o método.

```java
return a + b;
```

O valor retornado pode ser usado em uma variável, impressão, expressão etc.

---

## 11. Parâmetros de tipos primitivos

Exemplo:

```java
public void altera(int numero) {
    numero = 100;
}
```

O parâmetro recebe o valor passado para o método.

É importante diferenciar isso da passagem de uma referência para um objeto.

---

## 12. Parâmetros de tipos de referência

Podemos passar objetos para métodos.

```java
public void imprime(Estudante estudante) {
    System.out.println(estudante.nome);
}
```

Chamada:

```java
imprime(estudante);
```

Nesse caso, o método recebe uma referência para o objeto.

Alterações feitas nos atributos do objeto dentro do método podem ser percebidas pelo objeto original.

Exemplo:

```java
public void alteraNome(Estudante estudante) {
    estudante.nome = "Gohan";
}
```

Depois da chamada, o objeto que foi passado pode estar com `nome` alterado.

---

## 13. `this`

Dentro de um método de instância, `this` representa o próprio objeto/contexto em que o método está sendo executado.

Exemplo:

```java
public class Estudante {
    String nome;

    public void imprime() {
        System.out.println(this.nome);
    }
}
```

A ideia é:

```text
estudante1.imprime()
       ↓
this = estudante1

estudante2.imprime()
       ↓
this = estudante2
```

Assim, o mesmo método pode trabalhar com os atributos do objeto que o chamou.

---

## 14. Varargs

Varargs permitem receber uma quantidade variável de argumentos.

Exemplo:

```java
public void soma(int... numeros) {
    int soma = 0;

    for (int numero : numeros) {
        soma += numero;
    }

    System.out.println(soma);
}
```

Pode ser chamado com diferentes quantidades:

```java
soma();
soma(1);
soma(1, 2);
soma(1, 2, 3, 4);
```

No método, o varargs pode ser tratado como um array.

---

## 15. Exemplo integrado

```java
public class Funcionario {
    String nome;
    int idade;
    double[] salarios;

    public void imprimeDados() {
        System.out.println(nome);
        System.out.println(idade);
    }

    public double mediaSalarios() {
        double soma = 0;

        for (double salario : salarios) {
            soma += salario;
        }

        return soma / salarios.length;
    }
}
```

Uso:

```java
Funcionario funcionario = new Funcionario();

funcionario.nome = "Leo";
funcionario.idade = 25;
funcionario.salarios = new double[]{2000, 2500, 3000};

funcionario.imprimeDados();

double media = funcionario.mediaSalarios();
System.out.println(media);
```

Esse exemplo junta classe, objeto, atributos, métodos, retorno e array.

---

## Checklist

Antes de avançar, você deve conseguir explicar:

- [ ] O que é uma classe
- [ ] O que é um objeto
- [ ] O que é uma variável de referência
- [ ] O que é um atributo
- [ ] O que é um método
- [ ] Diferença entre atributo e método
- [ ] O que significa `void`
- [ ] O que é um parâmetro
- [ ] O que é um argumento
- [ ] Como um método retorna um valor
- [ ] Diferença entre parâmetro primitivo e de referência
- [ ] O que `this` representa
- [ ] O que é varargs
- [ ] O que significa uma classe ter alta coesão
