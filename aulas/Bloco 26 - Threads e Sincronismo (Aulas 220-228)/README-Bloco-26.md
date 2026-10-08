# Bloco 26 — Threads e Sincronismo

## Aulas 220 a 228

Chegou a parte avançada do curso: **programação concorrente**. Até aqui todo o seu código executava **uma instrução depois da outra**, em uma única linha de execução. Agora você aprende a executar **várias coisas ao mesmo tempo** — e também os **problemas** que isso traz (condição de corrida, deadlock) e as **ferramentas** para resolvê-los.

As aulas deste bloco são:

```text
220 — Threads pt 01 — Introdução
221 — Threads pt 02 — Estados das threads    (⚠ transcrição inutilizável)
222 — Threads pt 03 — Priority e Sleep
223 — Threads pt 04 — Yield e Join
224 — Threads pt 05 — Sincronismo pt 01 (o problema)
225 — Threads pt 06 — Sincronismo pt 02 (synchronized)
226 — Threads pt 07 — Sincronismo pt 03 (classes thread-safe)
227 — Threads pt 08 — Sincronismo pt 04 (deadlock)
228 — Threads pt 09 — Sincronismo pt 05 (wait, notify, notifyAll)
```

> ⚠️ **Aviso sobre a aula 221:** o arquivo de transcrição desta aula contém áudio em outro idioma (romeno) e **não tem conteúdo aproveitável**. A seção abaixo sobre os estados da thread foi escrita com base nas aulas vizinhas (222 e 223, que citam "runnable", "bloqueado", "espera") e na API do Java.

---

# Aula 220 — Threads pt 01 — Introdução

## 1. O que é uma thread?

A palavra "thread" tem **dois sentidos**:

1. **Objeto**: uma instância da classe `java.lang.Thread`.
2. **Linha de execução**: um "fio" de execução dentro de um processo.

Um **processo** (seu programa Java) pode ter **várias threads**, cada uma executando uma parte do trabalho "ao mesmo tempo". Quem decide **quando e por quanto tempo** cada thread executa é o **escalonador** (*scheduler*) da JVM + sistema operacional. Você não controla isso — pode apenas dar sugestões.

> Quase nada em threads é garantido. A ordem de execução, o tempo de cada uma e quando terminam são decididos pela JVM.

### Relação com o hardware

- Um processador tem **núcleos (cores)**; cada núcleo pode ter **processadores lógicos** (*hyper-threading*), em geral o dobro.
- Ex.: 4 núcleos → 8 threads simultâneas em hardware.
- Mesmo com mais threads Java do que processadores, o SO alterna entre elas rapidamente (*time slicing*).

---

## 2. Tipos de thread

| Tipo | Descrição |
|---|---|
| **User thread** (padrão) | a JVM só encerra quando **todas** terminam |
| **Daemon thread** | serviço de segundo plano (ex.: *garbage collector*); a JVM **encerra** quando só restam daemons |

```java
Thread t = new Thread(tarefa);
t.setDaemon(true);   // antes de start()
```

---

## 3. A thread `main`

Todo programa começa com uma thread chamada `main`:

```java
System.out.println(Thread.currentThread().getName());   // main
```

---

## 4. Criando threads — forma 1: estender `Thread`

```java
public class ThreadExample extends Thread {

    @Override
    public void run() {                     // o que a thread executa
        for (int i = 0; i < 500; i++) {
            System.out.print("A");
            if (i % 100 == 0) System.out.println();
        }
    }
}
```

Uso:

```java
ThreadExample t1 = new ThreadExample();
t1.run();      // ❌ NÃO cria thread: executa o método na thread atual (main)!
t1.start();    // ✅ pede à JVM para criar uma nova thread e executar o run() nela
```

**Diferença fundamental:**

| | `run()` | `start()` |
|---|---|---|
| Cria nova thread? | **não** | **sim** |
| Executa em | thread atual | nova thread |
| Pode chamar 2× | sim | **não** (`IllegalThreadStateException`) |

---

## 5. Forma 2 (recomendada): implementar `Runnable`

```java
public class RunnableExample implements Runnable {
    @Override
    public void run() {
        System.out.println("Executando em " + Thread.currentThread().getName());
    }
}

Thread t = new Thread(new RunnableExample());
t.start();
```

Por que é melhor?

