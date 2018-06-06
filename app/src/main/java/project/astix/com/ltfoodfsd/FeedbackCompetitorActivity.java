package project.astix.com.ltfoodfsd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FeedbackCompetitorActivity extends BaseActivity
{
    LinearLayout ll_parent;
    LinkedHashMap<String,String> hmapPrdctIdAndName=new LinkedHashMap<>();
    LinkedHashMap<String,ArrayList<String>> hmapCatIdAndPrdctId=new LinkedHashMap<>();
    LinkedHashMap<String,String> hmapCatIdAndDesc=new LinkedHashMap<>();

    LinkedHashMap<String,String> hmapSavedCompetitrData=new LinkedHashMap<>();
    String[] prdctIDArray={"13","83","98","99","55"};
    String[] prdctNameArray={"Indiagate","Kohinoor","Rajdhani","Aahaar","Other"};
    String[] catIDArray={"1","1","1","2","2"};
    String[] catDescArray={"Rice","Oil"};
    
    Button btn_save;
    DBAdapterKenya dbengine=new DBAdapterKenya(FeedbackCompetitorActivity.this);
    public String StoreID,ComeFrom;
    public String selStoreName,imei,date,pickerDate,flgFromFeedBackOrLastVisit;
    int bck=0;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_competitor);
        
        Intent intent=getIntent();
        StoreID=intent.getStringExtra("storeID");
        selStoreName = intent.getStringExtra("SN");
        imei = intent.getStringExtra("imei");
        date = intent.getStringExtra("userdate");
        pickerDate= intent.getStringExtra("pickerDate");
        bck = intent.getIntExtra("bck", 1);
        flgFromFeedBackOrLastVisit= intent.getStringExtra("flgFromFeedBackOrLastVisit");
        ComeFrom=intent.getStringExtra("ComeFrom");

        ll_parent=(LinearLayout) findViewById(R.id.ll_parent);
        btn_save= (Button) findViewById(R.id.btn_save);

        //putDataInHashmap();
        getDataFromDatabase();
        createViews();
        saveBtnWorking();
        backBtnWorking();
    }

   /* void putDataInHashmap()
    {
        for(int i=0;i<prdctIDArray.length;i++)
        {
            hmapPrdctIdAndName.put(prdctIDArray[i],prdctNameArray[i]);
        }

        String[] prdCatORiceId={"13","83","98"};
        String[] prdCatOilId={"99","55"};

        hmapCatIdAndPrdctId.put("1",prdCatORiceId);
        hmapCatIdAndPrdctId.put("2",prdCatOilId);

        hmapCatIdAndDesc.put("1","Rice");
        hmapCatIdAndDesc.put("2","Oil");
    }*/

    void getDataFromDatabase()
    {
        ArrayList<LinkedHashMap> list_fns=dbengine.getFeedbckCompMstrDetails();
        hmapCatIdAndPrdctId=list_fns.get(0); //catID and ProdID mapping
        hmapPrdctIdAndName=list_fns.get(1); //compID and compDesc
        hmapCatIdAndDesc=list_fns.get(2); //catID and catDesc

        hmapSavedCompetitrData=dbengine.fetchCompetitorData(StoreID);
    }

    void saveBtnWorking()
    {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alert=new AlertDialog.Builder(FeedbackCompetitorActivity.this);
                alert.setTitle("Alert");
                alert.setMessage("Do you want to save changes?");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dbengine.deletetblFeedbackCompetitrData(StoreID);
                        if(hmapSavedCompetitrData != null && hmapSavedCompetitrData.size()>0)
                        {
                            dbengine.open();
                            for(Map.Entry<String,String> entry:hmapSavedCompetitrData.entrySet())
                            {
                                String prdctID=entry.getKey();
                                String catID=entry.getValue().split(Pattern.quote("^"))[0];
                                String prdctDesc=entry.getValue().split(Pattern.quote("^"))[1];

                                //key=compID, value=compName
                                dbengine.savetblFeedbackCompetitr(StoreID,prdctID,prdctDesc,catID,"1");//1 here is Sstat
                                System.out.println("SAVING.."+StoreID+"--"+prdctID+"--"+prdctDesc+"--"+catID);
                            }
                            dbengine.close();
                        }
                        dialog.dismiss();

                        int flgcheckStoreHealthRelationship=dbengine.fncheckStoreHealthRelationship(StoreID);

                        int  ISNewStore=dbengine.fncheckStoreIsNewOrOld(StoreID);
                        int flgHasBussiness=dbengine.fncheckStoreHasBussiness(StoreID);
                        if(flgcheckStoreHealthRelationship==1 && ISNewStore==0 && flgHasBussiness==1)
                        {
                            Intent intent = new Intent(FeedbackCompetitorActivity.this, LastVisitDetail_AllButton.class);
                            intent.putExtra("storeID", StoreID);
                            intent.putExtra("SN", selStoreName);
                            intent.putExtra("imei", imei);
                            intent.putExtra("userdate", date);
                            intent.putExtra("pickerDate", pickerDate);
                            intent.putExtra("bck", 1);

                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                           //if(flgHasBussiness==1)
                               if(false)
                           {
                               Intent intent = new Intent(FeedbackCompetitorActivity.this, HealthRelationshipActivity.class);
                               intent.putExtra("storeID", StoreID);
                               intent.putExtra("SN", selStoreName);
                               intent.putExtra("imei", imei);
                               intent.putExtra("userdate", date);
                               intent.putExtra("pickerDate", pickerDate);
                               intent.putExtra("bck", 1);
                               intent.putExtra("HealthRelationPageToRedirect", "0");//Where to Send LAst Visit Page=1, Store List =1
                               startActivity(intent);
                               finish();
                           }
                           else
                           {
                               Intent intent = new Intent(FeedbackCompetitorActivity.this, LastVisitDetail_AllButton.class);
                               intent.putExtra("storeID", StoreID);
                               intent.putExtra("SN", selStoreName);
                               intent.putExtra("imei", imei);
                               intent.putExtra("userdate", date);
                               intent.putExtra("pickerDate", pickerDate);
                               intent.putExtra("bck", 1);

                               startActivity(intent);
                               finish();
                           }

                        }
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog=alert.create();
                dialog.show();
            }
        });
    }

    void createViews()
    {
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        for(Map.Entry<String,String> entry:hmapCatIdAndDesc.entrySet())
        {
            View view_template = inflater1.inflate(R.layout.template_feedbckcmp, null);

            TextView txt_catName= (TextView) view_template.findViewById(R.id.txt_catName);
            txt_catName.setText(entry.getValue());
            
            final LinearLayout ll_CatTempltParent= (LinearLayout) view_template.findViewById(R.id.ll_CatTempltParent);
            ll_CatTempltParent.setTag(entry.getKey()+"_ll"); //tag- catID_ll

            ArrayList<String> list_prdctID=hmapCatIdAndPrdctId.get(entry.getKey());
            for(int i=0;i<list_prdctID.size();i++)
            {
                String prdctDesc=hmapPrdctIdAndName.get(list_prdctID.get(i));

                View view_row= inflater1.inflate(R.layout.custom_row, null);

                CheckBox cb_CompBox= (CheckBox) view_row.findViewById(R.id.cb_CompBox);
                cb_CompBox.setTag(entry.getKey()+"_"+list_prdctID.get(i)+"_cb");//tag- catID_prdctID_cb

                TextView txt_CompName= (TextView) view_row.findViewById(R.id.txt_CompName);
                txt_CompName.setTag(entry.getKey()+"_"+list_prdctID.get(i)+"_text"); //tag- catID_prdctID_text
                txt_CompName.setText(prdctDesc);

                if(hmapSavedCompetitrData != null && hmapSavedCompetitrData.size()>0)
                {
                    if(hmapSavedCompetitrData.containsKey(list_prdctID.get(i)))
                    {
                        cb_CompBox.setChecked(true);
                    }
                    else
                    {
                        cb_CompBox.setChecked(false);
                    }
                }

                cb_CompBox.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String catID=v.getTag().toString().trim().split(Pattern.quote("_"))[0];
                        String prdctID=v.getTag().toString().trim().split(Pattern.quote("_"))[1];

                        String prdctName="";

                        TextView compText= (TextView) ll_parent.findViewWithTag(catID+"_"+prdctID+"_text");
                        if(compText != null)
                        {
                            prdctName=compText.getText().toString().trim();
                        }

                        CheckBox cb= (CheckBox) v;
                        if(cb.isChecked())
                        {
                            hmapSavedCompetitrData.put(prdctID,catID+"^"+prdctName);
                        }
                        else
                        {
                            hmapSavedCompetitrData.remove(prdctID);
                        }
                    }
                });

                ll_CatTempltParent.addView(view_row);
            }

            ll_parent.addView(view_template);
        }
    }

    void backBtnWorking()
    {
        img_back= (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


if(ComeFrom.equals("1"))
{
    Intent intent=new Intent(FeedbackCompetitorActivity.this,LastVisitDetail_AllButton.class);
    intent.putExtra("storeID", StoreID);
    intent.putExtra("SN", selStoreName);
    intent.putExtra("imei", imei);
    intent.putExtra("userdate", date);
    intent.putExtra("pickerDate", pickerDate);
    intent.putExtra("bck", 1);
    intent.putExtra("flgFromFeedBackOrLastVisit", flgFromFeedBackOrLastVisit);
    startActivity(intent);
    finish();
}
else
{
    Intent intent=new Intent(FeedbackCompetitorActivity.this,SpecialRemarksActivity.class);
    intent.putExtra("storeID", StoreID);
    intent.putExtra("SN", selStoreName);
    intent.putExtra("imei", imei);
    intent.putExtra("userdate", date);
    intent.putExtra("pickerDate", pickerDate);
    intent.putExtra("bck", 1);
    intent.putExtra("flgFromFeedBackOrLastVisit", flgFromFeedBackOrLastVisit);
    startActivity(intent);
    finish();
}


            }
        });
    }

}
