# Bloco 11 — Classes Utilitárias: Wrappers e Strings

## Aulas 106 a 111

Este bloco inicia a parte de **Classes Utilitárias** da Maratona Java.

Nas aulas anteriores, o foco estava principalmente em orientação a objetos e exceções. Agora o curso começa a trabalhar com classes que já fazem parte da biblioteca do Java e que aparecem constantemente no desenvolvimento.

As aulas deste bloco são:

```text
106 — Wrappers pt 01
107 — Wrappers pt 02
108 — Strings pt 01
109 — Strings pt 02
110 — Strings pt 03 — Desempenho
111 — Strings pt 04 — StringBuilder
```

A sequência tem uma lógica:

```text
tipos primitivos
      ↓
Wrappers
      ↓
autoboxing / unboxing
      ↓
String
      ↓
imutabilidade
      ↓
String Pool
      ↓
operações com String
      ↓
desempenho
      ↓
StringBuilder
```

---

# Aula 106 — Utility Classes — Wrappers pt 01

## 1. O problema dos tipos primitivos

Java possui tipos primitivos:

```java
byte
short
int
long
float
double
char
boolean
```

Eles não são objetos.

Por exemplo:

```java
int idade = 20;
```

`idade` é uma variável primitiva que armazena um valor.

Ela não é uma instância de uma classe.

Isso cria uma situação interessante porque Java é uma linguagem orientada a objetos e, em várias partes da API, precisamos trabalhar com **objetos**, e não diretamente com tipos primitivos.

É aí que entram os **Wrappers**.

---

## 2. O que são Wrappers?

Wrapper significa, literalmente, algo que “envolve”.

A ideia é criar uma classe capaz de representar um tipo primitivo como um objeto.

A relação principal é:

| Primitivo | Wrapper |
|---|---|
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

Observe que os nomes normalmente seguem o nome do primitivo com a primeira letra maiúscula, mas existem duas exceções importantes:

```text
int     → Integer
char    → Character
```

Não existe:

```java
Int
Char
```

---

## 3. Primitivo x Wrapper

Compare:

```java
int numero = 10;
```

com:

```java
Integer numero = 10;
```

No primeiro caso:

```text
int
↓
primitivo
```

No segundo:

```text
Integer
↓
objeto
```

Isso é fundamental.

`Integer` não é simplesmente “um `int` com letra maiúscula”.

É uma **classe**.

Por isso ela pode possuir métodos e participar das regras de objetos e herança.

---

## 4. Wrappers e herança

As classes numéricas, como:

```text
Byte
Short
Integer
Long
Float
Double
```

possuem uma relação de herança com `Number`.

De forma simplificada:

```text
Object
   ↓
Number
   ↓
Integer
```

Isso significa que estamos realmente trabalhando com objetos.

Por exemplo, uma referência:

```java
Number numero = Integer.valueOf(10);
```

é possível porque:

```text
Integer é um Number
```

Essa regra é diferente da conversão entre tipos primitivos.

---

## 5. Cuidado: agora estamos falando de objetos

Esse ponto é muito importante.

Quando temos:

```java
int a = 10;
long b = a;
```

estamos trabalhando com tipos primitivos.

As regras de conversão são as regras dos tipos primitivos.

Quando temos:

```java
Integer a = 10;
Number b = a;
```

estamos trabalhando com objetos.

Aqui entra a relação de herança/polimorfismo:

```text
Integer
   ↓
Number
```

Portanto, não misture mentalmente:

```text
conversão de primitivos
```

com:

```text
polimorfismo entre Wrappers
```

---

## 6. Por que os Wrappers existem?

A aula apresenta dois motivos especialmente importantes.

### Primeiro motivo: trabalhar com objetos

Transformar um valor primitivo em objeto permite que ele seja utilizado em contextos nos quais precisamos de referências.

### Segundo motivo: coleções

Mais adiante no curso você vai trabalhar bastante com:

```java
ArrayList
```

Coleções trabalham com objetos.

Você não cria:

```java
List<int>
```

O correto é:

```java
List<Integer>
```

