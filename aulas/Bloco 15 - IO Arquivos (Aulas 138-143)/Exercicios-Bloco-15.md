# Exercícios — Bloco 15: IO — Arquivos e Diretórios

## Aulas 138 a 143

Este arquivo acompanha o README do Bloco 15.

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

> **Onde criar os arquivos:** `src/main/bloco15_aulas138a143_io_arquivos/aulaXXX/`

> **Regras de segurança:** todos os arquivos e pastas dos exercícios devem ser criados dentro de uma pasta de trabalho do projeto (por exemplo `workspace-io/`). **Nunca** apague nada fora dela. Antes de qualquer `delete()` ou `renameTo()`, confirme o caminho com `getAbsolutePath()`.

> Sempre use **try-with-resources** para fechar os recursos.

---

# Aula 138 — File

## 🟢 Exercício 01 — Raio-X de um arquivo

Crie um programa que:

1. Cria `new File("workspace-io/info.txt")` (crie a pasta antes, se precisar — veja a aula 143, ou crie manualmente).
2. Imprime se o arquivo existe **antes** de criar.
3. Cria o arquivo com `createNewFile()`.
4. Imprime o resultado do `createNewFile()`.
5. Executa o programa **duas vezes** e compare os resultados.

Em seguida imprima: nome, caminho relativo, caminho absoluto, se é arquivo, se é diretório, tamanho e última modificação.

### Você deve conseguir responder

- Por que, ao executar `new File("info.txt")`, nenhum arquivo aparece no disco?
- O que `createNewFile()` retorna na segunda execução?

---

## 🟡 Exercício 02 — Apagador cuidadoso

Crie um método `static void apagarComSeguranca(File arquivo)` que:

1. Se o arquivo **não existir**, imprime `"Nada a apagar: <nome>"`.
2. Se for um **diretório**, imprime `"Não apago diretórios aqui"` e não faz nada.
3. Se for um arquivo, apaga e imprime se deu certo.

Teste com: um arquivo existente, um inexistente e uma pasta.

### Pergunta

O que acontece se você chamar `delete()` num arquivo que não existe? Lança exceção ou retorna algo?

---

## 🔴 Exercício 03 — Linha do tempo do arquivo

Crie 3 arquivos em instantes diferentes (use `Thread.sleep(1500)` entre as criações) e depois escreva um programa que:

1. Leia a data de modificação de cada arquivo (`lastModified`).
2. Converta para `LocalDateTime` (usando `Instant` e `ZoneId`) e imprima formatado com `DateTimeFormatter` (`dd/MM/yyyy HH:mm:ss`).
3. Descubra e imprima qual é o **arquivo mais antigo** e qual é o **mais recente**.
4. Calcule e imprima a diferença entre eles com `Duration`.

### Desafio extra

Modifique o conteúdo de um dos arquivos depois (você ainda não escreve em arquivos neste ponto; deixe pronto o código de comparação e rode de novo depois da aula 139, observando como o `lastModified` muda).

---

# Aula 139 — FileWriter

## 🟢 Exercício 04 — Primeira escrita

Crie um programa que escreva a frase `"Aprendendo IO em Java"` num arquivo `workspace-io/frase.txt` usando `FileWriter` com try-with-resources.

Execute **duas vezes**. O que você viu no arquivo? Escreva a resposta em comentário.

---

## 🟡 Exercício 05 — Diário de bordo

Crie um programa que **acrescente** uma linha ao arquivo `diario.txt` a cada execução, no formato:

```text
[2024-05-10 14:32:10] Executei o programa
```

### Regras

- Use `FileWriter` com `append = true`.
- A data/hora deve ser a atual (use `LocalDateTime` e `DateTimeFormatter`).
- Cada registro deve ficar em uma linha **usando `System.lineSeparator()`**, não `"\n"`.

Execute 4 vezes e confira o conteúdo.

### Pergunta

Por que `"\n"` pode causar problemas em programas que rodam em Windows e Linux?

---

## 🔴 Exercício 06 — Gerador de tabuadas

Crie um programa que gere um arquivo `tabuada.txt` com a tabuada de 1 a 10 (cada tabuada de 1 a 10 como bloco):

```text
Tabuada do 1
1 x 1 = 1
...
Tabuada do 2
...
```

### Regras

- Use `FileWriter` (ainda sem buffer).
- Deixe uma linha em branco entre as tabuadas.
- Meça com `System.nanoTime()` quanto tempo levou e imprima no console. Anote o tempo para comparar depois com o exercício do `BufferedWriter`.
- Chame `flush()` apenas no final.

### Pergunta

Por que o `FileWriter` é considerado de "baixo nível"?

