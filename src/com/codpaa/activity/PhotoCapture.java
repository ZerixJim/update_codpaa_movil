package com.codpaa.activity;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.codpaa.adapter.CategoriasProductoAdapter;
import com.codpaa.adapter.CustomAdapter;
import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.JsonPhotoUpload;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.model.SpinnerCateProdModel;
import com.codpaa.model.SpinnerMarcaModel;
import com.codpaa.provider.DbEstructure;
import com.codpaa.provider.PhotoProviderVan;
import com.codpaa.util.Utilities;
import com.codpaa.widget.MultiSpinnerSelect;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Handler;

import com.codpaa.db.BDopenHelper;




import cz.msebera.android.httpclient.Header;

import id.zelory.compressor.Compressor;


public class PhotoCapture extends AppCompatActivity implements OnClickListener, OnItemSelectedListener{


	//private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION = 124;
    private static final int CAMERA_PHOTO = 111;


    private ProgressBar progressFoto;
	private int idPromotor, idTienda;
    private DonutProgress donutProgress;
    private CardView cardView;

    private ImageView imageDone;
    private ImageView showImg = null;
    private PhotoCapture CameraActivity = null;
    private String mCurrentPhotoPath;
    private boolean imagenEspera = false;
    private Spinner spiMarca, spiExh, spiCate;
    private EditText editComentario;
    private final ArrayList<MarcaModel> array = new ArrayList<>();
 	private SQLiteDatabase base;
    private MultiSpinnerSelect multiSpinnerSelect;

    private Button btnSendImage;


    private Uri imageToUploadUri;

    private RadioGroup radioChoice, radioChoice2;
    private RadioButton radioNormal, rentadas, noRentadas;

