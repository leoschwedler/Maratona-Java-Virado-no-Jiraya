# Bloco 03 — Arrays

## 1. Objetivo

Neste bloco você vai aprender a trabalhar com **Arrays em Java**, desde a declaração e criação até a iteração com `for` e `foreach`, além de Arrays multidimensionais.

O conteúdo foi organizado a partir das transcrições das aulas **32 a 38** da Maratona Java Virado no Jiraya. A sequência do material identifica as aulas 32–35 como Arrays e 36–38 como Arrays multidimensionais. fileciteturn23file0L17-L23

### Aulas do bloco

- Aula 32 — Arrays pt 01
- Aula 33 — Arrays pt 02
- Aula 34 — Arrays pt 03
- Aula 35 — Arrays pt 04 — Foreach
- Aula 36 — Multidimensional Arrays pt 01
- Aula 37 — Multidimensional Arrays pt 02 — Foreach
- Aula 38 — Arrays Multidimensionais pt 03 — Inicialização

---

# 2. O que é um Array?

Um Array permite armazenar vários valores relacionados, mantendo-os dentro de uma mesma estrutura.

Exemplo: em vez de criar:

```java
int idade1;
int idade2;
int idade3;
```

podemos criar:

```java
int[] idades;
```

A variável `idades` é uma **variável de referência** que pode apontar para um objeto Array na memória.

> No conteúdo das aulas, Array é tratado como objeto, mesmo quando seus elementos são de tipos primitivos.

---

# 3. Declaração de um Array

A forma apresentada nas aulas é:

```java
int[] idades;
```

Também é possível escrever:

```java
int idades[];
```

As duas formas funcionam, mas a primeira é apresentada como a forma recomendada:

```java
int[] idades;
```

Isso deixa mais evidente que estamos trabalhando com um Array de `int`.

---

# 4. Criando o Array

Declarar a variável não cria o Array.

Para criar o objeto e definir seu tamanho:

```java
int[] idades = new int[3];
```

Nesse caso:

- `int[]` → tipo da variável
- `idades` → nome da variável
- `new int[3]` → cria um Array com 3 posições

Um Array possui tamanho definido quando é criado.

---

# 5. Índices

Os Arrays são indexados a partir de **zero**.

Se temos:

```java
int[] idades = new int[3];
```

temos:

```text
índice:   0    1    2
          |    |    |
valor:   ...  ...  ...
```

Existem 3 posições, mas os índices são `0`, `1` e `2`.

Portanto:

```java
idades[0]
idades[1]
idades[2]
```

são válidos.

Já:

```java
idades[3]
```

não existe nesse Array e provoca `ArrayIndexOutOfBoundsException`.

---

# 6. Colocando valores no Array

Podemos acessar uma posição pelo índice:

```java
idades[0] = 20;
idades[1] = 25;
idades[2] = 30;
```

Para ler:

```java
System.out.println(idades[0]);
```

Resultado:

```text
20
```

A mesma ideia funciona com `String`:

```java
String[] nomes = new String[3];

nomes[0] = "Maria";
nomes[1] = "João";
nomes[2] = "Ana";
```

---

# 7. Valores padrão

Quando um Array é criado, suas posições recebem valores padrão.

Para os principais tipos:

| Tipo | Valor padrão |
|---|---|
| `byte`, `short`, `int`, `long` | `0` |
| `float`, `double` | `0.0` |
| `char` | caractere nulo |
| `boolean` | `false` |
| tipos referência, como `String` | `null` |

Exemplo:

```java
int[] numeros = new int[3];

System.out.println(numeros[0]);
```

Resultado:

```text
0
```

E:

```java
String[] nomes = new String[3];

System.out.println(nomes[0]);
```

Resulta em:

```text
null
```

---

# 8. Tamanho do Array — `length`

Arrays possuem a propriedade:

```java
.length
```

Exemplo:

```java
int[] numeros = new int[5];

System.out.println(numeros.length);
```

Resultado:

```text
5
```

Isso é muito útil para percorrer o Array:

```java
for (int i = 0; i < numeros.length; i++) {
    System.out.println(numeros[i]);
}
```

## Atenção

`length` representa a quantidade de posições.

Se:

```java
numeros.length
```

é `5`, os índices válidos são:

```text
0, 1, 2, 3, 4
```

Por isso normalmente usamos:

```java
i < numeros.length
```

e não:

```java
i <= numeros.length
```

---

# 9. Array possui tamanho fixo

Depois de criado, o tamanho de um Array não aumenta ou diminui dinamicamente.

Por exemplo:

```java
String[] nomes = new String[3];
```

Esse Array possui 3 posições.

Não podemos simplesmente transformá-lo em um Array de 4 posições durante a execução.

Para ter outro tamanho, é necessário criar outro Array.

> No curso, coleções aparecem posteriormente como uma alternativa para situações em que precisamos de estruturas mais flexíveis.

---

# 10. Percorrendo um Array com `for`

Como já estudamos `for`, podemos usar o índice para percorrer todas as posições:

```java
String[] nomes = {"Maria", "João", "Ana"};

for (int i = 0; i < nomes.length; i++) {
    System.out.println(nomes[i]);
}
```

O `i` representa o índice atual.

A cada repetição:

```text
i = 0 → nomes[0]
i = 1 → nomes[1]
i = 2 → nomes[2]
```

---

# 11. `foreach`

O `foreach` permite percorrer os elementos de um Array sem controlar diretamente o índice.

Exemplo:

