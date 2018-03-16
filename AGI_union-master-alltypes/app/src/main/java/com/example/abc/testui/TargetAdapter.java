package com.example.abc.testui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * Created by abc on 2017/9/4.
 */

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.MyViewHolder> {
    private Context context;
    private List<RealTimeDatas> listRealTimeDatases;

    private int isSelected = -1;

    public TargetAdapter(Context context, List<RealTimeDatas> listRealTimeDatases){
        this.context = context;
        this.listRealTimeDatases = listRealTimeDatases;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.tab2_rv_target_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_sequence.setText(""+listRealTimeDatases.get(position).getSequence());
        holder.tv_imsi.setText(String.format("%d", listRealTimeDatases.get(position).getImsi()));
        holder.tv_count.setText(""+listRealTimeDatases.get(position).getCount());
//        String beginDateStr = new SimpleDateFormat("HH:mm:ss").format(listRealTimeDatases.get(position).getBeginDate());
//        holder.tv_beginDate.setText(beginDateStr);
        String endDateStr = new SimpleDateFormat("HH:mm:ss").format(listRealTimeDatases.get(position).getEndDate());
        holder.tv_endDate.setText(endDateStr);
        holder.tv_rssi.setText(""+listRealTimeDatases.get(position).getRssi());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (targetOnClickListenerInterface!=null){
                    isSelected = position; //qweqwe
                    targetOnClickListenerInterface.onClick(v, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (targetOnClickListenerInterface!=null){
                    targetOnClickListenerInterface.onLongClick(v, position);
                    return true;
                }
                return false;
            }
        });

        if (isSelected!=position){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        else{
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        }
    }

    @Override
    public int getItemCount() {
        return listRealTimeDatases.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_sequence;
        private TextView tv_imsi;
        private TextView tv_count;
//        private TextView tv_beginDate;
        private TextView tv_endDate;
        private TextView tv_rssi;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_sequence = (TextView) itemView.findViewById(R.id.tv_target_sequence_id);
            tv_imsi = (TextView) itemView.findViewById(R.id.tv_target_imsi_id);
            tv_count = (TextView) itemView.findViewById(R.id.tv_target_count_id);
//            tv_beginDate = (TextView) itemView.findViewById(R.id.tv_target_beginDate_id);
            tv_endDate = (TextView) itemView.findViewById(R.id.tv_target_endDate_id);
            tv_rssi = (TextView) itemView.findViewById(R.id.tv_target_rssi_id);
        }
    }

    public void addItem(int position){
        notifyItemInserted(position);
//        notifyItemRangeChanged(position-1, position);
    }

    public void changeItem(int position){
        notifyItemChanged(position);
    }

    public void refreshTarget(){
        notifyDataSetChanged();
//        notifyItemInserted(listRealTimeDatases.size());
    }

    // 设置中标流水条目点击、长按事件
    interface TargetOnClickListenerInterface{
        void onClick(View v, int position);
        void onLongClick(View v, int position);
    }

    private TargetOnClickListenerInterface targetOnClickListenerInterface;

    public void setTargetOnClickListenerInterface(TargetOnClickListenerInterface targetOnClickListenerInterface){
        this.targetOnClickListenerInterface = targetOnClickListenerInterface;
    }
}
