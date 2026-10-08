# Bloco 10 — Exceções

## Aulas 95 a 105

Este bloco é dedicado ao mecanismo de **exceções do Java**.

A ideia central não é simplesmente “evitar que o programa dê erro”. O objetivo é entender:

- o que é uma exceção;
- o que é um `Error`;
- quais problemas o Java considera `checked` e `unchecked`;
- como lançar uma exceção;
- como tratar uma exceção;
- como deixar uma exceção subir para outro método;
- como executar código mesmo quando ocorre uma exceção;
- como tratar diferentes tipos de exceção;
- como tratar várias exceções com o mesmo código;
- como fechar recursos automaticamente;
- como criar exceções próprias para representar regras do sistema;
- e como as exceções funcionam quando existe sobrescrita de métodos.

> **Fonte principal:** transcrições das aulas 95–105 da Maratona Java Virado no Jiraya.
>
> O README foi reorganizado para estudo. A explicação abaixo procura manter os conceitos apresentados nas aulas, em vez de apenas listar definições.

---

# 1. Visão geral do bloco

A progressão das aulas é importante:

```text
95 — Error
        ↓
96 — RuntimeException
        ↓
97 — Exception / checked exceptions
        ↓
98 — lançando unchecked exceptions
        ↓
99 — lançando checked exceptions
        ↓
100 — finally
        ↓
101 — múltiplos catch
        ↓
102 — multi-catch
        ↓
103 — try-with-resources
        ↓
104 — exceções customizadas
        ↓
105 — exceções e sobrescrita
```

Existe uma ideia que aparece várias vezes no bloco:

```text
acontece uma situação anormal
          ↓
Java lança uma exceção
          ↓
alguém precisa tratar ou propagar
          ↓
o programa continua de acordo com a estratégia definida
```

Mas isso muda conforme o tipo de problema.

---

# Aula 95 — Exceptions pt 01 — Errors

## 1. O que é uma situação excepcional?

Até aqui, normalmente trabalhamos com o chamado **fluxo feliz**.

Exemplo:

```java
public static void main(String[] args) {
    int numero1 = 10;
    int numero2 = 2;

    int resultado = numero1 / numero2;

    System.out.println(resultado);
}
```

Nós esperamos que:

```text
10 / 2
```

funcione.

Porém, um programa real possui situações que podem fugir desse fluxo.

Por exemplo:

- um arquivo pode não existir;
- um arquivo pode perder sua permissão;
- o banco pode estar indisponível;
- a internet pode cair;
- uma informação pode ser inválida;
- uma divisão pode tentar usar zero;
- uma execução pode consumir memória demais.

Algumas dessas situações estão relativamente sob controle da aplicação. Outras estão fora do controle direto do programa.

É justamente para lidar com essas situações anormais que existem as exceções.

---

## 2. Exceções são objetos

Um ponto importante apresentado na aula é que as exceções do Java são objetos.

Elas fazem parte da hierarquia:

```text
Throwable
├── Error
└── Exception
```

Portanto, `Throwable` está acima de `Error` e `Exception`.

Isso é importante porque muitas vezes usamos a palavra “erro” genericamente, mas tecnicamente o Java diferencia:

```text
Error
```

de

```text
Exception
```

---

## 3. O que é `Error`?

`Error` representa problemas graves relacionados à execução da JVM.

Um exemplo apresentado é:

```text
OutOfMemoryError
```

Isso acontece quando a JVM não consegue disponibilizar memória suficiente para a execução.

Outro exemplo trabalhado é:

```text
StackOverflowError
```

---

## 4. Stack e StackOverflowError

Para entender `StackOverflowError`, a aula mostra a relação com chamadas de métodos.

Imagine:

```java
public static void recursivo() {
    recursivo();
}
```

O método chama ele mesmo.

Temos:

```text
recursivo()
    ↓
recursivo()
    ↓
recursivo()
    ↓
recursivo()
    ↓
...
```

Cada chamada precisa de espaço na memória da execução.

Como não existe uma condição para parar, novas chamadas continuam sendo adicionadas.

Em determinado momento, a memória disponível para a stack acaba.

Resultado:

```text
StackOverflowError
```

O ponto importante da aula é perceber que não é uma exceção de negócio que você normalmente captura para continuar a aplicação.

