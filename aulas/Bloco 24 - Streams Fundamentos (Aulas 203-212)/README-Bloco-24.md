# Bloco 24 — Streams: Fundamentos

## Aulas 203 a 212

**Streams** (pacote `java.util.stream`, Java 8) são uma das maiores novidades da linguagem: um jeito **declarativo** de processar coleções de dados — filtrar, transformar, ordenar, agrupar, somar — descrevendo **o que você quer** em vez de **como fazer passo a passo**. Eles usam tudo que você aprendeu nos blocos anteriores: lambdas, method references, `Optional`, `Predicate`, `Function`, generics.

As aulas deste bloco são:

```text
203 — Streams pt 01 — Introdução pt 01
204 — Streams pt 02 — Introdução pt 02
205 — Streams pt 03 — Introdução pt 03
206 — Streams pt 04 — FlatMap pt 01   (⚠ sem transcrição)
207 — Streams pt 05 — FlatMap pt 02
208 — Streams pt 06 — Finding e Matching
209 — Streams pt 07 — Reduce pt 01
210 — Streams pt 08 — Reduce pt 02
211 — Streams pt 09 — Gerando streams pt 01
212 — Streams pt 10 — Gerando streams pt 02
```

> ⚠️ **Aviso sobre a aula 206:** não existe arquivo de transcrição para a aula 206 (Streams pt 04 — FlatMap pt 01). Pelo título e pela aula 207, que a continua, ela introduz o `flatMap`. Escrevi a seção da aula 206 a partir do que a aula 207 explica e da API do Java. Se o vídeo 206 tiver alguma particularidade, me conte para eu ajustar.

---

# Aula 203 — Streams pt 01 — Introdução (o problema)

## 1. O desafio proposto

Classe de domínio:

```java
public class LightNovel {
    private String title;
    private double price;
    // construtor, getters, toString
}
```

Lista de exemplo:

```java
List<LightNovel> lightNovels = new ArrayList<>(List.of(
        new LightNovel("Tensei Shittara", 8.99),
        new LightNovel("Overlord", 3.99),
        new LightNovel("Violet Evergarden", 5.99),
        new LightNovel("No Game No Life", 2.99),
        new LightNovel("Fullmetal Alchemist", 5.99),
        new LightNovel("Kumo desu ga", 1.99),
        new LightNovel("Monogatari", 4.00)));
```

**Requisito:** devolver os **títulos** das **três primeiras** light novels (ordenadas por título) cujo **preço seja menor ou igual a 4**.

---

## 2. A solução imperativa (sem streams)

```java
private static List<String> retrieveFirstThreeLightNovelsTitles() {
    // 1) ordenar
    lightNovels.sort(Comparator.comparing(LightNovel::getTitle));

    // 2) filtrar e limitar, extraindo só os títulos
    List<String> titulos = new ArrayList<>();
    for (LightNovel lightNovel : lightNovels) {
        if (lightNovel.getPrice() <= 4) {
            titulos.add(lightNovel.getTitle());
        }
        if (titulos.size() >= 3) {
            break;
        }
    }
    return titulos;
}
```

Funciona, mas:

- mistura **vários passos** dentro de um laço (filtro + extração + limite + parada);
- tem variáveis auxiliares e `break`;
- é preciso **ler com atenção** para entender o que ele faz;
- **modifica a lista original** (o `sort` é in-place).

> Nas aulas seguintes você reescreve isso em uma única expressão fluente.

## O que você precisa dominar (Aula 203)

- O problema: ordenar → filtrar → limitar → extrair um atributo.
- Como seria a solução imperativa.
- Por que isso é um bom candidato para stream.

---

# Aula 204 — Streams pt 02 — Introdução (a solução funcional)

## 1. O que é um Stream?

> Um `Stream` é uma **sequência de elementos** processada ao longo do tempo (por isso "fluxo").

O instrutor faz uma distinção importante:

| | Coleção | Stream |
|---|---|---|
| Natureza | dados no **espaço** (guardados na memória) | dados no **tempo** (fluindo) |
| Guarda elementos? | **sim** | **não** — apenas descreve operações sobre eles |
| Reutilizável? | sim | **uso único** |

