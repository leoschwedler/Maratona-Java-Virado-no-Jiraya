# Bloco 16 — NIO: `Path`, `Paths`, `Files` e Atributos

## Aulas 144 a 150

Neste bloco você sai do `java.io` clássico (`File`, `FileWriter`...) e entra no pacote **NIO** (*New I/O*), mais precisamente no **NIO.2** (Java 7):

```java
java.nio.file
```

As aulas deste bloco são:

```text
144 — NIO pt 01 — Path, Paths, Files pt 01
145 — NIO pt 02 — Path, Paths, Files pt 02
146 — NIO pt 03 — Normalização
147 — NIO pt 04 — Resolvendo Paths
148 — NIO pt 05 — Relativize
149 — NIO pt 06 — BasicFileAttributes pt 01
150 — NIO pt 07 — BasicFileAttributes pt 02
```

## Por que o NIO foi criado?

A classe `File` do `java.io` tem problemas conhecidos:

- muitos métodos devolvem só `boolean` e **não dizem por que falharam**;
- tratamento fraco de links simbólicos e atributos;
- não é pensada para diferenças entre sistemas operacionais;
- métodos pouco coesos.

O NIO.2 resolve isso com uma **divisão de responsabilidades** muito clara:

```text
java.io.File      (uma classe que fazia tudo)
        │
        ▼
┌──────────────────┬───────────────────────────────┐
│ Path (interface) │ Files (classe utilitária)     │
│ ONDE está o      │ O QUE fazer com ele:          │
│ arquivo/pasta    │ criar, copiar, mover, apagar, │
│ (o caminho)      │ ler atributos...              │
└──────────────────┴───────────────────────────────┘
        ▲
        │ criado por
   Paths.get(...)   (ou Path.of(...) a partir do Java 11)
```

| Antes (`java.io`) | Agora (`java.nio.file`) |
|---|---|
| `File` | `Path` (o caminho) + `Files` (as operações) |
| `new File("x.txt")` | `Paths.get("x.txt")` |
| `file.createNewFile()` | `Files.createFile(path)` |
| `file.mkdir()` | `Files.createDirectory(path)` |
| `file.exists()` | `Files.exists(path)` |
| `file.delete()` | `Files.delete(path)` |

> `File` ainda existe e tem pontes: `file.toPath()` e `path.toFile()`. O instrutor comenta que `File` provavelmente será depreciada no futuro.

---

# Aula 144 — NIO pt 01 — `Path`, `Paths`, `Files` (parte 1)

## 1. `Path` é uma **interface**

`Path` representa um caminho no sistema de arquivos. Por ser interface, a implementação concreta depende do sistema operacional (Windows, Linux...) — é o polimorfismo trabalhando a seu favor.

`Files` é uma **classe final utilitária**: quase todos os métodos são `static`, e a maioria recebe um `Path` como primeiro argumento.

```java
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
```

---

## 2. Criando um `Path`

Usa-se a classe `Paths` (no plural):

```java
Path p1 = Paths.get("C:\\Users\\dev1\\arquivo.txt");   // um único texto (absoluto)
Path p2 = Paths.get("arquivo.txt");                    // relativo
```

`get` aceita **varargs**: você pode passar o caminho em partes e o Java monta o separador correto para o sistema:

```java
Path p3 = Paths.get("C:", "Users", "dev1", "arquivo.txt");
Path p4 = Paths.get("pasta", "subpasta", "arquivo.txt");
```

Todas as formas acima são equivalentes.

### A partir do Java 11

```java
Path p = Path.of("pasta", "arquivo.txt");   // preferível em código novo
```

---

## 3. Conversão entre `File` e `Path`

```java
File file = new File("arquivo.txt");
Path path = file.toPath();      // File → Path

File outroFile = path.toFile(); // Path → File
```

Isso é importante para integrar código antigo (que usa `File`) com código novo (`Path`).

---

## 4. Métodos básicos do `Path`

