package com.example.abc.testui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jbtang.agi_union.R;

import java.util.List;

/**
 * Created by abc on 2017/10/12.
 */

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.MyViewHolder> {

    private Context context;
    private List<ScanData> listScanDatas;

    public ScanAdapter(Context context, List<ScanData> listScanDatas){
        this.context = context;
        this.listScanDatas = listScanDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.scan_rv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvSeq.setText(""+listScanDatas.get(position).getSeq());
        holder.tvEarfcn.setText(""+listScanDatas.get(position).getEarfcn());
        holder.tvPriority.setText(""+listScanDatas.get(position).getPriority());
        holder.tvPci.setText(""+listScanDatas.get(position).getPci());
    }

    @Override
    public int getItemCount() {
        return listScanDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvSeq;
        TextView tvEarfcn;
        TextView tvPriority;
        TextView tvPci;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvSeq = (TextView) itemView.findViewById(R.id.tv_scan_seq_id);
            tvEarfcn = (TextView) itemView.findViewById(R.id.tv_scan_earfcn_id);
            tvPriority = (TextView) itemView.findViewById(R.id.tv_scan_priority_id);
            tvPci = (TextView) itemView.findViewById(R.id.tv_scan_pci_id);
        }
    }

    public void addItem(int position){
        notifyItemInserted(position+1);
//        notifyDataSetChanged();
    }

    public void refresh(){
//        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
