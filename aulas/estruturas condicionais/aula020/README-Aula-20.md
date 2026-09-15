# Aula 20 - Estruturas Condicionais: `if`

**Vídeo:** 20 - Conditional Structures pt 01 - IF\
**YouTube ID:** `gk1_Pn8GZA4`

------------------------------------------------------------------------

## 🎯 Objetivo

Nesta aula começa o conteúdo de **estruturas condicionais**.

O foco é aprender a utilizar o `if` para fazer o programa executar
determinado código somente quando uma condição for verdadeira.

A ideia apresentada pelo professor é simples:

> **`if` significa "se".**

No dia a dia fazemos decisões seguindo exatamente essa lógica:

``` text
Se uma condição acontecer
    faça alguma coisa
```

Em programação, o `if` permite representar esse tipo de decisão.

------------------------------------------------------------------------

# 1. O que é o `if`?

O `if` é uma estrutura que permite executar um trecho do código
**somente quando uma condição for verdadeira**.

Estrutura básica:

``` java
if (condicao) {
    // código executado se a condição for verdadeira
}
```

A condição colocada dentro dos parênteses precisa resultar em um valor
booleano:

``` text
true
```

ou

``` text
false
```

------------------------------------------------------------------------

# 2. Exemplo simples

Podemos começar com uma condição diretamente:

``` java
if (true) {
    System.out.println("Executou!");
}
```

Como a condição é `true`, o código dentro do bloco será executado.

Agora:

``` java
if (false) {
    System.out.println("Executou!");
}
```

Nesse caso, o código dentro do `if` não será executado.

A regra fundamental é:

``` text
true  → entra no if
false → não entra no if
```

------------------------------------------------------------------------

# 3. O bloco do `if`

A forma apresentada na aula utiliza chaves:

``` java
if (condicao) {
    // instruções
}
```

As chaves delimitam o bloco de código pertencente ao `if`.

Por exemplo:

``` java
if (true) {
    System.out.println("Dentro do if");
}

System.out.println("Fora do if");
```

O código dentro do `if` depende da condição.

Já a instrução que está fora do bloco é executada independentemente do
resultado do `if`.

------------------------------------------------------------------------

# 4. É possível usar `if` sem chaves?

A aula mostra que, quando existe apenas uma instrução, Java permite
omitir as chaves:

``` java
if (true)
    System.out.println("Dentro do if");
```

Porém, o professor **não recomenda essa forma**, pois ela pode causar
confusão na leitura do código.

Por isso, vamos adotar como padrão:

``` java
if (condicao) {
    System.out.println("Dentro do if");
}
```

Mesmo quando existe apenas uma instrução.

------------------------------------------------------------------------

# 5. Usando uma comparação

O `if` fica realmente útil quando usamos uma condição baseada em dados.

Exemplo apresentado na aula:

``` java
int idade = 20;

if (idade >= 18) {
    System.out.println("Autorizado a comprar bebida alcoólica");
}
```

Aqui acontece o seguinte:

``` text
idade = 20
     ↓
20 >= 18
     ↓
true
     ↓
entra no if
```

Se a idade fosse:

``` java
int idade = 15;
```

teríamos:

``` text
15 >= 18
     ↓
false
     ↓
não entra no if
```

------------------------------------------------------------------------

# 6. O `if` depende do resultado da expressão

Uma das ideias mais importantes da aula é:

``` java
if (expressao) {
    ...
}
```

A expressão precisa produzir um resultado booleano.

Por exemplo:

``` java
idade >= 18
```

produz:

``` text
true
```

ou:

``` text
false
```

Por isso funciona no `if`.

Já colocar uma variável `int` diretamente:

``` java
int idade = 20;

if (idade) {
}
```

não funciona, porque `idade` não é um booleano.

------------------------------------------------------------------------

# 7. Guardando o resultado da condição em uma variável

A aula também mostra que podemos guardar o resultado de uma comparação
em uma variável `boolean`.

Exemplo:

``` java
int idade = 20;

boolean autorizado = idade >= 18;
```

Nesse momento:

``` text
idade >= 18
```

é avaliado e o resultado é armazenado em:

``` java
autorizado
```

Como `idade` é 20:

``` text
autorizado = true
```

Depois podemos utilizar a variável:

``` java
if (autorizado) {
    System.out.println("Autorizado a comprar bebida alcoólica");
}
```

Isso deixa a lógica separada em duas etapas:

``` text
1. Calcular a condição
2. Utilizar o resultado
```

