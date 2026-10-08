# Bloco 31 — JDBC: `UPDATE`, `ResultSet` e Metadados

## Aulas 260 a 267

No bloco anterior você **escreveu** no banco (`INSERT`, `DELETE`). Agora vai **atualizar** registros e, principalmente, **ler** dados com o `ResultSet`, o objeto que representa o resultado de uma consulta. Você também verá os **metadados** (informações sobre os dados e sobre o driver) e como usar um `ResultSet` "rolável" e "atualizável" para navegar, alterar, inserir e apagar linhas **sem escrever SQL de escrita**.

As aulas deste bloco são:

```text
260 — JDBC pt 09 — Atualizando dados com Statement
261 — JDBC pt 10 — Buscando dados com ResultSet pt 01 — findAll
262 — JDBC pt 11 — Buscando dados com ResultSet pt 02 — findByName
263 — JDBC pt 12 — ResultSetMetaData
264 — JDBC pt 13 — DatabaseMetaData
265 — JDBC pt 14 — ResultSet TYPE_SCROLL_INSENSITIVE
266 — JDBC pt 15 — Atualizando registros com ResultSet
267 — JDBC pt 16 — Inserindo e deletando registros com ResultSet
```

Revisão da arquitetura em camadas (do bloco anterior):

```text
Main/Test  →  ProducerService  →  ProducerRepository  →  ConnectionFactory  →  MySQL
(apresentação)  (regras)            (SQL / JDBC)            (conexão)
```

Pré-requisito: ambiente do bloco 30 rodando (`docker-compose up`, schema `anime_store`, Maven, Lombok, Log4j2).

---

# Aula 260 — JDBC pt 09 — Atualizando dados com `Statement`

## 1. O `UPDATE` e a regra de ouro: **sempre `WHERE`**

```sql
UPDATE `anime_store`.`producer` SET `name` = 'Mad House' WHERE `id` = 1;
```

⚠️ **Sem `WHERE`, todas as linhas da tabela são atualizadas.** O instrutor conta histórias reais de empresas (inclusive órgãos públicos) que pararam por um `UPDATE` ou `DELETE` sem `WHERE`. Dica: ao escrever um `UPDATE`/`DELETE` à mão, **escreva o `WHERE` primeiro**.

O que atualizar: normalmente **nunca** se altera o `id` (chave primária). Aqui só o `name` faz sentido.

## 2. Repositório

```java
@Log4j2
public class ProducerRepository {

    public static void update(Producer producer) {
        String sql = String.format("UPDATE `anime_store`.`producer` SET `name` = '%s' WHERE (`id` = '%d');",
                producer.getName(), producer.getId());

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Updated producer '{}', rows affected '{}'", producer.getId(), rowsAffected);

        } catch (SQLException e) {
            log.error("Error while trying to update producer '{}'", producer.getId(), e);
        }
    }
}
```

Boa prática de log (o instrutor explica o conceito de **PII — Personally Identifiable Information**): evite colocar nos logs dados que possam **identificar uma pessoa** (nome, CPF, e-mail). Registrar o `id` costuma ser suficiente.

## 3. Serviço com validação

Uma validação de `id` já existia no `delete`; agora o `update` também precisa. Para não repetir, extraia para um método:

```java
public class ProducerService {

    public static void update(Producer producer) {
        requireValidId(producer.getId());
        ProducerRepository.update(producer);
    }

    public static void delete(Integer id) {
        requireValidId(id);
        ProducerRepository.delete(id);
    }

    private static void requireValidId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid value for id");
        }
    }
}
```

`id == null` precisa ser verificado **antes** de `<= 0` (senão `NullPointerException` ao fazer unboxing).

## 4. Testando

```java
Producer producer = Producer.builder().id(1).name("MAD HOUSE").build();
ProducerService.update(producer);
```

Depois confira no Workbench com `SELECT * FROM producer`.

## O que você precisa dominar (Aula 260)

- `UPDATE ... SET ... WHERE` e o perigo do `WHERE` ausente.
- Repositório com `executeUpdate`.
- Evitar PII nos logs.
- Reutilizar validações no serviço.

---

