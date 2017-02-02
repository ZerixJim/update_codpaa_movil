package com.codpaa.fragment;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.MaterialSpinnerAdapter;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.MaterialModel;
import com.codpaa.model.SpinnerProductoModel;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by grim on 30/01/17.
 */

public class DialogMaterialRequest extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinnerMaterial, spinnerMarca, spinnerProducto;
    private EditText cantidad;
    Button btnAdd, btnClose;
    private int idTienda, idPromotor;
    private SelectMaterial materialListener;

    public static DialogMaterialRequest newInstance(){

        return new DialogMaterialRequest();
    }


    public void setOnMaterialListener(SelectMaterial listener){
        materialListener = listener;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        idTienda = getArguments().getInt("idTienda");
        idPromotor = getArguments().getInt("idPromotor");
        View v = inflater.inflate(R.layout.dialog_material_request, container, false);
        spinnerMaterial = (Spinner) v.findViewById(R.id.spinner_material);
        spinnerMarca = (Spinner) v.findViewById(R.id.spinner_marca);
        spinnerProducto = (Spinner) v.findViewById(R.id.spinner_producto);
        btnAdd = (Button) v.findViewById(R.id.btn_add);
        btnClose = (Button) v.findViewById(R.id.btn_close);
        cantidad = (EditText) v.findViewById(R.id.cantidad);


        spinnerMaterial.setOnItemSelectedListener(this);

        btnAdd.setOnClickListener(this);
        btnClose.setOnClickListener(this);


        setUpMaterial();


        return v;
    }



    private void setUpMaterial() {

        MaterialSpinnerAdapter adapter = new MaterialSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, getMaterials());

        if (spinnerMaterial != null){
            spinnerMaterial.setAdapter(adapter);
            spinnerMaterial.setOnItemSelectedListener(this);
        }

    }



    private void loadProductoTester() {

        ProductosCustomAdapter adapter = new ProductosCustomAdapter(getActivity(), android.R.layout.simple_spinner_item, getProductTester());
        spinnerProducto.setAdapter(adapter);
    }

    private void loadSpinnerMarca(){
        try {


            MarcasAdapter adapter = new MarcasAdapter(getActivity(), android.R.layout.simple_spinner_item, getArrayList());
            spinnerMarca.setAdapter(adapter);

        }catch(Exception e) {
            Toast.makeText(getActivity(), "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<MarcaModel> getArrayList(){

        SQLiteDatabase base = new BDopenHelper(getActivity()).getReadableDatabase();
        ArrayList<MarcaModel> array = new ArrayList<>();
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

        array.add(0,spiMfirst);
        cursorMarca.close();
        base.close();

        return array;

    }

    private ArrayList<SpinnerProductoModel> getProductTester(){

        Cursor curProByTienda = new BDopenHelper(getActivity()).getProductosTester();
        ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();

        for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
            final SpinnerProductoModel spP = new SpinnerProductoModel();
            spP.setIdProducto(curProByTienda.getInt(0));
            spP.setNombre(curProByTienda.getString(1));
            spP.setPresentacion(curProByTienda.getString(2));
            spP.setCodigoBarras(curProByTienda.getString(3));
            spP.setIdMarca(curProByTienda.getInt(4));
            arrayP.add(spP);
        }

        final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
        spPinicio.setIdProducto(0);
        spPinicio.setNombre("Seleccione Producto");
        spPinicio.setPresentacion("");
        spPinicio.setCodigoBarras("");

        arrayP.add(0,spPinicio);

        curProByTienda.close();
        return arrayP;


    }


    private void addMaterial(){

        MaterialModel mMterial = (MaterialModel) spinnerMaterial.getSelectedItem();

        if (mMterial.getIdMaterial() >= 1){

            //si el tipo de material es de producto
            if (mMterial.getIdTipoMaterial() == 2){
                // si el tipo de material es un probador


                MaterialModel material = new MaterialModel();
                material.setIdMaterial(mMterial.getIdMaterial());

                SpinnerProductoModel sProdMod = (SpinnerProductoModel) spinnerProducto.getSelectedItem();

                material.setIdProducto(sProdMod.getIdProducto());
                material.setNombreMaterial(mMterial.getNombreMaterial());
                material.setNombreProducto(sProdMod.getNombre());
                material.setIdTipoMaterial(mMterial.getIdTipoMaterial());
                material.setNombreMaterial(mMterial.getNombreMaterial());
                material.setUnidad(mMterial.getUnidad());

                if (cantidad.getText().length() > 0){

                    material.setCantidad(Integer.parseInt(cantidad.getText().toString()));


                } else {
                    Toast.makeText(getActivity(), "Debes escribir una cantidad", Toast.LENGTH_SHORT).show();

                }

                materialListener.onAddMaterial(material);
                clearData();


            } else {
                if (cantidad.getText().length() > 0){

                    MaterialModel material = new MaterialModel();
                    material.setIdTipoMaterial(mMterial.getIdTipoMaterial());
                    material.setIdMaterial(mMterial.getIdMaterial());
                    material.setCantidad(Integer.parseInt(cantidad.getText().toString()));
                    material.setNombreMaterial(mMterial.getNombreMaterial());
                    material.setUnidad(mMterial.getUnidad());

                    materialListener.onAddMaterial(material);
                    clearData();

                }else {
                    Toast.makeText(getActivity(), "Cantidad Requerida", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(getActivity(), "Selecciona un material", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearData(){

        spinnerMaterial.setSelection(0);
        cantidad.setText("");

    }

    private void productoSpinner(int idMarca){

        ProductosCustomAdapter adapter = new ProductosCustomAdapter(getActivity(), android.R.layout.simple_spinner_item, getArrayListProByTienda(idMarca,idTienda));
        spinnerProducto.setAdapter(adapter);

    }

    private ArrayList<SpinnerProductoModel> getArrayListProByTienda(int idMarca, int idTienda){

        Cursor curProByTienda = new BDopenHelper(getActivity()).getProductosByTienda(idMarca, idTienda);
        ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();
        if (curProByTienda.getCount() <= 0){

            Cursor curPro = new BDopenHelper(getActivity()).productos(idMarca);

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

        arrayP.add(0,spPinicio);

        curProByTienda.close();
        return arrayP;

    }

    private List<MaterialModel> getMaterials() {
        List<MaterialModel> array = new ArrayList<>();

        SQLiteDatabase db = new BDopenHelper(getActivity()).getReadableDatabase();
        String sql = "select idMaterial, material,  unidad, solicitudMaxima, tipo_material from materiales order by material asc";


        Cursor cursor = db.rawQuery(sql, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            final MaterialModel material = new MaterialModel();

            material.setNombreMaterial(cursor.getString(cursor.getColumnIndex("material")));

            material.setIdMaterial(cursor.getInt(cursor.getColumnIndex("idMaterial")));
            material.setIdTipoMaterial(cursor.getInt(cursor.getColumnIndex("tipo_material")));
            material.setUnidad(cursor.getString(cursor.getColumnIndex("unidad")));
            material.setSolicitudMaxima(cursor.getInt(cursor.getColumnIndex("solicitudMaxima")));

            array.add(material);
        }

        final MaterialModel materialDefault = new MaterialModel();
        materialDefault.setNombreMaterial("Selecciona Material");
        materialDefault.setIdMaterial(0);

        array.add(0, materialDefault);


        cursor.close();
        return array;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;

        switch (spinner.getId()){
            case R.id.spinner_material:

                    MaterialModel material = (MaterialModel) spinner.getSelectedItem();

                    if (material.getIdMaterial() > 0){

                        cantidad.setHint("Maximo "+material.getSolicitudMaxima() + material.getUnidad() + "s");

                    }
                    if (material.getIdTipoMaterial() == 2){
                        if (material.getIdMaterial() == 18){
                            if (spinnerProducto.getVisibility() == View.INVISIBLE || spinnerProducto.getVisibility() == View.GONE ){
                                spinnerProducto.setVisibility(View.VISIBLE);
                                loadProductoTester();
                                if (spinnerMarca.getVisibility() == View.VISIBLE)
                                    spinnerMarca.setVisibility(View.GONE);
                            }

                        }else if(material.getIdMaterial() >= 1) {
                            spinnerMarca.setVisibility(View.VISIBLE);
                            loadSpinnerMarca();
                            spinnerMarca.setOnItemSelectedListener(this);
                        }
                    }else {
                        spinnerMarca.setVisibility(View.GONE);
                        spinnerProducto.setVisibility(View.GONE);
                    }



                break;

            case R.id.spinner_marca:

                    MarcaModel marcaModel = (MarcaModel) spinner.getSelectedItem();
                    if (marcaModel.getId() >=1){
                        if (spinnerProducto.getVisibility() == View.INVISIBLE || spinnerProducto.getVisibility() == View.GONE ){
                            spinnerProducto.setVisibility(View.VISIBLE);

                            productoSpinner(marcaModel.getId());

                        }
                    }else {
                        spinnerProducto.setVisibility(View.GONE);
                    }

                break;


        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_add:
                addMaterial();

                break;

            case R.id.btn_close:
                this.dismiss();
                break;
        }
    }


    public interface SelectMaterial{
        void onAddMaterial(MaterialModel model);
        void onTestId(int idMaterial);
    }
}
