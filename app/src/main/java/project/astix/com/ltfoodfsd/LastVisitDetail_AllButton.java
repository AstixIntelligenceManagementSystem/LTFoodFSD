package project.astix.com.ltfoodfsd;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astix.Common.CommonInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sunil on 12/31/2017.
 */

public class LastVisitDetail_AllButton extends BaseActivity implements InterfaceClass
{

    int countSubmitClicked=0;
    public LocationManager locationManager;
    public static int flgRestart=0;
    public static int flgStoreOrder=0;

    LinearLayout  ll_TakeQuatation,ll_ViewQuatation,ll_Feebback,ll_TakeOrder,ll_NextCall,ll_Competitor,
            ll_HealthRelationship,ll_DistributorSKUMap,ll_CancelVisit,ll_CloseCall;
    int CheckIfStoreExistInStoreProdcutInvoiceDetails=0;
    LinearLayout FirstRow;
    int CheckIfStoreExistInStoreProdcutPurchaseDetails=0;
    DBAdapterKenya dbengine = new DBAdapterKenya(this);
    public String storeID;
    public String imei;
    public String date;
    public String pickerDate;
    public String SN;

    public int flgfeedbackReq=0;
    public int chkIfStoreAllowedQuote=1;
    DatabaseAssistant DA = new DatabaseAssistant(this);
    public String newfullFileName;
    public String OrderPDAID="0";
    int flgHasBussiness=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_visit_all_button);
        locationManager=(LocationManager) this.getSystemService(LOCATION_SERVICE);
        DayStartActivity.flgDaySartWorking=0;
        getDataFromIntent();
        getDataFromDatabase();
        initializeView();
      // String x= javax.microedition.khronos.opengles.GL10.glGetString(GL10.GL_EXTENSIONS);

    }

    public void getDataFromDatabase()
    {
        flgfeedbackReq=dbengine.fncheckStorefeedbackReq(storeID);
        chkIfStoreAllowedQuote=dbengine.fnchkIfStoreAllowQuotation(storeID);
    }

    private void getDataFromIntent()
    {
        Intent passedvals = getIntent();

        storeID = passedvals.getStringExtra("storeID");
        imei = passedvals.getStringExtra("imei");
        date = passedvals.getStringExtra("userdate");
        pickerDate = passedvals.getStringExtra("pickerDate");
        SN = passedvals.getStringExtra("SN");
        try
        {
           // OrderPDAID= passedvals.getStringExtra("OrderPDAID");
            CheckIfStoreExistInStoreProdcutPurchaseDetails=dbengine.fnCheckIfStoreExistInStoreProdcutPurchaseDetails(storeID);
            CheckIfStoreExistInStoreProdcutInvoiceDetails=dbengine.fnCheckIfStoreExistInStoreProdcutInvoiceDetails(storeID);
            if(CheckIfStoreExistInStoreProdcutPurchaseDetails==1 || CheckIfStoreExistInStoreProdcutInvoiceDetails==1)
            {
                OrderPDAID=dbengine.fngetOrderIDAganistStore(storeID);
            }
        }
        catch (Exception e)
        {

        }


    }

    public void initializeView()
    {
        FirstRow=(LinearLayout) findViewById(R.id.FirstRow);


        ll_TakeQuatation = (LinearLayout) findViewById(R.id.ll_TakeQuatation);
        ll_ViewQuatation = (LinearLayout) findViewById(R.id.ll_ViewQuatation);
        ll_Feebback = (LinearLayout) findViewById(R.id.ll_Feebback);
        ll_TakeOrder = (LinearLayout) findViewById(R.id.ll_TakeOrder);
        ll_NextCall = (LinearLayout) findViewById(R.id.ll_NextCall);
        ll_Competitor = (LinearLayout) findViewById(R.id.ll_Competitor);
        ll_HealthRelationship= (LinearLayout) findViewById(R.id.ll_HealthRelationship);
        ll_DistributorSKUMap = (LinearLayout) findViewById(R.id.ll_DistributorSKUMap);
        ll_CancelVisit = (LinearLayout) findViewById(R.id.ll_CancelVisit);
        ll_CloseCall = (LinearLayout) findViewById(R.id.ll_CloseCall);

        if(chkIfStoreAllowedQuote==0)
        {
            FirstRow.setVisibility(View.GONE);
            ll_TakeQuatation.setVisibility(View.GONE);
            ll_ViewQuatation.setVisibility(View.GONE);
        }
        else
        {
            FirstRow.setVisibility(View.VISIBLE);
            ll_TakeQuatation.setVisibility(View.VISIBLE);
            ll_ViewQuatation.setVisibility(View.VISIBLE);
        }

        if(flgfeedbackReq==0)
        {
            ll_Feebback.setVisibility(View.GONE);
        }
        else
        {
            ll_Feebback.setVisibility(View.VISIBLE);
        }

        ImageView imgVw_back=(ImageView)findViewById(R.id.imgVw_back);
        imgVw_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent ready4GetLoc = new Intent(LastVisitDetail_AllButton.this,LastVisitDetails.class);
                ready4GetLoc.putExtra("storeID", storeID);
                ready4GetLoc.putExtra("selStoreName", SN);
                ready4GetLoc.putExtra("imei", imei);
                ready4GetLoc.putExtra("userDate", date);
                ready4GetLoc.putExtra("pickerDate", pickerDate);
                ready4GetLoc.putExtra("bck", 1);
                startActivity(ready4GetLoc);
                finish();
            }
        });


        takeQuatation();
        viewQuatation();
        feedback();

        takeOrder();
        nextCall();
        competitor();

        healthRelationship();
        distributorSKUMap();

        cancelVisit();
        closeCall();
    }

    public void nextCall()
    {
        ll_NextCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_NextCall.isSelected())
                    ll_NextCall.setSelected(false);
                else
                    ll_NextCall.setSelected(true);
                Intent intents=new Intent(LastVisitDetail_AllButton.this,SpecialRemarksActivity.class);
                intents.putExtra("storeID", storeID);
                intents.putExtra("SN", SN);
                intents.putExtra("imei", imei);
                intents.putExtra("userdate", date);
                intents.putExtra("pickerDate", pickerDate);
                intents.putExtra("prcID", "NULL");
                intents.putExtra("OrderPDAID", OrderPDAID);

                intents.putExtra("flgFromFeedBackOrLastVisit", "DASHBOARD");

                startActivity(intents);
                finish();



            }
        });
    }
    public void competitor()
    {
        ll_Competitor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_Competitor.isSelected())
                    ll_Competitor.setSelected(false);
                else
                    ll_Competitor.setSelected(true);

                Intent intent=new Intent(LastVisitDetail_AllButton.this,FeedbackCompetitorActivity.class);
                intent.putExtra("storeID", storeID);
                intent.putExtra("SN", SN);
                intent.putExtra("imei", imei);
                intent.putExtra("userdate", date);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("bck", 1);
                intent.putExtra("flgFromFeedBackOrLastVisit", 0);
                intent.putExtra("ComeFrom", "1");
                startActivity(intent);
                finish();


            }
        });

    }
    public void healthRelationship()
    {
        flgHasBussiness=dbengine.fncheckStoreHasBussiness(storeID);
        final int feedbackCount=   dbengine.fetchTblFeedbackCount(storeID);
        ll_HealthRelationship.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_HealthRelationship.isSelected())
                    ll_HealthRelationship.setSelected(false);
                else
                    ll_HealthRelationship.setSelected(true);
                if(feedbackCount==0){

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LastVisitDetail_AllButton.this);
                    alertDialog.setTitle("Information");

                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("No Feedback taken yet, still want to continue? ");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(flgHasBussiness==1)
                            {
                                Intent intent = new Intent(LastVisitDetail_AllButton.this, HealthRelationshipActivity.class);
                                intent.putExtra("storeID", storeID);
                                intent.putExtra("SN", SN);
                                intent.putExtra("imei", imei);
                                intent.putExtra("userdate", date);
                                intent.putExtra("pickerDate", pickerDate);
                                intent.putExtra("bck", 1);
                                intent.putExtra("HealthRelationPageToRedirect", "0");//Where to Send LAst Visit Page=1, Store List =1
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();

                }
                else{
                    if(flgHasBussiness==1)
                    {
                        Intent intent = new Intent(LastVisitDetail_AllButton.this, HealthRelationshipActivity.class);
                        intent.putExtra("storeID", storeID);
                        intent.putExtra("SN", SN);
                        intent.putExtra("imei", imei);
                        intent.putExtra("userdate", date);
                        intent.putExtra("pickerDate", pickerDate);
                        intent.putExtra("bck", 1);
                        intent.putExtra("HealthRelationPageToRedirect", "0");//Where to Send LAst Visit Page=1, Store List =1
                        startActivity(intent);
                        finish();
                    }
                }



                //--------




            }
        });
    }
    public void distributorSKUMap()
    {
        ll_DistributorSKUMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_DistributorSKUMap.isSelected())
                    ll_DistributorSKUMap.setSelected(false);
                else
                    ll_DistributorSKUMap.setSelected(true);

                int check=dbengine.fnCheckIfStoreExistInStoreProdcutPurchaseDetails(storeID);
                if(check==0)
                {
                    showAlertSingleButtonInfo(getResources().getString(R.string.NoOrderExistForStore));
                }
                else
                {
                    Intent AmtCollectIntent = new Intent(LastVisitDetail_AllButton.this, DistributorSelectionForSKU.class);
                    AmtCollectIntent.putExtra("storeID", storeID);
                    AmtCollectIntent.putExtra("imei", imei);
                    AmtCollectIntent.putExtra("userdate", date);
                    AmtCollectIntent.putExtra("pickerDate", pickerDate);
                    AmtCollectIntent.putExtra("SN", "SN");
                    AmtCollectIntent.putExtra("OrderPDAID", OrderPDAID);
                    AmtCollectIntent.putExtra("ComeFrom", "1");
                    startActivity(AmtCollectIntent);
                    finish();

                }


            }
        });
    }
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Information");
        alertDialog.setIcon(R.drawable.error_info_ico);
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. \nPlease select all settings on the next page!");

        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void closeCall()
    {
        ll_CloseCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_CloseCall.isSelected())
                    ll_CloseCall.setSelected(false);
                else
                    ll_CloseCall.setSelected(true);



                boolean isGPSok = false;
                boolean isNWok=false;
                isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNWok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(!isGPSok)
                {
                    isGPSok = false;
                }
                if(!isNWok)
                {
                    isNWok = false;
                }
                if(!isGPSok && !isNWok)
                {
                    try
                    {
                        showSettingsAlert();
                    }
                    catch(Exception e)
                    {

                    }

                    isGPSok = false;
                    isNWok=false;
                }
                else
                {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LastVisitDetail_AllButton.this);
                    alertDialog.setTitle("Information");

                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(getText(R.string.submitConfirmAlert));
                    alertDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog,int which)
                        {
                            dialog.dismiss();
                            dbengine.open();
                            Boolean hasPrevLoc=dbengine.PrevLocChk(storeID.trim());
                            dbengine.close();
                            if (hasPrevLoc)
                            {
                                FullSyncDataNow task = new FullSyncDataNow(LastVisitDetail_AllButton.this);
                                task.execute();
                            }
                            else
                            {
                                LocationRetreivingGlobal llaaa=new LocationRetreivingGlobal();
                                llaaa.locationRetrievingAndDistanceCalculating(LastVisitDetail_AllButton.this,false,50);
                            }






                        }
                    });
                    alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog,int which)
                        {
                            dialog.dismiss();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }


            }
        });
    }

    public void takeQuatation()
    {
        ll_TakeQuatation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_TakeQuatation.isSelected())
                    ll_TakeQuatation.setSelected(false);
                else
                    ll_TakeQuatation.setSelected(true);

                Intent intents=new Intent(LastVisitDetail_AllButton.this,QuotationActivity.class);
                intents.putExtra("quatationFlag", "NEW");
                intents.putExtra("SalesQuoteId", "Null");
                intents.putExtra("storeID", storeID);
                intents.putExtra("SN", SN);
                intents.putExtra("imei", imei);
                intents.putExtra("userdate", date);
                intents.putExtra("pickerDate", pickerDate);
                intents.putExtra("FROM", "ALLBUTTON");
                intents.putExtra("prcID", "NULL");
                CommonInfo.SalesQuoteId="BLANK";
                CommonInfo.prcID="NULL";
                CommonInfo.newQuottionID="NULL";
                CommonInfo.globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";
                startActivity(intents);
                finish();
            }
        });
    }
    public void viewQuatation()
    {
        ll_ViewQuatation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_ViewQuatation.isSelected())
                    ll_ViewQuatation.setSelected(false);
                else
                    ll_ViewQuatation.setSelected(true);

                Intent intent=new Intent(LastVisitDetail_AllButton.this,ViewQuotation.class);
                intent.putExtra("storeID", storeID);
                intent.putExtra("SN", SN);
                intent.putExtra("imei", imei);
                intent.putExtra("userdate", date);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("bck", 1);

                startActivity(intent);
                finish();
            }
        });
    }
    public void feedback()
    {
        if(flgfeedbackReq==1)
        {
            ll_Feebback.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_Feebback.setVisibility(View.GONE);
        }
        ll_Feebback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_Feebback.isSelected())
                    ll_Feebback.setSelected(false);
                else
                    ll_Feebback.setSelected(true);

                Intent intent=new Intent(LastVisitDetail_AllButton.this,FeedBack_Activity.class);
                intent.putExtra("storeID", storeID);
                intent.putExtra("SN", SN);
                intent.putExtra("imei", imei);
                intent.putExtra("userdate", date);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("bck", 1);
                startActivity(intent);
                finish();
            }
        });
    }
    public void takeOrder()
    {
        ll_TakeOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_TakeOrder.isSelected())
                    ll_TakeOrder.setSelected(false);
                else
                    ll_TakeOrder.setSelected(true);

                long syncTIMESTAMP = System.currentTimeMillis();
                Date dateobjNew = new Date(syncTIMESTAMP);
                SimpleDateFormat dfnew = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                String startTS = dfnew.format(dateobjNew);

                dbengine.open();
                dbengine.UpdateStoreEndVisit(storeID,startTS);
                dbengine.close();
                Intent nxtP4 = new Intent(LastVisitDetail_AllButton.this,ProductOrderFilterSearch.class);
                nxtP4.putExtra("storeID", storeID);
                nxtP4.putExtra("SN", SN);
                nxtP4.putExtra("imei", imei);
                nxtP4.putExtra("userdate", date);
                nxtP4.putExtra("pickerDate", pickerDate);
                nxtP4.putExtra("RedirectedFrom", "Normal");
                startActivity(nxtP4);
                finish();
            }
        });
    }
    public void cancelVisit()
    {
        int valSstatValueAgainstStore=0;
        try
        {
            dbengine.open();
            valSstatValueAgainstStore=dbengine.fnGetStatValueagainstStore(storeID);
            dbengine.close();
            if(valSstatValueAgainstStore==0)
            {
                ll_CancelVisit.setVisibility(View.VISIBLE);
            }
            else
            {
                ll_CancelVisit.setVisibility(View.GONE);

            }

        }catch(Exception e)
        {

        }finally
        {

        }

        ll_CancelVisit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_CancelVisit.isSelected())
                    ll_CancelVisit.setSelected(false);
                else
                    ll_CancelVisit.setSelected(true);

                AlertDialog.Builder alertDialogSyncError = new AlertDialog.Builder(LastVisitDetail_AllButton.this);
                alertDialogSyncError.setTitle(R.string.AlertDialogHeaderMsg);
                alertDialogSyncError.setCancelable(false);
                alertDialogSyncError.setMessage(R.string.CancelButtonLastVisitPage);
                alertDialogSyncError.setPositiveButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        int flag=0;
                        Intent storeSaveIntent = new Intent(LastVisitDetail_AllButton.this, LauncherActivity.class);
                        startActivity(storeSaveIntent);
                        finish();
                    }

                });
                alertDialogSyncError.setNeutralButton("Cancel",new DialogInterface.OnClickListener()
                {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                alertDialogSyncError.setIcon(R.drawable.info_ico);

                AlertDialog alert = alertDialogSyncError.create();
                alert.show();
            }
        });
    }

    public boolean checkLastFinalLoctionIsRepeated(String currentLat,String currentLong,String currentAccuracy){
        boolean repeatedLoction=false;

        try {

            String chekLastGPSLat="0";
            String chekLastGPSLong="0";
            String chekLastGpsAccuracy="0";
            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.FinalLatLngJsonFile);
            if (!jsonTxtFolder.exists())
            {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew="FinalGPSLastLocation.txt";
            File file = new File(jsonTxtFolder,txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory()+"/"+ CommonInfo.FinalLatLngJsonFile+"/"+txtFileNamenew;

            // If file does not exists, then create it
            if (file.exists()) {
                StringBuffer buffer=new StringBuffer();
                String myjson_stampiGPSLastLocation="";
                StringBuffer sb = new StringBuffer();
                BufferedReader br = null;

                try {
                    br = new BufferedReader(new FileReader(file));

                    String temp;
                    while ((temp = br.readLine()) != null)
                        sb.append(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close(); // stop reading
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                myjson_stampiGPSLastLocation=sb.toString();

                JSONObject jsonObjGPSLast = new JSONObject(myjson_stampiGPSLastLocation);
                JSONArray jsonObjGPSLastInneralues = jsonObjGPSLast.getJSONArray("GPSLastLocationDetils");

                String StringjsonGPSLastnew = jsonObjGPSLastInneralues.getString(0);
                JSONObject jsonObjGPSLastnewwewe = new JSONObject(StringjsonGPSLastnew);

                chekLastGPSLat=jsonObjGPSLastnewwewe.getString("chekLastGPSLat");
                chekLastGPSLong=jsonObjGPSLastnewwewe.getString("chekLastGPSLong");
                chekLastGpsAccuracy=jsonObjGPSLastnewwewe.getString("chekLastGpsAccuracy");

                if(currentLat!=null )
                {
                    if(currentLat.equals(chekLastGPSLat) && currentLong.equals(chekLastGPSLong) && currentAccuracy.equals(chekLastGpsAccuracy))
                    {
                        repeatedLoction=true;
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repeatedLoction;

    }
    public void fnCreateLastKnownFinalLocation(String chekLastGPSLat,String chekLastGPSLong,String chekLastGpsAccuracy)
    {

        try {

            JSONArray jArray=new JSONArray();
            JSONObject jsonObjMain=new JSONObject();


            JSONObject jOnew = new JSONObject();
            jOnew.put( "chekLastGPSLat",chekLastGPSLat);
            jOnew.put( "chekLastGPSLong",chekLastGPSLong);
            jOnew.put( "chekLastGpsAccuracy", chekLastGpsAccuracy);


            jArray.put(jOnew);
            jsonObjMain.put("GPSLastLocationDetils", jArray);

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.FinalLatLngJsonFile);
            if (!jsonTxtFolder.exists())
            {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew="FinalGPSLastLocation.txt";
            File file = new File(jsonTxtFolder,txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory()+"/"+ CommonInfo.FinalLatLngJsonFile+"/"+txtFileNamenew;


            // If file does not exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


            FileWriter fw;
            try {
                fw = new FileWriter(file.getAbsoluteFile());

                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(jsonObjMain.toString());

                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
				 /*  file=contextcopy.getFilesDir();
				//fileOutputStream=contextcopy.openFileOutput("FinalGPSLastLocation.txt", Context.MODE_PRIVATE);
				fileOutputStream.write(jsonObjMain.toString().getBytes());
				fileOutputStream.close();*/
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{

        }
    }


    @Override
    public void testFunctionOne(String fnLati, String fnLongi, String finalAccuracy, String fnAccurateProvider, String GpsLat, String GpsLong, String GpsAccuracy, String NetwLat, String NetwLong, String NetwAccuracy, String FusedLat, String FusedLong, String FusedAccuracy, String AllProvidersLocation, String GpsAddress, String NetwAddress, String FusedAddress, String FusedLocationLatitudeWithFirstAttempt, String FusedLocationLongitudeWithFirstAttempt, String FusedLocationAccuracyWithFirstAttempt, int flgLocationServicesOnOff, int flgGPSOnOff, int flgNetworkOnOff, int flgFusedOnOff, int flgInternetOnOffWhileLocationTracking, String address, String pincode, String city, String state) {

        System.out.println("SHIVA"+fnLati+","+fnLongi+","+finalAccuracy+","+fnAccurateProvider+","+GpsLat+","+GpsLong+","+GpsAccuracy+","+NetwLat+","+NetwLong+","+NetwAccuracy+","+FusedLat+","+FusedLong+","+FusedAccuracy+","+AllProvidersLocation+","+GpsAddress+","+NetwAddress+","+FusedAddress+","+FusedLocationLatitudeWithFirstAttempt+","+FusedLocationLongitudeWithFirstAttempt+","+FusedLocationAccuracyWithFirstAttempt+","+fnLongi+","+flgLocationServicesOnOff+","+flgGPSOnOff+","+flgNetworkOnOff+","+flgFusedOnOff+","+flgInternetOnOffWhileLocationTracking+","+address+","+pincode+","+city+","+state);
        dbengine.fndeleteOldAddressDetailsofVisitedStore(storeID);
        dbengine.saveLatLngToTxtFile(storeID,fnLati, fnLongi,finalAccuracy,fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,FusedLong,FusedAccuracy,3,"0",
                FusedAddress,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,FusedLocationLatitudeWithFirstAttempt
                ,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
        dbengine.open();
        dbengine.UpdateStoreActualLatLongi(storeID,String.valueOf(fnLati), String.valueOf(fnLongi), "" + finalAccuracy,fnAccurateProvider,flgLocationServicesOnOff,flgGPSOnOff,flgNetworkOnOff,flgFusedOnOff,flgInternetOnOffWhileLocationTracking,flgRestart,flgStoreOrder);
        dbengine.close();
        if(!checkLastFinalLoctionIsRepeated(String.valueOf(fnLati), String.valueOf(fnLongi), String.valueOf(finalAccuracy)))
        {

            fnCreateLastKnownFinalLocation(String.valueOf(fnLati), String.valueOf(fnLongi), String.valueOf(finalAccuracy));


            FullSyncDataNow task = new FullSyncDataNow(LastVisitDetail_AllButton.this);
            task.execute();
        }
        else
        {
            countSubmitClicked++;
            if(countSubmitClicked==1)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LastVisitDetail_AllButton.this);

                // Setting Dialog Title
                alertDialog.setTitle("Information");
                alertDialog.setIcon(R.drawable.error_info_ico);
                alertDialog.setCancelable(false);
                // Setting Dialog Message
                alertDialog.setMessage("Your current location is same as previous, so please turn off your location services then turn on, it back again.");

                // On pressing Settings button
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        countSubmitClicked++;
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

                // Showing Alert Message
                alertDialog.show();



            }
            else
            {

                FullSyncDataNow task = new FullSyncDataNow(LastVisitDetail_AllButton.this);
                task.execute();
            }


        }







    }



    private class FullSyncDataNow extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialogGetStores;
        public FullSyncDataNow(LastVisitDetail_AllButton activity)
        {
           pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage("Submitting Order Details...");
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();


        }

        @Override

        protected Void doInBackground(Void... params)
        {

            // int Outstat=3;
            //TransactionTableDataDeleteAndSaving(Outstat);
            //InvoiceTableDataDeleteAndSaving(Outstat);

            long  syncTIMESTAMP = System.currentTimeMillis();
            java.util.Date dateobj = new java.util.Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
            String StampEndsTime = df.format(dateobj);


            dbengine.open();
            dbengine.UpdateStoreEndVisit(storeID, StampEndsTime);
            dbengine.UpdateStoreProductAppliedSchemesBenifitsRecords(storeID.trim(),"3",OrderPDAID);
            dbengine.UpdateStoreStoreReturnDetail(storeID.trim(),"3",OrderPDAID);
            dbengine.UpdateStoreFlag(storeID.trim(), 3);
            dbengine.UpdateStoreOtherMainTablesFlag(storeID.trim(), 3,OrderPDAID);


            dbengine.close();
            dbengine.updateStoreQuoteSubmitFlgInStoreMstr(storeID.trim(),0);
            if(dbengine.checkCountIntblStoreSalesOrderPaymentDetails(storeID,OrderPDAID)==0)
            {
                String strDefaultPaymentStageForStore=dbengine.fnGetDefaultStoreOrderPAymentDetails(storeID);
                if(!strDefaultPaymentStageForStore.equals(""))
                {
                    dbengine.open();
                    dbengine. fnsaveStoreSalesOrderPaymentDetails(storeID,OrderPDAID,strDefaultPaymentStageForStore,"3");
                    dbengine.close();
                }
            }
            dbengine.open();
            String presentRoute=dbengine.GetActiveRouteID();
            dbengine.close();



            SimpleDateFormat df1 = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss", Locale.ENGLISH);

            newfullFileName=imei+"."+presentRoute+"."+ df1.format(dateobj);




            try {


                File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

                if (!OrderXMLFolder.exists())
                {
                    OrderXMLFolder.mkdirs();

                }
                String routeID=dbengine.GetActiveRouteIDSunil();

                DA.open();
                DA.export(CommonInfo.DATABASE_NAME, newfullFileName,routeID);
                DA.close();


                dbengine.savetbl_XMLfiles(newfullFileName, "3","1");
                dbengine.open();

                dbengine.UpdateStoreFlag(storeID.trim(), 5);
                dbengine.UpdateStoreOtherMainTablesFlag(storeID.trim(), 5,OrderPDAID);
                dbengine.UpdateStoreMaterialphotoFlag(storeID.trim(), 5);
                dbengine.UpdateStoreReturnphotoFlag(storeID.trim(), 5);
                dbengine.UpdateNewAddedStorephotoFlag(storeID.trim(), 5);

                dbengine.close();
                if(dbengine.checkCountIntblStoreSalesOrderPaymentDetails(storeID,OrderPDAID)==0)
                {
                    String strDefaultPaymentStageForStore=dbengine.fnGetDefaultStoreOrderPAymentDetails(storeID);
                    if(!strDefaultPaymentStageForStore.equals(""))
                    {
                        dbengine.open();
                        dbengine. fnsaveStoreSalesOrderPaymentDetails(storeID,OrderPDAID,strDefaultPaymentStageForStore,"4");
                        dbengine.close();
                    }
                }


            } catch (Exception e) {

                e.printStackTrace();
               /* if(pDialogGetStores.isShowing())
                {
                    pDialogGetStores.dismiss();
                }*/
            }
            return null;
        }

        @Override
        protected void onCancelled() {

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /*if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }*/

            try
            {
                StoreSelection.flgChangeRouteOrDayEnd=0;
                Intent syncIntent = new Intent(LastVisitDetail_AllButton.this, SyncMaster.class);
                syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml");

                syncIntent.putExtra("OrigZipFileName", newfullFileName);
                syncIntent.putExtra("whereTo", "Regular");
                startActivity(syncIntent);
                finish();
				/*Intent AmtCollectIntent = new Intent(OrderReview.this, DistributorSelectionForSKU.class);
				AmtCollectIntent.putExtra("storeID", storeID);
				AmtCollectIntent.putExtra("imei", imei);
				AmtCollectIntent.putExtra("userdate", date);
				AmtCollectIntent.putExtra("pickerDate", pickerDate);
				AmtCollectIntent.putExtra("SN", "SN");
				AmtCollectIntent.putExtra("OrderPDAID", strGlobalOrderID);
				AmtCollectIntent.putExtra("ComeFrom", "0");
				startActivity(AmtCollectIntent);
				finish();*/


            } catch (Exception e) {

                e.printStackTrace();
            }


        }
    }
}
