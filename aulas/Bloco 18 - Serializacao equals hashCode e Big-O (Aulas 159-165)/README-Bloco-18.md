# Bloco 18 — Serialização, `equals`, `hashCode` e Complexidade Big-O

## Aulas 159 a 165

Este bloco fecha a parte de IO com **serialização** e abre a porta para o grande tema seguinte: as **Coleções**. Antes de usar `List`, `Set` e `Map`, você precisa dominar três fundamentos que elas usam por baixo dos panos: `equals`, `hashCode` e a ideia de complexidade (Big-O).

As aulas deste bloco são:

```text
159 — Serialização pt 01
160 — Serialização pt 02
161 — Coleções pt 01 — equals pt 01
162 — Coleções pt 02 — equals pt 02
163 — Coleções pt 03 — hashCode pt 01
164 — Coleções pt 04 — hashCode pt 02
165 — Coleções pt 05 — Big-O Complexity
```

Fluxo do bloco:

```text
Objeto em memória ──serializar──▶ bytes no arquivo ──desserializar──▶ objeto de novo
                                  (Serializable, transient, serialVersionUID)

Quando dois objetos são "o mesmo"?   ──▶ equals
Como achá-los rápido numa coleção?   ──▶ hashCode
Quão rápida é cada coleção?          ──▶ Big-O
```

---

# Aula 159 — Serialização pt 01

## 1. O que é serializar?

**Serializar** é transformar um objeto que está na memória em uma **sequência de bytes**, para poder **guardá-lo** (arquivo, banco, rede) e depois recuperá-lo. **Desserializar** é o caminho inverso.

```text
Objeto na memória  ──────────▶  [0xAC 0xED 0x00 0x05 ...]  (bytes)
                  serializar                      │
Objeto na memória  ◀──────────  arquivo/rede  ◀───┘
                 desserializar
```

Onde isso é usado? Muitos frameworks usam serialização por baixo dos panos (sessões web, cache, envio de objetos pela rede). Também é útil quando você quer **salvar o estado** de um objeto e recuperá-lo depois.

---

## 2. As classes envolvidas

São streams de **baixo nível** (trabalham com bytes) do pacote `java.io`:

| Classe | Papel |
|---|---|
| `ObjectOutputStream` | **escreve** objetos (serializa) |
| `ObjectInputStream` | **lê** objetos (desserializa) |
| `FileOutputStream` | destino em arquivo (bytes) |
| `FileInputStream` | origem em arquivo (bytes) |

E o encadeamento que você já conhece:

```text
new ObjectOutputStream( new FileOutputStream( "aluno.ser" ) )
        ▲                         ▲
   sabe escrever objeto     sabe escrever bytes em arquivo
```

Sobre os sufixos: `...InputStream` **lê**, `...OutputStream` **escreve**.

---

## 3. A classe de domínio

```java
import java.io.Serializable;

public class Aluno implements Serializable {
    private Long id;
    private String nome;

    public Aluno(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return "Aluno{id=" + id + ", nome='" + nome + "'}";
    }
}
```

### `Serializable` é uma interface **marcadora**

```java
public interface Serializable { }   // vazia!
```

Ela **não tem métodos**. Serve apenas como uma "etiqueta" que diz à JVM: "este objeto pode ser serializado". Se você tentar serializar sem implementá-la:

```text
java.io.NotSerializableException: Aluno
```

---

## 4. Serializando

