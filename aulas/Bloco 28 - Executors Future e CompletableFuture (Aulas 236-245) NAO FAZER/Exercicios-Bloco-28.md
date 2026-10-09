# Exercícios — Bloco 28: Executors, Future e CompletableFuture

## Aulas 236 a 245

Este arquivo acompanha o README do Bloco 28.

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

> **Onde criar os arquivos:** `src/main/bloco28_aulas236a245_executors_completablefuture/aulaXXX/`

> **Regras do bloco:**
> - Todo `ExecutorService` que você criar **precisa** ser encerrado (`shutdown`), de preferência em `try/finally`.
> - Meça tempos com `System.currentTimeMillis()` ou `nanoTime()` e rode cada experimento várias vezes.
> - Para simular chamadas lentas use `TimeUnit.SECONDS.sleep(...)` ou `Thread.sleep`.
> - Sempre trate `InterruptedException` corretamente (restaure a flag de interrupção).

---

# Aula 236 — Thread Pools

## 🟢 Exercício 01 — Pool fixo

Crie uma tarefa `Printer(int n)` que imprime o nome da thread, "iniciou n", dorme 2 s e imprime "finalizou n". Submeta 10 tarefas a um `newFixedThreadPool(3)`. Observe os nomes das threads e responda:

- Quantas threads diferentes foram usadas?
- Quanto tempo total levou? (calcule o esperado: ⌈10/3⌉ × 2 s)

Garanta que o programa **termina** (`shutdown` + `awaitTermination`).

---

## 🟡 Exercício 02 — Comparando pools

Execute 20 tarefas de 1 s cada em:

1. `newSingleThreadExecutor()`;
2. `newFixedThreadPool(4)`;
3. `newCachedThreadPool()`.

Meça o tempo total e a quantidade de threads distintas usadas em cada um (guarde os nomes numa coleção concorrente). Explique os resultados e discuta o risco de `newCachedThreadPool` com 100 000 tarefas.

---

## 🔴 Exercício 03 — Dimensionando

Crie dois tipos de tarefa:

- **CPU-bound**: calcular primos até 2 milhões;
- **I/O-bound**: `sleep(200 ms)` simulando uma chamada de rede.

Para cada tipo, execute 100 tarefas variando o tamanho do pool (1, 2, 4, 8, 16, 32, 64) e monte uma tabela de tempos. Em que ponto aumentar o pool deixa de ajudar para CPU? E para I/O? Relacione com `availableProcessors()`.

Depois mostre:

1. a diferença entre `shutdown()` e `shutdownNow()` (com tarefas na fila);
2. o que acontece se você submeter uma tarefa depois do `shutdown` (`RejectedExecutionException`);
3. o que acontece com o programa se você **esquecer** o `shutdown`.

---

# Aula 237 — ScheduledExecutorService

## 🟢 Exercício 04 — Uma vez, depois de um atraso

Agende uma tarefa que imprime a hora para daqui a 3 segundos, outra para 6 s e outra para 1 s (nesta ordem de agendamento). Observe a ordem de execução. Encerre o executor depois que todas rodarem.

---

## 🟡 Exercício 05 — Rate × Delay

Crie uma tarefa que imprime o horário de **início** e de **fim**, e dura 3 s. Agende-a duas vezes (em dois executores separados):

1. `scheduleAtFixedRate(tarefa, 0, 5, SECONDS)`;
2. `scheduleWithFixedDelay(tarefa, 0, 5, SECONDS)`.

Observe 5 execuções de cada e preencha uma tabela com os horários. Depois repita com uma tarefa que dura 7 s (mais longa que o período) e descreva o que mudou em cada versão.

---

## 🔴 Exercício 06 — Monitor de serviços

Implemente um `MonitorDeSaude` que:

1. A cada 2 s faz um *health check* simulado de 3 serviços (falha aleatória 20% das vezes).
2. Depois de 3 falhas **consecutivas** de um serviço, dispara um alerta e passa a verificar esse serviço a cada 10 s (alterando a frequência dinamicamente).
3. Um serviço volta ao ritmo normal ao se recuperar.
4. Um relatório agendado imprime um resumo a cada 15 s.
5. O monitor encerra sozinho após 60 s, cancelando os agendamentos (`ScheduledFuture.cancel`) e fazendo `shutdown` limpo.
6. Demonstre o problema de uma **exceção não tratada** dentro de uma tarefa periódica (as execuções seguintes param silenciosamente!) e corrija com `try/catch` dentro da tarefa.

---

# Aula 238 — Callable

## 🟢 Exercício 07 — Callable básico

Crie um `Callable<Integer>` que sorteia um número de 1 a 100 com `ThreadLocalRandom` e devolve. Submeta a um executor e imprima o resultado com `future.get()`.

---

## 🟡 Exercício 08 — Várias tarefas, um resultado

Crie 10 `Callable<Long>` que calculam o fatorial de números diferentes (use `BigInteger` se preferir). Submeta todas e **depois** colete os resultados (evite o erro de fazer `submit`/`get` alternados). Mostre o tempo de uma versão sequencial e da versão com pool.

Use `invokeAll(lista de callables)` e compare com `submit` em laço.

---

## 🔴 Exercício 09 — Tratando exceções

1. Faça um `Callable` que lança uma exceção checada (`IOException`) e outro que lança `RuntimeException`.
2. Capture a `ExecutionException` e extraia a causa com `getCause()`.
3. Compare com um `Runnable` que lança `RuntimeException` dentro de `execute` (a exceção é impressa pela thread e **o pool continua**) e dentro de `submit` (a exceção só aparece se você chamar `get`).
4. Implemente um `invokeAny` com 3 tarefas que podem falhar e mostre o resultado da primeira que concluir com sucesso.
5. Escreva uma classe utilitária `Tarefas.executarTodas(List<Callable<T>>)` que devolve `Map<Callable, Resultado>` onde `Resultado` indica sucesso (valor) ou falha (exceção).

---

# Aula 239 — Future

## 🟢 Exercício 10 — Fazendo outra coisa enquanto espera

Submeta uma tarefa que demora 3 s e devolve a cotação "4,35". Na thread `main`, enquanto isso, faça uma soma grande e imprima. Depois pegue o resultado. Mostre que o tempo total ≈ 3 s, e não 3 s + tempo da soma.

---

## 🟡 Exercício 11 — Timeout

1. Tarefa que demora 5 s; `future.get(2, SECONDS)` → trate `TimeoutException`.
2. Em seguida chame `future.cancel(true)` e verifique `isCancelled()`, `isDone()`.
3. Mostre que o cancelamento só interrompe de fato se a tarefa responde a interrupções: faça duas versões da tarefa (uma com `sleep` em laço, outra com um laço de CPU que **não** checa `Thread.interrupted()`) e veja a diferença.
4. Corrija a segunda para checar `isInterrupted()`.

---

## 🔴 Exercício 12 — Agregador de cotações

Simule 5 provedores de cotação de moeda (`Provedor1..5`) com tempos de resposta aleatórios (200–3000 ms) e uma probabilidade de falha de 20%.

1. Dispare as 5 consultas em paralelo.
2. Espere no máximo 2 s por provedor.
3. Calcule a média das cotações que **chegaram a tempo**, informando quantos provedores falharam, quantos estouraram o tempo e quantos responderam.
4. Cancele as consultas pendentes ao final.
5. Compare com `invokeAny` (usa a primeira resposta) e com `invokeAll` com timeout global.
6. Meça o tempo total e justifique.

---

# Aula 240 — CompletableFuture: get e join

## 🟢 Exercício 13 — Síncrono × assíncrono

Implemente um `StoreService` com `getPriceSync(store)` (2 s). Busque o preço de 4 lojas:

1. de forma síncrona;
2. com `CompletableFuture.supplyAsync` + `join`.

Compare os tempos (8 s × 2 s).

---