# Aula 261 — JDBC pt 10 — `ResultSet`: `findAll`

## 1. `executeQuery` e `ResultSet`

Para **consultar** (`SELECT`) usa-se `executeQuery`, que devolve um **`ResultSet`**:

```java
String sql = "SELECT id, name FROM anime_store.producer;";

try (Connection conn = ConnectionFactory.getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {      // ResultSet também é AutoCloseable → no try-with-resources
    ...
}
```

Boas práticas do instrutor:

- Evite `SELECT *`: **liste as colunas** que precisa (menos dados trafegando, menos acoplamento).
- Num `SELECT` sem filtro, cuidado em produção (traz **todas** as linhas).

---

## 2. Como o `ResultSet` funciona: o cursor

O `ResultSet` mantém um **cursor** (ponteiro) que começa **antes da primeira linha**:

```text
        ┌───────────────┐
cursor→ │  (antes da 1ª)│   ← posição inicial
        │ 1 | Mad House │
        │ 2 | Ghibli    │
        │ 3 | Bones     │
        │ (depois da última) │
        └───────────────┘
rs.next() → avança uma linha; devolve true se existe, false se acabou
```

Por isso o padrão é:

```java
while (rs.next()) {
    // lê a linha atual
}
```

(Parecido com o `Iterator`: `hasNext`+`next` num só método.)

---

## 3. Lendo as colunas: `getXxx`

Para cada tipo existe um getter, que aceita o **índice** da coluna (começa em **1**, não em 0!) ou o **nome**:

```java
int id = rs.getInt("id");          // por nome (mais legível — recomendado)
String name = rs.getString("name");
// equivalentes por índice:
int id2 = rs.getInt(1);
String name2 = rs.getString(2);
```

Mapeamento de tipos (principais):

| Tipo SQL (MySQL) | Método Java | Tipo Java |
|---|---|---|
| `INT` | `getInt` | `int` |
| `BIGINT` | `getLong` | `long` |
| `VARCHAR/TEXT` | `getString` | `String` |
| `DOUBLE/DECIMAL` | `getDouble` / `getBigDecimal` | `double` / `BigDecimal` |
| `BOOLEAN/TINYINT(1)` | `getBoolean` | `boolean` |
| `DATE` | `getDate` / `getObject(col, LocalDate.class)` | `Date` / `LocalDate` |
| `DATETIME/TIMESTAMP` | `getTimestamp` | `Timestamp` |

⚠️ Para colunas que aceitam `NULL`: `getInt` devolve `0` quando é nulo — use `rs.wasNull()` logo depois se precisar distinguir.

---

## 4. `findAll` completo

```java
public static List<Producer> findAll() {
    log.info("Finding all producers");
    String sql = "SELECT id, name FROM anime_store.producer;";
    List<Producer> producers = new ArrayList<>();

    try (Connection conn = ConnectionFactory.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Producer producer = Producer.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build();
            producers.add(producer);
        }
    } catch (SQLException e) {
        log.error("Error while trying to find all producers", e);
    }
    return producers;                      // ⬅ lista vazia, nunca null
}
```

Cada **linha** do banco vira **um objeto** Java. É o conceito de **mapeamento objeto-relacional** (que frameworks como o JPA/Hibernate automatizam).

Pontos de design destacados:

- A `List` é criada **fora** do `try`, para poder ser devolvida depois.
- **Devolva lista vazia, não `null`**: quem chama não precisa de `if (x != null)`.
- O serviço só repassa: `ProducerService.findAll()`.

Teste:

```java
List<Producer> producers = ProducerService.findAll();
log.info("Producers found {}", producers);
```

## O que você precisa dominar (Aula 261)

- `executeQuery` devolve `ResultSet`; cursor e `next()`.
- `getInt`/`getString` por nome ou índice (índice começa em 1).
- Mapear linha → objeto e montar `List`.
- Retornar lista vazia, nunca `null`.
- `ResultSet` no try-with-resources.

---

# Aula 262 — JDBC pt 11 — `ResultSet`: `findByName`

## 1. Filtrando com `LIKE`

