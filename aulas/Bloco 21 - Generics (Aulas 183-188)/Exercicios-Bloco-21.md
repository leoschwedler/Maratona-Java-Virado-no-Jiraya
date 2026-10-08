# Exercícios — Bloco 21: Generics

## Aulas 183 a 188

Este arquivo acompanha o README do Bloco 21.

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

> **Onde criar os arquivos:** `src/main/bloco21_aulas183a188_generics/aulaXXX/`

> **Regra do bloco:** não use lambdas nem streams. Quando o enunciado disser que "o código não compila", deixe a linha **comentada** com uma explicação do erro.

---

# Aula 183 — Introdução

## 🟢 Exercício 01 — Lista crua x lista tipada

1. Crie uma lista **sem generics** e coloque uma `String`, um `Integer` e um `Double`.
2. Percorra com `for (Object o : lista)` e imprima o tipo real (`getClass().getSimpleName()`).
3. Tente somar os números: precisará de `instanceof` e cast. Faça.
4. Crie `List<Integer>` e mostre que o `for-each` já dispensa cast.
5. Tente `add("texto")` na lista tipada (deixe comentado e explique o erro).

### Responda

Em que momento (compilação ou execução) cada tipo de erro aparece?

---

## 🟡 Exercício 02 — Sabotagem

Reproduza a sabotagem da aula:

1. Crie um método `static void sabotar(List lista)` (raw type) que adiciona um `Integer`.
2. Crie `List<String> nomes` com 3 nomes e passe para `sabotar`.
3. Percorra `nomes` com `for (String n : nomes)` e capture o `ClassCastException`.
4. Mostre o aviso do compilador e explique o que significa *unchecked*.
5. Corrija o método com `List<?>` e `List<String>` e explique cada versão.

---

## 🔴 Exercício 03 — Type erasure na prática

Prove que o tipo genérico desaparece em execução:

1. Compare `new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass()` e explique o resultado.
2. Tente criar uma sobrecarga `void tratar(List<String> l)` e `void tratar(List<Integer> l)` na mesma classe e explique o erro de compilação (deixe comentado).
3. Tente `new T[10]` e `T t = new T()` num método genérico (comentado) e explique por quê não funciona.
4. Crie um método `static <T> int contar(T[] array, T procurado)` que funciona com qualquer tipo e teste com `String[]`, `Integer[]` e `Cachorro[]`.

---

# Aula 184 — Wildcard (parte 1)

## 🟢 Exercício 04 — Hierarquia animal

Crie `Animal` (abstrata, método `abstract void emitirSom()`), `Cachorro` e `Gato`.

Faça um método `static void atender(Animal[] animais)` e chame com `Cachorro[]`, `Gato[]` e `Animal[]`. Todos devem funcionar.

---

## 🟡 Exercício 05 — ArrayStoreException

Dentro de `atender(Animal[] animais)`, adicione `animais[0] = new Gato();` e chame com um `Cachorro[]`.

1. Capture a `ArrayStoreException`.
2. Explique por que o código **compila** mas falha em execução.
3. Explique por que o mesmo código com `List<Animal>` não chega nem a compilar quando você passa `List<Cachorro>`.

---

## 🔴 Exercício 06 — Invariância

Demonstre, sem executar o código inválido (deixe comentado e explique cada linha), por que `List<Cachorro>` não pode ser tratado como `List<Animal>`:

1. Escreva o trecho que **seria** um problema (adicionar gato numa lista de cachorros).
2. Mostre que `List<Animal>` aceita adicionar `Cachorro` e `Gato` (polimorfismo comum).
3. Mostre que arrays de `Animal` aceitam `Cachorro` e `Gato` mas falham em execução quando o array real é `Cachorro[]`.
4. Faça um quadro comparando arrays x generics (covariância x invariância, verificação em compilação x execução).

---

# Aula 185 — Wildcard (parte 2)

## 🟢 Exercício 07 — `? extends`

Escreva `static void atenderTodos(List<? extends Animal> animais)` que percorre e chama `emitirSom()`. Teste com `List<Cachorro>`, `List<Gato>` e `List<Animal>`.

---

## 🟡 Exercício 08 — O que o `extends` proíbe

No método anterior, tente:

1. `animais.add(new Cachorro());`
2. `animais.add(null);`
3. `Animal a = animais.get(0);`
4. `Cachorro c = animais.get(0);`

Deixe cada linha como comentário e anote qual **compila** e qual **não**, explicando por quê. Depois escreva `static void adotar(List<? super Cachorro> destino)` que adiciona dois cachorros e teste com `List<Cachorro>`, `List<Animal>` e `List<Object>`. Tente `List<Gato>` (comentado).

---

