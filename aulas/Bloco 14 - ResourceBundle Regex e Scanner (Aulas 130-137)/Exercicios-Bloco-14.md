# Exercícios — Bloco 14: ResourceBundle, Regex e Scanner

## Aulas 130 a 137

Este arquivo acompanha o README do Bloco 14.

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

> **Onde criar os arquivos:** `src/main/bloco14_aulas130a137_resourcebundle_regex_scanner/aulaXXX/`
> Os arquivos `.properties` da aula 130 devem ficar em uma pasta marcada como *Resources Root* (ex.: `src/main/resources`).

> **Dica de estudo:** para os exercícios de Regex, teste primeiro a expressão em um site como o regex101.com e só depois passe para o Java (lembre-se de dobrar as barras: `\d` vira `"\\d"`).

---

# Aula 130 — ResourceBundle

## 🟢 Exercício 01 — Saudação internacional

Crie dois arquivos de mensagens:

```text
saudacoes_en_US.properties
saudacoes_pt_BR.properties
```

Cada um com as chaves `ola`, `tchau` e `obrigado`.

No `main`, carregue o `ResourceBundle` para os dois `Locale` e imprima as três mensagens em cada idioma.

### Você deve conseguir responder

Por que o nome base passado ao `getBundle` não leva a extensão `.properties`?

---

## 🟡 Exercício 02 — Fallback na prática

Crie os arquivos:

```text
loja_pt_BR.properties    → chaves: titulo, carrinho
loja_en_US.properties    → chaves: titulo, carrinho
loja.properties          → chaves: moeda, suporte
```

Faça um programa que peça os valores abaixo para o `Locale` `pt_BR`, `en_US` e `fr_CA`:

```text
titulo
carrinho
moeda
suporte
```

Anote em comentários **de qual arquivo cada resposta saiu** antes de executar e depois confirme.

### Responda

Qual é a ordem completa de busca quando o `Locale` pedido é `fr_CA` e o `Locale` padrão da JVM é `en_US`?

---

## 🔴 Exercício 03 — Menu multilíngue

Crie um mini menu de console com 3 opções de idioma (Português, Inglês, Espanhol). Depois de escolher:

1. Carregue o `ResourceBundle` correspondente (crie os 3 arquivos).
2. Mostre um menu com 4 opções (`menu.novo`, `menu.abrir`, `menu.salvar`, `menu.sair`) usando as mensagens traduzidas.
3. Se a chave não existir em algum arquivo, **não deixe o programa quebrar**: use `containsKey` para mostrar `"???chave???"` no lugar.

### Regras

- Nenhuma mensagem visível pode estar escrita direto no código Java (só as chaves).
- Crie um método `static String msg(ResourceBundle bundle, String chave)`.
- Deixe de propósito uma chave faltando em um dos arquivos para testar o tratamento.

---

# Aula 131 — Regex pt 01: `Pattern` e `Matcher`

## 🟢 Exercício 04 — Contando ocorrências

Dado o texto `"banana na banca da bananeira"`, use `Pattern`/`Matcher` para:

1. Contar quantas vezes aparece `"na"`.
2. Imprimir a posição (`start()`) de cada uma.

---

## 🟡 Exercício 05 — Sem sobreposição

Procure `"aa"` no texto `"aaaaa"` e `"aba"` em `"abababababa"`.

Antes de executar, escreva em comentário **quantas** ocorrências você espera e **em que posições**. Depois execute e explique por que o resultado não é o que a sua intuição de "todas as possibilidades" sugeriria.

---

## 🔴 Exercício 06 — `find`, `matches` e `lookingAt`

Para o padrão `"java"` e os textos abaixo, preencha uma tabela com o resultado esperado e depois confirme no código:

```text
Texto                     find()   matches()   lookingAt()
"java"
"java 17"
"eu amo java"
"JAVA"
"javajava"
```

Crie um método `static void testar(String regex, String texto)` que imprima as três respostas, e use-o para todas as linhas.

### Desafio extra

