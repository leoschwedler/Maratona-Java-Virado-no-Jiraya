# Exercícios — Bloco 24: Streams (Fundamentos)

## Aulas 203 a 212

Este arquivo acompanha o README do Bloco 24.

```text
Aula
 ↓
3 exercícios
 ↓
você implementa
 ↓
manda o código
 ↓
eu corrijo
 ↓
próxima aula
```

### Estrutura de cada aula

- 🟢 1 exercício fácil
- 🟡 1 exercício médio
- 🔴 1 exercício difícil

No final existe um:

- 🏆 **Desafio Integrador do Bloco**

> **Onde criar os arquivos:** `src/main/bloco24_aulas203a212_streams_fundamentos/aulaXXX/`

> **Regras do bloco:**
> - Para cada exercício, faça **primeiro a versão imperativa (com laços)** e depois a **versão com streams**, e confira se dão o mesmo resultado.
> - `Collectors.groupingBy`, `partitioningBy`, `joining` etc. ainda não foram estudados: use apenas `collect(Collectors.toList())` e `toSet()` por enquanto.
> - Use method references sempre que a lambda apenas chamar um método.

### Base de dados sugerida para vários exercícios

Reaproveite esta classe nos exercícios:

```text
Livro: titulo, autor, genero, preco, paginas, ano
```

Crie uma lista de pelo menos **12 livros**, com gêneros repetidos, autores repetidos, preços variados, anos entre 1990 e 2024 e pelo menos 2 livros exatamente iguais (para testar `distinct`).

---

# Aula 203 — Introdução pt 01 (o problema)

## 🟢 Exercício 01 — Versão imperativa

Usando a lista de `Livro`, escreva **com laços** (sem streams) um método que devolva os **títulos dos 3 primeiros livros** (ordem alfabética de título) com **preço menor ou igual a 50**.

Anote em comentário quantos "passos" (ordenar, filtrar, limitar, extrair) o laço mistura.

---

## 🟡 Exercício 02 — Cinco requisitos, cinco laços

Escreva, também com laços, métodos para:

1. Quantidade de livros com mais de 300 páginas.
2. Lista dos autores (sem repetir) dos livros de "Ficção".
3. Soma dos preços dos livros publicados depois de 2010.
4. O livro mais caro.
5. Lista dos títulos em maiúsculas dos livros com preço entre 30 e 80.

Guarde esses métodos: você os reescreverá com streams.

---

## 🔴 Exercício 03 — Ranking manual

Sem streams, gere um relatório que:

1. Ordena os livros por ano (do mais recente) e, em empate, por título.
2. Pega os 5 primeiros.
3. Para cada um, calcula "preço por página".
4. Mostra os resultados formatados.

Em comentário, liste todos os efeitos colaterais da sua solução (ordenou a lista original? usou variáveis auxiliares? usou `break`?).

---

# Aula 204 — Introdução pt 02 (a solução funcional)

## 🟢 Exercício 04 — Primeira pipeline

Reescreva o exercício 01 com stream:

```text
sorted → filter → limit → map → collect
```

Confirme que o resultado é igual ao da versão imperativa e que a lista original **não** foi alterada.

---

## 🟡 Exercício 05 — Reescrevendo os cinco

Reescreva os cinco requisitos do exercício 02 com streams. Para cada um, indique em comentário qual é a **operação terminal** e quais são as **intermediárias**.

---

## 🔴 Exercício 06 — Anatomia do pipeline

Responda **executando código** (use `peek` para observar):

1. Monte um pipeline com `filter`, `map`, `sorted`, `limit(2)` e `collect`, e coloque `peek(System.out::println)` entre as etapas.
2. Observe a **ordem** em que os elementos passam pelas etapas. A execução é "etapa por etapa para todos os elementos" ou "elemento por elemento por todas as etapas"? (Dica: `sorted` muda isso.)
3. Remova a operação terminal e prove que **nada** é executado (laziness).
4. Mostre que `limit` pode interromper o processamento antes de percorrer todos os elementos (use contadores nas lambdas).

Explique com suas palavras o que significa "stream é preguiçoso".

---

# Aula 205 — Introdução pt 03

## 🟢 Exercício 07 — `count` e `distinct`

Responda com streams:

1. Quantos livros existem?
2. Quantos são distintos (`distinct`, usando `equals`/`hashCode` do `Livro`)?
3. Quantos autores diferentes existem?
4. Quantos gêneros diferentes?

---

## 🟡 Exercício 08 — O stream que fecha

