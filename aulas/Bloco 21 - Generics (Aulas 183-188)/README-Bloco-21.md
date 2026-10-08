# Bloco 21 — Generics

## Aulas 183 a 188

Você já usou generics em todo o bloco de coleções (`List<String>`, `Map<String, Integer>`). Agora vai entender **o que está por trás** e aprender a **criar suas próprias classes e métodos genéricos**.

As aulas deste bloco são:

```text
183 — Generics pt 01 — Introdução
184 — Generics pt 02 — Wildcard pt 01
185 — Generics pt 03 — Wildcard pt 02
186 — Generics pt 04 — Classes genéricas pt 01
187 — Generics pt 05 — Classes genéricas pt 02
188 — Generics pt 06 — Métodos genéricos
```

Ideia central do bloco:

```text
Sem generics:  um código para Carro, outro igual para Barco, outro para Moto...
Com generics:  UM código que funciona para qualquer tipo, com checagem do compilador
```

---

# Aula 183 — Generics pt 01 — Introdução

## 1. Antes dos generics (até o Java 1.4)

```java
List lista = new ArrayList();        // lista "crua" (raw type)
lista.add("Texto");
lista.add(123);
lista.add(new Consumidor("Ana"));    // aceita QUALQUER coisa
```

Problemas:

1. Ao ler, o tipo é `Object`. Para usar, precisa de **cast** (e de `instanceof` para não errar):

```java
for (Object o : lista) {
    if (o instanceof String) {
        String s = (String) o;
        // ...
    }
}
```

2. Nada impede de misturar tipos; o erro só aparece **em tempo de execução** (`ClassCastException`).
3. Em sistemas com dezenas de classes, a quantidade de `instanceof` explode.

---

## 2. A solução: generics (Java 5)

```java
List<String> lista = new ArrayList<>();
lista.add("Texto");
lista.add(123);      // ❌ ERRO DE COMPILAÇÃO
```

Agora o **compilador** garante os tipos e você não precisa de cast:

```java
for (String s : lista) {          // sem cast, sem instanceof
    System.out.println(s.toUpperCase());
}
```

Antigamente era preciso repetir o tipo dos dois lados (`new ArrayList<String>()`); hoje o **diamante** `<>` (Java 7) infere.

---

## 3. Type Erasure (apagamento de tipo)

Para manter compatibilidade com código antigo e com a JVM existente, os generics funcionam **apenas em tempo de compilação**:

> Depois de compilado, o tipo genérico é **apagado** (*type erasure*). No bytecode, `List<String>` vira simplesmente `List` (de `Object`).

A aula demonstra o perigo disso: um método que recebe `List` "crua" aceita qualquer lista, e dá para sabotar a lista:

```java
static void adicionarConsumidor(List lista) {          // raw type
    lista.add(new Consumidor("Intruso"));
}

List<String> nomes = new ArrayList<>();
adicionarConsumidor(nomes);   // compila (com aviso!) — a lista de Strings recebeu um Consumidor

for (String nome : nomes) {   // ❌ ClassCastException aqui, bem longe de onde o erro foi feito
    System.out.println(nome);
}
```

Lições:

- Nunca use *raw types* em código novo (ignore os avisos de "unchecked" só se souber o que faz).
- A JVM não "sabe" o tipo da lista; quem sabe é o compilador. Por isso os generics são tão úteis: detectam o erro **antes** de rodar.

## O que você precisa dominar (Aula 183)

- O problema das coleções sem tipo (cast, `instanceof`, `ClassCastException`).
- Generics = verificação em tempo de compilação.
- *Type erasure*.
- Evitar *raw types*.

---

# Aula 184 — Generics pt 02 — Wildcard (parte 1): o problema

## 1. Pré-requisito: polimorfismo

```java
public abstract class Animal {
    public abstract void consulta();
}

public class Cachorro extends Animal {
    @Override public void consulta() { System.out.println("Consultando cachorro"); }
}

public class Gato extends Animal {
    @Override public void consulta() { System.out.println("Consultando gato"); }
}
```

Um método que atende qualquer animal:

```java
static void printConsulta(Animal[] animais) {
    for (Animal animal : animais) {
        animal.consulta();
    }
}
```

---