Porque:

```text
int
↓
primitivo

Integer
↓
objeto
```

Esse é um dos motivos pelos quais Wrappers são indispensáveis para quem trabalha com coleções em Java.

---

## 7. Wrappers possuem métodos

Um primitivo:

```java
int numero = 10;
```

não possui métodos próprios como um objeto.

Já:

```java
Integer numero = 10;
```

é um objeto da classe `Integer`.

A classe fornece métodos utilitários para trabalhar com o valor.

Esse padrão se repete nos outros Wrappers.

---

## 8. Um cuidado importante com valores literais

A aula também chama atenção para a diferença entre o valor primitivo e o objeto Wrapper.

Por exemplo:

```java
Integer numero = 10;
```

Aqui o Java pode fazer uma conversão automática.

Isso será explicado na próxima aula através de:

```text
autoboxing
unboxing
```

---

## O que você precisa dominar

Ao terminar esta aula, você deve saber:

- o que é um tipo primitivo;
- o que é um Wrapper;
- qual Wrapper corresponde a cada primitivo;
- por que `int` vira `Integer`;
- por que `char` vira `Character`;
- que Wrappers são objetos;
- que Wrappers numéricos possuem relação com `Number`;
- por que coleções utilizam Wrappers em vez de primitivos.

---

# Aula 107 — Classes Utilitárias — Wrappers pt 02

Agora a aula continua os Wrappers e apresenta duas ideias fundamentais:

```text
Autoboxing
Unboxing
```

---

## 1. Autoboxing

Autoboxing é quando o Java converte automaticamente um tipo primitivo em seu Wrapper.

Exemplo:

```java
int numero = 10;

Integer valor = numero;
```

Temos:

```text
int
 ↓
Integer
```

Você não precisou escrever manualmente a conversão.

O Java fez isso automaticamente.

Mentalidade:

```text
primitivo
   ↓
autoboxing
   ↓
Wrapper
```

---

## 2. Unboxing

É o contrário.

Temos um objeto Wrapper:

```java
Integer valor = 10;
```

e queremos um primitivo:

```java
int numero = valor;
```

O Java converte automaticamente:

```text
Integer
   ↓
unboxing
   ↓
int
```

Portanto:

```text
Autoboxing
primitivo → Wrapper

Unboxing
Wrapper → primitivo
```

Essa diferença precisa ficar muito clara.

---

## 3. Por que isso facilita o código?

Sem autoboxing e unboxing, teríamos que fazer conversões manualmente em vários lugares.

O Java automatiza essas operações quando a conversão é compatível.

Isso torna o código muito mais simples.

Por exemplo:

```java
Integer numero = 10;
int resultado = numero;
```

O Java consegue entender que:

```text
10
```

precisa ser colocado dentro de um `Integer` e depois retirado para voltar a um `int`.

---

## 4. Métodos `valueOf`

Os Wrappers também oferecem métodos estáticos para criar objetos a partir de valores.

Exemplo:

```java
Integer numero = Integer.valueOf(10);
```

A ideia é:

```text
valueOf
↓
obtém/cria um objeto Wrapper a partir de um valor
```

Esse padrão existe em vários Wrappers.

---

## 5. Converter String para Wrapper

Uma aplicação frequentemente recebe valores como `String`.

Por exemplo:

```java
String valor = "10";
```

Mas talvez você precise de:

```java
Integer
```

Os Wrappers oferecem métodos para realizar essas conversões.

Exemplo:

```java
Integer numero = Integer.valueOf("10");
```

Agora:

```text
"10"
↓
Integer
```

Isso é muito útil quando uma informação chega como texto e precisa ser utilizada como número.

---

## 6. Boolean

O `Boolean` também oferece métodos utilitários.

Por exemplo, podemos trabalhar com valores textuais para obter um booleano.

A aula chama atenção para o comportamento de métodos como:

```java
Boolean.parseBoolean(...)
```

Quando o valor não representa `true` de acordo com a regra do método, o resultado pode ser:

```java
false
```

Isso é diferente de lançar automaticamente uma exceção para qualquer texto inválido.

