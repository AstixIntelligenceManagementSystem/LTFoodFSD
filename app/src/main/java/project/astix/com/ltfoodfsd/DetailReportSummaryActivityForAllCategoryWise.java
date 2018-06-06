package project.astix.com.ltfoodfsd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.astix.Common.CommonInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Sunil on 11/20/2017.
 */

public class DetailReportSummaryActivityForAllCategoryWise extends BaseActivity
{

    String date_value="";
    String imei="";
    String rID;
    String pickerDate="";
    public String fDate;
    TableLayout tbl_inflate;
    DBAdapterKenya dbengine = new DBAdapterKenya(this);
   LinkedHashMap<String, String> hmapSummaryDataNew=new  LinkedHashMap<String, String>();
    TextView today_tv,MeasureName;

    String getTagVal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_categorywise);
        today_tv =(TextView)findViewById(R.id.today_tv);
        MeasureName=(TextView)findViewById(R.id.MeasureName);


        Intent extras = getIntent();
        if(extras !=null)
        {
            date_value=extras.getStringExtra("userDate");
            pickerDate= extras.getStringExtra("pickerDate");
            imei=extras.getStringExtra("imei");
            rID=extras.getStringExtra("rID");
            getTagVal=extras.getStringExtra("getTagVal");

            MeasureName.setText(getTagVal.split(Pattern.quote("_"))[2]);

        }

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tManager.getDeviceId();

        if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
        {
            imei = tManager.getDeviceId();
            CommonInfo.imei=imei;
        }
        else
        {
            imei=CommonInfo.imei.trim();
        }

        Date date1=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        fDate = sdf.format(date1).toString().trim();


        TextView txtSalessumuryDate=(TextView)findViewById(R.id.txtSalessumuryDate);
        txtSalessumuryDate.setText("Summary as on :"+fDate);

        setUpVariable();
        getDataFromDatabase();

    }

    public void setUpVariable()
    {


        ImageView but_back=(ImageView)findViewById(R.id.backbutton);
        but_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                finish();


            }
        });


    }

    public void getDataFromDatabase()
    {
        tbl_inflate= (TableLayout) findViewById(R.id.tbl_inflate);

        hmapSummaryDataNew=dbengine.fetch_tblAllSummaryDayCategoryWise(getTagVal.split(Pattern.quote("_"))[0]);

        //System.out.println("CountNew " +tblRowCount.length);
        //LinkedHashMap<String, LinkedHashMap<String, String>> hmapSummaryDataNew=new LinkedHashMap<String, LinkedHashMap<String, String>>();


            for (Map.Entry<String, String> entry1 : hmapSummaryDataNew.entrySet())
            {

                String key1 = entry1.getKey();

                String value = entry1.getValue();


                String TodaysSummary=value.split(Pattern.quote("^"))[0];
                String MTDSummary=value.split(Pattern.quote("^"))[1];

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.summary_row_category_inflate, null);



                TextView measure_val=(TextView) view.findViewById(R.id.measure_val);
                measure_val.setGravity(Gravity.CENTER);

                TextView txtValueAchievedToday=(TextView) view.findViewById(R.id.txtValueAchievedToday);
               // TextView txtValueAchievedMTD=(TextView) view.findViewById(R.id.txtValueAchievedMTD);

                measure_val.setText(key1);

                if(getTagVal.split(Pattern.quote("_"))[1].equals("1"))
                {
                    txtValueAchievedToday.setText(TodaysSummary);
                }
                else  if(getTagVal.split(Pattern.quote("_"))[1].equals("2"))
                {
                    txtValueAchievedToday.setText(MTDSummary);
                    today_tv.setText("MTD Summary");
                }



                tbl_inflate.addView(view);


            }












    }
}
