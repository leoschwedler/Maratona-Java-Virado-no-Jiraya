# Bloco 29 — Padrões de Projeto (Design Patterns)

## Aulas 246 a 251

**Padrões de projeto** (*design patterns*) são **soluções reutilizáveis para problemas que se repetem** no desenvolvimento de software. Não são bibliotecas nem código pronto: são **receitas de organização do código**, com nomes conhecidos por todos os desenvolvedores. Saber o nome de um padrão ("isso aqui é um Builder") permite conversar e entender código alheio rapidamente — e é assunto frequente de entrevistas.

As aulas deste bloco são:

```text
246 — Padrões de Projeto pt 01 — Builder
247 — Padrões de Projeto pt 02 — Factory
248 — Padrões de Projeto pt 03 — Singleton pt 01 — Eager Initialization
249 — Padrões de Projeto pt 04 — Singleton pt 02 — Lazy Initialization
250 — Padrões de Projeto pt 05 — Singleton pt 03 — Singleton com enum
251 — Padrões de Projeto pt 06 — Data Transfer Object (DTO)
```

Classificação clássica (GoF — *Gang of Four*):

| Categoria | Resolve | Neste bloco |
|---|---|---|
| **Criacionais** | como **criar** objetos | Builder, Factory, Singleton |
| Estruturais | como **organizar/compor** classes | (DTO é um padrão de arquitetura corporativa) |
| Comportamentais | como objetos **interagem** | (Strategy apareceu no bloco 22) |

---

# Aula 246 — Builder

## 1. O problema: construtores com muitos argumentos

```java
public class Person {
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public Person(String firstName, String lastName, String username, String email) { ... }
}
```

Criando:

```java
Person person = new Person("William", "Suane", "DevDojo", "william@dojo.com.br");
```

Qual é qual? Qual é o `username` e qual é o sobrenome? A IDE ajuda com dicas ("hints"), mas:

- nem toda IDE faz isso (ou o código pode ser lido em revisão no navegador);
- após uma semana você mesmo esquece a ordem;
- é muito fácil **trocar dois argumentos do mesmo tipo** (`String, String`) sem erro de compilação;
- com 15 ou 20 atributos (acontece!) fica ilegível;
- atributos **opcionais** obrigam a criar vários construtores sobrecarregados ("telescoping constructors").

---

## 2. A solução: o padrão Builder

Cria-se uma classe interna `static` (o **builder**) que vai **coletando os valores com métodos nomeados**, e só no final constrói o objeto.

```java
public class Person {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;

    private Person(String firstName, String lastName, String username, String email) {   // PRIVADO
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    public String getFirstName() { return firstName; }
    // ... demais getters

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static final class PersonBuilder {
        private String firstName;
        private String lastName;
        private String username;
        private String email;

        private PersonBuilder() { }

        public PersonBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;                    // ⬅ devolve o PRÓPRIO builder
        }

        public PersonBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonBuilder username(String username) {
            this.username = username;
            return this;
        }

        public PersonBuilder email(String email) {
            this.email = email;
            return this;
        }

        public Person build() {
            return new Person(firstName, lastName, username, email);
        }
    }
}
```

### Uso (note a legibilidade)

```java
Person person = Person.builder()
        .firstName("William")
        .lastName("Suane")
        .username("DevDojo")
        .email("william@dojo.com.br")
        .build();
```

---

## 3. Por que funciona?

1. **Construtor privado** de `Person`: ninguém de fora faz `new Person(...)` — a única forma de criar é pelo builder. A classe `PersonBuilder` é **interna estática**, por isso consegue chamar o construtor privado (classes internas enxergam o `private` da externa — bloco 22!).
2. **Métodos que devolvem `this`** ("interface fluente" / *method chaining*): cada chamada configura um campo e devolve o mesmo builder, permitindo encadear as chamadas em uma "frase".
3. **`build()`** é o ponto final: devolve o objeto pronto.

