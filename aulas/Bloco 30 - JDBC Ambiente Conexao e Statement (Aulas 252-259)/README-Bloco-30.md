# Bloco 30 — JDBC: Ambiente, Conexão e `Statement`

## Aulas 252 a 259

Chegou a hora de ligar o Java ao **banco de dados**. **JDBC** (*Java Database Connectivity*) é a API padrão do Java para conversar com bancos relacionais. Neste primeiro bloco você vai **montar o ambiente** (Docker, MySQL, Workbench, Maven), **conectar**, **inserir** e **deletar** dados com `Statement`, e conhecer **Lombok** e **Log4j2**, que vão simplificar o resto do curso.

> O instrutor avisa: o curso **não ensina SQL ou teoria de bancos**. Se você não souber o que é tabela, chave primária, chave estrangeira, `INSERT`, `SELECT`, `DELETE`, faça uma revisão rápida antes (o foco é JDBC).

As aulas deste bloco são:

```text
252 — JDBC pt 01 — Instalando Docker
253 — JDBC pt 02 — Criando um container MySQL
254 — JDBC pt 03 — Instalando o Workbench, criando schema e tabelas
255 — JDBC pt 04 — Instalando e adicionando Maven ao projeto
256 — JDBC pt 05 — Adicionando dependência e conectando com o banco
257 — JDBC pt 06 — Inserindo dados com Statement
258 — JDBC pt 07 — Lombok e Log4J2
259 — JDBC pt 08 — Deletando dados com Statement
```

Visão geral da arquitetura que vamos montar:

```text
 Seu código Java (JDBC)  ──────────▶  Driver MySQL (.jar)  ──────────▶  Servidor MySQL
                                       (dependência Maven)             (container Docker, porta 3306)
                                                                                ▲
 MySQL Workbench (interface gráfica) ───────────────────────────────────────────┘
```

---

# Aula 252 — JDBC pt 01 — Instalando o Docker

## 1. Por que Docker?

Você poderia instalar o MySQL **direto no Windows** (como um serviço), baixando o instalador do site oficial. O curso escolhe o **Docker** porque:

- Uma vez que você aprende Docker, resolve **qualquer** necessidade de instalar software (bancos, filas, ferramentas de monitoramento...), sem poluir seu computador.
- Dá para **subir, parar e remover** tudo com um comando.
- Muito usado no mercado (e nos cursos seguintes de Spring Boot do instrutor).

## 2. Docker x Máquina virtual

| | Máquina virtual (VirtualBox, VMware) | Docker (container) |
|---|---|---|
| Sistema operacional completo? | **sim**, um por VM | **não**: compartilha o kernel do SO hospedeiro |
| Peso | pesado (GBs) | leve (MBs) |
| Início | minutos | segundos |
| Isolamento | total | processo isolado |

```text
VMs:         Hardware → Hypervisor → [SO convidado + app] [SO convidado + app]
Containers:  Hardware → SO → Docker Engine → [app] [app] [app]
```

Um **container** é, de forma simplificada, "uma máquina virtual bem mais leve".

## 3. Instalação

1. Baixar o **Docker Desktop** (site oficial do Docker).
2. Executar o instalador (no Windows, marcar o uso do WSL 2 / virtualização, se perguntado).
3. Se necessário, **habilitar a virtualização na BIOS** (*hardware-assisted virtualization* / VT-x / SVM) — o instrutor precisou fazer isso e reiniciar.
4. Abrir o Docker Desktop e esperar o ícone indicar que está **rodando**.

Requisito prático: o Docker Desktop é pesado; com **menos de ~6 GB de RAM** o computador pode ficar lento — nesse caso prefira a **instalação nativa do MySQL**.

Verificação no terminal:

```bash
docker --version
docker ps
```

## O que você precisa dominar (Aula 252)

- O que é JDBC (conectar Java a bancos).
- O que é um container e a diferença para uma máquina virtual.
- Instalar e iniciar o Docker Desktop.

---

# Aula 253 — JDBC pt 02 — Criando um container MySQL

## 1. O arquivo `docker-compose.yml`

O `docker-compose` descreve, em **YAML**, os serviços que você quer subir. Crie na raiz do projeto `docker-compose.yml` (o nome precisa ser exatamente esse):

```yaml
version: '2.4'
services:
  db:
    image: mysql
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
    volumes:
      - maratona_data:/var/lib/mysql

volumes:
  maratona_data:
```

