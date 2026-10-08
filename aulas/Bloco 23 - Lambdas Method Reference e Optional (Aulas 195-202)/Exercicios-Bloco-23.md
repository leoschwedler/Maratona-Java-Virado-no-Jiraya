# Exercícios — Bloco 23: Lambdas, Method Reference e Optional

## Aulas 195 a 202

Este arquivo acompanha o README do Bloco 23.

```text
Aula
 ↓
3 exercícios
 ↓
você implementa
 ↓
manda o código
 ↓
eu corrijo
 ↓
próxima aula
```

### Estrutura de cada aula

- 🟢 1 exercício fácil
- 🟡 1 exercício médio
- 🔴 1 exercício difícil

No final existe um:

- 🏆 **Desafio Integrador do Bloco**

> **Onde criar os arquivos:** `src/main/bloco23_aulas195a202_lambdas_methodref_optional/aulaXXX/`

> **Regras do bloco:**
> - **Ainda não vimos Streams.** Use laços (`for`) e as coleções que você já conhece. A única exceção são os métodos de coleção que aceitam lambda (`forEach`, `removeIf`, `sort`, `replaceAll`, `computeIfAbsent`...).
> - Sempre tente primeiro a versão com **classe anônima**, depois converta para **lambda**, depois para **method reference** quando possível (a IDE ajuda com Alt+Enter, mas você deve ser capaz de converter à mão).

---

# Aula 195 — Lambdas pt 01: Predicate

## 🟢 Exercício 01 — Sintaxes equivalentes

Escreva um `Predicate<String>` que testa se a string tem mais de 5 letras, nas formas:

1. classe anônima;
2. lambda completa `(String s) -> { return ...; }`;
3. lambda com tipo inferido e sem chaves;
4. lambda sem parênteses.

Teste as quatro com `"Java"` e `"Programação"`.

### Responda

- Quando os parênteses do parâmetro são obrigatórios?
- Quando o `return` é obrigatório?

---

## 🟡 Exercício 02 — Filtro genérico

Crie `static <T> List<T> filtrar(List<T> lista, Predicate<T> p)` (**você** implementa, sem usar stream) e use com:

1. `List<Integer>`: números pares maiores que 10.
2. `List<String>`: palavras que começam com vogal.
3. `List<Pessoa>` (`nome`, `idade`): maiores de idade.
4. Combine com `and`, `or` e `negate`: maiores de idade **e** cujo nome comece com "A", ou menores de 12.

---

## 🔴 Exercício 03 — Interface funcional própria

Crie sua interface funcional `Validador<T>` com `boolean validar(T t)` e **um método default** `Validador<T> e(Validador<T> outro)` que combina dois validadores.

1. Anote com `@FunctionalInterface` e prove (comentado) que adicionar um segundo método abstrato quebra a compilação.
2. Crie validadores para `String` (e-mail simples, tamanho mínimo, sem espaços) e combine-os.
3. Faça um método `static <T> List<String> relatorio(List<T> itens, Validador<T> v)` que devolve a lista de textos "item X: válido/inválido".
4. Explique por que os métodos `default` e `static` não contam para a regra de "um só método abstrato".

---

# Aula 196 — Lambdas pt 02: Consumer

## 🟢 Exercício 04 — Meu `forEach`

Implemente `static <T> void paraCada(List<T> lista, Consumer<T> acao)` e use para:

1. imprimir cada nome de uma lista;
2. imprimir cada número ao quadrado;
3. imprimir cada `Pessoa` no formato `Nome (idade)`.

Depois repita usando o `forEach` nativo da lista.

---

## 🟡 Exercício 05 — Encadeando ações

Use `Consumer.andThen` para montar um pipeline de ações sobre cada `Pedido`:

1. imprimir resumo;
2. acumular o total em um array de uma posição (`double[] total = {0}`);
3. marcar como `PROCESSADO` (setter).

Explique por que o `total` precisa ser um array/objeto e não uma `double` simples (efetivamente final).

---

## 🔴 Exercício 06 — Auditoria

Crie um sistema de auditoria onde cada operação sobre uma lista de contas bancárias dispara uma lista de `Consumer<Conta>` "ouvintes":

```text
ouvinteLog         → imprime "[LOG] ..."
ouvinteAlerta      → imprime alerta se saldo < 0
ouvinteEstatistica → atualiza contadores (use um objeto Estatistica)
```

