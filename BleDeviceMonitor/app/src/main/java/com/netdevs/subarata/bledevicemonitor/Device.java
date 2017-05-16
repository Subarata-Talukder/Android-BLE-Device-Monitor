package com.netdevs.subarata.bledevicemonitor;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Subarata Talukder on 22/10/2016.
 */

public class Device {
    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public Device(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }
}
