# Bloco 19 — Listas, Ordenação, Busca Binária, Conversões e `Iterator`

## Aulas 166 a 174

Chegou o momento mais esperado: as **Coleções**. Este bloco apresenta a coleção que você mais vai usar na vida de programador — a **`List`** — e tudo que gira em torno dela: criar, adicionar, buscar, remover, **ordenar**, converter para array e **remover com segurança durante a iteração**.

As aulas deste bloco são:

```text
166 — List pt 01
167 — List pt 02
168 — List pt 03
169 — Sorting lists pt 01
170 — Sorting lists pt 02 — Comparable
171 — Sorting lists pt 03 — Comparator
172 — Binary Search
173 — Conversão de Lista para Array e vice-versa
174 — Iterator
```

Pacote principal: `java.util`.

```text
Collection (interface)
   └── List (interface) ── coleção ORDENADA (mantém a ordem de inserção), aceita duplicados
         └── ArrayList  ── um array que cresce sozinho
```

---

# Aula 166 — List pt 01

## 1. O que é uma `List`?

`List` é uma **interface** do pacote `java.util`. Descreve uma **coleção ordenada** (sequência) em que cada elemento tem uma **posição (índice)**, começando em 0, e onde **elementos podem se repetir**.

A implementação mais famosa é o **`ArrayList`**: basicamente um **array que se redimensiona dinamicamente**.

```java
import java.util.ArrayList;
import java.util.List;

List<String> nomes = new ArrayList<>();
```

---

## 2. Um pouco de história: antes dos Generics

Até o Java 1.4 não existia o `<String>`. A lista guardava `Object`:

```java
List nomes = new ArrayList();      // lista "crua" (raw type)
nomes.add("William");
nomes.add(121);                    // aceita QUALQUER coisa!
nomes.add(true);

for (Object nome : nomes) {        // só dá para tratar como Object
    System.out.println(nome);
}
```

Problema: nada impede de misturar tipos, e para usar o dado era preciso fazer cast (e arriscar `ClassCastException`).

---

## 3. Generics (Java 5+)

Com **generics** você diz qual o tipo da lista, e o **compilador** força:

```java
List<String> nomes = new ArrayList<>();
nomes.add("William");
nomes.add("DevDojo");
nomes.add(121);        // ERRO DE COMPILAÇÃO — não é String
```

Detalhes:

- `<String>` é o **parâmetro de tipo**. Dentro de `List<E>`, o `E` é substituído por `String`: `add(String)`, `get(int)` devolve `String`.
- O operador **diamante** `<>` (Java 7+) evita repetir o tipo no lado direito: `new ArrayList<>()`.
- A verificação acontece **em tempo de compilação**; no bytecode o tipo é apagado (para manter compatibilidade com versões antigas).
- Os dois lados precisam ser compatíveis: `List<String> l = new ArrayList<Integer>();` não compila.

---

## 4. Programe para a interface

```java
List<String> nomes = new ArrayList<>();    // ✅ recomendado
ArrayList<String> nomes = new ArrayList<>(); // funciona, mas amarra seu código
```

O lado esquerdo é a **interface** (o contrato); o direito é a **implementação** (escolhida pelo desempenho). Assim, trocar de `ArrayList` para `LinkedList` depois é só mudar uma palavra.

---

## 5. Operações básicas

```java
List<String> nomes = new ArrayList<>();

nomes.add("William");        // adiciona no fim
nomes.add("DevDojo");
nomes.add("Suane");

System.out.println(nomes.size());    // 3
System.out.println(nomes.get(0));    // William
System.out.println(nomes);           // [William, DevDojo, Suane]
```

### Iterando

```java
// for tradicional (com índice)
for (int i = 0; i < nomes.size(); i++) {
    System.out.println(i + " -> " + nomes.get(i));
}

// for-each (mais limpo)
for (String nome : nomes) {
    System.out.println(nome);
}
```

Note: em array usamos `.length` e `[]`; em lista usamos `.size()` e `.get(i)`.

---

## 6. Capacidade inicial

```java
List<String> nomes = new ArrayList<>(300);
```

