package com.netdevs.subarata.bledevicemonitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DeviceListAdapter deviceListAdapter;
    private ListViewCompat listViewCompat;
    private DeviceManager deviceManager;
    private ArrayList<Device> devices;
    private Map<String, Device> devicesMap;
    private static final int REQUEST_ENABLE_BT = 1;
    private static TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceManager = DeviceManager.getDeviceManager();
        devicesMap = new HashMap<>(20);
        devices = new ArrayList<>(20);
        deviceListAdapter = new DeviceListAdapter(MainActivity.this, R.layout.device_list_row, devices);

        listViewCompat = (ListViewCompat) findViewById(R.id.device_list);
        listViewCompat.setAdapter(deviceListAdapter);
        listViewCompat.setOnItemClickListener(this);

        state = (TextView) findViewById(R.id.state);

        ((AppCompatButton) findViewById(R.id.btn_start_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setText("Scan start");
                deviceManager.scan(MainActivity.this, scanCallback);
            }
        });

        ((AppCompatButton) findViewById(R.id.btn_stop_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setText("Scan stop");
                deviceManager.scan(MainActivity.this, scanCallback);
            }
        });

        ((AppCompatButton) findViewById(R.id.btn_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Under development", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DeviceManager.getDeviceManager().checkSupportBluetooth(MainActivity.this);
    }

    BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    state.setText("Founded : " + device);
                    addDevice(device, rssi);
                }
            });
        }
    };

    private void addDevice(final BluetoothDevice device, final int rssi) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String address = device.getAddress();

                if (!devicesMap.containsKey(address)) {

                    Device bleDevice = new Device(device);

                    bleDevice.setRssi(rssi);

                    devicesMap.put(address, bleDevice);

                    devices.add(bleDevice);

                } else {
                    devicesMap.get(address).setRssi(rssi);
                }

                // List view adapter must be in UI thread ether not display list
                deviceListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {

            if (resultCode == RESULT_CANCELED) {
                this.finish();
            } else {
                state.setText("Bluetooth Enabled");
            }
        }
    }

    public static int getRequestEnableBt() {
        return REQUEST_ENABLE_BT;
    }

    public static TextView getState() {
        return state;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String name = devices.get(position).getName();
        String address = devices.get(position).getAddress();

/*        Intent intent = new Intent(this, Class.class);
        intent.putExtra("DEVICE_NAME", name);
        intent.putExtra("DEVICE_ADDRESS", address);
        startActivity(intent);*/
    }
}
