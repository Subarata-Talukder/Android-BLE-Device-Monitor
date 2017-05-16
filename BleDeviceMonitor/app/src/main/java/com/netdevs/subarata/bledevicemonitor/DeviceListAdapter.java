package com.netdevs.subarata.bledevicemonitor;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Subarata Talukder on 23/10/2016.
 */

public class DeviceListAdapter extends BaseAdapter {

    private Activity context;
    private int layoutResourceId;
    private List<Device> deviceList;

    public DeviceListAdapter(Activity context, int layoutResourceId, List<Device> deviceList) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList != null ? deviceList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Device device = deviceList.get(position);
        String deviceName = device.getName();
        String deviceAddress = device.getAddress();
        int deviceRssi = device.getRssi();

        TextView tvDeviceName = (TextView) convertView.findViewById(R.id.tv_device_name);

        if (deviceName != null && deviceName.length() > 0) {

            tvDeviceName.setText(device.getName());
        } else {

            tvDeviceName.setText("No Name");
        }

        TextView tvDeviceRssi = (TextView) convertView.findViewById(R.id.tv_device_rssi);
        tvDeviceRssi.setText(Integer.toString(deviceRssi));

        TextView tvDeviceMacAddress = (TextView) convertView.findViewById(R.id.tv_mac_address);

        if (deviceAddress != null && deviceAddress.length() > 0) {

            tvDeviceMacAddress.setText(device.getAddress());
        } else {

            tvDeviceMacAddress.setText("No Address");
        }

        return convertView;
    }
}