## 🔴 Exercício 09 — Copiar coleções (PECS)

Implemente seus próprios métodos utilitários:

```java
static <T> void copiar(List<? super T> destino, List<? extends T> origem)
static double somar(List<? extends Number> numeros)
static void preencherComInteiros(List<? super Integer> lista, int quantidade)
static void imprimirTudo(List<?> lista)
```

Teste:

- `somar` com `List<Integer>`, `List<Double>`, `List<Number>`.
- `preencherComInteiros` com `List<Integer>`, `List<Number>`, `List<Object>`.
- `copiar` de `List<Cachorro>` para `List<Animal>`.

Escreva em comentário, para cada método, se a lista é **produtora** ou **consumidora** e por que aquele wildcard é o correto (PECS).

---

# Aula 186 — Classes genéricas (parte 1)

## 🟢 Exercício 10 — O problema da repetição

Crie `Carro` e `Barco` (com `nome`) e duas classes quase idênticas: `CarroRentalService` e `BarcoRentalService` (como na aula).

1. Faça cada uma ter `buscarDisponivel()` e `devolver(item)`.
2. Teste as duas com 2 itens cada, imprimindo a lista de disponíveis após cada operação.
3. Em comentário, liste **todas** as linhas que diferem entre as duas classes. Só diferem os tipos?

---

## 🟡 Exercício 11 — Mais um tipo, mais uma cópia

Adicione `Moto` e `MotoRentalService` (copiando de novo). Meça (em comentário) quantas linhas de código foram duplicadas no total. Depois adicione uma regra de negócio nova: "não é possível alugar se não houver itens disponíveis" (lance `IllegalStateException`) — e perceba que precisa alterar **três** classes.

Escreva uma reflexão (3 a 5 linhas) sobre manutenção de código duplicado.

---

## 🔴 Exercício 12 — Mapa da duplicação

Faça um quadro (em comentário ou em arquivo `.md` junto com o código) listando, para cada elemento do código das três classes, **o que muda** e **o que permanece**:

```text
nome da classe | tipo da lista | nome do atributo | tipo de retorno | tipo do parâmetro | mensagens
```

Com base nele, desenhe (em texto) como seria uma única classe `RentalService<T>` e quais pontos exigem cuidado (criar `T`, mensagens que citam o nome do tipo, etc.). **Não implemente ainda.**

---

# Aula 187 — Classes genéricas (parte 2)

## 🟢 Exercício 13 — `RentalService<T>`

Implemente `RentalService<T>` conforme a aula e use-a com `Carro`, `Barco` e `Moto`. Remova as três classes duplicadas.

---

## 🟡 Exercício 14 — Caixa genérica

Crie `Caixa<T>` com `guardar(T)`, `pegar()`, `estaVazia()`. Teste com `String`, `Integer`, `Carro`, `List<String>`.

Depois:

1. Tente guardar `Integer` numa `Caixa<String>` (comentado) e explique.
2. Crie `Par<A, B>` e use `Par<String, Integer>` e `Par<Carro, Barco>`.
3. Adicione a `Par` os métodos `trocar()` (retorna `Par<B, A>`) e `toString`.

---

## 🔴 Exercício 15 — Repositório genérico em memória

Crie a interface/classe:

```java
class Repositorio<T, ID> {
    void salvar(ID id, T objeto)
    T buscar(ID id)
    boolean existe(ID id)
    void remover(ID id)
    List<T> listarTodos()
    int tamanho()
}
```

Use um `Map<ID, T>` internamente.

1. Teste com `Repositorio<Cliente, Long>`, `Repositorio<Produto, String>` e `Repositorio<Carro, Integer>`.
2. Crie uma versão `RepositorioOrdenado<T extends Comparable<T>, ID>` que devolve `listarTodos()` ordenada.
3. Tente instanciar `RepositorioOrdenado<Barco, Long>` com `Barco` **não** comparável (comentado) e explique o erro.
4. Crie `static <T> void imprimir(Repositorio<T, ?> repo)` e explique o `?`.

---

# Aula 188 — Métodos genéricos

## 🟢 Exercício 16 — Meu primeiro método genérico

Crie `static <T> List<T> criarListaComUm(T objeto)` e use com `String`, `Integer`, `Carro`. Mostre que o compilador infere o tipo, e depois chame com tipo explícito (`Utils.<String>criarListaComUm("x")`).

---

## 🟡 Exercício 17 — Utilitários genéricos

Implemente numa classe `Utils`:

```java
static <T> void trocar(T[] array, int i, int j)
static <T> int indiceDe(T[] array, T procurado)
static <T extends Comparable<T>> T maior(T[] array)
static <T extends Comparable<T>> T menor(List<T> lista)
static <T> List<T> inverter(List<T> lista)
static <T> boolean todosIguais(List<T> lista)
```

