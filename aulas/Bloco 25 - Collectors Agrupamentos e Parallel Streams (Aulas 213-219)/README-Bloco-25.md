# Bloco 25 — Collectors, Agrupamentos (`groupingBy`) e Parallel Streams

## Aulas 213 a 219

No bloco anterior você viu que todo pipeline de stream termina em uma **operação terminal**. A mais poderosa delas é o **`collect`**, que recebe um **`Collector`** e decide **como reunir o resultado**: lista, conjunto, mapa, soma, média, texto, agrupamento... Este bloco aprofunda isso (principalmente o **`groupingBy`**) e termina com os **Streams paralelos**.

As aulas deste bloco são:

```text
213 — Streams pt 11 — Collectors pt 01        (⚠ sem transcrição)
214 — Streams pt 12 — Collectors pt 02 — Grouping by pt 01
215 — Streams pt 13 — Collectors pt 03 — Grouping by pt 02
216 — Streams pt 14 — Collectors pt 04 — Grouping by pt 03   (⚠ sem transcrição)
217 — Streams pt 15 — Collectors pt 05 — Grouping by pt 04
218 — Streams pt 16 — Parallel Streams pt 01
219 — Streams pt 17 — Parallel Streams pt 02
```

> ⚠️ **Avisos sobre as aulas sem transcrição:**
> - **213 (Collectors pt 01):** a transcrição não existe. Pelo título e pela sequência, é a introdução ao `collect` e aos coletores básicos (`toList`, `toSet`, `counting`, `summingX`, `averagingX`, `minBy/maxBy`, `summarizingX`, `joining`). Escrevi essa seção com base na API e nas aulas 214–217, que usam esses coletores.
> - **216 (Grouping by pt 03):** também sem transcrição. A aula 215 termina com o `groupingBy` aninhado e a 217 retoma com `groupingBy` + coletores "downstream", então preenchi o assunto intermediário (`counting`, `summingX`, `averagingX`, `maxBy`) com a API padrão.
>
> Se algum desses vídeos tiver conteúdo específico que não está aqui, me avise para completar.

---

# Aula 213 — Collectors pt 01 (⚠ sem transcrição)

## 1. O que é um `Collector`?

`stream.collect(collector)` é a operação terminal que **acumula** os elementos de um stream em uma estrutura de resultado. A classe utilitária `java.util.stream.Collectors` oferece dezenas de coletores prontos.

```java
import java.util.stream.Collectors;
```

Você já usou o mais comum:

```java
List<String> titulos = lightNovels.stream()
        .map(LightNovel::getTitle)
        .collect(Collectors.toList());
```

---

## 2. Coletores básicos

### Para coleções

| Coletor | Resultado |
|---|---|
| `toList()` | `List<T>` |
| `toSet()` | `Set<T>` (sem duplicados) |
| `toCollection(TreeSet::new)` | qualquer coleção que você escolher |
| `toMap(chave, valor)` | `Map<K,V>` (atenção a chaves duplicadas!) |

```java
Set<String> autores = livros.stream().map(Livro::getAutor).collect(Collectors.toSet());
TreeSet<String> ordenados = livros.stream().map(Livro::getTitulo).collect(Collectors.toCollection(TreeSet::new));
Map<String, Double> precoPorTitulo = livros.stream().collect(Collectors.toMap(Livro::getTitulo, Livro::getPreco));
```

`toMap` lança `IllegalStateException` se duas chaves forem iguais; para resolver conflitos passe uma função de fusão: `toMap(k, v, (a, b) -> a)`.

### Para números (redução)

| Coletor | Resultado |
|---|---|
| `counting()` | `Long` — quantos |
| `summingInt/Long/Double(f)` | soma |
| `averagingInt/Long/Double(f)` | média (`Double`) |
| `minBy(comparator)` / `maxBy(comparator)` | `Optional<T>` |
| `summarizingInt/Long/Double(f)` | estatísticas completas |

```java
long qtd = livros.stream().collect(Collectors.counting());
double total = livros.stream().collect(Collectors.summingDouble(Livro::getPreco));
double media = livros.stream().collect(Collectors.averagingDouble(Livro::getPreco));
Optional<Livro> maisCaro = livros.stream().collect(Collectors.maxBy(Comparator.comparingDouble(Livro::getPreco)));
DoubleSummaryStatistics stats = livros.stream().collect(Collectors.summarizingDouble(Livro::getPreco));
```

