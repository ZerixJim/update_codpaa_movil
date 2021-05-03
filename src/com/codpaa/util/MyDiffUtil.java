package com.codpaa.util;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.codpaa.model.ProductosModel;

import java.util.List;

public class MyDiffUtil extends DiffUtil.Callback {

    private List<ProductosModel> oldList;
    private List<ProductosModel> newLlist;


    public MyDiffUtil(List<ProductosModel> oldList, List<ProductosModel> newLlist) {
        this.oldList = oldList;
        this.newLlist = newLlist;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newLlist.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getIdProducto() == newLlist.get(newItemPosition).getIdProducto();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getCapturated() == newLlist.get(newItemPosition).getCapturated() ;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
