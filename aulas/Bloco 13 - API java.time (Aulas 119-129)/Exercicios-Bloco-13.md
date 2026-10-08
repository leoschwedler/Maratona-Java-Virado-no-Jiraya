# Exercícios — Bloco 13: API `java.time`

## Aulas 119 a 129

Este arquivo acompanha o README do Bloco 13.

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

> **Onde criar os arquivos:** `src/main/bloco13_aulas119a129_java_time/aulaXXX/`
> Crie uma classe por exercício (`Ex01`, `Ex02`...) dentro da pasta da aula.

> **Regra do bloco:** nada de `Date`, `Calendar` ou `SimpleDateFormat`. Só `java.time`.
> Antes de executar, escreva em comentário o resultado que você espera.

---

# Aula 119 — LocalDate

## 🟢 Exercício 01 — Ficha da data

Crie a data `27/01/2022` com `LocalDate.of` usando o enum `Month`. Imprima:

```text
Ano:
Mês (enum):
Mês (número):
Dia do mês:
Dia da semana:
Dia do ano:
Quantos dias tem o mês:
É bissexto?:
```

---

## 🟡 Exercício 02 — A armadilha da imutabilidade

Escreva um programa que:

1. Crie `LocalDate hoje = LocalDate.now();`.
2. Chame `hoje.plusDays(10)` **sem guardar o resultado**, e imprima `hoje`.
3. Guarde o resultado em outra variável e imprima as duas.
4. Crie um método `static void avancarUmMes(LocalDate data)` que faz `data = data.plusMonths(1)` e imprime dentro do método. Depois imprima a variável original no `main`.

### Responda em comentário

- Por que `hoje` não mudou na linha 2?
- Por que o valor original não mudou depois de chamar `avancarUmMes`?
- Que bug seria possível se `LocalDate` fosse mutável, como o `Date`?

---

## 🔴 Exercício 03 — Calendário de aniversários

Crie uma classe `Pessoa` com `nome` e `LocalDate nascimento`.

Crie 4 pessoas e, para cada uma, imprima:

1. A **idade** atual (sem usar `ChronoUnit`; compare `getYear()` e ajuste se o aniversário ainda não aconteceu neste ano — use `getMonthValue()`/`getDayOfMonth()` ou `isBefore`).
2. O **dia da semana** em que ela nasceu.
3. Se nasceu em **ano bissexto**.
4. A **data do próximo aniversário** (se já passou neste ano, use o ano seguinte).
5. Se o aniversário é em **29/02**, o que acontece no ano não bissexto? (Teste `LocalDate.of(2023, 2, 29)` e `LocalDate.of(2000,2,29).withYear(2023)` e anote o que viu.)

---

# Aula 120 — LocalTime

## 🟢 Exercício 04 — Horário da loja

Uma loja abre às `08:30` e fecha às `18:00`. Crie esses dois `LocalTime` e um terceiro com a hora atual. Imprima se a loja está **aberta** ou **fechada** usando `isBefore`/`isAfter`.

Teste com 3 horários fixos (07:00, 12:00, 19:30), não só com `now()`.

---

## 🟡 Exercício 05 — Hora inválida

Crie um método `static LocalTime criarHora(int h, int m, int s)` que tenta criar o `LocalTime` e, se os valores forem inválidos, imprime uma mensagem amigável e retorna `LocalTime.MIDNIGHT`.

Teste com:

```text
(10, 30, 0)
(24, 0, 0)
(12, 60, 0)
(-1, 0, 0)
```

### Responda

- Que exceção é lançada? Ela é checked ou unchecked?
- Qual a diferença com `Calendar`, que aceitava horas fora do intervalo?

---

## 🔴 Exercício 06 — Turnos de trabalho

Crie um método `static String turno(LocalTime hora)` que devolva:

```text
Madrugada  → 00:00 até 05:59
Manhã      → 06:00 até 11:59
Tarde      → 12:00 até 17:59
Noite      → 18:00 até 23:59:59.999999999
```

Use `LocalTime.MIDNIGHT`, `LocalTime.NOON`, `LocalTime.MAX` e `LocalTime.of`.

