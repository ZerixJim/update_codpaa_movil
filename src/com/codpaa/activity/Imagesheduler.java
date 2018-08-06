package com.codpaa.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.codpaa.R;
import com.codpaa.model.JsonPhotoUpload;
import com.codpaa.util.Utilities;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

public class Imagesheduler extends AppCompatActivity implements OnItemClickListener{
	
	ListView listV;
	CustomListAdapter adp;
	TextView text ;
    ProgressBar progressBar;
    private boolean imagenCola = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagesheduler);
		

		
		listV = (ListView) findViewById(R.id.listImageCola);
        progressBar = (ProgressBar) findViewById(R.id.proBarImaShe);

		listV.setOnItemClickListener(this);
		loadList();


		try {
			assert getSupportActionBar() != null;
			ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setLogo(R.drawable.ic_launcher);
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {


			case android.R.id.home:
				this.finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void loadList(){
		try {

			Log.d("listValue","cantidad: "+getList().size());
			adp = new CustomListAdapter(this, android.R.layout.simple_list_item_1, getList());
			listV.setAdapter(adp);

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ImageSheduler", "Error en adaptador");
		}
		
	}
	
	private ArrayList<PhotoListModel> getList(){
		ArrayList<PhotoListModel> array = new ArrayList<>();
		BDopenHelper basel = new BDopenHelper(this);
		Cursor cur = basel.datosPhoto();
		
		if(cur.getCount() > 0){
            for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
                final PhotoListModel mP = new PhotoListModel();
                mP.set_idPhoto(cur.getInt(0));
                mP.set_tienda(cur.getString(1));
                mP.setMarca(cur.getString(2));
                mP.set_fecha(cur.getString(3));
                mP.set_img(cur.getString(4));
				mP.set_status(cur.getInt(5));

                array.add(mP);

            }

		}else{
            text.setText(R.string.no_imagenes);
			listV.setVisibility(View.GONE);

		}
		
		
		basel.close();
		return array;
	}
	
	
	public class CustomListAdapter extends ArrayAdapter<PhotoListModel>{
		
		Activity _context;
		//TextView txtName, txtProducto, txtFecha;
		private ArrayList<PhotoListModel> _datos;

        private class ViewHolder{
            TextView txtName;
            TextView txtProducto;
            TextView txtFecha;
			TextView txtStatus;
            ImageView img;
            ProgressBar progressBar;
            PhotoListModel temp;
            //Button botonEnviar;
        }

		public CustomListAdapter(Activity con, int textViewResourceId,ArrayList<PhotoListModel> objects) {
			super(con, textViewResourceId, objects);
			
			this._context= con;
			this._datos = objects;
			
		}

		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            PhotoListModel temp = _datos.get(position);
            ViewHolder holder;

            if(null == row){
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.custom_photo_list, parent,false);

                holder = new ViewHolder();
                holder.txtName = (TextView) row.findViewById(R.id.txtNombretienda);
                holder.txtProducto = (TextView) row.findViewById(R.id.txtProdPhoto);
                holder.txtFecha = (TextView) row.findViewById(R.id.txtfechaPhoto);
				holder.txtStatus = (TextView) row.findViewById(R.id.statusSheduler);
                holder.img = (ImageView) row.findViewById(R.id.imgPhotoCola);
                holder.progressBar = (ProgressBar) row.findViewById(R.id.progressbar);
               // holder.botonEnviar = (Button) row.findViewById(R.id.buttonEnviarFoto);
                holder.temp = temp;

                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
                holder.temp = temp;
            }
            /*
            if(position % 2 == 0){
                row.setBackgroundColor(Color.WHITE);
            }else{
                row.setBackgroundColor(Color.LTGRAY);
            }*/
			
			/*BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;*/

			
			holder.txtName.setText(temp.get_tienda());
            holder.txtProducto.setText(temp.getMarca());
            holder.txtFecha.setText(temp.get_fecha());
			if (temp.get_status() == 1){

				holder.txtStatus.setText(R.string.process);
				holder.txtStatus.setTextColor(Color.BLUE);
			}else if (temp.get_status() == 2){
				holder.txtStatus.setText(R.string.sent);


			}
            try {
                /*Bitmap bitmap = BitmapFactory.decodeFile(temp.get_img(),options);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap,64,64);
                holder.img.setImageBitmap(thumbImage);*/

                Log.d("temp", temp.get_img());

                Picasso.get()
                        .load(new File(temp.get_img()))
                        .placeholder(R.drawable.placeholder)
                        .into(holder.img);
            }catch (Exception ex){
                ex.printStackTrace();
            }


            //final ProgressBar progress = holder.progressBar;
            /*
            holder.botonEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //progress.setVisibility(View.VISIBLE);
                }
            });*/
			
			
			return row;
			
		}

		
	}
	
	private class PhotoListModel{
		
		private String _tienda;
		private String _img;
		private String _fecha;
		private String _marca;
		private int _status;
		private int _idPhoto;

		public int get_status() {
			return _status;
		}

		public void set_status(int _status) {
			this._status = _status;
		}

		public String get_tienda() {
			return _tienda;
		}
		public void set_tienda(String _tienda) {
			this._tienda = _tienda;
		}
		public String get_img() {
			return _img;
		}
		public void set_img(String _img) {
			this._img = _img;
		}
		public String get_fecha() {
			return _fecha;
		}
		public void set_fecha(String _fecha) {
			this._fecha = _fecha;
		}
		public String getMarca() {
			return _marca;
		}
		public void setMarca(String marca) {
			this._marca = marca;
		}
		public int get_idPhoto() {
			return _idPhoto;
		}
		public void set_idPhoto(int _idPhoto) {
			this._idPhoto = _idPhoto;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		PhotoListModel pl = (PhotoListModel) arg0.getItemAtPosition(arg2);

		//ProgressBar progressBar = (ProgressBar) arg1.findViewById(R.id.progressBar2);
		dialogoFoto(pl.get_img(), pl.get_idPhoto(), arg2, pl.get_status());

		
	}
	
	private void dialogoFoto(String img, int idFoto, int position, int status) {
		
		Builder builder  = new AlertDialog.Builder(this);
		View vistaFotoEnviar = LayoutInflater.from(this).inflate(R.layout.dialogimg, null);
		ImageView imagen = (ImageView) vistaFotoEnviar.findViewById(R.id.imgDialog);
		/*BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 0;
		
		Bitmap bitmap = BitmapFactory.decodeFile(img,options);
		imagen.setImageBitmap(bitmap);*/

		Picasso.get()
				.load(new File(img))
				.placeholder(R.drawable.placeholder)
				.into(imagen);

		
		EnviarFotoListener listener = new EnviarFotoListener(idFoto, img, position, status);
		
		builder.setPositiveButton("Enviar Foto", listener).setNegativeButton("Cancelar", listener).setView(vistaFotoEnviar);
		builder.create().show();
		
	}
	
	
	private class EnviarFotoListener implements DialogInterface.OnClickListener{
		
		private int _idFoto= 0;
		private int _position;
		private int _status;
		private String _img;
		
		public EnviarFotoListener(int idFoto, String img, int position, int status){
			this._idFoto = idFoto;
			this._img = img;
			this._position = position;
			this._status = status;
		}
		
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			if(which == DialogInterface.BUTTON_POSITIVE){
				if(_idFoto != 0){
					if (_status == 2){
						Toast.makeText(getApplicationContext(),"La imagen ya esta enviada",Toast.LENGTH_SHORT).show();
					}else {
						enviarFoto(_idFoto, this._position);
						Toast.makeText(getApplicationContext(), "Enviando Imagen", Toast.LENGTH_LONG).show();

					}
				}
				
				
				
			}else if(which == DialogInterface.BUTTON_NEGATIVE){
				Toast.makeText(getApplicationContext(),"Cancelaste el Envio", Toast.LENGTH_SHORT).show();
			}
			
		}

	
		
	}
	
	public void enviarFoto(int idFoto,int position) {

        if(!imagenCola){
            try {


                BDopenHelper base = new BDopenHelper(this);

                AsyncHttpClient cliente = new AsyncHttpClient();
                Cursor curFoto = base.datosFoto(idFoto);


                if(verificarConexion()){
                    if(curFoto.getCount() != 0) {
                        curFoto.moveToFirst();


						Gson gson = new Gson();
						RequestParams rpFoto = new RequestParams();


						JsonPhotoUpload upload = new JsonPhotoUpload();

						upload.setIdTienda(curFoto.getInt(curFoto.getColumnIndex("idTienda")));
						upload.setIdPromotor(curFoto.getInt(curFoto.getColumnIndex("idCelular")));
						upload.setIdMarca(curFoto.getInt(curFoto.getColumnIndex("idMarca")));
						upload.setIdExhibicion(curFoto.getInt(curFoto.getColumnIndex("idExhibicion")));
						upload.setFecha(curFoto.getString(curFoto.getColumnIndex("fecha")));
						upload.setFechaCaptura(curFoto.getString(curFoto.getColumnIndex("fecha_captura")));
						upload.setDia(curFoto.getInt(curFoto.getColumnIndex("dia")));
						upload.setMes(curFoto.getInt(curFoto.getColumnIndex("mes")));
						upload.setAnio(curFoto.getInt(curFoto.getColumnIndex("anio")));
						upload.setEvento(curFoto.getInt(curFoto.getColumnIndex("evento")));


						if (curFoto.getString(curFoto.getColumnIndex("productos")) != null){
							Log.d("foto", "productos disponibles");
							String[] productos = curFoto.getString(curFoto.getColumnIndex("productos")).split(",");
							upload.convert(productos);
						}


						rpFoto.put("json", gson.toJson(upload));


						File file = new File(curFoto.getString(curFoto.getColumnIndex("imagen")));
						try {
							rpFoto.put("file", file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

						cliente.setTimeout(5000);

						cliente.post(Utilities.WEB_SERVICE_CODPAA + "uploadimage2.php",rpFoto, new HttpResponseFoto(this, idFoto, position));


                    }
                    base.close();
                }else{
                    Toast.makeText(getApplicationContext(), "Se Predio la conexion a Internet \n intentelo mas tarde", Toast.LENGTH_LONG).show();
                }



            }catch(Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Espere.. foto Pendiente", Toast.LENGTH_LONG).show();
        }

	}
	
	
	
	
	
	
	public boolean verificarConexion() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

	    return netInfo != null && netInfo.isConnected();
	}
	
	
	private class HttpResponseFoto extends JsonHttpResponseHandler{
		
		private int _idFoto;

		Activity act;
		SQLiteDatabase db;
		private int position;

		
		public HttpResponseFoto(Activity contex,int idFoto, int position){
			this._idFoto = idFoto;
			this.act = contex;
			this.position = position;

			db = new BDopenHelper(act).getWritableDatabase();

		}

        @Override
        public void onStart() {
            super.onStart();
            imagenCola = true;

            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

        }

        @Override
        public void onFinish() {
            super.onFinish();
            imagenCola = false;
            progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            },3000);
        }


		@Override
		public void onProgress(final long bytesWritten, final long totalSize) {
			super.onProgress(bytesWritten, totalSize);
			//Log.v("Progress"," "+(bytesWritten*100)/totalSize);
			progressBar.post(new Runnable() {
				@Override
				public void run() {
					progressBar.setProgress((int)bytesWritten);
					progressBar.setMax((int)totalSize);
				}
			});
		}



        @Override
		public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
			
			if(response != null){
				try {
					

					if(response.getBoolean("insert")){

                        //

                        //set status=2 (status 2 = send image)
						db.execSQL("Update photo set status=2 where idPhoto="+ this._idFoto);

                        //removemos el elmento de la lista
						//adp.remove(adp.getItem(_position));
                        //notificamos el cambio al adaptador

						adp.getItem(position).set_status(2);

						adp.notifyDataSetChanged();
                        //notificamos al usuario la respuesta del servidor
						Toast.makeText(getApplicationContext(), "foto enviada", Toast.LENGTH_SHORT).show();
                        //deleteArchivo(_imgPath);


                    }else{
						if(response.getInt("code") == 3){


							adp.getItem(position).set_status(2);

							//adp.remove(adp.getItem(_position));
							adp.notifyDataSetChanged();
                            //deleteArchivo(_imgPath);
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            db.execSQL("Update photo set status=2 where idPhoto="+this._idFoto);
						}
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				} finally {
                    db.close();
                }

			}else{
				Log.d("Response image", "Sin respuesta");
			}
			
			
		}
		
		

		@Override
		public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
			Toast.makeText(getApplicationContext(), "Error al enviar \n se perdio la conexion intentelo mas tarde" , Toast.LENGTH_LONG).show();


		}


		
	}


    /*public void deleteArchivo(String filePath){
		File img = new File(filePath);
		if(img.delete()){
			Log.d("Delete file","Archivo borrado");
		}else{
			Log.d("Delete file","Archivo no se pudo borrar");
		}
	}*/
	

}
