package com.example.abc.testui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.abc.testui.R;

import com.example.jbtang.agi_union.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by abc on 2017/9/5.
 */

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {
    private Context context;
    private List<StateValues> listStateValues;

    public StateAdapter(Context context, List<StateValues> listStateValues) {
        this.context = context;
        this.listStateValues = listStateValues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.state_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String dateStr = new SimpleDateFormat("HH:mm:ss").format(listStateValues.get(position).getDate());
        holder.tv_date.setText(dateStr);
        holder.tv_msg.setText(listStateValues.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return listStateValues.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_date;
        private TextView tv_msg;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_state_date_id);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_state_msg_id);
        }
    }

    public void addItem(int position){
        notifyItemInserted(position);
    }
}