Teste com pelo menos 8 horários, incluindo os limites exatos (`05:59:59`, `06:00`, `11:59:59`, `12:00`, `23:59:59.999999999`).

### Desafio extra

Imprima quantas horas faltam para a meia-noite, a partir da hora atual (use `LocalTime.MAX`, mas explique por que o resultado tem uma pequena imprecisão de 1 nanossegundo).

---

# Aula 121 — LocalDateTime

## 🟢 Exercício 07 — Cinco jeitos de montar

Crie a data e hora `06/08/2022 09:45` de **cinco formas diferentes**:

1. `LocalDateTime.of(ano, mês, dia, hora, minuto)`.
2. `LocalDateTime.of(LocalDate, LocalTime)`.
3. `LocalDate.atTime(...)`.
4. `LocalTime.atDate(...)`.
5. `LocalDateTime.parse("...")`.

Imprima as cinco e compare com `equals`. Todas devem dar `true`.

---

## 🟡 Exercício 08 — Agenda do dia

Crie uma classe `Compromisso` (`titulo` e `LocalDateTime inicio`) e uma lista (array) com 5 compromissos em dias diferentes.

Peça para:

1. Imprimir só os que acontecem **hoje**.
2. Imprimir só os que ainda **vão acontecer** (`isAfter(now)`).
3. Imprimir o compromisso mais próximo no futuro.

Para testar, crie datas relativas a `now()` (ex.: `now().plusHours(2)`, `now().minusDays(1)`).

---

## 🔴 Exercício 09 — Relatório do dia

Crie `static LocalDateTime[] intervaloDoDia(LocalDate dia)` que devolve um array de 2 posições: `[0]` o início do dia (`00:00`) e `[1]` o fim (`23:59:59.999999999`).

Depois, dado um array de 6 `LocalDateTime` de vendas, conte quantas aconteceram dentro do intervalo de um dia escolhido. Teste com vendas à meia-noite exata, `23:59:59` e `00:00` do dia seguinte para comprovar que o limite está correto.

### Responda

Por que usar `LocalTime.MAX` evita perder uma venda às `23:59:59.5`?

---

# Aula 122 — Instant

## 🟢 Exercício 10 — O instante e o Z

Imprima `Instant.now()`, `LocalDateTime.now()` e `ZonedDateTime.now()` lado a lado. Explique em comentário:

- O que significa o `Z` no final do `Instant`?
- Por que `Instant` e `LocalDateTime` podem mostrar horas diferentes no mesmo computador?

---

## 🟡 Exercício 11 — Brincando com a época

Usando `Instant.ofEpochSecond`, crie:

1. O instante zero.
2. Um instante 3 segundos depois da época.
3. Some `1_500_000_000L` nanossegundos a ele e imprima `getEpochSecond()` e `getNano()`.
4. Subtraia 5 segundos do resultado (valor negativo antes da época).

### Responda

O que o `getNano()` representa? Por que não existe um único `long` com os nanossegundos desde 1970?

---

## 🔴 Exercício 12 — Cronômetro

Crie uma classe `Cronometro` com `iniciar()` e `parar()` que usa `Instant` para medir quanto tempo um trecho de código demora.

Teste medindo:

1. Um laço que soma de 1 a 10 milhões.
2. Um `Thread.sleep(250)`.

Imprima o resultado em **milissegundos** e em **nanossegundos** (use `Duration.between(...)`, `toMillis()` e `toNanos()`).

### Desafio extra

Compare com `System.nanoTime()`. Qual é recomendado para medir desempenho e qual para marcar "um momento do calendário"? Justifique.

---

# Aula 123 — Duration

## 🟢 Exercício 13 — Lendo o PT

Crie 5 durações com `Duration.ofX` e imprima cada uma. Anote em comentário o que significa cada texto (`PT2H30M`, `PT45S`...).

Inclua `Duration.ofDays(3)` e explique por que aparece como horas.

---

## 🟡 Exercício 14 — Duração de uma viagem

