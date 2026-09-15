# Exercícios — Bloco 02: Estruturas de Repetição

## 🎯 Objetivo

Aqui não teremos 10 exercícios por vídeo.

As aulas **27 a 31 formam um único conteúdo**, então os exercícios foram organizados de forma progressiva para praticar o bloco inteiro sem repetir o mesmo exercício várias vezes.

### Progressão

- 🟢 4 fáceis
- 🟡 4 médios
- 🔴 4 difíceis
- 🏆 1 desafio final

**Total: 13 exercícios**

> Não veja a solução antes de tentar. Faça os exercícios no IntelliJ e envie seu código para correção.

---

# 🟢 FÁCIL

## Exercício 01 — Contagem com `while`

Crie um programa que utilize `while` para imprimir os números de:

```text
1
2
3
...
10
```

### Requisitos

- Utilize `while`.
- Crie uma variável para controlar a repetição.
- Utilize incremento.

---

## Exercício 02 — Contagem regressiva

Crie um programa que utilize `while` para imprimir:

```text
10
9
8
...
0
```

### Requisitos

- Utilize `while`.
- Utilize decremento.
- A condição deve controlar o momento em que o laço termina.

---

## Exercício 03 — `for` de 1 até 20

Utilize `for` para imprimir todos os números de `1` até `20`.

Exemplo:

```text
1
2
3
...
20
```

### Requisitos

- Utilize `for`.
- Não escreva os números manualmente.

---

## Exercício 04 — Números pares

Utilize `for` para imprimir todos os números pares de `1` até `100`.

Exemplo inicial:

```text
2
4
6
8
...
```

### Requisitos

- Utilize `for`.
- Faça uma condição para identificar os números pares.

---

# 🟡 MÉDIO

## Exercício 05 — Soma dos números

Crie um programa que percorra os números de `1` até `100` e calcule a soma de todos eles.

No final, imprima somente o resultado da soma.

### Requisitos

- Utilize `for`.
- Crie uma variável para armazenar a soma.
- Não calcule o resultado manualmente.

---

## Exercício 06 — `do while`

Crie um programa que utilize `do while`.

A variável deve começar com um valor que torne a condição inicialmente falsa.

Mesmo assim, o programa deverá imprimir o valor **uma vez**.

### Objetivo

Demonstrar na prática a principal diferença entre:

```text
while
```

e:

```text
do while
```

---

## Exercício 07 — Encontrando um valor com `break`

Crie um `for` que percorra os números de `1` até `100`.

Quando encontrar o número `37`:

1. Imprima o número.
2. Encerre o laço utilizando `break`.

### Objetivo

Comprovar que o `break` encerra a repetição imediatamente.

---

## Exercício 08 — Pulando números com `continue`

Crie um `for` de `1` até `20`.

Não imprima os números pares.

Utilize:

```java
continue;
```

para pular as iterações correspondentes aos números pares.

O resultado deverá conter somente:

```text
1
3
5
7
...
19
```

---

# 🔴 DIFÍCIL

## Exercício 09 — Contagem com regra

Crie um programa que percorra os números de `1` até `100`.

Para cada número:

- se for múltiplo de `3`, não imprima o número;
- utilize `continue` para pular essa iteração;
- os demais números devem ser impressos normalmente.

### Requisito

A decisão de pular o número deve ser feita dentro do laço.

---

## Exercício 10 — Soma até ultrapassar um limite

Crie um programa que percorra números começando em `1` e vá acumulando esses valores em uma variável.

Quando a soma atingir ou ultrapassar `500`:

- imprima a soma;
- encerre o laço utilizando `break`.

### Objetivo

Praticar uma situação em que você **não sabe previamente qual será a última iteração**.

---

## Exercício 11 — Parcelamento

Uma pessoa possui um valor de:

```text
R$ 30.000
```

Crie um programa para verificar quantas parcelas são possíveis começando com uma parcela de `R$ 30.000`.

A cada repetição, reduza o valor da parcela.

Quando a parcela ficar **menor que R$ 1.000**:

- pare o processamento utilizando `break`.

### Requisitos

- Utilize um laço.
- Utilize decremento.
- Utilize `break`.
- Mostre o valor da parcela a cada iteração válida.

---

## Exercício 12 — Escolhendo o laço

Resolva o mesmo problema três vezes.

Crie um programa que imprima os números de `1` até `10` utilizando:

### Parte 1

```text
while
```

### Parte 2

```text
do while
```

### Parte 3

```text
for
```

Depois, observe as três implementações e identifique:

- qual possui a inicialização mais explícita;
- qual verifica a condição antes da primeira execução;
- qual garante uma primeira execução antes da condição.

### Objetivo

Não é apenas fazer funcionar.

Você deve entender a diferença estrutural entre os três laços.

---

# 🏆 DESAFIO FINAL

## Exercício 13 — Sistema de processamento de parcelas

Crie um programa que simule o processamento de parcelas.

Comece com:

```text
valor da parcela = 30.000
```

A cada iteração:

1. Verifique o valor da parcela.
2. Se a parcela for menor que `1.000`, encerre o processamento utilizando `break`.
3. Se a parcela não for divisível por `2`, utilize `continue` para ignorar o processamento daquela parcela.
4. Caso contrário, imprima o valor da parcela.
5. Reduza o valor da parcela para a próxima iteração.

### Regras importantes

Você deverá utilizar:

- um laço de repetição;
- decremento;
- `break`;
- `continue`;
- uma condição para controlar o processamento.

### 🧠 Desafio extra

Depois de fazer funcionar, tente responder:

> Em que ponto do código a variável precisa ser alterada para evitar que o `continue` faça o programa ficar preso em um loop infinito?

Não precisa escrever a resposta no código. Quero que você consiga explicar isso quando enviar a solução.

---

# ✅ Checklist final

Antes de considerar o bloco concluído, tente marcar:

- [ ] Sei usar `while`.
- [ ] Sei usar `do while`.
- [ ] Sei usar `for`.
- [ ] Sei explicar a diferença entre `while` e `do while`.
- [ ] Entendo as três partes do `for`.
- [ ] Sei utilizar `++`.
- [ ] Sei utilizar `--`.
- [ ] Sei controlar uma variável dentro do laço.
- [ ] Sei utilizar `break`.
- [ ] Sei utilizar `continue`.
- [ ] Sei explicar a diferença entre `break` e `continue`.
- [ ] Consigo identificar risco de loop infinito.
- [ ] Consigo escolher um laço adequado para um problema simples.

---

## 📌 Regra da nossa correção

Quando você enviar um exercício:

1. Eu verifico se o código funciona.
2. Verifico se você utilizou a estrutura pedida.
3. Verifico a lógica.
4. Aponto erros e melhorias.
5. **Não vou simplesmente entregar a solução**, a menos que você peça.

Se houver um erro, primeiro vou explicar **onde está o problema e dar uma dica** para você tentar corrigir sozinho.