Por isso é importante conhecer o comportamento do método que você está utilizando.

---

## 7. Character

A classe:

```java
Character
```

é especialmente interessante porque possui vários métodos utilitários para trabalhar com caracteres.

Por exemplo:

```java
Character.isDigit(...)
```

pode verificar se determinado caractere representa um dígito.

Também existem métodos para trabalhar com:

```text
maiúsculas
minúsculas
letras
dígitos
```

A ideia da aula é mostrar que os Wrappers não servem apenas para “guardar” o primitivo.

Eles também fornecem funcionalidades relacionadas ao tipo.

---

## 8. Por que conhecer a API das classes é importante?

Uma das mensagens importantes da aula é que você não precisa decorar todos os métodos.

Você precisa aprender a:

```text
identificar a classe
       ↓
procurar o método adequado
       ↓
entender o nome e os parâmetros
       ↓
utilizar a documentação
```

Em desenvolvimento profissional, é normal consultar a documentação.

O importante é saber o que procurar.

---

## O que você precisa dominar

- Autoboxing.
- Unboxing.
- `valueOf`.
- Conversões entre `String` e Wrappers.
- Métodos utilitários dos Wrappers.
- `Boolean`.
- `Character`.
- Diferença entre primitivo e objeto durante essas operações.

---

# Aula 108 — Utility Classes — Strings pt 01

Agora começa um assunto extremamente importante:

```java
String
```

---

# 1. String é um objeto

Quando escrevemos:

```java
String nome = "João";
```

`nome` é uma referência para um objeto `String`.

Isso é importante porque `String` não é um tipo primitivo.

```text
String
↓
classe
↓
objeto
```

---

# 2. Strings são imutáveis

Esse é provavelmente o conceito mais importante desta aula.

`String` é **imutável**.

Isso significa:

> Depois que um objeto `String` foi criado, seu conteúdo não é alterado.

Imagine:

```java
String nome = "Joao";
```

Agora:

```java
nome.concat(" Silva");
```

Você pode imaginar que o objeto original foi modificado.

Mas não foi.

Uma nova `String` é produzida.

Para guardar o novo resultado:

```java
nome = nome.concat(" Silva");
```

Agora a variável `nome` passa a apontar para a nova String.

---

## 3. O que acontece na memória?

Imagine:

```java
String nome = "Joao";
```

Temos:

```text
nome
 ↓
"Joao"
```

Depois:

```java
nome.concat(" Silva");
```

conceitualmente:

```text
nome
 ↓
"Joao"

nova String
 ↓
"Joao Silva"
```

Se você não guardar o retorno:

```java
nome.concat(" Silva");
```

a variável `nome` continua apontando para:

```text
"Joao"
```

A nova String pode ficar sem nenhuma referência útil.

---

# 4. String Pool

O Java possui uma área especial chamada:

```text
String Pool
```

também chamada de pool de Strings.

Ela existe para permitir reutilização de Strings imutáveis.

Por exemplo:

```java
String nome1 = "Joao";
String nome2 = "Joao";
```

Como Strings são imutáveis, o Java pode reutilizar o mesmo objeto.

Conceitualmente:

```text
nome1 ──┐
        ├──> "Joao"
nome2 ──┘
```

Isso evita criar objetos iguais desnecessariamente.

---

# 5. String literal

Quando escrevemos:

```java
String nome = "Joao";
```

estamos utilizando uma **String literal**.

O Java pode procurar esse valor no String Pool.

Se já existir uma String igual no pool, pode reutilizar a referência.

Se não existir, o Java cria o objeto necessário.

---

# 6. `==` em Strings

Esse é um erro muito comum.

Quando fazemos:

```java
nome1 == nome2
```

estamos comparando:

```text
referências
```

e não o conteúdo.

Por exemplo:

```java
String nome1 = "Joao";
String nome2 = "Joao";
```

pode resultar em:

```java
nome1 == nome2
```

sendo `true`, porque ambas podem apontar para o mesmo objeto do String Pool.

Mas isso não significa que `==` seja a forma correta de comparar conteúdo de Strings.