O `ArrayList` começa (por padrão) com capacidade 10 e, quando enche, cria um array **maior** (cerca de 1,5×) e copia os dados. Se você já sabe aproximadamente quantos elementos terá, informe a capacidade para evitar redimensionamentos. (O instrutor cita 16 e duplicação; o importante é a ideia: **a lista cresce automaticamente, mas crescer custa tempo**.)

## O que você precisa dominar (Aula 166)

- `List` é interface; `ArrayList` é a implementação mais comum.
- Generics: `List<String>`, diamante `<>`, checagem em compilação.
- Programar para a interface.
- `add`, `get`, `size`, iteração com `for` e `for-each`.
- Capacidade inicial.

---

# Aula 167 — List pt 02

## 1. `ConcurrentModificationException`

Se você **altera a lista dentro de um `for-each`** que a percorre:

```java
for (String nome : nomes) {
    nomes.add("novo");   // ❌ ConcurrentModificationException
}
```

O `for-each` usa um `Iterator` por baixo dos panos e percebe que a lista foi modificada por fora. A regra: **não adicione nem remova elementos de uma coleção enquanto a percorre com for-each**. (Na aula 174 você verá a forma correta de remover.)

### O for com índice e o "laço infinito"

```java
for (int i = 0; i < nomes.size(); i++) {
    nomes.add("x");   // size() cresce junto → o laço nunca termina!
}
```

Correção: guarde o tamanho antes (`int size = nomes.size();`). Mas, de modo geral, evite mexer no tamanho da lista durante o laço.

---

## 2. Só objetos (nada de primitivos)

```java
List<int> numeros = new ArrayList<>();      // ❌ não compila
List<Integer> numeros = new ArrayList<>();  // ✅ Wrapper
numeros.add(1);                             // autoboxing: int → Integer
```

Por quê? Coleções chamam métodos como `equals` e `hashCode`, e primitivos não têm métodos. Por isso usamos os **Wrappers** (você viu no Bloco 11).

---

## 3. Remover elementos: `remove`

O método `remove` é **sobrecarregado**:

```java
nomes.remove(0);            // remove pela POSIÇÃO (índice)
nomes.remove("William");    // remove pelo OBJETO (usa equals para achar)
```

Ambos funcionam, mas há uma **armadilha clássica** com `List<Integer>`:

```java
List<Integer> numeros = new ArrayList<>(List.of(10, 20, 30));
numeros.remove(1);                    // remove o ELEMENTO NA POSIÇÃO 1 (o 20)
numeros.remove(Integer.valueOf(10));  // remove o OBJETO 10
```

Com `int` literal, o Java escolhe a versão por índice. Para remover pelo valor, passe um `Integer`.

`remove(Object)` devolve `boolean` (`true` se removeu). Para achar o objeto ele usa **`equals`** — por isso o `equals` da sua classe é tão importante (Bloco 18).

---

## 4. Juntando listas: `addAll`

```java
List<String> nomes1 = new ArrayList<>();
nomes1.add("William");
nomes1.add("DevDojo");

List<String> nomes2 = new ArrayList<>();
nomes2.add("Suane");
nomes2.add("Midorima");

nomes1.addAll(nomes2);   // nomes1 passa a ter os 4 nomes
```

`addAll` aceita **qualquer coleção** (por enquanto leia como "qualquer lista"; os detalhes vêm com generics/wildcard). Uma linha resolve o problema clássico de entrevista: "como juntar duas listas?".

Outros métodos úteis:

| Método | Faz |
|---|---|
| `isEmpty()` | `true` se não há elementos |
| `clear()` | remove todos os elementos |
| `set(i, e)` | substitui o elemento da posição `i` |
| `add(i, e)` | insere na posição `i`, empurrando os demais |
| `subList(a, b)` | "fatia" da lista (a inclusivo, b exclusivo) |

## O que você precisa dominar (Aula 167)

- `ConcurrentModificationException` e sua causa.
- Coleções só guardam objetos (Wrappers e autoboxing).
- `remove(int)` x `remove(Object)` e a armadilha com `Integer`.
- `addAll`.
- `remove(Object)` usa `equals`.

