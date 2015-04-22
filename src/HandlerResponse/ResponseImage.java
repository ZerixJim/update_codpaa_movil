package HandlerResponse;


import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.codpaa.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import BD.BDopenHelper;


public class ResponseImage extends JsonHttpResponseHandler  {


    private int _idTienda;
    private int _idMarca;
    private String _fecha;
    private String _imgPath;


    NotificationManager notification;
    NotificationCompat.Builder notificationBuilder;

    Context _con;
    int id = 1;
    SQLiteDatabase db;

    public ResponseImage(Context context,String imgPath, int idTienda, int idMarca,
                         String fecha) {
        this._con = context;


        this._imgPath = imgPath;
        this._idTienda = idTienda;
        this._idMarca = idMarca;
        this._fecha = fecha;


        notification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(context);

        Log.e("ResponseImage","paso 1");

    }

    @Override
    public void onStart() {
        super.onStart();
        notificationBuilder.setContentTitle("Enviando imagen")
                .setContentText("subiendo la foto")
                .setSmallIcon(R.drawable.subirimagen);

        notification.notify(id,notificationBuilder.build());
        Log.e("ResponseImage","paso 2");

    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);
        notificationBuilder.setProgress(totalSize,bytesWritten,false);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        Log.e("ResponseImage","paso 3");
        if(response != null){
            try {

                Log.d("Respuestas Ima", response.getString("insert"));
                if(response.getBoolean("bol")){

                    notificationBuilder.setContentText("se envio correctamente")
                            .setProgress(0,0,false);
                    notification.notify(id,notificationBuilder.build());
                    db = new BDopenHelper(_con).getWritableDatabase();
                    db.execSQL("Update photo set status=2 where idTienda="+this._idTienda+" and " +
                            "idMarca="+this._idMarca+" and fecha="+this._fecha);
                    deleteArchivo(_imgPath);

                    db.close();

                }else{
                    if(response.getInt("code") == 3){

                    }
                    deleteArchivo(_imgPath);


                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

        }else{
            Log.d("Response image", "Sin respuesta");
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, e, errorResponse);
        notificationBuilder.setContentText("fallo, el envio")
                .setProgress(0,0,false);
        notification.notify(id,notificationBuilder.build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    public void deleteArchivo(String filePath){
        File img = new File(filePath);
        if(img.delete()){
            Log.d("Delete file","Archivo borrado");
        }else{
            Log.d("Delete file","Archivo no se pudo borrar");
        }
    }
}
