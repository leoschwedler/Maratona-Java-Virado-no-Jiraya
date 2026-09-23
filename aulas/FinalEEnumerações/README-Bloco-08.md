# Bloco 08 --- `final` e Enumerações

## Aulas

-   77 --- Final Modifier pt 01 --- Tipo primitivo
-   78 --- Modificador `final` pt 02 --- Tipo referência
-   79 --- Final Modifier pt 03 --- Classes e métodos
-   80 --- Enumeração pt 01 --- Introdução
-   81 --- Enumeração pt 02 --- Construtores e atributos
-   82 --- Enumeração pt 03 --- Sobrescrita de métodos
-   83 --- Enumeração pt 04 --- Busca por atributos

> Estude uma aula por vez. Faça os exercícios da aula e só depois
> avance.
>
> **Nova regra:** no final existe um **desafio integrador obrigatório**,
> envolvendo tudo que foi apresentado nas aulas 77--83.

------------------------------------------------------------------------

# Aula 77 --- `final` em tipo primitivo

`final` pode impedir que uma variável receba uma nova atribuição depois
de inicializada.

``` java
final int IDADE = 20;
```

Uma nova atribuição para essa variável não é permitida.

A ideia principal é separar:

-   valores que podem mudar;
-   valores que devem permanecer fixos.

------------------------------------------------------------------------

# Aula 78 --- `final` em tipo referência

Quando `final` é aplicado a uma referência:

``` java
final Pessoa pessoa = new Pessoa();
```

a referência não pode passar a apontar para outro objeto.

Isso **não significa que o objeto ficou imutável**.

Se a classe permitir:

``` java
pessoa.setNome("João");
```

o estado do objeto ainda pode mudar.

Pense:

``` text
pessoa ─────→ Objeto A
```

A referência não pode ser redirecionada para o Objeto B, mas o Objeto A
pode continuar sendo mutável.

------------------------------------------------------------------------

# Aula 79 --- `final` em classes e métodos

Uma classe `final` não pode ser estendida:

``` java
public final class Configuracao {
}
```

Um método `final` não pode ser sobrescrito por uma subclasse:

``` java
public final void executar() {
}
```

Isso se conecta diretamente ao bloco anterior:

``` text
classe normal  → pode ser estendida
classe final   → não pode ser estendida

método normal  → pode ser sobrescrito
método final   → não pode ser sobrescrito
```

------------------------------------------------------------------------

# Aula 80 --- Enumeração: introdução

A aula apresenta `enum` para representar um conjunto fechado de opções.

Um problema comum é usar `String`:

``` java
String tipo = "Pessoa Fisica";
```

Isso deixa espaço para inconsistências de escrita.

Com `enum`:

``` java
public enum TipoCliente {
    PESSOA_FISICA,
    PESSOA_JURIDICA
}
```

o atributo passa a ter um tipo próprio:

``` java
private TipoCliente tipoCliente;
```

A ideia principal:

> `enum` representa um conjunto definido de possibilidades.

------------------------------------------------------------------------

# Aula 81 --- Enumeração com construtores e atributos

Uma enumeração pode possuir atributos, construtor e métodos.

Exemplo conceitual:

``` java
public enum TipoCliente {
    PESSOA_FISICA("Pessoa Física"),
    PESSOA_JURIDICA("Pessoa Jurídica");

    private String nomeRelatorio;

    TipoCliente(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }
}
```

Cada constante possui seu próprio valor associado.

O ponto importante é que uma constante de enum pode carregar dados.

------------------------------------------------------------------------

# Aula 82 --- Enumeração com sobrescrita de métodos

As constantes de uma enumeração podem possuir comportamentos diferentes.

Em vez de concentrar tudo em vários `if` ou `switch`, o comportamento
pode ficar associado às próprias constantes.

A ideia é:

``` text
DEBITO  → comportamento A
CREDITO → comportamento B
```

A enumeração pode declarar um método abstrato e cada constante fornecer
sua implementação.

Isso conecta `enum` com um conceito que você acabou de estudar:
**sobrescrita**.

------------------------------------------------------------------------

# Aula 83 --- Busca por atributos

A aula mostra como encontrar uma constante de enumeração através de um
atributo.

As constantes podem ser obtidas com:

``` java
TipoCliente.values()
```

e então percorridas:

``` java
for (TipoCliente tipo : TipoCliente.values()) {
    // comparação
}
```

A ideia é transformar:

``` text
"Pessoa Física"
```

em:

``` text
TipoCliente.PESSOA_FISICA
```

A busca pode ser colocada em um método estático como:

``` java
buscarPorDescricao(...)
```

Se não encontrar uma constante correspondente, o código deve tratar esse
caso de forma definida.

------------------------------------------------------------------------

# Resumo

``` text
final em variável
      ↓
final em referência
      ↓
final em método
      ↓
final em classe
      ↓
enum
      ↓
enum com atributos
      ↓
enum com comportamento
      ↓
busca por atributo
```
