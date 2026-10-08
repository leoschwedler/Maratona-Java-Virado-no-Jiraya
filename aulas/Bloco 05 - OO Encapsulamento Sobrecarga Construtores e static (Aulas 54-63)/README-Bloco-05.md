# Bloco 05 — Encapsulamento, Sobrecarga, Construtores e `static`

## Aulas 54–63

Este bloco continua diretamente a Orientação a Objetos e aprofunda uma ideia muito importante: **como controlar o acesso aos dados de um objeto e como organizar sua criação e seus comportamentos**.

As aulas deste bloco são:

- **54** — Modificador de acesso `private`, `get` e `set` pt 01
- **55** — Modificador de acesso `private`, `get` e `set` pt 02
- **56** — Modificador de acesso `private`, `get` e `set` pt 03
- **57** — Sobrecarga de métodos
- **58** — Construtores pt 01
- **59** — Construtores pt 02 — Sobrecarga
- **60** — Blocos de inicialização
- **61** — Modificador `static`
- **62** — Métodos `static`
- **63** — Bloco de inicialização estático

A sequência acima é a sequência real da playlist. fileciteturn28file0L12-L27

---

# Aula 54 — `private`, `get` e `set` pt 01

## 1. O problema que a aula apresenta

Antes desta aula, os atributos estavam sendo acessados diretamente.

Exemplo:

```java
public class Pessoa {
    public String nome;
    public int idade;
}
```

E outra classe poderia fazer:

```java
Pessoa pessoa = new Pessoa();

pessoa.nome = "Goku";
pessoa.idade = -11;
```

O problema é que qualquer código que tenha acesso ao objeto pode alterar diretamente seus atributos.

A aula usa isso para introduzir o conceito de **acoplamento**.

---

## 2. Acoplamento

Acoplamento é, na explicação da aula, o quanto uma classe está conectada/conhece outra.

Quando uma classe externa depende diretamente dos detalhes internos de outra classe, temos uma conexão maior entre elas.

Exemplo:

```java
pessoa.nome = "Goku";
pessoa.idade = 20;
```

O código externo conhece diretamente os atributos de `Pessoa`.

A ideia apresentada é buscar **baixo acoplamento**.

---

## 3. Como reduzir esse acoplamento?

Uma das formas apresentadas é utilizar **modificadores de acesso**.

O primeiro que vamos utilizar aqui é:

```java
private
```

---

## 4. O que significa `private`?

Quando um atributo é `private`, ele fica restrito à própria classe.

Exemplo:

```java
public class Pessoa {
    private String nome;
    private int idade;
}
```

Agora uma classe externa não pode fazer:

```java
pessoa.nome = "Goku";
```

Isso gera erro de compilação porque `nome` possui acesso privado.

---

## 5. `private` não significa que o atributo deixou de existir

O atributo continua existindo no objeto.

O que mudou foi **quem pode acessá-lo diretamente**.

Dentro da própria classe:

```java
public class Pessoa {
    private String nome;
    private int idade;

    public void imprimir() {
        System.out.println(nome);
        System.out.println(idade);
    }
}
```

A própria classe consegue acessar seus atributos privados.

---

## 6. Por que fazer isso?

A aula relaciona isso ao mundo real: detalhes internos de um objeto não precisam ficar expostos para qualquer código.

Em vez de permitir:

```java
pessoa.idade = -11;
```

podemos criar métodos que controlem como o valor será acessado ou alterado.

É aqui que entram os métodos `get` e `set`.

---

## 7. Setter

Um `set` normalmente é utilizado para **atribuir/alterar** um valor.

Exemplo:

```java
public void setNome(String nome) {
    this.nome = nome;
}
```

Agora:

```java
pessoa.setNome("Goku");
```

O código externo não acessa diretamente:

```java
pessoa.nome
```

Ele chama um método público.

---

## 8. Getter

Um `get` normalmente é utilizado para **obter/retornar** um valor.

Exemplo:

```java
public String getNome() {
    return nome;
}
```

Uso:

```java
System.out.println(pessoa.getNome());
```

---

## 9. O padrão `get` e `set`

Para:

```java
private String nome;
```

normalmente temos:

```java
public String getNome() {
    return nome;
}

public void setNome(String nome) {
    this.nome = nome;
}
```

Para:

```java
private int idade;
```

teríamos:

```java
public int getIdade() {
    return idade;
}

public void setIdade(int idade) {
    this.idade = idade;
}
```

---

## 10. Por que usar `this` no setter?

Observe:

```java
private String nome;

public void setNome(String nome) {
    this.nome = nome;
}
```

Existem dois `nome`:

```text
this.nome
   ↓
atributo do objeto

nome
   ↓
parâmetro do método
```

Então:

```java
this.nome = nome;
```

significa:

> coloque o valor recebido pelo parâmetro `nome` no atributo `nome` deste objeto.

---

# Aula 55 — `private`, `get` e `set` pt 02

Nesta aula o foco continua no funcionamento dos getters e setters e no acesso aos atributos privados.

## 1. O objeto ainda possui os atributos

Depois de transformar:

```java
public String nome;
```

em:

```java
private String nome;
```

o atributo continua dentro do objeto.

A diferença é que uma referência externa não pode acessá-lo diretamente.

---

## 2. O acesso acontece através dos métodos

Exemplo:

```java
public class Pessoa {
    private String nome;
    private int idade;

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }
}
```

A classe externa pode fazer:

```java
System.out.println(pessoa.getNome());
System.out.println(pessoa.getIdade());
```

---

## 3. A própria classe possui acesso aos seus atributos

Dentro da classe:

```java
public void imprimir() {
    System.out.println(nome);
    System.out.println(idade);
}
```

Também poderia ser escrito com `this`:

```java
public void imprimir() {
    System.out.println(this.nome);
    System.out.println(this.idade);
}
```

A aula reforça que, dentro do objeto/classe, os atributos e métodos estão disponíveis.

---

## 4. Getter não precisa receber parâmetro

Para obter o valor já existente no objeto:

```java
public String getNome() {
    return nome;
}
```

Não precisamos fazer:

```java
public String getNome(String nome)
```

porque o objetivo é pegar o valor que já está armazenado no objeto.

---

## 5. Setter recebe parâmetro

Para colocar um novo valor:

```java
public void setNome(String nome) {
    this.nome = nome;
}
```

Aqui existe uma informação nova chegando de fora, então o método precisa receber o valor.

---

## 6. A ideia central

Pense assim:

```text
private atributo
      ↓
não pode ser acessado diretamente de fora

get
 ↓
lê/retorna o valor

set
 ↓
altera/atribui o valor
```

---

# Aula 56 — `private`, `get` e `set` pt 03

A terceira aula fecha o assunto mostrando o padrão aplicado aos atributos.

## 1. Criando getters e setters

Para uma classe com vários atributos:

```java
private String nome;
private String cidade;
private double[] salarios;
```

podemos ter métodos seguindo o padrão:

```java
getNome()
setNome()

getCidade()
setCidade()

getSalarios()
setSalarios()
```

O nome do método acompanha o atributo.

---

## 2. Getter retorna

Exemplo:

```java
public String getCidade() {
    return cidade;
}
```

Ele não precisa receber a cidade como parâmetro porque sua função é obter o valor que já está no objeto.

---

## 3. Setter recebe

Exemplo:

```java
public void setCidade(String cidade) {
    this.cidade = cidade;
}
```

O parâmetro traz o novo valor.

---

## 4. Nem todo atributo precisa obrigatoriamente ter `get` e `set`

O ponto importante é entender que `private` controla o acesso.

Os métodos públicos são uma forma de criar uma interface para interagir com os dados.

Isso permite, por exemplo, ter apenas:

```java
public String getNome()
```

se você quiser permitir leitura, mas não fornecer:

```java
public void setNome(...)
```

para alteração.

---

## 5. Por que isso é melhor que acesso público direto?

Compare:

```java
pessoa.idade = -11;
```

com:

```java
pessoa.setIdade(-11);
```

No segundo caso existe um método intermediário.

Esse método pode concentrar regras de alteração posteriormente.

Por exemplo:

```java
public void setIdade(int idade) {
    // regras podem ser colocadas aqui
    this.idade = idade;
}
```

O objetivo desta etapa é entender a ideia de **encapsular o atributo e controlar seu acesso através de métodos**.

---

# Aula 57 — Sobrecarga de métodos

## 1. O que é sobrecarga?

Sobrecarga permite ter métodos com o **mesmo nome**, desde que a lista de parâmetros seja diferente.

Exemplo:

```java
public void init(String nome) {
}

public void init(String nome, int idade) {
}
```

