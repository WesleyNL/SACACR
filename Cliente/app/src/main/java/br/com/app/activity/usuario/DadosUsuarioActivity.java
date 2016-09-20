package br.com.app.activity.usuario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.app.Sistema;
import br.com.app.activity.R;
import br.com.app.business.usuario.AcessoDAO;
import br.com.app.utils.FuncoesData;
import br.com.app.utils.Utils;

public class DadosUsuarioActivity extends Activity {

    private AcessoDAO objAcessoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuario);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        objAcessoDAO = new AcessoDAO();

        int codUsuario = Sistema.USUARIO_ACESSO;

        try {
            codUsuario = Integer.parseInt(getIntent().getExtras().get("COD_USUARIO").toString());
        } catch (Exception e){
            e.printStackTrace();
        }

        carregar(codUsuario);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void carregar(int codUsuario){

        objAcessoDAO.setCodigo(codUsuario);
        if(!objAcessoDAO.carregar()){
            Toast.makeText(this, "Não foi possível carregar os dados do usuário", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        EditText txtNome = (EditText) findViewById(R.id.txtDadosNome);
        txtNome.setText(objAcessoDAO.getNome());

        EditText txtLogin = (EditText) findViewById(R.id.txtDadosLogin);
        txtLogin.setText(objAcessoDAO.getUsuario());

        EditText txtSenha = (EditText) findViewById(R.id.txtDadosSenha);
        txtSenha.setText(objAcessoDAO.getSenha());

        EditText txtDataAcesso = (EditText) findViewById(R.id.txtDadosAcesso);
        txtDataAcesso.setText(objAcessoDAO.getDataAcesso() == null ? "Não houve acesso" : FuncoesData.formatDate(objAcessoDAO.getDataAcesso(), FuncoesData.DDMMYYYY_HHMMSS));

        EditText txtDataInclusao = (EditText) findViewById(R.id.txtDadosInclusao);
        txtDataInclusao.setText(FuncoesData.formatDate(objAcessoDAO.getDataInclusao(), FuncoesData.DDMMYYYY_HHMMSS));

        Button btnExcluir = (Button) findViewById(R.id.btnExcluir);

        if(Sistema.USUARIO_ACESSO != Sistema.USUARIO_ADMIN){
            txtNome.setEnabled(false);

            btnExcluir.setVisibility(View.INVISIBLE);

//            TextView lblDataAcesso = (TextView) findViewById(R.id.lblDataAcesso);
//            lblDataAcesso.setVisibility(View.INVISIBLE);
//            txtDataAcesso.setVisibility(View.INVISIBLE);
//
//            TextView lblDataInclusao = (TextView) findViewById(R.id.lblDadosInclusao);
//            lblDataInclusao.setVisibility(View.INVISIBLE);
//            txtDataInclusao.setVisibility(View.INVISIBLE);

        }else{
            txtNome.setEnabled(true);
            btnExcluir.setVisibility(View.VISIBLE);
        }

        if(codUsuario == Sistema.USUARIO_ADMIN){
            txtNome.setEnabled(false);
            btnExcluir.setVisibility(View.INVISIBLE);
        }

        if(!txtNome.isEnabled()){
            txtSenha.requestFocus();
        }

        if(btnExcluir.getVisibility() != View.VISIBLE) {
            Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
            btnSalvar.setBackground(getDrawable(R.drawable.btndefaultshape));
        }
    }

    public void salvar(View view){

        EditText txtNome = (EditText) findViewById(R.id.txtDadosNome);
        objAcessoDAO.setNome(Utils.removerAcento(txtNome.getText().toString()).trim());

        EditText txtSenha = (EditText) findViewById(R.id.txtDadosSenha);
        objAcessoDAO.setSenha(txtSenha.getText().toString().trim());

        if(objAcessoDAO.getUsuario().contains(" ") || !Utils.soTexto(objAcessoDAO.getUsuario()) || Utils.temCaractereEspecial(objAcessoDAO.getUsuario())){
            Toast.makeText(this, "Login inválido", Toast.LENGTH_LONG).show();
            return;
        }

        if(!Utils.soTexto(objAcessoDAO.getNome())){
            Toast.makeText(this, "Nome inválido", Toast.LENGTH_LONG).show();
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
        objAcessoDAO.setEdicao(true);
        if(objAcessoDAO.salvar()){
            Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_LONG).show();
            carregar(objAcessoDAO.getCodigo());
        }else{
            Toast.makeText(this, "Não foi possível salvar", Toast.LENGTH_LONG).show();
        }
    }

    public void excluir(View view) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Confirmar");
        dialogo.setMessage("Deseja realmente excluir?");
        dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                excluir();
            }
        });
        dialogo.setNegativeButton("Não", null);
        dialogo.show();
    }

    public void excluir(){
        if (!objAcessoDAO.excluir()){
            Toast.makeText(this, "Não foi possível excluir", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Usuário excluído com sucesso", Toast.LENGTH_LONG).show();
        finish();
    }
}
