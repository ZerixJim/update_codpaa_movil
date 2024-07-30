package com.codpaa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codpaa.adapter.SliderAdapter;
import com.codpaa.model.SliderData;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

import com.codpaa.R;

public class Disclosure extends AppCompatActivity implements View.OnClickListener {
    // Urls of our images.
    String url1 = "https://www.geeksforgeeks.org/wp-content/uploads/gfg_200X200-1.png";
    String url2 = "https://qphs.fs.quoracdn.net/main-qimg-8e203d34a6a56345f86f1a92570557ba.webp";
    String url3 = "https://bizzbucket.co/wp-content/uploads/2020/08/Life-in-The-Metro-Blog-Title-22.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disclosure_view);

        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);
        TextView tv = findViewById(R.id.acceptDisclosure);

        //Set the listener
        tv.setOnClickListener(this);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(R.drawable.location, getString(R.string.titulo1), getString(R.string.avisoUbicacion)));
        sliderDataArrayList.add(new SliderData(R.drawable.images_icon, getString(R.string.titulo2), getString(R.string.avisoImagen)));
        sliderDataArrayList.add(new SliderData(R.drawable.policy, getString(R.string.titulo3), getString(R.string.avisoPoliticas)));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(6);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.acceptDisclosure){
            Intent i = new Intent(Disclosure.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
