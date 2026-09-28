# Bloco 08 — `final` e Enumerações

Este bloco apresenta dois assuntos diferentes, mas importantes:

- o modificador `final`;
- enumerações (`enum`).

As aulas **80 a 83 são a parte mais importante deste bloco para o nosso estudo**, porque aqui você começa a trabalhar com `enum` de uma forma mais completa: primeiro cria a enumeração, depois adiciona atributos e construtor, depois coloca comportamento dentro das constantes e, por fim, aprende a localizar uma constante a partir de um atributo.

> **Como estudar:** faça uma aula por vez. Leia a explicação, faça os 3 exercícios daquela aula e só avance depois de corrigir seu código.

> **Desafio integrador:** no final do bloco existe um desafio obrigatório que reúne tudo das aulas 77–83.

---

# 1. Visão geral do bloco

```text
Aula 77
final em tipos primitivos
        ↓
Aula 78
final em referências
        ↓
Aula 79
final em classes e métodos
        ↓
Aula 80
enum: o que é e por que existe
        ↓
Aula 81
enum + atributos + construtor
        ↓
Aula 82
enum + comportamento + sobrescrita
        ↓
Aula 83
buscar uma constante do enum por um atributo
        ↓
DESAFIO INTEGRADOR
```

O ponto em que normalmente começa a ficar confuso é este:

```text
enum simples
     ↓
enum com atributos
     ↓
enum com construtor
     ↓
enum com métodos
     ↓
cada constante com seu próprio comportamento
     ↓
buscar uma constante
```

Por isso, nas aulas de `enum`, não basta decorar a sintaxe. O objetivo é entender **o que uma constante de enum realmente representa**.

---

# Aula 77 — `final` em tipo primitivo

## 77.1 O que significa `final`?

Quando usamos `final` em uma variável, estamos dizendo que aquela referência de variável não poderá receber outra atribuição depois que for inicializada.

Exemplo:

```java
final int IDADE = 20;
```

Depois disso:

```java
IDADE = 30;
```

não é permitido.

A variável recebeu um valor e não pode receber outro.

Podemos pensar assim:

```text
IDADE
  ↓
  20

final
  ↓
não pode apontar para outro valor
```

O objetivo da aula é entender a diferença entre uma informação que pode mudar e uma informação que deve permanecer fixa.

---

## 77.2 `final` não significa necessariamente "constante global"

Um erro comum é pensar:

> "Se coloquei `final`, virou uma constante do sistema."

Não necessariamente.

`final` está relacionado à possibilidade de **reatribuição**.

Por exemplo:

```java
final int numero = 10;
```

A variável `numero` não poderá receber outro `int`.

O ponto principal é:

```text
final → não pode receber outra atribuição
```

---

## 77.3 Onde isso é útil?

Imagine que uma classe tenha um código que deve ser definido uma única vez:

```java
final int codigo;
```

Depois que esse código for inicializado, a classe não deve permitir que ele seja substituído por outro.

Isso é útil quando determinada informação deve permanecer fixa durante a vida daquele objeto ou daquela variável.

---

## O que você precisa saber da aula 77

Você deve conseguir olhar para:

```java
final int numero = 10;
```

e entender imediatamente:

```text
numero recebeu 10
       ↓
não pode receber outro valor depois
```

---

# Aula 78 — `final` em tipo referência

Esta aula é importante porque o comportamento do `final` muda de acordo com o que a variável guarda.

Considere:

```java
final Pessoa pessoa = new Pessoa();
```

Aqui `pessoa` não guarda diretamente o objeto.

Ela guarda uma **referência para o objeto**.

Podemos imaginar:

```text
pessoa
  |
  v
+----------------+
|    Pessoa      |
|                |
| nome = João    |
+----------------+
```

O `final` impede que a referência `pessoa` passe a apontar para outro objeto.

Por exemplo:

```java
final Pessoa pessoa = new Pessoa();

pessoa = new Pessoa();
```

Isso não é permitido.

---

## 78.1 Mas o objeto continua podendo mudar

Aqui está a parte mais importante da aula:

```java
final Pessoa pessoa = new Pessoa();

pessoa.setNome("João");
```

Isso pode ser permitido.

Por quê?

Porque não estamos mudando a referência.

Continuamos apontando para o mesmo objeto:

```text
ANTES

pessoa
  |
  v
Pessoa
nome = null
```

Depois:

```text
pessoa
  |
  v
Pessoa
nome = João
```

A referência continua sendo a mesma.

O que mudou foi o **estado do objeto**.

---

## 78.2 A diferença fundamental

### Isso não pode:

```java
pessoa = outraPessoa;
```

Porque estamos tentando mudar a referência.

### Isso pode ser permitido:

```java
pessoa.setNome("Maria");
```

Porque estamos alterando o objeto apontado pela referência.

Portanto:

```text
final em referência
        ↓
impede trocar o objeto referenciado
        ↓
NÃO significa que o objeto ficou imutável
```

Essa distinção é muito importante para entender `final`.

---

# Aula 79 — `final` em classes e métodos

Agora o `final` aparece em dois lugares diferentes.

---

## 79.1 Classe `final`

Podemos declarar:

```java
public final class Configuracao {
}
```

Isso significa que outra classe não poderá herdar de `Configuracao`.

Não será permitido:

```java
public class ConfiguracaoAvancada extends Configuracao {
}
```

A ideia é:

```text
Configuracao
    ↓
final
    ↓
ninguém pode criar uma subclasse dela
```

---

## 79.2 Método `final`

Também podemos colocar `final` em um método:

```java
public final void executar() {
}
```

Nesse caso, uma subclasse pode existir, mas não poderá sobrescrever esse método.

Exemplo:

```java
class Pai {

    public final void executar() {
        System.out.println("Executando");
    }
}
```

Uma subclasse não poderá fazer:

```java
@Override
public void executar() {
}
```

porque o método foi marcado como `final`.

---

## 79.3 Não confunda os dois

```text
final na classe
    ↓
impede HERANÇA

final no método
    ↓
permite HERANÇA
mas impede SOBRESCRITA daquele método
```

Essa diferença é uma continuação direta do que você estudou no bloco anterior sobre herança e sobrescrita.

---

# Parte mais importante do bloco: ENUM

A partir daqui começa a parte que merece mais atenção.

Se `enum` estiver confuso, não tente decorar os exemplos. Primeiro entenda o problema que ele resolve.

---

# Aula 80 — Enumeração: introdução

## 80.1 Qual problema o `enum` resolve?

Imagine um sistema que possui um cliente.

Esse cliente pode ser somente:

```text
Pessoa Física
OU
Pessoa Jurídica
```

Uma solução ruim seria representar isso com `String`:

```java
String tipo = "Pessoa Fisica";
```

O problema é que `String` aceita praticamente qualquer texto:

```java
"Pessoa Fisica"
"Pessoa Física"
"pessoa fisica"
"PF"
"Pessoa Juridica"
"qualquer coisa"
```

O compilador não sabe quais valores são realmente permitidos.

Isso cria espaço para inconsistências.

---

## 80.2 Criando um `enum`

A solução apresentada na aula é criar uma enumeração:

```java
public enum TipoCliente {

    PESSOA_FISICA,
    PESSOA_JURIDICA

}
```

Agora `TipoCliente` passa a ser um **tipo próprio**.

Podemos criar uma variável desse tipo:

```java
TipoCliente tipo;
```

E atribuir somente uma das opções definidas:

```java
tipo = TipoCliente.PESSOA_FISICA;
```

ou:

```java
tipo = TipoCliente.PESSOA_JURIDICA;
```

---

## 80.3 Pense no `enum` como uma lista fechada

Esta é a ideia mais importante da aula 80:

```java
public enum TipoCliente {

    PESSOA_FISICA,
    PESSOA_JURIDICA

}
```

Você está dizendo:

> "O tipo `TipoCliente` só possui estas opções."

Visualmente:

```text
TipoCliente
│
├── PESSOA_FISICA
└── PESSOA_JURIDICA
```

Não existe:

```java
TipoCliente.PESSOA_EMPRESA
```

se essa opção não foi declarada.

---

# 80.4 Usando o `enum` dentro de uma classe

Podemos ter:

```java
public class Cliente {

    private String nome;
    private TipoCliente tipoCliente;

}
```

Agora `tipoCliente` não é uma String.

Ele é especificamente:

```text
TipoCliente
```

E seu valor precisa ser uma das constantes do enum:

```java
TipoCliente.PESSOA_FISICA
```

