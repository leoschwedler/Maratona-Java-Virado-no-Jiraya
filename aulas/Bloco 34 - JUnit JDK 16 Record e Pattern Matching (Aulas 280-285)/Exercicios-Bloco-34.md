# Exercícios — Bloco 34: JUnit, JDK 16, Record e Pattern Matching

## Aulas 280 a 285

Este arquivo acompanha o README do Bloco 34.

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

> **Onde criar os arquivos:** código em `src/main/bloco34_aulas280a285_junit_record_pattern_matching/aulaXXX/` e testes em `src/test/java/...` (mesmo pacote da classe testada). Use JUnit 5 e JDK 16 ou superior.

> **Regras do bloco:**
> - Nomes de teste no padrão `metodo_ComportamentoEsperado_QuandoCondicao`.
> - Estrutura Arrange / Act / Assert.
> - Nenhum teste depende de outro nem da ordem de execução.
> - Nenhum teste depende de data atual, rede ou banco.

---

# Aula 280 — JUnit pt 01

## 🟢 Exercício 01 — Primeiro teste

Crie `Person`, `PersonService.isAdult(Person)` e `PersonServiceTest`.

1. Adicione a dependência `junit-jupiter` com `scope = test`.
2. Escreva 3 testes: menor, exatamente 18 e maior.
3. Rode `mvn test` no terminal e anote a saída.

---

## 🟡 Exercício 02 — Calculadora testada

Crie `Calculator` com `sum`, `subtract`, `multiply` e `divide`.

1. `divide` por zero deve lançar `IllegalArgumentException("Divisor can't be zero")`.
2. Escreva ao menos 2 testes por método, incluindo números negativos e zero.
3. Use `@DisplayName` em pelo menos 3 deles.

---

## 🔴 Exercício 03 — Quebrando de propósito

Pegue o `PersonService` do exercício 1.

1. Mude `>= 18` para `> 18` e rode os testes. Qual falhou? A mensagem foi clara?
2. Mude para `>= 21`. Quantos falham?
3. Escreva um relatório curto: *"Que casos teria deixado passar se eu não tivesse o teste da fronteira (18)?"*
4. Pesquise e explique o que é **teste de mutação** (ex.: PIT) e por que o experimento acima é uma versão manual dele.

---

# Aula 281 — JUnit pt 02

## 🟢 Exercício 01 — `@BeforeEach`

Refatore `PersonServiceTest` para criar `service` e as pessoas em um `@BeforeEach`.

1. Prove (com `System.out`) que o método roda antes de **cada** teste.
2. Crie um teste que altera o objeto e outro que depende do valor original. Mostre que um não afeta o outro.

---

## 🟡 Exercício 02 — Exceções e coleções

1. `assertThrows` para `isAdult(null)`, validando também a **mensagem**.
2. Implemente `filterRemovingNotAdult(List<Person>)` e teste: lista vazia, só menores, só adultos e mista.
3. Faça `Person` ter `@EqualsAndHashCode` e compare as listas por valor, não por instância.

---

## 🔴 Exercício 03 — Cobertura de verdade

1. Rode **Run with Coverage** e registre o percentual.
2. Escreva um conjunto **mínimo** de testes que chegue a 100% mas **não** detecte a troca de `>=` por `>`. Mostre-o.
3. Complete a suíte até que a troca seja detectada.
4. Use `@ParameterizedTest` com `@CsvSource` (dependência `junit-jupiter-params`) para testar várias idades de uma vez.
5. Use `assertAll` para validar vários campos em um único teste.

---

# Aula 282 — Atualizando o JDK

## 🟢 Exercício 01 — Conferindo o ambiente

1. Rode `java -version`, `javac -version` e `mvn -version`.
2. Anote qual JDK cada um usa e onde está o `JAVA_HOME`.
3. Confira no IntelliJ o *Project SDK*, o *Language level* e o *Target bytecode version*.

---

## 🟡 Exercício 02 — Subindo a versão

1. Altere `maven.compiler.source/target` (ou `maven.compiler.release`) para a versão do seu JDK.
2. Rode `mvn clean test`.
3. Provoque o erro clássico: deixe o *Target bytecode version* do IntelliJ em uma versão antiga e tente usar `record`. Descreva a mensagem e como corrigiu.

---

## 🔴 Exercício 03 — Conviver com duas JDKs

1. Instale (ou use o gerenciador de sua preferência para) uma segunda JDK.
2. Mostre como trocar de uma para a outra **sem desinstalar**, por `JAVA_HOME` e pelo IntelliJ.
3. Escreva um `checklist-upgrade-jdk.md` com 8 passos para atualizar um projeto real com segurança (incluindo "rodar a suíte de testes antes e depois").
4. Liste 3 recursos de linguagem que ficam disponíveis a cada salto de versão (por exemplo 11 → 17).

---

# Aula 283 — Record Class

## 🟢 Exercício 01 — Seu primeiro `record`

Crie `record Manga(String name, int episodes)`.

