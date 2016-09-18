package br.com.app.activity.usuario;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import br.com.app.activity.R;
import br.com.app.adapter.PesquisarUsuariosAdapter;
import br.com.app.business.usuario.Acesso;
import br.com.app.business.usuario.AcessoDAO;

public class PesquisarUsuarioActivity extends Activity {

    private AcessoDAO objAcessoDAO;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private EditText txtPesquisarUsuario;
    private TextView lblResultados;
    private TextView lblContResultados;

    private ProgressBar pgbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_usuario);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        txtPesquisarUsuario = (EditText) findViewById(R.id.txtPesquisarUsuario);
        lblResultados = (TextView) findViewById(R.id.lblResultados);
        lblContResultados = (TextView) findViewById(R.id.lblUsuariosEncontrados);

        lblResultados.setVisibility(View.INVISIBLE);
        lblContResultados.setVisibility(View.INVISIBLE);

        pgbLoading = (ProgressBar)findViewById(R.id.pgbLoading);

        objAcessoDAO = new AcessoDAO();

        pesquisar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        pesquisar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void filtrar(View view){
        EditText txtFiltro = (EditText) findViewById(R.id.txtPesquisarUsuario);
        pesquisar(txtFiltro.getText().toString().trim());
    }

    public void pesquisar(){
        pesquisar("");
    }

    public void pesquisar(final String filtro){

        pgbLoading = (ProgressBar)findViewById(R.id.pgbLoading);
        pgbLoading.setVisibility(View.VISIBLE);
        pgbLoading.bringToFront();

        final Context context = this;
        final Handler hRecycleView = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final LinkedList<Acesso> listaUsuarios = objAcessoDAO.pesquisar(filtro);

                hRecycleView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(listaUsuarios == null || listaUsuarios.isEmpty()){
                            Toast.makeText(context, "Usuários não encontrados", Toast.LENGTH_LONG).show();
                            if(listaUsuarios == null) {
                                pgbLoading.setVisibility(View.INVISIBLE);
                                return;
                            }
                        }

                        long cont = listaUsuarios.size();

                        lblResultados.setVisibility(View.VISIBLE);
                        lblResultados.setText("Resultados para '" + txtPesquisarUsuario.getText().toString().trim() + "'");
                        if(txtPesquisarUsuario.getText().toString().trim().isEmpty()){
                            lblResultados.setVisibility(View.INVISIBLE);
                        }

                        lblContResultados.setVisibility(View.VISIBLE);
                        lblContResultados.setText(getString(R.string.cont_discussoes, cont) + " " + (cont == 1 ? "Usuário Encontrado" : "Usuários Encontrados"));

                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        recyclerView.setVisibility(View.INVISIBLE);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));

                        adapter = new PesquisarUsuariosAdapter(context, listaUsuarios);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);

                        pgbLoading.setVisibility(View.INVISIBLE);

                        txtPesquisarUsuario.setText("");
                    }
                });
            }
        }).start();
    }
}