```java
Path path = Paths.get("pasta", "sub", "arquivo.txt");

path.getFileName();     // arquivo.txt
path.getParent();       // pasta/sub
path.getRoot();         // null (relativo) ou C:\ (absoluto)
path.getNameCount();    // 3
path.getName(0);        // pasta
path.isAbsolute();      // false
path.toAbsolutePath();  // caminho completo
path.toString();        // texto
```

## O que você precisa dominar (Aula 144)

- `Path` (interface) + `Paths`/`Path.of` (criação) + `Files` (operações).
- Por que o NIO substitui `File`.
- Criar `Path` com um texto ou em partes (varargs).
- `File.toPath()` e `Path.toFile()`.

---

# Aula 145 — NIO pt 02 — `Path`, `Paths`, `Files` (parte 2)

## 1. Criando diretórios

```java
Path diretorio = Paths.get("pasta");
Path criado = Files.createDirectory(diretorio);   // retorna o Path criado
```

Diferenças importantes em relação a `File.mkdir()`:

- retorna o **`Path`** criado (não `boolean`);
- lança **`IOException`** (checked) — por isso precisa de `try/catch` ou `throws`;
- se a pasta **já existir**, lança **`FileAlreadyExistsException`** (em vez de apenas devolver `false`).

### Evitando a exceção

```java
if (Files.notExists(diretorio)) {
    Files.createDirectory(diretorio);
}
```

(Também existe `Files.exists(path)`.)

---

## 2. Criando uma árvore de diretórios

`createDirectory` exige que o **pai exista**; senão lança `NoSuchFileException`. Para criar toda a hierarquia:

```java
Path arvore = Paths.get("pasta", "subpasta", "subsubpasta");
Files.createDirectories(arvore);   // cria os pais que faltam
```

`createDirectories` **não** falha se a pasta já existir.

| Método | Pai ausente | Já existe |
|---|---|---|
| `createDirectory` | `NoSuchFileException` | `FileAlreadyExistsException` |
| `createDirectories` | cria os pais | não falha |

---

## 3. Criando arquivos

```java
Path arquivo = Paths.get("pasta", "subpasta", "arquivo.txt");

if (Files.notExists(arquivo)) {
    Files.createFile(arquivo);
}
```

- `createFile` lança `FileAlreadyExistsException` se existir.
- O diretório pai precisa existir.

Montando o caminho a partir de outro `Path`:

```java
Path pasta = Paths.get("pasta", "subpasta");
Path arquivo = pasta.resolve("arquivo.txt");      // junta (veremos na aula 147)
```

---

## 4. Copiando arquivos

```java
Path origem = Paths.get("pasta", "subpasta", "arquivo.txt");
Path destino = Paths.get("pasta", "subpasta", "arquivo_copia.txt");

Files.copy(origem, destino);
```

### Armadilhas que o instrutor mostrou

1. O **destino** é o caminho completo do **arquivo novo**, não apenas da pasta. Passar só a pasta falha (a pasta já existe).
2. Se o destino já existir → `FileAlreadyExistsException`.
3. Para sobrescrever, use a opção:

```java
import java.nio.file.StandardCopyOption;

Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
```

`StandardCopyOption` também tem `COPY_ATTRIBUTES` e `ATOMIC_MOVE` (este último para `move`).

### Obtendo o diretório do arquivo

O instrutor usa `getParent()` para montar o destino na mesma pasta:

```java
Path pasta = origem.getParent();
Path destino = pasta.resolve("copia.txt");
```

---

## 5. Movendo e apagando

```java
Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);

Files.delete(path);            // lança NoSuchFileException se não existir
Files.deleteIfExists(path);    // retorna boolean, não lança se não existir
```

Para pastas, só apaga se estiverem **vazias** (`DirectoryNotEmptyException` caso contrário).

## 6. Mensagens de erro mais claras

Uma grande vantagem: em vez de um `false` misterioso, o NIO lança exceções **específicas** (`FileAlreadyExistsException`, `NoSuchFileException`, `DirectoryNotEmptyException`, `AccessDeniedException`), todas subclasses de `IOException`. Isso ajuda a saber **por que** falhou.

## O que você precisa dominar (Aula 145)

