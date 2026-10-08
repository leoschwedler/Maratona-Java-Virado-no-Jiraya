# Bloco 34 — JUnit, atualização do JDK, Record Class e Pattern Matching

## Aulas 280 a 285

O bloco final do curso. Ele traz uma prática que separa quem "faz código funcionar" de quem "mantém código funcionando": **testes unitários com JUnit 5**. Depois, apresenta duas novidades da linguagem (Java 16): **`record`** e **pattern matching para `instanceof`**, e fecha com a despedida do instrutor.

```text
280 — Testes unitários com JUnit pt 01
281 — Testes unitários com JUnit pt 02
282 — Atualizando o JDK (15 → 16)
283 — Record Class
284 — Pattern Matching for instanceof
285 — Just run for the hug (encerramento)
```

> **Nota sobre as transcrições:** são auto-geradas e ruidosas. O conteúdo foi reconstruído com o que o instrutor demonstrou, com o código reescrito de forma limpa. A aula 285 é uma mensagem de encerramento (sem conteúdo técnico).

---

# Aula 280 — Testes unitários com JUnit (parte 1)

## 1. Por que testar?

Até aqui o curso criou classes de "teste" que, na verdade, eram `main` com `System.out`/`log`. Isso tem problemas:

- Você precisa **olhar a saída** e julgar sozinho se está certo;
- Seis meses depois, ninguém lembra o comportamento esperado;
- Outras pessoas mexem no mesmo código e podem **quebrar** o que funcionava, sem perceber.

**Teste unitário** = verifica automaticamente **uma unidade** do código (normalmente um método) e diz *passou* ou *falhou*.

Tipos de teste (só para contexto): unitário, integração, componente, comportamento (BDD), ponta a ponta... O unitário é o mais básico e o mais exigido em entrevistas — o instrutor diz que a maioria dos candidatos que ele rejeita é por não saber escrever testes.

## 2. O cenário da aula

Domínio:

```java
@Getter @Setter @AllArgsConstructor
public class Person {
    private String name;
    private Integer age;
}
```

Regra de negócio (camada de **service**):

```java
public class PersonService {
    public boolean isAdult(Person person) {
        Objects.requireNonNull(person, "Person can't be null");
        return person.getAge() >= 18;
    }
}
```

Teste "à moda antiga" (sem JUnit):

```java
PersonService service = new PersonService();
log.info(service.isAdult(new Person("William", 15)));  // false
```

O problema: se alguém trocar `>= 18` por `>= 21`, **nada avisa**. É aí que entram os testes automatizados.

## 3. Adicionando o JUnit 5 (Jupiter)

No `pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.7.2</version>
    <scope>test</scope>
</dependency>
```

- Use sempre uma versão **estável** (evite `M1`, `RC` — *Milestone* / *Release Candidate*, ainda em desenvolvimento).
- `scope = test` → a biblioteca só existe no classpath de teste.
- Estrutura de pastas do Maven:

```text
src/main/java   → código da aplicação
src/test/java   → código de teste (mesmo pacote da classe testada)
```

## 4. Criando a classe de teste

No IntelliJ: abra a classe, `Alt+Enter` → **Create Test** (ou `Ctrl+Shift+T`). Escolha **JUnit5**. A convenção: `PersonServiceTest`, no mesmo pacote, dentro de `src/test/java`.

> **O que testar?** O instrutor testa a **camada de negócio** (service), não getters/setters. A cobertura de código é consequência de testar bem as funcionalidades.

## 5. Primeiro teste e *assertions*

```java
import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    @Test
    void isAdult_ReturnsFalse_WhenAgeIsLessThan18() {
        Person person = new Person("William", 15);
        PersonService service = new PersonService();

        boolean result = service.isAdult(person);

        assertFalse(result);                 // forma curta
        // assertEquals(false, result);      // forma longa equivalente
    }
}
```

**Assertiva** (*assertion*) = afirmação: "isso **tem** que ser assim; se não for, o teste falha".

Principais métodos de `Assertions`:

