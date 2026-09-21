# Exercícios — Bloco 06
## Associação entre Objetos e Entrada de Dados

### Como estudar este bloco

Não faça todos os exercícios de uma vez.

Para cada aula:

1. Leia a aula correspondente no README.
2. Faça os 3 exercícios daquela aula.
3. Envie seu código para mim.
4. Eu vou corrigir apontando problemas e dando dicas primeiro.
5. Depois de entender a correção, avance para a próxima aula.

Cada aula possui:

- 🟢 1 fácil
- 🟡 1 médio
- 🔴 1 difícil

---

# Aula 64 — Arrays com Objetos

## 🟢 64.1 — Lista de jogadores

Crie uma classe `Jogador` com:

- `nome`;
- método `imprimir()`.

No `main`:

1. Crie 3 jogadores.
2. Crie um `Jogador[]` com 3 posições.
3. Coloque os três jogadores no array.
4. Percorra o array usando `for-each`.
5. Chame `imprimir()` para cada jogador.

---

## 🟡 64.2 — Referências compartilhadas

Crie:

```java
Jogador jogador1 = new Jogador("Pelé");
Jogador jogador2 = new Jogador("Cafu");
```

Crie um array com 3 posições e faça:

```text
posição 0 → jogador1
posição 1 → jogador2
posição 2 → jogador1
```

Depois altere o nome do objeto referenciado por `jogador1`.

Mostre o array novamente.

**Objetivo:** descobrir o que acontece quando duas posições apontam para o mesmo objeto.

---

## 🔴 64.3 — Cadastro de jogadores

Crie um sistema que possua:

- classe `Jogador`;
- array de jogadores;
- método para imprimir todos;
- método para encontrar um jogador pelo nome.

Não crie novos objetos durante a busca.

A busca deve percorrer o array e trabalhar com as referências existentes.

**Desafio:** trate posições `null` corretamente.

---

# Aula 65 — Associação unidirecional um para muitos

## 🟢 65.1 — Jogador possui um time

Crie:

- `Jogador`
- `Time`

Um jogador deve possuir uma referência para um `Time`.

No `main`, crie os dois objetos e associe:

```text
Jogador → Time
```

Depois imprima o nome do time através do jogador.

---

## 🟡 65.2 — Jogador sem time

Evolua o exercício anterior.

Um jogador pode ser criado sem time.

Crie um método `imprimir()` que mostre:

```text
Jogador: Pelé
Time: Brasil
```

quando houver time, e algo como:

```text
Jogador: Pelé
Time: Nenhum
```

quando não houver.

**Objetivo:** praticar associação e tratamento de `null`.

---

## 🔴 65.3 — Cadastro de jogadores e times

Crie:

```text
Time
Jogador
```

Um jogador pode possuir um único time.

No `main`:

- crie 3 times;
- crie 6 jogadores;
- associe cada jogador a um time;
- permita que alguns jogadores fiquem sem time;
- imprima cada jogador junto com seu time.

O `Time` não pode possuir uma referência para seus jogadores.

**Desafio:** mantenha a associação estritamente unidirecional.

---

# Aula 66 — Associação unidirecional muitos para um

## 🟢 66.1 — Escola e professor

Crie:

- `Escola`
- `Professor`

Uma escola deve possuir um array de professores.

Crie uma escola com três professores e imprima os nomes.

---

## 🟡 66.2 — Professores de uma escola

Crie uma escola com capacidade para 5 professores.

Cadastre somente 3.

Ao imprimir:

- mostre os professores cadastrados;
- não tente acessar posições `null`.

**Objetivo:** praticar array de objetos + associação.

---

## 🔴 66.3 — Escola completa

Crie:

```text
Escola
Professor
```

Requisitos:

- uma escola pode possuir vários professores;
- um professor pode possuir uma escola;
- a escola deve conseguir imprimir seus professores;
- o professor deve conseguir imprimir sua escola;
- trate referências `null`.

**Atenção:** pense cuidadosamente sobre qual lado realmente precisa conhecer o outro. Não transforme automaticamente tudo em bidirecional.

---

# Aula 67 — Associação bidirecional

## 🟢 67.1 — Jogador ↔ Time

Crie:

```text
Jogador
Time
```

Faça os dois lados conhecerem a associação:

```text
Jogador → Time
Time → Jogadores
```

Crie um jogador e um time e estabeleça a relação.

---

## 🟡 67.2 — Detectando inconsistência

Crie dois jogadores e um time.

Associe os jogadores ao time.

Depois faça propositalmente uma alteração em apenas um dos lados.

Exemplo conceitual:

```text
jogador.setTime(brasil);
```

mas não coloque o jogador no array do Brasil.

Execute o programa e observe a inconsistência.

Depois corrija manualmente.

**Objetivo:** entender por que associação bidirecional exige cuidado.

---

## 🔴 67.3 — Manter os dois lados sincronizados

Crie `Time` e `Jogador`.

Implemente uma operação para adicionar um jogador ao time de forma que a associação seja criada nos dois lados.

Quando você executar:

```java
brasil.adicionarJogador(cafu);
```

o resultado deve ser:

```text
Time Brasil conhece Cafu
Cafu conhece Brasil
```

