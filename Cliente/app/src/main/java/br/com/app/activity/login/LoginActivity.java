package br.com.app.activity.login;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import br.com.app.Sistema;
import br.com.app.activity.R;
import br.com.app.business.usuario.AcessoDAO;
import br.com.app.enums.EnmTelas;
import br.com.app.utils.Utils;

public class LoginActivity extends Activity {

    private AcessoDAO objAcessoDAO;

    public static final String LEMBRAR_LOGIN = "LEMBRAR_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        objAcessoDAO = new AcessoDAO();

        SharedPreferences objSP = getSharedPreferences(LEMBRAR_LOGIN, MODE_PRIVATE);
        String login = objSP.getString("LOGIN", null);
        String senha = objSP.getString("SENHA", null);

        CheckBox chkLembrar = (CheckBox) findViewById(R.id.chkLembrar);
        chkLembrar.setChecked(false);

        if(login != null){
            chkLembrar.setChecked(true);

            EditText txtLogin = (EditText) findViewById(R.id.txtLogin);
            EditText txtSenha = (EditText) findViewById(R.id.txtSenha);

            txtLogin.setText(login);
            txtSenha.setText(senha);
        }
    }

    public void acessar(View view){

        EditText txtLogin = (EditText) findViewById(R.id.txtLogin);
        EditText txtSenha = (EditText) findViewById(R.id.txtSenha);

        objAcessoDAO.setUsuario(txtLogin.getText().toString().trim());
        objAcessoDAO.setSenha(txtSenha.getText().toString().trim());

        if(objAcessoDAO.getUsuario().isEmpty() || objAcessoDAO.getSenha().isEmpty()){
            Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show();
            return;
        }

        if(!objAcessoDAO.logar()){
            Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show();
            return;
        }

        CheckBox chkLembrar = (CheckBox) findViewById(R.id.chkLembrar);

        SharedPreferences.Editor objSP = getSharedPreferences(LEMBRAR_LOGIN, MODE_PRIVATE).edit();

        if(chkLembrar.isChecked()){
            objSP.putString("LOGIN", objAcessoDAO.getUsuario());
            objSP.putString("SENHA", objAcessoDAO.getSenha());
        }else{
            objSP.clear();
        }

        objSP.commit();

        Sistema.USUARIO_ACESSO = objAcessoDAO.getCodigo();
        Utils.chamarActivity(this, EnmTelas.APP_PAINEL);
    }

    public void fechar(View view){
        finish();
    }
}
