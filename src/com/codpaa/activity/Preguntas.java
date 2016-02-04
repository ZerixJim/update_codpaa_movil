package com.codpaa.activity;

/*
 * Created by grim on 3/02/16.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codpaa.R;

public class Preguntas extends AppCompatActivity{

    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_encuestas);

        listView = (ListView) findViewById(R.id.listView);

        String[] array = {"Encuesta 1", "Encuesta 2"};

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);


        listView.setAdapter(arrayAdapter);


    }




}