> ⚠️ **YAML é sensível à identação** (espaços, **nunca** tabs). Um espaço a mais ou a menos e o arquivo não funciona. Em caso de dúvida, copie o arquivo completo.

### Explicando cada parte

| Chave | Significado |
|---|---|
| `version` | versão do formato do arquivo (o curso usa a `2.4`) |
| `services` | lista de serviços (containers) |
| `db` | nome do serviço (livre) |
| `image: mysql` | imagem do **Docker Hub** (hub.docker.com). Sem versão = a mais recente. Você pode fixar: `mysql:8.0.25` |
| `container_name` | nome do container (livre) |
| `environment` | variáveis de ambiente exigidas pela imagem; `MYSQL_ROOT_PASSWORD` define a senha do usuário `root` |
| `ports` | mapeia `portaDoSeuPC:portaDoContainer` |
| `volumes` | guarda os dados **fora** do container |

### Portas: `"3306:3306"`

O formato é **`PC:CONTAINER`**. O MySQL dentro do container escuta na 3306. Se a 3306 já estiver ocupada no seu computador, mude o lado esquerdo (ex.: `"3307:3306"` → você acessa por `localhost:3307`, que é redirecionado para a 3306 do container).

### Volumes: não perder os dados

Containers são **descartáveis**: ao removê-los, tudo que está dentro some. O **volume** guarda os dados do banco **fora** do container, no gerenciamento do Docker. Assim você pode destruir e recriar o container e os dados continuam lá.

## 2. Comandos essenciais

```bash
docker-compose up         # cria e inicia (faz o download da imagem na primeira vez)
                          # Ctrl+C no terminal: para o container
docker ps                 # containers em execução
docker ps -a              # todos (inclusive parados)
docker-compose down       # para e REMOVE o container (o volume permanece!)
docker volume ls          # lista os volumes (nome = projeto_volume)
```

Dicas:

- O comando `docker-compose up` fica "preso" mostrando os logs; use outro terminal para `docker ps`.
- Para rodar em segundo plano: `docker-compose up -d` (e `docker-compose logs -f` para ver logs).
- Se der erro de porta em uso, outro MySQL/projeto já está usando a 3306.

## O que você precisa dominar (Aula 253)

- Estrutura do `docker-compose.yml`.
- Mapeamento de portas e volumes.
- `up`, `down`, `ps`.
- Por que usar volumes.

---

# Aula 254 — JDBC pt 03 — Workbench, schema e tabelas

## 1. Servidor × cliente

- O **MySQL Server** (no container) armazena os dados.
- Para criar tabelas e consultar de forma visual, você precisa de um **cliente**: o **MySQL Workbench** (baixe a versão compatível com a do servidor).

## 2. Criando a conexão

No Workbench, **MySQL Connections → +**:

| Campo | Valor |
|---|---|
| Connection Name | `localhost` |
| Hostname | `127.0.0.1` |
| Port | `3306` |
| Username | `root` |
| Password | (Store in Vault) `root` |

*Test Connection* → deve dizer que foi bem-sucedida. (O container precisa estar rodando.)

Testando o ciclo: pare o container (Ctrl+C), veja o Workbench mostrar o servidor "parado"; suba de novo e ele volta. É assim que você libera recursos quando não está estudando.

## 3. Criando o schema (banco)

```sql
CREATE SCHEMA anime_store;
```

(Em MySQL, *schema* = *database*.) Convenção: nomes em minúsculas, palavras separadas por `_`.

Defina-o como padrão (botão direito → *Set as Default Schema*) para não precisar prefixar tudo com `anime_store.`.

## 4. Criando as tabelas

Modelo simples (um produtor tem vários animes; um anime tem um produtor):

```text
producer 1 ───── N anime
```

