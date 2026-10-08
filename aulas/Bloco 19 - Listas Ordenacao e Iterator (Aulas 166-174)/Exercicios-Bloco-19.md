# Exercícios — Bloco 19: Listas, Ordenação e Iterator

## Aulas 166 a 174

Este arquivo acompanha o README do Bloco 19.

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

> **Onde criar os arquivos:** `src/main/bloco19_aulas166a174_listas_ordenacao_iterator/aulaXXX/`

> **Regras do bloco:**
> - Sempre declare a variável com a **interface** (`List<T>`).
> - Sempre use **generics**.
> - Ainda não vimos lambdas nem streams: **não use**, exceto `removeIf` na aula 174 (e só onde o enunciado permitir).

---

# Aula 166 — List pt 01

## 🟢 Exercício 01 — Lista de compras

Crie uma `List<String>` com 6 itens de compra. Imprima:

1. A lista inteira.
2. Cada item com seu número (`1 - Arroz`), usando `for` com índice.
3. Cada item com `for-each`.
4. O tamanho e o primeiro e o último item.

### Você deve conseguir responder

- Qual a diferença entre `array.length` e `lista.size()`?
- Por que declaramos `List<String>` e não `ArrayList<String>`?

---

## 🟡 Exercício 02 — Antes e depois dos generics

Em um mesmo programa:

1. Crie uma lista **sem generics** (`List lista = new ArrayList();`) e adicione uma `String`, um `Integer` e um `boolean`.
2. Percorra com `for (Object o : lista)` e imprima o tipo de cada elemento (`o.getClass().getSimpleName()`).
3. Tente somar todos os números dentro dela (somente os `Integer`) usando `instanceof` e cast.
4. Agora crie `List<Integer>` e mostre que `add("texto")` não compila (deixe a linha comentada com o erro explicado).

Explique em comentário por que os generics reduzem bugs.

---

## 🔴 Exercício 03 — Agenda de tarefas

Crie a classe `Tarefa` (`titulo`, `prioridade` 1-5, `concluida`).

Faça um menu de console (pode ser com comandos fixos no `main`, simulando um menu) com as operações implementadas em métodos separados:

```text
adicionar(List<Tarefa>, Tarefa)
listar(List<Tarefa>)
concluir(List<Tarefa>, int posicao)
contarPendentes(List<Tarefa>)
```

Regras:

- Valide posições inválidas **antes** de acessar (`posicao < 0 || posicao >= size()`).
- Mostre o total de tarefas, concluídas e pendentes.
- Teste com 8 tarefas, algumas inválidas.

---

# Aula 167 — List pt 02

## 🟢 Exercício 04 — Wrappers em ação

Crie uma `List<Integer>` com os números de 1 a 10 usando um laço. Remova o número 5 **pelo valor** e o elemento da posição 0 **pelo índice**. Imprima o resultado antes e depois.

### Pergunta

O que acontece se você chamar `numeros.remove(5)`? Remove o valor 5 ou a posição 5? Teste e explique.

---

## 🟡 Exercício 05 — Juntando e limpando

Crie duas listas de nomes (`turmaA` com 5 nomes e `turmaB` com 5 nomes, com 2 repetidos entre elas).

1. Junte tudo em `todos` usando `addAll`.
2. Remova **todas** as ocorrências de nomes repetidos (deixe uma só de cada) usando laços e `contains` (sem `Set`).
3. Informe quantos duplicados foram removidos.

---

## 🔴 Exercício 06 — Laço que não termina

Reproduza propositalmente os dois bugs:

1. `ConcurrentModificationException` ao adicionar dentro de um for-each. Capture a exceção e imprima a mensagem.
2. Laço aparentemente infinito ao adicionar dentro de um `for` com `i < lista.size()` (use um contador de segurança para abortar após 1000 iterações).

Depois escreva a versão **correta** de cada uma:

- Duplicar cada elemento da lista (cada nome aparece duas vezes seguidas).
- Remover os nomes com menos de 4 letras.

Explique em comentário qual abordagem usou em cada caso e por quê.

---

# Aula 168 — List pt 03

## 🟢 Exercício 07 — Lista de celulares

Reutilize a classe `Smartphone` (`serialNumber`, `marca`) com `equals`, `hashCode` e `toString` por `serialNumber`.