```sql
SELECT id, name FROM producer WHERE name LIKE '%mad%';
```

- `%` = "qualquer sequência de caracteres".
- `'%mad%'` → contém "mad". `'mad%'` → começa com "mad". `'%mad'` → termina com "mad".
- No MySQL o `LIKE` costuma ser *case-insensitive* (depende da *collation*).

## 2. Repositório

```java
public static List<Producer> findByName(String name) {
    String sql = String.format("SELECT id, name FROM anime_store.producer WHERE name LIKE '%%%s%%';", name);
    ...                                          // mesmo corpo do findAll
}
```

Atenção ao `String.format`: o caractere `%` é especial; para escrever um `%` literal é preciso `%%`. Por isso `'%%%s%%'` = `'%` + valor + `%'`. (O instrutor testa o escape até acertar.)

Serviço:

```java
public static List<Producer> findByName(String name) {
    return ProducerRepository.findByName(name);
}
```

Teste: buscar por `"mad"` devolve só o "Mad House".

## 3. Evitando duplicação: o problema aparece

`findAll` e `findByName` são **quase idênticos** (só muda o SQL). Truque simples:

```java
public static List<Producer> findAll() {
    return findByName("");           // LIKE '%%' casa com tudo
}
```

Funciona, e economiza muito código. O instrutor lembra que outras formas existem:

- extrair o laço de mapeamento para um método privado `mapResultSetToList(rs)`;
- guardar os SQLs em constantes, ou até em **arquivo de propriedades**;
- usar **JdbcTemplate** / ORMs (Spring, Hibernate) que resolvem isso.

## 4. Mas ainda há o problema da concatenação…

`findByName(String name)` monta o SQL **concatenando o texto do usuário** → vulnerável a **SQL Injection**. Se `name` for `' OR '1'='1`, a consulta devolve tudo. O instrutor menciona: "como é que a gente faz para colocar esse parâmetro sem ficar concatenando? Isso será visto adiante" (aula 268: `PreparedStatement`).

## O que você precisa dominar (Aula 262)

- `WHERE ... LIKE` e curingas `%`.
- `%%` dentro de `String.format`.
- Reaproveitar `findAll` via `findByName("")`.
- Reconhecer o risco de concatenar parâmetros.

---

# Aula 263 — JDBC pt 12 — `ResultSetMetaData`

## 1. Metadados: dados sobre os dados

`rs.getMetaData()` devolve um `ResultSetMetaData`, que descreve a **estrutura** do resultado: quantas colunas, nomes, tipos, tamanhos. Útil para ferramentas genéricas, relatórios dinâmicos, "exportar qualquer consulta", quando você **não conhece de antemão** as colunas.

```java
public static void showTypeMetaData() {
    String sql = "SELECT * FROM anime_store.producer;";

    try (Connection conn = ConnectionFactory.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        log.info("Columns count: {}", columnCount);

        for (int i = 1; i <= columnCount; i++) {             // colunas começam em 1!
            log.info("Table name: {}", meta.getTableName(i));
            log.info("Column name: {}", meta.getColumnName(i));
            log.info("Column size: {}", meta.getColumnDisplaySize(i));
            log.info("Column type: {}", meta.getColumnTypeName(i));
        }
    } catch (SQLException e) {
        log.error("Error", e);
    }
}
```

Saída típica para a tabela `producer`:

```text
Columns count: 2
Table name: producer   Column name: id    Column size: 10   Column type: INT
Table name: producer   Column name: name  Column size: 255  Column type: VARCHAR
```

### Métodos úteis de `ResultSetMetaData`

| Método | Devolve |
|---|---|
| `getColumnCount()` | número de colunas |
| `getColumnName(i)` / `getColumnLabel(i)` | nome real / rótulo (alias do `AS`) |
| `getTableName(i)` | nome da tabela de origem |
| `getColumnType(i)` | código numérico (constantes de `java.sql.Types`, ex.: `4` = `INTEGER`) |
| `getColumnTypeName(i)` | nome do tipo (`INT`, `VARCHAR`) |
| `getColumnDisplaySize(i)` | tamanho para exibição |
| `isNullable(i)` / `isAutoIncrement(i)` | propriedades da coluna |

