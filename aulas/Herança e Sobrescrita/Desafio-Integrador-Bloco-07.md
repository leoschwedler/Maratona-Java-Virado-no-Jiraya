# Desafio Integrador --- Bloco 07

## Sistema de Funcionários

Este desafio fecha as aulas 71--76 e deve envolver tudo que foi
estudado:

-   herança;
-   `extends`;
-   `super`;
-   `protected`;
-   construtores e `super(...)`;
-   sequência de inicialização;
-   blocos `static`;
-   blocos de inicialização;
-   sobrescrita;
-   `@Override`;
-   `toString()`;
-   associação;
-   referências;
-   array de objetos.

### Cenário

Crie um sistema de funcionários com uma hierarquia escolhida por você.

Você deverá decidir:

-   qual será a superclasse;
-   quais serão as subclasses;
-   quais atributos pertencem à classe base;
-   quais atributos são específicos;
-   qual relação será herança;
-   qual relação será associação.

### Requisitos

1.  Tenha pelo menos 3 níveis em uma parte da hierarquia.
2.  Use `protected` de forma consciente.
3.  Use `private` onde o encapsulamento fizer mais sentido.
4.  Faça os construtores das subclasses chamarem `super(...)`.
5.  Tenha pelo menos um bloco `static`.
6.  Tenha pelo menos um bloco de inicialização de instância.
7.  Crie mais de um objeto e analise a sequência de inicialização.
8.  Sobrescreva pelo menos um método.
9.  Em uma sobrescrita, utilize `super.metodo()`.
10. Sobrescreva `toString()`.
11. Tenha uma associação entre funcionários e outra classe.
12. Armazene diferentes tipos de funcionários em um array.
13. Imprima os objetos utilizando `System.out.println(objeto)`.

### Restrições

-   Não copie nenhum exemplo das aulas.
-   Não duplique atributos herdados.
-   Não coloque toda a lógica no `main`.
-   Não use herança apenas para reaproveitar código.
-   Não transforme toda propriedade em `protected`.

### Antes de executar

Escreva em comentários a ordem que você espera para:

``` text
static da superclasse
static da subclasse
inicialização da instância
construtor da superclasse
inicialização da subclasse
construtor da subclasse
```

Depois compare sua previsão com o resultado real e explique a diferença,
se houver.
