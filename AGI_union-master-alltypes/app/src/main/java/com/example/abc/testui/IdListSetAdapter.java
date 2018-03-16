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

import java.util.List;

/**
 * Created by abc on 2017/9/1.
 */

public class IdListSetAdapter extends RecyclerView.Adapter<IdListSetAdapter.MyViewHolder> {
    private Context context;
    private List<IdListDatas> listIdListDatas;

    private int isSelected = -1;

    public IdListSetAdapter(Context context, List<IdListDatas> listIdListDatas){
        this.context = context;
        this.listIdListDatas = listIdListDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.id_list_set_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_no.setText(""+listIdListDatas.get(position).getNo());
        holder.tv_imsi.setText(String.format("%d", listIdListDatas.get(position).getImsi()));
        holder.tv_name.setText(listIdListDatas.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rvItemClickListenerInterface!=null){
                    isSelected = position; //qweqwe
                    rvItemClickListenerInterface.onClick(v, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (rvItemClickListenerInterface!=null){
                    rvItemClickListenerInterface.onLongClick(v, position);
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
        return listIdListDatas.size();
    }

    public void addItem(int position){
        notifyItemInserted(position);
    }

    public void changeItem(int position){
        notifyItemChanged(position);
    }

    public void removeItem(int position){
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listIdListDatas.size()-position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_no;
        TextView tv_imsi;
        TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_no = (TextView) itemView.findViewById(R.id.tv_no_id);
            tv_imsi = (TextView) itemView.findViewById(R.id.tv_imsi_id);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name_id);
        }
    }

    // RecyclerView 中条目的点击、长按 回调接口方法
    interface RVItemClickListenerInterface{
        void onClick(View v, int position);
        void onLongClick(View v, int position);
    }

    private RVItemClickListenerInterface rvItemClickListenerInterface;

    public void setRVItemClickListenerInterface(RVItemClickListenerInterface rvItemClickListenerInterface){
        this.rvItemClickListenerInterface = rvItemClickListenerInterface;
    }

}
