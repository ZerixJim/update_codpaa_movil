package com.codpaa.activity.impulsor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.codpaa.R;
import com.codpaa.util.Utilities;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/*
 * Created by grim on 31/10/2017.
 */

public class TiendaNueva extends AppCompatActivity implements View.OnClickListener {

    private final int TAKE_PHOTO_CODE = 0;
    private Uri imageToUpload;
    private ImageView imageView;
    private EditText shopName;
    private DonutProgress progress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_tienda_activity);


        Button btnPhto = (Button) findViewById(R.id.btn_captura);
        shopName = (EditText) findViewById(R.id.nombre);
        imageView = (ImageView) findViewById(R.id.image);

        progress = (DonutProgress) findViewById(R.id.progress_photo);
        if (progress != null) {
            progress.setMax(100);
        }


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);


        }



        if (btnPhto != null) {
            btnPhto.setOnClickListener(this);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK){


            if (imageToUpload != null){


                Bitmap bitmap = getBitmap(imageToUpload.getPath());


                if (bitmap != null){

                    Picasso.get().load(imageToUpload).into(imageView);
                }


            }




        }else {




            File file = new File(imageToUpload.getPath());

            if (file.exists()){
                if (file.delete()){
                    Log.d("delete", "true");
                }else {
                    Log.d("delete", "false");
                }
            }

            imageToUpload = null;

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_captura:
                startCam();


                break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_upload_image, menu);

        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.send_image:

                sendImage();

                return true;

            case android.R.id.home:

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);


        }




    }

    public Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in;
        try {
            final int IMAGE_MAX_SIZE = 1000000; // 1.0MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            if (in != null) {
                in.close();
            }


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " +
                    o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " +
                        width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            if (in != null) {
                in.close();
            }

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    private void startCam() {

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            File file = createImageFile();

            imageToUpload = Uri.fromFile(file);


            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUpload);

            startActivityForResult(camIntent, TAKE_PHOTO_CODE);


        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    private File createImageFile() throws IOException {


        String timeStamp = new SimpleDateFormat("ddMMyyyykm", Locale.getDefault()).format(new Date());


        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);


        File codpaaDir = new File(storageDir.getPath() + "/codpaa/");
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


        //mCurrentPhotoPath = image.getAbsolutePath();

        return File.createTempFile(timeStamp, ".jpg", codpaaDir);
    }


    private void sendImage(){

        if (shopName.getText().length() > 0){

            if (imageToUpload != null){

                Toast.makeText(this, "Enviando", Toast.LENGTH_SHORT).show();

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();


                params.put("nombre", shopName.getText().toString());



                File file = new File(imageToUpload.getPath());

                try {
                    params.put("file",  file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



                client.post(Utilities.WEB_SERVICE_CODPAA
                        + "upload_image_estudio_mercado.php", params, new JsonHttpResponseHandler(){

                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {

                        final int progres = (int) ((bytesWritten * 100) / totalSize);

                        progress.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setProgress(progres);
                            }
                        });

                    }

                    @Override
                    public void onStart() {
                        progress.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);


                        //Log.d("response", response.toString());

                        if (response != null){

                            try {
                                if (response.getBoolean("insert")){


                                    Toast.makeText(getApplicationContext(), "Imagen enviada", Toast.LENGTH_SHORT).show();


                                    imageView.setImageResource(0);
                                    shopName.setText("");
                                    imageToUpload = null;



                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }




                    }

                    @Override
                    public void onFinish() {
                        progress.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        //Log.d("Error", "" + responseString + "errordfasdfds");
                        Toast.makeText(getApplicationContext(),
                                "Error al enviar, intentelo mas tarde", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                        //Log.d("response", "errorfasfsadfsdafsad");

                        super.onFailure(statusCode, headers, throwable, errorResponse);

                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                        //Log.d("Error", "errorororro");
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });




            }else {

                Toast.makeText(this, "Imagen no capturada", Toast.LENGTH_SHORT).show();

            }


        }else {
            Toast.makeText(this, "Campo requerido", Toast.LENGTH_SHORT).show();
        }


    }
}
