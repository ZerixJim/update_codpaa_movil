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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;

public class Frentes extends AppCompatActivity implements OnClickListener, OnItemSelectedListener{

	int idPromotor, idTienda;
	Button btnGuar, btn1,btn2,btn3,btn4,btn5,btn6;
	EditText unifila, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14;
	EditText Echa1,Echa2,Echa3,Echa4,Echa5,Echa6;
	InputMethodManager im;
	BDopenHelper baseH;
	Toolbar toolbar;
	Locale locale;

	ArrayList<MarcaModel> array = new ArrayList<>();
	SQLiteDatabase base;
	Spinner spiMarca, spiPro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frentes);

		toolbar = (Toolbar) findViewById(R.id.toolbar_frentes);

		if(toolbar != null){
			setSupportActionBar(toolbar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null){
				actionBar.setDisplayHomeAsUpEnabled(true);

			}
		}

		locale = new Locale("es_MX");

		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");

		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


		spiMarca = (Spinner) findViewById(R.id.spinnerMarFre);
		spiPro = (Spinner) findViewById(R.id.spinnerFrePro);

		//asiganacion de botones
		btn1 = (Button) findViewById(R.id.btnf1);
		btn2 = (Button) findViewById(R.id.btn_charola_2);
		btn3 = (Button) findViewById(R.id.btnf3);
		btn4 = (Button) findViewById(R.id.btnfoto);
		btn5 = (Button) findViewById(R.id.btnf5);
		btn6 = (Button) findViewById(R.id.btnf6);

		//asignacion de campos de texto
		Echa1 = (EditText) findViewById(R.id.editCha1);
		Echa2 = (EditText) findViewById(R.id.editCha2);
		Echa3 = (EditText) findViewById(R.id.editCha3);
		Echa4 = (EditText) findViewById(R.id.editCha4);
		Echa5 = (EditText) findViewById(R.id.editCha5);
		Echa6 = (EditText) findViewById(R.id.editCha6);

		//asignacion de escuchadores
		Echa1.setVisibility(View.INVISIBLE);
		Echa2.setVisibility(View.INVISIBLE);
		Echa3.setVisibility(View.INVISIBLE);
		Echa4.setVisibility(View.INVISIBLE);
		Echa5.setVisibility(View.INVISIBLE);
		Echa6.setVisibility(View.INVISIBLE);


		unifila = (EditText) findViewById(R.id.unifila);
		f1 = (EditText) findViewById(R.id.caja1);
		f2 = (EditText) findViewById(R.id.caja2);
		f3 = (EditText) findViewById(R.id.caja3);
		f4 = (EditText) findViewById(R.id.caja4);
		f5 = (EditText) findViewById(R.id.caja5);
		f6 = (EditText) findViewById(R.id.caja6);
		f7 = (EditText) findViewById(R.id.caja7);
		f8 = (EditText) findViewById(R.id.caja8);
		f9 = (EditText) findViewById(R.id.caja9);
		f10 = (EditText) findViewById(R.id.caja10);
		f11 = (EditText) findViewById(R.id.caja11);
		f12 = (EditText) findViewById(R.id.caja12);
		f13 = (EditText) findViewById(R.id.caja13);
		f14 = (EditText) findViewById(R.id.caja14);


		btnGuar = (Button) findViewById(R.id.btonChFr);


		if (btnGuar != null) {
			btnGuar.setOnClickListener(this);
		}
		spiMarca.setOnItemSelectedListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);



		try {

			baseH = new BDopenHelper(this);

			try {
				Cursor cTienda = baseH.tienda(idTienda);
				cTienda.moveToFirst();
				if(getSupportActionBar() != null){
					getSupportActionBar().setSubtitle(cTienda.getString(0)+" "+cTienda.getString(1));
				}
				cTienda.close();
				try {

					loadSpinner();


				}catch(Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Error Frentes 3", Toast.LENGTH_SHORT).show();
				}

			}catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "Error Frentes 2", Toast.LENGTH_SHORT).show();
			}


		}catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Error Frentes 1", Toast.LENGTH_SHORT).show();

		}


		/**
		 * keyboard soft hide
		 */
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



	}



	@Override
	protected void onPause() {
		super.onPause();


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {


			case android.R.id.home:
				this.finish();
				return true;

			case R.id.save_frentes:
				guardarDatos();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_frentes, menu);


		return true;
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
			case R.id.btnf1: reQuesFocus(Echa1);break;
			case R.id.btn_charola_2: reQuesFocus(Echa2);break;
			case R.id.btnf3: reQuesFocus(Echa3);break;
			case R.id.btnfoto: reQuesFocus(Echa4);break;
			case R.id.btnf5: reQuesFocus(Echa5);break;
			case R.id.btnf6: reQuesFocus(Echa6);break;
			case R.id.btonChFr: guardarDatos(); break;
			case R.id.buttonExhib: finish(); break;
		}


	}



	private void reQuesFocus(View v){
		v.setVisibility(View.VISIBLE);
		v.requestFocus();

		im.showSoftInput(v, 0);


	}



	public void guardarDatos() {
		int cha1 = 0,cha2 = 0, cha3 = 0, cha4 = 0, cha5 = 0, cha6 = 0;
		int uni, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12,v13,v14;

		try {

			if(Echa1.isShown() && Echa1.getText().length() >0) {
				cha1 = Integer.parseInt(Echa1.getText().toString());

			}
			if(Echa2.isShown() && Echa2.getText().length() >0) {
				cha2 = Integer.parseInt(Echa2.getText().toString());

			}

			if(Echa3.isShown() && Echa3.getText().length() >0) {
				cha3 = Integer.parseInt(Echa3.getText().toString());

			}

			if(Echa4.isShown() && Echa4.getText().length() >0) {
				cha4 = Integer.parseInt(Echa4.getText().toString());

			}

			if(Echa5.isShown() && Echa5.getText().length() >0) {
				cha5 = Integer.parseInt(Echa5.getText().toString());

			}
			if(Echa6.isShown() && Echa6.getText().length() >0) {
				cha6 = Integer.parseInt(Echa6.getText().toString());

			}

			try {

				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
				MarcaModel spm = (MarcaModel) spiMarca.getSelectedItem();
				SpinnerProductoModel spPm = (SpinnerProductoModel) spiPro.getSelectedItem();


				String fecha = dFecha.format(c.getTime());


				uni = unifila.getText().length() > 0 ? Integer.parseInt(unifila.getText().toString()) : 0;
				v1 = f1.getText().length() > 0 ? Integer.parseInt(f1.getText().toString()) : 0;
				v2 = f2.getText().length() > 0 ? Integer.parseInt(f2.getText().toString()) : 0;
				v3 = f3.getText().length() > 0 ? Integer.parseInt(f3.getText().toString()) : 0;
				v4 = f4.getText().length() > 0 ? Integer.parseInt(f4.getText().toString()) : 0;
				v5 = f5.getText().length() > 0 ? Integer.parseInt(f5.getText().toString()) : 0;
				v6 = f6.getText().length() > 0 ? Integer.parseInt(f6.getText().toString()) : 0;
				v7 = f7.getText().length() > 0 ? Integer.parseInt(f7.getText().toString()) : 0;
				v8 = f8.getText().length() > 0 ? Integer.parseInt(f8.getText().toString()) : 0;
				v9 = f9.getText().length() > 0 ? Integer.parseInt(f9.getText().toString()) : 0;
				v10 = f10.getText().length() > 0 ? Integer.parseInt(f10.getText().toString()) : 0;
				v11 = f11.getText().length() > 0 ? Integer.parseInt(f11.getText().toString()) : 0;
				v12 = f12.getText().length() > 0 ? Integer.parseInt(f12.getText().toString()) : 0;
				v13 = f13.getText().length() > 0 ? Integer.parseInt(f13.getText().toString()) : 0;
				v14 = f14.getText().length() > 0 ? Integer.parseInt(f14.getText().toString()) : 0;




				int idMarca = spm.getId();
				int idProdu = spPm.getIdProducto();
				String nombreP = spPm.getNombre();
				if(idMarca != 0){
					if(idProdu != 0){
						if(cha1 >= 0 || cha2 >= 0 || cha3 >= 0 || cha4 >= 0 || cha5 >= 0 || cha6 >= 0){

							new BDopenHelper(this).insertarFrentes(idTienda, idPromotor,
									fecha, idMarca, idProdu,cha1,cha2,cha3,cha4,cha5,cha6,1,uni,v1,v2,
									v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14);
							int totalFrentes = cha1+cha2+cha3+cha4+cha5+cha6;
							Toast.makeText(this,"("+totalFrentes+") Frentes Guardados de: \n  "+nombreP, Toast.LENGTH_SHORT).show();
							//spiMarca.setSelection(0);
							spiPro.setSelection(0);

							try {
								resetCamps();
								new EnviarDatos(this).enviarFrentes();


								Echa1.setVisibility(View.INVISIBLE);
								Echa2.setVisibility(View.INVISIBLE);
								Echa3.setVisibility(View.INVISIBLE);
								Echa4.setVisibility(View.INVISIBLE);
								Echa5.setVisibility(View.INVISIBLE);
								Echa6.setVisibility(View.INVISIBLE);


								Echa1.setText("");
								Echa2.setText("");
								Echa3.setText("");
								Echa4.setText("");
								Echa5.setText("");
								Echa6.setText("");

								im.hideSoftInputFromWindow(Echa1.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa2.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa3.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa4.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa5.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa6.getWindowToken(), 0);

							}catch(Exception e) {
								Toast.makeText(this,"error al ocultar", Toast.LENGTH_SHORT).show();
							}
						}else {
							Toast.makeText(this,"No llenaste ninguna charola", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(this,"No seleccionaste Producto", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this,"No seleccionaste Marca", Toast.LENGTH_SHORT).show();
				}



			}catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(this,"error 4", Toast.LENGTH_SHORT).show();
			}


		}catch(Exception e) {
			Toast.makeText(this, "Excep guar"+Echa1.getVisibility(), Toast.LENGTH_SHORT).show();
		}



	}


	private void resetCamps() {

		unifila.setText("");
		f1.setText("");
		f2.setText("");
		f3.setText("");
		f4.setText("");
		f5.setText("");
		f6.setText("");
		f7.setText("");
		f8.setText("");
		f9.setText("");
		f10.setText("");
		f11.setText("");
		f12.setText("");
		f13.setText("");
		f14.setText("");

	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {

		MarcaModel spm = (MarcaModel)  spiMarca.getSelectedItem();


		int idMarca = spm.getId();
		loadSpinnerProd(idMarca);

	}



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {


	}

	private void loadSpinner(){
		try {


			MarcasAdapter adapter = new MarcasAdapter(this,
					android.R.layout.simple_spinner_item, getArrayList());
			spiMarca.setAdapter(adapter);

		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}

	}


	private void loadSpinnerProd(int idM){
		try {
			ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListProByTiensda(idM, idTienda));
			spiPro.setAdapter(proAdap);

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
		spPinicio.setCodigoBarras(" ");

		arrayP.add(0,spPinicio);

		curPro.close();
		base.close();
		return arrayP;

	}

	private ArrayList<SpinnerProductoModel> getArrayListProByTiensda(int idMarca, int idTienda){

		Cursor curProByTienda = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
		ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();
		if (curProByTienda.getCount() <= 0){

			Cursor curPro = new BDopenHelper(this).productos(idMarca);

			for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
				final SpinnerProductoModel spP = new SpinnerProductoModel();
				spP.setIdProducto(curPro.getInt(0));
				spP.setNombre(curPro.getString(1));
				spP.setPresentacion(curPro.getString(2));
				spP.setCodigoBarras(curPro.getString(3));
				spP.setIdMarca(curPro.getInt(4));
				arrayP.add(spP);
			}


			curPro.close();
		} else {

			for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
				final SpinnerProductoModel spP = new SpinnerProductoModel();
				spP.setIdProducto(curProByTienda.getInt(0));
				spP.setNombre(curProByTienda.getString(1));
				spP.setPresentacion(curProByTienda.getString(2));
				spP.setCodigoBarras(curProByTienda.getString(3));
				spP.setIdMarca(curProByTienda.getInt(4));
				arrayP.add(spP);
			}

		}



		final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");
		spPinicio.setCodigoBarras(" ");

		arrayP.add(0,spPinicio);


		base.close();
		return arrayP;

	}



	private ArrayList<MarcaModel> getArrayList(){

		base = new BDopenHelper(this).getReadableDatabase();
		String sql = "select idMarca, nombre, img from marca order by nombre asc;";
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





}
