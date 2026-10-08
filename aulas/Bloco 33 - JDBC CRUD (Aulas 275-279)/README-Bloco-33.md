# Bloco 33 — JDBC: CRUD completo pelo console

## Aulas 275 a 279

Neste bloco você junta tudo o que aprendeu de JDBC (conexão, `PreparedStatement`, `ResultSet`) e monta um **CRUD de verdade** — *Create, Read, Update, Delete* — lendo os dados do console. Primeiro com `Producer`, depois com `Anime` (que tem relacionamento com `Producer`).

```text
275 — JDBC pt 24 — CRUD pt 01 — findByName e findAll
276 — JDBC pt 25 — CRUD pt 02 — delete
277 — JDBC pt 26 — CRUD pt 03 — save
278 — JDBC pt 27 — CRUD pt 04 — update
279 — JDBC pt 28 — CRUD pt 05 — Anime CRUD
```

Arquitetura montada no bloco:

```text
Menu (Scanner)
   ↓
Service      → conversa com o usuário (entrada/saída) e regras simples
   ↓
Repository   → só SQL / JDBC (PreparedStatement, ResultSet)
   ↓
ConnectionFactory → devolve a Connection
   ↓
Banco (MySQL)

Domain → classes Producer e Anime (imutáveis, com Builder)
```

> **Nota sobre as transcrições:** são auto-geradas e bem ruidosas. O conteúdo abaixo foi reconstruído com base no que o instrutor fez em cada aula, com o código reescrito de forma limpa.

---

# Aula 275 — CRUD pt 01 — `findByName` e `findAll`

## 1. Organização do projeto

Novo pacote (por exemplo `crud`) com subpacotes:

```text
crud/
 ├── conn/        → ConnectionFactory
 ├── dominio/     → Producer, Anime
 ├── repository/  → ProducerRepository
 ├── service/     → ProducerService
 └── test/        → CrudTest (main)
```

### `ConnectionFactory` enxuta

Copiada da aula anterior, mas **só com o método essencial** (o instrutor remove as partes não usadas para deixar tudo limpo):

```java
public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/anime_store";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }
}
```

### Domínio

```java
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Producer {
    private final Integer id;
    private final String name;
}
```

```java
@Getter
@Builder
public class Anime {
    private final Integer id;
    private final String name;
    private final int episodes;
    private final Producer producer;   // relacionamento: anime TEM um produtor
}
```

**Relacionamento:** um produtor pode ter **vários** animes; cada anime tem **um** produtor (1 → N). Para manter simples, `Producer` **não** guarda a lista de animes — só `Anime` conhece o `Producer`.

---

## 2. O Repository

Contém **apenas** acesso a dados. Retorna objetos de domínio, não imprime nada.

```java
@Log4j2
public class ProducerRepository {

    public static List<Producer> findByName(String name) {
        log.info("Finding producers by name '{}'", name);
        List<Producer> producers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                producers.add(Producer.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error while finding producers", e);
        }
        return producers;
    }

    private static PreparedStatement createPreparedStatementFindByName(Connection conn, String name)
            throws SQLException {
        String sql = "SELECT * FROM producer WHERE name LIKE ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));   // %nome%
        return ps;
    }
}
```

Detalhes importantes:

- O `LIKE` com `%...%` precisa que os `%` estejam **no valor** do parâmetro, não no SQL: `ps.setString(1, "%" + name + "%")`. (No `String.format`, `%%` vira um `%` literal.)
- Se `name` for vazio (`""`), o valor vira `%%` → casa com **tudo**. Isso é útil: "buscar vazio = listar todos". O instrutor testa e confirma.
- `findAll` pode ser só `findByName("")`.

---

## 3. O Service e o menu

O **Service** cuida da conversa com o usuário.

```java
public class ProducerService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void buildMenu(int op) {
        switch (op) {
            case 1:
                findByName();
                break;
        }
    }

    private static void findByName() {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        List<Producer> producers = ProducerRepository.findByName(name);
        for (int i = 0; i < producers.size(); i++) {
            System.out.printf("[%d] - %s%n", i, producers.get(i).getName());
        }
    }
}
```

Pontos de didática da aula:

- `System.out.println` para o "menu" (sem `log`), senão o Log4j enche o console de informação que não é para o usuário.
- A **responsabilidade** de ler o nome é do **Service**, não do Repository. O repository só recebe o `String name`.
- Indexar a lista com `for` + `[%d]` deixa a saída amigável.

### A classe de teste (menu principal)

```java
public class CrudTest {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int op;
        while (true) {
            menu();
            op = Integer.parseInt(SCANNER.nextLine());
            if (op == 0) break;
            switch (op) {
                case 1:
                    ProducerService.buildMenu(op);
                    break;
            }
        }
    }

    private static void menu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Search for producer");
        System.out.println("0. Exit");
    }
}
```

> **Armadilha clássica do `Scanner`:** misturar `nextInt()` com `nextLine()` deixa um `\n` no buffer e o `nextLine()` seguinte "pula". Por isso o instrutor usa **sempre `nextLine()`** e converte com `Integer.parseInt(...)`. Se o usuário digitar algo que não seja número, ocorre `NumberFormatException` (ele deixa sem tratar, de propósito, para simplificar).

---

# Aula 276 — CRUD pt 02 — `delete`

## 1. Qual critério usar para deletar?

Tecnicamente o certo é deletar **pelo ID** (único). Deletar por nome poderia apagar vários registros de uma vez. Por isso, a listagem passa a **mostrar o ID**:

```java
System.out.printf("[%d] - %s%n", producer.getId(), producer.getName());
```

Fluxo típico em sistemas reais:

```text
1) pesquisar (findByName) → vê a lista com IDs
2) escolher o ID
3) confirmar
4) deletar
```

## 2. Repository

```java
public static void delete(int id) {
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = createPreparedStatementDelete(conn, id)) {
        ps.execute();
        log.info("Deleted producer '{}' from the database", id);
    } catch (SQLException e) {
        log.error("Error while trying to delete producer '{}'", id, e);
    }
}

private static PreparedStatement createPreparedStatementDelete(Connection conn, Integer id)
        throws SQLException {
    String sql = "DELETE FROM producer WHERE id = ?;";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setInt(1, id);          // agora o parâmetro é INTEIRO → setInt
    return ps;
}
```

Observe: ao mudar de `String` para `int`, o setter muda de `setString` para `setInt`.

## 3. Service com confirmação

```java
private static void delete() {
    System.out.println("Type the id of the producer you want to delete");
    int id = Integer.parseInt(SCANNER.nextLine());
    System.out.println("Are you sure? Y/N");
    String choice = SCANNER.nextLine();
    if ("y".equalsIgnoreCase(choice)) {
        ProducerRepository.delete(id);
    }
}
```

Dica do instrutor sobre comparação de resposta:

| Forma | Observação |
|---|---|
| `choice.toLowerCase().startsWith("s")` | aceita "s", "sim", "S" |
| `"s".equalsIgnoreCase(choice)` | exige exatamente "s"/"S" |

E o menu ganha a opção 2 (delete). Como o switch do `buildMenu` fica `case 1 → findByName`, `case 2 → delete`.

> **Melhoria discutida:** em um sistema real, primeiro você **pesquisa**, mostra os resultados e só então pede o ID. Dá para unir `findByName` + `delete` no mesmo fluxo, mas a aula mantém tudo separado por simplicidade.

---

# Aula 277 — CRUD pt 03 — `save`

## 1. Refatorações no começo da aula

1. Como agora deletamos por **ID**, o índice `i` do `for` não é mais necessário. Usa-se o `producer.getId()` na saída (mais simples, e o usuário já vê o ID real do banco):

```java
producers.forEach(p -> System.out.printf("[%d] - %s%n", p.getId(), p.getName()));
```

2. O `for` + `System.out` vira um `forEach` com lambda ("já que estamos fazendo tudo de forma funcional").

## 2. Enhanced `switch` (Java 12+)

O IntelliJ sugere "Replace with enhanced switch". Como fica:

```java
switch (op) {
    case 1 -> findByName();
    case 2 -> delete();
    case 3 -> save();
    default -> throw new IllegalArgumentException("Not a valid option");
}
```

Vantagens:

- Sem `break` (**não existe fall-through**).
- Pode ser **expressão** que devolve valor:

