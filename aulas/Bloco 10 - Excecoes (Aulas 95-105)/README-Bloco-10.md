# Bloco 10 --- Exceções

## Aulas

-   95 --- Exceptions pt 01 --- Errors
-   96 --- Exceptions pt 02 --- RuntimeException
-   97 --- Exceções pt 03 --- Exception
-   98 --- Exceptions pt 04 --- Throwing unchecked exception
-   99 --- Exceções pt 05 --- Lançando exceção checked
-   100 --- Exceptions pt 06 --- Finally Block
-   101 --- Exceções pt 07 --- Capturando múltiplas exceções
-   102 --- Exceções pt 08 --- Multi catch em linha
-   103 --- Exceções pt 09 --- Try with resources
-   104 --- Exceções pt 10 --- Exceção customizada
-   105 --- Exceções pt 11 --- Exceção e regras de sobrescrita

> Estude uma aula por vez: explicação → 3 exercícios → correção →
> próxima aula. No final há um desafio integrador obrigatório.

------------------------------------------------------------------------

# Visão geral

Este bloco introduz o tratamento de situações excepcionais durante a
execução.

A sequência é:

``` text
Errors
  ↓
RuntimeException
  ↓
Exception
  ↓
throw / throws
  ↓
finally
  ↓
múltiplos catch
  ↓
multi-catch
  ↓
try-with-resources
  ↓
exceção customizada
  ↓
exceções + sobrescrita
```

A ideia não é simplesmente "impedir o programa de quebrar". É aprender a
representar um problema, decidir se ele deve ser tratado ou propagado e
manter o fluxo do programa organizado.

------------------------------------------------------------------------

# Aula 95 --- Errors

A primeira aula apresenta `Error` e ajuda a separar problemas graves da
execução da JVM das exceções normalmente tratadas pela aplicação.

A hierarquia importante para acompanhar o bloco é:

``` text
Throwable
├── Error
└── Exception
```

`Error` representa problemas graves relacionados à
execução/infraestrutura. Não trate `Error` simplesmente como uma
validação comum de negócio.

O ponto principal desta aula é reconhecer que `Error` e `Exception`
ocupam ramos diferentes da hierarquia.

------------------------------------------------------------------------

# Aula 96 --- RuntimeException

`RuntimeException` pertence ao ramo de `Exception`:

``` text
Throwable
├── Error
└── Exception
    └── RuntimeException
```

Exemplos de situações que podem produzir subclasses de
`RuntimeException`:

``` java
int resultado = 10 / 0;
```

``` java
int[] numeros = {1, 2, 3};
System.out.println(numeros[10]);
```

``` java
String nome = null;
System.out.println(nome.length());
```

Essas são **unchecked exceptions**: o compilador não obriga que você
declare ou capture uma `RuntimeException`.

Isso não significa que o problema deva ser ignorado.

------------------------------------------------------------------------

# Aula 97 --- Exception

Agora aparece `Exception` de forma mais ampla:

``` text
Throwable
├── Error
└── Exception
    ├── RuntimeException
    └── outras exceções
```

A diferença fundamental deste ponto do curso é:

``` text
RuntimeException
    ↓
unchecked

determinadas Exceptions
    ↓
checked
```

Uma checked exception precisa ser tratada ou declarada pelo método.

Essa diferença prepara as aulas seguintes.

------------------------------------------------------------------------

# Aula 98 --- Lançando exceção unchecked

Podemos lançar uma exceção explicitamente com `throw`:

``` java
throw new RuntimeException("Idade inválida");
```

Exemplo:

``` java
if (idade < 0) {
    throw new RuntimeException("Idade inválida");
}
```

O fluxo é:

``` text
regra inválida
    ↓
throw
    ↓
exceção lançada
```

## `throw` x `throws`

`throw` executa a ação de lançar:

``` java
throw new RuntimeException();
```

`throws` declara uma possibilidade no método:

``` java
public void executar() throws Exception {
}
```

Mentalidade:

``` text
throw  → lança
throws → declara
```

------------------------------------------------------------------------

# Aula 99 --- Lançando exceção checked

Podemos lançar uma checked exception:

``` java
throw new Exception("Problema");
```

Como `Exception` é checked, o compilador exige que ela seja tratada ou
declarada.

Exemplo de declaração:

``` java
public void executar() throws Exception {
    throw new Exception("Problema");
}
```

`throws` não significa que a exceção foi tratada. Significa que o método
declara que ela pode ser propagada.

Portanto:

``` text
checked
  ↓
tratar
OU
propagar com throws
```

------------------------------------------------------------------------

# Aula 100 --- Finally Block

Estrutura:

``` java
try {

    // código que pode falhar

} catch (Exception e) {

    // tratamento

} finally {

    // etapa final

}
```

`catch` trata uma exceção.