---

# 7. `equals()`

Para comparar o conteúdo:

```java
nome1.equals(nome2)
```

O objetivo é verificar se os valores são iguais.

Mentalidade:

```text
== 
↓
mesma referência?

equals()
↓
mesmo conteúdo?
```

Essa diferença é fundamental para Java.

---

# 8. Criar String com `new`

Também podemos fazer:

```java
String nome = new String("Joao");
```

Agora estamos explicitamente criando um objeto.

Mesmo que exista `"Joao"` no String Pool, a referência criada com `new` representa outro objeto.

Por isso é possível ter:

```java
String nome1 = "Joao";
String nome2 = new String("Joao");
```

e:

```java
nome1 == nome2
```

ser `false`.

Enquanto:

```java
nome1.equals(nome2)
```

pode ser `true`, porque o conteúdo é igual.

---

# 9. `intern()`

A aula também apresenta a ideia de obter a representação canônica de uma String através do pool.

O método:

```java
intern()
```

pode fazer uma referência apontar para a representação correspondente no String Pool.

O importante para esta etapa é entender o conceito:

```text
String criada fora do pool
        ↓
intern()
        ↓
representação do String Pool
```

Não é algo que você precise sair usando em qualquer código. O objetivo aqui é compreender a relação entre objetos String e o pool.

---

# O que você precisa dominar

- `String` é uma classe.
- String é objeto.
- String é imutável.
- Operações que parecem modificar uma String normalmente produzem outra String.
- O Java possui String Pool.
- Literais podem ser armazenadas/reutilizadas no pool.
- `==` compara referências.
- `equals()` compara conteúdo.
- `new String(...)` cria um objeto explicitamente.
- `intern()` está relacionado à representação no String Pool.

---

# Aula 109 — Utility Classes — Strings pt 02

Agora o foco passa para os principais métodos da classe `String`.

---

## 1. `charAt()`

Uma String pode ser entendida como uma sequência de caracteres.

Exemplo:

```java
String nome = "Lucas";
```

Podemos pensar:

```text
L u c a s
0 1 2 3 4
```

O índice começa em:

```text
0
```

Então:

```java
nome.charAt(0);
```

retorna:

```text
'L'
```

---

## 2. Índice inválido

Se você tentar acessar uma posição que não existe:

```java
nome.charAt(10);
```

o Java lança uma exceção relacionada ao índice da String.

Isso conecta este bloco ao bloco anterior de exceções.

É um exemplo prático de:

```text
operação inválida
↓
exceção em tempo de execução
```

---

# 3. `length()`

Para descobrir o tamanho da String:

```java
nome.length();
```

Se:

```java
String nome = "Lucas";
```

então:

```java
nome.length();
```

retorna:

```text
5
```

Observe a diferença em relação aos arrays.

Array:

```java
array.length
```

String:

```java
string.length()
```

No array, `length` é um atributo.

Na String, `length()` é um método.

---

# 4. `replace()`

O método:

```java
replace()
```

permite substituir caracteres ou sequências.

Por exemplo:

```java
String nome = "banana";
String novo = nome.replace('a', 'o');
```

A ideia é:

```text
banana
 ↓
bonono
```

Mas lembre novamente:

```text
String é imutável
```

Portanto, `replace()` não altera o objeto original.

Ele retorna outra String.

---

# 5. `toLowerCase()`

Converte a String para minúsculas.

```java
String nome = "JAVA";

String resultado = nome.toLowerCase();
```

Resultado:

```text
java
```

---

# 6. `toUpperCase()`

Faz o contrário:

```java
String nome = "java";

String resultado = nome.toUpperCase();
```

Resultado:

```text
JAVA
```

Esses métodos são muito úteis para normalização de dados.

Por exemplo, quando o usuário pode digitar:

```text
Java
JAVA
java
JaVa
```

Você pode transformar tudo para um padrão antes de comparar.

---

# 7. `substring()`

Um dos métodos mais importantes da aula.

Ele permite obter uma parte da String.

Imagine:

```java
String texto = "012345";
```

