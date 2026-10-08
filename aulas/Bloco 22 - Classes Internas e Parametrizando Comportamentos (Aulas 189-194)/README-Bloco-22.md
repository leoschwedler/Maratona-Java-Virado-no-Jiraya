# Bloco 22 — Classes Internas e Parametrização de Comportamentos

## Aulas 189 a 194

Este bloco tem duas partes que se encaixam perfeitamente:

1. **Classes internas** (aninhadas, locais, anônimas e estáticas): classes declaradas dentro de outras.
2. **Parametrização de comportamentos**: passar *o que fazer* como parâmetro de um método. A classe anônima da parte 1 é a ferramenta que viabiliza isso, e o bloco termina com a ponte para as **lambdas** (próximos blocos).

As aulas deste bloco são:

```text
189 — Classes internas pt 01 — Introdução (classe aninhada / membro)
190 — Classes internas pt 02 — Classes locais
191 — Classes internas pt 03 — Classes anônimas
192 — Classes internas pt 04 — Classes aninhadas estáticas
193 — Parametrizando comportamentos pt 01
194 — Parametrizando comportamentos pt 02
```

## Os quatro tipos de classes aninhadas

```text
class Externa {
    class Interna { }                  ← 1) Membro (não estática) — aula 189
    static class Estatica { }          ← 4) Aninhada estática — aula 192

    void metodo() {
        class Local { }                ← 2) Local (dentro de método) — aula 190
        new Animal() { ... };          ← 3) Anônima (sem nome) — aula 191
    }
}
```

---

# Aula 189 — Classes Internas pt 01 — Introdução

## 1. O que é uma classe interna?

É simplesmente **uma classe declarada dentro de outra classe**:

```java
public class OuterClassesTest01 {

    private String name = "William Suane";

    class Inner {
        public void printOuterClassAttribute() {
            System.out.println(name);          // acessa o atributo da classe EXTERNA
        }
    }
}
```

### Para que serve?

Quando duas classes são **tão fortemente acopladas** que uma só faz sentido dentro da outra. O exemplo do instrutor: uma interface gráfica (janela) e as ações dos botões (clicar em "enviar mensagem"). A ação não faz sentido fora da janela. Em código de interface gráfica clássico (Swing/AWT) e em padrões como o *Builder* isso é muito comum.

Vantagens:

- **Encapsulamento**: a classe interna pode ser `private` e invisível ao resto do sistema.
- **Acesso direto** aos atributos e métodos da classe externa, até os `private`.
- Organização: o código relacionado fica junto.

---

## 2. A classe interna depende de uma instância da externa

Uma classe interna (não estática) está **ligada a um objeto** da classe externa. Por isso você não pode instanciá-la sozinha:

```java
Inner inner = new Inner();   // ❌ a menos que você já esteja num contexto de instância da externa
```

Dois modos de criar de fora:

```java
Outer outer = new Outer();

// modo 1: a partir do objeto externo
Outer.Inner inner1 = outer.new Inner();

// modo 2: tudo em uma linha
Outer.Inner inner2 = new Outer().new Inner();
```

A sintaxe `outer.new Inner()` parece estranha, mas é exatamente o que significa: "crie uma `Inner` pertencente a *este* `outer`".

Note o tipo `Outer.Inner` (caminho completo: classe externa, ponto, classe interna).

---

## 3. O que `this` significa?

Há dois objetos envolvidos. Dentro da classe interna:

```java
class Inner {
    public void print() {
        System.out.println(this);            // o objeto da classe INTERNA
        System.out.println(Outer.this);      // o objeto da classe EXTERNA
    }
}
```

| Expressão | Refere-se a |
|---|---|
| `this` | à instância da classe **interna** |
| `Outer.this` | à instância da classe **externa** que "contém" a interna |

## 4. Modificadores de acesso

A classe interna pode ser `private`, `protected`, `public` ou pacote — algo que classes de topo não podem (só `public` ou pacote). Uma interna `private` só é acessível dentro da externa.

## O que você precisa dominar (Aula 189)

- Declarar uma classe dentro de outra.
- A interna acessa os atributos da externa (inclusive `private`).
- Criar com `outer.new Inner()`.
- `this` x `Externa.this`.
- Quando faz sentido usar (acoplamento forte).

---