## 🟡 Exercício 14 — O `get` precoce

1. Dispare as 4 consultas e chame `join()` imediatamente depois de cada `supplyAsync` (laço sequencial) → tempo?
2. Dispare as 4 e só depois colete → tempo?
3. Explique a diferença.
4. Compare `get()` e `join()` quando a tarefa lança uma exceção: capture `ExecutionException` x `CompletionException` e extraia a causa.

---

## 🔴 Exercício 15 — Do `Future` ao `CompletableFuture`

Reimplemente a busca de preços de 3 formas e meça o tempo e a quantidade de linhas:

1. `ExecutorService` + `Future` (tratando todas as exceções e o `shutdown`).
2. `CompletableFuture.supplyAsync` com o `ForkJoinPool` comum.
3. `CompletableFuture.supplyAsync` com um executor próprio de 4 threads.

Adicione:

- `orTimeout(1500, ms)` (Java 9+) e `completeOnTimeout(valorPadrao, ...)`: veja como se comportam quando uma loja demora mais.
- Um `CompletableFuture` completado **manualmente** (`complete`, `completeExceptionally`) por uma thread à parte.
- Comparação entre a quantidade de threads usadas por cada versão (imprima os nomes).

---

# Aula 241 — CompletableFuture e Streams

## 🟢 Exercício 16 — A armadilha

Reproduza o erro clássico:

```text
stores.stream().map(s -> supplyAsync(...)).map(CompletableFuture::join).collect(toList())
```

Meça o tempo (sequencial). Em seguida, corrija com o padrão de duas etapas e meça de novo.

---

## 🟡 Exercício 17 — `peek` revelando a ordem

Use `peek` para imprimir (com timestamp) quando cada loja é **disparada** e quando cada `join` **começa/termina**, nas duas versões (errada e correta). Desenhe a linha do tempo.

---

## 🔴 Exercício 18 — Comparando três estratégias

Para 20 lojas (cada uma com 1 s de latência), compare tempo e threads usadas:

1. `stores.stream()` sequencial (síncrono);
2. `stores.parallelStream()` (síncrono dentro de stream paralelo);
3. `CompletableFuture` com `ForkJoinPool` comum;
4. `CompletableFuture` com executor de 20 threads.

Explique por que o `parallelStream` demora ≈ ⌈20/(CPUs−1)⌉ s e o executor próprio ≈ 1 s. Teste também com 100 lojas. Escreva a conclusão: "para I/O, prefira...".

---

# Aula 242 — Executor e ThreadFactory

## 🟢 Exercício 19 — Executor próprio

Passe um `Executor` com 4 threads para o `supplyAsync` e mostre os nomes das threads usadas (`pool-N-thread-M`).

---

## 🟡 Exercício 20 — ThreadFactory

Crie uma `ThreadFactory` que:

1. dá nomes como `loja-worker-1`, `loja-worker-2`...;
2. marca como daemon;
3. define uma prioridade;
4. registra um `UncaughtExceptionHandler` que imprime erros.

Use-a em `newFixedThreadPool` e prove que o programa **termina** mesmo sem `shutdown` (daemon), e que sem daemon ele não termina.

---

## 🔴 Exercício 21 — Isolando recursos (bulkhead)

Monte dois executores separados: um para "chamadas de pagamento" (3 threads) e outro para "chamadas de catálogo" (10 threads). Simule uma carga em que o serviço de pagamento fica lento (5 s por chamada). Mostre que, com **executores separados**, o catálogo continua rápido; depois use **um único** executor e mostre o catálogo sendo prejudicado. Meça a latência do catálogo em ambos.

Explique o padrão *bulkhead* (anteparo) em comentário.

---

# Aula 243 — Encadeando chamadas pt 01 (⚠ aula sem transcrição)

## 🟢 Exercício 22 — thenApply e thenAccept

Monte: `supplyAsync` (preço) → `thenApply` (aplica 10% de desconto) → `thenApply` (formata em moeda) → `thenAccept` (imprime). Mostre o nome da thread em cada etapa. Compare `thenApply` e `thenApplyAsync`.

