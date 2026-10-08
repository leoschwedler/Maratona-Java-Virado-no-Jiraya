# Exercícios — Bloco 11: Wrappers e Strings

## Aulas 106 a 111

Este arquivo acompanha o README do Bloco 11.

A regra continua a mesma:

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

Os exercícios foram feitos para praticar **somente o conteúdo que aparece nas aulas correspondentes**, sem exigir assuntos posteriores.

> **Importante:** não quero que você simplesmente copie o exemplo da aula. Os exercícios mudam o contexto para verificar se você realmente entendeu o conceito.

---

# Aula 106 — Wrappers pt 01

## 🟢 Exercício 01 — Cadastro numérico com Wrappers

Crie uma classe `Produto`.

Ela deve possuir:

```text
nome
quantidade
preco
ativo
```

Utilize os **Wrappers correspondentes aos tipos primitivos** para representar os valores.

Crie um objeto no `main` e mostre todos os dados.

### Regras

- Não utilize `int`, `double` ou `boolean` nos atributos.
- Utilize os Wrappers.
- Não precisa criar getters/setters.
- O objetivo é praticar a identificação do Wrapper correto para cada primitivo.

### Você deve conseguir responder

Por que `quantidade` pode ser `Integer` em vez de `int`?

---

## 🟡 Exercício 02 — Wrapper e herança

Crie um método:

```java
public static void imprimirNumero(Number numero)
```

Esse método deve receber um `Number` e imprimir o valor recebido.

No `main`, faça chamadas passando objetos dos seguintes tipos:

```text
Integer
Double
Long
Float
```

### Objetivo

Perceber que os Wrappers numéricos possuem relação de herança com `Number`.

Você deve conseguir explicar:

```text
Integer → Number
Double  → Number
Long    → Number
```

### Restrição

Não transforme tudo manualmente para `int` ou `double` antes de chamar o método.

---

## 🔴 Exercício 03 — Descobrindo o tipo correto

Crie um programa que tenha variáveis Wrapper para:

```text
byte
short
int
long
float
double
char
boolean
```

Depois crie um método para receber cada uma delas.

O objetivo é montar um pequeno “catálogo” mostrando:

```text
Primitivo → Wrapper
```

Por exemplo:

```text
int → Integer
char → Character
```

### Desafio extra

Crie um método:

```java
public static void imprimirTipo(Number numero)
```

e tente utilizá-lo com os Wrappers numéricos.

Depois tente pensar:

> Por que `Character` e `Boolean` não entram nessa mesma hierarquia de `Number`?

Não precisa pesquisar ainda. Primeiro tente explicar com o que você aprendeu na aula.

---

# Aula 107 — Wrappers pt 02

## 🟢 Exercício 04 — Autoboxing e unboxing

Crie:

```java
int numero = 100;
```

Depois crie um `Integer` recebendo esse valor automaticamente.

Em seguida, faça o caminho contrário:

```text
Integer → int
```

Seu programa deve imprimir os dois valores.

### Objetivo

Praticar:

```text
autoboxing
int → Integer

unboxing
Integer → int
```

### Restrição

Não utilize métodos de conversão manual para realizar essas duas operações.

---

## 🟡 Exercício 05 — Conversor de texto

Crie um programa que possua:

```java
String quantidade = "25";
String preco = "49.90";
String ativo = "true";
```

Converta os valores para os respectivos Wrappers.

Resultado esperado conceitualmente:

```text
25    → Integer
49.90 → Double
true  → Boolean
```

Depois mostre os valores convertidos.

### Objetivo

Praticar os métodos estáticos de conversão dos Wrappers.

### Pergunta

Qual a diferença entre:

```text
String "25"
```

e:

```text
Integer 25
```

---

## 🔴 Exercício 06 — Analisador de caracteres

Crie um programa que receba uma sequência de caracteres, por exemplo:

```text
"Java2026"
```

Percorra cada caractere e utilize os métodos da classe `Character` para descobrir:

- quais caracteres são dígitos;
- quais são letras;
- quais são maiúsculos;
- quais são minúsculos.

O resultado deve ser algo semelhante a:

```text
J → letra / maiúscula
a → letra / minúscula
v → letra / minúscula
a → letra / minúscula
2 → dígito
0 → dígito
2 → dígito
6 → dígito
```

### Objetivo

Praticar os métodos utilitários de `Character` apresentados na aula.

### Restrição

Não faça comparações manuais como:

```java
if (c >= '0' && c <= '9')
```

Utilize os métodos da própria classe `Character`.

---

# Aula 108 — Strings pt 01

## 🟢 Exercício 07 — String Pool

Crie:

```java
String nome1 = "Java";
String nome2 = "Java";
```

Depois crie uma terceira String utilizando `new`.

Faça comparações utilizando:

```java
==
```

e:

```java
equals()
```

Antes de executar, escreva no comentário do código o que você espera que cada comparação produza.

Depois execute e confira.

### Objetivo

Entender a diferença entre:

```text
referência
```

e:

```text
conteúdo
```

---

## 🟡 Exercício 08 — Imutabilidade na prática

Comece com:

```java
String texto = "Java";
```

Execute:

```java
texto.concat(" é");
texto.concat(" muito");
texto.concat(" legal");
```

Depois imprima `texto`.

Em seguida, faça novamente, mas agora guardando o resultado:

```java
texto = texto.concat(...);
```

### Objetivo

Demonstrar na prática que `String` é imutável.

### Pergunta obrigatória

Explique por que a primeira versão não altera o valor armazenado na variável.

Não vale responder apenas:

> “Porque String é imutável.”

Explique **o que acontece com o objeto e com a referência**.

---

## 🔴 Exercício 09 — Investigação de referências

Crie três Strings:

```java
String a = "Java";
String b = "Java";
String c = new String("Java");
```

Faça um pequeno relatório no próprio código explicando o resultado de:

```java
a == b
a == c
b == c

a.equals(b)
a.equals(c)
b.equals(c)
```

Depois crie:

```java
String d = c.intern();
```

E compare novamente:

```java
a == d
```

### Objetivo

Juntar:

- String Pool;
- referências;
- `new String`;
- `equals()`;
- `==`;
- `intern()`.

Esse exercício deve ser explicado, não apenas executado.

---

# Aula 109 — Strings pt 02

## 🟢 Exercício 10 — Analisador de String

Crie:

```java
String texto = "Java";
```

Mostre:

1. tamanho da String;
2. primeiro caractere;
3. último caractere;
4. caractere da posição 2.

Utilize:

```java
length()
charAt()
```

### Atenção

Lembre-se:

```text
Java
0123
```

O índice começa em `0`.

---

## 🟡 Exercício 11 — Normalizador de texto

Receba:

```java
String nome = "   Leonardo   ";
```

Seu programa deve produzir:

```text
LEONARDO
```

Utilize os métodos estudados para:

1. remover espaços no início e no final;
2. transformar o texto para maiúsculo.

### Restrição

Faça o processo utilizando os métodos de `String` estudados na aula.

Não utilize bibliotecas externas.

---

## 🔴 Exercício 12 — Extrator de informação

Considere:

```java
String codigo = "PROD-2026-JAVA";
```

Utilize `substring()` para extrair:

```text
PROD
2026
JAVA
```

Depois faça o mesmo com:

```java
String codigo2 = "CLIENTE-9876-CURITIBA";
```

### Objetivo

Praticar:

- índices;
- `length()`;
- `substring(inicio, fim)`;
- índice final exclusivo.

### Desafio

Não use `split()`.

O exercício é especificamente para você praticar `substring()`.

---

# Aula 110 — Strings pt 03 — Desempenho

## 🟢 Exercício 13 — Medindo uma operação

Crie um método que execute uma quantidade determinada de concatenações de Strings.

Exemplo:

```text
1.000
10.000
50.000
```

Utilize:

```java
System.currentTimeMillis()
```