> O instrutor explica por que retornar `this`: "todas as vezes que eu chamo, eu retorno o mesmo objeto, trabalhando no mesmo espaço de memória". O builder vai acumulando os valores.

## 4. Vantagens

- **Legibilidade**: cada valor tem um nome na chamada.
- **Ordem livre** dos parâmetros.
- **Opcionais** fáceis: chama só o que precisa.
- Permite **validar** no `build()` (por exemplo, lançar exceção se `firstName == null`).
- Permite objetos **imutáveis** (campos `final`).

Exemplo de validação:

```java
public Person build() {
    if (firstName == null || firstName.isBlank()) {
        throw new IllegalStateException("firstName é obrigatório");
    }
    return new Person(firstName, lastName, username, email);
}
```

## 5. Geração automática

A IDE gera builder para você: no IntelliJ, instale o plugin **"InnerBuilder"** e use **Alt+Insert → Builder**. (Também existem bibliotecas como o **Lombok** com `@Builder`, que você verá no JDBC.)

O instrutor também gera o método estático `builder()` na própria classe para simplificar a chamada (isso já mistura com o padrão Factory Method).

## O que você precisa dominar (Aula 246)

- O problema do construtor longo.
- Estrutura: construtor privado + builder estático + métodos que devolvem `this` + `build()`.
- Validação no `build()`.
- Uso de classe interna estática.

---

# Aula 247 — Factory

## 1. O problema: acoplamento na criação

Quando o código que **usa** um objeto também sabe **qual classe concreta instanciar** e **como**, qualquer mudança na criação (regras, novos tipos) obriga a alterar todos os pontos de uso.

Exemplo: uma interface e duas implementações.

```java
public interface Currency {
    String getSymbol();
}

public class Real implements Currency {
    @Override public String getSymbol() { return "R$"; }
}

public class Dollar implements Currency {
    @Override public String getSymbol() { return "US$"; }
}
```

Quem usa precisaria decidir `new Real()` ou `new Dollar()` — acoplado às classes concretas.

---

## 2. O padrão Factory (Fábrica)

Concentra a **decisão de criação** em um único lugar:

```java
public enum Country {
    BRAZIL,
    USA
}

public class CurrencyFactory {

    private CurrencyFactory() { }

    public static Currency newCurrency(Country country) {
        switch (country) {
            case BRAZIL:
                return new Real();
            case USA:
                return new Dollar();
            default:
                throw new IllegalArgumentException("País sem moeda cadastrada: " + country);
        }
    }
}
```

Uso:

```java
Currency currency = CurrencyFactory.newCurrency(Country.BRAZIL);
System.out.println(currency.getSymbol());       // R$
```

Quem chama:

- **não sabe** qual classe concreta foi criada (só conhece a interface `Currency`);
- **não precisa saber** como ela é construída.

## 3. Vantagens

1. **Baixo acoplamento**: o código cliente depende da **interface**, não das classes concretas.
2. **Manutenção**: se a regra de criação mudar (um construtor novo, um cache, uma configuração), você altera **um** lugar.
3. **Extensibilidade**: novo país → nova classe + novo `case` na fábrica.
4. Boa combinação com polimorfismo e interfaces (blocos 8–9).

## 4. Relação com o Builder

O instrutor mostra que o método `Person.builder()` (que cria um `PersonBuilder` sem expor o construtor) é uma aplicação do **Factory Method estático**: "o construtor é privado, e um método estático fabrica a instância". Também comenta que uma fábrica pode **impor regras de negócio** na criação (por exemplo, "o builder exige pelo menos o primeiro nome").

## 5. Variações

| Variação | Ideia |
|---|---|
| **Factory Method estático** (esta aula) | um método estático decide e devolve o objeto |
| **Abstract Factory** | uma fábrica de *famílias* de objetos relacionados |
| **Factory com Map/lambda** | `Map<Country, Supplier<Currency>>` evita o `switch` (veja bloco 23) |