---

## 🟡 Exercício 23 — Erros

1. `supplyAsync` que lança exceção + `exceptionally` devolvendo um valor padrão.
2. O mesmo com `handle` (tratando sucesso e erro).
3. O mesmo com `whenComplete` (só observa).
4. O que acontece se você não tratar e nunca chamar `join/get`? (a exceção "some")
5. Propague um erro em uma cadeia de 3 etapas e mostre qual etapa o recebe.

---

## 🔴 Exercício 24 — Resiliência

Implemente `buscarComRetry(Supplier<T>, int tentativas, Duration espera)` que devolve um `CompletableFuture<T>`:

- tenta a chamada; se falhar, aguarda (sem bloquear thread — use `CompletableFuture.delayedExecutor` do Java 9+ ou `ScheduledExecutorService`) e tenta novamente;
- após esgotar as tentativas, completa excepcionalmente com a última causa;
- adicione um *timeout* por tentativa.

Teste com um serviço que falha 70% das vezes e mostre o número de tentativas usadas por chamada.

---

# Aula 244 — Encadeando chamadas pt 02

## 🟢 Exercício 25 — Preço → Cotação

Implemente o fluxo da aula com 4 lojas: `getPrice` (1 s) → `Quote` (conversão rápida) → `applyDiscount` (1 s). Faça a versão síncrona (8 s) e a assíncrona (≈2 s) com `thenApply` + `thenCompose`.

---

## 🟡 Exercício 26 — `thenApply` × `thenCompose`

1. Mostre o tipo (`CompletableFuture<CompletableFuture<String>>`) que resulta de usar `thenApply` com uma função que devolve `CompletableFuture`.
2. Corrija com `thenCompose`.
3. Use `thenCombine` para somar o preço de duas lojas independentes em paralelo.
4. Meça os tempos e desenhe o grafo de dependências.

---

## 🔴 Exercício 27 — Pipeline de checkout

Monte um fluxo assíncrono de compra:

```text
buscarUsuario(id) ─┐
                   ├─▶ calcularFrete(endereco, itens) ─┐
buscarCarrinho(id) ┘                                    ├─▶ gerarPedido ─▶ cobrar ─▶ enviarEmail
                      validarEstoque(itens) ────────────┘
```

Regras:

1. Cada serviço tem latência de 300–800 ms e 10% de chance de falhar.
2. Etapas independentes devem rodar em **paralelo** (`thenCombine`, `allOf`).
3. Se `validarEstoque` falhar, o checkout inteiro falha com mensagem clara.
4. Se `enviarEmail` falhar, o pedido continua válido (apenas registra aviso).
5. Meça o tempo total e compare com a versão sequencial.
6. Imprima o caminho crítico (a cadeia que determinou o tempo total).

---

# Aula 245 — allOf e anyOf

## 🟢 Exercício 28 — `allOf`

Dispare 5 tarefas com tempos aleatórios (0,5 a 3 s). Use `allOf(...).join()` para esperar todas e imprima o tempo total (≈ o da mais lenta). Use `thenAccept` para imprimir cada resultado **assim que fica pronto**.

---

## 🟡 Exercício 29 — `anyOf`

Consulte 5 "mirrors" redundantes e use `anyOf` para obter a resposta mais rápida. Mostre:

1. O valor e quem respondeu primeiro.
2. Que as outras tarefas continuam rodando (e como cancelá-las manualmente).
3. O que acontece quando a primeira a terminar falha com exceção.

---

## 🔴 Exercício 30 — Coletando tudo com tolerância a falhas

Implemente `static <T> CompletableFuture<List<T>> todasOuAlgumas(List<CompletableFuture<T>> futures, Duration limite)` que:

1. espera todas **até o limite de tempo**;
2. devolve os resultados que chegaram (descartando as que falharam ou estouraram o prazo);
3. também devolve estatísticas (quantas ok, quantas falharam, quantas estouraram).

