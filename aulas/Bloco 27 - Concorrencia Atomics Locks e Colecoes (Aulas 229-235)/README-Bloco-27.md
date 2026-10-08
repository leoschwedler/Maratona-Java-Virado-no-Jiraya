# Bloco 27 — Concorrência: Atomics, Locks e Coleções Concorrentes

## Aulas 229 a 235

No bloco anterior você trabalhou com threads "à moda antiga" (`Thread`, `synchronized`, `wait/notify`). Isso é o **nível básico**. A partir do Java 5 existe o pacote **`java.util.concurrent`**, que adiciona uma **camada de abstração** para facilitar o trabalho com concorrência — mais seguro, mais expressivo e geralmente mais eficiente.

> Importante (aviso do instrutor): o pacote `java.util.concurrent` **não dispensa** o conhecimento de threads. Você precisa entender bem sincronização, lock, deadlock, `wait/notify` para usar tudo isso bem.

As aulas deste bloco são:

```text
229 — Concorrência pt 01 — AtomicInteger
230 — Concorrência pt 02 — Lock e ReentrantLock
231 — Concorrência pt 03 — Conditions
232 — Concorrência pt 04 — ReentrantReadWriteLock
233 — Concorrência pt 05 — CopyOnWriteArrayList
234 — Concorrência pt 06 — ArrayBlockingQueue
235 — Concorrência pt 07 — LinkedTransferQueue
```

Mapa do pacote que você vai explorar:

```text
java.util.concurrent
├── atomic     → AtomicInteger, AtomicLong, AtomicBoolean, AtomicReference...  (contadores sem lock)
├── locks      → Lock, ReentrantLock, Condition, ReadWriteLock                  (locks explícitos)
├── Coleções   → CopyOnWriteArrayList, ConcurrentHashMap, BlockingQueue...
└── Executores → ExecutorService, Future, CompletableFuture...                  (próximo bloco)
```

---

# Aula 229 — Concorrência pt 01 — `AtomicInteger`

## 1. O problema do contador

Duas threads incrementando o mesmo contador:

```java
class Counter {
    private int count;
    void increment() { count++; }          // 😱 parece 1 instrução, mas são 3
    int getCount() { return count; }
}

Counter counter = new Counter();
Thread t1 = new Thread(() -> { for (int i = 0; i < 10_000; i++) counter.increment(); });
Thread t2 = new Thread(() -> { for (int i = 0; i < 10_000; i++) counter.increment(); });
t1.start(); t2.start();
t1.join();  t2.join();
System.out.println(counter.getCount());     // esperado: 20000 — obtido: 17432, 18911, ... (varia!)
```

Por quê? `count++` em nível de máquina é **ler → somar → escrever**. Duas threads podem ler o mesmo valor, ambas somar 1 e escrever, perdendo um incremento (**race condition**).

## 2. Solução 1 (a clássica): sincronizar

```java
synchronized void increment() { count++; }
```

Funciona, mas só uma thread entra por vez: **perde desempenho** e usa um mecanismo de baixo nível (bloqueio do sistema).

## 3. Solução 2: variável atômica

```java
import java.util.concurrent.atomic.AtomicInteger;

class Counter {
    private final AtomicInteger count = new AtomicInteger();

    void increment() { count.incrementAndGet(); }     // atômico e sem lock!
    int getCount()   { return count.get(); }
}
```

Resultado: sempre **20000**.

### Como funciona? CAS (*Compare-And-Swap*)

Em vez de travar, a operação faz:

```text
1) lê o valor atual (esperado)
2) calcula o novo valor
3) "compare-and-swap": se o valor na memória ainda é o esperado, troca pelo novo (tudo numa só instrução do processador);
   se mudou (outra thread alterou), tenta de novo
```

É uma técnica **lock-free** (sem lock): nenhuma thread bloqueia; no máximo repete a tentativa. Muito rápida quando a disputa é pequena ou moderada.