Voo decola às `22:40` do dia 10 e chega às `06:15` do dia 11 (use `LocalDateTime`).

Imprima:

1. A duração total (`PT...`).
2. Em horas e minutos separados (`toHours()` e `toMinutes() % 60`, ou `toMinutesPart()` se estiver no Java 9+).
3. O total em minutos.
4. O horário de chegada **se houvesse atraso de 1h45** (`plus(Duration)`).

---

## 🔴 Exercício 15 — Armadilha com LocalDate

Tente executar `Duration.between` com dois `LocalDate` e **capture a exceção**. Depois:

1. Mostre a mensagem da exceção.
2. Resolva o problema de três formas diferentes (sem mudar o objetivo, que é medir a distância entre dois dias):
   - convertendo para `LocalDateTime` com `atStartOfDay()`;
   - usando `Period`;
   - usando `ChronoUnit.DAYS`.
3. Tente também `Duration.of(2, ChronoUnit.MONTHS)` e explique por que falha.

---

# Aula 124 — Period

## 🟢 Exercício 16 — Prazo de contrato

Um contrato começa em `15/03/2022` e termina em `20/09/2024`. Calcule e imprima o `Period` entre as datas, mostrando anos, meses e dias separados.

---

## 🟡 Exercício 17 — Semanas viram dias

Crie `Period.ofWeeks(58)`. Imprima:

1. O `Period` (`P406D`).
2. `getDays()`, `getMonths()`, `getYears()`.
3. O mesmo com `Period.ofMonths(15)` e `toTotalMonths()`.
4. Aplique `normalized()` em `Period.ofMonths(15)`.

### Responda

Por que o `Period` não converte 406 dias em 1 ano e 1 mês? (Dica: um mês não tem tamanho fixo.)

---

## 🔴 Exercício 18 — Calculadora de aposentadoria

Dado `nascimento` e uma idade de aposentadoria (ex.: 65 anos):

1. Calcule a **data da aposentadoria** (`plusYears`).
2. Calcule o `Period` que **falta** desde hoje até lá (se já passou, mostre o período decorrido com mensagem diferente).
3. Mostre "Faltam X anos, Y meses e Z dias".
4. Use `isNegative()` para decidir qual mensagem exibir.

Teste com 3 pessoas: uma que ainda vai se aposentar, uma que acabou de se aposentar e uma que se aposenta hoje.

---

# Aula 125 — ChronoUnit

## 🟢 Exercício 19 — Quanto tempo falta

Calcule os dias que faltam desde hoje até o dia 31/12 do ano atual usando `ChronoUnit.DAYS.between`. Faça o mesmo em semanas e em meses.

---

## 🟡 Exercício 20 — Seu tempo de vida

Peça para o programa mostrar, para uma data de nascimento:

```text
Anos vividos:
Meses vividos:
Semanas vividas:
Dias vividos:
Horas vividas:
```

Use `LocalDateTime` e `ChronoUnit`. Explique em comentário por que usar `LocalDate` quebraria o cálculo das horas.

---

## 🔴 Exercício 21 — `Period` x `ChronoUnit`

Para o par de datas `01/01/2020` e `15/04/2023`:

1. Calcule o `Period` e imprima as partes.
2. Calcule `ChronoUnit.MONTHS`, `DAYS`, `WEEKS`, `YEARS`.
3. Reconstrua o total de meses usando o `Period` (`toTotalMonths()`), e compare com o `ChronoUnit.MONTHS`. Dá igual? Por quê?
4. Escreva em comentário uma regra prática: **quando usar cada um?**

Repita com 3 pares diferentes (inclua um período que atravesse um ano bissexto).

---

# Aula 126 — TemporalAdjusters

## 🟢 Exercício 22 — Os cinco ajustes

Partindo de `LocalDate.of(2024, 2, 14)`, imprima:

```text
Primeiro dia do mês
Último dia do mês
Primeiro dia do próximo mês
Primeiro dia do próximo ano
Último dia do ano
```

Use `TemporalAdjusters`.

---

## 🟡 Exercício 23 — Próxima sexta-feira 13