- **Herança de classe é um recurso escasso**: se você estende `Thread`, não pode estender mais nada. Com `Runnable` você só implementa uma interface.
- Separa **a tarefa** (o que fazer) do **mecanismo** (como executar).
- `Runnable` é uma **interface funcional** → funciona com lambda:

```java
new Thread(() -> System.out.println("Olá de uma thread")).start();
```

---

## 6. A ordem é imprevisível

Com 5 threads imprimindo letras diferentes (`A`, `B`, `C`, `D`), a saída vem **misturada** e **diferente a cada execução**. Isso é normal: o escalonador alterna entre elas.

```text
AAAAAAAAAABBBBBBCCCAAAAAADDDDDBBBB...
```

Nunca escreva código que dependa da ordem de execução das threads.

## O que você precisa dominar (Aula 220)

- Thread como objeto x linha de execução; processo x thread.
- Escalonador: pouca coisa é garantida.
- User thread x daemon.
- `Thread.currentThread().getName()` e a thread `main`.
- `run()` x `start()`.
- `extends Thread` x `implements Runnable` (e por que o segundo é melhor).

---

# Aula 221 — Threads pt 02 — Estados das threads (⚠ sem transcrição)

> Conteúdo baseado na API e nos comentários das aulas 222–223.

## 1. Os estados (`Thread.State`)

```java
Thread.State estado = thread.getState();
```

| Estado | Significado |
|---|---|
| `NEW` | criada com `new Thread(...)`, mas `start()` ainda não foi chamado |
| `RUNNABLE` | pronta para executar **ou** executando (a JVM não distingue) |
| `BLOCKED` | esperando adquirir um **lock** (`synchronized`) |
| `WAITING` | esperando indefinidamente por outra thread (`wait()`, `join()`, `park()`) |
| `TIMED_WAITING` | esperando por tempo limitado (`sleep(ms)`, `wait(ms)`, `join(ms)`) |
| `TERMINATED` | terminou (`run()` acabou ou lançou exceção) |

## 2. Ciclo de vida

```text
          start()                scheduler
 NEW ───────────────▶ RUNNABLE ◀────────────▶ (executando)
                         │  ▲
        tenta lock       │  │ consegue lock / notificada / tempo acabou
        ocupado          ▼  │
                  BLOCKED / WAITING / TIMED_WAITING
                         │
        run() termina    ▼
                    TERMINATED
```

Pontos:

- Uma thread `TERMINATED` **não pode ser reiniciada**.
- `sleep` coloca a thread em `TIMED_WAITING`; ao fim do tempo ela volta para `RUNNABLE` — **não** executa imediatamente: precisa esperar o escalonador (aula 222).

```java
Thread t = new Thread(() -> { try { Thread.sleep(500); } catch (InterruptedException e) { } });
System.out.println(t.getState());   // NEW
t.start();
System.out.println(t.getState());   // RUNNABLE
Thread.sleep(100);
System.out.println(t.getState());   // TIMED_WAITING
t.join();
System.out.println(t.getState());   // TERMINATED
```

## O que você precisa dominar (Aula 221)

- Os 6 estados e as transições principais.
- Thread terminada não reinicia.
- O que leva uma thread a `BLOCKED`, `WAITING` e `TIMED_WAITING`.

---

# Aula 222 — Threads pt 03 — Priority e Sleep

## 1. Nome da thread

O construtor aceita um nome (ótimo para depurar):

```java
Thread t1 = new Thread(new RunnableExample(), "Thread-Kakashi");
t1.getName();
```

---

## 2. Prioridade

Toda thread tem uma prioridade de **1 a 10**:

```java
Thread.MIN_PRIORITY    // 1
Thread.NORM_PRIORITY   // 5 (padrão)
Thread.MAX_PRIORITY    // 10

t4.setPriority(Thread.MAX_PRIORITY);
```

⚠️ A prioridade é só uma **dica (hint)** para o escalonador. **Não há garantia** de que a thread de maior prioridade execute primeiro — depende da implementação da JVM e do sistema operacional. Por isso: **nunca construa lógica de negócio baseada em prioridade**.

---

## 3. `Thread.sleep(ms)`

Faz a **thread atual** "dormir" pelo tempo informado (em milissegundos):

