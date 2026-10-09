# Exercícios — Bloco 17: NIO — Atributos, Visitor, PathMatcher e Zip

## Aulas 151 a 158

Este arquivo acompanha o README do Bloco 17.

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

> **Onde criar os arquivos:** `src/main/bloco17_aulas151a158_nio_visitor_matcher_zip/aulaXXX/`

> **Segurança:** trabalhe somente dentro de `workspace-nio/`. Qualquer exercício que apague arquivos deve conferir `toAbsolutePath().normalize()` antes.

> **Windows x Linux:** os exercícios da aula 152 (POSIX) só funcionam em Linux/macOS (ou WSL). Se você só tem Windows sem WSL, faça os exercícios com **verificação de suporte** (`supportedFileAttributeViews().contains("posix")`) e escreva o que *esperaria* ver.

---

# Aula 151 — DosFileAttributes

## 🟢 Exercício 01 — Ocultar e mostrar

Crie um arquivo, torne-o oculto com `Files.setAttribute(path, "dos:hidden", true)`, confirme no Explorer (opção "Itens ocultos") e depois volte ao normal.

Imprima `isHidden` antes e depois, lendo com `DosFileAttributes`.

---

## 🟡 Exercício 02 — Painel de atributos

Crie um método `static void imprimirDos(Path path)` que lê `DosFileAttributes` e imprime um quadro:

```text
Arquivo:       dados.txt
Oculto:        [ ]
Somente leit.: [X]
Arquivo:       [X]
Sistema:       [ ]
```

Teste com um arquivo comum, um oculto, um somente leitura e uma pasta.

---

## 🔴 Exercício 03 — Proteção de arquivos

Crie `static void proteger(Path... arquivos)` e `static void liberar(Path... arquivos)` (varargs!):

1. `proteger` deixa cada arquivo **somente leitura** e **oculto** usando `DosFileAttributeView`.
2. Tente escrever num arquivo protegido com `Files.write`/`FileWriter` e **capture** a exceção, explicando a mensagem.
3. `liberar` desfaz tudo.
4. Antes de qualquer alteração, verifique se o sistema suporta a view `dos` e, se não suportar, imprima um aviso e retorne.

### Responda

Por que o objeto `DosFileAttributes` lido antes da alteração continua mostrando os valores antigos?

---

# Aula 152 — PosixFileAttributes

## 🟢 Exercício 04 — Lendo `rwx`

Leia os atributos POSIX de um arquivo e imprima dono, grupo e as permissões no formato `rw-r--r--` (use `PosixFilePermissions.toString`).

(Se estiver no Windows sem WSL, imprima `"POSIX não suportado"` verificando a view.)

---

## 🟡 Exercício 05 — Decodificador de permissões

Crie um método que recebe uma string como `"rwxr-x---"` e imprime, em português:

```text
Dono:   pode ler, escrever e executar
Grupo:  pode ler e executar
Outros: sem acesso
```

Use apenas `String`/`substring`/`charAt`, sem a API POSIX. Depois valide a mesma string com `PosixFilePermissions.fromString` e capture `IllegalArgumentException` para entradas inválidas como `"rwxrwx"` ou `"abcdefghi"`.

---

## 🔴 Exercício 06 — Chmod em Java

Implemente um método `static void chmod(Path path, String octal)` que aceite `"644"`, `"755"`, `"600"` e converta para a string `rw-r--r--` etc., aplicando com `Files.setPosixFilePermissions`.

Regras:

1. Valide que o texto tem 3 dígitos entre 0 e 7.
2. Converta cada dígito em `rwx` (4 = r, 2 = w, 1 = x).
3. Imprima as permissões antes e depois.
4. Teste com `644`, `755`, `600`, `000` e `999` (inválido).

### Pergunta

Qual a diferença entre `PosixFilePermission` e `PosixFilePermissions`?

---

# Aula 153 — DirectoryStream

## 🟢 Exercício 07 — `ls` em Java

Liste o conteúdo do diretório atual com `DirectoryStream`, imprimindo para cada item se é `[D]` (diretório) ou `[F]` (arquivo) e o nome.

---

## 🟡 Exercício 08 — Listagem com filtro

Com um `DirectoryStream` filtrado por glob, liste:

1. Somente arquivos `.java`.
2. Somente `.txt` ou `.csv`.
3. Arquivos cujo nome começa com letra maiúscula.

Depois faça o mesmo usando um `DirectoryStream.Filter<Path>` (por exemplo, arquivos maiores do que 1 KB).

---

## 🔴 Exercício 09 — Relatório de uma pasta

Crie um método que, dado um `Path` de pasta, imprima uma tabela:

```text
Nome            Tipo      Tamanho   Modificado
relatorio.txt   arquivo   1.2 KB    10/05/2024 14:32
imagens         pasta     -         09/05/2024 08:10
```

E ao final: total de arquivos, total de pastas, soma dos tamanhos e o maior arquivo.

### Regras

- Use `DirectoryStream` (um nível).
- Formate o tamanho em B, KB ou MB.
- Ordene por nome **sem** usar `Collections.sort` (você pode copiar para um array e ordenar manualmente).