## 4. Métodos mais usados

| Método | Faz | Retorna |
|---|---|---|
| `get()` | lê | valor atual |
| `set(x)` | escreve | – |
| `incrementAndGet()` | `++i` | novo valor |
| `getAndIncrement()` | `i++` | valor antigo |
| `decrementAndGet()` / `getAndDecrement()` | `--i` / `i--` | |
| `addAndGet(n)` / `getAndAdd(n)` | `+= n` | |
| `compareAndSet(esperado, novo)` | CAS manual | `boolean` |
| `updateAndGet(f)` / `accumulateAndGet(x, f)` | aplica função | |

## 5. Família Atomic

`AtomicInteger`, `AtomicLong`, `AtomicBoolean`, `AtomicReference<T>`, `AtomicIntegerArray`, `LongAdder` (melhor sob alta disputa).

## 6. Limitação

Um atômico garante **uma operação** atômica. Se sua lógica envolve **várias variáveis** ou "ler e depois decidir" (invariantes compostas), ainda é preciso `synchronized`/`Lock`.

## O que você precisa dominar (Aula 229)

- Por que `count++` não é atômico.
- `AtomicInteger` e seus métodos.
- A ideia do CAS e de "lock-free".
- A família de atômicos.
- Limite: operações compostas.

---

# Aula 230 — Concorrência pt 02 — `Lock` e `ReentrantLock`

## 1. `synchronized` x `Lock`

Duas maneiras de proteger código. O `Lock` (interface) e sua implementação principal `ReentrantLock` (pacote `java.util.concurrent.locks`) dão **mais controle**.

| | `synchronized` | `ReentrantLock` |
|---|---|---|
| Liberação | automática ao sair do bloco | **manual** (`unlock()`) |
| Pode tentar sem bloquear? | não | **sim** (`tryLock()`) |
| Tempo limite | não | **sim** (`tryLock(tempo, unidade)`) |
| Pode ser interrompido enquanto espera? | não | **sim** (`lockInterruptibly()`) |
| Justiça (*fairness*) | não | **opcional** (`new ReentrantLock(true)`) |
| Várias condições | 1 (monitor) | **várias** (`newCondition()`) |
| Consulta de estado | limitada | `isLocked()`, `isHeldByCurrentThread()`, `getQueueLength()`... |
| Legibilidade | simples | mais verbosa |

> Opinião do instrutor: **prefira `synchronized` quando ele resolver**; use `Lock` só quando precisar das vantagens extras. Código mais simples é melhor.

---

## 2. A regra de ouro: `unlock()` no `finally`

Como a liberação é manual, **sempre** use `try/finally`:

```java
private final Lock lock = new ReentrantLock();

public void metodo() {
    lock.lock();               // adquire (bloqueia se outro tiver)
    try {
        // seção crítica
    } finally {
        lock.unlock();         // SEMPRE executa, mesmo com exceção
    }
}
```

Se uma exceção ocorrer e você não liberar o lock, ele fica "preso" para sempre e as outras threads travam.

---

## 3. "Reentrante"

A mesma thread pode adquirir o **mesmo** lock várias vezes (um contador interno acompanha); precisa chamar `unlock()` o mesmo número de vezes. Isso evita que uma thread trave a si mesma ao chamar um método sincronizado de dentro de outro.

---

## 4. `Fairness` (justiça)

```java
Lock justo = new ReentrantLock(true);
```

Com `true`, o lock é entregue à thread que **espera há mais tempo** (ordem FIFO). Sem isso (padrão `false`), qualquer thread pode "furar a fila" (melhor desempenho, risco de inanição). Mesmo no modo justo o agendador da JVM pode influenciar; a ordem de execução geral continua sem garantias.

---

## 5. Exemplo da aula: um `Worker`

