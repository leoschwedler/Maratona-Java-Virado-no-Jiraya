# Bloco 13 — A API Moderna de Datas: `java.time`

## Aulas 119 a 129

No bloco anterior você viu `Date`, `Calendar` e `SimpleDateFormat`. Agora você conhece a **API de datas de verdade**, introduzida no **Java 8**, no pacote:

```java
java.time
```

Ela foi inspirada na biblioteca **Joda-Time**, muito usada antes do Java 8 (o instrutor comenta que os criadores do Java se baseiam no Joda), e corrige os problemas das classes antigas.

## Por que a API nova é melhor?

| Problema antigo | Solução em `java.time` |
|---|---|
| `Date` é mutável | Todas as classes são **imutáveis** |
| Mês começa em 0 | Mês começa em **1** (e existe o enum `Month`) |
| `Date` mistura data e hora | Classes separadas: só data, só hora, data+hora, instante |
| `SimpleDateFormat` não é thread-safe | `DateTimeFormatter` é thread-safe |
| Fuso horário confuso | `ZoneId`, `ZonedDateTime`, `OffsetDateTime` |
| Cálculos manuais com milissegundos | `Duration`, `Period`, `ChronoUnit` |

As aulas deste bloco são:

```text
119 — LocalDate
120 — LocalTime
121 — LocalDateTime
122 — Instant
123 — Duration
124 — Period
125 — ChronoUnit
126 — TemporalAdjusters
127 — TemporalAdjuster (criando o seu)
128 — ZonedDateTime, ZoneId, OffsetDateTime
129 — DateTimeFormatter
```

## Mapa das classes principais

```text
                   Quero representar...
 ┌───────────────────────────────────────────────────────────────┐
 │ só a DATA (sem hora)             → LocalDate                  │
 │ só a HORA (sem data)             → LocalTime                  │
 │ DATA + HORA (sem fuso)           → LocalDateTime              │
 │ um INSTANTE na linha do tempo    → Instant                    │
 │ DATA + HORA + FUSO               → ZonedDateTime              │
 │ DATA + HORA + deslocamento UTC   → OffsetDateTime             │
 ├───────────────────────────────────────────────────────────────┤
 │ quantidade de TEMPO (h, min, s)  → Duration                   │
 │ quantidade de DATA (a, m, d)     → Period                     │
 │ diferença em UMA unidade         → ChronoUnit                 │
 └───────────────────────────────────────────────────────────────┘
```

---

# Aula 119 — LocalDate

## 1. O que é

`LocalDate` representa **apenas uma data**: ano, mês e dia. **Sem hora e sem fuso.**

Use quando a hora não importa: data de nascimento, data de vencimento, feriado.

```java
import java.time.LocalDate;
```

---

## 2. Como criar (não existe `new`)

O construtor é **privado**. Você cria usando métodos estáticos de fábrica:

```java
LocalDate hoje = LocalDate.now();                       // data de hoje
LocalDate natal = LocalDate.of(2022, 12, 25);           // ano, mês, dia
LocalDate janeiro = LocalDate.of(2022, Month.JANUARY, 27); // usando o enum Month
LocalDate texto = LocalDate.parse("2022-01-27");        // a partir de String (ISO)
```

### Os meses agora começam em 1

```java
LocalDate.of(2022, 1, 27);         // 27 de janeiro (1 = janeiro)
LocalDate.of(2022, Month.JANUARY, 27); // recomendado: legível
```

> Recomendação do instrutor: prefira o enum `Month` (`Month.JANUARY`) em vez do número, para o código ficar autoexplicativo.

---

## 3. Métodos utilitários

```java
LocalDate data = LocalDate.of(2022, Month.JANUARY, 27);

data.getYear();           // 2022
data.getMonth();          // JANUARY            (enum Month)
data.getMonthValue();     // 1                  (int)
data.getDayOfMonth();     // 27
data.getDayOfWeek();      // THURSDAY           (enum DayOfWeek)
data.getDayOfYear();      // 27
data.lengthOfMonth();     // 31 (quantos dias tem o mês)
data.lengthOfYear();      // 365
data.isLeapYear();        // false (2022 não é bissexto)
```

---

## 4. Lendo qualquer campo: `get(TemporalField)`

Para campos que não têm método dedicado, use `ChronoField`:

```java
import java.time.temporal.ChronoField;

data.get(ChronoField.YEAR);           // 2022
data.get(ChronoField.DAY_OF_MONTH);   // 27
data.get(ChronoField.MONTH_OF_YEAR);  // 1
```

`ChronoField` implementa a interface `TemporalField`.

---

## 5. A grande novidade: **IMUTABILIDADE**

Esse é o ponto mais importante da aula.

```java
LocalDate hoje = LocalDate.now();
hoje.plusDays(10);                       // IGNORADO! Nada muda em "hoje"
System.out.println(hoje);                // continua a data de hoje
```

Toda operação **devolve um novo objeto**. Você precisa guardar o resultado:

```java
LocalDate daquiA10Dias = hoje.plusDays(10);
// ou
hoje = hoje.plusDays(10);
```

A IDE avisa com "o resultado da chamada é ignorado" exatamente por causa disso.

### Por que imutabilidade é boa?