## 2. Com **arrays** funciona

```java
Cachorro[] cachorros = { new Cachorro(), new Cachorro() };
Gato[] gatos = { new Gato(), new Gato() };

printConsulta(cachorros);   // ✅
printConsulta(gatos);       // ✅
```

Um `Cachorro[]` **é um** `Animal[]` (arrays são **covariantes**). Isso funciona porque arrays *sabem o próprio tipo em tempo de execução*.

A "pegadinha" é que dá para sabotar:

```java
static void printConsulta(Animal[] animais) {
    animais[0] = new Gato();      // compila!
}
printConsulta(cachorros);          // ❌ ArrayStoreException em tempo de execução
```

O array de cachorros sabe que só aceita `Cachorro` e protege a si mesmo, **só que em tempo de execução**.

---

## 3. Com **listas** NÃO funciona

```java
static void printConsulta(List<Animal> animais) { ... }

List<Cachorro> cachorros = List.of(new Cachorro(), new Cachorro());
printConsulta(cachorros);      // ❌ ERRO DE COMPILAÇÃO
```

Por quê? Porque, para generics, `List<Cachorro>` **NÃO é** subtipo de `List<Animal>`. Generics são **invariantes**.

Se fosse permitido, haveria um buraco:

```java
List<Cachorro> cachorros = new ArrayList<>();
List<Animal> animais = cachorros;     // imagine que fosse permitido…
animais.add(new Gato());              // …e agora há um gato numa lista de cachorros!
Cachorro c = cachorros.get(0);        // 💥 ClassCastException
```

Como os tipos genéricos são apagados em execução (type erasure), a JVM não conseguiria impedir isso. Então o **compilador proíbe** logo na origem.

Observação: `Cachorro` **é** `Animal`, mas `List<Cachorro>` **não é** `List<Animal>`.

```text
Cachorro  ──é um──▶  Animal          ✅
List<Cachorro>  ──é um──▶  List<Animal>   ❌ (invariância)
```

Porém, **dentro** de uma `List<Animal>` você pode guardar um `Cachorro` (polimorfismo normal):

```java
List<Animal> animais = new ArrayList<>();
animais.add(new Cachorro());    // ✅
animais.add(new Gato());        // ✅
```

A solução para aceitar "listas de qualquer subtipo de Animal" é o **wildcard** (aula seguinte).

## O que você precisa dominar (Aula 184)

- Arrays são covariantes; listas genéricas são invariantes.
- `ArrayStoreException` x erro de compilação.
- Por que `List<Cachorro>` não é `List<Animal>`.
- O motivo: segurança de tipos com type erasure.

---

# Aula 185 — Generics pt 03 — Wildcard (parte 2)

## 1. O curinga `?`

O caractere `?` (*wildcard*) significa "um tipo desconhecido". Ele vem em dois sabores principais:

### `? extends Tipo` — "Tipo ou qualquer **filho** (limite superior)"

```java
static void printConsulta(List<? extends Animal> animais) {
    for (Animal animal : animais) {      // ✅ ler como Animal é seguro
        animal.consulta();
    }
}

printConsulta(listaDeCachorros);   // ✅
printConsulta(listaDeGatos);       // ✅
printConsulta(listaDeAnimais);     // ✅
```

**Regra do contrato**: com `extends` você pode **ler**, mas **não pode adicionar** (exceto `null`):

```java
static void printConsulta(List<? extends Animal> animais) {
    animais.add(new Cachorro());   // ❌ não compila
    animais.add(new Animal() {...}); // ❌
}
```

Por quê? A lista recebida pode ser de `Gato`; adicionar um `Cachorro` quebraria a lista. O compilador não sabe qual é o tipo exato, então proíbe qualquer inserção. **A lista vira "somente leitura"**.

Detalhes que o instrutor destaca:

- A palavra é **sempre `extends`**, mesmo que o limite seja uma **interface** (`? extends Comparable<T>`). Nunca `implements`.
- O tipo `Animal` incluído: `? extends Animal` aceita `List<Animal>` também.

---

### `? super Tipo` — "Tipo ou qualquer **pai** (limite inferior)"