```java
private static void serializar(Aluno aluno) {
    Path arquivo = Paths.get("pasta", "aluno.ser");

    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(arquivo))) {
        oos.writeObject(aluno);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

(Também pode usar `new FileOutputStream(new File("pasta/aluno.ser"))`.)

Abrir o `.ser` em um editor de texto mostra "lixo": são bytes que só a JVM entende. A extensão `.ser` é uma convenção.

---

## 5. Desserializando

```java
private static void desserializar() {
    Path arquivo = Paths.get("pasta", "aluno.ser");

    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(arquivo))) {
        Aluno aluno = (Aluno) ois.readObject();
        System.out.println(aluno);
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

Pontos importantes:

- `readObject()` devolve `Object` → **cast** obrigatório.
- Lança `ClassNotFoundException` (a JVM precisa ter a classe disponível) além de `IOException`; daí o multi-catch.
- O objeto lido é uma **cópia**: mudar o objeto original depois não afeta o lido.

---

## 6. ⚠️ O construtor NÃO é executado na desserialização

Se você colocar um `System.out.println` dentro do construtor de `Aluno`:

```text
Ao criar com new       → imprime
Ao desserializar       → NÃO imprime
```

O objeto é "reconstruído" a partir dos bytes, sem passar pelo construtor da classe serializável.

Consequências com **herança**:

- Se a superclasse **não** é `Serializable`, o Java chama o **construtor sem argumentos** dela (e ele precisa existir).
- Se a superclasse é `Serializable`, nenhum construtor da hierarquia serializável é executado.

Lembre disso: lógica de inicialização colocada só no construtor **não roda** ao desserializar.

## O que você precisa dominar (Aula 159)

- Serializar = objeto → bytes; desserializar = bytes → objeto.
- `Serializable` é uma interface marcadora (sem métodos).
- `ObjectOutputStream.writeObject` / `ObjectInputStream.readObject`.
- `NotSerializableException` e `ClassNotFoundException`.
- O construtor da classe serializável não roda ao desserializar.

---

# Aula 160 — Serialização pt 02

## 1. `transient` — campos que NÃO devem ser serializados

Imagine uma classe com senha:

```java
public class Aluno implements Serializable {
    private Long id;
    private String nome;
    private transient String password;   // não será gravada
}
```

Um atributo `transient` é **ignorado** na serialização. Ao desserializar ele volta com o **valor padrão** do tipo:

| Tipo | Valor ao desserializar |
|---|---|
| `String` e objetos | `null` |
| `int`, `long` | `0` |
| `boolean` | `false` |

Usos típicos: senhas e dados sensíveis, caches, atributos calculados, referências a recursos (conexões, threads).

---

## 2. `serialVersionUID`

Toda classe serializável tem um **número de versão**. Ao gravar, o Java guarda esse número junto com os bytes. Ao ler, compara com o número da classe atual; se diferirem:

```text
java.io.InvalidClassException: Aluno; local class incompatible:
stream classdesc serialVersionUID = ..., local class serialVersionUID = ...
```

Se você **não declara**, o Java calcula um valor automático (uma espécie de hash da estrutura da classe). Então, **qualquer alteração na classe** (adicionar um campo, por exemplo) muda o valor e quebra a leitura de arquivos antigos.

Boa prática: declarar explicitamente:

```java
private static final long serialVersionUID = 1L;
```

Com isso você controla a compatibilidade: pode adicionar `transient` ou outros campos e continuar lendo arquivos antigos (os campos novos virão com valor padrão).

> No IntelliJ: ativar a inspeção *Serializable class without serialVersionUID* e usar **Alt+Enter** para gerar o campo.

Quando **mudar** o `serialVersionUID`? Quando a alteração torna a leitura antiga **incompatível** de propósito.

---

## 3. Atributos `static` NÃO são serializados

`static` pertence à **classe**, não ao objeto. Portanto não vai para os bytes:

```java
private static String nomeEscola = "DevDojo";
```

Se você mudar `nomeEscola` entre a gravação e a leitura (dentro do mesmo programa ou depois), o valor lido será o **atual da classe**, não o que estava na hora de gravar.

---

## 4. Associação: o atributo também precisa ser serializável

```java
public class Aluno implements Serializable {
    private Turma turma;   // objeto dentro de objeto
}
```

- Se `turma` for `null` → tudo bem.
- Se `turma` tiver valor e `Turma` **não** for `Serializable` → `NotSerializableException: Turma`.

### Soluções

1. **Fazer `Turma implements Serializable`** (se você controla a classe).
2. Marcar o campo como `transient` (e perder o dado).
3. Se **não puder alterar** a classe (ela vem de uma biblioteca), **customizar** a serialização (próximo item).

---

## 5. Serialização personalizada: `writeObject` e `readObject`

Marque a referência como `transient` e escreva dois métodos **privados** com assinaturas exatas, dentro da classe `Aluno`:

```java
private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();          // grava normalmente os campos não-transient
    oos.writeUTF(turma.getNome());     // grava só o que interessa da turma
}

private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();           // lê os campos não-transient
    String nomeTurma = ois.readUTF();  // lê na MESMA ORDEM em que foi gravado
    this.turma = new Turma(nomeTurma); // reconstrói o objeto
}
```

Regras:

- Os métodos precisam ser `private`, com **exatamente** esses nomes e parâmetros (o Java os localiza por reflexão; sem `@Override` pois não são da interface).
- Chame `defaultWriteObject()`/`defaultReadObject()` primeiro.
- **A ordem de gravação e leitura precisa ser a mesma.**
- Grave tipos simples (`writeUTF`, `writeInt`, `writeLong`...) ou objetos que sejam serializáveis.
- Para reconstruir o objeto não serializável, você recria com `new`.

Resumo: você ensina a JVM como "desmontar" e "remontar" o que ela não sabe fazer sozinha.

---

## 6. Resumo das regras de ouro

| Situação | O que fazer |
|---|---|
| Quero serializar | `implements Serializable` |
| Dado sensível | `transient` |
| Controlar versão | `serialVersionUID` fixo |
| Campo estático | não é serializado |
| Atributo-objeto não serializável | tornar serializável, `transient`, ou `writeObject/readObject` |
| Ler | na mesma ordem que gravou |

## O que você precisa dominar (Aula 160)

- `transient` e seus valores padrão.
- `serialVersionUID` e `InvalidClassException`.
- `static` não é serializado.
- `NotSerializableException` em atributos.
- `writeObject` / `readObject` personalizados.

---

# Aula 161 — Coleções pt 01 — `equals` (parte 1)

> Observação do instrutor: o curso usa a **versão 8 da linguagem** (language level 8). Anotações introduzidas depois (ex.: `@Serial`, Java 14) não existem nesse nível.

## 1. Por que começar por `equals`?

As coleções (listas, sets, mapas) precisam responder perguntas como "esse objeto já está na lista?" e "qual a posição desse objeto?". Para isso, elas **comparam objetos** — e comparar objetos corretamente em Java é um tema delicado.

---

## 2. `==` compara **referências**

```java
String nome1 = "Gabriel";
String nome2 = "Gabriel";
nome1 == nome2;           // true  — String Pool: mesma referência

String nome3 = new String("Gabriel");
nome1 == nome3;           // false — objetos diferentes na memória
nome1.equals(nome3);      // true  — mesmo conteúdo
```

Para objetos, `==` pergunta: **"as duas variáveis apontam para o MESMO objeto na memória?"**

---

## 3. `equals` da classe `Object`

Todo objeto herda `equals` de `Object`. O comportamento original é **idêntico ao `==`**:

```java
// implementação em Object
public boolean equals(Object obj) {
    return (this == obj);
}
```

`String` **sobrescreve** `equals` para comparar o **conteúdo**.

---

## 4. O problema com classes próprias

```java
public class Smartphone {
    private String serialNumber;
    private String marca;

    public Smartphone(String serialNumber, String marca) {
        this.serialNumber = serialNumber;
        this.marca = marca;
    }
}
```

```java
Smartphone s1 = new Smartphone("123", "iPhone");
Smartphone s2 = new Smartphone("123", "iPhone");

System.out.println(s1.equals(s2));   // false!
```

Para nós, os dois são **o mesmo aparelho** (mesmo número de série). Mas, sem sobrescrever o `equals`, o Java usa o `==` de `Object` e vê **dois objetos distintos**:

```text
s1 ──▶ [Smartphone "123" iPhone]    (objeto A)
s2 ──▶ [Smartphone "123" iPhone]    (objeto B)  ← outro objeto na memória
```

Se fizéssemos `s2 = s1`, aí sim `equals` daria `true`, mas isso é o **mesmo objeto**, não **objetos logicamente iguais**.

---

## 5. Igualdade lógica

É preciso ensinar o Java o que significa "igual" para a sua classe, **sobrescrevendo `equals`**. Quem decide é a regra de negócio:

- Smartphone: igual se o `serialNumber` for igual.
- Cliente: igual se o CPF for igual.
- Produto: igual se o código for igual.

Na próxima aula você implementa.

## O que você precisa dominar (Aula 161)

- `==` compara referências; `equals` compara o que a classe definir.
- `Object.equals` por padrão faz `==`.
- `String` sobrescreve `equals`.
- Dois objetos podem ser logicamente iguais sem serem o mesmo objeto.
- Por que coleções dependem de `equals`.

---

# Aula 162 — Coleções pt 02 — `equals` (parte 2)

## 1. O contrato do `equals`

Ao sobrescrever, você deve respeitar as regras da documentação (para qualquer `x`, `y`, `z` **não nulos**):

| Regra | Significa |
|---|---|
| **Reflexivo** | `x.equals(x)` é sempre `true` |
| **Simétrico** | `x.equals(y)` é `true` se, e somente se, `y.equals(x)` é `true` |
| **Transitivo** | se `x.equals(y)` e `y.equals(z)`, então `x.equals(z)` |
| **Consistente** | várias chamadas devolvem o mesmo resultado, se nada mudou |
| **Nulo** | `x.equals(null)` é sempre `false` |

---

## 2. Passo a passo da implementação (como na aula)

```java
@Override
public boolean equals(Object o) {
    // 1) nulo nunca é igual
    if (o == null) return false;

    // 2) mesma referência → igual (atalho de performance)
    if (this == o) return true;

    // 3) classes diferentes → não podem ser iguais
    if (this.getClass() != o.getClass()) return false;

    // 4) agora é seguro converter
    Smartphone outro = (Smartphone) o;

    // 5) regra de negócio: o que define igualdade?
    return serialNumber != null && serialNumber.equals(outro.serialNumber);
}
```

Ordem importante:

1. `null` primeiro, para evitar `NullPointerException`.
2. `this == o` evita comparações caras.
3. Verificação de classe **antes** do cast, para evitar `ClassCastException`. (Alternativa: `instanceof`, com cuidado na herança — pode quebrar a simetria.)
4. Compare os atributos que a **regra de negócio** define. Se `serialNumber` pode ser `null`, trate (a aula usa `serialNumber != null && ...`). A forma moderna é `Objects.equals`:

```java
return Objects.equals(serialNumber, outro.serialNumber);
```

`Objects.equals(a, b)` já trata nulos dos dois lados.

---

## 3. Usando

```java
Smartphone s1 = new Smartphone("123", "iPhone");
Smartphone s2 = new Smartphone("123", "iPhone");

System.out.println(s1.equals(s2));   // true
System.out.println(s1 == s2);        // false (ainda são dois objetos)
System.out.println(s1.equals(null)); // false
```

---

## 4. Quais atributos entram no `equals`?

A pergunta é de **negócio**, não de programação:

| Critério de igualdade | Consequência |
|---|---|
| só `serialNumber` | dois aparelhos com o mesmo serial, mesmo com marcas diferentes, são "iguais" |
| `serialNumber` **e** `marca` | só são iguais se os dois baterem |

O instrutor lembra: no exemplo, a comparação só considerou o serial; para considerar a marca também, é só acrescentá-la no `equals` — e, como veremos, **também no `hashCode`**.

## 5. Erros comuns

- Sobrecarregar em vez de sobrescrever: `public boolean equals(Smartphone s)` **não** sobrescreve (a assinatura correta recebe `Object`). Use `@Override` para o compilador avisar.
- Esquecer o `null`.
- Fazer cast sem verificar a classe.
- Usar atributos mutáveis que mudam e quebram a consistência.
- Esquecer de sobrescrever também o `hashCode` (próximas aulas).

## O que você precisa dominar (Aula 162)

- As 5 regras do contrato do `equals`.
- Os passos: `null`, `this == o`, classe, cast, comparação.
- `Objects.equals` para atributos que podem ser nulos.
- `@Override` e parâmetro `Object`.
- Quais atributos entram na comparação é decisão de negócio.

---

# Aula 163 — Coleções pt 03 — `hashCode` (parte 1)

## 1. O problema: procurar em coleções enormes

Imagine um array com **70 milhões** de posições e você quer saber se o nome "Jiraya" está lá. Sem pistas, teria que olhar **posição por posição** (comparando com `equals`) — muito lento.

---

## 2. A ideia do hash

**Hash** é um número gerado a partir do objeto. Ele funciona como o "endereço" de uma **caixa** onde o objeto deve ser guardado.

Exemplo simplificado do instrutor: some o valor de cada letra do nome (a=1, b=2, c=3...).

```text
"Alex"  → a(1) + l(12) + e(5) + x(24) = 42
"Bob"   → b(2) + o(15) + b(2)         = 19
```

Agora, imagine uma estrutura com "caixas" numeradas:

```text
Caixa 19 → [Bob]
Caixa 42 → [Alex]
```

Para procurar "Alex": calcula o hash (42) e vai **direto** à caixa 42. Sem varrer nada. Isso é uma **tabela hash** (*hash table*).

Resultado: o tempo de busca é praticamente **constante**, não importa o tamanho da coleção.

---

## 3. Colisão

Pode acontecer de dois objetos diferentes gerarem o **mesmo hash**:

```text
"Alex" → 42
"Dirk" → d(4)+i(9)+r(18)+k(11) = 42   ← mesma caixa!
```

Isso é uma **colisão**. Nesse caso a caixa 42 passa a guardar **vários** objetos:

```text
Caixa 42 → [Alex, Dirk]
```

E agora entra o `equals`: ao buscar "Alex", o Java:

1. Calcula o hash → vai à caixa 42.
2. Compara, com `equals`, cada item da caixa até achar o que é igual.

```text
hashCode → escolhe a caixa (rápido)
equals   → confirma o objeto certo dentro da caixa
```

### Hash bom x hash ruim

- **Bom hash**: espalha os objetos em muitas caixas diferentes → poucas colisões → busca quase instantânea.
- **Hash ruim**: muitos objetos na mesma caixa → a busca vira uma varredura (lenta).

Criar bons algoritmos de hash é assunto de pesquisa acadêmica, mas na prática você usa os utilitários do Java.

## 4. Quem usa hash?

Coleções cujo nome contém "Hash": `HashSet`, `HashMap`, `LinkedHashMap`, `Hashtable` (vistas nas próximas aulas). Elas **dependem** de `hashCode` e `equals` bem implementados.

## O que você precisa dominar (Aula 163)

- O que é hash e por que acelera buscas.
- Caixas (buckets) e colisões.
- `hashCode` escolhe a caixa; `equals` confirma o objeto.
- Hash bom = poucas colisões.

---

# Aula 164 — Coleções pt 04 — `hashCode` (parte 2)

## 1. O método

```java
public int hashCode()
```

Definido em `Object`, onde é `native` (implementado em outra linguagem, devolve algo ligado ao endereço do objeto). Para o nosso uso, **sobrescrevemos**.

---

## 2. O contrato (as regras de ouro)

Para objetos `x` e `y`:

| # | Regra |
|---|---|
| 1 | Se `x.equals(y) == true`, então `x.hashCode() == y.hashCode()` **obrigatoriamente** |
| 2 | Se `x.hashCode() == y.hashCode()`, `x.equals(y)` **pode** ser `true` ou `false` (colisão) |
| 3 | Se `x.equals(y) == false`, os hashes **podem** ser iguais (ideal: diferentes) |
| 4 | Se `x.hashCode() != y.hashCode()`, então `x.equals(y)` **tem que** ser `false` |
| 5 | Várias chamadas ao `hashCode` do mesmo objeto devolvem o mesmo valor (se nada mudou) |

Resumo prático:

```text
equals true  ⇒  hashCode igual (OBRIGATÓRIO)
hashCode igual ⇏ equals true (pode ser colisão)
```

### Se quebrar a regra 1…

Se dois objetos são `equals` mas têm `hashCode` diferentes, uma coleção hash os coloca em **caixas diferentes** e **nunca os encontra como iguais** — o `HashSet` aceitaria "duplicados" e `contains` retornaria `false` para um objeto "igual". É um bug silencioso e difícil.

---

## 3. A regra de ouro de implementação

> Use **os mesmos atributos** no `equals` e no `hashCode`.

Se o `equals` compara só o `serialNumber`, o `hashCode` deve ser calculado só com o `serialNumber`. Se o `equals` comparar `serialNumber` e `marca`, o `hashCode` deve usar os dois.

---

## 4. Implementação

Como na aula, aproveitando o `hashCode` do `String`:

```java
@Override
public int hashCode() {
    return serialNumber == null ? 0 : serialNumber.hashCode();
}
```

Tratamos `null` com o operador ternário (porque chamar `hashCode` em `null` causaria `NullPointerException`).

Forma moderna e prática, com vários atributos:

```java
@Override
public int hashCode() {
    return Objects.hash(serialNumber);            // um atributo
    // return Objects.hash(serialNumber, marca);  // dois atributos
}
```

`Objects.hash(...)` já trata `null`.

### Equals + hashCode juntos (exemplo completo)

```java
public class Smartphone {
    private String serialNumber;
    private String marca;

    public Smartphone(String serialNumber, String marca) {
        this.serialNumber = serialNumber;
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Smartphone that = (Smartphone) o;
        return Objects.equals(serialNumber, that.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }
}
```

O IntelliJ gera os dois: **Alt+Insert → equals() and hashCode()**.

---

## 5. Dicas finais

- Quanto mais **específico** o `hashCode` (menos colisões), melhor o desempenho.
- Pode haver objetos diferentes com o mesmo hash — não é erro, só deve ser raro.
- Atributos usados no hash **não devem mudar** enquanto o objeto estiver numa coleção hash (senão ele "se perde" na caixa errada).
- Sempre implemente `equals` e `hashCode` **juntos**.

## O que você precisa dominar (Aula 164)

- O contrato `equals` ↔ `hashCode`.
- Mesmos atributos nos dois métodos.
- `Objects.hash` e tratamento de nulos.
- O bug de quebrar a regra 1.

---

# Aula 165 — Coleções pt 05 — Big-O

## 1. Ordenado x Sortido (Ordered x Sorted)

Duas características que classificam as coleções:

| Termo | Significa | Exemplo |
|---|---|---|
| **Ordenada** (*ordered*) | lembra a **ordem de inserção** (ou posição) | `List` |
| **Classificada** (*sorted*) | mantém os elementos em uma **ordem definida por critério** (ex.: alfabética), automaticamente | `TreeSet`, `TreeMap` |

Um `HashSet` não é nem ordenada, nem classificada.

---

## 2. A hierarquia das coleções

```text
Iterable
 └── Collection (interface)
      ├── List   → ArrayList, LinkedList, Vector...
      ├── Set    → HashSet, LinkedHashSet, TreeSet...
      └── Queue  → PriorityQueue, LinkedList, ArrayDeque...

Map (interface) → HashMap, LinkedHashMap, TreeMap, Hashtable...
   (NÃO é uma Collection!)
```

Nota importante: **`Map` não é uma `Collection`**. Ele faz parte do *framework* de coleções, mas não implementa a interface `Collection` (guarda pares chave-valor).

`Collection` (singular, interface) x `Collections` (plural, classe utilitária com métodos estáticos, como `sort`).

Cada coleção tem um **propósito específico** (alta coesão) e você deve programar **orientado à interface** (`List<String> lista = new ArrayList<>();`).

---

## 3. Complexidade Big-O

Big-O descreve **como o tempo (ou memória) cresce** conforme aumenta a quantidade de dados `n`. Normalmente se considera o **pior caso**.

| Notação | Nome | Comportamento | Exemplo |
|---|---|---|---|
| `O(1)` | constante | tempo não depende de `n` | acessar `array[i]`; busca em hash (caso bom) |
| `O(log n)` | logarítmica | cresce muito devagar | busca binária, `TreeMap` |
| `O(n)` | linear | cresce proporcional a `n` | percorrer uma lista |
| `O(n log n)` | linearítmica | ordenação eficiente | `Collections.sort` |
| `O(n²)` | quadrática | cresce ao quadrado | dois laços aninhados |

Visual (da melhor para a pior):

```text
O(1)  <  O(log n)  <  O(n)  <  O(n log n)  <  O(n²)
```

Exemplo numérico, para `n = 1.000.000`:

| Complexidade | Operações aproximadas |
|---|---|
| `O(1)` | 1 |
| `O(log n)` | ~20 |
| `O(n)` | 1.000.000 |
| `O(n log n)` | ~20.000.000 |
| `O(n²)` | 1.000.000.000.000 |

---

## 4. Comparando as coleções

| Coleção | Acessar por índice | Buscar (`contains`) | Inserir | Remover |
|---|---|---|---|---|
| `ArrayList` | `O(1)` | `O(n)` | `O(1)` no fim* / `O(n)` no meio | `O(n)` |
| `LinkedList` | `O(n)` | `O(n)` | `O(1)` nas pontas | `O(1)` com referência |
| `HashSet` / `HashMap` | – | `O(1)` | `O(1)` | `O(1)` |
| `TreeSet` / `TreeMap` | – | `O(log n)` | `O(log n)` | `O(log n)` |

\* amortizado.

### Conclusões que o instrutor tira

- `ArrayList.get(i)` é excelente; mas `contains` precisa comparar com `equals` item por item (`O(n)`).
- Se você faz **muitos** `contains`, um `HashSet` é muito mais rápido.
- **Cuidado**: `Set` **não aceita duplicados**. Uma lista com 10 nomes, com 2 repetidos, vira um `Set` de 9.
- Para inserir muito, estruturas ligadas podem ser melhores que arrays.

---

## 5. Como usar essa informação

```text
Preciso de ordem + acesso por posição?      → List (ArrayList)
Preciso evitar duplicados e buscar rápido?  → Set (HashSet)
Preciso manter ordenado?                    → TreeSet / TreeMap
Preciso associar chave → valor?             → Map (HashMap)
```

Sempre consulte a documentação de complexidade antes de escolher uma estrutura para uma operação **muito repetida**.

## O que você precisa dominar (Aula 165)

- Diferença entre coleção ordenada e classificada.
- `Collection` x `Collections` e `Map` não ser `Collection`.
- Big-O: `O(1)`, `O(log n)`, `O(n)`, `O(n log n)`, `O(n²)`.
- Por que `HashSet.contains` é mais rápido que `ArrayList.contains`.
- Impacto de `equals`/`hashCode` no desempenho das coleções.

---

# Mapa mental do bloco

```text
Serialização
├── implements Serializable (marcadora)
├── ObjectOutputStream.writeObject / ObjectInputStream.readObject
├── transient → não grava
├── static → não grava
├── serialVersionUID → compatibilidade de versão
├── construtor NÃO executa ao ler
└── writeObject / readObject privados para casos especiais

equals / hashCode
├── == → referência;  equals → igualdade lógica
├── equals: null → this==o → classe → cast → atributos
├── hashCode: mesmo(s) atributo(s) do equals
├── equals true ⇒ hashCode igual
└── Objects.equals / Objects.hash

Big-O
└── O(1) < O(log n) < O(n) < O(n log n) < O(n²)
```

# Cola de bolso

| Situação | Lembre |
|---|---|
| Classe precisa ser gravada em arquivo | `implements Serializable` + `serialVersionUID` |
| Senha em objeto serializado | `transient` |
| Dois objetos "iguais" para o negócio | sobrescrever `equals` **e** `hashCode` |
| Quais campos no hash? | os mesmos do `equals` |
| Muitos `contains` | prefira `HashSet` |
| Escolher coleção | pense na operação mais frequente e no Big-O |
