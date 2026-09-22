# Aula 23 - Tabela Verdade e Exercício

## 🎯 Objetivo

Entender como avaliar combinações de condições booleanas usando os
operadores lógicos estudados nas aulas anteriores e aplicar esse
raciocínio em estruturas condicionais.

**Aula da playlist:** 23\
**Vídeo:**
`23 - Estruturas Condicionais pt 04 - Tabela Verdade e exercício`\
**YouTube ID:** `Y3ODYE3npgo`

> **Observação sobre a fonte:** o material disponível para esta aula
> confirma o tema da aula e o bloco de conteúdos de estruturas
> condicionais, incluindo `&&`, `||`, `!` e tabela verdade básica. Como
> o transcript específico desta aula não está disponível no material
> consultado, este README não atribui ao professor explicações
> detalhadas que não estejam sustentadas pela fonte.

------------------------------------------------------------------------

## 1. O que é uma expressão booleana?

Uma condição em Java produz um dos dois valores possíveis:

``` java
true
```

ou

``` java
false
```

Por exemplo:

``` java
int idade = 20;

idade >= 18
```

Essa expressão resulta em `true`.

As estruturas condicionais usam esse resultado para decidir qual caminho
seguir.

------------------------------------------------------------------------

## 2. Operador lógico `&&` --- AND

O `&&` representa uma condição em que **todas as condições precisam ser
verdadeiras**.

``` java
condicao1 && condicao2
```

Tabela verdade:

  A       B       A && B
  ------- ------- --------
  true    true    true
  true    false   false
  false   true    false
  false   false   false

### Exemplo

``` java
int idade = 20;
boolean documentoValido = true;

if (idade >= 18 && documentoValido) {
    System.out.println("Acesso permitido");
}
```

Para entrar no `if`, as duas condições precisam resultar em `true`.

------------------------------------------------------------------------

## 3. Operador lógico `||` --- OR

O `||` representa uma condição em que **pelo menos uma condição precisa
ser verdadeira**.

``` java
condicao1 || condicao2
```

Tabela verdade:

  A       B       A \|\| B
  ------- ------- ----------
  true    true    true
  true    false   true
  false   true    true
  false   false   false

### Exemplo

``` java
boolean usuarioAdmin = false;
boolean usuarioGerente = true;

if (usuarioAdmin || usuarioGerente) {
    System.out.println("Acesso permitido");
}
```

Como uma das condições é verdadeira, o resultado é `true`.

------------------------------------------------------------------------

## 4. Operador lógico `!` --- NOT

O `!` **inverte o resultado booleano**.

  A       !A
  ------- -------
  true    false
  false   true

Exemplo:

``` java
boolean bloqueado = false;

if (!bloqueado) {
    System.out.println("Usuário liberado");
}
```

Como `bloqueado` é `false`, `!bloqueado` resulta em `true`.

------------------------------------------------------------------------

## 5. Como pensar usando tabela verdade

Quando uma expressão possui várias condições, não tente avaliar tudo de
uma vez.

Separe:

``` java
idade >= 18 && documentoValido
```

Primeiro descubra:

``` text
idade >= 18        → true
documentoValido    → true
```

Depois aplique o operador:

``` text
true && true       → true
```

Resultado final:

``` text
true
```

Esse processo ajuda principalmente quando as expressões começam a ficar
maiores.

------------------------------------------------------------------------

## 6. Combinando operadores

Também podemos combinar diferentes operadores lógicos.

Exemplo:

``` java
idade >= 18 && (possuiDocumento || possuiAutorizacao)
```

Uma forma organizada de analisar é:

``` text
idade >= 18
      ↓
    true

possuiDocumento || possuiAutorizacao
      ↓
    false || true
      ↓
      true

true && true
      ↓
     true
```

### ⚠️ Dica importante

Quando houver várias condições, use parênteses para deixar a intenção
clara:

``` java
if (idade >= 18 && (possuiDocumento || possuiAutorizacao)) {
    ...
}
```

------------------------------------------------------------------------

## 7. Relação com `if`

O resultado final da expressão é utilizado pelo `if`:

``` java
if (condicao) {
    // executa se condicao for true
}
```

Por exemplo:

``` java
int idade = 17;
boolean autorizado = true;

if (idade >= 18 && autorizado) {
    System.out.println("Entrada permitida");
}
```

Analisando:

``` text
idade >= 18
17 >= 18
false
```

e:

``` text
autorizado
true
```

Então:

``` text
false && true
      ↓
    false
```

O bloco do `if` não será executado.

------------------------------------------------------------------------

## 🧠 Resumo rápido

### `&&`

**Todos precisam ser `true`.**

``` java
A && B
```

### `||`

**Pelo menos um precisa ser `true`.**

``` java
A || B
```

### `!`

**Inverte o booleano.**

``` java
!A
```

### Tabela mental

``` text
&& → todos
|| → pelo menos um
!  → inverte
```

------------------------------------------------------------------------

## ✅ Checklist

Antes de avançar, você deve conseguir:

-   [ ] Identificar se uma expressão resulta em `true` ou `false`.
-   [ ] Montar uma tabela verdade simples.
-   [ ] Entender `&&`.
-   [ ] Entender `||`.
-   [ ] Entender `!`.
-   [ ] Avaliar uma condição composta passo a passo.
-   [ ] Usar condições compostas dentro de um `if`.
-   [ ] Saber separar uma expressão complexa em partes menores.

------------------------------------------------------------------------

## 📚 Fonte

A sequência da playlist identifica esta aula como **23 - Estruturas
Condicionais pt 04 - Tabela Verdade e exercício**, dentro do bloco de
aulas 20--26 de estruturas condicionais.

O material de apoio do bloco confirma como essenciais os operadores
`&&`, `||`, `!` e a tabela verdade básica.
