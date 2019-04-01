package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.alura.ceep.R;

public class FeedbackActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR_FEEDBACK = "Feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle(TITULO_APPBAR_FEEDBACK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show();
        Intent resultadoFeedback = new Intent();
        setResult(Activity.RESULT_OK,resultadoFeedback);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
