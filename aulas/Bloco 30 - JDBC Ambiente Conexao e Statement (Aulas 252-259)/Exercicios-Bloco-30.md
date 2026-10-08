# Exercícios — Bloco 30: JDBC — Ambiente, Conexão e Statement

## Aulas 252 a 259

Este arquivo acompanha o README do Bloco 30.

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

> **Onde criar os arquivos:** `src/main/bloco30_aulas252a259_jdbc_ambiente_statement/aulaXXX/` para os códigos Java. Arquivos de infraestrutura (`docker-compose.yml`, scripts `.sql`) podem ficar numa pasta `infra/` do projeto. Para as aulas de Maven, anote em um arquivo `NOTAS-AULA-255.md` os comandos que usou.

> **Pré-requisitos:** Docker Desktop, MySQL Workbench e (para as aulas 255 em diante) Maven. Se o seu computador não aguentar o Docker, instale o MySQL nativamente e adapte os exercícios (a URL JDBC é a mesma).

> **Segurança:** as credenciais (`root`/`root`) são **somente para estudo local**. Nos exercícios mais avançados, leia usuário/senha de **variáveis de ambiente** ou de um arquivo `.properties` (não versionado).

> **Regra do bloco:** neste bloco use apenas `Statement` com `executeUpdate`. `ResultSet`, `PreparedStatement` e transações vêm nos próximos blocos.

---

# Aula 252 — Instalando o Docker

## 🟢 Exercício 01 — Verificando o ambiente

1. Instale o Docker Desktop (ou confirme que já está instalado).
2. Execute `docker --version`, `docker ps` e `docker info` e cole as saídas em um arquivo `NOTAS-AULA-252.md`.
3. Rode `docker run hello-world` e explique, em suas palavras, o que o Docker fez (baixou uma imagem, criou um container, executou, saiu).

---

## 🟡 Exercício 02 — Container × VM

Escreva um pequeno texto (10–15 linhas, em `NOTAS-AULA-252.md`) com uma **tabela comparativa** entre máquina virtual e container (isolamento, peso, tempo de início, uso de memória, SO convidado) e um desenho em texto das duas arquiteturas. Responda: por que um container inicia em segundos e uma VM em minutos?

---

## 🔴 Exercício 03 — Explorando o Docker

1. Rode um container de outro serviço, por exemplo `docker run -d -p 8080:80 nginx`, e acesse `http://localhost:8080`.
2. Liste com `docker ps`, veja logs com `docker logs`, pare com `docker stop` e remova com `docker rm`.
3. Use `docker images` e `docker rmi` para limpar.
4. Explique a diferença entre **imagem** e **container**, e entre `stop`, `kill` e `rm`.
5. Rode **dois** containers nginx em portas diferentes ao mesmo tempo e acesse ambos.

---

# Aula 253 — Container MySQL

## 🟢 Exercício 04 — Meu `docker-compose.yml`

Crie o `docker-compose.yml` do MySQL como na aula (com volume nomeado). Suba com `docker-compose up`, confirme com `docker ps` que está rodando e pare com `Ctrl+C`.

Anote em comentário o que cada chave do arquivo significa.

---

## 🟡 Exercício 05 — Portas e volumes

1. Mude o mapeamento para `"3307:3306"` e suba; conecte pelo Workbench usando a porta 3307. Explique a diferença entre os dois lados do mapeamento.
2. Suba **dois** serviços MySQL no mesmo `docker-compose` (portas 3306 e 3307, volumes diferentes). Mostre que são bancos independentes (crie um schema em cada e confira que um não enxerga o outro).
3. Faça `docker-compose down` e depois `up` de novo: os dados criados continuam lá? Por quê?
4. Faça `docker-compose down -v` (remove volumes) e `up`: o que mudou?

---

## 🔴 Exercício 06 — Compose profissional

Melhore o arquivo:

1. Fixe a versão do MySQL (`mysql:8.0.25`) e explique por que fixar é uma boa prática.
2. Defina a senha por uma **variável de ambiente** (arquivo `.env`) em vez de escrever no YAML.
3. Adicione `restart: unless-stopped` (e compare com a decisão do instrutor de não colocar).
4. Adicione um `healthcheck` (`mysqladmin ping`) e acompanhe `docker ps` mostrando `healthy`.
5. Adicione um script `init.sql` em `/docker-entrypoint-initdb.d/` que cria o schema e as tabelas automaticamente na primeira inicialização.
6. Adicione o **Adminer** como segundo serviço (interface web em `http://localhost:8081`) e mostre que acessa o MySQL pelo nome do serviço (`db`).

