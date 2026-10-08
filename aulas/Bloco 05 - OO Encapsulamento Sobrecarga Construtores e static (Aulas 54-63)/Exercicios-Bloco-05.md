# Exercícios — Bloco 05
## Encapsulamento, Sobrecarga, Construtores e `static`

> **Metodologia deste bloco:** você deve estudar **uma aula por vez**.
>
> 1. Leia no README somente a aula que vai estudar.
> 2. Faça os **3 exercícios daquela aula**.
> 3. Me envie seu código.
> 4. Eu corrijo apontando os problemas e dando dicas, sem entregar a solução de imediato.
> 5. Só depois de entender/corrigir a aula, avance para a próxima.
>
> Cada aula possui:
> - 🟢 **1 exercício fácil**
> - 🟡 **1 exercício médio**
> - 🔴 **1 exercício difícil**
>
> Os exercícios são propositalmente mais desafiadores que os blocos anteriores. O objetivo não é apenas fazer o código funcionar, mas conseguir explicar **por que** você escolheu cada solução.

---

# Aula 54 — `private`, get e set pt. 01

## 🟢 1. Produto encapsulado

Crie uma classe `Aluno` com:

- `private String nome`
- `private double preco`

Crie getters e setters para os dois atributos.

No `main`:

1. Crie um produto.
2. Defina nome e preço usando setters.
3. Mostre os valores usando getters.
4. Tente acessar diretamente `produto.nome` e `produto.preco` para observar o erro de acesso.

**Objetivo:** entender na prática por que o atributo `private` não pode ser acessado diretamente de outra classe.

---

## 🟡 2. Conta bancária protegida

Crie uma classe `Conta` com:

- `private String titular`
- `private double saldo`

Implemente:

- getter para `titular`
- setter para `titular`
- getter para `saldo`

**Não crie setter para `saldo`.**

Crie também:

```java
public void depositar(double valor)
public void sacar(double valor)
```

Regras:

- depósito só pode aceitar valores positivos;
- saque não pode ser maior que o saldo;
- o saldo só pode ser alterado internamente pela própria classe.

No `main`, tente modificar o saldo diretamente e depois faça as operações pelos métodos.

**Objetivo:** perceber que encapsulamento não significa simplesmente criar getter e setter para tudo.

---

## 🔴 3. Encapsulamento com controle de acesso

Crie uma classe `Usuario` com:

- `private String nome`
- `private String email`
- `private int idade`

O usuário deve conseguir alterar o nome e o email.

A idade, porém, deve obedecer às regras:

- não pode ser menor que 0;
- não pode ser maior que 150.

Crie o setter da idade para impedir valores inválidos.

Depois:

1. tente inserir uma idade inválida;
2. verifique o comportamento;
3. tente acessar `idade` diretamente de outra classe;
4. explique por que o atributo continua existindo no objeto mesmo estando `private`.

**Desafio:** pense no `private` como uma forma de controlar **como** outras classes interagem com o estado do objeto.

---

# Aula 55 — `private`, get e set pt. 02

## 🟢 1. Getter e setter na prática

Crie uma classe `Aluno` com:

- `private String nome`
- `private int idade`

Crie getters e setters.

No `main`, crie dois alunos e altere os dados de cada um.

**Objetivo:** perceber que cada objeto possui seu próprio estado, mesmo quando os atributos são `private`.

---

## 🟡 2. Estado interno protegido

Crie uma classe `Carro` com:

- `private String modelo`
- `private int velocidade`

Crie:

- getter para modelo;
- getter para velocidade;
- setter para modelo;
- método `acelerar()`;
- método `frear()`.

Regras:

- `acelerar()` aumenta a velocidade em 10;
- `frear()` diminui a velocidade em 10;
- velocidade nunca pode ficar negativa;
- não crie setter para velocidade.

No `main`, tente controlar a velocidade diretamente e depois faça tudo pelos métodos.

**Objetivo:** usar métodos públicos como uma interface controlada para o estado privado.

---

## 🔴 3. Escolha consciente de getters e setters

Crie uma classe `Funcionario` com:

- `private String nome`
- `private double salario`
- `private int nivel`

