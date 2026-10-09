# Bloco 17 — NIO: Atributos DOS/POSIX, Percorrendo Diretórios, PathMatcher e Zip

## Aulas 151 a 158

Este bloco fecha o assunto **NIO**. Você já sabe criar, copiar e resolver `Path`s. Agora vai aprender a:

- mexer em atributos específicos de **Windows (DOS)** e **Linux/Mac (POSIX)**;
- **listar** e **percorrer** diretórios (inclusive subpastas);
- **filtrar** arquivos por padrão de nome;
- **compactar** arquivos em um `.zip`.

As aulas deste bloco são:

```text
151 — NIO pt 08 — DosFileAttributes
152 — NIO pt 09 — PosixFileAttributes
153 — NIO pt 10 — DirectoryStream
154 — NIO pt 11 — SimpleFileVisitor pt 01
155 — NIO pt 12 — SimpleFileVisitor pt 02
156 — NIO pt 13 — PathMatcher pt 01
157 — NIO pt 14 — PathMatcher pt 02
158 — NIO pt 15 — ZipOutputStream
```

> ⚠️ **Aviso sobre a aula 156:** a transcrição da aula 156 (PathMatcher pt 01) veio com texto de outro idioma/áudio errado e **não tem conteúdo aproveitável**. Escrevi a seção dessa aula com base no que é explicado na aula 157 (que usa o `PathMatcher`) e no que é padrão da API. Se você quiser conferir algum detalhe específico do vídeo 156, assista a ele e me diga o que viu de diferente para eu ajustar.

## Visão geral

```text
Atributos específicos por sistema
   Windows → DosFileAttributes / DosFileAttributeView
   Linux   → PosixFileAttributes / PosixFileAttributeView
            ↓
Listar um nível        → DirectoryStream
Percorrer a árvore     → Files.walkFileTree + SimpleFileVisitor
Filtrar por nome       → PathMatcher (glob / regex)
Empacotar              → ZipOutputStream
```

---

# Aula 151 — NIO pt 08 — `DosFileAttributes`

## 1. O que é

`DosFileAttributes` estende `BasicFileAttributes` e acrescenta atributos específicos do **Windows (DOS)**:

| Atributo | Significado | Leitura | Escrita (View) |
|---|---|---|---|
| `isReadOnly()` | somente leitura | ✅ | `setReadOnly(boolean)` |
| `isHidden()` | oculto | ✅ | `setHidden(boolean)` |
| `isArchive()` | marcado para backup | ✅ | `setArchive(boolean)` |
| `isSystem()` | arquivo de sistema | ✅ | `setSystem(boolean)` |

Lembre da regra do bloco anterior:

```text
...Attributes      → LEITURA (retrato)
...AttributeView   → LEITURA e ALTERAÇÃO
```

---

## 2. Alterando pelo nome do atributo (`Files.setAttribute`)

Existe uma forma "por texto", que usa o formato `"<visão>:<atributo>"`:

```java
Path path = Paths.get("pasta2", "teste.txt");

Files.setAttribute(path, "dos:hidden", true);     // torna oculto
Files.setAttribute(path, "dos:readonly", true);   // torna somente leitura
```

Cuidados (o instrutor errou e corrigiu na aula):

- O prefixo e o nome devem estar **exatamente** assim, em minúsculas: `dos:hidden`, `dos:readonly`, `dos:archive`, `dos:system`.
- O valor deve ser do tipo certo (`boolean`).
- Um nome errado lança `IllegalArgumentException`.
- Em um sistema que não é Windows, a visão `dos` pode não existir (`UnsupportedOperationException`).

Quando um arquivo vira **oculto**, ele some do Explorer (a menos que você mostre arquivos ocultos). Com **somente leitura**, aparece um cadeado e tentar alterar o conteúdo falha.

Para desfazer, basta passar `false`:

```java
Files.setAttribute(path, "dos:hidden", false);
```

---

## 3. Forma orientada a objetos (recomendada)

**Lendo:**

```java
DosFileAttributes dos = Files.readAttributes(path, DosFileAttributes.class);

System.out.println("Somente leitura? " + dos.isReadOnly());
System.out.println("Oculto?           " + dos.isHidden());
System.out.println("Arquivo?          " + dos.isArchive());
System.out.println("Sistema?          " + dos.isSystem());
```

**Alterando:**

