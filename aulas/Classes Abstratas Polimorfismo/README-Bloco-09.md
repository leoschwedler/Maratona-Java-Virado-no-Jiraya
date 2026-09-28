# Bloco 09 — Classes Abstratas, Interfaces e Polimorfismo

## Aulas

- 84 — Classes abstratas pt 01
- 85 — Classes abstratas pt 02 — Métodos abstratos
- 86 — Classes abstratas pt 03 — Regras de métodos abstratos
- 87 — Interfaces pt 01 — Introduction
- 88 — Interfaces pt 02 — Implementing multiple interfaces
- 89 — Interfaces pt 03 — Static attributes and methods
- 90 — Polymorphism pt 01 — Introduction
- 91 — Polymorphism pt 02 — Operation
- 92 — Polymorphism pt 03 — Polymorphic Parameters
- 93 — Polimorfismo pt 04 — Cast e instanceof
- 94 — Polymorphism pt 05 — Interface-Oriented Programming

> Estude uma aula por vez. Leia a explicação, faça os 3 exercícios da aula,
> envie seu código para correção e só depois avance.

> **Regra do bloco:** no final existe um **Desafio Integrador obrigatório**
> que reúne classes abstratas, métodos abstratos, interfaces, múltiplas
> interfaces, `static`, polimorfismo, parâmetros polimórficos, cast,
> `instanceof` e programação orientada a interfaces.

---

# 1. Visão geral

Este bloco é uma continuação direta de:

```text
Herança
   ↓
Sobrescrita
   ↓
Classes abstratas
   ↓
Interfaces
   ↓
Polimorfismo
   ↓
Cast / instanceof
   ↓
Programação orientada a interfaces
```

A ideia central do bloco é aprender a escrever código em que uma parte do
programa conhece apenas uma **abstração**, enquanto os objetos concretos
fornecem o comportamento.

Exemplo conceitual:

```text
Produto
 ├── Computador
 ├── Tomate
 └── Televisão
```

Em vez de criar uma lógica diferente para cada classe, podemos trabalhar
com uma referência mais genérica:

```java
Produto produto = new Computador(...);
```

Depois:

```java
Produto produto = new Tomate(...);
```

O código que recebe `Produto` consegue trabalhar com objetos diferentes,
desde que eles respeitem a mesma abstração.

Isso é a base do polimorfismo apresentado nas aulas 90–94.

---

# Aula 84 — Classes abstratas pt 01

## 84.1 Qual problema uma classe abstrata resolve?

Imagine uma empresa:

```text
Funcionário
├── Desenvolvedor
├── Gerente
└── Supervisor
```

Todos são funcionários, então faz sentido compartilhar informações como:

```text
nome
salário
```

Mas existe uma questão:

> Faz sentido criar diretamente um objeto `Funcionario`?

No exemplo da aula, a resposta é não. O que existe no sistema são
funcionários com cargos específicos.

Uma classe `Funcionario` pode servir como um **modelo/base** para as
outras classes, sem representar um objeto que deveria ser criado
diretamente.

---

## 84.2 Declarando uma classe abstrata

A classe recebe `abstract`:

```java
public abstract class Funcionario {

    private String nome;
    private double salario;

}
```

Agora:

```java
new Funcionario(...)
```

não pode ser usado para criar diretamente um objeto dessa classe.

A classe abstrata existe para ser estendida.

```text
Funcionario
   ↑
   |
   +-- Desenvolvedor
   |
   +-- Gerente
```

---

## 84.3 Classe abstrata como um modelo

Pense nela como uma estrutura comum:

```text
Funcionario
    |
    +-- nome
    +-- salario
    +-- comportamentos comuns
```

As subclasses aproveitam essa estrutura:

```text
Desenvolvedor
Gerente
Supervisor
```

A classe abstrata pode possuir atributos, construtores e métodos
concretos. O fato de ser abstrata não significa que tudo dentro dela
precisa ser abstrato.

---

## 84.4 O que você precisa entender

```text
classe normal
    ↓
pode ser instanciada

classe abstrata
    ↓
não pode ser instanciada
    ↓
serve como base para subclasses
```

A aula apresenta a classe abstrata principalmente como uma decisão de
modelagem: evitar a criação de objetos que representam apenas um conceito
genérico.

