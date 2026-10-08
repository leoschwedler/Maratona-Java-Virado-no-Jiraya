# Exercícios — Bloco 33: JDBC — CRUD completo pelo console

## Aulas 275 a 279

Este arquivo acompanha o README do Bloco 33.

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

> **Onde criar os arquivos:** `src/main/bloco33_aulas275a279_jdbc_crud/aulaXXX/`. Aproveite o projeto Maven e o banco `anime_store` dos blocos anteriores (MySQL no Docker, Lombok e Log4j2).

> **Regras do bloco:**
> - Somente `PreparedStatement`. Nada de SQL concatenado.
> - Repository **não** imprime nem lê do console; Service **não** escreve SQL.
> - Todo recurso JDBC em try-with-resources.
> - Entrada do usuário sempre com `nextLine()` + conversão.

---

# Aula 275 — CRUD pt 01 — `findByName` e `findAll`

## 🟢 Exercício 01 — Estrutura e busca

Monte os pacotes `conn`, `dominio`, `repository`, `service` e `test`. Crie `Producer` (imutável, com Builder) e `ProducerRepository.findByName(String)`.

1. Teste no `main` com `"mad"`, `"zzz"` e `""`.
2. Explique, em comentário, por que `""` lista todos os registros.

---

## 🟡 Exercício 02 — Menu com `Scanner`

Crie `ProducerService` e a classe `CrudTest` com laço de menu (`1` = buscar, `0` = sair).

1. Use apenas `nextLine()` + `Integer.parseInt`.
2. Mostre `[id] - nome` para cada resultado.
3. Se não houver resultado, escreva `No producers found`.

---

## 🔴 Exercício 03 — Entrada robusta

Crie um utilitário `Console` com `static int readInt(String message)` que **repete a pergunta** enquanto o usuário não digitar um número válido (trate `NumberFormatException`).

1. Use-o no menu principal.
2. Faça também `readNonEmpty(String message)`.
3. Escreva por que misturar `nextInt()` e `nextLine()` quebra o fluxo e demonstre o bug em um método separado.

---

# Aula 276 — CRUD pt 02 — `delete`

## 🟢 Exercício 01 — `delete` por ID

Implemente `ProducerRepository.delete(int id)` com `PreparedStatement`.

1. Teste apagando um ID existente e outro inexistente.
2. O que o JDBC faz quando o ID não existe? (exceção? silêncio?) Anote.

---

## 🟡 Exercício 02 — Confirmação

No `ProducerService`, peça o ID e a confirmação `Y/N`.

1. Aceite `y`, `Y`, `yes` (qualquer caixa).
2. Qualquer outra resposta cancela e imprime `Canceled`.
3. Liste os produtores **antes** de pedir o ID.

---

## 🔴 Exercício 03 — Informar quantas linhas foram afetadas

Troque `ps.execute()` por `ps.executeUpdate()` e mostre `"%d row(s) deleted"`.

1. Se retornar 0, avise `Nothing was deleted: id not found`.
2. Faça o mesmo para o `anime`.
3. Descubra: o que acontece ao deletar um `producer` que tem `anime` ligado (chave estrangeira)? Trate a exceção e mostre uma mensagem clara ao usuário.

---

# Aula 277 — CRUD pt 03 — `save`

## 🟢 Exercício 01 — `save`

Implemente `ProducerRepository.save(Producer)` e a opção `3` no menu.

1. Salve `Fox Kids` e confira com a busca.
2. Tente salvar `Howl's Studio` (com apóstrofo) e explique por que funciona.

---

## 🟡 Exercício 02 — Enhanced `switch` e `forEach`

1. Troque o `switch` antigo pelo **enhanced switch** com `->` e `default` lançando `IllegalArgumentException`.
2. Troque os `for` de impressão por `forEach` com lambda.
3. Faça uma versão do `switch` **como expressão** que devolve o nome da operação (`"SEARCH"`, `"DELETE"`...) e imprima no log.

---

## 🔴 Exercício 03 — Retornar o ID gerado

Faça `save` devolver o `Producer` já com o ID gerado pelo banco.

1. Use `conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)` e `ps.getGeneratedKeys()`.
2. Imprima `Saved with id X`.
3. Valide: não permita salvar nome vazio nem duplicado (consulte antes ou trate a violação de unicidade, se existir `UNIQUE`).