Um stream **não é uma estrutura de dados**; é um **pipeline** de operações.

---

## 2. Estrutura de um pipeline

```text
FONTE ──▶ operação intermediária ──▶ operação intermediária ──▶ ... ──▶ OPERAÇÃO TERMINAL
(list.stream())   (filter)               (map, sorted, limit)             (collect, forEach, count...)
```

- **Fonte**: de onde vêm os dados (`lista.stream()`, `Stream.of(...)`, arquivo...).
- **Operações intermediárias**: devolvem **outro `Stream`**, então podem ser **encadeadas**. São **preguiçosas** (*lazy*): nada executa até existir uma operação terminal.
- **Operação terminal**: dispara a execução e **fecha** o stream, produzindo um resultado (coleção, valor, ou efeito colateral).

---

## 3. A solução com stream

```java
List<String> titulos = lightNovels.stream()
        .sorted(Comparator.comparing(LightNovel::getTitle))   // intermediária
        .filter(ln -> ln.getPrice() <= 4)                     // intermediária
        .limit(3)                                             // intermediária
        .map(LightNovel::getTitle)                            // intermediária
        .collect(Collectors.toList());                        // TERMINAL

System.out.println(titulos);
```

Leitura (quase como uma frase): "**ordene** por título, **filtre** preço ≤ 4, **limite** a 3, **mapeie** para o título e **colete** numa lista".

### Operações usadas

| Operação | Tipo | Parâmetro | O que faz |
|---|---|---|---|
| `sorted(comparator)` | intermediária | `Comparator<T>` | ordena |
| `filter(predicate)` | intermediária | `Predicate<T>` | mantém só os que passam |
| `limit(n)` | intermediária | `long` | mantém os `n` primeiros |
| `map(function)` | intermediária | `Function<T,R>` | transforma cada elemento |
| `collect(collector)` | **terminal** | `Collector` | reúne o resultado |

`Collectors.toList()` (o instrutor mostra `toList` da classe `Collectors`) produz uma `List`. Outras: `toSet()`, `toMap(...)`, `joining(...)`, `groupingBy(...)` (blocos seguintes). No Java 16+ existe também `stream.toList()`.

### Vantagens

1. **Declarativo**: diz o que, não como.
2. **Sem estado intermediário** (listas auxiliares, contadores, `break`).
3. **Encadeável** e fácil de ler (mesmo por quem não programa).
4. **Não altera** a lista original.
5. Facilita **paralelizar** (`parallelStream`, aulas futuras).

## O que você precisa dominar (Aula 204)

- Stream = sequência no tempo; coleção = dados no espaço.
- Pipeline: fonte → intermediárias → terminal.
- Operações intermediárias devolvem `Stream`; terminal fecha.
- `filter`, `map`, `sorted`, `limit`, `collect(Collectors.toList())`.

---

# Aula 205 — Streams pt 03 — Introdução (mais operações terminais)

## 1. `forEach` terminal

```java
lightNovels.stream().forEach(System.out::println);
```

`Stream.forEach(Consumer)` é **terminal** (devolve `void`). Cuidado: `Iterable.forEach` (da lista) tem o mesmo nome, mas **não** é uma operação de stream. Se você só quer imprimir a lista, use diretamente `lista.forEach(...)` — criar um stream só para isso é desperdício (o instrutor comenta que isso reprovou um candidato numa entrevista de código).

## 2. `count`

```java
long total = lightNovels.stream().count();

long baratas = lightNovels.stream()
        .filter(ln -> ln.getPrice() <= 4)
        .count();
```

Terminal, devolve `long`.

## 3. `distinct`

Remove duplicados (usa `equals`/`hashCode`):

```java
long unicos = lightNovels.stream()
        .distinct()
        .filter(ln -> ln.getPrice() <= 4)
        .count();
```

`distinct` é intermediária. Para funcionar com objetos próprios, a classe precisa ter `equals` e `hashCode` (Bloco 18!). No exemplo da aula adicionou-se `equals`/`hashCode` por `title` e `price`.