```sql
CREATE TABLE `anime_store`.`producer` (
  `id`   INT          NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `anime_store`.`anime` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(300) NOT NULL,
  `episodes`    INT          NOT NULL,
  `producer_id` INT          NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_anime_producer`
    FOREIGN KEY (`producer_id`) REFERENCES `anime_store`.`producer` (`id`)
);
```

Pontos:

- **`PRIMARY KEY`** identifica cada linha; **`AUTO_INCREMENT`** deixa o banco gerar o `id` sozinho.
- **`FOREIGN KEY`**: `anime.producer_id` aponta para `producer.id` — o banco **impede** inserir um anime com produtor inexistente (integridade referencial). O tipo deve ser idêntico ao da coluna referenciada.
- Exemplo de verificação:

```sql
SELECT * FROM anime_store.producer;
```

Para executar no Workbench: `Ctrl+Enter` (linha atual) ou o ícone de raio.

## O que você precisa dominar (Aula 254)

- Cliente × servidor de banco.
- Configurar uma conexão no Workbench.
- Criar schema e tabelas com chave primária, `AUTO_INCREMENT` e chave estrangeira.

---

# Aula 255 — JDBC pt 04 — Instalando e adicionando o Maven

## 1. O que é o driver JDBC?

JDBC é uma **especificação**: o pacote `java.sql` define **interfaces** (`Connection`, `Statement`, `ResultSet`, `DriverManager`...). Quem **implementa** essas interfaces é o **driver** fornecido por cada fabricante de banco (MySQL, PostgreSQL, Oracle...).

```text
Seu código usa:     java.sql.Connection  (interface)
Implementação real: com.mysql.cj.jdbc.ConnectionImpl  (dentro do driver do MySQL)
```

Por isso, em teoria, trocar de banco é trocar o driver e a URL — seu código continua o mesmo (desde que você não use recursos específicos de um banco).

## 2. Por que Maven?

Antes: você baixaria o `.jar` do driver na mão e colocaria no projeto. Problemas: versões mudam, é preciso baixar de novo, organizar pastas...

**Maven** é um **gerenciador de dependências e *build* de projetos**: você declara no `pom.xml` "meu projeto precisa do driver X na versão Y", e o Maven baixa o `.jar` (e as dependências dele!) do repositório central (`mvnrepository.com`).

## 3. Instalando o Maven (Windows)

1. Baixar o **binário** (zip) em maven.apache.org e extrair em uma pasta (ex.: `C:\dev\apache-maven-3.8.1`).
2. Criar as variáveis de ambiente:
   - `M2_HOME` = pasta do Maven (**sem** barra no final);
   - `M2` = `%M2_HOME%\bin`;
   - Acrescentar `%M2%` ao `Path`.
3. Abrir um **novo** terminal e testar: `mvn -v` (mostra a versão do Maven e do Java).

## 4. Adicionando suporte Maven ao projeto (IntelliJ)

Botão direito no projeto → **Add Framework Support… → Maven**. A estrutura muda para o padrão Maven:

```text
projeto/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/        ← seu código
    │   └── resources/   ← arquivos de configuração (properties, xml...)
    └── test/java/       ← testes
```

O `pom.xml` (*Project Object Model*) começa assim:

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>academy.devdojo</groupId>      <!-- "dono": normalmente o pacote raiz -->
    <artifactId>maratona-java</artifactId>  <!-- nome do projeto -->
    <version>1.0-SNAPSHOT</version>
</project>
```

⚠️ O instrutor observa: depois de adotar o padrão Maven, arquivos de configuração (como o `log4j2.xml` que virá na aula 258) precisam estar em `src/main/resources` para serem encontrados; se ficarem fora, a aplicação não os acha — basta arrastá-los para a pasta `resources` e recompilar.

## O que você precisa dominar (Aula 255)

- JDBC = interfaces; driver = implementação do fabricante.
- Para que serve o Maven e como ele resolve dependências.
- Estrutura de pastas e `pom.xml` (`groupId`, `artifactId`, `version`).

---

# Aula 256 — JDBC pt 05 — Dependência e conexão

## 1. Declarando a dependência do driver

No `pom.xml`, dentro de `<dependencies>`:

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.25</version>
    </dependency>
</dependencies>
```

Onde achar o trecho: **mvnrepository.com** → pesquisar "mysql connector java" → escolher a versão → copiar o bloco XML.

Dicas:

- Escolha uma versão do conector **compatível com a do seu servidor** MySQL (aqui, 8.0.x).
- Depois de colar, clique em **"Load Maven Changes"** (ícone no canto do editor) ou `Ctrl+Shift+O`. O Maven baixa o driver e **as dependências transitivas** (o driver precisa de outras bibliotecas, como o `protobuf` da Google — você nem precisa saber).
- Os `.jar`s ficam em `~/.m2/repository` (cache local); não baixam de novo na próxima vez.

> Nota moderna: versões novas do driver usam outra coordenada: `com.mysql:mysql-connector-j`. O restante do uso é igual.

## 2. A classe `ConnectionFactory`

```java
package academy.devdojo.maratonajava.javacore.jdbc.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/anime_store";
        String username = "root";
        String password = "root";
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

(É o padrão **Factory** do bloco 29 aplicado: a fábrica esconde o *como* criar a conexão.)

### Anatomia da URL JDBC