ou:

```java
TipoCliente.PESSOA_JURIDICA
```

---

# 80.5 O que é uma constante do enum?

Quando escrevemos:

```java
TipoCliente.PESSOA_FISICA
```

`PESSOA_FISICA` é uma **constante da enumeração**.

Ela não é simplesmente uma String escrita de outra forma.

É uma das instâncias/constantes definidas pelo próprio tipo `TipoCliente`.

Por enquanto, pense assim:

```text
TipoCliente
    ↓
é o tipo

PESSOA_FISICA
PESSOA_JURIDICA
    ↓
são as opções desse tipo
```

Essa ideia será muito importante na aula 81.

---

# 80.6 Por que não usar apenas `String`?

Com:

```java
String tipo;
```

o programa precisa confiar que o programador vai escrever corretamente.

Com:

```java
TipoCliente tipo;
```

o próprio Java passa a conhecer as opções válidas.

Isso deixa o modelo mais explícito:

```text
String
  ↓
"pode ser qualquer texto"

enum
  ↓
"pode ser uma destas opções definidas"
```

---

# 80.7 Como pensar em `enum` no dia a dia

Sempre que você tiver uma informação que possui um **conjunto fechado de possibilidades**, pense em `enum`.

Exemplos conceituais:

```text
Status:
ATIVO
INATIVO
BLOQUEADO

Tipo:
PESSOA_FISICA
PESSOA_JURIDICA

Prioridade:
BAIXA
MEDIA
ALTA
```

O ponto não é decorar esses exemplos.

É reconhecer o padrão:

```text
conjunto fechado de opções
            ↓
          enum
```

---

# Aula 81 — Enumeração com atributos e construtor

Agora começa a parte que costuma causar confusão.

Na aula anterior tínhamos:

```java
public enum TipoCliente {

    PESSOA_FISICA,
    PESSOA_JURIDICA

}
```

Agora imagine que queremos guardar uma descrição para cada opção.

Queremos algo parecido com:

```text
PESSOA_FISICA  → "Pessoa Física"
PESSOA_JURIDICA → "Pessoa Jurídica"
```

Podemos fazer isso dentro do próprio enum.

---

# 81.1 O enum pode ter atributos

Exemplo:

```java
public enum TipoCliente {

    PESSOA_FISICA("Pessoa Física"),
    PESSOA_JURIDICA("Pessoa Jurídica");

    private String nomeRelatorio;

}
```

Agora cada constante possui uma informação associada.

Visualmente:

```text
PESSOA_FISICA
    |
    └── "Pessoa Física"

PESSOA_JURIDICA
    |
    └── "Pessoa Jurídica"
```

Isso é diferente de um enum simples.

Antes tínhamos somente as opções:

```text
PESSOA_FISICA
PESSOA_JURIDICA
```

Agora cada opção também carrega um dado.

---

# 81.2 Mas de onde vem esse valor?

Veja novamente:

```java
PESSOA_FISICA("Pessoa Física"),
PESSOA_JURIDICA("Pessoa Jurídica");
```

Os valores entre parênteses são enviados para o construtor do enum.

É aqui que entra o construtor:

```java
TipoCliente(String nomeRelatorio) {
    this.nomeRelatorio = nomeRelatorio;
}
```

Portanto:

```text
PESSOA_FISICA("Pessoa Física")
          ↓
chama o construtor
          ↓
nomeRelatorio recebe "Pessoa Física"
```

E:

```text
PESSOA_JURIDICA("Pessoa Jurídica")
          ↓
chama o construtor
          ↓
nomeRelatorio recebe "Pessoa Jurídica"
```

---

# 81.3 A estrutura completa

Um enum com atributo fica assim:

```java
public enum TipoCliente {

    PESSOA_FISICA("Pessoa Física"),
    PESSOA_JURIDICA("Pessoa Jurídica");

    private String nomeRelatorio;

    TipoCliente(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }
}
```

Não tente decorar tudo de uma vez.

Leia de cima para baixo:

### Primeiro:

```java
PESSOA_FISICA("Pessoa Física"),
PESSOA_JURIDICA("Pessoa Jurídica");
```

São as constantes.

### Depois:

```java
private String nomeRelatorio;
```

É o atributo que cada constante possui.

### Depois:

```java
TipoCliente(String nomeRelatorio)
```

