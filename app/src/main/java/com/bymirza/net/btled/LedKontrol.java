package com.bymirza.net.btled;

import android.net.Uri;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

public class LedKontrol extends AppCompatActivity {

    SwitchCompat switch_compat_onoff;
    ImageButton BaglantiKes;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(CihazListesi.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the LedKontrol
        setContentView(R.layout.activity_ledcontrol);
        //call the widgets
        BaglantiKes = findViewById(R.id.baglantikes);
        new ConnectBT().execute(); //Call the class to connect
        BaglantiKes.setOnClickListener(v -> {
            Disconnect(); //close connection
        });

        switch_compat_onoff = findViewById(R.id.switch_compat_onoff);
        switch_compat_onoff.setChecked(false);
        switch_compat_onoff.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                turnOnLed();
            } else {
                turnOffLed();
            }
        });
    }

    private void Disconnect() {
        if (btSocket!=null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Hata!");}
        }
        finish(); //return to the first layout
    }

    private void turnOffLed(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("0".toString().getBytes());
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }

    private void turnOnLed(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("1".toString().getBytes());
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(LedKontrol.this, "Bağlanıyor...", "Lütfen Bekleyiniz...");  //show a progress dialog
        }
        @RequiresPermission( allOf = {"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"})
        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try{
                if (btSocket == null || !isBtConnected){
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e){
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess){
                msg("Bağlantı başarısız! Tekrar deneyiniz.");
                finish();
            }
            else {
                msg("Bağlandı");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    public void web(View view){
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.isimizegitim.net/"));
        startActivity(webIntent);
    }
}