Crie uma lista com 4 celulares, imprima e use `contains` com um **novo objeto** de mesmo serial. Depois remova o `equals` temporariamente e veja o resultado mudar. Explique.

---

## 🟡 Exercício 08 — Quem está onde?

Com uma lista de 6 `Smartphone`:

1. Crie o método `static int posicaoDe(List<Smartphone> lista, String serial)` que devolve o índice (ou `-1`).
2. Crie `static boolean inserirSemDuplicar(List<Smartphone> lista, Smartphone s)`.
3. Crie `static void inserirNoInicio(...)`.
4. Teste cada método e mostre a lista depois de cada operação.

Use um *breakpoint* no `equals` para contar quantas vezes ele é chamado em cada `contains`/`indexOf`.

---

## 🔴 Exercício 09 — Biblioteca simples

Crie `Livro` (`isbn`, `titulo`, `autor`, `disponivel`) com `equals`/`hashCode` por `isbn`.

Implemente a classe `Biblioteca` com uma `List<Livro>`:

```text
cadastrar(Livro)            → rejeita ISBN repetido
emprestar(String isbn)      → marca indisponível; erro se não existir ou já emprestado
devolver(String isbn)
buscarPorAutor(String)      → devolve nova lista
remover(String isbn)
relatorio()                 → imprime disponíveis x emprestados
```

Regras:

- Não exponha a lista interna para fora (devolva cópia: `new ArrayList<>(livros)`).
- Cada erro de negócio deve ter uma mensagem clara.
- Teste com pelo menos 10 operações, incluindo as inválidas.

### Pergunta

Por que devolver a lista interna diretamente é perigoso?

---

# Aula 169 — Sorting lists pt 01

## 🟢 Exercício 10 — Ordenando o simples

Crie listas de `String`, `Integer` e `Double` com 6 valores cada, fora de ordem. Ordene todas com `Collections.sort` e também uma com `lista.sort(null)`. Imprima antes e depois.

Experimente com `"banana", "Abacaxi", "caju", "Uva"` e responda: qual vem primeiro, `"Uva"` ou `"banana"`? Por quê?

---

## 🟡 Exercício 11 — Ordem decrescente

Sem usar lambdas e sem `Comparator` customizado, descubra como obter a lista em ordem **decrescente** usando apenas o que a classe `Collections` oferece (dica: pesquise `Collections.reverse` e `Collections.reverseOrder()`).

Resolva de duas formas:

1. Ordena e depois inverte.
2. Ordena já em ordem inversa.

Aplique a `List<Integer>` e `List<String>`.

---

## 🔴 Exercício 12 — Preparando a classe `Produto`

Crie `Produto` (`id` Long, `nome`, `preco`, `quantidade`) com:

- validação de nulos no construtor (`Objects.requireNonNull` com mensagens);
- `equals` e `hashCode` por `id`;
- `toString` legível.

Escreva um teste que:

1. Tenta criar produtos com `null` em cada campo obrigatório e captura os `NullPointerException`, imprimindo as mensagens.
2. Cria uma lista com 8 produtos desordenados.
3. Tenta `Collections.sort(produtos)` (deixe comentado, com o erro de compilação anotado) e explica o que falta.

---

# Aula 170 — Comparable

## 🟢 Exercício 13 — Ordem natural por nome

Faça `Produto implements Comparable<Produto>` ordenando por **nome** (ordem alfabética). Ordene a lista e imprima.

---

## 🟡 Exercício 14 — Três `compareTo`

Para o mesmo `Produto`, implemente (um por vez, mantendo os anteriores comentados) o `compareTo` por:

1. `id` (delegando para `Long.compareTo`).
2. `preco` (usando `Double.compare`).
3. `quantidade` (usando `Integer.compare`).

Para cada versão, imprima a lista ordenada. Qual regra o valor retornado (negativo, zero, positivo) segue? Escreva em comentário.

---

## 🔴 Exercício 15 — Critério composto

Implemente um `compareTo` com **desempate**:

1. Primeiro por `quantidade` (menor primeiro).
2. Em caso de empate, por `preco` (maior primeiro).
3. Em caso de novo empate, por `nome`.

Teste com pelo menos 10 produtos, forçando empates em cada nível. Depois, verifique a consistência com o `equals`:

- Crie dois produtos com `id` diferentes mas todos os demais campos iguais. O `compareTo` retorna 0? O `equals` retorna `true`? Explique o problema de inconsistência.

