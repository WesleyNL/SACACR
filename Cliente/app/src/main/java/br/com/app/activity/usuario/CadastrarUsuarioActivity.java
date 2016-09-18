package br.com.app.activity.usuario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.app.activity.R;
import br.com.app.business.usuario.AcessoDAO;

public class CadastrarUsuarioActivity extends Activity {

    private AcessoDAO objAcessoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        objAcessoDAO = new AcessoDAO();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void salvar(View view){

        EditText txtNome = (EditText) findViewById(R.id.txtNomeCadUsuario);
        objAcessoDAO.setNome(txtNome.getText().toString().trim());

        EditText txtLogin = (EditText) findViewById(R.id.txtLoginCadUsuario);
        objAcessoDAO.setUsuario(txtLogin.getText().toString().trim());

        EditText txtSenha = (EditText) findViewById(R.id.txtSenhaCadUsuario);
        objAcessoDAO.setSenha(txtSenha.getText().toString().trim());

        if(objAcessoDAO.getUsuario().contains(" ")){
            Toast.makeText(this, "Login inválido", Toast.LENGTH_LONG).show();
            return;
        }

        if(objAcessoDAO.getUsuario().equalsIgnoreCase(objAcessoDAO.getSenha())){
            Toast.makeText(this, "Usuário e senha não podem ser iguais", Toast.LENGTH_LONG).show();
            return;
        }

        if(objAcessoDAO.getNome().isEmpty() || objAcessoDAO.getUsuario().isEmpty() || objAcessoDAO.getSenha().isEmpty()){
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        dialogo.setTitle("Confirmar");
        dialogo.setMessage("Deseja realmente salvar?");
        dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                salvar();
            }
        });
        dialogo.setNegativeButton("Não", null);
        dialogo.show();
    }

    public void salvar(){
        if(objAcessoDAO.salvar()){
            Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, DadosUsuarioActivity.class);
            i.putExtra("COD_USUARIO", objAcessoDAO.getNovoCodigo());
            startActivity(i);
            finish();
        }else{
            Toast.makeText(this, "Login já cadastrado", Toast.LENGTH_LONG).show();
        }
    }
}
