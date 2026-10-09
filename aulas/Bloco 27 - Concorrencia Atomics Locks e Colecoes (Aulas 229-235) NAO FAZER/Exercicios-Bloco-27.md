# Exercícios — Bloco 27: Concorrência (Atomics, Locks e Coleções Concorrentes)

## Aulas 229 a 235

Este arquivo acompanha o README do Bloco 27.

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

> **Onde criar os arquivos:** `src/main/bloco27_aulas229a235_concorrencia_locks_colecoes/aulaXXX/`

> **Regras do bloco:**
> - Concorrência dá resultados diferentes a cada execução: **rode cada experimento pelo menos 10 vezes**.
> - Ainda **não** vimos `ExecutorService`: crie as threads manualmente (`new Thread`).
> - Sempre dê nome às threads.
> - Quando o enunciado pedir "meça", use `System.nanoTime()` e a média de várias execuções (ignore a primeira, de aquecimento).

---

# Aula 229 — AtomicInteger

## 🟢 Exercício 01 — O contador quebrado

1. Crie um contador `int` comum e 2 threads que incrementam 100 000 vezes cada.
2. Rode 10 vezes e anote o resultado de cada uma.
3. Conserte com `AtomicInteger` e confirme 200 000 em todas as execuções.

### Responda

Por que `count++` não é atômico? (Descreva as 3 etapas.)

---

## 🟡 Exercício 02 — Três formas de proteger

Implemente o mesmo contador de três maneiras e compare resultado e tempo (4 threads × 1 000 000 incrementos):

1. `synchronized`;
2. `AtomicInteger`;
3. `LongAdder`.

Monte uma tabela com os tempos médios e explique por que o `AtomicInteger` costuma ganhar do `synchronized` e o `LongAdder` ganha de ambos sob alta disputa.

---

## 🔴 Exercício 03 — CAS na mão

1. Implemente um `incrementar()` usando um laço com `compareAndSet` (sem `incrementAndGet`) e prove que funciona.
2. Conte quantas **tentativas falhas** do CAS ocorreram com 4 threads (use outro `AtomicLong` para contabilizar).
3. Implemente um gerador de **IDs únicos** com `AtomicLong` (`proximoId()`), e teste com 8 threads gerando 100 000 ids cada; prove que não houve ids repetidos (guarde-os em um `ConcurrentHashMap` ou valide pelo total).
4. Implemente uma **"trava binária"** com `AtomicBoolean` (`compareAndSet(false, true)`) e use-a para garantir que um trecho de código nunca é executado por duas threads ao mesmo tempo.
5. Mostre uma situação em que `AtomicInteger` **não basta** (duas variáveis que precisam mudar juntas) e explique.

---

# Aula 230 — Lock e ReentrantLock

## 🟢 Exercício 04 — `synchronized` → `Lock`

Reescreva a conta bancária do bloco anterior (saque seguro) usando `ReentrantLock` com `try/finally`. Rode 20 vezes: o saldo nunca pode ficar negativo.

Depois **remova o `finally`** de propósito, lance uma exceção dentro da seção crítica, e mostre o que acontece com as outras threads (ficam presas).

---

## 🟡 Exercício 05 — `tryLock`

Crie uma "impressora" compartilhada protegida por `ReentrantLock`:

1. 5 threads tentam imprimir; cada impressão demora 1 s.
2. Cada thread usa `tryLock(300, TimeUnit.MILLISECONDS)`: se não conseguir, imprime "ocupada, vou tentar depois" e tenta de novo até 5 vezes.
3. Conte quantas conseguiram imprimir e quantas desistiram.
4. Use `getQueueLength()` e `isHeldByCurrentThread()` para produzir um log informativo.

---

## 🔴 Exercício 06 — Justo × injusto

Implemente um teste de **justiça** com 10 threads que disputam um lock 1000 vezes cada (trabalho curto dentro do lock):

1. Com `new ReentrantLock()` (injusto) e com `new ReentrantLock(true)` (justo).
2. Registre a ordem em que as threads conseguem o lock e calcule quantas "furaram a fila".
3. Meça o tempo total das duas versões.
4. Mostre a **reentrância**: um método `a()` que chama `b()`, ambos pegando o mesmo lock, funcionando sem travar; use `getHoldCount()`.
5. Reproduza um `IllegalMonitorStateException` chamando `unlock()` sem ter o lock.
6. Use `lockInterruptibly()` e interrompa uma thread que espera o lock.

