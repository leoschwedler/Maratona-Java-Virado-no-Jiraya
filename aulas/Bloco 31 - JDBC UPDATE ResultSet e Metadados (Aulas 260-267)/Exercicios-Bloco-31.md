# Exercícios — Bloco 31: JDBC — UPDATE, ResultSet e Metadados

## Aulas 260 a 267

Este arquivo acompanha o README do Bloco 31.

```text
Aula
 ↓
3 exercícios
 ↓
você implementa
 ↓
manda o código
 ↓
eu corrijo
 ↓
próxima aula
```

### Estrutura de cada aula

- 🟢 1 exercício fácil
- 🟡 1 exercício médio
- 🔴 1 exercício difícil

No final existe um:

- 🏆 **Desafio Integrador do Bloco**

> **Onde criar os arquivos:** `src/main/bloco31_aulas260a267_jdbc_resultset_metadados/aulaXXX/`. Se preferir, reaproveite o projeto Maven do bloco 30 (pacotes `conn`, `domain`, `repository`, `service`) e organize os exercícios dentro dele.

> **Pré-requisitos:** ambiente do bloco 30 funcionando (MySQL no Docker, schema `anime_store`, tabelas `producer` e `anime`, Maven, Lombok e Log4j2).

> **Regras do bloco:**
> - Ainda usamos `Statement` (o `PreparedStatement` vem no bloco 32).
> - **Nunca** devolva `null` para coleções: devolva lista vazia.
> - Use sempre try-with-resources para `Connection`, `Statement` e `ResultSet`.
> - Antes de qualquer `UPDATE`/`DELETE`, escreva o `WHERE` primeiro e teste o equivalente com `SELECT`.
> - Se você apagar dados sem querer, mantenha o script `infra/seed.sql` para repovoar o banco.

---

# Aula 260 — UPDATE com Statement

## 🟢 Exercício 01 — Primeiro UPDATE

Implemente `ProducerRepository.update(Producer)` e `ProducerService.update` (validando `id`). Altere o nome de dois produtores e confira no Workbench.

### Você deve conseguir responder

O que acontece se você esquecer o `WHERE`? Teste **primeiro** com um `SELECT` equivalente, nunca com o `UPDATE` real, e explique.

---

## 🟡 Exercício 02 — Update de anime

Crie `AnimeRepository.update(Anime)` que atualiza `name` e `episodes`. Regras no serviço:

1. `id` válido;
2. `name` não vazio (máx. 300);
3. `episodes` entre 1 e 5000.

Imprima, no log, quantas linhas foram afetadas e emita um **aviso** (`log.warn`) quando for `0` (id inexistente). Retorne um `boolean` indicando se algo foi alterado.

---

## 🔴 Exercício 03 — Reajustes em lote

No banco, adicione a coluna `anime.score DECIMAL(3,1)` e `anime.status VARCHAR(20)`. Implemente:

1. `updateStatusByProducer(int producerId, String status)` (um único `UPDATE ... WHERE producer_id = ...`);
2. `incrementEpisodes(int animeId, int delta)` usando `episodes = episodes + delta` (e proteja `delta` negativo que deixaria `episodes < 1`);
3. `normalizeNames()` que aplica `TRIM` e `UPPER` em todos os nomes de produtores (`UPDATE ... SET name = UPPER(TRIM(name))`);
4. um método que executa três updates em sequência e **demonstra** o problema se o segundo falhar (os dados ficam parcialmente alterados). Anote: isso será resolvido com transações.

Em todos, registre no log o número de linhas afetadas **sem PII**.

---

# Aula 261 — ResultSet: findAll

## 🟢 Exercício 04 — findAll de produtores

Implemente `ProducerRepository.findAll()` retornando `List<Producer>` e o `ProducerService.findAll()`. Imprima os resultados.

Responda: por que o `SELECT` lista colunas em vez de `SELECT *`?

---

## 🟡 Exercício 05 — findAll de animes com produtor

Implemente `AnimeRepository.findAll()` que devolve `List<Anime>` onde cada `Anime` **contém o objeto `Producer`**.

1. Faça com `JOIN`:
   ```sql
   SELECT a.id, a.name, a.episodes, p.id AS producer_id, p.name AS producer_name
   FROM anime a INNER JOIN producer p ON a.producer_id = p.id;
   ```
2. Use `getXxx` por **nome** e por **índice**; compare legibilidade.
3. Trate o caso de uma coluna `NULL` (adicione `anime.score` anulável): use `rs.wasNull()` ou `getObject`.
4. Imprima uma tabela formatada com `printf`.

---