## 4. ⚠️ Um stream só pode ser consumido UMA vez

```java
Stream<LightNovel> stream = lightNovels.stream();
stream.forEach(System.out::println);           // ✅ fecha o stream
long c = stream.count();                       // ❌ IllegalStateException: stream has already been operated upon or closed
```

Quando uma operação terminal é executada, o stream é **fechado**. Para processar novamente, peça outro: `lightNovels.stream()`.

Na prática isso raramente é um problema: você monta tudo em um único pipeline (`.filter(...).distinct().count()`).

## 5. Resumo: intermediárias × terminais

| Intermediárias (devolvem `Stream`) | Terminais (fecham) |
|---|---|
| `filter`, `map`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`, `peek` | `forEach`, `collect`, `count`, `reduce`, `min`, `max`, `findFirst`, `findAny`, `anyMatch`, `allMatch`, `noneMatch`, `toArray` |

## O que você precisa dominar (Aula 205)

- `forEach`, `count`, `distinct`.
- `Stream.forEach` ≠ `List.forEach`.
- Stream é de uso único (`IllegalStateException`).
- `equals`/`hashCode` para `distinct`.

---

# Aula 206 — Streams pt 04 — FlatMap pt 01 (⚠ sem transcrição)

> Conteúdo reconstruído com base no título e na aula 207.

## 1. O problema que o `flatMap` resolve

Dada uma lista de listas (ou qualquer estrutura "aninhada"), `map` produz um **stream de streams** (ou de listas), o que raramente é o que você quer.

```java
List<List<String>> turmas = List.of(
        List.of("Ana", "Bruno"),
        List.of("Carla", "Diego"),
        List.of("Eva"));

Stream<List<String>> s = turmas.stream().map(turma -> turma);   // Stream<List<String>> (aninhado)
```

Quero **uma lista só** com todos os nomes.

## 2. `flatMap` = map + "achatar" (flatten)

```java
List<String> nomes = turmas.stream()
        .flatMap(List::stream)               // cada List<String> vira Stream<String> e todos são unidos
        .collect(Collectors.toList());       // [Ana, Bruno, Carla, Diego, Eva]
```

Diagrama:

```text
map:      [ [Ana,Bruno], [Carla,Diego], [Eva] ]   →  Stream<List<String>>   (continua aninhado)
flatMap:  [ Ana, Bruno, Carla, Diego, Eva ]       →  Stream<String>         (achatado)
```

Regra prática: **se o seu `map` devolve um `Stream`, uma `List` ou um `Optional`, provavelmente você quer `flatMap`.**

Assinatura:

```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)
```

A função recebe um `T` e devolve um **Stream** de `R`; o `flatMap` junta todos esses streams em um só.

## O que você precisa dominar (Aula 206)

- O que é "achatar" um stream aninhado.
- `flatMap(Colecao::stream)`.
- Diferença visual entre `map` e `flatMap`.

---

# Aula 207 — Streams pt 05 — FlatMap pt 02

## 1. Exemplo da aula: letras das palavras

Dada a lista de palavras, devolva **uma lista com todas as letras**:

```java
List<String> words = List.of("Gokuu", "Vegeta");     // palavras (exemplo)
```

### Forma imperativa (para uma palavra)

```java
String[] letras = words.get(0).split("");    // separa cada caractere
System.out.println(Arrays.toString(letras));
```

### Primeira tentativa com `map` (não funciona como queremos)

```java
List<String[]> resultado = words.stream()
        .map(word -> word.split(""))         // cada palavra vira String[]
        .collect(Collectors.toList());       // List<String[]> — lista de arrays, não de letras
```

O tipo do stream virou `Stream<String[]>`. Queremos `Stream<String>`.

### Converter array em stream

`Arrays.stream(array)` (ou `Stream.of(array)`) transforma um array em `Stream<String>`:

```java
words.stream()
     .map(word -> word.split(""))
     .map(Arrays::stream)                    // agora é Stream<Stream<String>> — ainda aninhado!
