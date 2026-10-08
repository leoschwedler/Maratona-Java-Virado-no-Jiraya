# Bloco 15 — Entrada e Saída (IO): Arquivos e Diretórios

## Aulas 138 a 143

Este bloco inicia o assunto de **Entrada e Saída de dados (I/O — Input/Output)**. O instrutor avisa que é um assunto longo, mas um dos mais legais: você escreve código e vê o resultado "fisicamente", criando, lendo e apagando arquivos de verdade no seu computador.

As aulas deste bloco são:

```text
138 — IO pt 01 — File
139 — IO pt 02 — FileWriter
140 — IO pt 03 — FileReader
141 — IO pt 04 — BufferedWriter
142 — IO pt 05 — BufferedReader
143 — IO pt 06 — File para diretórios
```

Pacote: `java.io`.

## A grande ideia: cada classe tem UM propósito

O pacote `java.io` é composto por classes pequenas e **coesas** (lembra da aula de coesão?). Cada uma faz uma coisa só, e você **encadeia** (encapsula) uma dentro da outra para somar funcionalidades.

```text
File           → representa o caminho/arquivo/pasta no sistema (NÃO lê nem escreve)
FileWriter     → escreve caracteres no arquivo (baixo nível)
FileReader     → lê caracteres do arquivo (baixo nível)
BufferedWriter → coloca um BUFFER (memória) na frente do FileWriter → mais rápido
BufferedReader → coloca um BUFFER na frente do FileReader → mais rápido e lê linhas
```

Encadeamento:

```text
new BufferedWriter( new FileWriter( new File("arquivo.txt") ) )
        ▲                 ▲                  ▲
   buffer + newLine   escreve texto     aponta o arquivo
```

---

# Aula 138 — IO pt 01 — `File`

## 1. O que é a classe `File`?

`File` é uma **representação abstrata de um caminho** no sistema de arquivos. Pode representar:

- um **arquivo** (`.txt`);
- uma **pasta/diretório**;
- algo que **ainda não existe**.

> Importante: criar `new File("x.txt")` **NÃO cria o arquivo no disco.** Só cria um objeto Java que aponta para esse caminho.

```java
import java.io.File;

File file = new File("arquivo.txt");   // apenas uma referência
```

---

## 2. Caminho relativo x absoluto

| Tipo | Exemplo | Onde cria |
|---|---|---|
| **Relativo** | `new File("arquivo.txt")` | na pasta de onde o programa é executado (raiz do projeto, no IntelliJ) |
| **Absoluto** | `new File("C:\\Users\\dev1\\arquivo.txt")` | no local exato |

⚠️ No Windows, o `\` precisa ser **escapado** em String Java (`\\`). Alternativa: usar `/`, que o Java aceita em todos os sistemas, ou usar `File.separator`.

```java
File f = new File("C:" + File.separator + "temp" + File.separator + "a.txt");
```

---

## 3. Criando o arquivo de verdade: `createNewFile()`

```java
import java.io.File;
import java.io.IOException;

