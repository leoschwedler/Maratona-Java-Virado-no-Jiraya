# Bloco 28 — Executors, `Callable`, `Future` e `CompletableFuture`

## Aulas 236 a 245

Este bloco fecha a parte de concorrência. Você vai deixar de criar threads manualmente e passar a **submeter tarefas** a um *pool* gerenciado, receber **resultados futuros** e, por fim, compor **pipelines assíncronos** com `CompletableFuture`.

As aulas deste bloco são:

```text
236 — Concorrência pt 08 — Executors pt 01 — Thread Pools
237 — Concorrência pt 09 — Executors pt 02 — ScheduledExecutorService
238 — Concorrência pt 10 — Executors pt 03 — Interface Callable
239 — Concorrência pt 11 — Executors pt 04 — Future
240 — Concorrência pt 12 — CompletableFuture pt 01 — get e join
241 — Concorrência pt 13 — CompletableFuture pt 02 — Streams
242 — Concorrência pt 14 — CompletableFuture pt 03 — ThreadFactory
243 — Concorrência pt 15 — CompletableFuture pt 04 — Encadeando chamadas pt 01   (⚠ sem transcrição)
244 — Concorrência pt 16 — CompletableFuture pt 05 — Encadeando chamadas pt 02
245 — Concorrência pt 17 — CompletableFuture pt 06 — allOf, anyOf
```

> ⚠️ **Aviso sobre a aula 243:** não existe transcrição. Pelo título e pela aula 244 (que a continua e a retoma), ela introduz o encadeamento de chamadas (`thenApply`, `thenAccept`, `thenRun`, tratamento de erro). Escrevi essa seção com base na API e no contexto da 244.

Linha do tempo do que você já usou e do que vem agora:

```text
new Thread(runnable).start()        ← bloco 26: manual, uma thread por tarefa
ExecutorService + Runnable/Callable ← aulas 236–239: pool reaproveitável, resultado via Future
CompletableFuture                    ← aulas 240–245: assíncrono, encadeável, combinável
```

---

# Aula 236 — Executors pt 01 — Thread Pools

## 1. O problema de criar threads na mão

Criar uma `Thread` é **caro** (memória, tempo, recursos do sistema). Se a cada requisição de usuário você criar uma thread nova, 10 000 usuários simultâneos = 10 000 threads → o servidor cai.

A solução: um **pool de threads** — um conjunto **fixo/limitado de threads reutilizáveis**. Você **submete tarefas** e o pool decide qual thread as executa. Quando uma thread termina uma tarefa, pega a próxima da fila.

```text
tarefas ──▶ [ fila de tarefas ] ──▶ [ T1 ][ T2 ][ T3 ][ T4 ]  (pool com 4 threads)
```

O framework **Executor** (pacote `java.util.concurrent`) **separa** *submissão* da tarefa de *execução* dela.

---

## 2. A tarefa de exemplo

```java
public class Printer implements Runnable {
    private final int number;
    public Printer(int number) { this.number = number; }

    @Override
    public void run() {
        System.out.printf("Thread %s iniciou: %d%n", Thread.currentThread().getName(), number);
        try {
            Thread.sleep(3000);                 // simula trabalho
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("Thread %s finalizou: %d%n", Thread.currentThread().getName(), number);
    }
}
```

## 3. `ExecutorService` com pool fixo

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

