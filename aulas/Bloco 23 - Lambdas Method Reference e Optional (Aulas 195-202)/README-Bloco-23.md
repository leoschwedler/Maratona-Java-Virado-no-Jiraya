# Bloco 23 — Lambdas, Method Reference e Optional

## Aulas 195 a 202

Este é o bloco da **programação funcional** em Java. Você viu na aula 194 a ideia de passar comportamento como parâmetro; agora vai conhecer as ferramentas oficiais do Java 8 para isso:

- **Lambdas**: funções anônimas curtas.
- **Interfaces funcionais** do pacote `java.util.function`: `Predicate`, `Consumer`, `Function`, `Supplier`, `BiFunction`...
- **Method References** (`Classe::metodo`): lambdas ainda mais enxutas.
- **`Optional`**: um "contêiner" que representa a presença ou a ausência de um valor, para fugir do `NullPointerException`.

As aulas deste bloco são:

```text
195 — Lambdas pt 01 — Predicate
196 — Lambdas pt 02 — Consumer
197 — Lambdas pt 03 — Function
198 — Method Reference pt 01 — Referência a métodos estáticos
199 — Method Reference pt 02 — Referência a métodos não estáticos
200 — Method Reference pt 03 — Referência a construtor
201 — Optional pt 01
202 — Optional pt 02
```

Evolução que você vai ver:

```text
classe anônima  →  lambda  →  method reference
(5 linhas)         (1 linha)   (1 expressão curta)
```

---

# Aula 195 — Lambdas pt 01 — `Predicate`

## 1. Requisito: interface funcional

Uma **interface funcional** é uma interface com **exatamente um método abstrato**. Ela pode ter métodos `default` e `static` à vontade — eles têm corpo e não contam.

```java
@FunctionalInterface
public interface CarPredicate {
    boolean test(Car car);          // o único abstrato

    default void exemplo() { }      // ok: tem corpo
}
```

A anotação `@FunctionalInterface` é opcional, mas é uma boa prática: se alguém tentar adicionar um segundo método abstrato, o compilador reclama.

> **Regra 1:** lambda só funciona onde o Java espera uma **interface funcional**.

---

## 2. Anatomia de uma lambda

```text
 (parâmetros)   ->   corpo
 ───────────        ─────────
   o que entra      o que faz / retorna
```

O **descritor da função** é a assinatura do único método abstrato da interface: ele define quantos parâmetros a lambda tem, os tipos e o tipo de retorno.

Para `boolean test(Car car)`:

```java
(Car car) -> car.getColor().equals("green")
```

### Formas de escrever (todas equivalentes)

```java
(Car car) -> { return car.getColor().equals("green"); }   // completa, com tipo, chaves e return
(Car car) -> car.getColor().equals("green")               // corpo-expressão: sem chaves e sem return
(car) -> car.getColor().equals("green")                   // tipo inferido
car -> car.getColor().equals("green")                     // um parâmetro: parênteses opcionais
```

Regras de sintaxe:

| Situação | Como escrever |
|---|---|
| Sem parâmetros | `() -> System.out.println("oi")` |
| Um parâmetro | `x -> x * 2` (parênteses opcionais, **se não declarar o tipo**) |
| Dois ou mais | `(a, b) -> a + b` |
| Corpo com várias instruções | chaves `{ ... }` e `return` explícito se retornar valor |
| Corpo com uma expressão | sem chaves e sem `return` |

O nome do parâmetro é livre (`car`, `c`, `x`...), mas o **tipo** tem que ser compatível com o da interface. Se estiver declarado e for incompatível → erro "incomparable types".

---

## 3. Lambdas são "funções", não métodos de classe

O instrutor destaca: lambdas costumam ser chamadas de **funções anônimas**, porque não pertencem a nenhuma classe nomeada (diferente de métodos). Objetivo principal: **código conciso**.

Comparação (aula 194):

