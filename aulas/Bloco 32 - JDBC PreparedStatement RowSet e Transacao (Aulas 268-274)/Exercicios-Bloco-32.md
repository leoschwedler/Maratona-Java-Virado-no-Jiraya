# Exercícios — Bloco 32: JDBC — PreparedStatement, CallableStatement, RowSet e Transações

## Aulas 268 a 274

Este arquivo acompanha o README do Bloco 32.

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

> **Onde criar os arquivos:** `src/main/bloco32_aulas268a274_jdbc_prepared_rowset_transacao/aulaXXX/`. Aproveite o projeto Maven dos blocos 30 e 31 (MySQL no Docker, schema `anime_store`, Lombok e Log4j2).

> **Regras do bloco:**
> - A partir daqui **nenhum SQL deve ser montado por concatenação ou `String.format` com valores do usuário**. Use sempre `PreparedStatement`.
> - Parâmetros de identificadores (nome de coluna/tabela) não podem ser `?`: se precisar variá-los, use **lista branca**.
> - Todo recurso (`Connection`, `PreparedStatement`, `ResultSet`, `RowSet`) em try-with-resources.
> - Se precisar "resetar" o banco, mantenha o script `infra/seed.sql`.

---

# Aula 268 — PreparedStatement pt 01

## 🟢 Exercício 01 — Migrando o `findByName`

Reescreva `ProducerRepository.findByName` com `PreparedStatement`. Teste com:

1. `"mad"` (acha);
2. `"zzz"` (não acha → lista vazia);
3. `"x' OR 'x'='x"` (**não** pode vazar todos os registros).

Anote o que mudou em relação ao ataque que funcionava com `Statement`.

---

## 🟡 Exercício 02 — Todos os métodos seguros

Migre para `PreparedStatement` **todos** os métodos de `ProducerRepository` e `AnimeRepository` (`save`, `delete`, `findAll`, `findById`, `findByName`, `count`).

1. Cada método tem um `createPreparedStatementXxx` privado.
2. Teste com nomes que contêm apóstrofo (`Howl's`), aspas duplas, `%`, `_`, acentos e emojis.
3. Compare com a versão `Statement`: quais desses valores quebravam o SQL antes?

---

## 🔴 Exercício 03 — O ataque e a defesa

Em um banco de **estudo local** (sem dados reais), crie a tabela `usuario (id, login, senha_hash)`.

1. Implemente `loginInseguro(login, senha)` com concatenação e mostre que `login = admin' --` entra sem senha.
2. Mostre também um ataque `UNION SELECT` que lê dados de outra tabela.
3. Implemente `loginSeguro` com `PreparedStatement` e mostre que os mesmos ataques falham.
4. Armazene a senha com **hash** (`MessageDigest` SHA-256 + *salt*; só como exercício — em produção use bcrypt/argon2) e compare no Java ou no SQL.
5. Explique em comentário por que `PreparedStatement` **não protege** quando você concatena o nome de uma coluna em `ORDER BY` e como se defender (lista branca).
6. Liste 5 regras de defesa em profundidade além do `PreparedStatement` (privilégio mínimo do usuário do banco, validação de entrada, ORMs, WAF, logs).

---

# Aula 269 — PreparedStatement pt 02

## 🟢 Exercício 04 — UPDATE seguro

Implemente `update(Producer)` com `PreparedStatement` e confira a ordem dos `?`. Teste trocando a ordem dos `setXxx` de propósito e observe o erro ou o efeito.

---

## 🟡 Exercício 05 — Tipos variados

Adicione colunas à tabela `anime`: `release_date DATE`, `score DECIMAL(3,1)`, `finished BOOLEAN`, `description TEXT NULL`, `created_at TIMESTAMP`.

Implemente `save(Anime)` com `PreparedStatement` usando `setDate/setObject(LocalDate)`, `setBigDecimal`, `setBoolean`, `setNull` e `setTimestamp`. Leia de volta com `findById` e compare os valores. Teste um anime com `description = null`.

---

## 🔴 Exercício 06 — Batch

