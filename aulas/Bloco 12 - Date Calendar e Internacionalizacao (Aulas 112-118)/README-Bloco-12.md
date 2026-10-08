# Bloco 12 — Datas Clássicas, Internacionalização e Formatação

## Aulas 112 a 118

Este bloco continua a parte de **Classes Utilitárias** e entra no assunto que mais gera bugs em sistemas reais: **datas, números e moedas**.

Antes do Java 8, a API de datas era uma bagunça. Neste bloco você vai conhecer as classes **antigas** (`Date`, `Calendar`, `DateFormat`, `SimpleDateFormat`) e as classes de **internacionalização** (`Locale`, `NumberFormat`). Nos próximos blocos você verá a API moderna (`java.time`), mas é essencial entender o "legado" porque:

- sistemas antigos (e muitos sistemas em produção hoje) ainda usam essas classes;
- bibliotecas e drivers de banco ainda devolvem `java.util.Date`;
- `Locale` e `NumberFormat` continuam sendo usados normalmente em qualquer projeto moderno.

As aulas deste bloco são:

```text
112 — Date
113 — Calendar
114 — DateFormat
115 — Internacionalização de datas com Locale
116 — Internacionalização de números com Locale
117 — Internacionalização de moedas com Locale
118 — SimpleDateFormat
```

A sequência tem uma lógica:

```text
Date (um instante em milissegundos)
        ↓
Calendar (manipular campos: dia, mês, ano, hora)
        ↓
DateFormat (transformar data em texto, estilos prontos)
        ↓
Locale (formatar conforme país/idioma)
        ↓
NumberFormat (números por Locale)
        ↓
NumberFormat (moedas por Locale)
        ↓
SimpleDateFormat (formatação personalizada com padrões)
```

---

# Aula 112 — Utility Classes — Date

## 1. A história das datas no Java

O instrutor conta a história de forma bem-humorada, e ela é importante:

```text
Java 1.0  → java.util.Date            (não deu conta)
Java 1.1  → java.util.Calendar        (tentou consertar, também não deu conta)
Java 8    → pacote java.time          (API nova, resolveu de verdade)
```

Ou seja, existem **três gerações** de API de datas no Java. Neste bloco estudamos as duas primeiras. A terceira (`java.time`) é a que você deve usar em código novo.

> Regra prática: **código novo → `java.time`**. **Código legado → `Date`/`Calendar`**.

---

## 2. Cuidado com o import: `java.util.Date` x `java.sql.Date`

Existem duas classes chamadas `Date` no Java:

| Classe | Pacote | Para que serve |
|---|---|---|
| `java.util.Date` | `java.util` | A que usamos na aplicação Java |
| `java.sql.Date` | `java.sql` | Criada para trabalhar com banco de dados (JDBC) |

Na aula usamos **sempre** a de `java.util`:

```java
import java.util.Date;
```

Se a IDE importar `java.sql.Date` sem você perceber, o código pode até compilar, mas o comportamento será outro. Sempre confira o import.

---

## 3. O que um `Date` realmente guarda?

Um `Date` guarda **um único número**: um `long` com a quantidade de **milissegundos** desde:

```text
1º de janeiro de 1970, 00:00:00 UTC
```

Esse ponto no tempo é chamado de **Epoch** (ou "Unix time").

```java
import java.util.Date;

public class DateTest01 {
    public static void main(String[] args) {
        Date date = new Date(0);      // exatamente o Epoch
        System.out.println(date);     // Thu Jan 01 00:00:00 UTC 1970 (depende do fuso)
    }
}
```

Visualize como uma régua do tempo:

```text
1970-01-01        |-------------------------------------->   hoje
   0 ms           |            long milissegundos
```

---

## 4. Criando um `Date`

### 4.1 Com valor em milissegundos

```java
Date date = new Date(1_000_000_000L);   // 1 bilhão de ms
System.out.println(date);               // 12 de janeiro de 1970
```

Na aula o instrutor testa vários valores:

```java
new Date(1_000_000_000L)       // 12/01/1970
new Date(10_000_000_000L)      // 26/04/1970
new Date(1_000_000_000_000L)   // 09/09/2001
```

Isso prova que a classe apenas **conta milissegundos** e transforma em uma data legível quando você imprime.

### 4.2 Data e hora de agora

```java
Date agora = new Date();   // construtor sem argumentos = instante atual
```