É o construtor que recebe o valor.

### Depois:

```java
this.nomeRelatorio = nomeRelatorio;
```

É onde o valor recebido é guardado no atributo.

### Por fim:

```java
getNomeRelatorio()
```

É uma forma de consultar o valor.

---

# 81.4 Entenda isso como objetos diferentes

Uma forma muito melhor de entender enum é parar de pensar nele como "uma lista de Strings".

Imagine:

```text
TipoCliente.PESSOA_FISICA
```

como uma constante que possui seu próprio estado:

```text
PESSOA_FISICA
    |
    └── nomeRelatorio = "Pessoa Física"
```

E:

```text
TipoCliente.PESSOA_JURIDICA
    |
    └── nomeRelatorio = "Pessoa Jurídica"
```

São constantes diferentes, cada uma com seu próprio valor.

---

# 81.5 O construtor do enum

O construtor:

```java
TipoCliente(String nomeRelatorio) {
    this.nomeRelatorio = nomeRelatorio;
}
```

não é chamado assim:

```java
new TipoCliente(...)
```

Você **não cria as constantes do enum com `new`**.

As constantes são declaradas no próprio enum:

```java
PESSOA_FISICA("Pessoa Física")
```

e:

```java
PESSOA_JURIDICA("Pessoa Jurídica")
```

Essas declarações fornecem os valores usados pelo construtor.

Pense:

```text
enum declara as constantes
        ↓
cada constante fornece seus argumentos
        ↓
o construtor recebe esses argumentos
        ↓
o atributo é preenchido
```

---

# 81.6 Por que o construtor é importante?

Porque agora o enum deixa de ser apenas:

```text
opção 1
opção 2
opção 3
```

e passa a ser:

```text
opção 1 → possui dados
opção 2 → possui dados
opção 3 → possui dados
```

Isso permite representar informações associadas a cada opção.

---

# Aula 82 — Enumeração com sobrescrita de métodos

Agora chegamos à parte mais avançada do `enum`.

Até aqui cada constante tinha dados.

Agora cada constante poderá ter **um comportamento diferente**.

A ideia apresentada na aula é semelhante a:

```text
DEBITO  → comportamento A
CREDITO → comportamento B
```

Imagine que cada tipo precise calcular um desconto de maneira diferente.

Em vez de fazer:

```java
if (tipo == TipoPagamento.DEBITO) {
    // regra A
} else if (tipo == TipoPagamento.CREDITO) {
    // regra B
}
```

podemos colocar o comportamento nas próprias constantes.

---

# 82.1 O enum pode ter método

Podemos ter um método dentro da enumeração:

```java
public enum TipoPagamento {

    DEBITO {
        @Override
        public double calcularDesconto(double valor) {
            return valor * 0.10;
        }
    },

    CREDITO {
        @Override
        public double calcularDesconto(double valor) {
            return valor * 0.05;
        }
    };

    public abstract double calcularDesconto(double valor);
}
```

Agora observe a estrutura.

Temos:

```text
TipoPagamento
│
├── DEBITO
│      └── possui sua implementação
│
└── CREDITO
       └── possui sua implementação
```

---

# 82.2 O que está acontecendo aqui?

Esta é a parte que você precisa entender.

O enum declara:

```java
public abstract double calcularDesconto(double valor);
```

Isso diz:

> Toda constante desse enum precisa fornecer uma implementação desse método.

Então `DEBITO` faz:

```java
@Override
public double calcularDesconto(double valor) {
    return valor * 0.10;
}
```

E `CREDITO` faz:

```java
@Override
public double calcularDesconto(double valor) {
    return valor * 0.05;
}
```

Cada constante possui sua própria implementação.

---

# 82.3 Por que isso é sobrescrita?

Porque existe um método declarado:

```java
calcularDesconto(...)
```

e cada constante fornece sua própria implementação.

A relação é:

```text
enum
  ↓
declara método abstrato
  ↓
cada constante implementa
  ↓
cada constante possui seu comportamento
```

Isso usa diretamente o conceito de **sobrescrita de métodos** que você já estudou.

---

# 82.4 O grande benefício: evitar `if`/`switch`

Sem esse modelo, poderíamos ter algo assim:

```java
if (tipo == TipoPagamento.DEBITO) {
    // cálculo
}

if (tipo == TipoPagamento.CREDITO) {
    // outro cálculo
}
```

