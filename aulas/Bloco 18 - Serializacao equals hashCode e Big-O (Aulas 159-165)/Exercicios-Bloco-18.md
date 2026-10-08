# Exercícios — Bloco 18: Serialização, equals, hashCode e Big-O

## Aulas 159 a 165

Este arquivo acompanha o README do Bloco 18.

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

> **Onde criar os arquivos:** `src/main/bloco18_aulas159a165_serializacao_equals_hashcode/aulaXXX/`
> Cada aula pode ter suas classes de domínio no mesmo pacote.

> **Regra do bloco:** ainda não vimos `List`, `Set` nem `Map`. Quando precisar guardar vários objetos, use **arrays**. Os exercícios de `hashCode` usam arrays de "caixas" montados por você.

---

# Aula 159 — Serialização pt 01

## 🟢 Exercício 01 — Salvando um Produto

Crie a classe `Produto` (`id`, `nome`, `preco`) que implementa `Serializable`.

No `main`:

1. Crie um produto e serialize em `workspace/produto.ser`.
2. Desserialize em outra variável.
3. Imprima os dois e verifique `produto == lido` e os valores.

### Responda

- O que acontece se você remover `implements Serializable`? Qual exceção aparece?
- Por que o `produto == lido` é `false`?

---

## 🟡 Exercício 02 — O construtor que não executa

Na classe `Produto`, adicione um `System.out.println("Construtor chamado")` no construtor.

1. Crie o objeto e serialize.
2. Desserialize.
3. Conte quantas vezes a mensagem foi impressa e explique por quê.

Depois crie uma superclasse `Item` **sem** `Serializable` e com um construtor sem argumentos que imprime uma mensagem; faça `Produto extends Item`. Repita o experimento e explique o que mudou (a superclasse não serializável tem seu construtor sem argumentos executado!).

---

## 🔴 Exercício 03 — Cofre de objetos

Crie uma classe `Cofre` com os métodos:

```text
static void guardar(Object o, Path arquivo)
static Object abrir(Path arquivo)
static boolean existe(Path arquivo)
```

Regras:

1. Use `ObjectOutputStream`/`ObjectInputStream` com try-with-resources.
2. `guardar` deve lançar `IllegalArgumentException` com mensagem clara se o objeto não for `Serializable` (verifique com `instanceof` antes de tentar).
3. `abrir` deve tratar `FileNotFoundException`, `ClassNotFoundException` e `IOException` separadamente, com mensagens diferentes.
4. Teste guardando um `Produto`, uma `String`, um `Integer` e um objeto de uma classe **não** serializável.
5. Grave **três objetos em sequência** no mesmo arquivo (`writeObject` três vezes) e leia os três na mesma ordem.

---

# Aula 160 — Serialização pt 02

## 🟢 Exercício 04 — Campo `transient`

Crie `Usuario` (`login`, `senha`) serializável com `senha` como `transient`.

Serialize e desserialize. Imprima o resultado e responda: qual o valor de `senha` depois? Faça o mesmo experimento com um `int` transient e um `boolean` transient e anote os valores padrão.

---

## 🟡 Exercício 05 — Versões incompatíveis

Passo a passo:

1. Crie `Cliente` (`nome`) **sem** declarar `serialVersionUID`. Serialize.
2. Adicione um novo atributo `email` à classe e tente desserializar o arquivo antigo. Anote a exceção.
3. Agora declare `private static final long serialVersionUID = 1L;`, serialize, adicione outro atributo e desserialize de novo. O que mudou? Qual o valor do atributo novo?
4. Altere o `serialVersionUID` para `2L` e tente ler o arquivo antigo. Anote.

Explique em comentário quando faz sentido manter e quando faz sentido alterar o `serialVersionUID`.

---

## 🔴 Exercício 06 — Serialização personalizada

Crie `Turma` (**não** serializável): `nome`, `periodo`.

Crie `Aluno implements Serializable`: `nome`, `idade`, `Turma turma` (marcada como `transient`) e `static String escola`.

