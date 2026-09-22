# Exercícios — Bloco 03: Arrays

## Como fazer

Resolva os exercícios no IntelliJ.

Não procure a solução antes de tentar.

A progressão vai do básico até Arrays multidimensionais e combina o conteúdo das aulas do bloco.

---

# 🟢 FÁCIL

## Exercício 01 — Criando um Array

Crie um Array de `int` com 5 posições.

Imprima o valor de todas as posições.

### Requisitos

- Use `new`.
- Utilize `for`.
- Não informe os valores manualmente.

---

## Exercício 02 — Preenchendo posições

Crie:

```java
int[] numeros = new int[5];
```

Coloque os valores:

```text
10
20
30
40
50
```

nas posições do Array.

Depois, imprima todos os valores.

### Requisitos

- Use os índices para atribuir os valores.
- Use `for` para imprimir.

---

## Exercício 03 — Nomes

Crie um Array de `String` com 4 posições.

Adicione quatro nomes e depois percorra o Array imprimindo cada nome.

### Requisitos

- Use `String[]`.
- Use `length` no `for`.
- Não coloque o número `4` diretamente na condição do `for`.

---

## Exercício 04 — Valor padrão

Crie:

```java
int[] numeros = new int[5];
```

Sem atribuir nenhum valor manualmente, percorra o Array e imprima suas posições.

### Pergunta

Por que todos os valores aparecem como `0`?

---

# 🟡 MÉDIO

## Exercício 05 — Números pares

Crie um Array:

```java
int[] numeros = {2, 4, 6, 8, 10, 12};
```

Percorra o Array utilizando `for` e imprima somente os números maiores que `6`.

---

## Exercício 06 — Maior valor

Crie um Array de `int` com 8 números.

Percorra o Array e descubra qual é o maior valor.

No final, imprima somente o maior valor.

### Requisitos

- Use `for`.
- Use uma variável para armazenar o maior valor.
- Não utilize métodos prontos para encontrar o maior.

---

## Exercício 07 — Soma do Array

Crie um Array com 10 números inteiros.

Percorra o Array e calcule a soma de todos os elementos.

No final, imprima somente a soma.

### Requisitos

- Use `for`.
- Use `.length`.
- Crie uma variável acumuladora.

---

## Exercício 08 — Foreach

Crie um Array com 5 nomes.

Percorra o Array utilizando exclusivamente `foreach`.

Imprima:

```text
Nome: Maria
Nome: João
...
```

### Regra

Não utilize `array[i]` neste exercício.

---

# 🟠 DIFÍCIL

## Exercício 09 — Índice e valor

Crie um Array com 10 números.

Utilizando `for`, imprima:

```text
Índice 0: valor
Índice 1: valor
Índice 2: valor
...
```

### Requisito

O índice deve vir da variável do `for`.

---

## Exercício 10 — Array invertido

Crie um Array com 8 números.

Imprima os elementos começando pela última posição e terminando na primeira.

Exemplo:

```text
Array original:
1 2 3 4 5

Resultado:
5
4
3
2
1
```

### Requisito

- Use `for`.
- Não crie outro Array.

---

## Exercício 11 — Contando ocorrências

Crie um Array de inteiros contendo números repetidos.

Exemplo:

```java
int[] numeros = {2, 5, 2, 8, 2, 10, 5};
```

Conte quantas vezes o número `2` aparece.

Resultado esperado:

```text
3
```

### Requisitos

- Use `for`.
- Use uma variável contador.

---

## Exercício 12 — Array multidimensional

Crie um Array bidimensional:

```java
int[][] numeros = new int[3][3];
```

Preencha suas posições com números de sua escolha.

Depois, utilize dois `for` para imprimir todos os valores.

### Requisito

Utilize:

```java
numeros[i][j]
```

para acessar os valores.

---

# 🔴 DESAFIO FINAL

## Exercício 13 — Processando uma matriz

Crie o seguinte Array multidimensional:

```java
int[][] numeros = {
    {10, 20, 30},
    {40, 50, 60},
    {70, 80, 90}
};
```

