package br.com.cadastro;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	static final private int REMOVE_TODO = Menu.FIRST + 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
        
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
				Toast.makeText(ListaAlunos.this, "Posicão é: "+posicao, Toast.LENGTH_SHORT).show();
			}
        	
		});
        
        botaoInserir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ListaAlunos.this, Formulario.class));				
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
    	// Create and add new menu items.
    	MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE,
    	R.string.add_new);
    	MenuItem itemRem = menu.add(0, REMOVE_TODO, Menu.NONE,
    	R.string.remove);
    	// Assign icons
    	itemAdd.setIcon(R.drawable.icon);
    	itemRem.setIcon(R.drawable.icon);
    	// Allocate shortcuts to each of them.
    	itemAdd.setShortcut('0', 'a');
    	itemRem.setShortcut('1', 'r');
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if(item.getItemId() == 0){
    		Toast.makeText(ListaAlunos.this, "Você clicou no novoAluno", Toast.LENGTH_LONG).show();
    	}
    	return false;
    }
    
}