### Para texto

```java
String nomes = livros.stream().map(Livro::getTitulo).collect(Collectors.joining(", ", "[", "]"));
// [Dom Casmurro, O Hobbit, 1984]
```

`joining(separador, prefixo, sufixo)`.

### Transformar o resultado: `collectingAndThen`

```java
List<String> imutavel = livros.stream()
        .map(Livro::getTitulo)
        .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
```

## 3. Por que coletores em vez de operações terminais diretas?

Porque **coletores podem ser compostos** — um coletor pode receber outro coletor como argumento ("downstream"), o que é a base do `groupingBy` e do `partitioningBy` nas aulas seguintes.

## O que você precisa dominar (Aula 213)

- `collect(Collector)` e a classe `Collectors`.
- `toList`, `toSet`, `toMap`, `toCollection`.
- `counting`, `summingX`, `averagingX`, `minBy`, `maxBy`, `summarizingX`.
- `joining`.
- `collectingAndThen`.

---

# Aula 214 — Collectors pt 02 — `groupingBy` (parte 1)

## 1. Preparando o cenário

Adiciona-se uma **enumeração** de categorias:

```java
public enum Category {
    DRAMA,
    FANTASY,
    ROMANCE
}
```

E a `LightNovel` ganha o atributo `category` (com getter). Lista de exemplo (categorias atribuídas): `Tensei → FANTASY`, `Overlord → FANTASY`, `Violet Evergarden → DRAMA`, `No Game No Life → FANTASY`, `Fullmetal Alchemist → FANTASY`, `Kumo desu ga → FANTASY`, `Monogatari → ROMANCE`...

## 2. O objetivo: agrupar por categoria

Resultado desejado:

```text
Map<Category, List<LightNovel>>
 DRAMA   → [Violet Evergarden]
 FANTASY → [Tensei, Overlord, No Game No Life, ...]
 ROMANCE → [Monogatari]
```

### Sem streams (manual)

```java
Map<Category, List<LightNovel>> resultado = new HashMap<>();
List<LightNovel> drama = new ArrayList<>();
List<LightNovel> fantasy = new ArrayList<>();
List<LightNovel> romance = new ArrayList<>();

for (LightNovel ln : lightNovels) {
    switch (ln.getCategory()) {
        case DRAMA:   drama.add(ln);   break;
        case FANTASY: fantasy.add(ln); break;
        case ROMANCE: romance.add(ln); break;
    }
}
resultado.put(Category.DRAMA, drama);
resultado.put(Category.FANTASY, fantasy);
resultado.put(Category.ROMANCE, romance);
```

Problemas: muito código repetido, e se surgir uma 4ª categoria você precisa alterar tudo.

### Com `groupingBy`

```java
Map<Category, List<LightNovel>> grouped = lightNovels.stream()
        .collect(Collectors.groupingBy(LightNovel::getCategory));
```

**Uma linha.** O argumento é uma `Function<T, K>` (o **classificador**): ela diz qual será a **chave** de cada grupo.

```java
System.out.println(grouped);
```

Funciona para qualquer chave: categoria, autor, ano, faixa de preço...

## 3. Anatomia do coletor

```text
groupingBy( classificador )
              │
              └ Function<T, K>: para cada elemento, diz em QUAL grupo ele vai

Resultado: Map<K, List<T>>   (por padrão: HashMap com ArrayList nos valores)
```

## O que você precisa dominar (Aula 214)

- Agrupar = transformar `List<T>` em `Map<K, List<T>>`.
- `Collectors.groupingBy(classificador)`.
- Como seria o código manual e por que streams reduzem tudo a uma linha.
- Usar `enum` como chave.

---

# Aula 215 — Collectors pt 03 — `groupingBy` (parte 2)

## 1. Agrupando por uma regra de negócio (não por um atributo)

Nova enumeração:

```java
public enum Promotion {
    ON_PROMOTION,
    NORMAL_PRICE
}
```

O `LightNovel` **não** tem um atributo "promoção". A promoção é **calculada** a partir do preço (menor que 6 → em promoção). Nesse caso o classificador é uma **lambda**:

```java
Map<Promotion, List<LightNovel>> porPromocao = lightNovels.stream()
        .collect(Collectors.groupingBy(ln -> ln.getPrice() < 6
                ? Promotion.ON_PROMOTION
                : Promotion.NORMAL_PRICE));
```

