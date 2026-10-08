# Exercícios — Bloco 22: Classes Internas e Parametrização de Comportamentos

## Aulas 189 a 194

Este arquivo acompanha o README do Bloco 22.

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

> **Onde criar os arquivos:** `src/main/bloco22_aulas189a194_classes_internas_comportamentos/aulaXXX/`

> **Regra do bloco:** lambdas só podem aparecer a partir da aula 194, como "visão do futuro" (mostrando a equivalência com a classe anônima). Antes disso, use apenas as formas clássicas.

---

# Aula 189 — Classes Internas: Introdução

## 🟢 Exercício 01 — Cartão e transações

Crie a classe `ContaBancaria` com os atributos privados `titular` e `saldo`.

Dentro dela, crie uma classe interna `Extrato` com o método `imprimir()` que mostra o titular e o saldo (lendo os atributos privados da externa).

No `main`: crie uma conta, depois um `Extrato` com a sintaxe `conta.new Extrato()`, e imprima.

### Responda

Por que a classe interna consegue ler `saldo` mesmo sendo `private`?

---

## 🟡 Exercício 02 — `this` x `Externa.this`

Crie `Empresa` (com `nome`) e, dentro dela, a interna `Funcionario` (com `nome` próprio).

No método `apresentar()` da interna, imprima:

```text
Funcionário: <nome do funcionário>
Empresa: <nome da empresa>
this: <this>
Empresa.this: <Empresa.this>
```

Use `toString` nas duas classes para que a impressão mostre algo legível. Crie 2 empresas com 2 funcionários cada e prove que cada funcionário "pertence" à sua empresa.

---

## 🔴 Exercício 03 — Lista ligada com nó interno

Implemente uma **lista ligada simples** de `String` chamada `ListaLigada`, usando uma classe interna `No` (com `valor` e `proximo`).

Métodos: `adicionar(String)`, `remover(String)`, `contem(String)`, `tamanho()`, `imprimir()`.

Regras:

- `No` deve ser `private` (ninguém de fora enxerga).
- A lista guarda apenas a referência para o primeiro nó.
- Implemente também `inverter()`.
- Teste com 6 elementos, incluindo remoção do primeiro, do meio e do último.

### Pergunta

Por que `No` não precisaria ser uma classe de topo? Seria melhor `static class No`? Por quê? (Pense na aula 192.)

---

# Aula 190 — Classes Locais

## 🟢 Exercício 04 — Classe dentro do método

Crie o método `static void saudar(String nome)` e, dentro dele, declare a classe local `Saudacao` com o método `dizer()`, que imprime `"Olá, " + nome`. Instancie e use.

Teste com 3 nomes diferentes.

---

## 🟡 Exercício 05 — Variável efetivamente final

1. Crie um método com um parâmetro `String prefixo` e uma variável local `int vezes = 3`.
2. Crie uma classe local que usa as duas variáveis para imprimir `prefixo` `vezes` vezes.
3. Faça funcionar.
4. Agora **altere** `vezes` depois de declarada (fora da classe local) e observe o erro de compilação (deixe comentado e explique).
5. Corrija copiando para uma nova variável `final`.

Explique, com suas palavras, **por que** o Java exige isso (pilha x heap, tempo de vida).

---

## 🔴 Exercício 06 — Validador local

Crie `static List<String> validarSenhas(List<String> senhas, int tamanhoMinimo)` que retorna as senhas **inválidas**.

Dentro do método:

1. Declare uma classe local `Regra` (abstrata ou concreta) com o método `boolean valida(String s)`.
2. Declare **três** subclasses locais: `TamanhoMinimo` (usa o parâmetro `tamanhoMinimo`), `TemDigito` e `TemMaiuscula`.
3. Monte um array com as três regras e aplique a cada senha.
4. Retorne as inválidas, com o motivo impresso no console.

Teste com 8 senhas.

### Responda

Quando uma classe local é uma boa ideia e quando é melhor ser uma classe separada?

---

# Aula 191 — Classes Anônimas

## 🟢 Exercício 07 — Sobrescrita pontual

Crie a classe `Animal` com `emitirSom()` ("Som genérico"). Crie um `Animal` anônimo que sobrescreve `emitirSom()` para imprimir "Miau". Imprima também `animal.getClass().getName()` e explique o nome que aparece.

---

## 🟡 Exercício 08 — Comparator anônimo

Crie `Pessoa` (`nome`, `idade`) e uma lista com 6 pessoas.

Ordene a lista **três vezes**, cada uma com um `Comparator<Pessoa>` **anônimo**:

1. por nome;
2. por idade crescente;
3. por idade decrescente e, em caso de empate, por nome.

Use `Collections.sort(lista, new Comparator<Pessoa>() {...})`. Mostre a lista após cada ordenação.

---

## 🔴 Exercício 09 — Eventos simulados