```java
// classe anônima
filter(cars, new CarPredicate() {
    @Override
    public boolean test(Car car) {
        return car.getColor().equals("green");
    }
});

// lambda
filter(cars, car -> car.getColor().equals("green"));
```

## 4. A interface `Predicate<T>`

Já existe pronta em `java.util.function`:

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);

    // + métodos default: and(), or(), negate()
    // + static: isEqual()
}
```

```java
Predicate<String> vazia = s -> s.isEmpty();
Predicate<Integer> par = n -> n % 2 == 0;

par.test(4);                       // true
par.negate().test(4);              // false
par.and(n -> n > 10).test(12);     // true
par.or(n -> n < 0).test(-3);       // true
```

Resumo: **`Predicate<T>` recebe um `T` e devolve `boolean`** — é o "filtro".

## O que você precisa dominar (Aula 195)

- Interface funcional = 1 método abstrato (+ default/static).
- `@FunctionalInterface`.
- Sintaxe da lambda e variações.
- O descritor da função define o contrato.
- `Predicate<T>`: `test`, `and`, `or`, `negate`.

---

# Aula 196 — Lambdas pt 02 — `Consumer`

## 1. O que é

`Consumer<T>` **consome** um valor e **não retorna nada**: executa uma ação.

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);

    default Consumer<T> andThen(Consumer<? super T> after) { ... }
}
```

| Interface | Recebe | Retorna | Verbo |
|---|---|---|---|
| `Predicate<T>` | `T` | `boolean` | testa |
| `Consumer<T>` | `T` | `void` | consome / executa |

---

## 2. Criando seu próprio `forEach`

```java
private static <T> void forEach(List<T> lista, Consumer<T> consumer) {
    for (T elemento : lista) {
        consumer.accept(elemento);
    }
}
```

Usando:

```java
List<String> nomes = List.of("William", "DevDojo", "Suane");
forEach(nomes, (String s) -> System.out.println(s));
forEach(nomes, s -> System.out.println(s));          // tipo inferido, sem parênteses

List<Integer> numeros = List.of(1, 2, 3, 4, 5);
forEach(numeros, n -> System.out.println(n * 2));
```

O compilador **infere** o tipo do parâmetro (`String`, `Integer`) a partir de `T` da lista. Por isso `s ->` basta.

> Já existe `forEach` pronto em `Iterable`: `nomes.forEach(s -> System.out.println(s));`

## 3. Tipos de retorno

Como `accept` é `void`, o corpo da lambda é uma **instrução** (uma chamada de método, atribuição, etc.). Se a lambda tiver mais de uma instrução, use chaves:

```java
nomes.forEach(s -> {
    String maiusculo = s.toUpperCase();
    System.out.println(maiusculo);
});
```

## O que você precisa dominar (Aula 196)

- `Consumer<T>`: `void accept(T)`.
- Criar um método que recebe `Consumer<T>`.
- Inferência de tipos na lambda.
- `Iterable.forEach(Consumer)`.

---

# Aula 197 — Lambdas pt 03 — `Function`

## 1. O que é

`Function<T, R>` **transforma** um `T` em um `R`. Recebe um tipo e devolve outro (que pode ser igual ou diferente).

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);

    // default: andThen, compose;  static: identity()
}
```

Uso clássico: **map** — aplicar uma transformação a cada elemento de uma lista e coletar os resultados.

---

## 2. Implementando `map`

```java
private static <T, R> List<R> map(List<T> lista, Function<T, R> function) {
    List<R> resultado = new ArrayList<>();
    for (T elemento : lista) {
        R transformado = function.apply(elemento);
        resultado.add(transformado);
    }
    return resultado;
}
```

Usando:

```java
List<String> nomes = List.of("William", "DevDojo", "Suane", "Midorima", "Kuroko");

List<Integer> tamanhos = map(nomes, s -> s.length());        // String → Integer
List<String> maiusculos = map(nomes, s -> s.toUpperCase());  // String → String

