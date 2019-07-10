package com.example.marcos.last;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.Toast;
import com.example.marcos.last.BaseDatos;
import com.example.marcos.last.ListDatos;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class  MyReceiver extends BroadcastReceiver {
    double current_lat, current_lng;
    public LocationManager mlocManager;
    LocationListener mlocListener;
    Context mcontext;
    String numeroEnviar;
    String nombre;
    //public boolean gps_enable;
    public SharedPreferences preferences_gps;
    BaseDatos baseDatos;
    ListDatos gps_state;
//    public MyReceiver() {
//
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        baseDatos = new BaseDatos();
        gps_state = new ListDatos();
        try
        {
//                    preferences_gps = context.getSharedPreferences("GPS",Context.MODE_PRIVATE );
//                    int frec = preferences_gps.getInt("frecuencia",60);
                gps_state = baseDatos.LeerBaseDatosXML();
                String frec = gps_state.getNumero();
                Toast.makeText(context,"frecuencia: "+frec,Toast.LENGTH_SHORT).show();

                Bundle bundle = intent.getExtras();
                if (bundle != null) {

                    Object[] pdus = (Object[]) bundle.get("pdus");
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++)
                         messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);


                        if (messages.length > -1)
                        {
                            String messagebody = messages[0].getMessageBody();
                            numeroEnviar = messages[0].getOriginatingAddress();
                            if (messagebody.toString().contains(context.getString(R.string.CodigoGPS)))
                            {
                                String [] descompuesto = messagebody.split("@");
                                nombre = descompuesto[1];
                                /*LocationManager*/ mlocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                                /*LocationListener*/ mlocListener = new MyLocationListener(context.getApplicationContext());
                               /*Location*/

                                Toast.makeText(context.getApplicationContext(), "Inicio loco GPS", Toast.LENGTH_LONG).show();
                                int frec_Seconds = Integer.valueOf(gps_state.getNumero());

                                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,frec_Seconds*1000, 2, mlocListener);
                                gps_state.setEstado(true);
                                try {
                                    baseDatos.CrearBaseDatosXML(gps_state);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
//                                baseDatos.CrearBaseDatosXML(gps_state);
                                //gps_enable = true;


                            }
                            else if (messagebody.toString().contains(context.getString(R.string.DetenerGPS))){
                                String [] descompuesto = messagebody.split("@");
                                nombre = descompuesto[1];
                                gps_state.setEstado(false);
                                try {
                                    baseDatos.CrearBaseDatosXML(gps_state);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String Text = "DetenidoGPS9208@"+nombre;
                                SmsManager sender = SmsManager.getDefault();
                                sender.sendTextMessage(numeroEnviar, null, Text, null, null);
                                finalize();
                                System.exit(0);
//                                mlocManager.removeUpdates(mlocListener);
//                                preferences_gps = context.getSharedPreferences("GPS",Context.MODE_PRIVATE );
//                                preferences_gps.edit().putBoolean("gps_state",false);
//                                preferences_gps.edit().commit();
//                                gps_enable = false;



                            }

                        }


                    }

        }
        catch(Exception ex)
        {
            Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        // throw new UnsupportedOperationException("Not yet implemented");
    }
    public class MyLocationListener implements LocationListener {

        public MyLocationListener(Context context){
        mcontext = context;

        }

        public void onLocationChanged(Location loc) {

            loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (loc != null)
            {
//                    preferences_gps = mcontext.getSharedPreferences("GPS",Context.MODE_PRIVATE );
//                    gps_enable = preferences_gps.getBoolean("gps_state",false);
//                    gps_state = baseDatos.LeerBaseDatosXML();
                    boolean gps_enable = gps_state.getEstado();
                    if (!gps_enable){
                        mlocManager.removeUpdates(mlocListener);
                        mlocManager = null;
//                        String Text = "DetenidoGPS9208@"+nombre;
//                        SmsManager sender = SmsManager.getDefault();
//                        sender.sendTextMessage(numeroEnviar, null, Text, null, null);
                        Toast.makeText(mcontext, "Detenido GPS", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(mcontext, "GPS WORKING", Toast.LENGTH_LONG)
                                .show();
                        current_lat = loc.getLatitude();
                        current_lng = loc.getLongitude();
                        String Text = "codigo9208@" + current_lat + "@" + current_lng + "@" + nombre + "@" + fechaHoraActual();
                        SmsManager sender = SmsManager.getDefault();
                        sender.sendTextMessage(numeroEnviar, null, Text, null, null);
                        Toast.makeText(mcontext, "SMS SENT CON lOCALIZACION", Toast.LENGTH_LONG).show();
                        //                mlocManager.removeUpdates(mlocListener);
                    }

            }
            else
            {
                Toast.makeText(mcontext, "GPS WORKING", Toast.LENGTH_LONG)
                        .show();


                String Text = nombre + ": Localizacion no encontrada";

                SmsManager sender = SmsManager.getDefault();
                sender.sendTextMessage(numeroEnviar, null, Text, null, null);
                Toast.makeText(mcontext, "SMS SENT", Toast.LENGTH_LONG).show();
//                mlocManager.removeUpdates(mlocListener);
            }
        }

        public String fechaHoraActual(){
            return new SimpleDateFormat( "HH:mm:ss_dd-MM-yyyy", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            SmsManager sender=SmsManager.getDefault();
            sender.sendTextMessage(numeroEnviar,null,nombre+": GPS Enabled"+fechaHoraActual() , null, null);
            mlocManager.removeUpdates(mlocListener);
        }

        @Override
        public void onProviderDisabled(String provider) {
            SmsManager sender=SmsManager.getDefault();
            sender.sendTextMessage(numeroEnviar,null,nombre+": GPS Disabled"+fechaHoraActual() , null, null);
            mlocManager.removeUpdates(mlocListener);
        }
    }

    public void Detener_Envio(){
        gps_state = baseDatos.LeerBaseDatosXML();
        gps_state.setEstado(false);
        try {
            baseDatos.CrearBaseDatosXML(gps_state);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
