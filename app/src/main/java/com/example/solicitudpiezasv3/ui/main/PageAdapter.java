package com.example.solicitudpiezasv3.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.solicitudpiezasv3.R;

public class PageAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]
            {
                    R.string.tabTexto,
                    R.string.tabFoto,
            };
    private final Context mContext;
    static Context contexto;

    public PageAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
        contexto = context;
    }


    public Fragment getItem(int position) {

        return Fragmento.newInstance(position);
    }

    @Nullable
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }


    public int getCount() {
        return TAB_TITLES.length;
    }

    public static boolean esPagina (int position, String nombre_pagina)
    {
        return (contexto.getResources().getString(TAB_TITLES[position]).equalsIgnoreCase(nombre_pagina));
    }
}