O instrutor se perguntou o que significava o `4` do `getColumnType`: é a constante `Types.INTEGER`. Prefira `getColumnTypeName` para ficar legível.

### Quando usar?

Relatórios que juntam várias tabelas e dão colunas diferentes a cada consulta; ferramentas administrativas; exportação genérica (CSV/Excel) de qualquer `SELECT`.

## O que você precisa dominar (Aula 263)

- `ResultSetMetaData` e `getMetaData()`.
- Percorrer colunas de 1 até `getColumnCount()`.
- `getColumnName`, `getTableName`, `getColumnTypeName`.
- Quando metadados são úteis.

---

# Aula 264 — JDBC pt 13 — `DatabaseMetaData`

## 1. O que o `ResultSet` pode (ou não) fazer depende do **driver**

O `ResultSet` é mais poderoso do que "ler linha a linha":

- pode **navegar** em qualquer direção;
- pode **atualizar** o banco diretamente;
- pode (ou não) **refletir mudanças** feitas por outros enquanto você o usa.

Mas nem todo driver suporta tudo. Para descobrir, usa-se o **`DatabaseMetaData`** da **conexão**:

```java
public static void checkDriverStatus() {
    try (Connection conn = ConnectionFactory.getConnection()) {
        DatabaseMetaData dbMeta = conn.getMetaData();

        if (dbMeta.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)) {
            log.info("Supports TYPE_FORWARD_ONLY");
            if (dbMeta.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
                log.info("... and supports CONCUR_UPDATABLE");
            }
        }
        if (dbMeta.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)) {
            log.info("Supports TYPE_SCROLL_INSENSITIVE");
            if (dbMeta.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                log.info("... and supports CONCUR_UPDATABLE");
            }
        }
        if (dbMeta.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE)) {
            log.info("Supports TYPE_SCROLL_SENSITIVE");
        }
    } catch (SQLException e) { ... }
}
```

---

## 2. As duas dimensões de um `ResultSet`

### a) **Tipo** (navegação e sensibilidade a mudanças)

| Constante | Navegação | Enxerga mudanças feitas no banco por outros? |
|---|---|---|
| `TYPE_FORWARD_ONLY` (padrão) | só **para frente** (`next`) | — |
| `TYPE_SCROLL_INSENSITIVE` | **qualquer direção** (frente, trás, posição absoluta/relativa) | **não**: é um "retrato" (snapshot) |
| `TYPE_SCROLL_SENSITIVE` | qualquer direção | **sim**: reflete alterações em tempo real |

Explicando "insensitive": ao fazer a consulta, o resultado é copiado para a memória. Se alguém alterar o banco enquanto você navega, **você não vê**. "Sensitive" mostraria — mas é muito difícil de implementar e **poucos drivers suportam** (o MySQL não suporta de verdade).

### b) **Concorrência** (alterabilidade)

| Constante | Significado |
|---|---|
| `CONCUR_READ_ONLY` (padrão) | só leitura |
| `CONCUR_UPDATABLE` | permite **alterar** o banco através do próprio `ResultSet` (`updateXxx`, `insertRow`, `deleteRow`) |

Resultado no MySQL (exemplo da aula): suporta `FORWARD_ONLY`, suporta `SCROLL_INSENSITIVE`, suporta `CONCUR_UPDATABLE` nos dois, **não** suporta `SCROLL_SENSITIVE`.

## 3. Como pedir esse tipo de `ResultSet`

O tipo e a concorrência são definidos **ao criar o `Statement`**:

```java
Statement stmt = conn.createStatement(
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_UPDATABLE);
```

Sem esses argumentos você tem um `ResultSet` padrão (só avança, só leitura).

> Dica de organização: o instrutor renomeia métodos para o padrão `camelCase` de forma consistente (a IDE renomeia em todos os lugares com `Shift+F6`).

## O que você precisa dominar (Aula 264)

- `DatabaseMetaData` e `connection.getMetaData()`.
- Tipos: `FORWARD_ONLY`, `SCROLL_INSENSITIVE`, `SCROLL_SENSITIVE`.
- Concorrência: `READ_ONLY` x `UPDATABLE`.
- `createStatement(tipo, concorrência)`.
- Significado de "sensível/insensível".

