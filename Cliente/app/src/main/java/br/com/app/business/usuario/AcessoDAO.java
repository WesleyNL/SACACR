package br.com.app.business.usuario;

import java.util.LinkedList;

import br.com.app.Servidor;
import br.com.app.Sistema;

/**
 * Created by Wesley on 10/09/2016.
 */
public class AcessoDAO extends Acesso {

    public boolean logar(){
        return Servidor.acessar(this);
    }

    public boolean carregar(){
        return Servidor.carregarAcesso(this);
    }

    public boolean excluir(){
        return Servidor.excluirAcesso(this);
    }

    public boolean salvar(){
        return Servidor.salvarAcesso(this);
    }

    public LinkedList<Acesso> pesquisar(String filtro){
        LinkedList<Acesso> listaAcessos = Servidor.pesquisarAcessos(this, filtro);
        return removerUsuarioAcesso(listaAcessos);
    }

    private LinkedList<Acesso> removerUsuarioAcesso(LinkedList<Acesso> listaAcessos){
        for(int i = 0; i < listaAcessos.size(); i++){
            if(listaAcessos.get(i).getCodigo() == Sistema.USUARIO_ACESSO){
                listaAcessos.remove(i);
                return listaAcessos;
            }
        }
        return listaAcessos;
    }
}