---

# Aula 171 — Comparator

## 🟢 Exercício 16 — Meu primeiro Comparator

Crie `ProdutoPorPrecoComparator implements Comparator<Produto>` e ordene a lista com `Collections.sort(lista, comparator)` e com `lista.sort(comparator)`.

---

## 🟡 Exercício 17 — Vários critérios sem mexer na classe

Mantendo `Produto` com ordem natural por nome, crie 4 comparators externos:

```text
por id
por preço crescente
por preço decrescente
por quantidade
```

Escreva um método `static void imprimirOrdenado(List<Produto> lista, Comparator<Produto> c, String titulo)` e use-o para exibir a mesma lista sob cada critério, sem alterar a lista original (ordene uma cópia).

---

## 🔴 Exercício 18 — Ordenação encadeada

Crie uma classe `OrdenadorDeProdutos` que recebe **uma lista de comparators** e ordena aplicando-os em sequência como critérios de desempate (o primeiro que der diferente de zero decide).

Teste:

1. `[porQuantidade, porPrecoDecrescente, porNome]`.
2. `[porNome]`.
3. Lista de comparators vazia (deve manter a ordem original; lance exceção ou trate).

Estenda a ideia para ordenar `Pessoa`s por sobrenome, depois nome, depois idade.

### Pergunta

Quando usar `Comparable` e quando usar `Comparator`?

---

# Aula 172 — Binary Search

## 🟢 Exercício 19 — Achando posições

Crie uma lista de inteiros com 10 números, ordene e use `Collections.binarySearch` para buscar:

- um número presente (primeiro, meio e último),
- um ausente no meio,
- um menor que todos,
- um maior que todos.

Converta o retorno negativo em ponto de inserção (`-r - 1`) e imprima.

---

## 🟡 Exercício 20 — Inserção ordenada

Implemente `static void inserirOrdenado(List<Integer> lista, int valor)` que usa `binarySearch` para achar o ponto de inserção e insere com `add(indice, valor)`, mantendo a lista sempre ordenada.

Insira 20 valores aleatórios (com `Random`, semente fixa) e verifique no final que está ordenada.

---

## 🔴 Exercício 21 — Busca em objetos

Com `Produto`s ordenados por `id` (via `Comparator`):

1. Use `binarySearch` com um produto "modelo" (só com o `id` relevante) para achar um produto.
2. Mostre o que acontece se buscar com um comparator **diferente** do usado na ordenação.
3. Compare, para 100 mil produtos, o tempo de `indexOf` (linear) com o de `binarySearch` (binária) em 1000 buscas.
4. Conte (com um comparator que incrementa um contador) quantas comparações a busca binária fez para 100 mil elementos e confirme que está perto de `log2(n)`.

---

# Aula 173 — Conversão Lista ↔ Array

## 🟢 Exercício 22 — Ida e volta

Converta:

1. Uma `List<String>` em `String[]`.
2. Um `Integer[]` em `List<Integer>` com `Arrays.asList`.
3. Imprima ambos.

---

## 🟡 Exercício 23 — A lista que é uma janela

Com `Integer[] vetor = {1, 2, 3, 4}`:

1. Crie `List<Integer> janela = Arrays.asList(vetor)`.
2. Altere `janela.set(0, 99)` e imprima o vetor.
3. Altere `vetor[1] = 77` e imprima a lista.
4. Tente `janela.add(5)` e capture a exceção.
5. Crie uma cópia independente com `new ArrayList<>(janela)` e prove que alterar a cópia não afeta o vetor.

Explique por que `Arrays.asList` tem esse comportamento.

---

## 🔴 Exercício 24 — Comparando fábricas de listas

Monte uma tabela de testes (impressa no console) para `Arrays.asList`, `List.of`, `new ArrayList<>(...)` e `Collections.unmodifiableList(...)`, informando se cada operação **funciona** ou **lança** qual exceção:

```text
add       remove       set        add(null)      alterar o array de origem
```

Para cada célula, capture a exceção e preencha a tabela programaticamente (use `try/catch` e um método auxiliar).

---

# Aula 174 — Iterator

## 🟢 Exercício 25 — Remoção segura

Crie uma lista de 10 inteiros. Remova todos os números pares usando `Iterator`. Imprima antes e depois.

---

