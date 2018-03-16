package com.example.jbtang.agi_union.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbtang.agi_union.R;
import com.example.jbtang.agi_union.core.CellInfo;
import com.example.jbtang.agi_union.core.Global;
import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.Status;
import com.example.jbtang.agi_union.dao.cellinfos.CellInfoDAO;
import com.example.jbtang.agi_union.dao.cellinfos.CellInfoDBManager;
import com.example.jbtang.agi_union.dao.lbscellinfos.LbsCellInfoDBManager;
import com.example.jbtang.agi_union.device.Device;
import com.example.jbtang.agi_union.device.DeviceManager;
import com.example.jbtang.agi_union.device.MonitorDevice;
import com.example.jbtang.agi_union.external.MonitorApplication;
import com.example.jbtang.agi_union.messages.GenProtocolTraceMsg;
import com.example.jbtang.agi_union.messages.MessageDispatcher;
import com.example.jbtang.agi_union.messages.ag2pc.MsgCRS_RSRPQI_INFO;
import com.example.jbtang.agi_union.messages.ag2pc.MsgL1_PHY_COMMEAS_IND;
import com.example.jbtang.agi_union.messages.ag2pc.MsgL2P_AG_CELL_CAPTURE_IND;
import com.example.jbtang.agi_union.messages.base.MsgTypes;
import com.example.jbtang.agi_union.service.OrientationFinding;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 刘洋旭 on 2017/5/8.
 */
public class LbsLocationActivity extends AppCompatActivity {
    private CellInfoDBManager cellInfoDBManager;
    private LbsCellInfoDBManager lbsCellInfoDBManager;
    private List<CellInfo> cellInfoDBList;
    private List<CellInfo> cellsaveDBlist;
    private List<CellInfo> cellInfoList;
    private ListView listView;
    private Button stopbtn,locationbtn;
    private myHandler handler;
    public int index;
    private ProgressBar progressBar;
    private EditText  earfcn,pci,tac,ecgi;
    private boolean start;
//    private boolean locationflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lbstation);
        stopbtn= (Button) findViewById(R.id.lbs_stop);
        locationbtn= (Button) findViewById(R.id.lbs_location);
        listView = (ListView) findViewById(R.id.lbstation_listView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar5);
        earfcn= (EditText) findViewById(R.id.lbs_manual_earfcn);
        pci= (EditText) findViewById(R.id.lbs_manual_pci);
        tac= (EditText) findViewById(R.id.lbs_alarm_manual_tac);
        ecgi= (EditText) findViewById(R.id.lbs_alarm_manual_ecgi);
        cellInfoDBManager = new CellInfoDBManager(this);
        lbsCellInfoDBManager=new LbsCellInfoDBManager(this);

        cellInfoDBList = getCellInfoList();
        cellsaveDBlist=new ArrayList<CellInfo>();
        cellInfoList=new ArrayList<>(getlbsCellInfoList());
        if(cellInfoDBList.size()>0){
            boolean flag=true;
            CellInfo servicecell=cellInfoDBList.get(0);
            for (CellInfo info:cellInfoList){
                if (servicecell.earfcn==info.earfcn&&servicecell.pci==info.pci){
                    flag=false;
                    break;
                }
            }
            if (flag){
                if (cellInfoList.size()>0){
                    cellInfoList.set(0,servicecell);
                }else {
                    cellInfoList.add(servicecell);
                }

            }

        }
        start=false;
        handler=new myHandler(this);
        MessageDispatcher.getInstance().RegisterHandler(handler);
//        testpro();
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CellInfo clickcellInfo=cellInfoList.get(position);
                earfcn.setText(clickcellInfo.earfcn+"");
                pci.setText(clickcellInfo.pci+"");
                if (clickcellInfo.tai==Short.MAX_VALUE){
                    tac.setText(CellInfo.NULL_VALUE);
                }else {
                    tac.setText(clickcellInfo.tai+"");
                }
                if (clickcellInfo.ecgi==Integer.MAX_VALUE){
                    ecgi.setText(CellInfo.NULL_VALUE);
                }else {
                    ecgi.setText(clickcellInfo.ecgi+"");
                }
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start){
                    start=false;
                    for(MonitorDevice device:DeviceManager.getInstance().getDevices()){
                        device.stopMonitor();
                        device.setDeviceStatus(Status.DeviceStatus.IDLE);
                    }
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            LbsLocationActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    locationbtn.setEnabled(true);
                                }
                            });

                        }
                    },3000);

                }

            }
        });