- Segurança em **multithread**: ninguém altera o objeto "por baixo dos panos".
- Sem efeitos colaterais: passar uma data para um método nunca a modifica.
- Menos bugs de "alguém mexeu na minha data".

---

## 6. Operações `plus` / `minus` / `with`

```java
LocalDate d = LocalDate.of(2022, 1, 27);

d.plusDays(10);      // 2022-02-06
d.plusWeeks(2);
d.plusMonths(1);
d.plusYears(1);

d.minusDays(30);
d.minusMonths(2);

d.withDayOfMonth(1); // troca só o dia: 2022-01-01
d.withMonth(12);     // troca só o mês: 2022-12-27
d.withYear(2030);
```

Diferença importante:

| Método | O que faz |
|---|---|
| `plusDays(n)` | **soma** n dias (pode virar mês/ano) |
| `withDayOfMonth(n)` | **substitui** o dia por n (não vira nada) |

---

## 7. Limites

`LocalDate.MIN` e `LocalDate.MAX` mostram o intervalo suportado — muito maior que o do `Date` (que só pegava a partir de 1970). Na prática não é uma preocupação.

## 8. Saída no formato do banco

`System.out.println(LocalDate.now())` imprime no formato **ISO-8601**: `2022-01-27`. É exatamente o formato que bancos de dados usam, o que facilita muito a integração.

## O que você precisa dominar (Aula 119)

- `LocalDate` = só data.
- Criação com `now()`, `of()`, `parse()`.
- Mês começa em 1; use o enum `Month`.
- `getYear`, `getMonth`, `getDayOfWeek`, `lengthOfMonth`, `isLeapYear`.
- **Imutabilidade**: sempre guardar o retorno.
- Diferença entre `plusX` e `withX`.

---

# Aula 120 — LocalTime

## 1. O que é

`LocalTime` representa **apenas a hora** (hora, minuto, segundo, nanossegundo), sem data e sem fuso.

```java
LocalTime agora = LocalTime.now();
LocalTime reuniao = LocalTime.of(14, 30);          // 14:30
LocalTime exata = LocalTime.of(11, 30, 45);        // 11:30:45
LocalTime comNano = LocalTime.of(11, 30, 45, 1000);
LocalTime texto = LocalTime.parse("09:45:00");
```

---

## 2. Valores inválidos lançam exceção

A hora vai de **0 a 23**:

```java
LocalTime.of(24, 32, 12);
// DateTimeException: Invalid value for HourOfDay (valid values 0 - 23): 24
```

Isso é diferente de `Calendar`, que "aceita" e vira o dia silenciosamente. Aqui a API é **rigorosa**, o que evita bugs.

---

## 3. Métodos úteis

```java
LocalTime t = LocalTime.of(23, 15, 40);

t.getHour();      // 23
t.getMinute();    // 15
t.getSecond();    // 40
t.getNano();      // 0
t.get(ChronoField.HOUR_OF_DAY);  // 23
t.get(ChronoField.AMPM_OF_DAY);  // 1  → PM (0 = AM, 1 = PM)

t.plusHours(2);
t.minusMinutes(10);
t.withHour(8);
```

> Na aula o instrutor testa `ChronoField.AMPM_OF_DAY` e aprende que ele retorna `0` ou `1`, não a hora.

Por ser imutável, mesma regra: guarde o resultado.

---

## 4. Constantes: início e fim do dia

```java
LocalTime.MIN;       // 00:00
LocalTime.MAX;       // 23:59:59.999999999
LocalTime.MIDNIGHT;  // 00:00
LocalTime.NOON;      // 12:00
```

Isso é muito usado em **relatórios**: para pegar tudo que aconteceu em um dia, você quer de `00:00` até `23:59:59.999999999`.

```java
LocalDateTime inicioDoDia = LocalDate.of(2022, 2, 15).atTime(LocalTime.MIN);
LocalDateTime fimDoDia    = LocalDate.of(2022, 2, 15).atTime(LocalTime.MAX);
```

## O que você precisa dominar (Aula 120)

- `LocalTime` = só hora.
- `of`, `now`, `parse`.
- Hora vai de 0 a 23; fora disso, exceção.
- `MIN`, `MAX`, `MIDNIGHT`, `NOON`.
- Uso em relatórios (início/fim do dia).

---

# Aula 121 — LocalDateTime

## 1. O que é

`LocalDateTime` = `LocalDate` + `LocalTime`. Data **e** hora, **sem** fuso.

```java
LocalDateTime agora = LocalDateTime.now();
LocalDateTime evento = LocalDateTime.of(2022, Month.AUGUST, 6, 9, 45);
LocalDateTime evento2 = LocalDateTime.of(2022, 8, 6, 9, 45, 30);
```

A saída usa o `T` do ISO-8601 para separar data e hora:

```text
2022-08-06T09:45
```

---

## 2. Montando de pedaços

```java
LocalDate data = LocalDate.of(2022, Month.AUGUST, 6);
LocalTime hora = LocalTime.of(9, 45);

LocalDateTime dt1 = LocalDateTime.of(data, hora);   // junta data + hora
LocalDateTime dt2 = data.atTime(hora);              // data + hora
LocalDateTime dt3 = data.atTime(9, 45);             // data + números
LocalDateTime dt4 = hora.atDate(data);              // hora + data
LocalDateTime dt5 = data.atStartOfDay();            // data às 00:00
```

