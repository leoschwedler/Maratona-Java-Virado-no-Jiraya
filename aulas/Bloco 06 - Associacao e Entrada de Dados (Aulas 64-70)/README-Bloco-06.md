# Bloco 06 — Associação entre Objetos e Entrada de Dados

## Aulas 64–70

Este bloco continua a parte de **Orientação a Objetos** e introduz uma ideia muito importante: objetos podem se relacionar com outros objetos.

A sequência do curso neste bloco é:

- Aula 64 — Associação pt. 01 — Arrays com Objetos
- Aula 65 — Associação pt. 02 — Associação unidirecional um para muitos
- Aula 66 — Associação pt. 03 — Associação unidirecional muitos para um
- Aula 67 — Associação pt. 04 — Associação bidirecional
- Aula 68 — Reading data via the console
- Aula 69 — Keyboard Input Exercise
- Aula 70 — Associação pt. 05 — Exercício Associação

A organização abaixo segue as transcrições dessas aulas. fileciteturn31file2L1-L8

---

# Aula 64 — Associação pt. 01 — Arrays com Objetos

## 1. O que a aula começa a apresentar

A aula introduz **associação** como o relacionamento entre objetos.

A ideia é próxima do que acontece no mundo real: uma entidade pode se relacionar com outra. No exemplo usado na aula, são criados jogadores e depois esses jogadores são colocados dentro de um array.

O ponto importante é entender que o array não cria novos jogadores automaticamente. Ele guarda **referências para objetos**.

---

## 2. Criando os objetos

Imagine uma classe `Jogador` com um atributo `nome`.

Podemos criar objetos diferentes:

```java
Jogador jogador1 = new Jogador("Pelé");
Jogador jogador2 = new Jogador("Cafu");
Jogador jogador3 = new Jogador("Romário");
```

Agora existem três objetos diferentes na memória.

Cada variável é uma referência para um objeto.

---

## 3. Colocando objetos em um array

Podemos criar um array de `Jogador`:

```java
Jogador[] jogadores = new Jogador[3];
```

Nesse momento temos espaço para três referências de `Jogador`.

Podemos colocar os objetos dentro:

```java
jogadores[0] = jogador1;
jogadores[1] = jogador2;
jogadores[2] = jogador3;
```

Agora o array possui referências para aqueles três objetos.

---

## 4. O array não duplica o objeto

Esse é um ponto importante da aula.

Se:

```java
jogadores[0] = jogador1;
```

a posição `0` do array passa a referenciar o mesmo objeto que `jogador1` referencia.

Não foi criado outro `Jogador`.

Podemos pensar:

```text
jogador1 ─────┐
              ├──> objeto Jogador("Pelé")
jogadores[0] ─┘
```

Portanto, várias referências podem apontar para o mesmo objeto.

---

## 5. Percorrendo o array

Depois de colocar os jogadores no array, podemos percorrê-lo:

```java
for (Jogador jogador : jogadores) {
    jogador.imprimir();
}
```

O `for-each` recebe cada referência armazenada no array.

A cada repetição, `jogador` aponta para um dos objetos.

---

## 6. O que você precisa dominar

Ao terminar esta aula, você deve conseguir explicar:

- o que é uma referência;
- o que é um objeto;
- como criar um array de objetos;
- como colocar objetos dentro do array;
- que o array guarda referências;
- como percorrer o array;
- por que várias referências podem apontar para o mesmo objeto.

A transcrição enfatiza justamente essa relação entre as variáveis de referência, os objetos e as posições do array. 

---

# Aula 65 — Associação pt. 02 — Um para muitos

## 1. O que significa associação

A aula define associação como um **relacionamento entre dois objetos**.

Exemplos apresentados:

- um departamento possui várias pessoas;
- uma pessoa pode possuir um carro;
- um time possui vários jogadores.

Também são apresentadas relações como:

- um para um;
- um para muitos;
- muitos para um;
- muitos para muitos.

---

## 2. Exemplo jogador e time

O exemplo principal trabalha com:

```text
Jogador → Time
```