System.out.println(tamanhos);    // [7, 7, 5, 8, 6]
```

O tipo dos parâmetros genéricos `T` e `R` é inferido pelo contexto.

---

## 3. As interfaces funcionais mais importantes

| Interface | Método | Para que serve |
|---|---|---|
| `Predicate<T>` | `boolean test(T)` | testar/filtrar |
| `Consumer<T>` | `void accept(T)` | executar ação |
| `Function<T,R>` | `R apply(T)` | transformar |
| `Supplier<T>` | `T get()` | fornecer/criar um valor (sem entrada) |
| `BiFunction<T,U,R>` | `R apply(T,U)` | transformar 2 entradas |
| `BiPredicate<T,U>` | `boolean test(T,U)` | testar 2 entradas |
| `BiConsumer<T,U>` | `void accept(T,U)` | ação com 2 entradas |
| `UnaryOperator<T>` | `T apply(T)` | `Function<T,T>` |
| `BinaryOperator<T>` | `T apply(T,T)` | `BiFunction<T,T,T>` |

Para primitivos existem versões que evitam *autoboxing* (`IntPredicate`, `IntFunction`, `ToIntFunction`, `IntSupplier`...).

## 4. Combinando funções

```java
Function<Integer, Integer> dobro = n -> n * 2;
Function<Integer, Integer> maisDez = n -> n + 10;

dobro.andThen(maisDez).apply(5);   // (5*2)+10 = 20   (dobro primeiro, depois maisDez)
dobro.compose(maisDez).apply(5);   // (5+10)*2 = 30   (maisDez primeiro, depois dobro)
```

## O que você precisa dominar (Aula 197)

- `Function<T, R>` e `apply`.
- Criar `map` genérico.
- As nove interfaces funcionais e quando usar cada uma.
- `andThen` e `compose`.

---

# Aula 198 — Method Reference pt 01 — Métodos estáticos

## 1. A ideia

Quando a lambda **apenas chama um método que já existe**, dá para trocar a lambda por uma **referência ao método**, usando `::`.

```java
s -> s.toUpperCase()          // lambda
String::toUpperCase           // method reference (mesmo efeito)
```

Há **quatro tipos** de method reference:

| Tipo | Sintaxe | Exemplo |
|---|---|---|
| 1. Método **estático** | `Classe::metodoEstatico` | `Integer::parseInt` |
| 2. Método de instância de um **objeto específico** | `objeto::metodo` | `System.out::println` |
| 3. Método de instância de um **objeto arbitrário do tipo** | `Classe::metodoDeInstancia` | `String::toUpperCase` |
| 4. **Construtor** | `Classe::new` | `Anime::new` |

Esta aula: o tipo 1.

---

## 2. Cenário: ordenar animes

```java
public class Anime {
    private String title;
    private int episodes;
    // construtor, getters, toString
}
```

Ordenando com lambda:

```java
List<Anime> animes = new ArrayList<>(List.of(
        new Anime("Berserk", 43),
        new Anime("One Piece", 900),
        new Anime("Naruto", 500)));

Collections.sort(animes, (a1, a2) -> a1.getTitle().compareTo(a2.getTitle()));
```

(Use `new ArrayList<>(...)` porque `List.of` é imutável e não pode ser ordenada.)

---

## 3. Criando um método estático que serve de "comparator"

A lambda é só um **contexto** (*target type*): o que importa para o Java é que o método tenha a **mesma assinatura** do `Comparator<Anime>.compare(Anime, Anime)` → `int`. Então qualquer método estático com essa assinatura pode ser referenciado:

```java
public class AnimeComparators {
    public static int compareByTitle(Anime a1, Anime a2) {
        return a1.getTitle().compareTo(a2.getTitle());
    }