```

### Solução: `flatMap`

```java
List<String> letras = words.stream()
        .map(word -> word.split(""))         // Stream<String[]>
        .flatMap(Arrays::stream)             // Stream<String>  ← achatado
        .collect(Collectors.toList());
```

Ou, com o `flatMap` fazendo o split:

```java
List<String> letras = words.stream()
        .flatMap(word -> Arrays.stream(word.split("")))
        .collect(Collectors.toList());
```

Resultado: `[G, o, k, u, u, V, e, g, e, t, a]`.

Com `distinct()` você obtém só as letras diferentes:

```java
.flatMap(word -> Arrays.stream(word.split("")))
.distinct()
```

### Por que `map` sozinho não basta?

```text
map(w -> w.split(""))        →  Stream< String[] >       "um array por palavra"
map(Arrays::stream)          →  Stream< Stream<String> > "um stream por palavra"
flatMap(w -> Arrays.stream(...)) → Stream< String >      "todas as letras, um stream só"
```

O `flatMap` "abre" cada stream interno e despeja os elementos no stream externo.

## O que você precisa dominar (Aula 207)

- `Arrays.stream(array)`.
- Por que `map` gera estruturas aninhadas.
- `flatMap` para juntar tudo em um só nível.
- Combinar com `distinct`.

---

# Aula 208 — Streams pt 06 — Finding e Matching

## 1. Matching (respostas `boolean`)

| Método | Pergunta |
|---|---|
| `anyMatch(predicate)` | **algum** elemento satisfaz? |
| `allMatch(predicate)` | **todos** satisfazem? |
| `noneMatch(predicate)` | **nenhum** satisfaz? |

Todos são **terminais** e devolvem `boolean`.

```java
boolean existeCaro = lightNovels.stream().anyMatch(ln -> ln.getPrice() > 8);   // true
boolean existeMuitoCaro = lightNovels.stream().anyMatch(ln -> ln.getPrice() > 9); // false

boolean todosPositivos = lightNovels.stream().allMatch(ln -> ln.getPrice() > 0);    // true
boolean nenhumNegativo = lightNovels.stream().noneMatch(ln -> ln.getPrice() < 0);   // true
```

Dica para não se confundir: `allMatch(x > 0)` e `noneMatch(x <= 0)` são equivalentes.

Detalhe: são **curto-circuitantes** — param assim que a resposta é conhecida (o `anyMatch` pára no primeiro `true`).

---

## 2. Finding (devolvem `Optional`)

```java
Optional<LightNovel> qualquer = lightNovels.stream()
        .filter(ln -> ln.getPrice() > 3)
        .findAny();                 // qualquer um que satisfaça (ordem não garantida)

Optional<LightNovel> primeiro = lightNovels.stream()
        .filter(ln -> ln.getPrice() > 3)
        .findFirst();               // o primeiro, respeitando a ordem do stream
```

Como o stream pode ser vazio, o retorno é `Optional` — e agora você sabe tratar:

```java
lightNovels.stream()
        .filter(ln -> ln.getPrice() > 3)
        .findFirst()
        .ifPresent(System.out::println);
```

| Método | Quando usar |
|---|---|
| `findFirst()` | a ordem importa |
| `findAny()` | não importa qual (mais livre em streams paralelos) |

Em streams sequenciais, `findAny()` costuma devolver o mesmo que `findFirst()`, mas **não é garantido**.

## 3. Mostrando o "melhor" com ordenação

Para obter o **mais barato/caro** dentre os que passam no filtro, combine `sorted` + `findFirst`:

```java
lightNovels.stream()
        .filter(ln -> ln.getPrice() > 3)
        .sorted(Comparator.comparing(LightNovel::getPrice).reversed())
        .findFirst()
        .ifPresent(System.out::println);