```java
String[] nomes = {"Maria", "João", "Ana"};

for (String nome : nomes) {
    System.out.println(nome);
}
```

A estrutura é:

```java
for (Tipo variavel : array) {
    // código
}
```

Nesse exemplo:

```java
String nome
```

é a variável temporária que recebe cada elemento.

O Array:

```java
nomes
```

é a estrutura que será percorrida.

## Diferença principal

Com `for` tradicional:

```java
for (int i = 0; i < nomes.length; i++) {
    System.out.println(nomes[i]);
}
```

você possui o **índice**.

Com `foreach`:

```java
for (String nome : nomes) {
    System.out.println(nome);
}
```

você recebe diretamente o **elemento**.

O `foreach` é especialmente útil quando você só precisa dos valores e não precisa saber o índice.

---

# 12. Arrays multidimensionais

Um Array multidimensional pode ser entendido como um Array que possui outros Arrays.

Exemplo:

```java
int[][] numeros = new int[3][3];
```

Podemos imaginar:

```text
        coluna
        0   1   2

linha 0 [ ] [ ] [ ]
linha 1 [ ] [ ] [ ]
linha 2 [ ] [ ] [ ]
```

Para acessar um valor, usamos dois índices:

```java
numeros[0][1]
```

O primeiro índice representa uma posição do Array externo.

O segundo representa uma posição do Array interno.

---

# 13. Arrays multidimensionais podem ter tamanhos diferentes

Um ponto importante apresentado nas aulas é que os Arrays internos podem possuir tamanhos diferentes.

Por exemplo:

```java
int[][] numeros = new int[3][];

numeros[0] = new int[2];
numeros[1] = new int[3];
numeros[2] = new int[4];
```

Podemos visualizar:

```text
posição 0 → [ ][ ]

posição 1 → [ ][ ][ ]

posição 2 → [ ][ ][ ][ ]
```

Isso significa que não devemos assumir que todas as linhas possuem necessariamente o mesmo tamanho.

Por isso, ao percorrer um Array multidimensional, é importante usar o `length` do Array interno.

---

# 14. Percorrendo Array multidimensional com `for`

Podemos usar dois `for`.

Exemplo:

```java
for (int i = 0; i < numeros.length; i++) {
    for (int j = 0; j < numeros[i].length; j++) {
        System.out.println(numeros[i][j]);
    }
}
```

O primeiro `for` percorre o Array externo.

O segundo percorre o Array interno correspondente à posição atual.

A ideia é:

```text
i = 0
    j = 0
    j = 1
    j = 2
    ...

i = 1
    j = 0
    j = 1
    j = 2
    ...
```

O `for` interno termina completamente antes de o `for` externo avançar para a próxima posição.

---

# 15. `foreach` com Array multidimensional

Como cada posição do Array externo contém outro Array, o tipo da variável do primeiro `foreach` também será um Array.

Exemplo:

```java
for (int[] linha : numeros) {
    for (int valor : linha) {
        System.out.println(valor);
    }
}
```

Aqui:

```java
int[] linha
```

recebe cada Array interno.

Depois:

```java
int valor
```

recebe cada número daquele Array interno.

---

# 16. Inicialização direta

Também podemos criar e inicializar um Array diretamente:

```java
int[] numeros = {1, 2, 3, 4, 5};
```

Para um Array multidimensional:

```java
int[][] numeros = {
    {1, 2},
    {3, 4, 5},
    {6, 7, 8, 9}
};
```

Nesse caso, os Arrays internos podem possuir quantidades diferentes de elementos.

---

# 17. Pontos que você precisa dominar

Antes de sair deste bloco, você deve conseguir explicar:

- O que é um Array.
- Como declarar um Array.
- Como criar um Array com `new`.
- O que significa o tamanho do Array.
- Por que os índices começam em `0`.
- Como acessar uma posição.
- Como alterar uma posição.
- O que acontece quando acessamos um índice inexistente.
- O que significa `array.length`.
- Por que usamos `i < array.length`.
- Como percorrer um Array com `for`.
- Como percorrer um Array com `foreach`.
- Diferença entre `for` e `foreach`.
- O que é um Array multidimensional.
- Como acessar `array[i][j]`.
- Como percorrer um Array multidimensional.
- Como usar `foreach` em Arrays multidimensionais.
- Como inicializar Arrays diretamente.
- Que Arrays internos podem ter tamanhos diferentes.

---

# 18. Checklist

- [ ] Consigo declarar um Array.
- [ ] Consigo criar um Array.
- [ ] Entendo que Array é uma referência para um objeto.
- [ ] Entendo índices começando em `0`.
- [ ] Consigo inserir valores.
- [ ] Consigo acessar valores.
- [ ] Sei usar `.length`.
- [ ] Sei percorrer com `for`.
- [ ] Sei percorrer com `foreach`.
- [ ] Sei explicar quando usar `for` ou `foreach`.
- [ ] Consigo criar Array multidimensional.
- [ ] Consigo acessar posições de Array multidimensional.
- [ ] Consigo percorrer Array multidimensional com `for`.
- [ ] Consigo percorrer Array multidimensional com `foreach`.
- [ ] Entendo que o tamanho do Array é fixo depois da criação.

---

## Regra mental para lembrar

Array normal:

```java
array[indice]
```

Array multidimensional:

```java
array[indice][indice]
```

Percorrer Array:

```java
for (int i = 0; i < array.length; i++)
```

Percorrer sem precisar do índice:

```java
for (Tipo valor : array)
```