------------------------------------------------------------------------

# 8. Trabalhando com `false`

Também podemos ter:

``` java
boolean autorizado = false;

if (autorizado) {
    System.out.println("Autorizado");
}
```

Como o valor é `false`, o bloco não será executado.

A aula reforça que o `if` somente executa seu bloco quando a expressão
resulta em `true`.

------------------------------------------------------------------------

# 9. Operador de negação `!`

A aula retoma o operador de negação.

O `!` inverte um valor booleano.

Exemplo:

``` java
boolean autorizado = false;

if (!autorizado) {
    System.out.println("Não autorizado");
}
```

Podemos pensar:

``` text
autorizado = false

!autorizado
     ↓
true
```

Portanto, o `if` será executado.

------------------------------------------------------------------------

# 10. `!variavel` x `variavel == false`

A aula mostra duas formas de verificar uma condição negativa.

Uma delas é:

``` java
if (autorizado == false) {
    System.out.println("Não autorizado");
}
```

Outra forma é utilizar o operador de negação:

``` java
if (!autorizado) {
    System.out.println("Não autorizado");
}
```

A segunda forma é apresentada como uma maneira mais natural de expressar
a ideia:

``` text
"se NÃO está autorizado"
```

------------------------------------------------------------------------

# 11. Cuidado com `=` e `==`

Este é um ponto importante apresentado na aula.

Não devemos confundir:

``` java
=
```

com:

``` java
==
```

O operador `=` é utilizado para **atribuição**.

Exemplo:

``` java
boolean autorizado;

autorizado = false;
```

Estamos atribuindo `false` à variável.

Já:

``` java
autorizado == false
```

representa uma **comparação**.

A aula mostra que confundir esses operadores dentro de uma condição pode
gerar um comportamento incorreto.

------------------------------------------------------------------------

# 12. `if` e execução passo a passo

A aula também utiliza o debugger do IntelliJ para observar o valor de
uma variável durante a execução.

A ideia é importante para o aprendizado:

``` text
valor da variável
      ↓
expressão da condição
      ↓
true ou false
      ↓
decisão do if
```

Usar breakpoint pode ajudar a enxergar exatamente qual valor está sendo
utilizado naquele momento.

------------------------------------------------------------------------

# 🧠 Resumo mental

Pense no `if` desta maneira:

``` text
Tenho uma condição?
       ↓
Ela resulta em true ou false?
       ↓
      true
       ↓
executa o bloco
```

ou:

``` text
Tenho uma condição?
       ↓
Ela resulta em true ou false?
       ↓
      false
       ↓
não executa o bloco
```

------------------------------------------------------------------------

# 📌 O que você precisa dominar nesta aula

Antes de avançar, você deve conseguir explicar:

-   O que é `if`.
-   Por que a condição precisa resultar em `boolean`.
-   A diferença entre `true` e `false` na execução.
-   Como utilizar `{}` para criar o bloco do `if`.
-   Por que é recomendado utilizar chaves.
-   Como utilizar uma comparação dentro do `if`.
-   Como armazenar uma condição em uma variável `boolean`.
-   Como utilizar o operador `!`.
-   A diferença entre `=` e `==`.
-   Como utilizar o debugger para observar os valores durante a
    execução.

------------------------------------------------------------------------

# ⚠️ Observação

Esta aula é somente a **primeira parte de estruturas condicionais**.

Na sequência do curso aparecem outras aulas dedicadas a:

-   `else if`
-   operador ternário
-   tabela verdade e exercícios
-   `switch`

Portanto, **não tente aprender todas essas estruturas agora**. O
objetivo desta aula é dominar o `if`.

------------------------------------------------------------------------

# ✅ Checklist

-   [ ] Entendi o conceito de `if`.
-   [ ] Entendi que a condição precisa resultar em `true` ou `false`.
-   [ ] Sei criar um bloco com `{}`.
-   [ ] Sei fazer uma comparação dentro do `if`.
-   [ ] Sei armazenar o resultado de uma condição em `boolean`.
-   [ ] Entendi o operador `!`.
-   [ ] Sei diferenciar `=` de `==`.
-   [ ] Consigo executar um programa com `if` no IntelliJ.
-   [ ] Consigo utilizar um breakpoint para observar uma variável.
-   [ ] Consigo explicar com minhas palavras quando um `if` será
    executado.

------------------------------------------------------------------------

## 🔗 Aula

YouTube ID:

``` text
gk1_Pn8GZA4
```
