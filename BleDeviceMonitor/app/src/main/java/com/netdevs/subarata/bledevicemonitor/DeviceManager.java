package com.netdevs.subarata.bledevicemonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Subarata Talukder on 22/10/2016.
 */
public class DeviceManager {

    private boolean scanningFlag;
    private BluetoothAdapter bluetoothAdapter;
    private ProgressDialog progressBar;
    private final int SCAN_PERIOD = 3000;
    private static DeviceManager ourInstance = new DeviceManager();

    public static DeviceManager getDeviceManager() {
        return ourInstance;
    }

    private void scanDevice(final Activity activity, final BluetoothAdapter.LeScanCallback leScanCallback, boolean enable) {

        if (enable && !scanningFlag) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getState().setText("Stop scan");
                    hideProgress();
                    scanningFlag = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);
            MainActivity.getState().setText("Start scan");
            showProgress(activity);
            scanningFlag = true;
            bluetoothAdapter.startLeScan(leScanCallback);
        } else {
            MainActivity.getState().setText("Stop scan");
            hideProgress();
            scanningFlag = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    public boolean isScanning() {
        return scanningFlag;
    }

    public void scan(final Activity activity, final BluetoothAdapter.LeScanCallback scanCallback) {

        if (!isScanning()) {
            scanDevice(activity, scanCallback, true);
        }
    }

    public void stopScan(final Activity activity, final BluetoothAdapter.LeScanCallback scanCallback) {

        if (isScanning()) {
            scanDevice(activity, scanCallback, false);
            scanningFlag = false;
        }
    }

    public void checkSupportBluetooth(Activity activity) {

        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(activity, "Bluetooth does not support", Toast.LENGTH_SHORT).show();
            activity.finish();
            return;
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {

            Toast.makeText(activity, "Bluetooth does not support", Toast.LENGTH_SHORT).show();
            activity.finish();
            return;
        } else {

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, MainActivity.getRequestEnableBt());
            }
        }
    }

    public void showProgress(Activity activity) {

        progressBar = new ProgressDialog(activity);
        progressBar.setCancelable(true);
        progressBar.setMessage("Scanning....");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }

    public void hideProgress() {

        progressBar.dismiss();
    }
}
