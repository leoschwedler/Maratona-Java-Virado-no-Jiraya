# Aula 21 - Estruturas Condicionais: `else if`

**Vídeo:** 21 - Estruturas Condicionais pt 02 - ELSE IF\
**YouTube ID:** `exxeNsgcg3c`

## 🎯 Objetivo

Continuar o estudo das estruturas condicionais, aprendendo a trabalhar
com várias possibilidades usando `if`, `else if` e `else`.

## 1. `else`

O `else` representa o caminho alternativo quando a condição do `if` é
falsa:

``` java
if (condicao) {
    // se for verdadeira
} else {
    // se for falsa
}
```

Exemplo:

``` java
int idade = 16;

if (idade >= 18) {
    System.out.println("Maior de idade");
} else {
    System.out.println("Menor de idade");
}
```

## 2. `else if`

Quando existem várias possibilidades, podemos testar outras condições:

``` java
if (condicao1) {

} else if (condicao2) {

} else if (condicao3) {

} else {

}
```

Exemplo:

``` java
int nota = 8;

if (nota >= 9) {
    System.out.println("Excelente");
} else if (nota >= 7) {
    System.out.println("Bom");
} else if (nota >= 5) {
    System.out.println("Regular");
} else {
    System.out.println("Reprovado");
}
```

## 3. Como a cadeia funciona?

As condições são verificadas de cima para baixo.

``` text
if
 ↓
condição verdadeira?
 ↓ SIM → executa e termina a cadeia
 ↓ NÃO
else if
 ↓
outra condição?
 ↓ SIM → executa e termina a cadeia
 ↓ NÃO
próximo else if
 ↓
...
 ↓
else
```

Depois que uma condição é verdadeira, os próximos `else if` daquela
cadeia não são avaliados.

## 4. A ordem das condições importa

Considere:

``` java
int nota = 10;

if (nota >= 5) {
    System.out.println("Passou");
} else if (nota >= 9) {
    System.out.println("Excelente");
}
```

Para `nota = 10`, a primeira condição já é verdadeira. Portanto, o
`else if` não será alcançado.

Uma ordem mais adequada seria:

``` java
if (nota >= 9) {
    System.out.println("Excelente");
} else if (nota >= 5) {
    System.out.println("Passou");
}
```

## 5. Vários `if` x `if / else if`

Dois `if` independentes:

``` java
if (condicao1) {
}

if (condicao2) {
}
```

podem executar os dois blocos.

Já:

``` java
if (condicao1) {
} else if (condicao2) {
}
```

é uma única cadeia de decisão. Quando uma condição é verdadeira, a
cadeia para naquele bloco.

## 6. Condições combinadas

As condições podem usar operadores lógicos já estudados.

Exemplo com `&&`:

``` java
int idade = 25;
boolean possuiCarteira = true;

if (idade >= 18 && possuiCarteira) {
    System.out.println("Pode dirigir");
}
```

Exemplo com `||`:

``` java
boolean temIngresso = true;
boolean nomeNaLista = false;

if (temIngresso || nomeNaLista) {
    System.out.println("Pode entrar");
}
```

## 📌 O que dominar

-   `else`
-   `else if`
-   `if / else if / else`
-   ordem das condições
-   diferença entre `if` independentes e uma cadeia
-   condições com faixas de valores
-   combinação com `&&` e `||`

## ⚠️ Foco desta aula

Não avance ainda para operador ternário ou `switch`. O objetivo aqui é
dominar:

``` text
if → else if → else
```

## ✅ Checklist

-   [ ] Sei usar `else`.
-   [ ] Sei usar `else if`.
-   [ ] Entendo como a cadeia é percorrida.
-   [ ] Entendo por que a ordem importa.
-   [ ] Sei diferenciar `if` independentes de `if / else if`.
-   [ ] Consigo criar condições para diferentes faixas de valores.

## 🔗 Aula

YouTube ID: `exxeNsgcg3c`

**Fonte:** material disponível sobre o bloco de Estruturas Condicionais
das aulas 20--26, que identifica a Aula 21 como
`Estruturas Condicionais pt 02 - ELSE IF` e descreve
`if / else if / else`, o fluxo da cadeia e a importância da ordem das
condições.
