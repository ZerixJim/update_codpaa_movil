package com.codpaa.adapter;

/*
 * Created by grim on 24/01/17.
 */

import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.MenuTiendaModel;

import java.util.List;

public class MenuTiendaAdapter extends RecyclerView.Adapter<MenuTiendaAdapter.ViewHolder>{

    private List<MenuTiendaModel> menuModels;
    private MenuTiendaListener mMenuItemListener;

    public MenuTiendaAdapter(List<MenuTiendaModel> menuItems, MenuTiendaListener itemListener){

        mMenuItemListener = itemListener;
        menuModels = menuItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu_tienda, parent, false);
        return new ViewHolder(view, mMenuItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuTiendaModel model = getItem(position);
        holder.nombre.setText(model.getNombreMenu());

        Uri uri = Uri.parse("android.resource://com.codpaa/drawable/"+ model.getImage());
        holder.image.setImageURI(uri);

    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    private MenuTiendaModel getItem(int position){
        return menuModels.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView image;
        private TextView nombre;
        private MenuTiendaListener mMenuItemListener;

        public ViewHolder(View itemView, MenuTiendaListener listener) {
            super(itemView);


            mMenuItemListener = listener;

            image = (ImageView) itemView.findViewById(R.id.image);
            nombre = (TextView) itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MenuTiendaModel menuItem = getItem(position);
            mMenuItemListener.onMenuItemClick(menuItem);
        }
    }


    public interface MenuTiendaListener{
        void onMenuItemClick(MenuTiendaModel clickMenuItem);
    }

}
