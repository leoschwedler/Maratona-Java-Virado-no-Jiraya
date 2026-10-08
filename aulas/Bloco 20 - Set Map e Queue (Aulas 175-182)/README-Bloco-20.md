# Bloco 20 — `Set`, `Map` e `Queue`

## Aulas 175 a 182

Você já domina a `List`. Agora vai completar o *Collections Framework* com as outras três grandes famílias:

- **`Set`** — conjunto **sem duplicados**;
- **`Map`** — pares **chave → valor**;
- **`Queue`** — **fila** (primeiro a entrar, primeiro a sair, ou por prioridade).

As aulas deste bloco são:

```text
175 — Set, HashSet (e LinkedList)
176 — NavigableSet, TreeSet pt 01
177 — NavigableSet, TreeSet pt 02
178 — Map, HashMap, LinkedHashMap pt 01
179 — Map, HashMap, LinkedHashMap pt 02
180 — Map, HashMap, LinkedHashMap pt 03
181 — NavigableMap, TreeMap
182 — Queue, PriorityQueue
```

## O mapa das coleções

```text
Iterable
 └── Collection
      ├── List   → ArrayList, LinkedList
      ├── Set    → HashSet, LinkedHashSet, TreeSet (SortedSet → NavigableSet)
      └── Queue  → PriorityQueue, LinkedList, ArrayDeque

Map (NÃO é Collection) → HashMap, LinkedHashMap, TreeMap (SortedMap → NavigableMap)
```

### Como escolher (regra de ouro)

| Característica | Hash... | LinkedHash... | Tree... |
|---|---|---|---|
| Ordem | **nenhuma** garantida | ordem de **inserção** | **ordenada** (Comparable/Comparator) |
| Desempenho | `O(1)` | `O(1)` | `O(log n)` |
| Duplicidade decidida por | `equals` + `hashCode` | `equals` + `hashCode` | `compareTo`/`compare` |
| Aceita `null`? | sim (1) | sim (1) | **não** (a chave/elemento) |

---

# Aula 175 — Set, HashSet (e uma pausa em `LinkedList`)

## 1. Trocando a implementação de uma `List` — `LinkedList`

A aula começa provando o poder de programar para a interface: basta trocar `new ArrayList<>()` por `new LinkedList<>()` e o resto do código continua igual.

```java
List<Manga> mangas = new LinkedList<>();    // antes: new ArrayList<>()
```

| | `ArrayList` | `LinkedList` |
|---|---|---|
| Estrutura | array que cresce | cada nó conhece o **anterior** e o **próximo** |
| `get(i)` | `O(1)` | `O(n)` |
| Inserir/remover nas pontas | `O(1)`/`O(n)` | `O(1)` |
| Memória | menor | maior (guarda ponteiros) |

Regra prática: **na dúvida, `ArrayList`**. Use `LinkedList` quando faz muitas inserções/remoções nas pontas (como fila).

---

## 2. `Set` — o conjunto sem duplicados

`Set` é uma `Collection` que **não permite elementos repetidos**.

```java
Set<Manga> mangas = new HashSet<>();

mangas.add(new Manga(5L, "Berserk", 19.9));
mangas.add(new Manga(1L, "Dragon Ball Z", 2.99));
mangas.add(new Manga(1L, "Dragon Ball Z", 2.99));   // duplicado: ignorado!

System.out.println(mangas.size());   // 2
```

### Características

- **Sem índice**: não existe `get(i)`. Para percorrer use `for-each` (ou `Iterator`).
- `add` devolve `boolean`: `true` se inseriu, `false` se já existia.
- **Ordem não garantida** no `HashSet` — os elementos são organizados pelo **hash**. Ao imprimir, a ordem pode parecer aleatória e pode mudar.
- A detecção de duplicado usa **`hashCode` + `equals`**. Por isso sua classe precisa tê-los bem implementados.

```java
for (Manga manga : mangas) {
    System.out.println(manga);
}
```

### Como o `HashSet` decide que é duplicado

```text
1) calcula hashCode()  → vai para a "caixa"
2) compara com equals() os itens que já estão naquela caixa
3) se algum equals → true: duplicado, NÃO insere
```

### A importância da regra de negócio no `equals`

Se o `equals` do `Manga` considera `id`, `nome` **e** `preco`, dois mangás com mesmo id mas preço diferente são "diferentes" e ambos entram. Se a regra do negócio é "mesmo id = mesmo mangá", gere `equals`/`hashCode` **só com o `id`** (ou id + nome). Pense: o que identifica unicamente o objeto?