Todos produzem a mesma coisa. Você escolhe o mais legível.

---

## 3. Separando em pedaços

```java
dt1.toLocalDate();   // LocalDate
dt1.toLocalTime();   // LocalTime
dt1.getYear();       // 2022
dt1.getHour();       // 9
```

---

## 4. `parse` de String

Diferente de `SimpleDateFormat.parse` (checked `ParseException`), o `parse` de `java.time` lança `DateTimeParseException` — uma **unchecked** exception (`RuntimeException`):

```java
LocalDate d = LocalDate.parse("2022-08-06");
LocalTime t = LocalTime.parse("09:45:00");
LocalDateTime dt = LocalDateTime.parse("2022-08-06T09:45:00");

LocalTime.parse("24:00");   // DateTimeParseException (unchecked)
```

Sem `try/catch` obrigatório, mas é bom tratar quando a entrada vem do usuário.

---

## 5. Quando usar cada uma?

| Situação | Classe |
|---|---|
| Aniversário, feriado | `LocalDate` |
| Horário de abertura da loja | `LocalTime` |
| Data/hora de uma reunião local | `LocalDateTime` |
| Data/hora salva no banco para qualquer fuso | `Instant` ou `ZonedDateTime` |

## O que você precisa dominar (Aula 121)

- `LocalDateTime` = data + hora, sem fuso.
- Formas de criar: `now`, `of`, `parse`, `atTime`, `atDate`, `atStartOfDay`.
- `toLocalDate()` e `toLocalTime()`.
- `parse` lança exceção **unchecked**.

---

# Aula 122 — Instant

## 1. O que é

`Instant` é um **ponto exato na linha do tempo**, contado em segundos e nanossegundos desde o Epoch (01/01/1970 UTC). É o "irmão moderno" do `Date`, só que com **precisão de nanossegundos**.

```java
Instant agora = Instant.now();
System.out.println(agora);   // 2022-02-19T18:03:12.123456Z
```

Note o **`Z`** no final.

---

## 2. O que é o `Z`?

`Z` significa **Zulu time**, que é o mesmo que **UTC** (fuso zero, o horário "neutro" do mundo).

> Qualquer pessoa em qualquer lugar do mundo, ao consultar o horário UTC, vê o mesmo valor.

### Por que isso é importante em aplicações?

Imagine salvar "14:00" no banco. Em qual fuso? Se um usuário em Manaus e outro em São Paulo abrirem a mesma data, a diferença é de 1 hora. Se você não registra o fuso, perde a informação.

**Boa prática:** salvar datas/instantes em **UTC** e converter para o fuso do usuário **só na hora de exibir**.

```text
Banco de dados: 2022-02-19T18:00:00Z  (UTC)
São Paulo (UTC-3): 15:00
Manaus   (UTC-4): 14:00
Tóquio   (UTC+9): 03:00 do dia 20
```

---

## 3. Segundos e nanossegundos

O número total de nanossegundos desde 1970 **não cabe em um `long`**, então o `Instant` guarda em duas partes:

```java
Instant i = Instant.now();
long segundos = i.getEpochSecond();   // segundos desde 1970
int nanos = i.getNano();              // nanos dentro do segundo (0 a 999.999.999)
```

Quando os nanos passam de 999.999.999, o segundo é incrementado.

---

## 4. Criando e manipulando

```java
Instant i = Instant.ofEpochSecond(3);    // 1970-01-01T00:00:03Z
i = i.plusNanos(1_000_000_000L);         // +1 segundo (4s)
i = i.plusSeconds(60);
i = i.minusSeconds(2);
```

Também aceita valores negativos.

> Ao somar 1 bilhão de nanos, o segundo vira (3s → 4s).

---

## 5. Limitação: `Instant` não entende "dia do mês" etc.

`Instant` serve para máquinas. Para humanos (dia, mês, ano) use `LocalDate`, `LocalDateTime` ou `ZonedDateTime`.

```java
instant.getYear();   // NÃO EXISTE
```

Se tentar `instant.plus(1, ChronoUnit.DAYS)` funciona (um dia = 24h); mas `plus(1, ChronoUnit.MONTHS)` gera `UnsupportedTemporalTypeException`.

## O que você precisa dominar (Aula 122)

- `Instant` = ponto na linha do tempo, em UTC.
- Significado do `Z` (Zulu = UTC).
- Salvar em UTC, exibir no fuso do usuário.
- `getEpochSecond()` e `getNano()`.
- `Instant` é imutável.
- Precisão de nanossegundos (ao contrário de `Date`, em milissegundos).

---

# Aula 123 — Duration

## 1. O que é

`Duration` mede uma **quantidade de tempo** baseada em **segundos e nanossegundos**: horas, minutos, segundos...

```java
import java.time.Duration;
```

É a resposta a: "**quanto tempo** passou entre A e B?".

---

## 2. Criando uma `Duration`

### Entre dois momentos

```java
LocalDateTime inicio = LocalDateTime.now();
LocalDateTime fim = inicio.plusYears(2);

Duration d = Duration.between(inicio, fim);
System.out.println(d);   // PT17520H  (17.520 horas)
```

Funciona com `LocalDateTime`, `LocalTime` e `Instant`:

```java
Duration.between(LocalTime.now(), LocalTime.now().plusHours(7));   // PT7H
Duration.between(Instant.now(), Instant.now().plusSeconds(1000));  // PT16M40S
```

### Direto, por unidade

```java
Duration.ofDays(20);       // PT480H  (20 dias viram 480 horas)
Duration.ofHours(5);       // PT5H
Duration.ofMinutes(3);     // PT3M
Duration.ofSeconds(90);    // PT1M30S
Duration.ofMillis(1500);
Duration.of(3, ChronoUnit.MINUTES);
```

---

## 3. Lendo o formato `PT...` (ISO-8601)

```text
PT2H30M15S
│ │ │  │
│ │ │  └─ segundos
│ │ └──── minutos
│ └────── horas
└──────── P = Period (período), T = Time (parte do tempo)
```

- **P** = *Period* (começo de toda duração ISO).
- **T** = *Time*: separa a parte de data da parte de tempo.

Exemplos:

| Texto | Significado |
|---|---|
| `PT7H` | 7 horas |
| `PT16M40S` | 16 minutos e 40 segundos |
| `PT480H` | 480 horas (20 dias) |

`Duration` **nunca usa dias, meses ou anos na impressão** (só H, M, S). 20 dias aparecem como 480H.

---

## 4. Convertendo a duração

```java
Duration d = Duration.ofMinutes(150);

d.toHours();       // 2
d.toMinutes();     // 150
d.getSeconds();    // 9000
d.toMillis();      // 9_000_000
d.toDays();        // 0
d.toMinutesPart(); // 30  (Java 9+)
```

---

## 5. ⚠️ Armadilha: `Duration` com `LocalDate`

`LocalDate` **não tem hora**, então não tem segundos:

```java
LocalDate hoje = LocalDate.now();
LocalDate futuro = hoje.plusDays(2);

Duration.between(hoje, futuro);   // UnsupportedTemporalTypeException: Unsupported unit: Seconds
```

Para datas sem hora, use `Period` (próxima aula) ou `ChronoUnit`.

---

## 6. Armadilha de `Duration.of(valor, unidade)`

`Duration.of` só aceita unidades **exatas** (nanos, micros, millis, seconds, minutes, hours, half-days, days). Meses e anos não têm tamanho fixo, então lançam exceção:

```java
Duration.of(2, ChronoUnit.MONTHS);   // UnsupportedTemporalTypeException
```

Para essas, use `Period`.

## O que você precisa dominar (Aula 123)

- `Duration` = quantidade de tempo (h/min/s).
- `between`, `ofDays`, `ofHours`, `ofMinutes`, `ofSeconds`.
- Leitura de `PT7H30M`.
- Não funciona com `LocalDate`.
- Não aceita `MONTHS`/`YEARS`.

---

# Aula 124 — Period

## 1. O que é

`Period` mede uma **quantidade de data**: anos, meses e dias. É o "irmão" de `Duration`, mas para o **calendário**.

| Classe | Mede | Funciona com |
|---|---|---|
| `Duration` | horas, minutos, segundos | `LocalDateTime`, `LocalTime`, `Instant` |
| `Period` | anos, meses, dias | `LocalDate`, `LocalDateTime` |

---

## 2. Criando

```java
LocalDate hoje = LocalDate.now();
LocalDate futuro = hoje.plusYears(2).plusDays(7);

Period p = Period.between(hoje, futuro);
System.out.println(p);   // P2Y7D
```

Direto:

```java
Period.ofDays(10);     // P10D
Period.ofWeeks(58);    // P406D   ← vira dias!
Period.ofMonths(3);    // P3M
Period.ofYears(1);     // P1Y
Period.of(1, 3, 10);   // P1Y3M10D
```

Formato de impressão:

```text
P1Y3M10D
│ │ │ └─ 10 dias
│ │ └─── 3 meses
│ └───── 1 ano
└─────── Period
```

---

## 3. ⚠️ Semanas viram dias

```java
Period.ofWeeks(58);   // P406D  (58 × 7 = 406)
```

O `Period` **não guarda semanas**; converte para dias.

---

## 4. ⚠️ O `Period` NÃO normaliza dias em meses

```java
Period p = Period.ofWeeks(58);
p.getDays();      // 406
p.getMonths();    // 0   ← NÃO converte 406 dias em meses!
p.getYears();     // 0
```

Pior ainda, se você usar `toTotalMonths()`:

```java
Period p = Period.between(hoje, hoje.plusMonths(15));  // P1Y3M
p.getMonths();        // 3   (só a parte de meses)
p.toTotalMonths();    // 15  (anos*12 + meses)
```

A lição da aula: o `Period` mostra **cada pedaço separado** (anos, meses, dias), sem conversões entre eles. Se você quer o **total de meses** a partir de uma quantidade de dias, o `Period` não ajuda — é preciso usar `ChronoUnit` (próxima aula).

---

## 5. Métodos úteis

```java
Period p = Period.of(1, 3, 10);

p.getYears();       // 1
p.getMonths();      // 3
p.getDays();        // 10
p.toTotalMonths();  // 15
p.isNegative();     // false
p.plusDays(5);
p.normalized();     // converte meses excedentes em anos (ex.: 14M → 1Y2M)
```

`normalized()` só converte **meses em anos**, nunca dias em meses.

## O que você precisa dominar (Aula 124)