```java
public class Worker implements Runnable {
    private final String name;
    private final ReentrantLock lock;

    public Worker(String name, ReentrantLock lock) {
        this.name = name;
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            if (lock.isHeldByCurrentThread()) {
                System.out.printf("Thread %s entrou em uma seção crítica%n", name);
            }
            System.out.printf("%d threads esperando na fila%n", lock.getQueueLength());
            System.out.printf("Thread %s vai esperar 2 segundos%n", name);
            Thread.sleep(2000);
            System.out.printf("Thread %s finalizou a espera%n", name);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
```

Disparando 7 threads (A a G) com o **mesmo** lock:

```java
ReentrantLock lock = new ReentrantLock();      // ou new ReentrantLock(true) para justo
for (String n : new String[]{"A","B","C","D","E","F","G"}) {
    new Thread(new Worker(n, lock)).start();
}
```

Observação: a primeira thread entra; as outras ficam na fila (`getQueueLength()` mostra quantas). A cada 2 segundos uma entra. Com `fairness`, tendem a seguir a ordem de chegada — mas **não é garantia absoluta**.

### `printf` em 30 segundos

```text
%s → String      %d → inteiro      %f → decimal      %n → quebra de linha (independe do SO)
System.out.printf("Olá %s, você tem %d anos%n", nome, idade);
```

---

## 6. `tryLock()` — tentar sem ficar preso

```java
if (lock.tryLock()) {                       // tenta; não bloqueia
    try { /* seção crítica */ }
    finally { lock.unlock(); }
} else {
    System.out.println("Recurso ocupado, fazendo outra coisa...");
}

if (lock.tryLock(2, TimeUnit.SECONDS)) { ... }   // espera até 2 s
```

⚠️ **Nunca** chame `unlock()` sem ter o lock: lança `IllegalMonitorStateException`. Por isso o `unlock` só vai dentro do `if` bem-sucedido do `tryLock`.

## O que você precisa dominar (Aula 230)

- `Lock`/`ReentrantLock` x `synchronized`.
- `lock()` + `try/finally` + `unlock()`.
- Reentrância, fairness.
- `tryLock`, `isHeldByCurrentThread`, `getQueueLength`.
- Quando vale a pena usar `Lock`.

---

# Aula 231 — Concorrência pt 03 — `Condition`

## 1. `Condition` = `wait/notify` para `Lock`

Quando você usa `Lock`, o `wait/notify/notifyAll` de `Object` **não** é adequado. A substituição é a interface **`Condition`**, obtida de um lock:

```java
Lock lock = new ReentrantLock();
Condition condicao = lock.newCondition();
```

| `synchronized` + `Object` | `Lock` + `Condition` |
|---|---|
| `obj.wait()` | `condicao.await()` |
| `obj.notify()` | `condicao.signal()` |
| `obj.notifyAll()` | `condicao.signalAll()` |

Regras iguais às de `wait/notify`: só podem ser chamados **com o lock adquirido** (senão `IllegalMonitorStateException`), `await()` **libera o lock** enquanto espera e **readquire** ao acordar, e deve ficar em um `while`.

---

## 2. Vantagem: várias condições por lock

Com `synchronized` há **uma** fila de espera por objeto. Com `Lock` você cria quantas `Condition` quiser, por exemplo `naoVazio` e `naoCheio` em um buffer — acorda-se apenas quem interessa.

---

## 3. Reescrevendo o exemplo dos e-mails (aula 228) com `Lock` + `Condition`

```java
public class Members {
    private final Queue<String> emails = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition temEmail = lock.newCondition();
    private boolean open = true;

    public int pendingEmails() {
        lock.lock();
        try { return emails.size(); } finally { lock.unlock(); }
    }

    public boolean isOpen() { return open; }          // (para rigor, usar volatile ou lock)

    public void addMemberEmail(String email) {
        lock.lock();
        try {
            emails.add(email);
            temEmail.signalAll();                      // era notifyAll()
        } finally {
            lock.unlock();
        }
    }

    public String retrieveEmail() {
        lock.lock();
        try {
            while (emails.isEmpty()) {
                if (!open) return null;
                System.out.println(Thread.currentThread().getName() + ": sem e-mails, esperando");
                temEmail.await();                      // era wait()
            }
            return emails.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        lock.lock();
        try {
            open = false;
            temEmail.signalAll();                      // acorda todos para perceberem
        } finally {
            lock.unlock();
        }
    }
}
```

