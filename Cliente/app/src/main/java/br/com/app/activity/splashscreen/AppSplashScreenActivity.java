package br.com.app.activity.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import br.com.app.activity.R;
import br.com.app.activity.login.LoginActivity;

/**
 * Created by Wesley on 10/09/2016.
 */
public class AppSplashScreenActivity extends Activity {

    private final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_splash_screen);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorStatusBar_logotipo));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!temConexao()){
            Toast.makeText(this, "Sem conexão", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        iniciarAplicacao();
    }

    public boolean temConexao(){
        ConnectivityManager objConexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return objConexao.getActiveNetworkInfo() != null && objConexao.getActiveNetworkInfo().isAvailable() && objConexao.getActiveNetworkInfo().isConnected();
    }

    private void iniciarAplicacao() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(AppSplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}