1. Guarde um stream em uma variável, consuma-o com `forEach`, e tente usá-lo de novo. Capture a `IllegalStateException`.
2. Mostre a forma correta (obter um novo stream a partir da lista) e a forma ainda melhor (um único pipeline).
3. Explique por que o `Supplier<Stream<T>>` é uma forma de contornar isso quando você precisa reutilizar a *receita* do stream. Implemente um exemplo.

---

## 🔴 Exercício 09 — `forEach` do stream × `forEach` da lista

Compare, com `System.nanoTime` e 1 milhão de inteiros:

1. `lista.forEach(...)`.
2. `lista.stream().forEach(...)`.
3. um `for` tradicional.
4. `lista.stream().filter(...).forEach(...)`.

Imprima os tempos (rode cada medição 5 vezes e mostre a média) e discuta: quando criar um stream é desperdício? Escreva suas conclusões em comentário.

---

# Aula 206 — FlatMap pt 01 (⚠ aula sem transcrição)

## 🟢 Exercício 10 — Listas de listas

Dada `List<List<String>> turmas` com 3 turmas de 3 a 5 alunos, produza **uma única lista** com todos os alunos de duas maneiras:

1. com laços aninhados;
2. com `flatMap`.

Imprima os dois resultados e compare.

---

## 🟡 Exercício 11 — `map` × `flatMap`

Mostre a diferença **de tipo** (use `var` ou declare o tipo e deixe a IDE confirmar) entre:

```java
turmas.stream().map(t -> t)
turmas.stream().map(List::stream)
turmas.stream().flatMap(List::stream)
```

Imprima o `getClass()` do primeiro elemento de cada pipeline (use `findFirst`) e explique por que `map(List::stream)` resulta em `Stream<Stream<String>>`.

---

## 🔴 Exercício 12 — Pedidos e itens

Crie `Pedido` (`id`, `cliente`, `List<Item> itens`) e `Item` (`produto`, `quantidade`, `precoUnitario`).

Com `flatMap`, responda:

1. Quantos itens existem em todos os pedidos?
2. Quais produtos distintos foram vendidos?
3. Qual o valor total vendido?
4. Quais clientes compraram o produto "Mouse"?
5. Qual o item de maior valor total (quantidade × preço) de todos os pedidos?

---

# Aula 207 — FlatMap pt 02

## 🟢 Exercício 13 — Letras das palavras

Dada a lista `["java", "stream", "lambda"]`, devolva:

1. todas as letras;
2. as letras distintas, ordenadas.

Use `split("")`, `Arrays.stream` e `flatMap`.

---

## 🟡 Exercício 14 — Pares de números

Dadas `[1, 2, 3]` e `[3, 4]`, gere todos os pares `(a, b)` com `flatMap` + `map` (produto cartesiano). Depois mantenha só os pares cuja soma seja divisível por 3. Use `int[]` de duas posições (ou uma classe `Par`).

---

## 🔴 Exercício 15 — Texto para vocabulário

Dado um texto com várias linhas (uma `List<String>`), faça um pipeline que:

1. quebra cada linha em palavras (`split("\\s+")`);
2. remove pontuação e coloca em minúsculas;
3. descarta palavras com menos de 3 letras;
4. devolve as palavras distintas em ordem alfabética;
5. descubra a palavra mais longa;
6. conte o total de palavras (com repetição).

Teste com um texto de pelo menos 8 linhas (escreva você mesmo).

---

# Aula 208 — Finding e Matching

## 🟢 Exercício 16 — Perguntas de verdadeiro/falso

Responda com `anyMatch`, `allMatch` e `noneMatch`:

1. Existe algum livro com mais de 1000 páginas?
2. Todos os livros custam mais de R$ 0?
3. Nenhum livro foi publicado antes de 1900?
4. Todos os livros de "Terror" têm mais de 100 páginas?

---

## 🟡 Exercício 17 — Encontrando

Com `findFirst` e `findAny`:

1. O primeiro livro de "Fantasia" ordenado por preço.
2. Qualquer livro do autor "Tolkien".
3. O livro mais caro (de duas formas: `sorted` + `findFirst` e `max`).
4. Trate o caso de "não achou" com `orElse`, `orElseThrow` e `ifPresentOrElse`.

---

## 🔴 Exercício 18 — Equivalências lógicas

Prove com código que:

```text
allMatch(p)   ≡   !anyMatch(!p)   ≡   noneMatch(!p)
noneMatch(p)  ≡   !anyMatch(p)
```

Teste com 5 predicados diferentes (um deles com stream vazio!). Qual o resultado de `allMatch`, `anyMatch` e `noneMatch` em um stream **vazio**? Explique em comentário o porquê (lógica vacuamente verdadeira).