---

# Aula 278 — CRUD pt 04 — `update`

## 🟢 Exercício 01 — `findById`

Implemente `findById(Integer)` retornando `Optional<Producer>`.

1. Teste com ID existente e inexistente.
2. Por que `Optional` é melhor aqui do que `null` ou `List`?

---

## 🟡 Exercício 02 — `update`

Implemente `ProducerRepository.update(Producer)` e a opção `4`.

1. ID inexistente → `Producer not found`.
2. Nome vazio → mantém o atual (ternário).
3. Como `Producer` é imutável, monte um novo objeto com `builder`.

---

## 🔴 Exercício 03 — Atualização parcial genérica

Imagine que `producer` tem mais colunas: `country` e `foundedYear`.

1. Altere a tabela e a classe.
2. No `update`, o usuário pode deixar **qualquer campo vazio** para manter o valor atual.
3. Use `toBuilder = true` (Lombok) para copiar o objeto e alterar só o necessário.
4. Escreva os testes manuais (tabela com o cenário e o resultado esperado).

---

# Aula 279 — CRUD pt 05 — Anime CRUD

## 🟢 Exercício 01 — `Anime` e `INNER JOIN`

Crie `Anime` com `Producer` embutido e `AnimeRepository.findByName` usando `INNER JOIN` e alias `producer_name`.

1. Imprima `[id] nome - episódios - produtor`.
2. Escreva o SQL como **text block**.

---

## 🟡 Exercício 02 — CRUD de `Anime`

Implemente `findById`, `save`, `delete` e `update` do anime.

1. `save` pede nome, episódios e **id do produtor** (valide se o produtor existe com `ProducerRepository.findById`).
2. `update` só altera nome e episódios; vazio mantém.
3. Menu em dois níveis (`1` Producer / `2` Anime), com `9` para voltar.

---

## 🔴 Exercício 03 — Evitar N+1 e paginar

1. Mostre, com um contador de queries (log), a diferença entre buscar os produtores um a um e usar `JOIN`.
2. Implemente `findAll(int page, int size)` com `LIMIT ? OFFSET ?`.
3. Adicione ordenação por **lista branca** (`name`, `episodes`) — não pode ser parâmetro `?`.
4. Mostre `Page 2 of N`.

---

# 🏆 Desafio Integrador do Bloco 33 — **AnimeStore CLI**

Entregue um sistema de linha de comando completo, organizado em camadas.

## Requisitos

### 1. Arquitetura

```text
conn/  dominio/  repository/  service/  util/  test/
```

- `Console` (util) para entradas validadas.
- Repository sem I/O; Service sem SQL.

### 2. Funcionalidades

**Producer:** buscar (nome parcial), listar todos, cadastrar, atualizar, deletar.
**Anime:** mesmas operações, com `INNER JOIN` e produtor obrigatório.

### 3. Regras de negócio

1. Não cadastrar nome vazio.
2. Não cadastrar anime com produtor inexistente.
3. Ao deletar um produtor com animes: perguntar se deseja **deletar também os animes** (em uma única transação) ou cancelar.
4. Episódios: inteiro entre 1 e 5000.
5. Todo `delete` exige confirmação.

### 4. Qualidade

1. Enhanced `switch`, text blocks e `Optional` onde fizer sentido.
2. Tratamento de `SQLException` com mensagens amigáveis e log do erro real.
3. `findAll` paginado com ordenação por lista branca.
4. Um script `infra/seed.sql` com dados de teste.

### 5. Entrega

1. Código nos pacotes do bloco.
2. Um `README` curto com instruções de execução.
3. Um roteiro de testes manuais com 10 cenários (incluindo entradas inválidas).

---

## Checklist do bloco

- [ ] CRUD de `Producer` funcional
- [ ] CRUD de `Anime` funcional com `JOIN`
- [ ] Sem SQL concatenado
- [ ] Menu em dois níveis
- [ ] Entradas validadas
- [ ] Desafio integrador entregue

## Regra para as correções

Mande o código de cada aula e eu corrijo apontando: segurança (SQL), organização por camadas, tratamento de recursos e erros, e clareza do código.