## 🔴 Exercício 06 — Mapeamento reutilizável

Evite repetir o laço de mapeamento:

1. Crie uma interface funcional `RowMapper<T> { T map(ResultSet rs) throws SQLException; }`.
2. Escreva um método genérico `static <T> List<T> query(String sql, RowMapper<T> mapper)` que abre a conexão, executa e mapeia.
3. Reescreva `findAll` de produtores e de animes usando esse método com method reference ou lambda.
4. Implemente `queryForObject` (devolve `Optional<T>`) e `queryForInt` (para `SELECT COUNT(*)`).
5. Pesquise e anote no comentário: qual classe do Spring faz isso? (`JdbcTemplate`)

---

# Aula 262 — findByName

## 🟢 Exercício 07 — Busca por trecho

Implemente `findByName(String)` com `LIKE '%nome%'` e teste com 3 termos (um que acha vários, um que acha um, um que não acha nenhum — deve devolver lista vazia).

---

## 🟡 Exercício 08 — Mais filtros

Implemente em `AnimeRepository`:

```text
findByName(String trecho)
findByProducerName(String trecho)       (JOIN)
findByEpisodesBetween(int min, int max)
findTopByEpisodes(int limite)           (ORDER BY ... DESC LIMIT n)
countByProducer()                       (GROUP BY → mostre "produtor: quantidade")
```

Cada método devolve uma lista (ou um mapa, no caso do `count`). Sempre vazio, nunca `null`.

---

## 🔴 Exercício 09 — Paginação e ordenação

Implemente `AnimeRepository.findPage(int pagina, int tamanho, String ordenarPor, boolean asc)`:

1. Use `ORDER BY ... LIMIT ... OFFSET ...`.
2. **Valide** `ordenarPor` contra uma lista branca (`id`, `name`, `episodes`) — não se pode concatenar nome de coluna vindo do usuário! Explique por quê.
3. Devolva também o **total de registros** (`COUNT`) para calcular o número de páginas (crie uma classe `Pagina<T>` com `itens`, `paginaAtual`, `totalPaginas`, `totalRegistros`).
4. Teste com 25 animes (insira em massa) e pagine de 10 em 10.
5. **Demonstre SQL injection** (somente local) em `findByName` com a entrada `' OR '1'='1` e explique o resultado (você corrigirá com `PreparedStatement` no próximo bloco).

---

# Aula 263 — ResultSetMetaData

## 🟢 Exercício 10 — Mostrando as colunas

Escreva `showColumns(String tabela)` que executa `SELECT * FROM tabela LIMIT 1` e imprime nome, tipo e tamanho de cada coluna.

---

## 🟡 Exercício 11 — Impressora genérica de tabelas

Escreva `static void imprimir(String sql)` que, **sem conhecer as colunas**, executa a consulta e imprime uma tabela alinhada:

```text
id | name           | episodes
---+----------------+---------
1  | Naruto         | 500
2  | One Piece      | 1000
```

Use `ResultSetMetaData.getColumnLabel` e `getObject`. Teste com 3 consultas diferentes (tabela, `JOIN` e `COUNT`).

---

## 🔴 Exercício 12 — Exportador CSV/JSON

Crie `Exportador`:

1. `exportarCsv(String sql, Path destino)` — cabeçalho com os nomes das colunas, escapando vírgulas, aspas e quebras de linha nos valores.
2. `exportarJson(String sql, Path destino)` — lista de objetos, com os tipos corretos (números sem aspas, `null`, boolean), montada à mão (sem biblioteca).
3. Use `getColumnType` para decidir a formatação de cada coluna.
4. Teste com a consulta `JOIN` e um arquivo de 10 000 linhas (insira em massa) — **não** carregue tudo em memória (escreva linha a linha com `BufferedWriter`).
5. Inclua `isNullable` e `isAutoIncrement` em um "relatório de esquema" (`describe(tabela)`).

---

# Aula 264 — DatabaseMetaData

## 🟢 Exercício 13 — O que meu driver suporta?

Reproduza o método `checkDriverStatus` e liste, em um quadro, qual combinação (`TYPE` × `CONCUR`) o seu driver suporta (6 combinações: 3 tipos × 2 concorrências).

---

## 🟡 Exercício 14 — Relatório do banco

Com `DatabaseMetaData`, imprima:

1. nome e versão do produto do banco e do driver;
2. URL e usuário da conexão;
3. lista das tabelas do schema atual (`getTables`);
4. para cada tabela, as colunas (`getColumns`) e a chave primária (`getPrimaryKeys`);
5. as chaves estrangeiras (`getImportedKeys`).

