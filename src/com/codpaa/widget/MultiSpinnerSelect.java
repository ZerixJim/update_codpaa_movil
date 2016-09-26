package com.codpaa.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Spinner;


import com.codpaa.adapter.ProductosAdapter;
import com.codpaa.model.ProductosModel;

import java.util.ArrayList;
import java.util.List;


public class MultiSpinnerSelect extends Spinner implements
        OnMultiChoiceClickListener, OnCancelListener {

    private ArrayList<ProductosModel> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    ProductosAdapter arrayAdapter;
    ArrayList<ProductosModel> selectedProduct = new ArrayList<>();


    public MultiSpinnerSelect(Context context) {
        super(context);
    }

    public MultiSpinnerSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSpinnerSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        Log.d("Multi", "OnClick");

        selected[which] = isChecked;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //Log.d("Multi", "onCancel");
        StringBuilder spinnerBuffer = new StringBuilder();
        ArrayList<ProductosModel> productos = arrayAdapter.getProductos();


        String spinnerText;
        boolean someUnSelected = false;
        for (ProductosModel producto : productos){
            if (producto.isChecked()){
                spinnerBuffer.append(producto.getNombre());
                spinnerBuffer.append(", ");
                //Log.d("producto selected", producto.getNombre());
                someUnSelected = true;
            }

        }



        if (someUnSelected){
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
            else
                spinnerText = defaultText;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, new String[]{ spinnerText });
            setAdapter(adapter);
            listener.onItemsSelected(selected);

        } else {
            setAdapter(arrayAdapter);
        }




    }

    @Override
    public boolean performClick() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setAdapter(arrayAdapter, null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(this);

        AlertDialog alertDialog = builder.create();

        ListView listView = alertDialog.getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductosModel producto = (ProductosModel) parent.getItemAtPosition(position);

                //Log.d("Click", "" + view.getId());

                if (producto.isChecked())
                    producto.setChecked(false);
                else
                    producto.setChecked(true);


                arrayAdapter.notifyDataSetChanged();

            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductosModel producto = (ProductosModel) parent.getItemAtPosition(position);

                Log.d("Click", "" + view.getId());

                if (producto.isChecked())
                    producto.setChecked(false);
                else
                    producto.setChecked(true);


                arrayAdapter.notifyDataSetChanged();

            }
        });*/

        //alertDialog





        alertDialog.show();
        return true;
    }

    public void setItems(ArrayList<ProductosModel> items, String allText, MultiSpinnerListener listener){

        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        //all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{allText});
        setAdapter(adapter);*/
        arrayAdapter = new ProductosAdapter(getContext(),android.R.layout.simple_spinner_item,
                items);
        setAdapter(arrayAdapter);


    }
    


    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }

    public ArrayList<ProductosModel> getSelectedItems(){
        ArrayList<ProductosModel> productos = arrayAdapter.getProductos();
        ArrayList<ProductosModel> produtoSelected = new ArrayList<>();
        for (ProductosModel producto : productos){
            if (producto.isChecked()){
                produtoSelected.add(producto);
            }
        }
        return produtoSelected;
    }
    private ArrayList<ProductosModel> getProductos(){
        ArrayList<ProductosModel> arrayList = new ArrayList<>();
        for (int i=0; i < 20; i++){
            final ProductosModel producto = new ProductosModel();
            producto.setIdProducto(i + 1);
            producto.setNombre("Producto " + i);
            producto.setPresentacion("gr2" + i);

            arrayList.add(producto);
        }


        return arrayList;
    }


    public ArrayList<ProductosModel> getSelectedProduct(){
        return selectedProduct;
    }
}