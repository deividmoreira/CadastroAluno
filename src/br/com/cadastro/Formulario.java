/**
 * 
 */
package br.com.cadastro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import br.com.cadastro.dao.AlunoDAO;
import br.com.cadastro.modelo.Aluno;

/**
 * @author Deivid
 *
 */
public class Formulario extends Activity {
	
	private static final int TIRAR_FOTO = 101;
	
	private String arquivo;
	private ImageButton ib;
	
	private Aluno aluno;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulario);
		
		SharedPreferences preferences = getSharedPreferences("alunoselecionado", MODE_PRIVATE);
		final int posicao = preferences.getInt("posicao", -1);
		
		if(posicao != -1){
			AlunoDAO alunoDAO = new AlunoDAO(Formulario.this, "Aluno", null, 1);
			Aluno aluno = alunoDAO.getAlunoPorId(posicao+1);
			alunoDAO.close();
			((EditText)findViewById(R.id.editNome)).setText(aluno.getNome());
			((EditText)findViewById(R.id.editEndereco)).setText(aluno.getEndereco());
			((EditText)findViewById(R.id.editTelefone)).setText(aluno.getTelefone());
			((RatingBar)findViewById(R.id.editNota)).setRating((float) aluno.getNota());
			((Button)findViewById(R.id.botaoInserir)).setText("Alterar");
		}
		
		Button botaoInserir = (Button) findViewById(R.id.botaoInserir);
		
		botaoInserir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText nome = (EditText) findViewById(R.id.editNome);
				EditText telefone = (EditText) findViewById(R.id.editTelefone);
				EditText site = (EditText) findViewById(R.id.editSite);
				RatingBar nota = (RatingBar) findViewById(R.id.editNota);
				EditText endereco = (EditText) findViewById(R.id.editEndereco);
				
				Aluno aluno = new Aluno();
				aluno.setNome(nome.getEditableText().toString());
				aluno.setTelefone(telefone.getEditableText().toString());
				aluno.setSite(site.getEditableText().toString());
				aluno.setNota(nota.getRating());
				aluno.setEndereco(endereco.getEditableText().toString());
								
				AlunoDAO alunoDAO = new AlunoDAO(Formulario.this);
				if(posicao != -1){
					aluno.setId(posicao+1);
					alunoDAO.alterar(aluno);
				}else{
					alunoDAO.adicionar(aluno);
				}
				
				alunoDAO.close();
				startActivity(new Intent(Formulario.this, ListaAlunos.class));
			}
		});
		
		ib = (ImageButton) findViewById(R.id.botaoImagem);
		ib.setImageResource(R.drawable.icon);
		ib.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				arquivo = Environment.getExternalStorageDirectory() + "/" +System.currentTimeMillis() + ".jpg";
				File file = new File(arquivo);
				Uri uri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, TIRAR_FOTO);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == TIRAR_FOTO){
			if(resultCode != RESULT_CANCELED){
				aluno.setFoto(arquivo);
				carregaImagem();
			}
		}
	}

	private void carregaImagem() {
		FileInputStream inputStream = null;
		Bitmap bitmap = null;
		try{
			inputStream = new FileInputStream(aluno.getFoto());
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(bitmap != null){
			bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
			ib.setImageBitmap(bitmap);
		}
		
		if(aluno.getFoto() != null){
			carregaImagem();
		}
	}
	
}
