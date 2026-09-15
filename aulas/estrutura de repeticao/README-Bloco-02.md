# Bloco 02 — Estruturas de Repetição

## 🎯 Objetivo

Este bloco reúne as aulas **27 a 31** da Maratona Java Virado no Jiraya.

O objetivo é aprender a repetir um trecho de código enquanto uma determinada condição for atendida e controlar quando uma repetição deve continuar ou parar.

### Aulas do bloco

1. **Aula 27** — Laços de repetição: `while`, `do while` e `for`
2. **Aula 28** — Exercício com `for`
3. **Aula 29** — `break`
4. **Aula 30** — Exercício com `break`
5. **Aula 31** — `continue`

> A ideia deste material é estudar o **assunto como um bloco**, e não fazer uma lista de exercícios independente para cada vídeo.

---

# 1. Por que existem estruturas de repetição?

Sem um laço, quando precisamos executar o mesmo código várias vezes, teríamos que repetir as mesmas instruções manualmente.

As estruturas de repetição permitem dizer ao programa:

```text
repita este código
enquanto determinada condição for verdadeira
```

No bloco são apresentadas três estruturas principais:

```text
while
do while
for
```

---

# 2. `while`

O `while` verifica a condição antes de executar o bloco.

```java
while (condicao) {
    // código
}
```

Fluxo:

```text
        condição
            ↓
        true?
       /     \
     SIM     NÃO
      ↓       ↓
   executa   termina
      ↓
 volta para a condição
```

Exemplo:

```java
int contador = 0;

while (contador < 10) {
    System.out.println(contador);
    contador++;
}
```

### ⚠️ Cuidado com loop infinito

A condição precisa eventualmente deixar de ser verdadeira.

Exemplo problemático:

```java
int contador = 0;

while (contador < 10) {
    System.out.println(contador);
}
```

Nesse caso, `contador` nunca muda.

A condição continuará sendo:

```text
contador < 10
```

para sempre.

Por isso, é importante alterar a variável de controle quando necessário.

---

# 3. `do while`

O `do while` funciona de maneira diferente do `while`.

Primeiro executa o bloco e **depois** verifica a condição.

```java
do {
    // código
} while (condicao);
```

Fluxo:

```text
     executa
        ↓
     condição
        ↓
     true?
    /     \
  SIM     NÃO
   ↓       ↓
repete   termina
```

A principal característica apresentada no bloco:

> O `do while` executa o bloco **pelo menos uma vez**, mesmo que a condição inicialmente seja falsa.

Exemplo:

```java
int contador = 12;

do {
    System.out.println(contador);
    contador++;
} while (contador < 10);
```

Mesmo com `contador < 10` sendo falso inicialmente, o `System.out.println` será executado uma vez.

---

# 4. `for`

O `for` é apresentado como uma estrutura muito utilizada quando existe uma variável de controle e sabemos ou conseguimos definir uma condição para a repetição.

Sintaxe:

```java
for (inicialização; condição; alteração) {
    // código
}
```

Exemplo:

```java
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}
```

O `for` possui três partes:

```text
for (
    inicialização;
    condição;
    alteração
)
```

### Inicialização

Define a variável utilizada pelo laço:

```java
int i = 0
```

### Condição

Define até quando o laço continua:

```java
i < 10
```

### Alteração

Modifica a variável de controle:

```java
i++
```

Fluxo mental:

```text
inicializa
    ↓
verifica condição
    ↓
true?
 ↓
executa
 ↓
altera variável
 ↓
volta para condição
```

---

# 5. Incremento e decremento

O bloco utiliza operações como:

```java
i++;
```

para incrementar uma variável.

Também podemos decrementar:

```java
i--;
```

Isso permite controlar laços que caminham em sentidos diferentes.

Exemplo:

```java
for (int i = 10; i >= 0; i--) {
    System.out.println(i);
}
```

---

# 6. `while`, `do while` e `for`

Os três podem realizar repetições, mas possuem estruturas diferentes.

| Estrutura | Quando verifica a condição |
|---|---|
| `while` | antes da execução |
| `do while` | depois da execução |
| `for` | condição faz parte da estrutura do laço |

Modelo mental:

```text
while
→ verifica → executa

do while
→ executa → verifica

for
→ inicializa → verifica → executa → altera → verifica...
```

O bloco também destaca que existem diferentes formas de resolver um mesmo problema. A escolha depende do contexto.

