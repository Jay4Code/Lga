package com.sunricher.app.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lga.control.wificontrol.bean.Wlan;

import java.util.ArrayList;

/**
 * Created by Jay.X
 */
public class WlanAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView tvSsid;

        public ViewHolder(View itemView) {
            tvSsid = (TextView) itemView.findViewById(R.id.tv_ssid);
        }

        public void init(Wlan wlan) {
            tvSsid.setText(wlan.getSsid());
        }
    }

    private Context mContext;
    private int mLayoutId;
    private ArrayList<Wlan> mDataList;
    public WlanAdapter(Context context, int layoutId, ArrayList<Wlan> dataList) {
        mContext = context;
        mLayoutId = layoutId;
        mDataList = (dataList == null ? new ArrayList<Wlan>() : dataList);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Wlan getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.init(getItem(position));

        return convertView;
    }

    public void setDataList(ArrayList<Wlan> dataList) {
        if(dataList != null) {
            mDataList = dataList;
        }
    }
}