Crie `static LocalDate proximaSexta13(LocalDate inicio)` que avance de mês em mês (usando `firstDayOfNextMonth` e `withDayOfMonth(13)`) até encontrar uma sexta-feira 13.

Imprima as **próximas 5** sextas-feira 13 a partir de hoje.

---

## 🔴 Exercício 24 — Agenda de reuniões mensais

Uma empresa faz reunião na **primeira segunda-feira** de cada mês e fecha o balanço na **última sexta-feira** de cada mês.

Escreva um programa que, para os 12 meses do ano de 2025:

1. Imprima a data da reunião (`firstInMonth(DayOfWeek.MONDAY)`).
2. Imprima a data do fechamento (`lastInMonth(DayOfWeek.FRIDAY)`).
3. Imprima quantos dias existem entre elas.

### Desafio extra

Se a reunião cair em um feriado fixo (1º de janeiro, 1º de maio), mova para a terça-feira seguinte.

---

# Aula 127 — Criando um `TemporalAdjuster`

## 🟢 Exercício 25 — Próximo dia útil clássico

Crie um `TemporalAdjuster` chamado `ProximoDiaUtilClassico` onde os dias úteis são **segunda a sexta**. Sábado e domingo pulam para a segunda.

Teste com uma sexta, um sábado, um domingo e uma quarta.

---

## 🟡 Exercício 26 — Mais um ajustador

Crie um `TemporalAdjuster` chamado `UltimoDiaUtilDoMes` que devolva o último dia útil (segunda a sexta) do mês da data recebida.

Teste com meses cujo último dia cai em sábado, domingo e dia útil.

### Dica

Você pode usar `TemporalAdjusters.lastDayOfMonth()` dentro do seu ajustador, e depois recuar o necessário.

---

## 🔴 Exercício 27 — Prazo com feriados

Crie um `TemporalAdjuster` configurável: `SomarDiasUteis(int quantidade, Set<LocalDate> feriados)` que soma **N dias úteis** a partir da data recebida, pulando fins de semana e feriados.

Teste com:

1. 5 dias úteis a partir de uma segunda-feira sem feriados.
2. 5 dias úteis atravessando um fim de semana.
3. 3 dias úteis com um feriado no meio.
4. 0 dias úteis (deve devolver a mesma data).

### Responda

Por que implementar `TemporalAdjuster` é melhor do que criar um método estático solto? (Pense em polimorfismo e na leitura de `data.with(new SomarDiasUteis(5, feriados))`).

---

# Aula 128 — `ZonedDateTime`, `ZoneId`, `OffsetDateTime`

## 🟢 Exercício 28 — Mundo afora

Imprima a hora atual de `ZonedDateTime.now(ZoneId.of(...))` para:

```text
America/Sao_Paulo
America/Manaus
Europe/Lisbon
Asia/Tokyo
America/Los_Angeles
```

Use um `array` de `String` e um `for`.

---

## 🟡 Exercício 29 — Reunião global

Uma reunião acontece às `14:00` em São Paulo, em `10/05/2024`.

Mostre o horário local dessa reunião em Nova York, Lisboa, Tóquio e Sydney (use `withZoneSameInstant`).

### Responda

Qual a diferença entre `withZoneSameInstant` e `withZoneSameLocal`? Teste os dois e explique o que cada um imprime.

---

## 🔴 Exercício 30 — Voo internacional

Um voo decola de São Paulo às `23:10` do dia 20/12/2024 e dura 11h40. Pousa em Lisboa.

1. Calcule o horário de chegada em **horário de Lisboa**.
2. Calcule a duração real usando `Duration.between` com dois `ZonedDateTime`.
3. Repita com um voo para Tóquio que dure 24h30.
4. Teste um voo que atravessa a mudança de **horário de verão** (use uma data de março ou outubro na Europa) e compare com o cálculo ingênuo "hora local + duração".

### Responda

Por que `LocalDateTime` seria insuficiente aqui?

---

# Aula 129 — `DateTimeFormatter`

## 🟢 Exercício 31 — Três países, três formatos

