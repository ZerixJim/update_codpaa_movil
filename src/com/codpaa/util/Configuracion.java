package com.codpaa.util;

import android.content.Context;
import android.content.SharedPreferences;


public class Configuracion {
	

	private final String KEY_USER = "user";
    private final String KEY_PRODUCTO = "producto";
    private final String KEY_MARCA = "marca";
    private final String KEY_TIENDAS = "tiendas";
    private final String KEY_EXHIBI = "exhibiciones";
    private final String KEY_RUTA = "ruta";
    private final String KEY_VERSION = "version";
    private final String KEY_PRODUCTO_TIENDA = "productoTienda";
    private final String KEY_PRODUCTO_FORMATO = "productoFormato";
    private final String KEY_MATERIALES = "materiales";
    private final String KEY_INSTALL_APPS = "install_apps";
    //private final String KEY_ENCUESTA = "encuestaDisponible";


    private Context mContext;
	
	public Configuracion(Context context){
		
		mContext = context;
		
	}
	
	private SharedPreferences getSettings(){

        String SHARED_PREFS_FILE = "HMPrefs";
		return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
	} 

	 
	public void setUserUser(String user){
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(KEY_USER, user);
		//editor.commit();
        editor.apply();
	}

	public void setFecha(String fecha){
		SharedPreferences.Editor editor = getSettings().edit();

        String KEY_DATE = "fecha";
        editor.putString(KEY_DATE, fecha);
		//editor.commit();
        editor.apply();
	}

    public void setRuta(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();

        editor.putString(KEY_RUTA, fecha);
        //editor.commit();
        editor.apply();
    }

    public void setExhibicion(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();

        editor.putString(KEY_EXHIBI, fecha);
        //editor.commit();
        editor.apply();
    }


    public void setTiendas(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();

        editor.putString(KEY_TIENDAS, fecha);
        //editor.commit();
        editor.apply();
    }

    public void setInstallApps(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();

        editor.putString(KEY_INSTALL_APPS, fecha);

        editor.apply();
    }

    public void setMarca(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();

        editor.putString(KEY_MARCA, fecha);
        //editor.commit();
        editor.apply();
    }

    public void setProducto(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();

        editor.putString(KEY_PRODUCTO, fecha);
        //editor.commit();
        editor.apply();
    }

    public void setProductoByTienda(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_PRODUCTO_TIENDA, fecha);

        editor.apply();
    }

    public void setProductoByFormato(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_PRODUCTO_FORMATO, fecha);

        editor.apply();
    }

    public void setMaterialesUpdate(String fechaUpdate){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_MATERIALES, fechaUpdate);
        editor.apply();
    }

    /*public void setEncuestaDisponibleByDay(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();

        editor.putString(KEY_ENCUESTA, fecha);

        editor.apply();

    }*/

    public void setVersionDate(String fecha){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_VERSION, fecha);
        editor.apply();
    }

    public String getUserName() { return getSettings().getString(KEY_USER, null); }

    //public String getDate () { return getSettings().getString(KEY_DATE, null); }

    public String getRuta() {return getSettings().getString(KEY_RUTA, null);}

    public String getExhi() {return getSettings().getString(KEY_EXHIBI, null);}

    public String getTiendas() { return getSettings().getString(KEY_TIENDAS, null);}

    public String getMarca() { return getSettings().getString(KEY_MARCA, null);}

    public String getProducto() { return getSettings().getString(KEY_PRODUCTO, null);}

    public String getVersion(){
        return getSettings().getString(KEY_VERSION, null);
    }

    public String getProductoByTienda(){
        return getSettings().getString(KEY_PRODUCTO_TIENDA, null);
    }
	public String getProductoByFormato(){
        return getSettings().getString(KEY_PRODUCTO_FORMATO, null);
    }

    public String getInstallApps(){
        return getSettings().getString(KEY_INSTALL_APPS, null);
    }

    public String getMaterialesUpdate(){
        return getSettings().getString(KEY_MATERIALES, null);
    }
    /*public String getKEY_ENCUESTA(){
        return getSettings().getString(KEY_ENCUESTA, null);
    }
*/

    public String getKeyByTag(String tag){
        return getSettings().getString(tag, null);
    }

    public void setKey(String tag, String value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(tag, value);
        editor.apply();
    }
	

}
