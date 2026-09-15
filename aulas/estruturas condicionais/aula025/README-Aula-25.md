# Aula 25 - Estruturas Condicionais: `switch`

## 🎯 Objetivo

Aprender a utilizar a estrutura `switch` em Java para trabalhar com
diferentes possibilidades de um mesmo valor.

A aula 25 é a parte 06 de **Estruturas Condicionais** da playlist
*Maratona Java Virado no Jiraya*.

## 🎥 Aula

-   **Aula:** 25
-   **Título:** Conditional Structures pt 06 - Switch
-   **YouTube ID:** `AYMtqJSrQTU`

A playlist identifica a aula 25 como a introdução ao `switch`, seguida
pela aula 26, dedicada a exercícios de `switch`.
fileciteturn15file1L83-L90

------------------------------------------------------------------------

# 1. O que é `switch`?

O `switch` é uma estrutura condicional utilizada quando queremos
escolher um comportamento com base no **valor de uma expressão**.

Ele é especialmente útil quando existem várias possibilidades para o
mesmo valor.

Exemplo conceitual:

``` java
int dia = 2;

switch (dia) {
    case 1:
        System.out.println("Domingo");
        break;
    case 2:
        System.out.println("Segunda-feira");
        break;
    case 3:
        System.out.println("Terça-feira");
        break;
}
```

Nesse exemplo, o Java verifica o valor de `dia` e procura o `case`
correspondente.

------------------------------------------------------------------------

# 2. Estrutura básica

A estrutura tradicional possui:

-   `switch`
-   `case`
-   `break`
-   `default`

Modelo:

``` java
switch (variavel) {
    case valor1:
        // código
        break;

    case valor2:
        // código
        break;

    default:
        // código para nenhum case
}
```

## `switch`

É a expressão que será analisada.

``` java
switch (dia)
```

## `case`

Representa uma possibilidade.

``` java
case 1:
```

Se o valor analisado corresponder ao `case`, o bloco daquele caso será
executado.

## `break`

Interrompe a execução do `switch`.

``` java
break;
```

Na forma tradicional do `switch`, ele é importante para evitar que a
execução continue para os próximos `case`.

## `default`

É executado quando nenhum `case` corresponde ao valor analisado.

``` java
default:
    System.out.println("Opção inválida");
```

------------------------------------------------------------------------

# 3. `switch` x `if / else if`

Uma forma de pensar:

### `if / else if`

É muito útil quando as decisões dependem de **condições e comparações**:

``` java
if (idade >= 18) {
    // ...
} else if (idade >= 16) {
    // ...
}
```

### `switch`

É muito útil quando temos **várias opções relacionadas ao mesmo valor**:

``` java
switch (opcao) {
    case 1:
        // ...
        break;
    case 2:
        // ...
        break;
    case 3:
        // ...
        break;
}
```

O `switch` deixa esse tipo de decisão mais organizado.

------------------------------------------------------------------------

# 4. O fluxo do `switch`

Considere:

``` java
int opcao = 2;

switch (opcao) {
    case 1:
        System.out.println("Cadastrar");
        break;

    case 2:
        System.out.println("Consultar");
        break;

    case 3:
        System.out.println("Excluir");
        break;

    default:
        System.out.println("Opção inválida");
}
```

Fluxo:

``` text
opcao = 2
     ↓
 switch(opcao)
     ↓
 case 1? ❌
     ↓
 case 2? ✅
     ↓
 executa "Consultar"
     ↓
 break
     ↓
 fim do switch
```

------------------------------------------------------------------------

# 5. Por que o `break` é importante?

Na forma tradicional do `switch`, sem `break`, a execução pode continuar
nos próximos `case`.

Exemplo:

``` java
switch (numero) {
    case 1:
        System.out.println("Um");

    case 2:
        System.out.println("Dois");
        break;
}
```

Se `numero` for `1`, o primeiro `case` será encontrado e, sem um
`break`, a execução poderá continuar no `case 2`.

Por isso, durante o aprendizado da estrutura tradicional, associe:

``` text
case → executa o bloco
break → encerra o switch
```

------------------------------------------------------------------------

# 6. `default`

Use `default` para tratar valores que não foram previstos nos `case`.

``` java
int opcao = 10;

switch (opcao) {
    case 1:
        System.out.println("Cadastrar");
        break;

    case 2:
        System.out.println("Consultar");
        break;

    default:
        System.out.println("Opção inválida");
}
```

Nesse caso, nenhum `case` corresponde a `10`, então o `default` será
executado.

------------------------------------------------------------------------

# 7. `switch` com `String`

O `switch` também pode ser utilizado com `String`.

Exemplo:

``` java
String comando = "iniciar";

switch (comando) {
    case "iniciar":
        System.out.println("Sistema iniciado");
        break;

    case "parar":
        System.out.println("Sistema parado");
        break;

    default:
        System.out.println("Comando desconhecido");
}
```

------------------------------------------------------------------------

# 8. `switch` com `char`

Também podemos analisar um `char`:

``` java
char opcao = 'A';

switch (opcao) {
    case 'A':
        System.out.println("Opção A");
        break;

    case 'B':
        System.out.println("Opção B");
        break;

    default:
        System.out.println("Opção inválida");
}
```

------------------------------------------------------------------------

# 9. Ponto importante: escolha o `switch` pelo tipo de problema

Não tente substituir qualquer `if` por `switch`.

Se a regra for:

``` text
idade >= 18
```

isso é uma comparação e `if` costuma ser mais apropriado.

Se a regra for:

``` text
opcao == 1
opcao == 2
opcao == 3
```

o `switch` é uma escolha natural.

------------------------------------------------------------------------

# 10. Resumo

  Recurso     Função
  ----------- --------------------------
  `switch`    Analisa uma expressão
  `case`      Define uma possibilidade
  `break`     Interrompe o `switch`
  `default`   Trata valores sem `case`

### Modelo mental

``` text
Tenho um valor
      ↓
Tenho várias opções para esse valor?
      ↓
     SIM
      ↓
    switch
      ↓
    case
      ↓
   executa
      ↓
    break
```

------------------------------------------------------------------------

# ✅ Checklist

Antes de avançar, você deve conseguir:

-   [ ] Entender o objetivo do `switch`.
-   [ ] Criar um `switch`.
-   [ ] Criar vários `case`.
-   [ ] Entender o papel do `break`.
-   [ ] Utilizar `default`.
-   [ ] Utilizar `switch` com valores como `int`, `char` e `String`.
-   [ ] Reconhecer quando `switch` é mais adequado que uma sequência de
    `if / else if`.
-   [ ] Entender o problema causado pela ausência de `break` na forma
    tradicional.

------------------------------------------------------------------------

## 📌 Próxima aula

A **Aula 26** é:

> Conditional Structures pt 07 - Switch exercise

Ou seja, depois de aprender a estrutura, você vai praticar `switch` com
exercícios. fileciteturn15file1L83-L90

------------------------------------------------------------------------

## 📚 Fonte

A numeração e o título desta aula foram conferidos na relação da
playlist fornecida para o projeto: aula 25 = `AYMtqJSrQTU` ---
*Conditional Structures pt 06 - Switch*. fileciteturn15file1L83-L90