```java
try {
    Thread.sleep(2000);       // dorme 2 segundos
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

Características:

- `static`: sempre afeta **a thread que o executa** (uma thread não pode mandar outra dormir).
- Lança `InterruptedException` (checked): outra thread pode **interromper** o sono com `thread.interrupt()`. Dentro de `run()` você é obrigado a usar `try/catch` (a assinatura de `Runnable.run()` não declara exceções).
- **Estado**: a thread vai a `TIMED_WAITING` e depois volta a `RUNNABLE`.
- É uma das **poucas garantias**: ela dorme **pelo menos** o tempo pedido (pode acordar um pouco depois, mas não antes).
- **Não solta locks** (veremos na aula 225).

Uso típico: consultar uma API ou o estoque a cada N segundos, sem ocupar o processador em um laço infinito.

### Cuidado ao interpretar a saída

Com 4 threads dormindo 2s dentro de um laço, a saída parece "de 4 em 4", mas **não há garantia** de que as threads avancem em sincronia: uma pode chegar ao `sleep` antes das outras.

```java
// boa prática: restaurar a flag de interrupção
catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

## O que você precisa dominar (Aula 222)

- Nomear threads.
- Prioridade é uma dica, não uma garantia.
- `Thread.sleep`: estático, em ms, `InterruptedException`, `TIMED_WAITING`.
- Sleep não garante ordem entre threads.

---

# Aula 223 — Threads pt 04 — Yield e Join

## 1. Runnable com lambda

Modernizando o código das aulas anteriores:

```java
Thread t1 = new Thread(() -> {
    for (int i = 0; i < 5; i++) {
        System.out.println(Thread.currentThread().getName() + " " + i);
    }
}, "Thread-A");
t1.start();
```

(Uma lambda no lugar da classe que implementa `Runnable`.) Se precisar de dados externos, eles devem ser *final ou efetivamente finais*.

---

## 2. `Thread.yield()`

```java
Thread.yield();
```

É uma **sugestão** para o escalonador: "se houver outras threads prontas, pode executá-las agora; eu tenho tempo de sobra". A thread volta ao estado `RUNNABLE`.

- É apenas uma **dica**: o escalonador pode **ignorar**.
- Na prática, raramente é usado e não altera comportamento de forma confiável.
- Diferente do `sleep`: não há tempo de espera nem mudança para `TIMED_WAITING`.

---

## 3. `join()` — esperar outra thread terminar

Cenário: a thread `main` dispara a thread `t1`. Normalmente `main` segue em frente sem esperar.

```java
Thread t1 = new Thread(tarefa1, "T1");
Thread t2 = new Thread(tarefa2, "T2");

t1.start();
t1.join();      // ⬅ a thread que CHAMA (main) fica bloqueada até t1 terminar
t2.start();     // só começa depois que t1 acabou
```

Leitura: "**main, junte-se à t1**": `main` entra em espera (`WAITING`) até `t1` morrer. Depois segue.

```text
sem join:   main ──start t1──start t2──▶ (t1 e t2 rodam em paralelo com main)
com join:   main ──start t1──[espera t1 terminar]──start t2──▶
```

Variações:

```java
t1.join();            // espera indefinidamente
t1.join(2000);        // espera no máximo 2 segundos
```

Também lança `InterruptedException`.

### Importante

- O `join` é chamado **na thread que quer esperar**, sobre o objeto da thread esperada.
- Usos típicos: esperar que várias threads de cálculo terminem antes de somar os resultados; garantir que uma etapa acabe antes da próxima.

```java
t1.start(); t2.start(); t3.start();
t1.join(); t2.join(); t3.join();       // espera as três
System.out.println("Todas terminaram");
```

## O que você precisa dominar (Aula 223)

- `Thread.yield()`: apenas uma dica.
- `join()`: a thread atual espera a outra terminar.
- `join` x `sleep`.
- Esperar várias threads.

---

# Aula 224 — Sincronismo pt 01 — O problema (condição de corrida)

## 1. O exemplo clássico: conta bancária

```java
public class Account {
    private int balance = 50;

    public int getBalance() { return balance; }

    public void withdrawal(int amount) {
        this.balance = this.balance - amount;
    }
}
```

Uma regra de negócio na hora de sacar:

```java
private void withdrawal(int amount) {
    if (account.getBalance() >= amount) {                         // 1) verifica
        System.out.println(Thread.currentThread().getName() + " vai sacar " + amount);
        account.withdrawal(amount);                               // 2) saca
        System.out.println(Thread.currentThread().getName() + " completou. Saldo: " + account.getBalance());
    } else {
        System.out.println("Sem dinheiro para " + Thread.currentThread().getName() + ". Saldo: " + account.getBalance());
    }
}
```

E duas threads (`Kakashi` e `Jiraya`) compartilhando **o mesmo** objeto `Account` e sacando 10 em 10 repetidamente:

```java
Account account = new Account();            // UM objeto compartilhado
Thread t1 = new Thread(new Saque(account), "Kakashi");
Thread t2 = new Thread(new Saque(account), "Jiraya");
t1.start();
t2.start();
```

---

## 2. O que dá errado?

Em algumas execuções o saldo termina **negativo** (ex.: `-10`), o que a regra de negócio proíbe.

Considere o saldo em 10 e as duas threads chegando juntas:

```text
Saldo = 10
Kakashi: if (saldo >= 10)  → true   ← verificou
        (o escalonador troca de thread AQUI)
Jiraya:  if (saldo >= 10)  → true   ← também verificou (saldo ainda é 10!)
Jiraya:  saca → saldo = 0
Kakashi: saca → saldo = -10         ← estourou!
```

Esse padrão — **verificar e depois agir**, sem que ninguém garanta que a situação não mudou entre as duas etapas — é a **condição de corrida (race condition)**. O trecho verificação+saque deveria ser **atômico** (indivisível).

---

## 3. Por que é tão perigoso?

- O erro **não acontece sempre**: depende de timing. Uma execução funciona, a outra não.
- Difícil de **reproduzir**, de **depurar** e de **testar**. O instrutor brinca: "você vai ganhar cabelo branco".
- Pode passar meses em produção sem aparecer, e explodir num dia de carga alta.

> A causa raiz: **estado mutável compartilhado** entre threads sem coordenação.

## 4. Três jeitos de evitar

1. **Não compartilhar** estado (cada thread com seus dados).
2. **Imutabilidade** (objetos que não mudam são seguros).
3. **Sincronizar** o acesso (próxima aula).

## O que você precisa dominar (Aula 224)

- O que é condição de corrida.
- Padrão "check-then-act" não atômico.
- Por que o bug é intermitente e difícil.
- Estado compartilhado + mutável = perigo.

---

# Aula 225 — Sincronismo pt 02 — `synchronized`

## 1. A ideia: todo objeto tem um **lock** (monitor)

Cada objeto Java possui um **lock** (também chamado *monitor*), uma espécie de "chave". Se uma thread precisa entrar numa região `synchronized`, ela tenta pegar a chave do objeto:

- se a chave está **livre**, ela pega e entra;
- se está **com outra thread**, ela fica `BLOCKED` esperando;
- ao sair do bloco, **devolve a chave**.

Resultado: **uma thread por vez** executa aquela região (exclusão mútua), tornando-a **atômica**.

---

## 2. Método sincronizado

```java
private synchronized void withdrawal(int amount) {
    if (account.getBalance() >= amount) {
        account.withdrawal(amount);
        ...
    }
}
```

O lock usado é o do objeto `this` (a instância atual). Como as duas threads compartilham a **mesma instância** (o mesmo `Runnable`/objeto), disputam a mesma chave.

> Atenção: se cada thread tivesse seu **próprio** objeto, cada uma teria a sua própria chave e o `synchronized` não protegeria nada. O lock tem que ser **compartilhado**.

Agora o saldo nunca fica negativo. A ordem de quem saca continua imprevisível, mas cada saque (verificar+sacar) é indivisível.

---

## 3. `sleep` NÃO solta o lock

```java
private synchronized void withdrawal(int amount) {
    ...
    Thread.sleep(10_000);     // dorme 10s COM a chave na mão
}
```

A outra thread fica `BLOCKED` o tempo todo. Só `wait()` libera o lock (aula 228).

---

## 4. Bloco sincronizado

Você pode sincronizar apenas o trecho necessário (menos bloqueio = mais desempenho):

```java
private void withdrawal(int amount) {
    System.out.println(Thread.currentThread().getName() + " fora do bloco");   // livre para todos

    synchronized (account) {                       // pega o lock do objeto account
        if (account.getBalance() >= amount) {
            account.withdrawal(amount);
        }
    }
}
```