---

# Aula 85 — Classes abstratas pt 02 — Métodos abstratos

A aula 84 resolveu um problema:

```text
não quero permitir new Funcionario(...)
```

Agora aparece outro:

> Quero obrigar cada tipo de funcionário a implementar uma determinada
> operação.

Imagine o cálculo de bônus.

Cada funcionário pode possuir uma regra diferente.

---

## 85.1 Uma solução ruim

Poderíamos colocar um comportamento padrão na classe:

```java
public double calcularBonus() {
    return salario * 0.10;
}
```

Mas talvez o gerente precise de 5%, enquanto outra classe precise de
outra regra.

Então a classe base não deveria decidir uma implementação concreta.

---

## 85.2 Método abstrato

Podemos declarar:

```java
public abstract double calcularBonus();
```

Observe que não existe corpo:

```java
public abstract double calcularBonus();
```

e não:

```java
public abstract double calcularBonus() {
    ...
}
```

O método abstrato representa uma obrigação:

> Toda classe concreta que herdar essa estrutura precisa fornecer uma
> implementação.

---

## 85.3 A subclasse implementa

Por exemplo:

```java
public class Gerente extends Funcionario {

    @Override
    public double calcularBonus() {
        return getSalario() * 0.05;
    }

}
```

Outra classe pode fazer:

```java
public class Desenvolvedor extends Funcionario {

    @Override
    public double calcularBonus() {
        return getSalario() * 0.10;
    }

}
```

A classe abstrata define **o que precisa existir**.

A subclasse define **como funciona**.

---

## 85.4 Essa é uma ideia muito importante

```text
classe abstrata
      ↓
define uma obrigação
      ↓
subclasses concretas
      ↓
fornecem as implementações
```

Isso combina diretamente com sobrescrita.

---

# Aula 86 — Classes abstratas pt 03 — Regras dos métodos abstratos

Agora o foco é entender as regras.

## 86.1 Método abstrato não possui corpo

Correto:

```java
public abstract void imprimir();
```

Incorreto:

```java
public abstract void imprimir() {
}
```

O `abstract` informa que a implementação será fornecida posteriormente.

---

## 86.2 Uma classe concreta precisa implementar os métodos abstratos

Se:

```java
public abstract class Funcionario {

    public abstract void imprimir();

}
```

e:

```java
public class Gerente extends Funcionario {
}
```

`Gerente` precisará implementar `imprimir()` para ser uma classe concreta.

Caso contrário, ela também precisará ser abstrata.

```text
Funcionario
abstract
   ↓
Gerente
não implementou
   ↓
precisa continuar abstract
```

---

## 86.3 Classe abstrata pode possuir método concreto

Isto é permitido:

```java
public abstract class Funcionario {

    public void mostrarNome() {
        System.out.println("Funcionário");
    }

    public abstract void imprimir();

}
```

Portanto:

```text
classe abstrata
├── pode ter métodos concretos
└── pode ter métodos abstratos
```

Não confunda:

```text
classe abstrata ≠ classe formada somente por métodos abstratos
```

---

# Aula 87 — Interfaces pt 01 — Introduction

Agora começa uma mudança importante de modelagem.

## 87.1 O que é uma interface?

A aula apresenta a interface como um **contrato**.

Uma interface descreve algo que uma classe deve saber fazer.

Exemplo conceitual:

```text
Imprimivel
    ↓
"quem implementar precisa fornecer a operação definida pelo contrato"
```

Declaramos:

```java
public interface Imprimivel {

    void imprimir();

}
```

A classe implementa:

```java
public class Relatorio implements Imprimivel {

    @Override
    public void imprimir() {
        System.out.println("Imprimindo relatório");
    }

}
```

---

## 87.2 `implements`

Com herança de classe usamos:

```java
extends
```

Com interface usamos:

```java
implements
```

Então:

```java
public class Relatorio implements Imprimivel
```

significa:

```text
Relatorio
   ↓
assume o contrato Imprimivel
```

---

## 87.3 Interface não é objeto

Você não cria:

```java
new Imprimivel()
```

A interface representa o contrato.

Quem é instanciado é uma classe concreta:

```java
new Relatorio()
```

