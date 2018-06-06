package project.astix.com.ltfoodfsd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.astix.Common.CommonInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class ViewQuotation extends BaseActivity {

    LinkedHashMap<String, String> hmapDistinctSalesQuotePersonMeetMstr=new LinkedHashMap<String, String>();
    DBAdapterKenya dbengine = new DBAdapterKenya(this);
    public TableLayout tbl4_dyntable_dynprodtableQuatation;
    public TableLayout tbl4_dyntable_dynprodtableQuatationAprvd;

    public String storeID;
    public String imei;
    public String date;
    public String pickerDate;
    public Double currLon;
    public Double currLat;
    public String selStoreName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quotation);
        Intent passedvals = getIntent();
        imei = passedvals.getStringExtra("imei");
        date = passedvals.getStringExtra("userdate");
        pickerDate= passedvals.getStringExtra("pickerDate");
        selStoreName = passedvals.getStringExtra("SN");
        storeID = passedvals.getStringExtra("storeID");

        ImageView prevP2= (ImageView) findViewById(R.id.prevP2);
        prevP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ide=new Intent(ViewQuotation.this,LastVisitDetail_AllButton.class);
                ide.putExtra("SN", selStoreName);
                ide.putExtra("storeID", storeID);
                ide.putExtra("imei", imei);
                ide.putExtra("userdate", date);
                ide.putExtra("pickerDate", pickerDate);
                ide.putExtra("back", 1);
                startActivity(ide);
                finish();
            }
        });
        setQuotationList();
        setQuotationListAprvd();
    }


    public void setQuotationList()
    {

        hmapDistinctSalesQuotePersonMeetMstr=dbengine.fngetDistinctSalesQuotePersonMeetMstr(storeID,0);
       // hmapDistinctSalesQuotePersonMeetMstr.put("2","29473872377~1234^56^Approval Pending^23-Oct-2017");

        //hmapDistinctSalesQuotePersonMeetMstr.put("1", "Sunil^2^New^18-Oct-2016");
      /*  if(hmapDistinctSalesQuotePersonMeetMstr.size()<=0)
        {

            TextView txt_Quatation = (TextView)findViewById(R.id.txt_Quatation);
            txt_Quatation.setVisibility(View.GONE);
        }*/

        if(hmapDistinctSalesQuotePersonMeetMstr.size()>0)
        {

            TextView txt_Quatation_Value = (TextView)findViewById(R.id.txt_Quatation_Value);
            txt_Quatation_Value.setVisibility(View.GONE);
            RelativeLayout relLayout_for_Quatation = (RelativeLayout)findViewById(R.id.relLayout_for_Quatation);
            relLayout_for_Quatation.setVisibility(View.VISIBLE);

            tbl4_dyntable_dynprodtableQuatation = (TableLayout) findViewById(R.id.dynprodtableQuatation);


            //String[] prductId;
            String[] SalesQuoteId=changeHmapToArrayKey(hmapDistinctSalesQuotePersonMeetMstr);

            String[] SalesQuoteIdAllData=changeHmapToArrayValue(hmapDistinctSalesQuotePersonMeetMstr);


            LayoutInflater inflater2 = getLayoutInflater();

            for (int current2 = 0; current2 <= (hmapDistinctSalesQuotePersonMeetMstr.size() - 1); current2++)
            {

                final TableRow row2 = (TableRow)inflater2.inflate(R.layout.trans_row4_second, tbl4_dyntable_dynprodtableQuatation , false);


                StringTokenizer token = new StringTokenizer(String.valueOf(SalesQuoteIdAllData[current2]), "^");

                String SalesQuoteCode=token.nextToken().toString().trim();
                String SalesQuotePrcsId=token.nextToken().toString().trim();
                String SalesQuotePrcs=token.nextToken().toString().trim();
                String SalesQuoteDate=token.nextToken().toString().trim();


                TextView tv1 = (TextView)row2.findViewById(R.id.tvSalesQuoteCode);
                TextView tv2 = (TextView)row2.findViewById(R.id.tvSalesQuotePrcs);
                TextView tv3 = (TextView)row2.findViewById(R.id.tvSalesQuoteDate);

                tv1.setTag(SalesQuoteId[current2]);
                row2.setTag(SalesQuoteId[current2]);



                tv1.setTextColor(Color.parseColor("#303F9F"));
                tv1.setTypeface(null, Typeface.BOLD);
                String mystring=SalesQuoteCode;
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                tv1.setText(content);


                tv2.setText(SalesQuotePrcs);

                //surbhi
                String formattedDate=ChangeDateFormat(SalesQuoteDate);
                System.out.println("FORMATTED DATE: "+formattedDate);
                tv3.setText(formattedDate);

                row2.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String prcID=dbengine.fnGettPrcIDBasedOnQuotationId(v.getTag().toString().trim());

                        Intent intents=new Intent(ViewQuotation.this,QuotationActivity.class);
                        intents.putExtra("quatationFlag", "UPDATE");  //Update
                        intents.putExtra("SalesQuoteId", v.getTag().toString().trim());
                        intents.putExtra("storeID", storeID);
                        intents.putExtra("SN", selStoreName);
                        intents.putExtra("imei", imei);
                        intents.putExtra("userdate", date);
                        intents.putExtra("pickerDate", pickerDate);
                        intents.putExtra("FROM", "VIEWQUOTATION");
                        intents.putExtra("prcID", prcID);//prcID); // you have to send prcid
                        CommonInfo.SalesQuoteId="BLANK";
                        CommonInfo.prcID="NULL";
                        CommonInfo.newQuottionID="NULL";
                        CommonInfo.globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

                        startActivity(intents);
                        finish();
                    }
                });



                tbl4_dyntable_dynprodtableQuatation.addView(row2);
            }

        }
        else
        {
            TextView txt_Quatation_Value = (TextView)findViewById(R.id.txt_Quatation_Value);
            txt_Quatation_Value.setVisibility(View.VISIBLE);
				/*ScrollView QuatationListScroll = (ScrollView)findViewById(R.id.QuatationListScroll);
				QuatationListScroll.setVisibility(View.GONE);*/
            RelativeLayout relLayout_for_Quatation = (RelativeLayout)findViewById(R.id.relLayout_for_Quatation);
            relLayout_for_Quatation.setVisibility(View.GONE);
        }


    }


    public String ChangeDateFormat(String SalesQuoteDate)
    {
        String NewDate="NA";
        try
        {
            String date=SalesQuoteDate.split(Pattern.quote("-"))[0];
            String month=SalesQuoteDate.split(Pattern.quote("-"))[1];
            String year=SalesQuoteDate.split(Pattern.quote("-"))[2];

            String ChngdMonth=month.substring(0, 3);
            NewDate=date+"-"+ChngdMonth+"-"+year;
        }
        catch(Exception e)
        {
            //System.out.println("ERROR IN CHNGE DATE FORMAT() :"+e);
        }
        return NewDate;
    }



    public void setQuotationListAprvd()
    {
//Select SalesQuoteId,SalesQuoteCode,SalesQuotePrcsId,SalesQuotePrcs,SalesQuoteDate
        LinkedHashMap<String,String> hmapDistinctSalesQuotePersonMeetMstrAprvd=new LinkedHashMap<String,String>();
        hmapDistinctSalesQuotePersonMeetMstrAprvd=dbengine.fngetDistinctSalesQuotePersonMeetMstr(storeID,5);
      /*  hmapDistinctSalesQuotePersonMeetMstrAprvd.put("1","5323244324344~3566563^56^Approved^22-Oct-2017");
        hmapDistinctSalesQuotePersonMeetMstrAprvd.put("2","346675426563~46235632^56^Approved^23-Oct-2017");
        hmapDistinctSalesQuotePersonMeetMstrAprvd.put("3","127246746273~23123211^56^Approved^24-Oct-2017");*/

        //hmapDistinctSalesQuotePersonMeetMstr.put("1", "Sunil^2^New^18-Oct-2016");
     /*   if(hmapDistinctSalesQuotePersonMeetMstrAprvd.size()<=0)
        {

            TextView txt_Quatation = (TextView)findViewById(R.id.txt_Quatation);
            txt_Quatation.setVisibility(View.GONE);
        }*/

        if(hmapDistinctSalesQuotePersonMeetMstrAprvd.size()>0)
        {

            TextView txt_Quatation_Value = (TextView)findViewById(R.id.txt_Quatation_Value);
            txt_Quatation_Value.setVisibility(View.GONE);
            RelativeLayout relLayout_for_Quatation = (RelativeLayout)findViewById(R.id.relLayout_for_Quatation);
            relLayout_for_Quatation.setVisibility(View.VISIBLE);

            tbl4_dyntable_dynprodtableQuatationAprvd = (TableLayout) findViewById(R.id.dynprodtableQuatationAprvd);


            //String[] prductId;
            String[] SalesQuoteId=changeHmapToArrayKey(hmapDistinctSalesQuotePersonMeetMstrAprvd);

            String[] SalesQuoteIdAllData=changeHmapToArrayValue(hmapDistinctSalesQuotePersonMeetMstrAprvd);


            LayoutInflater inflater2 = getLayoutInflater();

            for (int current2 = 0; current2 <= (hmapDistinctSalesQuotePersonMeetMstrAprvd.size() - 1); current2++)
            {

                final TableRow row2 = (TableRow)inflater2.inflate(R.layout.trans_row4_second, tbl4_dyntable_dynprodtableQuatationAprvd , false);


                StringTokenizer token = new StringTokenizer(String.valueOf(SalesQuoteIdAllData[current2]), "^");

                String SalesQuoteCode=token.nextToken().toString().trim();
                String SalesQuotePrcsId=token.nextToken().toString().trim();
                String SalesQuotePrcs=token.nextToken().toString().trim();
                String SalesQuoteDate=token.nextToken().toString().trim();


                TextView tv1 = (TextView)row2.findViewById(R.id.tvSalesQuoteCode);
                TextView tv2 = (TextView)row2.findViewById(R.id.tvSalesQuotePrcs);
                TextView tv3 = (TextView)row2.findViewById(R.id.tvSalesQuoteDate);

                tv1.setTag(SalesQuoteId[current2]);
                row2.setTag(SalesQuoteId[current2]);



                tv1.setTextColor(Color.parseColor("#303F9F"));
                tv1.setTypeface(null, Typeface.BOLD);
                String mystring=SalesQuoteCode;
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                tv1.setText(SalesQuoteCode);


                tv2.setText(SalesQuotePrcs);

                //surbhi
              /*  String formattedDate=ChangeDateFormat(SalesQuoteDate);
                System.out.println("FORMATTED DATE: "+formattedDate);*/
                tv3.setText(SalesQuoteDate);

                row2.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String prcID=dbengine.fnGettPrcIDBasedOnQuotationId(v.getTag().toString().trim());

                       /* Intent intents=new Intent(ViewQuotation.this,QuotationActivity.class);
                        intents.putExtra("quatationFlag", "UPDATE");  //Update
                        intents.putExtra("SalesQuoteId", v.getTag().toString().trim());
                        intents.putExtra("storeID", storeID);
                        intents.putExtra("SN", selStoreName);
                        intents.putExtra("imei", imei);
                        intents.putExtra("userdate", date);
                        intents.putExtra("pickerDate", pickerDate);
                        intents.putExtra("prcID", prcID);//prcID); // you have to send prcid
                        CommonInfo.SalesQuoteId="BLANK";
                        CommonInfo.prcID="NULL";
                        CommonInfo.newQuottionID="NULL";
                        CommonInfo.globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

                        startActivity(intents);
                        finish();*/
                    }
                });



                tbl4_dyntable_dynprodtableQuatationAprvd.addView(row2);
            }

        }
        else
        {
            TextView txt_Quatation_Value = (TextView)findViewById(R.id.txt_Quatation_Value);
            txt_Quatation_Value.setVisibility(View.VISIBLE);
				/*ScrollView QuatationListScroll = (ScrollView)findViewById(R.id.QuatationListScroll);
				QuatationListScroll.setVisibility(View.GONE);*/
            RelativeLayout relLayout_for_Quatation = (RelativeLayout)findViewById(R.id.relLayout_for_QuatationAprvd);
            relLayout_for_Quatation.setVisibility(View.GONE);
        }


    }
}