`finally` representa a etapa final do fluxo e é especialmente útil para
garantir ações de finalização/liberação quando necessário.

Mentalidade:

``` text
try     → tenta
catch   → trata
finally → finaliza
```

------------------------------------------------------------------------

# Aula 101 --- Capturando múltiplas exceções

Uma operação pode produzir tipos diferentes:

``` java
try {

    // código

} catch (ArithmeticException e) {

    // tratamento A

} catch (NullPointerException e) {

    // tratamento B
}
```

Use múltiplos `catch` quando os tipos precisam de tratamentos
diferentes.

A ordem importa quando existe relação de herança: uma exceção mais
específica deve ser capturada antes de uma mais genérica.

Exemplo:

``` java
catch (ArithmeticException e) {
}
catch (Exception e) {
}
```

------------------------------------------------------------------------

# Aula 102 --- Multi-catch

Quando tipos diferentes precisam do mesmo tratamento, Java permite:

``` java
try {

    // código

} catch (ArithmeticException | NullPointerException e) {

    System.out.println("Erro no processamento");

}
```

A regra mental:

``` text
tratamento diferente → catch separado
mesmo tratamento     → multi-catch
```

Não use multi-catch apenas por estética: os tipos precisam realmente
compartilhar o tratamento.

------------------------------------------------------------------------

# Aula 103 --- Try with resources

O `try-with-resources` organiza o uso de recursos que precisam ser
fechados:

``` java
try (Recurso recurso = new Recurso()) {

    // utiliza o recurso

}
```

A ideia:

``` text
abre recurso
    ↓
usa recurso
    ↓
recurso é finalizado/fechado
```

O recurso precisa cumprir o contrato apropriado para participar dessa
estrutura.

Essa construção é especialmente importante para evitar esquecer o
fechamento de recursos.

Ela se relaciona com `finally`, mas não deve ser entendida simplesmente
como "um finally diferente": seu objetivo específico é o gerenciamento
automático de recursos.

------------------------------------------------------------------------

# Aula 104 --- Exceção customizada

Podemos criar uma exceção específica do domínio:

``` java
public class SaldoInsuficienteException extends Exception {

    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }

}
```

Agora o tipo da exceção já comunica o significado do problema:

``` text
SaldoInsuficienteException
```

Em vez de usar uma `Exception` genérica para tudo.

A escolha da classe pai determina se ela será checked ou unchecked:

``` java
extends Exception
```

→ checked

``` java
extends RuntimeException
```

→ unchecked

------------------------------------------------------------------------

# Aula 105 --- Exceção e regras de sobrescrita

Esta aula conecta exceções com:

``` text
herança
+
sobrescrita
```

Imagine:

``` java
class Pai {

    public void executar() throws Exception {
    }

}
```

Uma subclasse sobrescreve esse método e precisa respeitar as regras de
exceções da sobrescrita.

O ponto fundamental:

> Uma subclasse não pode simplesmente ampliar indiscriminadamente as
> checked exceptions declaradas pelo método da classe pai.

Isso deve ser entendido junto com `@Override`, herança e as diferenças
entre checked e unchecked exceptions.

------------------------------------------------------------------------

# Modelo mental do bloco

``` text
Throwable
│
├── Error
│
└── Exception
    │
    ├── RuntimeException
    │      ↓
    │   unchecked
    │
    └── checked
```

Depois:

``` text
problema
   ↓
qual exceção representa?
   ↓
throw
   ↓
tratamento ou propagação
   ↓
try/catch/finally
   ↓
try-with-resources quando houver recurso
   ↓
exceção customizada quando o domínio precisar
```

------------------------------------------------------------------------

# Checklist

Antes de avançar, você deve conseguir explicar:

-   [ ] diferença geral entre `Error` e `Exception`;
-   [ ] o que é `RuntimeException`;
-   [ ] checked x unchecked;
-   [ ] `throw`;
-   [ ] `throws`;
-   [ ] `try/catch`;
-   [ ] `finally`;
-   [ ] múltiplos `catch`;
-   [ ] multi-catch;
-   [ ] try-with-resources;
-   [ ] exceção customizada;
-   [ ] checked/unchecked em exceções customizadas;
-   [ ] regras básicas de exceções em sobrescrita.

## Progressão

``` text
95 Errors
 ↓
96 RuntimeException
 ↓
97 Exception
 ↓
98 throw unchecked
 ↓
99 throw checked
 ↓
100 finally
 ↓
101 múltiplos catch
 ↓
102 multi-catch
 ↓
103 try-with-resources
 ↓
104 exceção customizada
 ↓
105 sobrescrita + exceções
 ↓
🏆 desafio integrador
```

## Fonte

A numeração e os títulos foram conferidos na listagem da playlist
fornecida para este projeto. O escopo deste README segue esses títulos;
ele foi organizado para estudo progressivo e não pretende substituir as
transcrições integrais das aulas.
