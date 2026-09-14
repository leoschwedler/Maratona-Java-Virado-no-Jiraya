# Aula 22 - Estruturas Condicionais: Operador Ternário

**Vídeo:** 22 - Estruturas Condicionais pt 03 - Operador ternário\
**YouTube ID:** `gU-qCpBC5jk`

## 🎯 Objetivo

Aprender o **operador ternário**, uma forma compacta de realizar uma
decisão simples escolhendo entre dois valores.

A estrutura é:

``` java
condicao ? valor1 : valor2
```

## 1. Como funciona

O ternário possui três partes:

``` text
condição ? valor se verdadeiro : valor se falso
```

Exemplo:

``` java
int idade = 20;

String resultado = idade >= 18
        ? "Maior de idade"
        : "Menor de idade";
```

Fluxo:

``` text
idade >= 18?
   ↓
true  → "Maior de idade"
false → "Menor de idade"
```

`?` separa a condição do valor verdadeiro e `:` separa o valor
verdadeiro do valor falso.

## 2. Ternário x `if / else`

Com `if / else`:

``` java
int idade = 20;
String resultado;

if (idade >= 18) {
    resultado = "Maior de idade";
} else {
    resultado = "Menor de idade";
}
```

Com ternário:

``` java
int idade = 20;

String resultado = idade >= 18
        ? "Maior de idade"
        : "Menor de idade";
```

O ternário deixa uma decisão simples mais compacta.

## 3. O ternário produz um valor

O resultado pode ser atribuído a uma variável:

``` java
double nota = 8;

String resultado = nota >= 7 ? "Aprovado" : "Reprovado";
```

Também podemos escolher números:

``` java
int numero = 10;

int resultado = numero > 0 ? 1 : -1;
```

## 4. Quando utilizar?

O operador ternário é adequado principalmente para uma decisão
**simples**, quando precisamos escolher entre dois valores.

Modelo mental:

``` text
Tenho uma condição
       ↓
Preciso escolher entre dois valores?
       ↓
      SIM
       ↓
    ternário
```

## 5. Quando preferir `if / else`?

Quando a lógica fica maior ou envolve várias ações, `if / else` tende a
ser mais legível.

Por exemplo:

``` java
if (nota >= 9) {
    ...
} else if (nota >= 7) {
    ...
} else if (nota >= 5) {
    ...
} else {
    ...
}
```

Não é necessário transformar uma lógica complexa em um ternário gigante.

## 6. Cuidado com ternários complexos

O objetivo não é substituir todo `if / else` por ternário.

Pense:

``` text
Decisão simples → ternário
Decisão complexa → if / else
```

## 📌 O que você precisa dominar

-   O que é o operador ternário.
-   A estrutura `condicao ? valor1 : valor2`.
-   O significado de `?`.
-   O significado de `:`.
-   Como guardar o resultado em uma variável.
-   A diferença entre ternário e `if / else`.
-   Quando uma decisão simples pode usar ternário.
-   Quando `if / else` é mais adequado.

## 🧠 Resumo

``` java
condicao ? valorSeVerdadeiro : valorSeFalso
```

Exemplo:

``` java
int idade = 20;

String resultado = idade >= 18
        ? "Maior de idade"
        : "Menor de idade";
```

## 🔗 Aula

**YouTube ID:** `gU-qCpBC5jk`

A sequência da playlist identifica a Aula 22 como **Estruturas
Condicionais pt 03 - Operador ternário**. O material de apoio descreve o
ternário como uma escolha simples entre dois valores, usando
`condicao ? valor1 : valor2`.