- `Period` = anos/meses/dias.
- `Period.between(LocalDate, LocalDate)`.
- `ofDays`, `ofWeeks`, `ofMonths`, `ofYears`, `of`.
- Semanas viram dias.
- `getMonths()` x `toTotalMonths()`.
- `Period` não converte dias em meses.

---

# Aula 125 — ChronoUnit

## 1. O que é

`ChronoUnit` é um **enum** de unidades de tempo que implementa `TemporalUnit`:

```text
NANOS, MICROS, MILLIS, SECONDS, MINUTES, HOURS, HALF_DAYS,
DAYS, WEEKS, MONTHS, YEARS, DECADES, CENTURIES, MILLENNIA, ERAS...
```

Sua grande utilidade: calcular a **diferença total em UMA unidade**.

---

## 2. `ChronoUnit.X.between(a, b)`

```java
LocalDate hoje = LocalDate.now();
LocalDate futuro = hoje.plusDays(406);

ChronoUnit.DAYS.between(hoje, futuro);     // 406
ChronoUnit.WEEKS.between(hoje, futuro);    // 58
ChronoUnit.MONTHS.between(hoje, futuro);   // 13
ChronoUnit.YEARS.between(hoje, futuro);    // 1
```

Isso resolve exatamente o problema do `Period`: "58 semanas = quantos meses?" → `ChronoUnit.MONTHS.between(...)`.

### Comparação

```text
Period.between(a, b)             → 1 ano, 1 mês e 6 dias   (partes separadas)
ChronoUnit.MONTHS.between(a, b)  → 13                      (total em meses)
ChronoUnit.DAYS.between(a, b)    → 406                     (total em dias)
```

---

## 3. Com datas e horas

```java
LocalDateTime aniversario = LocalDateTime.of(1988, Month.AUGUST, 31, 0, 0);
LocalDateTime agora = LocalDateTime.now();

ChronoUnit.DAYS.between(aniversario, agora);     // dias vividos
ChronoUnit.WEEKS.between(aniversario, agora);    // semanas vividas
ChronoUnit.MONTHS.between(aniversario, agora);
ChronoUnit.YEARS.between(aniversario, agora);    // idade em anos
```

Isso é o jeito mais simples de calcular **idade**.

---

## 4. Atenção ao tipo

O primeiro argumento define o "tipo" de dados. Se usar `LocalDate` com `ChronoUnit.HOURS`:

```java
ChronoUnit.HOURS.between(LocalDate.now(), LocalDate.now().plusDays(1));
// UnsupportedTemporalTypeException: Unsupported unit: Hours
```

`LocalDate` não tem hora. Use `LocalDateTime`.

## 5. Também funciona para somar

```java
LocalDate d = LocalDate.now().plus(3, ChronoUnit.MONTHS);
LocalTime t = LocalTime.now().plus(30, ChronoUnit.MINUTES);
```

## O que você precisa dominar (Aula 125)

- `ChronoUnit` é um enum de unidades.
- `ChronoUnit.X.between(a, b)` retorna `long` (total).
- Diferença entre `Period` (partes) e `ChronoUnit` (total).
- Calcular idade e diferença de dias.

---

# Aula 126 — TemporalAdjusters

## 1. Contexto

- `TemporalAdjuster` → **interface** (um único método: `adjustInto`).
- `TemporalAdjusters` (com **s**) → **classe utilitária** com vários ajustes prontos (todos `static`).

Eles servem para fazer **ajustes inteligentes** na data, do tipo "próxima segunda-feira" ou "último dia do mês".

```java
import java.time.temporal.TemporalAdjusters;
```

---

## 2. `with` x `plus`

Revisão:

```java
LocalDate hoje = LocalDate.of(2021, 2, 18);   // quinta-feira

hoje.plusDays(20);                // SOMA 20 dias → 10/03/2021 (virou o mês)
hoje.withDayOfMonth(20);          // TROCA o dia → 20/02/2021 (mesmo mês)
hoje.with(ChronoField.DAY_OF_MONTH, 20);   // mesma coisa
```

`with` **substitui** o valor do campo; não "vira" para o campo maior.

---

## 3. Usando `TemporalAdjusters` com `with(...)`

```java
LocalDate hoje = LocalDate.of(2021, 2, 18);  // quinta-feira

hoje.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));          // 2021-02-25 (a próxima, NUNCA hoje)
hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));    // 2021-02-18 (hoje já é quinta)
hoje.with(TemporalAdjusters.previous(DayOfWeek.THURSDAY));      // 2021-02-11
hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY));// 2021-02-18

hoje.with(TemporalAdjusters.firstDayOfMonth());       // 2021-02-01
hoje.with(TemporalAdjusters.lastDayOfMonth());        // 2021-02-28 (sem você contar 28/29/30/31!)
hoje.with(TemporalAdjusters.firstDayOfNextMonth());   // 2021-03-01
hoje.with(TemporalAdjusters.firstDayOfNextYear());    // 2022-01-01
hoje.with(TemporalAdjusters.firstDayOfYear());
hoje.with(TemporalAdjusters.lastDayOfYear());
```

### Tabela de referência