Pense antes de programar:

- quais atributos realmente precisam de getter?
- quais precisam de setter?
- algum atributo deveria ser alterado somente por um método específico?

Implemente uma solução em que:

- o nome pode ser consultado e alterado;
- o salário pode ser consultado, mas não alterado diretamente;
- o nível pode ser consultado;
- o salário pode ser alterado por um método `aumentarSalario(double percentual)`.

**Objetivo:** deixar de pensar em getter/setter como uma obrigação automática e começar a pensar em encapsulamento.

---

# Aula 56 — `private`, get e set pt. 03

## 🟢 1. Padronização

Crie uma classe `Pessoa` com:

- `private String nome`
- `private int idade`
- `private double altura`

Crie os getters e setters seguindo o padrão convencional de Java.

No `main`, preencha e consulte todos os atributos.

---

## 🟡 2. Encapsulamento sem setter para tudo

Crie uma classe `ContaBancaria` com:

- `private String titular`
- `private double saldo`
- `private boolean ativa`

Implemente:

- getter/setter de titular;
- getter de saldo;
- getter de ativa;
- `depositar`;
- `sacar`;
- `ativarConta`;
- `desativarConta`.

Não crie setter para `saldo` nem para `ativa`.

**Objetivo:** praticar a decisão de quais operações devem ser permitidas externamente.

---

## 🔴 3. Refatoração de uma classe mal encapsulada

Crie primeiro uma classe `Pedido` com atributos públicos:

- `String cliente`
- `double valor`
- `boolean pago`

Crie um `main` que altere tudo diretamente.

Depois refatore a classe:

- torne os atributos `private`;
- crie somente os getters necessários;
- remova a possibilidade de alterar `valor` diretamente;
- crie métodos apropriados para alterar o estado do pedido.

**Objetivo:** perceber na prática o problema que o encapsulamento resolve.

---

# Aula 57 — Sobrecarga de métodos

## 🟢 1. Sobrecarga simples

Crie uma classe `Calculadora` com métodos:

```java
somar(int a, int b)
somar(double a, double b)
```

Teste os dois.

Depois crie:

```java
somar(int a, int b, int c)
```

**Objetivo:** identificar o que caracteriza uma sobrecarga.

---

## 🟡 2. Descobrindo qual método Java escolhe

Crie uma classe com métodos sobrecarregados:

```java
mostrar(int valor)
mostrar(double valor)
mostrar(String valor)
mostrar(int a, int b)
```

Faça chamadas usando diferentes tipos de argumentos.

Depois tente criar duas versões que diferenciem **somente o retorno**:

```java
int calcular(int valor)
double calcular(int valor)
```

Observe o erro e explique por que isso não é considerado sobrecarga.

---

## 🔴 3. Sistema de mensagens sobrecarregado

Crie uma classe `Mensagem` com um método `enviar` sobrecarregado para aceitar:

- somente texto;
- texto + destinatário;
- texto + destinatário + prioridade.

Cada versão deve produzir uma mensagem diferente no console.

Depois adicione uma quarta versão que receba dois destinatários.

**Desafio:** tente organizar as sobrecargas para evitar duplicação desnecessária de lógica.

---

# Aula 58 — Construtores pt. 01

## 🟢 1. Primeiro construtor

Crie uma classe `Pessoa` com:

- `private String nome`
- `private int idade`

Crie um construtor que receba nome e idade.

No `main`, crie duas pessoas usando o construtor.

Não inicialize os atributos manualmente depois da criação.

---

## 🟡 2. Construtor x inicialização manual

Crie uma classe `Aluno`.

Faça uma versão em que o objeto é criado e depois recebe os atributos por setters.

Depois altere a classe para receber os valores no construtor.

Compare os dois modelos e explique qual estado o objeto possui imediatamente após o `new`.

Também experimente declarar um construtor e verificar o que acontece com a possibilidade de usar `new Produto()`.

---

## 🔴 3. Objeto sempre válido

Crie uma classe `Funcionario` com:

- `private String nome`
- `private double salario`

O objeto deve ser criado obrigatoriamente com nome e salário.