```java
private static final Map<Country, Supplier<Currency>> FABRICAS = Map.of(
        Country.BRAZIL, Real::new,
        Country.USA, Dollar::new);

public static Currency newCurrency(Country country) {
    return FABRICAS.get(country).get();
}
```

## O que você precisa dominar (Aula 247)

- Para que serve: desacoplar criação do uso.
- Interface + implementações + fábrica estática.
- Uso de `switch` com `enum` na fábrica.
- Benefícios de manutenção.
- Relação com o Builder.

---

# Aula 248 — Singleton pt 01 — Eager Initialization

## 1. O problema

Há casos em que **só pode existir uma instância** de uma classe, e todo o sistema precisa compartilhá-la:

- conexão/pool com o banco de dados;
- configurações da aplicação;
- *cache*;
- *logger*;
- no exemplo da aula: **uma aeronave específica** (cada avião físico só existe uma vez, com seus assentos).

### Exemplo do problema

```java
public class Aircraft {
    private final String name;
    private final Set<String> availableSeats = new HashSet<>();

    public Aircraft(String name) {
        this.name = name;
        availableSeats.add("1A");
        availableSeats.add("1B");
    }

    public boolean bookSeat(String seat) {
        return availableSeats.remove(seat);          // true se conseguiu reservar
    }
}
```

Dois usuários fazendo:

```java
Aircraft a1 = new Aircraft("787-900");
Aircraft a2 = new Aircraft("787-900");              // OUTRO objeto, com os assentos "novos" outra vez!

a1.bookSeat("1A");   // true
a2.bookSeat("1A");   // true!! — o mesmo assento foi vendido duas vezes
```

Cada `new` cria **um objeto novo** com seus próprios assentos. Mas o avião é um só! Precisamos garantir que **todos usem o mesmo objeto** (mesmo espaço de memória, mesmos dados).

---

## 2. O padrão Singleton

Garante:

1. **Uma única instância** da classe.
2. Um **ponto global de acesso** a ela.

Ingredientes:

- **construtor privado** (ninguém de fora faz `new`);
- um atributo `private static` que guarda a instância;
- um método `public static getInstance()` que a devolve.

---

## 3. Eager Initialization (inicialização gulosa)

A instância é criada **no momento em que a classe é carregada**, antes mesmo de alguém pedir:

```java
public class AircraftSingletonEager {

    private static final AircraftSingletonEager INSTANCE = new AircraftSingletonEager("787-900");

    private final String name;
    private final Set<String> availableSeats = new HashSet<>();

    private AircraftSingletonEager(String name) {          // PRIVADO
        this.name = name;
        availableSeats.add("1A");
        availableSeats.add("1B");
    }

    public static AircraftSingletonEager getInstance() {
        return INSTANCE;
    }

    public boolean bookSeat(String seat) {
        return availableSeats.remove(seat);
    }
}
```

Uso:

```java
AircraftSingletonEager a1 = AircraftSingletonEager.getInstance();
AircraftSingletonEager a2 = AircraftSingletonEager.getInstance();

System.out.println(a1 == a2);              // true  — mesmo objeto
System.out.println(a1.bookSeat("1A"));     // true
System.out.println(a2.bookSeat("1A"));     // false — já reservado!  ✔
```

O instrutor prova com `hashCode`/`==`: de qualquer classe do sistema, `getInstance()` devolve o **mesmo endereço de memória**.

### Por que `static final`?

- `static`: pertence à classe, existe uma vez.
- `final`: a referência nunca muda (não pode ser reatribuída).
- Inicialização no carregamento da classe: a JVM garante que isso é **thread-safe** (o carregamento de classe é sincronizado).

## 4. Vantagens e desvantagens do *eager*