1. Insira 10 000 animes de três formas e meça o tempo de cada uma:
   - um `PreparedStatement` novo para cada insert;
   - **um** `PreparedStatement` reutilizado (`setXxx` + `executeUpdate` em laço);
   - **batch**: `addBatch()` + `executeBatch()` a cada 500 itens.
2. Para o driver MySQL, teste `rewriteBatchedStatements=true` na URL e meça de novo.
3. Capture os resultados do `executeBatch()` (`int[]`) e identifique inserções que falharam (`BatchUpdateException`).
4. Recupere as **chaves geradas** (`prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)` e `ps.getGeneratedKeys()`) e preencha o `id` nos objetos.
5. Monte uma tabela de tempos e conclusões.

---

# Aula 270 — CallableStatement

## 🟢 Exercício 07 — Minha primeira procedure

Crie no MySQL a procedure `sp_get_producer_by_name(IN p_name VARCHAR(100))` e chame-a com `CallableStatement`. Compare o código Java com o do `PreparedStatement`.

---

## 🟡 Exercício 08 — Procedure com saída

Crie `sp_count_animes_by_producer(IN p_id INT, OUT p_total INT)` e chame com `registerOutParameter` e `getInt(2)`.

1. Teste com um produtor que tem animes e outro que não tem.
2. Crie também uma **function** `fn_total_episodes(p_id INT) RETURNS INT` e chame com `SELECT fn_total_episodes(?)` e com `{? = call fn_total_episodes(?)}`.
3. Explique a diferença entre procedure e function.

---

## 🔴 Exercício 09 — Procedure de negócio

Crie uma procedure `sp_transfer_animes(IN p_from INT, IN p_to INT, OUT p_moved INT)` que:

1. valida que os dois produtores existem (`SIGNAL SQLSTATE '45000'` com mensagem de erro caso contrário);
2. move todos os animes de um produtor para o outro;
3. devolve a quantidade movida;
4. roda dentro de uma transação interna.

Chame pelo JDBC, trate a `SQLException` extraindo `getSQLState()`, `getErrorCode()` e a mensagem personalizada. Em comentário, discuta vantagens e desvantagens de colocar regras no banco (desempenho, versionamento, testes, portabilidade).

---

# Aula 271 — JdbcRowSet pt 01

## 🟢 Exercício 10 — Meu primeiro RowSet

Implemente `getJdbcRowSet()` na `ConnectionFactory` e `findByNameJdbcRowSet(String)`. Compare o número de linhas e de objetos (`Connection`/`PreparedStatement`/`ResultSet`) com a versão `PreparedStatement`.

---

## 🟡 Exercício 11 — Propriedades de um RowSet

1. Use `setUrl`, `setUsername`, `setPassword`, `setCommand`, `setType`, `setConcurrency`, `setMaxRows`, `setQueryTimeout`.
2. Mostre `setMaxRows(2)` limitando o resultado.
3. Rode o mesmo `RowSet` com **dois valores de parâmetro diferentes** (reaproveitando a mesma instância): `setString(1, ...)` + `execute()` duas vezes.
4. Leia as propriedades do `RowSet` com `getUrl()`, `getCommand()` (e discuta o que você **não** deve imprimir: senha).

---

## 🔴 Exercício 12 — Comparando abordagens

Escreva `findAll`, `findById`, `count` e `insert` de `Producer` em **quatro** estilos: `Statement`, `PreparedStatement`, `JdbcRowSet` e `ResultSet` atualizável. Monte uma tabela com: linhas de código, segurança, desempenho (10 000 execuções), facilidade de leitura e portabilidade. Dê sua recomendação.

---

# Aula 272 — JdbcRowSet pt 02

## 🟢 Exercício 13 — Atualizar com RowSet

Implemente `updateJdbcRowSet(Producer)` conforme a aula (SELECT por `id` + `updateString` + `updateRow`). Tente também `setCommand("UPDATE ...")` e capture o erro.

---

## 🟡 Exercício 14 — Ouvinte de eventos

Crie `LogRowSetListener implements RowSetListener` que registra no log `cursorMoved`, `rowChanged` e `rowSetChanged`. Registre-o em um `JdbcRowSet`, percorra 5 linhas, altere uma e reexecute. Preencha uma tabela "ação → evento disparado".

