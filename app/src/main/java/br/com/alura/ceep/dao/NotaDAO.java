package br.com.alura.ceep.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.ceep.model.Nota;

public class NotaDAO extends SQLiteOpenHelper {

    public NotaDAO(Context context) {
        super(context, "Nota", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Notas " +
                "(id INTEGER PRIMARY KEY, " +
                "titulo TEXT NOT NULL, " +
                "descricao TEXT, " +
                "posicao INTEGER, " +
                "cor TEXT DEFAULT '#FFFFFF');";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Nota> todos() {

        String sql = "SELECT * FROM Notas ORDER BY posicao ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<Nota> notas = populaNotas(c);
        c.close();

        return notas;
    }

    public void insere(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosDaNota(nota);
        db.insert("Notas", null, dados);
    }

    public void altera(Nota nota){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosDaNota(nota);
        String[] params = {nota.getId()+""};
        db.update("Notas", dados, "id = ?", params);

    }


    public void alteraTodasPosicoesDepoisDelete(int posicaoRemovida){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "UPDATE Notas SET posicao = posicao -1 WHERE posicao > ?" + ";";
        db.execSQL(sql, new String[]{posicaoRemovida+""});

    }

    public void alteraTodasPosicoesDepoisInsere(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "UPDATE Notas SET posicao = posicao + 1 ;";
        db.execSQL(sql);

    }

    public void remove(Nota nota) {
        alteraTodasPosicoesDepoisDelete(nota.getPosicao());

        SQLiteDatabase db = getWritableDatabase();

        String[] params = {nota.getId()+""};
        db.delete("Notas", "id = ?", params);
    }

    public void removeTodos() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Notas", null, null);
    }

    @NonNull
    private ContentValues pegaDadosDaNota(Nota nota) {
        ContentValues dados = new ContentValues();
        dados.put("id", nota.getId());
        dados.put("titulo", nota.getTitulo());
        dados.put("descricao", nota.getDescricao());
        dados.put("posicao", nota.getPosicao());
        dados.put("cor",nota.getCor());
        return dados;
    }

    @NonNull
    private List<Nota> populaNotas(Cursor c) {
        List<Nota> notas = new ArrayList<Nota>();
        while (c.moveToNext()) {
            Nota nota = new Nota();
            nota.setId(c.getLong(c.getColumnIndex("id")));
            nota.setTitulo(c.getString(c.getColumnIndex("titulo")));
            nota.setDescricao(c.getString(c.getColumnIndex("descricao")));
            nota.setPosicao(c.getInt(c.getColumnIndex("posicao")));
            nota.setCor(c.getString(c.getColumnIndex("cor")));
            notas.add(nota);
        }
        return notas;
    }
}