### Uso prático: eliminar duplicatas

```java
List<Manga> comDuplicados = ...;
Set<Manga> unicos = new HashSet<>(comDuplicados);   // duplicatas somem
```

Dados de banco sem `DISTINCT`? Joga num `Set`.

---

## 3. `LinkedHashSet` — mantém a ordem de inserção

```java
Set<Manga> mangas = new LinkedHashSet<>();
```

Usa hash (rápido) **e** mantém uma lista interna com a ordem em que os elementos foram inseridos. Imprime na ordem em que você adicionou.

| | `HashSet` | `LinkedHashSet` |
|---|---|---|
| Ordem | imprevisível | inserção |
| Velocidade | um pouco mais rápido | um pouco mais lento |

## O que você precisa dominar (Aula 175)

- `Set` não permite duplicados e não tem índice.
- `HashSet` x `LinkedHashSet`.
- Duplicidade via `equals` + `hashCode`.
- `new HashSet<>(lista)` para remover duplicatas.
- `LinkedList` vs `ArrayList`.

---

# Aula 176 — `NavigableSet` e `TreeSet` (parte 1)

## 1. Hierarquia

```text
Set
 └── SortedSet
      └── NavigableSet  ← interface
           └── TreeSet  ← implementação
```

`TreeSet` mantém os elementos **sempre ordenados**, reordenando a cada inserção. Internamente é uma árvore balanceada (rubro-negra), por isso o custo é `O(log n)` para inserir/buscar/remover.

---

## 2. Requisito: saber comparar

Para ordenar, o `TreeSet` precisa de um critério. Duas opções:

### a) A classe implementa `Comparable`

```java
Set<Manga> mangas = new TreeSet<>();   // usa o compareTo do Manga
```

### b) Você passa um `Comparator` no construtor

```java
Set<Smartphone> smartphones = new TreeSet<>(new SmartphoneMarcaComparator());
```

Se a classe **não** é `Comparable` e você não passa `Comparator`:

```text
ClassCastException: Smartphone cannot be cast to java.lang.Comparable
```

(a exceção aparece ao tentar inserir o primeiro elemento).

---

## 3. Exemplo

```java
Set<Manga> mangas = new TreeSet<>();       // Manga implements Comparable (por nome)
mangas.add(new Manga(5L, "Berserk", 19.9));
mangas.add(new Manga(1L, "Dragon Ball Z", 2.99));
mangas.add(new Manga(4L, "Pokémon", 3.2));
mangas.add(new Manga(3L, "Attack on Titan", 11.2));

System.out.println(mangas);   // ordem alfabética pelo nome
```

Se o `compareTo` ordena por nome, a saída vem em ordem alfabética. Mude o critério (por exemplo, por preço) e a ordem muda. Cada nova inserção reorganiza a coleção.

Dois TreeSets diferentes podem ordenar a mesma classe de formas diferentes — basta passar comparators diferentes.

## O que você precisa dominar (Aula 176)

- `TreeSet` implementa `NavigableSet` e mantém ordem.
- Precisa de `Comparable` na classe **ou** `Comparator` no construtor.
- `ClassCastException` quando falta critério.
- Custo `O(log n)`.

---

# Aula 177 — `NavigableSet` e `TreeSet` (parte 2)

## 1. ⚠️ O `TreeSet` NÃO usa `equals` para detectar duplicados

Esse é o ponto mais importante da aula. O contrato do `Set` diz que usa `equals`, mas o `TreeSet` usa o **`compareTo`/`compare`**:

> Dois elementos são "o mesmo" para o `TreeSet` quando a comparação devolve **0**.

Consequência:

```java
// compareTo compara só o NOME
new Manga(1L, "Dragon Ball Z", 2.99);
new Manga(9L, "Dragon Ball Z", 5.00);   // id diferente, mas mesmo nome → compareTo = 0

treeSet.add(m1);   // true
treeSet.add(m2);   // false!  → considerado duplicado, mesmo com equals false
```

E o inverso: se o `compareTo` ordenar por **preço** e dois mangás diferentes tiverem o mesmo preço, o segundo **não entra**.

Regra de ouro: **mantenha o `compareTo` consistente com o `equals`** (devolve 0 exatamente quando `equals` é `true`). Se for ordenar por um atributo que pode repetir (preço), acrescente **desempate** (por exemplo, por `id`).