Depois use `peek` para provar que `anyMatch` e `allMatch` são **curto-circuitantes** (param antes de percorrer tudo).

---

# Aula 209 — Reduce pt 01

## 🟢 Exercício 19 — Soma, produto e máximo

Dada `[3, 7, 1, 9, 4, 6]`, calcule com `reduce`:

1. soma com identidade;
2. soma sem identidade (`Optional`);
3. produto;
4. maior valor (`Integer::max`);
5. menor valor (`Integer::min`).

Faça o mesmo em uma lista vazia e observe o retorno de cada forma.

---

## 🟡 Exercício 20 — Reduce com strings

Com `reduce`:

1. Concatene `["Java", "é", "legal"]` em `"Java é legal"` (cuidado com o espaço extra).
2. Encontre a string mais longa de uma lista.
3. Conte o total de letras de todas as strings.
4. Implemente `join` (como o `String.join`) usando só `reduce`.

---

## 🔴 Exercício 21 — A identidade importa

1. Mostre que usar `0` como identidade em um `reduce` de multiplicação dá sempre 0.
2. Mostre o que acontece se usar uma identidade "errada" (por exemplo `10`) em uma soma.
3. Escreva um `reduce` com a **3ª forma** (`reduce(identidade, acumulador, combinador)`) para calcular o total de caracteres de uma `List<String>` (resultado `Integer` partindo de `String`). Explique o papel do combinador.
4. Mostre o que acontece com o resultado de um `reduce` em `parallelStream()` com uma operação **não associativa** (por exemplo, subtração) e explique por que operações devem ser associativas.

---

# Aula 210 — Reduce pt 02

## 🟢 Exercício 22 — Soma de preços

Calcule o valor total de todos os livros acima de R$ 40, de duas maneiras:

1. `map(Livro::getPreco).reduce(Double::sum)`;
2. `mapToDouble(Livro::getPreco).sum()`.

Compare tipo de retorno e legibilidade.

---

## 🟡 Exercício 23 — Estatísticas

Com `summaryStatistics`, imprima mínimo, máximo, média, soma e contagem de:

1. preços;
2. páginas;
3. anos de publicação.

Depois obtenha a média de preços **só** dos livros de "Ficção" com `average` e trate o `OptionalDouble`.

---

## 🔴 Exercício 24 — Boxing em números

Com 5 milhões de números:

1. Some usando `Stream<Integer>` + `reduce(0, Integer::sum)`.
2. Some usando `IntStream` + `sum()`.
3. Some usando um `for` clássico.

Meça o tempo de cada um (média de 5 execuções, ignorando a primeira para aquecimento da JVM) e explique a diferença. Mostre também `boxed()` e `mapToObj` convertendo de volta.

---

# Aula 211 — Gerando streams pt 01

## 🟢 Exercício 25 — Faixas

Com `IntStream`:

1. Imprima os números de 1 a 20.
2. Imprima os múltiplos de 7 até 100 (inclusive).
3. Some os quadrados dos números de 1 a 10.
4. Mostre a diferença entre `range(1, 10)` e `rangeClosed(1, 10)`.

---

## 🟡 Exercício 26 — Várias fontes

Crie um stream de cada fonte e faça uma operação simples sobre ele:

```text
Stream.of(...)            → nomes em maiúsculas
Arrays.stream(int[])      → média
Arrays.stream(String[])   → ordenados
collection.stream()       → contagem
IntStream.rangeClosed     → soma
"texto".chars()           → quantas vogais
```

---

## 🔴 Exercício 27 — Analisando um arquivo

Crie um arquivo de texto com ~20 linhas de qualquer conteúdo (ou use um `.java` seu) e, com `Files.lines` e try-with-resources:

1. Conte as linhas.
2. Mostre as linhas que contêm uma palavra (ex.: "class").
3. Conte as linhas em branco.
4. Mostre a linha mais longa e seu tamanho.
5. Liste as 5 palavras mais longas (distintas).
6. Grave um relatório em outro arquivo (`Files.write` ou `BufferedWriter`).

---

# Aula 212 — Gerando streams pt 02 (infinitos)

## 🟢 Exercício 28 — Sequências

Com `Stream.iterate` e `limit`:

1. Os 10 primeiros múltiplos de 5.
2. Potências de 2 até 2^15.
3. Os 10 primeiros quadrados perfeitos.
4. Os pares de 20 a 40 (use a versão do Java 9 com `hasNext`, se disponível).

---