---

# Aula 168 — List pt 03

## 1. Listas de objetos do mundo real

Agora com a classe `Smartphone` do bloco anterior (com `equals`/`hashCode` por `serialNumber`, e `toString`):

```java
Smartphone s1 = new Smartphone("1ABC2", "iPhone");
Smartphone s2 = new Smartphone("2ABC3", "Pixel");
Smartphone s3 = new Smartphone("3ABC4", "Samsung");

List<Smartphone> smartphones = new ArrayList<>(6);
smartphones.add(s1);
smartphones.add(s2);
smartphones.add(s3);

for (Smartphone smartphone : smartphones) {
    System.out.println(smartphone);
}
```

Sem `toString`, a impressão sairia como `Smartphone@1b6d3586`. Sobrescreva-o.

---

## 2. `clear()`

```java
smartphones.clear();   // esvazia a lista (a variável continua apontando para a MESMA lista)
```

---

## 3. `contains` — a lista tem esse objeto?

```java
Smartphone s4 = new Smartphone("2ABC3", "Pixel");   // outro objeto, mesmo serial do s2

smartphones.contains(s4);   // true!  (porque equals compara o serial)
```

Como o `contains` funciona: percorre a lista chamando `equals` em cada elemento até encontrar. Se você colocar um *breakpoint* no `equals`, verá que ele é chamado **uma vez por elemento percorrido**, o que explica o custo `O(n)` do Bloco 18.

> Conclusão do instrutor: **todas as classes que vão para coleções devem ter `equals` e `hashCode` bem implementados.**

---

## 4. `indexOf`

```java
int posicao = smartphones.indexOf(s4);   // 1 (posição do s2)
```

- Devolve o **primeiro índice** do elemento.
- Devolve **`-1`** se não encontrar.
- Também usa `equals`.
- `lastIndexOf` devolve a última ocorrência.

Para saber só se existe, use `contains`; para usar a posição, `indexOf` e valide `!= -1`.

---

## 5. Inserir em posição específica

```java
smartphones.add(0, s4);    // vira o primeiro; os outros são empurrados para a direita
```

Isso prova que a lista **mantém e controla a posição**.

---

## 6. `get` fora do limite

```java
smartphones.get(10);   // IndexOutOfBoundsException
```

Sempre confira `index >= 0 && index < list.size()` quando o índice vier de fora.

## O que você precisa dominar (Aula 168)

- Listas de objetos e a importância de `toString`.
- `contains` e `indexOf` usam `equals`.
- `clear`, `add(index, e)`.
- `IndexOutOfBoundsException`.

---

# Aula 169 — Sorting lists pt 01

## 1. Ordenando Strings e números

Duas formas equivalentes:

```java
List<String> mangas = new ArrayList<>();
mangas.add("Attack on Titan");
mangas.add("Berserk");
mangas.add("Dragon Ball Z");
mangas.add("Naruto");

Collections.sort(mangas);     // classe utilitária Collections (com S)
mangas.sort(null);            // método da List; null = ordem natural
```

- `Collections` (plural) é uma classe com métodos estáticos úteis.
- Strings são ordenadas em **ordem alfabética** (maiúsculas vêm antes de minúsculas na ordem natural).
- Números, do **menor para o maior**.

```java
List<Double> dinheiros = new ArrayList<>();
dinheiros.add(21.9);
dinheiros.add(3.98);
Collections.sort(dinheiros);   // [3.98, 21.9]
```

O algoritmo de ordenação (TimSort) tem custo `O(n log n)`.

---

## 2. Ordenando objetos próprios: o erro

```java
List<Smartphone> smartphones = ...;
Collections.sort(smartphones);   // ❌ ERRO DE COMPILAÇÃO
```

O Java pergunta: "ordenar por **qual** critério?". Ele não sabe se é pelo serial, pela marca...

Antes de resolver (próxima aula), crie a classe de apoio `Manga`:

```java
public class Manga {
    private Long id;
    private String nome;
    private double preco;

    public Manga(Long id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        // opcional: validar nulos
    }
    // getters, equals, hashCode, toString
}
```