# Aula 190 — Classes Internas pt 02 — Classes Locais

## 1. O que é

Uma **classe local** é declarada **dentro de um método** (ou de qualquer bloco de código). Sua "vida" é limitada ao bloco onde foi declarada.

```java
public class LocalClassesTest01 {
    private String name = "William";

    public void print() {

        class LocalClass {                              // classe dentro do método
            public void printLocal() {
                System.out.println(name);
            }
        }

        LocalClass local = new LocalClass();            // só pode ser instanciada AQUI
        local.printLocal();
    }
}
```

Regras:

1. Só existe dentro do método: **não é possível instanciá-la fora dele**. Quem quiser usar precisa instanciar no próprio método.
2. Pode ter atributos e métodos (e construtores) normalmente.
3. Acessa os atributos da classe externa.
4. **Não aceita modificadores de acesso** (`public`, `private`, ...), pois não é membro da classe. Só `abstract` ou `final`.
5. O instrutor admite: é difícil encontrar exemplos reais; é bom conhecer, mas o uso é raro.

---

## 2. Variáveis locais capturadas: "final ou efetivamente final"

A classe local pode usar as **variáveis locais e parâmetros** do método onde foi declarada, **desde que** elas sejam `final` ou **efetivamente finais**.

```java
public void print() {
    String lastName = "Suane";                  // nunca é reatribuída → efetivamente final

    class LocalClass {
        public void printLocal() {
            System.out.println(name + " " + lastName);   // ✅
        }
    }
    new LocalClass().printLocal();
}
```

*Efetivamente final* = variável que **nunca recebe um novo valor depois de inicializada** (você nem precisa escrever `final`; desde o Java 8 o compilador deduz).

```java
String lastName = "Suane";
lastName = "Outro";            // alterou! deixou de ser efetivamente final

class LocalClass {
    void p() { System.out.println(lastName); }   // ❌ ERRO: "must be final or effectively final"
}
```

A mesma regra vale para parâmetros do método.

### Por que essa regra existe?

O instrutor explica: variáveis locais vivem na **pilha (stack)** e morrem quando o método termina. Mas o **objeto** da classe local (que está no *heap*) pode continuar vivo depois do método (por exemplo, se for devolvido ou guardado). Para evitar que o objeto precise de uma variável que já deixou de existir, o Java **copia o valor** para dentro do objeto — e isso só é seguro se o valor nunca mudar. Daí a exigência: *final ou efetivamente final*.

```text
método termina → variável local some
objeto da classe local pode continuar vivo → precisa ter sua própria cópia, imutável
```

## O que você precisa dominar (Aula 190)

- Classe local = declarada em método/bloco, vive só ali.
- Instanciação dentro do método.
- Sem modificadores de acesso (apenas `final`/`abstract`).
- Variáveis capturadas precisam ser final/efetivamente finais e por quê.

---

# Aula 191 — Classes Internas pt 03 — Classes Anônimas

## 1. O que é

Uma **classe anônima** é uma classe **sem nome**, declarada e instanciada **na mesma expressão**, para ser usada **uma única vez**.

Cenário: classe `Animal`:

```java
public class Animal {
    public void walk() {
        System.out.println("Animal walking");
    }
}
```

Para mudar o comportamento de `walk` só em **um ponto** do código, em vez de criar uma subclasse `Cachorro` inteira e poluir o projeto, faça:

```java
Animal animal = new Animal() {          // ← chaves logo após o new
    @Override
    public void walk() {
        System.out.println("Walking in the dog's style");
    }
};
animal.walk();    // Walking in the dog's style
```

O que acontece de fato: o Java cria uma **subclasse sem nome** de `Animal`, sobrescreve `walk()` e já instancia **um objeto** dela.

Pontos de atenção:

- A variável é do tipo `Animal` (a superclasse). Por isso você **só consegue chamar** métodos que existem em `Animal`. Um método novo criado dentro da classe anônima (ex.: `void ok()`) não pode ser chamado de fora, porque a variável não "enxerga" esse tipo.
- Dentro do corpo da classe anônima, você pode chamar seus próprios métodos novos, sem problemas.
- Após a instrução, a classe nunca mais pode ser reutilizada.

---

## 2. Com **interfaces** (o uso mais comum)