---

## 🔴 Exercício 15 — Auditoria por eventos

Crie um `AuditoriaListener` que, a cada `rowChanged`, grava numa tabela `audit_log (id, tabela, registro_id, valor_antigo, valor_novo, data_hora)`.

1. Para saber o valor antigo, guarde um *snapshot* do valor da coluna ao mover o cursor (`cursorMoved`).
2. Teste com 3 atualizações e 1 exclusão.
3. Garanta que o log de auditoria seja gravado na **mesma transação** da alteração (ou discuta por que não está sendo, e o que aconteceria com um erro no meio).
4. Compare com uma `TRIGGER` do banco (crie uma e teste). Qual é a mais confiável? Por quê?

---

# Aula 273 — CachedRowSet

## 🟢 Exercício 16 — Carregar e desconectar

Carregue todos os produtores num `CachedRowSet`, **feche a conexão**, e percorra os dados (mostrando que funcionam sem conexão). Prove que a conexão está fechada.

---

## 🟡 Exercício 17 — Editar em memória e sincronizar

1. Carregue, altere 2 linhas e **insira** uma nova (`moveToInsertRow`/`insertRow`) e **apague** uma (`deleteRow`).
2. Antes de `acceptChanges`, confira no Workbench que **nada** mudou no banco.
3. Chame `acceptChanges(conn)` com `autoCommit=false` e confira que tudo foi gravado.
4. Teste o que acontece se `autoCommit` estiver `true`.
5. Use `rowSet.rollback()`/`restoreOriginal()` para desfazer mudanças em memória.

---

## 🔴 Exercício 18 — Conflito otimista

Reproduza o conflito da aula:

1. Thread A carrega o produtor 1 no `CachedRowSet`, altera o nome e **dorme** 10 s.
2. Thread B altera o mesmo produtor pelo JDBC e confirma.
3. A tenta `acceptChanges` → `SyncProviderException`.
4. Capture a exceção e inspecione `getSyncResolver()` para ver os valores em conflito (valor original, valor do banco, valor que você tentou gravar).
5. Implemente uma política de resolução (por exemplo, "o banco vence", "a minha edição vence", "pergunta ao usuário") e teste as três.
6. Compare com *locking otimista por versão* (coluna `version`) em um `UPDATE ... WHERE id = ? AND version = ?` e com *locking pessimista* (`SELECT ... FOR UPDATE`).

---

# Aula 274 — Transações

## 🟢 Exercício 19 — Commit

Implemente `saveTransaction(List<Producer>)` com `setAutoCommit(false)` e `commit()`. Insira 3 produtores válidos e confirme no Workbench.

---

## 🟡 Exercício 20 — Rollback

1. Faça o segundo item da lista lançar uma exceção (nome inválido).
2. Prove que, **sem** `rollback`, o resultado fica inconsistente (ou depende do driver); com `rollback` o banco permanece como estava.
3. Prove o isolamento: dentro de uma transação aberta (com `sleep`), consulte o banco por **outra conexão** e confirme que ainda não vê os dados.
4. Teste o que acontece quando o programa é encerrado (`System.exit`) com transação aberta.

---

## 🔴 Exercício 21 — Transferência entre contas

Crie a tabela `account (id, owner, balance DECIMAL(12,2))` e a tabela `transfer_log`.

Implemente `transfer(int from, int to, BigDecimal amount)`:

1. Debita a origem, credita o destino e grava o log — **atomicamente**.
2. Valida saldo suficiente (`balance >= amount`) **dentro da transação**, com `SELECT ... FOR UPDATE` para travar a linha.
3. Se qualquer passo falhar, `rollback` e uma exceção de negócio (`InsufficientFundsException`, `AccountNotFoundException`).
4. Use `Savepoint`: se apenas o log falhar, mantenha a transferência mas registre um aviso.
5. Teste concorrência: 20 threads fazendo 100 transferências aleatórias cada entre 5 contas; ao final, a **soma total** dos saldos deve ser a mesma do início.
6. Mostre o efeito de níveis de isolamento (`READ_COMMITTED` × `REPEATABLE_READ`) e provoque um *deadlock* do banco (duas transações invertendo a ordem) e trate o erro com *retry*.