```text
jdbc : mysql : // localhost : 3306 / anime_store
 │      │        │           │        └ nome do banco (schema)
 │      │        │           └ porta (a do SEU computador, conforme o docker-compose)
 │      │        └ host
 │      └ subprotocolo: identifica o banco/driver
 └ protocolo fixo
```

Cada banco tem seu formato (PostgreSQL: `jdbc:postgresql://...`, Oracle: `jdbc:oracle:thin:@...`) — consulte a documentação do fornecedor.

### `DriverManager.getConnection`

- Procura entre os drivers disponíveis no classpath um que "entenda" a URL e abre a conexão.
- Lança `SQLException` (checked): rede fora do ar, senha errada, banco inexistente...
- Em JDBC 4+, **não é mais necessário** `Class.forName("com.mysql.jdbc.Driver")` — o driver se registra automaticamente.

## 3. Testando

```java
public class ConnectionFactoryTest {
    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection();
        System.out.println(connection);      // se imprimiu um objeto, conectou!
    }
}
```

O container precisa estar rodando (`docker-compose up`). Se a senha estiver errada: `Access denied for user 'root'`.

> ⚠️ **Segurança:** usuário/senha escritos direto no código são só para estudo local. Em projetos reais, leia de variáveis de ambiente ou arquivos de configuração fora do repositório, e **nunca** use `root` em produção.

## O que você precisa dominar (Aula 256)

- Declarar a dependência no `pom.xml` e recarregar o Maven.
- Dependências transitivas e o cache `~/.m2`.
- Formato da URL JDBC.
- `DriverManager.getConnection(url, user, pass)` e `SQLException`.

---

# Aula 257 — JDBC pt 06 — Inserindo dados com `Statement`

## 1. Primeiro, o domínio

Cada tabela vira uma classe (a **entidade**):

```java
public class Producer {
    private Integer id;
    private String name;

    // construtor, getters, setters, equals/hashCode, toString
    // (builder opcional — bloco 29)
}
```

## 2. O repositório (camada de acesso a dados)

```java
public class ProducerRepository {

    public static void save(Producer producer) {
        String sql = String.format("INSERT INTO `anime_store`.`producer` (`name`) VALUES ('%s');", producer.getName());

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("Linhas afetadas: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

Conceitos:

1. **`Connection`**: a "linha telefônica" com o banco.
2. **`Statement`**: o objeto que **envia comandos SQL** pela conexão.
3. **`executeUpdate(sql)`**: para comandos que **alteram** dados (`INSERT`, `UPDATE`, `DELETE`, DDL). Devolve `int` = quantidade de linhas afetadas.
4. **`try-with-resources`**: `Connection` e `Statement` são `AutoCloseable`; precisam ser fechados para liberar recursos. O instrutor reforça: **abra e feche** — nunca deixe conexões abertas.
5. O `id` não é informado: o `AUTO_INCREMENT` cria sozinho.

Tabela de métodos de execução:

| Método | Use para | Retorna |
|---|---|---|
| `executeUpdate(sql)` | INSERT / UPDATE / DELETE / DDL | `int` (linhas afetadas) |
| `executeQuery(sql)` | SELECT | `ResultSet` (próximos blocos) |
| `execute(sql)` | qualquer um (não sabe o tipo) | `boolean` (true se há `ResultSet`) |

## 3. Chamando

```java
Producer producer = Producer.builder().name("Mad House").build();
ProducerRepository.save(producer);
```

Conferindo no Workbench:

```sql
SELECT * FROM anime_store.producer;
```

Depuração útil: coloque um *breakpoint* e veja a `String sql` montada para confirmar que ficou exatamente como no Workbench.

## 4. ⚠️ O grande problema desta abordagem: **SQL Injection**

Montar o SQL **concatenando texto** é perigoso:

```java
String nome = "x'); DROP TABLE producer; --";
String sql = "INSERT INTO producer (name) VALUES ('" + nome + "')";
```

Um usuário malicioso pode **alterar a consulta**. Em aplicações reais use **`PreparedStatement`** (aulas 268–269), que separa o SQL dos valores. Aqui o `Statement` é usado só para aprender o básico.

## O que você precisa dominar (Aula 257)

- Entidade (domínio) e repositório.
- `Connection`, `Statement`, `executeUpdate`.
- Try-with-resources para fechar recursos.
- Por que concatenar SQL é perigoso (SQL Injection).

---

# Aula 258 — JDBC pt 07 — Lombok e Log4J2

## 1. Lombok — menos código repetitivo

**Lombok** é uma biblioteca que gera código em **tempo de compilação** a partir de anotações. Elimina getters, setters, construtores, `equals`, `hashCode`, `toString`, builders...

Dependência (escopo `provided`, pois só é necessária na compilação):

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.20</version>
    <scope>provided</scope>
</dependency>
```