```

(Mais direto: `.max(Comparator.comparing(LightNovel::getPrice))` — terminal que devolve `Optional`.)

O instrutor encerra com um conselho: **tente resolver os desafios por conta própria**; é assim que o raciocínio funcional se desenvolve.

## O que você precisa dominar (Aula 208)

- `anyMatch`, `allMatch`, `noneMatch`.
- `findFirst` e `findAny` (devolvem `Optional`).
- Quando usar cada um.
- `sorted` + `findFirst`, e `max`/`min`.

---

# Aula 209 — Streams pt 07 — Reduce pt 01

## 1. O que é `reduce`

`reduce` é uma **operação terminal** que **combina todos os elementos em um único valor**, aplicando repetidamente uma operação binária.

```text
[1, 2, 3, 4, 5, 6]  ──reduce(+)──▶  21

((((((0 + 1) + 2) + 3) + 4) + 5) + 6)
```

Assinaturas principais:

```java
Optional<T> reduce(BinaryOperator<T> acumulador)               // sem valor inicial
T           reduce(T identidade, BinaryOperator<T> acumulador) // com valor inicial
```

---

## 2. Somando

```java
List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6);

// sem identidade → Optional (a lista pode estar vazia)
Optional<Integer> soma1 = numeros.stream().reduce((x, y) -> x + y);
soma1.ifPresent(System.out::println);               // 21

// com identidade (0) → o resultado nunca é nulo
int soma2 = numeros.stream().reduce(0, (x, y) -> x + y);   // 21

// com method reference
int soma3 = numeros.stream().reduce(0, Integer::sum);     // 21
```

- A **identidade** é o valor inicial e também o resultado se o stream for vazio. Para soma: `0`. Para multiplicação: `1`.
- Sem identidade, o retorno é `Optional`, porque não há valor padrão para uma lista vazia.

---

## 3. Multiplicando

```java
int produto = numeros.stream().reduce(1, (x, y) -> x * y);   // 720
```

⚠️ Se a identidade da multiplicação fosse `0` o resultado seria sempre `0`. Escolha a identidade correta para a operação.

---

## 4. Máximo e mínimo

```java
Optional<Integer> maximo = numeros.stream().reduce((x, y) -> x > y ? x : y);
Optional<Integer> maximo2 = numeros.stream().reduce(Integer::max);
Optional<Integer> minimo = numeros.stream().reduce(Integer::min);

int maximoSeguro = numeros.stream().reduce(Integer.MIN_VALUE, Integer::max);   // identidade segura
```

`Integer::sum`, `Integer::max`, `Integer::min` já existem como métodos estáticos prontos que cabem como `BinaryOperator<Integer>` — por isso a aula diz que é a forma "bonita" (a "feia" é escrever o `if`/ternário à mão).

## O que você precisa dominar (Aula 209)

- `reduce` combina elementos em um só valor.
- Com e sem identidade (e por que muda o tipo de retorno).
- Identidades corretas: soma 0, produto 1.
- `Integer::sum`, `Integer::max`, `Integer::min`.

---

# Aula 210 — Streams pt 08 — Reduce pt 02 (e streams primitivos)

## 1. Reduzir objetos: somar preços

Requisito: somar os preços das light novels **acima de 3**.

### Versão com Wrappers (boxing)

```java
Optional<Double> soma = lightNovels.stream()
        .filter(ln -> ln.getPrice() > 3)
        .map(LightNovel::getPrice)            // Stream<Double>
        .reduce(Double::sum);

soma.ifPresent(System.out::println);          // 35.96...
```

Ordem recomendada pelo instrutor: **primeiro filtrar, depois `map` do atributo, por fim `reduce`**.

O problema: `Stream<Double>` usa **objetos** `Double`, o que implica *boxing/unboxing* em cada operação. Para pequenas listas não importa; em aplicações de alto desempenho importa.

---

## 2. Streams primitivos: `IntStream`, `LongStream`, `DoubleStream`

Existem streams especializados para tipos primitivos, que evitam o custo de boxing e já trazem métodos numéricos:

| Stream | Elemento |
|---|---|
| `IntStream` | `int` |
| `LongStream` | `long` |
| `DoubleStream` | `double` |

(Não existe um para `float`, `short`, `char` etc.)

Converter um `Stream<T>` em primitivo com `mapToInt`, `mapToLong`, `mapToDouble`:

```java
double soma = lightNovels.stream()
        .filter(ln -> ln.getPrice() > 3)
        .mapToDouble(LightNovel::getPrice)    // DoubleStream
        .sum();                               // devolve double primitivo, direto!