---

## 87.4 Interface e abstração

Uma forma de visualizar:

```text
interface
    ↓
define um contrato

classe
    ↓
implementa o contrato
```

A interface é especialmente útil quando classes diferentes precisam
oferecer uma mesma capacidade, mesmo que não pertençam à mesma hierarquia
de classes.

---

# Aula 88 — Interfaces pt 02 — Implementando múltiplas interfaces

Uma das vantagens destacadas é que uma classe pode implementar várias
interfaces.

Exemplo:

```java
public interface Imprimivel {
    void imprimir();
}
```

e:

```java
public interface Digitalizavel {
    void digitalizar();
}
```

Uma classe pode fazer:

```java
public class Documento implements Imprimivel, Digitalizavel {

    @Override
    public void imprimir() {
        System.out.println("Imprimindo");
    }

    @Override
    public void digitalizar() {
        System.out.println("Digitalizando");
    }
}
```

Visualmente:

```text
           Documento
           /       \
          /         \
Imprimivel         Digitalizavel
```

A classe assume os dois contratos.

---

## 88.1 Por que isso é importante?

Uma classe pode possuir diferentes capacidades:

```text
Documento
├── pode imprimir
└── pode digitalizar
```

Interfaces permitem representar essas capacidades separadamente.

Isso é diferente de tentar colocar tudo em uma única superclasse.

---

## 88.2 Várias interfaces

O ponto da aula é que não existe a mesma limitação de herança de classes
quando se trata de implementar interfaces.

Uma classe pode implementar múltiplas interfaces:

```java
class MinhaClasse implements A, B, C {
}
```

Ela terá que cumprir os contratos exigidos por elas.

---

# Aula 89 — Interfaces pt 03 — Atributos e métodos `static`

Esta aula entra em características importantes das interfaces.

## 89.1 Atributos de interface são constantes

Os atributos declarados em uma interface possuem o comportamento de
constantes:

```java
public interface Configuracao {

    int LIMITE = 10;

}
```

A ideia é trabalhar com valores associados ao contrato e que não são
alterados por objetos.

Não pense nisso como um atributo de instância de cada objeto.

---

## 89.2 Métodos da interface

Os métodos de contrato são públicos por padrão no modelo apresentado.

A interface define aquilo que deve ser disponibilizado pela implementação.

---

## 89.3 Método `static`

A interface também pode possuir método `static`:

```java
public interface Calculadora {

    static int somar(int a, int b) {
        return a + b;
    }

}
```

Esse método pertence à própria interface.

A chamada é feita através dela:

```java
Calculadora.somar(10, 20);
```

A ideia é diferente de um método de instância implementado por uma
classe.

---

## 89.4 Interface como contrato

A aula também prepara o terreno para programação orientada a interfaces:

```text
interface
    ↓
contrato
    ↓
classes implementam
    ↓
código pode depender do contrato
```

Isso será explorado com mais força nas aulas de polimorfismo.

---

# Aula 90 — Polymorphism pt 01 — Introduction

Agora começa um dos conceitos mais importantes do bloco.

## 90.1 O que significa polimorfismo?

A ideia apresentada é trabalhar com uma referência mais genérica enquanto
o objeto concreto é mais específico.

Por exemplo:

```java
Produto produto = new Computador();
```

Aqui temos duas coisas diferentes:

```text
tipo da referência → Produto

tipo real do objeto → Computador
```

A referência é mais genérica.

O objeto é específico.

---

## 90.2 Outro exemplo

Podemos ter:

```java
Produto produto = new Tomate();
```

ou:

```java
Produto produto = new Computador();
```

O código que trabalha com `Produto` pode lidar com objetos diferentes.

```text
Produto
   ↑
   |
   +--- Computador
   |
   +--- Tomate
   |
   +--- Televisão
```

---

## 90.3 Por que isso é útil?

A aula utiliza um cenário de cálculo de impostos.

Em vez de criar:

```text
calcularImpostoComputador()
calcularImpostoTomate()
calcularImpostoTelevisao()
```

podemos ter uma operação mais genérica que recebe `Produto`.

O objeto concreto é quem fornece o comportamento sobrescrito.

---

# Aula 91 — Polymorphism pt 02 — Operation

