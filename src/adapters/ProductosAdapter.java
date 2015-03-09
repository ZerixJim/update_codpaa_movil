package adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.SpinnerProductoModel;

import java.util.ArrayList;
import modelos.ProductosModel;

public class ProductosAdapter extends ArrayAdapter<ProductosModel> {
    Context _context;
    private ArrayList<ProductosModel> _datos;
    LayoutInflater inflater;
    ProductosModel productosModel = null;


    private class ViewHolder {
        TextView nombreProducto;
        TextView presentacion;
        CheckBox checkBox;
        ProductosModel productosModel;
    }


    public ProductosAdapter(Context context, int textViewResourceId,
                            ArrayList<ProductosModel> objects) {


        super(context,textViewResourceId,objects);


        this._context = context;

        this._datos = objects;


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        if(row == null){
            row = inflater.inflate(R.layout.custom_view_productos, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombreProducto = (TextView) row.findViewById(R.id.textProNombre);
            viewHolder.presentacion = (TextView) row.findViewById(R.id.textProducPresentacion);
            viewHolder.checkBox = (CheckBox) row.findViewById(R.id.checkProduct);

            row.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) row.getTag();
            viewHolder.productosModel = productosModel;
        }

        productosModel = _datos.get(position);




        viewHolder.nombreProducto.setText(productosModel.getNombre());
        viewHolder.presentacion.setText(productosModel.getPresentacion());



        if (position == 0 && productosModel.getIdProducto() == 0)
            viewHolder.checkBox.setVisibility(View.INVISIBLE);

        if (position % 2 == 1){
            row.setBackgroundColor(Color.rgb(243,243,243));
        }else {
            row.setBackgroundColor(Color.WHITE);
        }

        if (productosModel.isSeleted()){
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);

        }



        return row;
    }



    public void addElements(ArrayList<ProductosModel> datos){
        this._datos = datos;
        notifyDataSetChanged();
    }

    private ProductosModel getProductosModel(){

        final ProductosModel pm = new ProductosModel();
        pm.setIdProducto(0);
        pm.setPresentacion("Selecciona Producto");
        pm.setNombre("");

        return pm;
    }




}
