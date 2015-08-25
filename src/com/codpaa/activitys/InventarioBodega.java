package com.codpaa.activitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.codpaa.adapters.CustomAdapter;
import com.codpaa.updates.EnviarDatos;
import com.codpaa.adapters.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.models.SpinnerMarcaModel;
import com.codpaa.models.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;


public class InventarioBodega extends AppCompatActivity implements OnClickListener,OnItemSelectedListener{
	
	
	int idTienda, idPromotor;
	TextView tienda, promotor;
	SQLiteDatabase base;
    RadioButton piezas, cajas, selec;
    RadioGroup radio;
	Spinner marca,producto;
	Button guardar;
    static TextView textFecha;

	EditText editFisico, editSistema, editLote;
	InputMethodManager im;
	ArrayList<SpinnerMarcaModel> array = new ArrayList<>();
	Locale locale;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventariobodega);
		locale = new Locale("es_MX");
		Intent i = getIntent();

		idTienda = i.getIntExtra("idTienda",0);
		idPromotor = i.getIntExtra("idPromotor",0);
		
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		tienda = (TextView) findViewById(R.id.textInvTie);
		promotor = (TextView) findViewById(R.id.textInvPromo);
		marca = (Spinner) findViewById(R.id.spiInMar);
		producto = (Spinner) findViewById(R.id.spiInvPro);
		textFecha = (TextView) findViewById(R.id.txtfecha);
	
		guardar = (Button) findViewById(R.id.bInvGuar);
		editFisico = (EditText) findViewById(R.id.editInv);
        editSistema = (EditText) findViewById(R.id.editSistema);
		editLote = (EditText) findViewById(R.id.editLote);

        radio = (RadioGroup) findViewById(R.id.radioInventario);
        piezas = (RadioButton) radio.findViewById(R.id.radioTipo1);
        cajas = (RadioButton) radio.findViewById(R.id.radioTipo2);




		piezas.setChecked(true);
		
		marca.setOnItemSelectedListener(this);
		guardar.setOnClickListener(this);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		try {
			
			Cursor cNomUser = new BDopenHelper(this).nombrePromotor(idPromotor);
			cNomUser.moveToFirst();
			promotor.setText(cNomUser.getString(0));
            cNomUser.close();
			try {
				Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
				cTienda.moveToFirst();
				tienda.setText(cTienda.getString(0)+" "+cTienda.getString(1));
				
				try {

					loadSpinner();
					
					
				}catch(Exception e) {
					Toast.makeText(this, "Error  3", Toast.LENGTH_SHORT).show();
				}
				cTienda.close();
			}catch(Exception e) {
				Toast.makeText(this, "Error  2", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error  1", Toast.LENGTH_SHORT).show();
			
		}


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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inventario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.save_inventario:
                guardarLosDatos();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {
		SpinnerMarcaModel spM = (SpinnerMarcaModel) marca.getSelectedItem();
		int idMarca = spM.getId();

		loadSpinnerProd(idMarca);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

	public void showDatePickerDialog(View v){
		DialogFragment dialogFragment = new TimePickerFragment();

		dialogFragment.show(getSupportFragmentManager(), "datePicker");

	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {

            case R.id.bInvGuar:
                guardarLosDatos();
                break;

        }
		
		
	}

	private void guardarLosDatos() {
		try {
			
			int cantidadFisico = 0, cantidadSistema = 0;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
			
			SpinnerMarcaModel spM = (SpinnerMarcaModel) marca.getSelectedItem();
			SpinnerProductoModel spP = (SpinnerProductoModel) producto.getSelectedItem();
			int idMarca = spM.getId();
			int idProdu = spP.getIdProducto();
			
			String fecha = dFecha.format(c.getTime());
			
			if(idMarca != 0){
				if(idProdu != 0){
					if(editFisico.getText().length() >0 || editSistema.getText().length() >0) {

                        if(editFisico.getText().length()>0)
						    cantidadFisico = Integer.parseInt(editFisico.getText().toString());
                        if(editSistema.getText().length()>0)
                            cantidadSistema = Integer.parseInt(editSistema.getText().toString());
						
						try {

                            selec = (RadioButton) findViewById(radio.getCheckedRadioButtonId());
							
							try {
								new BDopenHelper(this).insertarInventario(idTienda,idPromotor ,fecha, idProdu, cantidadFisico,cantidadSistema,1
										,selec.getText().toString(),getFechaCaducidad(), editLote.getText().toString());
								Toast.makeText(this,"Datos Guardados", Toast.LENGTH_SHORT).show();
								editFisico.setText("");
                                editSistema.setText("");
								editLote.setText("");
								textFecha.setText("fecha");
                                producto.setSelection(0);
								im.hideSoftInputFromWindow(editFisico.getWindowToken(), 0);
								new EnviarDatos(this).enviarInventario();
							}catch(Exception e) {
								e.printStackTrace();
							}
						}catch(Exception e) {
							e.printStackTrace();
							
						}
						
					} else {
						Toast.makeText(this,"No se definio la cantidad", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this,"NO seleccionaste Producto", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this,"NO seleccionaste Marca", Toast.LENGTH_SHORT).show();
			}
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadSpinner(){
		try {
			
			
			CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			marca.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
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
			arrayP.add(spP);
		}
		final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");
		
		arrayP.add(0, spPinicio);
		curPro.close();
		base.close();
		return arrayP;
		
	}
	
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

	public static class TimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			//estableciendo el tiempo actual como tiempo para el picker
			final Calendar c = Calendar.getInstance();


			return new DatePickerDialog(getActivity(),this,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}



		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {



            String dia = dayOfMonth+"";
            String mes = (monthOfYear+1)+"";
            if (dayOfMonth < 10){
                dia = 0 + dia;
            }

            if (monthOfYear + 1 < 10){
                mes = 0 + mes;
            }

            textFecha.setText(dia+"-"+mes+"-"+year);




		}
	}

	public String getFechaCaducidad(){
		String salidaFecha;
		if (!textFecha.getText().equals("fecha")){
			salidaFecha = textFecha.getText().toString();
		}else {
			salidaFecha = "";
		}
		return salidaFecha;
	}

	
	

}