| ✅ Vantagens | ❌ Desvantagens |
|---|---|
| Simples | Cria o objeto **mesmo que nunca seja usado** (desperdício se for caro) |
| **Thread-safe** sem esforço | Não permite adiar a criação nem passar parâmetros em tempo de execução |
| | Não trata exceções na criação elegantemente |

## O que você precisa dominar (Aula 248)

- O problema de múltiplas instâncias com dados compartilhados.
- Construtor privado + `static` + `getInstance()`.
- Eager = instância criada no carregamento da classe.
- Por que é thread-safe e qual o custo.

---

# Aula 249 — Singleton pt 02 — Lazy Initialization

## 1. Ideia: criar só quando for preciso

```java
public class AircraftSingletonLazy {

    private static AircraftSingletonLazy instance;        // sem final, sem criação inicial

    private AircraftSingletonLazy(String name) { ... }

    public static AircraftSingletonLazy getInstance() {
        if (instance == null) {
            instance = new AircraftSingletonLazy("787-900");
        }
        return instance;
    }
}
```

A instância só nasce na **primeira** chamada a `getInstance()`. Ótimo se o objeto é caro de criar e talvez nem seja usado.

## 2. ⚠️ O problema: threads

Em ambiente multithread, duas threads podem passar pelo `if (instance == null)` **ao mesmo tempo** (condição de corrida, bloco 26!), e cada uma cria seu objeto → dois "singletons".

```text
T1: instance == null? sim  ─┐
T2: instance == null? sim  ─┤ (as duas passaram na verificação)
T1: instance = new ...     │
T2: instance = new ...     ← sobrescreveu: houve 2 objetos
```

## 3. Solução 1: método `synchronized`

```java
public static synchronized AircraftSingletonLazy getInstance() { ... }
```

Correto, mas **toda** chamada passa pelo lock (mesmo depois da instância criada), prejudicando o desempenho.

## 4. Solução 2: *double-checked locking* (verificação dupla)

```java
private static volatile AircraftSingletonLazy instance;       // volatile é essencial!

public static AircraftSingletonLazy getInstance() {
    if (instance == null) {                                    // 1ª verificação (sem lock; rápida)
        synchronized (AircraftSingletonLazy.class) {           // só entra quem precisa criar
            if (instance == null) {                            // 2ª verificação (com lock)
                instance = new AircraftSingletonLazy("787-900");
            }
        }
    }
    return instance;
}
```

- Depois de criada, as chamadas **não pegam lock** (a 1ª verificação já devolve).
- A 2ª verificação evita que duas threads criem duas instâncias.
- **`volatile`** impede que a JVM reordene a escrita da referência antes da construção completa do objeto (sem ele, outra thread poderia ver um objeto "meio construído").

O instrutor menciona o conceito e diz que, mesmo assim, "o código não fica 100%" bonito — por isso a solução com enum é melhor.

## 5. ⚠️ O problema da Reflection

Mesmo com construtor privado, a **API de Reflection** consegue acessá-lo:

```java
Constructor<AircraftSingletonLazy> constructor =
        AircraftSingletonLazy.class.getDeclaredConstructor(String.class);
constructor.setAccessible(true);                               // "destranca" o private

AircraftSingletonLazy outra = constructor.newInstance("787-900");   // NOVA instância!
System.out.println(outra == AircraftSingletonLazy.getInstance());   // false 💥
```

Reflection é muito usada por frameworks (Spring, Hibernate...) para ler metadados e acessar membros privados, mas afeta o desempenho e **quebra a garantia do Singleton**.

Defesa parcial: lançar exceção no construtor se `instance != null`. Mas o instrutor lembra: na vida real, **revisão de código** (alguém fazendo isso levaria "um tapa na cabeça") e a solução com **enum** resolvem.