---

# 7. Exercício com `for`

A aula 28 utiliza um exercício para praticar o `for`.

Um dos exemplos trabalhados é percorrer uma faixa de números e imprimir os números pares.

A ideia é utilizar a repetição para evitar escrever cada número manualmente.

Exemplo conceitual:

```text
1
2
3
4
5
...
100
```

e verificar quais valores atendem à regra desejada.

---

# 8. `break`

O `break` permite **interromper imediatamente um laço de repetição**.

Exemplo:

```java
for (int i = 0; i < 50; i++) {

    if (i > 25) {
        break;
    }

    System.out.println(i);
}
```

Quando o `break` é executado:

```text
laço
 ↓
condição encontrada
 ↓
break
 ↓
sai do laço
```

O restante das iterações não acontece.

### O ponto principal

`break` significa:

```text
"pare o laço agora"
```

No conteúdo também é mencionado que `break` pode ser utilizado em estruturas de repetição e em `switch`.

---

# 9. `break` com uma condição

Uma utilização comum é parar o processamento quando determinada situação acontece.

Exemplo conceitual:

```java
for (int i = 0; i < 100; i++) {

    if (algumaCondicao) {
        break;
    }

    // processamento
}
```

Isso pode evitar que o programa continue executando iterações desnecessárias.

---

# 10. `continue`

O `continue` possui um comportamento diferente do `break`.

Enquanto:

```text
break
→ sai do laço
```

o:

```text
continue
→ ignora o restante daquela iteração
→ continua o laço
```

Exemplo:

```java
for (int i = 0; i < 10; i++) {

    if (i == 5) {
        continue;
    }

    System.out.println(i);
}
```

Quando `i` for `5`, o `continue` faz o programa pular o restante daquela iteração.

O laço **não termina**.

---

# 11. `break` x `continue`

| Comando | Comportamento |
|---|---|
| `break` | encerra o laço |
| `continue` | pula a iteração atual e continua o laço |

Pense assim:

```text
break
↓↓↓
"pare tudo"

continue
↓↓↓
"pule essa e vá para a próxima"
```

---

# 12. Controle do laço

Uma parte importante deste bloco é entender que a variável utilizada para controlar o laço precisa evoluir de acordo com a lógica do problema.

Exemplo crescente:

```java
int parcela = 1;

while (parcela <= 10) {
    parcela++;
}
```

Exemplo decrescente:

```java
int parcela = 30000;

while (parcela >= 1000) {
    parcela--;
}
```

O importante é compreender:

```text
valor inicial
     ↓
condição
     ↓
execução
     ↓
alteração
     ↓
nova condição
```

---

# 13. O que você precisa dominar

Ao terminar este bloco, você deve conseguir:

- [ ] Explicar o objetivo de um laço de repetição.
- [ ] Criar um `while`.
- [ ] Criar um `do while`.
- [ ] Criar um `for`.
- [ ] Explicar a diferença entre `while` e `do while`.
- [ ] Entender as três partes do `for`.
- [ ] Utilizar incremento `++`.
- [ ] Utilizar decremento `--`.
- [ ] Criar um laço que percorra uma faixa de valores.
- [ ] Utilizar `break` para interromper um laço.
- [ ] Utilizar `continue` para pular uma iteração.
- [ ] Diferenciar `break` de `continue`.
- [ ] Identificar situações que podem gerar um loop infinito.

---

# 🧠 Modelo mental do bloco

```text
QUERO REPETIR ALGO
       ↓
Escolho o laço
       ↓
while / do while / for
       ↓
Defino a condição
       ↓
Executo
       ↓
Atualizo a variável de controle
       ↓
Preciso parar antes?
       ↓
     SIM
    /   \
 break  continue
   ↓       ↓
 sai     pula
```

---

# 📌 Limite deste bloco

Este bloco deve focar apenas em:

- `while`
- `do while`
- `for`
- incremento/decremento
- `break`
- `continue`

**Arrays começam no bloco seguinte**, a partir da Aula 32.

---

## 📚 Aulas utilizadas

- Aula 27 — `hEl67ZAU_Hg`
- Aula 28 — `nvFAe05Ok3g`
- Aula 29 — `bcv-cgpw7Ak`
- Aula 30 — `RaEO2YsqFII`
- Aula 31 — `f65R-lQ4NpY`

Fonte principal: transcrições das aulas da Maratona Java Virado no Jiraya.
