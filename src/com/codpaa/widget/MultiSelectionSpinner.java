package com.codpaa.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.codpaa.adapters.ProductosAdapter;
import com.codpaa.models.ProductosModel;


public class MultiSelectionSpinner extends Spinner implements
        OnMultiChoiceClickListener{

    String[] _items = null;
    boolean[] mSelection = null;

    ArrayAdapter<String> simple_adapter;
    ArrayList<ProductosModel> productos = null;
    ProductosAdapter adapterModel;

    public MultiSelectionSpinner(Context context) {
        super(context);

        Toast.makeText(context,"constructor 0",Toast.LENGTH_SHORT).show();

        adapterModel = new ProductosAdapter(context,
                android.R.layout.simple_list_item_multiple_choice,productosArray());

        super.setAdapter(adapterModel);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        Toast.makeText(context,"constructor 1 \nCantidad "+productosArray().size(),
                Toast.LENGTH_SHORT).show();
        adapterModel = new ProductosAdapter(context,
                android.R.layout.simple_list_item_multiple_choice,productosArray());

        super.setAdapter(adapterModel);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        adapterModel = new ProductosAdapter(context,
                android.R.layout.simple_list_item_multiple_choice,productosArray());

        super.setAdapter(adapterModel);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        Toast.makeText(getContext(),"Evento Clicck",Toast.LENGTH_SHORT).show();

        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;

            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
            super.setAdapter(simple_adapter);
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }



    @Override
    public boolean performClick() {
        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setAdapter(adapterModel, null)
                .setNegativeButton("Cerra", null)
                .setOnKeyListener(new AlertDialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode,
                                         KeyEvent keyEvent) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            Toast.makeText(getContext(),"Dialog Back",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                })
                .create();

        if (productos != null){
            if (productos.size() > 0){

                builder.getListView().setItemsCanFocus(false);
                builder.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                builder.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ProductosModel pm = (ProductosModel) adapterView.getItemAtPosition(i);
                        if (pm.isSeleted()){
                            pm.setSeleted(false);
                        }else{
                            pm.setSeleted(true);
                        }

                        adapterModel.notifyDataSetChanged();
                        Log.d("Prueba","AdapterView: "+pm.getNombre()+" id: "+pm.getIdProducto());
                        //Toast.makeText(getContext(),pm.getNombre(),Toast.LENGTH_SHORT).show();
                    }
                });


                builder.show();
            }else{
                Toast.makeText(getContext(),"No se encontro producto",Toast.LENGTH_SHORT).show();
            }

        }else {

            Toast.makeText(getContext(),"Marca no seleccionada",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }

    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }


    public void setItemsModel(ArrayList<ProductosModel> items)
    {

        if (items != null && items.size() > 0){

            this.productos = items;
            adapterModel.clear();
            adapterModel.addAll(items);
            adapterModel.notifyDataSetChanged();
        }else {
            productos = null;
            Log.d("Items", "ArrayList is empty");
        }

    }

    public void setSelection(String[] selection) {
        for (String cell : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(cell)) {
                    mSelection[j] = true;
                }
            }
        }
    }

    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndicies) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (int index : selectedIndicies) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    public List<Integer> getSelectedIndicies() {
        List<Integer> selection = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;
        //ArrayList<ProductosModel> elements =

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }



    public ArrayList<ProductosModel> productosArray(){
        ArrayList<ProductosModel> array = new ArrayList<>();

        for (int i = 0;i < 10; i++){
            final ProductosModel sp = new ProductosModel();
            sp.setPresentacion(i+"gr");
            sp.setIdProducto(i+1);
            sp.setNombre("Producto"+(i+1));

            array.add(sp);
        }
        return array;
    }
}
