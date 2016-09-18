package br.com.app.enums;

/**
 * Created by Wesley on 10/09/2016.
 */
public enum EnmTelas {

    APP_LOGIN(0),
    APP_PAINEL(1),
    APP_SOBRE(2),
    APP_SPLASHSCREEN(3),
    APP_CAD_USUARIO(4),
    APP_PESQ_USUARIO(5),
    APP_DADOS_USUARIO(6);

    public int getEnmTela;
    EnmTelas(int valor) {
        getEnmTela = valor;
    }
}