executor.execute(new Printer(1));
executor.execute(new Printer(2));
executor.execute(new Printer(3));
executor.execute(new Printer(4));
executor.execute(new Printer(5));
executor.execute(new Printer(6));      // só 4 rodam ao mesmo tempo
```

Saída: as tarefas 1–4 iniciam (`pool-1-thread-1` … `pool-1-thread-4`); as tarefas 5 e 6 esperam na fila. Quando uma thread libera, **reaproveita o mesmo nome** (`pool-1-thread-1` executa a tarefa 5). O número de threads **não cresce**.

A vantagem: mesmo submetendo 500 tarefas, o pool continua com 4 threads.

---

## 4. Dimensionando o pool

`Runtime.getRuntime().availableProcessors()` informa o número de processadores lógicos (na aula: 8, com 4 núcleos físicos e *hyper-threading*).

| Tipo de tarefa | Tamanho de pool típico |
|---|---|
| **CPU-bound** (cálculo pesado) | ≈ número de processadores |
| **I/O-bound** (rede, disco, banco) | maior (as threads ficam esperando) |

---

## 5. Encerrando o pool: `shutdown`

⚠️ Se você não encerrar, **o programa não termina** (as threads do pool são "user threads" que continuam vivas esperando tarefas).

| Método | O que faz |
|---|---|
| `shutdown()` | **para de aceitar** novas tarefas, mas **conclui** as já submetidas |
| `shutdownNow()` | tenta **interromper** as em execução e devolve a lista das que não começaram |
| `awaitTermination(tempo, unidade)` | **espera** o término (retorna `boolean`) |
| `isShutdown()` / `isTerminated()` | consulta o estado |

O instrutor destaca: depois de `shutdown()`, o programa **ainda não acabou** — as tarefas em andamento continuam. Para esperar de verdade:

```java
executor.shutdown();
if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
    executor.shutdownNow();                 // passou do prazo: força
}
System.out.println("Programa finalizado");
```

Sempre use `try/finally` para garantir o `shutdown`.

---

## 6. Fábricas de pools (`Executors`)

| Método | Descrição |
|---|---|
| `newFixedThreadPool(n)` | exatamente `n` threads; fila ilimitada |
| `newSingleThreadExecutor()` | **uma** thread (tarefas executadas em ordem, uma por vez) |
| `newCachedThreadPool()` | cria threads **conforme a necessidade**, reaproveita ociosas e **remove** as que ficam 60 s paradas |
| `newScheduledThreadPool(n)` | para tarefas agendadas (próxima aula) |
| `newWorkStealingPool()` | pool de *work-stealing* (usa `ForkJoinPool`) |

Atenção: `newCachedThreadPool` pode criar threads **sem limite**; `newFixedThreadPool` pode acumular uma fila **sem limite**. Em produção, pools configurados com `ThreadPoolExecutor` (capacidade e rejeição) dão controle total.

## O que você precisa dominar (Aula 236)

- Por que pool de threads.
- `ExecutorService`, `execute`, `Executors.newFixedThreadPool`.
- Tarefas na fila e reaproveitamento das threads.
- `shutdown`, `shutdownNow`, `awaitTermination`.
- Os tipos de pool.

---

# Aula 237 — Executors pt 02 — `ScheduledExecutorService`

## 1. O que é

Um executor que roda tarefas **após um atraso** ou **periodicamente**:

```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
```

---

## 2. Executar **uma vez**, depois de um atraso: `schedule`

```java
executor.schedule(() -> beep(), 5, TimeUnit.SECONDS);   // roda uma vez, daqui a 5 s
```

Exemplo auxiliar:

```java
private static void beep() {
    System.out.println("beep " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
}
```

O programa não termina sozinho: é preciso `executor.shutdown()` (a tarefa agendada ainda será executada, mas depois o pool encerra).

---

## 3. Executar **repetidamente**

### `scheduleAtFixedRate(tarefa, atrasoInicial, periodo, unidade)`

```java
executor.scheduleAtFixedRate(() -> beep(), 1, 5, TimeUnit.SECONDS);
```

- Espera 1 s, executa; depois executa **a cada 5 s medidos a partir do INÍCIO** da execução anterior.
- Se a tarefa demora **menos** que o período → o intervalo é sempre 5 s.
- Se a tarefa demora **mais** que o período → a próxima começa **imediatamente** após terminar (não executa em paralelo com ela mesma; "compensa" o atraso).

### `scheduleWithFixedDelay(tarefa, atrasoInicial, atraso, unidade)`

```java
executor.scheduleWithFixedDelay(() -> beep(), 1, 5, TimeUnit.SECONDS);
```

- O atraso de 5 s é contado a partir do **FIM** da execução anterior.

Exemplo para visualizar a diferença, com tarefa que dura 3 s:

```text
FixedRate  (5s):  início 0s  → 5s  → 10s  → 15s ...      (a cada 5 s de INÍCIO a INÍCIO)
FixedDelay (5s):  início 0s, termina 3s → espera 5s → início 8s → 16s → 24s ...  (5 s APÓS terminar)
```

> Observação: a transcrição da aula inclui momentos em que o instrutor se confunde entre as duas e corrige-se; a regra acima é a correta pela documentação.

| | `scheduleAtFixedRate` | `scheduleWithFixedDelay` |
|---|---|---|
| Intervalo contado | de **início a início** | do **fim** ao início seguinte |
| Se a tarefa demora muito | executa a seguinte logo em seguida | sempre espera o atraso completo |

---

## 4. Cancelando

O método devolve um `ScheduledFuture<?>`:

```java
ScheduledFuture<?> agendamento = executor.scheduleAtFixedRate(() -> beep(), 1, 5, TimeUnit.SECONDS);

executor.schedule(() -> {
    System.out.println("Cancelando o agendamento");
    agendamento.cancel(false);        // false: não interrompe execução em curso
}, 10, TimeUnit.SECONDS);

// depois: executor.shutdown();
```

O cancelamento encerra as repetições; o pool continua vivo até o `shutdown`.

⚠️ **Exceção dentro da tarefa periódica cancela as próximas execuções silenciosamente** — sempre trate exceções dentro da tarefa.

## 5. Usos reais

Limpeza periódica de cache, envio de relatórios diários, *health checks*, tentativas de reconexão (*retry*) com atraso.

## O que você precisa dominar (Aula 237)

- `schedule`, `scheduleAtFixedRate`, `scheduleWithFixedDelay`.
- Diferença entre *rate* e *delay*.
- `ScheduledFuture.cancel`.
- Encerrar o executor.

---

# Aula 238 — Executors pt 03 — `Callable`

## 1. O limite do `Runnable`

`Runnable.run()` é `void`: **não devolve resultado** e **não pode lançar exceção checada**.

## 2. `Callable<V>`

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

| | `Runnable` | `Callable<V>` |
|---|---|---|
| Método | `void run()` | `V call()` |
| Retorna valor? | não | **sim** (tipo genérico `V`) |
| Lança exceção checada? | não | **sim** (`throws Exception`) |

Exemplo da aula: gerar um número aleatório e devolvê-lo:

```java
public class RandomNumberCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int numero = ThreadLocalRandom.current().nextInt(1, 11);
        System.out.printf("%s gerou o número %d%n", Thread.currentThread().getName(), numero);
        return numero;
    }
}
```

`ThreadLocalRandom.current().nextInt(1, 11)` — mais indicado que `Math.random()` em ambiente multithread (o `Math.random()` compartilha um único gerador sincronizado; o `ThreadLocalRandom` tem um por thread, sem disputa).

---

## 3. Submetendo: `submit` devolve um `Future`

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

Future<Integer> future = executor.submit(new RandomNumberCallable());
System.out.println("Programa continua, sem esperar o resultado...");

Integer resultado = future.get();        // ⬅ AQUI espera o resultado
System.out.println("Resultado: " + resultado);

executor.shutdown();
```