---

# Aula 265 — JDBC pt 14 — `ResultSet` rolável (`TYPE_SCROLL_INSENSITIVE`)

## 1. Navegando livremente

```java
public static void showDriverMetaData() { ... }       // aula anterior

public static void testTypeScroll() {
    String sql = "SELECT id, name FROM anime_store.producer;";

    try (Connection conn = ConnectionFactory.getConnection();
         Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = stmt.executeQuery(sql)) {

        // vai direto para a última linha
        log.info("Last row? {}", rs.last());                        // true se existe
        log.info("Row number {}", rs.getRow());                     // número da linha atual (3, por exemplo)
        log.info(Producer.builder().id(rs.getInt("id")).name(rs.getString("name")).build());

        // vai para a primeira
        log.info("First row? {}", rs.first());
        log.info("Row number {}", rs.getRow());                     // 1

        // linha absoluta
        log.info("Absolute 2 {}", rs.absolute(2));                  // vai para a linha 2

        // linha relativa
        log.info("Relative -1 {}", rs.relative(-1));                // volta uma (agora linha 1)

        // verificações (não movem o cursor)
        log.info("Is last? {}", rs.isLast());
        log.info("Is first? {}", rs.isFirst());
        log.info("Is before first? {}", rs.isBeforeFirst());
        log.info("Is after last? {}", rs.isAfterLast());

        // de baixo para cima
        rs.afterLast();                                             // posiciona DEPOIS da última
        while (rs.previous()) {
            log.info(rs.getString("name"));
        }
    } catch (SQLException e) { ... }
}
```

### Tabela de métodos de navegação

| Método | Move o cursor para... |
|---|---|
| `next()` | próxima linha |
| `previous()` | linha anterior |
| `first()` / `last()` | primeira / última linha |
| `beforeFirst()` / `afterLast()` | antes da primeira / depois da última |
| `absolute(n)` | linha `n` (positiva: do começo; negativa: do fim, `absolute(-1)` = última) |
| `relative(n)` | `n` linhas a partir da atual (`-1` volta uma) |
| `getRow()` | **consulta** o número da linha atual (não move) |
| `isFirst()`, `isLast()`, `isBeforeFirst()`, `isAfterLast()` | **consultam** a posição (não movem) |

Detalhes importantes:

- Os métodos de movimento devolvem `boolean` (existe a linha?). Se `absolute(10)` for além do fim, devolve `false` e o cursor vai para "depois da última".
- Em `TYPE_FORWARD_ONLY`, qualquer um desses (menos `next`) lança `SQLException`.
- Se o cursor já está na última linha e você chama `rs.next()`, ele passa para "depois da última" (`isAfterLast()` = `true`) — e a leitura de colunas aí lança exceção.
- A ordem dos dados é a do `SELECT`; para uma ordem determinada, use `ORDER BY`. (Para "o último pelo `id`", `ORDER BY id DESC`.)

## 2. Cuidado de desempenho

`TYPE_SCROLL_INSENSITIVE` costuma **carregar todo o resultado na memória**. Para consultas enormes, prefira `FORWARD_ONLY` com paginação (`LIMIT/OFFSET`).

## O que você precisa dominar (Aula 265)

- Criar `Statement` rolável.
- `first`, `last`, `absolute`, `relative`, `previous`, `afterLast`, `getRow`.
- Métodos `isXxx` que só consultam.
- Custo de memória.

---

# Aula 266 — JDBC pt 15 — Atualizando registros com `ResultSet`

## 1. A ideia

Com `CONCUR_UPDATABLE` você **altera o banco pelo próprio `ResultSet`**, sem escrever `UPDATE`. Útil quando você já está percorrendo os dados e quer ajustar linhas durante a navegação.

Cenário da aula: deixar todos os nomes em **maiúsculas** (ou "do jeito certo").

## 2. Passos