Dentro do bloco: uma thread por vez. Fora: todas executam livremente.

### Boa prática: o objeto de lock deve ser `final`

```java
private final Account account;       // a referência nunca muda
```

Se a variável pudesse ser reatribuída, você poderia sincronizar em um objeto e, logo depois, outra thread sincronizar em outro — e o lock deixaria de proteger. Use `final`, ou um objeto dedicado: `private final Object lock = new Object();`.

---

## 5. Métodos estáticos

`static synchronized` usa o lock da **classe** (`MinhaClasse.class`), e não de uma instância:

```java
public static synchronized void print() { ... }
// equivalente a:
public static void print() {
    synchronized (MinhaClasse.class) { ... }
}
```

Dois locks diferentes (da instância e da classe) **não se bloqueiam** entre si.

## 6. Resumo

| Forma | Lock usado |
|---|---|
| `synchronized` no método de instância | `this` |
| `synchronized` no método estático | `Classe.class` |
| `synchronized (obj) { }` | `obj` |

Prefira **blocos pequenos**: sincronizar demais tira o paralelismo e causa lentidão (e deadlock).

## O que você precisa dominar (Aula 225)

- Lock/monitor de todo objeto.
- Métodos e blocos sincronizados.
- O lock precisa ser compartilhado entre as threads.
- `sleep` não libera o lock.
- Lock `final`; estático usa lock da classe.

---

# Aula 226 — Sincronismo pt 03 — Classes thread-safe

## 1. O que é *thread-safe*?

Uma classe é **thread-safe** quando pode ser usada por várias threads **sem corromper seu estado**, porque seus métodos já são sincronizados internamente.

Exemplos antigos: `StringBuffer` (versão sincronizada do `StringBuilder`), `Vector`, `Hashtable`.

Para coleções modernas, o `Collections` oferece envoltórios:

```java
List<String> nomes = Collections.synchronizedList(new ArrayList<>());
```

Cada método individual (`add`, `remove`, `get`) é sincronizado.

---

## 2. ⚠️ Thread-safe não é "tudo seguro"

O instrutor mostra a armadilha com uma classe `Members`:

```java
public class Members {
    private final List<String> nomes = Collections.synchronizedList(new ArrayList<>());

    public void add(String nome) { nomes.add(nome); }

    public String removeFirstIfExists() {
        if (!nomes.isEmpty()) {                       // 1) verifica  (seguro)
            System.out.println(Thread.currentThread().getName() + " vai remover");
            return nomes.remove(0);                   // 2) remove    (seguro, MAS...)
        }
        return null;
    }
}
```

Cada chamada à lista é segura **individualmente**, mas a **sequência** `isEmpty` → `remove` **não é atômica**:

```text
Lista: ["William"]
Thread A: !isEmpty() → true
Thread B: !isEmpty() → true      ← ainda tem 1 elemento
Thread A: remove(0) → "William"
Thread B: remove(0) → IndexOutOfBoundsException!  (lista já vazia)
```

Ou seja, **operações compostas** (verificar+agir) em coleções thread-safe ainda precisam de sincronização **no nível acima**:

```java
public String removeFirstIfExists() {
    synchronized (nomes) {                  // protege a sequência inteira
        if (!nomes.isEmpty()) {
            return nomes.remove(0);
        }
        return null;
    }
}
```

> Regra: **sincronização de métodos individuais não torna seus algoritmos seguros**. Se a sua lógica envolve mais de uma operação dependente, ela precisa ser atômica como um todo.

(Para fila concorrente, no próximo bloco você conhecerá estruturas como `ConcurrentLinkedQueue` e `BlockingQueue`.)

## O que você precisa dominar (Aula 226)

- Conceito de thread-safe.
- `Collections.synchronizedList`.
- Operações compostas continuam inseguras (check-then-act).
- Sincronizar sobre o próprio objeto da coleção.

---

# Aula 227 — Sincronismo pt 04 — Deadlock

## 1. O que é

**Deadlock** (impasse) acontece quando duas (ou mais) threads ficam **esperando uma pela outra para sempre**, cada uma segurando um lock de que a outra precisa.

Analogia: duas pessoas em um corredor estreito, cada uma esperando a outra recuar.

```text
Thread A segura lock1 ──── quer lock2 ──┐
                                        ├─ ninguém libera → TRAVA PARA SEMPRE
Thread B segura lock2 ──── quer lock1 ──┘
```

