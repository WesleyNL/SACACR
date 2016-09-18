package br.com.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import br.com.app.business.painel.SistemaArduino;
import br.com.app.business.usuario.Acesso;
import br.com.app.utils.Criptografia;
import br.com.app.utils.FuncoesData;

/**
 * Created by Wesley on 11/09/2016.
 */
public class Servidor {

    public static String SERVIDOR_URL_PADRAO = "http://192.168.25.10:1337/";

    public static String ACAO_ACESSO = "acesso";
    public static String ACAO_SISTEMA_ARDUINO = "sistemaarduino";

    public static String METODO_ACESSAR = "acessar";
    public static String METODO_SALVAR = "salvar";
    public static String METODO_EXCLUIR = "excluir";
    public static String METODO_PESQUISAR = "pesquisar";
    public static String METODO_PESQUISAR_GERAL = "pesquisarGeral";
    public static String METODO_ALTERAR_PREFERENCIA = "alterarPreferencia";
    public static String METODO_PESQUISAR_PARAMETROS = "pesquisarParametros";

    public static String CAMPO_CODIGO = "codigo";
    public static String CAMPO_LOGIN = "login";
    public static String CAMPO_SENHA = "senha";
    public static String CAMPO_NOME = "nome";
    public static String CAMPO_DATA_INCLUSAO = "data_inclusao";
    public static String CAMPO_DATA_ACESSO  = "data_acesso";
    public static String CAMPO_OPERACAO = "operacao";
    public static String CAMPO_FILTRO = "filtro";
    public static String CAMPO_PREFERENCIA = "preferencia";
    public static String CAMPO_NIVEL = "nivel";
    public static String CAMPO_QTD_CONCESSIONARIA = "qtd_concessionaria";
    public static String CAMPO_QTD_CHUVA = "qtd_chuva";
    public static String CAMPO_RESPONSAVEL = "responsavel";
    public static String CAMPO_NOME_RESPONSAVEL = "nome_responsavel";
    public static String CAMPO_DATA_INICIAL = "data_inicial";
    public static String CAMPO_DATA_FINAL = "data_final";
    public static String CAMPO_NIVEL_CHUVA = "nivel_chuva";

    public static String CAMPO_RETORNO = "retorno";
    public static String CAMPO_CONTEUDO = "conteudo";

    public static String REQUISICAO_GET = "GET";
    public static String REQUISICAO_POST = "POST";
    public static String REQUISICAO_SUCESSO = "0";
    public static String REQUISICAO_ERRO = "1";
    public static String REQUISICAO_METODO_SEPARADOR = "?";
    public static String REQUISICAO_URL_SEPARADOR = "/";

    private static String getUrlRequisicao(String acao, String metodo){
        return SERVIDOR_URL_PADRAO + acao + REQUISICAO_URL_SEPARADOR + metodo + REQUISICAO_METODO_SEPARADOR;
    }

    private static String formatarDadosRequisicao(String chave, String valor){
        return chave + "=" + valor + "&";
    }

    public static boolean acessar(Acesso objAcesso){
        String url = getUrlRequisicao(ACAO_ACESSO, METODO_ACESSAR);
        url += formatarDadosRequisicao(CAMPO_LOGIN, objAcesso.getUsuario());
        url += formatarDadosRequisicao(CAMPO_SENHA, Criptografia.criptografar(objAcesso.getSenha()));

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);

