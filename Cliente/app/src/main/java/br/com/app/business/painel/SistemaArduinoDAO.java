package br.com.app.business.painel;

import br.com.app.Servidor;

/**
 * Created by Wesley on 11/09/2016.
 */
public class SistemaArduinoDAO extends SistemaArduino{

    public boolean carregar(){
        boolean carregou = Servidor.pesquisarLogReservatorios(this);

        if(carregou){
            calcularValor();
        }

        return carregou;
    }

    /** Cálculo com base na tabela da SABESP para contrato Residencial / Normal */
    private void calcularValor(){
        double base = 22.38;
        double consumo = getConsumoConcessionaria() / 1000;
        double acrescimo = 0;

        if(consumo >= 11 && consumo <= 20){
            acrescimo *= 3.50;
        } else if(consumo >= 21 && consumo <= 50){
            acrescimo *= 8.75;
        } else{
            acrescimo *= 9.64;
        }

        setValor(base + acrescimo);
    }

    public boolean alterarPreferencia(boolean utilizarReservatorioChuva){
        return Servidor.alterarPreferencia(this, utilizarReservatorioChuva);
    }

    public boolean pesquisarParametros(){
        return Servidor.pesquisarParametros(this);
    }
}