```java
int i = switch (op) {
    case 1 -> 100;
    case 2, 3, 4, 5 -> 200;      // vários rótulos no mesmo case
    default -> throw new IllegalArgumentException("invalid");
};
```

- Como expressão, precisa **cobrir todos os casos** (por isso o `default`).

Histórico: introduzido em preview no Java 12, melhorado no 13 e padronizado no 14. O instrutor usa JDK 15 (configurável em *Project Structure → Language level*). O IntelliJ também ajuda: `Ctrl+Alt+O` remove imports não utilizados.

## 3. Repository `save`

Baseado no `INSERT` que já foi feito antes do bloco:

```java
public static void save(Producer producer) {
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = createPreparedStatementSave(conn, producer)) {
        ps.execute();
        log.info("Saved producer '{}'", producer.getName());
    } catch (SQLException e) {
        log.error("Error while trying to save producer '{}'", producer.getName(), e);
    }
}

private static PreparedStatement createPreparedStatementSave(Connection conn, Producer producer)
        throws SQLException {
    String sql = "INSERT INTO producer (name) VALUES (?);";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, producer.getName());     // o id é AUTO_INCREMENT, não entra
    return ps;
}
```

Passar o **objeto** (e não só o nome) facilita evoluir o código quando a tabela ganhar mais colunas.

## 4. Service

```java
private static void save() {
    System.out.println("Type the name of the producer");
    String name = SCANNER.nextLine();
    Producer producer = Producer.builder().name(name).build();
    ProducerRepository.save(producer);
}
```

> Detalhe de português/inglês que o instrutor percebe na hora: ao confirmar com "Y/N", é `y` de *yes* (e não `s` de *sim*), porque a mensagem está em inglês.

---

# Aula 278 — CRUD pt 04 — `update`

## 1. Por que o update é "mais complicado"

Para atualizar você precisa do **objeto atual** (ou ao menos do ID), e o objeto é **imutável** (só `@Getter`, sem setters). Então o fluxo é:

```text
1) pedir o ID
2) buscar o registro atual no banco (findById)
3) se não existir → avisar o usuário
4) pedir o novo valor (ou vazio para manter)
5) construir um NOVO objeto (imutável) com os valores finais
6) chamar update
```

## 2. `findById` no Repository

```java
public static Optional<Producer> findById(Integer id) {
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = createPreparedStatementFindById(conn, id);
         ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) return Optional.empty();
        return Optional.of(Producer.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build());
    } catch (SQLException e) {
        log.error("Error while finding producer by id", e);
    }
    return Optional.empty();
}
```

Como a busca é por ID (único), retorna **no máximo um** → `Optional<Producer>` em vez de `List`. O instrutor explica que `Optional` obriga quem chama a tratar o caso "não encontrou".

## 3. `update` no Repository

```java
public static void update(Producer producer) {
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = createPreparedStatementUpdate(conn, producer)) {
        ps.execute();
        log.info("Updated producer '{}'", producer.getId());
    } catch (SQLException e) {
        log.error("Error while updating producer '{}'", producer.getId(), e);
    }
}

private static PreparedStatement createPreparedStatementUpdate(Connection conn, Producer producer)
        throws SQLException {
    String sql = "UPDATE producer SET name = ? WHERE id = ?;";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, producer.getName());
    ps.setInt(2, producer.getId());
    return ps;
}
```

A **ordem** dos `?` define a posição: primeiro o `name` (1), depois o `id` (2).

## 4. Service com `Optional`

```java
private static void update() {
    System.out.println("Type the id of the object you want to update");
    Optional<Producer> producerOptional =
            ProducerRepository.findById(Integer.parseInt(SCANNER.nextLine()));
    if (producerOptional.isEmpty()) {
        System.out.println("Producer not found");
        return;
    }
    Producer producerFromDb = producerOptional.get();
    System.out.println("Producer found: " + producerFromDb);
    System.out.println("Type the new name or empty to keep the same");
    String name = SCANNER.nextLine();
    name = name.isEmpty() ? producerFromDb.getName() : name;

    Producer producerToUpdate = Producer.builder()
            .id(producerFromDb.getId())
            .name(name)
            .build();
    ProducerRepository.update(producerToUpdate);
}
```