Teste cada um com pelo menos dois tipos diferentes e mostre que `maior` não aceita um tipo que não seja `Comparable` (comentado).

---

## 🔴 Exercício 18 — Mini-biblioteca de coleções

Crie uma classe `Colecoes` com os métodos genéricos:

```java
static <T> List<T> filtrar(List<T> lista, Predicado<T> condicao)
static <T, R> List<R> transformar(List<T> lista, Transformador<T, R> t)
static <T> T primeiroOuPadrao(List<T> lista, T padrao)
static <K, V> Map<V, K> inverterMapa(Map<K, V> mapa)
static <T extends Comparable<? super T>> void ordenar(List<T> lista)
```

Onde `Predicado<T>` e `Transformador<T, R>` são **interfaces genéricas** que você cria (sem lambdas ainda: implemente com classes comuns ou classes anônimas).

Teste:

- filtrar nomes com mais de 4 letras;
- transformar `List<String>` em `List<Integer>` (tamanhos);
- inverter `Map<String, Integer>` e discutir o problema de valores repetidos;
- ordenar `List<Cachorro>` quando `Cachorro implements Comparable<Cachorro>`.

Explique por que a assinatura da `ordenar` usa `Comparable<? super T>`.

---

# 🏆 Desafio Integrador do Bloco 21 — Framework Genérico de Cadastro e Aluguel

Você vai construir um mini-framework reutilizável para cadastros e aluguéis, usando intensamente generics.

## Parte 1 — Domínio

```text
Entidade (interface genérica):  interface Entidade<ID> { ID getId(); }
Carro, Barco, Moto, Bicicleta → implementam Entidade<Long>
Cliente → implementa Entidade<String> (CPF)
Aluguel<T extends Entidade<?>> → cliente, item (T), inicio, fim
```

## Parte 2 — Infraestrutura genérica

1. `Repositorio<T extends Entidade<ID>, ID>` com `salvar`, `buscarPorId`, `listar`, `remover`, `existe`, `contar`.
2. `RentalService<T extends Entidade<Long>>`:
   - estoque de itens disponíveis (lista) e alugados (mapa cliente → lista de itens);
   - `alugar(Cliente)`, `devolver(Cliente, T)`;
   - regra: cliente pode ter no máximo 2 itens; erro com mensagem específica quando violada;
   - histórico de `Aluguel<T>`.
3. `Resultado<T>`: classe genérica que representa "sucesso com valor" ou "erro com mensagem" (evita lançar exceção para regras de negócio). Métodos: `ok(T)`, `erro(String)`, `isOk()`, `getValor()`, `getErro()`, `map(...)` (com interface sua).
4. `Cache<K, V>` simples com limite de tamanho (remove o mais antigo ao estourar, use `LinkedHashMap`).

## Parte 3 — Utilitários com wildcard

5. `Relatorios.imprimirDisponiveis(List<? extends Entidade<?>> itens)`.
6. `Relatorios.copiarTodos(List<? super Carro> destino, List<? extends Carro> origem)`.
7. `Relatorios.somarValores(List<? extends Number> valores)` para totalizar valores de aluguel.
8. `Ordenacao.maiorDe(List<T> lista, Comparator<? super T> comparator)`.

## Parte 4 — Demonstração

No `main`, rode um cenário completo:

- 3 repositórios (carros, barcos, clientes);
- 3 serviços de aluguel (`RentalService<Carro>`, `RentalService<Barco>`, `RentalService<Moto>`);
- 4 clientes alugando e devolvendo, violando regras de propósito;
- relatórios com wildcards;
- uso do `Resultado<T>` para mostrar sucesso/erro sem exceções;
- uma tabela final comparando quantas classes você precisaria **sem** generics e quantas com generics.

## Regras

- Nada de *raw types* (nenhum aviso `unchecked` no seu código).
- Nenhum cast.
- Documente (comentário) por que cada wildcard foi escolhido (PECS).

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei o problema que os generics resolvem.
- [ ] Sei o que é type erasure e suas consequências.
- [ ] Sei por que `List<Cachorro>` não é `List<Animal>`.
- [ ] Sei por que arrays e generics se comportam diferente.
- [ ] Sei usar `? extends T` e sei que é somente leitura.
- [ ] Sei usar `? super T` e o que posso fazer com ele.
- [ ] Conheço a regra PECS.
- [ ] Sei criar uma classe genérica `Classe<T>`.
- [ ] Sei criar classe com vários parâmetros e com limites.
- [ ] Sei criar métodos genéricos e a posição do `<T>`.
- [ ] Sei por que não posso fazer `new T()`.

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
