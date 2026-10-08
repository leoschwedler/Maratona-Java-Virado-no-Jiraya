# Bloco 14 — ResourceBundle, Expressões Regulares e Scanner com Delimitadores

## Aulas 130 a 137

Este bloco mistura três assuntos de classes utilitárias que aparecem constantemente em projetos reais:

1. **`ResourceBundle`** — internacionalizar **mensagens** do sistema (textos de tela, erros, avisos) em vários idiomas.
2. **Expressões regulares (Regex)** — encontrar e validar **padrões** dentro de textos (`Pattern` e `Matcher`).
3. **`Scanner` com delimitadores** — quebrar textos em *tokens* e converter cada token no tipo certo.

As aulas deste bloco são:

```text
130 — ResourceBundle
131 — Regex pt 01 — Pattern e Matcher
132 — Regex pt 02 — Metacaracteres
133 — Regex pt 03 — Range (intervalos)
134 — Regex pt 04 — Quantificadores pt 01
135 — Regex pt 05 — Quantificadores pt 02
136 — Regex pt 06 — Anchor (^) e negação
137 — Scanner — Tokens e Delimitadores
```

A sequência tem uma lógica:

```text
Locale (bloco anterior)
        ↓
ResourceBundle  → textos traduzidos por idioma
        ↓
Pattern + Matcher → procurar padrões em texto
        ↓
Metacaracteres (\d \s \w) → atalhos
        ↓
Range [a-z] → conjuntos de caracteres
        ↓
Quantificadores (? * + {n,m}) → repetição
        ↓
Âncoras (^ $) e grupos ( ) | → posição e alternativa
        ↓
Scanner + delimitador → tokens tipados
```

---

# Aula 130 — Utility Classes — ResourceBundle

## 1. Qual problema ele resolve?

No bloco anterior você formatou datas, números e moedas com `Locale`. Mas e as **mensagens**?

Exemplo do instrutor: no site da Amazon holandesa (`amazon.nl`), todos os textos aparecem em holandês. Se o usuário trocar o idioma para inglês, os textos mudam. Isso é feito com **mensagens externas e traduzidas**.

Em Java, a ferramenta para isso é o **`ResourceBundle`**.

> Ideia central: o código **nunca** escreve a mensagem direto. Ele pede uma **chave** (`"hello"`), e o `ResourceBundle` devolve o texto no idioma certo.

```java
import java.util.ResourceBundle;
```

---

## 2. Arquivos `.properties`

As mensagens ficam em arquivos de texto com extensão `.properties`, no formato **chave = valor**:

```properties
# Isto é um comentário
! Isto também é um comentário
hello=Hello
good.morning=Good morning
```

- `#` ou `!` no início da linha → comentário.
- `=` separa a chave do valor.
- A chave precisa ser **única** no arquivo.

---

## 3. Onde criar e como nomear os arquivos

Os arquivos ficam na pasta de **resources** (não é uma pasta de código Java). No IntelliJ: criar uma pasta `resources` e marcá-la como *Resources Root*, ou colocar na raiz dos recursos do classpath.

### A regra de nomes (muito importante)

```text
<nomeBase>_<idioma>_<PAÍS>.properties
```

Exemplos com nome base `messages`:

```text
messages_en_US.properties     → inglês dos EUA
messages_pt_BR.properties     → português do Brasil
messages_pt.properties        → português (qualquer país)
messages.properties           → padrão (último recurso)
```

O **nome base** é livre (`messages`, `labels`, `mensagens`...). O que importa é o sufixo com idioma e país, que segue os mesmos padrões ISO do `Locale`.

### Exemplo

`messages_en_US.properties`:

```properties
hello=Hello
good.morning=Good morning
```

`messages_pt_BR.properties`:

```properties
hello=Olá
good.morning=Bom dia
```

---

## 4. Usando no código

```java
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleTest01 {
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("en", "US"));

        System.out.println(bundle.getString("hello"));         // Hello
        System.out.println(bundle.getString("good.morning"));  // Good morning
    }
}
```

Agora o usuário troca o idioma para português do Brasil. **O resto do código não muda**, só o `Locale`:

```java
ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("pt", "BR"));

System.out.println(bundle.getString("hello"));         // Olá
System.out.println(bundle.getString("good.morning"));  // Bom dia
```

Cuidados:

- O **nome base** passado ao `getBundle` não leva extensão: `"messages"`, não `"messages.properties"`.
- Se a pasta for um pacote, o nome inclui o caminho: `"resources.messages"`.
- Chave inexistente → `MissingResourceException` (unchecked).
- Para evitar a exceção, você pode testar antes: `bundle.containsKey("chave")`.
- Outros métodos úteis: `getKeys()`, `keySet()`, `getLocale()`.

---

## 5. A regra de fallback (a mais cobrada)

Digamos que o usuário esteja com o `Locale` **francês do Canadá** (`fr_CA`) e o sistema só tenha `en_US` como idioma padrão da JVM. O Java procura **nesta ordem**:

```text
1. messages_fr_CA.properties      ← idioma + país pedidos
2. messages_fr.properties         ← só o idioma pedido
3. messages_en_US.properties      ← Locale padrão da JVM (idioma + país)
4. messages_en.properties         ← idioma padrão da JVM
5. messages.properties            ← arquivo base, sem sufixo
6. MissingResourceException       ← nada encontrado
```

Isso se chama **fallback**. Em outras palavras: do mais específico para o mais genérico.

### Resumindo a regra

> Primeiro o `Locale` pedido (completo, depois só o idioma), depois o `Locale` padrão da JVM (completo, depois só o idioma), por último o arquivo sem sufixo.

---

## 6. Evitando repetição com o arquivo base

Se várias mensagens são **idênticas em todos os idiomas** (um código, uma sigla, uma mensagem técnica), não precisa repetir em todos os arquivos. Crie `messages.properties` com elas:

`messages.properties`:

```properties
ai=Isto vale para todos os idiomas
```

Mesmo pedindo `pt_BR`, a chave `ai` não está em `messages_pt_BR.properties`, então o Java cai no fallback e acha no arquivo base.

> Dica do instrutor: coloque no arquivo base as mensagens "universais" e nos específicos só o que realmente é traduzido. Cuidado ao deixar *tudo* em um idioma específico no arquivo base: se o `Locale` do sistema mudar, o fallback pode se perder.

---

## 7. Acentos em arquivos `.properties`

Historicamente (Java 8 e anteriores), arquivos `.properties` eram lidos em **ISO-8859-1**, então acentos exigiam escape Unicode (por exemplo, `á` virava `\u00e1`). A partir do Java 9, o `ResourceBundle` lê `.properties` em **UTF-8** (com fallback), o que simplifica. Configure a IDE para salvar o arquivo em UTF-8.

## O que você precisa dominar (Aula 130)

- `ResourceBundle` internacionaliza **mensagens**, `Locale` formata **valores**.
- Arquivos `.properties`: `chave=valor`, comentários com `#`/`!`.
- Nomenclatura `base_idioma_PAÍS.properties`.
- `ResourceBundle.getBundle(base, locale)` e `getString(chave)`.
- Ordem de fallback.
- `MissingResourceException` para chave inexistente.

---

# Aula 131 — Regex pt 01 — `Pattern` e `Matcher`

## 1. O que é uma expressão regular?

Expressão regular (**regex**, de *regular expression*) é uma **linguagem de padrões** feita de caracteres comuns e **metacaracteres**. Serve para:

- **Encontrar** trechos de texto que seguem um padrão (links, e-mails, números);
- **Validar** se um texto tem o formato correto (CPF, e-mail, telefone).

> O instrutor avisa: ninguém decora regex. Desenvolvedores experientes com 20 anos de carreira pesquisam no Google. O objetivo é entender o mecanismo.

Exemplos de uso:

- achar todos os links `http` de um texto;
- extrair os e-mails de um arquivo cheio de texto;
- verificar se o que o usuário digitou é um e-mail sintaticamente correto (isso **não** garante que o e-mail exista).

---

## 2. As duas classes principais

Pacote `java.util.regex`:

```java
import java.util.regex.Pattern;
import java.util.regex.Matcher;
```

| Classe | Papel |
|---|---|
| `Pattern` | **Compila** a expressão regular (o padrão que você procura) |
| `Matcher` | **Aplica** o padrão a um texto e guarda os resultados |