Agora o foco é observar o polimorfismo acontecendo na execução.

Imagine:

```java
Produto produto = new Computador();
```

e:

```java
Produto produto = new Tomate();
```

Mesmo que a variável seja declarada como `Produto`, a chamada de um
método sobrescrito considera o objeto que realmente está sendo utilizado.

Por exemplo:

```java
produto.calcularImposto();
```

Se o objeto real for `Computador`, executa a implementação de
`Computador`.

Se for `Tomate`, executa a implementação de `Tomate`.

---

## 91.1 A referência não determina sozinha o comportamento

Pense:

```text
Produto produto = new Computador();
       ↑              ↑
 referência        objeto real
```

Quando ocorre uma chamada sobrescrita:

```java
produto.calcularImposto();
```

o Java considera o objeto real:

```text
Computador
    ↓
calcularImposto()
```

Isso é o polimorfismo em operação.

---

## 91.2 O ganho

A lógica externa pode trabalhar com:

```java
Produto
```

sem precisar saber antecipadamente se recebeu:

```text
Computador
Tomate
Televisão
```

Cada classe concreta mantém sua própria regra.

---

# Aula 92 — Polymorphism pt 03 — Polymorphic Parameters

Aqui o conceito aparece como parâmetro de método.

Em vez de:

```java
calcularImposto(Computador computador)
```

podemos ter:

```java
calcularImposto(Produto produto)
```

Agora o método pode receber diferentes subclasses:

```java
calcularImposto(computador);
calcularImposto(tomate);
calcularImposto(televisao);
```

desde que sejam `Produto`.

---

## 92.1 O método fica genérico

Imagine:

```java
public void gerarRelatorio(Produto produto) {

    produto.calcularImposto();

}
```

Esse método não precisa receber um tipo concreto.

Ele depende de `Produto`.

Isso reduz o acoplamento entre a classe que gera o relatório e as classes
concretas.

---

## 92.2 A ideia principal

```text
parâmetro genérico
       ↓
recebe objetos específicos
       ↓
polimorfismo
       ↓
cada objeto executa sua própria implementação
```

Essa ideia é fundamental para código extensível.

---

# Aula 93 — Polimorfismo pt 04 — Cast e `instanceof`

Agora aparece uma situação diferente.

Imagine:

```java
Produto produto = new Tomate();
```

A variável é `Produto`, então através dela você só consegue acessar o que
`Produto` conhece.

Mas `Tomate` possui algo específico:

```java
getDataValidade()
```

Se esse método não existir em `Produto`, isto não funciona:

```java
produto.getDataValidade();
```

---

## 93.1 Cast

Podemos fazer um cast explícito:

```java
Tomate tomate = (Tomate) produto;
```

Agora:

```java
tomate.getDataValidade();
```

é acessível.

Visualmente:

```text
Produto
   |
   | referência
   v
Tomate
```

Depois do cast:

```text
Tomate tomate = (Tomate) produto;
```

a variável `tomate` passa a enxergar a interface da classe `Tomate`.

---

## 93.2 O perigo do cast

O cast só é seguro se o objeto realmente for compatível com o tipo para
o qual estamos convertendo.

Imagine:

```java
Produto produto = new Computador();
```

Fazer:

```java
Tomate tomate = (Tomate) produto;
```

não faz sentido, porque o objeto real é um `Computador`.

O cast pode provocar `ClassCastException` em execução.

---

## 93.3 `instanceof`

Por isso podemos verificar antes:

```java
if (produto instanceof Tomate) {
    Tomate tomate = (Tomate) produto;
    // usar tomate
}
```

A lógica fica:

```text
é realmente um Tomate?
       ↓
      SIM
       ↓
pode fazer cast
       ↓
usar métodos específicos
```

---

## 93.4 A importância do objeto real

Lembre novamente:

```java
Produto produto = new Tomate();
```

O tipo da referência é:

```text
Produto
```

mas o tipo real do objeto é:

```text
Tomate
```

É essa diferença que permite o polimorfismo e também torna possível o
cast.

---

# Aula 94 — Polymorphism pt 05 — Interface-Oriented Programming

A última aula leva o conceito para uma forma de programação mais
desacoplada.