Conforme novas opções aparecem, esse código pode crescer.

Com comportamento no enum:

```text
DEBITO
  ↓
sabe calcular seu próprio desconto

CREDITO
  ↓
sabe calcular seu próprio desconto
```

O código que utiliza o enum não precisa necessariamente conhecer os detalhes de cada regra.

---

# 82.5 Não confunda atributo com comportamento

Compare as duas aulas:

### Aula 81

Cada constante possui **dados**:

```text
PESSOA_FISICA
    ↓
nomeRelatorio = "Pessoa Física"
```

### Aula 82

Cada constante possui **comportamento**:

```text
DEBITO
    ↓
calcularDesconto() → regra do débito

CREDITO
    ↓
calcularDesconto() → regra do crédito
```

Então:

```text
Aula 81 → dados dentro do enum

Aula 82 → comportamento dentro do enum
```

E eles podem existir juntos.

---

# 82.6 O `@Override`

Você já estudou `@Override`.

Aqui ele aparece novamente:

```java
@Override
public double calcularDesconto(double valor) {
    ...
}
```

Ele indica que aquela implementação está sobrescrevendo/implementando o método declarado pelo enum.

Portanto, a aula 82 também reforça um conteúdo que você já estudou em herança e sobrescrita.

---

# Aula 83 — Busca por atributos

Agora chegamos à última etapa.

Você já sabe:

```text
Aula 80
enum simples

Aula 81
enum com atributos

Aula 82
enum com comportamento
```

Agora surge um problema real:

> "Eu tenho um valor de um atributo. Como descubro qual constante do enum possui esse valor?"

---

# 83.1 Exemplo do problema

Imagine:

```java
PESSOA_FISICA("Pessoa Física")
PESSOA_JURIDICA("Pessoa Jurídica")
```

Você recebe:

```java
"Pessoa Física"
```

Mas precisa descobrir que isso corresponde a:

```java
TipoCliente.PESSOA_FISICA
```

Precisamos fazer uma busca.

---

# 83.2 `values()`

O Java fornece:

```java
TipoCliente.values()
```

Esse método permite obter as constantes da enumeração.

Podemos pensar no resultado como:

```text
[
    PESSOA_FISICA,
    PESSOA_JURIDICA
]
```

Então podemos percorrer:

```java
for (TipoCliente tipo : TipoCliente.values()) {

}
```

A variável `tipo` vai representar uma constante por vez.

Primeiro:

```text
tipo → PESSOA_FISICA
```

Depois:

```text
tipo → PESSOA_JURIDICA
```

---

# 83.3 Comparando o atributo

Agora podemos consultar o atributo:

```java
tipo.getNomeRelatorio()
```

e comparar com o valor procurado.

Conceitualmente:

```java
for (TipoCliente tipo : TipoCliente.values()) {

    if (tipo.getNomeRelatorio().equals(nome)) {
        return tipo;
    }

}
```

O raciocínio é:

```text
recebi "Pessoa Física"
        ↓
percorro as constantes
        ↓
PESSOA_FISICA
        ↓
getNomeRelatorio()
        ↓
"Pessoa Física"
        ↓
é igual ao valor procurado?
        ↓
SIM
        ↓
retorno PESSOA_FISICA
```

---

# 83.4 Criando uma busca

Podemos colocar essa lógica dentro da própria enum:

```java
public static TipoCliente buscarPorNomeRelatorio(String nome) {

    for (TipoCliente tipo : TipoCliente.values()) {

        if (tipo.getNomeRelatorio().equals(nome)) {
            return tipo;
        }

    }

    return null;
}
```

Agora podemos imaginar:

```java
TipoCliente tipo = TipoCliente.buscarPorNomeRelatorio("Pessoa Física");
```

O resultado será:

```java
TipoCliente.PESSOA_FISICA
```

---

# 83.5 Por que o método é `static`?

Observe:

```java
TipoCliente.buscarPorNomeRelatorio(...)
```

Estamos chamando o método através do próprio enum:

```text
TipoCliente
    ↓
buscarPorNomeRelatorio(...)
```

Não estamos fazendo:

```java
PESSOA_FISICA.buscarPorNomeRelatorio(...)
```

A busca começa procurando entre **todas as constantes**.

