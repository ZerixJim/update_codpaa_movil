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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;

public class SurtidoMueble extends AppCompatActivity implements OnClickListener, OnItemSelectedListener,OnCheckedChangeListener{
	
	Spinner spiMar, spiPro;
	EditText cantidad;
    EditText unifila, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14;
	TextInputLayout cantidadLayout;
	TextView txtCantidad;
    Toolbar toolbar;
	InputMethodManager im;
	SQLiteDatabase base;
	RadioGroup radio;
	RadioButton si,no,selec;
    CardView cardView;
	ArrayList<MarcaModel> array = new ArrayList<>();
	int idTienda, idPromotor;
	Locale locale;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surtidomueble);
		
		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");
		
		spiMar = (Spinner) findViewById(R.id.spiSurM);
		spiPro = (Spinner) findViewById(R.id.spiSurP);
		cantidad = (EditText) findViewById(R.id.editSur);

		txtCantidad = (TextView) findViewById(R.id.txt_surtido);

        cardView = (CardView) findViewById(R.id.card_view);

        //Linea de Cajas
        unifila = (EditText) findViewById(R.id.unifila);
        c1 = (EditText) findViewById(R.id.caja1);
        c2 = (EditText) findViewById(R.id.caja2);
        c3 = (EditText) findViewById(R.id.caja3);
        c4 = (EditText) findViewById(R.id.caja4);
        c5 = (EditText) findViewById(R.id.caja5);
        c6 = (EditText) findViewById(R.id.caja6);
        c7 = (EditText) findViewById(R.id.caja7);
        c8 = (EditText) findViewById(R.id.caja8);
        c9 = (EditText) findViewById(R.id.caja9);
        c10 = (EditText) findViewById(R.id.caja10);
        c11 = (EditText) findViewById(R.id.caja11);
        c12 = (EditText) findViewById(R.id.caja12);
        c13 = (EditText) findViewById(R.id.caja13);
        c14 = (EditText) findViewById(R.id.caja14);



        toolbar = (Toolbar) findViewById(R.id.toolbar);

		

		radio = (RadioGroup) findViewById(R.id.radioGroup1);
		si = (RadioButton) radio.findViewById(R.id.radio0);
		no = (RadioButton) radio.findViewById(R.id.radio1);

		cantidadLayout = (TextInputLayout) findViewById(R.id.txt_input_cantidad);
		



		spiMar.setOnItemSelectedListener(this);
		
		//cantidad.setVisibility(View.INVISIBLE);
		cantidadLayout.setVisibility(View.INVISIBLE);
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		radio.setOnCheckedChangeListener(this);
		no.setChecked(true);


        ActionBar actionBar;
        if (toolbar != null){
            setSupportActionBar(toolbar);
             actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setHomeButtonEnabled(true);

                Cursor cNomUser = new BDopenHelper(this).nombrePromotor(idPromotor);
                cNomUser.moveToFirst();

                actionBar.setSubtitle(cNomUser.getString(0));


                Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
                cTienda.moveToFirst();

                actionBar.setTitle(cTienda.getString(0) + " " + cTienda.getString(1) );
            }
        }



        try {


            loadSpinner();


        }catch(Exception e) {
            Toast.makeText(this, "Error Surtido 3", Toast.LENGTH_SHORT).show();
        }


		locale = new Locale("es_MX");


		
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case android.R.id.home:
                this.finish();
                return true;
            case R.id.save_venta:
                guardar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_venta_promedio, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
	public void onItemSelected(AdapterView<?> adapter, View v, int id,long arg3) {

		MarcaModel spM = (MarcaModel) spiMar.getSelectedItem();
		int idMarca = spM.getId();

		loadSpinnerProd(idMarca);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

	
	@Override
	public void onClick(View v) {

		
	}



	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		switch(group.getCheckedRadioButtonId()) {
		
            case R.id.radio0:
                //cantidad.setVisibility(View.VISIBLE);
                cantidadLayout.setVisibility(View.VISIBLE);
                cantidad.requestFocus();
                im.showSoftInput(cantidad, 0);
                cardView.setVisibility(View.VISIBLE);
				txtCantidad.setVisibility(View.VISIBLE);
                break;
            case R.id.radio1:
                //cantidad.setVisibility(View.INVISIBLE);
                cantidadLayout.setVisibility(View.INVISIBLE);
                im.hideSoftInputFromWindow(cantidad.getWindowToken(), 0);

                cardView.setVisibility(View.INVISIBLE);
				txtCantidad.setVisibility(View.INVISIBLE);
                break;
		}
		
	}
	
	private void guardar() {
		try {
			
			int cajas = 0;
            int uni=0, v1=0, v2=0, v3=0, v4=0, v5=0, v6=0, v7=0, v8=0, v9=0, v10=0, v11=0, v12=0,v13=0,v14=0;





			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
			String fecha = dFecha.format(c.getTime());
			
			
			selec = (RadioButton) findViewById(radio.getCheckedRadioButtonId());
			MarcaModel spM = (MarcaModel) spiMar.getSelectedItem();
			SpinnerProductoModel spP = (SpinnerProductoModel) spiPro.getSelectedItem();
			
			//int idProdu = (int) spiPro.getItemIdAtPosition(spiPro.getSelectedItemPosition());
			int idProdu =  spP.getIdProducto();
			int idMarca = spM.getId();
			
			if(idMarca != 0){
				if(idProdu != 0){
					if(cantidad.isShown() && cantidad.getText().length() >0) {
						cajas = Integer.parseInt(cantidad.getText().toString());
						
					}

                    if (unifila.isShown()){
                        uni = unifila.getText().length() > 0 ? Integer.parseInt(unifila.getText().toString()) : 0;
                        v1 = c1.getText().length() > 0 ? Integer.parseInt(c1.getText().toString()) : 0;
                        v2 = c2.getText().length() > 0 ? Integer.parseInt(c2.getText().toString()) : 0;
                        v3 = c3.getText().length() > 0 ? Integer.parseInt(c3.getText().toString()) : 0;
                        v4 = c4.getText().length() > 0 ? Integer.parseInt(c4.getText().toString()) : 0;
                        v5 = c5.getText().length() > 0 ? Integer.parseInt(c5.getText().toString()) : 0;
                        v6 = c6.getText().length() > 0 ? Integer.parseInt(c6.getText().toString()) : 0;
                        v7 = c7.getText().length() > 0 ? Integer.parseInt(c7.getText().toString()) : 0;
                        v8 = c8.getText().length() > 0 ? Integer.parseInt(c8.getText().toString()) : 0;
                        v9 = c9.getText().length() > 0 ? Integer.parseInt(c9.getText().toString()) : 0;
                        v10 = c10.getText().length() > 0 ? Integer.parseInt(c10.getText().toString()) : 0;
                        v11 = c11.getText().length() > 0 ? Integer.parseInt(c11.getText().toString()) : 0;
                        v12 = c12.getText().length() > 0 ? Integer.parseInt(c12.getText().toString()) : 0;
                        v13 = c13.getText().length() > 0 ? Integer.parseInt(c13.getText().toString()) : 0;
                        v14 = c14.getText().length() > 0 ? Integer.parseInt(c14.getText().toString()) : 0;
                    }
					
					try {
						Toast.makeText(this, "Surtido guardado de:\n "+spP.getNombre(), Toast.LENGTH_SHORT).show();
						new BDopenHelper(this).insertarSurtido(idTienda, idPromotor, selec.getText().toString(),
                                fecha, idProdu, cajas,uni,v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14);
						im.hideSoftInputFromWindow(cantidad.getWindowToken(), 0);

                        resetCamps();
						new EnviarDatos(this).enviarSurtido();
					}catch(Exception e) {
						Toast.makeText(this, "No se guardo, error", Toast.LENGTH_SHORT).show();
						
					}
				}else{
					Toast.makeText(this, "No Selecionaste Producto", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "No Selecionaste Marca", Toast.LENGTH_SHORT).show();
			}
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

    private void resetCamps() {

        cantidad.setText("");
        unifila.setText("");
        c1.setText("");
        c2.setText("");
        c3.setText("");
        c4.setText("");
        c5.setText("");
        c6.setText("");
        c7.setText("");
        c8.setText("");
        c9.setText("");
        c10.setText("");
        c11.setText("");
        c12.setText("");
        c13.setText("");
        c14.setText("");

        spiPro.setSelection(0);



    }

    private void loadSpinner(){
		try {
			
			
			MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spiMar.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
		
	}

	
	private void loadSpinnerProd(int idM){
		try {
			ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListPro(idM));
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