---

# Aula 231 — Condition

## 🟢 Exercício 07 — await e signal

Crie uma thread que faz `condicao.await()` e outra que, depois de 2 s, faz `signal()`. Mostre o estado da primeira antes e depois. Tente chamar `await` sem ter o lock e capture a exceção.

---

## 🟡 Exercício 08 — Buffer com duas condições

Implemente `BufferLimitado<T>` (capacidade N) com `ReentrantLock` e **duas** condições: `naoCheio` e `naoVazio`.

1. 2 produtores e 2 consumidores, 50 itens cada.
2. Garanta que o buffer nunca excede a capacidade (valide com um contador interno).
3. Compare com a versão `wait/notifyAll` do bloco anterior: por que duas conditions evitam acordar threads à toa? Meça quantos "acordares inúteis" houve em cada versão.

---

## 🔴 Exercício 09 — Portal de reuniões

Implemente uma `SalaDeReuniao`:

- Só começa quando **N participantes** chegaram (todos esperam em `await`).
- Quando o último chega, `signalAll` e todos entram.
- Um **moderador** pode `encerrar()`, acordando e expulsando todos.
- Existe uma **lista de espera** com capacidade limitada: se a sala está cheia, o participante espera em outra condition até alguém sair.

Requisitos:

1. Use um único `ReentrantLock` com 3 conditions.
2. Simule 12 participantes chegando em tempos aleatórios.
3. Gere um log com horário relativo (`+0,45 s`) mostrando entradas, esperas e saídas.
4. Garanta que não ocorre *lost wakeup* nem *spurious wakeup* indevido (use `while`).

---

# Aula 232 — ReentrantReadWriteLock

## 🟢 Exercício 10 — Leitores simultâneos

Crie um `Config` com um `Map<String, String>` protegido por `ReentrantReadWriteLock`.

1. 3 leitores que dormem 1 s dentro do read lock; mostre que os três entram **ao mesmo tempo** (o tempo total é ~1 s, não 3 s).
2. 1 escritor que dorme 1 s dentro do write lock; mostre que os leitores esperam por ele.

---

## 🟡 Exercício 11 — Cache de preços

Implemente `CachePrecos` com `get(produto)` (leitura) e `atualizar(produto, preco)` (escrita).

1. 8 threads leitoras (10 000 leituras cada) e 1 escritora (100 atualizações).
2. Compare o tempo de: `synchronized` em tudo, `ReentrantLock` em tudo e `ReentrantReadWriteLock`.
3. Repita variando a proporção leitura/escrita (99/1, 90/10, 50/50) e descubra quando o ReadWriteLock deixa de compensar.

---

## 🔴 Exercício 12 — Armadilhas

Mostre com código:

1. **Rebaixamento** (*downgrade*): uma thread com write lock adquire o read lock e depois solta o write lock (funciona).
2. **Promoção** (*upgrade*): uma thread com read lock tenta adquirir o write lock (trava/deadlock). Prove com `tryLock` (que retorna `false`) para não travar de verdade.
3. **Escritores famintos** (*starvation*): com muitos leitores contínuos, o escritor demora muito. Meça a latência do escritor.
4. Implemente `getReadLockCount()` / `isWriteLocked()` em um monitor que imprime o estado a cada 100 ms.
5. Escreva um relatório curto (comentário) dizendo quando **não** usar `ReadWriteLock`.

---

# Aula 233 — CopyOnWriteArrayList

## 🟢 Exercício 13 — O erro do `ArrayList`

1. Percorra um `ArrayList` com `for-each` removendo elementos → capture `ConcurrentModificationException`.
2. Repita com `CopyOnWriteArrayList` → sem exceção.
3. Explique por que não houve exceção (iteração sobre o *snapshot*).

---

## 🟡 Exercício 14 — O snapshot

Reproduza o experimento da aula:

1. Lista com 2000 números.
2. Thread A obtém o iterator e dorme 2 s.
3. Thread B remove 500 elementos após 500 ms.
4. Thread A percorre o iterator: quantos elementos vê? E qual o tamanho final da lista?
5. Tente `iterator.remove()` e capture o `UnsupportedOperationException`.