```java
DosFileAttributeView view = Files.getFileAttributeView(path, DosFileAttributeView.class);

view.setHidden(true);
view.setReadOnly(true);
```

Depois de alterar, **leia de novo** com `readAttributes` — o objeto lido antes continua mostrando os valores antigos.

---

## 4. A forma antiga (`File`)

A classe `File` já tinha `isHidden()` e `setReadOnly()`, mas era limitada (não conseguia, por exemplo, ocultar um arquivo no Windows). O NIO resolve.

## O que você precisa dominar (Aula 151)

- `DosFileAttributes` (leitura) e `DosFileAttributeView` (alteração).
- Atributos DOS: `hidden`, `readonly`, `archive`, `system`.
- `Files.setAttribute(path, "dos:hidden", true)` e o cuidado com o nome.
- Atributos DOS só fazem sentido no Windows.

---

# Aula 152 — NIO pt 09 — `PosixFileAttributes`

## 1. O que é POSIX?

POSIX é o padrão dos sistemas Unix (Linux, macOS). Nele, cada arquivo tem **dono**, **grupo** e **permissões**.

Executando `ls -l` no terminal:

```text
-rw-r--r--  1 william william  12 mai 10 14:00 arquivo.txt
│└┬┘└┬┘└┬┘
│ │  │  └─ OUTROS (convidados)
│ │  └──── GRUPO
│ └─────── DONO (criador)
└───────── tipo: "-" arquivo, "d" diretório
```

Cada conjunto tem três letras:

| Letra | Significa |
|---|---|
| `r` | read (ler) |
| `w` | write (escrever) |
| `x` | execute (executar / entrar na pasta) |
| `-` | sem a permissão |

`rw-r--r--` = dono lê e escreve; grupo só lê; outros só leem.

---

## 2. Lendo permissões

```java
Path path = Paths.get("/home/william/arquivo.txt");

PosixFileAttributes posix = Files.readAttributes(path, PosixFileAttributes.class);

System.out.println(posix.owner());          // dono
System.out.println(posix.group());          // grupo
System.out.println(posix.permissions());    // Set<PosixFilePermission>
System.out.println(PosixFilePermissions.toString(posix.permissions()));  // rw-r--r--
```

⚠️ **No Windows** isso lança `UnsupportedOperationException` (a visão POSIX não existe). Por isso o instrutor executa a aula em **Linux** (Ubuntu dentro do Windows, o WSL). Para quem só tem Windows, vale testar via WSL ou uma máquina virtual.

---

## 3. Alterando permissões

O método `setPermissions` recebe um `Set<PosixFilePermission>`. O instrutor avisa que ainda não estudamos `Set` (coleções vêm adiante). Não precisa montar manualmente: use o utilitário **`PosixFilePermissions.fromString`**, que converte o texto de 9 caracteres em um `Set`:

```java
Set<PosixFilePermission> permissoes = PosixFilePermissions.fromString("rwxrw-r--");

Files.setPosixFilePermissions(path, permissoes);
```

Ou pela view:

```java
PosixFileAttributeView view = Files.getFileAttributeView(path, PosixFileAttributeView.class);
view.setPermissions(permissoes);
```

> ⚠️ Cuidado com os nomes parecidos: **`PosixFilePermission`** (uma única permissão — enum) e **`PosixFilePermissions`** (com **s** — classe utilitária com `fromString`/`toString`).

Também é possível trocar dono e grupo (se tiver privilégio):

```java
view.setOwner(owner);
view.setGroup(group);
```

### Experimento da aula

1. Arquivo criado com `rw-r--r--`.
2. Programa altera para `rw-rw-r--` (ou outro valor).
3. `ls -l` mostra a mudança.

## 4. Comparando

| | DOS (Windows) | POSIX (Linux/Mac) |
|---|---|---|
| Foco | oculto, somente leitura, sistema, arquivo | dono, grupo, permissões rwx |
| Leitura | `DosFileAttributes` | `PosixFileAttributes` |
| Alteração | `DosFileAttributeView` | `PosixFileAttributeView` |
| Se o SO não suporta | `UnsupportedOperationException` | `UnsupportedOperationException` |

Para código portátil, verifique:

```java
boolean ehPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
```

## O que você precisa dominar (Aula 152)

- Leitura do `rwxrwxrwx`.
- `PosixFileAttributes`, `PosixFileAttributeView`.
- `PosixFilePermissions.fromString("rw-r--r--")`.
- Não funciona no Windows → `UnsupportedOperationException`.
- Diferença `PosixFilePermission` x `PosixFilePermissions`.

