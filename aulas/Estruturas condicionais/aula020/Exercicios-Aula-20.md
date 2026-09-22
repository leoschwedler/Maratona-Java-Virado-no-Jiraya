# Aula 20 - Exercícios: Estruturas Condicionais `if`

## 🎯 Objetivo

Praticar exclusivamente o conteúdo da Aula 20.

**Não utilize `else`, `else if`, operador ternário ou `switch` nestes
exercícios.**

A ideia é dominar o `if` antes de avançar para as próximas estruturas.

------------------------------------------------------------------------

# 🟢 Fácil

## Exercício 1 --- Maior de idade

Crie uma classe `Exercicio01`.

Declare:

``` java
int idade = 20;
```

Utilize um `if` para imprimir:

``` text
Maior de idade
```

somente quando a idade for maior ou igual a 18.

### Regra

Utilize:

-   `int`
-   `if`
-   operador de comparação
-   `System.out.println`

------------------------------------------------------------------------

## Exercício 2 --- Número positivo

Crie uma classe `Exercicio02`.

Declare:

``` java
int numero = 10;
```

Utilize um `if` para imprimir:

``` text
Número positivo
```

somente quando o número for maior que zero.

Depois teste também com:

``` java
int numero = -5;
```

Observe o que acontece.

------------------------------------------------------------------------

## Exercício 3 --- Variável booleana

Crie uma classe `Exercicio03`.

Declare:

``` java
boolean usuarioAtivo = true;
```

Utilize um `if` para imprimir:

``` text
Usuário ativo
```

somente quando `usuarioAtivo` for `true`.

Depois altere para:

``` java
boolean usuarioAtivo = false;
```

e execute novamente.

------------------------------------------------------------------------

# 🟡 Médio

## Exercício 4 --- Resultado de uma comparação

Crie uma classe `Exercicio04`.

Declare:

``` java
int idade = 25;
```

Crie uma variável:

``` java
boolean autorizado;
```

Faça com que `autorizado` receba o resultado da comparação:

``` text
idade >= 18
```

Depois utilize:

``` java
if (autorizado) {
    ...
}
```

para imprimir:

``` text
Compra autorizada
```

------------------------------------------------------------------------

## Exercício 5 --- Temperatura

Crie uma classe `Exercicio05`.

Declare:

``` java
double temperatura = 30;
```

Utilize um `if` para imprimir:

``` text
Está quente
```

somente quando a temperatura for maior ou igual a 30.

Depois teste valores diferentes.

------------------------------------------------------------------------

## Exercício 6 --- Nota para aprovação

Crie uma classe `Exercicio06`.

Declare:

``` java
double nota = 8.5;
```

Utilize um `if` para imprimir:

``` text
Aluno aprovado
```

quando a nota for maior ou igual a 7.

### Restrição

Não utilize `else`.

O programa só precisa imprimir alguma coisa quando a condição for
verdadeira.

------------------------------------------------------------------------

# 🔴 Difícil

## Exercício 7 --- Duas condições independentes

Crie uma classe `Exercicio07`.

Declare:

``` java
int idade = 20;
boolean possuiDocumento = true;
```

Crie dois `if` independentes.

O primeiro deve verificar se a pessoa é maior de idade.

O segundo deve verificar se possui documento.

Imprima mensagens diferentes para cada condição verdadeira.

### Objetivo

Perceber que um `if` não impede que outro `if` seja executado.

------------------------------------------------------------------------

## Exercício 8 --- Negação

Crie uma classe `Exercicio08`.

Declare:

``` java
boolean autorizado = false;
```

Utilize o operador `!` para imprimir:

``` text
Usuário não autorizado
```

quando `autorizado` for `false`.

### Restrição

Neste exercício, não utilize:

``` java
autorizado == false
```

Você deve utilizar o operador:

``` java
!
```

------------------------------------------------------------------------

## Exercício 9 --- Atribuição x comparação

Crie uma classe `Exercicio09`.

Declare:

``` java
boolean ativo = true;
```

Crie dois exemplos separados que demonstrem a diferença entre:

``` java
=
```

e:

``` java
==
```

No primeiro exemplo, faça uma atribuição.

No segundo, faça uma comparação dentro de um `if`.

Depois escreva, como comentário no código, qual é a função de cada
operador.

Exemplo:

``` java
// =
// == 
```

Não copie uma explicação pronta. Escreva com suas próprias palavras.

------------------------------------------------------------------------

# 🏆 Desafio final

## Exercício 10 --- Sistema de autorização

Crie uma classe:

``` text
Exercicio10
```

Imagine que você está criando uma pequena validação para compra de
bebida alcoólica.

Crie:

``` java
int idade = 20;
```

Depois:

1.  Crie uma variável `boolean` chamada `autorizado`.
2.  Faça `autorizado` receber o resultado da verificação da idade.
3.  Utilize um `if` para imprimir:

``` text
Autorizado a comprar bebida alcoólica
```

quando a pessoa tiver idade suficiente. 4. Utilize outro `if` para
tratar o caso de não estar autorizada. 5. No segundo `if`, utilize o
operador `!`.

### Regras do desafio

Você deve utilizar:

-   `int`
-   `boolean`
-   comparação
-   `if`
-   `!`
-   `{}`

### Não utilize

-   `else`
-   `else if`
-   operador ternário
-   `switch`

------------------------------------------------------------------------

# 🧠 Depois dos exercícios

Antes de considerar a aula concluída, tente responder sem consultar o
README:

### 1.

O que acontece quando a condição de um `if` resulta em `true`?

### 2.

O que acontece quando resulta em `false`?

### 3.

Por que isto não funciona?

``` java
int idade = 20;

if (idade) {
}
```

### 4.

Qual a diferença entre:

``` java
=
```

e:

``` java
==
```

### 5.

O que significa:

``` java
!autorizado
```

------------------------------------------------------------------------

# ✅ Critério para avançar

Você estará pronto para a próxima aula quando conseguir criar um `if`
sem consultar exemplos e explicar:

``` text
condição
   ↓
true / false
   ↓
executa ou não executa
```

Se conseguir fazer isso, podemos avançar para a Aula 21, que continua
estruturas condicionais com `else if`.