| Método | Resultado |
|---|---|
| `next(dia)` | próximo dia da semana, **excluindo** hoje |
| `nextOrSame(dia)` | próximo, **incluindo** hoje |
| `previous(dia)` | anterior, excluindo hoje |
| `previousOrSame(dia)` | anterior, incluindo hoje |
| `firstDayOfMonth()` | dia 1 do mês |
| `lastDayOfMonth()` | último dia do mês (cuida de bissexto) |
| `firstDayOfNextMonth()` | dia 1 do mês seguinte |
| `firstDayOfYear()` / `lastDayOfYear()` | início/fim do ano |
| `firstDayOfNextYear()` | 01/01 do próximo ano |
| `firstInMonth(dia)` | 1ª ocorrência do dia da semana no mês |
| `lastInMonth(dia)` | última ocorrência do dia da semana no mês |
| `dayOfWeekInMonth(n, dia)` | n-ésima ocorrência |

Exemplo prático (relatórios mensais):

```java
LocalDate inicio = data.with(TemporalAdjusters.firstDayOfMonth());
LocalDate fim    = data.with(TemporalAdjusters.lastDayOfMonth());
```

Sem precisar saber se fevereiro tem 28 ou 29 dias.

---

## 4. Gancho para a próxima aula

E se quisermos **"próximo dia útil"**? Não existe pronto. A solução é criar o **nosso próprio `TemporalAdjuster`** — próxima aula.

## O que você precisa dominar (Aula 126)

- `TemporalAdjuster` (interface) x `TemporalAdjusters` (classe utilitária).
- `with(TemporalAdjusters.xxx())`.
- Diferença entre `next` e `nextOrSame`.
- `firstDayOfMonth`, `lastDayOfMonth`, `firstDayOfNextMonth`, `firstDayOfNextYear`.
- Diferença entre `plus` (soma) e `with` (substitui).

---

# Aula 127 — Criando um `TemporalAdjuster` (próximo dia útil)

## 1. Regra de negócio

Dias úteis: segunda a quinta (neste exercício **sexta, sábado e domingo não são úteis**).

Dada uma data, devolver o **próximo dia útil**:

| Dia da data | Próximo dia útil | Dias a somar |
|---|---|---|
| segunda | terça | +1 |
| terça | quarta | +1 |
| quarta | quinta | +1 |
| quinta | **segunda** | +4 |
| sexta | segunda | +3 |
| sábado | segunda | +2 |
| domingo | segunda | +1 |

---

## 2. A interface

`TemporalAdjuster` é uma **interface funcional** com um único método:

```java
Temporal adjustInto(Temporal temporal);
```

Recebe uma data (`Temporal`) e devolve a data ajustada.

---

## 3. Implementação (como na aula)

```java
import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

class ObterProximoDiaUtil implements TemporalAdjuster {

    @Override
    public Temporal adjustInto(Temporal temporal) {
        // 1) descobrir o dia da semana
        int valor = temporal.get(ChronoField.DAY_OF_WEEK);
        DayOfWeek dia = DayOfWeek.of(valor);

        // 2) decidir quantos dias somar
        int diasParaSomar = 1;
        switch (dia) {
            case THURSDAY:
                diasParaSomar = 4;
                break;
            case FRIDAY:
                diasParaSomar = 3;
                break;
            case SATURDAY:
                diasParaSomar = 2;
                break;
            default:
                diasParaSomar = 1;
        }

        // 3) somar e devolver
        return temporal.plus(diasParaSomar, ChronoUnit.DAYS);
    }
}
```

> Dica de arquitetura (dita na aula): em um mesmo arquivo `.java` você pode ter várias classes; só **uma** pode ser `public` e ela deve ter o nome do arquivo.

---

## 4. Usando

```java
LocalDate hoje = LocalDate.of(2021, 2, 18);   // quinta-feira
LocalDate proximo = hoje.with(new ObterProximoDiaUtil());
System.out.println(proximo);                  // 2021-02-22 (segunda)
```

Testes que o instrutor faz:

```text
dia 15 (segunda) → terça
dia 19 (sexta)   → segunda
dia 20 (sábado)  → segunda
dia 21 (domingo) → segunda
```

---

## 5. Por que isso funciona? (Polimorfismo)

`with(TemporalAdjuster adjuster)` recebe a **interface**. Como a sua classe a implementa, qualquer instância dela é aceita. Dentro do `with`, o Java apenas chama:

```java
adjuster.adjustInto(this);
```

Isso é **polimorfismo + interface** (assunto dos blocos 8–9) funcionando num caso real.

### Versão com lambda (visão do futuro)

Como `TemporalAdjuster` é funcional, no futuro você poderá escrever:

```java
TemporalAdjuster proximoDiaUtil = temporal -> {
    DayOfWeek dia = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
    int soma = switch (dia) {
        case THURSDAY -> 4;
        case FRIDAY -> 3;
        case SATURDAY -> 2;
        default -> 1;
    };
    return temporal.plus(soma, ChronoUnit.DAYS);
};
```

## O que você precisa dominar (Aula 127)

- A interface `TemporalAdjuster` e seu método `adjustInto`.
- Como a regra de negócio foi isolada numa classe.
- Uso com `with(...)`.
- Polimorfismo na prática.

---

# Aula 128 — `ZonedDateTime`, `ZoneId`, `OffsetDateTime`

## 1. Por que fusos importam?