## 🟡 Exercício 29 — Fibonacci e fatorial

1. Gere os 15 primeiros números de Fibonacci com `iterate` sobre `long[]`.
2. Gere os fatoriais de 0! a 15! (use `iterate` com um par `(n, n!)`).
3. Encontre o primeiro número de Fibonacci maior que 1 000 000 (use `filter` + `findFirst` num stream infinito).
4. Quantos números de Fibonacci são menores que 1 000 000? (use `takeWhile` — Java 9+ — ou `limit` com contador).

---

## 🔴 Exercício 30 — Simulador com `generate`

Com `Stream.generate` e `Random` com semente fixa:

1. Simule 1000 lançamentos de dado e conte quantas vezes saiu cada face (use um array de 6 posições).
2. Gere 20 senhas aleatórias de 8 caracteres.
3. Simule uma "corrida": gere o tempo de 8 corredores e descubra o vencedor.
4. Descubra quantos lançamentos de dado são necessários, em média (em 1000 experimentos), até sair 6 duas vezes seguidas.

Explique por que `generate` não pode ser usado para Fibonacci (dependência do elemento anterior).

---

# 🏆 Desafio Integrador do Bloco 24 — Analisador de Vendas de uma Livraria Online

Você vai construir um sistema de análise com **apenas streams** (sem laços para processar dados).

## Dados

```text
Livro   → isbn, titulo, autor, genero, preco, paginas, ano
Cliente → id, nome, cidade
Venda   → id, cliente, data (LocalDate), List<ItemVenda>
ItemVenda → livro, quantidade
```

Gere um conjunto realista: 25 livros, 10 clientes, 60 vendas (use `Random` com semente fixa e `IntStream`/`Stream.generate` para criar os dados — o próprio gerador deve usar streams).

## Perguntas a responder (cada uma em um método)

1. Os 5 livros mais vendidos (por quantidade total).
2. O livro que mais faturou.
3. Faturamento total e ticket médio por venda (`DoubleSummaryStatistics`).
4. Autores distintos que venderam mais de 10 unidades.
5. Clientes que compraram livros de pelo menos 3 gêneros diferentes (use `flatMap`).
6. Existe alguma venda com mais de 10 itens? Todas as vendas têm ao menos 1 item? (matching)
7. A primeira venda de cada mês de 2024 (`findFirst` por mês, num stream de `Month`).
8. Mês de maior faturamento.
9. Lista de "livros nunca vendidos" (diferença de listas).
10. Total de páginas vendidas.
11. Os livros com preço acima da média (calcule a média primeiro).
12. Geração de um relatório em arquivo com `Files.write`/`BufferedWriter` a partir de um stream de linhas formatadas.
13. Uma "sequência de datas" do primeiro ao último dia com venda, e quantos dias sem nenhuma venda (use `Stream.iterate` e `ChronoUnit`).

## Regras

- Nenhum laço `for`/`while` para processar dados (somente para I/O, se necessário).
- Use method references em pelo menos metade das lambdas.
- Use `IntStream`/`mapToDouble` onde houver somas numéricas, evitando boxing.
- Documente (comentário) para cada pergunta quais operações são intermediárias e qual é a terminal.
- Compare, num comentário final, o tamanho (linhas) e a clareza da solução com streams × uma solução hipotética com laços.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei a diferença entre coleção e stream.
- [ ] Sei o que é fonte, operação intermediária e operação terminal.
- [ ] Sei que streams são preguiçosos e de uso único.
- [ ] Sei usar `filter`, `map`, `sorted`, `limit`, `distinct`, `collect`.
- [ ] Sei explicar o `flatMap` e quando usar.
- [ ] Sei usar `anyMatch`, `allMatch`, `noneMatch`, `findFirst`, `findAny`.
- [ ] Sei usar `reduce` com e sem identidade.
- [ ] Conheço `IntStream`, `LongStream`, `DoubleStream` e `mapToXxx`.
- [ ] Sei gerar streams com `range`, `of`, `Arrays.stream`, `Files.lines`.
- [ ] Sei criar streams infinitos com `iterate` e `generate` e limitá-los.

---

# Regra para as correções

Quando você mandar cada exercício, a correção seguirá esta ordem:

```text
1. Verificar se funciona
2. Verificar se você entendeu o conceito
3. Apontar problemas
4. Dar uma dica
5. Você tenta corrigir
6. Só mostrar a solução completa se necessário
```

Não vou simplesmente entregar o código pronto na primeira tentativa.

O objetivo é você **aprender o conteúdo e conseguir escrever o código sozinho**.
