# Aula 24 - Resolução de Exercícios: Estruturas Condicionais

## 🎯 Objetivo

A Aula 24 é a continuação prática do bloco de estruturas condicionais e
é identificada na playlist como:

**24 - Estruturas Condicionais pt 05 - Resolução do exercícios**

**YouTube ID:** `W-ng6Dqa-E4`

A sequência da playlist coloca a aula 24 imediatamente depois da aula de
tabela verdade e exercício e antes de `switch`.

> **Nota sobre a fonte:** o transcript específico da Aula 24 não está
> disponível no material pesquisável neste momento. Por isso, este
> README não inventa uma reprodução da explicação do professor. O foco
> abaixo é a prática e revisão dos conceitos que o material disponível
> confirma para este bloco.

------------------------------------------------------------------------

## 1. O que esta aula representa

Depois de estudar:

``` text
if
else
else if
&&
||
!
tabela verdade
operador ternário
```

a etapa seguinte é aplicar esses conceitos em exercícios.

O objetivo não é decorar condições, mas conseguir transformar uma regra
em uma expressão lógica.

------------------------------------------------------------------------

## 2. Como resolver uma condição

Quando receber um problema, faça nesta ordem:

### 1. Identifique as regras

Exemplo:

``` text
Para entrar:
- idade >= 18
- documento válido
```

### 2. Transforme cada regra em uma condição

``` java
idade >= 18
```

``` java
documentoValido
```

### 3. Descubra como as condições se relacionam

Se as duas forem obrigatórias:

``` java
idade >= 18 && documentoValido
```

Se apenas uma for suficiente:

``` java
condicao1 || condicao2
```

Se precisar negar uma condição:

``` java
!condicao
```

### 4. Coloque o resultado na estrutura apropriada

Por exemplo:

``` java
if (condicao) {
    // ação
}
```

------------------------------------------------------------------------

## 3. `&&` na prática

Quando **todas** as regras precisam ser atendidas:

``` java
if (idade >= 18 && possuiDocumento) {
    System.out.println("Entrada autorizada");
}
```

Mentalmente:

``` text
regra 1 → true
regra 2 → true

true && true → true
```

Se qualquer uma for `false`, o resultado do `&&` será `false`.

------------------------------------------------------------------------

## 4. `||` na prática

Quando **uma das alternativas** já é suficiente:

``` java
if (possuiIngresso || possuiConvite) {
    System.out.println("Entrada autorizada");
}
```

Mentalmente:

``` text
regra 1 → false
regra 2 → true

false || true → true
```

------------------------------------------------------------------------

## 5. `!` na prática

Quando a regra depende da negação de um booleano:

``` java
boolean bloqueado = false;

if (!bloqueado) {
    System.out.println("Usuário liberado");
}
```

Mentalmente:

``` text
bloqueado → false
!bloqueado → true
```

------------------------------------------------------------------------

## 6. Condições compostas

É comum juntar os operadores:

``` java
if (idade >= 18 && (possuiIngresso || possuiConvite) && !bloqueado) {
    System.out.println("Entrada autorizada");
}
```

Divida para entender:

``` text
idade >= 18
        ↓
      true

possuiIngresso || possuiConvite
        ↓
      true

!bloqueado
        ↓
      true
```

Depois:

``` text
true && true && true
        ↓
      true
```

------------------------------------------------------------------------

## 7. `if / else if / else`

Quando existem vários caminhos possíveis:

``` java
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

### ⚠️ Ordem das condições

A ordem importa.

Por exemplo:

``` java
if (nota >= 5) {
    System.out.println("Regular");
} else if (nota >= 9) {
    System.out.println("Excelente");
}
```

Uma nota `10` já satisfaz `nota >= 5`, portanto o segundo teste não será
alcançado.

------------------------------------------------------------------------

## 8. Ternário

Para uma escolha simples entre dois valores:

``` java
String resultado = idade >= 18 ? "Maior" : "Menor";
```

Lembre:

``` text
condição ? valor_se_true : valor_se_false
```

O ternário produz um valor; ele não deve ser usado para substituir toda
lógica complexa de `if / else`.

------------------------------------------------------------------------

## 🧠 Modelo mental da aula

``` text
REGRA DO PROBLEMA
       ↓
separar condições
       ↓
cada condição → true / false
       ↓
combinar com && / || / !
       ↓
escolher if / else if / else / ternário
       ↓
testar casos diferentes
```

------------------------------------------------------------------------

## 🎯 O que você precisa dominar

-   [ ] Transformar uma regra em uma condição Java.
-   [ ] Saber quando utilizar `&&`.
-   [ ] Saber quando utilizar `||`.
-   [ ] Saber utilizar `!`.
-   [ ] Avaliar condições compostas.
-   [ ] Entender a ordem de um `if / else if / else`.
-   [ ] Utilizar ternário para decisões simples.
-   [ ] Testar valores que ficam nos limites das condições.
-   [ ] Conseguir explicar por que uma condição resultou em `true` ou
    `false`.

------------------------------------------------------------------------

## 📚 Referência

A playlist identifica a Aula 24 como **"Estruturas Condicionais pt 05 -
Resolução do exercícios"**, depois da Aula 23 de tabela verdade e
exercício. fileciteturn13file0L57-L62

O material de apoio do bloco 20--26 confirma `if`, `else`, `else if`,
`&&`, `||`, `!`, tabela verdade e ternário como partes do conjunto de
estruturas condicionais.
