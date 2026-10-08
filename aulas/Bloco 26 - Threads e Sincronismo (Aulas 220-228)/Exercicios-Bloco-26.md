# Exercícios — Bloco 26: Threads e Sincronismo

## Aulas 220 a 228

Este arquivo acompanha o README do Bloco 26.

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

> **Onde criar os arquivos:** `src/main/bloco26_aulas220a228_threads_sincronismo/aulaXXX/`

> **Regras do bloco:**
> - Programas com threads podem dar resultados **diferentes a cada execução**. Rode **várias vezes** (pelo menos 10) antes de concluir qualquer coisa.
> - Quando o enunciado pedir para "reproduzir o bug", use laços grandes e/ou `Thread.sleep` curtos para aumentar a chance.
> - Sempre dê **nomes às threads**.
> - Ainda **não** usamos `ExecutorService`, `AtomicInteger`, `Lock` etc. (próximos blocos): use `Thread`, `Runnable`, `synchronized`, `wait/notify`.

---

# Aula 220 — Threads: Introdução

## 🟢 Exercício 01 — `run()` × `start()`

1. Crie uma classe que estende `Thread` e imprime 5 vezes seu nome (`Thread.currentThread().getName()`).
2. Chame `run()` direto duas vezes e depois `start()` duas vezes (em objetos diferentes).
3. Compare os nomes impressos e explique.
4. Chame `start()` duas vezes **no mesmo objeto** e capture a exceção.

---

## 🟡 Exercício 02 — Três jeitos de criar

Crie uma thread que imprime os números de 1 a 10 usando:

1. `extends Thread`;
2. `implements Runnable` (classe nomeada);
3. `Runnable` com **classe anônima**;
4. `Runnable` com **lambda**.

Rode cada uma e mostre qual thread executou. Em comentário, explique por que `Runnable` é preferível.

---

## 🔴 Exercício 03 — Ordem imprevisível

Lance 5 threads que imprimem 20 vezes sua letra (A, B, C, D, E) sem quebra de linha.

1. Rode 10 vezes e salve a saída de cada rodada.
2. Calcule, para cada execução, quantas **trocas de letra** ocorreram na saída (quantas vezes a letra mudou). O que isso revela sobre o escalonador?
3. Mostre que `Thread.currentThread().getName()` na thread `main` é `main`.
4. Crie uma thread **daemon** que imprime um ponto a cada 200 ms e uma thread normal que dorme 1,5 s. Mostre que o programa termina quando a normal acaba, ainda que a daemon continue. Depois troque a daemon por normal e explique.

---

# Aula 221 — Estados das threads (⚠ aula sem transcrição útil)

## 🟢 Exercício 04 — Observando os estados

Crie uma thread que faz `Thread.sleep(500)`. Imprima `getState()` em 5 momentos: antes do `start`, logo após, durante o sono, depois do término e após `join`. Relacione cada um com o estado esperado (`NEW`, `RUNNABLE`, `TIMED_WAITING`, `TERMINATED`).

---

## 🟡 Exercício 05 — Estado BLOCKED e WAITING

1. Crie um objeto `lock` e uma thread A que entra em `synchronized (lock)` e dorme 2 s.
2. Crie uma thread B que tenta entrar no mesmo bloco. Observe e imprima o estado de B enquanto A segura o lock (`BLOCKED`).
3. Crie uma thread C que chama `lock.wait()` e mostre o estado (`WAITING`).
4. Acorde C com `notify` e mostre a transição.

Desenhe o diagrama de transições observadas.

---

## 🔴 Exercício 06 — Monitor de threads

Escreva uma classe `MonitorDeThreads` que roda em outra thread (daemon) e, a cada 100 ms, imprime o estado de uma lista de threads que você registra. Dispare 4 threads com comportamentos diferentes (CPU pesada, sleep, bloqueada num lock, esperando em `wait`) e produza uma "linha do tempo" textual, por exemplo:

```text
t=0ms    A:RUNNABLE  B:TIMED_WAITING  C:BLOCKED  D:WAITING
```

Pare quando todas terminarem.

---

# Aula 222 — Priority e Sleep

## 🟢 Exercício 07 — Prioridades

Crie 4 threads iguais (cada uma conta até 5 milhões incrementando uma variável local) com prioridades 1, 3, 5 e 10. Imprima a ordem de término. Repita 10 vezes e conte quantas vezes a de prioridade 10 terminou primeiro.

