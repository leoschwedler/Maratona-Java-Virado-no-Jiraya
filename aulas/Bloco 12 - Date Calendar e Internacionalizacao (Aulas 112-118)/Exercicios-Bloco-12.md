# Exercícios — Bloco 12: Date, Calendar e Internacionalização

## Aulas 112 a 118

Este arquivo acompanha o README do Bloco 12.

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

> **Onde criar os arquivos:** `src/main/bloco12_aulas112a118_date_calendar_locale/aulaXXX/`
> Crie uma classe por exercício (`Ex01`, `Ex02`...) dentro da pasta da aula.

> **Importante:** não copie o exemplo da aula. Os exercícios mudam o contexto para verificar se você realmente entendeu o conceito. Antes de rodar, escreva em um comentário o que você **espera** que apareça no console.

---

# Aula 112 — Date

## 🟢 Exercício 01 — Viagem no tempo

Crie um programa que:

1. Crie um `Date` com o valor `0`.
2. Crie um `Date` com `86_400_000L`.
3. Crie um `Date` com `System.currentTimeMillis()`.
4. Imprima os três e também o valor `long` de cada um usando `getTime()`.

### Você deve conseguir responder

- Que data representa o valor `0`?
- Quantos milissegundos tem um dia? E qual a diferença entre os dois primeiros `Date`?

---

## 🟡 Exercício 02 — Adiantando o relógio

Crie um programa que:

1. Crie um `Date` com a data/hora atual.
2. Crie um método `static Date somarHoras(Date data, int horas)` que **retorne um novo `Date`** com as horas somadas (sem alterar o original).
3. Chame o método com `1`, `24` e `-3` horas e imprima o original e os resultados.

### Regras

- Faça a conta em **milissegundos**, usando `getTime()`.
- Não use `Calendar` neste exercício.
- O `Date` original **não** pode ser modificado.

### Pergunta

Por que `Date` ser **mutável** é perigoso quando você passa o objeto para outro método?

---

## 🔴 Exercício 03 — Linha do tempo de eventos

Crie uma classe `Evento` com `String nome` e `Date data`. No `main`, crie uma lista de 5 eventos com datas diferentes (use milissegundos para criar as datas).

Faça:

1. Descobrir qual evento é o **mais antigo** e qual é o **mais recente**, usando `before()`/`after()`/`compareTo()`.
2. Imprimir quantos **dias** existem entre o evento mais antigo e o mais recente (conta com `long`).
3. Imprimir os eventos em ordem cronológica **sem usar `Collections.sort`** (use um laço duplo simples, estilo bubble sort, trocando as posições).

### Dica

Use `array` de `Evento` para fixar a ideia de comparação de datas.

---

# Aula 113 — Calendar

## 🟢 Exercício 04 — Raio-X do dia

Usando `Calendar.getInstance()`, imprima:

```text
Ano:
Mês (número que o Calendar devolve):
Mês (número que um humano espera):
Dia do mês:
Dia da semana (número):
Dia do ano:
```

### Pergunta

Por que o "mês que um humano espera" exige somar `1`?

---

## 🟡 Exercício 05 — Prazo de entrega

Uma loja promete entrega em **10 dias úteis**.

Crie um método `static Calendar calcularEntrega(Calendar compra)` que some dias um por um, **pulando sábados e domingos**, até completar 10 dias úteis. Use `Calendar.SATURDAY` e `Calendar.SUNDAY`.

Teste com 3 datas de compra diferentes (uma delas numa sexta-feira) e imprima o resultado usando `getTime()`.

### Regras

- Não clone o objeto original sem pensar: o método não deve alterar o `Calendar` recebido (dica: use `clone()` ou crie outro com `getInstance()` e `setTime`).

---

## 🔴 Exercício 06 — `add` x `roll` na prática

Crie um programa que, partindo da data **31/12/2021 23:00**, execute e imprima o resultado de cada operação em um `Calendar` **novo** (parta sempre da mesma data):

```text
add(HOUR_OF_DAY, 2)
roll(HOUR_OF_DAY, 2)
add(MONTH, 2)
roll(MONTH, 2)
add(DAY_OF_MONTH, 45)
roll(DAY_OF_MONTH, 45)
```

Antes de rodar, escreva no comentário o resultado esperado de cada linha. Depois compare.

### Desafio extra

Explique com suas palavras: em que situação real de negócio (ex.: vencimento de boleto) você usaria `add` e em qual você usaria `roll`? (Dica: provavelmente em quase nenhuma usa `roll`. Justifique.)

---

# Aula 114 — DateFormat

## 🟢 Exercício 07 — Quatro estilos

Imprima a data de hoje usando `DateFormat.getDateInstance` nos estilos `SHORT`, `MEDIUM`, `LONG` e `FULL`, cada um em uma linha, com um rótulo na frente (`SHORT -> ...`).

### Regras