---

## 2. `descendingSet()` — ordem inversa

```java
Set<Manga> inverso = ((NavigableSet<Manga>) mangas).descendingSet();
```

(Para usar os métodos de `NavigableSet`, declare a variável com o tipo `NavigableSet<Manga>`.)

---

## 3. Comparator por preço

```java
class MangaPrecoComparator implements Comparator<Manga> {
    @Override
    public int compare(Manga a, Manga b) {
        return Double.compare(a.getPreco(), b.getPreco());
    }
}

NavigableSet<Manga> porPreco = new TreeSet<>(new MangaPrecoComparator());
```

---

## 4. Métodos de navegação

Dado um elemento de referência `x`:

| Método | Devolve |
|---|---|
| `lower(x)` | o maior elemento **estritamente menor** que `x` (`< x`) |
| `floor(x)` | o maior elemento **menor ou igual** a `x` (`<= x`) |
| `higher(x)` | o menor elemento **estritamente maior** que `x` (`> x`) |
| `ceiling(x)` | o menor elemento **maior ou igual** a `x` (`>= x`) |

Todos devolvem `null` se não existir.

Exemplo da aula: mangás com preços `2.99, 3.2, 11.2, 19.9`:

```text
referência com preço 8.0 → lower = 3.2  (anterior imediato)    higher = 11.2 (seguinte imediato)
referência com preço 3.2 → lower = 2.99 (exclui o igual)       floor  = 3.2  (inclui o igual)
                           higher = 11.2                        ceiling = 3.2 (inclui o igual)
```

Lembrete simples:

```text
lower  <     floor  <=     ceiling  >=     higher  >
```

---

## 5. Retirando elementos

```java
mangas.first();       // primeiro elemento (não remove)
mangas.last();        // último
mangas.pollFirst();   // remove E devolve o primeiro (null se vazio)
mangas.pollLast();    // remove E devolve o último
mangas.size();        // diminui após o poll
```

E recortes (visões ligadas ao original):

```java
headSet(x)        // elementos antes de x
tailSet(x)        // x e os depois
subSet(a, b)      // de a (incl.) até b (excl.)
```

## O que você precisa dominar (Aula 177)

- `TreeSet` decide duplicidade por `compareTo`/`compare` (resultado 0), **não por `equals`**.
- `descendingSet`.
- `lower`, `floor`, `higher`, `ceiling`.
- `first`, `last`, `pollFirst`, `pollLast`.

---

# Aula 178 — `Map`, `HashMap`, `LinkedHashMap` (parte 1)

## 1. O que é um `Map`

Estrutura de dados que associa uma **chave** a um **valor**: `Map<K, V>`.

```text
Chave (única)  →  Valor
"teclado"      →  "teclado"
"mouse"        →  "mouse"
"cpf 123"      →  Cliente(João)
```

> `Map` **não é** uma `Collection` (não estende a interface), apesar de fazer parte do Collections Framework.

Regras fundamentais:

- **Chaves são únicas** (um `Set`).
- Valores podem se repetir.
- Se você faz `put` com uma chave existente, o valor **é substituído** (e o antigo é devolvido).
- O `HashMap` usa `hashCode` + `equals` **da chave** — logo a classe da chave precisa deles bem implementados (Strings, Integer etc. já têm).

---

## 2. Operações básicas

```java
Map<String, String> map = new HashMap<>();

map.put("teclado", "teclado");
map.put("mouse", "mouse");
map.put("monitor", "monitor");

map.put("teclado", "TECLADO NOVO");        // substitui o valor da chave existente
map.putIfAbsent("mouse", "outro");         // NÃO substitui: só insere se a chave não existir (Java 8)

String v = map.get("mouse");               // "mouse"; null se a chave não existir
map.getOrDefault("fone", "sem estoque");   // valor padrão se não existir (Java 8)
map.containsKey("mouse");                  // true
map.containsValue("mouse");                // true (percorre tudo → O(n))
map.remove("monitor");
map.size();
System.out.println(map);                   // {teclado=TECLADO NOVO, mouse=mouse}
```

---

## 3. Navegando em um `Map`: 3 visões

| Método | Retorna | Tipo |
|---|---|---|
| `keySet()` | todas as chaves | `Set<K>` (únicas) |
| `values()` | todos os valores | `Collection<V>` (podem repetir) |
| `entrySet()` | pares chave-valor | `Set<Map.Entry<K, V>>` |

