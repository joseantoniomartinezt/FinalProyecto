package com.example.solicitudpiezasv3.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.solicitudpiezasv3.R;



public class Fragmento extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private int PAGINA;

    public static Fragmento newInstance(int ind) {
        Fragmento fragment = new Fragmento();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, ind);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int ind = 0;
        if (getArguments() != null)
        {
            ind = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        PAGINA = ind;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;

        if(PageAdapter.esPagina(PAGINA,"Texto")){

            view = inflater.inflate(R.layout.texto,container,false);
            TextView txt = view.findViewById(R.id.textViewTitulo);
            txt.setText("Texto");
        }
        else{
            view = inflater.inflate(R.layout.imagen,container,false);
            TextView txt = view.findViewById(R.id.textViewTitulo);
            txt.setText("Imagen");
        }


        return view;
    }
}