Temos dois métodos chamados:

```java
init
```

mas com parâmetros diferentes.

---

## 2. O que pode mudar?

A sobrecarga pode ocorrer quando muda:

- a quantidade de parâmetros;
- o tipo dos parâmetros.

Exemplo por quantidade:

```java
public void init(String nome) {
}

public void init(String nome, int idade) {
}
```

Exemplo por tipo:

```java
public void init(int numero) {
}

public void init(String texto) {
}
```

---

## 3. Apenas mudar o retorno não cria sobrecarga

Isto não é suficiente:

```java
public int calcula(int valor) {
    return valor;
}

public double calcula(int valor) {
    return valor;
}
```

Os parâmetros são iguais.

Portanto, para a sobrecarga, a diferença relevante está na lista de parâmetros.

---

## 4. Por que utilizar sobrecarga?

Imagine que queremos inicializar um objeto de formas diferentes.

Podemos ter:

```java
init(String nome)
```

ou:

```java
init(String nome, int idade)
```

Em vez de criar nomes completamente diferentes, podemos manter o mesmo nome e fornecer diferentes formas de chamada.

---

## 5. Como Java decide qual método chamar?

A chamada:

```java
obj.init("Goku");
```

procura a versão compatível com um argumento `String`.

A chamada:

```java
obj.init("Goku", 30);
```

procura a versão compatível com:

```text
String
int
```

---

# Aula 58 — Construtores pt 01

## 1. O que é um construtor?

O construtor está relacionado diretamente à **criação do objeto**.

Quando fazemos:

```java
Pessoa pessoa = new Pessoa();
```

estamos criando um objeto.

O construtor participa desse processo de construção/inicialização.

---

## 2. Como reconhecer um construtor?

O construtor possui o **mesmo nome da classe**.

Exemplo:

```java
public class Pessoa {

    public Pessoa() {
    }
}
```

O nome:

```java
Pessoa
```

é o mesmo nome da classe.

---

## 3. Construtor não possui tipo de retorno

Isto é um construtor:

```java
public Pessoa() {
}
```

Isto é um método:

```java
public void Pessoa() {
}
```

Ao colocar `void`, deixou de ser construtor e passou a ser um método chamado `Pessoa`.

---

## 4. O construtor pode receber parâmetros

Exemplo:

```java
public Pessoa(String nome, int idade) {
    this.nome = nome;
    this.idade = idade;
}
```

Agora podemos criar:

```java
Pessoa pessoa = new Pessoa("Goku", 30);
```

Os dados são fornecidos no momento da criação.

---

## 5. Inicializando o objeto no construtor

Sem construtor parametrizado, poderíamos fazer:

```java
Pessoa pessoa = new Pessoa();

pessoa.setNome("Goku");
pessoa.setIdade(30);
```

Com um construtor:

```java
Pessoa pessoa = new Pessoa("Goku", 30);
```

O construtor permite concentrar a inicialização durante a criação do objeto.

---

## 6. Construtor padrão e criação automática

A aula também mostra a relação entre construtor e criação de objetos.

Quando não declaramos nenhum construtor, Java pode fornecer um construtor padrão sem argumentos.

Mas quando você declara um construtor, essa situação muda: você passa a controlar quais construtores existem.

---

# Aula 59 — Construtores pt 02 — Sobrecarga

## 1. Construtores também podem ser sobrecarregados

A mesma ideia de sobrecarga dos métodos se aplica aos construtores.

Exemplo:

```java
public Pessoa() {
}

public Pessoa(String nome) {
}

public Pessoa(String nome, int idade) {
}
```

Temos três formas de criar o objeto.

---

## 2. Diferentes formas de criação

Podemos fazer:

```java
Pessoa p1 = new Pessoa();
```

ou:

```java
Pessoa p2 = new Pessoa("Goku");
```

ou:

```java
Pessoa p3 = new Pessoa("Goku", 30);
```

Java identifica qual construtor utilizar através dos argumentos.

---

## 3. Construtor não é método

Embora exista sobrecarga de construtores, não confunda:

```java
public Pessoa(...)
```

com:

```java
public void Pessoa(...)
```

O primeiro é construtor.

O segundo é método.

O construtor não possui tipo de retorno.

---

## 4. `this` e construtores

Assim como nos setters:

```java
public Pessoa(String nome, int idade) {
    this.nome = nome;
    this.idade = idade;
}
```

