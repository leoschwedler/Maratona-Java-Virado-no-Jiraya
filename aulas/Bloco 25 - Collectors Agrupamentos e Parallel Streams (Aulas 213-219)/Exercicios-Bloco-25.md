# Exercícios — Bloco 25: Collectors, Agrupamentos e Parallel Streams

## Aulas 213 a 219

Este arquivo acompanha o README do Bloco 25.

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

> **Onde criar os arquivos:** `src/main/bloco25_aulas213a219_collectors_parallel/aulaXXX/`

> **Base de dados para os exercícios:** crie `Funcionario` (`nome`, `departamento` enum, `cargo`, `salario`, `idade`, `cidade`, `dataAdmissao`) e uma lista com **pelo menos 20 funcionários**, com departamentos, cargos e cidades repetidos, e salários variados. Reaproveite essa lista em todos os exercícios.

> **Regras:** nada de laços para agrupar/somar; use `Collectors`. Quando o enunciado pedir "compare com a versão manual", escreva a versão com laços e `Map` para ver a diferença.

---

# Aula 213 — Collectors pt 01 (⚠ aula sem transcrição)

## 🟢 Exercício 01 — Coletores básicos

Com a lista de funcionários, obtenha com `collect`:

1. a lista de nomes;
2. o conjunto de cidades (sem repetição);
3. um `TreeSet` com os cargos em ordem alfabética (`toCollection`);
4. os nomes em uma única `String` separada por vírgula e envolvida por colchetes (`joining`).

---

## 🟡 Exercício 02 — Números com coletores

Calcule com coletores (sem `mapToDouble`):

1. quantidade de funcionários (`counting`);
2. soma dos salários (`summingDouble`);
3. salário médio (`averagingDouble`);
4. o mais bem pago e o mais novo (`maxBy` e `minBy`), tratando o `Optional`;
5. estatísticas completas de salário (`summarizingDouble`).

---

## 🔴 Exercício 03 — `toMap` e seus perigos

1. Monte `Map<String, Double>` nome → salário com `toMap`. Duplique um nome de propósito e capture a `IllegalStateException`; explique a mensagem.
2. Resolva o conflito com a função de fusão (`(a, b) -> a + b` somando os salários).
3. Monte `Map<Departamento, Double>` (soma dos salários por departamento) **apenas** com `toMap` (sem `groupingBy`), usando a função de fusão.
4. Escolha o tipo do mapa: `LinkedHashMap` ordenado por inserção e `TreeMap`.
5. Torne a lista final imutável com `collectingAndThen`.

---

# Aula 214 — Grouping by pt 01

## 🟢 Exercício 04 — Primeiro agrupamento

Agrupe os funcionários por **departamento** (`Map<Departamento, List<Funcionario>>`) e imprima cada departamento com seus funcionários.

Depois escreva (em outro método) a **versão manual** com `Map` e laços e compare as linhas de código.

---

## 🟡 Exercício 05 — Vários critérios

Faça agrupamentos por:

1. cidade;
2. cargo;
3. faixa etária (chave `String`: "até 25", "26-40", "acima de 40") — com lambda;
4. ano de admissão.

Imprima cada resultado de forma legível (use um método `imprimir(Map<?, ? extends Collection<?>>)`).

---

## 🔴 Exercício 06 — Mapa ordenado e imutável

1. Agrupe por departamento, mas retorne um `TreeMap` (chaves ordenadas) usando a versão de 3 argumentos de `groupingBy`.
2. Agrupe por cidade usando `LinkedHashMap` e compare a ordem das chaves com `HashMap` (rode com mais de 15 cidades).
3. Use `EnumMap` como fábrica do mapa para o agrupamento por departamento (enum). Explique a vantagem.
4. Transforme os valores em listas imutáveis (`collectingAndThen` como downstream).

---

# Aula 215 — Grouping by pt 02

## 🟢 Exercício 07 — Regra de negócio como chave

Crie o enum `FaixaSalarial { BAIXA, MEDIA, ALTA }` e agrupe os funcionários por faixa (ex.: < 3000, 3000-8000, > 8000) usando uma lambda e depois um método estático.

---

## 🟡 Exercício 08 — Dois níveis

Agrupe por **departamento** e, dentro de cada um, por **FaixaSalarial**:

```text
Map<Departamento, Map<FaixaSalarial, List<Funcionario>>>
```

Imprima de forma hierárquica (identada). Depois faça um terceiro nível por cidade e discuta a legibilidade. Use imports estáticos e *Extract Method* para melhorar.

---

## 🔴 Exercício 09 — Relatório hierárquico

Gere um relatório textual como:

```text
TI
  ALTA (2)
    - Ana (R$ 12.000,00)
    - Bruno (R$ 9.500,00)
  MEDIA (3)
    - ...
RH
  ...
```

