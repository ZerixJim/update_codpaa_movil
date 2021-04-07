package com.codpaa.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;

import com.codpaa.R;
import com.codpaa.adapter.ProductosAdapter;
import com.codpaa.adapter.ProductosAdapterFilter;
import com.codpaa.model.ProductosModel;

import java.util.ArrayList;

public class SingleSpinnerSelect extends androidx.appcompat.widget.AppCompatSpinner implements
        OnMultiChoiceClickListener, OnCancelListener {


    ProductosAdapterFilter arrayAdapter;


    public SingleSpinnerSelect(Context context) {
        super(context);
    }

    public SingleSpinnerSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleSpinnerSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {



    }

    @Override
    public void onCancel(DialogInterface dialog) {





    }

    @Override
    public boolean performClick() {


        //final EditText editText = new EditText(getContext());

        final SearchView searchView = new SearchView(getContext());
        searchView.setQueryHint("Nombre producto o cb");

        final EditText editText = searchView.findViewById(R.id.search_src_text);
        editText.setHintTextColor(getResources().getColor(R.color.gris));
        editText.setTextColor(getResources().getColor(R.color.negroFondo));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {


                arrayAdapter.getFilter().filter(null);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if(TextUtils.isEmpty(newText)){

                    arrayAdapter.getFilter().filter(null);

                }else {

                    arrayAdapter.getFilter().filter(newText);
                }


                return false;
            }
        });





        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setAdapter(arrayAdapter, null);



        searchView.setQuery("", false);
        SingleSpinnerSelect.this.setSelection(0);




        final AlertDialog alertDialog = builder.create();

        //alertDialog.setCancelable(false);

        ListView listView = alertDialog.getListView();
        listView.setPadding(16,16,16,0);


        //editText.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_search_24), null, null, null);


        alertDialog.setCustomTitle(searchView);

        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                alertDialog.dismiss();

                SingleSpinnerSelect.this.setSelection(position);





            }
        });




        //alertDialog

        alertDialog.show();

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return true;
    }

    public void setItems(ArrayList<ProductosModel> items, String allText){


        //all selected by default
        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{allText});
        setAdapter(adapter);*/
        arrayAdapter = new ProductosAdapterFilter(getContext(),android.R.layout.simple_spinner_item,
                items);
        setAdapter(arrayAdapter);


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

    public ProductosModel getSelected(){


        return arrayAdapter.getItem(getSelectedItemPosition());

    }
}