```java
// só as chaves
for (String chave : map.keySet()) {
    System.out.println(chave + " = " + map.get(chave));   // pega o valor pela chave
}

// só os valores
for (String valor : map.values()) {
    System.out.println(valor);
}

// chave E valor juntos (a forma mais eficiente)
for (Map.Entry<String, String> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

O instrutor destaca:

- Não existe um jeito simples de obter a chave a partir do valor (e nem faria sentido: vários valores podem se repetir).
- O `entrySet()` evita chamar `get(chave)` a cada volta (o `get` é uma busca a mais).
- `Map.Entry` também permite `setValue(...)`, que altera o valor no próprio mapa.

---

## 4. Ordem no `HashMap` e no `LinkedHashMap`

```java
Map<String, String> m1 = new HashMap<>();         // ordem imprevisível
Map<String, String> m2 = new LinkedHashMap<>();   // mantém a ordem de inserção
```

(Quando o instrutor inseriu a quarta chave, a ordem do `HashMap` mudou — exatamente por ser baseada em hash.)

## O que você precisa dominar (Aula 178)

- `Map` = chave única → valor; não é `Collection`.
- `put` substitui, `putIfAbsent` não; `get`, `getOrDefault`, `containsKey`, `remove`.
- `keySet`, `values`, `entrySet` (e `Map.Entry`).
- `HashMap` x `LinkedHashMap`.

---

# Aula 179 — `Map` pt 02: objetos como chave e valor

## 1. Cenário

Um `Consumidor` compra um `Manga`. Queremos associar quem comprou o quê.

```java
public class Consumidor {
    private Long id;
    private String nome;

    public Consumidor(String nome) {
        this.id = ThreadLocalRandom.current().nextLong(0, 100_000);   // id aleatório
        this.nome = nome;
    }
    // equals e hashCode só por id, getters, toString
}
```

Pontos:

- `ThreadLocalRandom.current().nextLong(0, 100_000)` gera um id pseudoaleatório limitado (a aula comenta que `Random` sozinho geraria valores gigantescos ou negativos).
- Como o `Consumidor` é a **chave**, **`equals` e `hashCode` são obrigatórios** e baseados em um identificador único.

---

## 2. `Map<Consumidor, Manga>`

```java
Consumidor c1 = new Consumidor("William");
Consumidor c2 = new Consumidor("DevDojo");

Manga m1 = new Manga(5L, "Berserk", 19.9);
Manga m2 = new Manga(1L, "Dragon Ball Z", 2.99);

Map<Consumidor, Manga> consumidorManga = new HashMap<>();
consumidorManga.put(c1, m1);
consumidorManga.put(c2, m2);
```

Percorrendo:

```java
for (Map.Entry<Consumidor, Manga> entry : consumidorManga.entrySet()) {
    System.out.println(entry.getKey().getNome() + " comprou " + entry.getValue().getNome());
}
```

`getKey()` devolve um `Consumidor` e `getValue()` um `Manga`, então você acessa os getters deles normalmente. Se quiser imprimir direto (`println(entry.getKey())`), sobrescreva `toString`.

## O que você precisa dominar (Aula 179)

- Qualquer objeto pode ser chave, desde que tenha `equals`/`hashCode` coerentes.
- `Map<Objeto, Objeto>` e navegação com `entrySet`.
- Gerar ids aleatórios limitados.

---

# Aula 180 — `Map` pt 03: `Map<K, List<V>>`

## 1. O problema

Um consumidor pode comprar **vários** mangás. Um `Map<Consumidor, Manga>` só guarda **um valor por chave**. A solução: o valor passa a ser uma **lista**.

```java
Map<Consumidor, List<Manga>> consumidorMangas = new HashMap<>();
```

Tradução: "para cada consumidor (chave), uma lista de mangás (valor)".

```text
William  → [Berserk, Dragon Ball Z, Pokémon]
DevDojo  → [Attack on Titan, Naruto]
```

---

## 2. Montando

```java
List<Manga> mangasDoWilliam = new ArrayList<>(List.of(m1, m2, m3));
List<Manga> mangasDoDevDojo = new ArrayList<>(List.of(m4, m5));

