# Exercícios — Bloco 07: Herança e Sobrescrita

> **Regra desta etapa:** os exercícios abaixo NÃO são cópias dos exemplos das aulas.
>
> Eles usam os conceitos ensinados, mas exigem que você monte uma solução própria, combine conceitos e tome decisões de modelagem.
>
> Faça **3 exercícios por aula**, nesta ordem:
> 1. Fácil — consolidação
> 2. Médio — aplicação
> 3. Difícil — problema com mais de uma decisão
>
> **Não avance para a próxima aula antes de corrigir os três.**

---

# Aula 71 — Herança

## Exercício 1 — Cadastro de usuários

Crie:

```text
Usuario
Administrador
Cliente
```

`Administrador` e `Cliente` devem herdar de `Usuario`.

Requisitos:

- `Usuario` deve possuir `nome` e `email`.
- `Administrador` deve possuir `nivelAcesso`.
- `Cliente` deve possuir `saldo`.
- Crie objetos das duas subclasses.
- Demonstre no `main` quais informações pertencem à classe base e quais são específicas de cada especialização.

**Objetivo:** perceber o que é característica comum e o que é especialização.

---

## Exercício 2 — Sistema de documentos

Crie uma classe:

```text
Documento
```

e duas subclasses:

```text
DocumentoFiscal
Contrato
```

Requisitos:

- `Documento` deve possuir número, título e data.
- `DocumentoFiscal` deve possuir valor.
- `Contrato` deve possuir nome da parte contratante.
- Crie pelo menos dois objetos de cada tipo.
- Organize o código para evitar duplicação das informações comuns.
- Mostre os dados no console.

**Restrição:** não crie atributos duplicados de `numero`, `titulo` ou `data` nas subclasses.

---

## Exercício 3 — Sistema de funcionários

Modele:

```text
Funcionario
├── Desenvolvedor
├── Gerente
└── Estagiario
```

Requisitos:

- Todos devem possuir nome, matrícula e salário-base.
- `Desenvolvedor` deve possuir linguagem principal.
- `Gerente` deve possuir quantidade de subordinados.
- `Estagiario` deve possuir instituição de ensino.
- Crie pelo menos um objeto de cada tipo.
- Crie um método na classe base para exibir as informações comuns.
- Cada subclasse deve ter uma forma própria de exibir suas informações adicionais.

**Desafio:** pense cuidadosamente no que realmente pertence a `Funcionario` e no que pertence a cada especialização.

---

# Aula 72 — `super`

## Exercício 4 — Perfil especializado

Crie:

```text
Perfil
UsuarioPremium
```

`UsuarioPremium` deve herdar de `Perfil`.

A classe `Perfil` deve possuir um método:

```java
public void exibirTipo()
```

que imprime uma mensagem genérica.

Na subclasse:

- sobrescreva o método;
- use `super.exibirTipo()`;
- depois acrescente uma informação específica do usuário premium.

**Objetivo:** praticar a chamada do comportamento original antes de adicionar comportamento novo.

---

## Exercício 5 — Relatório em camadas

Crie:

```text
Relatorio
RelatorioFinanceiro
```

A classe `Relatorio` deve possuir um método:

```java
public void gerar()
```

que imprime informações gerais.

A subclasse deve sobrescrever `gerar()` e:

1. executar primeiro o comportamento da superclasse;
2. adicionar informações financeiras;
3. imprimir uma mensagem final indicando que o relatório financeiro foi concluído.

Depois crie um terceiro nível:

```text
RelatorioFinanceiroMensal
```

que também sobrescreve `gerar()` e utiliza `super.gerar()`.

**Objetivo:** perceber como `super` funciona em mais de um nível de herança.

---

## Exercício 6 — Comparando estado da classe pai e filha

Crie:

```text
Produto
ProdutoDigital
```

A classe `Produto` deve possuir um atributo relacionado à identificação do produto.

A subclasse deve possuir uma informação específica de produto digital.