---

## 🔴 Exercício 15 — Sistema de eventos

Implemente um `BarramentoDeEventos` com uma lista de **ouvintes** (`CopyOnWriteArrayList<Ouvinte>`):

1. `registrar(ouvinte)`, `remover(ouvinte)`, `publicar(evento)`.
2. Um ouvinte pode se **remover durante** o processamento de um evento — sem exceção.
3. 4 threads publicando 1000 eventos e 2 threads registrando/removendo ouvintes continuamente.
4. Compare com uma versão `ArrayList` + `synchronized` (mostrando o problema de deixar o `synchronized` durante a chamada de ouvintes) e com `Collections.synchronizedList`.
5. Meça o custo de escrever (1000 `add` seguidos) em `CopyOnWriteArrayList` × `ArrayList`.
6. Discuta (comentário) por que ela é ideal para ouvintes e ruim para filas de trabalho.

---

# Aula 234 — ArrayBlockingQueue

## 🟢 Exercício 16 — put e take

Reproduza a demonstração da aula com capacidade 1: a produtora tenta colocar 2 itens, a consumidora retira após 5 s. Mostre os horários e identifique onde a produtora ficou bloqueada.

---

## 🟡 Exercício 17 — Produtor-consumidor

Implemente: 3 produtores (cada um gera 20 números aleatórios com pausas curtas) e 2 consumidores (somam os números) com uma `ArrayBlockingQueue` de capacidade 5.

1. Use "pílulas de veneno" para encerrar os consumidores.
2. Verifique que a soma total consumida = soma total produzida.
3. Meça a vazão com capacidades 1, 5, 50 e 500.
4. Mostre a diferença de comportamento entre `put/take`, `offer/poll`, `offer(timeout)/poll(timeout)` e `add` (capture `IllegalStateException`).

---

## 🔴 Exercício 18 — Pipeline em estágios

Monte um *pipeline* de 3 estágios conectados por `BlockingQueue`s:

```text
Leitor → [fila1] → Processador (x2 threads) → [fila2] → Gravador
```

- **Leitor**: gera 1000 pedidos (objetos).
- **Processador**: valida e calcula o total (simule 5 ms de trabalho).
- **Gravador**: escreve linhas em um arquivo (`BufferedWriter`), uma única thread.

Requisitos:

1. As filas têm capacidade limitada (*backpressure*): se o gravador for lento, o leitor desacelera automaticamente.
2. Encerramento limpo propagando a pílula de veneno por todos os estágios (com 2 processadores, a pílula do processador precisa ser recontada!).
3. Mostre os tamanhos das filas ao longo do tempo (monitor).
4. Experimente tornar o gravador lento (`sleep(20)`) e analise o gargalo.
5. Compare com a versão sequencial.

---

# Aula 235 — LinkedTransferQueue

## 🟢 Exercício 19 — Todas as operações

Monte um quadro (executando o código) com o resultado de cada operação em uma `LinkedTransferQueue` **vazia** e depois com **um elemento**:

```text
add · offer · put(sem bloquear) · peek · element · poll · remove · take(com timeout via poll(1s))
```

Diferencie as que lançam exceção, as que devolvem `null/false` e as que bloqueiam.

---

## 🟡 Exercício 20 — `transfer` × `put`

1. Um produtor chama `put("msg")` e imprime "produtor seguiu"; um consumidor retira após 3 s. Meça quando o produtor seguiu.
2. Repita com `transfer("msg")`. Meça de novo.
3. Teste `tryTransfer(msg)` sem consumidor esperando (retorna `false`, não enfileira) e com consumidor esperando (retorna `true`).
4. Teste `tryTransfer(msg, 2, TimeUnit.SECONDS)`.
5. Use `hasWaitingConsumer()` e `getWaitingConsumerCount()`.

---

## 🔴 Exercício 21 — Requisição e resposta

Implemente um mini sistema **RPC** local:

- Clientes enviam pedidos (`transfer`) para um *pool* de 3 trabalhadores e esperam a confirmação de recebimento.
- Cada trabalhador responde por uma `BlockingQueue` individual do cliente.
- Meça a latência de ida e volta.
- Se não houver trabalhador livre em 1 s (`tryTransfer` com timeout), o cliente recebe "serviço ocupado" e tenta novamente com *backoff* exponencial.
- Simule 10 clientes × 20 requisições e produza um relatório (média, mínimo, máximo, percentil 95 da latência, e número de rejeições).

