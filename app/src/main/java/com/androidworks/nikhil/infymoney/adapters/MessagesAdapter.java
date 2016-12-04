package com.androidworks.nikhil.infymoney.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidworks.nikhil.infymoney.R;
import com.androidworks.nikhil.infymoney.model.SMS;
import com.androidworks.nikhil.infymoney.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikhil on 23-Nov-16.
 */
public class MessagesAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<SMS> smsItems;

    public MessagesAdapter(Context context, ArrayList<SMS> smsItems) {
        this.context = context;
        this.smsItems = smsItems;
    }

    @Override
    public int getCount() {
        return smsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.messages_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SMS item = smsItems.get(position);
       // Log.d("nikhil",item.getMessageBody());
        if (StringUtils.isNotEmpty(item.getAddress()) && (item.getLongDate() != 0) && (StringUtils.isNotEmpty(item.getMessageBody()))) {
            viewHolder.address.setText(item.getAddress());
            viewHolder.date.setText(Utils.getDate(item.getLongDate()));
            viewHolder.body.setText(item.getMessageBody());
        }

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tv_address)
        TextView address;
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.tv_body)
        TextView body;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
