# Bloco 32 — JDBC: `PreparedStatement`, `CallableStatement`, `RowSet` e Transações

## Aulas 268 a 274

Este bloco corrige o grande problema dos blocos anteriores — a **concatenação de SQL** (vulnerável a *SQL Injection*) — e apresenta as demais ferramentas de JDBC: **`PreparedStatement`** (o que se usa de verdade), **`CallableStatement`** (procedures), **`RowSet`** (conectado e desconectado) e **transações**.

As aulas deste bloco são:

```text
268 — JDBC pt 17 — PreparedStatement pt 01
269 — JDBC pt 18 — PreparedStatement pt 02
270 — JDBC pt 19 — CallableStatement
271 — JDBC pt 20 — Connected RowSet — JdbcRowSet pt 01
272 — JDBC pt 21 — Connected RowSet — JdbcRowSet pt 02
273 — JDBC pt 22 — Disconnected RowSet — CachedRowSet
274 — JDBC pt 23 — Transação
```

Mapa das ferramentas:

```text
Statement          → SQL fixo, concatenado (inseguro)
PreparedStatement  → SQL com ? (seguro e rápido)   ← uso padrão do dia a dia
CallableStatement  → chama stored procedures / functions
RowSet             → ResultSet "turbinado" (JavaBean)
   ├── JdbcRowSet    → conectado
   └── CachedRowSet  → desconectado (trabalha em memória)
Transação          → tudo ou nada (commit / rollback)
```

---

# Aula 268 — JDBC pt 17 — `PreparedStatement` (parte 1)

## 1. O que é

`PreparedStatement` é um `Statement` **pré-compilado**: você envia o SQL **com marcadores `?`** (parâmetros) **uma vez**, e depois informa os valores. Vantagens:

1. **Segurança**: os valores são enviados **separados** do SQL e tratados sempre como *dados* — **impede SQL Injection**.
2. **Desempenho**: o banco pode **compilar/planejar** o SQL uma vez e reaproveitá-lo.
3. **Legibilidade**: nada de `String.format` com `%s` e aspas.
4. Trata corretamente tipos, apóstrofos, acentos e datas.

---

## 2. Demonstrando o problema (SQL Injection) com `Statement`

Código vulnerável:

```java
String sql = String.format("SELECT id, name FROM producer WHERE name LIKE '%%%s%%';", name);
```

Se o usuário digitar:

```text
x' OR 'x'='x
```

o SQL final vira:

```sql
SELECT id, name FROM producer WHERE name LIKE '%x' OR 'x'='x%';
```

A condição `'x'='x'` é **sempre verdadeira**, então **todos** os registros são devolvidos. O instrutor reproduz o ataque e cita que grandes empresas (como o Yahoo) já tiveram vazamentos por bobagens desse tipo. Em um login, o mesmo truque permite **entrar sem senha**; com comandos mais perigosos, apagar tabelas.

---

## 3. Corrigindo com `PreparedStatement`

```java
public static List<Producer> findByName(String name) {
    String sql = "SELECT id, name FROM anime_store.producer WHERE name LIKE ?;";
    List<Producer> producers = new ArrayList<>();

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + name + "%");          // 1) substitui o primeiro ? (índice começa em 1!)

        try (ResultSet rs = ps.executeQuery()) {     // 2) executa (SEM passar o SQL de novo)
            while (rs.next()) {
                producers.add(Producer.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build());
            }
        }
    } catch (SQLException e) {
        log.error("Error while trying to find producers", e);
    }
    return producers;
}
```

### Pontos importantes

| Ponto | Explicação |
|---|---|
| `conn.prepareStatement(sql)` | **recebe o SQL** já na criação |
| `?` | marcador de parâmetro — **sem aspas** em volta |
| `ps.setString(1, valor)` | índice do `?` (começa em **1**) e o valor |
| `ps.executeQuery()` | **sem argumento** (o SQL já está preparado) — passar o SQL aqui seria um erro |
| `LIKE` | o `%` vai **dentro do valor**, não do SQL: `"%" + name + "%"` |
| Fechar o `ResultSet` | o `ResultSet` precisa ser criado **depois** que os parâmetros foram definidos, então não cabe no mesmo try-with-resources do `PreparedStatement` — abra um **try aninhado** ou, melhor, extraia um método |