Implemente `aplicar(List<Conta>, Consumer<Conta> operacao, List<Consumer<Conta>> ouvintes)`: executa a operação e depois notifica todos os ouvintes. Teste com operações "depositar 100", "sacar 500" e "cobrar tarifa".

---

# Aula 197 — Lambdas pt 03: Function

## 🟢 Exercício 07 — Meu `map`

Implemente `static <T, R> List<R> mapear(List<T> lista, Function<T, R> f)` e use para:

1. tamanho de cada string;
2. cada nome em maiúsculas;
3. cada `Pessoa` → seu `nome`;
4. cada número → "par" ou "ímpar".

---

## 🟡 Exercício 08 — Composição

Dadas as funções:

```java
Function<Integer, Integer> dobro = n -> n * 2;
Function<Integer, Integer> maisDez = n -> n + 10;
Function<Integer, String> texto = n -> "Resultado: " + n;
```

Mostre o resultado de `dobro.andThen(maisDez)`, `dobro.compose(maisDez)` e `dobro.andThen(maisDez).andThen(texto)` para os valores 1, 5 e 10. Explique a diferença entre `andThen` e `compose` em comentário.

---

## 🔴 Exercício 09 — Conversor universal

Crie um pequeno framework de conversões:

```text
Conversor<T, R> (usando Function<T,R>) com nome e descrição
registro: List<Conversor<?, ?>> (ou Map<String, Function<...>>)
converter(valor, "celsius->fahrenheit")
```

Implemente pelo menos 6 conversões (temperatura, distância, texto→inteiro, inteiro→texto formatado, nome→iniciais, data ISO→formato BR), permita **encadear** duas conversões e trate o caso "conversão inexistente" com `Optional`.

---

# Aula 198 — Method Reference pt 01: Estáticos

## 🟢 Exercício 10 — Lambda → method reference

Converta para method reference (e confira com a IDE):

```text
s -> Integer.parseInt(s)
n -> Math.abs(n)
(a, b) -> Integer.compare(a, b)
s -> String.valueOf(s)
() -> LocalDate.now()
```

Escreva a interface funcional correta para cada uma (`Function<String,Integer>`, etc.).

---

## 🟡 Exercício 11 — Comparadores estáticos

Crie `Produto` (`nome`, `preco`, `estoque`) e a classe `ProdutoComparators` com métodos **estáticos**:

```text
porNome, porPrecoCrescente, porPrecoDecrescente, porEstoque
```

Ordene uma lista de 8 produtos com `lista.sort(ProdutoComparators::porNome)` etc. Mostre o resultado de cada ordenação.

### Responda

Qual a diferença entre `ProdutoComparators::porNome` e `ProdutoComparators.porNome(a, b)`?

---

## 🔴 Exercício 12 — Registro de operações

Crie `Calculadora` com métodos estáticos `somar`, `subtrair`, `multiplicar`, `dividir`, `potencia`, `maximo`, `minimo` (todos `(double, double) → double`).

Monte um `Map<String, BinaryOperator<Double>>` com chaves `"+", "-", "*", "/", "^", "max", "min"` apontando para **method references** e implemente uma mini calculadora de expressões em notação pós-fixa (ex.: `"3 4 + 2 *"`), usando o mapa para achar a operação. Trate divisão por zero e operador desconhecido.

---

# Aula 199 — Method Reference pt 02: Não estáticos

## 🟢 Exercício 13 — Objeto específico

Crie `Impressora` com o método de instância `imprimir(String)` (que coloca um prefixo). Passe `impressora::imprimir` para `forEach` de uma lista. Depois use `System.out::println`.

Escreva a lambda equivalente de cada um.

---

## 🟡 Exercício 14 — Objeto arbitrário do tipo

Converta e explique (qual é o "dono" do método e qual é o parâmetro):

```text
(a, b) -> a.compareToIgnoreCase(b)       → String::compareToIgnoreCase
s -> s.trim()                             → String::trim
(lista, x) -> lista.contains(x)          → List::contains   (BiPredicate)
(s, prefixo) -> s.startsWith(prefixo)    → String::startsWith
p -> p.getNome()                          → Pessoa::getNome
```

Escreva um programa que use cada um com a interface funcional correta e uma chamada de teste.

---

## 🔴 Exercício 15 — Mapa de referências

Para uma classe `Funcionario` (`nome`, `salario`, `departamento`), escreva uma lista com os 4 tipos de method reference aplicados a:

1. `Funcionario::getSalario` (objeto arbitrário);
2. `reajustador::aplicar` (objeto específico, uma classe sua);
3. `Reajuste::percentualPadrao` (estático);
4. `Funcionario::new` (construtor).

Monte um pipeline sem streams (laços) que: cria funcionários a partir de strings, calcula o reajuste, ordena e imprime. Anote em comentário, ao lado de cada referência, a lambda equivalente.

---

# Aula 200 — Method Reference pt 03: Construtores

## 🟢 Exercício 16 — Supplier

Crie `Supplier<List<String>>` com `ArrayList::new` e `Supplier<Pessoa>` com `Pessoa::new` (construtor vazio). Chame `get()` três vezes e prove que são três objetos diferentes (`==`).

---

## 🟡 Exercício 17 — BiFunction e Function

Crie `Pessoa` com construtores `Pessoa(String nome)` e `Pessoa(String nome, int idade)`.

1. `Function<String, Pessoa> a = Pessoa::new;`
2. `BiFunction<String, Integer, Pessoa> b = Pessoa::new;`
3. Use `a` e `b` para criar objetos a partir de uma lista de nomes e uma lista de pares nome/idade.

Explique como o Java decidiu qual construtor usar em cada caso.

---

## 🔴 Exercício 18 — Fábrica configurável

Implemente `FabricaDeAnimais` com um `Map<String, Supplier<Animal>>`:

```text
"cachorro" → Cachorro::new
"gato"     → Gato::new
"pato"     → Pato::new
```

1. `criar(String tipo)` devolve `Optional<Animal>`.
2. Permita **registrar** novos tipos em tempo de execução.
3. Crie uma versão com parâmetro: `Map<String, Function<String, Animal>>` (o nome do animal).
4. Gere 10 animais aleatórios por tipo (use `Random` com semente fixa) e imprima o som de cada um.

---

# Aula 201 — Optional pt 01

## 🟢 Exercício 19 — Criar e extrair

1. Crie três `Optional<String>` com `of`, `ofNullable(null)` e `empty`.
2. Para cada um, imprima `isPresent`, `isEmpty` e o resultado de `orElse("vazio")`.
3. Tente `Optional.of(null)` e capture a exceção.
4. Tente `.get()` em um vazio e capture a exceção.

---

## 🟡 Exercício 20 — Método que pode não achar

Crie `static Optional<Funcionario> buscarPorNome(List<Funcionario> lista, String nome)` e use:

1. `ifPresent` para imprimir o salário se achar;
2. `orElse` com um funcionário "padrão";
3. `orElseThrow` com `NoSuchElementException` personalizada;
4. `map(Funcionario::getSalario).orElse(0.0)`.

Teste com um nome existente e outro inexistente.

---

## 🔴 Exercício 21 — Cadeia sem `if`

Dado `Usuario` → `Endereco` → `Cidade` → `nome`, onde qualquer nível pode ser nulo:

1. Escreva `String cidadeDoUsuario(Usuario u)` com a **pirâmide de `if (x != null)`**.
2. Reescreva usando `Optional.ofNullable(...).map(...).map(...).orElse("Desconhecida")`.
3. Altere os getters para retornarem `Optional` e use `flatMap`.
4. Compare legibilidade e explique quando `flatMap` é necessário e quando `map` basta.

---

# Aula 202 — Optional pt 02

## 🟢 Exercício 22 — Onde não usar

Liste (em comentário) 4 usos inadequados de `Optional` (parâmetro, atributo, coleção, `get()` sem verificar) e, para cada um, escreva o exemplo **ruim** e a **alternativa correta**.

---

## 🟡 Exercício 23 — Repositório com Optional

Implemente `ProdutoRepositorio` (lista em memória) com:

```text
Optional<Produto> findById(Long id)
Optional<Produto> findByNome(String nome)
Optional<Produto> findPrimeiro(Predicate<Produto> p)   // base das outras
```

Resolva três requisitos:

1. Se o produto existir, aumentar o preço em 10%.
2. Buscar por id; se não existir, lançar `IllegalArgumentException` com o id.
3. Buscar por nome; se não existir, criar um novo produto e adicioná-lo ao repositório (`orElseGet`).

---

## 🔴 Exercício 24 — Serviço de pedidos sem null

Crie `PedidoService` com as regras:

- `buscarCliente(id)` → `Optional<Cliente>`
- `buscarProduto(codigo)` → `Optional<Produto>`
- `obterDesconto(Cliente)` → `Optional<Double>` (só clientes VIP têm)
- `criarPedido(idCliente, codigoProduto, qtd)` → `Optional<Pedido>`: devolve vazio se cliente ou produto não existirem ou se o estoque for insuficiente.

Regras:

1. Nenhum `null` explícito nem `if (x == null)`.
2. Use `map`, `flatMap`, `filter`, `orElse`, `orElseThrow`, `ifPresentOrElse` (Java 9+) onde fizer sentido.
3. Teste 8 cenários (feliz e de erro) e imprima mensagens claras de cada caminho.

---

# 🏆 Desafio Integrador do Bloco 23 — Biblioteca de Funções para um Sistema de RH

Você vai construir o núcleo de um sistema de RH funcional (sem Streams).

## Domínio

```text
Funcionario → id, nome, cargo, departamento, salario, dataAdmissao, ativo
Departamento → nome, gerente (Optional<Funcionario>)
RH → lista de funcionários e departamentos
```

## Parte 1 — Infraestrutura funcional (sua)

Implemente utilitários genéricos usando **apenas as interfaces de `java.util.function`**:

```text
filtrar(List<T>, Predicate<T>)
mapear(List<T>, Function<T,R>)
paraCada(List<T>, Consumer<T>)
reduzir(List<T>, T inicial, BinaryOperator<T>)
agrupar(List<T>, Function<T,K>) → Map<K, List<T>>
ordenarPor(List<T>, Function<T, ? extends Comparable>)   (pode ser com Comparator)
contarSe, qualquerUm, todos, nenhum
```

## Parte 2 — Regras de negócio (com lambdas e method references)

1. Folha de pagamento por departamento (agrupar + reduzir).
2. Funcionários admitidos há mais de 5 anos com reajuste de 8% (Consumer + setter).
3. Top 3 salários (ordenar + limitar).
4. Departamentos sem gerente (Optional).
5. Aniversariantes de contrato no mês corrente.
6. Relatório "salário mais alto por cargo".
7. Buscar funcionário por id retornando `Optional`, com tratamento nos 3 estilos: `ifPresent`, `orElseThrow`, `orElseGet`.
8. Criação por fábrica: `Map<String, Supplier<Funcionario>>` para modelos de cargo ("estagiario", "pleno", "senior") com salários base.
9. Method references: `Funcionario::getNome`, `Funcionario::isAtivo`, `Funcionario::new`, `this::calcularBonus`, `String::compareToIgnoreCase`, `System.out::println`.

## Parte 3 — Comparação

Escreva um comentário final comparando:

- a quantidade de linhas e a legibilidade da versão com classe anônima, lambda e method reference;
- onde o `Optional` removeu `if`s de verificação de `null`;
- onde **não** vale a pena usar lambda (lambda com mais de 3 linhas deveria virar método com nome).

## Cenário de teste

```text
15 funcionários, 4 departamentos (um sem gerente)
Execute todas as regras e imprima os resultados formatados
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei o que é uma interface funcional.
- [ ] Sei escrever lambdas nas várias sintaxes.
- [ ] Conheço `Predicate`, `Consumer`, `Function`, `Supplier`, `BiFunction`, `UnaryOperator`, `BinaryOperator`.
- [ ] Sei criar métodos genéricos que recebem essas interfaces.
- [ ] Sei combinar com `and`, `or`, `negate`, `andThen`, `compose`.
- [ ] Sei os 4 tipos de method reference.
- [ ] Sei converter lambda ↔ method reference.
- [ ] Sei por que variáveis capturadas precisam ser efetivamente finais.
- [ ] Sei criar `Optional` (`of`, `ofNullable`, `empty`).
- [ ] Sei extrair com `orElse`, `orElseGet`, `orElseThrow`, `ifPresent`.
- [ ] Sei encadear com `map`, `flatMap`, `filter`.
- [ ] Sei onde NÃO usar `Optional`.

---

# Regra para as correções

Quando você mandar cada exercício, a correção seguirá esta ordem:

```text
1. Verificar se funciona
2. Verificar se você entendeu o conceito
3. Apontar problemas
4. Dar uma dica
5. Você tenta corrigir
6. Só mostrar a solução completa se necessário
```

Não vou simplesmente entregar o código pronto na primeira tentativa.

O objetivo é você **aprender o conteúdo e conseguir escrever o código sozinho**.
