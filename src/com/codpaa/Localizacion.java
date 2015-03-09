package com.codpaa;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Localizacion implements LocationListener{
	
	LocationManager lm;
	
	
	public String getBestProvider(){

		Criteria criterio = new Criteria();
		criterio.setPowerRequirement(Criteria.POWER_MEDIUM);
		String bestProvider = lm.getBestProvider(criterio, true);
		
		return bestProvider;
		
		
	}
	
	public Location ubicacion(){
		Location ubicacion = lm.getLastKnownLocation(getBestProvider());
		
		return ubicacion;
		
	}
	
	

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
