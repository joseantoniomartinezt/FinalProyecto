package com.example.solicitudpiezasv3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;


import com.example.solicitudpiezasv3.GridViewAdapter;
import com.example.solicitudpiezasv3.ui.main.PageAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    PageAdapter adaptadorPaginas;
    private ArrayList<Uri> listaImagenes = new ArrayList<Uri>();
    private GridViewAdapter adaptadorImagenes;
    private Uri imagenUri;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        adaptadorPaginas = new PageAdapter(this, getSupportFragmentManager());


        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adaptadorPaginas);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        gridView = findViewById(R.id.vistaImagenes);

        pedirPermisos();
    }

    public void pedirPermisos(){

        try {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions,1);

        }catch(Exception e){
            mostrarAlerta("Error",e.toString());
        }
    }

    String fileFoto = android.os.Environment.getExternalStorageDirectory() + File.separator+ "PRUEBA_TABS" + File.separator+ "temp.jpg";

    public void tomarFoto(View view){

        try {

            pedirPermisos();

            Intent intentTomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(fileFoto);
            Uri fp = FileProvider.getUriForFile(getApplicationContext(), this.getApplicationContext().getPackageName() + ".provider", f);

            new File(f.getParent()).mkdirs();

            intentTomarFoto.putExtra(MediaStore.EXTRA_OUTPUT,fp);
            intentTomarFoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentTomarFoto.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intentTomarFoto, 1);


        }catch (Exception e){
            String ex = e.getMessage();
            mostrarAlerta("tomarFoto", ex);
        }
    }

    public void abrirGaleria(){
        Intent galeriaIntent = new Intent();
        galeriaIntent.setType("image/*");
        galeriaIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galeriaIntent,100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //ClipData clipData = data.getClipData();
        try {
            if (resultCode == RESULT_OK && requestCode == 1){
                File f = new File(fileFoto);

                try{

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "SOLICITUD_PIEZAS" + File.separator;
                    OutputStream outFile = null;

                    File file = new File(path);
                    file.mkdirs();
                    file = new File(path, "SOLICITUD_" + new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().getTime()) + ".jpg");

                    outFile = new FileOutputStream(file);
                    System.gc();

                    bitmap.compress(Bitmap.CompressFormat.JPEG,70,outFile);
                    System.gc();
                    outFile.flush();
                    outFile.close();

                    f.delete();

                    Intent intentActualizarGaleria = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(file);
                    intentActualizarGaleria.setData(contentUri);
                    this.sendBroadcast(intentActualizarGaleria);

                    listaImagenes.add(contentUri);


                }catch (Exception e){
                    mostrarAlerta("Error al tomar la foto", e.getMessage());
                }
            }

            /*else if(requestCode == 100 && resultCode == RESULT_OK){
                try {

                    if(clipData == null){
                        imagenUri = data.getData();
                        listaImagenes.add(imagenUri);
                    }else {
                        for(int ind=0;ind<clipData.getItemCount();ind++){
                            listaImagenes.add(clipData.getItemAt(ind).getUri());
                        }
                    }

                }catch (Exception e){
                    mostrarAlerta("Error al abrir la galeria", e.getMessage());
                }
            }*/

            adaptadorImagenes = new GridViewAdapter(this,listaImagenes);
            gridView.setAdapter(adaptadorImagenes);

        }catch (Exception e){

        }
    }

    public void siguientePagina(){

        int pos = tabs.getSelectedTabPosition() + 1;

        if(pos < adaptadorPaginas.getCount()){
            tabs.getTabAt(pos).select();
        }
    }

    public void paginaAnterior(){

        int posicion = tabs.getSelectedTabPosition() - 1;

        if(posicion >= 0){
            tabs.getTabAt(posicion).select();
        }
    }

    public void mostrarAlerta(String asunto, String texto)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(asunto);
        builder.setMessage(texto);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }
}