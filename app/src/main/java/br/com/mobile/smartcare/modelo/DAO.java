package br.com.mobile.smartcare.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TI01N-2 on 17/11/2015.
 */
public class DAO {

    private SQLiteDatabase db;

    public DAO(Context context) {
        BdConnect conecta = new BdConnect(context);
        db = conecta.getWritableDatabase();
    }

    public void insertUsuario(Usuario usuario) {

        ContentValues values = new ContentValues();

        values.put("email", usuario.getEmail().toLowerCase());
        values.put("senha", usuario.getSenha());

        db.insert("usuario", null, values);

    }

    public List<Usuario> selectUsuario() {
        String[] colunas = new String[]{"_id", "email", "senha"};

        List<Usuario> lista = new ArrayList<Usuario>();


        Cursor cursor = db.query("usuario", colunas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

                lista.add(usuario);

            } while (cursor.moveToNext());
        }

        return lista;
    }


    public boolean emailExists (String email){
        List<Usuario> usuarios = this.selectUsuario();

        for (Usuario usuario : usuarios){
            if (usuario.getEmail().equalsIgnoreCase(email)){
                return true;
            }
        }
        return false;
    }

    public boolean login(String email, String senha) {
        String[] colunas = new String[]{"_id", "email", "senha"};

        email = email.toLowerCase();

        List<Usuario> lista = new ArrayList<Usuario>();

        Cursor cursor = db.query("usuario", colunas, "email = ? AND senha = ?", new String[]{email, senha}, null, null, null);

        return cursor.moveToFirst();

    }


    public void insertPaciente(Paciente paciente) {

        ContentValues values = new ContentValues();

        values.put("nome", paciente.getNome());
        values.put("idade", paciente.getIdade());
        values.put("telefone", paciente.getTelefone());
        values.put("leito", paciente.getLeito());
        values.put("doenca", paciente.getDoenca());
        values.put("situacao", paciente.getSituacao());

        db.insert("paciente", null, values);

    }

    public List<Paciente> selectPaciente() {
        String[] colunas = new String[]{"_id", "nome", "idade", "telefone", "leito", "doenca", "situacao"};

        List<Paciente> lista = new ArrayList<Paciente>();


        Cursor cursor = db.query("paciente", colunas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Paciente paciente = new Paciente(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );

                lista.add(paciente);

            } while (cursor.moveToNext());
        }

        return lista;
    }

    public void updatePaciente (Paciente paciente){

        ContentValues values = new ContentValues();
        values.put("telefone", paciente.getTelefone());
        values.put("leito", paciente.getLeito());
        values.put("situacao", paciente.getSituacao());


        int rows = db.update("paciente", values, "_id = ?", new String[]{"" + paciente.getId()});
        System.out.println("@@@@@@@@@@ROWS: " + rows);


    }

    public void deletePaciente(int id){
        db.delete("paciente", "_id = ?", new String[]{"" + id});
    }

    // CREATE TABLE paciente (

    // _id INTEGER PRIMARY KEY AUTOINCREMENT,
    // nome TEXT NOT NULL,
    // idade INTEGER NOT NULL,
    // telefone TEXT NOT NULL,
    // leito INTEGER NOT NULL,
    // doenca TEXT NOT NULL,
    // situacao TEXT NOT NULL

    // );

}