---

# 🏆 Desafio Integrador do Bloco 32 — Loja de Animes Transacional e Segura (versão 3)

Evolua o projeto para um sistema completo de **pedidos de animes (blu-rays)**, usando todos os recursos do bloco.

## Modelo

```text
producer(id, name)
anime(id, name, episodes, producer_id, price, stock)
customer(id, name, email UNIQUE)
orders(id, customer_id, created_at, status, total)
order_item(id, order_id, anime_id, quantity, unit_price)
audit_log(...)
```

## Requisitos

1. **Segurança**: 100% `PreparedStatement`; ordenação dinâmica apenas com lista branca; teste automatizado (um `main` de "red team") com 10 entradas maliciosas que **não** podem funcionar.
2. **Repositórios** com `save`, `update`, `delete`, `findById`, `findAll`, `findByName` (todos com `PreparedStatement`), mais:
   - `AnimeRepository.updateStock(animeId, delta)`;
   - `OrderRepository.save(Order)` com recuperação de `generatedKeys`.
3. **Transação de compra** `OrderService.placeOrder(customerId, List<ItemRequest>)`:
   - trava e verifica o estoque de cada item (`SELECT ... FOR UPDATE`);
   - baixa o estoque;
   - cria o pedido e os itens (batch);
   - calcula o total com `BigDecimal`;
   - se qualquer item falhar → **rollback total**; senão `commit`;
   - lança exceções de negócio específicas.
4. **Concorrência**: simule 30 clientes comprando o mesmo anime (estoque 20) ao mesmo tempo. No final: nenhum estoque negativo, `vendidos + estoque restante = estoque inicial`.
5. **Procedure**: `sp_sales_report(IN p_from DATE, IN p_to DATE)` chamada com `CallableStatement`.
6. **RowSet**: uma tela administrativa de "catálogo" que usa `CachedRowSet` para editar vários preços em memória e `acceptChanges` ao final, tratando conflito (alguém alterou o mesmo anime).
7. **Eventos**: `JdbcRowSet` + `RowSetListener` para registrar no log tudo que o administrador fez durante a navegação.
8. **Auditoria** em `audit_log` na **mesma transação** da alteração.
9. **Observabilidade**: logs com níveis corretos, tempo de cada operação, e **sem** PII (use ids).
10. **Relatório de desempenho**: tempo de inserir 20 000 itens por batch × individual; compare e conclua.
11. **Qualidade**: zero vazamento de conexão (`SHOW PROCESSLIST` antes/depois de 1000 pedidos); `README-PROJETO.md` com instruções de execução, modelo de dados (diagrama em texto), decisões de segurança e limitações (pool de conexões, migrations com Flyway, ORM — assuntos futuros).

## Cenários de teste

```text
Compra normal com 3 itens.
Compra com 1 item sem estoque → rollback total (confirmar que o estoque dos outros itens não mudou).
30 compras concorrentes do mesmo item.
Ataque de SQL Injection em busca, login e ordenação.
Conflito de edição de preço no CachedRowSet.
Falha forçada no meio do batch.
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei explicar SQL Injection e demonstrar um ataque.
- [ ] Sei usar `PreparedStatement` com `?` e `setXxx` (índice 1).
- [ ] Sei usar `LIKE` com `PreparedStatement`.
- [ ] Sei extrair `createPreparedStatementXxx`.
- [ ] Sei usar batch e recuperar chaves geradas.
- [ ] Sei chamar procedures/functions com `CallableStatement`.
- [ ] Sei a diferença entre `JdbcRowSet` e `CachedRowSet`.
- [ ] Sei usar `RowSetListener`.
- [ ] Sei usar `acceptChanges` e entendo o conflito otimista.
- [ ] Sei usar `setAutoCommit(false)`, `commit` e `rollback`.
- [ ] Entendo ACID, savepoints e isolamento.

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
