package com.codpaa.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.codpaa.R;
import com.codpaa.model.JsonPhotoUpload;
import com.codpaa.util.Utilities;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;


import cz.msebera.android.httpclient.Header;

public class ImageSheduler extends AppCompatActivity implements OnItemClickListener{
	

	private RecyclerView recyclerView;
	private ListImageRecyclerAdapter adapter;
	private ImageView placeholder;


    private boolean imagenCola = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagesheduler);

		recyclerView = findViewById(R.id.recycler_list);

		placeholder = findViewById(R.id.placeholder_image);


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
		if (item.getItemId() == android.R.id.home) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void loadList(){
		try {


			StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


			recyclerView.setLayoutManager(gridLayoutManager);


			adapter = new ListImageRecyclerAdapter();

			List<PhotoListModel> list = adapter.getList();


			if (list.size() > 0) {
				placeholder.setVisibility(View.GONE);
			}


			adapter.loadData();

			recyclerView.setAdapter(adapter);



		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ImageSheduler", "Error en adaptador");
		}
		
	}
	

	

	private class ListImageRecyclerAdapter  extends RecyclerView.Adapter<ListImageRecyclerAdapter.ViewHolder>{


		private List<PhotoListModel> list = new ArrayList<>();


		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_photo_list, parent, false);


			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


			PhotoListModel model = list.get(position);

			holder.sucursal.setText(model.getTienda());
			holder.marca.setText(model.getMarca());
			holder.fecha.setText(model.getFecha());


			Glide.with(getApplicationContext()).load(model.getImg()).into(holder.imageView);


            if (model.getStatus() == 2){

				holder.imageStatus.setImageResource(R.mipmap.ic_done);

			}else {


            	holder.imageStatus.setImageResource(R.drawable.ic_cloud_off_black_24dp);
			}



		}

		@Override
		public int getItemCount() {
			return list.size();
		}


		public PhotoListModel getItemAtPosition(int position){

			return list.get(position);

		}

		public void loadData(){


			this.list = getList();

			Log.d("list size", list.size() + "");
			notifyDataSetChanged();

		}

		public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

			TextView sucursal, marca, fecha;
			ImageView imageView;
			LinearLayout linearLayout;
			ImageView imageStatus;
			FrameLayout frameLayout;


			public ViewHolder(@NonNull View itemView) {
				super(itemView);

				marca = itemView.findViewById(R.id.txt_marca);
				sucursal = itemView.findViewById(R.id.txt_sucursal);
				fecha = itemView.findViewById(R.id.txt_fecha);

				imageView = itemView.findViewById(R.id.image);
				linearLayout = itemView.findViewById(R.id.linear_layou_text);
				imageStatus = itemView.findViewById(R.id.image_status);
				frameLayout = itemView.findViewById(R.id.RelativeLayout1);


				itemView.setOnClickListener(this);


			}

			@Override
			public void onClick(View view) {
				PhotoListModel model = getItemAtPosition(getAdapterPosition());

				dialogoFoto(model.getImg(), model.getIdPhoto(), getAdapterPosition(), model.getStatus());


			}
		}



		private List<PhotoListModel> getList(){
			List<PhotoListModel> array = new ArrayList<>();
			BDopenHelper basel = new BDopenHelper(getApplicationContext());
			Cursor cur = basel.datosPhoto();

			if(cur.getCount() > 0){
				for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
					final PhotoListModel mP = new PhotoListModel();
					mP.setIdPhoto(cur.getInt(0));
					mP.setTienda(cur.getString(1));
					mP.setMarca(cur.getString(2));
					mP.setFecha(cur.getString(3));
					mP.setImg(cur.getString(4));
					mP.setStatus(cur.getInt(5));

					array.add(mP);

				}

			}

			basel.close();
			return array;
		}



	}



	
	private static class PhotoListModel{
		
		private String tienda;
		private String img;
		private String fecha;
		private String marca;
		private int status;
		private int idPhoto;

		public int getStatus() {
			return this.status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getTienda() {
			return this.tienda;
		}
		public void setTienda(String tienda) {
			this.tienda = tienda;
		}
		public String getImg() {
			return this.img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getFecha() {
			return this.fecha;
		}
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}
		public String getMarca() {
			return this.marca;
		}
		public void setMarca(String marca) {
			this.marca = marca;
		}
		public int getIdPhoto() {
			return this.idPhoto;
		}
		public void setIdPhoto(int idPhoto) {
			this.idPhoto = idPhoto;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		PhotoListModel pl = (PhotoListModel) arg0.getItemAtPosition(arg2);

		//ProgressBar progressBar = (ProgressBar) arg1.findViewById(R.id.progressBar2);
		dialogoFoto(pl.getImg(), pl.getIdPhoto(), arg2, pl.getStatus());

		
	}
	
	private void dialogoFoto(String img, int idFoto, int position, int status) {
		
		Builder builder  = new AlertDialog.Builder(this);
		View vistaFotoEnviar = LayoutInflater.from(this).inflate(R.layout.dialogimg, null);
		ImageView imagen = vistaFotoEnviar.findViewById(R.id.imgDialog);
		/*BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 0;
		
		Bitmap bitmap = BitmapFactory.decodeFile(img,options);
		imagen.setImageBitmap(bitmap);*/


		Glide.with(this).load(new File(img))
				.placeholder(R.drawable.placeholder)
				.into(imagen);

		
		EnviarFotoListener listener = new EnviarFotoListener(idFoto, img, position, status);
		
		builder.setPositiveButton("Enviar Foto", listener).setNegativeButton("Cancelar", listener).setView(vistaFotoEnviar);
		builder.create().show();
		
	}
	
	
	private class EnviarFotoListener implements DialogInterface.OnClickListener{
		
		private int _idFoto;
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


		private SQLiteDatabase db;
		private int position;

		
		public HttpResponseFoto(Context contex,int idFoto, int position){
			this._idFoto = idFoto;

			this.position = position;

			db = new BDopenHelper(contex).getWritableDatabase();

		}

        @Override
        public void onStart() {
            super.onStart();
            imagenCola = true;



        }

        @Override
        public void onFinish() {
            super.onFinish();
            imagenCola = false;

        }


		@Override
		public void onProgress(final long bytesWritten, final long totalSize) {
			super.onProgress(bytesWritten, totalSize);
			//Log.v("Progress"," "+(bytesWritten*100)/totalSize);

		}



        @Override
		public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
			
			if(response != null){
				try {
					

					if(response.getBoolean("insert")){

						db.execSQL("Update photo set status=2 where idPhoto="+ this._idFoto);

						adapter.getItemAtPosition(position).setStatus(2);
						adapter.notifyItemChanged(position);
						//Toast.makeText(getApplicationContext(), "foto enviada", Toast.LENGTH_SHORT).show();


                    }else{
						if(response.getInt("code") == 3){


							adapter.getItemAtPosition(position).setStatus(2);
							adapter.notifyItemChanged(position);
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