Quando ocorre, o programa **trava** (não lança exceção) e só resta matar o processo.

---

## 2. Reproduzindo

```java
Object lock1 = new Object();
Object lock2 = new Object();

Thread t1 = new Thread(() -> {
    synchronized (lock1) {
        System.out.println("T1 pegou lock1");
        dormir(10);
        synchronized (lock2) {
            System.out.println("T1 pegou lock2");
        }
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lock2) {                         // ordem INVERTIDA
        System.out.println("T2 pegou lock2");
        dormir(10);
        synchronized (lock1) {
            System.out.println("T2 pegou lock1");
        }
    }
});

t1.start();
t2.start();
// Saída típica: "T1 pegou lock1", "T2 pegou lock2" ... e para.
```

O `sleep` entre os dois locks aumenta a chance do bug aparecer (sem ele pode funcionar "de sorte" muitas vezes — a mesma característica traiçoeira das condições de corrida).

---

## 3. Como evitar

1. **Ordem consistente de aquisição de locks**: todas as threads devem pegar os locks **na mesma ordem** (`lock1` sempre antes de `lock2`). É a solução mostrada na aula: basta inverter uma das threads e o deadlock some.
2. **Reduzir o escopo** do `synchronized`; evitar chamar código desconhecido dentro de blocos sincronizados.
3. Usar `tryLock` com tempo-limite (`java.util.concurrent.locks.Lock`, próximos blocos).
4. Evitar a necessidade de múltiplos locks.

## 4. As quatro condições de Coffman (para entrevistas)

Um deadlock só ocorre se as 4 existirem ao mesmo tempo: **exclusão mútua**, **posse e espera**, **sem preempção**, **espera circular**. Quebrar qualquer uma evita o deadlock (a ordem consistente quebra a espera circular).

## 5. Diagnóstico

Com o programa travado, `jstack <pid>` ou o *Thread Dump* da IDE mostram "Found one Java-level deadlock".

## O que você precisa dominar (Aula 227)

- O que é deadlock e como ele se forma.
- Reproduzir com dois locks em ordem invertida.
- Evitar com ordem consistente.
- Pergunta clássica de entrevista.

---

# Aula 228 — Sincronismo pt 05 — `wait`, `notify`, `notifyAll`

## 1. O problema: produtor × consumidor

Imagine um sistema de envio de e-mails:

- Uma parte do programa **recebe** e-mails de uma lista de membros (**produtor**).
- Outras threads **enviam** esses e-mails (**consumidores**).

Quando a lista está vazia, as threads consumidoras **não devem encerrar** nem ficar em laço ocupado (gastando CPU). Devem **esperar** até que apareça um e-mail novo e serem **acordadas**.

Para isso, `Object` fornece três métodos:

| Método | O que faz |
|---|---|
| `wait()` | a thread atual **libera o lock** e fica esperando (`WAITING`) |
| `notify()` | acorda **uma** thread que esteja esperando neste objeto |
| `notifyAll()` | acorda **todas** as threads esperando neste objeto |

### Regras obrigatórias

1. Só podem ser chamados **dentro de `synchronized`** sobre o mesmo objeto (senão: `IllegalMonitorStateException`).
2. `wait()` lança `InterruptedException`.
3. `wait()` **solta o lock**; ao ser acordada, a thread precisa **readquirir o lock** antes de continuar.
4. Sempre coloque `wait()` dentro de um **`while`** que re-verifica a condição (por causa de despertares espúrios e de outras threads terem consumido o item antes).

---

## 2. A classe `Members` (produtor)

```java
public class Members {

    private final Queue<String> emails = new ArrayDeque<>();
    private boolean open = true;

    public int pendingEmails() {
        synchronized (emails) {
            return emails.size();
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void addMemberEmail(String email) {
        synchronized (emails) {
            emails.add(email);
            emails.notifyAll();                     // avisa quem estiver esperando
        }
    }

    public String retrieveEmail() {
        synchronized (emails) {
            while (emails.isEmpty()) {              // sem e-mail?
                if (!open) {                        // lista fechada → desiste
                    return null;
                }
                System.out.println(Thread.currentThread().getName() + ": sem e-mails, entrando em modo de espera");
                try {
                    emails.wait();                  // espera (solta o lock)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return emails.poll();                   // tem e-mail → retira
        }
    }

    public void close() {
        synchronized (emails) {
            open = false;
            emails.notifyAll();                     // acorda todos para perceberem o fechamento
        }
    }
}
```