---

# Aula 254 — Workbench, schema e tabelas

## 🟢 Exercício 07 — Criando o banco

No Workbench, crie o schema `anime_store`, as tabelas `producer` e `anime` (com `AUTO_INCREMENT` e chave estrangeira) e salve o SQL gerado em `infra/schema.sql`.

---

## 🟡 Exercício 08 — Testando as restrições

Execute (e anote o resultado/erro) cada comando:

1. Inserir um produtor.
2. Inserir um anime com `producer_id` existente.
3. Inserir um anime com `producer_id` **inexistente** → erro de FK.
4. Inserir um produtor sem `name` → erro de `NOT NULL`.
5. Apagar um produtor que tem animes → erro. Como resolver? (`ON DELETE CASCADE`, apagar os animes antes...)
6. Inserir dois produtores com o mesmo nome. Deveria ser permitido? Adicione `UNIQUE` e teste de novo.

---

## 🔴 Exercício 09 — Evoluindo o modelo

Evolua o banco com um script `infra/schema-v2.sql`:

1. Tabela `studio` e relacionamento **N:N** entre `anime` e `genre` (tabela associativa `anime_genre` com chave primária composta).
2. Índices para busca por nome.
3. Coluna `created_at` com valor padrão `CURRENT_TIMESTAMP`.
4. Dados de exemplo (pelo menos 5 produtores, 10 animes, 6 gêneros).
5. Consultas de verificação: animes com seus produtores (`JOIN`), animes por gênero, quantidade de animes por produtor (`GROUP BY`), produtores sem animes (`LEFT JOIN … IS NULL`).

---

# Aula 255 — Maven

## 🟢 Exercício 10 — Instalando e testando

Instale o Maven, configure as variáveis de ambiente e registre em `NOTAS-AULA-255.md` a saída de `mvn -v`. Explique o que são `M2_HOME`, `M2` e o `Path`.

---

## 🟡 Exercício 11 — Projeto Maven à mão

Crie um projeto Maven **pela linha de comando** (`mvn archetype:generate` ou criando as pastas e o `pom.xml` à mão). Faça uma classe `Main` que imprime "Olá Maven" e rode:

```bash
mvn compile
mvn package
java -cp target/classes pacote.Main
```

Explique a estrutura `src/main/java`, `src/main/resources`, `src/test/java` e o que é a pasta `target`.

---

## 🔴 Exercício 12 — Entendendo o `pom.xml`

1. Adicione a dependência do driver do MySQL e rode `mvn dependency:tree`. Liste as dependências **transitivas** que apareceram.
2. Descubra onde os `.jar`s ficaram em `~/.m2/repository` e o tamanho total.
3. Use o `scope` `test` para uma dependência (JUnit) e `provided` (Lombok), e explique a diferença para `compile` (padrão).
4. Defina a versão do Java no `pom.xml` (`maven.compiler.source/target` ou `release`) e mostre o erro ao usar uma sintaxe mais nova que a configurada.
5. Mostre como **centralizar versões** em `<properties>`.
6. Explique o ciclo de vida do Maven (`validate → compile → test → package → verify → install → deploy`).

---

# Aula 256 — Dependência e conexão

## 🟢 Exercício 13 — Conectando

Crie `ConnectionFactory.getConnection()` conforme a aula e uma classe de teste que imprime a conexão. Anote o que acontece (e a mensagem de erro) quando:

1. a senha está errada;
2. o container está parado;
3. o nome do banco está errado;
4. a porta está errada.

---

## 🟡 Exercício 14 — Configuração externa

Refatore a `ConnectionFactory` para ler `url`, `user` e `password` de um arquivo `db.properties` em `src/main/resources` (use `Properties` e `getResourceAsStream`).

1. Se o arquivo não existir, use valores padrão e registre um aviso.
2. Permita sobrescrever por variáveis de ambiente (`DB_URL`, `DB_USER`, `DB_PASSWORD`).
3. Nunca imprima a senha em log (mascare).
4. Teste os três cenários.