System.out.println(soma);
```

Diferenças:

- `mapToDouble` devolve `DoubleStream` (e não `Stream<Double>`).
- `.sum()` já existe e devolve `double` — sem `Optional` (a soma de nada é `0`).
- Também existem `average()`, `min()`, `max()`, `count()`, `summaryStatistics()`.

```java
OptionalDouble media = lightNovels.stream().mapToDouble(LightNovel::getPrice).average();
DoubleSummaryStatistics stats = lightNovels.stream().mapToDouble(LightNovel::getPrice).summaryStatistics();
// stats.getMin(), getMax(), getAverage(), getSum(), getCount()
```

Para voltar ao mundo de objetos: `.boxed()` (`IntStream` → `Stream<Integer>`) ou `.mapToObj(...)`.

## O que você precisa dominar (Aula 210)

- Sequência: `filter` → `map` (atributo) → `reduce`.
- Custo de boxing com `Stream<Double>`.
- `IntStream`, `LongStream`, `DoubleStream`.
- `mapToInt/Long/Double` e `sum`, `average`, `min`, `max`, `summaryStatistics`.
- `boxed()` e `mapToObj`.

---

# Aula 211 — Streams pt 09 — Gerando streams pt 01

Até agora todos os streams vieram de **listas**. Existem muitas outras fontes.

## 1. Faixas de números: `range` e `rangeClosed`

```java
IntStream.range(1, 50)         // 1 até 49  (fim EXCLUSIVO)
IntStream.rangeClosed(1, 50)   // 1 até 50  (fim INCLUSIVO)
```

(Também `LongStream.range`/`rangeClosed`.)

Exemplo: imprimir os pares de 1 a 50:

```java
IntStream.rangeClosed(1, 50)
         .filter(n -> n % 2 == 0)
         .forEach(n -> System.out.print(n + " "));
```

## 2. A partir de valores: `Stream.of`

```java
Stream<String> nomes = Stream.of("William", "DevDojo", "Suane");
nomes.map(String::toUpperCase).forEach(System.out::println);
```

Aceita qualquer tipo (objetos, strings, números) via *varargs*.

## 3. A partir de arrays: `Arrays.stream`

```java
int[] numeros = {1, 2, 3, 4, 5};
Arrays.stream(numeros)                // IntStream (já primitivo!)
      .sum();                         // 15