Quem é seguro? Quando alguém envia `x' OR 'x'='x`, o banco procura literalmente por um produtor cujo nome **contém** esse texto — e não acha nada. O ataque deixa de funcionar.

Mesmo teste do instrutor: `ps.setString(1, "%" + "x' OR 'x'='x" + "%")` → resultado vazio.

---

## 4. Organizando: extrair a criação do `PreparedStatement`

O código fica aninhado e feio. O instrutor extrai um método privado:

```java
private static PreparedStatement createPreparedStatementFindByName(Connection conn, String name) throws SQLException {
    String sql = "SELECT id, name FROM anime_store.producer WHERE name LIKE ?;";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, "%" + name + "%");
    return ps;
}
```

E o uso:

```java
try (Connection conn = ConnectionFactory.getConnection();
     PreparedStatement ps = createPreparedStatementFindByName(conn, name);
     ResultSet rs = ps.executeQuery()) {          // agora tudo cabe no mesmo try-with-resources
    while (rs.next()) { ... }
}
```

Ganhos: todos os recursos num único `try-with-resources`; a criação do statement (SQL + parâmetros) fica isolada e testável; o corpo do `try` fica limpo.

## 5. Tipos de `setXxx`

| Método | Para |
|---|---|
| `setString(i, v)` | `VARCHAR`, `CHAR`, `TEXT` |
| `setInt(i, v)` / `setLong(i, v)` | inteiros |
| `setDouble(i, v)` / `setBigDecimal(i, v)` | decimais |
| `setBoolean(i, v)` | boolean |
| `setDate(i, java.sql.Date)` / `setTimestamp(i, Timestamp)` | datas |
| `setObject(i, v)` | genérico (aceita `LocalDate`, `LocalDateTime` em drivers modernos) |
| `setNull(i, Types.INTEGER)` | `NULL` explícito |

## O que você precisa dominar (Aula 268)

- O que é SQL Injection e como acontece.
- `PreparedStatement` com `?` e `setXxx` (índice de 1).
- `executeQuery()` sem argumento.
- `LIKE` com `%` no valor do parâmetro.
- Extrair a criação do statement para um método privado.

---

# Aula 269 — JDBC pt 18 — `PreparedStatement` (parte 2): `UPDATE`

## 1. Cada SQL, um `PreparedStatement`

Para cada comando (SELECT, UPDATE, DELETE, INSERT) existe um SQL próprio, então um `createPreparedStatementXxx` para cada.

## 2. UPDATE seguro

```java
public static void update(Producer producer) {
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = createPreparedStatementUpdate(conn, producer)) {

        int rowsAffected = ps.executeUpdate();           // sem argumento
        log.info("Updated producer '{}', rows affected '{}'", producer.getId(), rowsAffected);

    } catch (SQLException e) {
        log.error("Error while trying to update producer '{}'", producer.getId(), e);
    }
}

private static PreparedStatement createPreparedStatementUpdate(Connection conn, Producer producer) throws SQLException {
    String sql = "UPDATE anime_store.producer SET name = ? WHERE id = ?;";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, producer.getName());      // 1º ?  → name
    ps.setInt(2, producer.getId());           // 2º ?  → id
    return ps;
}
```

Atenção à **ordem** dos `?` — o índice 1 é o primeiro `?` que aparece no SQL, o 2 é o segundo, e assim por diante. Trocar a ordem é um erro comum e silencioso.

Observação do instrutor: se um `executeUpdate` é chamado sem `WHERE`, tudo é alterado — a regra de ouro continua valendo mesmo com `PreparedStatement`.

## 3. `executeUpdate` x `executeQuery` no `PreparedStatement`

| | Retorna | Use para |
|---|---|---|
| `ps.executeUpdate()` | `int` | INSERT / UPDATE / DELETE |
| `ps.executeQuery()` | `ResultSet` | SELECT |
| `ps.execute()` | `boolean` | qualquer um |

## 4. Mesmo padrão para INSERT e DELETE

```java
String sqlInsert = "INSERT INTO anime_store.producer (name) VALUES (?);";
String sqlDelete = "DELETE FROM anime_store.producer WHERE id = ?;";
```

Resultado: nenhum `String.format`, nenhum risco de injeção, e o apóstrofo em nomes como `Howl's Moving Castle` deixa de ser problema.

## 5. Reaproveitando o mesmo `PreparedStatement`