    private LinearLayout layoutRentas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photocapture);
        //SQLiteDatabase db = this.openOrCreateDatabase("codpaa", Context.MODE_PRIVATE, null);
        //Log.i("DBV", String.valueOf(db.getVersion()));
        //aplication context
        CameraActivity = this;

        //get value by intention
        Intent i = getIntent();
		idTienda = i.getIntExtra("idTienda", 0);
		idPromotor = i.getIntExtra("idPromotor", 0);


        //instancia de views
        showImg = findViewById(R.id.showImg);
        //imgAdd = findViewById(R.id.add_photo);


        imageDone = findViewById(R.id.ic_done);

        btnSendImage = findViewById(R.id.btn_send_image);
        btnSendImage.setOnClickListener(this);



        spiMarca = findViewById(R.id.spiMarPhoto);
        spiExh =  findViewById(R.id.spiExhPho);
        spiCate = findViewById(R.id.spiCategoria);

        editComentario = findViewById(R.id.photo_comment);

        progressFoto =  findViewById(R.id.progressEnviarFoto);
        donutProgress =  findViewById(R.id.progress_photo);

        multiSpinnerSelect = findViewById(R.id.multi_spinner);

        cardView = findViewById(R.id.card);

        //imgAdd.setOnClickListener(this);
        layoutRentas = findViewById(R.id.llRentadas);


        radioChoice =  findViewById(R.id.radioChoice);
        if (radioChoice != null) {
            radioNormal =  radioChoice.findViewById(R.id.radioNormal);
        }

        radioChoice2 = findViewById(R.id.radioChoice2);
        if(radioChoice2 != null){
            rentadas = radioChoice2.findViewById(R.id.radioRentada);
            noRentadas = radioChoice2.findViewById(R.id.radioNoRentada);
        }




        spiMarca.setOnItemSelectedListener(this);


        loadSpinner();
        spinnerExhi();



        //load default image (not image loaded)
        //showImg.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.noimage));

        try {
            //assert getSupportActionBar() != null;
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setIcon(R.drawable.ic_launcher);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.iniciar_camara) {
            dispatchTakePictureIntent();
            return true;
        } else if (itemId == R.id.save_photo) {//EnviarImagen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);

        return super.onCreateOptionsMenu(menu);
    }




    //metod start camera
    private void dispatchTakePictureIntent(){
        //intention start camera

        if (cameraPermissionEneabled()){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Toast.makeText(this, "AQUI2",Toast.LENGTH_SHORT).show();

            //if exists respost to camera
            try {
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile;
                    try {
                        photoFile = createImageFile();
                        Log.d("PHOTOFILE",photoFile.getName());

                        Uri photoUri;

                        if (Build.VERSION.SDK_INT >= 24) {
                            //photoUri = PhotoProviderVan.getPhotoUri(photoFile);
                            photoUri = FileProvider.getUriForFile(CameraActivity, CameraActivity.getPackageName() + ".fileprovider", photoFile);
                            Log.d("URIFOTO", photoUri.toString());
                        } else {

                            photoUri = Uri.fromFile(photoFile);

                        }


                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());


                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                        imageToUploadUri = PhotoProviderVan.getPhotoUri(photoFile);

                        startActivityForResult(takePictureIntent, CAMERA_PHOTO);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try{
                        Log.v("PACKAGE123", takePictureIntent.getAction());
                    }catch (Exception e){
                        Log.e("ERRORFOTO: ", e.getMessage());
                    }
                }
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else{
            camaraPermission();
        }


    }




    //metod create image to file
    private File createImageFile() throws IOException {


		String timeStamp = new SimpleDateFormat("ddMMyyyykm", Locale.getDefault()).format(new Date());
		String imageFileName = idTienda+timeStamp;
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

		Log.d("FILENAME", imageFileName);

                //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);



        File codpaaDir = new File(storageDir.getPath() + "/.codpaa/");
		if(!storageDir.exists()){
			//Log.v("PictireCir", "Directorio No Existe");

			if(storageDir.mkdir()){
				Log.v("PictireCir", "Directorio Creado");

                if (!codpaaDir.exists()){
                    if (codpaaDir.mkdir())
                        Log.v("PictireCir", "Directorio Creado");
                }

			}else{
				Log.v("PictireCir", "Directorio No Creado");
			}
		}else{

            if (!codpaaDir.exists()){
                if (codpaaDir.mkdir())
                    Log.v("PictireCir", "Directorio Creado");
            }

			Log.v("PictireCir", "Directorio Ya Existe");
		}


		return File.createTempFile(imageFileName, ".jpg", codpaaDir);
	}


    //result of takeImage
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PHOTO && resultCode == RESULT_OK) {


            imagenEspera = true;
            if (imageToUploadUri != null) {

                Uri selectedImage = imageToUploadUri;
                getContentResolver().notifyChange(selectedImage, null);
                //Bitmap reduceImageSize = getBitmap(selectedImage.getPath());


                mCurrentPhotoPath = selectedImage.getPath();


                if (mCurrentPhotoPath != null) {



                    cardView.setVisibility(View.VISIBLE);



                    final File photo = new File(mCurrentPhotoPath);

                    File file = decodeFile(photo);

                    if (file != null) {

                        if (photo.exists()) {

                            if (photo.delete()) {


                                mCurrentPhotoPath = file.getAbsolutePath();

                            }

                        }


                        Glide.with(this).load(file).into(showImg);


                        /*Picasso.get().load(file)
                                .placeholder(R.drawable.placeholder)
                                .into(showImg);*/

                    } else {
                        /*Picasso.get().load(photo)
                                .placeholder(R.drawable.placeholder)
                                .into(showImg);*/

                        Glide.with(this).load(photo).into(showImg);



                    }


                    showImg.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(photo), "image");
                                startActivity(intent);

                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                }


            }


        } else {


            try {
                File file = new File(imageToUploadUri.getPath());

                if (file.exists()) {
                    if (file.delete()) {
                        Log.d("delete", "true");
                    } else {
                        Log.d("delete", "false");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            imageToUploadUri = null;
            imagenEspera = false;
            mCurrentPhotoPath = null;

            cardView.setVisibility(View.GONE);
        }


    }

    //get image from Uri
	/*public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

	    inImage.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}*/
    //get real path from Uri
	/*public String getRealPathFromURI(Uri uri) {
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        String path = "";

        if (cursor != null) {
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);
            cursor.close();
        }

	    return path;
	}*/





    private File decodeFile(File file) {

        Compressor comp = new Compressor(this);
        try {

            String[] fileName = file.getName().split("\\.");


            String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/.codpaa/";


            return comp.setDestinationDirectoryPath(path)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToFile(file, fileName[0] + "-comp." + fileName[1]);
        }  catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }



    //method: sent image to server
	public void EnviarImagen(){
		MarcaModel spm = (MarcaModel) spiMarca.getSelectedItem();
		SpinnerMarcaModel spe = (SpinnerMarcaModel) spiExh.getSelectedItem();

		int idMarca = spm.getId();
		int idExhibicion = spe.getId();
		String comentario = editComentario.getText().toString();

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String ano = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String mes = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String dia = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String date = df.format(c.getTime());

		if(imagenEspera){
			if(idMarca != 0 && idExhibicion != 0){

			    //SI LA MARCA SELECCIONADA ES CLOROX (249) O
                //ALGUNA MARCA COMPETENCIA DE CLOROX (356 A 360)
                //ENTONCES VALIDAR QUE SE HAYA SELECCIONADO UNA CATEGORÍA
                //DE LO CONTRARIO VERIFICAR QUE SE HAYA SELECCIONADO UN PRODUCTO
                ArrayList<ProductosModel> selected = multiSpinnerSelect.getSelectedItems();

                long idCateSelected = 0;

                //VERIFICAMOS SI HAY UNA CATEGORÍA SELECCIONADA
                try{
                    idCateSelected = spiCate.getSelectedItemId();
                    Log.i("ITEM SELECTED", String.valueOf(spiCate.getSelectedItemId()));
                }catch (Exception e){
                    Log.i("ITEM SELECTED ERROR", e.getMessage());
                }

                if (selected.size() > 0 || idCateSelected > 0){

                    /*String timeStamp = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String ano = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                    String mes = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                    String dia = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());


                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    String date = df.format(c.getTime());*/

                    if(verificarConexion()){

                        AsyncHttpClient clienteFoto = new AsyncHttpClient();
                        clienteFoto.setTimeout(1000);

                        //Log.w("Timeout server", " cT=" +  clienteFoto.getConnectTimeout() + " rT=" + clienteFoto.getResponseTimeout() );
                        BDopenHelper registraImagen = new BDopenHelper(this);

                        if(!mCurrentPhotoPath.equals("")){

                            //SE GENERA EL ID DE LA FOTO
                            long id = registraImagen.insertarImagenId(idTienda,idPromotor,idMarca,idExhibicion,timeStamp, Integer.parseInt(dia),
                                    Integer.parseInt(mes),Integer.parseInt(ano),mCurrentPhotoPath,1,getSelectedRadioGroup(), date, comentario);

                            Log.d("FOTOID", String.valueOf(id));

                            //SI ES CLOROX O LA COMPETENCIA NO NECESITAMOS LOS ID DE PRODUCTOS
                            //SOLO LAS CATEGORÍAS

                            if(idMarca == 249 || (idMarca >= 356 && idMarca <= 360)){
                                //PARA ESTE PUNTO YA NOS ASEGURAMOS DE QUE LA CATEGORÍA SELECCIONADA SEA MAYOR A CERO
                                long cateSelected = spiCate.getSelectedItemId();

                                //INSERTAMOS EL ID DE LA FOTO CON EL ID DE LA CATEGORÍA

                                try{
                                    BDopenHelper fotoCategoria = new BDopenHelper(this);
                                    fotoCategoria.insertarFotoCategoria(id, cateSelected);
                                    fotoCategoria.close();
                                }catch (SQLiteConstraintException e) {
                                    e.printStackTrace();
                                }

                            }else{
                                ArrayList<ProductosModel> proSelected = multiSpinnerSelect.getSelectedItems();
                                SQLiteDatabase db = registraImagen.getWritableDatabase();

                                for (ProductosModel producto : proSelected){

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DbEstructure.PhotoProducto.ID_PHOTO, id);
                                    contentValues.put(DbEstructure.PhotoProducto.ID_PRODUCTO, producto.getIdProducto());


                                    try {

                                        db.insertOrThrow(DbEstructure.PhotoProducto.TABLE_NAME, null, contentValues);


                                    }catch (SQLiteConstraintException e){
                                        e.printStackTrace();
                                    }
                                }
                            }


                            if(id > 0){


                                Log.d("EnviarImage", "Enviando la imagen");
                                Cursor datosFoto = registraImagen.datosFoto((int) id);

                                if (datosFoto.getCount() > 0){
                                    datosFoto.moveToFirst();
                                    File file = new File(mCurrentPhotoPath);
                                    RequestParams requ = new RequestParams();


                                    Gson gson = new Gson();
                                    JsonPhotoUpload view = new JsonPhotoUpload();


                                    view.setIdTienda(datosFoto.getInt(datosFoto.getColumnIndex("idTienda")));
                                    view.setIdPromotor(datosFoto.getInt(datosFoto.getColumnIndex("idCelular")));
                                    view.setIdMarca(datosFoto.getInt(datosFoto.getColumnIndex("idMarca")));
                                    view.setIdExhibicion(datosFoto.getInt(datosFoto.getColumnIndex("idExhibicion")));
                                    view.setFecha(datosFoto.getString(datosFoto.getColumnIndex("fecha")));
                                    view.setDia(datosFoto.getInt(datosFoto.getColumnIndex("dia")));
                                    view.setMes(datosFoto.getInt(datosFoto.getColumnIndex("mes")));
                                    view.setAnio(datosFoto.getInt(datosFoto.getColumnIndex("anio")));
                                    view.setEvento(datosFoto.getInt(datosFoto.getColumnIndex("evento")));
                                    view.setFechaCaptura(datosFoto.getString(datosFoto.getColumnIndex("fecha_captura")));
                                    view.setComentario(datosFoto.getString(datosFoto.getColumnIndex("comentario")));

                                    if(idMarca == 249 || (idMarca >= 356 && idMarca <= 360)){
                                        requ.put("idCate", idCateSelected);
                                    }else {
                                        String productosRaw = datosFoto.getString(datosFoto.getColumnIndex("productos"));

                                        if (productosRaw != null) {
                                            final String[] productos = productosRaw.split(",");
                                            view.convert(productos);
                                        } else {
                                            Log.e("Error Split", "Error Split");
                                        }
                                    }


                                    requ.put("json", gson.toJson(view));
                                    Log.d("Json", gson.toJson(view));
                                    try {
                                        requ.put("file", file );

                                        Log.w("fileSizeCel" , file.length() + " ");

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }


                                    clienteFoto.post(Utilities.WEB_SERVICE_CODPAA + "uploadimage2.php", requ,
                                            new HttpResponseImage(CameraActivity, (int) id));
                                    //Log.d("http foto", requ.toString());
                                    datosFoto.close();
                                    radioNormal.setChecked(true);
                                }


                                editComentario.setText("");

                            }

                        }else{
                            Toast.makeText(this, "Sin Imagen para enviar", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(this, "Se perdio la conexion \n a Internet", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Eviado a Imagenes pendientes\n(opcion enviar)", Toast.LENGTH_LONG).show();

                        spiExh.setSelection(0);
                        spiMarca.setSelection(0);
                        editComentario.setText("");

                        BDopenHelper baseinsert = new BDopenHelper(this);


                        try {
                            baseinsert.insertarImagen(idTienda, idPromotor, idMarca, idExhibicion,
                                    timeStamp, Integer.parseInt(dia),Integer.parseInt(mes) ,
                                    Integer.parseInt(ano), mCurrentPhotoPath, 1,getSelectedRadioGroup(),date, comentario);
                            imagenEspera = false;
                            //showImg.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.noimage));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cardView.setVisibility(View.GONE);
                                }
                            }, 3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    Toast.makeText(this, "Selecciona por lo menos un producto", Toast.LENGTH_SHORT).show();
                }



			}else{
				Toast.makeText(this, "No Seleccionaste Marca o \n Tipo de Exhibicion", Toast.LENGTH_SHORT).show();
			}


		}else{
			Toast.makeText(this, "No hay imagen capturada", Toast.LENGTH_SHORT).show();
		}


	}


    public void loadMultiSpinner(int idMarca){

        //LAS CATEGORÍAS SOLO SE VAN A CARGAR CUANDO SE TRATE DE CLOROX Y SU COMPETENCIA
        if(idMarca == 249 || (idMarca >= 356 && idMarca <=360)){

            //OCULTAR SPINNER DE PRODUCTOS Y MOSTRAR SPINNER DE CATEGORÍA
            spiCate.setVisibility(View.VISIBLE);
            multiSpinnerSelect.setVisibility(View.GONE);

            try {
                CategoriasProductoAdapter adapterCate = new CategoriasProductoAdapter(this, android.R.layout.simple_spinner_item, getArrayCategoriasByMarca(idMarca));
                spiCate.setAdapter(adapterCate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //multiSpinnerSelect.setCategorias(getArrayCategoriasByMarca(idMarca));
        }else{

            //OCULTAR SPINNER DE CATEGORÍAS Y MOSTRAR SPINNER DE PRODUCTOS
            spiCate.setVisibility(View.GONE);
            multiSpinnerSelect.setVisibility(View.VISIBLE);

            multiSpinnerSelect.setItems(getArrayListProByTiensda(idMarca, idTienda),
                    "Seleccione Producto");
        }


    }

    public void setRentadas(int idMarca){
        if(idMarca == 249){
            layoutRentas.setVisibility(View.VISIBLE);
        }else{
            layoutRentas.setVisibility(View.GONE);
        }
    }




    private ArrayList<ProductosModel> getArrayListProByTiensda(int idMarca, int idTienda){

        Cursor curProByTienda = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
        ArrayList<ProductosModel> arrayP = new ArrayList<>();
        if (curProByTienda.getCount() <= 0){

            Cursor curPro = new BDopenHelper(this).productos(idMarca);

            for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
                final ProductosModel spP = new ProductosModel();
                spP.setIdProducto(curPro.getInt(0));
                spP.setNombre(curPro.getString(1));
                spP.setPresentacion(curPro.getString(2));
                spP.setCodigoBarras(curPro.getString(3));
                spP.setIdMarca(curPro.getInt(4));
                spP.setHasImage(curPro.getInt(curPro.getColumnIndex("has_image")));



                arrayP.add(spP);
            }


            curPro.close();
        } else {

            Cursor curProByTienda2 = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);

            for(curProByTienda2.moveToFirst(); !curProByTienda2.isAfterLast(); curProByTienda2.moveToNext()){
                final ProductosModel spP = new ProductosModel();
                spP.setIdProducto(curProByTienda2.getInt(0));
                spP.setNombre(curProByTienda2.getString(1));
                spP.setPresentacion(curProByTienda2.getString(2));
                spP.setCodigoBarras(curProByTienda2.getString(3));
                spP.setIdMarca(curProByTienda2.getInt(4));
                spP.setHasImage(curProByTienda2.getInt(curProByTienda.getColumnIndex("has_image")));


                arrayP.add(spP);
            }

            curProByTienda2.close();

        }



        final ProductosModel spPinicio = new ProductosModel();
        spPinicio.setIdProducto(0);
        spPinicio.setNombre("Seleccione Producto");
        spPinicio.setPresentacion("producto sin seleccionar");
        spPinicio.setCodigoBarras(" ");

        arrayP.add(0,spPinicio);


        base.close();
        return arrayP;

    }

    //SE LLENA UN ARRAY DE LAS CATEGORÍAS DEPENDIENDO LA MARCA
    private ArrayList<SpinnerCateProdModel> getArrayCategoriasByMarca(int idMarca){
        Cursor curCategoria = new BDopenHelper(this).getCategorias();

        ArrayList<SpinnerCateProdModel> arrayCategorias = new ArrayList<>();

        if(idMarca == 249 || idMarca == 360){
            for(curCategoria.moveToFirst(); !curCategoria.isAfterLast(); curCategoria.moveToNext()) {
                final SpinnerCateProdModel categoria = new SpinnerCateProdModel();
                categoria.setId(curCategoria.getInt(0));
                categoria.setCategoria(curCategoria.getString(1));

                arrayCategorias.add(categoria); //AGREGAR TODAS LAS CATEGORÍAS PARA CLOROX Y OTRAS MARCAS
                //Log.i("ARRAY_CATE", categoria.getCategoria());
            }
            curCategoria.close();

        }else if(idMarca == 356){
            for(curCategoria.moveToFirst(); !curCategoria.isAfterLast(); curCategoria.moveToNext()) {
                final SpinnerCateProdModel categoria = new SpinnerCateProdModel();
                categoria.setId(curCategoria.getInt(0));
                categoria.setCategoria(curCategoria.getString(1));

                if(categoria.getId() == 1 || categoria.getId() == 6){
                    arrayCategorias.add(categoria); //AGREGAR SOLO BLEACH Y ANTISPLASH
                }
            }
            curCategoria.close();
        }else if(idMarca == 357){
            for(curCategoria.moveToFirst(); !curCategoria.isAfterLast(); curCategoria.moveToNext()) {
                final SpinnerCateProdModel categoria = new SpinnerCateProdModel();
                categoria.setId(curCategoria.getInt(0));
                categoria.setCategoria(curCategoria.getString(1));

                if(categoria.getId() == 3){
                    arrayCategorias.add(categoria); //AGREGAR SOLO LAUNDRY
                }
            }
            curCategoria.close();
        }else if(idMarca == 358){
            for(curCategoria.moveToFirst(); !curCategoria.isAfterLast(); curCategoria.moveToNext()) {
                final SpinnerCateProdModel categoria = new SpinnerCateProdModel();
                categoria.setId(curCategoria.getInt(0));
                categoria.setCategoria(curCategoria.getString(1));

                if(categoria.getId() == 4 || categoria.getId() == 5){
                    arrayCategorias.add(categoria); //AGREGAR SOLO MARVEL Y CDW
                }
            }
            curCategoria.close();
        }else if(idMarca == 359){
            for(curCategoria.moveToFirst(); !curCategoria.isAfterLast(); curCategoria.moveToNext()) {
                final SpinnerCateProdModel categoria = new SpinnerCateProdModel();
                categoria.setId(curCategoria.getInt(0));
                categoria.setCategoria(curCategoria.getString(1));

                if(categoria.getId() == 2){
                    arrayCategorias.add(categoria); //AGREGAR SOLO LIMPIADORES
                }
            }
            curCategoria.close();
        }

        final SpinnerCateProdModel primeraOpcion = new SpinnerCateProdModel();

        //SE AGREGA UNA OPCIÓN INICIAL
        primeraOpcion.setId(0);
        primeraOpcion.setCategoria("Selecciona una categoría");

        arrayCategorias.add(0, primeraOpcion);

        /*for(int j=0; j<7; j++){
            Log.i("ARRAY-CATE " + j, arrayCategorias.get(j).getCategoria());
        }*/

        base.close();
        return arrayCategorias;
    }


    //escucha cuando se selecciona un elemento en el spinner marca
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        MarcaModel marca = (MarcaModel) adapterView.getSelectedItem();

        int idMarca = marca.getId();

        loadMultiSpinner(idMarca);

        setRentadas(idMarca);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}




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
                    //progressFoto.setVisibility(View.VISIBLE);
                    donutProgress.setVisibility(View.VISIBLE);

                    btnSendImage.setVisibility(View.GONE);
                }
            });



        }

        @Override
        public void onProgress(final long bytesWritten, final long totalSize) {
            super.onProgress(bytesWritten, totalSize);



            progressFoto.post(new Runnable() {
                @Override
                public void run() {

                    /*
                    progressFoto.setProgress((int)bytesWritten);
                    progressFoto.setMax((int)totalSize);*/


                    donutProgress.setProgress((int) ((bytesWritten * 100) / totalSize));
                    donutProgress.setMax(100);

                }
            });
        }

        @Override
        public void onFinish() {
            super.onFinish();

            progressFoto.post(new Runnable() {
                @Override
                public void run() {
                    //progressFoto.setVisibility(View.GONE);
                    donutProgress.setVisibility(View.GONE);

                    //btnSendImage.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            Log.d("onFailure", responseString);
       }

        @Override
		public void onFailure(int statusCode, Header[] header,Throwable e,JSONObject errorResponse) {

            if(errorResponse != null) {
                Log.e("ERROR HTTP RESPONSE", errorResponse.toString());
            } else {
                Log.e("ERROR HTTP RESPONSE", "ErrorResponse is null");
            }


			Toast.makeText(getApplicationContext(), "No fue posible conectarse con el servidor", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "Imagen no envida \n (Menu Enviar)", Toast.LENGTH_SHORT).show();


			try {

				//db.insertarImagen(_idTienda, _idPromo, _idMarca, _idExhib, _timeStamp, _dia, _mes, _ano, mCurrentPhotoPath, 1);
				imagenEspera = false;

				cardView.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView.setVisibility(View.GONE);
                        btnSendImage.setVisibility(View.VISIBLE);
                    }
                });
				//showImg.setImageDrawable(ContextCompat.getDrawable(PhotoCapture.this,R.drawable.noimage));
			} catch (SQLiteException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void onSuccess(int statusCode,Header[] headers,JSONObject response) {

            Log.d("EnviarFoto","Estatus "+statusCode);
            //Log.d("EnviarFoto","Response" + response);


			if(response != null){
				try {

					//Log.d("Respuestas Ima", response.getString("insert"));
					if(response.getBoolean("insert")){
						Toast.makeText(getApplicationContext(), "Imagen Recibida", Toast.LENGTH_SHORT).show();
						//showImg.setImageDrawable(ContextCompat.getDrawable(PhotoCapture.this,R.mipmap.ic_done));

						imageDone.setVisibility(View.VISIBLE);


						Log.w("fileSizeServer",response.getString("fileSize"));


						imagenEspera = false;
						spiExh.setSelection(0);
						spiMarca.setSelection(0);

                        //deleteArchivo(_imgPath);
                        base.execSQL("Update photo set status=2 where idPhoto="+this._idPhotho);
                        base.close();
                        //Log.v("DEBUG117", "AQUI");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showImg.setImageDrawable(null);
                                imageDone.setVisibility(View.INVISIBLE);
                                cardView.setVisibility(View.GONE);
                                btnSendImage.setVisibility(View.VISIBLE);
                            }
                        },2500);


					}else{
						if(response.getInt("code") == 3){
							//showImg.setImageDrawable(ContextCompat.getDrawable(PhotoCapture.this,R.mipmap.ic_done));
                            imageDone.setVisibility(View.VISIBLE);
							imagenEspera = false;
						}
                        //deleteArchivo(_imgPath);

						Toast.makeText(getApplicationContext(), response.getString("message"),
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
        if (v.getId() == R.id.btn_send_image) EnviarImagen();
	}

    //metod: conexion verify
	public boolean verificarConexion() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }

        return netInfo != null && netInfo.isConnected();
	}

    //metod: spinner load
	private void loadSpinner(){
		try {
			MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spiMarca.setAdapter(adapter);

		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}

	}
    //
	private ArrayList<MarcaModel> getArrayList(){

		base = new BDopenHelper(this).getReadableDatabase();
        String sql = "select tm.idMarca as _id, m.nombre, m.img " +
                "from tienda_marca tm " +
                "left join marca m on tm.idMarca = m.idMarca " +
                "where tm.idTienda = " + idTienda + " " +
                "and tm.idMarca not in(356, 357, 358, 359, 360)" + ";";
		//String sql1 = "select idMarca as _id, nombre, img from marca order by nombre asc;";
		Cursor cursorMarca = base.rawQuery(sql, null);

		for(cursorMarca.moveToFirst(); !cursorMarca.isAfterLast(); cursorMarca.moveToNext()){

			final MarcaModel spiM = new MarcaModel();
			spiM.setNombre(cursorMarca.getString(1));
			spiM.setId(cursorMarca.getInt(0));
            spiM.setUrl(cursorMarca.getString(2));

			array.add(spiM);
		}

		final MarcaModel spiMfirst = new MarcaModel();
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

    private int getSelectedRadioGroup(){


        int radioButtonId = radioChoice.getCheckedRadioButtonId();
        View radioButton = radioChoice.findViewById(radioButtonId);

        Log.d("RadioValue","valor:"+ radioChoice.indexOfChild(radioButton));

        return radioChoice.indexOfChild(radioButton);

    }

    private void camaraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CAMERA_PERMISSION);
        }
    }


    private boolean cameraPermissionEneabled(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }




}