    public static int compareByEpisodes(Anime a1, Anime a2) {
        return Integer.compare(a1.getEpisodes(), a2.getEpisodes());
    }
}
```

Agora:

```java
Collections.sort(animes, AnimeComparators::compareByTitle);       // por título
Collections.sort(animes, AnimeComparators::compareByEpisodes);    // por episódios
```

Observações do instrutor:

- Quem você está passando **não é um `Comparator`** que você criou — é uma referência ao método, que o Java adapta para a interface funcional esperada.
- Sem os parênteses: **`AnimeComparators::compareByTitle`** (não é uma chamada!). Se você escrever `AnimeComparators.compareByTitle(a1, a2)` estará *chamando* o método e passando o resultado (`int`), o que dá erro.
- Você pode criar vários métodos de comparação e escolher na hora de ordenar.

## 4. Exemplo da lambda x method reference

```java
List<String> nomes = Arrays.asList("c", "a", "b");

Function<String, Integer> conversor1 = s -> Integer.parseInt(s);   // lambda
Function<String, Integer> conversor2 = Integer::parseInt;          // method reference
```

## O que você precisa dominar (Aula 198)

- Os 4 tipos de method reference.
- `Classe::metodoEstatico`.
- O método referenciado deve ter assinatura compatível com a interface funcional.
- `::` não chama o método; apenas o referencia.

---

# Aula 199 — Method Reference pt 02 — Métodos não estáticos

## 1. Tipo 2 — Método de instância de um **objeto específico**

Você tem **um objeto** em mãos e quer usar um método dele como função:

```java
public class AnimeComparators {
    public int compareByEpisodesNonStatic(Anime a1, Anime a2) {      // sem static
        return Integer.compare(a1.getEpisodes(), a2.getEpisodes());
    }
}

AnimeComparators comparators = new AnimeComparators();
animes.sort(comparators::compareByEpisodesNonStatic);          // objeto::metodo
```

Aqui o `::` vem depois de uma **variável** (o objeto), não depois do nome da classe. A lambda equivalente:

```java
animes.sort((a1, a2) -> comparators.compareByEpisodesNonStatic(a1, a2));
```

Exemplo clássico: `System.out::println` — `System.out` é um objeto (`PrintStream`) específico.

```java
nomes.forEach(System.out::println);      // = nome -> System.out.println(nome)
```

---

## 2. Tipo 3 — Método de instância de um **objeto arbitrário** de um tipo

Aqui você usa o **nome da classe**, mas o método é **não estático**. O Java usa o **primeiro parâmetro como o objeto** que vai "receber" a chamada, e os demais como argumentos.

```java
List<String> nomes = new ArrayList<>(List.of("William", "DevDojo", "Suane"));

nomes.sort(String::compareTo);
// equivale a: nomes.sort((s1, s2) -> s1.compareTo(s2));
```

Tradução mental: `String::compareTo` ⇒ `(s1, s2) -> s1.compareTo(s2)` — o primeiro argumento (`s1`) é o dono do método, o segundo (`s2`) é o parâmetro.

Mais exemplos:

```java
Function<String, Integer> tamanho = String::length;         // s -> s.length()
Function<String, String>  maiuscula = String::toUpperCase;  // s -> s.toUpperCase()
Function<String, Integer> converter = Integer::parseInt;    // estático (tipo 1)
```

### `BiPredicate` e `contains`

```java
BiPredicate<List<String>, String> contem = List::contains;
// (lista, nome) -> lista.contains(nome)