### Gerando `equals` e `hashCode` na IDE

No IntelliJ: **Alt+Insert → equals() and hashCode()** e escolher os atributos. Ele pergunta quais atributos usar e quais podem ser nulos (os "non-null" geram código mais simples). Pode manter os três (`id`, `nome`, `preco`) ou só o `id`, conforme a regra de negócio.

### Validando nulos no construtor

```java
this.id = Objects.requireNonNull(id, "O id não pode ser nulo");
this.nome = Objects.requireNonNull(nome, "O nome não pode ser nulo");
```

Se alguém passar `null`, o `NullPointerException` já vem com uma mensagem clara.

## O que você precisa dominar (Aula 169)

- `Collections.sort(lista)` e `lista.sort(null)`.
- Ordem natural de String e números.
- Por que ordenar objetos próprios não compila.
- `Objects.requireNonNull`.

---

# Aula 170 — Sorting lists pt 02 — `Comparable`

## 1. A solução: implementar `Comparable<T>`

```java
public class Manga implements Comparable<Manga> {
    ...
    @Override
    public int compareTo(Manga outro) {
        // regra do retorno:
        //   negativo → este vem ANTES do outro
        //   zero     → são iguais na ordenação
        //   positivo → este vem DEPOIS do outro
    }
}
```

`Comparable` é a interface do pacote `java.lang` que ensina o objeto a se comparar com outro. `String`, `Integer`, `Double`, `LocalDate` etc. já a implementam — por isso `Collections.sort` funciona com eles.

---

## 2. Implementação manual (por `id`)

```java
@Override
public int compareTo(Manga outro) {
    if (this.id < outro.getId()) return -1;
    if (this.id.equals(outro.getId())) return 0;
    return 1;
}
```

## 3. Delegando para o Wrapper (recomendado)

Como `Long` já é `Comparable`, delegue:

```java
@Override
public int compareTo(Manga outro) {
    return this.id.compareTo(outro.getId());
}
```

Uma linha, sem erro.

### Para atributos primitivos

`double` não tem método. Use o `compare` estático do Wrapper:

```java
return Double.compare(this.preco, outro.getPreco());   // ordena por preço
```

### Para Strings

```java
return this.nome.compareTo(outro.getNome());           // ordem alfabética
```

Escolha **um** atributo por `compareTo`.

---

## 4. Usando

```java
List<Manga> mangas = new ArrayList<>();
mangas.add(new Manga(5L, "Berserk", 19.9));
mangas.add(new Manga(2L, "Attack on Titan", 11.2));
mangas.add(new Manga(4L, "Dragon Ball Z", 3.2));

Collections.sort(mangas);   // agora compila e usa o compareTo
```

Quem chama o `compareTo` é o **próprio Java**, dentro do algoritmo de ordenação (você pode ver com um *breakpoint*). Se a classe não implementa `Comparable`, `Collections.sort` nem compila.

## 5. Consistência com `equals`

Boa prática: `compareTo` devolver `0` exatamente quando `equals` devolver `true`. Isso evita comportamentos estranhos em `TreeSet`/`TreeMap` (aulas futuras).

## 6. Limitação

Um objeto só tem **um** `compareTo` ("ordem natural"). E se eu quiser ordenar de formas diferentes? → `Comparator` (próxima aula).

## O que você precisa dominar (Aula 170)

- `Comparable<T>` e o método `compareTo`.
- Regra de retorno (negativo, zero, positivo).
- Delegar para `Long.compareTo`, `Double.compare`, `String.compareTo`.
- Quem chama o `compareTo` é o framework.

---

# Aula 171 — Sorting lists pt 03 — `Comparator`

## 1. O problema do `Comparable`

Seu `Manga` está ordenado por nome (ordem natural). Um dia o negócio pede "ordenar por id" em uma tela específica. Se você trocar o `compareTo`, **todo o sistema** que contava com a ordem por nome quebra.

A solução: ordenações **externas**, sem mexer na classe — o **`Comparator<T>`**.