1. Imprima o objeto e explique cada parte do `toString`.
2. Compare dois mangás iguais com `equals` e `==`. Qual a diferença?
3. Use `javap -p Manga.class` e anote o que o compilador gerou.

---

## 🟡 Exercício 02 — Validação e testes

1. Adicione um **construtor compacto** que rejeita `name` nulo/vazio e `episodes` negativo.
2. Escreva testes JUnit para: accessors, `equals`, `hashCode`, `toString` e as validações.
3. Teste que `Manga.class.isRecord()` é verdadeiro.

---

## 🔴 Exercício 03 — Limites do `record`

1. Tente (e documente os erros do compilador): campo de instância extra, `extends`, setter, bloco de inicialização de instância.
2. Crie um `record` **genérico** `Pair<A, B>` com um método estático `of`.
3. Implemente uma **interface** em um record.
4. Use um record **local** dentro de um método para agrupar resultados de um `stream` (ex.: nome + média).
5. Converta uma classe com Lombok `@Value` em record e liste as diferenças de uso (`getX()` → `x()`).

---

# Aula 284 — Pattern Matching for `instanceof`

## 🟢 Exercício 01 — Removendo o cast

Dada uma hierarquia `Employee → Developer, Manager`, escreva um método com o `instanceof` + cast antigo e outro com pattern matching. Compare.

---

## 🟡 Exercício 02 — Escopo da variável

1. Crie exemplos que **compilam** e **não compilam** com `&&`, `||` e `!`.
2. Explique, em comentário, por que `obj instanceof String s || s.isEmpty()` não compila.
3. Reescreva um `equals` usando `instanceof` com variável de padrão.

---

## 🔴 Exercício 03 — Despachante de formas

Crie `Shape` (interface) com `Circle`, `Rectangle` e `Triangle` (records).

1. Escreva `double area(Shape s)` usando uma cadeia de `if (s instanceof X x)`.
2. Escreva o mesmo com polimorfismo (método na interface). Compare qual é melhor e quando.
3. Escreva testes JUnit com casos de borda (valores zero, negativos, `null`).
4. Pesquise o que é `sealed` + *pattern matching for switch* (recursos futuros) e explique como simplificariam a questão 1.

---

# Aula 285 — Encerramento

## 🟢 Exercício 01 — Retrospectiva

Escreva um texto com 10 linhas: os 5 conceitos mais difíceis do curso e os 5 que mais mudaram sua forma de programar.

---

## 🟡 Exercício 02 — Mapa do curso

Monte um mapa mental (pode ser em Markdown) ligando os 34 blocos: Fundamentos → OO → Coleções → Streams → Concorrência → I/O → JDBC → Testes.

---

## 🔴 Exercício 03 — Plano de continuidade

Monte um plano de 90 dias com metas semanais: um projeto próprio no GitHub (com testes e README), uma API com Spring Boot e um estudo de Docker/CI. Defina critérios de "pronto" para cada meta.

---

# 🏆 Desafio Integrador do Bloco 34 — **Biblioteca de Animes Testada**

Crie uma pequena biblioteca (sem banco) totalmente coberta por testes.

## Requisitos

### 1. Domínio com `record`

```java
record Producer(int id, String name) { }
record Anime(int id, String name, int episodes, Producer producer) { }
```

- Construtor compacto com validações (nome não vazio, episódios entre 1 e 5000, produtor não nulo).

### 2. Serviço

`AnimeLibraryService` com:

1. `add(Anime)` — rejeita ID duplicado (lança exceção própria);
2. `findByName(String)` — busca parcial, sem diferenciar maiúsculas;
3. `findByProducer(Producer)`;
4. `longerThan(int episodes)` — com Streams;
5. `groupByProducer()` — `Map<Producer, List<Anime>>`;
6. `describe(Object o)` — usa **pattern matching para `instanceof`** (`Anime`, `Producer`, `String`, outro).

### 3. Testes (JUnit 5)

1. Pelo menos **25 testes**, nomeados com o padrão do bloco.
2. `@BeforeEach` com dados novos a cada teste.
3. `assertThrows` validando tipo **e** mensagem para cada regra de validação.
4. Pelo menos um `@ParameterizedTest`.
5. Testes de fronteira (1 e 5000 episódios; 0 e 5001).
6. Teste que confirma `Anime.class.isRecord()`.

### 4. Qualidade

1. Cobertura ≥ 90% **e** testes que detectem uma mutação manual (troque `<` por `<=` e veja falhar).
2. Compilar e testar com `mvn clean test` no JDK 16+.
3. Um `README` curto com como executar os testes.

---

## Checklist do bloco

- [ ] JUnit 5 configurado e rodando
- [ ] Testes independentes (`@BeforeEach`)
- [ ] Exceções testadas com `assertThrows`
- [ ] JDK atualizado e projeto compilando
- [ ] Records com validação
- [ ] Pattern matching para `instanceof`
- [ ] Desafio integrador entregue

## Regra para as correções

Mande o código e os testes de cada aula e eu corrijo apontando: nomes de teste, independência, cobertura de casos de borda, uso correto das assertivas e clareza do código.