Pontos importantes:

- `executor.execute(Runnable)` → não devolve nada.
- `executor.submit(Callable)` / `submit(Runnable)` → devolve `Future`.
- O `submit` **não bloqueia**: devolve o `Future` imediatamente.
- `future.get()` **bloqueia** a thread atual até a tarefa terminar. Lança:
  - `InterruptedException` (alguém interrompeu quem esperava),
  - `ExecutionException` (a tarefa lançou exceção; a causa original está em `getCause()`).

### Recomendação do instrutor

> Ao usar executor com *pool*, **evite** chamar `get()` / bloquear dentro das próprias tarefas: você estaria acoplando tarefas e correndo o risco de travar o pool (todas as threads esperando resultados de tarefas que nunca executam).

## O que você precisa dominar (Aula 238)

- `Callable<V>` x `Runnable`.
- `submit` devolve `Future`.
- `future.get()` bloqueia e lança duas exceções.
- `ThreadLocalRandom` x `Math.random()`.

---

# Aula 239 — Executors pt 04 — `Future`

## 1. Programação síncrona × assíncrona

- **Síncrona**: uma operação só começa quando a anterior termina; a thread **espera**.
- **Assíncrona**: dispara a operação e **segue em frente**, pegando o resultado depois.

Exemplo: uma tela que mostra a cotação do dólar. Se a mesma thread que cuida da tela fizer a chamada ao servidor remoto, a **tela congela** até a resposta chegar. Por isso: uma thread para a interface e outra para buscar a cotação.

`Future<V>` representa **"o resultado de uma computação assíncrona que ainda pode estar em andamento"**.

---

## 2. Exemplo da aula

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

Future<Double> dolarFuture = executor.submit(() -> {
    TimeUnit.SECONDS.sleep(2);          // simula chamada lenta ao servidor
    return 4.35;
});

// Enquanto isso, a thread principal faz OUTRA coisa:
System.out.println(Thread.currentThread().getName() + " fazendo outra tarefa");
long soma = 0;
for (int i = 0; i < 1_000_000; i++) soma += i;
System.out.println("Soma: " + soma);