Também funciona com interfaces — aqui você implementa uma interface anonimamente:

```java
List<Barco> barcos = new ArrayList<>(List.of(new Barco("Lancha"), new Barco("Canoa")));

Collections.sort(barcos, new Comparator<Barco>() {
    @Override
    public int compare(Barco b1, Barco b2) {
        return b1.getNome().compareTo(b2.getNome());
    }
});
```

Em vez de criar `class BarcoComparator implements Comparator<Barco>` num arquivo separado e usá-la uma vez, você a escreve ali mesmo.

> `new Comparator<Barco>() { ... }` **não está instanciando uma interface** (isso é impossível) — está criando uma **classe anônima que implementa** `Comparator`.

Observação do instrutor: `List.of` cria lista imutável e dá `UnsupportedOperationException` ao ordenar; por isso copiamos para `new ArrayList<>(...)`.

---

## 3. Evolução da sintaxe

```java
// 1) classe anônima (Java 1.1+)
Collections.sort(barcos, new Comparator<Barco>() {
    @Override
    public int compare(Barco b1, Barco b2) {
        return b1.getNome().compareTo(b2.getNome());
    }
});

// 2) lambda (Java 8) — mesmo comportamento, uma linha
Collections.sort(barcos, (b1, b2) -> b1.getNome().compareTo(b2.getNome()));

// 3) method reference + Comparator utilitário (Java 8)
barcos.sort(Comparator.comparing(Barco::getNome));
```

O instrutor mostra a segunda forma como "spoiler" e avisa: **não é classe anônima**, é lambda — assunto dos próximos blocos. O que você precisa entender agora é a classe anônima, porque **lambdas existem para substituir exatamente esse tipo de classe anônima** (de interface com um só método).

## 4. Regras importantes

- Pode capturar variáveis locais **efetivamente finais**, assim como a classe local.
- Pode ter atributos, blocos de inicialização e métodos, mas **não construtores** (não tem nome).
- Estende **uma** classe **ou** implementa **uma** interface (nunca as duas ao mesmo tempo).
- Dentro dela, `this` refere-se à própria classe anônima.
- Usos clássicos: `Comparator`, `Runnable`, ouvintes de eventos em interfaces gráficas.

## O que você precisa dominar (Aula 191)

- `new Tipo() { ... }` cria uma subclasse/implementação anônima.
- Só os métodos do tipo da variável são acessíveis de fora.
- Implementar interfaces anonimamente (`Comparator`).
- Quando faz sentido (comportamento pontual, usado uma vez).
- Lambda como evolução da classe anônima.

---

# Aula 192 — Classes Internas pt 04 — Classes Aninhadas Estáticas

## 1. O que é

É uma classe interna marcada com `static`:

```java
public class StaticNestedTest01 {
    private String name = "William";

    static class Nested {
        public void print() {
            System.out.println("Dentro da classe aninhada estática");
            // System.out.println(name);   // ❌ não compila: name não é estático
        }
    }
}
```

### Diferenças para a classe interna comum

| | Interna (não estática) | Aninhada estática |
|---|---|---|
| Precisa de instância da externa? | **sim** (`outer.new Inner()`) | **não** |
| Acessa atributos de instância da externa? | **sim** | **não** (só os `static`) |
| É como... | parte do objeto externo | uma classe de **alto nível** apenas "empacotada" dentro de outra |

Para acessar o atributo `name` da externa, a estática precisa de um **objeto** da externa:

```java
static class Nested {
    void print(StaticNestedTest01 outer) {
        System.out.println(outer.name);     // ✅ usando uma referência explícita
    }
}
```

(Isso é exatamente como se `Nested` fosse uma classe separada.)

---

## 2. Como instanciar

```java
StaticNestedTest01.Nested nested = new StaticNestedTest01.Nested();   // sem objeto externo!
nested.print();
```

---

## 3. Quando usar?

Quando a classe tem relação **lógica** com a externa, mas não depende do estado dela. Exemplos famosos da própria API do Java:

- **`Map.Entry<K, V>`**: é uma interface aninhada dentro de `Map` (como o instrutor mostra). Só faz sentido no contexto de um `Map`, então vive dentro dele.
- `AbstractMap.SimpleEntry`.
- O padrão **Builder** (aula de padrões de projeto): a classe `Builder` costuma ser uma `static class` aninhada dentro da classe que ela constrói.