### Responda

Dá para confiar em prioridade? Por quê?

---

## 🟡 Exercício 08 — Relógio

Crie uma thread `Relogio` que imprime a hora atual a cada segundo por 10 segundos usando `Thread.sleep(1000)`. Mostre:

1. Que o tempo real entre impressões é **um pouco maior** que 1000 ms (meça com `System.nanoTime`).
2. Que a thread fica `TIMED_WAITING` durante o sono.
3. O que acontece se outra thread chamar `relogio.interrupt()` durante o sono (capture `InterruptedException`).

---

## 🔴 Exercício 09 — Sleep sem garantia de sincronia

Crie 3 threads que, em um laço de 5 iterações, imprimem seu nome e dormem 1 s. Observe a saída e explique por que o padrão "uma de cada vez" **não é garantido**.

Depois implemente um **sincronizador manual de rodadas** (usando `synchronized` + `wait/notifyAll` ou `join`) para que as 3 threads só passem para a iteração seguinte quando as três terminaram a atual (uma "barreira" simples). Teste 10 vezes.

---

# Aula 223 — Yield e Join

## 🟢 Exercício 10 — `join` básico

Lance a thread `T1` (que conta de 1 a 5 com `sleep(300)`) e peça para `main` fazer `join` nela antes de imprimir "Fim". Depois remova o `join` e compare a ordem das mensagens.

---

## 🟡 Exercício 11 — Etapas dependentes

Simule uma "linha de montagem":

```text
Thread Corte      (leva 500 ms)
Thread Solda      (só pode começar depois de Corte)
Thread Pintura    (só depois de Solda)
Thread Embalagem  (só depois de Pintura)
```

Implemente com `join` (cada thread espera a anterior) e imprima o tempo total. Depois adapte para que **Corte** e **Compra de embalagem** rodem em paralelo e **Embalagem** espere as duas.

---

## 🔴 Exercício 12 — Soma paralela

Divida um array de 10 milhões de inteiros em 4 partes e some cada parte em uma thread. Use `join` para esperar todas e some os parciais.

1. Compare com a soma sequencial (tempo e resultado).
2. Varie o número de threads (1, 2, 4, 8, 16, 32) e faça uma tabela de tempos. Em que ponto não compensa mais?
3. Teste `join(100)` com timeout e trate o caso "thread ainda viva" (`isAlive()`).
4. Mostre o que acontece se você esquecer o `join` e somar os parciais antes de as threads terminarem.
5. Teste `Thread.yield()` dentro das threads e discuta se mudou algo.

---

# Aula 224 — Sincronismo pt 01: O problema

## 🟢 Exercício 13 — Reproduzindo o bug

Implemente a `Conta` (saldo inicial 50) e duas threads que sacam 10 até acabar o saldo, **sem sincronização**. Rode 20 vezes e conte em quantas o saldo final foi negativo. Anote.

---

## 🟡 Exercício 14 — Contador quebrado

1. Crie `Contador` com `int valor` e `incrementar()` (`valor++`).
2. Lance 4 threads que incrementam 100 000 vezes cada.
3. Imprima o valor final (esperado: 400 000). Rode 10 vezes e anote os valores.
4. Explique por que `valor++` não é atômico (leitura, soma, escrita).

---

## 🔴 Exercício 15 — Mais condições de corrida

Escreva 3 programas pequenos, cada um expondo uma condição de corrida diferente:

1. **Check-then-act** em um estoque (vender se `estoque > 0`).
2. **Read-modify-write** em um contador (já visto).
3. **Inicialização preguiçosa** (`if (instancia == null) instancia = new Objeto()`) com duas threads — conte quantas instâncias foram criadas.

Para cada um: reproduza, mostre o resultado errado e descreva a sequência de eventos que o causa. Não corrija ainda.

---

# Aula 225 — Sincronismo pt 02: synchronized

## 🟢 Exercício 16 — Corrigindo a conta

Corrija o exercício 13 com `synchronized` no método e rode 20 vezes: o saldo nunca pode ficar negativo. Depois corrija o exercício 14 e confirme o 400 000.

---

## 🟡 Exercício 17 — Método × bloco