## 🟡 Exercício 26 — Duas formas de remover

Para uma lista de `Produto` com algumas quantidades zero:

1. Remova os produtos com `quantidade == 0` usando `Iterator`.
2. Repita o cenário (crie a lista de novo) usando `removeIf`.
3. Reproduza o erro de `ConcurrentModificationException` com for-each e capture-o.

Qual das três formas você usaria no dia a dia? Por quê?

---

## 🔴 Exercício 27 — Limpeza com relatório

Escreva um método `static List<Produto> limparEstoque(List<Produto> estoque)` que, com **uma única passada** usando `Iterator`:

1. Remove produtos com quantidade zero **e** preço acima de 100 (descontinuados).
2. Aplica 10% de desconto aos produtos com quantidade acima de 50 (altera o objeto — use um setter).
3. Mantém os demais.
4. Retorna uma **lista com os removidos** (para auditoria).

Imprima antes/depois e a lista de removidos. Teste com 15 produtos e com uma lista vazia.

### Pergunta

Por que `it.remove()` é seguro e `lista.remove(obj)` dentro do laço não?

---

# 🏆 Desafio Integrador do Bloco 19 — Sistema de Gerenciamento de Pedidos

Você vai construir o núcleo de um sistema de pedidos usando **apenas listas**.

## Classes

```text
Cliente    → id, nome, cidade
Produto    → id, nome, preco, estoque   (Comparable por nome)
ItemPedido → produto, quantidade
Pedido     → id, cliente, List<ItemPedido>, data
Loja       → List<Produto> catalogo, List<Cliente> clientes, List<Pedido> pedidos
```

## Requisitos

1. **Validação e igualdade**: `Cliente` e `Produto` com `equals`/`hashCode` por `id`; construtores com `Objects.requireNonNull`; `toString` legível.
2. **Cadastro** sem duplicidade (`contains`/`indexOf`) para clientes e produtos.
3. **Pedido**: adicionar item (se o produto já está no pedido, soma a quantidade), remover item, calcular total, verificar estoque (erro com mensagem clara se faltar).
4. **Fechar pedido**: baixa o estoque do catálogo; se faltar estoque de algum item, não fecha nada (tudo ou nada).
5. **Ordenações** (via `Comparator`, sem alterar as classes):
   - produtos por preço crescente/decrescente;
   - pedidos por valor total;
   - clientes por cidade e depois por nome;
   - ranking de produtos mais vendidos (você deve montar a lista de totais sem `Map`).
6. **Busca binária**: manter o catálogo ordenado por `id` e buscar produto por `id` com `binarySearch`. Ao cadastrar novo produto, inserir na posição correta (exercício 20).
7. **Limpeza com `Iterator`**: remover do catálogo produtos sem estoque e sem vendas, e remover pedidos cancelados, devolvendo o estoque quando necessário.
8. **Conversões**: exportar o catálogo para `Produto[]` (para um sistema legado), e importar um `Produto[]` mesclando ao catálogo sem duplicar.
9. **Relatório** impresso: total vendido, ticket médio, cliente que mais gastou, produtos abaixo de 5 unidades.
10. **Cópias defensivas**: nenhuma das listas internas deve ser exposta para modificação externa.

## Cenários de teste

```text
10 produtos, 5 clientes, 8 pedidos (um com estoque insuficiente)
Cadastro duplicado de cliente e de produto
Ordenar de 4 formas diferentes e imprimir
Remover produtos sem estoque com Iterator
Buscar um id inexistente na busca binária
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei criar e usar `List<T>` com `ArrayList`.
- [ ] Sei por que programamos para a interface.
- [ ] Sei o que são generics e o diamante.
- [ ] Sei a diferença entre `remove(int)` e `remove(Object)`.
- [ ] Sei que `contains`/`indexOf`/`remove(Object)` usam `equals`.
- [ ] Sei por que o for-each lança `ConcurrentModificationException`.
- [ ] Sei ordenar com `Collections.sort` e `List.sort`.
- [ ] Sei implementar `Comparable` e `Comparator`.
- [ ] Sei a diferença entre os dois.
- [ ] Sei usar `binarySearch` e interpretar o retorno negativo.
- [ ] Sei converter lista ↔ array e os riscos do `Arrays.asList`.
- [ ] Sei remover com `Iterator` e `removeIf`.

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