Crie a interface `OuvinteDeEvento` com `void aoOcorrer(String evento)`. Crie a classe `Botao` com:

```text
adicionarOuvinte(OuvinteDeEvento o)
clicar()          → avisa todos os ouvintes
```

No `main`, crie um botão e adicione 4 ouvintes **anônimos**:

1. imprime o evento em maiúsculas;
2. conta quantos cliques já ocorreram (use uma variável de instância da classe anônima);
3. grava (em uma lista externa, efetivamente final) o histórico de eventos;
4. ignora eventos que comecem com "ignorar".

Simule 6 cliques/eventos diferentes. Depois tente alterar uma variável local capturada de dentro do ouvinte e explique o erro. Resolva usando um array de 1 posição (`int[] contador = {0};`) e discuta por que isso "funciona" mas deve ser usado com cuidado.

---

# Aula 192 — Classes Aninhadas Estáticas

## 🟢 Exercício 10 — Estática x interna

Em uma classe `Externa` com um atributo de instância `valor` e um atributo estático `contador`, crie:

1. `class Interna` e `static class Estatica`.
2. Em cada uma, tente acessar `valor` e `contador` (as linhas que não compilam devem ficar comentadas, com a explicação).
3. Instancie as duas no `main` e mostre que a estática dispensa o objeto externo.

---

## 🟡 Exercício 11 — Builder simples

Crie a classe `Pizza` (`sabor`, `tamanho`, `borda`, `extras`) com construtor **privado** e uma classe `static class Builder` aninhada que monta a pizza com métodos encadeados:

```java
Pizza p = new Pizza.Builder("Calabresa").tamanho("grande").borda("catupiry").extras("azeitona").build();
```

Valide no `build()` que o sabor e o tamanho foram informados (lance `IllegalStateException` com mensagem clara).

### Pergunta

Por que o `Builder` é `static`? O que aconteceria se não fosse?

---

## 🔴 Exercício 12 — Meu próprio `Map.Entry`

Implemente uma classe `MeuMapa<K, V>` com um `Par<K, V>` interno `static` (chave, valor), usando um array interno de `Par` que cresce.

Métodos: `colocar(K, V)` (substitui se existir), `pegar(K)`, `remover(K)`, `tamanho()`, `Par<K,V>[] pares()`.

Regras:

- A classe `Par` deve ser `public static` para permitir `MeuMapa.Par<String,Integer>` de fora.
- Compare chaves com `equals`.
- Sobrescreva `toString` de `MeuMapa`.
- Compare com o `Map.Entry` da API: qual a relação de design?

Teste com `String`→`Integer` e `Integer`→`List<String>`.

---

# Aula 193 — Parametrizando Comportamentos (pt 01)

## 🟢 Exercício 13 — O problema da repetição

Crie a classe `Livro` (`titulo`, `autor`, `ano`, `preco`) e uma lista de 8 livros.

Escreva **três métodos separados** (sem nenhuma abstração):

```text
filtrarPorAutor(lista, autor)
filtrarMaisBaratosQue(lista, preco)
filtrarAnteriorA(lista, ano)
```

Marque (com comentários) no código **a única linha que muda** entre eles.

---

## 🟡 Exercício 14 — Cliente nunca está satisfeito

Adicione, **cada um como método novo** (de propósito, para sentir a dor), os requisitos:

1. livros cujo título começa com uma letra;
2. livros entre dois preços;
3. livros de autor X **e** anteriores a ano Y.

Conte quantos métodos você já tem e quantas linhas estão duplicadas. Escreva (3 a 5 linhas) como você explicaria ao seu gestor por que isso vira um problema de manutenção.

---

## 🔴 Exercício 15 — O mesmo problema, outro domínio

Aplique o mesmo raciocínio a outro domínio: `Funcionario` (`nome`, `cargo`, `salario`, `departamento`, `anoAdmissao`).

Escreva 5 métodos de filtro/contagem/soma que seguem o mesmo esqueleto (percorre → testa → acumula):

```text
filtrarPorDepartamento
contarComSalarioAcimaDe
somarSalariosDoCargo
nomesAdmitidosAntesDe
existeAlgumNoDepartamento (boolean)
```

Identifique o esqueleto comum e a "parte que muda" em cada um. Desenhe (em texto) a interface que você criaria para parametrizar cada parte que varia (você vai implementar na próxima aula).

---

# Aula 194 — Parametrizando Comportamentos (pt 02)

## 🟢 Exercício 16 — Interface + classe anônima

Crie a interface `FiltroLivro { boolean testar(Livro l); }` e o método `filtrar(List<Livro>, FiltroLivro)`.

Refaça os três filtros do exercício 13 usando **classes anônimas**. Elimine os três métodos antigos.

---

## 🟡 Exercício 17 — Para lambda

Reescreva o exercício anterior com **lambdas**. Mostre lado a lado a versão anônima e a lambda de um dos filtros e explique o que o compilador "adivinha" (tipo do parâmetro, método da interface).