//        freshbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationflag=true;
//                index=0;
//                MonitorDevice device = null;
//                if(DeviceManager.getInstance().getDevices().size()>0){
//                    device=DeviceManager.getInstance().getDevices().get(0);
//                }
//                if (device==null){
//                    Log.e("lbstation","device=null return");
//                    return;
//                }
//                while (true){
//                    if(cellInfoDBList.get(index).ecgi!=0&&cellInfoDBList.get(index).ecgi!=Integer.MAX_VALUE){
//                        index++;
////                        device.send(GenProtocolTraceMsg.gen((byte) 2, cellInfoDBList.get(i).earfcn, cellInfoDBList.get(i).pci, new byte[]{}));
//                    }else {
//                        device.setCellInfo(cellInfoDBList.get(index));
//                        device.send(GenProtocolTraceMsg.gen((byte) 2, cellInfoDBList.get(index).earfcn, cellInfoDBList.get(index).pci, new byte[]{}));
//                        Log.e("fresh","index"+index);
//                        index++;
//                        break;
//                    }
//                    if (index==cellInfoDBList.size()-1){
//                        break;
//                    }
//
//                }
//
//            }
//        });
        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                locationflag=false;
                if (validateCell()){
                    MonitorDevice odevice = null;
                    if(DeviceManager.getInstance().getDevices().size()>0){
                        odevice=DeviceManager.getInstance().getDevices().get(0);
                    }
                    if (odevice==null){
                        Toast.makeText(LbsLocationActivity.this, "无可用板卡！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    locationbtn.setEnabled(false);
                    start=true;
                    byte[] stmsi = "0000000000".getBytes();
//                    odevice.send(GenProtocolTraceMsg.gen((byte) 2, cellInfoDBList.get(index).earfcn, cellInfoDBList.get(index).pci, new byte[]{}));
                    Log.e("lbstation","msgsend!!"+odevice.getName());
                    CellInfo cellinfo=new CellInfo();
                    cellinfo.earfcn= Integer.parseInt(earfcn.getText().toString());
                    cellinfo.pci= Short.parseShort(pci.getText().toString());
                    if(ecgi.getText().toString().equals(CellInfo.NULL_VALUE)||ecgi.getText().toString().equals("")){
                        cellinfo.ecgi=Integer.MAX_VALUE;
                    }else{
                        cellinfo.ecgi= Integer.parseInt(ecgi.getText().toString());
                    }
                    if(tac.getText().toString().equals(CellInfo.NULL_VALUE)||tac.getText().toString().equals("")){
                        cellinfo.tai=Short.MAX_VALUE;
                    }else{
                        cellinfo.tai= Short.parseShort(tac.getText().toString());
                    }
                    boolean flag=true;
                    for (int i=0;i<cellInfoList.size();i++){
                        if (cellinfo.earfcn.equals(cellInfoList.get(i).earfcn)&&cellinfo.pci.equals(cellInfoList.get(i).pci)){
                            flag=false;
                            Log.e("aaaa",""+flag);
                            if (cellInfoList.get(i).ecgi.equals(Integer.MAX_VALUE)&&cellInfoList.get(i).tai.equals(Short.MAX_VALUE)){
                                cellInfoList.get(i).ecgi=cellinfo.ecgi;
                                cellInfoList.get(i).tai=cellinfo.tai;
                            }
                        }
                    }
//                    for (CellInfo xx:cellInfoList){
//                        if (xx.earfcn==cellinfo.earfcn&&xx.pci==cellinfo.pci){
//                            flag=false;
//                            Log.e("aaaa",""+flag);
//
//                        }
//                    }
                    if (flag){
                        Log.e("bbbbb",""+flag);
                        if (cellInfoList.size()>10){
                            cellInfoList.remove(1);
                            cellInfoList.add(cellinfo);
                        }else {
                            cellInfoList.add(cellinfo);
                        }

                    }
//                    ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
                    odevice.setCellInfo(cellinfo);
                    odevice.send(GenProtocolTraceMsg.gen((byte) 2, Integer.parseInt(earfcn.getText().toString()), Integer.parseInt(pci.getText().toString()), new byte[]{}));
                    
                }else {
                    Toast.makeText(LbsLocationActivity.this, "请选择定位小区！", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (start){
            for (MonitorDevice device:DeviceManager.getInstance().getDevices()){
                device.stopMonitor();
                device.setDeviceStatus(Status.DeviceStatus.IDLE);
            }
            locationbtn.setEnabled(true);
            start=false;
        }
        lbsCellInfoDBManager.clear();
        lbsCellInfoDBManager.add(cellInfoList);
        lbsCellInfoDBManager.closeDB();
    }

    public boolean validateCell(){
        if (earfcn.getText().toString().equals("")||pci.getText().toString().equals("")){
            return false;
        }else {
            return true;
        }
    }

    private List<CellInfo> getCellInfoList() {
        List<CellInfoDAO> cellInfoDAOList = cellInfoDBManager.listDB();
        List<CellInfo> cellInfoDBList = new ArrayList<>();
        for (CellInfoDAO dao : cellInfoDAOList) {
            CellInfo cellInfo = new CellInfo();
            cellInfo.earfcn = dao.earfcn;
            cellInfo.pci = dao.pci;
            cellInfo.tai = dao.tai;
            cellInfo.ecgi = dao.ecgi;
            cellInfoDBList.add(cellInfo);

        }
        return cellInfoDBList;
    }
    private List<CellInfo> getlbsCellInfoList() {
        List<CellInfoDAO> cellInfoDAOList = lbsCellInfoDBManager.listDB();
        List<CellInfo> cellInfoDBList = new ArrayList<>();
        for (CellInfoDAO dao : cellInfoDAOList) {
            CellInfo cellInfo = new CellInfo();
            cellInfo.earfcn = dao.earfcn;
            cellInfo.pci = dao.pci;
            cellInfo.tai = dao.tai;
            cellInfo.ecgi = dao.ecgi;
            cellInfoDBList.add(cellInfo);

        }
        return cellInfoDBList;
    }

    /**
     * for ListView
     */
    public final class ViewHolder {
        public TextView earfcn;
        public TextView pci;
        public TextView tai;
        public TextView ecgi;
        public TextView sinr;
        public TextView rsrp;
        public CheckBox choose;
    }

    static class myHandler extends Handler {
        private final WeakReference<LbsLocationActivity> mOuter;

        public myHandler(LbsLocationActivity lbsLocationActivity) {
            mOuter = new WeakReference<>(lbsLocationActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            Global.GlobalMsg globalMsg = (Global.GlobalMsg) msg.obj;
            switch (msg.what) {
                case MsgTypes.L2P_AG_UE_CAPTURE_IND_MSG_TYPE:
//                    mOuter.get().resolveUECaptureMsg(globalMsg);
                    break;
                case MsgTypes.L2P_AG_CELL_CAPTURE_IND_MSG_TYPE:
                    Log.e("LbsLocationActivity", "L2P_AG_CELL_CAPTURE_IND_MSG_TYPE captured!!!!!!!!!!!!!!!!!!!!!!!");
                    mOuter.get().resolveCellCaptureMsg(globalMsg);
                    break;
                case MsgTypes.L1_PHY_COMMEAS_IND_MSG_TYPE:
                    Log.e("LbsLocationActivity", "L1_PHY_COMMEAS_IND_MSG_TYPE captured!!!!!!!!!!!!!!!!!!!!!!!");
                    mOuter.get().resolvePhyCommeasIndMsg(globalMsg);
                    break;
                case MsgTypes.L2P_PROTOCOL_DATA_MSG_TYPE:
//                    mOuter.get().resolveProtocolMsg(globalMsg);
//                    Log.e(TAG, "L2P_PROTOCOL_DATA_MSG_TYPE captured：：：：：：：：");
                    break;
                default:
                    break;
            }
        }
    }

    private void resolveCellCaptureMsg(Global.GlobalMsg globalMsg) {
//        if (locationflag){
            MsgL2P_AG_CELL_CAPTURE_IND msg = new MsgL2P_AG_CELL_CAPTURE_IND(globalMsg.getBytes());
            long ecgi=msg.getMu32CellID()==0?Integer.MAX_VALUE:msg.getMu32CellID();
            int  tai=msg.getMu16TAC()==0?Short.MAX_VALUE:msg.getMu16TAC();
            if(ecgi==Integer.MAX_VALUE){
                Toast.makeText(LbsLocationActivity.this, "解析失败！！", Toast.LENGTH_SHORT).show();
            }
            Log.e("CELLCAPTION","ECGI"+ecgi+"--TAI"+tai);
            final MonitorDevice device=DeviceManager.getInstance().getDevice(globalMsg.getDeviceName());
            CellInfo cellInfo=device.getCellInfo();
            for (int i=0;i<cellInfoList.size();i++){
                if(cellInfoList.get(i).earfcn==cellInfo.earfcn&&cellInfoList.get(i).pci==cellInfo.pci){
                    cellInfoList.get(i).tai=Short.parseShort(tai+"");
                    cellInfoList.get(i).ecgi=Integer.parseInt(ecgi+"");
//                break;
                }
            }
            ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
            Log.e("CELLCAPTION","cellcaption!!!!!");
//            device.stopMonitor();
//            if (index<cellInfoList.size()){
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        device.setCellInfo(cell.get(index));
//                        device.send(GenProtocolTraceMsg.gen((byte) 2, cellInfoDBList.get(index).earfcn, cellInfoDBList.get(index).pci, new byte[]{}));
//                        index++;
//                    }
//                },5000);
//            }
//        }

    }

    private void resolvePhyCommeasIndMsg(Global.GlobalMsg globalMsg) {
        /*if (!needToCount) {
            return;
        }*/
        MsgL1_PHY_COMMEAS_IND msg = new MsgL1_PHY_COMMEAS_IND(globalMsg.getBytes());
        Log.e("lbstation","phycommeasmsg received!!");
        if (isCRSChType(msg.getMstL1PHYComentIndHeader().getMu32MeasSelect())) {
            MsgCRS_RSRPQI_INFO crs_rsrpqi_info = new MsgCRS_RSRPQI_INFO(
                    MsgSendHelper.getSubByteArray(globalMsg.getBytes(), MsgL1_PHY_COMMEAS_IND.byteArrayLen, MsgCRS_RSRPQI_INFO.byteArrayLen));
//            if (cellRSRPMap.get(globalMsg.getDeviceName()) != null) {
//                cellRSRPMap.get(globalMsg.getDeviceName()).add(crs_rsrpqi_info.getMstCrs0RsrpqiInfo().getMs16CRS_RP() * 0.125F);
//            }
            if (globalMsg.getDeviceName() != null&&DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).getCellInfo()!=null) {
                DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).getCellInfo().rsrp = crs_rsrpqi_info.getMstCrs0RsrpqiInfo().getMs16CRS_RP() * 0.125F;
            }
            Float x=crs_rsrpqi_info.getMstCrs0RsrpqiInfo().getMs16CRS_RP() * 0.125F;
            float y=(120+x)*1.5F+10;
            progressBar.setProgress((int)y);
        }
    }
    private boolean isCRSChType(long type) {
        return (type & 0x2000) == 0x2000;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cellInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.cell_monitor_list_item, null);
                holder = new ViewHolder();
                holder.earfcn = (TextView) convertView.findViewById(R.id.cell_monitor_item_earfcn);
                holder.pci = (TextView) convertView.findViewById(R.id.cell_monitor_item_pci);
                holder.tai = (TextView) convertView.findViewById(R.id.cell_monitor_item_tai);
                holder.ecgi = (TextView) convertView.findViewById(R.id.cell_monitor_item_ecgi);
                holder.sinr = (TextView) convertView.findViewById(R.id.cell_monitor_item_sinr);
                holder.rsrp = (TextView) convertView.findViewById(R.id.cell_monitor_item_rsrp);
                holder.choose = (CheckBox) convertView.findViewById(R.id.cell_monitor_item_choose);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Integer earfcn = cellInfoList.get(position).earfcn;
            if (earfcn == Integer.MAX_VALUE) {
                holder.earfcn.setText(CellInfo.NULL_VALUE);
            } else {
                holder.earfcn.setText("" + earfcn);
            }

            final Short pci = cellInfoList.get(position).pci;
            if (pci == Short.MAX_VALUE) {
                holder.pci.setText(CellInfo.NULL_VALUE);
            } else {
                holder.pci.setText("" + pci);
            }

            final Short tai = cellInfoList.get(position).tai;
            if (tai == Short.MAX_VALUE) {
                holder.tai.setText(CellInfo.NULL_VALUE);
            } else if (tai >= 0) {
                holder.tai.setText("" + tai);
            } else if (tai < 0) {
                int utai = tai & 0x0000FFFF;
                holder.tai.setText("" + utai);
            }

            final Integer ecgi = cellInfoList.get(position).ecgi;
            if (ecgi == Integer.MAX_VALUE) {
                holder.ecgi.setText(CellInfo.NULL_VALUE);
            } else if (ecgi >= 0) {
                holder.ecgi.setText("" + ecgi);
            } else if (ecgi < 0) {
                long uecgi = ecgi & 0x00000000ffffffff;
                holder.ecgi.setText("" + uecgi);
            }
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumIntegerDigits(3);
            nf.setMaximumFractionDigits(1);


            final Float sinr = cellInfoList.get(position).sinr;
            if (!Float.isNaN(sinr)) {
                holder.sinr.setText(nf.format(sinr));
            } else {
                holder.sinr.setText(CellInfo.NULL_VALUE);
            }
            final Float rsrp = cellInfoList.get(position).rsrp;
            if (!Float.isNaN(rsrp)) {
                holder.rsrp.setText(nf.format(rsrp));
            } else {
                holder.rsrp.setText(CellInfo.NULL_VALUE);
            }

//            holder.choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    CellInfo info = cellInfoDBList.get(position);
//                    info.isChecked = isChecked;
//
//                    if (isChecked) {
//                        monitorCellSet.add(info);
//                    } else {
//                        monitorCellSet.remove(info);
//                    }
//                }
//            });
//            holder.choose.setChecked(monitorCellSet.contains(cellInfoList.get(position)));

            return convertView;
        }
    }

    public  void testpro(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                progressBar.setProgress((int)(Math.random()*100));
            }
        },1000,1000);
    }

}
