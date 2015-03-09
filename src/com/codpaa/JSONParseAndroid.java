/****
 * Autor: Gustavo Ramon Ibarra Maciel code version: 1.0.4
 ******/
package com.codpaa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;

import BD.BDopenHelper;

public class JSONParseAndroid {
	
	private Activity activity;
	private JSONObject jObject, jObjectTienda, jObjectPro, jObjectMarc, jObjectExhi;
	private Runnable runReadAndParseJSON;
	private int idPromo;
	
	public JSONParseAndroid(Activity a) {
		activity = a;
		
	}
	
	public void readAndParseJSON(int id) throws JSONException{
		idPromo = id;
		runReadAndParseJSON = new Runnable() {
			@Override
			public void run() {
				
				
				try {
					readJSONRutaTienda(idPromo);
					readJSONClientes(idPromo);
					readJSONExhibicion();
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			
		};
		
		Thread thread = new Thread(null, runReadAndParseJSON, "ReadJSON");
		thread.start();
		Toast.makeText(activity, "Actualizando...", Toast.LENGTH_LONG).show();
		
	}
	
	public void readAndParseProdcutos(int id) throws JSONException{
		
		idPromo = id;
		Runnable runReadAndParsePro = new Runnable(){

			@Override
			public void run() {
				try {
					readJSONMarca(idPromo);
					readJSONProductos(idPromo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		};
		
		Thread threadProductos = new Thread(null, runReadAndParsePro, "ReadJsonProductos");
		threadProductos.start();
		Toast.makeText(activity, "actualizando Producto", Toast.LENGTH_SHORT).show();
	}
	
	
	
	
	
	private void readJSONRutaTienda(int id) throws JSONException {
		
		jObject = JSONManager.getJSONfromURL("http://promotoressumma.com/codpaa/serv.php?solicitud=rutas&id="+id);
		
		if(jObject != null) 
			parseJSONRutaTienda(jObject.getJSONArray("V"));

	}
	
	private void readJSONClientes(int id) throws JSONException{
		jObjectTienda = JSONManager.getJSONfromURL("http://promotoressumma.com/codpaa/serv.php?solicitud=client&id="+id);
		if(jObjectTienda !=null){
			parseJSONClientes(jObjectTienda.getJSONArray("C"));
			
		}
		
		
	}
	
	private void readJSONProductos(int id) throws JSONException{
		
		jObjectPro = JSONManager.getJSONfromURL("http://promotoressumma.com/codpaa/serv.php?solicitud=productoid&id="+id);
		if(jObjectPro !=null){
			parseJSONProductos(jObjectPro.getJSONArray("P"));
			
		}
		
	}
	
	private void readJSONMarca(int id) throws JSONException{
		
		jObjectMarc = JSONManager.getJSONfromURL("http://promotoressumma.com/codpaa/serv.php?solicitud=marcaid&id="+id);
		if(jObjectMarc !=null){
			parseJSONMarca(jObjectMarc.getJSONArray("M"));
			
		}
		
	}
	
	private void readJSONExhibicion() throws JSONException{
		
		jObjectExhi = JSONManager.getJSONfromURL("http://promotoressumma.com/codpaa/serv.php?solicitud=exhibicion");
		if(jObjectExhi !=null){
			parseJSONExhi(jObjectExhi.getJSONArray("E"));
			
		}
		
	}
	
	private void parseJSONExhi(JSONArray exhiArray) throws JSONException{
		BDopenHelper b = new BDopenHelper(activity);
		b.vaciarTabla("exhibiciones");
		
		for(int i= 0; i < exhiArray.length(); i++) {
			b.insertarTipoExhibicion(exhiArray.getJSONObject(i).getInt("IE"), exhiArray.getJSONObject(i).getString("N"));
		}
	}
	
	private void parseJSONMarca(JSONArray marcaArray) throws JSONException{
		BDopenHelper b = new BDopenHelper(activity);
		b.vaciarTabla("marca");
		
		for(int i= 0; i < marcaArray.length(); i++) {
			
			b.insertarMarca(marcaArray.getJSONObject(i).getInt("IM"), marcaArray.getJSONObject(i).getString("N"));
		}
	}
	
	private void parseJSONProductos(JSONArray productosArray) throws JSONException{
		BDopenHelper b = new BDopenHelper(activity);
		b.vaciarTabla("producto");
		
		for(int i= 0; i < productosArray.length(); i++) {
			
			b.insertarProducto(productosArray.getJSONObject(i).getInt("IP"), productosArray.getJSONObject(i).getString("N"), productosArray.getJSONObject(i).getString("P"), productosArray.getJSONObject(i).getInt("IM"),productosArray.getJSONObject(i).getString("CB"));
		}
	}
	
	private void parseJSONRutaTienda(JSONArray rutasArray) throws JSONException{
		BDopenHelper b = new BDopenHelper(activity);
		b.vaciarTabla("visitaTienda");
		
		for(int i= 0; i < rutasArray.length(); i++) {
			
			b.insertarRutaVisitas(rutasArray.getJSONObject(i).getInt("IT"), rutasArray.getJSONObject(i).getInt("lu"), rutasArray.getJSONObject(i).getInt("ma"), rutasArray.getJSONObject(i).getInt("mi"), rutasArray.getJSONObject(i).getInt("ju"), rutasArray.getJSONObject(i).getInt("vi"), rutasArray.getJSONObject(i).getInt("sa"), rutasArray.getJSONObject(i).getInt("do"), rutasArray.getJSONObject(i).getInt("IP"), rutasArray.getJSONObject(i).getString("R"));
			
		}

	}
	
	private void parseJSONClientes(JSONArray clientesArray) throws JSONException{
		BDopenHelper b = new BDopenHelper(activity);
		b.vaciarTabla("clientes");
		for(int i=0; i<clientesArray.length(); i++){
			b.insertarClientes(clientesArray.getJSONObject(i).getInt("IT"), clientesArray.getJSONObject(i).getString("G"), clientesArray.getJSONObject(i).getString("S"), clientesArray.getJSONObject(i).getString("X"), clientesArray.getJSONObject(i).getString("Y"));
		}
		
	}
	
	
	
	

}