O problema precisa ser corrigido na origem.

No exemplo de recursividade:

```text
recursão sem limite
       ↓
muitas chamadas
       ↓
stack cresce
       ↓
StackOverflowError
```

A solução não é simplesmente “capturar o erro”.

A solução é corrigir a lógica, por exemplo estabelecendo uma condição de parada.

---

## 5. Error x problema normal da aplicação

A ideia apresentada é:

```text
Error
↓
problema grave da JVM
↓
normalmente não existe recuperação simples em tempo de execução
```

Já as exceções que serão estudadas nas próximas aulas são situações que podem ser tratadas pela aplicação.

Portanto, não pense:

> “Tudo que deu errado é `Error`.”

Pense:

```text
Throwable
├── Error       → problemas graves
└── Exception   → exceções tratáveis pela aplicação
```

---

## O que você precisa saber desta aula

Você deve conseguir explicar sem consultar:

1. O que é `Throwable`.
2. Qual a diferença geral entre `Error` e `Exception`.
3. O que é `StackOverflowError`.
4. Por que uma recursividade sem condição de parada pode causar `StackOverflowError`.
5. Por que simplesmente capturar um `Error` não resolve o problema original.

---

# Aula 96 — Exceptions pt 02 — RuntimeException

Agora entramos no ramo de `Exception`.

A hierarquia começa a ficar mais interessante:

```text
Throwable
├── Error
└── Exception
    └── RuntimeException
```

---

## 1. O que é RuntimeException?

`RuntimeException` representa uma família de exceções que podem acontecer durante a execução do programa.

A aula diferencia essas exceções das chamadas **checked exceptions**.

As `RuntimeException` são **unchecked**.

Isso significa que o compilador não obriga você a fazer:

```java
try {
    ...
} catch (...) {
    ...
}
```

nem a declarar:

```java
throws ...
```

para cada `RuntimeException`.

---

## 2. ArithmeticException

Um exemplo clássico:

```java
int resultado = 10 / 0;
```

Isso gera:

```text
ArithmeticException
```

A exceção acontece durante a execução.

O código compila, mas quando chega à divisão por zero, ocorre a exceção.

---

## 3. NullPointerException

Outro exemplo importante:

```java
String nome = null;

System.out.println(nome.length());
```

`nome` não aponta para um objeto.

Quando fazemos:

```java
nome.length()
```

estamos tentando utilizar uma referência que está `null`.

Isso pode gerar:

```text
NullPointerException
```

---

## 4. ArrayIndexOutOfBoundsException

Imagine:

```java
int[] numeros = {10, 20, 30};

System.out.println(numeros[10]);
```

O array possui somente três posições válidas:

```text
0
1
2
```

A posição `10` não existe.

Durante a execução, ocorre uma exceção relacionada ao acesso inválido.

---

## 5. RuntimeException e polimorfismo

Uma informação importante da aula é que podemos trabalhar com o tipo específico:

```java
ArithmeticException
```

ou com um tipo mais genérico:

```java
RuntimeException
```

Por causa da herança.

A ideia é:

```text
RuntimeException
       ↑
ArithmeticException
```

Então uma referência do tipo `RuntimeException` pode representar uma `ArithmeticException`.

Isso será importante quando estudarmos múltiplos `catch`.

---

## 6. Unchecked não significa “não existe problema”

Esse é um ponto importante.

Quando dizemos:

```text
RuntimeException = unchecked
```

não significa:

> “Pode ignorar.”

Significa:

> “O compilador não obriga o tratamento.”

Por exemplo:

```java
int resultado = 10 / 0;
```

O Java permite compilar.

Mas durante a execução:

```text
ArithmeticException
```

pode interromper o fluxo.

---

## O que você precisa saber desta aula

- `RuntimeException` é filha de `Exception`.
- `RuntimeException` é unchecked.
- O compilador não obriga `try/catch` ou `throws` para ela.
- `ArithmeticException`, `NullPointerException` e exceções relacionadas a acesso inválido são exemplos importantes.
- Unchecked não significa que a exceção não possa ou não deva ser tratada.

---

# Aula 97 — Exceções pt 03 — Exception

Aqui aparece uma das partes mais importantes do bloco:

```text
checked x unchecked
```

---

## 1. Checked exception

Algumas exceções são **checked**.

Elas são verificadas pelo compilador.