Um `LocalDateTime` **não sabe em que fuso está**. `18:03` em Amsterdã e `18:03` em Tóquio são momentos totalmente diferentes na linha do tempo. Para isso existem as classes de fuso.

---

## 2. `ZoneId` — o identificador do fuso

```java
import java.time.ZoneId;

System.out.println(ZoneId.getAvailableZoneIds());   // lista (Set<String>) de todos os fusos
System.out.println(ZoneId.SHORT_IDS);               // Map<String,String> com siglas
System.out.println(ZoneId.systemDefault());         // fuso do seu computador

ZoneId tokyo = ZoneId.of("Asia/Tokyo");
ZoneId saoPaulo = ZoneId.of("America/Sao_Paulo");
```

### Observações

- O formato do ID é `Continente/Cidade`: `America/Sao_Paulo`, `Europe/Berlin`, `Asia/Tokyo`.
- Não existe "Brasília": o ID para o horário de Brasília é `America/Sao_Paulo`.
- ID inválido → `ZoneRulesException`.
- Os nomes que o Windows mostra podem ser diferentes dos IDs do Java.

---

## 3. `ZonedDateTime` — data + hora + fuso

`LocalDateTime` sozinho não tem fuso. Você "anexa" um fuso com `atZone`:

```java
LocalDateTime ldt = LocalDateTime.now();
ZonedDateTime zdt = ldt.atZone(ZoneId.of("Asia/Tokyo"));
System.out.println(zdt);   // 2021-02-19T18:03+09:00[Asia/Tokyo]
```

Detalhe fundamental: `atZone` **não converte o horário**; apenas diz "essa hora (18:03) é de Tóquio". O relógio continua 18:03, só ganha `+09:00`.

### Convertendo de verdade a partir de um `Instant`

```java
Instant instante = Instant.now();
ZonedDateTime tokyo = instante.atZone(ZoneId.of("Asia/Tokyo"));
```

Agora sim o instante (UTC) é **traduzido** para o horário de Tóquio (UTC+9). Se em UTC são 17:03, em Tóquio são 02:03 do dia seguinte.

### Convertendo entre fusos

```java
ZonedDateTime saoPaulo = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
ZonedDateTime emTokyo = saoPaulo.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));  // mesmo instante, relógio diferente
ZonedDateTime mesmaHora = saoPaulo.withZoneSameLocal(ZoneId.of("Asia/Tokyo"));  // mesma hora local, instante diferente
```

| Método | O que mantém |
|---|---|
| `withZoneSameInstant` | o **instante** (a hora do relógio muda) |
| `withZoneSameLocal` | a **hora do relógio** (o instante muda) |

---

## 4. `ZoneOffset` — só o deslocamento em relação ao UTC

Às vezes você não sabe a "cidade", só o deslocamento (ex.: -04:00 para Manaus).

```java
ZoneOffset manaus = ZoneOffset.of("-04:00");
System.out.println(ZoneOffset.MIN);   // -18:00
System.out.println(ZoneOffset.MAX);   // +18:00
```

Atenção ao formato: `"-04:00"`. Se escrever `"-400"` dá exceção em tempo de execução.

Diferença importante:

| | `ZoneId` | `ZoneOffset` |
|---|---|---|
| Exemplo | `America/Sao_Paulo` | `-03:00` |
| Conhece horário de verão? | **Sim** (tem regras) | Não (valor fixo) |

---

## 5. `OffsetDateTime` — data + hora + deslocamento

```java
LocalDateTime ldt = LocalDateTime.now();
ZoneOffset manaus = ZoneOffset.of("-04:00");

OffsetDateTime o1 = ldt.atOffset(manaus);
OffsetDateTime o2 = OffsetDateTime.of(ldt, manaus);
OffsetDateTime o3 = Instant.now().atOffset(manaus);   // este CONVERTE o horário
```

Quando usar `OffsetDateTime`? Em transporte de dados (APIs, JSON, banco) em que só interessa a diferença para o UTC.

---

## 6. Calendários não gregorianos

O `java.time` também suporta outros calendários (pacote `java.time.chrono`):

```java
JapaneseDate japones = JapaneseDate.from(LocalDate.now());
System.out.println(japones.getEra());    // Reiwa
```

## 7. Comparação final

| Classe | Data | Hora | Fuso/offset |
|---|---|---|---|
| `LocalDate` | ✅ | ❌ | ❌ |
| `LocalTime` | ❌ | ✅ | ❌ |
| `LocalDateTime` | ✅ | ✅ | ❌ |
| `Instant` | (UTC) | (UTC) | UTC |
| `OffsetDateTime` | ✅ | ✅ | offset |
| `ZonedDateTime` | ✅ | ✅ | ZoneId (com regras de horário de verão) |

## O que você precisa dominar (Aula 128)

- `ZoneId.of("Continente/Cidade")` e `systemDefault()`.
- `atZone` (anexa) x `Instant.atZone` (converte).
- `withZoneSameInstant` x `withZoneSameLocal`.
- `ZoneOffset.of("-04:00")`.
- Diferença `ZonedDateTime` x `OffsetDateTime`.

---

# Aula 129 — `DateTimeFormatter`

## 1. Formatar x Parsear

Regra de ouro (do instrutor):

```text
format → objeto  →  String
parse  → String  →  objeto
```

---

## 2. Formatadores prontos (constantes ISO)

