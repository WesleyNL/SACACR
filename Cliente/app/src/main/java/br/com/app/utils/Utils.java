package br.com.app.utils;

import android.app.Activity;
import android.content.Intent;

import java.security.MessageDigest;

import br.com.app.activity.login.LoginActivity;
import br.com.app.activity.painel.PainelActivity;
import br.com.app.activity.splashscreen.AppSplashScreenActivity;
import br.com.app.activity.sobre.AppSobreActivity;
import br.com.app.activity.usuario.CadastrarUsuarioActivity;
import br.com.app.activity.usuario.DadosUsuarioActivity;
import br.com.app.activity.usuario.PesquisarUsuarioActivity;
import br.com.app.enums.EnmTelas;

/**
 * Created by Wesley on 10/09/2016.
 */
public class Utils {

    public static void chamarActivity(Activity activity, EnmTelas enmActivity) {
        chamarActivity(activity, enmActivity, "", false);
    }

    public static void chamarActivity(Activity activity, EnmTelas enmActivity, String extras, boolean valExtras) {
        Intent i = new Intent();
        Class classe = null;

        try {
            switch (enmActivity) {
                case APP_SOBRE:
                    classe = AppSobreActivity.class;
                    break;
                case APP_SPLASHSCREEN:
                    classe = AppSplashScreenActivity.class;
                    break;
                case APP_PAINEL:
                    classe = PainelActivity.class;
                    break;
                case APP_LOGIN:
                    classe = LoginActivity.class;
                    break;
                case APP_CAD_USUARIO:
                    classe = CadastrarUsuarioActivity.class;
                    break;
                case APP_DADOS_USUARIO:
                    classe = DadosUsuarioActivity.class;
                    break;
                case APP_PESQ_USUARIO:
                    classe = PesquisarUsuarioActivity.class;
                    break;
            }

            i.setClass(activity, classe);

            if (!extras.equals("")) {
                i.putExtra(extras, valExtras);
            }

            activity.startActivity(i);

            if (enmActivity == EnmTelas.APP_LOGIN || enmActivity == EnmTelas.APP_PAINEL) {
                activity.finish();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean soNumeros(String texto){

        if(texto == null || texto.isEmpty()) {
            return false;
        }

        for (char letra : texto.toCharArray()) {
            if (letra < '0' || letra > '9') {
                return false;
            }
        }

        return true;
    }
}