Índices:

```text
0 1 2 3 4 5
0 1 2 3 4 5
```

Com:

```java
texto.substring(1, 3);
```

o índice final:

```text
3
```

é **exclusivo**.

Portanto, são utilizados:

```text
1
2
```

Resultado:

```text
"12"
```

---

## 8. Regra do índice final

Essa regra precisa ficar muito clara:

```java
substring(inicio, fim)
```

significa:

```text
começa em inicio
termina antes de fim
```

Ou seja:

```text
[início, fim)
```

O início entra.

O fim não entra.

---

## 9. `substring(inicio)`

Existe uma sobrecarga:

```java
substring(int inicio)
```

Nesse caso, você informa apenas onde começa.

A String é retornada:

```text
do índice informado
até o final
```

Exemplo conceitual:

```java
texto.substring(3);
```

significa:

```text
posição 3
↓
até o final
```

Isso é um bom exemplo de **sobrecarga de método**, conceito que você já estudou.

---

# 10. `trim()`

O método:

```java
trim()
```

remove espaços em branco no início e no final da String.

Exemplo:

```java
String nome = "   Lucas   ";

String resultado = nome.trim();
```

Resultado:

```text
"Lucas"
```

Ele não remove os espaços internos.

Por exemplo:

```text
"Lucas Silva"
```

continua:

```text
"Lucas Silva"
```

---

## 11. Uso prático do trim

Isso é muito útil quando dados vêm de usuários.

Imagine:

```text
"   leonardo   "
```

Se sua aplicação espera:

```text
"leonardo"
```

você pode normalizar:

```java
nome = nome.trim();
```

Mas é importante lembrar que a regra depende do domínio.

Às vezes os espaços fazem parte do valor válido.

---

# O que você precisa dominar

Você deve saber explicar e utilizar:

```java
charAt()
length()
replace()
toLowerCase()
toUpperCase()
substring()
trim()
```

E principalmente entender:

```text
índice começa em 0
substring final é exclusivo
String continua imutável
métodos retornam novas Strings quando precisam representar uma alteração
```

---

# Aula 110 — Classes Utilitárias — Strings pt 03 — Desempenho

Agora a aula muda o foco.

Não é mais apenas:

> “Como manipular uma String?”

A pergunta passa a ser:

> “O que acontece na memória quando fazemos muitas operações com Strings?”

---

# 1. O problema da imutabilidade

Lembre:

```java
String nome = "Joao";
```

Strings são imutáveis.

Agora imagine:

```java
nome = nome.concat(" Silva");
nome = nome.concat(" Junior");
nome = nome.concat(" Java");
```

Cada operação pode produzir uma nova String.

Conceitualmente:

```text
"Joao"
   ↓
"Joao Silva"
   ↓
"Joao Silva Junior"
   ↓
"Joao Silva Junior Java"
```

Ou seja, estamos criando novos objetos durante o processo.

---

# 2. Por que isso pode afetar desempenho?

Uma operação isolada:

```java
nome = nome.concat(" Silva");
```

normalmente não é um problema.

Mas imagine centenas ou milhares de concatenações.

Você pode acabar criando muitos objetos temporários.

Isso significa:

```text
mais objetos
↓
mais alocações
↓
mais trabalho para memória/GC
↓
possível impacto de desempenho
```

Por isso a aula chama atenção para o desempenho quando trabalhamos intensamente com Strings.

---

# 3. O problema não é “String é lenta”

Não interprete assim.

`String` é extremamente importante e muito utilizada.

O problema aparece principalmente quando você precisa fazer **muitas modificações sucessivas**.

Por exemplo:

```java
String texto = "";

for (...) {
    texto += algumaCoisa;
}
```

Nesse tipo de situação, precisamos pensar na estrutura mais adequada.

É justamente isso que leva ao:

```text
StringBuilder
```

---

# 4. String Pool e desempenho

O String Pool ajuda a reutilizar Strings literais iguais.

Mas ele não elimina o problema da imutabilidade.

Quando uma operação produz uma nova String, o Java ainda precisa representar o novo valor.

