package br.com.cadastro;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONStringer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import br.com.cadastro.dao.AlunoDAO;
import br.com.cadastro.modelo.Aluno;

public class ListaAlunos extends Activity {
	
	static final private int ADD_NEW_TODO = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
//		ProgressDialog show = ProgressDialog.show(ListaAlunos.this, "Aguarde...", "Enviando dados!", true);        
        AlunoDAO alunoDAO = new AlunoDAO(this, "Aluno", null, 1);
        List<Aluno> alunos = alunoDAO.getLista();
        alunoDAO.close();

        ArrayAdapter<Aluno> adapterView = new ArrayAdapter<Aluno>(this,android.R.layout.simple_list_item_1, alunos);
        ListView listaAlunos = (ListView) findViewById(R.id.listaAlunos);
        Button botaoInserir = (Button) findViewById(R.id.botaoInserir);
        listaAlunos.setAdapter(adapterView);
        listaAlunos.setClickable(true);
        listaAlunos.setLongClickable(true);
        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long id) {
				return false;
			}
        	
		});
        
        registerForContextMenu(listaAlunos);
        
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int posicao,
					long id) {
				SharedPreferences preferences = getSharedPreferences("alunoselecionado", MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putInt("posicao", posicao);
				editor.commit();
				
				startActivity(new Intent(ListaAlunos.this, Formulario.class));
			}
        	
		});
        
        botaoInserir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ListaAlunos.this, Formulario.class));				
			}
		});
        
       Button botaoGaleria = (Button) findViewById(R.id.botaoGaleria);
       botaoGaleria.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(ListaAlunos.this, Galeria.class));
		}
	});
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, view, menuInfo);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	return super.onContextItemSelected(item);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE,
    	R.string.add_new);
    	itemAdd.setIcon(R.drawable.icon);
    	MenuItem sincronizar = menu.add("Sincronizar");
    	sincronizar.setIcon(R.drawable.icon);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if(item.getItemId() == 0){
    		Toast.makeText(ListaAlunos.this, "Você clicou no novoAluno", Toast.LENGTH_LONG).show();
    	}
    	if(item.getTitle().equals("Sincronizar")){
    		final ProgressDialog progressDialog = ProgressDialog.show(ListaAlunos.this, "Aguarde...", "Enviando dados", true);
    		final Toast aviso = Toast.makeText(ListaAlunos.this, "Dados enviados com sucessos", Toast.LENGTH_SHORT);
    		new Thread(new Runnable() {
    			String retorno;
				@Override
				public void run() {
					try{
						Thread.sleep(2000);
						AlunoDAO alunoDAO = new AlunoDAO(ListaAlunos.this);
						List<Aluno> alunos = alunoDAO.getLista();
						
						JSONStringer j = new JSONStringer();
						j.object()
						.key("alunos").array();
						for (Aluno aluno : alunos) {
							j.value(aluno);
						}
						j.endArray()
						.endObject();
						alunoDAO.close();
						Sicronismo sicronismo = new Sicronismo();
						retorno = sicronismo.enviarDado(j.toString());
						Log.i("Retorno", retorno);
					}catch (JSONException e) {
						Log.e("ERROR", "JSON", e);
					} catch (InterruptedException e) {
						Log.e("ERROR", "Thread", e);
					} catch (ClientProtocolException e) {
						Log.e("ERROR", "Protocolo", e);
					} catch (IOException e) {
						Log.e("ERROR", "Leitura", e);
					}
					
				}
			});
    		aviso.show();
    		progressDialog.dismiss();
    	}
    	return false;
    }
    
}