Crie métodos de impressão nas duas classes e faça a subclasse utilizar `super` para executar o comportamento da classe pai antes de apresentar seus dados.

**Desafio:** evite simplesmente repetir no método da subclasse tudo que já é responsabilidade da superclasse.

---

# Aula 73 — `protected`

## Exercício 7 — Controle de acesso na hierarquia

Crie:

```text
Conta
ContaCorrente
```

Na classe `Conta`, tenha:

- um identificador `private`;
- um saldo `protected`.

Na subclasse:

- crie um método para exibir o saldo;
- altere o saldo através de uma operação controlada;
- não tente acessar diretamente o identificador `private`.

Depois explique em comentário no código por que um dos atributos pode ser acessado pela subclasse e o outro não.

---

## Exercício 8 — Hierarquia de veículos

Crie:

```text
Veiculo
Carro
Moto
```

Na classe `Veiculo`, mantenha um atributo `protected` que represente uma informação compartilhada pela hierarquia.

Cada subclasse deve utilizar esse atributo em um comportamento próprio.

Depois crie também um atributo `private` na classe `Veiculo` e tente utilizá-lo diretamente nas subclasses.

**Objetivo:** observar na prática a diferença de visibilidade.

---

## Exercício 9 — Refatorando uma hierarquia

Você recebeu este cenário:

```text
Funcionario
Desenvolvedor
Gerente
Analista
```

Existem informações que todas as subclasses precisam utilizar, mas que não devem ficar públicas.

Modele uma solução usando `protected` onde fizer sentido e `private` onde o acesso deve permanecer encapsulado.

Requisitos:

- pelo menos 2 atributos `private`;
- pelo menos 1 atributo `protected`;
- métodos de acesso quando necessários;
- cada subclasse deve utilizar corretamente os membros herdados.

**Desafio:** não transforme tudo em `protected` só para facilitar o código.

---

# Aula 74 — Construtores e `super(...)`

## Exercício 10 — Cadastro com construtores encadeados

Crie:

```text
Pessoa
Funcionario
```

A classe `Pessoa` deve exigir no construtor:

```text
nome
cpf
```

A classe `Funcionario` deve exigir:

```text
nome
cpf
salario
```

O construtor de `Funcionario` deve inicializar a parte herdada através de `super(...)`.

Depois crie objetos e mostre todos os dados.

**Objetivo:** entender que a subclasse precisa participar da construção da superclasse.

---

## Exercício 11 — Hierarquia com três níveis

Crie:

```text
Pessoa
Funcionario
Gerente
```

Regras:

- `Pessoa` exige `nome`.
- `Funcionario` exige `nome` e `salario`.
- `Gerente` exige `nome`, `salario` e `departamento`.

Cada construtor deve chamar corretamente o construtor da classe imediatamente acima.

Depois crie um `Gerente` usando apenas o construtor da própria classe.

**Desafio:** acompanhe mentalmente a cadeia:

```text
Gerente
   ↓
Funcionario
   ↓
Pessoa
```

---

## Exercício 12 — Construtores com regras diferentes

Crie:

```text
Veiculo
VeiculoEletrico
```

`Veiculo` deve exigir marca e modelo.

`VeiculoEletrico` deve exigir marca, modelo e capacidade da bateria.

Crie pelo menos dois construtores diferentes na subclasse, mas garanta que ambos inicializem corretamente a parte herdada.

**Desafio:** não duplique a lógica de inicialização da classe pai.

---

# Aula 75 — Sequência de inicialização

## Exercício 13 — Descubra a ordem

Crie:

```text
Base
Derivada
```

Na classe `Base`, coloque:

- um bloco `static`;
- um bloco de inicialização de instância;
- um construtor.

Na classe `Derivada`, faça a mesma coisa.

No `main`:

1. crie um objeto de `Derivada`;
2. crie outro objeto de `Derivada`.

Use mensagens diferentes em cada bloco.

Antes de executar, escreva em comentário a ordem que você acredita que será impressa.

Depois compare com o resultado.

---

## Exercício 14 — Sequência com três níveis

