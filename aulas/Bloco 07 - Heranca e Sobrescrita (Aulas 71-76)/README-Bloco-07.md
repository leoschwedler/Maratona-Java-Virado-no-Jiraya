# Bloco 07 — Herança e Sobrescrita

## Aulas do bloco

- Aula 71 — Herança pt 01
- Aula 72 — Herança pt 02 — `super`
- Aula 73 — Herança pt 03 — `protected`
- Aula 74 — Herança pt 04 — Construtores
- Aula 75 — Herança pt 05 — Sequência de inicialização
- Aula 76 — Sobrescrita do método `toString`

> **Metodologia:** estudar uma aula por vez. Leia a explicação da aula, faça os 3 exercícios daquela aula e só avance depois de corrigir seu código.

---

# Aula 71 — Herança pt 01

## 1. O que é herança?

Herança permite criar uma classe especializada a partir de outra classe.

Imagine:

```text
Pessoa
  ↑
  |
Funcionário
```

Um `Funcionário` é uma `Pessoa`, mas possui características próprias.

A classe `Pessoa` é a **superclasse** (classe mãe/base).

A classe `Funcionário` é a **subclasse** (classe filha).

Em Java:

```java
public class Funcionario extends Pessoa {
}
```

O `extends` indica a relação de herança.

## 2. O que a subclasse recebe?

A subclasse passa a fazer parte da hierarquia da superclasse e pode utilizar os membros que são acessíveis para ela.

Exemplo conceitual:

```java
public class Pessoa {
    private String nome;
    private String cpf;
}

public class Funcionario extends Pessoa {
    private double salario;
}
```

A ideia é:

```text
Pessoa
├── nome
└── cpf

Funcionario
└── salario
```

Mas atenção: `private` continua sendo privado da classe que o declarou. A subclasse não acessa diretamente um atributo `private` da superclasse como se fosse seu.

## 3. Herança representa "é um"

Uma boa forma de identificar herança é perguntar:

> "Um Funcionário é uma Pessoa?"

Sim.

```text
Funcionário é uma Pessoa
```

Já:

```text
Funcionário possui um endereço
```

representa associação, não herança.

Essa diferença é importante:

```text
É um       → herança
Possui um  → associação
```

## 4. Generalização e especialização

A superclasse contém características mais gerais.

A subclasse acrescenta características mais específicas.

```text
             Pessoa
            /      \
           /        \
    Funcionário     Aluno
```

`Pessoa` representa o que é comum.

`Funcionário` e `Aluno` representam especializações.

## 5. Herança não copia código simplesmente

É importante não imaginar que o Java pega o código da superclasse e cola dentro da subclasse.

Existe uma relação entre as classes.

A subclasse faz parte da hierarquia e pode utilizar os membros que estão acessíveis para ela.

## 6. Java não possui herança múltipla de classes

Uma classe não pode estender duas classes ao mesmo tempo:

```java
// Não permitido
public class Funcionario extends Pessoa, Empresa {
}
```

Nesta parte do curso, a herança é apresentada entre uma classe e sua superclasse.

---

# Aula 72 — Herança pt 02 — `super`

## 1. Para que serve `super`?

`super` permite fazer referência à superclasse.

Por exemplo:

```java
super.nome
```

faz referência ao membro `nome` da superclasse quando esse acesso é permitido.

Também podemos usar:

```java
super.metodo();
```

para chamar um método da superclasse.

## 2. `this` x `super`

Essa comparação é fundamental:

```java
this.nome
```

refere-se ao contexto da classe atual.

```java
super.nome
```

refere-se ao contexto da superclasse.

Visualmente:

```text
Pessoa
  ↑
  |
Funcionario
```

Dentro de `Funcionario`:

```text
this  → Funcionario
super → Pessoa
```

## 3. `super` não significa criar outro objeto

Isto é importante.

`super` não cria uma nova `Pessoa`.

Ele é uma forma de acessar a parte da superclasse dentro da hierarquia do objeto atual.

## 4. Chamando método da superclasse

Suponha:

```java
public class Pessoa {
    public void imprimir() {
        System.out.println("Pessoa");
    }
}
```

E:

```java
public class Funcionario extends Pessoa {
    public void imprimir() {
        super.imprimir();
        System.out.println("Funcionario");
    }
}
```

A chamada:

```java
super.imprimir();
```

executa a implementação da superclasse.

Isso é especialmente útil quando a subclasse quer manter o comportamento original e adicionar algo depois.

## 5. A palavra `super` ajuda a deixar explícita a intenção

Quando existe uma implementação na classe mãe e outra na filha, `super` permite dizer claramente:

> "Agora quero executar a versão da superclasse."

---

# Aula 73 — Herança pt 03 — `protected`

## 1. Por que existe `protected`?

Você já viu `private`.

Com `private`, o membro pertence à classe e não pode ser acessado diretamente pela subclasse.

`protected` permite que a própria classe e classes da hierarquia tenham acesso ao membro de acordo com as regras de acesso do Java.

Exemplo:

```java
public class Pessoa {
    protected String nome;
}
```

Uma subclasse pode utilizar esse membro:

```java
public class Funcionario extends Pessoa {

    public void imprimir() {
        System.out.println(nome);
    }
}
```

## 2. Comparando `private` e `protected`

```text
private
→ acesso restrito à própria classe

protected
→ permite acesso dentro da hierarquia, observadas as regras de acesso
```

O objetivo da aula é mostrar como `protected` pode facilitar o trabalho das subclasses.

## 3. Não confunda `protected` com `public`

`protected` não significa "qualquer classe pode acessar".

`public` possui uma visibilidade muito mais ampla.

