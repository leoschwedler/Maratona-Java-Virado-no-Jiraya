# Exercícios — Bloco 16: NIO — `Path`, `Files` e Atributos

## Aulas 144 a 150

Este arquivo acompanha o README do Bloco 16.

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

> **Onde criar os arquivos:** `src/main/bloco16_aulas144a150_nio_path_files/aulaXXX/`

> **Regra do bloco:** nada de `java.io.File` (exceto onde o enunciado pedir a conversão). Use `Path` e `Files`.
> **Segurança:** trabalhe sempre dentro de uma pasta `workspace-nio/` do projeto.

---

# Aula 144 — Path, Paths, Files (pt 01)

## 🟢 Exercício 01 — Cinco jeitos de criar o mesmo Path

Crie o caminho `workspace-nio/docs/leia-me.txt` de **quatro formas** diferentes (texto único, partes separadas, `Paths.get` e `Path.of` se estiver no Java 11+, e a partir de um `File` com `toPath()`).

Confirme com `equals` que todos são iguais e imprima o resultado.

---

## 🟡 Exercício 02 — Anatomia de um Path

Para o caminho `workspace-nio/docs/relatorios/2024/jan.txt` imprima:

```text
Nome do arquivo (getFileName)
Pai (getParent)
Raiz (getRoot)
Quantidade de nomes (getNameCount)
Cada nome do caminho (getName(i) num laço)
É absoluto?
Caminho absoluto (toAbsolutePath)
```

Repita com um caminho absoluto. Compare `getRoot()` nos dois casos.

---

## 🔴 Exercício 03 — Ponte entre mundos

Crie um método `static void migrar(File antigo)` que recebe um `File` do código legado e:

1. Converte para `Path`.
2. Imprime nome, pai e se existe (usando `Files.exists`).
3. Devolve novamente um `File` (`toFile`) para outro método legado `static void imprimirLegado(File f)` que usa `f.getName()` e `f.length()`.

Teste com um arquivo existente e um inexistente.

### Responda

Por que o NIO separou `File` em `Path` + `Files`? Qual a vantagem de `Path` ser uma interface?

---

# Aula 145 — Path, Paths, Files (pt 02)

## 🟢 Exercício 04 — Criando a árvore

Crie `workspace-nio/projeto/src/main/java` com **uma só chamada**. Execute duas vezes e confirme que não lança exceção na segunda.

Depois tente com `createDirectory` (que exige o pai) numa pasta cujo pai não existe e **capture e imprima** a exceção.

---

## 🟡 Exercício 05 — Copiar com cuidado

1. Crie `origem.txt` (use `Files.createFile`).
2. Copie para `copia.txt`.
3. Rode o programa de novo e capture `FileAlreadyExistsException`, imprimindo uma mensagem amigável.
4. Corrija usando `StandardCopyOption.REPLACE_EXISTING`.
5. Tente copiar para o caminho de uma **pasta** existente em vez de um arquivo e anote o que acontece.

---

## 🔴 Exercício 06 — Backup automático

Crie `static Path backup(Path arquivo)` que:

1. Cria uma pasta `backup` ao lado do arquivo (mesmo pai), se não existir.
2. Copia o arquivo para dentro dela com o nome `nomeOriginal.bak`.
3. Se já existir um backup, **renomeia o antigo** para `nomeOriginal.bak.1`, `.bak.2`... antes de gravar o novo.
4. Retorna o `Path` do backup criado.
5. Se o arquivo de origem não existir, lança `IllegalArgumentException` com mensagem clara.

Teste fazendo 4 backups seguidos do mesmo arquivo.

---

# Aula 146 — Normalização

## 🟢 Exercício 07 — Limpando caminhos

Normalize e imprima antes/depois:

```text
a/b/./c
a/b/../c
/a/b/c/../../d
./x/./y/../z
../../a
```

Explique o que acontece com `../../a` (relativo).

---

## 🟡 Exercício 08 — Comparação inteligente

Crie `static boolean mesmoLocal(Path a, Path b)` que retorna `true` quando os dois caminhos apontam para o mesmo local **após** `toAbsolutePath().normalize()`.

Teste com pares como:

```text
"pasta/arquivo.txt"  e  "pasta/../pasta/./arquivo.txt"
"pasta/arquivo.txt"  e  "outra/arquivo.txt"
```

Compare com `a.equals(b)` e explique a diferença.

---

