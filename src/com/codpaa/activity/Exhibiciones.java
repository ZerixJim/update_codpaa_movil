
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.codpaa.adapter.CustomAdapter;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.SpinnerMarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;

public class Exhibiciones extends AppCompatActivity implements OnItemSelectedListener{
	
	int idTienda, idPromotor;
	SQLiteDatabase base;
	Spinner marca,producto,exhibicion;
    EditText cantidadExhi;
	InputMethodManager im;

	ArrayList<MarcaModel> array = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibiciones);

		Intent i = getIntent();
		idTienda =  i.getExtras().getInt("idTienda");
		idPromotor =  i.getExtras().getInt("idPromotor");

		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		

		marca =  findViewById(R.id.spiExhMarca);
		producto =  findViewById(R.id.spiExhProd);
		exhibicion =  findViewById(R.id.spiExhibi);


        Toolbar toolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        cantidadExhi =  findViewById(R.id.cantidadExhibicion);

		marca.setOnItemSelectedListener(this);


		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		try {

            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                Cursor cNomUser = new BDopenHelper(this).nombrePromotor(idPromotor);
                cNomUser.moveToFirst();

                actionBar.setSubtitle(cNomUser.getString(0));

                Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
                cTienda.moveToFirst();

                actionBar.setTitle(String.format("%s %s", cTienda.getString(0), cTienda.getString(1)));

            }

            try {

                loadSpinner();

                try {

                    CustomAdapter adaptadorExi = new CustomAdapter(this,android.R.layout.simple_spinner_item, getArrayExhi());

                    exhibicion.setAdapter(adaptadorExi);



                }catch(Exception e){
                    Toast.makeText(this, "Error  4", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }catch(Exception e) {
                Toast.makeText(this, "Error  3", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

			
		}catch(Exception e) {
			Toast.makeText(this, "Error  1", Toast.LENGTH_SHORT).show();
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

			case R.id.save_exhibiciones:

				guardarLosDatos();
				return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_exhibiciones, menu);

		return true;
	}


	private void guardarLosDatos() {
		try {
			
			float cantidad = 0;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
			
			String fecha = dFecha.format(c.getTime());
			if(cantidadExhi.getText().toString().length() > 0){
                cantidad = Float.valueOf(cantidadExhi.getText().toString());
            }

			try {
				MarcaModel spM = (MarcaModel) marca.getSelectedItem();
				SpinnerProductoModel spP = (SpinnerProductoModel) producto.getSelectedItem();
                SpinnerMarcaModel spe = (SpinnerMarcaModel) exhibicion.getSelectedItem();
				
				int idMarca = spM.getId();
				int idProdu = spP.getIdProducto();
				int idEx = spe.getId();
				
				if(idMarca != 0){
					if(idProdu != 0 && idEx != 0){
						try {
							new BDopenHelper(this).insertarExhibiciones(idTienda, idPromotor, idEx, fecha, idProdu, cantidad,1);
							Toast.makeText(this,"Datos Guardados", Toast.LENGTH_SHORT).show();
                            cantidadExhi.setText("");
                            producto.setSelection(0);
                            exhibicion.setSelection(0);
							
							new EnviarDatos(this).enviarExibiciones();
						}catch(Exception e) {
							Toast.makeText(this,"Error al guardar", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(this,"No seleccionaste Producto o Exhibicion", Toast.LENGTH_SHORT).show();
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
	
	

}