boolean tem = contem.test(nomes, "William");
```

`BiPredicate` recebe **dois** argumentos: o primeiro é a lista (dono do método `contains`), o segundo é o parâmetro. É a mesma regra do tipo 3.

---

## 3. Comparando os 3 primeiros tipos

```text
Integer::parseInt         (tipo 1) → método ESTÁTICO       → s -> Integer.parseInt(s)
comparators::metodo       (tipo 2) → objeto específico     → (a,b) -> comparators.metodo(a,b)
String::compareTo         (tipo 3) → objeto arbitrário     → (a,b) -> a.compareTo(b)
```

O instrutor admite que tentar explicar com palavras é confuso; o melhor é **praticar** e usar a IDE: ela converte lambda ↔ method reference com `Alt+Enter`.

## O que você precisa dominar (Aula 199)

- `objeto::metodo` x `Classe::metodoDeInstancia`.
- Em `Classe::metodo` não estático, o 1º parâmetro vira o "dono".
- `System.out::println`.
- `BiPredicate`, `Function<String,Integer>` com method reference.

---

# Aula 200 — Method Reference pt 03 — Construtores

## 1. `Supplier<T>` — fornecer um objeto

`Supplier<T>` não recebe nada e **devolve** um `T`:

```java
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

Com construtor sem argumentos (precisa existir um construtor vazio):

```java
Supplier<AnimeComparators> supplier = AnimeComparators::new;     // referência ao construtor
AnimeComparators comparators = supplier.get();                   // AQUI o objeto é criado
```

Ponto importante: `AnimeComparators::new` **não cria nada** por si só — só descreve "como criar". O objeto é criado a cada `supplier.get()`.

---

## 2. `BiFunction<T, U, R>` — construtor com 2 parâmetros

Para `new Anime(String titulo, int episodios)`:

```java
BiFunction<String, Integer, Anime> animeCreator = Anime::new;
Anime anime = animeCreator.apply("Berserk", 43);
```

O Java escolhe, entre os construtores disponíveis, aquele que casa com a assinatura da interface (`String` e `Integer`→`int` por autoboxing/unboxing).

Equivalente em lambda:

```java
BiFunction<String, Integer, Anime> animeCreator = (titulo, ep) -> new Anime(titulo, ep);
```

## 3. Outras variações

```java
Function<String, Anime> criarComTitulo = Anime::new;            // construtor com 1 String
Supplier<List<String>> novaLista = ArrayList::new;
Function<Integer, int[]> criarArray = int[]::new;               // array
```

| Construtor | Interface adequada |
|---|---|
| sem parâmetros | `Supplier<T>` |
| 1 parâmetro | `Function<A, T>` |
| 2 parâmetros | `BiFunction<A, B, T>` |
| 3+ | interface funcional customizada |

## 4. Para que serve na prática

Fábricas (factories), `Collectors.toCollection(TreeSet::new)`, `stream.map(Pessoa::new)`, injeção de "como criar". Você verá bastante em Streams.

## O que você precisa dominar (Aula 200)

- `Classe::new`.
- `Supplier`, `Function`, `BiFunction` para construtores.
- O objeto só é criado quando você chama `get()`/`apply()`.
- O construtor é escolhido pela assinatura da interface funcional.

---

# Aula 201 — Optional pt 01

## 1. O problema do `null`

```java
static String findName(String name) {
    List<String> lista = List.of("William", "DevDojo");
    int index = lista.indexOf(name);
    if (index == -1) return null;      // ← "não achei" representado por null
    return lista.get(index);
}

String nome = findName("Ana");
System.out.println(nome.toUpperCase());   // 💥 NullPointerException
```

O `null` é traiçoeiro: nada na assinatura do método avisa que pode ser nulo. O criador do `null` (Tony Hoare) chamou isso de "meu erro de um bilhão de dólares".

## 2. `Optional<T>` — uma caixa que pode estar vazia

`java.util.Optional<T>` (Java 8) **encapsula** um valor que pode **estar ou não presente**. O objetivo: tornar a possibilidade de ausência **explícita no tipo de retorno**.

> O intuito não é eliminar todo `NullPointerException` do mundo, e sim **facilitar o retorno de métodos que podem não ter resultado**.

---

## 3. Criando um `Optional`

