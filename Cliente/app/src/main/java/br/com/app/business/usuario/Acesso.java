package br.com.app.business.usuario;

import java.util.Date;

/**
 * Created by Wesley on 10/09/2016.
 */
public class Acesso {

    private int codigo;
    private int novoCodigo;
    private String nome;
    private String usuario;
    private String senha;
    private byte nivel;
    private Date dataAcesso;
    private Date dataInclusao;
    private boolean edicao;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getNovoCodigo() {
        return novoCodigo;
    }

    public void setNovoCodigo(int novoCodigo) {
        this.novoCodigo = novoCodigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public byte getNivel() {
        return nivel;
    }

    public void setNivel(byte nivel) {
        this.nivel = nivel;
    }

    public Date getDataAcesso() {
        return dataAcesso;
    }

    public void setDataAcesso(Date dataAcesso) {
        this.dataAcesso = dataAcesso;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public boolean isEdicao() {
        return edicao;
    }

    public void setEdicao(boolean edicao) {
        this.edicao = edicao;
    }
}