```java
static void printConsulta(List<? super Cachorro> lista) {
    lista.add(new Cachorro());   // ✅ pode adicionar
}

printConsulta(new ArrayList<Cachorro>());   // ✅
printConsulta(new ArrayList<Animal>());     // ✅ (Animal é pai de Cachorro)
printConsulta(new ArrayList<Object>());     // ✅ (Object é pai de todos)
printConsulta(new ArrayList<Gato>());       // ❌ (Gato não é pai de Cachorro)
```

Com `super`:

- ✅ Você **pode adicionar** `Cachorro` (e filhos de `Cachorro`): qualquer que seja a lista, ela aceita `Cachorro`, porque é uma lista de `Cachorro`, de `Animal` ou de `Object`.
- ❌ Você **não pode ler** com um tipo útil: só consegue ler como `Object`.

```java
Object o = lista.get(0);       // ✅ só como Object
Cachorro c = lista.get(0);     // ❌ não compila
```

---

## 2. A regra de ouro: PECS

> **P**roducer **E**xtends, **C**onsumer **S**uper

| A coleção... | Use | Operação |
|---|---|---|
| **produz** dados para você (você só lê) | `? extends T` | ler |
| **consome** dados seus (você só escreve) | `? super T` | escrever |
| os dois | tipo exato `T` (sem wildcard) | ler e escrever |

Exemplo clássico (`Collections.copy`):

```java
public static <T> void copy(List<? super T> destino, List<? extends T> origem)
```

A origem **produz** (extends), o destino **consome** (super).

## 3. `?` sem limite

```java
static void imprimir(List<?> lista) { ... }   // qualquer lista; só leitura como Object
```

## 4. Resumo visual

```text
                  Object
                    ▲
                    │           ? super Cachorro  → sobe até Object
                  Animal
                  ▲    ▲
             Cachorro  Gato
                    │           ? extends Animal  → desce até os filhos
```

## O que você precisa dominar (Aula 185)

- `? extends T`: aceita T e filhos; **somente leitura**.
- `? super T`: aceita T e pais; **escrita**, leitura só como `Object`.
- Sempre `extends` (mesmo para interface).
- PECS.
- `List<?>`.

---

# Aula 186 — Generics pt 04 — Classes genéricas (parte 1): o problema da repetição

## 1. Cenário: serviço de aluguel

Duas classes de domínio:

```java
public class Carro {
    private String nome;
    public Carro(String nome) { this.nome = nome; }
    public String getNome() { return nome; }
}

public class Barco {
    private String nome;
    public Barco(String nome) { this.nome = nome; }
    public String getNome() { return nome; }
}
```

Um serviço para alugar carros:

```java
public class CarroRentalService {
    private List<Carro> carrosDisponiveis = new ArrayList<>(List.of(new Carro("BMW"), new Carro("Fusca")));

    public Carro buscarCarroDisponivel() {
        System.out.println("Buscando carro disponível...");
        Carro carro = carrosDisponiveis.remove(0);          // pega o primeiro
        System.out.println("Alugando carro: " + carro);
        System.out.println("Carros disponíveis: " + carrosDisponiveis);
        return carro;
    }

    public void retornarCarroAlugado(Carro carro) {
        System.out.println("Devolvendo carro: " + carro);
        carrosDisponiveis.add(carro);
        System.out.println("Carros disponíveis: " + carrosDisponiveis);
    }
}
```

(Uso: pega o carro, "usa por um mês", devolve.)

---

## 2. O problema: copiar e colar

Chega o requisito: "agora também alugamos **barcos**". A tentação: copiar a classe inteira e trocar `Carro` por `Barco`:

```java
public class BarcoRentalService {
    private List<Barco> barcosDisponiveis = ...;
    public Barco buscarBarcoDisponivel() { ... }
    public void retornarBarcoAlugado(Barco barco) { ... }
}
```