| | Eager | Lazy simples | Lazy `synchronized` | Lazy double-check |
|---|---|---|---|---|
| Cria quando? | no carregamento | 1º uso | 1º uso | 1º uso |
| Thread-safe? | ✅ | ❌ | ✅ | ✅ (com `volatile`) |
| Custo por chamada | nenhum | nenhum | lock sempre | só na criação |
| Resiste a Reflection? | ❌ | ❌ | ❌ | ❌ |

## O que você precisa dominar (Aula 249)

- Lazy initialization e o problema multithread.
- `synchronized` no método x double-checked locking com `volatile`.
- Como a Reflection quebra o Singleton.
- Por que buscar uma solução melhor.

---

# Aula 250 — Singleton pt 03 — Singleton com `enum`

## 1. A solução elegante

Uma **enumeração com um único valor** é um Singleton:

```java
public enum AircraftSingleton {

    INSTANCE;                                               // a única instância

    private final Set<String> availableSeats = new HashSet<>();

    AircraftSingleton() {                                   // construtor (implicitamente private)
        availableSeats.add("1A");
        availableSeats.add("1B");
    }

    public boolean bookSeat(String seat) {
        return availableSeats.remove(seat);
    }
}
```

Uso:

```java
boolean ok = AircraftSingleton.INSTANCE.bookSeat("1A");     // sempre o mesmo objeto
```

Qualquer classe do sistema que use `AircraftSingleton.INSTANCE` está usando **exatamente o mesmo objeto**. O instrutor confirma comparando os `hashCode`.

---

## 2. Por que o enum é a melhor forma?

1. **Código curtíssimo e limpo** (comparado com a versão com `volatile` + `synchronized` + construtor privado).
2. **Thread-safe na criação**: a JVM garante que cada constante do enum é criada uma única vez, de forma segura.
3. **Resistente a Reflection**: a Reflection **não consegue** instanciar enums (`IllegalArgumentException: Cannot reflectively create enum objects`).
4. **Resistente a serialização**: ao desserializar um enum, o Java devolve a mesma constante (um Singleton comum precisaria de `readResolve`).
5. É a recomendação do livro *Effective Java* (Joshua Bloch).

## 3. Limitações / cuidados

- Um enum **não pode estender** outra classe (mas pode implementar interfaces).
- Cria a instância no carregamento da classe (como o *eager*), embora a classe do enum só seja carregada no primeiro uso.
- ⚠️ **Thread-safety da criação ≠ thread-safety do uso.** O instrutor alerta: o método `bookSeat` manipula um `HashSet` mutável. Se duas threads reservarem ao mesmo tempo, haverá condição de corrida! É preciso proteger os métodos (`synchronized`, `Lock`, coleções concorrentes), exatamente como no bloco de threads:

```java
public synchronized boolean bookSeat(String seat) {
    return availableSeats.remove(seat);
}
```

## 4. Resumo comparativo dos Singletons

| Forma | Simplicidade | Thread-safe criação | Reflection | Serialização |
|---|---|---|---|---|
| Eager | ⭐⭐⭐ | ✅ | ❌ | ❌ (precisa `readResolve`) |
| Lazy + double-check | ⭐ | ✅ | ❌ | ❌ |
| **Enum** | ⭐⭐⭐⭐ | ✅ | ✅ | ✅ |

## 5. Singleton é bom ou ruim?

Cuidado: Singleton é um **estado global**, o que dificulta **testes** (difícil trocar por um falso) e pode esconder dependências. Em aplicações modernas, frameworks de **injeção de dependências** (Spring) gerenciam "beans singleton" de forma mais testável. Use o padrão com critério.

## O que você precisa dominar (Aula 250)

- Singleton com `enum` (`INSTANCE`).
- Por que resolve thread-safety de criação, Reflection e serialização.
- Mas o **uso** dos métodos ainda precisa de sincronização.
- Crítica ao uso excessivo de Singleton.

---

# Aula 251 — Data Transfer Object (DTO)

## 1. O problema