            boolean acessou = objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);

            if(acessou){
                objAcesso.setCodigo(Integer.parseInt(objJsonObject.get(CAMPO_CODIGO).toString().trim()));
            }

            return acessou;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean carregarAcesso(Acesso objAcesso){
        String url = getUrlRequisicao(ACAO_ACESSO, METODO_PESQUISAR);
        url += formatarDadosRequisicao(CAMPO_CODIGO, String.valueOf(objAcesso.getCodigo()));

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);

            boolean carregou = objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);

            if(carregou){
                JSONArray objJsonArray = objJsonObject.getJSONArray(CAMPO_CONTEUDO);
                objAcesso.setCodigo(Integer.parseInt(objJsonArray.getJSONObject(0).get(CAMPO_CODIGO.toUpperCase()).toString().trim()));
                objAcesso.setNome(objJsonArray.getJSONObject(0).get(CAMPO_NOME.toUpperCase()).toString().trim());
                objAcesso.setUsuario(objJsonArray.getJSONObject(0).get(CAMPO_LOGIN.toUpperCase()).toString().trim());
                objAcesso.setSenha(Criptografia.descriptografar(objJsonArray.getJSONObject(0).get(CAMPO_SENHA.toUpperCase()).toString().trim()));
                objAcesso.setDataInclusao(FuncoesData.toDate(objJsonArray.getJSONObject(0).get(CAMPO_DATA_INCLUSAO.toUpperCase()).toString().trim(), FuncoesData.YYYYMMDD_HHMMSS));
                if(objJsonArray.getJSONObject(0).get(CAMPO_DATA_ACESSO.toUpperCase()).toString().trim().equalsIgnoreCase("null")){
                    objAcesso.setDataAcesso(null);
                }else {
                    objAcesso.setDataAcesso(FuncoesData.toDate(objJsonArray.getJSONObject(0).get(CAMPO_DATA_ACESSO.toUpperCase()).toString().trim(), FuncoesData.YYYYMMDD_HHMMSS));
                }
            }

            return carregou;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean salvarAcesso(Acesso objAcesso){
        String url = getUrlRequisicao(ACAO_ACESSO, METODO_SALVAR);
        url += formatarDadosRequisicao(CAMPO_CODIGO, String.valueOf(objAcesso.getCodigo()));
        url += formatarDadosRequisicao(CAMPO_NOME, objAcesso.getNome().replace(" ", ".-."));
        url += formatarDadosRequisicao(CAMPO_LOGIN, objAcesso.getUsuario());
        url += formatarDadosRequisicao(CAMPO_SENHA, Criptografia.criptografar(objAcesso.getSenha()));
        url += formatarDadosRequisicao(CAMPO_OPERACAO, String.valueOf(objAcesso.isEdicao() ? 1 : 0));

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);

            boolean salvou = objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);

            if (salvou) {
                if(objJsonObject.has("NOVO_CODIGO")){
                    objAcesso.setNovoCodigo(Integer.parseInt(objJsonObject.get("NOVO_CODIGO").toString()));
                }
            }

            return salvou;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean excluirAcesso(Acesso objAcesso){
        String url = getUrlRequisicao(ACAO_ACESSO, METODO_EXCLUIR);
        url += formatarDadosRequisicao(CAMPO_CODIGO, String.valueOf(objAcesso.getCodigo()));

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);
            return objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static LinkedList<Acesso> pesquisarAcessos(Acesso objAcesso, String filtro){
        String url = getUrlRequisicao(ACAO_ACESSO, METODO_PESQUISAR_GERAL);
        url += formatarDadosRequisicao(CAMPO_FILTRO, filtro.trim());

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);

            boolean carregou = objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);

            LinkedList<Acesso> listaAcessos = new LinkedList<>();

            if(carregou){
                Acesso objAcessoPesquisado = null;
                JSONArray objJsonArray = objJsonObject.getJSONArray(CAMPO_CONTEUDO);
                for(int i = 0; i < objJsonArray.length(); i++){
                    objJsonObject = objJsonArray.getJSONObject(i);
                    objAcessoPesquisado = new Acesso();
                    objAcessoPesquisado.setCodigo(Integer.parseInt(objJsonObject.get(CAMPO_CODIGO.toUpperCase()).toString().trim()));
                    objAcessoPesquisado.setNome(objJsonObject.get(CAMPO_NOME.toUpperCase()).toString().trim());
                    listaAcessos.add(objAcessoPesquisado);
                }
            }
            return  listaAcessos;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean alterarPreferencia(SistemaArduino objSistemaArduino, boolean utilizarReservatorioChuva){
        String url = getUrlRequisicao(ACAO_SISTEMA_ARDUINO, METODO_ALTERAR_PREFERENCIA);
        url += formatarDadosRequisicao(CAMPO_PREFERENCIA, String.valueOf(utilizarReservatorioChuva ? 1 : 0));
        url += formatarDadosRequisicao(CAMPO_LOGIN, String.valueOf(Sistema.USUARIO_ACESSO));

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);

            boolean alterou = objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);

            if(alterou){
                objSistemaArduino.setPreferencia(utilizarReservatorioChuva ? (byte)1 : (byte)0);
            }
            return alterou;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean pesquisarParametros(SistemaArduino objSistemaArduino){
        String url = getUrlRequisicao(ACAO_SISTEMA_ARDUINO, METODO_PESQUISAR_PARAMETROS);

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);

            boolean carregou = objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);

            if(carregou){
                JSONArray objJsonArray = objJsonObject.getJSONArray(CAMPO_CONTEUDO);
                objSistemaArduino.setCodigoResponsavel(Integer.parseInt(objJsonArray.getJSONObject(0).get(CAMPO_RESPONSAVEL.toUpperCase()).toString().trim()));
                objSistemaArduino.setUltimoResponsavel(objJsonArray.getJSONObject(0).get(CAMPO_NOME_RESPONSAVEL.toUpperCase()).toString().trim());
                objSistemaArduino.setPreferencia(Byte.parseByte(objJsonArray.getJSONObject(0).get(CAMPO_PREFERENCIA.toUpperCase()).toString().trim()));
            }

            return carregou;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean pesquisarLogReservatorios(SistemaArduino objSistemaArduino){
        String url = getUrlRequisicao(ACAO_SISTEMA_ARDUINO, METODO_PESQUISAR);
        url += formatarDadosRequisicao(CAMPO_DATA_INICIAL, FuncoesData.formatDate(FuncoesData.getPrimeiroDia(), FuncoesData.YYYYMMDD));
        url += formatarDadosRequisicao(CAMPO_DATA_FINAL, FuncoesData.formatDate(FuncoesData.getUltimoDia(), FuncoesData.YYYYMMDD));

        try {
            JSONObject objJsonObject = getJSONObjectFromURL(url, REQUISICAO_GET);

            boolean carregou = objJsonObject.get(CAMPO_RETORNO).toString().trim().equals(REQUISICAO_SUCESSO);

            if(carregou){
                JSONArray objJsonArray = objJsonObject.getJSONArray(CAMPO_CONTEUDO);
                if(!objJsonArray.getJSONObject(0).get(CAMPO_QTD_CHUVA.toUpperCase()).toString().trim().equals("null")){
                    objSistemaArduino.setConsumoConcessionaria(Double.parseDouble(objJsonArray.getJSONObject(0).get(CAMPO_QTD_CONCESSIONARIA.toUpperCase()).toString().trim()));
                    objSistemaArduino.setConsumoChuva(Double.parseDouble(objJsonArray.getJSONObject(0).get(CAMPO_QTD_CHUVA.toUpperCase()).toString().trim()));
                    objSistemaArduino.setNivel(Double.parseDouble(objJsonArray.getJSONObject(0).get(CAMPO_NIVEL_CHUVA.toUpperCase()).toString().trim()));

                    double consumoTotal = objSistemaArduino.getConsumoConcessionaria() + objSistemaArduino.getConsumoChuva();
                    double pConc = objSistemaArduino.getConsumoConcessionaria() <= 0 ? 0.1 : objSistemaArduino.getConsumoConcessionaria();
                    double pChuva = objSistemaArduino.getConsumoChuva() <= 0 ? 0.1 : objSistemaArduino.getConsumoChuva();
                    objSistemaArduino.setPorcentagemConcessionaria((pConc / consumoTotal) * 100);
                    objSistemaArduino.setPorcentagemChuva((pChuva / consumoTotal) * 100);
                }else{
                    objSistemaArduino.setConsumoConcessionaria(0.1);
                    objSistemaArduino.setConsumoChuva(0.1);
                    objSistemaArduino.setNivel(-1);
                    objSistemaArduino.setPorcentagemConcessionaria(0.05);
                    objSistemaArduino.setPorcentagemChuva(0.05);
                }
            }

            return carregou;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject getJSONObjectFromURL(String urlFormatada, String tipoRequisicao) throws IOException, JSONException {

        try {
            URL url = new URL(urlFormatada);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod(tipoRequisicao);
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            return new JSONObject(sb.toString());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