Se você precisa executar o **mesmo SQL várias vezes** com valores diferentes, crie **uma vez** e troque os parâmetros (veja a aula de transação).

## O que você precisa dominar (Aula 269)

- `UPDATE` com `PreparedStatement`.
- Ordem dos parâmetros.
- `executeUpdate()` sem argumento.
- Padrão `createPreparedStatementXxx`.

---

# Aula 270 — JDBC pt 19 — `CallableStatement`

## 1. O que é

`CallableStatement` é um `PreparedStatement` especializado em chamar **stored procedures** e **functions** — código SQL armazenado **dentro do banco**.

| | Procedure | Function |
|---|---|---|
| Retorna valor? | opcional (parâmetros `OUT`) | **obrigatório** |
| Chamada | `CALL nome(...)` | usada dentro de um `SELECT` |

O instrutor faz um **comentário honesto**: nos anos 2006–2008 era comum colocar lógica em procedures; hoje, na prática profissional, **quase ninguém usa** (a lógica fica na aplicação, mais fácil de testar, versionar e manter). Mas é bom conhecer.

Motivos antigos para usá-las: restringir o acesso — por exemplo, impedir que um usuário consulte livremente a tabela, permitindo só uma busca limitada por nome (máx. 100 caracteres).

---

## 2. Criando a procedure no MySQL

No Workbench (o `DELIMITER` é necessário para que o `;` interno não encerre o comando antes da hora):

```sql
DELIMITER $$

CREATE PROCEDURE `anime_store`.`sp_get_producer_by_name`(IN p_name VARCHAR(100))
BEGIN
    SELECT * FROM anime_store.producer WHERE name LIKE p_name;
END$$

DELIMITER ;
```

- `DELIMITER $$` → muda o fim de comando para `$$`; ao terminar, volta para `;`.
- `IN p_name VARCHAR(100)` → parâmetro de entrada, limitado a 100 caracteres.
- Teste no Workbench: `CALL anime_store.sp_get_producer_by_name('%');`

Parâmetros possíveis: `IN` (entrada), `OUT` (saída), `INOUT` (ambos).

---

## 3. Chamando pelo JDBC

```java
private static CallableStatement createCallableStatementFindByName(Connection conn, String name) throws SQLException {
    String sql = "CALL `anime_store`.`sp_get_producer_by_name`(?);";
    CallableStatement cs = conn.prepareCall(sql);
    cs.setString(1, "%" + name + "%");
    return cs;
}
```

Uso — idêntico ao `PreparedStatement`:

```java
try (Connection conn = ConnectionFactory.getConnection();
     CallableStatement cs = createCallableStatementFindByName(conn, name);
     ResultSet rs = cs.executeQuery()) {
    while (rs.next()) { ... }
}
```

- Quem cria: `conn.prepareCall(sql)` (em vez de `prepareStatement`).
- A sintaxe JDBC "portável" é `{call nome(?)}`; no MySQL, `CALL nome(?)` também funciona.
- `CallableStatement` **herda** de `PreparedStatement`, portanto `setXxx` e `executeQuery` funcionam igual.

## 4. Parâmetros de saída (`OUT`)

```java
// CREATE PROCEDURE sp_count_producers(OUT total INT) ...
CallableStatement cs = conn.prepareCall("{call sp_count_producers(?)}");
cs.registerOutParameter(1, Types.INTEGER);
cs.execute();
int total = cs.getInt(1);
```

## O que você precisa dominar (Aula 270)

- O que são procedures e functions e por que hoje são pouco usadas.
- `DELIMITER`, `CREATE PROCEDURE`, `IN`/`OUT`.
- `conn.prepareCall` e `CallableStatement`.
- `registerOutParameter` e `getXxx(índice)`.

---

# Aula 271 — JDBC pt 20 — `JdbcRowSet` (parte 1)

## 1. O que é um `RowSet`

`RowSet` é uma interface do pacote `javax.sql.rowset` que **estende `ResultSet`**, acrescentando:

- ser um **JavaBean** (com propriedades `url`, `username`, `password`, `command`...);
- poder se **conectar sozinho** ao banco (sem você abrir `Connection`/`Statement`);
- suportar **ouvintes de eventos** (`RowSetListener`);
- (nos desconectados) poder ser **serializado** e **enviado** (por exemplo, pela rede), pois guarda os dados em memória.

Duas famílias:

| | Conectado | Desconectado |
|---|---|---|
| Interface | `JdbcRowSet` | `CachedRowSet` (e `WebRowSet`, `FilteredRowSet`, `JoinRowSet`) |
| Conexão com o banco | mantida aberta | só quando necessário |
| Serializável? | **não** | **sim** |

O `ResultSet` comum **não** pode ser enviado pela rede nem guardado; um `CachedRowSet` pode.

---

## 2. Obtendo um `JdbcRowSet`

Pela *factory* `RowSetProvider`:

```java
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public static JdbcRowSet getJdbcRowSet() throws SQLException {
    JdbcRowSet jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
    jdbcRowSet.setUrl("jdbc:mysql://localhost:3306/anime_store?useSSL=false");
    jdbcRowSet.setUsername("root");
    jdbcRowSet.setPassword("root");
    return jdbcRowSet;
}
```

Na `ConnectionFactory`, ele aparece como um método a mais (`getJdbcRowSet()`), ao lado de `getConnection()`.

## 3. Consultando

```java
public static List<Producer> findByNameJdbcRowSet(String name) {
    String sql = "SELECT * FROM anime_store.producer WHERE name LIKE ?;";
    List<Producer> producers = new ArrayList<>();

    try (JdbcRowSet jrs = ConnectionFactory.getJdbcRowSet()) {     // JdbcRowSet é AutoCloseable
        jrs.setCommand(sql);                                       // 1) define o SQL
        jrs.setString(1, "%" + name + "%");                        // 2) define o parâmetro
        jrs.execute();                                             // 3) executa

        while (jrs.next()) {                                       // ← é um ResultSet!
            producers.add(Producer.builder()
                    .id(jrs.getInt("id"))
                    .name(jrs.getString("name"))
                    .build());
        }
    } catch (SQLException e) {
        log.error("Error", e);
    }
    return producers;
}
```

Comparação do que o `JdbcRowSet` **elimina**:

| Antes | Com `JdbcRowSet` |
|---|---|
| `Connection` + `PreparedStatement` + `ResultSet` (3 objetos, 3 recursos) | 1 objeto |
| `ps.setString` | `jrs.setString` |
| `ps.executeQuery()` | `jrs.execute()` |

O instrutor confirma que o código "simplificou bastante". Dica: a documentação do `RowSet` explica o porquê de ele existir.

## O que você precisa dominar (Aula 271)

- O que é `RowSet` (JavaBean que estende `ResultSet`).
- Conectado × desconectado.
- `RowSetProvider.newFactory().createJdbcRowSet()`.
- `setUrl/Username/Password`, `setCommand`, `setXxx`, `execute`.

---

# Aula 272 — JDBC pt 21 — `JdbcRowSet` (parte 2): atualizar e ouvir eventos

## 1. O `UPDATE` não funciona em `setCommand`

Tentar usar um `UPDATE`:

```java
jrs.setCommand("UPDATE producer SET name = ? WHERE id = ?");
jrs.execute();      // ❌ SQLException: não é possível executar manipulação de dados assim
```

O `RowSet` (como o `ResultSet` atualizável) trabalha com **consultas**; para alterar dados, use o mesmo mecanismo do `ResultSet` atualizável:

```java
public static void updateJdbcRowSet(Producer producer) {
    String sql = "SELECT * FROM anime_store.producer WHERE id = ?;";

    try (JdbcRowSet jrs = ConnectionFactory.getJdbcRowSet()) {
        jrs.setCommand(sql);
        jrs.setInt(1, producer.getId());
        jrs.execute();

        if (!jrs.next()) {
            return;                                   // nada encontrado
        }
        jrs.updateString("name", producer.getName()); // altera na memória
        jrs.updateRow();                              // grava no banco
    } catch (SQLException e) {
        log.error("Error", e);
    }
}
```

Mesma regra do bloco 31: `updateXxx` + **`updateRow`**.

## 2. Vantagem: ouvintes de eventos (`RowSetListener`)

Como o `JdbcRowSet` é um JavaBean, ele dispara **eventos** quando algo acontece. Você registra um **ouvinte**:

```java
public class CustomRowSetListener implements RowSetListener {

    @Override
    public void rowSetChanged(RowSetEvent event) {      // o conteúdo inteiro mudou (ex.: execute())
        log.info("Command execute");
        if (event.getSource() instanceof RowSet) {
            try {
                ((RowSet) event.getSource()).execute();
            } catch (SQLException e) { ... }
        }
    }

    @Override
    public void rowChanged(RowSetEvent event) {         // uma linha foi inserida/atualizada/apagada
        log.info("Row changed");
    }

    @Override
    public void cursorMoved(RowSetEvent event) {        // o cursor se moveu (ex.: next())
        log.info("Cursor moved");
    }
}
```

Registrando:

```java
jrs.addRowSetListener(new CustomRowSetListener());
```

Comportamento observado na aula:

- Cada `next()` → `cursorMoved` (aparece no log para cada linha percorrida).
- Cada `updateRow()` → `rowChanged`.
- `execute()` → `rowSetChanged`.

**Para que serve?** Atualizar uma tela automaticamente quando os dados mudam, registrar auditoria, validar alterações etc. Observador (*Observer pattern*) aplicado aos dados.

Observação do instrutor: o código de exemplo usou `@SneakyThrows` do Lombok, que "esconde" a exceção checada — ele alerta que **não se deve usar em produção**, apenas em estudo, porque você perde o tratamento correto da exceção.

## 3. Vantagem frente ao `ResultSet` do bloco 31

O `ResultSet` `TYPE_SCROLL_INSENSITIVE` **não** enxerga mudanças feitas no banco depois da consulta; com o `RowSet` + ouvinte você pode **reagir** às mudanças (e recarregar).

## O que você precisa dominar (Aula 272)

- `JdbcRowSet` não executa `UPDATE` via `setCommand`; usa `updateXxx` + `updateRow`.
- `RowSetListener` e seus 3 métodos.
- `addRowSetListener`.
- Quando um `RowSet` com eventos é útil.

---

# Aula 273 — JDBC pt 22 — `CachedRowSet` (desconectado)

## 1. Diferença para o `JdbcRowSet`

| | `JdbcRowSet` | `CachedRowSet` |
|---|---|---|
| Conexão | **mantém** aberta | **fecha** após carregar os dados |
| Onde ficam os dados | no banco (cursor) | **na memória** (cache) |
| Serializável | não | **sim** |
| Alterações | `updateRow` vai direto ao banco | ficam locais até o `acceptChanges()` |

Uso típico: carregar uma tabela, **desconectar**, o usuário edita em memória (sem ocupar conexão do banco), e depois **sincronizar** as mudanças. Muito bom para desempenho e para ambientes onde a conexão é cara.

## 2. Obtendo e atualizando

```java
public static void updateCachedRowSet(Producer producer) {
    String sql = "SELECT * FROM producer WHERE id = ?;";     // sem prefixo de schema (veja o problema abaixo)

    try (Connection conn = ConnectionFactory.getConnection()) {
        conn.setAutoCommit(false);                         // ⬅ IMPORTANTE (ver abaixo)

        try (CachedRowSet crs = ConnectionFactory.getCachedRowSet()) {
            crs.setCommand(sql);
            crs.setInt(1, producer.getId());
            crs.execute(conn);                              // carrega os dados USANDO esta conexão

            if (!crs.next()) {
                return;
            }
            crs.updateString("name", producer.getName());   // altera NA MEMÓRIA
            crs.updateRow();                                // marca a linha como alterada (memória)

            crs.acceptChanges(conn);                        // ⬅ SINCRONIZA com o banco
        }
    } catch (SQLException e) {
        log.error("Error", e);
    }
}
```

Factory:

```java
public static CachedRowSet getCachedRowSet() throws SQLException {
    CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
    crs.setUrl("jdbc:mysql://localhost:3306/anime_store?useSSL=false");
    crs.setUsername("root");
    crs.setPassword("root");
    return crs;
}
```

### Fluxo

```text
execute(conn)       → traz os dados do banco para a MEMÓRIA
updateXxx/updateRow → altera só na memória
acceptChanges(conn) → abre transação, aplica as mudanças no banco e confirma
```

## 3. O problema que custou "uma hora e meia" ao instrutor

Ao testar, nada funcionava. Passo de depuração (vale como aula de *debug*):