Vantagens: nome organizado (`Map.Entry`), acesso a membros `private static` da externa, e redução de arquivos soltos no pacote.

## 4. Resumo dos 4 tipos

| Tipo | Declarada em | Precisa de objeto externo? | Tem nome? |
|---|---|---|---|
| Membro (interna) | corpo da classe | sim | sim |
| Estática aninhada | corpo da classe, com `static` | não | sim |
| Local | dentro de método/bloco | (está num contexto de instância ou estático) | sim |
| Anônima | na expressão `new` | (idem) | **não** |

## O que você precisa dominar (Aula 192)

- Classe `static` aninhada não precisa de instância da externa.
- Não acessa membros de instância da externa diretamente.
- Instanciação `new Externa.Aninhada()`.
- Exemplo real: `Map.Entry`.

---

# Aula 193 — Parametrizando comportamentos pt 01 — o problema

## 1. Cenário

Classe de domínio:

```java
public class Car {
    private String name;
    private String color;
    private int year;

    // construtor, getters, toString
}
```

Uma "base de dados" em memória:

```java
private static List<Car> cars = List.of(
        new Car("Audi", "green", 2011),
        new Car("Fusca", "black", 1998),
        new Car("Ferrari", "red", 2019)
);
```

### Requisito 1 — "quero ver só os carros verdes"

```java
private static List<Car> filterGreenCars(List<Car> cars) {
    List<Car> filtered = new ArrayList<>();
    for (Car car : cars) {
        if (car.getColor().equals("green")) {
            filtered.add(car);
        }
    }
    return filtered;
}
```

### Requisito 2 — "agora os vermelhos"

Copia e cola: `filterRedCars`. (O instrutor pede para não julgar: todo mundo já fez!)

### Melhoria 1 — cor como parâmetro

```java
private static List<Car> filterCarByColor(List<Car> cars, String color) {
    List<Car> filtered = new ArrayList<>();
    for (Car car : cars) {
        if (car.getColor().equals(color)) {
            filtered.add(car);
        }
    }
    return filtered;
}
```

Funciona para "green", "red", "black"...

### Requisito 3 — "carros fabricados antes de X"

Aí a cor como parâmetro não serve: o critério é outro (ano). Nasce mais um método:

```java
private static List<Car> filterCarOlderThan(List<Car> cars, int year) {
    List<Car> filtered = new ArrayList<>();
    for (Car car : cars) {
        if (car.getYear() < year) {
            filtered.add(car);
        }
    }
    return filtered;
}
```

E amanhã vem "carros acima de tal preço", "que comecem com A"... uma explosão de métodos.

---

## 2. Observando o padrão

Compare os métodos:

```text
filterGreenCars         → if (car.getColor().equals("green"))
filterCarByColor        → if (car.getColor().equals(color))
filterCarOlderThan      → if (car.getYear() < year)
```

**Todo o resto é idêntico.** A única coisa que muda é a **condição do `if`** — o **comportamento**.

```text
estrutura (igual):   percorre → testa condição → adiciona na lista nova → devolve
condição (muda):     o que significa "passar no filtro"
```

A ideia da próxima aula: passar essa **condição como parâmetro**.

## 3. Dica de carreira (do instrutor)

Quando o cliente pede uma alteração "rápida", **nunca responda prazo de primeira**: peça um tempo para avaliar. Prazos dados no impulso viram promessas que você precisa cumprir.

## O que você precisa dominar (Aula 193)

- Reconhecer a repetição de código onde só a condição muda.
- Por que parâmetros simples (valores) não resolvem tudo.
- O conceito de "comportamento" como a parte que varia.

---

# Aula 194 — Parametrizando comportamentos pt 02 — a solução

## 1. Uma interface para o comportamento

Cria-se uma interface com **um único método** que responde "este carro passa no filtro?":

```java
public interface CarPredicate {
    boolean test(Car car);
}
```

Agora o filtro **não decide mais** nada: ele recebe a decisão por parâmetro.

```java
private static List<Car> filter(List<Car> cars, CarPredicate predicate) {
    List<Car> filtered = new ArrayList<>();
    for (Car car : cars) {
        if (predicate.test(car)) {         // <- o comportamento vem de fora (polimorfismo!)
            filtered.add(car);
        }
    }
    return filtered;
}
```

