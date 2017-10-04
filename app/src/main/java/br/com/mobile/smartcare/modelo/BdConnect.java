package br.com.mobile.smartcare.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TI01N-2 on 17/11/2015.
 */
public class BdConnect extends SQLiteOpenHelper {

    private static final String NOME_BASEDADOS = "smartcare";
    private static final int VERSAO_BD = 1;

    public BdConnect(Context context) {

        super(context, NOME_BASEDADOS, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE paciente ( _id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, idade INTEGER NOT NULL, telefone TEXT NOT NULL, leito INTEGER NOT NULL, doenca TEXT NOT NULL, situacao TEXT NOT NULL);");
        db.execSQL("CREATE TABLE usuario (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT UNIQUE NOT NULL, senha TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE paciente");
        db.execSQL("DROP TABLE usuario");
        onCreate(db);
    }
}