- `Files.createDirectory` x `createDirectories`.
- `Files.createFile`.
- `Files.copy` + `StandardCopyOption.REPLACE_EXISTING`.
- `exists`/`notExists`, `delete`/`deleteIfExists`, `move`.
- Exceções específicas em vez de `boolean`.

---

# Aula 146 — NIO pt 03 — Normalização

## 1. Contexto: `.` e `..` nos caminhos

Em qualquer sistema (Windows, Linux, Mac):

| Símbolo | Significado |
|---|---|
| `.` | o diretório **atual** |
| `..` | o diretório **pai** (volta um nível) |

Exemplo no terminal Linux:

```text
/home/william/dev        ← estou aqui
cd ..                    ← vou para /home/william
cd ../..                 ← vou para /home
cd ./pasta               ← "./" = a partir do atual, igual a "pasta"
```

---

## 2. O problema

Às vezes você recebe caminhos "sujos":

```java
Path p = Paths.get("C:\\Users\\dev1\\projeto\\dev\\.\\..\\..\\arquivo.txt");
```

O caminho funciona, mas é redundante e difícil de ler/comparar. **Normalizar** significa remover essas redundâncias.

---

## 3. `normalize()`

```java
Path sujo = Paths.get("/home/william/dev/../../arquivo.txt");
Path limpo = sujo.normalize();

System.out.println(sujo);   // /home/william/dev/../../arquivo.txt
System.out.println(limpo);  // /home/arquivo.txt
```

Regras:

- `.` é removido.
- `nome/..` é eliminado (volta um nível).
- Os `..` são resolvidos **apenas por texto**; o Java **não verifica** se as pastas existem.

```java
Paths.get("a/b/./c/../d").normalize();   // a/b/d
Paths.get("/a/b/../../..").normalize();  // /  (não volta além da raiz)
Paths.get("../x").normalize();           // ../x (relativo não pode subir mais do que o início)
```

### Importante

`normalize()` devolve **um novo `Path`** (os objetos `Path` são imutáveis, como as classes de `java.time`).

## 4. Para que serve na prática?

- Comparar caminhos corretamente (`./a/../b` e `b` representam o mesmo lugar).
- **Segurança**: evitar ataques de *path traversal* (alguém enviando `../../etc/senha` para tentar sair da pasta permitida). Sempre normalize antes de validar o caminho.

```java
Path base = Paths.get("/dados/uploads").toAbsolutePath().normalize();
Path alvo = base.resolve(entradaDoUsuario).normalize();

if (!alvo.startsWith(base)) {
    throw new SecurityException("Caminho fora da pasta permitida");
}
```

## O que você precisa dominar (Aula 146)

- O que significam `.` e `..`.
- `normalize()` remove redundâncias, **sem acessar o disco**.
- `Path` é imutável.
- Importância de normalizar antes de validar caminhos.

---

# Aula 147 — NIO pt 04 — Resolvendo Paths (`resolve`)

## 1. O que é "resolver"?

`resolve` **junta** dois caminhos: `caminhoBase.resolve(outro)`.

```java
Path pasta = Paths.get("/home/william/dev");
Path arquivo = Paths.get("arquivo.txt");

Path completo = pasta.resolve(arquivo);
System.out.println(completo);   // /home/william/dev/arquivo.txt
```

Também aceita String:

```java
pasta.resolve("arquivo.txt");
```

---

## 2. Relativo x absoluto (a base para entender)