Regras:

- nome não pode ser vazio;
- salário não pode ser negativo.

Faça essas validações no construtor.

Depois tente criar objetos inválidos e observe o comportamento.

**Objetivo:** usar o construtor para estabelecer o estado inicial do objeto.

---

# Aula 59 — Construtores pt. 02 — Sobrecarga

## 🟢 1. Dois construtores

Crie uma classe `Aluno` com:

- `nome`
- `idade`

Crie:

```java
Aluno()
Aluno(String nome)
Aluno(String nome, int idade)
```

Teste os três.

---

## 🟡 2. Construtores com estados diferentes

Crie uma classe `Livro` com:

- `titulo`
- `autor`
- `paginas`

Crie três construtores:

1. sem argumentos;
2. título e autor;
3. título, autor e páginas.

Depois crie objetos usando os três construtores e mostre os estados.

**Objetivo:** entender que construtores também podem ser sobrecarregados.

---

## 🔴 3. Construtores + `this`

Crie uma classe `Carro` com:

- `modelo`
- `ano`
- `preco`

Crie três construtores:

```java
Carro()
Carro(String modelo)
Carro(String modelo, int ano, double preco)
```

Faça os construtores de forma organizada, evitando repetir inicializações desnecessariamente.

**Desafio:** utilize `this(...)` para fazer um construtor chamar outro.

Depois explique a diferença entre:

```java
this.modelo = modelo;
```

e

```java
this(...);
```

---

# Aula 60 — Blocos de inicialização

## 🟢 1. Ordem de execução

Crie uma classe `Anime` contendo:

- um bloco de inicialização;
- um construtor;
- um método `imprimir`.

Coloque mensagens diferentes em cada parte.

Crie um objeto e descubra a ordem de execução.

---

## 🟡 2. Vários objetos

Crie uma classe `Anime` com um bloco de inicialização que imprime:

```text
Bloco de inicialização
```

Crie um construtor que imprime:

```text
Construtor
```

No `main`, crie quatro objetos.

Antes de executar, escreva no papel a sequência esperada de mensagens.

Depois compare com a execução.

**Objetivo:** perceber que o bloco de inicialização de instância está relacionado à criação de cada objeto.

---

## 🔴 3. Estado inicial usando bloco + construtor

Crie uma classe `Personagem` com:

- `nome`
- `nivel`
- `vida`

Use um bloco de inicialização para definir um estado inicial padrão.

Depois use construtores sobrecarregados para permitir diferentes formas de criação.

Crie pelo menos quatro objetos.

**Desafio:** explique exatamente quantas vezes o bloco de inicialização executou e quantas vezes cada construtor executou.

---

# Aula 61 — Modificador `static`

## 🟢 1. Atributo compartilhado

Crie uma classe `Carro` com:

- `private String modelo`
- `private static double velocidadeLimite`

Crie três carros.

Altere a velocidade limite e observe o resultado nos três objetos.

**Objetivo:** perceber a diferença entre atributo de instância e atributo `static`.

---

## 🟡 2. Contador de objetos

Crie uma classe `Aluno` com:

```java
private static int quantidade;
```

Toda vez que um aluno for criado, incremente esse contador.

Crie cinco alunos e mostre a quantidade.

**Objetivo:** utilizar um atributo pertencente à classe para representar uma informação compartilhada.

---

## 🔴 3. Estado individual x estado compartilhado

Crie uma classe `Jogo` com:

- `private String jogador`
- `private int pontos`
- `private static int jogadoresCriados`
- `private static int pontuacaoMaxima`

Faça um sistema em que:

- cada objeto tenha seus próprios pontos;
- a quantidade criada seja compartilhada;
- a maior pontuação seja compartilhada;
- ao alterar os pontos de um jogador, a pontuação máxima seja atualizada quando necessário.

**Desafio:** explique quais dados pertencem ao objeto e quais pertencem à classe.

---

# Aula 62 — Static Methods

## 🟢 1. Método estático simples

Crie uma classe `Calculadora` com:

```java
public static int somar(int a, int b)
public static int multiplicar(int a, int b)
```