1. Faça a conta funcionar com `synchronized` em um **método** e depois com um **bloco** apenas ao redor do trecho crítico.
2. Meça o tempo total quando cada saque tem uma parte "demorada" **fora** da seção crítica (`sleep(50)` antes do saque) e uma **dentro**. Compare os dois estilos.
3. Mostre o que acontece se cada thread criar o seu **próprio** objeto de conta (nada é compartilhado) — o `synchronized` ainda tem efeito?
4. Mostre que `sleep` dentro do bloco **não** solta o lock (a outra thread fica `BLOCKED`).

---

## 🔴 Exercício 18 — Locks diferentes, proteção diferente

Crie uma classe `Banco` com duas contas e um método `transferir(origem, destino, valor)`.

1. Primeiro, sincronize **cada método da conta** separadamente (`sacar`, `depositar` `synchronized`) e mostre que ainda há problema de consistência na transferência (a soma total muda em alguma execução).
2. Corrija sincronizando a **transferência inteira** com um lock do banco.
3. Mostre a diferença entre `synchronized` de instância, de bloco com objeto e `static synchronized` (lock da classe), com um exemplo em que dois métodos **não** se bloqueiam porque usam locks diferentes.
4. Explique por que o objeto de lock deve ser `final`. Demonstre o problema com um lock reatribuído.

---

# Aula 226 — Classes thread-safe

## 🟢 Exercício 19 — `StringBuffer` × `StringBuilder`

1. Duas threads adicionam 100 000 caracteres cada em um `StringBuilder`; imprima o tamanho final (esperado 200 000). Rode várias vezes e capture eventuais exceções.
2. Repita com `StringBuffer`.
3. Meça o tempo de ambos em uma única thread e discuta o custo da sincronização.

---

## 🟡 Exercício 20 — Lista sincronizada com bug composto

Reproduza o exemplo da aula:

1. `Collections.synchronizedList` com um método `removerPrimeiroSeExistir()` do tipo check-then-act.
2. Duas threads removendo de uma lista com **um** elemento.
3. Capture `IndexOutOfBoundsException` e conte quantas vezes aconteceu em 1000 rodadas.
4. Corrija com `synchronized (lista)` e confirme zero falhas.

---

## 🔴 Exercício 21 — Cache seguro

Implemente `CacheDeCalculos` que guarda resultados num `Map<Integer, Long>` (fatorial).

1. Versão ingênua com `HashMap` e duas threads → mostre `NullPointerException`/resultados inconsistentes.
2. Versão com `Collections.synchronizedMap` e check-then-act (`if (!map.containsKey(n)) map.put(n, calcular(n))`) → mostre que o cálculo pode ser executado **duas vezes** para a mesma chave.
3. Versão correta com bloco sincronizado.
4. Meça o desempenho com 8 threads e 100 000 consultas. Discuta o custo de sincronizar tudo e antecipe a ideia de `ConcurrentHashMap` (próximos blocos).

---

# Aula 227 — Deadlock

## 🟢 Exercício 22 — Reproduzindo um deadlock

Reproduza o deadlock da aula com dois objetos `lock1`, `lock2` e duas threads em ordem invertida. Use `sleep(10)` entre os locks. Prove que o programa travou: imprima o estado das duas threads em `main` após 2 s (`BLOCKED`).

---

## 🟡 Exercício 23 — Consertando

1. Corrija o deadlock do exercício anterior padronizando a ordem dos locks.
2. Gere automaticamente a ordem usando `System.identityHashCode` para decidir qual lock pegar primeiro (útil quando a ordem não é óbvia).
3. Mostre que o programa termina corretamente em 100 execuções.

---

## 🔴 Exercício 24 — Jantar dos filósofos

Implemente o clássico **jantar dos filósofos** (5 filósofos, 5 garfos):

1. Versão ingênua: cada filósofo pega o garfo da esquerda e depois o da direita → provoque o deadlock.
2. Corrija com **hierarquia de recursos** (numerar os garfos e sempre pegar o de menor número primeiro).
3. Corrija com outra estratégia (no máximo 4 filósofos à mesa ao mesmo tempo, usando contador + `wait/notify`).
4. Faça cada filósofo comer 5 vezes e imprima um relatório de quantas refeições cada um fez.
5. Em comentário, relacione sua solução com as quatro condições de Coffman.

---

# Aula 228 — wait, notify, notifyAll

## 🟢 Exercício 25 — Espera e aviso

Crie uma thread `Esperadora` que faz `wait()` em um objeto e uma thread `Avisadora` que dorme 2 s e faz `notify()`. Imprima o estado da Esperadora antes e depois. Tente chamar `wait()` **fora** de `synchronized` e capture a exceção.

