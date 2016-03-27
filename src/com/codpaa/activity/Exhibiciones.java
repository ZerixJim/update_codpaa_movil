
package com.codpaa.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;

public class Exhibiciones extends AppCompatActivity implements OnClickListener,OnItemSelectedListener{
	
	int idTienda, idPromotor;
	TextView tienda, promotor;
	SQLiteDatabase base;
	Spinner marca,producto,exhibicion;
    EditText cantidadExhi;
    Locale locale;
	Button guardar;
	InputMethodManager im;

	ArrayList<MarcaModel> array = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibiciones);

        locale = new Locale("es_MX");
		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");

		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		tienda = (TextView) findViewById(R.id.ExhiTienda);
		promotor = (TextView) findViewById(R.id.ExhiPromo);
		marca = (Spinner) findViewById(R.id.spiExhMarca);
		producto = (Spinner) findViewById(R.id.spiExhProd);
		exhibicion = (Spinner) findViewById(R.id.spiExhibi);
		guardar = (Button) findViewById(R.id.btnGuaEx);


        cantidadExhi = (EditText) findViewById(R.id.cantidadExhibicion);

		marca.setOnItemSelectedListener(this);
		guardar.setOnClickListener(this);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		try {
			
			Cursor cNomUser = new BDopenHelper(this).nombrePromotor(idPromotor);
			cNomUser.moveToFirst();
			promotor.setText(cNomUser.getString(0));
			try {
				Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
				cTienda.moveToFirst();
				tienda.setText(String.format("%s %s", cTienda.getString(0), cTienda.getString(1)));
				
				try {

					loadSpinner();
					
					try {
						base = new BDopenHelper(this).getReadableDatabase();
						Cursor cuExhi = base.rawQuery("select idExhibicion as _id, nombre from" +
                                " tipoexhibicion order by nombre asc;", null);
						SimpleCursorAdapter adaptadorExi = new SimpleCursorAdapter(this,
                                android.R.layout.simple_spinner_item,
                                cuExhi, new String[]{"nombre"},new int[]{android.R.id.text1},0);

						exhibicion.setAdapter(adaptadorExi);

						base.close();
						


					}catch(Exception e){
						Toast.makeText(this, "Error  4", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}

				}catch(Exception e) {
					Toast.makeText(this, "Error  3", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				
			}catch(Exception e) {
				Toast.makeText(this, "Error  2", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error  1", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		try {
			//assert getSupportActionBar() != null;
			ActionBar actionBar = getSupportActionBar();

            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setLogo(R.drawable.ic_launcher);
            }


		}catch (NullPointerException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {
		MarcaModel spM = (MarcaModel) marca.getSelectedItem();
		int idMarca = spM.getId();
	
		
		loadSpinnerProd(idMarca);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
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

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		
		case R.id.btnGuaEx:
			guardarLosDatos();
			break;

		}
		
		
	}

	private void guardarLosDatos() {
		try {
			
			float cantidad = 0;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",locale);
			
			String fecha = dFecha.format(c.getTime());
			if(cantidadExhi.getText().toString().length() > 0){
                cantidad = Float.valueOf(cantidadExhi.getText().toString());
            }

			try {
				MarcaModel spM = (MarcaModel) marca.getSelectedItem();
				SpinnerProductoModel spP = (SpinnerProductoModel) producto.getSelectedItem();
				
				int idMarca = spM.getId();
				int idProdu = spP.getIdProducto();
				int idEx = (int) exhibicion.getItemIdAtPosition(exhibicion.getSelectedItemPosition());
				
				if(idMarca != 0){
					if(idProdu != 0){
						try {
							new BDopenHelper(this).insertarExhibiciones(idTienda, idPromotor, idEx, fecha, idProdu, cantidad,1);
							Toast.makeText(this,"Datos Guardados", Toast.LENGTH_SHORT).show();
                            cantidadExhi.setText("");
                            producto.setSelection(0);
							
							new EnviarDatos(this).enviarExibiciones();
						}catch(Exception e) {
							Toast.makeText(this,"Error al guardar", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(this,"No seleccionaste Producto", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this,"No seleccionaste Marca", Toast.LENGTH_SHORT).show();
				}
				
				
			}catch(Exception e) {
				
				Toast.makeText(this,"Articulos no seleccionados", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadSpinner(){
		try {
			
			
			MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			marca.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
		}
		
	}

	
	private void loadSpinnerProd(int idM){
		try {
			ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListPro(idM));
			producto.setAdapter(proAdap);
			
		} catch (Exception e) {
			Toast.makeText(this, "Error Mayoreo 4", Toast.LENGTH_SHORT).show();
		}
	}
	
	private ArrayList<SpinnerProductoModel> getArrayListPro(int idMarca){
		
		Cursor curPro = new BDopenHelper(this).productos(idMarca);
		ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();
		for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
			final SpinnerProductoModel spP = new SpinnerProductoModel();
			spP.setIdProducto(curPro.getInt(0));
			spP.setNombre(curPro.getString(1));
			spP.setPresentacion(curPro.getString(2));
            spP.setCodigoBarras(curPro.getString(3));
			spP.setIdMarca(curPro.getInt(4));
			arrayP.add(spP);
		}
		final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");
		
		arrayP.add(0,spPinicio);
		
		base.close();
		return arrayP;
		
	}
	
	private ArrayList<MarcaModel> getArrayList(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String sql = "select idMarca as _id, nombre, img from marca order by nombre asc;";
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
		
		array.add(0, spiMfirst);
        cursorMarca.close();
		base.close();
		return array;
		
	}
	
	

}