Use a flag `Pattern.CASE_INSENSITIVE` e refaça a tabela. O que mudou?

---

# Aula 132 — Regex pt 02: Metacaracteres

## 🟢 Exercício 07 — Só os números

Do texto `"Pedido 12 custa R$ 340 e vence em 05 dias"`, extraia apenas os números usando `\d+` e imprima cada um.

Depois use `\D+` e veja o que muda.

---

## 🟡 Exercício 08 — Raio-X do texto

Para o texto `"Java 17, versão: LTS!\tOK_2024"`, conte e imprima quantos caracteres são:

```text
dígitos          (\d)
não dígitos      (\D)
espaços          (\s)
palavra          (\w)
não palavra      (\W)
```

Confira se `\d + \D` é igual ao tamanho do texto. E `\w + \W`?

---

## 🔴 Exercício 09 — Limpador de texto

Crie um método `static String limpar(String texto)` que:

1. Troque qualquer sequência de espaços em branco (inclui tab e quebra de linha) por um único espaço (`replaceAll`).
2. Remova qualquer caractere que **não** seja letra, número ou espaço (use um metacaractere e a negação).
3. Faça `trim()` no final.

Teste com:

```text
"   Olá,   mundo!!!\n\tJava @ 2024   "
"###Preço:   R$   19,90***"
```

### Pergunta

Por que, ao usar `\w`, as letras acentuadas (á, ç) podem ser tratadas como "não palavra" por padrão no Java? (Pesquise `Pattern.UNICODE_CHARACTER_CLASS` depois de tentar sozinho.)

---

# Aula 133 — Regex pt 03: Range

## 🟢 Exercício 10 — Vogais

Do texto `"Programação em Java é divertido"`, encontre todas as vogais minúsculas sem acento usando `[aeiou]` e conte quantas existem.

Depois inclua as maiúsculas e veja se a contagem muda.

---

## 🟡 Exercício 11 — Placas de carro

Considere o formato antigo de placa brasileira: **3 letras maiúsculas + 4 números** (ex.: `ABC1234`).

Escreva uma regex **sem usar quantificadores** (só colchetes repetidos) que ache placas válidas no texto:

```text
"Carros: ABC1234, abc1234, AB12345, XYZ9876, QWE12"
```

### Pergunta

Por que a versão sem quantificadores é desconfortável? (Vai servir de gancho para a próxima aula.)

---

## 🔴 Exercício 12 — Hexadecimal por partes

Reproduza o exercício da aula, mas sem copiar a resposta final:

1. Crie o texto `"valores: 0x1F 0X2a 0x 0xZZ 0x9 0xFFFF 0x1G"`.
2. Ache primeiro só o prefixo `0x`/`0X`.
3. Depois acrescente **um** dígito hexa.
4. Anote em comentário o que a regex achou em cada passo.
5. Explique por que ainda **não** conseguimos pegar `0xFFFF` inteiro apenas com ranges.

---

# Aula 134 — Regex pt 04: Quantificadores pt 01

## 🟢 Exercício 13 — Telefones

Ache no texto abaixo todos os telefones no formato `(DD) NNNNN-NNNN`:

```text
"Ligue (11) 98765-4321 ou (21) 91234-5678. Errado: (1) 9876-54321 e 11 98765-4321"
```

Use `\d{n}`.

---

## 🟡 Exercício 14 — `*`, `+`, `?` e `{n,m}`

Para cada regex, escreva em comentário o que ela casa e teste com o texto `"a ab abb abbb b"`:

```text
ab?
ab*
ab+
ab{2}
ab{1,2}
(ab)+
```

Imprima o `group()` de cada ocorrência e confira.

---

## 🔴 Exercício 15 — Hexadecimal completo

Construa a regex final da aula **por conta própria**, sem olhar o README:

```text
0[xX][0-9a-fA-F]+(\s|$)
```

Teste com `"12 0x 0X 0xFFABC 0x109 0x1 0xG1 0x23"`.

Depois responda:

1. Por que `*` no lugar de `+` seria um erro?
2. O que o grupo `(\s|$)` faz?
3. O que aconteceria com `0x1G` se você tirasse o grupo?
4. Altere a regex para **não** incluir o espaço no `group()` (dica: pesquise *lookahead* `(?=\s|$)` depois de tentar sozinho).

---

# Aula 135 — Regex pt 05: Quantificadores pt 02

## 🟢 Exercício 16 — CEP

Valide, com `matches`, CEPs no formato `NNNNN-NNN`:

```text
"01310-100"   → true
"01310100"    → false
"1310-100"    → false
"01310-1000"  → false
```

---

## 🟡 Exercício 17 — Extrator de e-mails

Dado o texto:

```text
"contato@empresa.com.br; ana_silva@gmail.com, joao#teste.com; @erro.com, maria.s@uol.com.br e suporte@x"
```

1. Extraia todos os e-mails válidos (use a regex da aula como base).
2. Depois use `split` com uma regex (`[;,\\s]+`) e **valide cada pedaço** com `matches`.
3. Compare os dois resultados e anote as diferenças.

---

## 🔴 Exercício 18 — Validador de senha

Crie um método `static boolean senhaForte(String senha)`.

Regras:

- Entre 8 e 20 caracteres.
- Pelo menos uma letra minúscula.
- Pelo menos uma letra maiúscula.
- Pelo menos um dígito.
- Pelo menos um caractere especial (`!@#$%&*`).
- Sem espaços.

### Orientação

Resolva primeiro **sem** lookahead, combinando várias regex pequenas (`find()` em cada). Cada requisito pode ser uma verificação separada. Depois mostre qual das senhas é inválida e **por qual regra** (mensagem específica).

Teste:

```text
"Abcdef1!"      → forte
"abcdef1!"      → falta maiúscula
"ABCDEF1!"      → falta minúscula
"Abcdefg!"      → falta dígito
"Abcdef12"      → falta especial
"Abc 123!x"     → tem espaço
```

---

# Aula 136 — Regex pt 06: Âncora e negação

## 🟢 Exercício 19 — Início de linha

Dado o texto em várias linhas:

```text
"Java é legal\nKotlin também\nScala é diferente"
```

Use `^\\w+` com `Pattern.MULTILINE` para imprimir a primeira palavra de cada linha. Depois remova a flag e veja o que muda.

---

## 🟡 Exercício 20 — Negação

Do texto `"Cod: A1-B2_C3 @ D4!"`, usando `[^...]`:

1. Extraia tudo que **não** for letra nem dígito.
2. Substitua esses caracteres por `_` com `replaceAll`.

Qual a diferença entre o `^` fora e dentro de colchetes?

---

## 🔴 Exercício 21 — Validadores de formulário

Crie uma classe `Validador` com métodos estáticos que retornam `boolean`, cada um usando `matches`:

```text
cpfFormatado("123.456.789-09")        → formato NNN.NNN.NNN-NN
dataBr("31/12/2024")                  → dd/MM/yyyy (só o formato, sem checar se existe)
horario24h("23:59")                   → HH:mm entre 00:00 e 23:59
placaMercosul("ABC1D23")              → 3 letras + 1 dígito + 1 letra + 2 dígitos
hexColor("#A1B2C3")                   → # + 6 dígitos hexa
```

Crie pelo menos 4 testes (2 válidos e 2 inválidos) para cada validador.

### Pergunta

Por que validar apenas o **formato** não garante que um CPF ou data são realmente válidos?

---

# Aula 137 — Scanner: Tokens e Delimitadores

## 🟢 Exercício 22 — Quebrando com split

Dado `"Maria;João;Ana;Pedro"`, quebre o texto de duas formas: com `split` e com `Scanner` + `useDelimiter`. Imprima os nomes das duas maneiras e confirme que são iguais.

---

## 🟡 Exercício 23 — Tipos misturados

Dado o texto:

```text
"Notebook, 3500.50, 2, true, Mouse, 89.9, 10, false"
```