| Tipo | Começa com... | Exemplo |
|---|---|---|
| **Absoluto** | raiz do sistema (`/` ou `C:\`) | `/home/william/dev` |
| **Relativo** | nome da pasta, sem raiz | `dev/arquivo.txt` |

Um caminho **relativo** é interpretado a partir de onde o programa roda (a "pasta atual"); um **absoluto** é inequívoco.

---

## 3. A regra do `resolve` com as 4 combinações

Seja `a.resolve(b)`:

| `a` (base) | `b` (argumento) | Resultado |
|---|---|---|
| absoluto | relativo | `a + b` ✅ (junta) |
| relativo | relativo | `a + b` ✅ (junta) |
| qualquer | **absoluto** | **`b`** (ignora a base!) |

A regra de ouro:

> Se o argumento (`b`) já for **absoluto**, o `resolve` devolve ele próprio, pois não há o que resolver.

```java
Path abs1 = Paths.get("/home/william");
Path abs2 = Paths.get("/tmp/outro");
Path rel1 = Paths.get("dev");
Path rel2 = Paths.get("arquivo.txt");

abs1.resolve(rel2);   // /home/william/arquivo.txt
rel1.resolve(rel2);   // dev/arquivo.txt
abs1.resolve(abs2);   // /tmp/outro        ← o argumento absoluto vence
rel1.resolve(abs2);   // /tmp/outro        ← idem
```

O instrutor demonstrou exatamente essas combinações para mostrar que misturar caminhos pode dar resultados diferentes do que se imagina.

---

## 4. Outros métodos relacionados

```java
Path p = Paths.get("/home/william/dev/a.txt");

p.resolveSibling("b.txt");   // /home/william/dev/b.txt  (resolve a partir do pai)
```

`resolveSibling` troca só o nome do arquivo, mantendo a pasta.

## 5. Dica prática

Para montar um caminho de arquivo a partir de uma pasta base, `resolve` é mais seguro que concatenar strings (`pasta + "/" + nome`), pois cuida do separador correto do sistema.

## O que você precisa dominar (Aula 147)

- `resolve` junta caminhos.
- Argumento absoluto → o resultado é o argumento.
- Diferença entre relativo e absoluto.
- `resolveSibling`.

---

# Aula 148 — NIO pt 05 — `relativize`

## 1. O que é

`relativize` faz o contrário do `resolve`: dados dois caminhos, calcula **como chegar do primeiro ao segundo**.

```java
Path origem = Paths.get("/home/william");
Path destino = Paths.get("/home/william/dev/ola.txt");

Path caminho = origem.relativize(destino);
System.out.println(caminho);   // dev/ola.txt
```

Leitura: "partindo de `/home/william`, para chegar em `/home/william/dev/ola.txt`, vá em `dev/ola.txt`".

Garantia matemática (para caminhos normalizados):

```java
origem.resolve(origem.relativize(destino)).equals(destino);   // true
```

---

## 2. Quando é preciso voltar (`..`)

```java
Path a = Paths.get("/home/william");
Path b = Paths.get("/home/maria");

a.relativize(b);   // ../maria    (volta um nível e entra em maria)
```

Exemplo da aula (3 absolutos):

```java
Path abs1 = Paths.get("/home/william");
Path abs2 = Paths.get("/usr");
Path abs3 = Paths.get("/home/william/dev/ola.txt");

abs1.relativize(abs3);   // dev/ola.txt
abs3.relativize(abs1);   // ../..          (volta 2 pastas)
abs1.relativize(abs2);   // ../../usr      (volta 2 níveis até a raiz e vai para usr)
```

Visualização:

```text
/                    ← raiz
├── home
│    └── william     ← abs1
│         └── dev
│              └── ola.txt  ← abs3
└── usr              ← abs2
```

De `abs1` para `abs2`: sobe `william` → `home` → `/` (2 `..`), depois desce para `usr`.

Com caminhos relativos funciona do mesmo jeito:

```java
Paths.get("temp").relativize(Paths.get("temp/ola.txt"));   // ola.txt
```

---

## 3. ⚠️ A regra obrigatória

**Ambos os caminhos precisam ser do mesmo tipo**: os dois absolutos **ou** os dois relativos.

```java
Path absoluto = Paths.get("/home/william");
Path relativo = Paths.get("temp/ola.txt");

absoluto.relativize(relativo);   // IllegalArgumentException: 'other' is different type of Path
```

Faz sentido: o Java não tem como saber de onde o caminho relativo parte, então não consegue calcular a rota.

## 4. Resumo: `resolve` x `relativize`

```text
resolve:     base + relativo   →  caminho completo
relativize:  caminho completo  →  relativo à base
```

| Operação | Pergunta |
|---|---|
| `normalize` | "Como limpo este caminho?" |
| `resolve` | "Como junto estes caminhos?" |
| `relativize` | "Como vou de A até B?" |

## O que você precisa dominar (Aula 148)

- `relativize` gera a rota de A até B (usando `..` quando precisa).
- Ambos precisam ser absolutos ou ambos relativos.
- Relação inversa com `resolve`.

---

# Aula 149 — NIO pt 06 — `BasicFileAttributes` (parte 1)

## 1. A hierarquia de atributos

O pacote `java.nio.file.attribute` tem **interfaces** para ler/escrever metadados de arquivos (data de criação, tamanho, permissões...), com variações por sistema:

```text
BasicFileAttributes          → atributos comuns a qualquer sistema (somente LEITURA)
├── DosFileAttributes        → específicos do Windows (oculto, somente leitura, sistema...)
└── PosixFileAttributes      → específicos de Unix/Linux/Mac (dono, grupo, permissões)

BasicFileAttributeView       → permite ALTERAR alguns atributos básicos
├── DosFileAttributeView
└── PosixFileAttributeView
```

Duas famílias, fáceis de memorizar:

| Termina com... | Função |
|---|---|
| `...Attributes` | **ler** os atributos (um "retrato" do momento) |
| `...AttributeView` | **ler e alterar** atributos |

Por serem interfaces, o Java devolve a implementação adequada ao sistema operacional onde o programa roda.

---

## 2. A primeira alteração: data da última modificação

A forma antiga (`File`):

```java
File arquivo = new File("pasta2/novo.txt");
LocalDateTime data = LocalDateTime.now().minusDays(10);
long ms = data.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
boolean ok = arquivo.setLastModified(ms);   // boolean
```

A forma NIO (`Files`):

```java
Path path = Paths.get("pasta2", "novo.txt");
FileTime fileTime = FileTime.fromMillis(ms);   // objeto próprio do NIO

Files.setLastModifiedTime(path, fileTime);
```

Pontos:

- O NIO não aceita `long` direto: você cria um `FileTime`.
- `FileTime.fromMillis(long)` ou `FileTime.from(Instant)`.
- Para ler: `Files.getLastModifiedTime(path)` devolve um `FileTime`.
- `FileTime.toMillis()` e `toInstant()` convertem de volta.

### Observação do instrutor

A "data de modificação" do Windows nem sempre é confiável (pode diferir da data de criação), porque o sistema atualiza em momentos específicos. Não construa lógica crítica em cima dela.

---

## 3. Outros métodos úteis de `Files`

```java
Files.size(path);               // tamanho em bytes
Files.isDirectory(path);
Files.isRegularFile(path);
Files.isHidden(path);
Files.isReadable(path);
Files.isWritable(path);
Files.isExecutable(path);
```

Todos lançam `IOException` quando necessário e devolvem tipos claros.

## O que você precisa dominar (Aula 149)

- A hierarquia `BasicFileAttributes` / `DosFileAttributes` / `PosixFileAttributes`.
- Diferença entre `...Attributes` (leitura) e `...AttributeView` (alteração).
- `FileTime` e `Files.setLastModifiedTime`.
- Converter `LocalDateTime` → milissegundos → `FileTime`.

---

# Aula 150 — NIO pt 07 — `BasicFileAttributes` (parte 2)

## 1. Lendo os atributos

```java
import java.nio.file.attribute.BasicFileAttributes;

Path path = Paths.get("pasta2", "novo.txt");

BasicFileAttributes atributos = Files.readAttributes(path, BasicFileAttributes.class);

System.out.println("Criação:           " + atributos.creationTime());
System.out.println("Último acesso:     " + atributos.lastAccessTime());
System.out.println("Última modif.:     " + atributos.lastModifiedTime());
System.out.println("É diretório?       " + atributos.isDirectory());
System.out.println("É arquivo comum?   " + atributos.isRegularFile());
System.out.println("É link simbólico?  " + atributos.isSymbolicLink());
System.out.println("Tamanho (bytes):   " + atributos.size());
```

- O segundo parâmetro é um `Class` (`BasicFileAttributes.class`), que diz ao Java **qual interface** você deseja.
- O objeto retornado é uma **implementação** da interface escolhida pelo sistema; você programa para a **interface** (polimorfismo — como disse o instrutor: "não importa o que ele retorna").

### ⚠️ Horário em UTC

Todos os `FileTime` vêm em **UTC** (o horário do mundo, `Z`). Se o seu fuso é UTC-3, as horas parecem "adiantadas". Converta:

```java
atributos.creationTime().toInstant().atZone(ZoneId.systemDefault());
```

---

## 2. Os atributos são um **retrato** (snapshot)

O objeto `BasicFileAttributes` representa o estado **no momento da leitura**. Se o arquivo mudar depois, o objeto **não se atualiza**: é preciso ler novamente com `readAttributes`.

---

## 3. Alterando: `BasicFileAttributeView`

Para alterar, use a **View**:

```java
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;

BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);