```java
public static void updateNamesToLowerCase() {
    String sql = "SELECT id, name FROM anime_store.producer;";

    try (Connection conn = ConnectionFactory.getConnection();
         Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            String nameInUpper = rs.getString("name").toUpperCase();
            rs.updateString("name", nameInUpper);        // 1) altera o valor na linha atual (EM MEMÓRIA)
            rs.updateRow();                              // 2) CONFIRMA: envia ao banco
        }
    } catch (SQLException e) { ... }
}
```

### O erro clássico

> O instrutor mostrou: **só chamar `updateString` NÃO grava no banco.** Sem `updateRow()` a alteração fica apenas no objeto `ResultSet`; ao fechar, se perde.

| Método | O que faz |
|---|---|
| `rs.updateString("coluna", valor)` / `updateInt` / `updateXxx` | prepara a alteração (memória) |
| `rs.updateRow()` | **grava** a linha atual no banco |
| `rs.cancelRowUpdates()` | **descarta** as alterações pendentes da linha atual (só vale **antes** de `updateRow`) |

> Para "desfazer", use `cancelRowUpdates()` — não tente reler o valor antigo e regravar. Depois do `updateRow`, não há como cancelar (só fazendo outro update, ou com transações, bloco 33).

### Requisitos para o `ResultSet` ser atualizável (MySQL)

- Consulta sobre **uma única tabela**.
- Deve incluir a **chave primária** (`id`) no `SELECT`.
- Sem `GROUP BY`, `DISTINCT`, funções agregadas etc.
- `Statement` criado com `CONCUR_UPDATABLE`.

Se não atender, o `updateRow()` lança `SQLException` (ou o `ResultSet` vira somente leitura).

### Combinando com `findByName`

O instrutor também evolui para: "procure por nome (LIKE) e, para cada linha encontrada, atualize o nome para maiúsculas" — a navegação e a alteração acontecem no **mesmo** `ResultSet`.

## 3. Vantagens e desvantagens

| ✅ | ❌ |
|---|---|
| Sem SQL de escrita | Mantém a conexão e a leitura abertas durante todas as alterações |
| Simples para ajustes em massa | Menos controle e **pior desempenho** do que um `UPDATE` direto com `WHERE` (que atualiza tudo no servidor) |
| | Depende do suporte do driver |

Regra prática: para alterar muitos registros com a mesma regra, um único `UPDATE ... WHERE` é melhor; o `ResultSet` atualizável serve quando a decisão é **linha a linha**.

## O que você precisa dominar (Aula 266)

- `updateXxx` + `updateRow()`.
- `cancelRowUpdates()` para desfazer antes de gravar.
- Requisitos de um `ResultSet` atualizável.
- Quando usar e quando preferir `UPDATE` SQL.

---

# Aula 267 — JDBC pt 16 — Inserindo e deletando com `ResultSet`

## 1. Inserindo: a "linha de inserção"

O `ResultSet` atualizável tem uma **linha especial (temporária)** para montar um novo registro antes de gravá-lo:

```java
public static void insertNewProducerIfNotExists(String name) {
    String sql = String.format("SELECT id, name FROM anime_store.producer WHERE name = '%s';", name);

    try (Connection conn = ConnectionFactory.getConnection();
         Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = stmt.executeQuery(sql)) {

        if (rs.next()) {                       // já existe → nada a fazer
            return;
        }

        rs.moveToInsertRow();                  // 1) vai para a linha de inserção
        rs.updateString("name", name);         // 2) preenche as colunas (o id é AUTO_INCREMENT)
        rs.insertRow();                        // 3) grava no banco

        rs.beforeFirst();                      // 4) volta o cursor para ler o novo registro
        rs.next();
        Producer producer = Producer.builder()
                .id(rs.getInt("id"))           // o id gerado pelo banco aparece aqui
                .name(rs.getString("name"))
                .build();
        log.info("Inserted {}", producer);

    } catch (SQLException e) { ... }
}
```

Sequência essencial:

```text
moveToInsertRow()   →   updateXxx(...)   →   insertRow()   →   (moveToCurrentRow())
```