1. Implemente `writeObject` e `readObject` privados para gravar e recuperar `turma.nome` e `turma.periodo`.
2. Teste: grave o aluno, **altere** o valor de `escola` e leia. O que foi lido para `escola`? Por quê?
3. Teste invertendo a ordem de leitura em `readObject` (propositalmente errado) e veja o que acontece com os valores.
4. Adicione uma validação em `readObject`: se `idade` for negativa, lance `InvalidObjectException`.

### Pergunta

Por que os métodos `writeObject`/`readObject` precisam ser `private` e ter exatamente esses nomes?

---

# Aula 161 — equals pt 01

## 🟢 Exercício 07 — `==` x `equals`

Escreva um programa que compare, com `==` e com `equals`, os seguintes pares e **anote a previsão antes de rodar**:

```text
"java" e "java"
"java" e new String("java")
new String("a") e new String("a")
Integer 127 e Integer 127   (Integer.valueOf)
Integer 1000 e Integer 1000
new Smartphone("1","X") e new Smartphone("1","X")
```

Explique cada resultado (String Pool, cache de `Integer`, `Object.equals`).

---

## 🟡 Exercício 08 — O Livro idêntico

Crie `Livro` (`isbn`, `titulo`, `autor`) **sem** sobrescrever `equals`.

Crie um array com 5 livros, sendo dois logicamente iguais (mesmo ISBN) e escreva um método `static boolean contem(Livro[] livros, Livro procurado)` que usa `equals`.

Mostre que o método retorna `false` mesmo com o livro "presente" e explique por quê. Deixe tudo pronto para consertar no próximo exercício.

---

## 🔴 Exercício 09 — Duas referências, um objeto

Analise e responda (em comentários) o que será impresso, desenhando um diagrama de memória simples:

```java
Livro a = new Livro("111", "Java", "Ana");
Livro b = new Livro("111", "Java", "Ana");
Livro c = a;
Livro d = b;
d = c;
a = null;

a == b ?   a == c ?   b == c ?   c == d ?   a.equals(b) ?
```

Execute e confira. Depois, escreva 3 situações reais (sem ser smartphone ou livro) em que duas instâncias diferentes deveriam ser consideradas "o mesmo" objeto pelo negócio e diga **qual atributo** definiria a igualdade em cada caso.

---

# Aula 162 — equals pt 02

## 🟢 Exercício 10 — Sobrescrevendo `equals`

Sobrescreva o `equals` de `Livro` considerando apenas o `isbn`, com todos os passos da aula (null, mesma referência, classe, cast, comparação). Repita o teste do exercício 08.

---

## 🟡 Exercício 11 — Testando o contrato

Crie um programa de testes manual que verifica, para três objetos `Livro` (x, y, z, sendo x e y e z com o mesmo ISBN), as propriedades:

```text
reflexiva:   x.equals(x)
simétrica:   x.equals(y) == y.equals(x)
transitiva:  x.equals(y) && y.equals(z) => x.equals(z)
consistente: x.equals(y) repetido 3 vezes
nulo:        x.equals(null) == false
```

Imprima um quadro `[OK]`/`[FALHOU]` para cada. Depois **quebre** a implementação de propósito (por exemplo, comparando por `titulo` para x e por `isbn` para y) e veja qual propriedade falha.

---

## 🔴 Exercício 12 — Herança e simetria

Crie `Pessoa` (`cpf`, `nome`) com `equals` baseado no `cpf` usando `instanceof`. Crie `Aluno extends Pessoa` (adiciona `matricula`) com `equals` que compara `cpf` **e** `matricula`.

1. Mostre que `pessoa.equals(aluno)` e `aluno.equals(pessoa)` dão resultados diferentes (violando a simetria).
2. Refaça usando `getClass()` e mostre que a simetria volta (mas `Pessoa` e `Aluno` nunca serão iguais).
3. Escreva um comentário explicando o dilema e qual abordagem você escolheria para o seu domínio.

---

# Aula 163 — hashCode pt 01

## 🟢 Exercício 13 — Hash das palavras

Implemente `static int hashSimples(String palavra)` somando o valor de cada letra (a=1, b=2...), ignorando maiúsculas e minúsculas, como no exemplo da aula.

Calcule o hash de `alex`, `bob`, `dirk`, `java`, `avaj` e `ajav`. Quais colidem? O que isso revela sobre esse hash?

---

## 🟡 Exercício 14 — Minha tabela hash