consumidorMangas.put(c1, mangasDoWilliam);
consumidorMangas.put(c2, mangasDoDevDojo);
```

---

## 3. Navegando

```java
for (Map.Entry<Consumidor, List<Manga>> entry : consumidorMangas.entrySet()) {
    System.out.println(entry.getKey().getNome());

    for (Manga manga : entry.getValue()) {      // percorre a lista do valor
        System.out.println("   - " + manga.getNome());
    }
}
```

É um `for` dentro de outro: primeiro o par, depois a lista do valor.

## 4. Adicionando uma compra a um consumidor existente

```java
List<Manga> lista = consumidorMangas.get(c1);
if (lista == null) {
    lista = new ArrayList<>();
    consumidorMangas.put(c1, lista);
}
lista.add(novoManga);
```

Versão enxuta (Java 8):

```java
consumidorMangas.computeIfAbsent(c1, k -> new ArrayList<>()).add(novoManga);
```

Esse padrão (**agrupar** itens por chave) é um dos usos mais comuns de `Map` em sistemas reais — e será a base do `Collectors.groupingBy` das aulas de Streams.

## O que você precisa dominar (Aula 180)

- `Map<K, List<V>>` para um-para-muitos.
- Navegar com dois laços.
- Adicionar ao valor existente (e o caso da chave inexistente).
- `computeIfAbsent`.

---

# Aula 181 — `NavigableMap` e `TreeMap`

## 1. `TreeMap`

Mesma ideia do `TreeSet`, só que para mapas: mantém as **chaves ordenadas**.

```java
NavigableMap<String, String> map = new TreeMap<>();
map.put("D", "letra D");
map.put("A", "letra A");
map.put("C", "letra C");
map.put("B", "letra B");

for (Map.Entry<String, String> e : map.entrySet()) {
    System.out.println(e.getKey() + " - " + e.getValue());
}
// A - letra A
// B - letra B
// C - letra C
// D - letra D
```

- A ordenação é pela **chave**. O valor **não** precisa ser comparável.
- A chave precisa ser `Comparable` ou você passa um `Comparator`:

```java
NavigableMap<Consumidor, String> m = new TreeMap<>();   // ❌ ClassCastException se Consumidor não for Comparable
NavigableMap<Consumidor, String> m = new TreeMap<>(new ConsumidorComparator());   // ✅
```

- Como no `TreeSet`, chaves "iguais" são as que o `compareTo`/`compare` considera 0.

---

## 2. Métodos de navegação (similares ao `NavigableSet`)

Para as **chaves**:

| Retorna o par (`Entry`) | Retorna só a chave |
|---|---|
| `lowerEntry(k)` | `lowerKey(k)` |
| `floorEntry(k)` | `floorKey(k)` |
| `higherEntry(k)` | `higherKey(k)` |
| `ceilingEntry(k)` | `ceilingKey(k)` |
| `firstEntry()` / `lastEntry()` | `firstKey()` / `lastKey()` |
| `pollFirstEntry()` / `pollLastEntry()` | – |

A diferença `...Entry` x `...Key` é só o **tipo de retorno** (par completo × só a chave).

---

## 3. Recortes — `headMap`, `tailMap`, `subMap`

```java
SortedMap<String, String> antesDoC = map.headMap("C");          // A, B   (exclui C)
NavigableMap<String, String> ateOC = map.headMap("C", true);    // A, B, C (inclui C)
```

| Método | Pega |
|---|---|
| `headMap(k)` / `headMap(k, inclusive)` | chaves **antes** de `k` |
| `tailMap(k)` / `tailMap(k, inclusive)` | chaves de `k` em diante |
| `subMap(a, b)` | de `a` até `b` |

### ⚠️ São VISÕES ligadas ao mapa original

Remover algo do `headMap` remove do `TreeMap` original (e vice-versa). Para uma cópia independente: `new TreeMap<>(map.headMap("C"))`.

## O que você precisa dominar (Aula 181)

- `TreeMap` = `Map` com chaves ordenadas.
- Chave precisa de `Comparable` ou `Comparator`.
- `lower/floor/higher/ceiling` em versões `Key` e `Entry`.
- `headMap`, `tailMap`, `subMap` são visões ligadas.

---

# Aula 182 — `Queue` e `PriorityQueue`

## 1. A `Queue` (fila)

Por padrão segue **FIFO** (*First In, First Out*): o primeiro que entra é o primeiro que sai — como uma fila de banco.

Métodos principais (cada operação tem duas versões: lança exceção ou devolve valor especial):

| Operação | Lança exceção se falhar | Devolve `null`/`false` |
|---|---|---|
| Inserir | `add(e)` | `offer(e)` |
| Remover o primeiro | `remove()` | `poll()` |
| Olhar o primeiro (sem remover) | `element()` | `peek()` |

- **`poll()`**: tira a cabeça da fila e devolve (ou `null` se vazia).
- **`peek()`**: só espia a cabeça, **sem remover**.
- `add` lança exceção se a fila tem capacidade limitada e está cheia; `offer` devolve `false`.

O instrutor sugere sempre usar `offer`/`poll`/`peek`.

---

## 2. `PriorityQueue` — fila com prioridade

Em vez de FIFO puro, quem sai primeiro é o **elemento de maior prioridade**, definida pela **ordenação** (`Comparable` ou `Comparator`): o **menor** pelo critério de comparação é o "primeiro".

```java
Queue<String> fila = new PriorityQueue<>();
fila.offer("C");
fila.offer("A");
fila.offer("B");