---

# Aula 140 — FileReader

## 🟢 Exercício 07 — Lendo caracter por caracter

Leia o arquivo `frase.txt` (do exercício 04) usando `FileReader.read()` e imprima o conteúdo.

Imprima também, lado a lado, cada caractere e o seu código numérico:

```text
A -> 65
p -> 112
...
```

---

## 🟡 Exercício 08 — Contador de caracteres

Crie um método `static int[] contar(File arquivo)` que lê um arquivo com `FileReader` e devolve um array com 3 posições:

```text
[0] total de caracteres
[1] total de letras (use Character.isLetter)
[2] total de dígitos
```

Teste com o arquivo `diario.txt` do exercício 05.

### Pergunta

Por que `read()` devolve `int` e não `char`? Qual valor o `int` assume quando chega ao fim do arquivo?

---

## 🔴 Exercício 09 — Copiadora de arquivos (versão baixo nível)

Crie um método `static void copiar(File origem, File destino)` que usa `FileReader` + `FileWriter` para copiar o conteúdo.

### Regras

1. Faça **duas versões**:
   - lendo **caractere por caractere** (`read()`);
   - lendo em blocos com `char[] buffer = new char[1024]` (`read(char[])`).
2. Meça o tempo de cada uma com um arquivo de pelo menos alguns milhares de caracteres (gere-o com um laço).
3. Em caso de origem inexistente, imprima mensagem amigável tratando `FileNotFoundException` **separadamente** de `IOException`.
4. No final, compare os dois arquivos copiados caractere a caractere para provar que ficaram iguais.

### Cuidado

Ao usar `read(char[])`, escreva apenas as posições realmente lidas (`write(buffer, 0, lidos)`).

---

# Aula 141 — BufferedWriter

## 🟢 Exercício 10 — Lista de compras

Escreva um programa que grave 5 itens de compra em `compras.txt`, **um por linha**, usando `BufferedWriter` e `newLine()`.

---

## 🟡 Exercício 11 — Agenda em CSV

Crie uma classe `Contato` (`nome`, `telefone`, `email`) e uma lista (array) com 5 contatos.

Grave em `agenda.csv`:

```text
nome;telefone;email
Ana;11999990000;ana@mail.com
...
```

### Regras

- Cabeçalho na primeira linha.
- Use `BufferedWriter` encadeado com `FileWriter`.
- Crie um método `static void salvar(Contato[] contatos, File arquivo, boolean append)`.
- Teste os dois modos (`append` verdadeiro e falso) e explique o resultado.

---

## 🔴 Exercício 12 — Benchmark de escrita

Compare o desempenho de escrever **200 mil linhas** com:

1. `FileWriter` puro.
2. `BufferedWriter` com buffer padrão.
3. `BufferedWriter` com buffer de `64 * 1024`.

Para cada abordagem: meça o tempo (`System.nanoTime()`) e imprima em milissegundos. Faça cada medição **3 vezes** e calcule a média.

### Responda

- Qual foi mais rápido? Por quê?
- Por que o `BufferedWriter` usa um buffer na memória?
- O que acontece se você esquecer o `flush()`/`close()` ao final em `BufferedWriter` sem try-with-resources? (Teste de propósito, num arquivo à parte, e veja se o conteúdo ficou incompleto.)

---

# Aula 142 — BufferedReader

## 🟢 Exercício 13 — Lendo linha por linha

Leia o arquivo `compras.txt` com `BufferedReader` e imprima cada linha numerada:

```text
1: Arroz
2: Feijão
...
```

---

## 🟡 Exercício 14 — Leitor de CSV

Leia o `agenda.csv` do exercício 11:

1. Ignore o cabeçalho.
2. Use `split(";")` para separar os campos.
3. Crie objetos `Contato` com os dados lidos.
4. Imprima todos eles em formato de tabela alinhada (`printf`).
5. Se alguma linha tiver número de campos diferente de 3, ignore e informe a linha problemática.

---

## 🔴 Exercício 15 — Analisador de texto

Crie um arquivo `texto.txt` com um parágrafo de pelo menos 10 linhas (pode ser escrito por um programa seu).

Escreva um programa que, lendo com `BufferedReader`, informe:

1. Total de linhas.
2. Total de linhas em branco.
3. Total de palavras (use `split("\\s+")`).
4. A linha mais longa (e seu tamanho).
5. As 3 palavras que mais se repetem (sem diferenciar maiúsculas de minúsculas — use arrays/laços; ainda não vimos `Map`).
6. Grave um relatório com esses resultados em `relatorio.txt` usando `BufferedWriter`.