(A aula usa uma lista sincronizada com limite de 10; a ideia é a mesma.)

---

## 3. O consumidor

```java
public class EmailDeliveryService implements Runnable {
    private final Members members;

    public EmailDeliveryService(Members members) { this.members = members; }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        while (members.isOpen() || members.pendingEmails() > 0) {
            String email = members.retrieveEmail();      // pode esperar aqui
            if (email == null) continue;

            System.out.println(name + " enviando e-mail para " + email);
            try {
                Thread.sleep(2000);                       // simula envio
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(name + " enviou com sucesso para " + email);
        }
        System.out.println(name + ": todos os e-mails foram enviados");
    }
}
```

---

## 4. O `main`

```java
Members members = new Members();

Thread jiraya = new Thread(new EmailDeliveryService(members), "Jiraya");
Thread kakashi = new Thread(new EmailDeliveryService(members), "Kakashi");
jiraya.start();
kakashi.start();                    // as duas ficam esperando (lista vazia)

while (true) {
    String email = JOptionPane.showInputDialog("Entre com seu e-mail");   // ou Scanner
    if (email == null || email.isEmpty()) {
        members.close();            // sinaliza que acabou
        break;
    }
    members.addMemberEmail(email);  // notifyAll acorda uma das threads
}
```

Fluxo observado na aula:

```text
1. Jiraya e Kakashi iniciam, não há e-mails → ambos entram em "modo de espera".
2. main adiciona um e-mail + notifyAll → ambos acordam; um (Jiraya) consegue o lock e retira o e-mail.
3. Kakashi acorda, vê a lista vazia de novo → volta a esperar.
4. Jiraya envia (2s), conclui, e volta a esperar.
5. Quando main chama close() + notifyAll → todos acordam, veem open=false e terminam.
```

## 5. `notify` x `notifyAll`

| | `notify()` | `notifyAll()` |
|---|---|---|
| Acorda | uma thread (qualquer) | todas |
| Risco | acordar a thread "errada" e perder o sinal | pequena sobrecarga |
| Uso | só se todas as esperas são equivalentes | **recomendado** na maioria dos casos |

## 6. Em código moderno

`wait/notify` é baixo nível e fácil de errar. No bloco seguinte você conhecerá abstrações prontas (`BlockingQueue`, `Condition`, `ExecutorService`) que fazem o mesmo com mais segurança. Mas entender `wait/notify` é essencial para entrevistas e para compreender como tudo funciona por baixo.

## O que você precisa dominar (Aula 228)

- Problema produtor × consumidor.
- `wait`, `notify`, `notifyAll` e que só funcionam dentro de `synchronized`.
- `wait` solta o lock; `sleep` não.
- `wait` dentro de `while`.
- Sinal de encerramento (`close`) com `notifyAll`.

---

# Mapa mental do bloco

```text
Threads
├── Criação:  extends Thread  |  implements Runnable (preferido)  |  lambda
├── start() (nova thread)  ≠  run() (mesma thread)
├── Estados:  NEW → RUNNABLE ↔ BLOCKED / WAITING / TIMED_WAITING → TERMINATED
├── Controle: setPriority (dica) · sleep (TIMED_WAITING) · yield (dica) · join (espera outra) · interrupt
├── Problema: condição de corrida (check-then-act) com estado compartilhado
├── Solução:  synchronized (método, bloco, estático) · lock final
├── Thread-safe: cada método seguro ≠ algoritmo seguro
├── Perigo:   deadlock (locks em ordem diferente)
└── Coordenação: wait / notify / notifyAll  (dentro de synchronized, em while)
```

# Cola de bolso

| Quero... | Use |
|---|---|
| Executar algo em paralelo | `new Thread(runnable).start()` |
| Esperar uma thread acabar | `t.join()` |
| Pausar a thread atual | `Thread.sleep(ms)` |
| Proteger trecho crítico | `synchronized (lockFinal) { ... }` |
| Evitar deadlock | pegar locks sempre na mesma ordem |
| Fazer thread esperar condição | `while (!cond) lock.wait();` |
| Acordar quem espera | `lock.notifyAll()` |
