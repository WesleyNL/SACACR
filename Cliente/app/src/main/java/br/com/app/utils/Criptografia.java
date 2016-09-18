package br.com.app.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Wesley on 15/09/2016.
 */
public class Criptografia {

    private static LinkedHashMap<String, String> hashTabela;

    private static void popularHashTabela(){

        hashTabela = new LinkedHashMap<String, String>();

        hashTabela.put("00", "B");
        hashTabela.put("01", "P");
        hashTabela.put("02", "H");
        hashTabela.put("03", "I");
        hashTabela.put("04", "J");
        hashTabela.put("05", "R");
        hashTabela.put("06", "K");
        hashTabela.put("07", "Q");
        hashTabela.put("08", "7");
        hashTabela.put("09", "M");
        hashTabela.put("010", "U");
        hashTabela.put("011", "C");

        hashTabela.put("10", "B");
        hashTabela.put("11", "P");
        hashTabela.put("12", "H");
        hashTabela.put("13", "I");
        hashTabela.put("14", "J");
        hashTabela.put("15", "R");
        hashTabela.put("16", "K");
        hashTabela.put("17", "Q");
        hashTabela.put("18", "7");
        hashTabela.put("19", "M");

        hashTabela.put("20", "I");
        hashTabela.put("21", "J");
        hashTabela.put("22", "R");
        hashTabela.put("23", "K");
        hashTabela.put("24", "Q");
        hashTabela.put("25", "7");
        hashTabela.put("26", "M");
        hashTabela.put("27", "U");
        hashTabela.put("28", "C");
        hashTabela.put("29", "D");

        hashTabela.put("30", "K");
        hashTabela.put("31", "Q");
        hashTabela.put("32", "7");
        hashTabela.put("33", "M");
        hashTabela.put("34", "U");
        hashTabela.put("35", "C");
        hashTabela.put("36", "D");
        hashTabela.put("37", "F");
        hashTabela.put("38", "N");
        hashTabela.put("39", "2");

        hashTabela.put("40", "M");
        hashTabela.put("41", "U");
        hashTabela.put("42", "C");
        hashTabela.put("43", "D");
        hashTabela.put("44", "F");
        hashTabela.put("45", "N");
        hashTabela.put("46", "2");
        hashTabela.put("47", "G");
        hashTabela.put("48", "L");
        hashTabela.put("49", "A");

        hashTabela.put("50", "D");
        hashTabela.put("51", "F");
        hashTabela.put("52", "N");
        hashTabela.put("53", "2");
        hashTabela.put("54", "G");
        hashTabela.put("55", "L");
        hashTabela.put("56", "A");
        hashTabela.put("57", "S");
        hashTabela.put("58", "B");
        hashTabela.put("59", "P");

        hashTabela.put("60", "2");
        hashTabela.put("61", "G");
        hashTabela.put("62", "L");
        hashTabela.put("63", "A");
        hashTabela.put("64", "S");
        hashTabela.put("65", "B");
        hashTabela.put("66", "P");
        hashTabela.put("67", "H");
        hashTabela.put("68", "I");
        hashTabela.put("69", "J");

        hashTabela.put("70", "A");
        hashTabela.put("71", "S");
        hashTabela.put("72", "B");
        hashTabela.put("73", "P");
        hashTabela.put("74", "H");
        hashTabela.put("75", "I");
        hashTabela.put("76", "J");
        hashTabela.put("77", "R");
        hashTabela.put("78", "K");
        hashTabela.put("79", "Q");

        hashTabela.put("80", "P");
        hashTabela.put("81", "H");
        hashTabela.put("82", "I");
        hashTabela.put("83", "J");
        hashTabela.put("84", "R");
        hashTabela.put("85", "K");
        hashTabela.put("86", "Q");
        hashTabela.put("87", "7");
        hashTabela.put("88", "M");
        hashTabela.put("89", "U");

        hashTabela.put("90", "J");
        hashTabela.put("91", "R");
        hashTabela.put("92", "K");
        hashTabela.put("93", "Q");
        hashTabela.put("94", "7");
        hashTabela.put("95", "M");
        hashTabela.put("96", "U");
        hashTabela.put("97", "C");
        hashTabela.put("98", "D");
        hashTabela.put("99", "F");

        hashTabela.put("100", "Q");
        hashTabela.put("101", "7");
        hashTabela.put("102", "M");
        hashTabela.put("103", "U");
        hashTabela.put("104", "C");
        hashTabela.put("105", "D");
        hashTabela.put("106", "F");
        hashTabela.put("107", "N");
        hashTabela.put("108", "2");
        hashTabela.put("109", "G");

        hashTabela.put("110", "U");
        hashTabela.put("111", "C");
        hashTabela.put("112", "D");
        hashTabela.put("113", "F");
        hashTabela.put("114", "N");
        hashTabela.put("115", "2");
        hashTabela.put("116", "G");
        hashTabela.put("117", "L");
        hashTabela.put("118", "A");
        hashTabela.put("119", "S");
    }

    public static String criptografar(String senhaDigitada) {

        int tamanho = senhaDigitada.length();

        if(tamanho == 0){
            return senhaDigitada;
        }

        String senhaCriptografada = "";

        popularHashTabela();

        try{
            for(int i=0; i<tamanho; i++){
                senhaCriptografada += hashTabela.get(String.valueOf(i+1) + senhaDigitada.charAt(i));
            }
        } catch (Exception e) {
            return senhaDigitada;
        }

        return hashTabela.get("0" + tamanho) + senhaCriptografada;
    }

    private static String getChave(char value, int col) throws Exception{
        try{
            for(Map.Entry<String, String> e : hashTabela.entrySet()){
                if(e.getValue().equalsIgnoreCase(String.valueOf(value)) && Integer.parseInt(e.getKey()) / 10 == col){
                    return e.getKey();
                }
            }
        } catch (Exception e) {
            throw new Exception ("Não foi possível pegar a chave da senha.");
        }

        throw new Exception ("Não foi possível pegar a chave.");
    }

    public static String descriptografar(String senhaCriptografada) throws Exception{

        senhaCriptografada = senhaCriptografada.trim();

        if(senhaCriptografada.isEmpty()){
            return senhaCriptografada;
        }

        popularHashTabela();

        String senhaDescriptografada = "";
        int tamDesc = Integer.parseInt(getChave(senhaCriptografada.charAt(0), 0)) % 10;

        try{
            if(tamDesc > senhaCriptografada.length() - 1){
                throw new Exception("Tamanho informado maior do que o tamanho real.");
            }

            senhaCriptografada = senhaCriptografada.substring(1, tamDesc + 1);

            for(int i=0; i<tamDesc; i++){
                senhaDescriptografada += String.valueOf(Integer.parseInt(getChave(senhaCriptografada.charAt(i), i+1)) % 10);
            }
            return senhaDescriptografada;
        } catch (Exception e) {
            return senhaDescriptografada;
        }
    }
}
