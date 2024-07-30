package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.widgets.Helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.CategoriasProductoAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.model.SpinnerCateProdModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;
import com.codpaa.widget.SingleSpinnerSelect;

public class Frentes extends AppCompatActivity implements OnClickListener, OnItemSelectedListener{

	private int idPromotor, idTienda;
	/*Button  btn1,btn2,btn3,btn4,btn5,btn6;
	private EditText unifila, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14;
	private EditText Echa1,Echa2,Echa3,Echa4,Echa5,Echa6;*/

	private EditText editCantidad;
	//private InputMethodManager im;
	BDopenHelper baseH;
	Toolbar toolbar;

	private ArrayList<MarcaModel> array = new ArrayList<>();
	private ArrayList<SpinnerCateProdModel> array2 = new ArrayList<>();
	private SQLiteDatabase base;
	private Spinner spiMarcA;
	private Spinner spiCloroxCat;
	//private Spinner spiPro;
	private SingleSpinnerSelect spinnerProducto;

	int cloroxFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frentes);

		toolbar = findViewById(R.id.toolbar_frentes);

		if(toolbar != null){
			setSupportActionBar(toolbar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null){
				actionBar.setDisplayHomeAsUpEnabled(true);

			}
		}



		Intent i = getIntent();
		idTienda = i.getIntExtra("idTienda", 0);
		idPromotor = i.getIntExtra("idPromotor", 0);

		//im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


		spiMarcA =  findViewById(R.id.spinnerMarFre);
		//spiPro =  findViewById(R.id.spinnerFrePro);
		spinnerProducto = findViewById(R.id.spinner_front_product);
		spiCloroxCat = findViewById(R.id.spinnerCatClorox);
		//asiganacion de botones
		/*btn1 = (Button) findViewById(R.id.btnf1);
		btn2 = (Button) findViewById(R.id.btn_charola_2);
		btn3 = (Button) findViewById(R.id.btnf3);
		btn4 = (Button) findViewById(R.id.btnfoto);
		btn5 = (Button) findViewById(R.id.btnf5);
		btn6 = (Button) findViewById(R.id.btnf6);*/

		//asignacion de campos de texto
		/*Echa1 = (EditText) findViewById(R.id.editCha1);
		Echa2 = (EditText) findViewById(R.id.editCha2);
		Echa3 = (EditText) findViewById(R.id.editCha3);
		Echa4 = (EditText) findViewById(R.id.editCha4);
		Echa5 = (EditText) findViewById(R.id.editCha5);
		Echa6 = (EditText) findViewById(R.id.editCha6);*/

		editCantidad = findViewById(R.id.cantidad);


		//asignacion de escuchadores
		/*Echa1.setVisibility(View.INVISIBLE);
		Echa2.setVisibility(View.INVISIBLE);
		Echa3.setVisibility(View.INVISIBLE);
		Echa4.setVisibility(View.INVISIBLE);
		Echa5.setVisibility(View.INVISIBLE);
		Echa6.setVisibility(View.INVISIBLE);*/


		/*unifila = (EditText) findViewById(R.id.unifila);
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
		f14 = (EditText) findViewById(R.id.caja14);*/



		spiMarcA.setOnItemSelectedListener(this);
		spiCloroxCat.setOnItemSelectedListener(this);
		/*btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);*/



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
					loadSpinnerCloroxCat();
					//SE LEEN LOS ID MARCA PARA VER SI EXISTE CLOROX
					/*Cursor c = getMarcasList();
					for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
						Log.v("MARCATEST", c.getString(0));
						if(c.getInt(0) == 249){
							cloroxFlag = 1;
						}
					}*/


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


		/*
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
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			this.finish();
			return true;
		} else if (itemId == R.id.save_frentes) {
			guardarDatos();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_frentes, menu);


		return true;
	}

	@Override
	public void onClick(View v) {

		/*case R.id.btnf1: reQuesFocus(Echa1);break;
			case R.id.btn_charola_2: reQuesFocus(Echa2);break;
			case R.id.btnf3: reQuesFocus(Echa3);break;
			case R.id.btnfoto: reQuesFocus(Echa4);break;
			case R.id.btnf5: reQuesFocus(Echa5);break;
			case R.id.btnf6: reQuesFocus(Echa6);break;*/
		if (v.getId() == R.id.buttonExhib) {
			finish();
		}


	}



	/*private void reQuesFocus(View v){
		v.setVisibility(View.VISIBLE);
		v.requestFocus();

		im.showSoftInput(v, 0);


	}*/



	public void guardarDatos() {
		//int cha1 = 0,cha2 = 0, cha3 = 0, cha4 = 0, cha5 = 0, cha6 = 0;
		int cantidad;
		int categoria = 0;
		//int uni, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12,v13,v14;


		try {

			/*if(Echa1.isShown() && Echa1.getText().length() >0) {
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

			}*/

			try {

				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
				MarcaModel spm = (MarcaModel) spiMarcA.getSelectedItem();
				//SpinnerProductoModel spPm = (SpinnerProductoModel) spiPro.getSelectedItem();
				ProductosModel spPm = spinnerProducto.getSelected();


				String fecha = dFecha.format(c.getTime());


				//Log.d("FECHA",fecha);


				/*uni = unifila.getText().length() > 0 ? Integer.parseInt(unifila.getText().toString()) : 0;
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
				v14 = f14.getText().length() > 0 ? Integer.parseInt(f14.getText().toString()) : 0;*/


				cantidad = editCantidad.getText().length() > 0 ? Integer.parseInt(editCantidad.getText().toString()) : 0;



				int idMarca = spm.getId();
				int idProdu = spPm.getIdProducto();
				String nombreP = spPm.getNombre();


				if(idMarca != 0){
					if(idProdu != 0){
						if(cantidad >= 0){

							int contador = 0;

							contador = new BDopenHelper(this).cuentaFrentesV1(idPromotor, idTienda, idMarca, idProdu, fecha);

							Log.v("CUENTAFRENTES", String.valueOf(contador));

							if(contador == 0) {

								try {

									new BDopenHelper(this).insertFrentesCantidad(idTienda, idPromotor,
											fecha, idMarca, idProdu, cantidad, categoria);
								} catch (Exception e) {
									Log.v("FRONTERROR", e.getMessage());
								}

								Toast.makeText(this, "(" + cantidad + ") Frentes Guardados de: \n  " + nombreP, Toast.LENGTH_SHORT).show();
								//spiMarca.setSelection(0);
								//spiPro.setSelection(0);

								spinnerProducto.resetFilter();
								spinnerProducto.setSelection(0);

								new EnviarDatos(this).enviarFrentes();

								editCantidad.setText("");

								Log.e("FRENTESCAPT", "Frente capturado");
							}else{
								Log.e("FRENTESCAPT", "Ya existe un registro de frentes igual");
							}

							/*try {
								//resetCamps();


*//*
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
								im.hideSoftInputFromWindow(Echa6.getWindowToken(), 0);*//*

							}catch(Exception e) {
								Toast.makeText(this,"error al ocultar", Toast.LENGTH_SHORT).show();
							}*/
						}else {
							//Toast.makeText(this,"No llenaste ninguna charola", Toast.LENGTH_SHORT).show();
							Toast.makeText(this,"No llenaste la cantidad", Toast.LENGTH_SHORT).show();
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
			e.printStackTrace();
		}



	}


	/*private void resetCamps() {

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

	}*/



	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		int spinnerId = parent.getId();

		if (spinnerId == R.id.spinnerMarFre) {
			MarcaModel marca = (MarcaModel) parent.getSelectedItem();
			int idMarca = marca.getId();
			// Lista de IDs de marcas que activan el spinner de categorías
			Set<Integer> idsPermitidos = new HashSet<>(Arrays.asList(249, 356, 357, 358, 359, 360));

			if (idsPermitidos.contains(idMarca)) {
				spiCloroxCat.setVisibility(View.VISIBLE);
				// Reiniciar el spinner de categorías cuando se muestra
				if (spiCloroxCat.getAdapter() != null) {
					spiCloroxCat.setSelection(0);
				}
			} else {
				spiCloroxCat.setVisibility(View.GONE);
				loadSpinnerProd(idMarca);
			}
		} else if (spiCloroxCat.getVisibility() == View.VISIBLE) {
			SpinnerCateProdModel categoria = (SpinnerCateProdModel) parent.getSelectedItem();
			int idCategoria = categoria.getId();
			if (idCategoria > 0) { // Asegúrate de que se selecciona una categoría válida antes de cargar productos
				int idMarca = ((MarcaModel) spiMarcA.getSelectedItem()).getId();
				loadSpinnerCatProd(idCategoria, idMarca);
			}
		} else if (spinnerId == R.id.spinner_front_product) {
			ProductosModel producto = (ProductosModel) parent.getSelectedItem();
			// Manejar la selección de productos aquí si es necesario
		}
	}



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {


	}

	private void loadSpinner(){
		try {


			MarcasAdapter adapter = new MarcasAdapter(this,
					android.R.layout.simple_spinner_item, getArrayList());
			spiMarcA.setAdapter(adapter);

		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}

	}


	private void loadSpinnerProd(int idM){
		try {

			// Old spinner
			//ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListProByTiensda(idM, idTienda));
			//spiPro.setAdapter(proAdap);


			spinnerProducto.setItems(getArrayListProByTien2(idM, idTienda), "Selecciona producto");

		} catch (Exception e) {
			Toast.makeText(this, "Error E25", Toast.LENGTH_SHORT).show();
		}
	}
	private void loadSpinnerCloroxCat() {
		try {


			CategoriasProductoAdapter adapter2 = new CategoriasProductoAdapter(this,
					android.R.layout.simple_spinner_item, getArrayList2());
			spiCloroxCat.setAdapter(adapter2);


		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
	}

	private void loadSpinnerCatProd(int idCategoria, int idMarca){
		try {

			// Old spinner
			//ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListProByTiensda(idM, idTienda));
			//spiPro.setAdapter(proAdap);


			spinnerProducto.setItems(getArrayListProdCat(idCategoria, idMarca), "Selecciona producto");


		} catch (Exception e) {
			Toast.makeText(this, "Error E26", Toast.LENGTH_SHORT).show();
		}
	}

	private ArrayList<ProductosModel> getArrayListProdCat(int idCategoria, int idMarca){

		Cursor curProByTienda = new BDopenHelper(this).getProductosByCat(idCategoria, idMarca);
		ArrayList<ProductosModel> arrayP = new ArrayList<>();
		if (curProByTienda.getCount() <= 0){

			Cursor curPro = new BDopenHelper(this).productosCloroxCat(idCategoria, idMarca);

			for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
				final ProductosModel spP = new ProductosModel();
				spP.setIdProducto(curPro.getInt(0));
				spP.setNombre(curPro.getString(1));
				spP.setPresentacion(curPro.getString(2));
				spP.setCodigoBarras(curPro.getString(3));
				spP.setIdMarca(curPro.getInt(4));
				spP.setHasImage(curPro.getInt(curPro.getColumnIndex("has_image")));

				arrayP.add(spP);
			}


			curPro.close();
		} else {

			for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
				final ProductosModel spP = new ProductosModel();
				spP.setIdProducto(curProByTienda.getInt(0));
				spP.setNombre(curProByTienda.getString(1));
				spP.setPresentacion(curProByTienda.getString(2));
				spP.setCodigoBarras(curProByTienda.getString(3));
				spP.setIdMarca(curProByTienda.getInt(4));
				spP.setHasImage(curProByTienda.getInt(curProByTienda.getColumnIndex("has_image")));
				arrayP.add(spP);
			}

		}



		final ProductosModel spPinicio = new ProductosModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");
		spPinicio.setCodigoBarras(" ");

		arrayP.add(0,spPinicio);


		base.close();
		return arrayP;

	}


	private ArrayList<ProductosModel> getArrayListProByTien2(int idMarca, int idTienda){

		Cursor curProByTienda = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
		ArrayList<ProductosModel> arrayP = new ArrayList<>();
		if (curProByTienda.getCount() <= 0){

			Cursor curPro = new BDopenHelper(this).productos(idMarca);

			for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
				final ProductosModel spP = new ProductosModel();
				spP.setIdProducto(curPro.getInt(0));
				spP.setNombre(curPro.getString(1));
				spP.setPresentacion(curPro.getString(2));
				spP.setCodigoBarras(curPro.getString(3));
				spP.setIdMarca(curPro.getInt(4));
				spP.setHasImage(curPro.getInt(curPro.getColumnIndex("has_image")));

				arrayP.add(spP);
			}


			curPro.close();
		} else {

			for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
				final ProductosModel spP = new ProductosModel();
				spP.setIdProducto(curProByTienda.getInt(0));
				spP.setNombre(curProByTienda.getString(1));
				spP.setPresentacion(curProByTienda.getString(2));
				spP.setCodigoBarras(curProByTienda.getString(3));
				spP.setIdMarca(curProByTienda.getInt(4));
				spP.setHasImage(curProByTienda.getInt(curProByTienda.getColumnIndex("has_image")));
				arrayP.add(spP);
			}

		}



		final ProductosModel spPinicio = new ProductosModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");
		spPinicio.setCodigoBarras(" ");

		arrayP.add(0,spPinicio);


		base.close();
		return arrayP;

	}



	private ArrayList<ProductosModel> getArrayListProByTiensda(int idMarca, int idTienda){

		Cursor curProByTienda = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
		ArrayList<ProductosModel> arrayP = new ArrayList<>();
		if (curProByTienda.getCount() <= 0){

			Cursor curPro = new BDopenHelper(this).productos(idMarca);

			for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
				final ProductosModel spP = new ProductosModel();
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
				final ProductosModel spP = new ProductosModel();
				spP.setIdProducto(curProByTienda.getInt(0));
				spP.setNombre(curProByTienda.getString(1));
				spP.setPresentacion(curProByTienda.getString(2));
				spP.setCodigoBarras(curProByTienda.getString(3));
				spP.setIdMarca(curProByTienda.getInt(4));
				arrayP.add(spP);
			}

		}



		final ProductosModel spPinicio = new ProductosModel();
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
		//String sql = "select idMarca, nombre, img from marca order by nombre asc;";
		String sql = "select tm.idMarca as _id, m.nombre, m.img from tienda_marca tm left join marca m on tm.idMarca = m.idMarca where tm.idTienda = " + idTienda + ";";
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

	private ArrayList<SpinnerCateProdModel> getArrayList2() {
		base = new BDopenHelper(this).getReadableDatabase();
		String sql2 = "select * from categoriasproducto";
		Cursor cursorCategoria = base.rawQuery(sql2, null);


		try {
			for(cursorCategoria.moveToFirst(); !cursorCategoria.isAfterLast(); cursorCategoria.moveToNext()) {
				final SpinnerCateProdModel spiC = new SpinnerCateProdModel();
				spiC.setCategoria(cursorCategoria.getString(1));
				spiC.setId(cursorCategoria.getInt(0));
				array2.add(spiC);
				Log.d("Elemento Array2", "ID: " + spiC.getId() + ", Categoria: " + spiC.getCategoria());
			}

			final SpinnerCateProdModel spiCatFirst = new SpinnerCateProdModel();
			spiCatFirst.setCategoria("Selecciona categoria");
			spiCatFirst.setId(0);

			array2.add(0,spiCatFirst);
		} catch (Exception e) {
			Log.e("Error", "Error al obtener datos de la base de datos: " + e.getMessage());
		} finally {
			cursorCategoria.close();
			base.close();
		}

		Log.d("Array2", "Contenido del array2: " + array2.toString()); // Imprime el contenido completo del array2

		return array2;
	}
	private Cursor getMarcasList(){
		base = new BDopenHelper(this).getReadableDatabase();
		//String sql = "select idMarca, nombre, img from marca order by nombre asc;";
		String sql = "select tm.idMarca as _id, m.nombre, m.img from tienda_marca tm left join marca m on tm.idMarca = m.idMarca where tm.idTienda = " + idTienda + ";";
		Cursor cursorMarca = base.rawQuery(sql, null);

		return cursorMarca;
	}





}