O classificador é um **operador ternário** dentro da lambda. Se a regra crescer, extraia para um método:

```java
private static Promotion getPromotion(LightNovel ln) {
    return ln.getPrice() < 6 ? Promotion.ON_PROMOTION : Promotion.NORMAL_PRICE;
}

lightNovels.stream().collect(Collectors.groupingBy(Main::getPromotion));
```

---

## 2. Agrupamento em dois níveis (multinível)

Agora quero agrupar **primeiro por categoria**, e **dentro de cada categoria** por promoção:

```text
Map<Category, Map<Promotion, List<LightNovel>>>
```

O `groupingBy` aceita um **segundo argumento**: outro coletor, aplicado a cada grupo (o **coletor downstream**). Colocando outro `groupingBy` nele:

```java
Map<Category, Map<Promotion, List<LightNovel>>> resultado = lightNovels.stream()
        .collect(Collectors.groupingBy(
                LightNovel::getCategory,                                   // 1º nível
                Collectors.groupingBy(Main::getPromotion)));               // 2º nível
```

Saída (conceitual):

```text
DRAMA   → { NORMAL_PRICE=[...] }
FANTASY → { ON_PROMOTION=[...], NORMAL_PRICE=[...] }
ROMANCE → { ON_PROMOTION=[...] }
```

### Dicas da aula

- Código de agrupamento aninhado pode ficar **difícil de ler**. O instrutor aconselha: **se der**, deixe esse tipo de agrupamento para o **banco de dados** (SQL `GROUP BY`) — mas saber fazer em Java ajuda em entrevistas.
- Use **imports estáticos** (`Alt+Enter` na IDE) para reduzir o ruído: `import static java.util.stream.Collectors.groupingBy;`.
- Extraia trechos em métodos (*Extract Method*: `Ctrl+Alt+M`) para dar nomes às regras.

## O que você precisa dominar (Aula 215)

- Classificador como lambda com regra de negócio.
- Agrupamento multinível com `groupingBy(f1, groupingBy(f2))`.
- O segundo argumento do `groupingBy` é um coletor "downstream".
- Quando delegar para o banco.

---

# Aula 216 — Collectors pt 04 — `groupingBy` (parte 3) (⚠ sem transcrição)

> Conteúdo reconstruído a partir das aulas 215 e 217.

## 1. O coletor downstream muda o **valor** do mapa

O `groupingBy` de dois argumentos funciona assim:

```java
groupingBy(classificador, coletorDownstream)
```

O coletor downstream recebe os elementos **de cada grupo** e decide o que vira o **valor** daquele grupo. Com `toList()` (padrão) o valor é uma lista; com outros coletores, vira outra coisa.

### Contar por grupo

```java
Map<Category, Long> quantidadePorCategoria = lightNovels.stream()
        .collect(Collectors.groupingBy(LightNovel::getCategory, Collectors.counting()));
// {DRAMA=1, FANTASY=5, ROMANCE=1}
```

### Somar / média por grupo

```java
Map<Category, Double> totalPorCategoria = lightNovels.stream()
        .collect(Collectors.groupingBy(LightNovel::getCategory,
                 Collectors.summingDouble(LightNovel::getPrice)));

Map<Category, Double> mediaPorCategoria = lightNovels.stream()
        .collect(Collectors.groupingBy(LightNovel::getCategory,
                 Collectors.averagingDouble(LightNovel::getPrice)));
```

### O mais caro de cada grupo

```java
Map<Category, Optional<LightNovel>> maisCaroPorCategoria = lightNovels.stream()
        .collect(Collectors.groupingBy(LightNovel::getCategory,
                 Collectors.maxBy(Comparator.comparingDouble(LightNovel::getPrice))));
```

Para evitar o `Optional` no valor:

```java
.collect(groupingBy(LightNovel::getCategory,
         collectingAndThen(maxBy(comparingDouble(LightNovel::getPrice)), Optional::get)));
```

### Títulos por grupo (em vez dos objetos)

```java
Map<Category, List<String>> titulosPorCategoria = lightNovels.stream()
        .collect(Collectors.groupingBy(LightNovel::getCategory,
                 Collectors.mapping(LightNovel::getTitle, Collectors.toList())));
```

(O `mapping` é a aula 217.)

---

## 2. Escolhendo o tipo do mapa: três argumentos