Chame os métodos sem criar um objeto `Calculadora`.

---

## 🟡 2. Instância x `static`

Crie uma classe `Usuario` com:

- `private String nome`;
- método de instância `imprimirNome()`;
- método `static` `mostrarMensagem()`.

No `main`, chame cada método da forma apropriada.

Depois tente chamar o método de instância diretamente pela classe e observe o erro.

---

## 🔴 3. Investigando o limite do `static`

Crie uma classe:

```java
public class Pessoa {
    private String nome;

    public static void imprimirNome() {
        System.out.println(nome);
    }
}
```

O código não deve compilar.

Sua tarefa é:

1. descobrir exatamente por que isso acontece;
2. transformar `imprimirNome()` em método de instância;
3. criar uma segunda solução mantendo o método como `static`, mas recebendo o nome como parâmetro;
4. explicar por que as duas soluções são diferentes.

**Objetivo:** entender que um método `static` não possui um objeto específico associado a ele.

---

# Aula 63 — Bloco de inicialização estático

## 🟢 1. Primeiro `static {}`

Crie uma classe com:

```java
static {
    System.out.println("Bloco estático");
}
```

Crie um objeto da classe.

Observe quando a mensagem aparece.

Depois crie um segundo objeto e veja se o bloco aparece novamente.

---

## 🟡 2. Dois blocos estáticos

Crie uma classe contendo dois blocos:

```java
static {
    System.out.println("Primeiro");
}

static {
    System.out.println("Segundo");
}
```

Crie objetos e observe a ordem.

Depois coloque um bloco de inicialização de instância e um construtor.

Descubra a ordem entre:

1. bloco estático;
2. bloco de instância;
3. construtor.

---

## 🔴 3. Sistema de configuração da aplicação

Crie uma classe `Configuracao` com:

- `private static String ambiente`;
- getter e setter estáticos;
- um bloco `static {}` responsável por definir um valor inicial para `ambiente`.

No `main`:

1. consulte o ambiente antes de criar qualquer objeto;
2. crie três objetos;
3. altere o ambiente por meio do método `static`;
4. consulte o ambiente novamente através dos objetos ou da classe;
5. observe que o estado é compartilhado;
6. adicione um bloco de inicialização de instância e compare sua execução com o bloco estático.

**Desafio:** explique a diferença entre:

```java
{
    // inicialização de instância
}
```

e:

```java
static {
    // inicialização da classe
}
```

---

# 🧠 Revisão final do bloco

Depois de terminar todas as aulas, tente responder **sem consultar o README**:

1. O que significa um atributo ser `private`?
2. Por que usamos getters e setters?
3. Todo atributo `private` precisa obrigatoriamente de getter e setter?
4. O que caracteriza uma sobrecarga de método?
5. Por que mudar apenas o retorno não cria uma sobrecarga?
6. O que é um construtor?
7. Qual a diferença entre construtor e método?
8. Podemos ter vários construtores na mesma classe?
9. Para que serve `this` dentro de um construtor?
10. Quando um bloco de inicialização de instância é executado?
11. Qual a diferença entre atributo de instância e atributo `static`?
12. Por que um método `static` pode ser chamado sem criar um objeto?
13. Por que um método `static` não pode acessar diretamente um atributo de instância?
14. Qual a diferença entre um bloco de inicialização comum e um bloco `static`?
15. Se uma classe possui vários blocos `static`, em que ordem eles são executados?

---

# 📌 Regra para o estudo

**Não faça todos os exercícios de uma vez.**

A sequência será:

```text
Leia Aula 54
    ↓
Faça 54.1
    ↓
Faça 54.2
    ↓
Faça 54.3
    ↓
Envie seu código para correção
    ↓
Entenda/corrija
    ↓
Leia Aula 55
    ↓
Faça 55.1
    ↓
...
```

Assim cada aula vira um pequeno ciclo de **teoria → prática → correção → avanço**.

> **Importante:** nos exercícios difíceis, tente resolver sozinho antes de pesquisar. Quando você me enviar o código, eu primeiro vou apontar o que está errado e dar uma dica. Não vou simplesmente entregar a resposta.