Por isso faz sentido colocá-la como um método da própria enumeração que recebe o valor procurado e percorre `values()`.

---

# 83.6 O que acontece quando não encontra?

Imagine:

```java
TipoCliente.buscarPorNomeRelatorio("Empresa");
```

Não existe nenhuma constante com essa descrição.

A aula apresenta o tratamento de retorno para o caso em que nenhuma constante foi encontrada.

No modelo mostrado, isso pode resultar em:

```java
return null;
```

O ponto importante é:

```text
encontrou
   ↓
retorna a constante

não encontrou
   ↓
trata o caso definido pelo método
```

---

# 83.7 Entenda a busca como uma conversão

Essa parte é muito útil para memorizar.

Você começa com:

```text
String
"Pessoa Física"
```

e quer chegar em:

```text
TipoCliente.PESSOA_FISICA
```

Então:

```text
"Pessoa Física"
       ↓
buscarPorNomeRelatorio()
       ↓
TipoCliente.PESSOA_FISICA
```

Isso é diferente de simplesmente comparar Strings no sistema inteiro.

A busca centraliza a conversão dentro do próprio enum.

---

# 83.8 O fluxo completo do enum

Agora junte as quatro aulas:

```text
AULA 80
Criamos o conjunto de opções

TipoCliente
├── PESSOA_FISICA
└── PESSOA_JURIDICA

        ↓

AULA 81
Cada opção pode possuir dados

PESSOA_FISICA
└── "Pessoa Física"

PESSOA_JURIDICA
└── "Pessoa Jurídica"

        ↓

AULA 82
Cada opção pode possuir comportamento

DEBITO
└── sua implementação

CREDITO
└── sua implementação

        ↓

AULA 83
Podemos procurar uma opção

"Pessoa Física"
       ↓
values()
       ↓
PESSOA_FISICA
```

Esse é o conceito central do bloco de `enum`.

---

# 9. A estrutura mental que você deve levar

Se você esquecer a sintaxe, lembre desta sequência:

## Primeiro: tenho opções fechadas?

```text
SIM
 ↓
enum
```

## Segundo: cada opção precisa guardar alguma informação?

```text
SIM
 ↓
atributos + construtor
```

## Terceiro: cada opção precisa executar uma regra diferente?

```text
SIM
 ↓
método + implementação por constante
```

## Quarto: recebi um valor e preciso descobrir qual opção representa esse valor?

```text
SIM
 ↓
values()
 ↓
percorre as constantes
 ↓
compara o atributo
 ↓
retorna a constante encontrada
```

---

# 10. Um exemplo completo para estudar

Este exemplo reúne principalmente as aulas 80–83.

```java
public enum TipoCliente {

    PESSOA_FISICA("Pessoa Física") {
        @Override
        public String gerarDescricao() {
            return "Cliente pessoa física";
        }
    },

    PESSOA_JURIDICA("Pessoa Jurídica") {
        @Override
        public String gerarDescricao() {
            return "Cliente pessoa jurídica";
        }
    };

    private String nomeRelatorio;

    TipoCliente(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public abstract String gerarDescricao();

    public static TipoCliente buscarPorNomeRelatorio(String nome) {

        for (TipoCliente tipo : TipoCliente.values()) {

            if (tipo.getNomeRelatorio().equals(nome)) {
                return tipo;
            }

        }

        return null;
    }
}
```

Não tente decorar esse código.

Leia por partes:

### Parte 1 — opções

```java
PESSOA_FISICA(...)
PESSOA_JURIDICA(...)
```

Aula 80.

### Parte 2 — dados

```java
private String nomeRelatorio;
```

e:

```java
TipoCliente(String nomeRelatorio)
```

Aula 81.

### Parte 3 — comportamento

```java
public abstract String gerarDescricao();
```

e cada constante implementa:

```java
@Override
public String gerarDescricao() {
    ...
}
```

Aula 82.

### Parte 4 — busca

```java
TipoCliente.values()
```

e:

```java
buscarPorNomeRelatorio(...)
```

Aula 83.

---

# 11. O que você NÃO deve confundir

## `enum` não é `String`

Isto:

```java
TipoCliente.PESSOA_FISICA
```

não é simplesmente:

```java
"Pessoa Física"
```

São coisas diferentes.

O primeiro é uma constante do tipo:

```java
TipoCliente
```

O segundo é uma String.

---

## A descrição não é a constante

Neste exemplo:

```java
PESSOA_FISICA("Pessoa Física")
```

temos:

```text
PESSOA_FISICA
    ↓
constante do enum

"Pessoa Física"
    ↓
valor armazenado no atributo
```

São duas coisas diferentes.

---

## O construtor não cria uma nova constante

Quando você escreve:

```java
PESSOA_FISICA("Pessoa Física")
```

você não está fazendo:

```java
new TipoCliente(...)
```

Você está declarando uma das constantes da enumeração e fornecendo os dados usados na inicialização dela.

---

## `values()` não busca pelo atributo sozinho

Isto:

```java
TipoCliente.values()
```

fornece as constantes.

Quem faz a busca é a lógica que você escreve:

```java
for (TipoCliente tipo : TipoCliente.values()) {
    // compara o atributo
}
```

---

## `abstract` no enum não significa que o enum inteiro é abstrato

Quando temos:

```java
public abstract String gerarDescricao();
```

estamos declarando um método que precisa ser implementado pelas constantes que possuem comportamento próprio.

O enum continua sendo um tipo concreto com suas constantes definidas.

---

# 12. Resumo das aulas

## Aula 77

```text
final + primitivo
        ↓
não permite nova atribuição
```

## Aula 78

```text
final + referência
        ↓
não permite trocar a referência
        ↓
objeto ainda pode ser mutável
```

## Aula 79

```text
final + classe
        ↓
não pode herdar

final + método
        ↓
não pode sobrescrever
```

## Aula 80

```text
enum
 ↓
conjunto fechado de opções
```

## Aula 81

```text
enum
 ↓
atributos
 ↓
construtor
 ↓
cada constante pode carregar dados
```

## Aula 82

```text
enum
 ↓
método
 ↓
cada constante pode possuir sua própria implementação
```

## Aula 83

```text
values()
 ↓
percorre as constantes
 ↓
compara um atributo
 ↓
retorna a constante encontrada
```

---

# 13. Checklist antes de avançar

Antes de considerar as aulas de `enum` dominadas, você deve conseguir explicar com suas próprias palavras:

- [ ] O que é um `enum`.
- [ ] Por que usar `enum` em vez de `String` quando existe um conjunto fechado de opções.
- [ ] O que é uma constante de enum.
- [ ] Como usar uma constante de enum.
- [ ] Como declarar atributos dentro de um enum.
- [ ] Para que serve o construtor do enum.
- [ ] Como cada constante recebe seus próprios dados.
- [ ] Como declarar métodos dentro de um enum.
- [ ] Como cada constante pode possuir seu próprio comportamento.
- [ ] Por que `@Override` aparece nas constantes.
- [ ] O que significa declarar um método `abstract` no enum.
- [ ] O que `values()` retorna.
- [ ] Como percorrer `values()` com `for`.
- [ ] Como procurar uma constante usando um atributo.
- [ ] O que fazer quando a busca não encontra nenhuma constante.
- [ ] A diferença entre a constante `PESSOA_FISICA` e o atributo `"Pessoa Física"`.

Se você ainda não consegue explicar principalmente estes três pontos:

```text
1. constante de enum
2. enum com construtor/atributo
3. enum com comportamento
```

não avance para a próxima aula. Volte ao exemplo completo e acompanhe o código de cima para baixo.

---

# 14. Regra dos exercícios deste bloco

Cada aula possui:

```text
1 exercício fácil
1 exercício médio
1 exercício difícil
```

Os exercícios devem usar **problemas novos**, não simplesmente copiar os exemplos apresentados na aula.

Depois de terminar todas as aulas:

```text
Aulas 77–83
      ↓
3 exercícios por aula
      ↓
correções
      ↓
DESAFIO INTEGRADOR
```

O desafio integrador reúne `final`, `enum`, atributos, construtores, comportamento, sobrescrita e busca por atributo em um único problema maior.

---

# Fonte

Conteúdo baseado nas transcrições das aulas 77–83 da playlist **Maratona Java Virado no Jiraya**.

A sequência das aulas e os títulos foram conferidos na listagem da playlist. As explicações de `enum` deste README foram reorganizadas para tornar explícita a progressão apresentada nas aulas 80–83.
