# Exercícios — Bloco 20: Set, Map e Queue

## Aulas 175 a 182

Este arquivo acompanha o README do Bloco 20.

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

> **Onde criar os arquivos:** `src/main/bloco20_aulas175a182_set_map_queue/aulaXXX/`

> **Regras do bloco:**
> - Declare sempre pela **interface** (`Set<T>`, `Map<K,V>`, `Queue<T>`, `NavigableSet<T>`, `NavigableMap<K,V>`).
> - Use generics e o diamante.
> - Ainda sem lambdas e streams (exceto `putIfAbsent`, `getOrDefault` e `computeIfAbsent` quando o enunciado citar).

---

# Aula 175 — Set, HashSet

## 🟢 Exercício 01 — Removendo duplicatas

Dada a lista `[3, 7, 3, 9, 7, 1, 9, 9, 4]`:

1. Crie um `Set<Integer>` a partir dela.
2. Imprima a lista original (tamanho) e o set (tamanho).
3. Mostre o retorno do `add` quando o elemento é novo e quando já existe.
4. Repita com `LinkedHashSet` e compare a ordem de impressão.

### Responda

- Por que `Set` não tem `get(int)`?
- Qual a diferença visível entre `HashSet` e `LinkedHashSet`?

---

## 🟡 Exercício 02 — Quem define o duplicado?

Crie `Aluno` (`matricula`, `nome`, `nota`).

1. Sem `equals`/`hashCode`: insira dois alunos "idênticos" num `HashSet`. Quantos ficaram?
2. Gere `equals`/`hashCode` com **todos os campos** e repita; depois com **só a matrícula**.
3. Altere a nota de um aluno depois de inserido e tente `contains` — o que acontece quando um campo usado no `hashCode` muda? (Anote em comentário.)

Explique qual das três regras faz sentido para uma escola e por quê.

---

## 🔴 Exercício 03 — Operações de conjuntos

Implemente, com `Set<Integer>`, as operações matemáticas (cada uma devolvendo **um novo set**, sem alterar os de entrada):

```text
uniao(a, b)               → addAll
intersecao(a, b)          → retainAll
diferenca(a, b)           → removeAll
diferencaSimetrica(a, b)  → está em A ou em B, mas não em ambos
ehSubconjunto(a, b)       → containsAll
```

Teste com `{1,2,3,4,5}` e `{4,5,6,7}`. Depois use as operações para resolver:

"Dada a lista de e-mails de duas listas de convidados, quem foi convidado nas duas, quem só na primeira e quantos e-mails únicos existem no total?"

---

# Aula 176 — NavigableSet, TreeSet (pt 01)

## 🟢 Exercício 04 — Ordem automática

Insira em um `TreeSet<String>` os nomes de 8 frutas fora de ordem (misture maiúsculas e minúsculas). Imprima e explique a ordem. Depois use `String.CASE_INSENSITIVE_ORDER` como comparator e compare.

---

## 🟡 Exercício 05 — Quando falta critério

Crie `Cachorro` (`nome`, `idade`) **sem** `Comparable`.

1. Tente inserir num `TreeSet<Cachorro>` e capture o `ClassCastException`.
2. Resolva de duas maneiras:
   - fazendo `Cachorro implements Comparable<Cachorro>` por nome;
   - passando um `Comparator` por idade no construtor.
3. Mostre os dois conjuntos ordenados.

---

## 🔴 Exercício 06 — Ranking de jogadores

Crie `Jogador` (`nome`, `pontos`) e mantenha um `TreeSet<Jogador>` com o ranking (maior pontuação primeiro).

Regras:

1. O `Comparator` deve ordenar por pontos decrescente e, em caso de empate, por nome.
2. Insira 10 jogadores com alguns empates.
3. Mostre o ranking com a posição (1º, 2º, ...).
4. Mostre o que acontece se o comparator **não** tiver desempate (jogadores com os mesmos pontos desaparecem!). Explique.
5. Para "atualizar" os pontos de um jogador existente: remova e reinsira (por quê não basta alterar o campo?).

---

# Aula 177 — NavigableSet, TreeSet (pt 02)

## 🟢 Exercício 07 — Navegando em números

Com um `NavigableSet<Integer>` contendo `10, 20, 30, 40, 50`, imprima o resultado de `lower`, `floor`, `higher`, `ceiling` para as referências `5`, `10`, `25`, `50` e `60`. Preencha uma tabela e explique os `null`.

