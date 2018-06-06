package project.astix.com.ltfoodfsd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;

public class SpecialRemarksActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    ImageView img_callToSchedule, img_expectedCallBy;
    TextView txt_callToSchedule, txt_expectedCallBy;
    SimpleDateFormat sdf;
    Button btn_save,btn_Competitor;
    EditText txtComments;
    String checkVal="NA";
    public static String VisitStartTS="NA";
String OrderPDAID;
    CheckBox cbArrangeFactoryVisit,cbSeniorVisit,cbQualityVisit,cbColdLead,cbCallToschedule,cbExpectedCallBy;
    String StoreId;
    public String selStoreName,imei,date,pickerDate,flgFromFeedBackOrLastVisit;
    int bck=0;
    LinkedHashMap<String,LinkedHashMap> hmapSave=new LinkedHashMap<>();
    LinkedHashMap<String,String> hmapRemarkData=new LinkedHashMap<>();

    ArrayList<String> arrListRemark=new ArrayList();
    DBAdapterKenya dbengine = new DBAdapterKenya(this);

    String cbArrangeFactoryVisitCheckVal="0",cbSeniorVisitCheckVal="0",cbQualityVisitCheckVal="0",cbColdLeadCheckVal="0",cbCallToscheduleCheckVal="0",cbExpectedCallByCheckVal="0";
    String ScheduleCall = "NA", ExpectedCall = "NA", FactoryVisit = "0",SeniorVisit="0",QualityResourceVisit="0",ColdLead="0",Others="NA";

    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_remarks_new);

        initializeAll();
/*
        Date date1 = new Date();
        sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);

        String fDate = sdf.format(date1).toString().trim();
*/

        txt_callToSchedule.setText("NA");
        txt_expectedCallBy.setText("NA");



       // StoreId=genTempID();

        Intent intent=getIntent();
        StoreId=intent.getStringExtra("storeID");
        selStoreName = intent.getStringExtra("SN");
        imei = intent.getStringExtra("imei");
        date = intent.getStringExtra("userdate");
        pickerDate= intent.getStringExtra("pickerDate");
        bck = intent.getIntExtra("bck", 1);
        flgFromFeedBackOrLastVisit= intent.getStringExtra("flgFromFeedBackOrLastVisit");
        if(flgFromFeedBackOrLastVisit.equals("DASHBOARD")){
            OrderPDAID= intent.getStringExtra("OrderPDAID");
        }
        calClick();

        fetchSpecialRemarksActivity();


        img_callToSchedule.setEnabled(false);
        txt_callToSchedule.setEnabled(false);
        img_expectedCallBy.setEnabled(false);
        txt_expectedCallBy.setEnabled(false);


        cbCallToschedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()==true){
                    img_callToSchedule.setEnabled(true);
                    txt_callToSchedule.setEnabled(true);
                }

                if(compoundButton.isChecked()==false){
                    img_callToSchedule.setEnabled(false);
                    txt_callToSchedule.setEnabled(false);
                    txt_callToSchedule.setText("NA");

                }
            }
        });


        cbExpectedCallBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()==true){
                    img_expectedCallBy.setEnabled(true);
                    txt_expectedCallBy.setEnabled(true);
                }

                if(compoundButton.isChecked()==false){
                    img_expectedCallBy.setEnabled(false);
                    txt_expectedCallBy.setEnabled(false);
                    txt_expectedCallBy.setText("NA");
                }
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flgFromFeedBackOrLastVisit.equals("DASHBOARD")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SpecialRemarksActivity.this);
                    alertDialog.setTitle("Information");

                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Have you saved your data before going back ");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();


                            Intent intent=new Intent(SpecialRemarksActivity.this,LastVisitDetail_AllButton.class);
                            intent.putExtra("storeID", StoreId);
                            intent.putExtra("SN", selStoreName);
                            intent.putExtra("imei", imei);
                            intent.putExtra("userdate", date);
                            intent.putExtra("pickerDate", pickerDate);
                            intent.putExtra("bck", 1);
                            intent.putExtra("OrderPDAID", OrderPDAID);
                            startActivity(intent);
                            finish();
                           /* dbengine.open();
                            String rID=dbengine.GetActiveRouteID();
                            dbengine.close();
                            Intent prevP2 = new Intent(SpecialRemarksActivity.this, StoreSelection.class);

                            //Location_Getting_Service.closeFlag = 0;
                            prevP2.putExtra("imei", imei);
                            prevP2.putExtra("userDate", date);
                            prevP2.putExtra("pickerDate", pickerDate);
                            prevP2.putExtra("rID", rID);
                            startActivity(prevP2);
                            finish();*/

                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
                else
                {
                    Intent intent=new Intent(SpecialRemarksActivity.this,FeedBack_Activity.class);
                    intent.putExtra("storeID", StoreId);
                    intent.putExtra("SN", selStoreName);
                    intent.putExtra("imei", imei);
                    intent.putExtra("userdate", date);
                    intent.putExtra("pickerDate", pickerDate);
                    intent.putExtra("bck", 1);

                    startActivity(intent);
                    finish();
                }

    }
});