```java
Map<Category, List<LightNovel>> ordenado = lightNovels.stream()
        .collect(Collectors.groupingBy(
                LightNovel::getCategory,
                TreeMap::new,                 // fábrica do Map (aqui, ordenado pela chave)
                Collectors.toList()));
```

Úteis: `TreeMap::new` (chaves ordenadas), `LinkedHashMap::new` (ordem de encontro), `() -> new EnumMap<>(Category.class)`.

## 3. `partitioningBy` — grupos "sim/não"

Quando o classificador é um **boolean**, use `partitioningBy` — sempre devolve **duas chaves** (`true` e `false`), mesmo que um grupo esteja vazio:

```java
Map<Boolean, List<LightNovel>> baratosECaros = lightNovels.stream()
        .collect(Collectors.partitioningBy(ln -> ln.getPrice() < 6));
baratosECaros.get(true);    // os baratos
baratosECaros.get(false);   // os caros
```

Também aceita downstream: `partitioningBy(pred, counting())`.

## O que você precisa dominar (Aula 216)

- Coletores downstream: `counting`, `summingX`, `averagingX`, `maxBy`, `mapping`.
- `groupingBy` com 3 argumentos (fábrica do mapa).
- `partitioningBy` para classificadores boolean.
- `collectingAndThen` para limpar o `Optional`.

---

# Aula 217 — Collectors pt 05 — `groupingBy` (parte 4)

## 1. Estatísticas por grupo

Pergunta: para cada categoria, qual o menor, a média e o maior preço (e soma, contagem)?

```java
Map<Category, DoubleSummaryStatistics> estatisticas = lightNovels.stream()
        .collect(Collectors.groupingBy(
                LightNovel::getCategory,
                Collectors.summarizingDouble(LightNovel::getPrice)));

estatisticas.forEach((categoria, stats) ->
        System.out.println(categoria + " -> " + stats));
```

Saída (exemplo): `FANTASY -> DoubleSummaryStatistics{count=5, sum=32.94, min=1.99, average=6.58, max=10.99}`.

Calcular isso manualmente daria muito trabalho. O instrutor observa que, com uma categoria de um item só (`DRAMA`), as estatísticas são triviais; faz mais sentido numa categoria com vários.

`summarizingDouble` devolve `DoubleSummaryStatistics` com `getMin()`, `getMax()`, `getAverage()`, `getSum()`, `getCount()`.

---

## 2. `mapping` — transformar o valor antes de coletar

Objetivo: agrupar por categoria, mas o **valor** de cada grupo deve ser a lista de **promoções** presentes (em vez dos livros).

```java
Map<Category, Set<Promotion>> promocoesPorCategoria = lightNovels.stream()
        .collect(Collectors.groupingBy(
                LightNovel::getCategory,
                Collectors.mapping(Main::getPromotion, Collectors.toSet())));
```

- `Collectors.mapping(função, coletorFinal)` aplica a função em cada elemento do grupo **antes** de entregá-lo ao coletor final.
- É o equivalente do `map` do stream, só que **dentro** do coletor (porque o `groupingBy` já está no fim do pipeline).

Por que `toSet()` e não `toList()`? Para **não repetir** a mesma promoção várias vezes. Saída:

```text
DRAMA   → [NORMAL_PRICE]
FANTASY → [ON_PROMOTION, NORMAL_PRICE]
ROMANCE → [ON_PROMOTION]
```

O `toSet()` entrega um `HashSet`. Se quiser **manter a ordem** de inserção, use `toCollection(LinkedHashSet::new)`:

```java
Collectors.mapping(Main::getPromotion, Collectors.toCollection(LinkedHashSet::new))
```

---

## 3. Resumo de downstream collectors

| Quero como valor... | Use como downstream |
|---|---|
| lista dos próprios elementos | `toList()` (padrão) |
| quantidade | `counting()` |
| soma / média | `summingX` / `averagingX` |
| estatísticas | `summarizingX` |
| o maior / menor | `maxBy` / `minBy` |
| só um atributo | `mapping(f, toList())` |
| conjunto de valores | `mapping(f, toSet())` |
| um grupo dentro de outro | `groupingBy(f2)` |
| texto | `mapping(f, joining(", "))` |

Segundo o instrutor: **conhecer bem `groupingBy` ajuda muito em entrevistas** para desenvolvedor júnior e pleno.

## O que você precisa dominar (Aula 217)