---

## 🟡 Exercício 08 — Os dois extremos

Usando `pollFirst` e `pollLast`, esvazie um `TreeSet` retirando alternadamente do menor e do maior até acabar, imprimindo a ordem de retirada. Depois repita com `descendingSet`.

Mostre também `headSet`, `tailSet` e `subSet` (com os booleanos de inclusão) e demonstre que são **visões ligadas** (remova algo da visão e veja o conjunto original).

---

## 🔴 Exercício 09 — Faixas de preço e promoção

Crie `Produto` (`id`, `nome`, `preco`) e um `NavigableSet<Produto>` ordenado por preço (com desempate por id).

Implemente:

```text
produtoMaisProximo(double preco)       → o de preço mais próximo (use floor e ceiling e compare as distâncias)
produtosNaFaixa(double min, double max)→ subSet com limites inclusivos
proximoUpgrade(Produto atual)          → higher
alternativaMaisBarata(Produto atual)   → lower
```

Teste com 12 produtos e valores de referência fora do intervalo (que devem tratar `null`).

### Pergunta

Por que, ao criar produtos "modelo" para as buscas (`new Produto(0, "", 50.0)`), o desempate por `id` pode atrapalhar? Como resolver?

---

# Aula 178 — Map pt 01

## 🟢 Exercício 10 — Dicionário

Crie um `Map<String, String>` com 6 palavras em inglês e suas traduções. Imprima:

1. O mapa inteiro.
2. Cada par com `keySet`.
3. Cada par com `entrySet`.
4. Apenas as traduções com `values`.
5. A tradução de uma palavra que existe e de uma que não existe (com `getOrDefault`).

---

## 🟡 Exercício 11 — Contador de palavras

Dado o texto:

```text
"o rato roeu a roupa do rei de roma e o rei roeu o rato"
```

Conte quantas vezes cada palavra aparece usando `Map<String, Integer>`. Faça de duas maneiras:

1. Com `containsKey` e `put`.
2. Com `getOrDefault`.

Imprima o resultado ordenado alfabeticamente (copie para um `TreeMap`) e mostre a palavra mais frequente.

---

## 🔴 Exercício 12 — Estoque de loja

Implemente `Estoque` com `Map<String, Integer>` (código → quantidade):

```text
adicionar(codigo, qtd)        → soma ao existente ou cria
retirar(codigo, qtd)          → erro se faltar; remove a chave se zerar
consultar(codigo)
itensAbaixoDe(int minimo)     → novo Map só com os itens críticos
merge(Estoque outro)          → soma as quantidades das chaves em comum
```

Regras:

- `putIfAbsent` onde fizer sentido.
- Demonstre o efeito de `put` em chave repetida (retorna o valor antigo).
- Imprima o estoque ao final de cada operação com `entrySet`.
- Teste 10 operações, incluindo as inválidas.

---

# Aula 179 — Map pt 02

## 🟢 Exercício 13 — Chaves objeto

Crie `Cliente` (`cpf`, `nome`) com `equals`/`hashCode` por CPF. Crie `Map<Cliente, String>` (cliente → plano contratado).

1. Insira 4 clientes.
2. Faça `put` com um **novo objeto** de mesmo CPF e mostre que o valor foi substituído.
3. Remova o `hashCode` e repita: o que muda? Explique.

---

## 🟡 Exercício 14 — Quem comprou o quê

Reproduza o cenário da aula com `Consumidor` e `Manga`:

1. `Consumidor` com id gerado por `ThreadLocalRandom` (0 a 100 000) e `equals`/`hashCode` por id.
2. Um `Map<Consumidor, Manga>` com 5 compras.
3. Impressão no formato `Fulano comprou Naruto (R$ 20,00)`.
4. Descubra quem comprou o mangá mais caro (percorrendo o `entrySet`).

### Pergunta

O que acontece se o mesmo consumidor fizer uma segunda compra com `put`? Que estrutura resolveria? (gancho para a próxima aula)

---

## 🔴 Exercício 15 — Mapa de índices

Escreva um programa que receba uma lista de 10 `Livro`s (`titulo`, `autor`, `ano`) e construa **três mapas**:

```text
porTitulo  : Map<String, Livro>
porAutor   : Map<String, Livro>   → atenção: um autor pode ter vários! (guarde o primeiro e conte os repetidos)
porAno     : Map<Integer, Livro>
```