Alternativas que o instrutor comenta:

```java
// lança exceção se não existir:
Producer p = optional.orElseThrow(() -> new IllegalArgumentException("Producer not found"));
```

Pontos-chave:

- **Objeto imutável não é alterado**: criamos um novo (`builder`) com o `id` antigo e o nome novo.
- **Ternário** para "vazio = manter": `name.isEmpty() ? atual : novo`.
- **Nunca** altere o `id` no update.

---

# Aula 279 — CRUD pt 05 — Anime CRUD

## 1. Cria o `AnimeRepository` e o `AnimeService`

Estratégia do instrutor: **copiar e adaptar** o `ProducerRepository`/`ProducerService` usando *Find/Replace* do IntelliJ (`Ctrl+R`):

- ative **Match Case** e **Words** para trocar corretamente `producer` → `anime`, `Producer` → `Anime`, `producers` → `animes`;
- depois, corrija à mão o que não for simples renomeação.

## 2. O desafio: o anime tem um `Producer`

Para montar um `Anime` você precisa também do `Producer` dele. Duas abordagens:

| Abordagem | Como |
|---|---|
| Chamar o `ProducerRepository.findById` para cada anime | simples, mas faz **N+1 consultas** |
| Fazer um **`INNER JOIN`** | **uma** consulta traz tudo |

### `INNER JOIN`

```java
String sql = """
        SELECT a.id, a.name, a.episodes, a.producer_id, p.name AS producer_name
        FROM anime a
        INNER JOIN producer p ON a.producer_id = p.id
        WHERE a.name LIKE ?;
        """;
```

Aqui aparecem duas novidades:

1. **Alias de coluna** (`p.name AS producer_name`): `anime.name` e `producer.name` têm o mesmo nome; com o alias, você diferencia no `ResultSet` (`rs.getString("producer_name")`).
2. **Text Block** (`"""`), do Java 15 (preview em 13/14): o SQL fica em várias linhas, sem `+` e sem `\n`.

```java
// Antes
String sql = "SELECT a.id, a.name, a.episodes, a.producer_id, p.name AS producer_name " +
             "FROM anime a INNER JOIN producer p ON a.producer_id = p.id " +
             "WHERE a.name LIKE ?;";
```

**Regras do text block:** a abertura `"""` precisa ser seguida de **quebra de linha**; a indentação comum a todas as linhas é removida; o fechamento define o recuo.

## 3. Montando o objeto no `ResultSet`

```java
Producer producer = Producer.builder()
        .id(rs.getInt("producer_id"))
        .name(rs.getString("producer_name"))
        .build();

Anime anime = Anime.builder()
        .id(rs.getInt("id"))
        .name(rs.getString("name"))
        .episodes(rs.getInt("episodes"))
        .producer(producer)
        .build();
```

> Veja que `producer_id` já é a coluna da própria tabela `anime` — não é preciso trazer `p.id` no `SELECT`.

## 4. `findById`, `delete`, `save`, `update` do Anime

- **`findById`**: mesmo `SELECT` com `JOIN`, trocando o `WHERE` para `a.id = ?` e devolvendo `Optional<Anime>`.
- **`delete`**: `DELETE FROM anime WHERE id = ?;`
- **`save`** (um `INSERT` com três parâmetros):

```java
String sql = "INSERT INTO anime (name, episodes, producer_id) VALUES (?, ?, ?);";
ps.setString(1, anime.getName());
ps.setInt(2, anime.getEpisodes());
ps.setInt(3, anime.getProducer().getId());
```

> O instrutor testa o `INSERT` direto no cliente SQL antes de colocar no código; as posições dos `?` seguem a ordem das colunas: **1 = name, 2 = episodes, 3 = producer_id**.

- **`update`**: o produtor de um anime **não muda** (regra de negócio da aula), então só atualiza `name` e `episodes`:

```java
String sql = "UPDATE anime SET name = ?, episodes = ? WHERE id = ?;";
```

## 5. `AnimeService`

