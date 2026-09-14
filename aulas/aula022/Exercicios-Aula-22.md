# Aula 22 - Exercícios: Operador Ternário

## 🎯 Objetivo

Praticar o operador ternário:

``` java
condicao ? valor1 : valor2
```

Nesta aula o foco é utilizar o ternário para decisões simples que
produzem um valor.

### 🚫 Não utilize

-   `switch`
-   ternários encadeados
-   soluções desnecessariamente complexas

# 🟢 Fácil

## Exercício 1 --- Maior de idade

Crie `Exercicio01`.

Declare:

``` java
int idade = 20;
```

Utilize o operador ternário para criar uma `String` contendo
`Maior de idade` ou `Menor de idade`.

Depois imprima o resultado.

## Exercício 2 --- Número positivo

Crie `Exercicio02`.

Declare:

``` java
int numero = 10;
```

Utilize o ternário para armazenar em uma `String` `Positivo` quando o
número for maior que zero e `Negativo` caso contrário.

## Exercício 3 --- Usuário ativo

Crie `Exercicio03`.

Declare:

``` java
boolean usuarioAtivo = true;
```

Utilize o ternário para produzir `Usuário ativo` ou `Usuário inativo`.

# 🟡 Médio

## Exercício 4 --- Aprovação

Crie `Exercicio04`.

Declare:

``` java
double nota = 8.5;
```

Utilize o ternário para criar uma `String` contendo `Aprovado` ou
`Reprovado`.

A nota mínima é `7`.

## Exercício 5 --- Maior número

Crie `Exercicio05`.

Declare:

``` java
int numero1 = 10;
int numero2 = 20;
```

Utilize somente o operador ternário para armazenar o maior número em:

``` java
int maior;
```

## Exercício 6 --- Positivo ou negativo

Crie `Exercicio06`.

Declare:

``` java
int numero = -15;
```

Utilize o ternário para armazenar `1` se o número for positivo e `-1` se
for negativo.

# 🔴 Difícil

## Exercício 7 --- Menor entre dois números

Crie `Exercicio07`.

Declare:

``` java
int numero1 = 35;
int numero2 = 20;
```

Utilize somente o ternário para descobrir o menor valor e armazená-lo
em:

``` java
int menor;
```

## Exercício 8 --- Verificação de acesso

Crie `Exercicio08`.

Declare:

``` java
int idade = 22;
boolean possuiIngresso = true;
```

Utilize uma condição com `&&`.

Se tiver 18 anos ou mais e possuir ingresso, o resultado deve ser
`Acesso permitido`. Caso contrário, `Acesso negado`.

Armazene o resultado em uma `String`.

## Exercício 9 --- Frete

Crie `Exercicio09`.

Declare:

``` java
double valorCompra = 250;
```

Utilize o ternário para definir:

-   compras de `200` ou mais → frete `0`
-   compras abaixo de `200` → frete `30`

Armazene em:

``` java
double frete;
```

# 🏆 Exercício 10 --- Desafio final: Status do usuário

Crie `Exercicio10`.

Declare:

``` java
boolean usuarioAtivo = true;
boolean usuarioAdmin = false;
```

Crie uma variável `String` chamada `status`.

Utilizando o operador ternário, faça uma decisão baseada em
`usuarioAtivo`:

``` text
Usuário ativo
```

ou:

``` text
Usuário inativo
```

Depois crie `tipoUsuario`, que deve receber:

``` text
Administrador
```

quando `usuarioAdmin` for `true`, ou:

``` text
Usuário comum
```

quando for `false`.

### Regras

-   Utilize o operador ternário.
-   Não utilize `if`.
-   Não utilize `else`.
-   Não utilize `switch`.
-   Faça duas decisões ternárias independentes.

# 🧠 Depois dos exercícios

Responda sem consultar o README:

1.  Qual é a estrutura do operador ternário?
2.  O que significa o `?`?
3.  O que significa o `:`?
4.  O que o ternário retorna?
5.  Quando você escolheria ternário em vez de `if / else`?
6.  Por que um ternário muito complexo pode ser ruim para a leitura?

# 🏆 Desafio extra

Pegue o exercício de aprovação e faça duas versões:

### Versão 1

Utilizando `if / else`.

### Versão 2

Utilizando ternário.

Depois compare qual ficou mais simples e mais fácil de ler.

# ✅ Critério para avançar

Você estará pronto para a Aula 23 quando conseguir escrever sem
consultar exemplos:

``` java
condicao ? valor1 : valor2;
```

e explicar:

``` text
condição verdadeira → valor 1
condição falsa      → valor 2
```

Além disso, deve saber identificar quando uma decisão simples pode usar
ternário e quando `if / else` seria mais legível.
