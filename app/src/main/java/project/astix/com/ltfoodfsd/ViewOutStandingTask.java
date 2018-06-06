package project.astix.com.ltfoodfsd;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewOutStandingTask extends BaseActivity {

    LinkedHashMap<String, ArrayList<String>> lnkdHmapAddedStore=new LinkedHashMap<String, ArrayList<String>>();
    DBAdapterKenya dbHelper;
    LinearLayout ll_Outstandings;
    View viewAddedStore;
    public String userDate;

    public String pickerDate;
    public String imei;
    public String rID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstandings);

        dbHelper=new DBAdapterKenya(ViewOutStandingTask.this);
       /* Intent getStorei = getIntent();
        if(getStorei !=null)
        {
            imei = getStorei.getStringExtra("imei").trim();
            pickerDate = getStorei.getStringExtra("pickerDate").trim();
            userDate = getStorei.getStringExtra("userDate");
            rID = getStorei.getStringExtra("rID");
        }*/
        getAllVisitPlansFromDataBase();
        intializeFields();

    }
    private void intializeFields() {

        ll_Outstandings=(LinearLayout) findViewById(R.id.ll_Outstandings);
        ImageView img_back=(ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                finish();
            }
        });

        showOutletAdded();


    }
    private void showOutletAdded()
    {
        if(lnkdHmapAddedStore!=null)
        {
            if(lnkdHmapAddedStore.size()>0)
            {
                LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewAddedStore=inflater.inflate(R.layout.custom_view_outstandingtasks, null);

                RelativeLayout rlOutltActivity= (RelativeLayout) viewAddedStore.findViewById(R.id.rlOutltActivity);
                ImageView imgVw_dtlOutlet=(ImageView) viewAddedStore.findViewById(R.id.imgVw_dtlOutlet);
                TextView tvOutletName=(TextView) viewAddedStore.findViewById(R.id.tvOutletName);
                LinearLayout ll_RowParent= (LinearLayout) viewAddedStore.findViewById(R.id.ll_RowParent);

                for(Entry<String, ArrayList<String>> entry:lnkdHmapAddedStore.entrySet())
                {
                    View viewRow=inflater.inflate(R.layout.custom_row_visit, null);

                    TextView outstnding_StrName=(TextView) viewRow.findViewById(R.id.visitPlan_StrName);
                    TextView outstnding_Desc=(TextView) viewRow.findViewById(R.id.visitPlan_Desc);
                    TextView outstndingTask_Date=(TextView) viewRow.findViewById(R.id.visitPlan_Date);

                    String outstandingID=entry.getKey();
                    ArrayList<String> outStndingData=entry.getValue();

                    if(outStndingData != null && outStndingData.size() >0)
                    {
                        outstnding_StrName.setText(outStndingData.get(0));
                        outstnding_Desc.setText(outStndingData.get(1));
                        outstndingTask_Date.setText(outStndingData.get(2));
                    }

                    ll_RowParent.addView(viewRow);

                    //tvOutletName.setText(entry.getValue().toString());
                    //rlOutltActivity.setTag(entry.getKey());
                   /* rlOutltActivity.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(ViewVisitPlan.this, AddNewStore_DynamicSectionWise.class);
                            //Intent intent = new Intent(StoreSelection.this, Add_New_Store_NewFormat.class);
                            //Intent intent = new Intent(StoreSelection.this, Add_New_Store.class);
                            intent.putExtra("activityFrom", "ViewAddedStore");
                            intent.putExtra("outletId", v.getTag().toString());
                            intent.putExtra("userdate", userDate);
                            intent.putExtra("pickerDate", pickerDate);
                            intent.putExtra("imei", imei);
                            intent.putExtra("rID", rID);
                            startActivity(intent);
                            finish();

                        }
                    });*/
                 }

                ll_Outstandings.addView(viewAddedStore);
            }
        else
            {
                LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                TextView tv=new TextView(this);
                tv.setLayoutParams(lparams);
                tv.setText("No Outstanding Tasks at present.");

                ll_Outstandings.addView(tv);
            }

        }
        else
        {
            LayoutParams lparams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            TextView tv=new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText("test");

            ll_Outstandings.addView(tv);
        }

    }
    private void getAllVisitPlansFromDataBase() {

        lnkdHmapAddedStore=dbHelper.getAllOutStandingTasks();

    }


}