As duas classes são **idênticas linha a linha**, mudando apenas o tipo do objeto. E se vierem `Moto`, `Bicicleta`, `Casa`…? Você teria N classes duplicadas — o oposto de código limpo (princípio DRY: *Don't Repeat Yourself*).

```text
CarroRentalService   ←  só muda o tipo →   BarcoRentalService
```

A solução: **deixar o tipo como parâmetro**. É o que a próxima aula faz.

## O que você precisa dominar (Aula 186)

- Reconhecer duplicação causada apenas por diferença de tipo.
- Que isso é exatamente o problema que generics resolve.

---

# Aula 187 — Generics pt 05 — Classes genéricas (parte 2)

## 1. Criando a classe genérica

Troque o tipo concreto por um **parâmetro de tipo** `T`, declarado logo após o nome da classe:

```java
public class RentalService<T> {

    private List<T> objetosDisponiveis;

    public RentalService(List<T> objetosDisponiveis) {
        this.objetosDisponiveis = new ArrayList<>(objetosDisponiveis);
    }

    public T buscarObjetoDisponivel() {
        System.out.println("Buscando objeto disponível...");
        T objeto = objetosDisponiveis.remove(0);
        System.out.println("Alugando: " + objeto);
        System.out.println("Disponíveis: " + objetosDisponiveis);
        return objeto;
    }

    public void retornarObjetoAlugado(T objeto) {
        System.out.println("Devolvendo: " + objeto);
        objetosDisponiveis.add(objeto);
        System.out.println("Disponíveis: " + objetosDisponiveis);
    }
}
```

O `<T>` é uma **variável de tipo**: "ainda não sei qual será, mas será definido por quem usar a classe".

Por ser lista, o serviço recebe os dados pelo construtor (na vida real, viriam de um banco).

> Detalhe: não se pode fazer `new T()` nem `new T[10]` — por causa do *type erasure*, o tipo não existe em execução.

---

## 2. Usando

```java
List<Carro> carros = List.of(new Carro("BMW"), new Carro("Fusca"));
RentalService<Carro> carroService = new RentalService<>(carros);

Carro carro = carroService.buscarObjetoDisponivel();   // devolve Carro (sem cast!)
// usa por um mês
carroService.retornarObjetoAlugado(carro);

List<Barco> barcos = List.of(new Barco("Lancha"), new Barco("Canoa"));
RentalService<Barco> barcoService = new RentalService<>(barcos);

Barco barco = barcoService.buscarObjetoDisponivel();
barcoService.retornarObjetoAlugado(barco);
```

Ao escrever `RentalService<Carro>`, o compilador **substitui `T` por `Carro`** em toda a classe. O construtor passa a exigir `List<Carro>`, o método devolve `Carro`, etc. Se tentar devolver um `Barco` ao serviço de carros → erro de compilação.

**Uma classe, infinitos tipos.**

---

## 3. Convenção de nomes dos parâmetros de tipo

| Letra | Significado |
|---|---|
| `T` | Type (tipo genérico qualquer) |
| `E` | Element (elemento de coleção) |
| `K` | Key (chave) |
| `V` | Value (valor) |
| `N` | Number |
| `R` | Result (retorno) |

Podem-se usar vários: `class Par<K, V>`.

## 4. Vários parâmetros

```java
public class Par<A, B> {
    private A primeiro;
    private B segundo;

    public Par(A primeiro, B segundo) { this.primeiro = primeiro; this.segundo = segundo; }
    public A getPrimeiro() { return primeiro; }
    public B getSegundo() { return segundo; }
}

Par<String, Integer> p = new Par<>("idade", 30);
```

## 5. Limitando o tipo: `bounded types`

Se `T` precisa ter certas capacidades, restrinja:

```java
public class Caixa<T extends Comparable<T>> {
    public T maior(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;    // pode usar compareTo porque T é Comparable
    }
}
```

Para múltiplos limites: `<T extends Animal & Comparable<T>>` (classe primeiro, depois interfaces).

## 6. Quando usar?

O instrutor faz um alerta honesto:

> No dia a dia de uma aplicação simples você **usa** muito generics (coleções), mas **cria** poucos. A necessidade de criar classes genéricas aparece quando o sistema cresce, quando você descobre que está repetindo o mesmo código para tipos diferentes — e começa a generalizar. Mas cuidado: código muito genérico pode ficar **difícil de ler**.

Frameworks e bibliotecas (como o próprio Spring, que o instrutor cita) usam generics intensamente.

## O que você precisa dominar (Aula 187)

- Declarar `class Nome<T>`.
- Usar `T` em atributos, parâmetros e retornos.
- Instanciar com `new Nome<Tipo>(...)`.
- Parâmetros múltiplos e convenção de letras.
- `T extends Limite`.

---

# Aula 188 — Generics pt 06 — Métodos genéricos

## 1. Quando o tipo é só do método

Às vezes você não quer (ou não pode) parametrizar a classe inteira, só um método. Aí declara o parâmetro de tipo **no método**, entre o modificador e o tipo de retorno:

```java
public static <T> List<T> criarArrayComUmObjeto(T objeto) {
    List<T> lista = new ArrayList<>();
    lista.add(objeto);
    return lista;
}
```

Anatomia:

```text
public static  <T>  List<T>  criarArrayComUmObjeto ( T objeto )
   │            │      │                              └ usa T
   │            │      └ tipo de retorno (usa T)
   │            └ declaração do tipo (ANTES do retorno)
   └ modificadores
```

---

## 2. Usando

```java
List<Barco> barcos = criarArrayComUmObjeto(new Barco("Lancha"));
List<String> textos = criarArrayComUmObjeto("Olá");
List<Integer> numeros = criarArrayComUmObjeto(42);
```

Aqui não escrevemos `<Barco>` na chamada: o compilador faz **inferência de tipo** a partir do argumento. Se quiser explicitar: `Classe.<Barco>criarArrayComUmObjeto(...)`.

---

## 3. Métodos genéricos com limite

```java
public static <T extends Comparable<T>> T maior(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

maior(10, 20);            // 20
maior("abc", "xyz");      // "xyz"
maior(new Barco("x"), new Barco("y"));   // ❌ Barco não é Comparable
```

O `extends` aqui (e a presença de `Comparable`) é o mesmo mecanismo visto em `Collections.sort`:

```java
public static <T extends Comparable<? super T>> void sort(List<T> list)
```

Leitura: "`T` é qualquer tipo que seja comparável com ele mesmo (ou com um pai dele)". Parece alienígena à primeira vista, mas resume tudo que você aprendeu no bloco: **método genérico + limite + wildcard**.

## 4. Classe genérica x método genérico

| | Classe genérica | Método genérico |
|---|---|---|
| Declaração | `class Caixa<T>` | `<T> T metodo(T x)` |
| O tipo vale para | a classe toda (atributos, métodos) | só aquele método |
| Quem define o tipo | quem faz `new Caixa<Tipo>()` | o argumento da chamada |
| Métodos `static` | **não** podem usar o `T` da classe | podem declarar o próprio `<T>` |

Por isso métodos estáticos que precisam de generics sempre declaram o seu próprio `<T>`.

## 5. Nada de tipos primitivos

`List<int>` não existe. Use Wrappers (`Integer`...).

## O que você precisa dominar (Aula 188)

- Sintaxe: `<T>` antes do tipo de retorno.
- Inferência de tipo na chamada.
- Métodos genéricos estáticos.
- Limites (`T extends Comparable<T>`).
- Diferença entre classe genérica e método genérico.

---

# Mapa mental do bloco

```text
Generics
├── Por quê:  checagem em COMPILAÇÃO, sem cast, sem ClassCastException
├── Type erasure: o tipo some no bytecode
├── Invariância:  List<Cachorro> NÃO é List<Animal>
├── Wildcards
│    ├── ? extends T  → ler (produtor)      — não adiciona
│    ├── ? super T    → escrever (consumidor) — lê como Object
│    └── PECS
├── Classes genéricas:  class RentalService<T> { ... }
│    ├── vários parâmetros: <K, V>
│    └── limite: <T extends Comparable<T>>
└── Métodos genéricos:  public static <T> List<T> criar(T x)
```

# Cola de bolso

| Situação | Use |
|---|---|
| Lista só de Strings | `List<String>` |
| Método que só **lê** uma lista de qualquer filho de `Animal` | `List<? extends Animal>` |
| Método que só **grava** `Cachorro` em listas de `Cachorro`, `Animal` ou `Object` | `List<? super Cachorro>` |
| Mesma lógica para vários tipos | `class Servico<T>` |
| Função utilitária sobre qualquer tipo | `static <T> ...` |
| Precisa comparar `T` | `<T extends Comparable<T>>` |