O fluxo sempre é:

```text
String regex ──Pattern.compile()──▶ Pattern
Pattern + texto ──pattern.matcher(texto)──▶ Matcher
Matcher ──find()──▶ true/false  (+ start(), end(), group())
```

---

## 3. Primeiro exemplo — como na aula

```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest01 {
    public static void main(String[] args) {
        String regex = "ab";
        String texto = "abaabbabbabab";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        System.out.println("Texto:       " + texto);
        System.out.println("Índice:      0123456789...");
        System.out.println("Expressão:   " + regex);
        System.out.println("Posições encontradas:");

        while (matcher.find()) {
            System.out.println(matcher.start() + " " + matcher.group());
        }
    }
}
```

### Entendendo os métodos

| Método | O que faz |
|---|---|
| `Pattern.compile(regex)` | compila (e valida) a expressão |
| `pattern.matcher(texto)` | cria o `Matcher` para aquele texto |
| `matcher.find()` | procura a **próxima** ocorrência; retorna `true` se achou |
| `matcher.start()` | índice (posição) onde a ocorrência **começa** |
| `matcher.end()` | índice **logo depois** do final da ocorrência |
| `matcher.group()` | o texto encontrado |

`find()` dentro de um `while` percorre todo o texto: cada chamada continua de onde a anterior parou.

---

## 4. As ocorrências **não se sobrepõem**

Exemplo da aula: procurar `aba` em `abababa`:

```java
Pattern p = Pattern.compile("aba");
Matcher m = p.matcher("abababa");
while (m.find()) {
    System.out.println(m.start());
}
// 0
// 4
```

Visualize:

```text
índice:  0 1 2 3 4 5 6
texto:   a b a b a b a
         └─┬─┘     └─┬─┘
         aba(0)     aba(4)
```

Por que não achou na posição 2? Porque a ocorrência que começou em 0 **consumiu** os índices 0, 1 e 2. A busca continua a partir do índice 3 (que é `b`, não bate), depois 4 (acha `aba`).

> Regra: depois de achar, o `Matcher` continua **depois do fim** da ocorrência.

---

## 5. `matches()` x `find()` x `lookingAt()`

| Método | Pergunta que responde |
|---|---|
| `find()` | Existe um trecho que bate **em algum lugar** do texto? |
| `matches()` | O texto **inteiro** bate com o padrão? |
| `lookingAt()` | O **começo** do texto bate com o padrão? |

```java
Pattern.compile("ab").matcher("abab").find();      // true
Pattern.compile("ab").matcher("abab").matches();   // false (texto inteiro não é "ab")
Pattern.compile("ab").matcher("abxyz").lookingAt();// true
```

## O que você precisa dominar (Aula 131)

- O que é regex e para que serve (buscar e validar).
- Fluxo: `Pattern.compile` → `matcher` → `find`.
- `start()`, `end()`, `group()`.
- Ocorrências não se sobrepõem.
- Diferença entre `find`, `matches` e `lookingAt`.

---

# Aula 132 — Regex pt 02 — Metacaracteres

## 1. O que são?

**Metacaracteres** são caracteres com significado especial na regex. Eles funcionam como **atalhos** para grupos de caracteres, assim você não precisa listar cada um.

Texto de exemplo da aula (com índices para você visualizar): `"a1 2 b3\t4"` – letras, números, espaços e tab misturados.

---

## 2. ⚠️ O problema da barra invertida em Java