Implemente uma **tabela hash simples** com um array de `String[]` de 20 posições (as "caixas"; cada caixa guarda apenas **um** nome por enquanto):

1. `inserir(nome)`: calcula `hashSimples(nome) % 20` e coloca na posição; se a posição estiver ocupada, imprime `"Colisão com <nome>"`.
2. `buscar(nome)`: vai direto à caixa e compara com `equals`.
3. Insira 15 nomes e conte quantas colisões ocorreram.

---

## 🔴 Exercício 15 — Resolvendo colisões

Evolua a tabela do exercício anterior: cada caixa passa a ser um **array pequeno (balde)** que guarda vários nomes (tamanho 5, com contador).

1. `inserir` coloca no balde; se o balde encher, imprime aviso.
2. `buscar` calcula a caixa e percorre **apenas** o balde, contando quantas comparações `equals` foram feitas.
3. Compare, para 50 nomes, o número médio de comparações com uma busca linear em um array de 50 posições.
4. Melhore a função de hash para espalhar melhor (por exemplo, multiplicando por 31 a cada letra: `h = h * 31 + c`) e mostre a redução de colisões.

### Pergunta

Qual o papel do `hashCode` e qual o papel do `equals` nesse processo?

---

# Aula 164 — hashCode pt 02

## 🟢 Exercício 16 — `hashCode` do Livro

Implemente `hashCode` em `Livro` usando **apenas** o `isbn` (consistente com o `equals`). Imprima os hashes de dois livros iguais e de dois diferentes.

---

## 🟡 Exercício 17 — Contrato quebrado

Crie `ContaBancaria` (`numero`, `titular`).

1. Sobrescreva **apenas** o `equals` (por `numero`) e **não** o `hashCode`.
2. Imprima `c1.equals(c2)` e os `hashCode()` das duas contas.
3. Explique em comentário qual regra do contrato foi violada e qual problema isso causaria numa coleção hash (você verá na prática na aula de `HashSet`).
4. Corrija implementando `hashCode` com `Objects.hash(numero)`.
5. Agora altere `equals` para considerar `numero` **e** `titular`, e responda: o `hashCode` precisa mudar? Por quê?

---

## 🔴 Exercício 18 — Gerador e auditor

Crie uma classe `Endereco` (`rua`, `numero`, `cidade`, `cep`) e implemente `equals`/`hashCode` considerando `cep` e `numero`.

Escreva um **auditor** `static void auditarContrato(Object a, Object b, Object c)` que verifica automaticamente:

1. Reflexividade, simetria, transitividade, nulo.
2. `equals ⇒ hashCode igual`.
3. Consistência do `hashCode` (várias chamadas).

Teste com `Endereco` e também com uma classe propositalmente errada (`EnderecoRuim`, sem `hashCode`) e mostre o auditor apontando o erro.

---

# Aula 165 — Big-O

## 🟢 Exercício 19 — Contando passos

Implemente três funções que recebem um array de `n` inteiros e **contam quantas operações** executam:

1. `acessarPrimeiro` — `O(1)`.
2. `somarTodos` — `O(n)`.
3. `compararTodosComTodos` (pares) — `O(n²)`.

Execute com `n = 10, 100, 1000, 10000`, imprima uma tabela com o número de operações e a classificação Big-O de cada uma.

---

## 🟡 Exercício 20 — Busca linear x binária

Crie um array ordenado de 1 milhão de inteiros.

1. Implemente `buscaLinear` e `buscaBinaria` (por conta própria, sem `Arrays.binarySearch`), contando as comparações.
2. Procure 5 valores (primeiro, último, meio, inexistente, aleatório).
3. Imprima uma tabela comparando comparações e tempo (`System.nanoTime`).

### Pergunta

Qual a complexidade de cada uma? Por que a binária exige que o array esteja ordenado?

---

## 🔴 Exercício 21 — Escolha da estrutura

Resolva o mesmo problema com três abordagens e meça: "Dado um array de 200 mil CPFs (como `long`), identificar quantos são repetidos".

1. **Força bruta**: dois laços (`O(n²)`) — use apenas 20 mil elementos nessa versão para não demorar.
2. **Ordenando antes** (use `Arrays.sort` e compare vizinhos) — `O(n log n)`.
3. **Tabela hash caseira** (a do exercício 15 adaptada para `long`) — `O(n)` médio.

