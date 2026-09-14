# Aula 21 - Exercícios: `else if`

## 🎯 Objetivo

Praticar `if`, `else if` e `else`.

### 🚫 Não utilize

-   operador ternário
-   `switch`

# 🟢 Fácil

## Exercício 1 --- Maior ou menor de idade

Crie `Exercicio01`.

Declare:

``` java
int idade = 20;
```

Use `if` e `else` para imprimir `Maior de idade` ou `Menor de idade`.

A condição para maior de idade é `idade >= 18`.

## Exercício 2 --- Positivo ou negativo

Crie `Exercicio02`.

Declare:

``` java
int numero = -5;
```

Use `if` e `else` para informar se o número é `Positivo` ou `Negativo`.

Teste também com um número positivo.

## Exercício 3 --- Aprovado ou reprovado

Crie `Exercicio03`.

Declare:

``` java
double nota = 7.5;
```

Se `nota >= 7`, mostre `Aprovado`; caso contrário, `Reprovado`.

# 🟡 Médio

## Exercício 4 --- Classificação por idade

Crie `Exercicio04`.

Declare:

``` java
int idade = 15;
```

Classifique usando `if / else if / else`:

-   `18` ou mais → `Adulto`
-   `12` até `17` → `Adolescente`
-   abaixo de `12` → `Criança`

## Exercício 5 --- Classificação da nota

Crie `Exercicio05`.

Declare:

``` java
double nota = 8.5;
```

Classifique:

-   `9` ou mais → `Excelente`
-   `7` ou mais → `Bom`
-   `5` ou mais → `Regular`
-   abaixo de `5` → `Reprovado`

Use uma única cadeia de decisão.

## Exercício 6 --- Temperatura

Crie `Exercicio06`.

Declare:

``` java
double temperatura = 25;
```

Classifique:

-   `30` ou mais → `Muito quente`
-   `20` ou mais → `Agradável`
-   `10` ou mais → `Frio`
-   abaixo de `10` → `Muito frio`

# 🔴 Difícil

## Exercício 7 --- Faixa salarial

Crie `Exercicio07`.

Declare:

``` java
double salario = 3500;
```

Classifique:

-   `5000` ou mais → `Salário alto`
-   `3000` ou mais → `Salário médio`
-   `1500` ou mais → `Salário baixo`
-   abaixo de `1500` → `Salário muito baixo`

Preste atenção na ordem das condições.

## Exercício 8 --- Três `if` x `if / else if`

Crie `Exercicio08`.

Declare:

``` java
int numero = 10;
```

Faça primeiro três `if` independentes verificando:

-   maior que `0`
-   maior que `5`
-   maior que `9`

Observe o resultado.

Depois faça novamente usando uma cadeia `if / else if / else if`.

Compare os comportamentos.

## Exercício 9 --- Condições combinadas

Crie `Exercicio09`.

Declare:

``` java
int idade = 20;
boolean possuiDocumento = true;
```

Crie uma decisão:

-   `18+` e possui documento → `Entrada autorizada`
-   `18+` mas sem documento → `Documento necessário`
-   caso contrário → `Entrada não autorizada`

Utilize `if`, `else if`, `else` e `&&`.

# 🏆 Exercício 10 --- Desafio final: Sistema de notas

Crie `Exercicio10`.

Declare:

``` java
double nota = 8.7;
```

Classifique:

``` text
9.0 até 10.0 → Excelente
7.0 até 8.9 → Bom
5.0 até 6.9 → Regular
0.0 até 4.9 → Reprovado
```

Use:

``` text
if
else if
else if
else
```

### Regras

1.  Use uma única cadeia de decisão.
2.  Não utilize ternário.
3.  Não utilize `switch`.
4.  Teste pelo menos:

``` text
10
9
8
7
6
5
4
```

### 🧠 Desafio extra

Explique, com suas palavras, por que colocar `nota >= 5` antes de
`nota >= 9` pode gerar um resultado incorreto.

# 🧠 Depois dos exercícios

Responda sem consultar o README:

1.  Qual a diferença entre `else` e `else if`?
2.  O que acontece depois que uma condição de uma cadeia é verdadeira?
3.  Por que a ordem das condições importa?
4.  Qual a diferença entre dois `if` independentes e `if / else if`?
5.  Quando você utilizaria `else if`?

# ✅ Critério para avançar

Você estará pronto para a Aula 22 quando conseguir criar e explicar uma
cadeia:

``` text
if
 ↓
else if
 ↓
else if
 ↓
else
```

e entender por que a ordem das condições altera o resultado.
