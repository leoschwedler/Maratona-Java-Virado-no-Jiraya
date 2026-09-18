package encapsulamento_sobrecarga_construtores.aula56;

public class Pedido {
    private String cliente;
    private double valor;
    private boolean pago;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public double getValor() {
        return valor;
    }



    public boolean isPago() {
        return pago;
    }

    public void alterarEstadoPedido(boolean input){
        if(!input){
            System.out.println("Nao foi possivel alterar o estado do pedido");
            return;
        }
        this.pago = true;
        System.out.println("Estado do pedido alterado: ");
    }
}