// Depois pega o resultado (com timeout!)
try {
    Double dolar = dolarFuture.get(3, TimeUnit.SECONDS);
    System.out.println("Dólar: " + dolar);
} catch (TimeoutException e) {
    System.out.println("Não foi possível obter a cotação, tente novamente mais tarde");
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
} finally {
    executor.shutdown();
}
```

Sequência observada:

1. A thread `pool-1-thread-1` começa a buscar o dólar.
2. A thread `main` faz sua soma **ao mesmo tempo**.
3. Depois `main` chama `get` e espera, se preciso.

---

## 3. Métodos de `Future`

| Método | Faz |
|---|---|
| `get()` | espera **indefinidamente** pelo resultado |
| `get(tempo, unidade)` | espera até o limite; lança `TimeoutException` |
| `isDone()` | já terminou? (não bloqueia) |
| `isCancelled()` | foi cancelado? |
| `cancel(mayInterrupt)` | tenta cancelar |
| `resultNow()` / `state()` | (Java 19+) |

**Sempre defina um *timeout*!** Um `get()` sem limite pode travar sua thread para sempre se o servidor remoto cair. Com timeout você trata o problema e informa o usuário.

### `cancel` x interrupção

`future.cancel(true)` tenta **interromper** a thread que executa a tarefa, mas só funciona se a tarefa **responde a interrupções** (por exemplo, está em `sleep`, `wait`, `BlockingQueue.take`). Uma tarefa que não verifica `Thread.interrupted()` continua rodando. O instrutor observa também que, para a exceção de timeout, é boa prática cancelar o `Future`.

---

## 4. Limitações do `Future`

1. **Não é possível completá-lo manualmente** (ex.: em caso de erro de API).
2. **Não dá para encadear** ações ("quando terminar, faça X") sem bloquear com `get`.
3. **Não combina** facilmente vários futures.
4. Não tem tratamento de erros elegante.

Essas limitações são a razão de existir do `CompletableFuture`.

## O que você precisa dominar (Aula 239)

- Síncrono × assíncrono.
- `Future`: `get`, `get(timeout)`, `isDone`, `cancel`.
- Sempre usar timeout.
- Limitações que levam ao `CompletableFuture`.

---

# Aula 240 — CompletableFuture pt 01 — `get` e `join`

## 1. Cenário: consulta de preços em várias lojas

Um serviço de loja que simula uma chamada lenta:

```java
public class StoreService {
    public double getPriceSync(String store) {
        return calculatePrice(store);
    }

    public Future<Double> getPriceAsync(String store) {          // (versão com Future, a seguir)
        ...
    }

    private double calculatePrice(String store) {
        System.out.printf("Calculando preço para %s%n", store);
        delay();                                                  // 2 segundos
        return ThreadLocalRandom.current().nextDouble(1, 500);
    }

