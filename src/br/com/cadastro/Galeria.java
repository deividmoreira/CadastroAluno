/**
 * 
 */
package br.com.cadastro;

import java.util.List;

import br.com.cadastro.dao.AlunoDAO;
import br.com.cadastro.modelo.Aluno;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * @author Deivid
 *
 */
public class Galeria extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.galeria);
		
		AlunoDAO alunoDAO = new AlunoDAO(this);
		final List<Aluno> alunos = alunoDAO.getLista();
		alunoDAO.close();
		
		((Gallery)findViewById(R.id.gallery)).setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView i = new ImageView(Galeria.this);
				i.setImageResource(R.drawable.icon);
				Aluno aluno = alunos.get(position);
				if(aluno.getFoto() != null){
					Bitmap bitmap = BitmapFactory.decodeFile(aluno.getFoto());
					bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
					i.setImageBitmap(bitmap);
				}
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				i.setLayoutParams(new Gallery.LayoutParams(150, 150));
				return i;
			}
			
			@Override
			public long getItemId(int posicao) {
				// TODO Auto-generated method stub
				return posicao;
			}
			
			@Override
			public Object getItem(int posicao) {
				// TODO Auto-generated method stub
				return posicao;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return alunos.size();
			}
		});
	}
}