Regras:

- Departamentos em ordem alfabética, faixas na ordem do enum, funcionários por nome.
- O número entre parênteses é a quantidade de pessoas.
- Faça com `groupingBy` aninhado (`TreeMap`/`EnumMap`) e um `forEach` aninhado.
- Grave o relatório em arquivo com `Files.write`.

---

# Aula 216 — Grouping by pt 03 (⚠ aula sem transcrição)

## 🟢 Exercício 10 — Contagens

Com `groupingBy` + `counting`:

1. Quantos funcionários por departamento?
2. Quantos por cidade?
3. Qual departamento tem mais funcionários? (ordene as entradas do mapa)

---

## 🟡 Exercício 11 — Somas, médias e extremos

Por departamento, calcule:

1. folha de pagamento (soma);
2. salário médio;
3. o funcionário mais bem pago (sem `Optional` no valor, use `collectingAndThen`);
4. o mais novo e o mais velho.

Apresente tudo em uma tabela formatada com `printf`.

---

## 🔴 Exercício 12 — `partitioningBy`

1. Separe funcionários em "ganha acima da média" e "ganha até a média" (calcule a média antes).
2. Separe em "mais de 5 anos de casa" e "menos".
3. Use `partitioningBy` com downstream `counting()` e `averagingDouble`.
4. Faça `partitioningBy` dentro de `groupingBy`: por departamento, quantos estão acima/abaixo da média **do departamento**.
5. Compare `partitioningBy` com `groupingBy(boolean)`: qual a diferença quando um dos grupos está vazio?

---

# Aula 217 — Grouping by pt 04

## 🟢 Exercício 13 — Estatísticas por grupo

Com `summarizingDouble`, imprima para cada departamento: mínimo, média, máximo, soma e quantidade de salários.

---

## 🟡 Exercício 14 — `mapping`

Por departamento, obtenha:

1. a lista de **nomes** dos funcionários;
2. o conjunto de **cidades** onde há funcionários;
3. os nomes em uma `String` separada por vírgula (`mapping` + `joining`);
4. os cargos distintos em `LinkedHashSet` (`toCollection`).

---

## 🔴 Exercício 15 — Pipeline analítico

Responda, usando apenas streams e coletores:

1. Para cada cidade: o departamento com mais funcionários.
2. Para cada departamento: a cidade onde os funcionários ganham em média mais.
3. Os 3 departamentos com maior folha salarial.
4. Para cada cargo: a idade média, arredondada em 1 casa.
5. `Map<Departamento, Map<String, Long>>`: para cada departamento, quantos funcionários em cada cidade.
6. Um `Map<Boolean, Map<Departamento, Double>>`: faixa "mais de 5 anos de casa" × salário médio por departamento.

Em comentário, desenhe o tipo de cada coletor (`classificador`, `downstream`).

---

# Aula 218 — Parallel Streams pt 01

## 🟢 Exercício 16 — Quantos processadores?

Imprima `Runtime.getRuntime().availableProcessors()`. Execute um `parallel` simples (somar 1 a 1000) e imprima o nome da thread em cada elemento (`Thread.currentThread().getName()`) para ver as threads do `ForkJoinPool.commonPool-worker-N`.

---

## 🟡 Exercício 17 — O experimento da aula

Some os números de 1 a 100 milhões de 5 formas e meça cada uma (média de 5 execuções, ignorando a primeira):

1. `for` clássico;
2. `Stream.iterate(1L, i -> i + 1).limit(N).reduce(0L, Long::sum)`;
3. o mesmo com `.parallel()`;
4. `LongStream.rangeClosed(1, N).sum()`;
5. o mesmo com `.parallel()`.

Imprima uma tabela comparativa e explique cada resultado.

---

## 🔴 Exercício 18 — Fontes que dividem bem e mal

Compare `parallel` x sequencial para uma operação **pesada** por elemento (ex.: contar primos até N para cada número de uma lista) em:

1. `ArrayList<Integer>` de 2 milhões;
2. `LinkedList<Integer>` com o mesmo conteúdo;
3. `HashSet<Integer>`;
4. `IntStream.range`;
5. `Stream.iterate` limitado.

Meça tudo, monte a tabela e explique por que as fontes "indexáveis" são melhores. (Dica: o conceito de *splitterator*.)

---

# Aula 219 — Parallel Streams pt 02

## 🟢 Exercício 19 — Quando NÃO usar

Meça `parallel` x sequencial para somar uma lista de **100 elementos**. Mostre que o paralelo é mais lento. Escreva em comentário três regras práticas que você extraiu.

---

## 🟡 Exercício 20 — Estado compartilhado