### Pergunta

Por que `readLine()` devolve `null` no fim, mas `FileReader.read()` devolve `-1`?

---

# Aula 143 — File com diretórios

## 🟢 Exercício 16 — Estrutura de pastas

Usando `mkdir()` e `mkdirs()`, crie a estrutura:

```text
workspace-io/
 ├── docs/
 ├── imagens/
 └── backup/
      └── 2024/
           └── maio/
```

Imprima o retorno de cada chamada. Execute duas vezes e explique por que os retornos mudam.

### Pergunta

Qual a diferença entre `mkdir()` e `mkdirs()`? O que acontece se a pasta pai não existir?

---

## 🟡 Exercício 17 — Organizador de arquivos

Dentro de `workspace-io/entrada/`, crie 6 arquivos vazios: `a.txt`, `b.txt`, `c.csv`, `d.csv`, `e.log`, `f.log`.

Escreva um programa que:

1. Liste os arquivos da pasta com `listFiles()`.
2. Para cada arquivo, descubra a extensão.
3. Mova (usando `renameTo`) cada um para uma subpasta com o nome da extensão (`workspace-io/organizado/txt/`, `/csv/`, `/log/`), criando a pasta se não existir.
4. Imprima um resumo: quantos arquivos foram movidos para cada pasta e se alguma operação falhou.

---

## 🔴 Exercício 18 — Navegador de pastas recursivo

Crie um método **recursivo** `static void listar(File pasta, int nivel)` que imprima a árvore completa de uma pasta, com indentação por nível:

```text
workspace-io/
  entrada/
    a.txt
  organizado/
    txt/
      a.txt
```

Depois, adicione:

1. `static long tamanhoTotal(File pasta)` — soma o tamanho (em bytes) de todos os arquivos dentro da pasta, incluindo as subpastas.
2. `static int contarArquivos(File pasta)`.
3. `static void apagarTudo(File pasta)` — apaga o conteúdo e depois a própria pasta (comece de baixo para cima!). **Use somente dentro da pasta `workspace-io`.**

### Pergunta

Por que `delete()` falha em pastas que ainda têm conteúdo?

---

# 🏆 Desafio Integrador do Bloco 15 — Mini Sistema de Notas em Arquivo

Você vai criar um mini sistema de anotações que guarda tudo em arquivos.

## Estrutura de pastas

```text
notas/
 ├── pessoais/
 ├── trabalho/
 └── arquivadas/
```

## Funcionalidades

1. **Criar nota**: pede (ou define no código) uma categoria, um título e um texto; cria `notas/<categoria>/<titulo>.txt` com `BufferedWriter`. Primeira linha: data/hora de criação; segunda: título; depois o texto.
2. **Listar notas**: percorre todas as categorias e imprime título, tamanho e data da última modificação.
3. **Ler nota**: dado título e categoria, imprime o conteúdo com `BufferedReader`.
4. **Acrescentar texto**: adiciona uma nova linha ao final (append) e registra a data.
5. **Renomear nota**: usando `renameTo`, validando se o novo nome já existe.
6. **Arquivar nota**: move para `notas/arquivadas/`.
7. **Buscar palavra**: procura uma palavra em todas as notas e informa em quais arquivos e linhas ela aparece.
8. **Relatório**: gera `notas/relatorio.txt` com a quantidade de notas por pasta e o tamanho total.
9. **Apagar nota** (com confirmação simulada e verificação de existência).

## Regras

- Todo acesso a arquivo usa try-with-resources.
- Cada funcionalidade é um método separado, com tratamento de exceções claro (`FileNotFoundException`, `IOException`).
- Nenhuma mensagem pode ser engolida em branco no `catch`.
- Valide nomes de arquivo (use uma regex simples do bloco anterior para impedir caracteres como `/ \ : * ? " < > |`).

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei que `new File(...)` não cria nada em disco.
- [ ] Sei usar `createNewFile`, `exists`, `delete`, `mkdir`, `mkdirs`.
- [ ] Sei a diferença entre caminho relativo e absoluto.
- [ ] Sei usar `FileWriter` com e sem append.
- [ ] Entendo o que é `flush()` e buffer.
- [ ] Sei usar `FileReader` e o laço `while ((i = read()) != -1)`.
- [ ] Sei usar `BufferedWriter` e `newLine()`.
- [ ] Sei usar `BufferedReader` e `readLine()` até `null`.
- [ ] Sei encadear as classes de IO.
- [ ] Sei usar `renameTo` para renomear e mover.
- [ ] Sei listar uma pasta com `list()`/`listFiles()`.
- [ ] Sempre uso try-with-resources.

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