Teste com 10 futures de latência e falhas aleatórias. Compare com `allOf` (que falha se uma falhar).

---

# 🏆 Desafio Integrador do Bloco 28 — Comparador de Preços Assíncrono (Mini Buscapé)

Você vai construir um serviço que consulta **dezenas de lojas** de forma assíncrona e entrega o melhor resultado ao usuário.

## Cenário

```text
Loja (nome, latência simulada 300–3000 ms, taxa de falha, moeda: BRL/USD/EUR)
Cotação de moeda (serviço externo simulado, 200–800 ms)
Programa de desconto (serviço externo, 300–1000 ms, depende do cliente VIP ou não)
Frete (serviço externo, 200–900 ms, depende do CEP)
```

## Requisitos

1. **Catálogo de lojas**: 30 lojas geradas aleatoriamente (semente fixa).
2. **Busca paralela** com `CompletableFuture` e um executor próprio (nome das threads `buscador-N`, daemon).
3. **Pipeline por loja**: `preço → converter para BRL (cotação) → aplicar desconto → somar frete`, com `thenCompose`/`thenCombine` onde fizer sentido. As consultas de **cotação** e **frete** são independentes entre si e devem ocorrer em paralelo.
4. **Tolerância a falhas**: se uma loja falhar, é descartada (`exceptionally`/`handle`) e contabilizada; cada etapa tem timeout (`orTimeout`).
5. **Resposta incremental**: cada oferta é impressa **assim que fica pronta** (`thenAccept`), com o tempo decorrido.
6. **Melhor oferta**: `allOf` para obter o ranking final (3 mais baratas) e `anyOf` para a "resposta mais rápida" como pré-visualização.
7. **Cache** com `ConcurrentHashMap` (se já conhecer) ou `synchronized` de cotações com validade de 5 s.
8. **Agendamento**: um `ScheduledExecutorService` repete a busca a cada 10 s por 3 rodadas e compara se o melhor preço mudou ("alerta de queda de preço").
9. **Limite de concorrência**: no máximo 8 consultas simultâneas (use um `Semaphore` ou o tamanho do pool) — mostre com o log que o limite é respeitado.
10. **Encerramento limpo**: `shutdown` + `awaitTermination`; nenhuma thread sobrando.
11. **Métricas**: tempo total, p95 de latência por etapa, taxa de falhas, threads usadas, e comparação com a versão sequencial (estimada ou executada com poucas lojas).

## Cenários de teste

```text
30 lojas, 20% de falha: o sistema retorna o ranking mesmo assim.
Cotação lenta (3 s): timeout e uso do último valor em cache.
Cliente VIP × não VIP.
3 rodadas agendadas com variação de preços.
```

## Regras

- Sem `Thread.sleep` fora do código que simula latência.
- Sem bloquear (`join/get`) dentro das etapas do pipeline, exceto no `main` ao final.
- Documente (comentário) cada decisão: por que `thenApply`, por que `thenCompose`, por que `thenCombine`.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei por que usar pool de threads em vez de criar threads manualmente.
- [ ] Sei criar `ExecutorService` e encerrá-lo corretamente.
- [ ] Conheço os tipos de pool e quando usar cada um.
- [ ] Sei usar `ScheduledExecutorService` e a diferença entre rate e delay.
- [ ] Sei a diferença entre `Runnable` e `Callable`.
- [ ] Sei usar `Future.get` com timeout e `cancel`.
- [ ] Sei criar `CompletableFuture` com `supplyAsync`.
- [ ] Sei a diferença entre `get` e `join`.
- [ ] Sei por que não se deve `join` dentro do mesmo pipeline de stream.
- [ ] Sei passar um `Executor` e uma `ThreadFactory` personalizados.
- [ ] Sei encadear com `thenApply`, `thenCompose`, `thenCombine`, `thenAccept`.
- [ ] Sei tratar erros com `exceptionally`/`handle`.
- [ ] Sei usar `allOf` e `anyOf`.

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