Um padrão de **arquitetura de aplicações corporativas** (*Enterprise Application Architecture*, de Martin Fowler). Sistema A precisa enviar dados para o sistema B (outra API, um *front-end*, um relatório).

Esses dados vêm de **várias entidades**: aeronave, país, moeda, pessoa... Enviar todos os objetos inteiros seria:

- pesado (muito mais dados do que o necessário);
- inseguro (exporia atributos internos, senhas...);
- acoplado (quem recebe passa a depender do modelo interno).

---

## 2. A solução: o DTO

Um objeto **simples**, só com **os atributos que o destinatário precisa**, sem lógica de negócio. É montado juntando pedaços de vários objetos.

```java
public class ReportDTO {
    private final String aircraftName;
    private final String country;
    private final String currencySymbol;
    private final String personFirstName;

    // construtor (ou builder!), getters, toString
}
```

Montagem a partir das entidades:

```java
Aircraft aircraft = AircraftSingleton.INSTANCE;                 // do domínio
Country country = Country.BRAZIL;
Currency currency = CurrencyFactory.newCurrency(country);
Person person = Person.builder().firstName("William").lastName("Suane").build();

ReportDTO report = ReportDTO.builder()                          // (combina com o Builder!)
        .aircraftName(aircraft.getName())
        .country(country.name())
        .currencySymbol(currency.getSymbol())
        .personFirstName(person.getFirstName())
        .build();

System.out.println(report);       // toString com todos os campos
```

Em vez de enviar quatro objetos, envia-se **um**.

## 3. Características

- Só **dados** (getters, construtor/builder, `toString`, `equals/hashCode`).
- **Sem lógica de negócio**.
- Costuma ser **imutável**.
- Formato pensado para o **consumidor** (JSON de uma API, tela, relatório).

## 4. DTO × Entidade

| | Entidade (domínio) | DTO |
|---|---|---|
| Representa | conceito do negócio, com regras | formato de transporte |
| Tem lógica? | sim | não |
| Mapeada no banco (JPA)? | sim | não |
| Quem a conhece | o núcleo do sistema | as fronteiras (API, UI) |

Conversão entre os dois é feita por um *mapper* (manual ou por bibliotecas como MapStruct).

## 5. Em Java moderno

A partir do Java 16, `record` é ideal para DTOs (bloco 32):

```java
public record ReportDTO(String aircraftName, String country, String currencySymbol, String personFirstName) { }
```

## 6. Encerramento do bloco

O instrutor anuncia: "esse é o último vídeo sobre design patterns; agora vamos falar sobre banco de dados — **JDBC**".

## O que você precisa dominar (Aula 251)

- O problema de transportar dados de várias entidades.
- DTO: classe simples, só com dados necessários.
- DTO × entidade.
- Combinar DTO com Builder.

---

# Mapa mental do bloco

```text
Padrões Criacionais
├── Builder      → construtor privado + builder estático + métodos fluentes + build()
│                  (objetos com muitos atributos; legibilidade; imutabilidade; validação)
├── Factory      → método estático decide qual classe concreta criar (desacopla criação do uso)
├── Singleton    → uma única instância global
│    ├── Eager   → static final INSTANCE = new ...  (thread-safe, mas criado sempre)
│    ├── Lazy    → cria no 1º uso; precisa synchronized / double-check + volatile
│    └── Enum    → INSTANCE;  (simples, seguro contra Reflection e serialização)
└── (Arquitetura) DTO → objeto de transporte só com dados
```

# Cola de bolso

| Situação | Padrão |
|---|---|
| Construtor com muitos parâmetros | **Builder** |
| Decidir qual implementação criar | **Factory** |
| Precisa de uma única instância compartilhada | **Singleton** (preferir `enum`) |
| Enviar dados agregados para outro sistema/tela | **DTO** |
| Evitar `new` espalhado pelo código | **Factory** |
| Singleton + várias threads | proteger os **métodos** além da criação |