---

# 🏆 Desafio Integrador do Bloco 27 — Plataforma de Pedidos de Alta Concorrência

Você vai construir o núcleo de uma plataforma de e-commerce simplificada, combinando **tudo** do bloco.

## Componentes

```text
Estoque               → quantidade por produto (muita leitura, escrita moderada)
Pedido                → id, itens, status
GeradorDeIds          → ids únicos de pedido
Contadores            → pedidos recebidos, aprovados, rejeitados
FilaDeEntrada         → pedidos chegando (BlockingQueue limitada)
Validadores (N)       → consomem a fila de entrada
FilaDePagamento       → TransferQueue: entrega direta ao gateway
Gateway de pagamento  → M trabalhadores
Auditoria             → lista de ouvintes de eventos (CopyOnWriteArrayList)
Painel                → thread que exibe métricas a cada 500 ms
```

## Requisitos

1. **Ids** com `AtomicLong`; **contadores** com `AtomicInteger`/`LongAdder` (compare os dois com benchmark).
2. **Estoque** protegido por `ReentrantReadWriteLock`: consultas concorrentes e baixa de estoque exclusiva. A baixa é atômica para o pedido inteiro (todos os itens ou nenhum).
3. **Fila de entrada** `ArrayBlockingQueue(50)`; 5 "clientes" produzem 200 pedidos cada em ritmo variável; a fila deve aplicar *backpressure*.
4. **Validadores** (3 threads): validam o pedido, reservam estoque e enviam para o gateway com `transfer`/`tryTransfer(timeout)`; sem gateway livre em 500 ms → o pedido é devolvido ao estoque e marcado "rejeitado por indisponibilidade".
5. **Gateway** com 2 trabalhadores que "processam" o pagamento (10–50 ms) e emitem um evento de auditoria.
6. **Auditoria** com `CopyOnWriteArrayList<Ouvinte>`: ouvintes de log, de estatística e um ouvinte que se **remove sozinho** após 100 eventos.
7. **Lock + Condition**: implemente um *throttle* (limite de pedidos por segundo): quando excedido, os validadores esperam em uma `Condition` até a janela seguinte, quando uma thread "relógio" faz `signalAll`.
8. **Painel** (daemon): imprime a cada 500 ms: tamanho das filas, contadores, estoque total, `getQueueLength` do lock de estoque.
9. **Encerramento limpo** (sem `System.exit`): quando todos os pedidos foram produzidos, propagar pílulas de veneno; esperar todas as threads (`join`); relatório final.
10. **Verificação de invariantes**: no final, `estoque inicial = estoque final + unidades vendidas`; `aprovados + rejeitados = recebidos`. Execute 20 vezes e prove que as invariantes valem sempre.

## Experimentos

- Compare a vazão (pedidos/s) com 1, 2, 4 e 8 validadores.
- Compare estoque com `synchronized` × `ReentrantReadWriteLock` × `StampedLock` (pesquisa opcional).
- Injete um erro proposital (esquecer o `finally` no `unlock`) e mostre o sistema travando; depois corrija.

## Regras

- Nada de `ExecutorService`, `ConcurrentHashMap` ou `CompletableFuture` (próximos blocos).
- Documente (comentário) cada recurso compartilhado, qual mecanismo o protege e **por quê** foi escolhido.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei por que `count++` não é atômico e como `AtomicInteger` resolve.
- [ ] Sei explicar CAS e o que é "lock-free".
- [ ] Sei usar `ReentrantLock` com `try/finally`.
- [ ] Conheço `tryLock`, fairness, reentrância e `lockInterruptibly`.
- [ ] Sei usar `Condition` (`await/signal/signalAll`) no lugar de `wait/notify`.
- [ ] Sei quando usar `ReentrantReadWriteLock` e suas armadilhas.
- [ ] Sei como `CopyOnWriteArrayList` funciona e quando usá-la.
- [ ] Sei o que é `BlockingQueue` e diferencio `put/take` de `offer/poll`.
- [ ] Sei usar pílula de veneno para encerrar consumidores.
- [ ] Sei o que `LinkedTransferQueue.transfer` faz de diferente.

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