---

## 🔴 Exercício 15 — Documentador de esquema

Escreva `GeradorDeDocumentacao` que usa metadados para gerar um arquivo Markdown (`docs/esquema.md`) com:

- uma seção por tabela;
- tabela de colunas (nome, tipo, tamanho, anulável?, PK?, FK → tabela.coluna);
- diagrama textual dos relacionamentos;
- índices (`getIndexInfo`).

Teste com o schema do projeto e depois adicione tabelas novas e rode de novo para ver se o documento acompanha. Ainda, gere classes Java simples (POJOs) a partir das colunas (tipos mapeados com `JDBCType`) — uma "mini geração de código".

---

# Aula 265 — ResultSet rolável

## 🟢 Exercício 16 — Navegando

Com `TYPE_SCROLL_INSENSITIVE`, imprima: o primeiro, o último, o terceiro (`absolute(3)`) e o anterior ao terceiro (`relative(-1)`). Mostre `getRow()` a cada passo.

---

## 🟡 Exercício 17 — De trás para frente

1. Percorra os produtores do **último ao primeiro** com `afterLast()` + `previous()`.
2. Mostre os valores de `isBeforeFirst`, `isFirst`, `isLast` e `isAfterLast` em cada posição relevante (antes de tudo, na primeira, na última, depois de tudo).
3. Teste `absolute(-1)` e `absolute(100)` (fora do limite) e explique.
4. Tente `previous()` num `ResultSet` `FORWARD_ONLY` e capture o erro.

---

## 🔴 Exercício 18 — Paginador por cursor

Implemente `PaginadorDeCursor`:

1. Uma consulta de animes com `TYPE_SCROLL_INSENSITIVE`.
2. Métodos `proxima()`, `anterior()`, `irPara(n)`, `primeira()`, `ultima()` que devolvem uma página de 5 itens usando **só** a navegação do cursor (sem `LIMIT`).
3. Imprima "página X de Y".
4. Mantenha o `ResultSet` aberto entre chamadas (classe `AutoCloseable`) e discuta o risco (conexão aberta por muito tempo).
5. Compare com a paginação por `LIMIT/OFFSET` em memória e tempo para 100 000 linhas.

---

# Aula 266 — Atualizando com ResultSet

## 🟢 Exercício 19 — Maiúsculas

Com `CONCUR_UPDATABLE`, coloque todos os nomes de produtores em maiúsculas usando `updateString` + `updateRow`. Mostre o que acontece se você **esquecer** o `updateRow`.

---

## 🟡 Exercício 20 — Cancelando

1. Altere o nome em memória (`updateString`), chame `cancelRowUpdates()` e releia o nome — está igual ao original?
2. Chame `updateRow()` e depois `cancelRowUpdates()` — o que acontece?
3. Altere apenas os registros cujo nome começa com "A" (decisão linha a linha).
4. Tente atualizar um `ResultSet` de `SELECT name FROM producer` (sem o `id`) e um de um `JOIN` — registre as exceções.

---

## 🔴 Exercício 21 — Normalizador de dados

Crie `NormalizadorDeProdutores` que percorre todos os produtores e, para cada um:

1. aplica `trim`, colapsa espaços múltiplos e usa "Title Case";
2. se o nome ficar vazio, **marca** (log) em vez de gravar;
3. conta quantos foram alterados e quantos já estavam corretos (não chame `updateRow` à toa!);
4. termina com um relatório antes/depois.

Compare o tempo com a versão `UPDATE` + `SELECT` para 20 000 produtores. Quando o `ResultSet` atualizável compensa?

---

# Aula 267 — Inserindo e deletando com ResultSet

## 🟢 Exercício 22 — Inserir se não existir

Implemente `insertIfNotExists(String nome)` com `moveToInsertRow`/`insertRow`. Execute duas vezes com o mesmo nome e prove que só o primeiro insere.

---

## 🟡 Exercício 23 — Deletar por trecho

Implemente `deleteByNameLike(String trecho)` com `deleteRow()`.

1. Conte e devolva quantos foram apagados.
2. Teste um trecho que não existe (0 apagados).
3. Teste apagar um produtor que possui animes → capture a violação de FK e continue com os demais.
4. Compare com `DELETE ... WHERE name LIKE ...` direto.

---

## 🔴 Exercício 24 — Sincronizador

Implemente `ProducerSynchronizer.sync(List<String> nomesDesejados)` usando **um único** `ResultSet` atualizável sobre `SELECT id, name FROM producer`:

1. Os nomes que já existem permanecem.
2. Os nomes que faltam são inseridos.
3. Os que existem no banco mas **não** estão na lista são apagados (cuidado com produtores com animes — registre e preserve).
4. Devolva um relatório `inseridos`, `mantidos`, `removidos`, `ignorados`.
5. Teste três cenários e mostre o banco antes e depois.
6. Discuta (comentário) os riscos de concorrência (outra aplicação alterando o banco durante a sincronização) e que mecanismos você usaria (transações, bloqueio — blocos futuros).

---

# 🏆 Desafio Integrador do Bloco 31 — Painel Administrativo do Anime Store (versão 2)

Evolua o projeto do bloco anterior para um **mini-sistema de administração** que usa **todos** os recursos do bloco.

## Requisitos

### Repositórios

1. `ProducerRepository` e `AnimeRepository` com: `save`, `update`, `delete`, `findAll`, `findById`, `findByName`, `count`.
2. `AnimeRepository` com relacionamento carregado (o `Anime` carrega o `Producer`).
3. Uma classe utilitária `JdbcHelper` (`RowMapper`, `query`, `queryForObject`) usada por todos os repositórios.
4. Paginação (`Pagina<T>`) e ordenação segura (lista branca de colunas).

### Serviços

5. Todas as regras de negócio (validação de `id`, tamanhos, intervalos) no serviço.
6. `AnimeService.moverParaProdutor(animeId, novoProducerId)` — verificando se ambos existem.
7. Mensagens de erro claras com exceções de negócio próprias (`ValidationException`, `NotFoundException`).

### Ferramentas com metadados

8. Comando `describe <tabela>`: colunas, tipos, nulabilidade, PK/FK.
9. Comando `export <consulta> <csv|json>`: exporta qualquer `SELECT` (somente `SELECT` — rejeite outros comandos).
10. Comando `driver`: o que o driver suporta (tipos de `ResultSet`).

### Navegação e edição interativa

11. Um "navegador" de console baseado em `ResultSet` rolável: `n` (próximo), `p` (anterior), `f` (primeiro), `l` (último), `g <n>` (ir para), `e <novo nome>` (editar usando `updateRow`), `d` (apagar com `deleteRow`), `i <nome>` (inserir com `insertRow`), `q` (sair).

### Qualidade

12. Logs em todos os repositórios (níveis corretos, sem PII).
13. Nenhum `ResultSet`/`Statement`/`Connection` sem try-with-resources.
14. Provar que não há vazamento: `SHOW PROCESSLIST` antes e depois de 500 operações.
15. Um relatório final com tempos: `findAll` de 50 000 animes por `FORWARD_ONLY` × `SCROLL_INSENSITIVE`.
16. Em comentário, liste os pontos de **vulnerabilidade a SQL Injection** do projeto (todos os lugares onde há concatenação) — você os corrigirá no bloco seguinte.

## Cenários de teste

```text
Popular 10 produtores e 50 animes (script).
Buscar, paginar e ordenar.
Editar e apagar pelo navegador de console.
Exportar a consulta JOIN em CSV e JSON.
Descrever as tabelas.
Tentar injeção em findByName e registrar o resultado.
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei fazer `UPDATE` com `Statement` e a importância do `WHERE`.
- [ ] Sei o que é um `ResultSet` e como o cursor funciona.
- [ ] Sei ler colunas por nome e por índice (1-based).
- [ ] Sei mapear linhas para objetos e devolver lista vazia.
- [ ] Sei usar `LIKE` e escapar `%` no `String.format`.
- [ ] Sei usar `ResultSetMetaData` e `DatabaseMetaData`.
- [ ] Conheço os tipos `FORWARD_ONLY`, `SCROLL_INSENSITIVE`, `SCROLL_SENSITIVE`.
- [ ] Conheço `READ_ONLY` e `UPDATABLE`.
- [ ] Sei navegar com `first/last/absolute/relative/previous`.
- [ ] Sei atualizar com `updateXxx` + `updateRow`.
- [ ] Sei inserir com `moveToInsertRow`/`insertRow` e apagar com `deleteRow`.
- [ ] Reconheço onde meu código é vulnerável a SQL Injection.

---

# Regra para as correções

Quando você mandar cada exercício, a correção seguirá esta ordem:

```text
1. Verificar se funciona
2. Verificar se você entendeu o conceito
3. Apontar problemas
4. Dar uma dica
5. Você tenta corrigir
6. Só mostrar a solução completa se necessário
```

Não vou simplesmente entregar o código pronto na primeira tentativa.

O objetivo é você **aprender o conteúdo e conseguir escrever o código sozinho**.