1. Escreva um `IntStream.range(0, 1_000_000).parallel().forEach(i -> contador[0]++)` e mostre que o resultado varia entre execuções.
2. Corrija de 3 maneiras: usando `AtomicLong`, usando `reduce`/`count`, e usando `sum()`.
3. Explique por que `reduce` com uma operação não associativa (subtração) dá resultados diferentes no paralelo.
4. Mostre `forEach` x `forEachOrdered` no paralelo e o custo de manter a ordem.

---

## 🔴 Exercício 21 — Benchmark honesto

Implemente um pequeno **harness de benchmark** reutilizável:

```text
static Resultado medir(String nome, Supplier<Object> tarefa, int aquecimentos, int medicoes)
```

que roda a tarefa algumas vezes para aquecer a JVM, mede as demais e devolve média, mínimo e máximo (em ms). Use-o para comparar sequencial x paralelo em:

1. ordenar 5 milhões de inteiros (`sorted` e `Arrays.parallelSort`);
2. filtrar + contar números primos;
3. agrupar 1 milhão de objetos por uma chave (`groupingBy` x `groupingByConcurrent` em paralelo);
4. uma tarefa com `Thread.sleep(1)` por elemento (simulando I/O) e discuta por que paralelo ajuda aqui, mas um `ExecutorService` própria seria melhor.

Escreva suas conclusões.

---

# 🏆 Desafio Integrador do Bloco 25 — Painel de Indicadores de uma Rede de Lojas

Você vai construir o motor de relatórios de uma rede de varejo usando **somente streams e coletores**.

## Dados

```text
Loja     → id, nome, cidade, estado
Produto  → sku, nome, categoria (enum), marca, preco
Venda    → id, loja, produto, quantidade, data (LocalDate), vendedor, formaPagamento (enum)
```

Gere 8 lojas, 40 produtos em 6 categorias, e **200 mil vendas** (gerador com `Random` de semente fixa e `IntStream`/`Stream.generate`).

## Indicadores (cada um em um método com nome claro)

1. Faturamento total por loja.
2. Faturamento por estado e, dentro de cada estado, por cidade (agrupamento multinível).
3. Top 5 produtos mais vendidos por categoria (quantidade).
4. Ticket médio por forma de pagamento.
5. Para cada mês, a loja que mais vendeu (`groupingBy(mês)` → `maxBy`).
6. Para cada vendedor: quantidade de vendas, faturamento e maior venda (`summarizing`).
7. Marcas por categoria (conjunto ordenado, `TreeSet`).
8. `partitioningBy`: vendas acima/abaixo do ticket médio geral, com contagem e faturamento de cada lado.
9. Participação percentual de cada categoria no faturamento total.
10. Dia da semana com mais vendas (use `DayOfWeek`).
11. Produtos que nunca foram vendidos.
12. Relatório em texto (arquivo) com os 10 indicadores mais importantes.

## Parte de desempenho

13. Execute os indicadores 1, 3 e 6 em **modo sequencial** e **paralelo** (use `groupingByConcurrent` quando fizer sentido).
14. Faça o benchmark com seu harness (exercício 21) e decida, **com base em números**, quais indicadores se beneficiam do paralelo.
15. Verifique que os resultados (sequencial e paralelo) são **idênticos** (compare os mapas). Se não forem, explique por quê (ordem? associatividade?).
16. Use `mapToDouble`/`LongStream` onde evitar boxing e meça a diferença.

## Regras

- Zero laços de processamento (apenas o gerador pode usar `IntStream`).
- Imports estáticos de `Collectors` para legibilidade; extraia lambdas grandes para métodos nomeados.
- Documente (comentário) para cada indicador o tipo do `Map` resultante.
- No final, escreva um parágrafo (comentário) respondendo: "Quando eu levaria isso para o banco de dados em vez de fazer em Java?".

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Conheço os principais coletores (`toList`, `toSet`, `toMap`, `joining`, `counting`, `summingX`, `averagingX`, `minBy/maxBy`, `summarizingX`).
- [ ] Sei usar `groupingBy` com classificador por atributo e por lambda.
- [ ] Sei usar coletor downstream (`counting`, `mapping`, `summarizing`...).
- [ ] Sei agrupar em vários níveis.
- [ ] Sei escolher o tipo do `Map` (3º argumento).
- [ ] Sei quando usar `partitioningBy`.
- [ ] Sei usar `collectingAndThen`.
- [ ] Sei o que é um stream paralelo e como ativá-lo.
- [ ] Sei por que `iterate` + `parallel` é lento e `rangeClosed` + `parallel` é rápido.
- [ ] Conheço os critérios para usar (ou não) paralelismo.
- [ ] Sei o que é Fork/Join e por que estado compartilhado é perigoso.

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