Depois responda programaticamente:

1. Quais autores têm mais de um livro (use um `Map<String, Integer>` auxiliar).
2. Há anos repetidos? Mostre quais livros "perderam" a posição.
3. Mostre por que `Map<String, Livro>` perde informação nesse caso e antecipe a solução da próxima aula.

---

# Aula 180 — Map pt 03

## 🟢 Exercício 16 — Turma e alunos

Monte um `Map<String, List<String>>`: nome da turma → lista de alunos. Crie 3 turmas com 3 a 5 alunos cada. Imprima:

```text
Turma A:
  - Ana
  - Bruno
```

---

## 🟡 Exercício 17 — Agrupar por inicial

Dada uma lista de 15 nomes, construa um `Map<Character, List<String>>` agrupando por inicial. Faça:

1. Com `containsKey` + criação manual da lista.
2. Com `computeIfAbsent`.

Imprima ordenado pela chave e o tamanho de cada grupo.

---

## 🔴 Exercício 18 — Loja de mangás completa

Evolua o cenário da aula:

```text
Map<Consumidor, List<Manga>> compras
```

Implemente:

1. `registrarCompra(Consumidor, Manga)` — cria a lista na primeira compra.
2. `totalGasto(Consumidor)`.
3. `consumidorQueMaisGastou()`.
4. `mangasMaisVendidos()` — devolve um `Map<Manga, Integer>` (mangá → quantidade de vendas); depois imprima do mais para o menos vendido (sem streams: copie as entradas para uma lista e ordene com `Comparator`).
5. `quemComprou(Manga)` — lista de consumidores.
6. `removerCompras(Consumidor)`.

Teste com 5 consumidores e 8 mangás, com compras repetidas.

---

# Aula 181 — NavigableMap, TreeMap

## 🟢 Exercício 19 — Mapa ordenado

Insira em um `TreeMap<String, Integer>` (nome → idade) 8 pessoas fora de ordem. Imprima, mostrando que sai em ordem alfabética pelo nome. Use `firstEntry`, `lastEntry`, `firstKey` e `lastKey`.

---

## 🟡 Exercício 20 — Tabela de faixas

Crie um `TreeMap<Integer, String>` representando faixas de nota → conceito:

```text
0 → "F"
50 → "D"
60 → "C"
75 → "B"
90 → "A"
```

Implemente `conceito(int nota)` usando **uma só chamada** a `floorEntry`. Teste com 0, 49, 50, 74, 75, 89, 90, 100 e com -5 (trate o `null`).

---

## 🔴 Exercício 21 — Agenda por horário

Use `TreeMap<LocalTime, String>` (horário → compromisso):

1. `proximoCompromisso(LocalTime agora)` → `ceilingEntry`.
2. `compromissoAnterior(LocalTime agora)` → `lowerEntry`.
3. `compromissosDaManha()` → `headMap(12:00)`.
4. `compromissosEntre(a, b)` → `subMap`.
5. `compromissoAtual(LocalTime agora)` → o último que começou (`floorEntry`).
6. Mostre que as visões são ligadas: remova um item do `headMap` e confira no mapa original.
7. Mostre a diferença entre `...Entry` e `...Key`.

---

# Aula 182 — Queue, PriorityQueue

## 🟢 Exercício 22 — Fila do banco

Simule uma fila com `Queue<String>` (`LinkedList`): 5 clientes entram com `offer`. Depois:

1. Use `peek` para ver quem é o próximo (sem remover).
2. Atenda todos com `poll` até esvaziar.
3. Chame `poll` e `peek` numa fila vazia e mostre o `null`; depois `remove()` e `element()` e capture a exceção.

---

## 🟡 Exercício 23 — Fila com prioridade

Crie `Paciente` (`nome`, `gravidade` de 1 a 5, onde 5 é o mais grave) e uma `PriorityQueue<Paciente>` com comparator por gravidade decrescente.

1. Insira 8 pacientes.
2. Imprima a fila com `System.out.println(fila)` e com for-each. Esses resultados estão em ordem de atendimento? Explique.
3. Atenda todos com `poll` e confirme a ordem.

---

## 🔴 Exercício 24 — Central de atendimento

Simule um pronto-atendimento:

```text
Paciente: nome, gravidade (1-5), horaChegada (LocalTime)
```

Regras de prioridade:

1. Maior gravidade primeiro.
2. Em empate, quem chegou primeiro.
3. Idosos (idade ≥ 60) ganham +1 de gravidade efetiva (mas nunca acima de 5).

Implemente:

- `chegar(Paciente)`;
- `atenderProximo()` (mostra quem foi atendido e quantos esperam);
- `espiarProximo()`;
- `cancelar(Paciente)` (use `remove(Object)` e explique o custo e o papel do `equals`);
- um relatório final com o tempo médio de espera simulado.

Simule 15 chegadas intercaladas com atendimentos.

---

# 🏆 Desafio Integrador do Bloco 20 — Sistema de Streaming (Catálogo, Usuários e Fila de Processamento)

Você vai construir o núcleo de uma plataforma de filmes e séries.

## Classes

```text
Titulo   → id, nome, genero, ano, nota, duracaoMin
Usuario  → id, nome, plano
Plataforma:
   Map<Long, Titulo>                 catalogo
   Map<String, Set<Titulo>>          porGenero
   Map<Usuario, List<Titulo>>        historico
   Map<Usuario, Set<Titulo>>         favoritos
   Queue<Titulo>                     fila de processamento de novos títulos (FIFO)
   PriorityQueue<Titulo>             fila de moderação por nota/denúncias
```

## Requisitos

1. **Igualdade**: `Titulo` e `Usuario` com `equals`/`hashCode` por `id`.
2. **Catálogo**: sem duplicados. Rejeite um título repetido com mensagem clara (retorno de `put`/`add`).
3. **Índice por gênero**: `Map<String, Set<Titulo>>` mantendo cada gênero com os títulos **ordenados por nota decrescente** (`TreeSet` com comparator e desempate por id).
4. **Histórico** e **favoritos**: registrar visualização (lista, permite repetição) e favoritar (set, sem repetição). Calcular `maisAssistidos()` (contagem por título usando `Map<Titulo,Integer>`).
5. **Recomendação**: para um usuário, sugerir títulos dos gêneros que mais assistiu, que ele ainda **não** viu (use operações de conjunto: `removeAll`).
6. **Navegação**: usar `NavigableSet`/`NavigableMap` para:
   - títulos com nota entre X e Y;
   - o título imediatamente melhor (`higher`) e pior (`lower`) que um dado;
   - o ano mais próximo (`floorKey`/`ceilingKey`) de um ano pedido, em um `TreeMap<Integer, List<Titulo>>` por ano.
7. **Fila de processamento**: novos títulos entram numa `Queue` e são "processados" um a um (`poll`), passando para o catálogo; títulos inválidos (sem nome, nota fora de 0-10) são rejeitados com motivo.
8. **Moderação**: `PriorityQueue` onde títulos com mais denúncias são tratados primeiro; empate pela menor nota.
9. **Ranking final**: top 5 por nota em cada gênero; usuários mais ativos (por tamanho do histórico).
10. **Relatório** formatado no console (e opcionalmente em arquivo com `BufferedWriter`).

## Cenários de teste

```text
20 títulos em 4 gêneros (incluindo 2 duplicados)
5 usuários com históricos e favoritos diferentes
Recomendação para 2 usuários
Fila de 8 títulos novos (2 inválidos)
Moderação com 6 títulos denunciados
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei a diferença entre `List`, `Set`, `Map` e `Queue`.
- [ ] Sei que `Map` não é `Collection`.
- [ ] Sei como `HashSet` detecta duplicados (`hashCode` + `equals`).
- [ ] Sei a diferença entre `HashSet`, `LinkedHashSet` e `TreeSet`.
- [ ] Sei que `TreeSet`/`TreeMap` usam `compareTo`/`compare` e não `equals`.
- [ ] Sei usar `lower`, `floor`, `higher`, `ceiling`.
- [ ] Sei usar `pollFirst`, `pollLast`, `headSet`, `tailSet`, `subSet`.
- [ ] Sei usar `put`, `putIfAbsent`, `get`, `getOrDefault`, `containsKey`, `remove`.
- [ ] Sei percorrer `keySet`, `values` e `entrySet`.
- [ ] Sei montar `Map<K, List<V>>`.
- [ ] Sei usar `headMap`, `tailMap`, `subMap` e que são visões.
- [ ] Sei `offer`, `poll`, `peek` e a diferença para `add`, `remove`, `element`.
- [ ] Sei que a impressão de uma `PriorityQueue` não mostra a ordem de saída.

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