Imprima uma tabela de tempo para cada abordagem (ajustando o tamanho para comparar de forma justa, ou escalando os resultados) e escreva sua conclusão sobre qual escolher e por quê.

### Responda

- Qual coleção você usaria se pudesse (sem ter estudado ainda)? Por que um `Set` resolveria?
- O que muda se a regra for "manter a ordem de chegada"?

---

# 🏆 Desafio Integrador do Bloco 18 — Cadastro Persistente de Produtos

Você vai construir um pequeno sistema de cadastro que **persiste** objetos em arquivo e **evita duplicidade** usando `equals`/`hashCode`.

## Classes

```text
Categoria  → nome (NÃO serializável, de uma "biblioteca" que você não pode alterar)
Fornecedor → cnpj, razaoSocial  (Serializable)
Produto    → codigo, nome, preco, estoque, Fornecedor, Categoria (transient),
             senhaAdmin (transient), static contador de produtos
Catalogo   → guarda produtos num array próprio (capacidade crescente)
```

## Requisitos

1. **Igualdade de negócio**:
   - `Produto` é igual a outro se tiver o mesmo `codigo`.
   - `Fornecedor` é igual se tiver o mesmo `cnpj`.
   - Implemente `equals`/`hashCode` coerentes e escreva testes manuais do contrato.
2. **Catálogo sem duplicados**: `adicionar(Produto)` rejeita um produto igual (use `equals`) com uma mensagem clara.
3. **Persistência**:
   - `salvar(Path)` serializa o catálogo todo (array).
   - `carregar(Path)` desserializa e restaura o contador estático (grave-o manualmente em `writeObject` do `Catalogo`).
   - `Categoria` deve ser salva/restaurada com `writeObject`/`readObject` personalizados em `Produto`.
   - `senhaAdmin` nunca deve ir para o arquivo.
4. **Versionamento**:
   - Declare `serialVersionUID`.
   - Crie uma "versão 2" do `Produto` com um novo campo (`desconto`) e prove que o arquivo da versão 1 ainda é lido (o novo campo vem com valor padrão).
5. **Busca rápida**: implemente uma tabela hash caseira com **balde** (exercício 15) para buscar um produto por `codigo` em `O(1)` médio. Compare com a busca linear em um catálogo de 100 mil produtos (gerados por um laço) e mostre o ganho.
6. **Backup**: a cada `salvar`, copie o arquivo antigo para `.bak` (reutilize NIO do bloco anterior).
7. **Relatório**: grave `relatorio.txt` com `BufferedWriter`: totais, produtos com estoque baixo, comparação de tempos (Big-O na prática).
8. **Tratamento de erros**: arquivo corrompido, arquivo inexistente e versão incompatível devem ter mensagens específicas.

## Cenários de teste

```text
Adicionar 5 produtos e tentar adicionar um duplicado
Salvar, encerrar, carregar e conferir os dados
Verificar que senhaAdmin voltou null e que a Categoria foi restaurada
Alterar a classe (novo campo) e ler o arquivo antigo
Buscar 1 produto entre 100 mil: linear x hash
```

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei o que é serializar e desserializar.
- [ ] Sei que `Serializable` é uma interface marcadora.
- [ ] Sei usar `ObjectOutputStream` e `ObjectInputStream`.
- [ ] Entendo que o construtor não roda na desserialização.
- [ ] Sei usar `transient` e sei que `static` não é serializado.
- [ ] Sei para que serve o `serialVersionUID`.
- [ ] Sei customizar com `writeObject`/`readObject`.
- [ ] Sei a diferença entre `==` e `equals`.
- [ ] Sei implementar `equals` seguindo o contrato.
- [ ] Sei por que `hashCode` existe e como funciona uma tabela hash.
- [ ] Sei a regra: `equals` true ⇒ `hashCode` igual.
- [ ] Sei usar `Objects.equals` e `Objects.hash`.
- [ ] Sei interpretar `O(1)`, `O(log n)`, `O(n)`, `O(n log n)`, `O(n²)`.
- [ ] Sei a diferença entre coleção ordenada e classificada.

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
