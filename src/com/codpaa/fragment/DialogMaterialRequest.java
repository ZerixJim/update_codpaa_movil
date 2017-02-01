package com.codpaa.fragment;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.codpaa.R;
import com.codpaa.adapter.MaterialSpinnerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MaterialModel;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by grim on 30/01/17.
 */

public class DialogMaterialRequest extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinnerMaterial, spinnerMarca, spinnerProducto;
    Button btnAdd, btnClose;
    private SelectMaterial materialListener;

    public static DialogMaterialRequest newInstance(){

        return new DialogMaterialRequest();
    }


    public void setOnMaterialListener(SelectMaterial listener){
        materialListener = listener;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_material_request, container, false);
        spinnerMaterial = (Spinner) v.findViewById(R.id.spinner_material);
        spinnerMarca = (Spinner) v.findViewById(R.id.spinner_marca);
        spinnerProducto = (Spinner) v.findViewById(R.id.spinner_producto);
        btnAdd = (Button) v.findViewById(R.id.btn_add);
        btnClose = (Button) v.findViewById(R.id.btn_close);

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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_add:
                MaterialModel materialModel = (MaterialModel) spinnerMaterial.getSelectedItem();
                materialListener.onTestId(materialModel.getIdMaterial());

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