```java
Optional<String> o1 = Optional.of("William");          // valor NÃO nulo (se for null → NullPointerException)
Optional<String> o2 = Optional.ofNullable(talvezNulo); // aceita null → vira Optional vazio
Optional<String> o3 = Optional.empty();                // vazio
```

| Método | Quando usar |
|---|---|
| `of(x)` | você tem certeza de que `x != null` |
| `ofNullable(x)` | `x` pode ser nulo |
| `empty()` | quer retornar "nada" explicitamente |

Imprimindo: `Optional[William]` ou `Optional.empty`.

---

## 4. Retornando `Optional` do método

```java
static Optional<String> findName(String name) {
    List<String> lista = List.of("William", "DevDojo");
    int index = lista.indexOf(name);
    if (index == -1) return Optional.empty();
    return Optional.of(lista.get(index));
}
```

Agora quem chama é **forçado a pensar** no caso "não encontrado".

---

## 5. Extraindo o valor com segurança

```java
Optional<String> opt = findName("Ana");

opt.isPresent();                          // true/false
opt.isEmpty();                            // (Java 11) o contrário

String a = opt.orElse("não encontrado");               // valor padrão
String b = opt.orElseGet(() -> "calculado agora");     // padrão via Supplier (só executa se vazio)
String c = opt.orElseThrow(() -> new IllegalArgumentException("Nome não encontrado"));

opt.ifPresent(s -> System.out.println(s.toUpperCase()));   // só executa se houver valor (Consumer)
```

### `orElse` x `orElseGet`

- `orElse(x)`: `x` é **sempre calculado**, mesmo se o `Optional` tiver valor.
- `orElseGet(supplier)`: só chama o `supplier` se **vazio**. Prefira quando o valor padrão for caro de criar.

### Cuidado com `get()`

`opt.get()` lança `NoSuchElementException` se estiver vazio. Evite; prefira `orElse...`/`ifPresent`.

---

## 6. Encadeando (estilo funcional)

```java
findName("William")
    .map(String::toUpperCase)        // transforma se existir
    .filter(s -> s.length() > 3)     // descarta se não passar
    .ifPresent(System.out::println);
```

Um `map` num `Optional` vazio continua vazio — sem `if`, sem `NullPointerException`.

## O que você precisa dominar (Aula 201)

- Por que `Optional` existe.
- `of`, `ofNullable`, `empty`.
- Retornar `Optional` de um método.
- `isPresent`, `ifPresent`, `orElse`, `orElseGet`, `orElseThrow`.
- Evitar `get()` cego.

---

# Aula 202 — Optional pt 02

## 1. Onde NÃO usar `Optional`

O IntelliJ avisa (e o instrutor explica):

| ❌ Evite | Por quê |
|---|---|
| **Parâmetro** de método | complica a chamada; use sobrecarga ou o valor puro |
| **Atributo** de classe | `Optional` **não é `Serializable`**, e frameworks (como JPA/Hibernate) esperam tipos simples nos atributos |
| **Coleções** (`Optional<List<T>>`) | retorne a lista vazia |

> **Regra:** `Optional` serve principalmente para o **retorno** de métodos.

---

## 2. Exemplo prático: repositório de mangás

Classe de domínio:

```java
public class Manga {
    private Integer id;
    private String title;
    private int episodes;
    // construtor, getters, setter só do title, toString
}
```

"Banco de dados" em memória:

```java
public class MangaRepository {
    private static List<Manga> mangas = new ArrayList<>(List.of(
            new Manga(1, "Boku no Hero", 50),
            new Manga(2, "Overlord", 25)));

    public static Optional<Manga> findByTitle(String title) { ... }
    public static Optional<Manga> findById(Integer id) { ... }
}
```

Se você escrevesse `findByTitle` e `findById` separadamente, teria duas buscas quase idênticas. Aplicando o que você aprendeu em comportamento por parâmetro, o laço fica em um só lugar:

```java
private static Optional<Manga> findBy(Predicate<Manga> predicate) {
    for (Manga manga : mangas) {
        if (predicate.test(manga)) {
            return Optional.of(manga);
        }
    }
    return Optional.empty();
}

public static Optional<Manga> findByTitle(String title) {
    return findBy(manga -> manga.getTitle().equals(title));
}

public static Optional<Manga> findById(Integer id) {
    return findBy(manga -> manga.getId().equals(id));
}
```

Ou mais curto:

```java
return mangas.stream().filter(m -> m.getTitle().equals(title)).findFirst();   // Streams (próximos blocos)
```

---

## 3. Os três requisitos, com `Optional`

### Requisito 1 — "se existir, atualize o título; senão, não faça nada"

```java
MangaRepository.findByTitle("Boku no Hero")
        .ifPresent(m -> m.setTitle("Boku no Hero 2"));
```

### Requisito 2 — "procure por id; se não existir, lance uma exceção"

```java
Manga manga = MangaRepository.findById(3)
        .orElseThrow(() -> new IllegalArgumentException("Manga not found"));
```

O `orElseThrow` recebe um `Supplier` que cria a exceção (só é criada se necessário).

### Requisito 3 — "procure por título; se não existir, crie um novo"

```java
Manga manga = MangaRepository.findByTitle("Dragon Ball")
        .orElseGet(() -> new Manga(3, "Dragon Ball", 500));
```

### Comparando com `if/else`

Sem `Optional`:

```java
Manga m = findByTitle("X");
if (m != null) {
    m.setTitle("Y");
} else {
    ...
}
```

Com `Optional` o código "se lê" quase como uma frase: **"encontre pelo título, ou então lance exceção"**. Quem não é programador consegue entender o fluxo. Esse é o grande ganho da programação funcional: **legibilidade**.

## 4. Resumo final de boas práticas

```text
✅ Optional como RETORNO de método que pode não ter resultado
✅ ifPresent / orElse / orElseGet / orElseThrow / map / filter
❌ Optional como parâmetro
❌ Optional como atributo
❌ opt.get() sem verificar
❌ Optional.of(null)
```

## O que você precisa dominar (Aula 202)

- Onde usar e onde não usar `Optional`.
- `ifPresent`, `orElseThrow`, `orElseGet` em cenários reais.
- Combinar `Optional` + `Predicate` + comportamento por parâmetro.
- Ganho de legibilidade.

---

# Mapa mental do bloco

```text
Programação funcional (Java 8)
├── Interface funcional (1 método abstrato) + @FunctionalInterface
├── Lambda:  (params) -> corpo
├── java.util.function
│    ├── Predicate<T>      test      → boolean
│    ├── Consumer<T>       accept    → void
│    ├── Function<T,R>     apply     → R
│    ├── Supplier<T>       get       → T
│    └── Bi*, Unary/BinaryOperator
├── Method Reference (::)
│    ├── Classe::estatico            (Integer::parseInt)
│    ├── objeto::metodo              (System.out::println)
│    ├── Classe::metodoDeInstancia   (String::toUpperCase)
│    └── Classe::new                 (Anime::new)
└── Optional<T>
     ├── criar:  of / ofNullable / empty
     └── usar:   isPresent / ifPresent / orElse / orElseGet / orElseThrow / map / filter
```

# Cola de bolso

| Quero... | Use |
|---|---|
| Testar uma condição | `Predicate<T>` |
| Executar uma ação sobre cada item | `Consumer<T>` |
| Transformar A em B | `Function<A, B>` |
| Criar um objeto sob demanda | `Supplier<T>` |
| Lambda que só chama um método | method reference |
| Método que pode não ter resultado | retornar `Optional<T>` |
| Valor padrão se vazio | `orElse(x)` / `orElseGet(() -> ...)` |
| Exceção se vazio | `orElseThrow(() -> new ...)` |