```java
private static void save() {
    System.out.println("Type the name of the anime");
    String name = SCANNER.nextLine();
    System.out.println("Type the number of episodes");
    int episodes = Integer.parseInt(SCANNER.nextLine());
    System.out.println("Type the id of the producer");
    Integer producerId = Integer.parseInt(SCANNER.nextLine());

    Anime anime = Anime.builder()
            .name(name)
            .episodes(episodes)
            .producer(Producer.builder().id(producerId).build())   // só o id importa
            .build();
    AnimeRepository.save(anime);
}
```

No `update`, mesma ideia do Producer: busca o anime atual, pede nome e episódios (vazio mantém), cria um **novo** `Anime` com o `Producer` antigo:

```java
String name = SCANNER.nextLine();
name = name.isEmpty() ? animeFromDb.getName() : name;

String episodesInput = SCANNER.nextLine();
int episodes = episodesInput.isEmpty()
        ? animeFromDb.getEpisodes()
        : Integer.parseInt(episodesInput);
```

> **Problema que o instrutor aponta:** se você fizer `Integer.parseInt` direto e o usuário não digitar nada, vem `NumberFormatException`. Para "vazio mantém", é preciso checar `isEmpty()` **antes** de converter (como acima).

## 6. Menu em dois níveis

O menu principal passa a escolher **a entidade**, e cada Service tem seu próprio submenu:

```text
Type the number of your operation
1. Producer
2. Anime
0. Exit
```

```java
while (true) {
    menu();
    int op = Integer.parseInt(SCANNER.nextLine());
    if (op == 0) break;
    switch (op) {
        case 1 -> ProducerService.buildMenu();   // submenu: 1 search, 2 delete, 3 save, 4 update, 9 back
        case 2 -> AnimeService.buildMenu();
    }
}
```

Cada `buildMenu()` do Service tem um laço próprio: mostra as opções, lê, e a opção **9** volta ao menu anterior (`return`).

Bug que apareceu e foi corrigido na aula: a ordem do `case` e da leitura do submenu estava errada (uma opção digitada era interpretada no nível errado). Moral: **desenhe o fluxo dos menus antes de codar**.

## 7. Testando o CRUD completo

O instrutor executa o ciclo:

1. cadastrar anime (ex.: *Overlord*, 24 episódios, produtor existente);
2. listar todos;
3. deletar e depois procurar o ID (não acha mais → o `Optional` vazio protege);
4. atualizar (nome e episódios) e conferir no `findByName`.

Impressão final do `findByName` do anime:

```java
animes.forEach(a -> System.out.printf("[%d] - %s %d%n", a.getId(), a.getName(), a.getEpisodes()));
```

> Fechamento da aula: "tem muita coisa que poderia melhorar (várias validações), mas você já tem um CRUD 100% funcional usando JDBC em vez de dados em memória". Em uma aplicação real, a saída seria uma tabela com cabeçalho — aqui basta o `printf`.

---

# Resumo do bloco

| Operação | SQL | Método JDBC | Retorno do Repository |
|---|---|---|---|
| Create | `INSERT ... VALUES (?, ...)` | `ps.execute()` | `void` |
| Read (lista) | `SELECT ... LIKE ?` | `ps.executeQuery()` | `List<T>` |
| Read (um) | `SELECT ... WHERE id = ?` | `ps.executeQuery()` | `Optional<T>` |
| Update | `UPDATE ... SET ... WHERE id = ?` | `ps.execute()` | `void` |
| Delete | `DELETE ... WHERE id = ?` | `ps.execute()` | `void` |

Boas práticas vistas:

- Repository só faz SQL; Service só fala com o usuário.
- `PreparedStatement` + try-with-resources sempre.
- Objetos imutáveis: update = construir um novo objeto.
- `Optional` para "pode não existir".
- `INNER JOIN` com alias para evitar N+1 consultas.
- Enhanced `switch` e text block para código mais limpo.

### Armadilhas comuns

- Esquecer o `WHERE` no `UPDATE`/`DELETE` (afeta a tabela inteira!).
- `NumberFormatException` ao converter entrada vazia.
- Misturar `nextInt()` com `nextLine()` no `Scanner`.
- Colunas com o mesmo nome em um `JOIN` sem alias.
- Esquecer que `LIKE` precisa dos `%` dentro do parâmetro.