**Na IDE**: habilitar *Annotation Processing* em *Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing* (e instalar o plugin do Lombok, se solicitado).

### Anotações principais

| Anotação | Gera |
|---|---|
| `@Getter` / `@Setter` | getters / setters |
| `@ToString` | `toString()` |
| `@EqualsAndHashCode` | `equals` e `hashCode` |
| `@NoArgsConstructor` / `@AllArgsConstructor` | construtores |
| `@Data` | `@Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor` |
| `@Value` | versão **imutável** (campos `private final`, só getters) |
| `@Builder` | o padrão Builder (bloco 29) automaticamente |
| `@Log4j2` | cria o campo `log` pronto para uso |

Exemplo: a classe `Producer` fica assim:

```java
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Producer {
    Integer id;
    String name;
}
```

Isso substitui dezenas de linhas. O instrutor confere no `target/classes` (o `.class` compilado) que o construtor, o `equals`, o `hashCode`, o `toString` e o `builder()` **foram realmente gerados**, e os atributos ficaram `final` (classe imutável).

Uso do builder gerado:

```java
Producer p = Producer.builder().name("Studio Ghibli").build();
```

> Observação: algumas empresas **não** permitem Lombok. Para quem está aprendendo, o instrutor sugere usá-lo; mas é importante saber escrever o código "na mão" também.

---

## 2. Log em vez de `System.out.println`

Em aplicações reais **não se usa `System.out.println`** para registrar eventos:

- É **lento** (I/O no console sincronizado).
- Não diz o **nível** (informação? aviso? erro?).
- Não tem **data/hora**, **nome da thread**, **classe de origem**.
- Não dá para **desligar** níveis ou **enviar para arquivo/serviço**.

Bibliotecas de log: **Log4j2**, Logback, SLF4J (fachada)...

### Dependência

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.14.1</version>
</dependency>
```

### Uso com Lombok

```java
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProducerRepository {

    public static void save(Producer producer) {
        ...
        int rows = stmt.executeUpdate(sql);
        log.info("Producer '{}' inserted, rows affected: {}", producer.getName(), rows);
    }
}
```

- `@Log4j2` cria `private static final Logger log = LogManager.getLogger(...)`.
- Os `{}` são *placeholders*: os argumentos são substituídos em ordem (mais eficiente que concatenar).

### Níveis de log (do mais detalhado ao mais grave)

| Nível | Uso |
|---|---|
| `TRACE` | detalhe extremo |
| `DEBUG` | informação para o desenvolvedor depurar (**não** deixar em produção: pode vazar dados sensíveis) |
| `INFO` | eventos normais ("pedido criado") |
| `WARN` | algo estranho, mas o sistema segue |
| `ERROR` | falha |
| `FATAL` | falha grave, sistema não consegue continuar |

Registrando erros com a exceção:

```java
} catch (SQLException e) {
    log.error("Error while trying to save producer '{}'", producer.getName(), e);
}
```

### Arquivo de configuração `log4j2.xml`

Precisa estar em **`src/main/resources/log4j2.xml`**. Exemplo mínimo (console):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{1} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="academy.devdojo" level="debug" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

- `%d` data/hora, `%t` thread, `%level` nível, `%logger{1}` nome curto da classe, `%msg` mensagem, `%n` quebra de linha.
- `Logger name="academy.devdojo" level="debug"`: tudo do seu pacote é registrado a partir de `DEBUG`.
- `Root level="info"`: para o resto (bibliotecas), só `INFO` ou acima.

Sem o `level="debug"`, as chamadas `log.debug(...)` **não aparecem**.

> Dica do instrutor: o nome longo do pacote pode ser reduzido com `%logger{1}`.

## O que você precisa dominar (Aula 258)

- O que o Lombok faz e as anotações principais (`@Value`, `@Builder`, `@Getter`, `@Data`, `@Log4j2`).
- Habilitar annotation processing.
- Por que usar log em vez de `println`.
- Níveis de log e o `log4j2.xml` em `resources`.
- Placeholders `{}`.

---

# Aula 259 — JDBC pt 08 — Deletando dados com `Statement`

## 1. Organizando em camadas

Para não misturar tudo, o instrutor introduz uma **camada de serviço**:

```text
Test / Main (simula a camada de visualização/controle)
        │
        ▼
