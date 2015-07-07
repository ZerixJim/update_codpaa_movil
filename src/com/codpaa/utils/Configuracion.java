package com.codpaa.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Configuracion {
	

	private final String KEY_USER = "user";

	
	private Context mContext;
	
	public Configuracion(Context context){
		
		mContext = context;
		
	}
	
	private SharedPreferences getSettings(){

        String SHARED_PREFS_FILE = "HMPrefs";
		return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
	} 

	 
	public String getUserName(){ 
		return getSettings().getString(KEY_USER, null);
	} 
	
	public void setUserUser(String user){     
		SharedPreferences.Editor editor = getSettings().edit();     
		editor.putString(KEY_USER, user);     
		//editor.commit();
        editor.apply();
	}
	
	public void setFecha(String fecha){
		SharedPreferences.Editor editor = getSettings().edit();
        String KEY_UPDATE = "fechaUP";
		editor.putString(KEY_UPDATE, fecha);
		//editor.commit();
        editor.apply();
	}
	
	

}