---

## 5. Convertendo `Date` em `long` e vice-versa

```java
Date date = new Date();

long ms = date.getTime();   // Date → long
System.out.println(ms);     // ex.: 1613000000000

date.setTime(ms);           // long → Date (altera o próprio objeto)
```

Na aula, o instrutor mostra que dá para pegar o `long` de "hoje" com `getTime()` e passá-lo depois no construtor para recriar a mesma data.

| Método | O que faz |
|---|---|
| `new Date()` | data/hora atual |
| `new Date(long)` | data a partir de milissegundos |
| `getTime()` | devolve os milissegundos (`long`) |
| `setTime(long)` | altera o instante do objeto |
| `toString()` | texto legível (depende do fuso/idioma da JVM) |

---

## 6. Por que `Date` está "obsoleta" (deprecated)?

Se você abrir a classe `Date` na IDE, verá que **quase todos os métodos aparecem riscados**:

```java
date.getYear();   // riscado
date.getMonth();  // riscado
date.getDay();    // riscado
```

Isso significa **deprecated** (depreciado): o Java mantém o método por compatibilidade, mas **recomenda não usar** e pode removê-lo no futuro. Sempre que um método é depreciado, existe uma alternativa melhor (no caso, `Calendar` e depois `java.time`).

O que sobrou de "útil" em `Date` é basicamente:

```text
new Date(), new Date(long), getTime(), setTime(long), before(), after(), compareTo()
```

Comparando datas:

```java
Date a = new Date(1000);
Date b = new Date(2000);

System.out.println(a.before(b));      // true
System.out.println(a.after(b));       // false
System.out.println(a.compareTo(b));   // -1 (a vem antes)
```

---

## 7. Somando uma hora na mão

Como `Date` não tem método de "adicionar", a aula mostra o que era necessário fazer: mexer nos milissegundos.

```java
Date date = new Date();
long umaHoraEmMs = 60 * 60 * 1000;          // 3.600.000 ms
date.setTime(date.getTime() + umaHoraEmMs); // soma 1 hora
```

Funciona, mas é feio e propenso a erro (e se tiver horário de verão?). Esse é um dos motivos pelos quais a classe `Date` foi considerada insuficiente.

---

## 8. Problemas da `Date`

- Quase tudo depreciado.
- Difícil de manipular (somar dias, meses...).
- **Internacionalização** ruim: o `toString()` depende da configuração da JVM.
- Mutável: `setTime` altera o objeto original, o que gera bugs quando compartilhada entre métodos.
- Nome enganoso: representa **data e hora**, não só data.

## O que você precisa dominar (Aula 112)

- `Date` guarda `long` com milissegundos desde 01/01/1970.
- `new Date()`, `new Date(long)`, `getTime()`, `setTime(long)`.
- `java.util.Date` x `java.sql.Date`.
- Por que os métodos estão deprecated.
- `Date` ainda aparece em sistemas legados.

---

# Aula 113 — Utility Classes — Calendar

## 1. Por que `Calendar` surgiu?

`Calendar` foi a tentativa do Java 1.1 de consertar a `Date`. Ela resolve principalmente:

- manipular **campos** (dia, mês, ano, hora);
- **somar** e subtrair tempo;
- suportar diferentes **calendários** do mundo.

---

## 2. `Calendar` é abstrata

```java
Calendar c = new Calendar();   // ERRO DE COMPILAÇÃO — classe abstrata
```

Você não pode instanciar. Para obter uma instância, usa-se o método estático (padrão **Factory Method**):

```java
import java.util.Calendar;

Calendar calendar = Calendar.getInstance();
```

`getInstance()` devolve a implementação adequada para a sua região. No Brasil/Europa/EUA é o `GregorianCalendar`:

| Implementação | Calendário |
|---|---|
| `GregorianCalendar` | gregoriano (o que usamos) |
| `BuddhistCalendar` | budista |
| `JapaneseImperialCalendar` | imperial japonês |

---

## 3. Imprimir o `Calendar` não é útil

```java
System.out.println(calendar);
```

O resultado é um textão cheio de campos internos (`java.util.GregorianCalendar[time=..., areFieldsSet=true...`). Ninguém quer ler isso.

### O "truque" do instrutor

Como ainda não sabemos formatar (isso vem na aula 114), ele converte para `Date`:

```java
System.out.println(calendar.getTime());   // Calendar → Date
```

Relação entre os dois:

```text
Calendar ──getTime()──▶ Date
Calendar ◀──setTime(date)── Date
```

---

## 4. Constantes de `Calendar`

Os métodos de `Calendar` usam **constantes inteiras** para indicar o campo desejado:

| Constante | O que representa |
|---|---|
| `Calendar.YEAR` | ano |
| `Calendar.MONTH` | mês (**começa em 0!**) |
| `Calendar.DAY_OF_MONTH` | dia do mês |
| `Calendar.DAY_OF_WEEK` | dia da semana |
| `Calendar.DAY_OF_YEAR` | dia do ano |
| `Calendar.DAY_OF_WEEK_IN_MONTH` | ocorrência do dia da semana no mês (1ª sexta, 2ª sexta...) |
| `Calendar.HOUR` / `HOUR_OF_DAY` | hora (12h / 24h) |
| `Calendar.MINUTE` / `SECOND` | minuto / segundo |

### ⚠️ Armadilha clássica: o mês começa em zero

```java
Calendar.JANUARY  == 0
Calendar.FEBRUARY == 1
...
Calendar.DECEMBER == 11
```

Por isso **sempre use as constantes** (`Calendar.FEBRUARY`) em vez de números soltos.

### Dias da semana

```java
Calendar.SUNDAY    == 1
Calendar.MONDAY    == 2
...
Calendar.SATURDAY  == 7
```

---

## 5. Primeiro dia da semana

Depende do país: no Brasil/EUA é domingo; na França é segunda.

```java
Calendar c = Calendar.getInstance();

if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
    System.out.println("Domingo é o primeiro dia da semana");
}
```

> Por isso a aula pede para **não comparar com números mágicos** (`== 1`). Use `Calendar.SUNDAY`.

---

## 6. Lendo campos com `get()`

```java
Calendar c = Calendar.getInstance();

System.out.println("Dia da semana: "    + c.get(Calendar.DAY_OF_WEEK));
System.out.println("Dia do mês: "       + c.get(Calendar.DAY_OF_MONTH));
System.out.println("Dia do ano: "       + c.get(Calendar.DAY_OF_YEAR));
System.out.println("Semana no mês: "    + c.get(Calendar.DAY_OF_WEEK_IN_MONTH));
```

Na aula, em 11/02/2021 (quinta-feira):

```text
Dia da semana: 5     (quinta = 5, pois domingo = 1)
Dia do mês: 11
Dia do ano: 42
DAY_OF_WEEK_IN_MONTH: 2   (segunda quinta-feira do mês)
```

`get()` sempre devolve `int`.

---

## 7. Alterando campos com `set()`

```java
c.set(Calendar.YEAR, 2030);
c.set(Calendar.MONTH, Calendar.MARCH);
c.set(Calendar.DAY_OF_MONTH, 15);

// ou tudo de uma vez
c.set(2030, Calendar.MARCH, 15);
```

---

## 8. Somando com `add()`

```java
Calendar c = Calendar.getInstance();

c.add(Calendar.DAY_OF_MONTH, 2);   // daqui a 2 dias
c.add(Calendar.HOUR, 2);           // + 2 horas
c.add(Calendar.MONTH, -1);         // 1 mês atrás (valor negativo subtrai)
```

### O `add()` **vira** o campo maior

Se você somar mais do que o campo comporta, o `add()` "transborda" para o campo seguinte:

```text
18:00 do dia 13  + add(HOUR, 12)  →  06:00 do dia 14   (virou o dia)
```

O mesmo vale para mês→ano:

```text
add(MONTH, 12) em 2021  →  2022   (virou o ano)
```

---

## 9. `roll()` — somar **sem** virar o campo maior

```java
c.roll(Calendar.HOUR, 12);
```

O `roll` gira apenas dentro do próprio campo:

```text
18:00 do dia 13  + roll(HOUR, 12)  →  06:00 do dia 13  (continua dia 13!)
```

| Método | Comportamento |
|---|---|
| `add(campo, valor)` | soma e **propaga** para o campo maior (vira dia/mês/ano) |
| `roll(campo, valor)` | soma e **mantém** o campo maior (rodízio dentro do campo) |

Exemplo comparativo com meses:

```java
Calendar c1 = Calendar.getInstance();
c1.set(2021, Calendar.NOVEMBER, 10);
c1.add(Calendar.MONTH, 3);
// 10/02/2022  → virou o ano

Calendar c2 = Calendar.getInstance();
c2.set(2021, Calendar.NOVEMBER, 10);
c2.roll(Calendar.MONTH, 3);
// 10/02/2021  → ano continua 2021
```

---

## 10. Convertendo entre `Calendar` e `Date`

```java
Calendar c = Calendar.getInstance();

Date date = c.getTime();     // Calendar → Date

c.setTime(new Date());       // Date → Calendar
```

Isso é muito útil ao integrar sistemas legados que usam `Date` com código que manipula `Calendar`.

## Observação final do instrutor

> Você provavelmente só vai usar `Calendar` e `Date` em **sistemas legados**. Em sistemas novos use o pacote `java.time`.

## O que você precisa dominar (Aula 113)

- `Calendar` é abstrata e usa `getInstance()`.
- O mês começa em **0**.
- `get()`, `set()`, `add()`, `roll()`.
- Diferença entre `add` e `roll`.
- Converter `Calendar ↔ Date`.
- Usar constantes em vez de números mágicos.

---

# Aula 114 — Classes Utilitárias — DateFormat

## 1. Para que serve?

`DateFormat` transforma uma `Date` em **texto formatado** (e também o contrário, via `parse`). Está no pacote:

```java
import java.text.DateFormat;
```

É uma **classe abstrata**, então usamos seus métodos estáticos de fábrica (`getXxxInstance`).

---

## 2. Os métodos de fábrica

| Método | O que formata |
|---|---|
| `DateFormat.getDateInstance(estilo)` | só a **data** |
| `DateFormat.getTimeInstance(estilo)` | só a **hora** |
| `DateFormat.getDateTimeInstance(estiloData, estiloHora)` | data **e** hora |

---

## 3. Os estilos (constantes)

| Constante | Exemplo (padrão inglês dos EUA) |
|---|---|
| `DateFormat.SHORT` | `2/11/21` |
| `DateFormat.MEDIUM` | `Feb 11, 2021` |
| `DateFormat.LONG` | `February 11, 2021` |
| `DateFormat.FULL` | `Thursday, February 11, 2021` |
| `DateFormat.DEFAULT` | equivale a `MEDIUM` |

O resultado depende da **configuração regional do computador** (por isso em um Windows em português sai em português, em inglês sai em inglês).

---

## 4. Exemplo completo — igual ao da aula

Usando um `array` de `DateFormat`:

```java
import java.text.DateFormat;
import java.util.Calendar;

public class DateFormatTest01 {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();

        DateFormat[] formats = new DateFormat[6];
        formats[0] = DateFormat.getInstance();
        formats[1] = DateFormat.getDateInstance();
        formats[2] = DateFormat.getDateInstance(DateFormat.SHORT);
        formats[3] = DateFormat.getDateInstance(DateFormat.MEDIUM);
        formats[4] = DateFormat.getDateInstance(DateFormat.LONG);
        formats[5] = DateFormat.getDateInstance(DateFormat.FULL);

        for (DateFormat df : formats) {
            System.out.println(df.format(calendar.getTime()));
        }
    }
}
```

### Detalhe importante

`format()` recebe um `Date`, **não** um `Calendar`:

```java
df.format(calendar);            // ERRO
df.format(calendar.getTime());  // OK
```

Por isso aparece sempre `calendar.getTime()` (ou `new Date()`).

---

## 5. Data e hora juntas

```java
DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
System.out.println(df.format(new Date()));
// February 11, 2021 at 6:30 PM
```

Só hora:

```java
DateFormat dfHora = DateFormat.getTimeInstance(DateFormat.MEDIUM);
System.out.println(dfHora.format(new Date()));
```

---

## 6. Limitação

Os estilos `SHORT`, `MEDIUM`, `LONG` e `FULL` são **prontos** — você não controla o desenho exato. Para formatos personalizados (ex.: `15/02/2021 às 14h`), existe o `SimpleDateFormat` (aula 118).

## O que você precisa dominar (Aula 114)

- `DateFormat` é abstrata, usa métodos de fábrica.
- `getDateInstance`, `getTimeInstance`, `getDateTimeInstance`.
- Os 4 estilos + `DEFAULT`.
- `format(Date)`.
- A saída depende da configuração da JVM/SO.