para medir aproximadamente quanto tempo a operação demora.

Estrutura:

```text
tempo inicial
↓
executa operação
↓
tempo final
↓
diferença
```

### Objetivo

Reproduzir a ideia da aula de medir o tempo de execução de operações com Strings.

---

## 🟡 Exercício 14 — Compare os tamanhos

Faça um programa que execute a mesma construção de texto com quantidades crescentes de concatenações:

```text
1.000
10.000
50.000
100.000
```

Registre os tempos obtidos.

Não precisa produzir um gráfico.

Monte uma saída organizada:

```text
1000 concatenações   → X ms
10000 concatenações  → X ms
50000 concatenações  → X ms
100000 concatenações → X ms
```

### Objetivo

Observar experimentalmente como o custo pode aumentar quando fazemos muitas operações de concatenação.

### Importante

Não espere que os números sejam exatamente iguais aos da aula.

O computador, JVM e condições de execução influenciam a medição.

O objetivo é observar o comportamento, não obter um número específico.

---

## 🔴 Exercício 15 — Encontre o problema de desempenho

Você recebeu este código:

```java
public static String gerarTexto(int quantidade) {
    String texto = "";

    for (int i = 0; i < quantidade; i++) {
        texto += i;
    }

    return texto;
}
```

Faça duas coisas:

### Parte 1

Teste o método com:

```text
1000
10000
50000
100000
```

e observe o comportamento.

### Parte 2

Explique:

> Por que esse código pode criar várias Strings durante a construção?

Você ainda não precisa implementar a solução com `StringBuilder`.

A intenção é diagnosticar o problema antes de corrigi-lo.

---

# Aula 111 — StringBuilder

## 🟢 Exercício 16 — Construção com append

Crie um `StringBuilder` vazio.

Utilize `append()` para construir:

```text
Meu nome é Leonardo e estou estudando Java.
```

Depois converta o resultado para `String` utilizando:

```java
toString()
```

### Objetivo

Praticar:

```text
StringBuilder
append()
toString()
```

---

## 🟡 Exercício 17 — Operações no StringBuilder

Crie:

```java
StringBuilder sb = new StringBuilder("0123456789");
```

Faça, em sequência:

1. adicione `"JAVA"` no final;
2. inverta o conteúdo;
3. crie outro resultado removendo um trecho específico com `delete()`.

Em cada etapa, imprima o conteúdo.

### Atenção

Preste atenção nos índices.

No `delete()`:

```java
delete(inicio, fim)
```

o índice final é exclusivo.

### Objetivo

Praticar a diferença entre métodos que alteram o próprio `StringBuilder` e métodos que retornam outro tipo.

---

## 🔴 Exercício 18 — Refatorando concatenação

Pegue o problema da Aula 110:

```java
public static String gerarTexto(int quantidade) {
    String texto = "";

    for (int i = 0; i < quantidade; i++) {
        texto += i;
    }

    return texto;
}
```

Crie uma segunda versão utilizando:

```java
StringBuilder
```

Depois compare as duas implementações utilizando:

```java
System.currentTimeMillis()
```

Teste com:

```text
1000
10000
50000
100000
```

Mostre:

```text
String       → X ms
StringBuilder → X ms
```

### Depois responda no código

1. Por que `StringBuilder` é adequado para esse cenário?
2. Qual é a diferença de mutabilidade entre `String` e `StringBuilder`?
3. Por que a quantidade de Strings intermediárias pode ser diferente?

---

# 🏆 Desafio Integrador — Gerador de relatório

Agora você vai juntar **todo o conteúdo das aulas 106–111**.

Crie um pequeno programa chamado:

```text
GeradorRelatorio
```

O programa deve receber informações de um produto e gerar um relatório textual.

## 1. Dados

Crie uma classe `Produto` com:

```text
nome
codigo
quantidade
preco
ativo
```

Use os **Wrappers** apropriados.

Exemplo:

```text
nome       → String
codigo     → String
quantidade → Integer
preco      → Double
ativo      → Boolean
```

