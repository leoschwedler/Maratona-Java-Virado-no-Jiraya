# Exercícios — Bloco 29: Padrões de Projeto

## Aulas 246 a 251

Este arquivo acompanha o README do Bloco 29.

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

> **Onde criar os arquivos:** `src/main/bloco29_aulas246a251_padroes_projeto/aulaXXX/`

> **Regras do bloco:**
> - Para cada padrão, primeiro escreva a versão **sem** o padrão (para sentir a dor) e depois a versão **com** o padrão.
> - Escreva, em comentário, **qual problema** o padrão resolveu naquele exercício.
> - Nada de bibliotecas externas (Lombok, MapStruct): faça à mão.

---

# Aula 246 — Builder

## 🟢 Exercício 01 — Sentindo a dor

Crie a classe `Carro` com 7 atributos (`marca`, `modelo`, `ano`, `cor`, `motor`, `portas`, `automatico`) e **construtor com todos**. Instancie 3 carros diferentes.

Em comentário responda:

- Quantos construtores você precisaria para permitir só marca+modelo, ou marca+modelo+ano?
- Qual a chance de trocar dois `String` sem notar?

---

## 🟡 Exercício 02 — Meu primeiro Builder

Refaça `Carro` com o padrão Builder:

1. construtor privado;
2. `CarroBuilder` como classe `static` interna;
3. métodos fluentes que retornam `this`;
4. `build()` que valida: `marca`, `modelo` e `ano` são obrigatórios (lance `IllegalStateException` com mensagem clara), e `ano` deve estar entre 1900 e o ano atual + 1.

Crie 5 carros: três válidos (com combinações diferentes de atributos opcionais) e dois inválidos (capture a exceção).

---

## 🔴 Exercício 03 — Builder avançado

Implemente `Pedido` com os atributos:

```text
cliente (obrigatório)
List<Item> itens (pelo menos 1)
enderecoEntrega (obrigatório)
cupom (opcional)
observacao (opcional)
prioridade (enum, padrão NORMAL)
```

Requisitos:

1. `Pedido` **imutável**: lista defensivamente copiada (`List.copyOf`).
2. O builder tem `adicionarItem(Item)` (acumula) e `itens(List<Item>)` (substitui).
3. Crie um método `toBuilder()` em `Pedido` que devolve um builder já preenchido (para criar uma versão modificada: `pedido.toBuilder().prioridade(URGENTE).build()`).
4. Valide no `build()` com **todas** as mensagens de erro juntas (lista de problemas), e não só a primeira.
5. Prove a imutabilidade: tente alterar a lista devolvida por `getItens()`.
6. Compare com um construtor de 6 parâmetros (comentário).

---

# Aula 247 — Factory

## 🟢 Exercício 04 — Fábrica de moedas

Reproduza a aula: `interface Moeda { String simbolo(); }`, `Real`, `Dolar`, `Euro`, `enum Pais { BRASIL, EUA, ALEMANHA }` e `MoedaFactory.criar(Pais)`.

Teste com os três países e capture o que acontece com um país ainda não tratado no `switch`.

---

## 🟡 Exercício 05 — Fábrica de formatadores

Crie `Formatador` com `String formatar(Object)` e as implementações `JsonFormatador`, `XmlFormatador`, `CsvFormatador`.

1. Uma `FormatadorFactory.paraFormato(String formato)` (case-insensitive) que lança exceção clara para formato desconhecido.
2. Uma versão da fábrica que usa um `Map<String, Supplier<Formatador>>` em vez de `switch`.
3. Um método para **registrar** novos formatadores em tempo de execução.
4. Prove que o código cliente (`main`) só conhece a interface.

---

## 🔴 Exercício 06 — Fábrica de pagamentos

Implemente um sistema de pagamento:

```text
interface Pagamento { ResultadoPagamento pagar(double valor); }
CartaoCredito, Pix, Boleto, Criptomoeda
```

Requisitos:

1. `PagamentoFactory.criar(TipoPagamento, Map<String, String> parametros)`: cada tipo exige parâmetros diferentes (cartão: número, cvv; pix: chave; boleto: cpf; cripto: carteira). Valide os parâmetros dentro da fábrica.
2. Regras: pix não aceita valor acima de R$ 5.000; boleto só acima de R$ 10; cartão cobra taxa de 2%.
3. Crie uma **Abstract Factory** simples: `PagamentoFactoryBR` e `PagamentoFactoryUS` que oferecem conjuntos diferentes de métodos.
4. Teste 10 cenários (válidos e inválidos).
5. Explique em comentário como a fábrica reduziu o acoplamento e o que acontece quando você adiciona um novo meio de pagamento.

