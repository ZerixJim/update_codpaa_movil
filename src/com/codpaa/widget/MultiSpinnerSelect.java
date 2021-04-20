package com.codpaa.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.codpaa.R;
import com.codpaa.adapter.ProductosAdapter;
import com.codpaa.model.ProductosModel;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiSpinnerSelect extends androidx.appcompat.widget.AppCompatSpinner implements
        OnMultiChoiceClickListener, OnCancelListener {


    ProductosAdapter arrayAdapter;


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



        /*editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //editText.requestFocus();

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //Log.d("Text ", s.toString());

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {


                       if(TextUtils.isEmpty(s)){

                           arrayAdapter.getFilter().filter(null);

                       }else {

                           arrayAdapter.getFilter().filter(s);
                       }

                   }
               }, 900);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setAdapter(arrayAdapter, null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                searchView.setQuery("", false);

            }
        });


        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {




                dialog.cancel();


            }
        });

        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {



                ArrayList<ProductosModel> productos = getSelectedItems();



                String spinnerText;

                int count =  productos.size();

                if (count == 1){
                    spinnerText =  count+ " producto seleccionado";

                }else if(count > 1){

                    spinnerText = count + " productos seleccionados";


                }else {
                    spinnerText = "Selecciona producto";
                }





                if (count > 0){
                   /* spinnerText = spinnerBuffer.toString();
                    if (spinnerText.length() > 2)
                        spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
                    else
                        spinnerText = defaultText;*/

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                            R.layout.my_textview, new String[]{ spinnerText });
                    setAdapter(adapter);


                } else {
                    setAdapter(arrayAdapter);
                }

            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.setCancelable(false);

        ListView listView = alertDialog.getListView();
        listView.setDivider(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        listView.setDividerHeight(1);
        listView.setPadding(16,16,16,0);


        //editText.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_search_24), null, null, null);


        alertDialog.setCustomTitle(searchView);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductosModel producto = (ProductosModel) parent.getItemAtPosition(position);

                //Log.d("Click", "" + view.getId());

                producto.setChecked(!producto.isChecked());

                arrayAdapter.notifyDataSetChanged();

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
        arrayAdapter = new ProductosAdapter(getContext(),android.R.layout.simple_spinner_item,
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


}