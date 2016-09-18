package br.com.app.business.painel;

import java.text.DecimalFormat;

/**
 * Created by Wesley on 11/09/2016.
 */
public class SistemaArduino {

    private byte preferencia = 1;
    private double valor = 22.38;
    private double nivel = -1;
    private double consumoConcessionaria = 0.1;
    private double consumoChuva = 0.1;
    private double porcentagemConcessionaria = 0.05;
    private double porcentagemChuva = 0.05;
    private int codigoResponsavel;
    private String ultimoResponsavel;

    private DecimalFormat format2Casas = new DecimalFormat("#.##");

    public byte getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(byte preferencia) {
        this.preferencia = preferencia;
    }

    public double getValor() {
        return Double.valueOf(format2Casas.format(valor));
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getNivel() {
        return nivel;
    }

    public void setNivel(double nivel) {
        this.nivel = nivel;
    }

    public double getConsumoConcessionaria() {
        return consumoConcessionaria;
    }

    public void setConsumoConcessionaria(double consumoConcessionaria) {
        this.consumoConcessionaria = consumoConcessionaria;
    }

    public double getConsumoChuva() {
        return consumoChuva;
    }

    public void setConsumoChuva(double consumoChuva) {
        this.consumoChuva = consumoChuva;
    }

    public double getPorcentagemConcessionaria() {
        return porcentagemConcessionaria;
    }

    public void setPorcentagemConcessionaria(double porcentagemConcessionaria) {
        this.porcentagemConcessionaria = porcentagemConcessionaria;
    }

    public double getPorcentagemChuva() {
        return porcentagemChuva;
    }

    public void setPorcentagemChuva(double porcentagemChuva) {
        this.porcentagemChuva = porcentagemChuva;
    }

    public int getCodigoResponsavel() {
        return codigoResponsavel;
    }

    public void setCodigoResponsavel(int codigoResponsavel) {
        this.codigoResponsavel = codigoResponsavel;
    }

    public String getUltimoResponsavel() {
        return ultimoResponsavel;
    }

    public void setUltimoResponsavel(String ultimoResponsavel) {
        this.ultimoResponsavel = ultimoResponsavel;
    }
}