---

# Aula 248 — Singleton Eager

## 🟢 Exercício 07 — Dois objetos, um problema

Implemente `Aviao` (nome + `Set<String>` de assentos) **sem** Singleton. Crie duas "visões" do mesmo avião e reserve o mesmo assento nas duas. Mostre o assento vendido duas vezes.

---

## 🟡 Exercício 08 — Eager

Converta para Singleton eager (`static final INSTANCE`):

1. Mostre que `getInstance() == getInstance()`.
2. Imprima `System.identityHashCode` de quatro chamadas feitas em classes diferentes.
3. Coloque um `System.out.println` no construtor e demonstre **quando** a instância é criada (no carregamento da classe: imprima antes e depois da primeira referência à classe).
4. Tente `new Aviao(...)` de fora (comentado) e explique o erro.

---

## 🔴 Exercício 09 — Singleton de configuração

Crie `Configuracao` (Singleton eager) que carrega um arquivo `app.properties` no construtor (se o arquivo não existir, use valores padrão).

1. `get(String chave)`, `get(String chave, String padrao)`, `getInt`, `getBoolean`.
2. A configuração é imutável após a criação.
3. Use em 5 classes diferentes e prove que o arquivo foi lido **uma única vez** (contador).
4. Discuta (comentário) o problema do eager quando o construtor lança exceção (`ExceptionInInitializerError`) e reproduza-o.
5. Discuta o que dificulta testar uma classe que usa `Configuracao.getInstance()` internamente e proponha uma alternativa (passar a configuração por parâmetro).

---

# Aula 249 — Singleton Lazy

## 🟢 Exercício 10 — Lazy simples

Implemente `Aviao` com lazy initialization (sem sincronização). Imprima uma mensagem no construtor e mostre que o objeto só é criado na **primeira** chamada a `getInstance()`.

---

## 🟡 Exercício 11 — Quebrando o lazy

1. Lance 100 threads chamando `getInstance()` ao mesmo tempo (use um `CountDownLatch` ou uma "barreira" para largar juntas). Conte quantas instâncias diferentes foram criadas (guarde os `identityHashCode`).
2. Corrija com `synchronized` no método e meça o tempo de 10 milhões de chamadas.
3. Corrija com double-checked locking + `volatile` e meça o tempo.
4. Retire o `volatile` e explique (comentário) o risco teórico.

---

## 🔴 Exercício 12 — Reflection

1. Use Reflection (`getDeclaredConstructor`, `setAccessible(true)`, `newInstance`) para criar uma segunda instância do seu Singleton lazy. Prove que `==` dá `false`.
2. Defenda o construtor lançando exceção se a instância já existir; mostre a defesa funcionando **e** o furo (criar a segunda instância via Reflection **antes** de qualquer `getInstance()`).
3. Tente quebrar o Singleton por **serialização**: serialize a instância, desserialize e compare. Conserte com `readResolve()`.
4. Tente quebrar por **clone** (`implements Cloneable`). Conserte.
5. Tabele quais técnicas de ataque existem e quais defesas são necessárias.

---

# Aula 250 — Singleton com Enum

## 🟢 Exercício 13 — Enum Singleton

Reescreva o `Aviao` como `enum AviaoSingleton { INSTANCE; ... }`. Compare o tamanho do código com as versões eager e lazy.

---

## 🟡 Exercício 14 — Ataques que não funcionam

Aplique ao enum os mesmos ataques do exercício 12 (Reflection, serialização) e registre as exceções/resultados. Explique por que cada um falha.

---

## 🔴 Exercício 15 — Enum com estado e concorrência

Implemente `GeradorDeIds` como enum Singleton com um contador.

1. Primeiro com `long` comum e 8 threads gerando 100 000 ids cada → prove que há ids repetidos.
2. Corrija com `synchronized`, depois com `AtomicLong`, e meça o tempo de cada um.
3. Adicione ao enum um método `reiniciar()` apenas para testes e discuta o perigo.
4. Faça o enum implementar uma interface `GeradorId` e inclua uma segunda constante `FAKE` para testes — discuta se isso ainda é um Singleton.
5. Discuta, em comentário, as críticas ao Singleton (estado global, testes, acoplamento) e como a injeção de dependências resolve.

---

# Aula 251 — Data Transfer Object

## 🟢 Exercício 16 — Primeiro DTO

Crie as entidades `Cliente` (id, nome, cpf, senha, endereço, telefone) e `Produto` (id, nome, preço, estoque, custoInterno). Crie `ResumoDoPedidoDTO` com apenas: nome do cliente, nome do produto, preço e quantidade. Monte o DTO a partir das entidades.