---

# Aula 115 — Internacionalização de Datas com `Locale`

## 1. O que é `Locale`?

`Locale` representa uma **região geográfica, política ou cultural**. Ele é usado para formatar **datas, números e moedas** do jeito que as pessoas daquele lugar esperam ver.

```java
import java.util.Locale;
```

> O Java sempre apostou em ser multiplataforma e multi-idioma. Por isso a parte de internacionalização é muito sólida.

---

## 2. Como um `Locale` é identificado: idioma + país

Segue dois padrões ISO:

| Parte | Padrão | Exemplo |
|---|---|---|
| **Idioma** | ISO 639 (2 letras minúsculas) | `pt`, `it`, `en`, `ja` |
| **País** | ISO 3166 (2 letras maiúsculas) | `BR`, `IT`, `US`, `JP` |

```java
Locale brasil  = new Locale("pt", "BR");   // português do Brasil
Locale italia  = new Locale("it", "IT");   // italiano da Itália
Locale suica   = new Locale("it", "CH");   // italiano falado na Suíça
```

> Note a flexibilidade: o **mesmo idioma** (`it`) em **países diferentes** gera formatações diferentes (Itália x Suíça).

### Nota moderna
Nas versões novas do Java (19+) o construtor `new Locale(...)` está depreciado em favor de `Locale.of("pt", "BR")` ou `Locale.forLanguageTag("pt-BR")`. Na aula (Java 8/11) usa-se o construtor.

---

## 3. Constantes prontas

Para alguns países existem constantes:

```java
Locale.US
Locale.UK
Locale.ITALY
Locale.JAPAN
Locale.FRANCE
Locale.GERMANY
Locale.CHINA
```

> **Não existe `Locale.BRAZIL`** nas constantes. Para o Brasil é preciso criar `new Locale("pt", "BR")`. (O instrutor comenta isso na aula seguinte.)

---

## 4. Usando `Locale` com `DateFormat`

Todos os `getXxxInstance` têm uma sobrecarga que recebe o `Locale`:

```java
Calendar calendar = Calendar.getInstance();

Locale italia = new Locale("it", "IT");
Locale suica  = new Locale("it", "CH");

DateFormat df1 = DateFormat.getDateInstance(DateFormat.FULL, italia);
DateFormat df2 = DateFormat.getDateInstance(DateFormat.FULL, suica);

System.out.println(df1.format(calendar.getTime()));   // giovedì 11 febbraio 2021
System.out.println(df2.format(calendar.getTime()));   // giovedì, 11 febbraio 2021
```

Observe que na Suíça há uma **vírgula** depois do dia da semana. São essas nuances que importam quando um sistema atende vários países.

Outros exemplos da aula:

```java
Locale india   = new Locale("hi", "IN");
Locale japao   = Locale.JAPAN;
Locale holanda = new Locale("nl", "NL");
```

Cada um formata a mesma data com caracteres, ordem e separadores diferentes.

---

## 5. Nomes traduzidos: `getDisplayCountry` e `getDisplayLanguage`

```java
Locale italia = new Locale("it", "IT");
Locale japao  = Locale.JAPAN;

System.out.println(italia.getDisplayCountry());        // Italy  (no idioma do SO)
System.out.println(italia.getDisplayCountry(japao));   // イタリア (no idioma japonês)

System.out.println(italia.getDisplayLanguage());       // Italian
System.out.println(italia.getDisplayLanguage(italia)); // italiano
```

Ou seja, você escolhe **em que idioma** o nome do país/idioma será exibido passando outro `Locale`.

---

## 6. De onde vem o `Locale` do usuário?

Na vida real, o `Locale` costuma vir de:

1. **Cabeçalho HTTP** `Accept-Language` enviado pelo navegador.
2. **Preferência** escolhida pelo usuário no sistema (ex.: seletor de país/idioma da Amazon).
3. **Locale padrão da JVM** (`Locale.getDefault()`).

Por isso o sistema precisa ser capaz de receber um `Locale` e formatar tudo a partir dele.

## O que você precisa dominar (Aula 115)

- O que é `Locale` e para que serve.
- Idioma (ISO 639) + País (ISO 3166).
- Criar com `new Locale(idioma, país)` e com constantes.
- Passar `Locale` para `DateFormat.getXxxInstance`.
- `getDisplayCountry` / `getDisplayLanguage`.

