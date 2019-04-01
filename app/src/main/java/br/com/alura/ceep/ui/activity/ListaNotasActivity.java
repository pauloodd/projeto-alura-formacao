package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {


    public static final String TITULO_APPBAR = "Notas";
    public static final String LINEAR_LAYOUT_MANAGER = "LinearLayoutManager";
    public static final String STAGGERED_GRID_LAYOUT_MANAGER = "StaggeredGridLayoutManager";

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO(this);
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoInsereNota(requestCode, data)) {

            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }

        }

        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (ehPosicaoValida(posicaoRecebida)) {
                    notaRecebida.setPosicao(posicaoRecebida);
                    altera(notaRecebida);
                }
            }
        }
    }

    private void altera(Nota nota) {
        new NotaDAO(this).altera(nota);
        adapter.altera(nota.getPosicao(), nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        new NotaDAO(this).alteraTodasPosicoesDepoisInsere();
        new NotaDAO(this).insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter, this));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_notas, menu);
        configuraLayoutManagerMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_lista_notas_layout:
                RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
                if(listaNotas.getLayoutManager() instanceof LinearLayoutManager){
                    item.setIcon(R.drawable.ic_action_linear);
                    StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                    listaNotas.setLayoutManager(staggeredManager);
                    salvaSharedPreferencesLayoutType(STAGGERED_GRID_LAYOUT_MANAGER);
                }else{
                    item.setIcon(R.drawable.ic_action_staggered);
                    LinearLayoutManager linearManager = new LinearLayoutManager(this);
                    listaNotas.setLayoutManager(linearManager);
                    salvaSharedPreferencesLayoutType(LINEAR_LAYOUT_MANAGER);
                }
                break;
            case R.id.menu_item_ajuda:
                Intent abreAjudaActivity = new Intent(ListaNotasActivity.this, FeedbackActivity.class);
                startActivity(abreAjudaActivity);
                break;
            default:
                Log.d("MenuItem:", item.getTitle()+"");
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private SharedPreferences.Editor salvaSharedPreferencesLayoutType(String type) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_text), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LayoutMode",type);
        editor.apply();
        return editor;
    }

    private void configuraLayoutManagerMenu(Menu menu) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_text), Context.MODE_PRIVATE);
        String layoutType = sharedPreferences.getString("LayoutMode", "LinearLayoutManager");
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        MenuItem item = menu.getItem(0);
        if(item != null && layoutType.equals(LINEAR_LAYOUT_MANAGER)){
            item.setIcon(R.drawable.ic_action_staggered);
            LinearLayoutManager linearManager = new LinearLayoutManager(this);
            listaNotas.setLayoutManager(linearManager);
        }else{
            item.setIcon(R.drawable.ic_action_linear);
            StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
            listaNotas.setLayoutManager(staggeredManager);
        }
    }


}