FileTime criacao = atributos.creationTime();            // manter
FileTime modificacao = atributos.lastModifiedTime();    // manter
FileTime acessoAgora = FileTime.fromMillis(System.currentTimeMillis());

view.setTimes(modificacao, acessoAgora, criacao);
// setTimes(lastModifiedTime, lastAccessTime, createTime)
```

Atenção à **ordem** dos parâmetros: `setTimes(modificação, acesso, criação)`. Para **não alterar** um dos campos, passe `null`.

Depois de alterar, **leia novamente** os atributos (com um novo `readAttributes`) para ver os valores atualizados — o objeto antigo continua mostrando os valores velhos.

A `View` também permite ler:

```java
BasicFileAttributes atual = view.readAttributes();
```

---

## 4. Resumo do padrão

```text
Para LER atributos:
  Files.readAttributes(path, XxxAttributes.class)   → retrato

Para ALTERAR atributos:
  Files.getFileAttributeView(path, XxxAttributeView.class) → view.setXxx(...)
```

| Quero... | Use |
|---|---|
| Só consultar | `readAttributes` |
| Mudar datas | `getFileAttributeView(...).setTimes(...)` |
| Mudar só a modificação | `Files.setLastModifiedTime` |

## O que você precisa dominar (Aula 150)

- `Files.readAttributes(path, BasicFileAttributes.class)`.
- `creationTime`, `lastAccessTime`, `lastModifiedTime`, `size`, `isDirectory`...
- `FileTime` sempre em UTC.
- O objeto lido é um retrato do momento.
- `getFileAttributeView` + `setTimes(modificação, acesso, criação)`.
- Programar para a interface, não para a implementação.

---

# Mapa mental do bloco

```text
java.nio.file
├── Path (interface)  → ONDE
│   ├── Paths.get(...) / Path.of(...)
│   ├── normalize()   → limpa . e ..
│   ├── resolve(b)    → junta A + B (se B é absoluto, vence B)
│   └── relativize(b) → rota de A até B (mesmo tipo!)
├── Files (utilitária) → O QUE
│   ├── createDirectory / createDirectories / createFile
│   ├── copy (REPLACE_EXISTING) / move / delete / deleteIfExists
│   ├── exists / notExists / size / isDirectory...
│   └── readAttributes / getFileAttributeView / setLastModifiedTime
└── attribute
    ├── BasicFileAttributes (leitura)  → Dos... / Posix...
    ├── BasicFileAttributeView (alteração)
    └── FileTime (sempre UTC)
```

# Cola de bolso

| Antes (`File`) | Agora (NIO) |
|---|---|
| `new File("a.txt")` | `Paths.get("a.txt")` |
| `file.mkdirs()` | `Files.createDirectories(path)` |
| `file.createNewFile()` | `Files.createFile(path)` |
| `file.delete()` | `Files.deleteIfExists(path)` |
| `file.renameTo(x)` | `Files.move(a, b)` |
| `file.lastModified()` | `Files.getLastModifiedTime(path)` |
| `file.setLastModified(ms)` | `Files.setLastModifiedTime(path, FileTime.fromMillis(ms))` |