- `summarizingDouble` por grupo.
- `Collectors.mapping`.
- `toSet` x `toCollection(LinkedHashSet::new)`.
- A tabela de downstream collectors.

---

# Aula 218 — Parallel Streams pt 01

## 1. Conceitos de hardware

- **Processador (CPU)** tem vários **núcleos (cores)**.
- Cada núcleo executa **threads** — "trabalhadores" independentes. Mais threads = mais trabalho em **paralelo**.
- `Runtime.getRuntime().availableProcessors()` informa quantos processadores lógicos a JVM enxerga (na aula, 8).

Um **parallel stream** divide o trabalho entre várias threads automaticamente (usando o `ForkJoinPool` comum). Basta chamar `.parallel()` (ou `collection.parallelStream()`).

---

## 2. O experimento: somar de 1 a 100 milhões

Medindo com `System.currentTimeMillis()` (e comparando):

### a) `for` clássico

```java
long soma = 0;
for (long i = 1; i <= N; i++) soma += i;
```

→ **~6 ms** (excelente: é de baixo nível, sem objetos).

### b) `Stream.iterate` sequencial

```java
Stream.iterate(1L, i -> i + 1).limit(N).reduce(0L, Long::sum);
```

→ **~208 ms**: muito mais lento (cria objetos `Long`, faz boxing e unboxing a cada passo).

### c) `Stream.iterate` **paralelo**

```java
Stream.iterate(1L, i -> i + 1).limit(N).parallel().reduce(0L, Long::sum);
```

→ **~3,5 s**: **pior ainda**! Por quê?

> Porque `iterate` gera os números **um a um**, cada um dependendo do anterior. As threads não conseguem dividir o trabalho com antecedência (não sabem como "cortar" a sequência). O custo de coordenar as threads supera o ganho.

(A aula mostra, no gerenciador de tarefas, a CPU "indo lá em cima" — vários núcleos trabalhando — mas sem benefício.)

### d) `LongStream.rangeClosed` sequencial

```java
LongStream.rangeClosed(1, N).reduce(0L, Long::sum);
```

→ **~164 ms** (melhor que `iterate`: sem boxing, tamanho conhecido).

### e) `LongStream.rangeClosed` **paralelo**

```java
LongStream.rangeClosed(1, N).parallel().reduce(0L, Long::sum);
```

→ **~25 ms**: agora sim, o paralelo ganha! Pois o intervalo tem **tamanho conhecido e é facilmente divisível** em pedaços iguais.

Tabela:

| Abordagem | Tempo aprox. |
|---|---|
| `for` clássico | 6 ms |
| `Stream.iterate` | 208 ms |
| `Stream.iterate` + parallel | 3500 ms ❌ |
| `LongStream.rangeClosed` | 164 ms |
| `LongStream.rangeClosed` + parallel | 25 ms ✅ |

## 3. Lições

1. **Paralelo nem sempre é mais rápido.**
2. O que importa é a **forma da fonte de dados**: fontes divisíveis (arrays, `ArrayList`, `range`) funcionam bem; fontes sequenciais (`iterate`, `LinkedList`) não.
3. Prefira **streams primitivos** (`LongStream`/`IntStream`) para evitar boxing.
4. **Meça** (benchmark) sempre.

## O que você precisa dominar (Aula 218)

- Núcleos, threads e `availableProcessors()`.
- `.parallel()` / `parallelStream()`.
- Por que `iterate` + `parallel` é lento.
- Por que `LongStream.rangeClosed` + `parallel` é rápido.
- Sempre medir.

---

# Aula 219 — Parallel Streams pt 02 — Quando usar (e quando evitar)

## 1. Lista de verificação para decidir

### 1) Meça, não adivinhe
Use benchmark (ideal: biblioteca como JMH; no mínimo `System.nanoTime` com várias execuções) para saber se o paralelo realmente compensa.

### 2) Cuidado com boxing/unboxing
Mesmo em paralelo, `Stream<Integer>` tem custo. Prefira `IntStream`/`LongStream`/`DoubleStream`.

### 3) Tarefas pequenas não compensam
Dividir o trabalho em threads tem um **custo fixo**. Se a tarefa é pequena, esse custo é maior que o ganho.

### 4) Evite operações dependentes de ordem
`limit`, `findFirst` e `skip` exigem respeitar a ordem dos elementos, o que atrapalha o paralelismo. `findAny` é uma alternativa melhor em paralelo.