---

# Aula 116 — Internacionalização de Números com `Locale`

## 1. Métodos úteis de `Locale`

```java
Locale padrao = Locale.getDefault();                    // Locale do computador
System.out.println(padrao);                             // en_US, pt_BR, ...

String[] paises  = Locale.getISOCountries();            // todos os códigos de país
String[] idiomas = Locale.getISOLanguages();            // todos os códigos de idioma
Locale[] todos   = Locale.getAvailableLocales();        // todas as combinações suportadas
```

Existem **mais Locales do que idiomas**, porque o mesmo idioma pode variar por país (português de Portugal, de Angola, do Brasil).

---

## 2. `NumberFormat`

Pacote: `java.text.NumberFormat` (também **abstrata**, também usa métodos de fábrica).

| Método | Para quê |
|---|---|
| `NumberFormat.getInstance()` | número genérico |
| `NumberFormat.getNumberInstance()` | número |
| `NumberFormat.getIntegerInstance()` | número inteiro (arredonda) |
| `NumberFormat.getPercentInstance()` | porcentagem |
| `NumberFormat.getCurrencyInstance()` | moeda (aula 117) |
| `NumberFormat.getCompactNumberInstance()` | `1K`, `2M` (disponível a partir do Java 12) |

Todos aceitam um `Locale` como parâmetro.

> Na aula, o instrutor está no Java 8, então `getCompactNumberInstance` aparece em vermelho (só existe no Java 12+). Ele explica que o Java 8 cobre ~85% dos casos.

---

## 3. Exemplo da aula

```java
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatTest01 {
    public static void main(String[] args) {
        Locale brasil = new Locale("pt", "BR");

        NumberFormat[] nfa = new NumberFormat[4];
        nfa[0] = NumberFormat.getInstance();                    // padrão do SO
        nfa[1] = NumberFormat.getInstance(Locale.JAPAN);
        nfa[2] = NumberFormat.getInstance(brasil);
        nfa[3] = NumberFormat.getInstance(Locale.ITALY);

        double valor = 10_000.2123;

        for (NumberFormat nf : nfa) {
            System.out.println(nf.format(valor));
        }
    }
}
```

Saída típica (SO em inglês):

```text
10,000.212     ← padrão EUA (vírgula = milhar, ponto = decimal)
10,000.212     ← Japão (mesmo padrão dos EUA)
10.000,212     ← Brasil (ponto = milhar, vírgula = decimal)
10.000,212     ← Itália (igual ao Brasil)
```

### Pontos importantes

- Em código, o literal numérico **sempre** usa ponto (`10000.2123`) — vírgula não é válida para decimal em código Java.
- O formatador é que "traduz" para a convenção do país.
- Por padrão `NumberFormat` mostra no **máximo 3 casas decimais** e arredonda (`10,000.2123 → 10,000.212`).

---

## 4. Por que isso importa?

Imagine um sistema financeiro exibindo `1.000` para um usuário dos EUA (mil) e para um usuário do Brasil (mil, com ponto)... e outro exibindo `1,000` (um, com três casas decimais, para um brasileiro). Errar a formatação pode **mudar o valor interpretado**.

## O que você precisa dominar (Aula 116)

- `Locale.getDefault()`, `getISOCountries()`, `getISOLanguages()`, `getAvailableLocales()`.
- `NumberFormat` é abstrata e usa métodos de fábrica.
- Diferença de separadores milhar/decimal por país.
- Literal numérico no código usa ponto sempre.

---

# Aula 117 — Internacionalização de Moedas com `Locale`

## 1. `getCurrencyInstance`

Quase idêntico ao número, mas devolve valores monetários com **símbolo** e **casas decimais da moeda**.

```java
Locale brasil = new Locale("pt", "BR");

NumberFormat[] nfa = new NumberFormat[4];
nfa[0] = NumberFormat.getCurrencyInstance();               // padrão do SO ($)
nfa[1] = NumberFormat.getCurrencyInstance(Locale.JAPAN);   // ￥
nfa[2] = NumberFormat.getCurrencyInstance(brasil);         // R$
nfa[3] = NumberFormat.getCurrencyInstance(Locale.ITALY);   // €

double valor = 10_000.2123;
for (NumberFormat nf : nfa) {
    System.out.println(nf.format(valor));
}
```

Saída:

```text
$10,000.21
￥10,000
R$ 10.000,21
€ 10.000,21
```

Note que o **iene não tem centavos**: casas decimais zero.

---

## 2. Controlando as casas decimais

```java
NumberFormat nf = NumberFormat.getInstance(Locale.US);

System.out.println(nf.getMaximumFractionDigits());   // 3
System.out.println(nf.getMinimumFractionDigits());   // 0
System.out.println(nf.getMaximumIntegerDigits());    // enorme

nf.setMaximumFractionDigits(2);                      // agora só 2 casas
System.out.println(nf.format(10_000.2123));          // 10,000.21
```

Padrões por moeda:

| Locale | Casas decimais da moeda |
|---|---|
| EUA, Brasil, Itália | 2 |
| Japão | 0 |

---

## 3. O caminho inverso: `parse()`

`format()` transforma **número → texto**. `parse()` transforma **texto → número**.

```java
String valorTexto = "1,200.50";
NumberFormat nf = NumberFormat.getInstance(Locale.US);

Number n = nf.parse(valorTexto);   // lança ParseException (checked!)
System.out.println(n);             // 1200.5
```

`parse` retorna `Number` e declara `throws ParseException`, então você precisa de `try/catch` ou `throws`.

Para obter um tipo específico:

```java
double d = n.doubleValue();
int i = n.intValue();
long l = n.longValue();
```

### Cuidado 1 — `parse` lê só o começo do texto

Ele para no primeiro caractere inválido **depois** de ter lido algo:

```java
nf.parse("1,200.50abc");   // OK → 1200.5  (ignora "abc")
nf.parse("abc1,200.50");   // ParseException (começa inválido)
```

### Cuidado 2 — parse de moeda exige o símbolo certo

```java
NumberFormat nfDolar = NumberFormat.getCurrencyInstance(Locale.US);
nfDolar.parse("$1,200.50");   // OK

NumberFormat nfIene = NumberFormat.getCurrencyInstance(Locale.JAPAN);
nfIene.parse("$1,200.50");    // ParseException — símbolo é outro
```

Cada `NumberFormat` só entende o símbolo da **sua** moeda.

---

## 4. Boa prática do mundo real

O instrutor termina com uma dica valiosa:

> No banco de dados você **não guarda** "R$ 1.200,50". Guarda o valor **numérico** (`1200.50`). Na hora de mostrar para o usuário, aplica o `Locale`.

Ou seja:

```text
Banco de dados → número puro
Tela / relatório → NumberFormat + Locale
```

Em sistemas financeiros reais também se usa `BigDecimal` em vez de `double`, mas isso é assunto futuro.

## O que você precisa dominar (Aula 117)

- `getCurrencyInstance` com e sem `Locale`.
- Símbolo e casas decimais variam por moeda.
- `setMaximumFractionDigits` / `getMaximumFractionDigits`.
- `format` (número → texto) e `parse` (texto → número).
- `parse` lança `ParseException` e é exigente com símbolo.
- Não armazenar texto formatado no banco.

---

# Aula 118 — Classes Utilitárias — SimpleDateFormat

## 1. Para que serve?

`SimpleDateFormat` permite criar **formatos personalizados** de data usando um **padrão** (pattern) montado com letras. É uma classe concreta (não abstrata), subclasse de `DateFormat`:

```java
import java.text.SimpleDateFormat;
```

---

## 2. Principais letras do padrão

| Letra | Significado | Exemplo |
|---|---|---|
| `y` | ano | `yyyy` → 2021, `yy` → 21 |
| `M` | mês | `M` → 2, `MM` → 02, `MMM` → fev, `MMMM` → fevereiro |
| `d` | dia do mês | `dd` → 15 |
| `E` | dia da semana | `EEE` → seg, `EEEE` → segunda-feira |
| `H` | hora (0–23) | `HH` → 14 |
| `h` | hora (1–12) | `hh` → 02 |
| `m` | minuto | `mm` → 05 |
| `s` | segundo | `ss` → 09 |
| `a` | AM/PM | `a` → PM |
| `G` | era | `G` → AD |
| `z` | fuso horário | `z` → BRT |
| `w` | semana do ano | `w` → 7 |
| `D` | dia do ano | `D` → 46 |

### ⚠️ Atenção a maiúsculas/minúsculas

