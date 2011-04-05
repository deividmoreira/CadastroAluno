/**
 * 
 */
package br.com.cadastro;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * @author Deivid
 *
 */
public class Sicronismo {
	private String endereco = "http://www.caelum.com.br/mobile?dado=";
	
	public String enviarDado(String dado) throws ClientProtocolException, IOException{
		
		HttpClient httpClient = new DefaultHttpClient();
		String encode = endereco + URLEncoder.encode(dado);
		Log.i("envio", encode);
		HttpGet httpGet = new HttpGet(encode);
		HttpResponse response;
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		try{
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				is = entity.getContent();
				Scanner scanner = new Scanner(is);
				while(scanner.hasNext()){
					sb.append(scanner.next());
				}
			}
		}finally{
			if(is != null){
				is.close();
			}
		}
		return sb.toString();
	}
}