---

## 🔴 Exercício 15 — Diagnóstico de conexão

Escreva uma ferramenta `DiagnosticoDeConexao` que imprime:

1. versão do driver (`DatabaseMetaData.getDriverVersion`) e do servidor (`getDatabaseProductVersion`);
2. se a conexão é válida (`connection.isValid(2)`) e o tempo que levou para abrir;
3. o catálogo (banco) atual e o `autoCommit`;
4. o resultado de abrir 100 conexões em sequência (tempo médio) — **fechando todas** — e depois 100 **sem fechar** (e o erro `Too many connections` que pode aparecer; mostre `SHOW PROCESSLIST` no Workbench);
5. uma explicação (comentário) de por que abrir uma conexão por operação é caro e o que é um *connection pool* (HikariCP) — apenas explicação.

---

# Aula 257 — Inserindo com Statement

## 🟢 Exercício 16 — Primeiro INSERT

Crie a entidade `Producer`, o `ProducerRepository.save` e insira 3 produtores. Imprima as linhas afetadas e confira no Workbench.

---

## 🟡 Exercício 17 — Anime e chave estrangeira

Crie `Anime` (`id`, `name`, `episodes`, `producerId`) e `AnimeRepository.save`. Insira 6 animes ligados aos produtores existentes.

1. Tente inserir um anime com `producerId` inexistente e capture a `SQLException` — imprima `getSQLState()`, `getErrorCode()` e a mensagem.
2. Insira um anime com `name` contendo apóstrofo (`Howl's Moving Castle`). O que acontece? Por quê? (Gancho para SQL Injection.)

---

## 🔴 Exercício 18 — Demonstração de SQL Injection

**Apenas no seu banco local de estudo:**

1. Crie `ProducerRepository.saveRisky(String nome)` que concatena o texto no SQL.
2. Mostre que um nome como `x'), ('hacker` insere **duas** linhas.
3. Mostre que `executeUpdate` não aceita múltiplos comandos por padrão (`; DROP ...` falha), mas que `allowMultiQueries=true` na URL muda isso — e por que **nunca** se deve habilitar isso sem necessidade.
4. Escreva uma função de "escape" manual (substituindo `'` por `''`) e discuta por que **não é uma solução confiável**.
5. Em comentário, descreva como `PreparedStatement` resolve o problema (você implementará no bloco seguinte).

---

# Aula 258 — Lombok e Log4J2

## 🟢 Exercício 19 — Lombok

Converta `Producer` e `Anime` para Lombok (`@Value @Builder`). Compare o tamanho dos arquivos e abra o `.class` compilado (com `javap -p`) para confirmar os métodos gerados.

---

## 🟡 Exercício 20 — Log

1. Configure o Log4j2 (`pom.xml` + `log4j2.xml` em `resources`).
2. Substitua todos os `System.out.println` do repositório por `log.info` / `log.error`.
3. Gere uma mensagem de cada nível (`trace`, `debug`, `info`, `warn`, `error`) e mostre o efeito de trocar o nível do logger entre `info` e `debug`.
4. Reduza o nome do logger no formato (`%logger{1}`).

---

## 🔴 Exercício 21 — Logs em arquivo

Evolua o `log4j2.xml`:

1. `RollingFile` appender que grava em `logs/app.log` e gira por **tamanho** (1 MB) ou por **dia**, mantendo no máximo 5 arquivos.
2. Logs de erro também em um arquivo `logs/error.log` (filtro por nível).
3. Um logger específico para o pacote do repositório em `DEBUG` e o resto em `INFO`.
4. Gere 10 000 mensagens e confirme a rotação.
5. Registre a stack trace de uma `SQLException` e verifique como aparece no arquivo.
6. Pesquise e anote em comentário: o que é *MDC* (contexto de log) e para que serve em aplicações web.

---

# Aula 259 — Deletando

## 🟢 Exercício 22 — DELETE

Implemente `ProducerRepository.delete(int id)`, o `ProducerService.delete` (com validação `id > 0`) e delete um produtor sem animes. Mostre as linhas afetadas.

---

## 🟡 Exercício 23 — Camadas