O comportamento é **idêntico** ao da aula 228, mas o código é mais **verboso** (muitos `try/finally`). O instrutor conclui: "não sei se vale muito a pena neste caso; o `synchronized` resolve e o código fica mais limpo. Faça benchmark se desempenho for crítico."

### Bug que o instrutor encontrou na aula

Ao encerrar, as threads consumidoras continuavam esperando porque o método `close()` não **sinalizava** (`signalAll`) as threads em espera — elas ficavam eternamente em `await()`. A lição: **todo encerramento precisa acordar quem espera**.

## 4. Bônus: `lock.tryLock` + condição

A aula também reforça que chamar `unlock()` numa thread que não tem o lock lança `IllegalMonitorStateException` — por isso o padrão `if (lock.tryLock()) { try { ... } finally { lock.unlock(); } }` e o uso de `isHeldByCurrentThread()` quando necessário.

## O que você precisa dominar (Aula 231)

- `Condition`: `await`, `signal`, `signalAll`.
- Equivalência com `wait/notify`.
- Várias condições para um lock.
- Sempre sinalizar no encerramento.
- Quando a verbosidade compensa.

---

# Aula 232 — Concorrência pt 04 — `ReentrantReadWriteLock`

## 1. A ideia: leitores em paralelo, escritor sozinho

Imagine uma estrutura lida **muitas vezes** e alterada **raramente** (cache, configuração, tabela de preços). Um lock exclusivo faria até os leitores esperarem uns pelos outros sem necessidade.

`ReadWriteLock` mantém **um par de locks**:

| Lock | Quem pode ter ao mesmo tempo |
|---|---|
| **Leitura** (`readLock()`) | **várias** threads simultaneamente |
| **Escrita** (`writeLock()`) | **só uma**, e **nenhum leitor** ao mesmo tempo |

Regras:

- Enquanto há escritor, ninguém lê nem escreve.
- Enquanto há leitores, o escritor espera.
- Vários leitores não se bloqueiam.

```text
leitores:  L1 ✅  L2 ✅  L3 ✅      (juntos)
escritor:  W  🔒                      (exclusivo — todos esperam)
```

---

## 2. Exemplo da aula: um mapa protegido

```java
public class MapWithLock {
    private final Map<String, String> internal = new LinkedHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void put(String key, String value) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " obteve o write lock");
            Thread.sleep(1000);                 // simula processamento
            internal.put(key, value);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock();
        }
    }

    public Set<String> allKeys() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " obteve o read lock");
            return new HashSet<>(internal.keySet());      // cópia para não vazar a coleção interna
        } finally {
            readLock.unlock();
        }
    }
}
```

Cenário de teste (1 escritora e 2 leitoras):

```java
MapWithLock map = new MapWithLock();

Thread writer = new Thread(() -> {
    for (int i = 0; i < 20; i++) map.put(String.valueOf(i), String.valueOf(i));
}, "Writer");

Runnable lerTudo = () -> System.out.println(Thread.currentThread().getName() + ": " + map.allKeys());
Thread reader1 = new Thread(lerTudo, "Reader-1");
Thread reader2 = new Thread(lerTudo, "Reader-2");

writer.start(); reader1.start(); reader2.start();
```

Saída observada:

1. A escritora pega o write lock e escreve.
2. As leitoras tentam; ficam **bloqueadas** até a escritora soltar.
3. Quando soltar, **as duas leitoras entram juntas** (veem o mesmo read lock) e imprimem.

Detalhes:

- É possível **adquirir o read lock enquanto se tem o write lock** (degradar), mas o contrário (promover de leitura para escrita) causa deadlock.
- O instrutor recomenda sempre `finally` para liberar.
- `lock.isWriteLocked()`, `getReadLockCount()` ajudam em depuração.

## 3. Quando usar

| Cenário | Melhor opção |
|---|---|
| muita leitura, pouca escrita | `ReentrantReadWriteLock` |
| leitura e escrita equilibradas | `ReentrantLock`/`synchronized` (RW tem custo extra) |
| mapa concorrente de uso geral | `ConcurrentHashMap` |

## O que você precisa dominar (Aula 232)

- Diferença entre read lock e write lock.
- Vários leitores simultâneos; escritor exclusivo.
- Estrutura com `readLock()`/`writeLock()`.
- Quando vale a pena.

---

# Aula 233 — Concorrência pt 05 — `CopyOnWriteArrayList`

## 1. Imutabilidade e segurança

Um objeto **imutável** nunca muda depois de criado — logo, é naturalmente seguro para várias threads. Para criar uma classe imutável:

- classe `final`;
- atributos `private final`;
- sem setters;
- construtor define tudo; "alterar" = criar um novo objeto.

## 2. A ideia do `CopyOnWriteArrayList` (COW)

Uma `List` thread-safe que, a cada **modificação** (`add`, `remove`, `set`), **copia o array interno inteiro**, aplica a mudança na cópia e troca a referência.

```text
lista:   [1, 2]
add(3):  copia [1,2] → [1,2,3] e passa a apontar para a cópia nova
remove:  copia [1,2,3] → [1,2] ...
```

Os **leitores** continuam lendo a versão antiga enquanto a nova é montada → **leituras sem lock** e sem `ConcurrentModificationException`.

### Característica-chave: o *snapshot* do `Iterator`

O iterador enxerga uma **fotografia** da lista no momento em que foi criado e **nunca** é afetado por alterações posteriores:

```java
List<Integer> lista = new CopyOnWriteArrayList<>();
for (int i = 0; i < 2000; i++) lista.add(i);

Thread leitora = new Thread(() -> {
    Iterator<Integer> it = lista.iterator();     // snapshot com 2000 elementos
    dormir(2000);
    it.forEachRemaining(n -> { /* ... */ });     // ainda vê todos os 2000
}, "Leitora");

Thread removedora = new Thread(() -> {
    dormir(500);
    for (int i = 0; i < 500; i++) lista.remove(0);   // remove 500 enquanto a leitora dorme
}, "Removedora");
```

Resultado: a `Leitora` ainda imprime os 2000 originais, enquanto a lista real já tem 1500. Nenhuma exceção.

- Compare com `ArrayList`: modificar durante iteração → `ConcurrentModificationException`.
- O iterador do COW **não suporta** `remove()` (lança `UnsupportedOperationException`).

## 3. Quando usar (e quando não)

| ✅ Boa escolha | ❌ Má escolha |
|---|---|
| muita leitura e iteração, poucas escritas | muitas escritas (cada uma copia tudo — custo `O(n)`) |
| listas de ouvintes/observadores (*listeners*) | listas grandes que mudam o tempo todo |
| configurações lidas com frequência | |

Também existe `CopyOnWriteArraySet`.

## 4. Cuidado

A **lista** é segura, mas os **elementos** guardados nela, se forem mutáveis, continuam precisando de proteção. Prefira guardar objetos imutáveis.

## O que você precisa dominar (Aula 233)

- Conceito de imutabilidade e classe imutável.
- COW: copia a cada escrita, leitura sem lock.
- Iterador como *snapshot*.
- Custo de escrita e quando usar.

---

# Aula 234 — Concorrência pt 06 — `ArrayBlockingQueue`

## 1. `BlockingQueue`

Uma **fila bloqueante**: a interface `BlockingQueue<E>` (em `java.util.concurrent`) acrescenta operações que **esperam** quando necessário.