---

# Aula 153 — NIO pt 10 — `DirectoryStream`

## 1. Para que serve

`DirectoryStream` lista os itens de **um diretório** (apenas o **primeiro nível**), como o comando `dir`/`ls`.

```java
Path dir = Paths.get(".");

try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
    for (Path p : stream) {
        System.out.println(p.getFileName());
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

Pontos importantes:

1. **Genéricos**: `DirectoryStream<Path>` — o `<Path>` diz que cada elemento é um `Path`. Ainda veremos genéricos em detalhe; por enquanto, leia como "um fluxo de Path".
2. **Precisa ser fechado** (`Closeable`): use **try-with-resources**.
3. É iterável: dá para usar `for` aprimorado como se fosse um array.
4. O ponto `"."` representa o diretório atual.
5. **Inclui arquivos ocultos** (como `.git`, `.idea`).
6. **Não entra nas subpastas** — só mostra o que está no nível pedido.

---

## 2. Filtrando com padrão (glob)

Existe uma sobrecarga que aceita um padrão:

```java
try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{java,class}")) {
    for (Path p : stream) {
        System.out.println(p.getFileName());
    }
}
```

Isso lista somente arquivos `.java` e `.class` do nível atual.

(Os padrões *glob* são explicados na aula de `PathMatcher`.)

---

## 3. Filtro programático

```java
DirectoryStream.Filter<Path> soDiretorios = Files::isDirectory;
try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, soDiretorios)) {
    ...
}
```

Um `DirectoryStream.Filter` é uma interface funcional; você implementa `accept(Path)` e devolve `true` para incluir.

---

## 4. Quando usar?

- Listar o conteúdo de **uma** pasta → `DirectoryStream`.
- Percorrer **toda a árvore**, entrando nas subpastas → `Files.walkFileTree` com `SimpleFileVisitor` (próxima aula).

## O que você precisa dominar (Aula 153)

- `Files.newDirectoryStream(path)` e o `for`.
- Try-with-resources obrigatório.
- Lista só o primeiro nível, incluindo ocultos.
- Filtro com glob.

---

# Aula 154 — NIO pt 11 — `SimpleFileVisitor` (parte 1)

## 1. O problema do `DirectoryStream`

Ele não entra nas subpastas. Para **visitar toda a árvore**, o NIO oferece o **padrão Visitor**:

```java
Files.walkFileTree(Path inicio, FileVisitor<? super Path> visitor);
```

- O primeiro argumento é **onde começar**.
- O segundo é **o que fazer** em cada arquivo/pasta encontrado.

A API navega sozinha por todas as subpastas e **chama os seus métodos** (callbacks) em cada etapa. Isso é polimorfismo: o Java chama métodos da sua classe.

---

## 2. `SimpleFileVisitor<T>`

`FileVisitor` é uma interface com 4 métodos. Para não precisar implementar todos, existe a classe **`SimpleFileVisitor<T>`**, que já os implementa com comportamento padrão ("continuar") e permite **sobrescrever apenas o que interessa**.

```java
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class SimpleFileVisitorTest01 {
    public static void main(String[] args) throws IOException {
        Path inicio = Paths.get(".");
        Files.walkFileTree(inicio, new ListAllFiles());
    }
}