Um jogador pode pertencer a um time.

Nesse primeiro modelo, o relacionamento é **unidirecional**.

Isso significa que o jogador conhece o time, mas o time não necessariamente conhece os jogadores.

---

## 3. Como representar isso em Java

A classe `Jogador` pode possuir:

```java
private Time time;
```

Agora `Jogador` possui uma referência para um objeto `Time`.

Isso cria a possibilidade de fazer:

```java
jogador.setTime(time);
```

A relação passa a existir porque uma referência foi armazenada.

---

## 4. Um objeto não cria o relacionamento sozinho

Um ponto destacado na aula é que simplesmente existirem:

```java
Jogador jogador = new Jogador(...);
Time time = new Time(...);
```

não significa que existe uma associação.

São apenas dois objetos independentes.

É necessário estabelecer a referência:

```java
jogador.setTime(time);
```

Agora o jogador possui uma referência para aquele time.

---

## 5. Cuidado com `null`

Se o jogador puder existir sem um time, então:

```java
private Time time;
```

pode inicialmente conter `null`.

Ao imprimir o nome do time, precisamos considerar essa possibilidade:

```java
if (time != null) {
    System.out.println(time.getNome());
}
```

Caso contrário, podemos tentar acessar um objeto que não existe naquela referência.

---

## 6. Um para muitos

A ideia de um para muitos aparece quando temos, por exemplo:

```text
Time
 ├── Jogador
 ├── Jogador
 └── Jogador
```

Um time pode estar relacionado a vários jogadores.

No modelo unidirecional, a direção do relacionamento determina qual objeto conhece o outro.

---

# Aula 66 — Associação pt. 03 — Muitos para um

## 1. Invertendo a perspectiva

Nesta aula o curso continua trabalhando com associação unidirecional.

O exemplo muda a perspectiva para uma relação em que:

```text
Escola → vários Professores
```

e cada:

```text
Professor → uma Escola
```

A ideia é perceber que a cardinalidade depende de qual lado estamos observando.

---

## 2. Escola com vários professores

Uma escola pode possuir vários professores.

Em Java, isso pode ser representado por um array:

```java
private Professor[] professores;
```

Agora a escola possui uma referência para vários objetos `Professor`.

---

## 3. Professor com uma escola

Um professor pode possuir uma referência para uma escola:

```java
private Escola escola;
```

Nesse modelo, o professor conhece sua escola.

Dependendo da direção que estamos modelando, uma classe pode conhecer a outra sem que o caminho inverso exista.

---

## 4. Um array continua sendo um conjunto de referências

Se tivermos:

```java
Professor[] professores = new Professor[3];
```

temos três posições capazes de guardar referências para objetos `Professor`.

Podemos ter:

```text
Escola
  |
  +--> Professor 1
  +--> Professor 2
  +--> Professor 3
```

O importante é não confundir as posições do array com os próprios objetos.

---

## 5. `null` novamente

Uma posição do array pode não possuir objeto:

```java
Professor[] professores = new Professor[3];
```

Antes de preencher:

```text
[null, null, null]
```

Por isso, ao percorrer e acessar atributos, precisamos entender se a posição realmente possui uma referência.

---

# Aula 67 — Associação pt. 04 — Bidirecional

## 1. O que muda

Agora o relacionamento passa a existir nos dois lados.

No exemplo:

```text
Jogador ↔ Time
```

O jogador conhece o time:

```java
private Time time;
```

E o time também conhece seus jogadores:

```java
private Jogador[] jogadores;
```

Isso é uma associação **bidirecional**.

---

## 2. Os dois lados precisam ser relacionados

Imagine:

```java
Jogador jogador = new Jogador("Cafu");
Time time = new Time("Brasil");
```

Ainda não existe associação.

Podemos fazer:

```java
jogador.setTime(time);
```

Agora o jogador conhece o time.

Mas isso não significa automaticamente que o time passou a conhecer o jogador.

Também precisamos colocar o jogador no array do time:

```java
time.setJogadores(jogadores);
```

A aula chama atenção exatamente para esse cuidado.