---

# Aula 154 — SimpleFileVisitor (pt 01)

## 🟢 Exercício 10 — Contador

Conte quantos arquivos existem em uma árvore de diretórios usando `walkFileTree` e um `SimpleFileVisitor`. Imprima o total.

---

## 🟡 Exercício 11 — Tamanho da árvore

Crie um visitor `CalculadoraDeTamanho` que:

1. Soma o tamanho (via `attrs.size()`) de todos os arquivos.
2. Guarda o maior arquivo (nome e tamanho).
3. Conta quantos arquivos têm cada extensão (use um array de contadores ou variáveis simples para as extensões `.java`, `.txt`, `.class` e "outras").

Exponha os resultados por getters e imprima no `main`.

---

## 🔴 Exercício 12 — Busca de palavra

Crie um visitor `BuscadorDeTexto` que recebe uma palavra e procura em todos os arquivos `.txt` e `.java` da árvore:

1. Use `Files.newBufferedReader` (try-with-resources) para ler cada arquivo.
2. Imprima `arquivo:linha: texto` para cada ocorrência.
3. Se o arquivo não puder ser lido (permissão/encoding), trate no `visitFileFailed` ou no `try/catch` e continue.
4. Use `TERMINATE` para parar a busca assim que encontrar `N` ocorrências (parâmetro do construtor).

### Pergunta

Qual a diferença de uso entre `DirectoryStream` e `walkFileTree`?

---

# Aula 155 — SimpleFileVisitor (pt 02)

## 🟢 Exercício 13 — Entrando e saindo

Imprima a árvore com marcações de entrada e saída:

```text
> pasta
  > subpasta1
      arquivo1.txt
  < subpasta1
< pasta
```

Use `preVisitDirectory`, `visitFile` e `postVisitDirectory` e uma variável para controlar a indentação.

---

## 🟡 Exercício 14 — Ignorando pastas

Faça o visitor da aula anterior (contador de tamanho) **ignorar** completamente as pastas `.git`, `.idea`, `out` e `target`, retornando `SKIP_SUBTREE`.

Compare os resultados com e sem o filtro e explique a diferença no tempo e na contagem.

Depois teste `SKIP_SIBLINGS` e descreva em comentário o que mudou (qual arquivo/pasta deixou de ser visitado?).

---

## 🔴 Exercício 15 — Limpador de pasta temporária

Escreva `static void apagarArvore(Path raiz)` com `walkFileTree`:

1. Apaga os arquivos em `visitFile`.
2. Apaga as pastas em `postVisitDirectory`.
3. **Antes de apagar**, valide que `raiz` está dentro de `workspace-nio` (`normalize` + `startsWith`).
4. Em `visitFileFailed`, registre o erro e continue.
5. Mostre o total de arquivos e pastas apagados e o espaço liberado.
6. Faça uma versão "simulação" (dry-run) que apenas imprime o que *seria* apagado.

### Pergunta

Por que a pasta só pode ser apagada em `postVisitDirectory` e não em `preVisitDirectory`?

---

# Aula 156 — PathMatcher (pt 01)

## 🟢 Exercício 16 — Glob básico

Crie um `PathMatcher` para `glob:*.txt` e teste com:

```text
"a.txt", "a.TXT", "pasta/a.txt", "a.txt.bak", ".txt"
```

Preveja o resultado antes de rodar e explique cada surpresa (por exemplo, por que `pasta/a.txt` não casa).

---

## 🟡 Exercício 17 — Tabela de padrões

Crie uma lista de 8 padrões glob e 6 nomes de arquivo, e imprima uma matriz mostrando `true/false`:

```text
Padrões: *.java | *.{java,class} | Teste?.java | [A-Z]*.txt | **/*.txt | *.* | ?*.log | {a,b}*
```

Escolha os nomes de forma que cada padrão tenha pelo menos um acerto e um erro.

---

## 🔴 Exercício 18 — `glob` x `regex`

Escreva, para o mesmo objetivo, um `glob` e um `regex` equivalentes:

1. Arquivos `.log` cujo nome começa com `app-` e tem uma data `AAAA-MM-DD` (ex.: `app-2024-05-10.log`).
2. Imagens `.png`, `.jpg` ou `.jpeg`.
3. Backups terminados em `.bak` ou `.bak.N` (N = número).

Teste com nomes válidos e inválidos e comente qual é mais fácil de ler e qual é mais preciso. (Dica: `glob` não valida "4 dígitos"; `regex` sim.)

---

# Aula 157 — PathMatcher (pt 02)

## 🟢 Exercício 19 — Meu primeiro buscador

Reproduza o exercício da aula (arquivos que começam com `Teste` e terminam em `.java` ou `.class`) **sem olhar a solução do README** e imprima o caminho completo de cada um.

---

## 🟡 Exercício 20 — Buscador configurável

Crie a classe `BuscadorDeArquivos extends SimpleFileVisitor<Path>` que recebe no construtor **um padrão glob** e armazena os caminhos encontrados (array crescente ou contagem + impressão).

No `main`, use o mesmo buscador com:

```text
"*.java"
"Ex0?.java"
"**/aula0*/*.java"
"*.{md,txt}"
```

