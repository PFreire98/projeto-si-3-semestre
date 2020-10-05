package venda;

import connection.ReadBD;
import produtos.Produto;
import tad.TADPilha;

import java.sql.SQLException;
import java.util.Scanner;

public class Pedido {

    private int quantidadeTotal;
    private Produto produto;
    private double valorTotal;
    private TADPilha fPedido = new TADPilha(15);
    private TADPilha fQuantidade = new TADPilha(15);

    public Pedido(){}

    public void setPedido(Produto produto){
        this.produto = produto;
        fPedido.inserir(produto.getId());
    }

    public void setQuantidade(int quantidade, double valor){
        this.quantidadeTotal += quantidade;
        fQuantidade.inserir(quantidade);
        this.valorTotal += (quantidade * valor);
    }

    public String getPedido() throws SQLException, ClassNotFoundException {
        String p ="=======================\n" +
                "  Carrinho de Compras\n" +
                "=======================\n";
        for(int i = 0; i < fPedido.tamanho(); i++) {
            Produto produto =  ReadBD.getProdutoById(fPedido.getElemento(i));
            p +=    "|ID: " + produto.getId() +"\n"+
                    "|Produto: " + produto.getNome() +"\n"+
                    "|Quantidade: " + fQuantidade.getElemento(i) +"\n"+
                    "|Valor: " + "R$"+ produto.getValorVenda() +"\n"+
                    "=======================\n";
        }
        p += "Valor Total: R$" + valorTotal + "\n";
        return p;
    }

    public void alteraPedido() throws SQLException, ClassNotFoundException {
        int menu = 0;
        Scanner in = new Scanner(System.in);
        do{
            System.out.println("[!] Qual é o id do produto que deseja alterar? ");
            int id = in.nextInt();
            int index = fPedido.procurar(id);
            if(index == 0){
                System.out.println("[!] Produto não está no pedido! ");
            }else {
                System.out.println("[!] O que voce gostaria de fazer?\n" +
                        "[1] Exluir o produto\n" +
                        "[2] Alterar o produto\n" +
                        "[3] Sair");
                menu = in.nextInt();

                if (menu == 1) {
                    Produto produto = ReadBD.getProdutoById(id);
                    double valor = produto.getValorVenda();
                    int quantidade = fQuantidade.getElemento(index);
                    this.valorTotal -= (quantidade * valor);

                    fPedido.removeElementoN(index);
                    fQuantidade.removeElementoN(index);
                    System.out.println(getPedido());
                }
                if(menu == 2){
                    int novoId;
                    int novaQuantidade;

                    double valor = ReadBD.getValorById(id);
                    int quantidade = fQuantidade.getElemento(index);
                    this.valorTotal -= (quantidade * valor);

                    System.out.println("[!] Qual é o id do novo produto? ");
                    novoId = in.nextInt();
                    System.out.println("[!] Qual é a quantidade do novo produto? ");
                    novaQuantidade = in.nextInt();

                    double valor2 = ReadBD.getValorById(novoId);
                    this.valorTotal += (novaQuantidade * valor2);

                    fPedido.alteraElementoN(index, novoId);
                    fQuantidade.alteraElementoN(index, novaQuantidade);
                    System.out.println(getPedido());
                }
            }
        }while(menu != 3);
    }
}