Crie:

```text
A
B extends A
C extends B
```

Cada classe deve possuir:

- um bloco `static`;
- um bloco de inicialização de instância;
- um construtor.

No `main`, crie um objeto de `C` e depois outro.

**Desafio:** determine previamente a ordem completa das mensagens.

Depois responda em comentários:

1. Quais mensagens aparecem apenas uma vez?
2. Quais aparecem novamente quando o segundo objeto é criado?
3. Qual classe é inicializada primeiro?

---

## Exercício 15 — Diagnóstico de inicialização

Monte uma hierarquia de três classes:

```text
Sistema
Servidor
ServidorWeb
```

Cada nível deve possuir mensagens em:

- bloco `static`;
- bloco de instância;
- construtor.

Adicione também pelo menos um atributo inicializado diretamente na declaração.

Crie dois objetos `ServidorWeb`.

Depois organize a saída em grupos:

```text
Inicialização das classes
-------------------------
...

Criação do primeiro objeto
-------------------------
...

Criação do segundo objeto
-------------------------
...
```

**Desafio maior:** não copie uma ordem pronta. Faça sua previsão antes de executar e explique por que a ordem aconteceu daquela maneira.

---

# Aula 76 — Sobrescrita de `toString()`

## Exercício 16 — `toString()` de cadastro

Crie:

```text
Cliente
```

com:

- id;
- nome;
- email.

Sobrescreva `toString()` para retornar uma representação organizada do cliente.

Depois:

```java
System.out.println(cliente);
```

deve mostrar os dados do objeto de forma legível.

**Não use vários `System.out.println()` para montar a representação.** O método `toString()` deve ser responsável por ela.

---

## Exercício 17 — `toString()` com herança

Crie:

```text
Produto
ProdutoImportado
```

`Produto` deve possuir:

- código;
- nome;
- preço.

`ProdutoImportado` deve acrescentar:

- país de origem;
- taxa de importação.

As duas classes devem possuir `toString()`.

Na implementação da subclasse, reutilize a representação da classe pai em vez de duplicar todas as informações manualmente.

**Objetivo:** combinar `toString()`, herança e `super`.

---

## Exercício 18 — Desafio final do bloco: catálogo de personagens

Crie uma pequena hierarquia para um sistema de personagens:

```text
Personagem
├── Guerreiro
└── Mago
```

### `Personagem`

Deve possuir:

- nome;
- nível;
- pontos de vida.

### `Guerreiro`

Deve possuir:

- arma;
- força.

### `Mago`

Deve possuir:

- elemento;
- mana.

### Requisitos

1. Use herança corretamente.
2. Utilize construtores em todas as classes.
3. Faça os construtores das subclasses chamarem os da superclasse.
4. Escolha conscientemente onde `private` e `protected` fazem sentido.
5. Crie um método de apresentação na classe base.
6. Sobrescreva esse comportamento nas subclasses.
7. Utilize `super` para reaproveitar o comportamento da classe pai.
8. Sobrescreva `toString()` nas classes.
9. Crie pelo menos dois guerreiros e dois magos.
10. Imprima todos os personagens usando apenas:
   ```java
   System.out.println(personagem);
   ```
11. Adicione pelo menos um terceiro nível de especialização, por exemplo:
   ```text
   Mago
     └── MagoElemental
   ```
12. Faça uma saída final que permita identificar claramente o tipo de cada personagem.

### Regras do desafio

- Não copie a estrutura de nenhum exemplo da aula.
- Não coloque todos os atributos como `public`.
- Não duplique atributos que pertencem à classe pai.
- Não repita no `toString()` da subclasse toda a lógica já existente na superclasse.
- O código deve demonstrar que você entendeu **herança + `super` + `protected` + construtores + sobrescrita + `toString()`**.

### Antes de programar

Desenhe primeiro:

```text
              Personagem
              /        \
             /          \
       Guerreiro        Mago
                         |
                         ↓
                  MagoElemental
```

Depois defina quais atributos pertencem a cada nível.