- Use um `array` de `DateFormat` e um `for`, como na aula.
- Lembre-se: `format` recebe `Date`, não `Calendar`.

---

## 🟡 Exercício 08 — Recibo com data e hora

Monte um "recibo" de texto que imprima:

```text
===== RECIBO =====
Emitido em: <data LONG> às <hora SHORT>
Impresso em: <data e hora MEDIUM>
```

Use `getDateInstance`, `getTimeInstance` e `getDateTimeInstance` conforme o caso.

### Pergunta

Por que o mesmo programa pode imprimir resultados diferentes em dois computadores diferentes?

---

## 🔴 Exercício 09 — Linha do tempo formatada

Pegue o `array` de eventos do Exercício 03 e imprima uma tabela:

```text
Evento           | Data (SHORT) | Data (FULL)
-----------------+--------------+----------------------
Lançamento       | 2/11/21      | Thursday, February 11, 2021
```

### Regras

- Os nomes dos eventos devem ficar alinhados (use `printf` com `%-15s`).
- Use um único `DateFormat` por estilo (crie fora do `for`).

---

# Aula 115 — Locale e datas

## 🟢 Exercício 10 — Um mesmo dia, vários países

Imprima a data atual no estilo `FULL` para estes `Locale`:

```text
Brasil (pt, BR)
Estados Unidos (Locale.US)
Japão (Locale.JAPAN)
Alemanha (Locale.GERMANY)
```

Crie um `array` de `Locale` e use um `for`.

---

## 🟡 Exercício 11 — Tradução de países

Para os mesmos quatro `Locale` do exercício anterior, imprima:

```text
Locale  | País (no idioma do SO) | País (em português) | País (no idioma do próprio país)
```

Use `getDisplayCountry()` com e sem argumento.

### Pergunta

Qual a diferença entre `getDisplayCountry()` e `getDisplayCountry(Locale)`?

---

## 🔴 Exercício 12 — Menu multi-país

Crie um programa que simula um site internacional:

1. Mostra um menu com 4 países (Brasil, EUA, Itália, Japão).
2. Dependendo da escolha (use um `Scanner` ou uma variável fixa se preferir), exibe uma **mensagem de boas-vindas** com a data de hoje por extenso formatada para aquele país.
3. A estrutura deve ser um método `static void exibirBoasVindas(Locale locale)`.

### Regras

- Nada de `if` por país para formatar data: o `Locale` deve ser o que muda o resultado.
- O único `switch`/`if` permitido é o que **escolhe** o `Locale`.

---

# Aula 116 — NumberFormat e números

## 🟢 Exercício 13 — Mil e uma formas

Formate o número `1234567.891` para os `Locale` Brasil, EUA, Alemanha e Japão usando `NumberFormat.getInstance`.

Imprima uma linha por país e escreva em um comentário **quais separadores** (milhar e decimal) cada país usa.

---

## 🟡 Exercício 14 — Inventário de Locales

Faça um programa que:

1. Mostre `Locale.getDefault()`.
2. Mostre **quantos** países ISO e **quantos** idiomas ISO existem (`getISOCountries().length`, etc.).
3. Mostre **quantos** `Locale` estão disponíveis (`getAvailableLocales().length`).
4. Imprima apenas os `Locale` disponíveis cujo idioma seja `"pt"` (use `getLanguage()`).

### Pergunta

Por que existem mais `Locale` do que idiomas?

---

## 🔴 Exercício 15 — Ranking formatado

Crie um array de `double` com 5 "populações de cidades" (valores como `12_325_232.5`). Imprima uma tabela com o número formatado **para cada um dos três** `Locale` (Brasil, EUA, Itália), alinhada em colunas.

Depois, faça o experimento:

1. Descubra o `getMaximumFractionDigits()` padrão.
2. Altere para `0` e depois para `5`.
3. Imprima o mesmo valor com cada configuração.

### Pergunta

Que tipo de bug aconteceria se um sistema brasileiro lesse `"1.250"` achando que está no padrão americano?

---

# Aula 117 — NumberFormat e moedas

## 🟢 Exercício 16 — Etiqueta de preço

O preço de um produto é `1999.9`. Imprima o valor como moeda para Brasil, EUA, Japão e Itália.

Responda em comentário: por que o iene não mostra casas decimais?

---

## 🟡 Exercício 17 — Parse seguro

Crie um método `static double lerValor(String texto, Locale locale)` que:

1. Use `NumberFormat.getCurrencyInstance(locale)` e `parse`.
2. Trate `ParseException` imprimindo `"Valor inválido: <texto>"` e devolvendo `-1`.

Teste com:

```text
lerValor("R$ 1.250,90", brasil)
lerValor("$1,250.90", Locale.US)
lerValor("$1,250.90", brasil)      // o que acontece?
lerValor("abc", Locale.US)
```

---

## 🔴 Exercício 18 — Conversor de carrinho

Crie uma classe `Carrinho` com uma lista de preços (array de `double`).

