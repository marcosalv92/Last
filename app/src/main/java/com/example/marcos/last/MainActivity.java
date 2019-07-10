package com.example.marcos.last;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.marcos.last.BaseDatos;
import com.example.marcos.last.ListDatos;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    Button bton_aceptar;
    Button bton_cancelar;
    EditText intervalo;
//    public SharedPreferences preferences;
    public int intfrecuencia = 60;
    public boolean mystateGps = false;
    BaseDatos mBaseDatos;
    ListDatos mListDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finish();
        bton_aceptar = (Button)findViewById(R.id.button);
        bton_cancelar = (Button)findViewById(R.id.bton_detener);
        intervalo = (EditText)findViewById(R.id.editText);
        mBaseDatos = new BaseDatos();
        mListDatos = new ListDatos();

//        preferences = getSharedPreferences("Intervalo",MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
        if (intfrecuencia == 60){
//            editor.putInt("frecuencia",intfrecuencia);
            mListDatos = mBaseDatos.LeerBaseDatosXML();
//            mListDatos.setEstado(false);
//            mListDatos.setNumero(String.valueOf(intfrecuencia));
//            mListDatos.setNombre("Estado");
            try {
                mBaseDatos.CrearBaseDatosXML(mListDatos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        intfrecuencia = Integer.parseInt(mBaseDatos.LeerBaseDatosXML().getNumero());//preferences.getInt("frecuencia",intfrecuencia);
        intervalo.setHint(String.valueOf(intfrecuencia));
        bton_aceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String stringfrecuencia = intervalo.getText().toString();
                if (stringfrecuencia.equals("")){
                    Toast.makeText(getApplicationContext(), "Introduzca intervalo requerido", Toast.LENGTH_LONG).show();
                }
                else {
                    intfrecuencia = Integer.parseInt(stringfrecuencia);
//                    preferences = getSharedPreferences("Intervalo",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putInt("frecuencia",intfrecuencia);
//                    editor.commit();
                    mListDatos = mBaseDatos.LeerBaseDatosXML();
                    mListDatos.setNumero(stringfrecuencia);
                    try {
                        mBaseDatos.CrearBaseDatosXML(mListDatos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    intervalo.setHint(String.valueOf(intfrecuencia));
                    Toast.makeText(getApplicationContext(), "Las actualizaciones serán recibidas cada "+ intfrecuencia + " segundos." , Toast.LENGTH_LONG).show();

                }

            }
        });
        bton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDatos = mBaseDatos.LeerBaseDatosXML();
                mListDatos.setEstado(false);
                try {
                    mBaseDatos.CrearBaseDatosXML(mListDatos);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.exit(0);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