`this.nome` representa o atributo do objeto.

`nome` representa o parâmetro recebido.

---

## 5. Responsabilidade do construtor

O construtor pode receber os dados necessários para que o objeto seja criado já com seu estado inicial definido.

Isso evita depender de várias chamadas posteriores para montar o objeto.

---

# Aula 60 — Blocos de inicialização

## 1. O que é um bloco de inicialização?

A aula apresenta o **bloco de inicialização de instância**.

Ele é escrito como um bloco de código dentro da classe, sem nome de método:

```java
{
    // código
}
```

---

## 2. Quando ele é executado?

O bloco de inicialização de instância é executado quando um novo objeto é criado.

Se você criar três objetos, ele será executado três vezes.

Exemplo conceitual:

```java
public class Anime {

    {
        System.out.println("Bloco de inicialização");
    }
}
```

Se:

```java
new Anime();
new Anime();
new Anime();
```

o bloco será executado três vezes.

---

## 3. Ordem de inicialização

A aula mostra que a inicialização acontece antes da execução do construtor.

A ideia simplificada é:

```text
criação do objeto
      ↓
inicialização da instância
      ↓
construtor
```

Isso é importante porque o bloco de inicialização faz parte do processo de construção do objeto.

---

## 4. Por que usar?

A aula apresenta um exemplo em que determinada lógica poderia ser executada para todos os objetos.

Se uma lógica pertence à inicialização de cada instância, ela pode ficar no bloco de inicialização.

Porém, se algo deveria ser executado apenas uma vez para a classe inteira, o bloco de instância não é adequado. É justamente isso que leva ao próximo conceito: `static`.

---

# Aula 61 — Modificador `static`

## 1. O que significa `static`?

Até aqui, muitos atributos pertenciam a cada objeto.

Exemplo:

```java
Carro carro1 = new Carro();
Carro carro2 = new Carro();
```

Cada objeto possui seu próprio estado de instância.

Quando usamos:

```java
static
```

o atributo passa a pertencer à **classe**, e não individualmente a cada objeto.

---

## 2. Atributo de instância x atributo estático

Sem `static`:

```java
private int velocidade;
```

Cada objeto possui sua própria velocidade.

Com `static`:

```java
private static int velocidadeLimite;
```

existe um valor associado à classe.

A aula usa a ideia de uma velocidade limite que deve ser compartilhada pelos objetos.

---

## 3. Um valor compartilhado

Imagine:

```java
Carro carro1 = new Carro();
Carro carro2 = new Carro();
Carro carro3 = new Carro();
```

Se `velocidadeLimite` for `static`, alterar esse valor afeta o valor compartilhado pela classe, e não uma cópia independente em cada objeto.

---

## 4. Acesso através da classe

Para um atributo estático, a aula demonstra o acesso através do nome da classe.

Exemplo:

```java
Carro.velocidadeLimite
```

A ideia é que o atributo pertence à classe.

---

## 5. `static` e objetos

Um ponto importante da aula:

```text
atributo de instância
→ pertence ao objeto

atributo static
→ pertence à classe
```

Por isso não faz sentido pensar em cada objeto tendo uma cópia independente do atributo estático.

---

# Aula 62 — Métodos `static`

## 1. Criando um método estático

A declaração segue a estrutura:

```java
public static void imprime() {
}
```

O `static` indica que o método pertence à classe.

---

## 2. Chamando sem criar objeto

Como o método é da classe, podemos chamá-lo através da classe.

Exemplo:

```java
Carro.imprime();
```

Não é necessário criar um objeto de `Carro` somente para acessar um método estático.

---

## 3. A regra mais importante da aula

Um método estático **não pode acessar diretamente atributos de instância**.

Por exemplo:

```java
private String nome;
```

é atributo de instância.

Dentro de:

```java
public static void imprime() {
}
```

não podemos simplesmente acessar:

```java
nome
```

---

## 4. Por que?

A explicação da aula parte da existência do objeto.

Um método estático pode ser utilizado sem que exista uma instância específica.

Então não existe garantia de qual objeto deveria fornecer o atributo:

```java
nome
```

Se não existe um objeto específico, não existe um `this` representando uma instância específica.

---

## 5. `this` não existe em contexto estático

Como `this` representa o objeto/instância atual, ele não pode ser utilizado em um contexto estático.

Por isso isto não funciona:

```java
public static void imprime() {
    System.out.println(this.nome);
}
```

---

## 6. O contrário é possível

Um método de instância pode acessar membros estáticos.

Isso ocorre porque o membro estático pertence à classe e existe independentemente de uma instância específica.

---

## 7. Quando utilizar método estático?

A aula apresenta como referência uma boa prática:

Um método pode ser `static` quando não precisa acessar atributos de instância.

Um exemplo é uma calculadora cujos métodos recebem os valores por parâmetros e não precisam armazenar estado em um objeto específico.

---

# Aula 63 — Bloco de inicialização estático

## 1. O problema

Na aula anterior vimos o bloco de inicialização de instância:

```java
{
    // código
}
```

Ele é executado toda vez que um objeto é criado.

Mas imagine que determinada inicialização precise acontecer **uma única vez**, independentemente da quantidade de objetos.

---

## 2. Bloco estático

Para isso existe o bloco de inicialização estático:

```java
static {
    // código
}
```

---

## 3. Quantas vezes ele executa?

O bloco de inicialização estático é executado **uma vez quando a classe é carregada pela JVM**.

Portanto, criar vários objetos não faz o bloco estático executar novamente.

Conceitualmente:

```text
classe carregada
      ↓
bloco static executado uma vez
      ↓
objetos podem ser criados
```

---

## 4. Comparação

### Bloco de instância

```java
{
    System.out.println("Instância");
}
```

Executa quando uma instância é criada.

### Bloco estático

```java
static {
    System.out.println("Classe");
}
```

Executa uma vez durante o carregamento da classe.

---

## 5. Ordem dos blocos estáticos

A aula também mostra que múltiplos blocos estáticos são executados na ordem em que aparecem na classe.

Exemplo:

```java
static {
    System.out.println("1");
}

static {
    System.out.println("2");
}

static {
    System.out.println("3");
}
```

A execução segue a ordem:

```text
1
2
3
```

---

# Resumo geral do bloco

## Encapsulamento

```java
private String nome;
```

Protege o atributo contra acesso direto externo.

```java
public String getNome()
```

Permite obter o valor.

```java
public void setNome(String nome)
```

Permite alterar o valor.

---

## Sobrecarga

Mesmo nome de método/construtor + parâmetros diferentes:

```java
init()
init(String nome)
init(String nome, int idade)
```

---

## Construtor

Mesmo nome da classe e sem retorno:

```java
public Pessoa(String nome) {
    this.nome = nome;
}
```

É utilizado na construção do objeto.

---

## `static`

Pertence à classe, não a uma instância específica:

```java
private static int velocidadeLimite;
```

---

## Método `static`

Pode ser chamado sem criar objeto:

```java
Carro.imprime();
```

E não pode acessar diretamente atributos de instância.

---

## Blocos de inicialização

Instância:

```java
{
}
```

Executa na criação de cada objeto.

Estático:

```java
static {
}
```

Executa uma vez no carregamento da classe.

---

# Checklist final

Antes de avançar para o próximo bloco, você deve conseguir explicar sem consultar:

- [ ] O que é acoplamento
- [ ] Por que queremos reduzir acoplamento
- [ ] O que `private` faz
- [ ] Por que uma classe ainda consegue acessar seu próprio atributo `private`
- [ ] O que é um getter
- [ ] O que é um setter
- [ ] Por que getter normalmente não precisa de parâmetro
- [ ] Por que setter normalmente recebe parâmetro
- [ ] Por que usamos `this` em muitos setters
- [ ] O que é sobrecarga
- [ ] O que pode mudar para existir sobrecarga
- [ ] Por que mudar somente o retorno não cria sobrecarga
- [ ] O que é um construtor
- [ ] Como reconhecer um construtor
- [ ] Por que construtor não possui retorno
- [ ] Como sobrecarregar construtores
- [ ] O que é um bloco de inicialização de instância
- [ ] Quando ele é executado
- [ ] O que `static` significa
- [ ] Diferença entre atributo de instância e atributo estático
- [ ] Por que um método `static` pode ser chamado sem objeto
- [ ] Por que um método `static` não pode acessar diretamente atributos de instância
- [ ] Por que `this` não pode ser usado em método estático
- [ ] O que é um bloco `static`
- [ ] Quando o bloco `static` é executado
- [ ] Ordem de execução de múltiplos blocos estáticos