Use `Scanner` com o delimitador `,\\s*` e classifique cada token como `int`, `double`, `boolean` ou `String`, imprimindo o tipo e o valor.

### Pergunta

O que acontece se você testar `hasNextDouble()` **antes** de `hasNextInt()`? Teste e explique.

---

## 🔴 Exercício 24 — Importador de produtos

Cada linha do texto representa um produto:

```text
"1;Notebook;3500.50;true\n2;Mouse;89.90;true\n3;Monitor;abc;false\n4;Teclado;150;maybe"
```

Crie uma classe `Produto` (`id`, `nome`, `preco`, `ativo`). Faça:

1. Separe as linhas (use `Scanner` com `\n` ou `split("\\n")`).
2. Para cada linha, use outro `Scanner` com delimitador `;` e valide **antes** de consumir (`hasNextInt`, `hasNext`, `hasNextDouble`, `hasNextBoolean`).
3. Linhas inválidas não entram na lista; imprima `Linha N inválida: <motivo>`.
4. No final, imprima os produtos válidos e a soma dos preços.

---

# 🏆 Desafio Integrador do Bloco 14 — Analisador de Logs Multilíngue

Você recebe um arquivo de log (como `String` fixa no código) com linhas assim:

```text
2024-05-10 14:32:10 | ERROR | usuario=ana@empresa.com | ip=192.168.0.10 | msg=login_falhou
2024-05-10 14:33:02 | INFO  | usuario=joao@teste.com  | ip=10.0.0.5     | msg=login_ok
2024-05-10 14:35:47 | WARN  | usuario=invalido#x.com  | ip=999.1.1.1    | msg=email_invalido
2024-05-10 14:40:00 | ERROR | usuario=maria.s@uol.com.br | ip=172.16.0.20 | msg=timeout
```

## Requisitos

1. Use `Scanner` com delimitador para separar os campos de cada linha (`|`).
2. Use **regex** para:
   - validar o e-mail do usuário;
   - validar o IP (4 grupos de 1 a 3 dígitos separados por ponto; bônus: cada grupo entre 0 e 255);
   - extrair a data e a hora.
3. Converta data/hora para `LocalDateTime` (bloco anterior).
4. Conte quantas linhas de cada nível (`ERROR`, `WARN`, `INFO`).
5. As **mensagens exibidas ao usuário** (relatório) devem vir de `ResourceBundle`, em português e inglês (`relatorio_pt_BR.properties`, `relatorio_en_US.properties`), com chaves como `total.erros`, `linha.invalida`, `ip.invalido`.
6. Formate datas com `DateTimeFormatter` de acordo com o `Locale`.
7. Linhas com e-mail ou IP inválidos devem ser listadas num bloco separado, com o motivo.

## Saída esperada (exemplo conceitual, em português)

```text
=== Relatório de logs ===
Total de linhas: 4
Erros: 2 | Avisos: 1 | Informações: 1

Linhas inválidas:
 - Linha 3: e-mail inválido (invalido#x.com), IP inválido (999.1.1.1)
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei criar arquivos `.properties` com a nomenclatura correta.
- [ ] Sei usar `ResourceBundle.getBundle` e `getString`.
- [ ] Sei explicar a ordem de fallback.
- [ ] Sei usar `Pattern.compile`, `matcher`, `find`, `start`, `group`.
- [ ] Sei a diferença entre `find`, `matches` e `lookingAt`.
- [ ] Sei dobrar a barra invertida em Java.
- [ ] Sei usar `\d`, `\s`, `\w` e os opostos.
- [ ] Sei usar ranges e negação `[^...]`.
- [ ] Sei usar `? * + {n,m}`.
- [ ] Sei usar grupos `( )` e alternativa `|`.
- [ ] Sei usar `^` e `$`.
- [ ] Sei usar `split` com regex.
- [ ] Sei usar `Scanner` com `useDelimiter`.
- [ ] Sei usar `hasNextXxx()` antes de `nextXxx()`.

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