Em regex, o metacaractere de dígito é `\d`. Mas dentro de uma String Java, a barra `\` já é caractere de escape. Então é preciso **duplicá-la**:

```java
String regex = "\\d";     // regex real: \d
```

Regra prática:

```text
Na regex:        \d
Na String Java:  "\\d"
```

Para casar uma barra invertida **de verdade** seriam 4: `"\\\\"`.

---

## 3. Os principais metacaracteres

| Meta | Casa com | Oposto |
|---|---|---|
| `\d` | um **dígito** (0–9) | `\D` — tudo que **não** é dígito |
| `\s` | **espaço em branco** (espaço, `\t`, `\n`, `\r`, `\f`) | `\S` — tudo que **não** é espaço |
| `\w` | **caractere de palavra**: letras, dígitos e `_` | `\W` — tudo que **não** é de palavra |
| `.` | **qualquer** caractere (menos quebra de linha) | — |

> Fácil de lembrar: a letra **minúscula** pega o grupo; a **maiúscula** pega o contrário.

### Exemplo — achar todos os dígitos

```java
String texto = "a1 2 b3\t4";
Pattern p = Pattern.compile("\\d");
Matcher m = p.matcher(texto);
while (m.find()) {
    System.out.println(m.start() + " " + m.group());
}
// 1 1
// 3 2
// 6 3
// 8 4
```

### Exemplo — espaços em branco

```java
Pattern.compile("\\s")   // acha o espaço e o TAB
```

Note que `\s` pega espaço **e** tabulação.

### Exemplo — `\w` x `\W`

- `\w` acha letras, dígitos e underscore.
- `\W` acha o que sobrou: espaços e símbolos (`@`, `.`, `-`, ...).

> Observe: `\w` **não** inclui o ponto, o arroba nem o hífen. Isso importa quando você for validar e-mail (aula 135).

---

## 4. Resumo visual

```text
texto:  H e l l o _ 2 0 ,   J a v a !
\w      ✔ ✔ ✔ ✔ ✔ ✔ ✔ ✔ ✘ ✘ ✔ ✔ ✔ ✔ ✘
\W      ✘ ✘ ✘ ✘ ✘ ✘ ✘ ✘ ✔ ✔ ✘ ✘ ✘ ✘ ✔
\d      ✘ ✘ ✘ ✘ ✘ ✘ ✔ ✔ ✘ ✘ ✘ ✘ ✘ ✘ ✘
\s      ✘ ✘ ✘ ✘ ✘ ✘ ✘ ✘ ✘ ✔ ✘ ✘ ✘ ✘ ✘
```

## O que você precisa dominar (Aula 132)

- `\d`, `\D`, `\s`, `\S`, `\w`, `\W` e `.`.
- A barra tem que ser dobrada em Java (`"\\d"`).
- A diferença entre o metacaractere minúsculo e o maiúsculo.
- `\s` inclui tab e quebra de linha.

---

# Aula 133 — Regex pt 03 — Range (intervalos)

## 1. Colchetes `[ ]` = "um caractere dentre estes"

Um **conjunto** (ou *classe de caracteres*) casa com **um** caractere que esteja dentro dele.

```java
"[abc]"      // a OU b OU c  (um caractere)
```

⚠️ Não confunda: `[abc]` **não** procura a palavra `abc`. Procura **uma** letra, que pode ser `a`, `b` ou `c`.

```java
String texto = "Abacaxi e banana na mesa";
Pattern p = Pattern.compile("[abc]");
// acha cada 'a', 'b' ou 'c' minúsculo (o 'A' maiúsculo NÃO conta)
```

Regex é **case-sensitive**: `[abc]` ignora `A`, `B`, `C`. Para incluir maiúsculas:

```java
"[abcABC]"
```

---

## 2. Intervalos com hífen

Para não listar tudo:

```java
"[a-z]"        // qualquer letra minúscula
"[A-Z]"        // qualquer letra maiúscula
"[0-9]"        // qualquer dígito (equivale a \d)
"[a-zA-Z]"     // qualquer letra
"[a-zA-Z0-9]"  // letra ou número
"[a-e]"        // a, b, c, d, e
```

Exemplo da aula: `"[a-fA-F]"` pega as letras de A a F, maiúsculas e minúsculas — as mesmas letras usadas em números **hexadecimais**.

---

## 3. Exercício da aula — números hexadecimais

### Contexto

Hexadecimal é um sistema numérico de base 16. Os dígitos são `0–9` e `A–F`. Em Java, um literal hexadecimal começa com `0x` ou `0X`:

```java
int numero = 0x59F86A;      // hexadecimal
System.out.println(numero); // 5871722 (valor decimal)
```

Se tentar `59F86A` sem o `0x`, é erro de compilação. E `0xG1` também (G não é dígito hexa).

### Regra de um hexadecimal válido

```text
1) começa com 0
2) seguido de x ou X
3) seguido de dígitos 0-9 e/ou letras a-f / A-F
```

### Construindo a regex passo a passo

```text
0              → o zero inicial
[xX]           → x minúsculo ou maiúsculo
[0-9a-fA-F]    → um dígito hexadecimal
```

Em Java:

```java
String regex = "0[xX][0-9a-fA-F]";
```

Teste com o texto da aula:

```java
String texto = "12 0x 0X 0xFFABC 0x109 0x1";
Matcher m = Pattern.compile("0[xX][0-9a-fA-F]").matcher(texto);
while (m.find()) {
    System.out.println(m.start() + " " + m.group());
}
```

Resultado: ele encontra só **um** dígito depois do `0x`. Em `0xFFABC` achou só `0xF`. Para pegar o número inteiro precisamos de **quantificadores** — próxima aula.

---

## 4. Atenção: `-` dentro de colchetes

O hífen só é "intervalo" quando está **entre** dois caracteres. Para ser o caractere hífen, coloque no começo, no fim ou escape:

```java
"[a-z-]"    // letras minúsculas e hífen
"[\\-a-z]"
```

## O que você precisa dominar (Aula 133)

- Conjunto `[abc]` = um caractere dentre os listados.
- Intervalo `[a-z]`, `[0-9]`, `[a-zA-Z]`.
- Regex diferencia maiúsculas de minúsculas.
- Literal hexadecimal em Java (`0x...`).
- Montar a regex do hexa passo a passo.

---

# Aula 134 — Regex pt 04 — Quantificadores pt 01

## 1. O que são quantificadores?

São metacaracteres que dizem **quantas vezes** o elemento anterior pode se repetir.

| Quantificador | Significa |
|---|---|
| `?` | **zero ou uma** vez |
| `*` | **zero ou mais** vezes |
| `+` | **uma ou mais** vezes |
| `{n}` | exatamente **n** vezes |
| `{n,}` | **pelo menos n** vezes |
| `{n,m}` | de **n até m** vezes |

Outros metacaracteres que aparecem com eles:

| Meta | Significado |
|---|---|
| `( )` | **agrupamento** (trata vários elementos como um) |
| `\|` | **ou** (alternativa) |
| `$` | **fim da linha/texto** |
| `.` | qualquer caractere |

### Exemplos rápidos

```text
a?      → "" ou "a"
a*      → "", "a", "aa", "aaa"...
a+      → "a", "aa", "aaa"...
a{3}    → "aaa"
a{2,4}  → "aa", "aaa" ou "aaaa"
\d{3}   → três dígitos
(ab)+   → "ab", "abab", "ababab"...
(Java|Kotlin) → "Java" ou "Kotlin"
```

⚠️ `*` e `+` aplicam ao elemento **imediatamente anterior**. `ab+` significa `a` seguido de um ou mais `b`. Para repetir `ab` use `(ab)+`.

---

## 2. Continuando o exercício dos hexadecimais

Texto de exemplo (como na aula):

```java
String texto = "12 0x 0X 0xFFABC 0x109 0x1 0xG1 0x23";
```

Regra: um hexa válido é `0`, `x/X`, **um ou mais** dígitos hexa, e deve ser um "token completo" (terminar em espaço ou no final do texto).

### Passo 1 — Repetir os dígitos hexa

```java
"0[xX][0-9a-fA-F]+"
```

Com o `+`, `0xFFABC` agora é encontrado inteiro. 

### Por que não usamos `*`?

`0[xX][0-9a-fA-F]*` aceitaria `0x` sozinho (zero dígitos) — que **não** é um hexa válido. Foi o erro que o instrutor mostrou na aula: com `*` apareceram resultados inválidos.

### Passo 2 — Delimitar o final

Mesmo com `+`, a regex ainda pode encontrar "pedaços" de coisas inválidas. Em `0xG1`... o `0x` é seguido de `G` (não é hexa), então não casa — ok. Mas um valor como `0x1G` casaria `0x1` e ignoraria `G`, o que é errado. Por isso exigimos que **depois** venha espaço ou fim do texto:

```java
"0[xX][0-9a-fA-F]+\\s"
```

Mas aí o último valor do texto (sem espaço depois) é perdido. Solução: aceitar espaço **ou** fim de linha, usando `|` dentro de um grupo:

```java
"0[xX][0-9a-fA-F]+(\\s|$)"
```

Leitura em português:

```text
0               → começa com zero
[xX]            → x minúsculo ou maiúsculo
[0-9a-fA-F]+    → um ou mais dígitos hexadecimais
(\s|$)          → seguido de espaço em branco OU fim do texto
```

Esta é a expressão final da aula. Ela é longa e à primeira vista assustadora — por isso o instrutor diz que você vai voltar nessa aula muitas vezes.

### Código completo

```java
String regex = "0[xX][0-9a-fA-F]+(\\s|$)";
String texto = "12 0x 0X 0xFFABC 0x109 0x1 0xG1 0x23";