ProducerService   ← regras de negócio (validações)
        │
        ▼
ProducerRepository ← acesso ao banco (SQL/JDBC)
        │
        ▼
ConnectionFactory  ← cria conexões
```

- `domain` (entidade) mapeia a tabela;
- `repository` conversa com o banco;
- `service` aplica as **regras de negócio**;
- Se o SQL mudar, só o repositório muda; se a regra mudar, só o serviço muda.

É o início da divisão em camadas, base de aplicações como as do Spring (MVC).

## 2. Repositório: `delete`

```java
@Log4j2
public class ProducerRepository {

    public static void delete(int id) {
        String sql = String.format("DELETE FROM `anime_store`.`producer` WHERE (`id` = '%d');", id);

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(sql);
            log.info("Deleted producer '{}' from the database, rows affected '{}'", id, rowsAffected);

        } catch (SQLException e) {
            log.error("Error while trying to delete producer '{}'", id, e);
        }
    }
}
```

Observações:

- O SQL do `DELETE` pode ser copiado do Workbench (botão direito → *Delete Row(s)* → mostrar a instrução gerada).
- Se nenhum registro tiver aquele `id`, `rowsAffected` será `0` — não é erro.
- ⚠️ Sem a cláusula `WHERE`, o `DELETE` apaga **todas** as linhas. Cuidado!

## 3. Serviço: regra de negócio

```java
public class ProducerService {

    public static void save(Producer producer) {
        ProducerRepository.save(producer);
    }

    public static void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid value for id");     // regra de negócio
        }
        ProducerRepository.delete(id);
    }
}
```

A validação acontece **antes** de ir ao banco (ids negativos nunca existirão).

O teste/controle passa a chamar o **serviço**, nunca o repositório diretamente:

```java
ProducerService.save(Producer.builder().name("Mad House").build());
ProducerService.delete(4);
```

## 4. Deletando vários de uma vez

Para vários ids, use `IN`:

```sql
DELETE FROM producer WHERE id IN (4, 5, 6, 7);
```

(o instrutor experimenta e acaba deletando os registros 4 a 11 e depois o 5, com a ressalva de que o `AUTO_INCREMENT` **não reaproveita** ids apagados — o próximo `id` continua de onde parou).

## 5. Resumo do que o bloco montou

```text
docker-compose  → MySQL em container
Workbench       → criar schema e tabelas
Maven           → dependências (driver, Lombok, Log4j2)
ConnectionFactory → DriverManager.getConnection
domain          → Producer (@Value @Builder)
repository      → save / delete com Statement.executeUpdate
service         → regras de negócio
log             → @Log4j2 + log4j2.xml
```

## O que você precisa dominar (Aula 259)

- `DELETE` com JDBC via `executeUpdate`.
- Camadas `domain` / `repository` / `service`.
- Validação de regra de negócio no serviço.
- `WHERE` e `IN`; `AUTO_INCREMENT` não reaproveita ids.

---

# Mapa mental do bloco

```text
JDBC (java.sql)  ← interfaces;  Driver MySQL (.jar) ← implementação
├── Ambiente: Docker (docker-compose.yml: image, env, ports, volumes) + Workbench + Maven (pom.xml)
├── Conexão:  DriverManager.getConnection("jdbc:mysql://localhost:3306/anime_store", user, pass)
├── Escrita:  Connection → Statement → executeUpdate(INSERT/UPDATE/DELETE)  → int linhas afetadas
├── Recursos: try-with-resources (fechar Connection e Statement)
├── Camadas:  domain → repository → service → (controle/teste)
├── Lombok:   @Value · @Builder · @Getter · @Data · @Log4j2
└── Log:      Log4j2 + log4j2.xml (src/main/resources) · níveis TRACE…FATAL · placeholders {}
```

# Cola de bolso

| Preciso... | Use |
|---|---|
| Subir o MySQL | `docker-compose up` |
| Parar e remover o container | `docker-compose down` |
| Conectar | `DriverManager.getConnection(url, user, pass)` |
| INSERT / UPDATE / DELETE | `statement.executeUpdate(sql)` |
| Evitar vazamento de conexão | `try (Connection c = ...; Statement s = ...)` |
| Menos getters/setters/builder | Lombok |
| Registrar eventos | `@Log4j2` + `log.info("...{}", valor)` |
| Regra de negócio antes do banco | camada `service` |