---

## 3. O risco de inconsistência

Podemos acidentalmente criar uma situação como:

```text
Jogador → Brasil

Brasil → jogador pertence à Suíça
```

Os objetos possuem informações conflitantes.

Em uma associação bidirecional, o desenvolvedor precisa tomar cuidado para manter os dois lados coerentes.

---

## 4. Visualizando as referências

Podemos pensar:

```text
Jogador
   |
   | referencia
   v
 Time
   |
   | referencia
   v
Jogador[]
```

E o array contém referências para jogadores.

Portanto, temos objetos separados e referências conectando esses objetos.

---

## 5. O curso não está tratando isso como banco de dados

A aula menciona que bancos de dados normalmente ajudam a garantir a integridade dos relacionamentos.

Aqui o objetivo é entender como esses relacionamentos funcionam **entre objetos Java**.

Você precisa saber criar e manter as referências corretamente.

---

# Aula 68 — Reading data via the console

## 1. Por que aprender entrada pelo teclado

A aula introduz uma forma de fazer o programa receber dados do usuário pelo teclado.

A classe utilizada é:

```java
Scanner
```

Ela pertence ao pacote:

```java
java.util
```

O import fica:

```java
import java.util.Scanner;
```

---

## 2. Criando o Scanner

Para ler a entrada padrão:

```java
Scanner entrada = new Scanner(System.in);
```

`System.in` representa a entrada que será utilizada pelo `Scanner`.

---

## 3. Lendo diferentes tipos

A aula apresenta métodos como:

```java
next()
nextLine()
nextInt()
nextDouble()
```

A escolha do método determina o tipo de dado esperado.

Por exemplo:

```java
int idade = entrada.nextInt();
```

---

## 4. `next()` x `nextLine()`

Esta diferença é muito importante.

`next()` lê uma palavra/token.

Se o usuário digitar:

```text
Leonardo Schwedler
```

`next()` pega somente:

```text
Leonardo
```

Já:

```java
nextLine()
```

lê a linha inteira.

---

## 5. Lendo um caractere

No exemplo da aula, o sexo é recebido como uma palavra e depois é retirado o primeiro caractere.

A ideia é:

```java
char sexo = entrada.next().charAt(0);
```

`charAt(0)` pega o caractere na posição `0`.

---

## 6. O programa pode ficar esperando

Quando chegamos em uma leitura:

```java
entrada.nextInt();
```

o programa fica aguardando o usuário fornecer um valor.

Isso explica por que o programa parece parado durante a execução.

Ele está esperando uma entrada.

---

## 7. O que você precisa dominar

Ao terminar a aula, você deve saber:

- importar `Scanner`;
- criar um objeto `Scanner`;
- usar `System.in`;
- ler `String`;
- ler texto com espaços;
- ler `int`;
- entender `charAt(0)`;
- saber que o programa pode ficar aguardando entrada.

---

# Aula 69 — Keyboard Input Exercise

Esta aula é um pequeno exercício prático com `Scanner`.

O programa solicita uma pergunta ao usuário e responde `sim` ou `não`.

O objetivo principal é praticar:

- `Scanner`;
- `nextLine()`;
- `String`;
- acesso a caracteres de uma String;
- `charAt(0)`;
- estruturas condicionais.

---

## 1. Ler uma pergunta

O programa pede ao usuário uma pergunta:

```text
Digite sua pergunta e eu responderei sim ou não
```

A entrada pode ser lida com:

```java
String pergunta = entrada.nextLine();
```

---

## 2. Acessar uma posição da String

A aula utiliza a primeira posição da pergunta:

```java
pergunta.charAt(0)
```

Como as posições começam em `0`, isso representa o primeiro caractere.

---

## 3. Usar o caractere em uma condição

A partir desse caractere, o programa pode decidir qual resposta imprimir.

O exercício serve principalmente para praticar a leitura do teclado combinada com uma estrutura de decisão.

---

## 4. Atenção ao espaço

A aula mostra uma brincadeira baseada em colocar um espaço antes da pergunta.