### 5) Custo total da computação
`custo total = (custo por elemento) × (quantidade de elementos)`. Quanto **maior** esse custo, mais o paralelo ajuda. Trabalho pesado por elemento (ex.: cálculo complexo) → bom candidato.

### 6) Quantidade de dados
Poucos dados → use sequencial. O paralelo só vale a pena em volumes grandes.

### 7) Tipo da coleção (facilidade de dividir)

| Fonte | Paralelizável? |
|---|---|
| `ArrayList`, arrays | ✅ excelente (acesso direto por índice) |
| `IntStream.range` | ✅ excelente |
| `HashSet`, `TreeSet` | 🟡 razoável |
| `LinkedList` | ❌ ruim (precisa percorrer a lista) |
| `Stream.iterate`, `Stream.generate` | ❌ ruim |

### 8) Tamanho conhecido
Streams com tamanho pré-definido (`SIZED`) dividem melhor do que streams criados dinamicamente.

---

## 2. Como o paralelo funciona por dentro: Fork/Join

A estratégia é **divide e conquista** (*Fork/Join*):

```text
if (tarefa é pequena)
    executa sequencialmente
else
    divide em duas partes (FORK)
    processa cada parte recursivamente
    combina os resultados (JOIN)
```

Diagrama:

```text
                [tarefa grande]
                 /           \
            [metade 1]     [metade 2]          ← fork (divide)
             /     \         /     \
          [¼]      [¼]     [¼]      [¼]        ← continua dividindo até ficar "pequena"
           │        │       │        │
        resultado resultado resultado resultado ← executa sequencialmente cada pedaço
             \     /         \     /
           [soma 1]         [soma 2]            ← join (combina)
                 \           /
                  [resultado final]
```

Por isso a operação precisa ser **associativa** e **sem estado compartilhado**: se as threads alterarem uma variável em comum (por exemplo, `contador++` dentro de um `forEach` paralelo), o resultado fica **errado** (condição de corrida — assunto do bloco de Threads).

```java
long[] contador = {0};
IntStream.range(0, 1_000_000).parallel().forEach(i -> contador[0]++);   // ❌ resultado imprevisível
```

## 3. Conclusão do instrutor

Há boas práticas, mas não existe regra absoluta: **faça o benchmark**. Mesmo assim, em entrevistas, saber explicar quando o paralelo ajuda e por que (Fork/Join) demonstra maturidade.

## O que você precisa dominar (Aula 219)

- Os critérios: medir, boxing, tamanho da tarefa, operações dependentes de ordem, custo total, quantidade de dados, tipo da coleção, tamanho conhecido.
- Fork/Join: dividir, processar, combinar.
- Operações associativas e sem efeitos colaterais.
- Perigo de estado compartilhado em streams paralelos.

---

# Mapa mental do bloco

```text
collect(Collector)
├── Coleções:   toList · toSet · toMap · toCollection
├── Números:    counting · summingX · averagingX · minBy · maxBy · summarizingX
├── Texto:      joining(sep, prefixo, sufixo)
├── Pós-proc.:  collectingAndThen
├── Agrupar:    groupingBy(classificador)
│     ├── groupingBy(classificador, downstream)     ← counting, mapping, summarizing, groupingBy (multinível)
│     ├── groupingBy(classificador, mapFactory, downstream)
│     └── partitioningBy(predicado)  → Map<Boolean, ...>
└── Paralelo:   .parallel() / parallelStream()
      ├── bom: ArrayList, arrays, range, trabalho pesado, muitos dados
      └── ruim: iterate/generate, LinkedList, limit/findFirst, pouco dado, estado compartilhado
```

# Cola de bolso

| Quero... | Use |
|---|---|
| Lista dos objetos agrupados por atributo | `groupingBy(Obj::getX)` |
| Quantos em cada grupo | `groupingBy(f, counting())` |
| Soma/média por grupo | `groupingBy(f, summingDouble(g))` / `averagingDouble(g)` |
| Só um atributo em cada grupo | `groupingBy(f, mapping(g, toList()))` |
| Estatísticas por grupo | `groupingBy(f, summarizingDouble(g))` |
| Grupos ordenados pela chave | `groupingBy(f, TreeMap::new, toList())` |
| Dividir em "sim/não" | `partitioningBy(predicado)` |
| Juntar em texto | `joining(", ")` |
| Acelerar processamento pesado | `.parallel()` (depois de medir!) |