## 🔴 Exercício 09 — Proteção contra path traversal

Crie `static Path abrirDentroDe(Path base, String nomeFornecidoPeloUsuario)` que:

1. Resolve o nome dentro da pasta `base`.
2. Normaliza.
3. Garante (com `startsWith`) que o resultado continua **dentro** de `base`; senão, lança `SecurityException`.

Teste:

```text
"relatorio.txt"          → ok
"sub/relatorio.txt"      → ok
"../segredo.txt"         → bloqueado
"sub/../../segredo.txt"  → bloqueado
"/etc/passwd"            → bloqueado (absoluto!)
```

### Responda

Por que validar **depois** de normalizar e não antes?

---

# Aula 147 — Resolve

## 🟢 Exercício 10 — Montando caminhos

Dada a pasta base `workspace-nio/dados`, use `resolve` para montar os caminhos de `clientes.csv`, `pedidos.csv` e `log/erros.txt`. Imprima.

---

## 🟡 Exercício 11 — Tabela de combinações

Crie dois caminhos absolutos (`abs1`, `abs2`) e dois relativos (`rel1`, `rel2`).

Imprima o resultado de todas as 4 combinações `x.resolve(y)` possíveis entre (abs/rel) e (abs/rel), com um comentário prevendo cada resultado **antes** de rodar. Qual regra explica o resultado quando o argumento é absoluto?

---

## 🔴 Exercício 12 — Gerador de caminhos por data

Crie um método `static Path caminhoDoLog(Path base, LocalDate data, String nome)` que monta `base/ano/mes/dia/nome` (ex.: `logs/2024/05/09/app.log`).

Depois:

1. Crie fisicamente a árvore (com `createDirectories`) e o arquivo vazio.
2. Gere caminhos para 5 datas diferentes.
3. Use `resolveSibling` para criar, para cada log, o caminho de um arquivo `.gz` ao lado (`app.log` → `app.log.gz`) sem criar o arquivo.

---

# Aula 148 — Relativize

## 🟢 Exercício 13 — De A até B

Dados `/home/ana` e `/home/ana/docs/notas.txt` (mesmo que não existam), use `relativize` nos dois sentidos e imprima.

---

## 🟡 Exercício 14 — Mapa de rotas

Crie 4 caminhos absolutos em lugares diferentes da árvore (`/home/ana`, `/home/bia/docs`, `/usr/lib`, `/home/ana/projetos/app`).

Imprima uma **matriz 4x4** com o `relativize` de cada um para cada outro. Para cada resultado conte quantos `..` aparecem (`getName(i).toString().equals("..")`).

---

## 🔴 Exercício 15 — Pacote relocável

Dado um diretório com vários arquivos em subpastas, gere um arquivo `manifesto.txt` que lista **todos os arquivos com caminhos relativos à raiz do pacote** (um por linha).

Depois, escreva um método que lê o manifesto e, dado um **novo diretório de destino**, recria (via `resolve`) os caminhos completos no novo local, imprimindo "origem → destino".

### Responda

O que acontece quando você tenta `relativize` entre um `Path` absoluto e um relativo? Teste e capture a exceção.

---

# Aula 149 — BasicFileAttributes (pt 01)

## 🟢 Exercício 16 — Informações com Files

Para um arquivo qualquer, imprima usando **apenas métodos de `Files`**: tamanho, se é diretório, se é arquivo regular, se está oculto, se pode ler, escrever e executar.

---

## 🟡 Exercício 17 — Máquina do tempo

Crie um arquivo e altere sua data de modificação para:

1. Exatamente 10 dias atrás.
2. Daqui a 1 hora.
3. `01/01/2000 00:00`.

Use `FileTime` e `Files.setLastModifiedTime`. Releia e imprima a data em formato legível, convertendo para o seu fuso.

---

## 🔴 Exercício 18 — Arquivos antigos

Crie um método `static List<Path>`... (como ainda não vimos listas, use um array de `Path` de tamanho fixo ou imprima direto) que percorra uma pasta (somente o primeiro nível, com `File.list` ou `Files.list`) e identifique os arquivos **modificados há mais de N dias**.

1. Prepare a pasta com 6 arquivos e ajuste manualmente as datas de modificação com `setLastModifiedTime` (um deles "ontem", outro "há 40 dias", etc.).
2. Mostre quais seriam candidatos à limpeza com N = 30.
3. **Não apague nada**, apenas simule imprimindo `[SIMULAÇÃO] apagaria x`.