while (!fila.isEmpty()) {
    System.out.println(fila.poll());   // A, B, C
}
```

### ⚠️ Pontos que enganam

1. **A ordem interna não é a ordem de saída.** A `PriorityQueue` é uma *heap* (monte). Se você apenas imprimir (`System.out.println(fila)`) ou percorrer com for-each, os elementos **não** aparecem ordenados. A ordem correta só aparece **ao retirar** com `poll()`.
2. Os elementos precisam ser `Comparable` ou você passa um `Comparator`; senão: `ClassCastException` (mudança de versão do Java: em versões antigas ela só falhava na segunda inserção, nas atuais já na primeira).
3. `remove(objeto)` usa `equals` para achar o objeto, mas **perde o sentido** da fila (a ideia é retirar pela cabeça).

---

## 3. Prioridade por regra de negócio

```java
// quero retirar PRIMEIRO os mangás mais caros
Queue<Manga> fila = new PriorityQueue<>(new MangaPrecoComparator().reversed());
fila.addAll(mangas);

while (!fila.isEmpty()) {
    System.out.println(fila.poll());   // do mais caro para o mais barato
}
```

O comparator padrão por preço retira o **mais barato** primeiro (menor = maior prioridade). Para inverter, use `reversed()` (Java 8) ou `Collections.reverseOrder(comparator)`.

Exemplos reais de prioridade: fila de atendimento (idosos primeiro), carrinhos de compras (maior valor primeiro), tarefas por urgência, mensagens por importância.

---

## 4. Resumo

| Coleção | Quando |
|---|---|
| `List` | ordem + posição + duplicados |
| `Set` | sem duplicados |
| `Map` | busca por chave |
| `Queue` | processar em ordem de chegada |
| `PriorityQueue` | processar por prioridade |

## O que você precisa dominar (Aula 182)

- FIFO e os métodos `offer`, `poll`, `peek` (e suas versões que lançam exceção).
- `PriorityQueue` retira por prioridade, não por inserção.
- A impressão da fila não mostra a ordem de saída.
- Precisa de `Comparable`/`Comparator`.
- Inverter a prioridade.

---

# Mapa mental do bloco

```text
Collection framework
├── List  ─ ArrayList / LinkedList
├── Set   ─ sem duplicados, sem índice
│    ├── HashSet        (hash, sem ordem)
│    ├── LinkedHashSet  (hash + ordem de inserção)
│    └── TreeSet        (ordenado; duplicidade por compareTo; lower/floor/higher/ceiling; pollFirst/Last)
├── Map   ─ chave → valor (chave única)
│    ├── HashMap / LinkedHashMap / TreeMap
│    ├── put / putIfAbsent / get / getOrDefault / containsKey / remove
│    ├── keySet / values / entrySet
│    └── Map<K, List<V>> para agrupar
└── Queue ─ FIFO; offer/poll/peek
     └── PriorityQueue (por prioridade, não é "ordenada" por dentro)
```

# Cola de bolso

| Preciso... | Use |
|---|---|
| Eliminar duplicados | `new HashSet<>(lista)` (ou `LinkedHashSet` para manter ordem) |
| Conjunto sempre ordenado | `TreeSet` (+ `Comparable`/`Comparator`) |
| Buscar por chave rapidamente | `HashMap` |
| Map na ordem de inserção | `LinkedHashMap` |
| Map ordenado pela chave | `TreeMap` |
| Um-para-muitos | `Map<K, List<V>>` |
| Fila FIFO | `Queue<T> q = new LinkedList<>();` |
| Fila por prioridade | `PriorityQueue` + comparator |