    private void delay() {
        try { TimeUnit.SECONDS.sleep(2); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}
```

---

## 2. Versão síncrona (lenta)

```java
StoreService service = new StoreService();
long start = System.currentTimeMillis();

service.getPriceSync("Store 1");
service.getPriceSync("Store 2");
service.getPriceSync("Store 3");
service.getPriceSync("Store 4");

System.out.println("Tempo total: " + (System.currentTimeMillis() - start) + " ms");   // ≈ 8000 ms
```

4 lojas × 2 s = **8 s**. Com 30 lojas o usuário desistiria.

---

## 3. Versão com `Future` e `ExecutorService`

Para paralelizar, cada consulta vai para uma thread:

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

Future<Double> f1 = executor.submit(() -> service.getPriceSync("Store 1"));
Future<Double> f2 = executor.submit(() -> service.getPriceSync("Store 2"));
Future<Double> f3 = executor.submit(() -> service.getPriceSync("Store 3"));
Future<Double> f4 = executor.submit(() -> service.getPriceSync("Store 4"));

System.out.println(f1.get());      // as 4 chamadas já estão em andamento ao mesmo tempo
System.out.println(f2.get());
...
executor.shutdown();
```

Tempo ≈ **2 s** (as quatro rodam em paralelo). Mas dá trabalho: criar o executor, tratar `InterruptedException`/`ExecutionException`, fechar o executor.

⚠️ Cuidado (erro comum mostrado na aula): **se você fizer `submit` e `get` na mesma sequência dentro de um laço** (`submit; get; submit; get...`), perde o paralelismo, pois cada `get` bloqueia antes do próximo `submit`. Dispare todos primeiro e só depois colete.

---

## 4. `CompletableFuture` simplifica

```java
public CompletableFuture<Double> getPriceAsync(String store) {
    return CompletableFuture.supplyAsync(() -> calculatePrice(store));
}
```

- `supplyAsync(Supplier<T>)` executa o `Supplier` em outra thread (por padrão o `ForkJoinPool.commonPool()`) e devolve um `CompletableFuture<T>`.
- **Não precisa** criar nem encerrar um `ExecutorService`.
- Não obriga a tratar exceções checadas no ponto de chamada (pode usar `join`).

```java
CompletableFuture<Double> c1 = service.getPriceAsync("Store 1");
CompletableFuture<Double> c2 = service.getPriceAsync("Store 2");
CompletableFuture<Double> c3 = service.getPriceAsync("Store 3");
CompletableFuture<Double> c4 = service.getPriceAsync("Store 4");

System.out.println(c1.join());
System.out.println(c2.join());
System.out.println(c3.join());
System.out.println(c4.join());
```

Tempo ≈ **2 s**.

---

## 5. `get()` × `join()`

| | `get()` | `join()` |
|---|---|---|
| Exceções | `InterruptedException`, `ExecutionException` (**checadas**) | `CompletionException` (**não checada**) |
| Timeout | `get(tempo, unidade)` | `orTimeout`/`completeOnTimeout` (Java 9+) |
| Uso em lambdas/streams | incômodo | **ideal** |

O `join()` lança uma exceção não checada, por isso fica melhor dentro de streams (aula seguinte).

## 6. Vantagens sobre `Future` (resumo)

1. Pode ser **completado manualmente**: `complete(valor)`, `completeExceptionally(ex)`.
2. Dá para **encadear** (`thenApply`, `thenCompose`...).
3. Dá para **combinar** (`thenCombine`, `allOf`, `anyOf`).
4. Tem **tratamento de erro** (`exceptionally`, `handle`).
5. Não exige gerenciar `ExecutorService` (embora aceite um).

O instrutor também alerta: quando uma tarefa assíncrona lança exceção e você não faz `get`/`join`, **a exceção fica "engolida"**. Trate sempre.

## O que você precisa dominar (Aula 240)

- Por que paralelizar chamadas lentas.
- `Future` + `ExecutorService` na mão.
- `CompletableFuture.supplyAsync`.
- `get()` × `join()`.
- O erro de fazer `get` imediatamente após cada `submit`.

---

# Aula 241 — CompletableFuture pt 02 — Streams

## 1. A armadilha de usar stream com `CompletableFuture`

Você tem uma lista de lojas e quer buscar os preços de todas, em paralelo, e depois juntar os resultados.

### ❌ Versão errada (sequencial disfarçada)

```java
List<Double> precos = stores.stream()
        .map(store -> CompletableFuture.supplyAsync(() -> service.getPriceSync(store)))
        .map(CompletableFuture::join)            // ⬅ join logo depois de cada supplyAsync!
        .collect(Collectors.toList());
```

Tempo ≈ 8 s. Por quê? Streams são **preguiçosos** e processam **um elemento por vez por todas as etapas**: loja 1 → `supplyAsync` → `join` (espera 2 s) → só então a loja 2 entra no pipeline.

```text
loja1: supplyAsync → join (espera 2s) → loja2: supplyAsync → join (espera 2s) → ...
```

### ✅ Versão correta: quebrar em duas etapas

```java
// 1ª etapa: DISPARA todas (coleta os CompletableFutures numa lista)
List<CompletableFuture<Double>> futures = stores.stream()
        .map(store -> CompletableFuture.supplyAsync(() -> service.getPriceSync(store)))
        .collect(Collectors.toList());               // terminal: força o disparo de todas

// 2ª etapa: ESPERA os resultados
List<Double> precos = futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
```

Tempo ≈ 2 s. Na primeira etapa as quatro tarefas já foram **disparadas** antes de qualquer `join`.

### Regra geral

> **Nunca coloque `supplyAsync` e `join` no mesmo pipeline de stream (um logo após o outro).** Primeiro dispare tudo (`collect`), depois `join`.

(Com `parallelStream` o problema se mascara, mas você perde o controle sobre o pool; o recomendado é o padrão de duas etapas.)

## 2. Comparação com `parallelStream`

`stores.parallelStream().map(service::getPriceSync)` também paralelizaria, mas usa o `ForkJoinPool` comum (com limite de ~número de CPUs) e **não permite** configurar um executor próprio nem compor etapas assíncronas. Para chamadas de I/O, `CompletableFuture` com executor próprio dá mais controle.

## O que você precisa dominar (Aula 241)

- Por que `map(supplyAsync).map(join)` é sequencial.
- O padrão das duas etapas.
- Streams são preguiçosos e processam elemento a elemento.

---

# Aula 242 — CompletableFuture pt 03 — `Executor` e `ThreadFactory`

## 1. Quando o serviço só oferece versão síncrona

Se você **não tem acesso** ao código do `StoreService` (vem de uma biblioteca) e ele só tem o método síncrono, você pode mesmo assim executá-lo de forma assíncrona **embrulhando a chamada**:

```java
List<CompletableFuture<Double>> futures = stores.stream()
        .map(store -> CompletableFuture.supplyAsync(() -> service.getPriceSync(store)))
        .collect(Collectors.toList());

List<Double> precos = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
```

Ou seja, o assincronismo é uma "camada por cima" de código síncrono.

---

## 2. Usando seu próprio `Executor`

`supplyAsync` tem uma sobrecarga que recebe um `Executor`:

```java
ExecutorService executor = Executors.newFixedThreadPool(
        Math.min(stores.size(), 100));          // dimensione conforme a tarefa

CompletableFuture<Double> cf = CompletableFuture.supplyAsync(
        () -> service.getPriceSync("Store 1"), executor);
```

Isso evita competir pelo `ForkJoinPool.commonPool()` (pequeno, compartilhado com os streams paralelos) e permite um pool grande para tarefas de I/O.

Lembre de `executor.shutdown()` no fim — o `ForkJoinPool` comum usa threads *daemon* (não segura a JVM), mas seu pool próprio normalmente usa *user threads*.

---

## 3. `ThreadFactory` — controlando como as threads nascem

Você pode personalizar as threads do pool (nome, prioridade, daemon):

```java
ExecutorService executor = Executors.newFixedThreadPool(4, runnable -> {
    Thread t = new Thread(runnable);
    t.setDaemon(true);                       // não impede a JVM de encerrar
    t.setName("loja-worker");
    return t;
});
```

`ThreadFactory` é uma interface funcional: `Thread newThread(Runnable r)`.

Benefícios do `daemon`: o programa termina mesmo que alguém esqueça o `shutdown()`. (Cuidado: tarefas daemon são interrompidas abruptamente no fim da JVM.)

Efeito visível: o nome das threads muda nos logs (`loja-worker` em vez de `pool-1-thread-1`), o que facilita depurar.

## O que você precisa dominar (Aula 242)

- Tornar assíncrono um método síncrono de terceiros.
- Passar um `Executor` para `supplyAsync`.
- `ThreadFactory`: nome, daemon, prioridade.
- Dimensionamento do pool.

---

# Aula 243 — CompletableFuture pt 04 — Encadeando chamadas pt 01 (⚠ sem transcrição)

> Conteúdo reconstruído com base na aula 244 e na API.

## 1. Encadeamento: "quando terminar, faça isto"

O grande poder do `CompletableFuture` é **compor** etapas assíncronas **sem bloquear**:

| Método | Recebe | Faz |
|---|---|---|
| `thenApply(Function)` | resultado `T` | **transforma** o resultado (`map`) |
| `thenAccept(Consumer)` | resultado `T` | **consome** o resultado (sem devolver) |
| `thenRun(Runnable)` | nada | executa algo após terminar |
| `thenCompose(Function→CF)` | resultado `T` | encadeia **outro** `CompletableFuture` (`flatMap`) |
| `thenCombine(outroCF, BiFunction)` | dois resultados | **combina** dois futures independentes |

```java
CompletableFuture<String> cf = CompletableFuture
        .supplyAsync(() -> service.getPriceSync("Store 1"))          // etapa 1 (async)
        .thenApply(preco -> preco * 0.9)                              // etapa 2: 10% de desconto
        .thenApply(preco -> String.format("R$ %.2f", preco));         // etapa 3: formatar

cf.thenAccept(System.out::println);                                   // etapa final: imprimir
```

Cada `then...` devolve **um novo** `CompletableFuture`, formando uma pipeline.

## 2. Variantes `...Async`

Todos têm uma versão `thenApplyAsync`, `thenAcceptAsync`...: a etapa pode rodar **em outra thread** do pool (sem o sufixo, roda na thread que completou a etapa anterior ou na que chamou, se já estava pronta).

## 3. Tratamento de erros

```java
CompletableFuture<Double> cf = CompletableFuture
        .supplyAsync(() -> { if (true) throw new IllegalStateException("falhou"); return 1.0; })
        .exceptionally(ex -> -1.0);                      // valor alternativo em caso de erro

cf.handle((valor, ex) -> ex == null ? valor : -1.0);     // trata sucesso e erro
cf.whenComplete((valor, ex) -> ...);                      // observa sem alterar
```

## 4. Completar manualmente

```java
CompletableFuture<String> cf = new CompletableFuture<>();
new Thread(() -> cf.complete("pronto")).start();   // ou completeExceptionally(new Exception(...))
```

## O que você precisa dominar (Aula 243)

- `thenApply`, `thenAccept`, `thenRun`, `thenCompose`, `thenCombine`.
- Cada etapa devolve um novo `CompletableFuture`.
- Versões `...Async`.
- `exceptionally`, `handle`, `whenComplete`.
- `complete` / `completeExceptionally`.

---

# Aula 244 — CompletableFuture pt 05 — Encadeando chamadas pt 02

## 1. O cenário: preço → desconto

Agora cada loja tem **duas** chamadas lentas (1 segundo cada):

1. `getPriceSync(store)` → devolve uma `String` no formato `"Loja:preço:CODIGO_DESCONTO"`.
2. `Discount.applyDiscount(Quote)` → aplica o desconto (também lenta).

Fluxo de dados:

```text
Store ──getPrice──▶ String "loja:preço:código"
      ──parse────▶ Quote(loja, preço, código)
      ──applyDiscount──▶ String "loja preço com desconto"
```

### Versão síncrona (lenta)

```java
stores.stream()
      .map(service::getPriceSync)          // 1 s por loja
      .map(Quote::new)
      .map(Discount::applyDiscount)        // 1 s por loja
      .collect(Collectors.toList());
```

Com 4 lojas: 4 × (1 + 1) = **8 s**.

---

## 2. Versão assíncrona encadeada

Pipeline de **três etapas**, cada uma ligada à anterior **sem bloquear**:

```java
List<CompletableFuture<String>> futures = stores.stream()
        // etapa 1: busca o preço de forma assíncrona
        .map(store -> CompletableFuture.supplyAsync(() -> service.getPriceSync(store)))
        // etapa 2: transforma texto em objeto (rápido, síncrono) → thenApply
        .map(future -> future.thenApply(Quote::new))
        // etapa 3: aplica o desconto (lento!) em outro future → thenCompose
        .map(future -> future.thenCompose(quote ->
                CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote))))
        .collect(Collectors.toList());                        // dispara tudo

List<String> resultados = futures.stream()
        .map(CompletableFuture::join)                          // espera tudo
        .collect(Collectors.toList());
```

Tempo observado: **≈ 2 segundos** (em vez de 8). Cada loja tem 1 s de preço + 1 s de desconto, mas **as quatro lojas rodam em paralelo**.

---

## 3. `thenApply` × `thenCompose`

Esta é a pergunta mais importante da aula:

| | `thenApply` | `thenCompose` |
|---|---|---|
| Função recebida | `T → R` (devolve um valor comum) | `T → CompletableFuture<R>` (devolve outro future) |
| Analogia com Streams | `map` | `flatMap` |
| Quando usar | a etapa é **rápida/síncrona** | a etapa é **assíncrona/lenta** (devolve um `CompletableFuture`) |
| Resultado | `CompletableFuture<R>` | `CompletableFuture<R>` (achatado, não `CF<CF<R>>`) |

Se você usasse `thenApply` com uma função que devolve `CompletableFuture`, obteria `CompletableFuture<CompletableFuture<String>>` — o mesmo problema que o `map` com `flatMap` nos streams.

> Observação do instrutor: a etapa 2 (`Quote::new`) é só conversão de texto em objeto, por isso `thenApply` e **síncrona**; não faz sentido torná-la assíncrona. Já o desconto é uma chamada lenta a "um servidor externo", então vai por `thenCompose` com `supplyAsync`.

---

## 4. Quem executa cada etapa?

A cada loja, três momentos acontecem possivelmente em threads diferentes:

```text
[loja] supplyAsync(getPrice)  ──thread A──▶ resultado
       thenApply(Quote::new)  ──(mesma thread que terminou a etapa anterior)──▶
       thenCompose(supplyAsync(applyDiscount)) ──thread B──▶ resultado final
```

Você não controla qual thread executa; o framework faz o melhor aproveitamento.

## 5. Composição de futures independentes: `thenCombine`

Quando duas consultas são **independentes** e você quer combinar seus resultados:

```java
CompletableFuture<Double> precoEmDolar = CompletableFuture.supplyAsync(() -> service.getPriceSync("Store 1"));
CompletableFuture<Double> cotacao       = CompletableFuture.supplyAsync(() -> cambio.getRate("USD", "BRL"));

CompletableFuture<Double> precoEmReal = precoEmDolar.thenCombine(cotacao, (preco, taxa) -> preco * taxa);
```

As duas rodam **em paralelo** e a combinação ocorre quando ambas terminam.

## O que você precisa dominar (Aula 244)

- Montar pipeline `supplyAsync` → `thenApply` → `thenCompose`.
- Diferença entre `thenApply` e `thenCompose` (map × flatMap).
- Dispare tudo primeiro e `join` depois.
- Tempo total ≈ caminho mais longo, não a soma.
- `thenCombine` para resultados independentes.

---

# Aula 245 — CompletableFuture pt 06 — `allOf` e `anyOf`

## 1. Tempos de resposta variáveis

No mundo real cada loja responde em um tempo diferente. Para simular:

```java
private void randomDelay() {
    int delay = ThreadLocalRandom.current().nextInt(500, 2500);
    try { Thread.sleep(delay); } catch (InterruptedException e) { throw new RuntimeException(e); }
}
```

Sem organização, você quer **reagir assim que cada loja responder**, em vez de esperar todas para imprimir.

---

## 2. Reagindo a cada resposta: `thenAccept`

Em vez de coletar e depois imprimir, registre uma **ação** para cada future:

```java
long start = System.currentTimeMillis();

CompletableFuture<?>[] futures = stores.stream()
        .map(store -> CompletableFuture.supplyAsync(() -> service.getPriceSync(store)))
        .map(f -> f.thenApply(Quote::new))
        .map(f -> f.thenCompose(q -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(q))))
        .map(f -> f.thenAccept(resultado ->
                System.out.printf("%s (terminou em %d ms)%n", resultado, System.currentTimeMillis() - start)))
        .toArray(CompletableFuture[]::new);
```

Cada resposta é impressa **assim que fica pronta** — a loja mais rápida primeiro.

Observação: `thenAccept` devolve `CompletableFuture<Void>` (não há valor). Por isso o array é de `CompletableFuture<?>`/`CompletableFuture<Void>`.

---

## 3. `CompletableFuture.allOf(...)` — esperar **todas**

```java
CompletableFuture.allOf(futures).join();           // bloqueia até TODAS terminarem
System.out.println("Todas as lojas responderam em " + (System.currentTimeMillis() - start) + " ms");
```

- Aceita **varargs/array** (não aceita `List` diretamente → `toArray(CompletableFuture[]::new)`).
- Devolve `CompletableFuture<Void>` que completa quando **todas** completarem.
- Sem o `join`, o `main` seguiria em frente e o programa "acabaria" antes dos resultados.

Útil para: "só continue quando todos os cálculos terminarem", relatórios agregados.

---

## 4. `CompletableFuture.anyOf(...)` — esperar **a primeira**

```java
CompletableFuture.anyOf(futures).join();           // completa quando QUALQUER uma terminar
System.out.println("A primeira loja respondeu!");
```

- Devolve `CompletableFuture<Object>` com o resultado da **primeira** que terminar.
- As demais **continuam executando** (não são canceladas automaticamente).
- Útil quando você consulta **várias fontes redundantes** (por exemplo 5 provedores de cotação) e usa a resposta mais rápida.

## 5. Comparação

| | `allOf` | `anyOf` |
|---|---|---|
| Completa quando | **todas** terminam | **a primeira** termina |
| Resultado | `Void` (colete dos futures originais) | `Object` (valor da vencedora) |
| Uso típico | agregar resultados | resposta mais rápida entre redundantes |

Se uma delas falhar com exceção, `allOf` completa excepcionalmente; `anyOf` também, se a primeira que terminar tiver falhado.

## O que você precisa dominar (Aula 245)

- Reagir a cada resultado com `thenAccept`.
- `allOf(...).join()` e `anyOf(...).join()`.
- Converter lista em array (`toArray(CompletableFuture[]::new)`).
- Diferença entre `allOf` e `anyOf`.

---

# Mapa mental do bloco

```text
Executor framework
├── ExecutorService
│     ├── newFixedThreadPool / newSingleThreadExecutor / newCachedThreadPool
│     ├── execute(Runnable)  ·  submit(Runnable|Callable) → Future
│     └── shutdown → awaitTermination → shutdownNow
├── ScheduledExecutorService
│     └── schedule · scheduleAtFixedRate (início→início) · scheduleWithFixedDelay (fim→início)
├── Callable<V> (com retorno e exceção)  +  Future<V> (get, get(timeout), cancel, isDone)
└── CompletableFuture<T>
      ├── Criar:      supplyAsync / runAsync (+ Executor)
      ├── Esperar:    join() (unchecked)  ·  get() (checked)
      ├── Encadear:   thenApply (map) · thenCompose (flatMap) · thenAccept · thenCombine
      ├── Erros:      exceptionally · handle · whenComplete
      ├── Combinar:   allOf · anyOf
      └── Cuidado:    em stream, dispare tudo (collect) ANTES de join
```

# Cola de bolso

| Quero... | Use |
|---|---|
| Rodar N tarefas com no máximo K threads | `Executors.newFixedThreadPool(K)` |
| Tarefa com resultado | `Callable` + `submit` + `Future.get(timeout)` |
| Repetir a cada 5 s | `scheduleAtFixedRate(..., 5, SECONDS)` |
| Esperar após terminar a anterior | `scheduleWithFixedDelay` |
| Chamada assíncrona sem gerenciar executor | `CompletableFuture.supplyAsync(...)` |
| Transformar resultado | `thenApply` |
| Encadear outra chamada assíncrona | `thenCompose` |
| Combinar dois resultados independentes | `thenCombine` |
| Esperar todas | `CompletableFuture.allOf(...).join()` |
| Pegar a mais rápida | `CompletableFuture.anyOf(...).join()` |
| Tratar erro | `.exceptionally(ex -> padrao)` |