---

# Aula 150 — BasicFileAttributes (pt 02)

## 🟢 Exercício 19 — Retrato do arquivo

Leia os atributos de um arquivo com `Files.readAttributes(path, BasicFileAttributes.class)` e imprima criação, último acesso, última modificação, tamanho e tipo.

Converta as datas para `LocalDateTime` no seu fuso e explique em comentário por que o `FileTime` aparece com horas diferentes.

---

## 🟡 Exercício 20 — Snapshot desatualizado

1. Leia os atributos e guarde em uma variável `antes`.
2. Altere a data de acesso com `BasicFileAttributeView.setTimes`.
3. Imprima `antes.lastAccessTime()` e depois um novo `readAttributes`.

Explique por que o primeiro continua mostrando o valor antigo.

---

## 🔴 Exercício 21 — Auditor de arquivos

Crie uma classe `Auditor` com o método `static void auditar(Path arquivo)` que imprime um relatório:

```text
Arquivo: relatorio.txt
Tipo: arquivo regular
Tamanho: 1.2 KB (formate: B, KB, MB)
Criado há: 12 dias
Modificado há: 3 horas
Último acesso: há 5 minutos
Status: RECENTE | ANTIGO | PARADO
```

Regras de status: modificado há menos de 1 dia → RECENTE; mais de 30 dias → ANTIGO; entre os dois → PARADO.

Use `Duration` e `ChronoUnit` para calcular os intervalos e teste com arquivos cujas datas você manipulou com `setTimes`.

---

# 🏆 Desafio Integrador do Bloco 16 — Organizador de Downloads

Você vai criar um programa que organiza uma pasta `workspace-nio/downloads` bagunçada.

## Preparação

Um método `prepararCenario()` cria, usando só `Files`/`Path`:

- ~15 arquivos de tamanhos e extensões variadas (`.pdf`, `.jpg`, `.txt`, `.zip`, `.log`, sem extensão);
- subpastas com arquivos dentro;
- datas de modificação variadas (alguns com mais de 60 dias, usando `FileTime`).

## Requisitos

1. **Organizar por tipo**: mover cada arquivo para `organizado/<extensão>/` (`Files.move`), criando as pastas com `createDirectories`. Arquivos sem extensão vão para `organizado/outros/`.
2. **Nomes duplicados**: se já existir um arquivo com o mesmo nome no destino, renomeie com sufixo `_1`, `_2`...
3. **Segurança**: valide cada caminho com `normalize` e `startsWith` para nunca mexer fora de `downloads`.
4. **Arquivar antigos**: arquivos com mais de 60 dias vão para `arquivo-morto/AAAA/MM/` (ano e mês da **data de modificação**).
5. **Relatório** `relatorio.txt` (escrito com `Files.write` ou `BufferedWriter`): lista, para cada arquivo, o caminho **relativo** (`relativize`) de origem e destino, tamanho e data.
6. **Desfazer**: grave um `log-movimentos.txt` com "destino → origem" e implemente `desfazer()` que lê o log e devolve tudo à posição original.
7. **Cópia de segurança** opcional: antes de mover, `Files.copy` para `backup/` com `REPLACE_EXISTING`.
8. **Auditor**: no final imprima um resumo (quantidade e tamanho por tipo; mais antigo, mais recente e maior arquivo).

## Regras

- Nenhum `java.io.File`.
- Tratar cada exceção específica (`FileAlreadyExistsException`, `NoSuchFileException`, `AccessDeniedException`) com mensagem própria.
- Use try-with-resources onde houver leitura/escrita.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei criar `Path` com `Paths.get`/`Path.of`.
- [ ] Sei converter `File` ↔ `Path`.
- [ ] Sei a diferença entre `createDirectory` e `createDirectories`.
- [ ] Sei usar `Files.copy` com `REPLACE_EXISTING`, `move`, `delete`, `deleteIfExists`.
- [ ] Entendo `.` e `..` e uso `normalize()`.
- [ ] Sei usar `resolve` e conheço a regra do argumento absoluto.
- [ ] Sei usar `relativize` e sua restrição.
- [ ] Sei usar `FileTime`.
- [ ] Sei ler atributos com `readAttributes`.
- [ ] Sei alterar com `getFileAttributeView(...).setTimes`.
- [ ] Sei que os atributos lidos são um retrato do momento.

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
