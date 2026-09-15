# Exercícios - Aula 25 - `switch`

## 🎯 Objetivo

Praticar a estrutura `switch` em Java.

### Regras

-   Faça os exercícios no seu projeto Java.
-   Utilize `switch` nos exercícios em que ele for solicitado.
-   Não procure a resposta pronta.
-   Teste diferentes valores para verificar o comportamento do programa.
-   Não precisa criar uma classe para cada exercício. Você pode
    organizar como preferir dentro do pacote `maratonajava.aula025`.

------------------------------------------------------------------------

# 🟢 Fáceis

## Exercício 01 - Dia da semana

Crie uma variável `int` chamada `dia`.

Utilize `switch` para exibir:

``` text
1 → Domingo
2 → Segunda-feira
3 → Terça-feira
4 → Quarta-feira
5 → Quinta-feira
6 → Sexta-feira
7 → Sábado
```

Caso o valor não esteja entre `1` e `7`, exiba:

``` text
Dia inválido
```

------------------------------------------------------------------------

## Exercício 02 - Menu simples

Crie uma variável `int` chamada `opcao`.

Utilize `switch` para exibir:

``` text
1 → Cadastrar
2 → Consultar
3 → Atualizar
4 → Excluir
```

Para qualquer outro valor:

``` text
Opção inválida
```

------------------------------------------------------------------------

## Exercício 03 - Mês do ano

Crie uma variável `int` chamada `mes`.

Utilize `switch` para exibir o nome correspondente:

``` text
1 → Janeiro
2 → Fevereiro
3 → Março
...
12 → Dezembro
```

Para valores inválidos, exiba:

``` text
Mês inválido
```

------------------------------------------------------------------------

# 🟡 Médios

## Exercício 04 - Operação matemática

Crie:

``` java
double numero1;
double numero2;
char operador;
```

Utilize `switch` para realizar:

``` text
+ → soma
- → subtração
* → multiplicação
/ → divisão
```

Exiba o resultado.

Trate uma operação inválida.

Também trate a tentativa de divisão por zero.

------------------------------------------------------------------------

## Exercício 05 - Categoria por código

Crie uma variável `int codigo`.

Utilize `switch`:

``` text
1 → Eletrônico
2 → Alimento
3 → Vestuário
4 → Livro
5 → Móvel
```

Para qualquer outro código:

``` text
Categoria desconhecida
```

------------------------------------------------------------------------

## Exercício 06 - Turno de trabalho

Crie uma variável `char turno`.

Utilize `switch` para interpretar:

``` text
'M' → Manhã
'T' → Tarde
'N' → Noite
```

Para qualquer outro caractere:

``` text
Turno inválido
```

Teste pelo menos um valor de cada possibilidade e um valor inválido.

------------------------------------------------------------------------

# 🔴 Difíceis

## Exercício 07 - Sistema de comandos

Crie uma variável `String comando`.

O sistema deve reconhecer:

``` text
"iniciar"
"pausar"
"continuar"
"parar"
```

Utilize `switch`.

Mensagens esperadas:

``` text
iniciar   → Sistema iniciado
pausar    → Sistema pausado
continuar → Sistema continuando
parar     → Sistema parado
```

Para qualquer outro comando:

``` text
Comando inválido
```

------------------------------------------------------------------------

## Exercício 08 - Calculadora com menu

Crie um pequeno menu:

``` text
1 - Soma
2 - Subtração
3 - Multiplicação
4 - Divisão
```

O programa deve possuir dois números e uma opção.

Utilize `switch` para escolher a operação.

Requisitos:

-   usar `switch`;
-   usar `break`;
-   possuir `default`;
-   tratar divisão por zero;
-   testar todas as operações.

------------------------------------------------------------------------

## Exercício 09 - Status de pedido

Crie uma variável `int status`.

Utilize `switch` para interpretar:

``` text
1 → Pedido recebido
2 → Pagamento aprovado
3 → Em preparação
4 → Enviado
5 → Entregue
6 → Cancelado
```

Para qualquer outro código:

``` text
Status desconhecido
```

Depois, teste o programa com todos os status.

------------------------------------------------------------------------

# 🏆 Desafio Final - Caixa eletrônico

Crie um programa que simule um menu simples de caixa eletrônico.

A variável `opcao` representa:

``` text
1 → Consultar saldo
2 → Sacar
3 → Depositar
4 → Sair
```

Crie também uma variável para representar o saldo.

### Regras

#### Opção 1

Exiba o saldo atual.

#### Opção 2

Solicite um valor para saque.

O saque só deve acontecer se houver saldo suficiente.

#### Opção 3

Solicite um valor para depósito e atualize o saldo.

#### Opção 4

Exiba uma mensagem de saída.

#### `default`

Exiba:

``` text
Opção inválida
```

### Requisitos obrigatórios

-   [ ] Utilizar `switch`.
-   [ ] Utilizar `case`.
-   [ ] Utilizar `break`.
-   [ ] Utilizar `default`.
-   [ ] Trabalhar com `double` para valores monetários.
-   [ ] Impedir saque maior que o saldo.
-   [ ] Testar todas as opções.
-   [ ] Testar uma opção inválida.
-   [ ] Testar um saque válido.
-   [ ] Testar um saque maior que o saldo.
-   [ ] Testar um depósito.

------------------------------------------------------------------------

# 🧠 Exercício extra de atenção

Analise este código **sem executar primeiro**:

``` java
int numero = 1;

switch (numero) {
    case 1:
        System.out.println("A");

    case 2:
        System.out.println("B");

    case 3:
        System.out.println("C");
        break;

    default:
        System.out.println("D");
}
```

Responda:

1.  O que será impresso?
2.  Por que mais de uma mensagem pode aparecer?
3.  O que mudaria se fosse colocado um `break` depois do `case 1`?
4.  O que mudaria se fosse colocado um `break` depois do `case 2`?

**Não altere o código ainda. Primeiro tente prever o resultado.**