A aula utiliza operações com arquivos para demonstrar isso.

Por exemplo, determinadas operações de arquivo podem declarar que existe a possibilidade de lançar:

```java
IOException
```

Quando o método declara uma checked exception, o Java exige que você faça alguma coisa com essa possibilidade.

Você tem basicamente duas alternativas:

```text
1. tratar
2. propagar
```

---

## 2. Tratar a exceção

Você pode utilizar:

```java
try {
    // código que pode lançar a exceção
} catch (IOException e) {
    // tratamento
}
```

A ideia é:

```text
try
 ↓
tente executar
 ↓
ocorreu IOException?
 ↓
sim
 ↓
catch
 ↓
tratamento
```

---

## 3. Propagar a exceção

A outra possibilidade é deixar outro método cuidar dela.

Para isso utilizamos:

```java
throws
```

Exemplo:

```java
public void criarArquivo() throws IOException {
    // código
}
```

Aqui o método está dizendo:

> “Existe a possibilidade de esse método lançar `IOException`.”

Ele não está necessariamente tratando a exceção.

Ele está informando que a responsabilidade pode subir para quem chamou o método.

---

## 4. `throw` x `throws`

Não confunda.

### `throws`

Aparece na assinatura do método:

```java
public void executar() throws IOException {
}
```

Ele declara uma possibilidade.

### `throw`

É usado para efetivamente lançar uma exceção:

```java
throw new IOException();
```

Mentalidade:

```text
throws
↓
declara

throw
↓
lança
```

Essa diferença será aprofundada na aula seguinte.

---

## 5. Por que o Java obriga tratamento?

A aula apresenta a ideia de que isso funciona como uma camada de segurança.

Imagine que você tente criar um arquivo.

Existem fatores que você não controla completamente:

- permissão;
- existência do diretório;
- sistema operacional;
- estado do arquivo;
- outras condições de entrada/saída.

Então o Java exige que você reconheça a possibilidade.

Se você simplesmente tentar usar uma checked exception sem tratar nem propagar, o código não compila.

---

## 6. `catch (Exception e)`

Também é possível capturar um tipo mais genérico:

```java
catch (Exception e) {
}
```

Isso pode capturar diferentes exceções que sejam filhas de `Exception`.

Mas a aula chama atenção para um problema:

Se você captura tudo de forma genérica, pode perder informação.

Por exemplo, imagine que seu código pode falhar por:

```text
problema no arquivo
```

ou:

```text
problema na conversão de número
```

Se você tratar tudo simplesmente como:

```java
catch (Exception e)
```

fica mais difícil saber qual situação realmente aconteceu e oferecer um tratamento específico.

---

## 7. Nunca deixe o catch vazio

A aula também chama atenção para isso.

Evite:

```java
catch (Exception e) {

}
```

Se você capturou uma exceção, existe alguma razão para isso.

Você precisa decidir o que fazer com ela.

Pode ser:

- registrar;
- informar;
- transformar;
- tratar;
- propagar;
- executar alguma ação de recuperação.

O importante é não simplesmente esconder o problema.

---

## O que você precisa saber desta aula

Você deve dominar:

```text
checked
↓
compilador exige tratamento ou propagação

unchecked
↓
compilador não exige
```

E:

```java
try/catch
```

para tratamento e:

```java
throws
```

para propagação.

---

# Aula 98 — Exceptions pt 04 — Throwing unchecked exception

Agora o foco é **lançar exceções unchecked**.

---

## 1. Podemos criar uma exceção manualmente

Até aqui vimos situações em que o próprio Java lança uma exceção.

Mas nossa aplicação também pode identificar que uma situação é inválida e lançar uma exceção.

Exemplo:

```java
throw new IllegalArgumentException();
```

Aqui estamos dizendo explicitamente:

> “Essa condição não é aceitável.”

---

## 2. `throw`

A palavra-chave:

```java
throw
```

é utilizada para lançar uma exceção.

Exemplo:

```java
if (idade < 0) {
    throw new IllegalArgumentException();
}
```

Fluxo:

```text
idade < 0
   ↓
condição inválida
   ↓
throw
   ↓
IllegalArgumentException
```

---

## 3. Exceções também podem representar regras

Imagine um método:

```java
public void sacar(double valor) {
    if (valor <= 0) {
        throw new IllegalArgumentException();
    }
}
```

O método está dizendo que determinado valor não faz sentido para aquela operação.

A exceção passa a fazer parte do comportamento do método.

---

## 4. `throw` não é `throws`

Novamente:

```java
throw new RuntimeException();
```

significa:

> lance esta exceção agora.

Enquanto:

```java
public void executar() throws RuntimeException {
}
```

declara uma possibilidade na assinatura.

Como `RuntimeException` é unchecked, o compilador não obriga o chamador a tratá-la.

---

## 5. Lançar uma RuntimeException não obriga catch

Por exemplo:

```java
public void validar(int idade) {
    if (idade < 0) {
        throw new IllegalArgumentException();
    }
}
```

Quem chama:

```java
validar(-10);
```

não é obrigado pelo compilador a colocar `try/catch`.

Mas, se a exceção realmente for lançada e ninguém tratá-la, ela poderá subir pela cadeia de chamadas até interromper o fluxo.

---

## O que você precisa saber desta aula

- `throw` efetivamente lança uma exceção.
- Podemos lançar exceções do Java manualmente.
- `RuntimeException` e suas subclasses são unchecked.
- Podemos usar unchecked exceptions para representar entradas ou estados inválidos.
- `throw` e `throws` têm funções diferentes.

---

# Aula 99 — Exceções pt 05 — Lançando exceção checked

Agora fazemos algo semelhante, mas utilizando uma **checked exception**.

---

## 1. O que muda?

Imagine:

```java
throw new Exception();
```

Como `Exception` é checked, o Java não deixa simplesmente lançar essa exceção sem que exista uma estratégia.

Você precisa:

```text
tratar
ou
propagar
```

---

## 2. Tratando

Uma possibilidade:

```java
try {
    throw new Exception();
} catch (Exception e) {
    // tratamento
}
```

A exceção é lançada e imediatamente tratada naquele contexto.

---

## 3. Propagando com `throws`

Outra possibilidade:

```java
public void executar() throws Exception {
    throw new Exception();
}
```

Agora quem chama `executar()` precisa lidar com a possibilidade.

Por exemplo:

```java
public void metodoSuperior() throws Exception {
    executar();
}
```

A responsabilidade continua subindo.

Podemos imaginar:

```text
executar()
   ↓
lança Exception
   ↓
throws
   ↓
chamador
   ↓
pode tratar ou continuar propagando
```

---

## 4. Por que isso é importante?

Essa é uma diferença prática entre checked e unchecked.

### Unchecked

```java
throw new RuntimeException();
```

O compilador não exige que o chamador trate.

### Checked

```java
throw new Exception();
```

O compilador exige que a exceção seja tratada ou propagada.

---

## 5. Não use `throws Exception` automaticamente

Embora seja possível escrever:

```java
public void executar() throws Exception
```

isso torna o contrato muito genérico.

Se o método pode lançar uma exceção específica, é melhor deixar isso claro:

```java
public void executar() throws IOException
```

em vez de esconder tudo atrás de:

```java
throws Exception
```

A intenção é que o código deixe claro o tipo de problema que pode acontecer.

---

# Aula 100 — Exceptions pt 06 — Finally Block

Agora aparece:

```java
finally
```

---

## 1. Para que serve o finally?

O `finally` representa um bloco que deve ser executado depois da tentativa de execução/tratamento do bloco.

Estrutura:

```java
try {
    // tentativa
} catch (Exception e) {
    // tratamento
} finally {
    // código final
}
```

Mentalidade:

```text
try
 ↓
aconteceu exceção?
 ↙       ↘
não       sim
 ↓         ↓
continua   catch
      ↘   ↙
       finally
```

---

## 2. Por que ele existe?

A ideia principal é permitir uma ação que precisa acontecer independentemente do resultado normal ou excepcional da execução.

Historicamente isso era muito utilizado para limpeza de recursos.

Por exemplo:

```text
abrir recurso
   ↓
usar recurso
   ↓
finally
   ↓
fechar/limpar
```

O bloco `finally` é importante porque o código pode seguir caminhos diferentes.

---

## 3. Exemplo

```java
try {
    System.out.println("Executando");
} catch (Exception e) {
    System.out.println("Tratando");
} finally {
    System.out.println("Finalizando");
}
```

