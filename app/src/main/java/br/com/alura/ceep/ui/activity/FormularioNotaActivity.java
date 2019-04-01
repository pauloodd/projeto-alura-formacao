package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaCoresAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListenerCor;
import br.com.alura.ceep.util.CoresEnum;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {


    public static final String TITULO_APPBAR_INSERE = "Insere nota";
    public static final String TITULO_APPBAR_ALTERA = "Altera nota";
    private int posicaoRecibida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;
    private ConstraintLayout formularioLayout;
    static final String STATE_COLOR = "corEscolhida";
    private String mCurrenteColor = "#FFFFFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR_INSERE);
        inicializaCampos();

        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)){
            setTitle(TITULO_APPBAR_ALTERA);
            Nota notaRecebida = (Nota) dadosRecebidos
                    .getSerializableExtra(CHAVE_NOTA);
            posicaoRecibida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            mCurrenteColor = notaRecebida.getCor();
            preencheCampos(notaRecebida);
        }

        RecyclerView lista = findViewById(R.id.lista_cores_nota);
        ListaCoresAdapter adapter = new ListaCoresAdapter(this, CoresEnum.todasCores());
        adapter.setOnItemClickListenerCor(new OnItemClickListenerCor() {
            @Override
            public void onItemClick(String cor, int posicao) {
                setmCurrenteColor(cor);
                formularioLayout.setBackgroundColor(Color.parseColor(cor));
            }
        });
        lista.setAdapter(adapter);


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_COLOR, mCurrenteColor);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrenteColor = savedInstanceState.getString(STATE_COLOR);
        ConstraintLayout form = findViewById(R.id.formulario_activity);
        form.setBackgroundColor(Color.parseColor(mCurrenteColor));
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
        formularioLayout.setBackgroundColor(Color.parseColor(mCurrenteColor));
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
        formularioLayout = findViewById(R.id.formulario_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(ehMenuSalvaNota(item)){
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecibida);
        setResult(Activity.RESULT_OK,resultadoInsercao);
    }

    @NonNull
    private Nota criaNota() {
        Nota notaCriada = new Nota(titulo.getText().toString(),
                descricao.getText().toString(), mCurrenteColor);
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)){
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            notaCriada.setId(notaRecebida.getId());
            notaCriada.setPosicao(notaRecebida.getPosicao());
        }
        return notaCriada;
    }

    private boolean ehMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }

    public String getmCurrenteColor() {
        return mCurrenteColor;
    }

    public void setmCurrenteColor(String mCurrenteColor) {
        this.mCurrenteColor = mCurrenteColor;
    }
}
