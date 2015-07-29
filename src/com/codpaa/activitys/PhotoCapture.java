package com.codpaa.activitys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.Header; //detectado
import org.json.JSONException;
import org.json.JSONObject;

import com.codpaa.adapters.CustomAdapter;
import com.codpaa.R;
import com.codpaa.models.SpinnerMarcaModel;
import com.codpaa.utils.Utilities;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.codpaa.db.BDopenHelper;

import com.codpaa.models.ProductosModel;
import com.squareup.picasso.Picasso;


public class PhotoCapture extends AppCompatActivity implements OnClickListener, OnItemSelectedListener{


	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int widthRequerida = 640;
    private static final int heightRequerida = 480;


    ProgressBar progressFoto;
    File imageCaptured = null;
    TextView textoEnvio;
	int idPromotor, idTienda;
	Button photo, enviarPhoto;
    Locale local;
    public static ImageView showImg = null;
    PhotoCapture CameraActivity = null;
    String mCurrentPhotoPath;
    boolean imagenEspera = false;
    Spinner spiMarca, spiExh;
    ArrayList<SpinnerMarcaModel> array = new ArrayList<>();
 	SQLiteDatabase base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.photocapture);
        //aplication context
        CameraActivity = this;

        //get value by intention
        Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");


        //instancia de views
        showImg = (ImageView) findViewById(R.id.showImg);

        photo = (Button) findViewById(R.id.photo);
        enviarPhoto = (Button) findViewById(R.id.btnEnviPho);


        spiMarca = (Spinner) findViewById(R.id.spiMarPhoto);
        spiExh = (Spinner) findViewById(R.id.spiExhPho);

        progressFoto = (ProgressBar) findViewById(R.id.progressEnviarFoto);
        textoEnvio = (TextView) findViewById(R.id.textEvioFoto);

        //add event listener
        photo.setOnClickListener(this);
        enviarPhoto.setOnClickListener(this);


        spiMarca.setOnItemSelectedListener(this);


        loadSpinner();
        spinnerExhi();




        local = new Locale("es_MX");

        //load default image (not image loaded)
        showImg.setImageDrawable(getResources().getDrawable(R.drawable.noimage));

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
            case R.id.iniciar_camara:
                dispatchTakePictureIntent();
                return true;
            case R.id.save_photo:
                EnviarImagen();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);

        return super.onCreateOptionsMenu(menu);
    }




    //metod start camera
    //voy a modificar este metodo
    private void dispatchTakePictureIntent(){
        //intention start camera

        Log.d("dispath", "1");
    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if exists respost to camera
    	if(takePictureIntent.resolveActivity(getPackageManager()) != null){
    		File photoFile;
    		try {
				photoFile = createImageFile();
				if(photoFile != null){
	    			Log.d("PhotoCapture", "dispatch2");
	    			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
	    			startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    		}
				Log.d("PhotoCapture", "dispatch1");
			} catch (IOException e) {
				Log.v("Excepcion", "dispath");
				e.printStackTrace();
			}


    	}
    }

    //metod create image to file
    private File createImageFile() throws IOException {


		String timeStamp = new SimpleDateFormat("ddMMyyyykm", local).format(new Date());
		String imageFileName = idTienda+timeStamp;
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if(!storageDir.exists()){
			Log.v("PictireCir", "Directorio No Existe");
			if(storageDir.mkdir()){
				Log.v("PictireCir", "Directorio Creado");
			}else{
				Log.v("PictireCir", "Directorio No Creado");
			}
		}else{
			Log.v("PictireCir", "Directorio Ya Existe");
		}

		imageCaptured = File.createTempFile(imageFileName, ".jpg",storageDir);

		//mCurrentPhotoPath = image.getAbsolutePath();

		return imageCaptured;
	}


    //result of takeImage
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data){

        Log.d("onActivityResult", "1");

    	if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

    		imagenEspera = true;
    		if(data != null){


        		Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                Log.v("Image ",imageBitmap.getWidth()+"x"+imageBitmap.getHeight());
        		showImg.setImageBitmap(imageBitmap);

        		Uri tempUri = getImageUri(CameraActivity, imageBitmap);

        		File myFile = new File(getRealPathFromURI(tempUri));

        		mCurrentPhotoPath =  myFile.getAbsolutePath();


        		Log.v("PhothoCapture", "Data != null");
    		}else{

    			Log.v("PhothoCapture", "Data == null");


                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 0;*/
                mCurrentPhotoPath = imageCaptured.getAbsolutePath();
                File archivo = new File(mCurrentPhotoPath);
                long tamano = archivo.length();
                double kb = tamano/1024;
                double mb = kb/1024;

                Log.e("Imagen","Tamaño de imagen: "+mb+"mb");


                if (tamano > 0 ) {


                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

                    try {
                        if (bitmap.getWidth() > widthRequerida && bitmap.getHeight() > heightRequerida) {
                            File fileP = new File(mCurrentPhotoPath);
                            FileOutputStream fileOut = new FileOutputStream(fileP);
                            //Log.v("Tamaño imagen: ",Long.toString(fileP.length()));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOut);
                        }

                    } catch (FileNotFoundException fe) {
                        fe.printStackTrace();
                    }
                    Log.v("Image ", bitmap.getWidth() + "x" + bitmap.getHeight());
                    //falta validacion
                    //falta validacion

                    /*Bitmap thum = ThumbnailUtils.extractThumbnail(bitmap,256,128);
                    showImg.setImageBitmap(thum);*/
                    Picasso.with(this).load(new File(mCurrentPhotoPath))
                            .placeholder(R.drawable.placeholder)
                            .resize(512,256)
                            .centerCrop()
                            .into(showImg);


                } else {
                    Toast.makeText(getApplicationContext(),"Error al cargar " +
                            "la foto \n intente tomar la foto de nuevo",Toast.LENGTH_SHORT).show();
                }
    		}

    	}else {
            imageCaptured = null;
        }

    }

    //get image from Uri
	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

	    inImage.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}
    //get real path from Uri
	public String getRealPathFromURI(Uri uri) {
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	    cursor.moveToFirst();
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
	    return cursor.getString(idx);
	}
    //metod: sent image to server
	public void EnviarImagen(){
		SpinnerMarcaModel spm = (SpinnerMarcaModel) spiMarca.getSelectedItem();
		SpinnerMarcaModel spe = (SpinnerMarcaModel) spiExh.getSelectedItem();

		int idMarca = spm.getId();
		int idExhibicion = spe.getId();
		if(imagenEspera){
			if(idMarca != 0 && idExhibicion != 0){

				String timeStamp = new SimpleDateFormat("dd-MM-yyyy", local).format(new Date());
				String ano = new SimpleDateFormat("yyyy", local).format(new Date());
				String mes = new SimpleDateFormat("MM", local).format(new Date());
				String dia = new SimpleDateFormat("dd", local).format(new Date());

				if(verificarConexion()){

                    AsyncHttpClient clienteFoto = new AsyncHttpClient();
                    BDopenHelper registraImagen = new BDopenHelper(this);
                    RequestParams requ = new RequestParams();


                    if(!mCurrentPhotoPath.equals("")){

                        long id = registraImagen.insertarImagenId(idTienda,idPromotor,idMarca,idExhibicion,timeStamp,Integer.valueOf(dia),
                                Integer.valueOf(mes),Integer.valueOf(ano),mCurrentPhotoPath,1);

                        if(id > 0){
                            Log.d("EnviarImage", "Enviando la imagen");
                            Cursor datosFoto = registraImagen.datosFoto((int)(long)id);
                            datosFoto.moveToFirst();

                            //next

                            File file = new File(mCurrentPhotoPath);


                            requ.put("idtienda", Integer.toString(datosFoto.getInt(datosFoto.getColumnIndex("idTienda"))));
                            requ.put("idpromo", Integer.toString(datosFoto.getInt(datosFoto.getColumnIndex("idCelular"))));
                            requ.put("idmarca", Integer.toString(datosFoto.getInt(datosFoto.getColumnIndex("idMarca"))));
                            requ.put("idex", Integer.toString(datosFoto.getInt(datosFoto.getColumnIndex("idExhibicion"))));
                            requ.put("fecha", datosFoto.getString(datosFoto.getColumnIndex("fecha")));
                            requ.put("dia", Integer.toString(datosFoto.getInt(datosFoto.getColumnIndex("dia"))));
                            requ.put("mes", Integer.toString(datosFoto.getInt(datosFoto.getColumnIndex("mes"))));
                            requ.put("ano", Integer.toString(datosFoto.getInt(datosFoto.getColumnIndex("anio"))));
                            try {
                                requ.put("file", file );
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }


                            clienteFoto.post(Utilities.WEB_SERVICE_CODPAA + "upimage1.php", requ,
                                    new HttpResponseImage(CameraActivity, (int)(long)id));
                            Log.d("http foto", requ.toString());
                            datosFoto.close();
                        }

                    }else{
                        Toast.makeText(this, "Sin Imagen para enviar", Toast.LENGTH_SHORT).show();
                    }

				}else{
					Toast.makeText(this, "Se perdio la conexion \n a Internet", Toast.LENGTH_SHORT).show();
					Toast.makeText(this, "Eviado a Imagenes pendientes\n(opcion enviar)", Toast.LENGTH_LONG).show();

					spiExh.setSelection(0);
					spiMarca.setSelection(0);

					BDopenHelper baseinsert = new BDopenHelper(this);

					try {
						baseinsert.insertarImagen(idTienda, idPromotor, idMarca, idExhibicion, timeStamp, Integer.parseInt(dia),Integer.parseInt(mes) , Integer.parseInt(ano), mCurrentPhotoPath, 1);
						imagenEspera = false;
						showImg.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.noimage));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}else{
				Toast.makeText(this, "No Seleccionaste Marca o \n Tipo de Exhibicion", Toast.LENGTH_SHORT).show();
			}


		}else{
			Toast.makeText(this, "No hay imagen capturada", Toast.LENGTH_SHORT).show();
		}


	}


    //escucha cuando se selecciona un elemento en el spinner marca
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {




    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //metod: listener response of image loaded
	private class HttpResponseImage extends JsonHttpResponseHandler{

		int _idPhotho;
		Activity _act;
		BDopenHelper db;
        SQLiteDatabase base;

		public HttpResponseImage(Activity context, int idPhoto) {
				this._act = context;
            this._idPhotho = idPhoto;
				db = new BDopenHelper(_act);

            base = db.getWritableDatabase();
		}


        @Override
        public void onStart() {
            super.onStart();
            progressFoto.post(new Runnable() {
                @Override
                public void run() {
                    progressFoto.setVisibility(View.VISIBLE);
                    textoEnvio.setVisibility(View.VISIBLE);
                    textoEnvio.setText("Enviando...");
                }
            });

        }

        @Override
        public void onProgress(final long bytesWritten, final long totalSize) {
            super.onProgress(bytesWritten, totalSize);

            progressFoto.post(new Runnable() {
                @Override
                public void run() {
                    progressFoto.setProgress((int)bytesWritten);
                    progressFoto.setMax((int)totalSize);
                }
            });
        }

        @Override
        public void onFinish() {
            super.onFinish();

            progressFoto.post(new Runnable() {
                @Override
                public void run() {
                    progressFoto.setVisibility(View.GONE);
                    textoEnvio.setVisibility(View.GONE);
                }
            });
        }

        @Override
		public void onFailure(int statusCode, Header[] header,Throwable e,JSONObject errorResponse) {

            Log.d("EnviarFoto","Estatus "+statusCode);
            Log.d("EnviarFoto","ErrorRespo"+errorResponse);
            Log.d("EnviarFoto","Thro:" + e );
			Toast.makeText(getApplicationContext(), "No fue posible conectarse con el servidor", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "Imagen no envida \n (Menu Enviar)", Toast.LENGTH_SHORT).show();

            textoEnvio.post(new Runnable() {
                @Override
                public void run() {

                    textoEnvio.setText("Error el enviar");
                    textoEnvio.setTextColor(Color.RED);
                }
            });



			try {

				//db.insertarImagen(_idTienda, _idPromo, _idMarca, _idExhib, _timeStamp, _dia, _mes, _ano, mCurrentPhotoPath, 1);
				imagenEspera = false;
				showImg.setImageDrawable(ContextCompat.getDrawable(PhotoCapture.this,R.drawable.noimage));
			} catch (SQLiteException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void onSuccess(int statusCode,Header[] headers,JSONObject response) {

            Log.d("EnviarFoto","Estatus "+statusCode);
            Log.d("EnviarFoto","Response"+response);
            textoEnvio.post(new Runnable() {
                @Override
                public void run() {
                    textoEnvio.setText("Enviada...");
                    textoEnvio.setTextColor(Color.GREEN);
                }
            });



			if(response != null){
				try {

					Log.d("Respuestas Ima", response.getString("insert"));
					if(response.getBoolean("bol")){
						Toast.makeText(getApplicationContext(), response.getString("insert"), Toast.LENGTH_SHORT).show();
						showImg.setImageDrawable(getResources().getDrawable(R.drawable.imagesend));
						imagenEspera = false;
						spiExh.setSelection(0);
						spiMarca.setSelection(0);

                        //deleteArchivo(_imgPath);
                        base.execSQL("Update photo set status=2 where idPhoto="+this._idPhotho);
                        base.close();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showImg.setImageDrawable(ContextCompat.getDrawable(PhotoCapture.this,R.drawable.noimage));
                            }
                        },3000);
					}else{
						if(response.getInt("code") == 3){
							showImg.setImageDrawable(ContextCompat.getDrawable(PhotoCapture.this,R.drawable.imagesend));
							imagenEspera = false;
						}
                        //deleteArchivo(_imgPath);

						Toast.makeText(getApplicationContext(), response.getString("insert"),
                                Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}else{
				Log.d("Response image", "Sin respuesta");
			}


		}


	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.photo:
			dispatchTakePictureIntent();

			break;

		case R.id.btnEnviPho:
			EnviarImagen();
			break;

		}

	}

    //metod: conexion verify
	public boolean verificarConexion() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

	    return netInfo != null && netInfo.isConnected();
	}

    //metod: spinner load
	private void loadSpinner(){
		try {


			CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spiMarca.setAdapter(adapter);

		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}

	}
    //
	private ArrayList<SpinnerMarcaModel> getArrayList(){

		base = new BDopenHelper(this).getReadableDatabase();
		String sql = "select idMarca as _id, nombre from marca order by nombre asc;";
		Cursor cursorMarca = base.rawQuery(sql, null);

		for(cursorMarca.moveToFirst(); !cursorMarca.isAfterLast(); cursorMarca.moveToNext()){

			final SpinnerMarcaModel spiM = new SpinnerMarcaModel();
			spiM.setNombre(cursorMarca.getString(1));
			spiM.setId(cursorMarca.getInt(0));

			array.add(spiM);
		}

		final SpinnerMarcaModel spiMfirst = new SpinnerMarcaModel();
		spiMfirst.setNombre("Selecciona Marca");
		spiMfirst.setId(0);

		array.add(0,spiMfirst);
        cursorMarca.close();
		base.close();
		return array;

	}

    //metod: spinner load exhibicion
	private void spinnerExhi(){
		try {
			CustomAdapter adapterExhi = new CustomAdapter(this, android.R.layout.simple_spinner_item, getArrayExhi());
			spiExh.setAdapter(adapterExhi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    //metod:
	private ArrayList<SpinnerMarcaModel> getArrayExhi(){

		base = new BDopenHelper(this).getReadableDatabase();
		ArrayList<SpinnerMarcaModel> arrayE = new ArrayList<>();
		String sql = "select idExhibicion, nombre from tipoexhibicion order by nombre asc;";
		Cursor cursorE = base.rawQuery(sql, null);

		for(cursorE.moveToFirst(); !cursorE.isAfterLast(); cursorE.moveToNext()){

			final SpinnerMarcaModel spiM = new SpinnerMarcaModel();
			spiM.setNombre(cursorE.getString(1));
			spiM.setId(cursorE.getInt(0));

			arrayE.add(spiM);
		}

		final SpinnerMarcaModel spiMfirst = new SpinnerMarcaModel();
		spiMfirst.setNombre("Selecciona tipo Exhibicion");
		spiMfirst.setId(0);

		arrayE.add(0,spiMfirst);

        cursorE.close();
		base.close();
		return arrayE;

	}


    public void deleteArchivo(String filePath){
        File img = new File(filePath);
        if(img.delete()){
            Log.d("Delete file","Archivo borrado");
        }else{
            Log.d("Delete file","Archivo no se pudo borrar");
        }
    }



    private ArrayList<ProductosModel> getArrayListProductos(int idMarca){

        Cursor curPro = new BDopenHelper(this).productos(idMarca);
        ArrayList<ProductosModel> arrayP = new ArrayList<>();
        for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
            final ProductosModel spP = new ProductosModel();
            spP.setIdProducto(curPro.getInt(0));
            spP.setNombre(curPro.getString(1));
            spP.setPresentacion(curPro.getString(2));
            arrayP.add(spP);
        }

        final ProductosModel pm2 = new ProductosModel();
        pm2.setIdProducto(0);
        pm2.setNombre("Seleccine Producto");
        pm2.setPresentacion("");
        arrayP.add(0,pm2);


        base.close();
        return arrayP;

    }






}