Matcher m = Pattern.compile(regex).matcher(texto);
while (m.find()) {
    System.out.println(m.start() + " " + m.group().trim());
}
```

Observação: o `group()` inclui o espaço final, por isso o `trim()` na impressão.

---

## 3. Dica de estudo

A forma mais fácil de aprender regex é **construir passo a passo**, testando a cada ajuste. Sites como o regex101.com mostram, ao passar o mouse na expressão, o que cada parte faz (aula 136).

## O que você precisa dominar (Aula 134)

- Quantificadores `? * + {n} {n,} {n,m}`.
- Diferença prática entre `*` (zero ou mais) e `+` (um ou mais).
- Agrupamento `( )` e alternativa `|`.
- `$` como fim de linha.
- Construção incremental da regex do hexadecimal.

---

# Aula 135 — Regex pt 05 — Quantificadores pt 02 (e-mails)

## 1. O curinga `.`

O ponto (`.`) casa com **qualquer** caractere:

```text
1.3   → "123", "1a3", "1@3", "1 3"
```

Para casar um ponto **literal** (por exemplo, o ponto de `gmail.com`) é preciso **escapá-lo**:

```java
"\\."      // regex \. → ponto literal
```

---

## 2. Exercício da aula: extrair e-mails de um texto

Texto de exemplo:

```text
"fulano@hotmail.com, 123@gmail.com, jose#gmail.com, @gmail.com, teste@gmail.com.br"
```

Queremos achar apenas os e-mails **válidos**.

### Passo a passo

**Parte 1 — antes do `@` (usuário)**

Pode ter letras minúsculas, maiúsculas, dígitos, ponto, hífen e underscore. Precisa de **pelo menos um** caractere:

```java
"[a-zA-Z0-9._-]+"
```

> Dentro de `[ ]` o ponto é literal (não precisa escapar).

**Parte 2 — o arroba**

```java
"@"
```

**Parte 3 — provedor**

Só letras minúsculas, uma ou mais vezes:

```java
"[a-z]+"
```

**Parte 4 — domínio**

Um ponto literal + letras, e isso pode se repetir (para `.com`, `.com.br`, `.co.uk`):

```java
"(\\.[a-z]+)+"
```

### Regex final

```java
String regex = "[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)+";
```

### Resultado esperado

```text
fulano@hotmail.com            ✔
123@gmail.com                 ✔
jose#gmail.com                ✘ (# não é arroba; não casa inteiro)
@gmail.com                    ✘ (falta usuário)
teste@gmail.com.br            ✔
```

### O erro que o instrutor mostrou

Com `\\.[a-z]+` **sem o `+` externo**, o endereço `teste@gmail.com.br` era cortado em `teste@gmail.com`, porque só um `.xxx` era aceito. Com `(\\.[a-z]+)+` o grupo pode repetir.

---

## 3. `find()` x `matches()` (validar de verdade)

O que fizemos até aqui foi **encontrar** e-mails dentro de um texto grande. Para **validar** se uma String *inteira* é um e-mail, use `matches`:

```java
Pattern.matches(regex, "teste@gmail.com.br");   // true
Pattern.matches(regex, "@gmail.com");           // false
Pattern.matches(regex, "teste@gmail.com abc");  // false (sobrou texto)
```

Ou:

```java
"teste@gmail.com".matches(regex);   // o método matches da própria String
```

`matches` exige que **a String toda** case com o padrão.

---

## 4. `String.split` com regex

A classe `String` também aceita regex em alguns métodos:

```java
String[] partes = texto.split(",");        // quebra por vírgula
String[] partes2 = texto.split("\\s*,\\s*"); // vírgula com espaços opcionais ao redor
```

Para imprimir um array com facilidade:

```java
System.out.println(Arrays.toString(partes));
```

Se a divisão deixou espaços, use `trim()` em cada posição:

```java
for (String p : partes) {
    System.out.println(p.trim());
}
```

Esse é o conceito de **delimitador**: o que separa os pedaços (tokens). Quebrar o texto por vírgula e validar cada pedaço com `matches` é a forma correta de validar uma lista de e-mails.

## O que você precisa dominar (Aula 135)

- `.` é curinga; `\\.` é ponto literal.
- Montar uma regex de e-mail por partes.
- Quantificador aplicado a **grupo**: `(\\.[a-z]+)+`.
- `find()` (procurar) x `matches()` (validar tudo).
- `String.split(regex)`.

---

# Aula 136 — Regex pt 06 — Âncora `^` e negação

## 1. Ferramenta de apoio

O instrutor recomenda um site (como o **regex101.com**) onde você digita a expressão e um texto de teste e vê, em tempo real, o que casa e **o que cada parte da expressão significa** (explicação ao passar o mouse). É a melhor forma de aprender sem ficar compilando Java.

---

## 2. O acento circunflexo `^` tem dois significados

### Significado 1 — **Início da linha** (âncora)

Fora de colchetes, `^` casa com o **começo** do texto (ou da linha):

```java
Pattern.compile("^\\w+")    // a primeira palavra do texto
```

Com a flag de múltiplas linhas (`Pattern.MULTILINE`), `^` casa com o começo de **cada linha**:

```java
Pattern p = Pattern.compile("^\\w+", Pattern.MULTILINE);
```

Resultado: a primeira palavra de cada linha.

### Significado 2 — **Negação** (dentro de colchetes)

Se `^` é o **primeiro** caractere dentro de `[ ]`, ele **nega** o conjunto:

```java
"[abc]"     // a, b ou c
"[^abc]"    // qualquer caractere que NÃO seja a, b nem c
```

Cuidado com maiúsculas: `[^abc]` ainda casa com `A`, `B` e `C` porque a regex diferencia maiúsculas e minúsculas.

---

## 3. Resumo geral dos metacaracteres vistos no bloco

| Símbolo | Significado |
|---|---|
| `\d` `\D` | dígito / não dígito |
| `\s` `\S` | espaço / não espaço |
| `\w` `\W` | caractere de palavra / não |
| `.` | qualquer caractere |
| `[abc]` | um dentre a, b ou c |
| `[a-z]` | intervalo |
| `[^abc]` | negação |
| `?` `*` `+` | 0 ou 1 / 0 ou mais / 1 ou mais |
| `{n,m}` | de n a m vezes |
| `( )` | grupo |
| `\|` | ou |
| `^` | início da linha |
| `$` | fim da linha |
| `\\.` | ponto literal |

## 4. Tabela de decisão

```text
Quero achar algo dentro de um texto grande?   → find() em while
Quero saber se a String inteira é válida?     → matches()
Quero separar o texto?                        → split(regex)
Quero apenas "começa com"?                    → lookingAt() ou ^
```

## O que você precisa dominar (Aula 136)

- `^` como início de linha e como negação em `[ ]`.
- `Pattern.MULTILINE`.
- Usar uma ferramenta online para testar regex.
- Ter a cola dos metacaracteres em mãos.

---

# Aula 137 — `Scanner`: Tokens e Delimitadores

## 1. Token e delimitador

- **Delimitador**: o separador (vírgula, espaço, ponto-e-vírgula...).
- **Token**: cada pedaço que sobra depois de separar.

Exemplo:

```text
texto:       "William, Suane, Anna"
delimitador: ","
tokens:      "William", " Suane", " Anna"
```

---

## 2. Com `String.split`

```java
String texto = "William, Suane, Anna";
String[] nomes = texto.split(",");