Organize o projeto nos pacotes `domain`, `repository`, `service` e `conn`. Faça `ProducerService.save` validar que o nome não é vazio nem tem mais de 255 caracteres, e `delete` devolver um `boolean` (se algo foi apagado). Teste com valores válidos e inválidos.

---

## 🔴 Exercício 24 — Remoções em lote e consistência

1. `deleteAll(List<Integer> ids)` usando `DELETE … WHERE id IN (...)` montado dinamicamente.
2. `deleteByName(String nome)` que informa quantos registros foram afetados.
3. Tente apagar um produtor que tem animes: capture o erro de FK e implemente `deleteWithAnimes(int id)` que apaga primeiro os animes e depois o produtor — **sem transação** (ainda), e mostre o problema se a segunda operação falhar (dados inconsistentes). Anote que a solução vem no bloco de transações.
4. Implemente um "soft delete" (coluna `deleted boolean`) com `UPDATE` em vez de `DELETE` e discuta as vantagens.
5. Mostre que `AUTO_INCREMENT` não reaproveita ids.

---

# 🏆 Desafio Integrador do Bloco 30 — Mini Sistema de Cadastro de Animes (versão 1)

Você vai montar, do zero, o projeto completo deste bloco: infraestrutura + camadas + logs.

## Requisitos

### Infraestrutura

1. `docker-compose.yml` com MySQL (versão fixa, senha por `.env`, volume, `healthcheck`, `init.sql` criando schema/tabelas e dados iniciais).
2. Projeto **Maven** com dependências: driver MySQL, Lombok, Log4j2.
3. `log4j2.xml` com console + arquivo rotativo.
4. `db.properties` + variáveis de ambiente para a conexão.

### Código (camadas)

5. `conn/ConnectionFactory` (lê configuração e valida a conexão).
6. `domain/Producer` e `domain/Anime` (`@Value @Builder`).
7. `repository/ProducerRepository` e `AnimeRepository` com `save`, `delete` e (novos) `update` simples — tudo com `Statement` e `executeUpdate`, com log em cada operação.
8. `service/ProducerService` e `AnimeService` com regras de negócio:
   - nome obrigatório (1–255);
   - `episodes` entre 1 e 5000;
   - não permitir anime sem produtor existente (por enquanto, trate o erro de FK);
   - não permitir apagar produtor com animes (mensagem clara).
9. Uma classe `Main` (camada de apresentação em console) com um menu simples que chama **apenas os serviços**.

### Qualidade

10. Nenhum `System.out.println` fora do `Main`.
11. Nenhuma conexão fica aberta (prove com `SHOW PROCESSLIST` antes e depois de 200 operações).
12. Um relatório final (log) com a quantidade de inserções, remoções e falhas.
13. Escreva um `README-PROJETO.md` explicando como subir o ambiente do zero em 5 comandos.
14. Escreva um comentário final listando **3 limitações** da abordagem atual que serão resolvidas nos próximos blocos (SQL Injection, falta de consultas, falta de transações).

## Cenário de teste

```text
Subir o ambiente do zero.
Inserir 5 produtores e 15 animes.
Tentar inserir anime com produtor inexistente.
Tentar apagar produtor com animes.
Apagar anime e depois o produtor.
Mostrar o log com todos os eventos.
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei o que é JDBC e a diferença entre interface e driver.
- [ ] Sei o que é um container e como o Docker se diferencia de uma VM.
- [ ] Sei escrever um `docker-compose.yml` com imagem, porta, variável e volume.
- [ ] Sei criar schema e tabelas (PK, AUTO_INCREMENT, FK).
- [ ] Sei instalar o Maven e escrever um `pom.xml` com dependências.
- [ ] Sei montar a URL JDBC e abrir conexão com `DriverManager`.
- [ ] Sei usar `Statement.executeUpdate` com `INSERT` e `DELETE`.
- [ ] Sei usar try-with-resources com `Connection` e `Statement`.
- [ ] Sei por que concatenar SQL é perigoso.
- [ ] Sei usar Lombok (`@Value`, `@Builder`, `@Log4j2`).
- [ ] Sei configurar o Log4j2 e usar os níveis de log.
- [ ] Sei separar o código em `domain`, `repository` e `service`.

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