class ListAllFiles extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        System.out.println(file.getFileName());
        return FileVisitResult.CONTINUE;
    }
}
```

Pontos:

- `SimpleFileVisitor<Path>`: o `<Path>` define o tipo dos argumentos (genéricos).
- `visitFile` é chamado **para cada arquivo** encontrado. Recebe o `Path` e os `BasicFileAttributes`.
- O retorno é um enum `FileVisitResult` que diz ao Java como continuar.
- Em um mesmo arquivo `.java` pode haver várias classes, mas só uma `public` com o nome do arquivo.

---

## 3. `FileVisitResult`

| Valor | Efeito |
|---|---|
| `CONTINUE` | segue visitando normalmente |
| `TERMINATE` | **encerra** toda a busca |
| `SKIP_SUBTREE` | pula o conteúdo da pasta atual (usado em `preVisitDirectory`) |
| `SKIP_SIBLINGS` | pula os "irmãos" (itens do mesmo nível) restantes |

---

## 4. Mini-desafio da aula: só `.java`

"Como imprimir apenas os arquivos `.java` e ignorar o resto?" Basta filtrar dentro do `visitFile`:

```java
@Override
public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (file.getFileName().toString().endsWith(".java")) {
        System.out.println(file.getFileName());
    }
    return FileVisitResult.CONTINUE;
}
```

Isso mostra o poder do visitor: **você decide a lógica** (imprimir, copiar, apagar, alterar permissões, medir tamanho...), e a API cuida da navegação.

## O que você precisa dominar (Aula 154)

- `Files.walkFileTree(inicio, visitor)`.
- Estender `SimpleFileVisitor<Path>` e sobrescrever `visitFile`.
- `FileVisitResult` e o que cada valor faz.
- Diferença para `DirectoryStream`: o visitor percorre **recursivamente**.

---

# Aula 155 — NIO pt 12 — `SimpleFileVisitor` (parte 2)

## 1. Os quatro métodos do visitor

```text
preVisitDirectory(dir, attrs)   → ANTES de entrar em uma pasta
visitFile(file, attrs)          → para cada ARQUIVO
visitFileFailed(file, exc)      → quando NÃO consegue acessar o arquivo
postVisitDirectory(dir, exc)    → DEPOIS de visitar tudo dentro da pasta
```

Todos retornam `FileVisitResult`.

---

## 2. Montando um cenário

Estrutura de teste:

```text
pasta/
 ├── subpasta1/
 │    ├── subsubpasta1/
 │    │     └── sub_sub_arquivo1.txt
 │    └── sub_arquivo1.txt
 └── subpasta2/
      └── sub_arquivo2.txt
```

---

## 3. `preVisitDirectory` (ao entrar)

```java
@Override
public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    System.out.println("Entrando em: " + dir.getFileName());
    return FileVisitResult.CONTINUE;
}
```

Executa **antes** de entrar em cada pasta. O instrutor observa que a ordem exata de visita entre irmãos não é garantida pela API.

Se retornar `SKIP_SUBTREE`, o conteúdo da pasta é ignorado:

```java
if (dir.getFileName().toString().equals(".git")) {
    return FileVisitResult.SKIP_SUBTREE;   // nunca entra em .git
}
```

Se retornar `SKIP_SIBLINGS`, os outros itens do mesmo nível são ignorados.

---

## 4. `postVisitDirectory` (ao sair)

```java
@Override
public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    System.out.println("Saindo de: " + dir.getFileName());
    return FileVisitResult.CONTINUE;
}
```

Executa **depois** que tudo que está dentro da pasta foi visitado. A ordem é do mais profundo para o mais raso.

Pela documentação, só faz sentido retornar `SKIP_SIBLINGS` aqui para pular irmãos; `SKIP_SUBTREE` não faz sentido, porque a subárvore já foi visitada.

> Aplicação clássica: **apagar uma pasta com conteúdo**. Apaga-se os arquivos em `visitFile` e a pasta (já vazia) em `postVisitDirectory`.

---

## 5. `visitFileFailed`

Chamado quando o arquivo não pôde ser lido (falta de permissão, arquivo removido durante a varredura...). Aqui você decide: registrar, ignorar ou abortar.

```java
@Override
public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    System.out.println("Falha ao ler " + file + ": " + exc.getMessage());
    return FileVisitResult.CONTINUE;
}
```

---

## 6. Exemplo: apagando uma árvore inteira

```java
Files.walkFileTree(pasta, new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
    }
});
```

(Cuidado: **só** teste dentro da pasta de exercícios!)

## O que você precisa dominar (Aula 155)

- Os 4 métodos do visitor e quando cada um é chamado.
- `SKIP_SUBTREE` e `SKIP_SIBLINGS` em `preVisitDirectory`.
- `postVisitDirectory` após visitar tudo dentro da pasta.
- `visitFileFailed` para tratar falhas.

---

# Aula 156 — NIO pt 13 — `PathMatcher` (parte 1)

> ⚠️ A transcrição desta aula está inutilizável (texto em outro idioma). O conteúdo abaixo é o **padrão da API**, coerente com o uso feito na aula 157.

## 1. O que é

`PathMatcher` é uma interface que responde: "este `Path` casa com este padrão?".

```java
PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.java");

