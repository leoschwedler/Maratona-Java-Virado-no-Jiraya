# Exercícios - Aula 24

## 📌 Objetivo

A Aula 24 é uma aula de **resolução/prática**. Estes exercícios servem
para você aplicar o raciocínio das aulas 20--23.

Não coloquei respostas.

Estrutura sugerida:

``` text
src/main/java/
└── maratonajava/
    └── estrutura_condicionais.aula024/
        ├── Ex01.java
        ├── Ex02.java
        ├── ...
        └── Ex10.java
```

------------------------------------------------------------------------

# 🟢 Fáceis

## Ex01 - Maior de idade

Crie:

``` java
int idade = 20;
```

Utilize `if` para verificar se a pessoa tem 18 anos ou mais.

Se tiver, mostre:

``` text
Maior de idade
```

------------------------------------------------------------------------

## Ex02 - Número positivo ou negativo

Crie:

``` java
int numero = -10;
```

Utilize uma estrutura condicional para informar se o número é:

``` text
Positivo
```

ou:

``` text
Negativo
```

Considere também o caso `0`.

------------------------------------------------------------------------

## Ex03 - Acesso simples

Crie:

``` java
boolean usuarioAtivo = true;
```

Se o usuário estiver ativo, mostre:

``` text
Usuário ativo
```

Caso contrário:

``` text
Usuário inativo
```

Use `if / else`.

------------------------------------------------------------------------

# 🟡 Médios

## Ex04 - Compra autorizada

Crie:

``` java
int idade = 20;
boolean possuiDinheiro = true;
```

A compra só pode ser realizada quando:

-   idade \>= 18
-   possuiDinheiro == true

Utilize `&&`.

Mostre:

``` text
Compra autorizada
```

ou:

``` text
Compra não autorizada
```

------------------------------------------------------------------------

## Ex05 - Entrada com alternativa

Crie:

``` java
boolean possuiIngresso = false;
boolean possuiConvite = true;
```

A entrada será permitida se a pessoa:

-   possuir ingresso **ou**
-   possuir convite.

Utilize `||`.

------------------------------------------------------------------------

## Ex06 - Usuário bloqueado

Crie:

``` java
boolean bloqueado = false;
```

Utilize `!` para verificar se o usuário **não** está bloqueado.

Mostre:

``` text
Usuário liberado
```

ou:

``` text
Usuário bloqueado
```

------------------------------------------------------------------------

# 🔴 Difíceis

## Ex07 - Sistema de acesso

Crie:

``` java
int idade = 22;
boolean usuarioAtivo = true;
boolean bloqueado = false;
```

O acesso será permitido somente quando:

-   idade \>= 18
-   usuário estiver ativo
-   usuário não estiver bloqueado

Monte uma única condição utilizando:

``` text
&&
!
```

------------------------------------------------------------------------

## Ex08 - Sistema de desconto

Crie:

``` java
double valorCompra = 350;
boolean clienteVip = true;
```

O cliente recebe desconto quando:

-   a compra for maior ou igual a `300`;
-   **ou** ele for cliente VIP.

Utilize `||`.

Depois mostre se o desconto foi concedido.

------------------------------------------------------------------------

## Ex09 - Classificação de nota

Crie:

``` java
double nota = 8.5;
```

Classifique utilizando `if / else if / else`:

``` text
9 ou mais  → Excelente
7 ou mais  → Bom
5 ou mais  → Regular
abaixo de 5 → Reprovado
```

### ⚠️ Atenção

A ordem das condições é importante.

Teste:

``` text
10
9
8
7
6
5
4
```

------------------------------------------------------------------------

# 🏆 Desafio Final

## Ex10 - Sistema completo de autorização

Crie:

``` java
int idade = 25;
boolean usuarioAtivo = true;
boolean bloqueado = false;
boolean possuiIngresso = false;
boolean possuiConvite = true;
```

A pessoa poderá entrar somente se:

1.  tiver 18 anos ou mais;
2.  estiver com o usuário ativo;
3.  não estiver bloqueada;
4.  possuir ingresso **ou** convite.

O resultado deve ser:

``` text
Entrada autorizada
```

ou:

``` text
Entrada negada
```

### Regras

Você deve utilizar:

``` text
>=
&&
||
!
if
```

### 🎯 Parte mais importante

Antes de escrever a condição final, analise cada parte:

``` text
idade >= 18 → ?

usuarioAtivo → ?

!bloqueado → ?

possuiIngresso || possuiConvite → ?
```

Depois combine tudo.

### 🧪 Testes obrigatórios

Depois de fazer funcionar, altere os valores e teste pelo menos:

**Teste 1**

``` text
idade = 25
ativo = true
bloqueado = false
ingresso = false
convite = true
```

**Teste 2**

``` text
idade = 17
ativo = true
bloqueado = false
ingresso = true
convite = false
```

**Teste 3**

``` text
idade = 25
ativo = false
bloqueado = false
ingresso = true
convite = false
```

**Teste 4**

``` text
idade = 25
ativo = true
bloqueado = true
ingresso = true
convite = false
```

**Teste 5**

``` text
idade = 25
ativo = true
bloqueado = false
ingresso = false
convite = false
```

Para cada teste, tente prever o resultado **antes de executar**.

------------------------------------------------------------------------

# 🧠 Revisão final

Sem consultar o README, responda:

1.  Quando usar `&&`?
2.  Quando usar `||`?
3.  O que `!` faz?
4.  Por que a ordem do `else if` pode alterar o resultado?
5.  Quando o ternário é uma boa escolha?
6.  Como você quebra uma condição grande em partes menores?

Se você conseguir responder e resolver os exercícios sem ficar
consultando a sintaxe, o conteúdo das condicionais está começando a
ficar sólido.
