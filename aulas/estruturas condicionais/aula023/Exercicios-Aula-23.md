# Exercícios - Aula 23

## 📌 Regras

Resolva todos os exercícios em Java.

**Não use a resposta pronta.** O objetivo é praticar o raciocínio com
condições booleanas e tabela verdade.

Estrutura sugerida:

``` text
src/main/java/
└── maratonajava/
    └── estrutura_condicionais.aula023/
        ├── Ex01.java
        ├── Ex02.java
        ├── Ex03.java
        ├── ...
        └── Ex10.java
```

------------------------------------------------------------------------

# 🟢 Fáceis

## Ex01 - Duas condições com `&&`

Crie:

``` java
int idade = 20;
boolean documentoValido = true;
```

Verifique com `if` se a pessoa pode entrar.

A pessoa só pode entrar quando:

-   tiver 18 anos ou mais;
-   **e** o documento for válido.

Use `&&`.

------------------------------------------------------------------------

## Ex02 - Duas condições com `||`

Crie:

``` java
boolean usuarioAdmin = false;
boolean usuarioGerente = true;
```

Mostre:

``` text
Acesso permitido
```

quando o usuário for administrador **ou** gerente.

Use `||`.

------------------------------------------------------------------------

## Ex03 - Negação com `!`

Crie:

``` java
boolean bloqueado = false;
```

Mostre:

``` text
Usuário liberado
```

somente quando o usuário **não** estiver bloqueado.

Use `!`.

------------------------------------------------------------------------

# 🟡 Médios

## Ex04 - Aprovado

Crie:

``` java
double nota = 8.0;
double frequencia = 80.0;
```

Um aluno será aprovado quando:

-   a nota for maior ou igual a 7;
-   **e** a frequência for maior ou igual a 75%.

Use `&&`.

------------------------------------------------------------------------

## Ex05 - Pode comprar

Crie:

``` java
int idade = 20;
boolean possuiDinheiro = true;
```

A pessoa poderá comprar quando:

-   tiver 18 anos ou mais;
-   **e** possuir dinheiro.

Mostre uma mensagem indicando se a compra foi permitida ou negada.

------------------------------------------------------------------------

## Ex06 - Entrada alternativa

Crie:

``` java
boolean possuiIngresso = false;
boolean possuiConvite = true;
```

A pessoa poderá entrar quando tiver ingresso **ou** convite.

Use `||`.

------------------------------------------------------------------------

# 🔴 Difíceis

## Ex07 - Acesso ao sistema

Crie:

``` java
int idade = 25;
boolean usuarioAtivo = true;
boolean bloqueado = false;
```

O acesso será permitido somente quando:

-   a idade for maior ou igual a 18;
-   o usuário estiver ativo;
-   e o usuário **não** estiver bloqueado.

Monte uma única condição lógica usando `&&` e `!`.

------------------------------------------------------------------------

## Ex08 - Duas formas de pagamento

Crie:

``` java
boolean cartao = false;
boolean pix = true;
boolean contaBloqueada = false;
```

O pagamento será permitido quando:

-   o cliente tiver cartão **ou** Pix;
-   e a conta **não** estiver bloqueada.

Use `||`, `&&` e `!`.

------------------------------------------------------------------------

## Ex09 - Analise a expressão

Considere:

``` java
int idade = 17;
boolean autorizado = true;
boolean bloqueado = false;
```

Analise a seguinte condição:

``` java
idade >= 18 && autorizado && !bloqueado
```

Sem executar o programa primeiro, determine:

1.  Qual é o resultado de `idade >= 18`?
2.  Qual é o resultado de `!bloqueado`?
3.  Qual é o resultado final da expressão?
4.  O `if` será executado?

Depois confirme sua resposta executando o código.

------------------------------------------------------------------------

# 🏆 Desafio Final

## Ex10 - Sistema de acesso

Crie um pequeno sistema de autorização.

Utilize:

``` java
int idade = 22;
boolean usuarioAtivo = true;
boolean bloqueado = false;
boolean possuiIngresso = false;
boolean convidado = true;
```

A entrada será permitida somente quando:

1.  o usuário tiver 18 anos ou mais;
2.  o usuário estiver ativo;
3.  o usuário não estiver bloqueado;
4.  e possuir ingresso **ou** for convidado.

Você deverá montar uma condição combinando:

-   `&&`
-   `||`
-   `!`
-   operadores relacionais

O programa deve mostrar:

``` text
Acesso permitido
```

ou:

``` text
Acesso negado
```

### 🎯 Regra do desafio

Antes de escrever o `if`, escreva no papel ou em comentários quais são
os resultados individuais:

``` text
condição 1 → ?
condição 2 → ?
condição 3 → ?
condição 4 → ?
```

Depois combine os resultados usando a tabela verdade.

------------------------------------------------------------------------

# 🧠 Desafio extra --- Tabela verdade

Para fixar o conteúdo, escreva no seu código um comentário contendo a
tabela verdade dos três operadores:

``` text
&&
||

!
```

Não precisa criar um programa para isso. O objetivo é conseguir lembrar:

``` text
&& → ?
|| → ?
!  → ?
```

sem consultar o README.