| | `Comparable` | `Comparator` |
|---|---|---|
| Pacote | `java.lang` | `java.util` |
| Onde fica | **dentro** da classe do objeto | **fora**, em outra classe/objeto |
| Método | `compareTo(T outro)` | `compare(T a, T b)` |
| Quantidade de critérios | um (ordem natural) | quantos quiser |

---

## 2. Criando um `Comparator`

```java
import java.util.Comparator;

class MangaByIdComparator implements Comparator<Manga> {
    @Override
    public int compare(Manga m1, Manga m2) {
        return m1.getId().compareTo(m2.getId());
    }
}
```

Mesma regra de retorno (negativo / zero / positivo), mas agora você recebe **os dois objetos** como parâmetro.

---

## 3. Usando

```java
Collections.sort(mangas);                              // ordem natural (compareTo → nome)
Collections.sort(mangas, new MangaByIdComparator());   // ordem por id (Comparator)
mangas.sort(new MangaByIdComparator());                // equivalente, via List
```

Observação do instrutor: `mangas.sort(...)` **exige** um `Comparator` (ou `null`); `Collections.sort` tem duas versões (com e sem `Comparator`).

Vários critérios, sem tocar na classe `Manga`:

```java
class MangaByPrecoComparator implements Comparator<Manga> {
    public int compare(Manga a, Manga b) {
        return Double.compare(a.getPreco(), b.getPreco());
    }
}
```

## 4. Spoiler do que vem pela frente

Escrever uma classe só para um critério é verboso. Com **classes anônimas** e **lambdas** (blocos futuros) a mesma coisa cai para uma linha:

```java
mangas.sort(Comparator.comparing(Manga::getPreco));
```

## O que você precisa dominar (Aula 171)

- `Comparator<T>` e o método `compare(a, b)`.
- Diferença `Comparable` x `Comparator`.
- `Collections.sort(lista, comparator)` e `lista.sort(comparator)`.
- Vantagem: múltiplos critérios sem alterar a classe.

---

# Aula 172 — Binary Search

## 1. O que é

`Collections.binarySearch(lista, chave)` faz **busca binária**: em vez de olhar um a um (`O(n)`), compara com o elemento do **meio** e descarta metade da lista a cada passo (`O(log n)`).

```text
[1, 3, 5, 7, 9, 11, 13]   procurando 11
meio = 7  → 11 > 7  → descarta a metade esquerda
[9, 11, 13] meio = 11 → achou!
```

## 2. ⚠️ Pré-requisito: a lista precisa estar ORDENADA

```java
List<Integer> numeros = new ArrayList<>();
numeros.add(2);
numeros.add(0);
numeros.add(4);
numeros.add(3);

Collections.sort(numeros);                           // [0, 2, 3, 4]
int pos = Collections.binarySearch(numeros, 2);      // 1
```

Se a lista **não** estiver ordenada (na mesma ordem usada na busca), o resultado é **imprevisível**.

---

## 3. O retorno

| Situação | Retorno |
|---|---|
| Encontrou | o **índice** (≥ 0) |
| Não encontrou | `-(pontoDeInserção) - 1` (sempre **negativo**) |

**Ponto de inserção** = posição em que o elemento deveria ser inserido para manter a lista ordenada.

```java
// numeros = [0, 2, 3, 4]
Collections.binarySearch(numeros, 1);    // -2   → ponto de inserção = 1  → -(1) - 1 = -2
Collections.binarySearch(numeros, -1);   // -1   → ponto de inserção = 0  → -(0) - 1 = -1
Collections.binarySearch(numeros, 10);   // -5   → ponto de inserção = 4  → -(4) - 1 = -5
```

### Por que o "-1"?

Se não tivesse o `-1`, o ponto de inserção `0` (negativo de zero é zero) se confundiria com "encontrei na posição 0". O `-1` garante que **negativo = não encontrou**. Para recuperar o ponto de inserção:

```java
int pontoDeInsercao = -resultado - 1;
```

---

## 4. Com objetos e `Comparator`

Se a lista de objetos foi ordenada com um `Comparator` específico, a busca **também** precisa do mesmo:

```java
MangaByIdComparator comparator = new MangaByIdComparator();

Collections.sort(mangas, comparator);
Manga procurado = new Manga(2L, "qualquer", 0);

int pos = Collections.binarySearch(mangas, procurado, comparator);
```

Observação importante: a busca binária **não usa `equals`** — usa a comparação (`compareTo`/`compare`). Por isso, se ordenar por `id`, o objeto procurado só precisa ter o `id` certo.

---

## 5. Arrays

O mesmo existe para arrays em `java.util.Arrays`:

```java
int[] vetor = {0, 2, 3, 4};
Arrays.binarySearch(vetor, 3);   // 2
```

(Também precisa estar ordenado: `Arrays.sort(vetor)`.)

## O que você precisa dominar (Aula 172)

- Busca binária: `O(log n)`, exige lista ordenada.
- Interpretar o retorno negativo.
- Usar o mesmo `Comparator` na ordenação e na busca.
- `Arrays.binarySearch`.

---

# Aula 173 — Conversão de Lista para Array e vice-versa

## 1. Lista → Array: `toArray`

```java
List<Integer> numeros = new ArrayList<>();
numeros.add(1);
numeros.add(2);
numeros.add(3);

Object[] arrayDeObjects = numeros.toArray();           // sem tipo (pouco útil)
Integer[] array = numeros.toArray(new Integer[0]);     // tipado
```

Sobre o argumento `new Integer[0]`: ele só indica o **tipo** do array desejado. Passar tamanho `0` (ou `numeros.size()`) é questão de estilo/desempenho; o Java cria um array do tamanho certo quando o fornecido é pequeno.

Note: o resultado é `Integer[]` (array de Wrappers), **não** `int[]`.

---

## 2. Array → Lista: `Arrays.asList`

```java
Integer[] numerosArray = new Integer[3];
numerosArray[0] = 1;
numerosArray[1] = 2;
numerosArray[2] = 3;

List<Integer> lista = Arrays.asList(numerosArray);
```

### ⚠️ Cuidado 1 — é uma "janela" para o array original

```java
lista.set(0, 12);
System.out.println(numerosArray[0]);   // 12  ← o array mudou também!
numerosArray[1] = 99;
System.out.println(lista.get(1));      // 99
```

A lista e o array **compartilham os mesmos dados**.

### ⚠️ Cuidado 2 — tamanho fixo

```java
lista.add(19);      // ❌ UnsupportedOperationException
lista.remove(0);    // ❌ UnsupportedOperationException
```

Pode trocar valores (`set`), mas não aumentar nem diminuir.

### Como criar uma lista independente e mutável

```java
List<Integer> mutavel = new ArrayList<>(Arrays.asList(numerosArray));
mutavel.add(15);    // ✅ funciona e não afeta o array original
```

O construtor do `ArrayList` aceita outra coleção e **copia** os elementos.

---

## 3. Criando listas em uma linha

```java
List<Integer> a = Arrays.asList(1, 2, 3, 4, 5);          // aceita varargs
List<String>  b = Arrays.asList("A", "B", "C");

List<Integer> c = List.of(1, 2, 3, 4, 5);                // Java 9+
```

| | `Arrays.asList` | `List.of` (Java 9+) |
|---|---|---|
| Alterar elemento (`set`) | ✅ | ❌ |
| Adicionar/remover | ❌ | ❌ |
| Aceita `null` | ✅ | ❌ (`NullPointerException`) |
| Ligada a um array | ✅ | não |

`List.of` cria uma lista **imutável** (totalmente somente-leitura). O instrutor avisa que, a partir desta aula, o projeto passa a usar o *language level* 11.

## O que você precisa dominar (Aula 173)

- `toArray(new T[0])`.
- `Arrays.asList`: compartilha dados com o array e tem tamanho fixo.
- `new ArrayList<>(outraColecao)` para criar uma cópia mutável.
- `List.of` (imutável, Java 9+).

---

# Aula 174 — Iterator

## 1. O problema

Remover elementos de uma lista **enquanto** a percorre:

```java
List<Manga> mangas = ...;

for (Manga manga : mangas) {
    if (manga.getQuantidade() == 0) {
        mangas.remove(manga);       // ❌ ConcurrentModificationException
    }
}
```

O `for-each` não aceita que a lista seja alterada "por fora". Em vez de for-each, use o **`Iterator`**.

---

## 2. A analogia da fila do banco

O `Iterator` é como o gerente de uma fila: antes de chamar alguém, **olha se tem alguém na fila**; depois **chama a próxima pessoa**; e se precisa tirar alguém da fila, ele mesmo faz isso com segurança.

```java
Iterator<Manga> it = mangas.iterator();

while (it.hasNext()) {              // tem próximo?
    Manga manga = it.next();        // pega o próximo
    if (manga.getQuantidade() == 0) {
        it.remove();                // remove o ÚLTIMO elemento retornado por next()
    }
}
```

| Método | Faz |
|---|---|
| `hasNext()` | `true` se ainda há elementos |
| `next()` | devolve o próximo elemento e avança |
| `remove()` | remove o elemento devolvido por `next()` (opcional) |

Regras:

- Chame `next()` **antes** de `remove()`; chamar `remove()` duas vezes seguidas sem `next()` → `IllegalStateException`.
- Se `next()` for chamado sem `hasNext()` e não há mais elementos → `NoSuchElementException`.
- O `Iterator` é de **uso único**: para percorrer novamente, peça outro com `iterator()`.

---

## 3. A forma moderna: `removeIf` (Java 8)

```java
mangas.removeIf(manga -> manga.getQuantidade() == 0);
```

Uma única linha, usa um *Predicate* (programação funcional — assunto dos próximos blocos). Por trás dos panos usa o mesmo `Iterator`. Leitura: "remova da lista todo `manga` tal que `quantidade == 0`".

Mudança de estilo:

```text
Imperativo (o que vimos até agora): você descreve COMO fazer, passo a passo.
Funcional:                           você descreve O QUE quer.
```

## 4. Um construtor extra

Na aula, o `Manga` ganhou um atributo `quantidade` e um construtor sobrecarregado que reaproveita o anterior, com `this(...)`, para não quebrar o código antigo:

```java
public Manga(Long id, String nome, double preco) {
    this(id, nome, preco, 0);
}
```

## O que você precisa dominar (Aula 174)

- Por que o `for-each` + `remove` lança `ConcurrentModificationException`.
- `Iterator`: `hasNext`, `next`, `remove`.
- `removeIf` como alternativa moderna.
- Remoção segura durante a iteração.

---

# Mapa mental do bloco

```text
List<E>  (ordenada, aceita duplicados)  →  ArrayList
├── Generics:  List<String>, diamante <>
├── Básico:    add, get, set, size, isEmpty, clear, addAll
├── Remover:   remove(int) x remove(Object)  ·  Iterator.remove  ·  removeIf
├── Buscar:    contains, indexOf  (usam equals)
├── Ordenar
│    ├── Collections.sort(lista) / lista.sort(null)  → Comparable.compareTo
│    └── Collections.sort(lista, comparator)         → Comparator.compare
├── Busca binária:  Collections.binarySearch (lista ordenada!)  ·  retorno negativo = -(inserção) - 1
└── Conversão
     ├── lista → array:  toArray(new T[0])
     └── array → lista:  Arrays.asList (tamanho fixo, ligada ao array)  ·  new ArrayList<>(...)  ·  List.of
```

# Cola de bolso

| Preciso... | Use |
|---|---|
| Lista que cresce | `List<T> l = new ArrayList<>();` |
| Saber se existe objeto | `l.contains(obj)` (requer `equals`) |
| Ordenar por ordem natural | `Collections.sort(l)` (requer `Comparable`) |
| Ordenar por outro critério | `l.sort(comparator)` |
| Busca rápida em lista ordenada | `Collections.binarySearch(l, chave[, comparator])` |
| Remover itens durante laço | `Iterator.remove()` ou `removeIf` |
| Criar lista pequena | `List.of(...)` (imutável) / `new ArrayList<>(Arrays.asList(...))` |
| Lista → array | `l.toArray(new T[0])` |