---

## 🟡 Exercício 26 — Caixa de mensagem

Implemente `Caixa` (um único slot):

```text
colocar(String msg)  → espera se a caixa estiver cheia
retirar()            → espera se estiver vazia
```

Com um produtor que envia 10 mensagens e um consumidor que as lê. As mensagens devem chegar **todas, em ordem, sem perda e sem repetição**. Use `while` ao redor do `wait`. Mostre o que acontece se trocar o `while` por `if` quando há 2 consumidores.

---

## 🔴 Exercício 27 — Buffer limitado

Implemente `BufferLimitado<T>` (capacidade N) com `colocar` e `retirar` bloqueantes usando `wait/notifyAll`.

1. 3 produtores (cada um gera 20 itens) e 2 consumidores.
2. Garanta que o buffer **nunca** excede a capacidade nem fica negativo (valide com contadores e asserts).
3. Implemente um sinal de **encerramento** ("pílula de veneno" ou flag `fechado`) para os consumidores terminarem.
4. Compare `notify` e `notifyAll` com 2 tipos de consumidor e mostre o perigo do `notify` (thread esquecida).
5. Meça a vazão (itens/segundo) com capacidades 1, 10, 100.

---

# 🏆 Desafio Integrador do Bloco 26 — Simulador de Agência Bancária Concorrente

Você vai construir uma agência com várias threads: clientes, caixas e fiscalização.

## Domínio

```text
Conta            → numero, saldo
Banco            → mapa de contas
Cliente (thread) → faz depósitos, saques, transferências aleatórias
Caixa (thread)   → atende uma FILA de clientes (produtor × consumidor)
Auditor (daemon) → verifica periodicamente se a soma de todos os saldos permanece constante
Relatorio (main) → espera tudo terminar e imprime um resumo
```

## Requisitos

1. **Condição de corrida**: primeiro implemente sem sincronização e, com 50 clientes e 20 000 operações, mostre (no relatório) o desvio da soma total e saldos negativos.
2. **Correção com `synchronized`**: sacar, depositar e **transferir** (a transferência deve ser atômica e **sem deadlock** — ordene os locks pelo número da conta).
3. **Fila de atendimento**: os clientes (produtores) colocam pedidos numa fila limitada com `wait/notifyAll`; 3 caixas (consumidores) processam. A fila fecha quando todos os clientes terminam.
4. **Auditor daemon**: a cada 200 ms, calcula a soma dos saldos (sincronizando o suficiente para obter um retrato consistente) e imprime alerta se mudar.
5. **Encerramento limpo**: `main` usa `join` nos clientes, fecha a fila, faz `join` nos caixas e imprime o relatório (operações por caixa, tempo médio por operação, maior fila observada).
6. **Estados**: imprima, no final, o `getState()` de todas as threads (todas `TERMINATED`).
7. **Experimento de deadlock**: inclua uma flag que usa uma versão **ruim** da transferência (locks na ordem origem→destino) e mostre o travamento; depois a versão correta.
8. **Medição**: compare o tempo total com 1, 2, 4 e 8 caixas e explique o resultado.
9. **Testes de estresse**: execute o cenário completo 20 vezes e comprove que a soma total final é sempre igual à inicial.

## Regras

- Sem `AtomicInteger`, `Lock`, `ExecutorService` nem coleções concorrentes (ficam para o próximo bloco).
- Sempre nomear as threads (`Cliente-1`, `Caixa-2`...).
- Documente (comentário) cada região crítica: qual lock protege qual invariante.

---

# Checklist do bloco

Antes do desafio, confirme:

- [ ] Sei a diferença entre processo e thread.
- [ ] Sei criar threads com `Thread`, `Runnable` e lambda.
- [ ] Sei a diferença entre `start()` e `run()`.
- [ ] Conheço os estados de uma thread.
- [ ] Sei usar `sleep`, `join`, `yield` e sei o que cada um garante.
- [ ] Sei explicar condição de corrida e check-then-act.
- [ ] Sei usar `synchronized` em método, bloco e estático.
- [ ] Sei que o lock precisa ser compartilhado e `final`.
- [ ] Sei que `sleep` não solta o lock e `wait` solta.
- [ ] Sei que thread-safe por método não garante algoritmo seguro.
- [ ] Sei explicar e evitar deadlock.
- [ ] Sei usar `wait`, `notify` e `notifyAll` com `while`.

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
