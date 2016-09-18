package br.com.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.app.Sistema;
import br.com.app.activity.R;
import br.com.app.activity.usuario.DadosUsuarioActivity;
import br.com.app.business.usuario.Acesso;

/**
 * Created by Wesley on 10/09/2016.
 */
public class PesquisarUsuariosAdapter extends RecyclerView.Adapter<PesquisarUsuariosAdapter.ViewHolder> {

    private Context context;
    private static List<Acesso> listaUsuarios;

    public PesquisarUsuariosAdapter(Context context, List<Acesso> listaDiscussoes) {
        this.context = context;
        this.listaUsuarios = listaDiscussoes;

        removerUsuarioAcesso();
    }

    public void removerUsuarioAcesso(){
        for(int i = 0; i < listaUsuarios.size(); i++){
            if(listaUsuarios.get(i).getCodigo() == Sistema.USUARIO_ACESSO){
                listaUsuarios.remove(i);
                return;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisar_usuario, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Acesso objAcesso = listaUsuarios.get(position);

        viewHolder.lblCodUsuario.setText("#" + objAcesso.getCodigo());
        viewHolder.lblUsuario.setText(objAcesso.getNome());

        viewHolder.setPosicao(position);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int posicao;

        public TextView lblCodUsuario;
        public TextView lblUsuario;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            itemLayoutView.setOnClickListener(this);

            lblCodUsuario = (TextView) itemLayoutView.findViewById(R.id.lblCodUsuario);
            lblUsuario = (TextView) itemLayoutView.findViewById(R.id.lblUsuario);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), DadosUsuarioActivity.class);
            i.putExtra("COD_USUARIO", listaUsuarios.get(posicao).getCodigo());
            view.getContext().startActivity(i);
        }

        public void setPosicao(int posicao) {
            this.posicao = posicao;
        }
    }
}