| Método | Verifica |
|---|---|
| `assertTrue(cond)` / `assertFalse(cond)` | condição booleana |
| `assertEquals(esperado, atual)` | igualdade (`equals`) |
| `assertNotEquals(a, b)` | diferença |
| `assertNull(x)` / `assertNotNull(x)` | nulidade |
| `assertSame(a, b)` | mesma referência |
| `assertThrows(Tipo.class, () -> ...)` | lançamento de exceção |
| `assertAll(...)` | várias asserções juntas |
| `assertArrayEquals(a, b)` | arrays |

> **Ordem dos parâmetros:** `assertEquals(esperado, atual)` — se inverter, a mensagem de falha fica confusa.

## 6. Nome do método de teste

Não há convenção única. A preferência do instrutor:

```text
nomeDoMetodo_ComportamentoEsperado_QuandoCondicao
isAdult_ReturnsFalse_WhenAgeIsLessThan18
```

O nome longo é bom: quando o teste **falha** num servidor/CI, o nome é o que você lê.

Outra opção — `@DisplayName`, para um texto mais bonito no relatório:

```java
@Test
@DisplayName("isAdult returns false when age is less than 18")
void isAdult_ReturnsFalse_WhenAgeIsLessThan18() { ... }
```

Os testes não rodam só na sua máquina: em **integração contínua (CI)**, a cada push o servidor executa todos os testes. Esta é uma boa prática hoje em dia.

---

# Aula 281 — Testes unitários com JUnit (parte 2)

## 1. Cobertura de código ≠ qualidade

No IntelliJ: botão direito no teste → **Run with Coverage**. Ele mostra quantas linhas/métodos foram executados.

Mas atenção: **100% de cobertura não garante bons testes**. Um único teste pode "passar" por todas as linhas e ainda assim deixar casos de fora. O que importa é cobrir **comportamentos**, inclusive os não óbvios:

- idade **menor** que 18 (false)
- idade **igual** a 18 (true) ← caso de fronteira
- idade **maior** que 18 (true)
- objeto **nulo** (exceção)

> Dica de entrevista (do instrutor): quem mostra testes que cobrem *casos não explícitos* se destaca.

## 2. Mais testes

```java
@Test
void isAdult_ReturnsTrue_WhenAgeIsGreaterOrEqual18() {
    Person person = new Person("William", 18);
    assertTrue(service.isAdult(person));
}
```

## 3. Independência dos testes e `@BeforeEach`

Cada teste deve ser **independente** dos outros. Se um teste altera um objeto compartilhado, pode afetar o seguinte.

`@BeforeEach` roda **antes de cada teste**, criando objetos novos:

```java
class PersonServiceTest {
    private PersonService service;
    private Person adult;
    private Person minor;

    @BeforeEach
    void setUp() {
        service = new PersonService();
        adult = new Person("William", 18);
        minor = new Person("Suane", 15);
    }

    @Test
    void isAdult_ReturnsTrue_WhenAgeIsGreaterOrEqual18() {
        assertTrue(service.isAdult(adult));
    }

    @Test
    void isAdult_ReturnsFalse_WhenAgeIsLessThan18() {
        assertFalse(service.isAdult(minor));
    }
}
```

Anotações de ciclo de vida do JUnit 5:

| Anotação | Quando roda |
|---|---|
| `@BeforeEach` | antes de **cada** teste |
| `@AfterEach` | depois de cada teste |
| `@BeforeAll` | uma vez, antes de todos (método `static`) |
| `@AfterAll` | uma vez, depois de todos (método `static`) |
| `@Disabled` | desativa o teste |

## 4. O valor dos testes na prática (a "demonstração do estrago")

Cenário: um colega muda `>= 18` para `>= 21`. Ao rodar a suíte:

```text
isAdult_ReturnsTrue_WhenAgeIsGreaterOrEqual18  → FALHOU
```

Você descobre **na hora** que uma mudança quebrou o comportamento. Então decide: reverter, ou (se o requisito mudou) atualizar o teste **e** investigar quem mais dependia do valor 18.

## 5. Testando exceções com `assertThrows`

```java
@Test
void isAdult_ThrowsNullPointerException_WhenPersonIsNull() {
    assertThrows(NullPointerException.class, () -> service.isAdult(null));
}
```

Para validar também a **mensagem**:

```java
NullPointerException ex = assertThrows(NullPointerException.class,
        () -> service.isAdult(null));
assertEquals("Person can't be null", ex.getMessage());
```

Lição da aula: o instrutor mostra um teste que falha porque a mensagem esperada era diferente da mensagem real. Por que validar tipo (e às vezes mensagem)? Porque **clientes (front-end, outros serviços) podem depender da exceção** que você lança. Trocar de `NullPointerException` para `IllegalArgumentException` pode mudar o comportamento deles (ex.: um servidor web devolver 400 em vez de 500).

## 6. Testando coleções

Novo método do service:

```java
public List<Person> filterRemovingNotAdult(List<Person> people) {
    return people.stream()
            .filter(this::isAdult)
            .collect(Collectors.toList());
}
```

Teste:

```java
@Test
void filterRemovingNotAdult_ReturnsOnlyAdults_WhenListContainsAdultsAndMinors() {
    Person p1 = new Person("A", 15);
    Person p2 = new Person("B", 18);
    Person p3 = new Person("C", 21);
    List<Person> people = List.of(p1, p2, p3);

    List<Person> result = service.filterRemovingNotAdult(people);

    List<Person> expected = List.of(p2, p3);
    assertEquals(expected, result);   // compara com equals de List (elemento a elemento)
}
```

Para o `assertEquals` entre listas funcionar com objetos próprios, `Person` precisa implementar `equals/hashCode` (ex.: `@EqualsAndHashCode`) ou você compara **as mesmas instâncias**, como acima.

Nomes longos? "Quando você tiver acesso ao terminal / CI, vai agradecer pelos nomes descritivos".

Se alguém mudar a regra de 18 para 21, **dois testes falham** (o de adulto e o do filtro) — a suíte aponta o impacto da mudança.

## 7. Boas práticas (resumo)

- **AAA**: *Arrange* (monta), *Act* (executa), *Assert* (verifica).
- Um teste = um comportamento.
- Teste independente e determinístico (sem depender de ordem, data atual, rede, banco...).
- Cubra: caminho feliz, bordas, entradas inválidas e exceções.
- Teste o **comportamento**, não a implementação.

---

# Aula 282 — Atualizando o JDK (15 → 16)

## Passo a passo demonstrado (Windows)

1. Baixar o JDK 16 (ou o que for mais atual na sua época) e instalar (*next, next, install*).
   - Não é obrigatório desinstalar a versão anterior: **várias JDKs podem conviver**.
2. Atualizar a variável de ambiente **`JAVA_HOME`** (variáveis de **sistema**) para a pasta do novo JDK.
3. Conferir no terminal:

```bash
java -version
javac -version
```

4. Limpar o projeto: com Maven, apagar o `target` (`mvn clean`).
5. No IntelliJ, **`F4` / Project Structure**:
   - *Project SDK* → JDK 16
   - *Language level* → 16
   - *Modules* → language level 16
6. **Maven**: ajustar a versão do compilador no `pom.xml`:

```xml
<properties>
    <maven.compiler.source>16</maven.compiler.source>
    <maven.compiler.target>16</maven.compiler.target>
</properties>
```

7. Recarregar o Maven e **rodar os testes**.

> **A grande lição:** depois de atualizar o JDK, como saber se algo quebrou? Em um projeto grande, testar classe por classe seria "um parto". Ter **testes unitários** diz em segundos se a atualização afetou algo. É uma das maiores vantagens de testar.

Dicas extras:

- Prefira versões **LTS** (8, 11, 17, 21...) em produção.
- Ao atualizar, observe warnings de APIs removidas/depreciadas.
- Se o IntelliJ ainda compilar com a versão antiga, confira **Settings → Build → Compiler → Java Compiler → Target bytecode version** (problema mostrado na aula 283).

---

# Aula 283 — Record Class

## 1. O que é

`record` é um novo tipo de classe (preview no Java 14, 2º preview no 15, **oficial no Java 16**) para modelar **dados imutáveis**. Faz o mesmo que um Lombok `@Value`: o compilador gera tudo.

## 2. Declaração

```java
public record Manga(String name, int episodes) { }
```

O compilador gera automaticamente:

- campos `private final` para cada componente;
- **construtor canônico** `Manga(String name, int episodes)`;
- **accessors** `name()` e `episodes()` — **sem prefixo `get`**;
- `equals`, `hashCode` e `toString`.

Uso:

```java
Manga m1 = new Manga("Berserk", 24);
Manga m2 = new Manga("Berserk", 24);

m1.name();             // "Berserk"
m1.equals(m2);         // true  (compara valores)
m1.hashCode() == m2.hashCode();   // true
System.out.println(m1);           // Manga[name=Berserk, episodes=24]
```

Se você compilar e inspecionar o `.class` com `javap`, verá que ele:

- estende `java.lang.Record`;
- é `final` (não pode ser estendido);
- não tem setters.

## 3. Testando um record (com JUnit)

```java
class MangaTest {
    private Manga manga1;
    private Manga manga2;

    @BeforeEach
    void setUp() {
        manga1 = new Manga("Berserk", 24);
        manga2 = new Manga("Berserk", 24);
    }

    @Test
    void accessors_ReturnData() {
        assertEquals("Berserk", manga1.name());
        assertEquals(24, manga1.episodes());
    }

    @Test
    void equals_ReturnsTrue_WhenComponentsAreEqual() {
        assertEquals(manga1, manga2);
    }

    @Test
    void hashCode_IsEqual_WhenComponentsAreEqual() {
        assertEquals(manga1.hashCode(), manga2.hashCode());
    }
}
```

## 4. O que **pode** e o que **não pode**

| Pode | Não pode |
|---|---|
| Campos **estáticos** | Campos de **instância** extras |
| Métodos (de instância e estáticos) | Estender outra classe (já estende `Record`) |
| Blocos de inicialização **estáticos** | Blocos de inicialização de instância |
| Implementar **interfaces** | Alterar os campos (são `final`) |
| Usar **generics** (`record Box<T>(T value)`) | Criar setters |
| Construtor compacto / customizado | |
| Ser declarado como **aninhado** (dentro de classe/método) | |

## 5. Construtores: canônico x compacto

**Canônico** (repete tudo — o instrutor *não recomenda* escrever assim):

```java
public record Manga(String name, int episodes) {
    public Manga(String name, int episodes) {
        this.name = name;
        this.episodes = episodes;
    }
}
```

**Compacto** (sem parênteses; ideal para **validações**):

```java
public record Manga(String name, int episodes) {
    public Manga {
        Objects.requireNonNull(name, "name can't be null");
        if (episodes < 0) throw new IllegalArgumentException("episodes must be >= 0");
        // atribuição dos campos acontece automaticamente no fim
    }
}
```

Teste da validação:

```java
@Test
void constructor_ThrowsNullPointerException_WhenNameIsNull() {
    assertThrows(NullPointerException.class, () -> new Manga(null, 10));
}
```

## 6. Garantindo que continua sendo um `record`

Por que testar isso? Porque a classe é **imutável por padrão**; se alguém trocar para classe comum, quem confia na imutabilidade pode ter problemas:

```java
@Test
void manga_IsARecord() {
    assertTrue(Manga.class.isRecord());
}
```

`Class.isRecord()` existe a partir do Java 16.

## 7. Outros usos

- `record` como substituto de DTOs, *value objects*, chaves compostas de mapas, retornos de múltiplos valores de um método.
- Pode ser declarado **local** (dentro de um método) — ótimo para agrupar resultados temporários em streams.
- Para quem conhece **Kotlin** (`data class`), a ideia é parecida.

## 8. Record x Lombok x classe comum

| | Classe comum | Lombok `@Value` | `record` |
|---|---|---|---|
| Boilerplate | muito | pouco (anotações) | nenhum |
| Dependência extra | não | sim | **não** |
| Imutável | você garante | sim | **sim** |
| Herança | sim | sim | **não** |
| Accessor | `getX()` | `getX()` | `x()` |

---

# Aula 284 — Pattern Matching for `instanceof`

## 1. O problema (antes)

Quando se trabalha com polimorfismo, é comum verificar o tipo real e **fazer cast**:

```java
Employee employee = new Developer(1, "Java");

if (employee instanceof Developer) {
    Developer developer = (Developer) employee;   // cast manual
    System.out.println(developer.getMainLanguage());
}
```

São duas etapas repetitivas: **testar** e **converter**.

## 2. A solução (Java 16, oficial)

Declare a variável **dentro** do `instanceof`:

```java
if (employee instanceof Developer developer) {
    System.out.println(developer.getMainLanguage());   // já é Developer
}
```

- Se `instanceof` for verdadeiro, a variável `developer` já está **tipada e inicializada**.
- Mesmo comportamento, menos código.
- Histórico: preview no Java 14, 2ª preview no 15, **permanente no 16**.

## 3. Exemplo completo

```java
public class Employee {
    private final int id;
    public Employee(int id) { this.id = id; }
    public int getId() { return id; }
}

public class Developer extends Employee {
    private final String mainLanguage;
    public Developer(int id, String mainLanguage) {
        super(id);
        this.mainLanguage = mainLanguage;
    }
    public String getMainLanguage() { return mainLanguage; }
}
```

Teste:

```java
@Test
void instanceOf_BindsVariable_WhenObjectIsDeveloper() {
    Employee employee = new Developer(1, "Java");
    String language = null;

    if (employee instanceof Developer developer) {
        language = developer.getMainLanguage();
    }

    assertEquals("Java", language);
}
```

## 4. Escopo da variável (*flow scoping*)

A variável só existe onde o compilador **tem certeza** de que o `instanceof` foi verdadeiro:

```java
if (obj instanceof String s && s.length() > 3) {   // ok: s existe após o &&
    ...
}

if (!(obj instanceof String s)) {
    return;                      // se não é String, sai
}
System.out.println(s.length());  // ok: aqui s existe
```

```java
if (obj instanceof String s || s.length() > 3) { }  // ERRO: s pode não existir no ||
```

## 5. Caso `null`

`null instanceof Qualquer` é sempre **falso**, então a variável de padrão nunca é inicializada com `null`. O instrutor demonstra: com `new Employee(...)` (que não é `Developer`), nenhum bloco é executado e o teste "passa direto" sem entrar no `if` — dá para provar com um *breakpoint*.

## 6. Dica de uso com `equals`

Uma aplicação muito comum:

```java
@Override
public boolean equals(Object o) {
    return o instanceof Manga other
            && name.equals(other.name)
            && episodes == other.episodes;
}
```

---

# Aula 285 — "Just run for the hug" (encerramento)

Não há conteúdo técnico. O instrutor:

- **Parabeniza** quem assistiu as 285 aulas: pouquíssimas pessoas terminam um curso desse tamanho;
- Agradece à comunidade e pede **feedback** e **comentários**;
- Convida a seguir para os próximos cursos/trilhas do canal.

Recado para você: você também chegou ao fim. O próximo passo natural é aplicar tudo em **projetos reais**, com **testes unitários** e **Git**.

Sugestões de continuidade:

1. Spring Boot (web, JPA, segurança);
2. Testes: Mockito, TestContainers;
3. Banco de dados e SQL avançado;
4. Docker e CI/CD;
5. Arquitetura (Clean, Hexagonal) e DDD.

---

# Resumo do bloco

| Tema | Ideia-chave |
|---|---|
| JUnit 5 | `@Test`, `@BeforeEach`, `Assertions`, `assertThrows` |
| Cobertura | não é sinônimo de qualidade |
| Independência | cada teste monta seu próprio estado |
| Atualizar JDK | `JAVA_HOME`, Project Structure, `pom.xml`, rodar os testes |
| `record` | dados imutáveis, `equals/hashCode/toString` automáticos, accessor sem `get` |
| Construtor compacto | validação em records |
| Pattern matching `instanceof` | `if (o instanceof Tipo t)` elimina o cast |

### Armadilhas comuns

- Inverter `esperado` e `atual` em `assertEquals`.
- Testes que dependem uns dos outros ou da ordem.
- Esquecer de atualizar o *target bytecode version* e o `maven.compiler` ao subir o JDK.
- Achar que `record` permite setters ou herança.
- Usar a variável de padrão fora do escopo em que ela é garantida.