Path p = Paths.get("Teste01.java");
System.out.println(matcher.matches(p));   // true
```

O texto passado a `getPathMatcher` tem o formato:

```text
sintaxe:padrão
```

---

## 2. As duas sintaxes

| Prefixo | Tipo | Exemplo |
|---|---|---|
| `glob:` | padrão simples (como no terminal) | `glob:*.java` |
| `regex:` | expressão regular (a do bloco 14!) | `regex:.*\\.java` |

Um erro de sintaxe lança `IllegalArgumentException`; um prefixo desconhecido, `UnsupportedOperationException`.

---

## 3. Sintaxe glob

| Padrão | Significa |
|---|---|
| `*` | zero ou mais caracteres, **sem** cruzar `/` |
| `**` | zero ou mais caracteres, **cruzando** diretórios |
| `?` | exatamente um caractere |
| `[abc]` | um caractere dentre os listados |
| `[a-z]` | um caractere no intervalo |
| `{java,class}` | uma das alternativas (grupo) |
| `\` | escapa um caractere especial |

Exemplos:

```text
glob:*.java              → arquivos terminados em .java (só o nome)
glob:*.{java,class}      → .java OU .class
glob:Teste??.java        → Teste01.java, TesteAB.java
glob:**/*.txt            → .txt em qualquer subpasta
glob:[A-Z]*              → começa com letra maiúscula
```

### Armadilha importante

O `matches(Path)` compara o padrão com o **Path inteiro**, não apenas com o nome do arquivo. Por isso, ao percorrer arquivos, o comum é testar `file.getFileName()`:

```java
matcher.matches(file.getFileName())
```

Senão, `glob:*.java` falharia para `src/aula/Teste.java` (o `*` não atravessa o `/`). Alternativa: usar `glob:**.java` ou `glob:**/*.java`.

## O que você precisa dominar (Aula 156)

- `FileSystems.getDefault().getPathMatcher("glob:...")`.
- Prefixos `glob:` e `regex:`.
- Curingas `*`, `**`, `?`, `[ ]`, `{ }`.
- Testar com `getFileName()`.

---

# Aula 157 — NIO pt 14 — `PathMatcher` (parte 2) — exercício

## 1. Enunciado

Usando `SimpleFileVisitor` + `PathMatcher`, retornar **todos os arquivos** do projeto cujo nome:

1. **começa** com `Teste` (com qualquer coisa depois, como `Teste01`, `Teste02`...);
2. e termina com `.java` **ou** `.class`.

---

## 2. Solução (a ideia da aula)

```java
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class PathMatcherTest01 {
    public static void main(String[] args) throws IOException {
        Files.walkFileTree(Paths.get("."), new FindTestFiles());
    }
}

class FindTestFiles extends SimpleFileVisitor<Path> {