Aqui está o **polimorfismo** em ação: `filter` não sabe qual implementação receberá; apenas chama `test`.

## 2. Usando com classes anônimas

```java
List<Car> greenCars = filter(cars, new CarPredicate() {
    @Override
    public boolean test(Car car) {
        return car.getColor().equals("green");
    }
});

List<Car> oldCars = filter(cars, new CarPredicate() {
    @Override
    public boolean test(Car car) {
        return car.getYear() < 2015;
    }
});
```

Uma só função `filter`; o comportamento específico é "plugado" a cada chamada. Isso é o padrão **Strategy** (estratégia) de forma leve.

## 3. A ponte para as Lambdas (Java 8)

Como `CarPredicate` tem **um único método abstrato** (é uma **interface funcional**), a classe anônima pode ser substituída por uma **lambda**:

```java
List<Car> greenCars = filter(cars, car -> car.getColor().equals("green"));
List<Car> redCars   = filter(cars, car -> car.getColor().equals("red"));
List<Car> oldCars   = filter(cars, car -> car.getYear() < 2015);
```

Leitura: "dado um `car`, devolva (`->`) o resultado desta condição".

Sintaxe resumida:

```text
(parâmetros) -> expressão ou { bloco }
car -> car.getYear() < 2015
```

Tudo que antes ocupava cinco linhas de classe anônima virou **uma linha**.

---

## 4. Tornando o filtro genérico (generics + interface funcional)

A interface `CarPredicate` só serve para carros. Com generics (bloco anterior) fica para qualquer tipo:

```java
public interface Predicate<T> {
    boolean test(T t);
}

private static <T> List<T> filter(List<T> lista, Predicate<T> predicate) {
    List<T> filtered = new ArrayList<>();
    for (T elemento : lista) {
        if (predicate.test(elemento)) {
            filtered.add(elemento);
        }
    }
    return filtered;
}
```

Agora o **mesmo método** filtra qualquer lista:

```java
List<Car> verdes = filter(cars, car -> car.getColor().equals("green"));

List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> pares = filter(numeros, num -> num % 2 == 0);

List<String> nomes = List.of("Ana", "Beatriz", "Caio");
List<String> grandes = filter(nomes, nome -> nome.length() > 3);
```

## 5. Você não precisa criar essa interface

O Java já tem exatamente isso no pacote **`java.util.function`**:

```java
java.util.function.Predicate<T>    // boolean test(T t)
```

Foi justamente a criação desse pacote que trouxe as interfaces funcionais prontas no Java 8. As próximas aulas (195 em diante) estudam `Predicate`, `Consumer`, `Function`...

## 6. Resumo da evolução

```text
1) um método por requisito          → repetição
2) valor por parâmetro              → ainda rígido
3) interface + classe anônima       → comportamento por parâmetro
4) lambda                           → mesma coisa, sintaxe enxuta
5) generics + Predicate<T>          → serve para qualquer tipo
```

## O que você precisa dominar (Aula 194)

- Interface com um único método define o "comportamento".
- Passar comportamento como argumento (Strategy).
- Classe anônima → lambda.
- Filtro genérico `<T>` com `Predicate<T>`.
- A existência de `java.util.function`.

---

# Mapa mental do bloco

```text
Classes aninhadas
├── Membro (inner)       → precisa de outer.new Inner(); acessa tudo da externa; this x Outer.this
├── Local                → dentro de método; só final/efetivamente final; sem modificador de acesso
├── Anônima              → new Tipo() { ... }; sem nome; uso único; base das lambdas
└── Estática aninhada    → static class; não precisa de objeto externo (Map.Entry, Builder)

Parametrizando comportamentos
└── condição variável → interface funcional → classe anônima → lambda → Predicate<T> + generics
```

# Cola de bolso

| Preciso... | Use |
|---|---|
| Classe que só faz sentido dentro de outra e usa seu estado | classe interna (membro) |
| Classe auxiliar que não precisa do estado da externa | `static class` aninhada |
| Classe usada num único método | classe local |
| Implementação pontual de interface/classe | classe anônima (hoje, lambda quando possível) |
| Vários métodos iguais variando só a condição | interface funcional + comportamento por parâmetro |