---

## 2. Código do produto

Utilize uma String com este formato:

```text
PROD-2026-JAVA
```

O programa deve extrair as partes do código utilizando os conhecimentos de `String`.

Não utilize `split()`.

---

## 3. Normalização

O nome do produto pode chegar assim:

```text
"   curso java   "
```

Antes de colocá-lo no relatório:

```text
remova espaços externos
transforme para maiúsculas
```

---

## 4. Validação dos caracteres

Percorra uma informação textual do produto e utilize `Character` para identificar caracteres que sejam:

- letras;
- dígitos;
- maiúsculos;
- minúsculos.

Não faça a verificação manualmente com comparações de caracteres.

---

## 5. Relatório

O relatório final deve possuir uma estrutura semelhante a:

```text
================================
RELATÓRIO DO PRODUTO
================================
NOME: CURSO JAVA
CÓDIGO: PROD-2026-JAVA

CATEGORIA: PROD
ANO: 2026
TECNOLOGIA: JAVA

QUANTIDADE: 25
PREÇO: 49.90
ATIVO: true
================================
```

O texto deve ser construído utilizando:

```java
StringBuilder
```

Não utilize uma sequência de:

```java
texto += ...
```

para construir o relatório.

---

## 6. Conversão

Inclua no sistema pelo menos uma informação inicialmente representada como `String` e converta para o Wrapper correspondente.

Exemplo:

```java
String quantidadeTexto = "25";
```

e depois:

```text
String → Integer
```

---

## 7. Comparação

O programa deve possuir pelo menos uma comparação de conteúdo de Strings utilizando:

```java
equals()
```

Não utilize `==` para verificar se dois textos possuem o mesmo conteúdo.

---

## 8. Desempenho

Crie uma segunda operação que gere vários relatórios em sequência.

Faça uma medição aproximada do tempo utilizando:

```java
System.currentTimeMillis()
```

Você deve utilizar `StringBuilder` nessa construção.

Não precisa comparar com `String` novamente se isso deixar o desafio muito grande.

O objetivo é aplicar a ideia de construção eficiente de texto.

---

# Restrições do desafio

Você **não pode**:

- usar `split()` para separar o código;
- usar bibliotecas externas;
- usar `StringBuilder` sem entender o motivo;
- usar `==` para comparar conteúdo de Strings;
- fazer toda a lógica dentro do `main`;
- copiar literalmente os exemplos das aulas.

Você **deve** utilizar:

- Wrappers;
- autoboxing ou unboxing em algum ponto adequado;
- conversão de String para Wrapper;
- `Character`;
- `String`;
- `equals()`;
- `substring()`;
- `charAt()`;
- `length()`;
- `trim()`;
- `toUpperCase()` ou `toLowerCase()`;
- `StringBuilder`;
- `append()`;
- `toString()`;
- medição com `System.currentTimeMillis()`.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei identificar os Wrappers.
- [ ] Sei diferenciar Wrapper de primitivo.
- [ ] Entendo autoboxing.
- [ ] Entendo unboxing.
- [ ] Sei utilizar métodos de `Character`.
- [ ] Entendo que `String` é objeto.
- [ ] Entendo que `String` é imutável.
- [ ] Sei explicar String Pool.
- [ ] Sei diferenciar `==` e `equals()`.
- [ ] Sei usar `charAt()`.
- [ ] Sei usar `length()`.
- [ ] Sei usar `replace()`.
- [ ] Sei usar `substring()`.
- [ ] Entendo índice inicial e final exclusivo.
- [ ] Sei usar `trim()`.
- [ ] Entendo por que muitas concatenações podem afetar desempenho.
- [ ] Sei criar um `StringBuilder`.
- [ ] Sei usar `append()`.
- [ ] Sei usar `reverse()`.
- [ ] Sei usar `delete()`.
- [ ] Sei usar `toString()`.
- [ ] Sei explicar a diferença entre `String` e `StringBuilder`.

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
