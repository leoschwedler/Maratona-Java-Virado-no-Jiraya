# Exercícios --- Bloco 08

> **Regra:** os exercícios não devem copiar os exemplos das aulas. Eles
> devem apresentar problemas novos e exigir decisões de implementação.
>
> Além dos 3 exercícios por aula, este bloco possui um **DESAFIO
> INTEGRADOR FINAL** que combina todo o conteúdo das aulas 77--83.

## Aula 77 --- `final` primitivo

### 1. Configurações

Crie uma classe de configuração com pelo menos 4 valores que não possam
ser reatribuídos depois da inicialização. Escolha valores coerentes e
explique a decisão.

### 2. Pedido

Crie uma classe `Pedido` com um identificador que não possa ser
reatribuído, mas com outros dados que possam mudar.

### 3. Regras de negócio

Crie uma classe `Ingresso` com pelo menos 2 informações fixas e 2
mutáveis. Implemente operações que respeitem essa diferença.

------------------------------------------------------------------------

## Aula 78 --- `final` referência

### 4. Conta e titular

Crie `Conta` e `Titular`. A conta deve manter uma referência final para
o titular, enquanto o saldo pode mudar.

### 5. Objeto mutável

Crie `Carrinho` com referência final para `Cliente`. Mostre que a
referência não muda, mas o estado do cliente pode mudar.

### 6. Diagnóstico

Crie exemplos com referência final e marque, antes de executar, quais
atribuições devem compilar e quais devem gerar erro. Depois explique
cada resultado.

------------------------------------------------------------------------

## Aula 79 --- `final` em classes e métodos

### 7. Classe protegida contra herança

Crie uma classe de configuração que não possa ser estendida. Tente criar
uma subclasse e observe o erro.

### 8. Método protegido contra sobrescrita

Crie `Relatorio` e `RelatorioFinanceiro`. Um método deve ser `final` e
outro deve poder ser sobrescrito. Demonstre a diferença.

### 9. Hierarquia controlada

Crie uma hierarquia com pelo menos três classes. Use `final` em uma
classe e em um método, e deixe outro método sobrescritível. Tente
quebrar as regras propositalmente.

------------------------------------------------------------------------

## Aula 80 --- Enumeração

### 10. Status de pedido

Crie `StatusPedido` com pelo menos 5 estados e utilize-o em `Pedido`.
Não use `String` para representar o status.

### 11. Tipos de acesso

Crie uma enumeração de tipos de acesso e utilize-a em `Usuario`. Tente
atribuir um valor inexistente e observe o que o compilador impede.

### 12. Refatoração

Imagine um sistema que usa `String` para representar cinco estados.
Refatore o modelo para `enum` sem mudar a ideia geral do sistema.

------------------------------------------------------------------------

## Aula 81 --- Enumeração com atributos

### 13. Estados com descrição

Crie uma enumeração em que cada constante possua uma descrição amigável.
Crie um método para obtê-la.

### 14. Categorias com código

Crie uma enumeração de categorias com código numérico e descrição.
Utilize construtor e métodos de acesso.

### 15. Prioridade rica

Crie uma enumeração de prioridades com pelo menos 3 atributos. Use-a em
uma classe `Tarefa`, evitando campos soltos para representar a
prioridade.

------------------------------------------------------------------------

## Aula 82 --- Enumeração com comportamento

### 16. Tipos de operação

Crie uma enumeração em que cada constante implemente uma forma diferente
de calcular um resultado. Não concentre a decisão em um `if/else`.

### 17. Regras de desconto

Crie uma enumeração de tipos de cliente. Cada constante deve possuir sua
própria regra de desconto por meio de um método abstrato.

### 18. Motor de cálculo

Crie uma enumeração de operações. O programa recebe dois valores e uma
constante da enumeração e executa o comportamento correspondente. Não
use `switch`.

------------------------------------------------------------------------

## Aula 83 --- Busca por atributos

### 19. Buscar por descrição

Crie `buscarPorDescricao(...)` em uma enumeração com código e descrição.
Use `values()`.

### 20. Buscar por código

Crie `buscarPorCodigo(...)`. Teste códigos existentes e inexistentes.

### 21. Duas formas de busca

Crie uma enumeração com dois atributos e implemente buscas pelos dois.
Evite duplicar desnecessariamente o algoritmo.

------------------------------------------------------------------------

# DESAFIO INTEGRADOR FINAL --- Sistema de Pedidos

Este desafio deve ser resolvido **somente depois das aulas 77--83**.

Você precisa criar o núcleo de um sistema de pedidos com:

``` text
Pedido
Cliente
Produto
```

Você deve decidir a modelagem.

## O projeto precisa obrigatoriamente demonstrar

-   `final` em tipo primitivo;
-   `final` em referência;
-   `final` em método;
-   `final` em classe;
-   enum simples;
-   enum com atributos;
-   enum com construtor;
-   enum com comportamento próprio por constante;
-   sobrescrita em enum;
-   busca por atributo usando `values()`;
-   herança;
-   associação;
-   construtores;
-   `super`;
-   `toString()`;
-   array de objetos.

## Regras do desafio

1.  Não copie os exemplos das aulas.
2.  Não use `String` para representar algo que seja claramente um
    conjunto fechado de opções.
3.  Não use `if/switch` para substituir comportamento que pertence às
    constantes da enumeração.
4.  Não use `final` aleatoriamente: cada uso deve ter uma justificativa.
5.  Não crie herança apenas para reutilizar código.
6.  Não concentre toda a lógica no `main`.
7.  Utilize associação quando a relação for "possui um".
8.  Utilize herança quando a relação for "é um".
9.  Use `toString()` para apresentar os objetos.
10. Crie dados suficientes para testar diferentes caminhos do sistema.

## Antes de programar

Faça primeiro um desenho:

``` text
Classes
   ↓
Herança
   ↓
Associação
   ↓
Enumerações
   ↓
Atributos
   ↓
Comportamentos
```

Depois faça uma lista indicando onde cada requisito será utilizado.

## Checklist

``` text
[ ] final primitivo
[ ] final referência
[ ] final método
[ ] final classe
[ ] enum
[ ] enum com atributos
[ ] enum com construtor
[ ] enum com comportamento
[ ] sobrescrita
[ ] busca por atributo
[ ] herança
[ ] associação
[ ] construtores
[ ] super
[ ] toString
[ ] array de objetos
```

**O objetivo é que esse desafio seja significativamente mais difícil que
qualquer exercício individual do bloco.**
