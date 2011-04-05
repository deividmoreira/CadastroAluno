/**
 * 
 */
package br.com.cadastro.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.cadastro.modelo.Aluno;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Deivid
 *
 */
public class AlunoDAO extends SQLiteOpenHelper {

	private static int VERSION = 1;
	private static String TABELA = "Aluno";
	private static String[] COLS = {"id", "endereco", "foto", "nome", "nota", "telefone"};
	
	public AlunoDAO(Context context){
		super(context, TABELA, null, VERSION);
	}
	
	public AlunoDAO(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE "+TABELA+
		"(ID INTEGER PRIMARY KEY, "+
		"ENDERECO TEXT, "+
		"FOTO TEXT, "+
		"NOME TEXT UNIQUE NOT NULL, "+
		"NOTA REAL, "+
		"SITE TEXT, "+
		"TELEFONE TEXT);";
		try{
			db.execSQL(sql);	
		}catch (SQLiteException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+ AlunoDAO.TABELA);
		this.onCreate(db);
	}
	
	public void adicionar(Aluno aluno){
		try{
			ContentValues values = new ContentValues();
			values.put("nome", aluno.getNome());
			values.put("nota", aluno.getNota());
			values.put("endereco", aluno.getEndereco());
			values.put("telefone", aluno.getTelefone());
//			values.put("site", aluno.getSite());
			getWritableDatabase().insert(TABELA, null, values);
		}catch (SQLiteException e) {
			e.getCause();
		}
	}
	
	public List<Aluno> getLista(){
		List<Aluno> alunos = new ArrayList<Aluno>();
		try{
			Cursor cursor = getWritableDatabase().query(TABELA, COLS, null, null, null, null, null);
			while (cursor.moveToNext()) {
				Aluno aluno = new Aluno();
				aluno.setId(cursor.getInt(0));
				aluno.setEndereco(cursor.getString(1));
				aluno.setFoto(cursor.getString(2));
				aluno.setNome(cursor.getString(3));
				aluno.setNota(cursor.getDouble(4));
//				aluno.setSite(cursor.getString(5));
				aluno.setTelefone(cursor.getString(5));
				alunos.add(aluno);
			}
			cursor.close();
		}catch (SQLiteException e) {
			e.printStackTrace();
		}

		return alunos;
	}

	public Aluno getAlunoPorId(int posicao) {
		Cursor cursor = getWritableDatabase().query(TABELA, COLS, "id=?", new String[]{""+posicao}, null, null, null);
		
		cursor.moveToFirst();
		
		Aluno aluno = new Aluno();
		aluno.setId(0);
		aluno.setEndereco(cursor.getString(1));
		aluno.setFoto(cursor.getString(2));
		aluno.setNome(cursor.getString(3));
		aluno.setNota(cursor.getDouble(4));
		aluno.setTelefone(cursor.getString(5));
		
		cursor.close();
		
		return aluno;
	}

	public void alterar(Aluno aluno) {
		ContentValues values = new ContentValues();
		values.put("nome", aluno.getNome());
		values.put("nota", aluno.getNota());
		values.put("foto", aluno.getFoto());
		values.put("telefone", aluno.getTelefone());
		values.put("endereco", aluno.getEndereco());
		getWritableDatabase().update(TABELA, values, "id=?", new String[]{""+aluno.getId()});
	}

}