Faça um programa que:

1. Imprima todos os valores.
2. Calcule a soma de todos os valores.
3. Descubra o maior valor.
4. Conte quantos valores são pares.
5. Imprima os resultados no final.

### Requisitos

- Utilize `for` aninhado.
- Utilize `.length`.
- Não utilize métodos prontos para calcular soma ou maior valor.

### Bônus

Depois de terminar, refaça a parte de impressão utilizando `foreach`:

```java
for (int[] linha : numeros) {
    for (int valor : linha) {
        // ...
    }
}
```

---

# 🧠 Perguntas para testar se você realmente entendeu

Sem olhar o README, responda:

### 1.
Se temos:

```java
int[] numeros = new int[5];
```

quais são os índices válidos?

### 2.
Qual é a diferença entre:

```java
i < numeros.length
```

e:

```java
i <= numeros.length
```

### 3.
O que acontece se tentarmos acessar:

```java
numeros[5]
```

em um Array de tamanho `5`?

### 4.
Qual é a principal diferença entre:

```java
for (int i = 0; i < numeros.length; i++)
```

e:

```java
for (int numero : numeros)
```

### 5.
Em:

```java
int[][] numeros
```

o que representa cada um destes índices?

```java
numeros[i][j]
```

### 6.
Por que este código é importante em Arrays multidimensionais?

```java
numeros[i].length
```

### 7.
Um Array pode aumentar de tamanho durante a execução?

Explique o motivo.

---

# Ordem recomendada

Faça nessa ordem:

```text
01 → 02 → 03 → 04
          ↓
05 → 06 → 07 → 08
          ↓
09 → 10 → 11 → 12
          ↓
       13 🚀
```

Quando terminar, envie seu código dos exercícios. A correção será feita apontando primeiro o erro e dando uma dica para você tentar corrigir antes de receber a solução.


---

# 🟢 PARTE 2 — ARRAYS NORMAIS

Agora vamos aumentar bastante a quantidade de exercícios antes de entrar em Arrays multidimensionais.

## Exercício 14 — Média dos valores

Crie um Array com 5 números `double`.

Calcule a média de todos os valores e imprima o resultado.

---

## Exercício 15 — Menor valor

Crie um Array de inteiros com 8 valores.

Percorra o Array e descubra o menor valor.

Não utilize métodos prontos.

---

## Exercício 16 — Quantidade de pares

Crie um Array com 10 números inteiros.

Conte quantos números pares existem.

No final, imprima somente a quantidade.

---

## Exercício 17 — Quantidade de ímpares

Faça o mesmo exercício anterior, mas conte somente os números ímpares.

---

## Exercício 18 — Soma dos pares

Crie um Array com 10 números.

Some somente os valores pares.

---

## Exercício 19 — Soma dos ímpares

Crie um Array com 10 números.

Some somente os valores ímpares.

---

## Exercício 20 — Positivos e negativos

Crie um Array contendo números positivos, negativos e zero.

Conte separadamente:

- Quantos são positivos.
- Quantos são negativos.
- Quantos são zero.

---

## Exercício 21 — Substituindo valores

Crie um Array:

```java
int[] numeros = {1, 2, 3, 4, 5};
```

Percorra o Array e substitua todos os números ímpares por `0`.

Depois imprima o Array.

Resultado esperado:

```text
0
2
0
4
0
```

---

## Exercício 22 — Dobro dos valores

Crie um Array com 6 números.

Percorra o Array e substitua cada elemento pelo seu dobro.

Exemplo:

```text
Original:
1 2 3 4 5 6

Resultado:
2 4 6 8 10 12
```

---

## Exercício 23 — Array invertido

Crie um Array com 10 números.

Crie um segundo Array e coloque nele os valores do primeiro Array em ordem inversa.

No final, imprima o segundo Array.

---

## Exercício 24 — Comparando Arrays

Crie dois Arrays de mesmo tamanho.

Exemplo:

```java
int[] a = {1, 2, 3, 4, 5};
int[] b = {1, 2, 0, 4, 8};
```