Formate `LocalDate.of(2024, 3, 9)` no padrão do Brasil (`dd/MM/yyyy`), dos EUA (`MM/dd/yyyy`) e do Japão (`yyyy/MM/dd`). Depois, volte cada String para `LocalDate` usando `parse` e confirme com `equals`.

---

## 🟡 Exercício 32 — Por extenso em três idiomas

Mostre a data de hoje no formato:

```text
sexta-feira, 9 de março de 2024
Friday, March 9, 2024
Freitag, 9. März 2024
```

Use `DateTimeFormatter.ofPattern(..., Locale)`. Cada um com o seu `Locale`.

---

## 🔴 Exercício 33 — Conversor de logs

Você recebe linhas de log com texto:

```text
"2024-03-09T14:30:00 | login | ana"
"09/03/2024 15:45 | compra | bruno"
"March 9, 2024 16:10 | logout | carla"
```

Escreva um programa que:

1. Separe as 3 partes (`split`).
2. Converta a primeira parte para `LocalDateTime` (cada linha tem **um formatter diferente**).
3. Imprima todas as linhas no padrão único `dd/MM/yyyy HH:mm`, ordenadas por data (sem `Collections.sort` — use um laço simples).
4. Se alguma linha não puder ser lida, imprima `Linha inválida` e continue (trate `DateTimeParseException`).

---

# 🏆 Desafio Integrador do Bloco 13 — Sistema de Reservas de Hotel Internacional

Você vai criar o núcleo de um sistema de reservas.

## Classes

```text
Hospede  → nome, ZoneId fusoHorario
Reserva  → hospede, LocalDate checkIn, LocalDate checkOut, double valorDiaria
```

## Regras

1. O **check-in** é às `14:00` e o **check-out** às `12:00`, no fuso do hotel (`America/Sao_Paulo`).
2. A reserva só é válida se `checkOut` for depois de `checkIn` e se `checkIn` não estiver no passado.
3. **Quantidade de diárias** = `ChronoUnit.DAYS.between(checkIn, checkOut)`.
4. Valor total = diárias × valor da diária. Aplique **10% de desconto** se tiver 7 diárias ou mais.
5. Cancelamento grátis até **48 horas antes** do check-in (use `Duration` e `ZonedDateTime`).
6. O hóspede vê as datas **no próprio fuso** (ex.: hóspede em Tóquio vê o check-in convertido).
7. Gere o **comprovante** com `DateTimeFormatter`, com data por extenso e hora, em português.
8. Se o check-out cair em **sábado ou domingo**, ofereça uma sugestão: o próximo dia útil (crie um `TemporalAdjuster`).
9. Imprima também o `Period` entre o check-in e o check-out (ex.: `P0Y0M7D`) e o primeiro e último dias do mês do check-in.

## Cenários de teste

```text
Reserva de 3 diárias começando amanhã
Reserva de 10 diárias
Reserva com check-out em sábado
Reserva inválida (check-out antes do check-in)
Hóspede em Tóquio e hóspede em Nova York
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei criar `LocalDate`, `LocalTime`, `LocalDateTime`.
- [ ] Sei que o mês começa em 1 e usar o enum `Month`.
- [ ] Entendo a imutabilidade e sempre guardo o retorno.
- [ ] Sei a diferença entre `plusDays` e `withDayOfMonth`.
- [ ] Entendo o que é `Instant` e o `Z`.
- [ ] Sei usar `Duration` e ler `PT...`.
- [ ] Sei usar `Period` e entendo suas limitações.
- [ ] Sei usar `ChronoUnit.X.between`.
- [ ] Sei usar `TemporalAdjusters`.
- [ ] Sei implementar meu próprio `TemporalAdjuster`.
- [ ] Sei usar `ZoneId`, `ZonedDateTime` e `OffsetDateTime`.
- [ ] Sei a diferença entre `withZoneSameInstant` e `withZoneSameLocal`.
- [ ] Sei usar `DateTimeFormatter` com `ofPattern` e `Locale`.
- [ ] Sei fazer `format` e `parse`.

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