OptionalInt max = Arrays.stream(numeros).max();
```

Para `int[]` o retorno é um `IntStream`, que já possui `sum`, `average`, `min`, `max`... evitando boxing.

## 4. A partir de arquivos: `Files.lines`

Aqui o curso encontra o Bloco de NIO:

```java
try (Stream<String> linhas = Files.lines(Paths.get("pasta", "teste.txt"))) {
    linhas.filter(l -> l.contains("Java"))
          .forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```

Pontos:

- `Files.lines(Path)` devolve `Stream<String>` — cada elemento é uma **linha**.
- Lança `IOException`.
- O stream mantém o arquivo aberto, então use **try-with-resources** (o stream é `AutoCloseable`). O instrutor enfatiza que fechar corretamente evita travar o arquivo.
- Não carrega o arquivo inteiro na memória: processa linha a linha (ótimo para arquivos grandes).

## O que você precisa dominar (Aula 211)

- `IntStream.range` x `rangeClosed`.
- `Stream.of`, `Arrays.stream`, `Files.lines`.
- Try-with-resources com `Files.lines`.
- Streams primitivos nativos.

---

# Aula 212 — Streams pt 10 — Gerando streams pt 02 (streams infinitos)

Streams podem ser **infinitos**: nunca terminam sozinhos, por isso **precisam de `limit`** (ou de uma operação de curto-circuito como `findFirst`/`anyMatch`) para parar. Funciona porque os streams são **preguiçosos**.

## 1. `Stream.iterate(semente, função)`

Cada elemento é calculado a partir do anterior.

```java
Stream.iterate(1, n -> n + 2)       // 1, 3, 5, 7, ... (ímpares, infinito)
      .limit(10)
      .forEach(n -> System.out.print(n + " "));
```

(Java 9+ também aceita `iterate(semente, hasNext, next)`, que já tem condição de parada.)

Parâmetros:

1. **Semente**: o valor inicial (do tipo `T`).
2. **`UnaryOperator<T>`**: dado o elemento atual, devolve o próximo (`n -> n + 2`).

## 2. Exemplo: sequência de Fibonacci

Cada número é a soma dos dois anteriores: `0, 1, 1, 2, 3, 5, 8, 13, 21, 34...`

A ideia: o "elemento" do stream é um **array de 2 posições** `[a, b]`; o próximo é `[b, a+b]`.

```java
Stream.iterate(new int[]{0, 1}, arr -> new int[]{arr[1], arr[0] + arr[1]})
      .limit(10)
      .map(arr -> arr[0])                     // só o primeiro de cada par
      .forEach(n -> System.out.print(n + " "));
// 0 1 1 2 3 5 8 13 21 34
```

Sem o `map`, você imprimiria cada par `[0,1] [1,1] [1,2] ...`.

## 3. `Stream.generate(supplier)`

Gera valores **independentes** (sem depender do anterior), usando um `Supplier<T>`.

```java
Stream.generate(() -> ThreadLocalRandom.current().nextInt(1, 500))
      .limit(90)
      .forEach(n -> System.out.print(n + " "));
```

Para números aleatórios existe também `new Random().ints(90, 1, 500)` (devolve `IntStream`).

| | `iterate` | `generate` |
|---|---|---|
| Parâmetro | semente + `UnaryOperator` | `Supplier` |
| Cada elemento depende do anterior? | **sim** | **não** |
| Exemplos | progressões, Fibonacci | aleatórios, ids, timestamps |

## 4. Cuidado

Sem `limit` (ou outra condição de parada), um stream infinito rodará até estourar a memória ou o tempo.

## O que você precisa dominar (Aula 212)

- Streams infinitos e preguiçosos; sempre use `limit`.
- `Stream.iterate` (com dependência do elemento anterior).
- `Stream.generate` (com `Supplier`).
- Fibonacci com `iterate` + `map`.

---

# Mapa mental do bloco

```text
Stream<T>   (sequência no tempo, uso único, preguiçoso)
├── Fontes:  collection.stream() · Stream.of · Arrays.stream · IntStream.range/rangeClosed
│            Files.lines · Stream.iterate · Stream.generate
├── Intermediárias (devolvem Stream):
│     filter · map · flatMap · sorted · distinct · limit · skip · peek
│     mapToInt/Long/Double → IntStream / LongStream / DoubleStream
└── Terminais (fecham):
      forEach · collect · count · reduce · min · max
      findFirst · findAny · anyMatch · allMatch · noneMatch
      sum · average (nos primitivos)
```

# Cola de bolso

| Quero... | Use |
|---|---|
| Filtrar | `.filter(predicate)` |
| Transformar | `.map(function)` |
| Achatar listas/arrays aninhados | `.flatMap(x -> x.stream())` |
| Ordenar | `.sorted(comparator)` |
| Remover duplicados | `.distinct()` |
| Pegar os N primeiros | `.limit(n)` |
| Reunir em lista | `.collect(Collectors.toList())` |
| Contar | `.count()` |
| Somar números | `.mapToInt(...).sum()` / `.reduce(0, Integer::sum)` |
| Existe algum? | `.anyMatch(...)` |
| Primeiro que satisfaz | `.filter(...).findFirst()` |
| Intervalo de números | `IntStream.rangeClosed(1, 50)` |
| Sequência infinita | `Stream.iterate(...)` / `Stream.generate(...)` + `limit` |
| Linhas de arquivo | `Files.lines(path)` (try-with-resources) |