Implemente:

1. `double total()`.
2. `String totalFormatado(Locale locale)`.
3. `static double somarDeTextos(String[] valores, Locale locale)` que converte vários textos monetários para número e soma, **ignorando** os inválidos (mas contando quantos foram ignorados).

No `main`, mostre o mesmo carrinho em 3 países e a soma dos textos.

### Pergunta

Por que, em um sistema real, o banco de dados guarda o `double`/`BigDecimal` e não o texto "R$ 1.250,90"?

---

# Aula 118 — SimpleDateFormat

## 🟢 Exercício 19 — Padrões básicos

Imprima a data/hora atual nos formatos:

```text
15/02/2021
2021-02-15
15/02/2021 14:35:09
14:35
```

Cada um com um `SimpleDateFormat` diferente.

---

## 🟡 Exercício 20 — Por extenso

Crie um programa que imprima:

```text
Hoje é segunda-feira, 15 de fevereiro de 2021, às 14h35min.
```

### Regras

- Use texto literal entre aspas simples.
- Use `Locale` do Brasil para sair em português.
- O dia/mês/ano/hora devem ser reais (a data de hoje), não escritos à mão.

### Pergunta

O que acontece se você esquecer as aspas simples em `de`?

---

## 🔴 Exercício 21 — Validador de datas

Crie um método `static boolean dataValida(String texto)` que usa `SimpleDateFormat("dd/MM/yyyy")` com `setLenient(false)` e retorna `true`/`false`.

Teste:

```text
"15/02/2021"   → true
"31/02/2021"   → false
"29/02/2024"   → true
"29/02/2023"   → false
"2021-02-15"   → false
"abc"          → false
```

Depois responda: o que mudaria no resultado se você **não** chamasse `setLenient(false)`? Teste e comprove.

### Desafio extra

Crie `static int diasAte(String dataFutura)` que devolva quantos dias faltam, partindo de uma data textual no formato `dd/MM/yyyy` até hoje.

---

# 🏆 Desafio Integrador do Bloco 12 — Sistema de Fatura Internacional

Você vai construir uma pequena **fatura** que funciona para clientes de vários países.

## Requisitos

Crie as classes:

```text
Cliente   → nome, Locale
Item      → descricao, quantidade (int), precoUnitario (double)
Fatura    → cliente, Date emissao, Date vencimento, lista de Item (array)
```

### Regras de negócio

1. A data de **emissão** é a data de hoje (use `Calendar`).
2. O **vencimento** é emissão + 30 dias usando `Calendar.add`. Se cair em sábado ou domingo, empurre para a **segunda-feira** seguinte.
3. A fatura é impressa **de acordo com o `Locale` do cliente**:
   - emissão e vencimento em `FULL`;
   - valores unitários e total em moeda do país;
   - quantidade formatada como número.
4. Crie também uma impressão com `SimpleDateFormat` fixo no formato `dd/MM/yyyy` para uso interno da empresa.
5. Crie um método que, recebendo um texto como `"20/03/2021"`, converta para `Date` com `parse`, trate `ParseException`, e permita **alterar o vencimento** manualmente.

### Teste

No `main`, crie 3 clientes (Brasil, Estados Unidos, Japão) e imprima a mesma fatura para cada um.

### Saída esperada (exemplo conceitual)

```text
========= FATURA =========
Cliente: Maria (pt_BR)
Emissão: segunda-feira, 15 de fevereiro de 2021
Vencimento: segunda-feira, 15 de março de 2021
--------------------------
2 x Mouse        R$ 50,00     R$ 100,00
1 x Teclado      R$ 150,00    R$ 150,00
--------------------------
TOTAL                         R$ 250,00
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei explicar o que um `Date` guarda.
- [ ] Sei por que a maioria dos métodos de `Date` é deprecated.
- [ ] Sei a diferença entre `java.util.Date` e `java.sql.Date`.
- [ ] Sei criar um `Calendar` com `getInstance()`.
- [ ] Sei que o mês do `Calendar` começa em 0.
- [ ] Sei usar `get`, `set`, `add` e `roll`.
- [ ] Sei explicar a diferença entre `add` e `roll`.
- [ ] Sei converter `Calendar` ↔ `Date`.
- [ ] Sei usar `DateFormat` com os 4 estilos.
- [ ] Sei criar um `Locale` e entendo ISO 639 / ISO 3166.
- [ ] Sei usar `getDisplayCountry` e `getDisplayLanguage`.
- [ ] Sei formatar números por país com `NumberFormat`.
- [ ] Sei formatar moeda por país com `getCurrencyInstance`.
- [ ] Sei usar `parse` e tratar `ParseException`.
- [ ] Sei montar padrões do `SimpleDateFormat` sem confundir `MM`/`mm` e `HH`/`hh`.
- [ ] Sei usar aspas simples para texto literal.
- [ ] Sei usar `setLenient(false)`.

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
