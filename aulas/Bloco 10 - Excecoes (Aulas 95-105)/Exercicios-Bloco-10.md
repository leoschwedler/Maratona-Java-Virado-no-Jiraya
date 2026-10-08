# Exercícios --- Bloco 10 --- Exceções

> Faça uma aula por vez. Depois de resolver os 3 exercícios, envie o
> código para correção. Não avance antes da correção.

# Aula 95 --- Errors

### 1. Fácil

Provoque uma situação problemática em execução, sem `try/catch`. Observe
a exceção/mensagem apresentada e identifique a linha do problema.

### 2. Médio

Pesquise no próprio projeto a relação entre `Throwable`, `Error` e
`Exception` e escreva um pequeno exemplo que ajude a visualizar os dois
ramos.

### 3. Difícil

Crie três situações de execução diferentes e documente qual problema
ocorre em cada uma e por que ele não deve ser tratado simplesmente como
uma regra de negócio.

# Aula 96 --- RuntimeException

### 4. Fácil

Crie um método de divisão. Teste o divisor zero e observe a
`RuntimeException`.

### 5. Médio

Crie um cadastro cuja idade negativa provoque uma `RuntimeException`. A
regra deve ficar na classe responsável pelo cadastro.

### 6. Difícil

Crie uma operação de transferência bancária com pelo menos duas regras
que possam lançar unchecked exceptions. O `main` apenas deve consumir a
operação.

# Aula 97 --- Exception

### 7. Fácil

Crie um método que declare `throws Exception` e faça o chamador tratar a
exceção.

### 8. Médio

Crie três métodos em cadeia. O último lança uma checked exception e ela
deve ser propagada até uma camada superior.

### 9. Difícil

Crie três camadas de uma pequena aplicação e escolha conscientemente
onde a exceção deve ser tratada. Explique a decisão em comentário.

# Aula 98 --- Throwing unchecked exception

### 10. Fácil

Lance explicitamente uma `RuntimeException` quando um valor for
inválido.

### 11. Médio

Crie uma `Conta` em que um saque maior que o saldo lance uma unchecked
exception.

### 12. Difícil

Crie três operações de negócio com regras diferentes e exceções
unchecked. Não coloque as regras dentro do `main`.

# Aula 99 --- Checked exception

### 13. Fácil

Crie um método que lance `Exception` e declare `throws Exception`.
Trate-o no chamador.

### 14. Médio

Propague uma checked exception por três métodos até uma camada
responsável pelo tratamento.

### 15. Difícil

Crie uma operação que possa falhar por uma condição que você considere
apropriada para uma checked exception. Escolha tratar ou propagar e
justifique.

# Aula 100 --- Finally

### 16. Fácil

Crie `try/catch/finally` e imprima uma mensagem em cada bloco. Teste com
e sem exceção.

### 17. Médio

Crie um recurso fictício com `fechar()` e garanta a chamada usando
`finally`.

### 18. Difícil

Crie caminhos normal e excepcional que precisem executar uma mesma etapa
final. Use `finally`.

# Aula 101 --- Múltiplas exceções

### 19. Fácil

Crie duas exceções diferentes e trate cada uma com um `catch`.

### 20. Médio

Crie uma hierarquia de exceções e organize os `catch` na ordem correta.
Observe por que a ordem incorreta pode gerar erro de compilação.

### 21. Difícil

Crie um serviço que possa gerar três tipos de exceção, cada um com
tratamento específico.

# Aula 102 --- Multi-catch

### 22. Fácil

Faça duas exceções diferentes compartilharem o mesmo tratamento com
multi-catch.

### 23. Médio

Crie três exceções: duas com o mesmo tratamento e uma com tratamento
diferente. Use multi-catch somente nas duas primeiras.

### 24. Difícil

Comece com três `catch` e refatore apenas os tratamentos realmente
iguais para multi-catch.

# Aula 103 --- Try-with-resources

### 25. Fácil

Use um recurso fechável com try-with-resources e observe o encerramento.

### 26. Médio

Faça uma exceção acontecer dentro do try-with-resources e verifique que
o recurso ainda é fechado.

### 27. Difícil

Use múltiplos recursos no mesmo try-with-resources e observe o
fechamento quando o processamento falha.

# Aula 104 --- Exceção customizada

### 28. Fácil

Crie `SaldoInsuficienteException` e faça uma operação lançá-la.

### 29. Médio

Crie duas exceções customizadas: uma checked e outra unchecked. Use cada
uma em um cenário coerente.

### 30. Difícil

Crie um domínio de pedidos com pelo menos duas exceções customizadas que
tenham significado real no domínio.

# Aula 105 --- Exceções e sobrescrita

### 31. Fácil

Crie uma classe pai com método que declara uma checked exception e
sobrescreva-o corretamente na subclasse.

### 32. Médio

Crie duas subclasses e faça ambas sobrescreverem o mesmo método,
respeitando as regras de exceções.

### 33. Difícil

Monte uma hierarquia de três níveis e documente por que as declarações
de exceção de cada sobrescrita são válidas.

# 🏆 DESAFIO INTEGRADOR --- Sistema de processamento de pedidos

Crie um sistema com:

-   `Pedido`;
-   `Cliente`;
-   itens do pedido;
-   serviço de processamento.

O sistema deve reunir **todo o conteúdo das aulas 95--105**.

## Requisitos

1.  Crie pelo menos duas exceções customizadas de domínio.
2.  Escolha conscientemente quais são checked e quais são unchecked.
3.  Use `throw` nas regras de negócio.
4.  Use `throws` para propagar pelo menos uma checked exception.
5.  Tenha uma camada responsável pelo tratamento.
6.  Use múltiplos `catch`.
7.  Use multi-catch onde os tratamentos realmente forem iguais.
8.  Use `finally` em uma operação que possua etapa final necessária.
9.  Use try-with-resources para um recurso que precise ser fechado.
10. Tenha uma pequena hierarquia de classes com método sobrescrito e
    respeite as regras de exceções.

## Restrições

Não faça um `main` gigante.

Não coloque `catch (Exception e)` indiscriminadamente.

Não crie exceções customizadas apenas para cumprir requisito.

Não use `throws Exception` em todos os métodos sem necessidade.

Não use `try/catch` como substituto de toda validação de negócio.

## Você deverá conseguir apontar no próprio código

``` text
onde está a checked exception?
onde está a unchecked?
onde estão throw e throws?
onde acontece o tratamento?
onde está finally?
onde está multi-catch?
onde está try-with-resources?
quais são as exceções customizadas?
por que são checked ou unchecked?
onde está a sobrescrita envolvendo exceções?
```

Se você não conseguir explicar essas decisões, o desafio ainda não está
concluído.

# Progressão

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
105 regras de sobrescrita
 ↓
🏆 desafio integrador
```