Isso é possível porque o primeiro caractere da String passa a ser o espaço.

O objetivo aqui é didático: mostrar que `charAt(0)` realmente acessa a posição zero da String.

---

# Aula 70 — Associação pt. 05 — Exercício Associação

## 1. O problema

Esta aula fecha o bloco com um exercício maior de associação.

O sistema deve gerenciar **seminários**, envolvendo:

- seminário;
- estudante;
- professor;
- local.

Os requisitos apresentados são:

- o sistema cadastra seminários;
- o sistema cadastra estudantes;
- o sistema cadastra professores;
- o sistema cadastra o local onde o seminário acontece;
- um aluno pode estar em apenas um seminário;
- um seminário pode ter nenhum ou vários alunos;
- um professor pode ministrar vários seminários;
- um seminário deve ter obrigatoriamente um local.

---

## 2. Classes básicas

A aula cria as classes:

```text
Seminario
Aluno
Professor
Local
```

### Aluno

Possui:

```text
nome
idade
```

e uma referência para um seminário.

### Professor

Possui:

```text
nome
especialidade
```

e pode estar relacionado a vários seminários.

### Local

Possui:

```text
endereco
```

### Seminario

Possui:

```text
titulo
```

e relacionamentos com:

- alunos;
- professor;
- local.

---

## 3. Relacionamento Aluno → Seminário

O requisito diz que um aluno pode estar em apenas um seminário.

Então o aluno possui uma referência:

```java
private Seminario seminario;
```

É uma relação de muitos alunos para um seminário, considerando que vários alunos podem participar do mesmo seminário.

---

## 4. Seminário → Alunos

O seminário pode ter nenhum ou vários alunos.

Por isso a aula usa um array:

```java
private Aluno[] alunos;
```

Podemos representar:

```text
Seminário
   |
   +--> Aluno
   +--> Aluno
   +--> Aluno
```

---

## 5. Professor → Seminários

Um professor pode ministrar vários seminários.

Por isso o professor possui uma referência para vários seminários:

```java
private Seminario[] seminarios;
```

---

## 6. Seminário → Local

O seminário precisa obrigatoriamente de um local.

Então:

```java
private Local local;
```

representa a relação.

Diferentemente de uma lista de alunos, aqui o seminário possui apenas um local.

---

## 7. A ordem para criar os objetos

A aula mostra uma ideia importante: quando existem relacionamentos, pode ser necessário criar primeiro os objetos que não dependem de outros.

Uma ordem possível apresentada é:

```text
Local
 ↓
Aluno / Professor
 ↓
Seminário
```

Primeiro criamos os objetos básicos.

Depois usamos suas referências para montar o seminário.

---

## 8. Montando o relacionamento

Primeiro:

```java
Local local = new Local(...);
```

Depois alunos e professor.

Por fim, criamos o seminário usando os objetos já existentes.

O objetivo é perceber que associação é construída ligando **referências de objetos**.

---

# Resumo do bloco

Ao terminar o bloco, você deve entender a evolução:

```text
Arrays de objetos
      ↓
Relacionamento entre objetos
      ↓
Associação unidirecional
      ↓
Um para muitos / muitos para um
      ↓
Associação bidirecional
      ↓
Entrada de dados com Scanner
      ↓
Exercício completo de associação
```

## Checklist

- [ ] Consigo criar um array de objetos.
- [ ] Entendo que o array guarda referências.
- [ ] Consigo percorrer um array de objetos.
- [ ] Entendo o que é associação.
- [ ] Consigo representar um objeto que referencia outro objeto.
- [ ] Entendo associação unidirecional.
- [ ] Entendo um para muitos.
- [ ] Entendo muitos para um.
- [ ] Entendo associação bidirecional.
- [ ] Sei por que uma associação bidirecional pode ficar inconsistente.
- [ ] Sei criar um `Scanner`.
- [ ] Sei usar `next()`, `nextLine()` e `nextInt()`.
- [ ] Sei usar `charAt(0)`.
- [ ] Consigo montar um pequeno sistema com várias classes associadas.