1. Ler o erro (era um erro de **sintaxe SQL**).
2. Descobrir **onde** acontece: colocar *breakpoints* seguindo a pilha (`Ctrl+B` para abrir a implementação, `F9`/`F8` para avançar).
3. Copiar o SQL **exato** que o driver executava e testá-lo no Workbench. O instrutor identificou que o SQL gerado pelo `CachedRowSet` para gravar as alterações vinha com o nome do schema (`anime_store`) já incluído, e o prefixo que ele mesmo tinha escrito no `SELECT` fazia a sintaxe quebrar (ele próprio diz não saber exatamente por que o driver adiciona isso).
4. Corrigir o `SELECT` removendo o prefixo do schema (`SELECT * FROM producer WHERE id = ?`).

Um segundo erro: o `acceptChanges` precisa que o **`autoCommit` esteja `false`** na conexão (senão o driver tenta fazer commit por conta própria e conflita). Daí o `conn.setAutoCommit(false)` e passar a **conexão** explicitamente (`execute(conn)` e `acceptChanges(conn)`). Depois de corrigidos os dois pontos, o update funcionou.

Lição: **bugs assim não se resolvem em segundos**; método: ler → localizar → reproduzir isoladamente → corrigir.

## 4. Conflitos de concorrência (otimista)

Como o `CachedRowSet` está desconectado, outra pessoa pode alterar o mesmo registro **enquanto você edita em memória**. Demonstração do instrutor: ele pausa a thread com `Thread.sleep(10_000)` antes do `acceptChanges`, altera o registro pelo Workbench, e o `acceptChanges` encontra um **conflito** (`SyncProviderException`) — o valor no banco já não é o mesmo que foi lido.

Esse modelo se chama **concorrência otimista**: "assumo que ninguém mexeu; confiro no momento de gravar". Resolver conflitos de forma elegante é complexo e **foge ao escopo do curso**.

## O que você precisa dominar (Aula 273)

- `CachedRowSet`: dados em memória, desconectado, serializável.
- `execute(conn)`, `updateRow`, `acceptChanges(conn)` e `setAutoCommit(false)`.
- Conflito de concorrência otimista.
- Método de depuração diante de erro difícil.

---

# Aula 274 — JDBC pt 23 — Transações

## 1. O problema da atomicidade

Imagine uma compra: baixar estoque, gerar pedido, registrar pagamento. **Todas** as operações precisam acontecer, ou **nenhuma**. Se a terceira falhar, as duas primeiras devem ser desfeitas. Isso é uma **transação**: uma unidade de trabalho **atômica** ("tudo ou nada").

As propriedades **ACID**:

| Letra | Significa |
|---|---|
| **A**tomicidade | tudo ou nada |
| **C**onsistência | o banco sai de um estado válido para outro válido |
| **I**solamento | transações simultâneas não interferem uma na outra |
| **D**urabilidade | após o *commit*, os dados persistem |

## 2. O que o JDBC faz por padrão: *auto-commit*

Por padrão, a conexão está em **`autoCommit = true`**: **cada comando** é uma transação própria, **confirmada imediatamente**. Para agrupar vários comandos numa só transação:

```java
conn.setAutoCommit(false);     // ⬅ início manual da transação
// ... vários comandos ...
conn.commit();                 // confirma TUDO
// ou
conn.rollback();               // desfaz TUDO desde o último commit
```

---

## 3. Exemplo da aula: salvar vários produtores como um bloco

```java
public static void saveTransaction(List<Producer> producers) {
    try (Connection conn = ConnectionFactory.getConnection()) {

        conn.setAutoCommit(false);                    // 1) desliga o auto-commit
        try {
            preparedStatementSaveTransaction(conn, producers);   // 2) executa vários INSERTs
            conn.commit();                            // 3) tudo certo → confirma
            log.info("Transaction committed");

        } catch (SQLException e) {
            log.warn("Transaction is being rolled back", e);
            conn.rollback();                          // 4) algo falhou → desfaz TUDO
        }
        conn.setAutoCommit(true);                     // (boa prática ao devolver a conexão)

    } catch (SQLException e) {
        log.error("Error", e);
    }
}

private static void preparedStatementSaveTransaction(Connection conn, List<Producer> producers) throws SQLException {
    String sql = "INSERT INTO anime_store.producer (name) VALUES (?);";

    for (Producer producer : producers) {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {   // um PS por item (ou reutilize o mesmo!)
            log.info("Saving producer '{}'", producer.getName());
            ps.setString(1, producer.getName());
            ps.execute();
        }
    }
}
```

Pontos:

1. Precisamos de **uma única conexão** compartilhada por todos os comandos (por isso ela é passada como parâmetro). Abrir uma conexão por comando quebraria a transação — cada conexão tem a sua.
2. Cada `PreparedStatement` é aberto/fechado dentro do laço — ou reuse o mesmo statement com `ps.addBatch()` / `ps.executeBatch()`.
3. Em produção, as conexões vêm de um **pool** (HikariCP), não são criadas à mão.

### Antes do `commit` nada é permanente

Com `autoCommit=false`, se você consultar o banco pelo Workbench antes do `commit`, **não verá** os dados novos (isolamento).

---

## 4. Testando o cenário de falha

Para provocar um erro no meio, o instrutor lança uma exceção simulada quando o nome é `"White Fox"`:

```text
Produtor 1: "Toei Animation"   → INSERT ok (ainda não confirmado)
Produtor 2: "White Fox"        → lança exceção
Produtor 3: "Studio Ghibli"    → nunca é executado
```

### Primeira versão (sem rollback): ❌

O instrutor percebe que, no primeiro experimento, **o primeiro registro foi gravado** mesmo com a falha — porque a conexão foi fechada (fim do `try-with-resources`) e alguns drivers fazem *commit* ao fechar, ou por conta do tratamento incorreto. Daí a necessidade de chamar `rollback()` **explicitamente**.

### Versão correta: ✅

Com o `catch` chamando `conn.rollback()`, ao reexecutar o banco fica **sem nenhum** dos três produtores. Sem falha → os três entram. A transação preservou a **atomicidade**.

Detalhes:

- `rollback()` lança `SQLException`; por isso o bloco precisa lidar com ela (o instrutor faz `try` aninhado e sugere logar um `warn` do tipo "transaction is being rolled back").
- O `rollback` só pode ser chamado enquanto a conexão **ainda está aberta**; por isso fica dentro do `try-with-resources` da conexão, não depois.
- Quem chama o método não recebe a exceção (ela foi tratada); em sistemas reais você relançaria uma exceção de negócio para o chamador saber que a operação falhou.

## 5. Recursos adicionais (conceitos)

| Recurso | Para quê |
|---|---|
| `Savepoint` (`conn.setSavepoint()`, `rollback(savepoint)`) | desfazer só **parte** da transação |
| Níveis de isolamento (`conn.setTransactionIsolation`) | `READ_UNCOMMITTED`, `READ_COMMITTED`, `REPEATABLE_READ`, `SERIALIZABLE` |
| `ps.addBatch()` / `executeBatch()` | enviar muitos comandos de uma vez (mais rápido) |

Frameworks como o **Spring** fazem tudo isso com uma anotação (`@Transactional`) — mas é essencial entender o que acontece por baixo.

## O que você precisa dominar (Aula 274)

- Conceito de transação e ACID.
- `setAutoCommit(false)`, `commit()`, `rollback()`.
- Usar **uma conexão** para todos os comandos da transação.
- Provocar uma falha e comprovar o `rollback`.
- Savepoints, isolamento e batch (noções).

---

# Mapa mental do bloco

```text
SQL com parâmetros
├── Statement           → concatena  → SQL Injection ❌
├── PreparedStatement   → "...?"  + setXxx(1..n) + executeQuery()/executeUpdate()  ✅
└── CallableStatement   → prepareCall("CALL proc(?)")  (+ registerOutParameter)

RowSet (javax.sql.rowset)  — JavaBean que estende ResultSet
├── JdbcRowSet     → conectado · setUrl/setUsername/setPassword · setCommand · execute · RowSetListener
└── CachedRowSet   → desconectado · execute(conn) · updateRow · acceptChanges(conn) · conflito otimista

Transação
└── setAutoCommit(false) → comandos (mesma conexão) → commit()  |  rollback() no catch
```

# Cola de bolso

| Preciso... | Use |
|---|---|
| Consulta com parâmetro | `PreparedStatement` + `?` |
| Evitar SQL Injection | **sempre** `PreparedStatement` |
| Busca por trecho | `ps.setString(1, "%" + texto + "%")` |
| Inserir/atualizar/apagar | `ps.executeUpdate()` |
| Chamar procedure | `conn.prepareCall("CALL p(?)")` |
| Menos código de infraestrutura | `JdbcRowSet` |
| Trabalhar desconectado | `CachedRowSet` + `acceptChanges(conn)` |
| Várias operações atômicas | `setAutoCommit(false)` + `commit()`/`rollback()` |