Compare posição por posição e conte quantas posições possuem o mesmo valor.

Resultado do exemplo:

```text
3
```

---

## Exercício 25 — Elemento procurado

Crie um Array de inteiros e uma variável:

```java
int numeroProcurado = 7;
```

Percorra o Array procurando esse número.

Se encontrar, imprima:

```text
Número encontrado
```

Caso contrário:

```text
Número não encontrado
```

---

## Exercício 26 — Índice do elemento

Faça o exercício anterior, mas agora, quando encontrar o número, imprima também seu índice.

Exemplo:

```text
Número encontrado no índice: 4
```

### Desafio extra

Se o número aparecer várias vezes, imprima todos os índices em que ele aparece.

---

## Exercício 27 — Primeiro e último elemento

Crie um Array com 10 números.

Imprima:

```text
Primeiro elemento: ...
Último elemento: ...
```

### Requisito

Utilize `.length` para descobrir a última posição.

---

## Exercício 28 — Copiando Array manualmente

Crie um Array com 5 números.

Crie outro Array com o mesmo tamanho.

Copie os valores do primeiro para o segundo utilizando `for`.

Não utilize métodos prontos de cópia.

---

## Exercício 29 — Array com valores crescentes

Crie um Array com 10 posições.

Preencha o Array utilizando um `for`, de forma que ele fique:

```text
1
2
3
4
5
6
7
8
9
10
```

Depois percorra e imprima os valores.

---

## Exercício 30 — Array com valores pares

Crie um Array com 10 posições.

Preencha-o utilizando um `for` para que fique:

```text
2
4
6
8
10
12
14
16
18
20
```

Depois percorra e imprima.

---

# 🟡 PARTE 3 — FOREACH

## Exercício 31 — Soma com foreach

Crie um Array de inteiros.

Calcule a soma utilizando exclusivamente `foreach`.

---

## Exercício 32 — Maior valor com foreach

Crie um Array de inteiros.

Descubra o maior valor utilizando `foreach`.

Não utilize o índice.

---

## Exercício 33 — Contando valores

Crie um Array de inteiros.

Utilizando `foreach`, conte quantos valores são maiores que `50`.

---

## Exercício 34 — Modificação com foreach

Tente resolver o seguinte:

Crie um Array de inteiros e tente dobrar todos os valores utilizando `foreach`.

Depois observe o resultado.

### Pergunta

Por que simplesmente alterar a variável do `foreach` não modifica necessariamente o elemento do Array?

> O objetivo aqui é entender uma limitação importante do uso do `foreach`.

---

## Exercício 35 — Escolhendo entre for e foreach

Para cada situação abaixo, diga se você prefere `for` tradicional ou `foreach` e explique o motivo:

### A

Você precisa imprimir todos os nomes.

### B

Você precisa saber o índice de cada nome.

### C

Você precisa alterar elementos do Array utilizando suas posições.

### D

Você só precisa calcular a soma dos valores.

---

# 🟠 PARTE 4 — ARRAYS MULTIDIMENSIONAIS

Agora começa a segunda parte do bloco.

## Exercício 36 — Matriz 2x2

Crie:

```java
int[][] numeros = new int[2][2];
```

Preencha todas as posições e imprima utilizando dois `for`.

---

## Exercício 37 — Matriz 3x3

Crie uma matriz `3x3` e preencha com os números de `1` até `9`.

Imprima no formato:

```text
1 2 3
4 5 6
7 8 9
```

---

## Exercício 38 — Soma da matriz

Crie uma matriz `3x3`.

Calcule a soma de todos os elementos.

---

## Exercício 39 — Maior valor da matriz

Crie uma matriz `3x3`.

Percorra todas as posições e descubra o maior valor.

---

## Exercício 40 — Contando pares na matriz

Crie uma matriz `4x4`.

Conte quantos números pares existem.

---

## Exercício 41 — Soma de cada linha

Crie uma matriz:

```text
1 2 3
4 5 6
7 8 9
```

Calcule a soma de cada linha.

Resultado:

