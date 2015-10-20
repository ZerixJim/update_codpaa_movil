package com.codpaa.fragment;
/*
 * Created by Gustavo on 20/10/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.codpaa.R;
import com.codpaa.adapter.SimpleRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentTest extends Fragment{



    String[] data = new String[]{"Tienda 1", "Tienda 2", "Tienda 3", "Tienda 4",
            "Tienda 1", "Tienda 2", "Tienda 3", "Tienda 4","Tienda 1", "Tienda 2",
            "Tienda 3", "Tienda 4"};
    SimpleRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_calendario);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            list.add(data[i]);
        }
        adapter = new SimpleRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