Use `protected` quando fizer sentido permitir acesso controlado para a hierarquia.

## 4. Herança múltipla

A aula também reforça que Java não permite uma classe herdar de múltiplas classes.

Isso:

```java
class A extends B, C
```

não é válido.

---

# Aula 74 — Herança pt 04 — Construtores

## 1. Construtores também participam da herança

Uma subclasse não simplesmente ignora a construção da superclasse.

Quando um objeto da subclasse é criado, a inicialização da parte herdada precisa acontecer.

Por isso os construtores das classes da hierarquia possuem uma relação importante.

## 2. `super()` no construtor

Dentro do construtor da subclasse podemos chamar o construtor da superclasse:

```java
public Funcionario(String nome) {
    super(nome);
}
```

Nesse exemplo, `super(nome)` chama o construtor correspondente da superclasse.

## 3. `super(...)` deve estar no início

Quando utilizado em um construtor, `super(...)` deve ser a primeira instrução do construtor.

Exemplo:

```java
public Funcionario(String nome) {
    super(nome);
    // restante da inicialização
}
```

## 4. E se eu não escrever `super()`?

Existe uma chamada implícita ao construtor sem argumentos da superclasse quando as condições para isso existem.

Por isso, se a superclasse tiver apenas:

```java
public Pessoa(String nome) {
    this.nome = nome;
}
```

e não possuir construtor sem argumentos, a subclasse precisará chamar explicitamente um construtor compatível:

```java
super(nome);
```

## 5. Construtores não são sobrescritos

Construtor não é método comum e não é herdado da mesma maneira.

A subclasse precisa declarar seus próprios construtores conforme suas necessidades.

---

# Aula 75 — Herança pt 05 — Sequência de inicialização

Esta é uma das partes mais difíceis do bloco.

A aula relaciona herança com:

- atributos;
- blocos de inicialização;
- blocos `static`;
- construtores.

## 1. Por que existe uma sequência?

Quando criamos um objeto de uma subclasse, a JVM precisa inicializar corretamente a hierarquia.

A superclasse participa do processo antes da construção da parte específica da subclasse.

## 2. Blocos `static`

Os blocos estáticos estão ligados à inicialização da classe.

Na hierarquia, a inicialização estática da superclasse ocorre antes da inicialização estática da subclasse.

De forma simplificada:

```text
static da superclasse
        ↓
static da subclasse
```

Isso está relacionado ao carregamento/inicialização das classes.

## 3. Inicialização de instância

Quando um objeto é criado, a inicialização da superclasse ocorre antes da construção da subclasse.

Uma visão simplificada para memorizar:

```text
Superclasse
  ↓
atributos / blocos de inicialização
  ↓
construtor da superclasse
  ↓
Subclasse
  ↓
atributos / blocos de inicialização
  ↓
construtor da subclasse
```

A ordem exata deve ser observada considerando a inicialização de classes, campos e blocos na hierarquia.

## 4. Por que isso importa?

Porque você pode ter código sendo executado em:

```java
static { ... }

{ ... }

public Pessoa() { ... }

public Funcionario() { ... }
```

Se não souber a ordem, fica difícil prever o resultado de um programa.

## 5. Técnica para estudar a sequência

Quando encontrar um exercício desse tipo, não tente responder de cabeça.

Faça uma lista:

```text
1. static da superclasse
2. static da subclasse
3. inicialização da instância da superclasse
4. construtor da superclasse
5. inicialização da instância da subclasse
6. construtor da subclasse
```

Depois adapte a lista ao código concreto.

---

# Aula 76 — Sobrescrita do método `toString`

## 1. O problema

Quando imprimimos diretamente um objeto:

```java
System.out.println(objeto);
```

o Java precisa transformar aquele objeto em texto.

É aí que entra:

```java
toString()
```

## 2. Sobrescrever `toString`

Podemos fornecer uma representação útil do nosso objeto:

```java
@Override
public String toString() {
    return "Nome: " + nome;
}
```

Assim:

```java
System.out.println(pessoa);
```

passa a produzir uma representação definida pela classe.

## 3. `@Override`

A anotação:

```java
@Override
```

indica que estamos sobrescrevendo um método herdado.

Ela também ajuda o compilador a verificar se a assinatura realmente corresponde a um método da superclasse.

## 4. Por que isso é útil?

Sem sobrescrever `toString`, imprimir objetos geralmente não mostra os dados do domínio da forma que queremos.

Com `toString`, podemos criar uma representação útil para:

- depuração;
- logs;
- testes;
- visualização dos dados do objeto.

## 5. Sobrescrita x sobrecarga

Não confunda:

**Sobrescrita:**

```text
mesma assinatura
nova implementação na subclasse
```

**Sobrecarga:**

```text
mesmo nome
parâmetros diferentes
```

Essa distinção já apareceu anteriormente no curso e volta a ser importante aqui.

---

# Resumo do bloco

```text
Herança
   ↓
extends
   ↓
Superclasse ← Subclasse
   ↓
super
   ↓
protected
   ↓
construtores
   ↓
sequência de inicialização
   ↓
sobrescrita
   ↓
toString()
```

## Checklist antes de avançar

Você deve conseguir explicar com suas palavras:

- O que é uma superclasse?
- O que é uma subclasse?
- O que significa `extends`?
- Como identificar uma relação de herança?
- Qual a diferença entre herança e associação?
- Qual a diferença entre `this` e `super`?
- Para que serve `super.metodo()`?
- Para que serve `protected`?
- Por que `super(...)` aparece nos construtores?
- Por que a ordem de inicialização importa?
- O que `@Override` indica?
- Qual a diferença entre sobrescrita e sobrecarga?
- Por que sobrescrever `toString()` é útil?