Em comentário: quais campos você **não** deve expor (senha, custoInterno) e por quê.

---

## 🟡 Exercício 17 — Mapper

Escreva uma classe `ClienteMapper` com:

```text
ClienteDTO paraDTO(Cliente c)
Cliente paraEntidade(ClienteDTO dto)           (campos que o DTO traz; o resto é padrão)
List<ClienteDTO> paraDTOs(List<Cliente>)
```

Regras:

1. O `ClienteDTO` não traz `senha` nem `cpf` completo (mascarar: `***.456.789-**`).
2. Valide o DTO recebido na conversão para entidade.
3. Teste com 5 clientes e imprima o JSON "manual" (monte o texto com `StringBuilder`).

---

## 🔴 Exercício 18 — Serviço de relatórios

Monte um `RelatorioService` que consome 3 "serviços" (simulados) — `ClienteService`, `PedidoService`, `ProdutoService` — e devolve um `RelatorioVendasDTO` (imutável, com Builder) contendo:

- período;
- total vendido;
- top 3 produtos;
- top 3 clientes;
- quantidade de pedidos por status.

Requisitos:

1. As entidades têm campos sensíveis que **não podem** aparecer no DTO.
2. Os DTOs internos (`ProdutoVendidoDTO`, `ClienteMelhorDTO`) são classes estáticas aninhadas.
3. Escreva um `toString` e um `toJson()` manual.
4. Compare o tamanho do DTO com o tamanho das entidades somadas.
5. Escreva em comentário quando usar DTO e quando seria exagero.

---

# 🏆 Desafio Integrador do Bloco 29 — Sistema de Reservas de Voos

Você vai construir o núcleo de uma companhia aérea usando os **cinco padrões** do bloco, cada um no lugar certo.

## Requisitos

1. **Singleton (enum)**: `FrotaDeAeronaves` — única fonte da verdade de aeronaves e assentos (cada aeronave física existe uma vez). Métodos de reserva **thread-safe**.
2. **Builder**: `Voo` (número, origem, destino, partida, chegada, aeronave, preço-base, classes, escalas opcionais) e `Reserva` (passageiro, voo, assento, bagagens, refeição opcional, seguro opcional). Validações no `build()`.
3. **Factory**:
   - `PagamentoFactory` (cartão, pix, boleto);
   - `TarifaFactory` (econômica, executiva, primeira classe) que cria objetos `Tarifa` com regras de preço, bagagem e remarcação diferentes;
   - `NotificadorFactory` (e-mail, SMS, push) escolhido pela preferência do passageiro.
4. **DTO**: `ConfirmacaoDeReservaDTO` (código da reserva, nome do passageiro, rota, horário, assento, valor total formatado) e `RelatorioDeOcupacaoDTO`. As entidades nunca vazam para a "camada de apresentação".
5. **Concorrência**: simule 200 passageiros tentando reservar 30 assentos de um voo ao mesmo tempo, com 20 threads. Nenhum assento pode ser vendido duas vezes; ao final, imprima a ocupação, quantos tiveram sucesso/falha, e os assentos que sobraram.
6. **Segurança do Singleton**: prove que Reflection e serialização não geram uma segunda `FrotaDeAeronaves`.
7. **Extensibilidade**: adicione um novo meio de pagamento (`Criptomoeda`) e uma nova classe tarifária (`Promocional`) alterando **apenas** as fábricas e as novas classes — mostre (em comentário) quais arquivos precisaram mudar.
8. **Relatório final** com todos os DTOs, em formato de tabela no console, gravado também em arquivo.

## Regras

- Em cada classe, escreva um comentário de uma linha: `// Padrão: X — motivo`.
- Escreva um texto curto (comentário) comparando o design **com** e **sem** padrões (acoplamento, legibilidade, facilidade de adicionar novos tipos).

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei explicar o que é um padrão de projeto e por que usá-los.
- [ ] Sei implementar um Builder com construtor privado e métodos fluentes.
- [ ] Sei validar dentro do `build()`.
- [ ] Sei implementar uma Factory e desacoplar o cliente das classes concretas.
- [ ] Sei implementar Singleton eager, lazy (com double-check + volatile) e com enum.
- [ ] Sei como Reflection e serialização quebram o Singleton e como o enum previne.
- [ ] Sei que Singleton thread-safe na criação não garante uso thread-safe.
- [ ] Sei explicar o que é um DTO e quando usar.
- [ ] Sei por que não devo expor entidades diretamente.

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