```text
MM = mês        mm = minuto
HH = hora 0-23  hh = hora 1-12
yyyy = ano      YYYY = "week year" (pode dar bug em virada de ano!)
```

Esse erro (`mm` no lugar de `MM`) é um dos bugs mais comuns com datas.

---

## 3. Criando e usando

```java
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatTest01 {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(sdf.format(new Date()));   // 15/02/2021 14:35:09
    }
}
```

---

## 4. Texto literal entre aspas simples

Letras do padrão são interpretadas. Se quiser escrever texto fixo, coloque entre **aspas simples**:

```java
SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
System.out.println(sdf.format(new Date()));   // 15 de fevereiro de 2021
```

Sem as aspas, o `d` de "de" viraria o dia e o `e`, `a`... seriam interpretados — dando erro ou resultado estranho.

Na aula o instrutor testa algo como:

```java
new SimpleDateFormat("G, 'dia' dd 'de' MMMM 'de' yyyy")
// AD, dia 15 de fevereiro de 2021
```

`G` = Anno Domini (do latim "no ano do Senhor", ou seja, depois de Cristo).

---

## 5. Combinando com `Locale`

O nome do mês/dia depende do idioma. Passe um `Locale` como segundo argumento:

```java
Locale brasil = new Locale("pt", "BR");
SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", brasil);
System.out.println(sdf.format(new Date()));
// segunda-feira, 15 de fevereiro de 2021
```

---

## 6. O caminho inverso: `parse()`

Transforma **String → Date** seguindo o mesmo padrão:

```java
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

try {
    Date data = sdf.parse("15/02/2021");
    System.out.println(data);
} catch (ParseException e) {
    System.out.println("Data inválida!");
}
```

Detalhes:

- `parse` lança `ParseException` (checked) → obrigatório tratar.
- O texto precisa **casar com o padrão**; se não casar, exceção.
- O resultado é um `Date` simples (sem frescura): apenas o instante equivalente.

### Cuidado: `setLenient`
Por padrão o `SimpleDateFormat` é "leniente": `32/01/2021` vira `01/02/2021`. Para ser rigoroso:

```java
sdf.setLenient(false);
```

### Cuidado: `SimpleDateFormat` não é thread-safe
Nunca compartilhe a mesma instância entre várias threads. (A API nova `DateTimeFormatter` resolve isso.)

---

## 7. Resumo comparativo dos formatadores

| Classe | Vantagem | Limitação |
|---|---|---|
| `DateFormat` | estilos prontos e simples | pouco controle |
| `DateFormat` + `Locale` | adapta ao país | ainda pouco controle |
| `SimpleDateFormat` | padrão totalmente personalizado | cuidado com letras e thread-safety |
| `DateTimeFormatter` (futuro) | moderno e thread-safe | só para `java.time` |

## O que você precisa dominar (Aula 118)

- Montar padrões com `y M d H m s E a`.
- Diferença entre `MM` e `mm`, `HH` e `hh`.
- Texto literal entre aspas simples.
- `format(Date)` e `parse(String)`.
- `ParseException`.
- Passar `Locale` ao construtor.

---

# Mapa mental do bloco

```text
Tempo no Java (legado)
├── Date           → long de milissegundos desde 1970 (quase tudo deprecated)
├── Calendar       → get / set / add / roll (mês começa em 0)
├── DateFormat     → SHORT / MEDIUM / LONG / FULL
├── SimpleDateFormat → "dd/MM/yyyy HH:mm" (personalizado)
└── Internacionalização
    ├── Locale         → idioma (ISO 639) + país (ISO 3166)
    ├── NumberFormat   → números por país
    └── NumberFormat   → moedas por país (currency)
```

# Resumo rápido (cola de bolso)

| Preciso de... | Use |
|---|---|
| Instante atual (legado) | `new Date()` |
| Somar dias/horas (legado) | `Calendar.add(...)` |
| Girar um campo sem virar o maior | `Calendar.roll(...)` |
| Data com estilo pronto | `DateFormat.getDateInstance(estilo, locale)` |
| Data com padrão próprio | `new SimpleDateFormat("dd/MM/yyyy")` |
| Texto → data | `sdf.parse("...")` |
| Número por país | `NumberFormat.getInstance(locale)` |
| Moeda por país | `NumberFormat.getCurrencyInstance(locale)` |
| Locale do Brasil | `new Locale("pt", "BR")` |