Imprima a quantidade de resultados de cada busca.

---

## 🔴 Exercício 21 — Faxina inteligente

Crie um programa de limpeza que percorre uma pasta e, usando **um conjunto de padrões**, classifica cada arquivo:

```text
LIXO     → *.tmp, *.bak, ~*, Thumbs.db
LOG      → *.log
CÓDIGO   → *.{java,class}
OUTROS
```

Para cada categoria, mostre quantidade e espaço ocupado. Depois:

1. Simule (dry-run) a remoção dos arquivos `LIXO`.
2. Mostre os 5 maiores arquivos de `LOG`.
3. Ignore `.git` e `.idea` com `SKIP_SUBTREE`.

---

# Aula 158 — ZipOutputStream

## 🟢 Exercício 22 — Zip simples

Crie 3 arquivos de texto e um `.zip` contendo os três usando `ZipOutputStream`. Abra o zip no gerenciador de arquivos e confirme o conteúdo.

---

## 🟡 Exercício 23 — Zip com relatório

Modifique o exercício anterior para:

1. Compactar todos os arquivos `.txt` de uma pasta (use `DirectoryStream` com glob).
2. Imprimir, para cada arquivo, o nome, o tamanho original e (via `ZipEntry`/`getCompressedSize` ao reler) o tamanho compactado.
3. Mostrar a taxa de compressão (%).

Dica: depois de fechar a entrada, `entrada.getCompressedSize()` retorna o tamanho compactado.

---

## 🔴 Exercício 24 — Zip recursivo e unzip

Faça duas funções:

1. `static void zipar(Path pasta, Path zipFile)` — usa `walkFileTree`, grava os caminhos **relativos** (com `/`) como nomes das entradas, e também cria entradas para as pastas vazias (nome terminando em `/`).
2. `static void unzip(Path zipFile, Path destino)` — usa `ZipInputStream`, recria a estrutura e copia os arquivos.

Requisitos:

- Proteção contra *Zip Slip*: ao descompactar, normalize o caminho e verifique se continua dentro do destino.
- No fim, compare as duas árvores (origem e destino extraído) contando arquivos e somando tamanhos.

### Pergunta

O que acontece se você esquecer o `Files.copy` entre `putNextEntry` e `closeEntry`?

---

# 🏆 Desafio Integrador do Bloco 17 — Gerenciador de Projetos Backup

Você vai criar uma ferramenta de **backup incremental** de uma pasta de projeto.

## Preparação

Monte uma árvore `workspace-nio/projeto` com código-fonte (`.java`), arquivos `.class`, logs, arquivos temporários, uma pasta `.git` falsa, imagens e documentos, em vários níveis.

## Requisitos

1. **Varredura** (`walkFileTree`): percorre toda a árvore ignorando `.git`, `.idea`, `out` (`SKIP_SUBTREE`).
2. **Classificação** (`PathMatcher`): categorias por padrões glob (fonte, binário, imagem, documento, log, temporário). Temporários são excluídos do backup.
3. **Inventário**: gera `inventario.txt` com `caminho relativo | categoria | tamanho | data de modificação`.
4. **Incremental**: se já existir um inventário anterior, só entram no zip os arquivos **novos ou modificados** desde então (compare tamanho e data).
5. **Compactação** (`ZipOutputStream`): gera `backup-AAAAMMDD-HHmmss.zip` com a estrutura de pastas preservada.
6. **Atributos**:
   - no Windows, marque o zip gerado como **somente leitura** (`dos:readonly`);
   - em Linux, aplique permissões `rw-r-----`;
   - detecte o suporte da view antes e trate os dois casos.
7. **Relatório final**: quantidade e tamanho por categoria, quantos arquivos foram ao zip, taxa de compressão, tempo total (`Duration`).
8. **Restauração**: método `restaurar(zip, destino)` com proteção contra *Zip Slip*, que depois compara a árvore restaurada com a original.
9. **Limpeza de backups antigos**: mantém só os 3 backups mais recentes (por data de modificação), apagando os demais com cuidado.

## Regras

- Use `Path`/`Files`, `DirectoryStream` onde bastar listar um nível, `SimpleFileVisitor` onde precisar recursão.
- Cada requisito em uma classe/método próprio.
- Trate exceções específicas e nunca deixe um `catch` vazio.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei ler e alterar atributos DOS.
- [ ] Sei ler e alterar permissões POSIX com `fromString`.
- [ ] Sei que POSIX não funciona no Windows.
- [ ] Sei listar um nível com `DirectoryStream` e fechar com try-with-resources.
- [ ] Sei percorrer a árvore com `walkFileTree` e `SimpleFileVisitor`.
- [ ] Sei os 4 métodos do visitor e os 4 valores de `FileVisitResult`.
- [ ] Sei usar `SKIP_SUBTREE` para ignorar pastas.
- [ ] Sei criar `PathMatcher` com `glob:` e `regex:`.
- [ ] Sei os curingas `* ** ? [] {}` do glob.
- [ ] Sei compactar com `ZipOutputStream` (`putNextEntry` → `copy` → `closeEntry`).

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