public class FileTest01 {
    public static void main(String[] args) {
        File file = new File("arquivo.txt");

        try {
            boolean criado = file.createNewFile();
            System.out.println("Criado? " + criado);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

- Retorna `true` se criou.
- Retorna `false` se o arquivo **já existia** (não sobrescreve, não dá erro).
- Lança `IOException` (checked): disco cheio, sem permissão, pasta inexistente etc.

---

## 4. Outros métodos principais

```java
File file = new File("arquivo.txt");

file.exists();            // true/false — existe?
file.delete();            // apaga; retorna boolean (false se não existia)
file.getName();           // "arquivo.txt"
file.getPath();           // caminho informado (relativo se foi relativo)
file.getAbsolutePath();   // caminho completo
file.isDirectory();       // é uma pasta?
file.isFile();            // é um arquivo?
file.isHidden();          // está oculto?
file.canRead();           // posso ler?
file.canWrite();          // posso escrever?
file.length();            // tamanho em bytes
file.lastModified();      // long (milissegundos desde 1970)
```

### Boa prática

Antes de apagar, verifique se existe:

```java
if (file.exists()) {
    boolean apagado = file.delete();
    System.out.println("Apagado: " + apagado);
}
```

Chamar `delete()` num arquivo inexistente **não lança exceção**; só retorna `false`.

---

## 5. `lastModified()` e as classes de data

`lastModified()` devolve um `long` em milissegundos desde 01/01/1970 (você já viu isso na aula do `Date`). Para ler como data:

```java
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

long ms = file.lastModified();

// Jeito antigo
System.out.println(new java.util.Date(ms));

// Jeito moderno
LocalDateTime ultimaModificacao = Instant.ofEpochMilli(ms)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
System.out.println(ultimaModificacao);
```

Isso junta os dois mundos: IO + `java.time`.

## 6. Limitação da classe `File`

`File` só **gerencia** o arquivo (criar, apagar, perguntar propriedades). Para **escrever e ler conteúdo** precisamos de outras classes (próximas aulas). Existem também melhorias modernas no pacote `java.nio` (que o curso verá adiante).

## O que você precisa dominar (Aula 138)

- `new File(...)` não cria arquivo em disco.
- `createNewFile()`, `exists()`, `delete()`.
- Caminho relativo x absoluto e o `\\` no Windows.
- `getName`, `getPath`, `getAbsolutePath`, `isDirectory`, `isFile`, `length`, `lastModified`.
- `IOException` é checked.

---

# Aula 139 — IO pt 02 — `FileWriter`

## 1. O que é

`FileWriter` **escreve caracteres** em um arquivo. É uma classe de **baixo nível** (sem buffer): cada escrita vai direto para o recurso do sistema.

---

## 2. Recursos precisam ser fechados

Ao escrever em arquivo você usa um **recurso do sistema operacional**. Se não fechar, pode gerar:

- arquivo bloqueado;
- dados que nunca chegam ao disco;
- vazamento de recursos.

A forma moderna é o **try-with-resources** (visto no bloco de exceções): qualquer classe que implementa `AutoCloseable` (como `FileWriter`) é fechada automaticamente.

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterTest01 {
    public static void main(String[] args) {
        File file = new File("arquivo.txt");

        try (FileWriter fw = new FileWriter(file)) {
            fw.write("O Java é muito legal!");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

Observações:

- O próprio `FileWriter` **cria o arquivo** se não existir (não precisa chamar `createNewFile`).
- Você pode passar o `File` ou o nome do arquivo em String.

---

## 3. `flush()` — o túnel (buffer)

O instrutor usa uma analogia: ao assistir um vídeo no YouTube, a barrinha cinza é um **buffer** — uma área de memória intermediária onde os dados esperam antes de chegar ao destino.

Na escrita é a mesma coisa:

```text
seu código ──write()──▶ [BUFFER na memória] ──flush()──▶ arquivo no disco
```

`flush()` **descarrega** o que está no buffer para o arquivo. Se o programa fechar sem isso, parte do conteúdo pode se perder.

> O `close()` já executa um `flush()` internamente, mas é bom hábito chamar `flush()` antes de encerrar quando se escreve em blocos.

---

## 4. Sobrescrever x Acrescentar

Por padrão o `FileWriter` **apaga tudo** que existia e escreve do zero:

```java
new FileWriter(file);          // sobrescreve (padrão)
```

Para **acrescentar ao final** do arquivo (append), passe `true` como segundo argumento:

```java
new FileWriter(file, true);    // append
```

Testando:

```java
try (FileWriter fw = new FileWriter(file, true)) {
    fw.write("Mais uma linha\n");
}
// executando 3 vezes → o texto aparece 3 vezes no arquivo
```

---

## 5. Pulando linha: o problema do `\n`

`FileWriter` é de baixo nível: **não tem** método "escreva uma linha". Você precisa escrever o caractere de nova linha:

```java
fw.write("linha 1\n");   // funciona no Linux/Mac; no Windows o ideal é \r\n
```

O instrutor alerta: `\n` **não é portátil** (Windows usa `\r\n`). A solução correta aparece na próxima aula, com `BufferedWriter.newLine()`, que usa o separador do sistema operacional.

Alternativa: `System.lineSeparator()`.

```java
fw.write("linha 1" + System.lineSeparator());
```

## 6. Outros métodos

```java
fw.write(int c);              // um caractere
fw.write(char[] c);           // array de chars
fw.write(String s);           // texto
fw.append("texto");           // também aceita encadeamento
```

## O que você precisa dominar (Aula 139)

- `FileWriter` escreve texto, baixo nível, sem buffer.
- Sempre fechar recursos → try-with-resources.
- `flush()` e a ideia de buffer.
- Sobrescreve por padrão; `true` no construtor = append.
- `\n` não é portátil; use `System.lineSeparator()`.

---

# Aula 140 — IO pt 03 — `FileReader`

## 1. O que é

`FileReader` **lê caracteres** de um arquivo. Também de baixo nível.

```java
try (FileReader fr = new FileReader(file)) {
    // lê
} catch (IOException e) {
    e.printStackTrace();
}
```

Se o arquivo não existir → `FileNotFoundException` (subclasse de `IOException`).

---

## 2. Lendo um caractere: `read()`

```java
int c = fr.read();
System.out.println(c);          // 65 (código da letra 'A')
System.out.println((char) c);   // A
```

- `read()` devolve um **`int`** com o código do caractere.
- Para ver a letra, faça cast: `(char) c`.
- Quando chega ao fim do arquivo, devolve **`-1`**.

---

## 3. Lendo para dentro de um array: `read(char[])`

```java
char[] buffer = new char[30];
int lidos = fr.read(buffer);          // preenche o array

for (char c : buffer) {
    System.out.print(c);
}
```

⚠️ Aqui `read` devolve a **quantidade de caracteres lidos** (não o caractere). Se o arquivo tiver menos do que o tamanho do array, as posições restantes ficam vazias (`\u0000`); se tiver mais, você só lê parte.

---

## 4. Lendo o arquivo inteiro

Usa-se um laço que lê até encontrar `-1`:

```java
try (FileReader fr = new FileReader(file)) {
    int i;
    while ((i = fr.read()) != -1) {
        System.out.print((char) i);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

O truque `while ((i = fr.read()) != -1)`:

1. atribui o retorno de `read()` à variável `i`;
2. compara com `-1`;
3. repete até o fim.

Versão com array (mais eficiente, lê blocos de uma vez):

```java
char[] buffer = new char[1024];
int lidos;
while ((lidos = fr.read(buffer)) != -1) {
    System.out.print(new String(buffer, 0, lidos));
}
```

Note que usamos só as posições que foram realmente preenchidas (`0` até `lidos`).

## 5. Por que não é ideal?

Ler caractere por caractere é lento e trabalhoso. É por isso que vamos usar `BufferedReader` (aula 142).

## O que você precisa dominar (Aula 140)

- `FileReader` e `FileNotFoundException`.
- `read()` devolve `int` (código do char), `-1` = fim.
- `read(char[])` devolve quantidade lida.
- O laço `while ((i = fr.read()) != -1)`.
- try-with-resources para fechar.

---

# Aula 141 — IO pt 04 — `BufferedWriter`

## 1. Por que existe

`FileWriter` e `FileReader` são de **baixo nível**: não foram feitos pensando em desempenho. Para arquivos grandes, é melhor usar as versões **bufferizadas**.

`BufferedWriter` não escreve sozinho: ele **recebe um `Writer`** (como o `FileWriter`) e coloca um **buffer** na frente. Os dados ficam na memória e são enviados ao disco em blocos maiores, o que é muito mais rápido.

```text
Escrita sem buffer:  write → disco, write → disco, write → disco ...  (lento)
Escrita com buffer:  write → memória, write → memória ... → flush → disco (rápido)
```

---

## 2. Encadeamento

```java
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedWriterTest01 {
    public static void main(String[] args) {
        File file = new File("arquivo.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("Primeira linha");
            bw.newLine();
            bw.write("Segunda linha");
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

Para acrescentar ao final, o `true` vai no **`FileWriter`**:

```java
new BufferedWriter(new FileWriter(file, true))
```

Também é possível definir o tamanho do buffer (segundo parâmetro do construtor), mas o padrão é suficiente na maioria dos casos.

---

## 3. `newLine()` — nova linha portátil

```java
bw.newLine();
```

Escreve o separador de linha **do sistema operacional**: `\r\n` no Windows, `\n` no Linux/Mac. É por isso que é preferível em vez de `"\n"`.

---

## 4. `flush()` e `close()`

- `flush()` envia o que está no buffer para o arquivo.
- O `close()` (feito automaticamente pelo try-with-resources) faz o `flush()` e fecha também o `FileWriter` de dentro.

Quando você tem várias camadas, **só precisa fechar a de fora**: o `BufferedWriter` fecha o `FileWriter`.

## 5. Dica do instrutor

Existem plugins para colorir parênteses aninhados no IntelliJ (*Rainbow Brackets*), o que ajuda a visualizar o encadeamento de classes.

## O que você precisa dominar (Aula 141)

- `BufferedWriter` = buffer + `Writer`.
- Encadeamento `new BufferedWriter(new FileWriter(file))`.
- `newLine()` x `\n`.
- `flush()` e fechamento em cadeia.
- Append no `FileWriter`.

---

# Aula 142 — IO pt 05 — `BufferedReader`

## 1. Por que existe

Igual ao `BufferedWriter`, mas para leitura: coloca um buffer na frente de um `Reader`. E tem uma grande vantagem: lê **linha inteira**.

```java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BufferedReaderTest01 {
    public static void main(String[] args) {
        File file = new File("arquivo.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 2. `readLine()`

- Devolve a próxima linha **sem** o caractere de quebra de linha.
- Quando chega ao fim do arquivo, devolve **`null`** (e não `-1`!).

Compare:

| Classe | Método | Fim do arquivo |
|---|---|---|
| `FileReader` | `read()` (int) | `-1` |
| `BufferedReader` | `readLine()` (String) | `null` |

---

## 3. Outros métodos úteis

```java
br.read();            // ainda lê um char
br.ready();           // tem algo para ler?
br.lines();           // Stream<String> (Java 8; veremos em Streams)
```

---

## 4. Por que várias classes e não uma só?

O instrutor reforça o princípio por trás do design:

> Cada classe tem um **propósito específico**; encadeá-las deixa o código **flexível e coeso**. Você escolhe os "blocos" que precisa.

Exemplo de montagem:

```text
Quero ler texto de arquivo      → FileReader
Quero desempenho + readLine     → BufferedReader(FileReader)
Quero ler de outra fonte        → BufferedReader(InputStreamReader(System.in))
```

Sim: é possível ler do teclado com um `BufferedReader` também.

## O que você precisa dominar (Aula 142)

- `BufferedReader` = buffer + `Reader`.
- `readLine()` retorna `null` no fim.
- Diferença entre `-1` e `null`.
- Encadeamento e try-with-resources.
- Por que encadear classes pequenas.

---

# Aula 143 — IO pt 06 — `File` com diretórios

## 1. Criando pastas

```java
File dir = new File("pasta");
boolean criada = dir.mkdir();
System.out.println(criada);   // true na primeira vez, false se já existir
```

| Método | Faz |
|---|---|
| `mkdir()` | cria **uma** pasta (o pai precisa existir) |
| `mkdirs()` | cria a pasta **e todas as intermediárias** |

```java
new File("a/b/c").mkdirs();   // cria a, depois b, depois c
```

Se a pasta já existe, `mkdir()` devolve `false` e o objeto `File` continua apontando para ela (útil como "referência").

---

## 2. Criando um arquivo dentro de uma pasta

**Forma 1** — caminho completo:

```java
File arquivo = new File("pasta/arquivo.txt");
arquivo.createNewFile();
```

**Forma 2** — passando o `File` da pasta como "pai":

```java
File diretorio = new File("pasta");
diretorio.mkdir();

File arquivo = new File(diretorio, "arquivo.txt");   // pai + nome do filho
arquivo.createNewFile();
```

Se você esquecer o diretório e usar apenas `new File("arquivo.txt")`, o arquivo vai para a **raiz do projeto**, não para a pasta.

> O construtor `new File(File pai, String filho)` é o que o instrutor mais mostra.

---

## 3. Renomeando: `renameTo()`

```java
File original = new File(diretorio, "arquivo.txt");
File novoNome = new File(diretorio, "arquivoRenomeado.txt");

boolean renomeou = original.renameTo(novoNome);
System.out.println(renomeou);
```

Pontos importantes que o instrutor demonstra:

1. `renameTo` recebe **outro `File`** (não uma String).
2. O `File` de destino define **o novo caminho completo**: se você esquecer a pasta, o arquivo é **movido** para a raiz do projeto (renomear e mover são a mesma operação).
3. Se o destino já existir, `renameTo` retorna `false` e nada acontece.
4. Funciona também para **pastas**:

```java
File pasta = new File("pasta");
File pasta2 = new File("pasta2");
pasta.renameTo(pasta2);
```

O retorno é `boolean`: confira sempre se deu certo.

### Dica de depuração do instrutor

Use *breakpoints* e acompanhe a árvore de pastas do projeto enquanto executa passo a passo — é a melhor forma de enxergar o que cada linha faz no disco.

---

## 4. Listando o conteúdo de uma pasta

(Complemento natural da aula)

```java
File pasta = new File("pasta");

String[] nomes = pasta.list();          // só os nomes
for (String nome : nomes) {
    System.out.println(nome);
}

File[] arquivos = pasta.listFiles();    // objetos File
for (File f : arquivos) {
    System.out.println(f.getName() + (f.isDirectory() ? " [pasta]" : " [arquivo]"));
}
```

`list()`/`listFiles()` retornam `null` se a pasta não existir — verifique antes.

## 5. Apagando uma pasta

`delete()` só apaga pastas **vazias**. Para apagar uma pasta cheia é preciso apagar o conteúdo antes (recursão — assunto de exercício).

## O que você precisa dominar (Aula 143)

- `mkdir()` x `mkdirs()`.
- `new File(File pai, String filho)`.
- `renameTo(File)` renomeia **e** move.
- `list()` e `listFiles()`.
- `delete()` só em pasta vazia.
- Checar sempre o retorno `boolean`.

---

# Mapa mental do bloco

```text
java.io
├── File                → gerencia caminho (arquivo/pasta): create, delete, exists, rename, mkdir
├── Escrita
│   ├── FileWriter      → baixo nível, sobrescreve ou append(true)
│   └── BufferedWriter  → buffer + newLine() + flush()
└── Leitura
    ├── FileReader      → read() → int / -1 no fim
    └── BufferedReader  → readLine() → String / null no fim
```

# Cola de bolso

| Quero... | Use |
|---|---|
| Saber se o arquivo existe | `file.exists()` |
| Criar arquivo vazio | `file.createNewFile()` |
| Escrever texto simples | `new FileWriter(file)` |
| Acrescentar ao final | `new FileWriter(file, true)` |
| Escrever rápido com linhas | `new BufferedWriter(new FileWriter(file))` + `newLine()` |
| Ler linha por linha | `new BufferedReader(new FileReader(file))` + `readLine()` |
| Criar pasta | `mkdir()` / `mkdirs()` |
| Renomear ou mover | `renameTo(novoFile)` |
| Fechar sem esquecer | try-with-resources |