- `moveToCurrentRow()` volta o cursor à linha em que ele estava antes de ir para a linha de inserção.
- Para ler o registro recém-inserido (com o `id` gerado), reposicione o cursor (`beforeFirst()` + `next()`) — o instrutor extrai isso para métodos pequenos (`insertNewProducer`, `getProducer`) usando *Extract Method*, deixando cada função com uma única responsabilidade.

> A consulta usa `WHERE name = ...` — se o nome já existe, `rs.next()` devolve `true` e o método sai (comportamento "inserir se não existir").

## 2. Deletando: `deleteRow()`

```java
public static void deleteProducersByName(String name) {
    String sql = String.format("SELECT id, name FROM anime_store.producer WHERE name LIKE '%%%s%%';", name);

    try (Connection conn = ConnectionFactory.getConnection();
         Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            log.info("Deleting '{}'", rs.getString("name"));
            rs.deleteRow();                    // apaga a linha atual DIRETAMENTE no banco (não precisa de commit extra)
        }
    } catch (SQLException e) { ... }
}
```

O instrutor testa apagando todos os produtores cujo nome contém um trecho (ex.: "pictures") e confere no Workbench.

| Operação | `ResultSet` atualizável |
|---|---|
| Alterar | `updateXxx` + `updateRow` |
| Inserir | `moveToInsertRow` + `updateXxx` + `insertRow` |
| Apagar | `deleteRow` |
| Descartar alteração pendente | `cancelRowUpdates` |

## 3. Resumo do bloco: duas formas de escrever no banco

| | `Statement.executeUpdate(sql)` | `ResultSet` atualizável |
|---|---|---|
| Escreve SQL de escrita? | **sim** | **não** |
| Bom para | operações pontuais e em massa | ajustes linha a linha enquanto navega |
| Desempenho em lote | melhor | pior |
| Portabilidade | alta | depende do driver |

Na prática do dia a dia, o mercado usa `PreparedStatement` (bloco seguinte) ou ORMs. Mas conhecer o `ResultSet` atualizável ajuda a entender o JDBC como um todo.

## O que você precisa dominar (Aula 267)

- `moveToInsertRow`, `updateXxx`, `insertRow`.
- `deleteRow`.
- Voltar o cursor para ler o registro inserido.
- Comparar `executeUpdate` com `ResultSet` atualizável.

---

# Mapa mental do bloco

```text
Statement
├── executeUpdate(sql)  → INSERT/UPDATE/DELETE        → int linhas afetadas
└── executeQuery(sql)   → SELECT                      → ResultSet

ResultSet
├── Ler:     next() · getInt/getString/getXxx(nome ou índice 1-based)
├── Mapear:  linha → objeto  → List (vazia, nunca null)
├── Tipo:    FORWARD_ONLY (padrão) · SCROLL_INSENSITIVE · SCROLL_SENSITIVE
├── Concor.: READ_ONLY (padrão) · UPDATABLE
├── Navegar: first · last · previous · absolute(n) · relative(n) · beforeFirst · afterLast · getRow · isXxx
├── Alterar: updateXxx + updateRow · cancelRowUpdates
├── Inserir: moveToInsertRow + updateXxx + insertRow
└── Apagar:  deleteRow

Metadados
├── ResultSetMetaData  (rs.getMetaData())     → colunas, nomes, tipos
└── DatabaseMetaData   (conn.getMetaData())   → o que o DRIVER suporta
```

# Cola de bolso

| Preciso... | Use |
|---|---|
| Atualizar | `UPDATE ... SET ... WHERE id = ?` (SEMPRE com WHERE) |
| Listar | `executeQuery` + `while (rs.next())` |
| Filtrar por trecho | `WHERE col LIKE '%x%'` |
| Saber quantas colunas | `rs.getMetaData().getColumnCount()` |
| Saber se o driver suporta rolagem | `conn.getMetaData().supportsResultSetType(...)` |
| Navegar para qualquer linha | `Statement` com `TYPE_SCROLL_INSENSITIVE` |
| Alterar durante a navegação | `CONCUR_UPDATABLE` + `updateXxx` + `updateRow` |
| Inserir via ResultSet | `moveToInsertRow` → `updateXxx` → `insertRow` |
| Apagar via ResultSet | `deleteRow` |