Se não houver exceção:

```text
Executando
Finalizando
```

Se houver exceção tratada:

```text
Tratando
Finalizando
```

O `finally` fica associado à etapa final desse fluxo.

---

## 4. Finally não substitui try-with-resources

Isso será importante na aula 103.

O `finally` pode ser usado para garantir uma ação final.

Mas, para recursos que implementam `AutoCloseable`, o Java possui uma construção própria:

```java
try (recurso) {
}
```

que automatiza o fechamento.

---

# Aula 101 — Exceções pt 07 — Capturando múltiplas exceções

Agora imagine que um método pode produzir diferentes exceções.

Por exemplo:

```text
Exceção A
Exceção B
Exceção C
```

E cada uma precisa de um tratamento diferente.

Podemos ter:

```java
try {
    // código
} catch (ArithmeticException e) {
    // tratamento A
} catch (NullPointerException e) {
    // tratamento B
} catch (Exception e) {
    // tratamento genérico
}
```

---

## 1. Como o Java escolhe o catch?

O Java procura os `catch` de cima para baixo.

Quando encontra um tipo compatível com a exceção lançada, entra naquele bloco.

Imagine:

```text
exceção lançada
      ↓
primeiro catch
      ↓
é compatível?
   ↙       ↘
 sim       não
 ↓          ↓
executa   próximo catch
```

---

## 2. Ordem dos catch é importante

Esse código é problemático:

```java
try {
    ...
} catch (Exception e) {
    ...
} catch (NullPointerException e) {
    ...
}
```

Por quê?

Porque:

```text
Exception
   ↑
NullPointerException
```

Se o `catch (Exception e)` vem primeiro, ele já consegue capturar uma `NullPointerException`.

Então o segundo `catch` nunca seria alcançado.

A regra é:

```text
mais específico
      ↓
mais genérico
```

Exemplo correto:

```java
catch (NullPointerException e) {
}
catch (Exception e) {
}
```

---

## 3. Por que o polimorfismo interfere nisso?

Porque uma referência do tipo pai pode representar um objeto do tipo filho.

Da mesma maneira:

```java
Exception e
```

pode representar uma exceção específica que é filha de `Exception`.

Por isso o `catch` genérico pode capturar exceções específicas.

---

## 4. E se nenhuma exceção for compatível?

Se ocorrer uma exceção que não seja capturada por nenhum `catch`, ela continua subindo pela cadeia de chamadas.

Por isso pode existir um `catch` mais genérico no final, quando fizer sentido.

---

# Aula 102 — Exceções pt 08 — Multi-catch em linha

Agora temos uma sintaxe para quando **duas ou mais exceções precisam exatamente do mesmo tratamento**.

Em vez de:

```java
catch (ExceptionA e) {
    tratar();
}

catch (ExceptionB e) {
    tratar();
}
```

podemos utilizar:

```java
catch (ExceptionA | ExceptionB e) {
    tratar();
}
```

---

## 1. O objetivo do multi-catch

O objetivo é principalmente reduzir repetição e melhorar a legibilidade quando o tratamento é realmente igual.

Mentalidade:

```text
Exceção A ──┐
            ├── mesmo tratamento
Exceção B ──┘
```

---

## 2. Não use multi-catch quando os tratamentos são diferentes

Se:

```text
ExceptionA → tratamento A
ExceptionB → tratamento B
```

não faz sentido juntá-las.

Use catches separados.

Multi-catch é apropriado quando:

```text
A → tratamento X
B → tratamento X
```

---

## 3. A variável do multi-catch

Exemplo:

```java
catch (IOException | SQLException e) {
    System.out.println(e.getMessage());
}
```

A variável:

```java
e
```

representa a exceção capturada naquele bloco.

Ela é usada para o tratamento comum.

---

## 4. Relação com herança

A aula também mostra uma regra importante: os tipos colocados no multi-catch precisam representar alternativas que possam ser tratadas conjuntamente.

Não faria sentido colocar:

```java
Exception | IOException
```

porque `IOException` já é filha de `Exception`.

Você estaria tentando colocar um tipo e seu próprio subtipo como alternativas.

---

# Aula 103 — Exceções pt 09 — Try with resources

Esta é uma aula muito importante para código Java que trabalha com recursos.

---

## 1. O problema

Imagine que você abre um recurso:

```text
abrir arquivo
     ↓
ler arquivo
     ↓
fechar arquivo
```

O problema aparece quando algo dá errado durante a leitura.

Por exemplo:

```text
abrir
 ↓
ler
 ↓
EXCEÇÃO
```

Como garantir que o recurso será fechado?

Uma forma tradicional seria usar `finally`.

Mas isso pode deixar o código mais trabalhoso.

---

## 2. Try-with-resources

O Java possui:

```java
try (recurso) {
    // utilização
}
```

O recurso deve implementar:

```java
AutoCloseable
```

ou uma interface compatível com esse mecanismo.

A ideia é:

```text
try
 ↓
usa recurso
 ↓
fim do bloco
 ↓
Java chama close()
```

Mesmo quando ocorre uma exceção durante o processamento, o Java se encarrega do fechamento do recurso.

---

## 3. AutoCloseable

A aula cria classes próprias para demonstrar o conceito.

Uma classe pode implementar:

```java
class Leitor implements AutoCloseable {

    @Override
    public void close() {
        System.out.println("Fechando leitor");
    }
}
```

Agora podemos utilizar:

```java
try (Leitor leitor = new Leitor()) {
    // usa leitor
}
```

Ao sair do bloco, o Java chama:

```java
close();
```

---

## 4. Por que o Java consegue fazer isso?

Porque existe um contrato.

Ao trabalhar com:

```java
AutoCloseable
```

o Java sabe que existe um método:

```java
close()
```

Então o recurso pode ser fechado automaticamente.

Isso é uma aplicação direta de orientação a objetos e polimorfismo:

```text
referência AutoCloseable
        ↓
objeto concreto
        ↓
possui close()
```

---

## 5. Vários recursos

Também podemos declarar mais de um:

```java
try (
    Leitor leitor1 = new Leitor();
    Leitor leitor2 = new Leitor()
) {
    ...
}
```

A aula demonstra que os recursos são fechados na **ordem inversa da declaração**.

Se foram declarados:

```text
leitor1
leitor2
```

o fechamento ocorre:

```text
leitor2
leitor1
```

Isso é importante.

---

## 6. Por que try-with-resources é importante?

Porque reduz a responsabilidade manual do desenvolvedor.

Em vez de lembrar:

```text
abrir
usar
capturar
finally
fechar
```

podemos declarar o recurso diretamente no `try`.

O Java passa a cuidar do fechamento.

---

# Aula 104 — Exceções pt 10 — Exceção customizada

Agora vamos criar nossas próprias exceções.

---

## 1. Por que criar uma exceção?

Imagine um sistema de login.

Existe uma situação específica:

```text
usuário ou senha inválidos
```

Poderíamos lançar:

```java
RuntimeException
```

Mas isso não comunica muito bem o domínio.

Podemos criar:

```java
LoginInvalidException
```

Assim o código passa a expressar o problema diretamente.

---

## 2. Como criar?

Uma exceção customizada nada mais é que uma classe que herda de uma exceção existente.

Por exemplo:

```java
public class LoginInvalidException extends Exception {
}
```

Agora temos uma exceção própria.

Como ela estende `Exception`, ela é:

```text
checked
```

---

## 3. Exceção customizada unchecked

Também podemos criar:

```java
public class LoginInvalidException extends RuntimeException {
}
```

Nesse caso ela será:

```text
unchecked
```

Portanto:

```text
extends Exception
        ↓
checked

extends RuntimeException
        ↓
unchecked
```

A escolha depende da natureza do problema e do contrato que queremos criar para o método.

---

## 4. Nome da exceção

A convenção mostrada na aula é terminar o nome com:

```text
Exception
```

Exemplos:

```text
LoginInvalidException
SaldoInsuficienteException
UsuarioNaoEncontradoException
```

O nome deve representar o problema.

---

## 5. Construtores

Uma exceção customizada também pode possuir construtores.

Por exemplo:

```java
public class LoginInvalidException extends Exception {

    public LoginInvalidException(String message) {
        super(message);
    }
}
```

Aqui:

```java
super(message);
```

envia a mensagem para a classe pai.

Depois podemos fazer:

```java
throw new LoginInvalidException("Usuário ou senha inválidos");
```

Isso permite que o tratamento obtenha a mensagem:

```java
e.getMessage()
```

---

## 6. Exceção customizada e regra de negócio

Esse é o principal motivo para criar exceções próprias.

O sistema pode ter regras específicas:

```text
login inválido
produto indisponível
saldo insuficiente
pedido inválido
cliente inexistente
```

Esses problemas fazem parte do domínio da aplicação.

Uma exceção customizada permite representar essas situações diretamente no código.

---

# Aula 105 — Exceções pt 11 — Exceção e regras de sobrescrita

A última aula do bloco conecta exceções com um assunto que já estudamos:

```text
herança
+
sobrescrita
```

---

## 1. Exceções fazem parte da assinatura do método?

Para as regras de checked exceptions, sim: a declaração de exceções checked influencia o contrato que uma subclasse precisa respeitar ao sobrescrever um método.

Imagine:

```java
class Funcionario {

    public void salvar() throws LoginInvalidException {
    }
}
```

Uma subclasse sobrescreve:

```java
class Gerente extends Funcionario {

    @Override
    public void salvar() throws LoginInvalidException {
    }
}
```

Isso é permitido.

---

## 2. A subclasse pode usar uma exceção mais específica?

Sim.

Se o método pai declara uma checked exception mais genérica, a implementação sobrescrita pode declarar uma exceção compatível mais específica.

A ideia apresentada na aula é:

```text
método pai
    ↓
declara uma checked exception

método filho
    ↓
pode declarar uma exceção filha/específica
```

---

## 3. O que a subclasse não pode fazer?

Ela não pode simplesmente adicionar uma checked exception nova e mais genérica que não fazia parte do contrato original.

Por exemplo, se o método original não declara:

```java
Exception
```

a subclasse não pode simplesmente fazer:

```java
@Override
public void executar() throws Exception {
}
```

se isso ampliar o contrato checked do método pai.

Isso quebraria uma expectativa importante de quem trabalha com a referência da classe pai.

---

## 4. Por que isso importa?

Imagine:

```java
Funcionario funcionario = new Gerente();
funcionario.salvar();
```

O código é escrito considerando o contrato de:

```java
Funcionario
```

A subclasse precisa respeitar esse contrato.

Esse é mais um caso em que herança e polimorfismo influenciam o desenho do código.

---

# 2. O modelo mental completo do bloco

Agora junte tudo:

```text
                         Throwable
                            │
              ┌─────────────┴─────────────┐
              │                           │
            Error                      Exception
              │                           │
      problemas graves             ┌──────┴──────┐
      da JVM                       │             │
                           RuntimeException   checked
                                │
                            unchecked
```

Depois:

```text
situação anormal
      ↓
exceção
      ↓
é unchecked ou checked?
      ↓
   ┌──┴──┐
   │     │
unchecked checked
   │     │
   │     ├── tratar
   │     │
   │     └── propagar
   │
   └── tratar ou deixar propagar
```

E para o tratamento:

```text
try
 ↓
catch
 ↓
finally
```

Quando temos várias exceções:

```text
try
 ↓
catch específico
 ↓
catch específico
 ↓
catch genérico
```

Quando duas exceções têm exatamente o mesmo tratamento:

```text
catch (A | B e)
```

Quando existe um recurso que precisa ser fechado:

```text
try (recurso)
```

Quando o domínio possui um problema próprio:

```text
Exceção customizada
```

E quando existe herança:

```text
classe pai
   ↓
método
   ↓
sobrescrita
   ↓
regras das checked exceptions
```

---

# 3. O que você precisa dominar antes dos exercícios

Não avance apenas decorando sintaxe.

Você deve conseguir explicar:

### Hierarquia

- `Throwable`
- `Error`
- `Exception`
- `RuntimeException`

### Tipos

- checked
- unchecked

### Lançamento

- `throw`
- `throws`

### Tratamento

- `try`
- `catch`
- `finally`

### Múltiplos tratamentos

- vários `catch`
- ordem dos `catch`
- específico antes do genérico
- multi-catch

### Recursos

- `AutoCloseable`
- `close()`
- try-with-resources
- ordem inversa de fechamento

### Domínio

- exceção customizada
- `extends Exception`
- `extends RuntimeException`
- mensagem da exceção

### Herança

- exceções em métodos sobrescritos
- restrições envolvendo checked exceptions

---

