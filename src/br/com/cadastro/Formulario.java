/**
 * 
 */
package br.com.cadastro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import br.com.cadastro.dao.AlunoDAO;
import br.com.cadastro.modelo.Aluno;

/**
 * @author Deivid
 *
 */
public class Formulario extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulario);
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
				alunoDAO.adicionar(aluno);
				alunoDAO.close();
				startActivity(new Intent(Formulario.this, ListaAlunos.class));
			}
		});
	}
	
}