/*
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SpecialRemarksActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
*/

        saveButton();
    }
    public String genTempID()
    {

        String cxz;
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tManager.getDeviceId();
        cxz = imei.trim();

        return cxz;
    }


    public void initializeAll(){

        img_callToSchedule = (ImageView) findViewById(R.id.img_callToSchedule);
        img_expectedCallBy = (ImageView) findViewById(R.id.img_expectedCallBy);

        txt_callToSchedule = (TextView) findViewById(R.id.txt_callToSchedule);
        txt_expectedCallBy = (TextView) findViewById(R.id.txt_expectedCallBy);
        btn_save= (Button) findViewById(R.id.btn_save);
        btn_Competitor= (Button) findViewById(R.id.btn_Competitor);
        cbArrangeFactoryVisit= (CheckBox) findViewById(R.id.cbArrangeFactoryVisit);
        cbSeniorVisit= (CheckBox) findViewById(R.id.cbSeniorVisit);
        cbQualityVisit= (CheckBox) findViewById(R.id.cbQualityVisit);
        cbColdLead= (CheckBox) findViewById(R.id.cbColdLead);

        cbCallToschedule= (CheckBox) findViewById(R.id.cbCallToschedule);
        cbExpectedCallBy= (CheckBox) findViewById(R.id.cbExpectedCallBy);

        txtComments= (EditText) findViewById(R.id.txtComments);
        img_back= (ImageView) findViewById(R.id.img_back);
    }



    public void calClick() {
        img_callToSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(SpecialRemarksActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)


                );

                datepickerdialog.showYearPickerFirst(false); //choose year first?
                datepickerdialog.setAccentColor(Color.parseColor("#9C27A0")); // custom accent color
                datepickerdialog.setTitle("Please select a date"); //dialog title
                datepickerdialog.show(getFragmentManager(), "Datepickerdialog"); //show dialog
                datepickerdialog.setMinDate(now);

                datepickerdialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String yearStringLastTwoDigits=	String.valueOf(year).substring(2);
                        year=Integer.parseInt(yearStringLastTwoDigits);
                        String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                        String mon=MONTHS[monthOfYear];

                        String date = dayOfMonth+"-"+mon+"-"+year;
                        txt_callToSchedule.setText(date);

                    }
                });


            }
        });


        img_expectedCallBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();

                DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                        (DatePickerDialog.OnDateSetListener) SpecialRemarksActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );


                datepickerdialog.showYearPickerFirst(false); //choose year first?
                datepickerdialog.setAccentColor(Color.parseColor("#9C27A0")); // custom accent color
                datepickerdialog.setTitle("Please select a date"); //dialog title
                datepickerdialog.show(getFragmentManager(), "Datepickerdialog"); //show dialog
                datepickerdialog.setMinDate(now);

                datepickerdialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String yearStringLastTwoDigits=	String.valueOf(year).substring(2);
                        year=Integer.parseInt(yearStringLastTwoDigits);
                        String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                        String mon=MONTHS[monthOfYear];

                        String date = dayOfMonth+"-"+mon+"-"+year;
                        txt_expectedCallBy.setText(date);

                    }
                });


            }
        });

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }



    public void saveButton(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hmapRemarkData!=null && hmapRemarkData.size()>0){
                    if(hmapRemarkData.containsKey(StoreId)){
                        dbengine.deleteTblSpecialRemark(StoreId);
                    }

                }
                String date_callToSchedule,date_expectedCallBy;

                String otherComment= txtComments.getText().toString().trim();

                if(TextUtils.isEmpty(otherComment)){
                    otherComment="NA";
                }

                if( cbArrangeFactoryVisit.isChecked()==true){
                    cbArrangeFactoryVisitCheckVal="1";
                }
                else {
                    cbArrangeFactoryVisitCheckVal="0";
                }


                if( cbSeniorVisit.isChecked()==true){
                    cbSeniorVisitCheckVal="1";
                }
                else {
                    cbSeniorVisitCheckVal="0";
                }

                if( cbQualityVisit.isChecked()==true){
                    cbQualityVisitCheckVal="1";
                }
                else {
                    cbQualityVisitCheckVal="0";
                }

                if( cbColdLead.isChecked()==true){
                    cbColdLeadCheckVal="1";
                }
                else {
                    cbColdLeadCheckVal="0";
                }

                if( cbCallToschedule.isChecked()==true){
                    cbCallToscheduleCheckVal="1";
                    date_callToSchedule=txt_callToSchedule.getText().toString().trim();
                    img_callToSchedule.setEnabled(true);
                }
                else {
                    cbCallToscheduleCheckVal="0";
                    date_callToSchedule="NA";
                }





                    if( cbExpectedCallBy.isChecked()==true){
                    cbExpectedCallByCheckVal="1";
                    date_expectedCallBy=txt_expectedCallBy.getText().toString().trim();
                    img_expectedCallBy.setEnabled(true);
                }
                else {
                    cbExpectedCallByCheckVal="0";
                    date_expectedCallBy="NA";
                }
                if(cbCallToschedule.isChecked()==true && date_callToSchedule.equals("NA")){

                    alertDialog("Alert","Please Select Call to Schedule Date.");

                }
               else if(cbExpectedCallBy.isChecked()==true && date_expectedCallBy.equals("NA")){

                    alertDialog("Alert","Please Select Expected Call By Date.");

                }

                else {
                    hmapRemarkData.put(StoreId,date_callToSchedule+"^"+date_expectedCallBy+"^"+cbArrangeFactoryVisitCheckVal+"^"+cbSeniorVisitCheckVal+"^"+cbQualityVisitCheckVal+"^"+cbColdLeadCheckVal+"^"+otherComment);
                    dbengine.insertSpecialRemarks(hmapRemarkData);

                }

            }
        });




        btn_Competitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if(hmapRemarkData!=null && hmapRemarkData.size()>0){
                    if(hmapRemarkData.containsKey(StoreId)){

                    }

                }*/
                dbengine.deleteTblSpecialRemark(StoreId);
                String date_callToSchedule,date_expectedCallBy;

                String otherComment= txtComments.getText().toString().trim();

                if(TextUtils.isEmpty(otherComment)){
                    otherComment="NA";
                }

                if( cbArrangeFactoryVisit.isChecked()==true){
                    cbArrangeFactoryVisitCheckVal="1";
                }
                else {
                    cbArrangeFactoryVisitCheckVal="0";
                }


                if( cbSeniorVisit.isChecked()==true){
                    cbSeniorVisitCheckVal="1";
                }
                else {
                    cbSeniorVisitCheckVal="0";
                }

                if( cbQualityVisit.isChecked()==true){
                    cbQualityVisitCheckVal="1";
                }
                else {
                    cbQualityVisitCheckVal="0";
                }

                if( cbColdLead.isChecked()==true){
                    cbColdLeadCheckVal="1";
                }
                else {
                    cbColdLeadCheckVal="0";
                }

                if( cbCallToschedule.isChecked()==true){
                    cbCallToscheduleCheckVal="1";
                    date_callToSchedule=txt_callToSchedule.getText().toString().trim();
                    img_callToSchedule.setEnabled(true);
                }
                else {
                    cbCallToscheduleCheckVal="0";
                    date_callToSchedule="NA";
                }





                if( cbExpectedCallBy.isChecked()==true){
                    cbExpectedCallByCheckVal="1";
                    date_expectedCallBy=txt_expectedCallBy.getText().toString().trim();
                    img_expectedCallBy.setEnabled(true);
                }
                else {
                    cbExpectedCallByCheckVal="0";
                    date_expectedCallBy="NA";
                }
                if(cbCallToschedule.isChecked()==true && date_callToSchedule.equals("NA")){

                    alertDialog("Alert","Please Select Call to Schedule Date.");

                }
                else if(cbExpectedCallBy.isChecked()==true && date_expectedCallBy.equals("NA")){

                    alertDialog("Alert","Please Select Expected Call By Date.");

                }

                else {
                    hmapRemarkData.put(StoreId,date_callToSchedule+"^"+date_expectedCallBy+"^"+cbArrangeFactoryVisitCheckVal+"^"+cbSeniorVisitCheckVal+"^"+cbQualityVisitCheckVal+"^"+cbColdLeadCheckVal+"^"+otherComment);
                    dbengine.insertSpecialRemarks(hmapRemarkData);



                    Intent intent=new Intent(SpecialRemarksActivity.this,FeedbackCompetitorActivity.class);
                    intent.putExtra("storeID", StoreId);
                    intent.putExtra("SN", selStoreName);
                    intent.putExtra("imei", imei);
                    intent.putExtra("userdate", date);
                    intent.putExtra("pickerDate", pickerDate);
                    intent.putExtra("bck", 1);
                    intent.putExtra("flgFromFeedBackOrLastVisit", flgFromFeedBackOrLastVisit);
                    intent.putExtra("ComeFrom", "2");
                    startActivity(intent);
                    finish();


                }

            }
        });
    }

    public void fetchSpecialRemarksActivity(){

        arrListRemark.clear();
        arrListRemark = dbengine.fetchSpecilaRemarks(StoreId);

        if(!arrListRemark.isEmpty() && arrListRemark.size()>0){
            ScheduleCall=arrListRemark.get(0);
            ExpectedCall=arrListRemark.get(1);
            FactoryVisit=arrListRemark.get(2);
            SeniorVisit=arrListRemark.get(3);
            QualityResourceVisit=arrListRemark.get(4);
            ColdLead=arrListRemark.get(5);
            Others=arrListRemark.get(6);


            System.out.println("Nitika fetch"+ColdLead);


                if(ScheduleCall.equals("0"+"^"+"NA")){
                    cbCallToschedule.setChecked(false);
                    img_callToSchedule.setEnabled(false);
                    txt_callToSchedule.setText(ScheduleCall.split(Pattern.quote("^"))[1]);
                }
                else {
                    cbCallToschedule.setChecked(true);
                    img_callToSchedule.setEnabled(true);
                    txt_callToSchedule.setText(ScheduleCall.split(Pattern.quote("^"))[1]);

                }

                if(ExpectedCall.equals("0"+"^"+"NA")){
                    cbExpectedCallBy.setChecked(false);
                    img_expectedCallBy.setEnabled(false);
                    txt_expectedCallBy.setText(ExpectedCall.split(Pattern.quote("^"))[1]);
                }

                else {
                    cbExpectedCallBy.setChecked(true);
                    img_expectedCallBy.setEnabled(true);
                    txt_expectedCallBy.setText(ExpectedCall.split(Pattern.quote("^"))[1]);
                }



                if(FactoryVisit.equals("1"+"^"+"NA")){
                    cbArrangeFactoryVisit.setChecked(true);
                }
                else {
                    cbArrangeFactoryVisit.setChecked(false);

                }




                if(SeniorVisit.equals("1"+"^"+"NA")){
                    cbSeniorVisit.setChecked(true);
                }
                else if(SeniorVisit.equals("0"+"^"+"NA")) {
                    cbSeniorVisit.setChecked(false);

                }

                if(QualityResourceVisit.equals("1"+"^"+"NA")){
                    cbQualityVisit.setChecked(true);
                }
                else if(SeniorVisit.equals("0"+"^"+"NA")) {
                    cbQualityVisit.setChecked(false);

                }

                if(ColdLead.equals("1"+"^"+"NA")){
                    cbColdLead.setChecked(true);
                }
                else if(SeniorVisit.equals("0"+"^"+"NA")) {
                    cbColdLead.setChecked(false);

                }




                if(Others.equals("0"+"^"+"NA")){
                    txtComments.setText("");
                }
                else {
                    txtComments.setText(Others.split(Pattern.quote("^"))[1]);
                }

            }



    }

    public void alertDialog(String title,String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SpecialRemarksActivity.this);
        alertDialog.setTitle(title);

        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();

    }


    public void alertDialogDone(String title,String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SpecialRemarksActivity.this);
        alertDialog.setTitle(title);

        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
            }
        });
        alertDialog.show();

    }
}