# 4. Erros conceituais que você não pode cometer

## Erro 1 — achar que Error e Exception são a mesma coisa

Não são:

```text
Throwable
├── Error
└── Exception
```

---

## Erro 2 — achar que RuntimeException precisa obrigatoriamente de try/catch

Não precisa.

Ela é unchecked.

---

## Erro 3 — confundir throw e throws

```java
throw new Exception();
```

lança.

```java
void executar() throws Exception
```

declara a possibilidade.

---

## Erro 4 — colocar catch genérico antes do específico

Errado:

```java
catch (Exception e) {
}
catch (IOException e) {
}
```

A exceção genérica captura o caso que poderia chegar ao específico.

---

## Erro 5 — usar multi-catch para tratamentos diferentes

Se os tratamentos são diferentes:

```text
A → tratamento A
B → tratamento B
```

use catches separados.

---

## Erro 6 — esquecer o fechamento do recurso

Com recursos que precisam ser fechados, prefira compreender e utilizar:

```java
try (recurso) {
}
```

quando o recurso suporta `AutoCloseable`.

---

## Erro 7 — criar exceção customizada sem entender checked x unchecked

Não basta colocar:

```java
extends Exception
```

ou:

```java
extends RuntimeException
```

Você precisa entender o impacto disso no código que chama a exceção.

---

## Erro 8 — achar que a subclasse pode declarar qualquer checked exception

Na sobrescrita existem regras.

A implementação filha não pode simplesmente ampliar o contrato checked do método pai.

---

# 5. Checklist final

Antes do desafio integrador, marque:

- [ ] Sei explicar `Throwable`.
- [ ] Sei diferenciar `Error` e `Exception`.
- [ ] Sei explicar `StackOverflowError`.
- [ ] Sei o que é `RuntimeException`.
- [ ] Sei explicar checked e unchecked.
- [ ] Sei usar `throw`.
- [ ] Sei diferenciar `throw` e `throws`.
- [ ] Sei tratar uma exceção com `try/catch`.
- [ ] Sei para que serve `finally`.
- [ ] Sei tratar várias exceções.
- [ ] Sei por que o `catch` específico deve vir antes do genérico.
- [ ] Sei usar multi-catch.
- [ ] Sei quando não devo usar multi-catch.
- [ ] Sei o que é `AutoCloseable`.
- [ ] Sei usar try-with-resources.
- [ ] Sei que vários recursos são fechados em ordem inversa.
- [ ] Sei criar uma exceção customizada.
- [ ] Sei diferenciar exceção customizada checked e unchecked.
- [ ] Entendo as regras de checked exceptions na sobrescrita.

---

# 6. Como estudar este bloco

Não tente fazer as 11 aulas de uma vez.

Siga exatamente esta sequência:

```text
Aula 95
  ↓
leia a explicação
  ↓
faça os 3 exercícios
  ↓
mande seu código
  ↓
correção
  ↓
Aula 96
  ↓
...
  ↓
Aula 105
  ↓
Desafio Integrador
```

A parte mais importante deste bloco é perceber a evolução:

```text
entender o problema
       ↓
entender a hierarquia
       ↓
entender checked/unchecked
       ↓
lançar
       ↓
tratar
       ↓
propagar
       ↓
organizar vários tratamentos
       ↓
gerenciar recursos
       ↓
criar exceções do domínio
       ↓
entender o impacto na herança
```

Esse é o conhecimento que deve ficar depois do bloco, e não apenas a capacidade de copiar um `try/catch`.

---

## Fonte

Conteúdo estruturado a partir das transcrições das aulas:

- 95 — Exceptions pt 01 — Errors
- 96 — Exceptions pt 02 — RuntimeException
- 97 — Exceções pt 03 — Exception
- 98 — Exceptions pt 04 — Throwing unchecked exception
- 99 — Exceções pt 05 — Lançando exceção checked
- 100 — Exceptions pt 06 — Finally Block
- 101 — Exceções pt 07 — Capturando múltiplas exceções
- 102 — Exceções pt 08 — Multi catch em linha
- 103 — Exceções pt 09 — Try with resources
- 104 — Exceções pt 10 — Exceção customizada
- 105 — Exceções pt 11 — Exceção e regras de sobrescrita

A ordem e os títulos das aulas foram conferidos na listagem da playlist.
