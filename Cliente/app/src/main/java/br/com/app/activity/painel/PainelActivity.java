package br.com.app.activity.painel;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import br.com.app.Sistema;
import br.com.app.activity.R;
import br.com.app.activity.usuario.DadosUsuarioActivity;
import br.com.app.business.painel.SistemaArduinoDAO;
import br.com.app.enums.EnmTelas;
import br.com.app.utils.Utils;

public class PainelActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel);

        if(Sistema.USUARIO_ACESSO == Sistema.USUARIO_ADMIN) {
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
            mTitle = getTitle();

            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        boolean criar = false;

        if(Sistema.USUARIO_ACESSO != Sistema.USUARIO_ADMIN){
            getMenuInflater().inflate(R.menu.menu_principal, menu);

            for(int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0);
                item.setTitle(spanString);
            }

            criar = true;
        }

        return criar;
    }

    @Override
    public boolean onMenuItemSelected(int panel, MenuItem item) {

        if(Sistema.USUARIO_ACESSO == Sistema.USUARIO_ADMIN){
            return super.onMenuItemSelected(panel, item);
        }

        switch (item.getItemId()) {
            case R.id.itmMinhaConta:
                Intent i = new Intent(this, DadosUsuarioActivity.class);
                i.putExtra("COD_USUARIO", Sistema.USUARIO_ACESSO);
                startActivity(i);
                break;
            case R.id.itmSobre:
                Utils.chamarActivity(this, EnmTelas.APP_SOBRE);
                break;
            default:
        }

        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private SistemaArduinoDAO objSistemaArduino;
        private static View viewResumo;

        private ProgressBar pgbLoading;
        private Switch sthPreferencia;
        private TextView lblNivel;
        private TextView lblValor;
        private TextView lblUltimoResponsavel;
        private ImageView imgReload;

        public static boolean rodando = true;

        private Thread threadCarregamento;

        public PlaceholderFragment() {
            objSistemaArduino = new SistemaArduinoDAO();
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onResume() {
            super.onResume();
            carregar(viewResumo);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_painel, container, false);

            carregar(rootView);
            viewResumo = rootView;
            pgbLoading = (ProgressBar) rootView.findViewById(R.id.pgbLoading);
            lblNivel = (TextView) rootView.findViewById(R.id.lblNivelAtual);
            lblValor = (TextView) rootView.findViewById(R.id.lblTotalGasto);
            lblUltimoResponsavel = (TextView) rootView.findViewById(R.id.lblUltimaAlteracao);
            imgReload = (ImageView) rootView.findViewById(R.id.btnReload);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((PainelActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void fechar(){
            Utils.chamarActivity(getActivity(), EnmTelas.APP_LOGIN);
        }

        public void carregar(final View view){

            Button btnFechar = (Button) view.findViewById(R.id.btnFechar);
            btnFechar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fechar();
                }
            });

            pgbLoading = (ProgressBar) view.findViewById(R.id.pgbLoading);
            pgbLoading.setVisibility(View.VISIBLE);
            pgbLoading.bringToFront();

            final Handler hRecycleView = new Handler();

            if(threadCarregamento != null && threadCarregamento.isAlive()){
                threadCarregamento.interrupt();
            }

            threadCarregamento = new Thread(new Runnable() {
                @Override
                public void run() {

                    hRecycleView.post(new Runnable() {
                        @Override
                        public void run() {

                            if (!objSistemaArduino.carregar()) {
                                Toast.makeText(getActivity(), "Não foi possível atualizar o painel", Toast.LENGTH_LONG).show();
                                pgbLoading.setVisibility(View.INVISIBLE);
                                return;
                            }

                            imgReload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.chamarActivity(getActivity(), EnmTelas.APP_PAINEL);
                                    getActivity().finish();
                                    return;
                                }
                            });

                            sthPreferencia = (Switch) view.findViewById(R.id.sthUtilizarReservatorio);
                            sthPreferencia.setOnClickListener(null);

                            if (objSistemaArduino.pesquisarParametros()) {
                                sthPreferencia.setChecked(objSistemaArduino.getPreferencia() == 1);
                                lblUltimoResponsavel.setText("Última alteração feita por: " + objSistemaArduino.getUltimoResponsavel());
                            } else {
                                Toast.makeText(getActivity(), "Não foi possível verificar o último responsável", Toast.LENGTH_LONG).show();
                            }

                            sthPreferencia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (objSistemaArduino.alterarPreferencia(isChecked)) {
                                        Toast.makeText(getActivity(), "Preferência alterada com sucesso", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Não foi possível alterar", Toast.LENGTH_LONG).show();
                                    }
                                    sthPreferencia.setChecked(objSistemaArduino.getPreferencia() == 1);
                                    carregar(view);
                                }
                            });

                            if (objSistemaArduino.getNivel() < 0) {
                                Toast.makeText(getActivity(), "Não foi possível atualizar o painel", Toast.LENGTH_LONG).show();
                            }

                            lblValor.setText("Total gasto para o mês atual: R$ " + objSistemaArduino.getValor());
                            lblNivel.setText("Nível do reservatório da chuva: " + (int) objSistemaArduino.getNivel() + "%");

                            sthPreferencia.setEnabled(true);

                            if (objSistemaArduino.getNivel() <= 5) {
                                sthPreferencia.setEnabled(false);
                                String text = "Nível do reservatório da chuva: ";
                                String text2 = text + (int) objSistemaArduino.getNivel() + "%";

                                Spannable spannable = new SpannableString(text2);
                                spannable.setSpan(new ForegroundColorSpan(Color.RED), text.length(), (text + (int) objSistemaArduino.getNivel() + "%").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                lblNivel.setText(spannable, TextView.BufferType.SPANNABLE);
                            }

                            String dadosGrafico = "https://chart.googleapis.com/chart?" +
                                    "cht=p3&" +
                                    "chs=290x70&" +
                                    "chd=t:" + objSistemaArduino.getPorcentagemChuva() + "," + objSistemaArduino.getPorcentagemConcessionaria() + "&" +
                                    "chco=33CCFF&" +
                                    "chdl=Chuva (" + objSistemaArduino.getConsumoChuva() + ")|Concessionária (" + objSistemaArduino.getConsumoConcessionaria() + ")";

                            WebView wvGrafico = (WebView) view.findViewById(R.id.wvGrafico);
                            wvGrafico.loadUrl(dadosGrafico);

                            pgbLoading.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });

            threadCarregamento.start();
        }
    }
}
