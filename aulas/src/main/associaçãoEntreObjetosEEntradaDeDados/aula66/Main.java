package associaçãoEntreObjetosEEntradaDeDados.aula66;

public class Main {
    public static void main(String[] args) {
       Escola escola1 = new Escola("Gelvira");
       Escola escola2 = new Escola("Santa candida");
       Professor[] professors1 = new Professor[2];
       Professor[] professors2 = new Professor[1];

       Professor professor1 = new Professor("itachi");
       professor1.setEscola(escola1);
       professor1.imprimir();
       Professor professor2 = new Professor("kakachi");
        professor2.setEscola(escola1);
        professor2.imprimir();
       Professor professor3 = new Professor("Obito");
        professor3.setEscola(escola2);
        professor3.imprimir();

        professors1[0] = professor1;
        professors1[1] = professor2;
        professors2[0] = professor3;

        escola1.setProfessores(professors1);
        escola1.imprimir();

        escola2.setProfessores(professors2);
        escola2.imprimir();
    }
}


