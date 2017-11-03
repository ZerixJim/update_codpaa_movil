package com.codpaa.activity.impulsor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.codpaa.R;
import com.codpaa.activity.PhotoCapture;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * Created by grim on 31/10/2017.
 */

public class TiendaNueva extends AppCompatActivity implements View.OnClickListener {

    private final int TAKE_PHOTO_CODE = 0;
    private Button btnPhto;
    private Uri imageToUpload;
    private ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_tienda_activity);



        btnPhto = (Button) findViewById(R.id.btn_captura);
        imageView = (ImageView) findViewById(R.id.image);


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

                    Picasso.with(this).load(imageToUpload).into(imageView);
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



    public Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
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
}