    private final PathMatcher matcher =
            FileSystems.getDefault().getPathMatcher("glob:Teste*.{java,class}");

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (matcher.matches(file.getFileName())) {
            System.out.println(file);
        }
        return FileVisitResult.CONTINUE;
    }
}
```

Leitura do padrão `Teste*.{java,class}`:

```text
Teste          → começa com a palavra Teste
*              → qualquer coisa (inclusive nada)
.              → um ponto
{java,class}   → java OU class
```

---

## 3. O que o instrutor aprendeu testando

- Na primeira tentativa o programa **lançou exceção** — havia um problema de sintaxe no padrão. Ele simplificou e foi testando por partes.
- Moral dita por ele: **"Se você tem dúvida se funciona, testa."**

Dica prática: monte o `glob` aos poucos, testando cada pedaço com poucos arquivos.

### Observação: a ordem dos filtros

Compile a `PathMatcher` **uma vez** (campo da classe) e reutilize, em vez de recriar a cada arquivo.

## O que você precisa dominar (Aula 157)

- Combinar `walkFileTree` + `PathMatcher`.
- Padrão `Teste*.{java,class}`.
- Testar em partes quando algo dá exceção.

---

# Aula 158 — NIO pt 15 — `ZipOutputStream`

## 1. Objetivo

Criar um arquivo `.zip` com o conteúdo de uma pasta. A aula usa `ZipOutputStream` (do pacote `java.util.zip`), uma stream de **saída** binária (o sufixo `OutputStream` = "escreve bytes para algum lugar", enquanto `InputStream` lê).

---

## 2. Passo a passo

**Entradas do método:**

```java
Path origem = Paths.get("pasta", "subpasta1");        // pasta a compactar
Path zipFile = Paths.get("pasta", "arquivo.zip");     // zip a criar
```

**Esqueleto:**

```java
import java.io.IOException;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipOutputStreamTest01 {
    public static void main(String[] args) {
        Path origem = Paths.get("pasta", "subpasta1");
        Path zipFile = Paths.get("pasta", "arquivo.zip");
        zip(zipFile, origem);
    }

    private static void zip(Path zipFile, Path origem) {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(zipFile));
             DirectoryStream<Path> arquivos = Files.newDirectoryStream(origem)) {

            for (Path arquivo : arquivos) {
                ZipEntry entrada = new ZipEntry(arquivo.getFileName().toString());
                zip.putNextEntry(entrada);        // 1) prepara a entrada
                Files.copy(arquivo, zip);         // 2) copia o conteúdo
                zip.closeEntry();                 // 3) fecha a entrada
            }
            System.out.println("Zip criado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 3. Os três passos obrigatórios por arquivo

```text
1) putNextEntry(new ZipEntry(nome))   → "reserva o lugar" do arquivo dentro do zip
2) Files.copy(arquivo, zip)           → escreve o CONTEÚDO
3) closeEntry()                       → fecha aquela entrada
```

O instrutor mostrou o que acontece se você esquecer o passo 2: o zip é criado e contém os arquivos, **mas todos vazios** (só o "esqueleto"). O conteúdo só aparece quando se faz o `Files.copy`.

### Conversões usadas

- `Files.newOutputStream(path)` cria o `OutputStream` que `ZipOutputStream` precisa.
- `ZipEntry(String nome)` — o nome dentro do zip (use `getFileName().toString()`).

### Fechamento

`ZipOutputStream` e `DirectoryStream` são `Closeable` → declarados no **try-with-resources**.

---

## 4. Limitações da versão da aula

- Só empacota **um nível** (usa `DirectoryStream`). Para subpastas, troque por `walkFileTree` e use caminhos relativos (`origem.relativize(arquivo)`) como nome da entrada.
- Não protege com senha.

Versão recursiva (ideia):

```java
Files.walkFileTree(origem, new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String nome = origem.relativize(file).toString().replace('\\', '/');
        zip.putNextEntry(new ZipEntry(nome));
        Files.copy(file, zip);
        zip.closeEntry();
        return FileVisitResult.CONTINUE;
    }
});
```

## 5. Para ler (descompactar)

O caminho inverso usa `ZipInputStream`/`ZipFile`: lê cada `ZipEntry` e copia para o disco.

## O que você precisa dominar (Aula 158)

- `ZipOutputStream` sobre `Files.newOutputStream`.
- O trio `putNextEntry` → `Files.copy` → `closeEntry`.
- Esquecer o `copy` gera arquivos vazios.
- Try-with-resources e `IOException`.

---

# Mapa mental do bloco

```text
NIO — avançado
├── Atributos por sistema
│   ├── DOS    → hidden, readonly, archive, system (Windows)
│   └── POSIX  → owner, group, permissions rwx (Linux/Mac)
├── Listar
│   ├── DirectoryStream → 1 nível, try-with-resources, glob
│   └── Files.walkFileTree + SimpleFileVisitor → árvore toda
│        ├── preVisitDirectory / postVisitDirectory
│        ├── visitFile / visitFileFailed
│        └── FileVisitResult: CONTINUE, TERMINATE, SKIP_SUBTREE, SKIP_SIBLINGS
├── Filtrar
│   └── PathMatcher: "glob:..." ou "regex:..."
└── Compactar
    └── ZipOutputStream: putNextEntry → copy → closeEntry
```

# Cola de bolso

| Quero... | Use |
|---|---|
| Ocultar um arquivo no Windows | `Files.setAttribute(p, "dos:hidden", true)` |
| Mudar permissão no Linux | `Files.setPosixFilePermissions(p, PosixFilePermissions.fromString("rw-r--r--"))` |
| Listar uma pasta | `Files.newDirectoryStream(dir)` |
| Percorrer toda a árvore | `Files.walkFileTree(inicio, new SimpleFileVisitor<Path>(){...})` |
| Ignorar uma pasta | `preVisitDirectory` → `SKIP_SUBTREE` |
| Apagar pasta cheia | `visitFile` apaga arquivo; `postVisitDirectory` apaga a pasta |
| Filtrar `.java`/`.class` | `getPathMatcher("glob:*.{java,class}")` |
| Compactar arquivos | `ZipOutputStream` + `ZipEntry` |