```java
import java.time.format.DateTimeFormatter;

LocalDate hoje = LocalDate.now();

hoje.format(DateTimeFormatter.BASIC_ISO_DATE);   // 20210219
hoje.format(DateTimeFormatter.ISO_LOCAL_DATE);   // 2021-02-19
hoje.format(DateTimeFormatter.ISO_DATE);         // 2021-02-19
```

Para `LocalDateTime`:

```java
LocalDateTime agora = LocalDateTime.now();
agora.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);  // 2021-02-19T18:03:12.123
agora.format(DateTimeFormatter.ISO_DATE_TIME);
```

Cada constante é feita para certos tipos. Usar `ISO_OFFSET_DATE` em `LocalDate` (que não tem offset) gera:

```text
UnsupportedTemporalTypeException
```

---

## 3. `parse` com o mesmo padrão

```java
String s = hoje.format(DateTimeFormatter.ISO_LOCAL_DATE);   // "2021-02-19"

LocalDate d1 = LocalDate.parse(s);                                    // usa ISO_LOCAL_DATE por padrão
LocalDate d2 = LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);  // explícito
```

Se o formato do texto **não bate** com o formatter: `DateTimeParseException`.

```java
LocalDate.parse("19/02/2021");   // DateTimeParseException — formato ISO esperado
```

---

## 4. Padrões personalizados: `ofPattern`

Brasil: dia/mês/ano. EUA: mês/dia/ano. Japão: ano/mês/dia.

```java
DateTimeFormatter br = DateTimeFormatter.ofPattern("dd/MM/yyyy");
DateTimeFormatter eua = DateTimeFormatter.ofPattern("MM/dd/yyyy");
DateTimeFormatter jp = DateTimeFormatter.ofPattern("yyyy/MM/dd");

LocalDate hoje = LocalDate.of(2021, 2, 19);

hoje.format(br);    // 19/02/2021
hoje.format(eua);   // 02/19/2021
hoje.format(jp);    // 2021/02/19

LocalDate d = LocalDate.parse("19/02/2021", br);   // texto brasileiro → LocalDate
```

As letras seguem as mesmas do `SimpleDateFormat` (`yyyy`, `MM`, `dd`, `HH`, `mm`, `ss`, `MMMM`, `EEEE`, `a`...).

Com hora:

```java
DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
LocalDateTime.now().format(f);                // 19/02/2021 18:03:12
LocalDateTime.parse("19/02/2021 18:03:12", f);
```

⚠️ Em `LocalDate` não use letras de hora (`HH`) no padrão: lança exceção.

---

## 5. Com `Locale`

O nome do mês/dia vem no idioma escolhido:

```java
DateTimeFormatter alemao = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.GERMANY);
hoje.format(alemao);                // 19 Februar 2021

LocalDate.parse("19 Februar 2021", alemao);   // volta para LocalDate
```

Ou:

```java
DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
```

Estilos prontos localizados:

```java
DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(new Locale("pt", "BR"));
```

---

## 6. Vantagens sobre `SimpleDateFormat`

| `SimpleDateFormat` | `DateTimeFormatter` |
|---|---|
| Não é thread-safe | **Thread-safe** (imutável) |
| Trabalha com `Date` | Trabalha com `java.time` |
| `parse` lança checked | `parse` lança unchecked |

## O que você precisa dominar (Aula 129)

- `format` (objeto→String) e `parse` (String→objeto).
- Constantes ISO (`ISO_LOCAL_DATE`, `BASIC_ISO_DATE`...).
- `ofPattern("dd/MM/yyyy")` e `Locale`.
- O texto de entrada do `parse` deve casar com o padrão.
- `DateTimeFormatter` é thread-safe.

---

# Mapa mental do bloco

```text
java.time (imutável, mês começa em 1)
├── Representam um ponto
│   ├── LocalDate        → 2022-01-27
│   ├── LocalTime        → 09:45
│   ├── LocalDateTime    → 2022-01-27T09:45
│   ├── Instant          → UTC, nanossegundos
│   ├── ZonedDateTime    → data+hora+ZoneId
│   └── OffsetDateTime   → data+hora+offset
├── Representam uma quantidade
│   ├── Duration         → horas/min/seg
│   ├── Period           → anos/meses/dias
│   └── ChronoUnit       → diferença em UMA unidade
├── Ajustes
│   ├── TemporalAdjusters → next, lastDayOfMonth...
│   └── TemporalAdjuster  → interface para o seu ajuste
└── Formatação
    └── DateTimeFormatter → ISO e ofPattern
```

# Resumo rápido (cola de bolso)

| Preciso de... | Use |
|---|---|
| Idade de alguém | `ChronoUnit.YEARS.between(nascimento, hoje)` |
| Dias até um evento | `ChronoUnit.DAYS.between(hoje, evento)` |
| "2 anos e 7 dias" | `Period.between(a, b)` |
| "5 h 30 min" | `Duration.between(a, b)` |
| Último dia do mês | `data.with(TemporalAdjusters.lastDayOfMonth())` |
| Salvar no banco sem dor | `Instant` / UTC |
| Mostrar em outro fuso | `instant.atZone(ZoneId.of("..."))` |
| Texto → data | `LocalDate.parse(txt, formatter)` |
| Data → texto | `data.format(formatter)` |
