package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import br.com.alura.ceep.R;

public class SplashActivity  extends Activity {

    private Handler mWaitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_text), Context.MODE_PRIVATE);
        boolean mostrouApresentacao = sharedPreferences.getBoolean("ApresentacaoMostrada", false);

        if(!mostrouApresentacao){
            mWaitHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_text), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("ApresentacaoMostrada",true);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), ListaNotasActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            }, 2000);  // Give a 2 seconds delay.
        }else{
            try {
                Intent intent = new Intent(getApplicationContext(), ListaNotasActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