Não permita que o código de teste precise fazer manualmente as duas operações.

**Desafio:** pense em como evitar que a associação fique inconsistente.

---

# Aula 68 — Scanner

## 🟢 68.1 — Cadastro simples

Use `Scanner` para perguntar:

```text
Digite seu nome:
Digite sua idade:
```

Leia:

- nome com `nextLine()`;
- idade com `nextInt()`.

Depois imprima os dados.

---

## 🟡 68.2 — Formulário

Crie um pequeno formulário no console:

```text
Nome:
Idade:
Sexo:
```

Use:

- `nextLine()` para nome;
- `nextInt()` para idade;
- `next().charAt(0)` para sexo.

Depois mostre um relatório com os dados.

---

## 🔴 68.3 — Cadastro de objeto pelo teclado

Crie uma classe `Pessoa` com:

- nome;
- idade;
- sexo.

No `main`, use `Scanner` para receber todos os dados.

Depois utilize esses dados para criar um objeto `Pessoa`.

Não deixe o usuário preencher o objeto manualmente através de setters depois da criação.

**Objetivo:** juntar entrada de dados + construtor + objeto.

---

# Aula 69 — Exercício de entrada pelo teclado

## 🟢 69.1 — Pergunta sim ou não

Crie um programa que leia uma pergunta usando `nextLine()`.

Se o primeiro caractere da pergunta for um espaço, responda:

```text
SIM
```

Caso contrário:

```text
NÃO
```

Use `charAt(0)`.

---

## 🟡 69.2 — Analisador da pergunta

Leia uma pergunta completa.

Mostre:

- primeira letra;
- quantidade de caracteres;
- se começa com espaço;
- se começa com a letra `a` ou `A`.

**Objetivo:** combinar `Scanner`, `String`, `charAt()` e condicionais.

---

## 🔴 69.3 — Simulador de respostas

Crie um programa que:

1. peça uma pergunta;
2. leia a pergunta inteira;
3. analise o primeiro caractere;
4. produza respostas diferentes dependendo do caractere inicial;
5. trate uma pergunta vazia sem quebrar o programa.

**Desafio:** pense antes de usar `charAt(0)`. O que acontece se a String estiver vazia?

---

# Aula 70 — Exercício de Associação

## 🟢 70.1 — Sistema básico de seminário

Crie as quatro classes:

```text
Aluno
Professor
Local
Seminario
```

Use os atributos apresentados na aula.

Monte pelo menos:

- 1 local;
- 1 professor;
- 2 alunos;
- 1 seminário.

Associe os objetos.

---

## 🟡 70.2 — Sistema de seminários

Evolua o exercício.

O sistema deve permitir:

- um professor ministrar vários seminários;
- um seminário possuir vários alunos;
- um aluno estar em apenas um seminário;
- um seminário possuir um local.

Crie:

- 2 professores;
- 3 seminários;
- 5 alunos;
- 2 locais.

Monte os relacionamentos e crie métodos `imprimir()` para mostrar as informações.

---

## 🔴 70.3 — Desafio final — Cadastro de Seminários

Implemente o exercício completo da aula.

### Classes obrigatórias

```text
Aluno
Professor
Local
Seminario
```

### Requisitos

**Aluno**

- nome;
- idade;
- referência para um seminário.

**Professor**

- nome;
- especialidade;
- referência para vários seminários.

**Local**

- endereço.

**Seminario**

- título;
- array de alunos;
- professor(es);
- local.

### Regras

1. Um aluno pode estar em apenas um seminário.
2. Um seminário pode ter zero ou vários alunos.
3. Um professor pode ministrar vários seminários.
4. Um seminário deve possuir um local.
5. Os relacionamentos devem ser representados através de referências.
6. Não use `String` para representar outro objeto.
7. Não coloque tudo em uma única classe.
8. Crie métodos para imprimir as informações.
9. Trate referências `null` quando elas forem permitidas.
10. No `main`, crie os objetos em uma ordem que faça sentido considerando as dependências.

### Desafio adicional

Depois que o sistema estiver funcionando, utilize `Scanner` para permitir que pelo menos alguns dados do cadastro sejam informados pelo usuário.

A ideia é juntar:

```text
Classes
+
Construtores
+
Encapsulamento
+
Arrays
+
Referências
+
Associação
+
Scanner
```

Esse é o exercício mais importante do bloco.

---

# Checklist final

Antes de avançar para o próximo bloco, você deve conseguir explicar:

- [ ] O que um array de objetos realmente armazena.
- [ ] A diferença entre objeto e referência.
- [ ] Como duas referências podem apontar para o mesmo objeto.
- [ ] O que significa associação entre objetos.
- [ ] O que é associação unidirecional.
- [ ] O que é associação bidirecional.
- [ ] O que significa um para muitos.
- [ ] O que significa muitos para um.
- [ ] Por que associações bidirecionais podem ficar inconsistentes.
- [ ] Como usar `Scanner`.
- [ ] Diferença entre `next()` e `nextLine()`.
- [ ] Como ler números.
- [ ] Como obter um `char` usando `charAt(0)`.
- [ ] Como juntar vários objetos em um pequeno sistema.