- Se a fila está **cheia**, quem tenta **colocar** espera por espaço.
- Se a fila está **vazia**, quem tenta **retirar** espera por um elemento.

É a implementação pronta do padrão **produtor-consumidor** — aquilo que você fez à mão com `wait/notify`!

## 2. `ArrayBlockingQueue`

Fila **limitada** (capacidade fixa) apoiada em array:

```java
BlockingQueue<String> fila = new ArrayBlockingQueue<>(1);     // capacidade 1
```

## 3. Operações

| Operação | Fila cheia/vazia | Observação |
|---|---|---|
| `put(e)` | **bloqueia** até haver espaço | lança `InterruptedException` |
| `take()` | **bloqueia** até haver elemento | idem |
| `offer(e)` | devolve `false` imediatamente | não bloqueia |
| `offer(e, tempo, unidade)` | espera até o tempo limite | |
| `poll()` | devolve `null` | não bloqueia |
| `poll(tempo, unidade)` | espera até o tempo limite | |
| `add(e)` | lança `IllegalStateException` se cheia | |
| `peek()` | espia a cabeça sem remover | |

---

## 4. Demonstração da aula

```java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

Thread produtora = new Thread(() -> {
    try {
        queue.put("William");
        System.out.println("Produtora colocou William");
        queue.put("DevDojo");                           // ⬅ fila cheia (capacidade 1): BLOQUEIA
        System.out.println("Produtora colocou DevDojo");
    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
}, "Produtora");

Thread consumidora = new Thread(() -> {
    try {
        Thread.sleep(5000);                             // atrasa de propósito
        System.out.println("Consumidora retirou " + queue.take());   // libera espaço
        System.out.println("Consumidora retirou " + queue.take());
    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
}, "Consumidora");

produtora.start();
consumidora.start();
```

Saída:

```text
Produtora colocou William
(... 5 segundos esperando, a Produtora está BLOQUEADA no 2º put ...)
Consumidora retirou William
Produtora colocou DevDojo
Consumidora retirou DevDojo
```

Se a fila está **vazia**, `take()` espera até alguém colocar.

## 5. Reescrevendo o e-mail com `BlockingQueue`

O `Members` do bloco anterior fica trivial: trocar tudo por uma `BlockingQueue<String>`; consumidores fazem `take()` e o encerramento usa uma **"pílula de veneno"** (um valor especial na fila) para acordar e finalizar cada consumidor.

```java
String POISON = "__FIM__";
fila.put(POISON);       // um por consumidor, para terminarem
```

## O que você precisa dominar (Aula 234)

- `BlockingQueue` = produtor-consumidor pronto.
- `put`/`take` bloqueiam; `offer`/`poll` não (ou com tempo).
- `ArrayBlockingQueue` tem capacidade limitada.
- Pílula de veneno para encerrar consumidores.

---

# Aula 235 — Concorrência pt 07 — `LinkedTransferQueue`

## 1. Uma fila "tudo em um"

`LinkedTransferQueue` (Java 7) combina:

- `ConcurrentLinkedQueue` (não bloqueante, ilimitada);
- `LinkedBlockingQueue` (bloqueante);
- `SynchronousQueue` (entrega direta produtor→consumidor).

Implementa `TransferQueue<E>`, que estende `BlockingQueue<E>`. É **ilimitada** (capacidade praticamente infinita), então `put` quase nunca bloqueia.

> Dica do instrutor: com coleções concorrentes, **prefira as versões mais novas** — a cada versão do Java elas ficam melhores.

---

## 2. Operações herdadas (não bloqueantes e bloqueantes)