Depois torne o filtro genérico:

```java
interface Condicao<T> { boolean testar(T t); }
static <T> List<T> filtrar(List<T> lista, Condicao<T> c)
```

E use com `List<Livro>`, `List<Integer>` (pares) e `List<String>` (começam com "A"). Por fim, troque `Condicao<T>` por `java.util.function.Predicate<T>` e confirme que continua funcionando.

---

## 🔴 Exercício 18 — Mini-framework de comportamentos

Crie, com generics e interfaces funcionais **suas** (sem usar `java.util.function`), os métodos utilitários:

```java
static <T> List<T> filtrar(List<T> l, Condicao<T> c)
static <T> int contar(List<T> l, Condicao<T> c)
static <T> boolean existe(List<T> l, Condicao<T> c)
static <T> boolean todos(List<T> l, Condicao<T> c)
static <T> void paraCada(List<T> l, Acao<T> a)              // Acao<T> { void executar(T t); }
static <T, R> List<R> mapear(List<T> l, Transformacao<T, R> t)  // Transformacao<T,R> { R aplicar(T t); }
static <T> T reduzir(List<T> l, T inicial, Operacao<T> op)  // Operacao<T> { T aplicar(T a, T b); }
```

Teste com `Funcionario`:

1. nomes dos funcionários do departamento "TI" com salário acima de 5000;
2. soma de todos os salários (use `reduzir`);
3. existe algum admitido antes de 2010?
4. todos têm salário positivo?
5. imprimir cada funcionário com `paraCada`.

Escreva cada chamada de duas maneiras: classe anônima e lambda.

---

# 🏆 Desafio Integrador do Bloco 22 — Motor de Regras de Promoções (E-commerce)

Você vai construir um **motor de regras** onde o comportamento é passado por parâmetro e as peças ficam organizadas com classes internas.

## Domínio

```text
Produto   → id, nome, categoria, preco, estoque
Cliente   → id, nome, tipo (COMUM, VIP), cidade
Carrinho  → cliente, itens (produto + quantidade)
Promocao  → nome, condicao(Carrinho), calculo(Carrinho) → desconto
```

## Requisitos

1. **Interfaces funcionais** suas: `Condicao<T>`, `Calculo<T, R>`, `Acao<T>`. Todas genéricas.
2. **Classes anônimas** (versão "antiga") para pelo menos 3 promoções:
   - 10% para clientes VIP;
   - frete grátis (desconto = valor do frete) acima de R$ 300;
   - leve 3 pague 2 em produtos da categoria "livros".
3. **Lambdas** (versão "moderna") para mais 3 promoções:
   - desconto progressivo por quantidade;
   - cupom de aniversário;
   - 5% na compra de produtos com estoque acima de 100 (queima de estoque).
4. **Motor**: `MotorDePromocoes` aplica todas as promoções que passam na condição ao carrinho e devolve o melhor combo (a regra: máximo de 30% de desconto total).
5. **Classes internas**:
   - `Carrinho` com uma classe interna `Item` (conhece o carrinho dono);
   - `MotorDePromocoes` com uma classe interna `Resultado` com o histórico de promoções aplicadas (usa o estado do motor);
   - `Produto` com um `static class Builder`;
   - uma classe local dentro de um método para uma regra pontual de "teste A/B" de promoção;
   - uma classe anônima para o ouvinte de eventos "promoção aplicada" (`OuvinteDePromocao`).
6. **Genéricos**: um método `static <T> List<T> filtrar(List<T>, Condicao<T>)` usado para: produtos da categoria X, clientes de uma cidade, carrinhos acima de um valor.
7. **Relatório**: para 5 carrinhos de cenários diferentes, imprimir os itens, o total, as promoções aplicadas, o desconto e o total final.

## Regras

- Nenhuma duplicação de laço de filtragem: toda filtragem passa por `filtrar`.
- Documente em comentário, para cada trecho de classe interna, **qual dos 4 tipos** é e por que o escolheu.
- Compare, em um comentário final, a legibilidade da versão com classe anônima x lambda.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei criar uma classe interna e instanciá-la com `outer.new Inner()`.
- [ ] Sei usar `this` e `Externa.this`.
- [ ] Sei o que é uma classe local e suas limitações.
- [ ] Sei o que é "efetivamente final" e por quê.
- [ ] Sei criar classes anônimas a partir de classes e interfaces.
- [ ] Sei que a classe anônima não tem nome nem construtor.
- [ ] Sei a diferença entre classe interna e classe aninhada estática.
- [ ] Reconheço `Map.Entry` e o `Builder` como casos de classe estática aninhada.
- [ ] Reconheço código repetido cuja única diferença é a condição.
- [ ] Sei parametrizar comportamento com interface + classe anônima.
- [ ] Sei transformar uma classe anônima de interface funcional em lambda.
- [ ] Sei tornar o filtro genérico.

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