## 94.1 Em vez de depender da implementação, dependa do contrato

Imagine um:

```java
Repositorio
```

como interface.

A classe que utiliza o repositório pode depender de:

```java
Repositorio repositorio;
```

sem precisar conhecer qual implementação concreta será utilizada.

Podemos ter:

```text
Repositorio
    ↑
    |
    +--- RepositorioMemoria
    |
    +--- RepositorioBanco
```

O código consumidor trabalha com:

```text
Repositorio
```

e não precisa ficar preso a uma implementação específica.

---

## 94.2 Programação orientada a interfaces

A ideia pode ser resumida:

```text
interface
    ↓
contrato
    ↓
implementações diferentes
    ↓
polimorfismo
    ↓
código depende do contrato
```

Isso reduz o acoplamento e permite trocar implementações sem precisar
reescrever toda a classe que utiliza o serviço.

---

## 94.3 Exemplo mental

Imagine:

```java
public void salvar(Repositorio repositorio) {
    repositorio.salvar();
}
```

O método não precisa saber se recebeu:

```text
RepositorioBanco
```

ou:

```text
RepositorioMemoria
```

Ele só precisa saber:

```text
"esse objeto cumpre o contrato Repositorio"
```

Essa é uma das ideias mais importantes para levar deste bloco.

---

# 2. Relação entre os conceitos

Agora junte tudo:

```text
HERANÇA
    ↓
compartilhar estrutura
    ↓
CLASSE ABSTRATA
    ↓
impedir instanciação de um conceito genérico
    ↓
MÉTODO ABSTRATO
    ↓
obrigar subclasses a fornecer implementação
    ↓
INTERFACE
    ↓
definir contrato/capacidade
    ↓
MÚLTIPLAS INTERFACES
    ↓
uma classe pode cumprir vários contratos
    ↓
POLIMORFISMO
    ↓
referência genérica para objetos específicos
    ↓
PARÂMETRO POLIMÓRFICO
    ↓
métodos aceitam abstrações
    ↓
CAST / instanceof
    ↓
recuperar uma visão mais específica quando necessário
    ↓
PROGRAMAÇÃO ORIENTADA A INTERFACES
    ↓
código depende de contratos, não de implementações concretas
```

---

# 3. Diferença entre classe abstrata e interface

Não memorize apenas uma definição. Observe a intenção:

```text
CLASSE ABSTRATA
        ↓
representa uma base comum
        ↓
normalmente existe relação de herança
        ↓
pode possuir estado e implementação comum
```

Enquanto:

```text
INTERFACE
        ↓
representa um contrato/capacidade
        ↓
classes diferentes podem implementar
        ↓
uma classe pode implementar várias interfaces
```

Exemplo:

```text
Funcionario
├── Desenvolvedor
└── Gerente
```

é uma relação de especialização.

Enquanto:

```text
Imprimivel
Digitalizavel
```

podem representar capacidades que uma mesma classe pode possuir.

---

# 4. O que você precisa dominar ao terminar o bloco

Você deve conseguir explicar:

- por que uma classe pode ser abstrata;
- por que não podemos instanciar uma classe abstrata;
- o que é um método abstrato;
- por que um método abstrato não possui corpo;
- por que uma classe concreta precisa implementar métodos abstratos;
- o que é uma interface;
- o que significa `implements`;
- por que uma classe pode implementar múltiplas interfaces;
- o que são os membros `static` apresentados em interfaces;
- o que é polimorfismo;
- diferença entre referência e objeto real;
- como funciona um parâmetro polimórfico;
- por que um cast pode ser necessário;
- por que um cast pode falhar;
- para que serve `instanceof`;
- o que significa programação orientada a interfaces.

---

# 5. Fluxo de estudo

```text
Aula 84
Classes abstratas
       ↓
3 exercícios
       ↓
correção

Aula 85
Métodos abstratos
       ↓
3 exercícios
       ↓
correção

...

Aula 94
Programação orientada a interfaces
       ↓
3 exercícios
       ↓
correção

DESAFIO INTEGRADOR
       ↓
todo o conteúdo do bloco
```

O desafio final não será uma repetição dos exercícios. Ele exigirá que
você decida a modelagem e combine os conceitos em um sistema maior.
