package br.com.pi.pi4;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.com.pi.pi4.entity.ParticipanteGrupo;
import br.com.pi.pi4.fragment.GroupListFragment;
import br.com.pi.pi4.rest.PIService;
import br.com.pi.pi4.rest.Rest;
import br.com.pi.pi4.util.GrupoUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupSelectionActivity extends AppCompatActivity  implements GroupListFragment.OnListFragmentInteractionListener {

    private static final String TAG = "GroupSelectionActivity";
    private GroupPageAdapter gpa;
    private String evento;
    private ProgressDialog loadGroupPD;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String evento = b.getString("evento");
        if (evento == null) {
            Log.e(TAG,"Evento não informado");
        }
        this.evento = evento;
        setContentView(R.layout.activity_group_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Escolha seu grupo!");

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        gpa = new GroupPageAdapter(getSupportFragmentManager());
        //gpa.addItem(GroupListFragment.newInstance(1));
        pager.setAdapter(gpa);

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);





        if (evento!=null) {
            loadGroupPD = ProgressDialog.show(GroupSelectionActivity.this, "", "Carregando grupos...", true);
            updateGroups();
        }
    }


    private void updateGroups () {
        Retrofit retrofit = Rest.getInstance().get();
        PIService service = retrofit.create(PIService.class);
       // List<ParticipanteGrupo> lpg = new ArrayList<ParticipanteGrupo>();

        Call<List<ParticipanteGrupo>> call;
        call = service.getGrupos(evento);
        call.enqueue(new Callback<List<ParticipanteGrupo>>() {
            @Override
            public void onResponse(Call<List<ParticipanteGrupo>> call, Response<List<ParticipanteGrupo>> response) {
                List<ParticipanteGrupo> lpg = new ArrayList<ParticipanteGrupo>();
                lpg = response.body();
                HashMap<String,ArrayList<ParticipanteGrupo>> grupos = GrupoUtil.getGrupos(lpg);
                Iterator<String> it = grupos.keySet().iterator();
                while (it.hasNext()) {
                    String tabName = it.next();
                    ArrayList<ParticipanteGrupo> participantes = grupos.get(tabName);
                    gpa.refreshGrupos(tabName,participantes);

                }
                gpa.notifyDataSetChanged();


                loadGroupPD.hide();
            }

            @Override
            public void onFailure(Call<List<ParticipanteGrupo>> call, Throwable t) {
                t.printStackTrace();
                Snackbar.make(GroupSelectionActivity.this.getWindow().getDecorView().getRootView(), "Servidor Indisponivel", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.e(TAG, "erro ao buscar dados dos grupos");
                loadGroupPD.hide();
            }


        });

        /*try {
            lpg = service.getGrupos(evento).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(this.getWindow().getDecorView().getRootView(), "Servidor Indisponivel", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Log.e(TAG,"erro ao buscar dados dos grupos");


        }*/

    }

    @Override
    public void onListFragmentInteraction(ParticipanteGrupo item) {
        Snackbar.make(GroupSelectionActivity.this.getCurrentFocus(), "item selecionado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