Por isso:

```text
String Pool
```

e:

```text
StringBuilder
```

resolvem problemas diferentes.

Não confunda:

```text
Pool
↓
reutilização de Strings

StringBuilder
↓
construção/modificação eficiente de texto
```

---

# 5. Quando começar a pensar em desempenho?

Não é necessário substituir toda String por StringBuilder.

Use String normalmente quando estiver representando um texto que não precisa sofrer inúmeras alterações.

Pense em `StringBuilder` principalmente quando existe construção incremental de texto.

Exemplo conceitual:

```text
começa vazio
↓
adiciona parte 1
↓
adiciona parte 2
↓
adiciona parte 3
↓
adiciona parte 4
```

Esse cenário combina muito mais com `StringBuilder`.

---

# O que você precisa dominar

- String é imutável.
- Operações que alteram o texto produzem novos objetos.
- Muitas concatenações podem gerar várias Strings intermediárias.
- Isso pode afetar desempenho.
- String Pool e StringBuilder resolvem problemas diferentes.
- `StringBuilder` será apresentado como alternativa para construção de texto.

---

# Aula 111 — Classes Utilitárias — Strings pt 04 — StringBuilder

Agora aparece a solução para o problema apresentado na aula anterior:

```java
StringBuilder
```

---

# 1. O que é StringBuilder?

`StringBuilder` é uma classe utilizada para construir e modificar uma sequência de caracteres.

A diferença fundamental é:

```text
String
↓
imutável

StringBuilder
↓
permite modificar o conteúdo do objeto
```

Isso faz diferença principalmente quando fazemos muitas alterações.

---

# 2. Criando um StringBuilder

Podemos criar:

```java
StringBuilder sb = new StringBuilder();
```

Nesse caso, ele começa com uma capacidade inicial.

A aula explica que a capacidade padrão é:

```text
16 caracteres
```

A capacidade não significa que o objeto só poderá armazenar 16 caracteres.

Quando necessário, ele aumenta a capacidade interna.

---

# 3. Capacidade x tamanho

Não confunda:

```text
length
```

com:

```text
capacity
```

A capacidade representa quanto espaço interno foi reservado.

O conteúdo representa quantos caracteres realmente estão sendo utilizados.

Quando a capacidade não é suficiente, o `StringBuilder` pode aumentar seu espaço interno.

---

# 4. StringBuilder não é String

Isso é importante.

Temos:

```java
StringBuilder sb = new StringBuilder();
```

O tipo do objeto é:

```text
StringBuilder
```

e não:

```text
String
```

Ele possui métodos próprios.

---

# 5. `append()`

O principal método apresentado é:

```java
append()
```

Ele adiciona conteúdo ao final.

Exemplo:

```java
StringBuilder sb = new StringBuilder();

sb.append("Java");
sb.append(" ");
sb.append("é");
sb.append(" ");
sb.append("legal");
```

O conteúdo vai sendo construído dentro do mesmo `StringBuilder`.

Mentalidade:

```text
"Java"
   ↓ append
"Java "
   ↓ append
"Java é"
   ↓ append
"Java é legal"
```

---

# 6. `append()` aceita vários tipos

Uma característica importante é que `append()` possui várias versões.

Podemos adicionar:

```text
String
int
boolean
char
objetos
```

entre outros valores.

O `StringBuilder` transforma esses valores em uma representação textual apropriada para adicioná-los à sequência.

Isso torna o método muito prático para construir textos.

---

# 7. `toString()`

Depois de construir o conteúdo, podemos transformar o `StringBuilder` em uma `String`:

```java
String resultado = sb.toString();
```

Agora:

```text
StringBuilder
      ↓
toString()
      ↓
String
```

Isso é importante porque muitas APIs esperam um `String`, não um `StringBuilder`.

---

# 8. `append()` x concatenação com `+`

Compare:

```java
String texto = "";

texto += "Java";
texto += " ";
texto += "é";
texto += " ";
texto += "legal";
```

com:

```java
StringBuilder sb = new StringBuilder();

sb.append("Java");
sb.append(" ");
sb.append("é");
sb.append(" ");
sb.append("legal");
```