for (String nome : nomes) {
    System.out.println(nome.trim());   // trim remove espaços ao redor
}
```

O delimitador do `split` é uma **regex**: pode ser um espaço, um dígito, etc. Mas `split` sempre devolve **`String`** — se um token for número ou booleano, você ainda precisa converter à mão.

---

## 3. Com `Scanner` e delimitador personalizado

O `Scanner` (que você conhece do teclado) também lê de uma **String**, e pode converter cada token no tipo certo.

```java
import java.util.Scanner;

String texto = "William, 20, true, 3.5, Suane";
Scanner scanner = new Scanner(texto);
scanner.useDelimiter(",\\s*");    // delimitador: vírgula + espaços opcionais (regex)

while (scanner.hasNext()) {
    if (scanner.hasNextInt()) {
        int i = scanner.nextInt();
        System.out.println("int: " + i);
    } else if (scanner.hasNextBoolean()) {
        boolean b = scanner.nextBoolean();
        System.out.println("boolean: " + b);
    } else if (scanner.hasNextDouble()) {
        double d = scanner.nextDouble();
        System.out.println("double: " + d);
    } else {
        String s = scanner.next();
        System.out.println("String: " + s);
    }
}
scanner.close();
```

Saída:

```text
String: William
int: 20
boolean: true
double: 3.5
String: Suane
```

### Como funciona o "dois ponteiros"

O instrutor explica com uma imagem mental:

```text
hasNextXxx()  → olha o próximo token SEM consumir (só verifica o tipo)
nextXxx()     → consome e devolve o token
```

Sempre **pergunte antes** (`hasNextInt`) e **só depois** consuma (`nextInt`). Se você chamar `nextInt()` num token que não é número, recebe `InputMismatchException`.

### Ordem importa

`hasNextInt()` vem antes de `hasNextDouble()` porque todo `int` também é um `double` válido; se testar `double` primeiro, `20` seria lido como `20.0`.

---

## 4. Principais métodos

| Método | Função |
|---|---|
| `useDelimiter(regex)` | define o separador |
| `hasNext()` | existe próximo token? |
| `next()` | próximo token (String) |
| `hasNextInt()` / `nextInt()` | inteiro |
| `hasNextDouble()` / `nextDouble()` | decimal |
| `hasNextBoolean()` / `nextBoolean()` | booleano |
| `hasNextLine()` / `nextLine()` | linha inteira |
| `close()` | libera o recurso |

## 5. `split` x `Scanner`

| `String.split` | `Scanner` |
|---|---|
| Quebra tudo de uma vez em array | Lê um token por vez |
| Só devolve `String` | Converte para `int`, `double`, `boolean`... |
| Simples e rápido | Mais flexível para dados mistos |

## O que você precisa dominar (Aula 137)

- O que são token e delimitador.
- `split` devolve `String[]`.
- `Scanner(String)` + `useDelimiter`.
- Padrão `while (hasNext())` + `hasNextXxx()` / `nextXxx()`.
- Cuidado com `InputMismatchException`.

---

# Mapa mental do bloco

```text
Classes utilitárias — mensagens, padrões e tokens
├── ResourceBundle
│   ├── messages_pt_BR.properties / messages_en_US.properties
│   ├── getBundle(base, Locale) + getString(chave)
│   └── Fallback: pedido → padrão da JVM → base
├── Regex
│   ├── Pattern.compile + matcher + find/start/group
│   ├── Metacaracteres: \d \s \w . ^ $
│   ├── Range: [abc] [a-z] [^abc]
│   ├── Quantificadores: ? * + {n,m}
│   └── Grupos e alternativa: ( ) |
└── Scanner com delimitador
    └── useDelimiter + hasNextXxx / nextXxx
```

# Cola das expressões da aula

| Objetivo | Regex em Java |
|---|---|
| Um dígito | `"\\d"` |
| Um ou mais dígitos | `"\\d+"` |
| Hexadecimal | `"0[xX][0-9a-fA-F]+(\\s\|$)"` |
| E-mail simples | `"[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)+"` |
| Primeira palavra de cada linha | `"^\\w+"` (com `Pattern.MULTILINE`) |
| Tudo que não é a, b ou c | `"[^abc]"` |
