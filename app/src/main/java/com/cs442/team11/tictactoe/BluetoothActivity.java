package com.cs442.team11.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.logging.LogRecord;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    BluetoothAdapter myBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayList<String> pDevices;
    ArrayList<BluetoothDevice> devices;
    ListView myListView;
    ArrayAdapter<String> BTArrayAdapter;
    public static int REQUEST_BLUETOOTH = 1;
    IntentFilter filter;
    BroadcastReceiver receiver;
    protected static final int SUCCESS_CONNECT=0;
    protected static final int MESSAGE_READ=1;
    public static final UUID MY_UUID= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        init();
        // Phone does not support Bluetooth so let the user know and exit.
        if (myBluetoothAdapter == null) {
            new AlertDialog.Builder(BluetoothActivity.this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            if (!myBluetoothAdapter.isEnabled()) {
                turnOn();
            }
            getPairedDevices();
            startDiscovery();
        }

      mHandler = new Handler()
      {
          @Override
          public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what)
            {
                case SUCCESS_CONNECT:
                    ConnectedThread connectedThread=new ConnectedThread((BluetoothSocket)msg.obj);
                    Toast.makeText(getApplicationContext(),"Connect",Toast.LENGTH_SHORT).show();
                    String s= "Successfully connected";
                    connectedThread.write(s.getBytes());
                    break;

                case MESSAGE_READ:
                    byte[] readBuf=(byte[])msg.obj;
                    String string= new String(readBuf);
                    Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
      };
    }

    public void init()
    {
        myListView =(ListView)findViewById(R.id.listView1);
        myListView.setOnItemClickListener(this);
        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        myListView.setAdapter(BTArrayAdapter);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pDevices=new ArrayList<String>();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        devices=new ArrayList<BluetoothDevice>();
        receiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action= intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    String s="";
                    for(int a=0;a<pDevices.size();a++)
                    {
                        if(device.getName().equals(pDevices.get(a))){
                            s="(PAIRED)";
                            break;
                        }
                    }
                    BTArrayAdapter.add(device.getName()+" "+s+" " + "\n" + device.getAddress());
                   // BTArrayAdapter.add(device.getName()+" "+ device.getAddress());
                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                }
                else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    if(myBluetoothAdapter.getState()== myBluetoothAdapter.STATE_OFF) {
                        turnOn();
                    }
                }
            }
        };
        registerReceiver(receiver, filter);
        filter= new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, filter);
        filter= new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        filter= new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver,filter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
           Toast.makeText(getApplicationContext(),"Bluetooth must be enabled to Continue",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void getPairedDevices()
    {
        pairedDevices = myBluetoothAdapter.getBondedDevices();
      /*  if(pairedDevices.size()>0)
        {*/
            for(BluetoothDevice device : pairedDevices) {
               //  pDevices.add(device.getName());
                BTArrayAdapter.add(device.getName());
            }
       // }
       /* Toast.makeText(getApplicationContext(),"Show Paired Devices",
                Toast.LENGTH_SHORT).show();*/
    }

    public void startDiscovery()
    {
        myBluetoothAdapter.cancelDiscovery();
        myBluetoothAdapter.startDiscovery();
    }

    public void turnOn()
    {
        Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBT, REQUEST_BLUETOOTH);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
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
*/

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3)
    {
        if(myBluetoothAdapter.isDiscovering())
        {
            myBluetoothAdapter.cancelDiscovery();
        }
       // if(BTArrayAdapter.getItem(arg2).contains("(Paired)")){
            Object[] o=pairedDevices.toArray();
            BluetoothDevice selectedDevice= (BluetoothDevice)o[arg2];
            ConnectThread connect=new ConnectThread(selectedDevice);
            connect.start();
            Toast.makeText(getApplicationContext(),"Device is paired",Toast.LENGTH_SHORT).show();
       // }
       /* else
            Toast.makeText(getApplicationContext(),"Device is not paired",Toast.LENGTH_SHORT).show();*/
    }


    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            myBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
           // manageConnectedSocket(mmSocket);
            mHandler.obtainMessage(SUCCESS_CONNECT,mmSocket).sendToTarget();
        }

       /* private  void manageConnectedSocket(BluetoothSocket mmSocket2)
        {

        }*/
        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    buffer = new byte[1024];
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}