Quando existe muita construção incremental de texto, `StringBuilder` pode evitar a criação repetida de Strings intermediárias e, por isso, é uma alternativa mais adequada.

---

# 9. `reverse()`

A aula também apresenta métodos que trabalham diretamente no conteúdo do `StringBuilder`.

Por exemplo:

```java
sb.reverse();
```

Ele inverte a sequência.

Se temos:

```text
Java
```

podemos obter:

```text
avaJ
```

A diferença importante é que o `StringBuilder` é mutável.

Então operações como essa podem alterar o conteúdo do próprio objeto.

---

# 10. `delete()`

Outro método apresentado é:

```java
delete()
```

Ele permite remover uma parte da sequência.

Exemplo conceitual:

```java
sb.delete(0, 3);
```

Assim como `substring()`, existe uma regra de índice final exclusivo.

Ou seja:

```text
delete(inicio, fim)
```

remove:

```text
inicio
até
fim - 1
```

---

# 11. Cuidado com o tipo de retorno dos métodos

Esse é um dos pontos mais importantes da aula.

Nem todo método do `StringBuilder` retorna um `StringBuilder`.

Por exemplo:

```java
sb.append("Java");
```

retorna um `StringBuilder`.

Já:

```java
sb.substring(0, 2);
```

retorna:

```text
String
```

Isso muda completamente o comportamento.

Portanto, quando estiver trabalhando com `StringBuilder`, observe sempre:

```text
qual é o tipo de retorno do método?
```

Porque:

```text
retorna StringBuilder
↓
pode continuar manipulando o mesmo builder

retorna String
↓
você recebeu uma String
```

---

# 12. StringBuilder e mutabilidade

Esse é o contraste que você deve guardar:

### String

```java
String texto = "Java";
```

Se fizermos uma operação que produz outro texto:

```text
objeto original permanece
```

Uma nova String é produzida.

### StringBuilder

```java
StringBuilder sb = new StringBuilder("Java");
```

Quando fazemos:

```java
sb.append("!");
```

o conteúdo do próprio objeto é modificado.

Mentalidade:

```text
String
↓
imutável
↓
nova String

StringBuilder
↓
mutável
↓
modifica o próprio objeto
```

---

# 13. Por que StringBuilder pode ser mais performático?

Porque ele foi projetado para esse cenário:

```text
construção incremental
```

Em vez de:

```text
String 1
↓
String 2
↓
String 3
↓
String 4
```

podemos trabalhar com um objeto mutável:

```text
StringBuilder
↓
append
↓
append
↓
append
↓
append
```

Isso reduz a quantidade de Strings intermediárias criadas durante a construção.

---

# 14. StringBuilder não significa que você deve abandonar String

Não.

O uso depende do problema.

Use:

```java
String
```

quando estiver trabalhando com textos que não precisam de alterações sucessivas.

Considere:

```java
StringBuilder
```

quando estiver construindo ou modificando texto repetidamente.

---

# Comparação final do bloco

## Wrapper

```text
primitivo
   ↓
Wrapper
```

Exemplo:

```java
int → Integer
```

Serve para trabalhar com o valor como objeto e fornece métodos utilitários.

---

## String

```text
String
↓
objeto imutável
↓
String Pool
```

Operações que produzem novos valores resultam em novas Strings.

---

## StringBuilder

```text
StringBuilder
↓
objeto mutável
↓
construção incremental
```

É adequado para operações repetidas de construção/modificação de texto.

---

# Modelo mental do bloco

```text
Tipos primitivos
       ↓
Wrappers
       ↓
Autoboxing / Unboxing
       ↓
Wrappers possuem métodos
       ↓
String
       ↓
String é imutável
       ↓
String Pool
       ↓
== x equals()
       ↓
Métodos de String
       ↓
problema de muitas concatenações
       ↓
StringBuilder
       ↓
append / reverse / delete
       ↓
toString()
```

---

# Erros que você não pode cometer

## 1. Confundir Wrapper com primitivo

```java
int
```