| Operação | Comportamento |
|---|---|
| `add(e)` | insere; lança `IllegalStateException` se não houver espaço (em fila ilimitada nunca ocorre) |
| `offer(e)` | insere; devolve `true`/`false`, sem bloquear |
| `offer(e, tempo, unidade)` | espera até o limite |
| `put(e)` | insere; bloqueia se necessário |
| `take()` | retira a cabeça, **bloqueia** se vazia |
| `poll()` | retira a cabeça ou devolve `null` |
| `peek()` | espia a cabeça (ou `null`) |
| `element()` | espia a cabeça; lança exceção se vazia |
| `remove()` | retira a cabeça; lança exceção se vazia |

---

## 3. O que há de novo: a transferência (*transfer*)

```java
TransferQueue<String> fila = new LinkedTransferQueue<>();

fila.transfer("mensagem");
```

`transfer(e)` **bloqueia o produtor até um consumidor receber o elemento**. É uma entrega **síncrona** ("handoff"): o produtor só continua depois que alguém realmente pegou a mensagem.

| Método | Comportamento |
|---|---|
| `transfer(e)` | bloqueia até um consumidor receber |
| `tryTransfer(e)` | tenta entregar **imediatamente** a um consumidor já esperando; se não houver, devolve `false` **e não enfileira** |
| `tryTransfer(e, tempo, unidade)` | espera até o limite por um consumidor; se não aparecer, `false` e não enfileira |
| `hasWaitingConsumer()` | há consumidor esperando? |
| `getWaitingConsumerCount()` | quantos? |

Diferença para `put`: `put` coloca o item na fila e **continua**; `transfer` **espera a entrega**.

Exemplo:

```java
boolean entregue = fila.tryTransfer("oi");            // sem consumidor: false
System.out.println(entregue);                          // false

fila.tryTransfer("oi", 5, TimeUnit.SECONDS);           // espera até 5 s por um consumidor
```

## 4. Quando usar

Sistemas de mensagens em que o produtor precisa de **confirmação de recebimento** (*backpressure*), ou quando você quer a flexibilidade de uma única classe para todos os estilos.

## 5. Resumo das filas do bloco

| Fila | Limitada? | Bloqueia? | Destaque |
|---|---|---|---|
| `ArrayBlockingQueue` | sim | sim | simples, capacidade fixa |
| `LinkedBlockingQueue` | opcional | sim | boa vazão |
| `ConcurrentLinkedQueue` | não | não | sem lock |
| `PriorityBlockingQueue` | não | sim (take) | por prioridade |
| `LinkedTransferQueue` | não | sim | `transfer` (entrega síncrona) |

## O que você precisa dominar (Aula 235)

- O que `LinkedTransferQueue` combina.
- Todas as operações de inserção/remoção e a diferença entre as que lançam exceção, devolvem especial ou bloqueiam.
- `transfer`, `tryTransfer`, `hasWaitingConsumer`.
- `transfer` x `put`.

---

# Mapa mental do bloco

```text
java.util.concurrent
├── Atomics:       AtomicInteger/Long/Boolean/Reference  → CAS, sem lock
├── Locks:         Lock / ReentrantLock
│    ├── lock() · unlock() no finally · tryLock() · lockInterruptibly() · fairness
│    ├── Condition: await / signal / signalAll   (≈ wait/notify)
│    └── ReentrantReadWriteLock: muitos leitores OU um escritor
└── Coleções:
     ├── CopyOnWriteArrayList  → copia a cada escrita; iterador = snapshot
     └── BlockingQueue
          ├── ArrayBlockingQueue    (limitada)
          └── LinkedTransferQueue   (transfer: entrega síncrona)
```

# Cola de bolso

| Preciso... | Use |
|---|---|
| Contador compartilhado | `AtomicInteger.incrementAndGet()` |
| Exclusão mútua simples | `synchronized` |
| Tentar pegar o lock sem travar | `ReentrantLock.tryLock()` |
| Várias filas de espera num lock | `Condition` |
| Muitas leituras, poucas escritas | `ReentrantReadWriteLock` / `CopyOnWriteArrayList` |
| Produtor-consumidor | `BlockingQueue` (`put`/`take`) |
| Entrega com confirmação | `TransferQueue.transfer` |