```text
Linha 0: 6
Linha 1: 15
Linha 2: 24
```

---

## Exercício 42 — Soma de cada coluna

Utilize a mesma matriz.

Calcule a soma de cada coluna.

Resultado:

```text
Coluna 0: 12
Coluna 1: 15
Coluna 2: 18
```

---

## Exercício 43 — Diagonal principal

Crie uma matriz `4x4`.

Imprima somente os elementos da diagonal principal.

Dica:

A diagonal principal possui posições em que:

```text
linha == coluna
```

---

## Exercício 44 — Diagonal secundária

Crie uma matriz `4x4`.

Imprima somente os elementos da diagonal secundária.

Não procure a solução pronta: tente descobrir a relação entre linha, coluna e tamanho da matriz.

---

## Exercício 45 — Foreach em matriz

Crie uma matriz `3x3`.

Utilize dois `foreach` para imprimir todos os valores.

Estrutura esperada:

```java
for (int[] linha : numeros) {
    for (int valor : linha) {
        // ...
    }
}
```

---

## Exercício 46 — Matriz irregular

Crie uma matriz em que cada linha tenha um tamanho diferente.

Exemplo:

```text
Linha 0 → 2 elementos
Linha 1 → 3 elementos
Linha 2 → 4 elementos
```

Percorra toda a estrutura sem acessar índices inexistentes.

### Requisito

Use:

```java
numeros[i].length
```

para descobrir o tamanho de cada linha.

---

## Exercício 47 — Soma de matriz irregular

Utilizando a matriz do exercício anterior, calcule a soma de todos os valores.

### Requisito

Não assuma que todas as linhas possuem o mesmo tamanho.

---

# 🔴 PARTE 5 — DESAFIOS DE ARRAYS

## Exercício 48 — Separando pares e ímpares

Crie um Array com 10 números.

Crie dois novos Arrays:

```text
pares
impares
```

Coloque cada número no Array correspondente.

Depois imprima os dois Arrays.

---

## Exercício 49 — Segundo maior

Crie um Array de inteiros.

Descubra qual é o **segundo maior valor**.

### Regras

- Não ordene o Array.
- Não utilize métodos prontos.
- Percorra o Array utilizando `for`.

---

## Exercício 50 — Array sem repetição

Crie um Array contendo números repetidos.

Exemplo:

```text
1 2 2 3 4 4 5 5 5
```

Crie outro Array contendo somente os valores que aparecem pela primeira vez.

Resultado:

```text
1 2 3 4 5
```

### Desafio

Tente resolver sem utilizar `ArrayList`.

---

# 🚀 DESAFIO FINAL — SISTEMA DE NOTAS

Crie um programa que trabalhe com as notas de alunos.

Utilize um Array:

```java
double[] notas
```

com pelo menos 8 notas.

O programa deve:

1. Imprimir todas as notas.
2. Calcular a média.
3. Encontrar a maior nota.
4. Encontrar a menor nota.
5. Contar quantas notas estão acima da média.
6. Contar quantas notas estão abaixo da média.
7. Contar quantas notas são maiores ou iguais a `7`.
8. Imprimir os resultados.

### Regras

- Use `for`.
- Use `.length`.
- Não utilize métodos prontos para descobrir maior/menor.
- Não ordene o Array.

### Bônus

Refaça a parte de impressão utilizando `foreach`.

---

# 📌 Divisão recomendada do estudo

Não faça os 50 exercícios de uma vez.

### Etapa 1 — Arrays básicos

```text
01 → 04
```

### Etapa 2 — Manipulação

```text
05 → 13
```

### Etapa 3 — Mais prática com Arrays

```text
14 → 30
```

### Etapa 4 — Foreach

```text
31 → 35
```

### Etapa 5 — Multidimensionais

```text
36 → 47
```

### Etapa 6 — Desafios

```text
48 → 50
```

### Etapa 7 — Desafio final

```text
Sistema de Notas 🚀
```

**Objetivo:** chegar ao desafio final conseguindo trabalhar com Arrays sem precisar copiar uma solução pronta.