não é a mesma coisa que:

```java
Integer
```

---

## 2. Achar que autoboxing e unboxing são a mesma coisa

```text
autoboxing
int → Integer

unboxing
Integer → int
```

---

## 3. Usar `==` para comparar conteúdo de String

```java
nome1 == nome2
```

compara referências.

Para conteúdo:

```java
nome1.equals(nome2)
```

---

## 4. Achar que `concat()` altera a String

Não.

```java
nome.concat(" Silva");
```

não altera o objeto original.

Se quiser guardar o resultado:

```java
nome = nome.concat(" Silva");
```

---

## 5. Esquecer que índices começam em zero

```text
J a v a
0 1 2 3
```

---

## 6. Esquecer que o fim de `substring()` é exclusivo

```java
substring(1, 3)
```

pega:

```text
1
2
```

e não:

```text
1
2
3
```

---

## 7. Confundir `length` de array com `length()` de String

Array:

```java
array.length
```

String:

```java
texto.length()
```

---

## 8. Achar que StringBuilder é String

Não são a mesma classe.

```text
StringBuilder ≠ String
```

Para obter uma String:

```java
sb.toString();
```

---

## 9. Não verificar o retorno dos métodos do StringBuilder

Alguns métodos retornam:

```text
StringBuilder
```

outros retornam:

```text
String
```

Sempre confira o retorno.

---

# Checklist do bloco

Antes de avançar, você deve conseguir explicar:

- [ ] O que são Wrappers.
- [ ] O Wrapper correspondente a cada primitivo.
- [ ] Por que `int` utiliza `Integer`.
- [ ] O que é autoboxing.
- [ ] O que é unboxing.
- [ ] Para que servem `valueOf`.
- [ ] Como converter texto em Wrapper.
- [ ] O que a classe `Character` oferece.
- [ ] O que significa String ser imutável.
- [ ] O que é String Pool.
- [ ] Diferença entre `==` e `equals()`.
- [ ] O que acontece com `concat()`.
- [ ] Como `charAt()` funciona.
- [ ] Como `length()` funciona.
- [ ] Como `replace()` funciona.
- [ ] `toLowerCase()` e `toUpperCase()`.
- [ ] Como `substring()` funciona.
- [ ] Por que o índice final é exclusivo.
- [ ] Para que serve `trim()`.
- [ ] Por que muitas concatenações podem afetar desempenho.
- [ ] O que é `StringBuilder`.
- [ ] Para que serve `append()`.
- [ ] Para que serve `reverse()`.
- [ ] Para que serve `delete()`.
- [ ] Para que serve `toString()`.
- [ ] Diferença entre mutabilidade de `String` e `StringBuilder`.
- [ ] Por que `StringBuilder` é útil na construção incremental de texto.

---

# Como estudar o bloco

Não faça todas as aulas de uma vez.

A sequência continua sendo:

```text
Aula 106
   ↓
leia a explicação
   ↓
faça os exercícios
   ↓
mande o código
   ↓
correção
   ↓
Aula 107
   ↓
...
   ↓
Aula 111
   ↓
Desafio Integrador
```

O objetivo é chegar ao final entendendo a diferença entre:

```text
valor primitivo
      ↓
Wrapper
      ↓
objeto
```

e também:

```text
String
↓
imutável

StringBuilder
↓
mutável
```

Essas diferenças vão aparecer muitas vezes nos próximos assuntos do curso, principalmente quando começarmos a trabalhar com coleções, generics e estruturas de dados.

---

# Fonte

Conteúdo estruturado a partir das transcrições das aulas:

- 106 — Utility Classes — Wrappers pt 01
- 107 — Classes Utilitárias — Wrappers pt 02
- 108 — Utility Classes — Strings Part 1
- 109 — Utility Classes — Strings Part 2
- 110 — Classes Utilitárias — Strings pt 03 — Desempenho
- 111 — Utility Classes — Strings pt 04 — StringBuilder

A numeração e os títulos das aulas foram conferidos na listagem da playlist e as explicações foram estruturadas a partir das respectivas transcrições.
