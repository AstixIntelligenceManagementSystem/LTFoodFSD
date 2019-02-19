package project.astix.com.ltfoodfsd;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.astix.Common.CommonInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class AllButtonActivity extends BaseActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String the_string_response = "0";
    InputStream inputStream;
    public int IMGsyOK = 0;
    ProgressDialog mProgressDialog;

    String AccuracyFromLauncher = "NA";
    String LattitudeFromLauncher = "NA";
    String ProviderFromLauncher = "NA";
    String LongitudeFromLauncher = "NA";
    SharedPreferences sharedPref;

    public int valDayEndOrChangeRoute = 1; // 1=Clicked on Day End Button, 2=Clicked on Change Route Button
    String whereTo = "11";
    public String[] StoreList2Procs;
    public String[] storeCode;
    public String[] storeName;
    ArrayList<String> stIDs;
    ArrayList<String> stNames;
    public boolean[] checks;
    int whatTask = 0;
    public long syncTIMESTAMP;
    static int flgChangeRouteOrDayEnd = 0;
    ProgressDialog pDialog2;

    ArrayList mSelectedItems = new ArrayList();

    public String[] storeStatus;


    public int powerCheck = 0;

    public PowerManager.WakeLock wl;
    public String rID = "0";    // Abhinav Sir tell Sunil for set its value zero at 10 October 2017
    LinearLayout ll_marketVisit, ll_storeVal, ll_reports, ll_outstandingTask, ll_visitPlan, ll_appointment, ll_distrbtrStock, ll_execution;

    LinearLayout ll_dsrTracker, ll_distrbtnMap, ll_noVisit, ll_DayEnd, ll_SoProfile;

    String[] drsNames;
    DBAdapterKenya dbengine = new DBAdapterKenya(this);

    // DBAdapterLtFoods dbengineSo = new DBAdapterLtFoods(this);
    LinkedHashMap<String, String> hmapdsrIdAndDescr_details = new LinkedHashMap<String, String>();
    public String SelectedDSRValue = "";
    ServiceWorker newservice = new ServiceWorker();
    String imei;
    public int chkFlgForErrorToCloseApp = 0;
    public String fDate;
    public SimpleDateFormat sdf;

    public String ReasonId;
    public String ReasonText = "NA";
    public static int RowId = 0;


    public int click_but_distribtrStock = 0;
    int CstmrNodeId = 0, CstomrNodeType = 0;


    public String newfullFileName;
    DatabaseAssistantDistributorEntry DA = new DatabaseAssistantDistributorEntry(this);
    DatabaseAssistant DASFA = new DatabaseAssistant(this);
    SyncXMLfileData task2;
    public String[] xmlForWeb = new String[1];
    int serverResponseCode = 0;
    public int syncFLAG = 0;

    public ProgressDialog pDialogGetStores;
    public TextView noVisit_tv;
    String[] reasonNames;
    LinkedHashMap<String, String> hmapReasonIdAndDescr_details = new LinkedHashMap<String, String>();

    public Date currDate;
    public SimpleDateFormat currDateFormat;
    public String currSysDate;

    ImageView imgVw_logout;

    //report alert
    String[] Distribtr_list;
    String DbrNodeId, DbrNodeType, DbrName;
    ArrayList<String> DbrArray = new ArrayList<String>();
    LinkedHashMap<String, String> hmapDistrbtrList = new LinkedHashMap<>();
    public String SelectedDistrbtrName = "";

    //market visit loc alert
    public LocationManager locationManager;
    android.app.AlertDialog GPSONOFFAlert = null;
    public AppLocationService appLocationService;
    public PowerManager pm;

    public ProgressDialog pDialog2STANDBY;

    private final long startTime = 15000;
    private final long interval = 200;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    LocationRequest mLocationRequest;
    public Location location;
    public AllButtonActivity.CoundownClass countDownTimer;
    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;

    public String fnAccurateProvider = "";
    public String fnLati = "0";
    public String fnLongi = "0";
    public Double fnAccuracy = 0.0;

    public String FusedLocationLatitudeWithFirstAttempt = "0";
    public String FusedLocationLongitudeWithFirstAttempt = "0";
    public String FusedLocationAccuracyWithFirstAttempt = "0";
    public String AllProvidersLocation = "";
    public String FusedLocationLatitude = "0";
    public String FusedLocationLongitude = "0";
    public String FusedLocationProvider = "";
    public String FusedLocationAccuracy = "0";

    public String GPSLocationLatitude = "0";
    public String GPSLocationLongitude = "0";
    public String GPSLocationProvider = "";
    public String GPSLocationAccuracy = "0";

    public String NetworkLocationLatitude = "0";
    public String NetworkLocationLongitude = "0";
    public String NetworkLocationProvider = "";
    public String NetworkLocationAccuracy = "0";
    public static int flgLocationServicesOnOff = 0;
    public static int flgGPSOnOff = 0;
    public static int flgNetworkOnOff = 0;
    public static int flgFusedOnOff = 0;
    public static int flgInternetOnOffWhileLocationTracking = 0;
    public static int flgRestart = 0;
    public static int flgStoreOrder = 0;

    public static String address, pincode, city, state, latitudeToSave, longitudeToSave, accuracyToSave;
    int countSubmitClicked = 0;
    String fusedData;
    public boolean serviceException = false;

    public static boolean isDayEndClicked = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        wl.release();
    }

    private void getReasonDetail() throws IOException {

        int check = dbengine.countDataIntblNoVisitReasonMaster();

        hmapReasonIdAndDescr_details = dbengine.fetch_Reason_List();

        int index = 0;
        if (hmapReasonIdAndDescr_details != null) {
            reasonNames = new String[hmapReasonIdAndDescr_details.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapReasonIdAndDescr_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while (iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry) iterator.next();
                reasonNames[index] = me2.getKey().toString();
                index = index + 1;
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_button);
        DayStartActivity.flgDaySartWorking = 0;
        sharedPref = getSharedPreferences(CommonInfo.Preference, MODE_PRIVATE);
        if (sharedPref.contains("CoverageAreaNodeID")) {
            if (sharedPref.getInt("CoverageAreaNodeID", 0) != 0) {
                CommonInfo.CoverageAreaNodeID = sharedPref.getInt("CoverageAreaNodeID", 0);
                CommonInfo.CoverageAreaNodeType = sharedPref.getInt("CoverageAreaNodeType", 0);
            }
        }
        if (sharedPref.contains("SalesmanNodeId")) {
            if (sharedPref.getInt("SalesmanNodeId", 0) != 0) {
                CommonInfo.SalesmanNodeId = sharedPref.getInt("SalesmanNodeId", 0);
                CommonInfo.SalesmanNodeType = sharedPref.getInt("SalesmanNodeType", 0);
            }
        }
        if (sharedPref.contains("flgDataScope")) {
            if (sharedPref.getInt("flgDataScope", 0) != 0) {
                CommonInfo.flgDataScope = sharedPref.getInt("flgDataScope", 0);

            }
        }
        if (sharedPref.contains("flgDSRSO")) {
            if (sharedPref.getInt("flgDSRSO", 0) != 0) {
                CommonInfo.FlgDSRSO = sharedPref.getInt("flgDSRSO", 0);

            }
        }
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tManager.getDeviceId();

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (CommonInfo.imei == null || CommonInfo.imei.trim().equals("")) {
            imei = tManager.getDeviceId();
            CommonInfo.imei = imei;
        } else {
            imei = CommonInfo.imei.trim();
        }

      /*  //  SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("CoverageAreaNodeID", coverageAreaNodeID);
        editor.putInt("CoverageAreaNodeType", coverageAreaNodeType);


        editor.commit();*/
        shardPrefForSalesman(0, 0);

        flgDataScopeSharedPref(0);
        Date date1 = new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        fDate = sdf.format(date1).toString().trim();

        currDate = new Date();
        currDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        currSysDate = currDateFormat.format(currDate).toString();
        initialiseViews();
        if (powerCheck == 0) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
            wl.acquire();
        }
    }


    void initialiseViews() {
        ll_marketVisit = (LinearLayout) findViewById(R.id.ll_marketVisit);
        ll_reports = (LinearLayout) findViewById(R.id.ll_reports);
        ll_storeVal = (LinearLayout) findViewById(R.id.ll_storeVal);


        ll_outstandingTask = (LinearLayout) findViewById(R.id.ll_outstandingTask);
        ll_visitPlan = (LinearLayout) findViewById(R.id.ll_visitPlan);
        ll_appointment = (LinearLayout) findViewById(R.id.ll_appointment);


        ll_distrbtrStock = (LinearLayout) findViewById(R.id.ll_distrbtrStock);
        // ll_execution = (LinearLayout) findViewById(R.id.ll_execution);

        ll_distrbtnMap = (LinearLayout) findViewById(R.id.ll_distrbtnMap);
        //  ll_dsrTracker = (LinearLayout) findViewById(R.id.ll_dsrTracker);

        ll_noVisit = (LinearLayout) findViewById(R.id.ll_noVisit);
        ll_DayEnd = (LinearLayout) findViewById(R.id.ll_DayEnd);


        distributorMapWorking();
        marketVisitWorking();
        reportsWorking();
        storeValidationWorking();

        outstandingTaskWorking();
        visitPlanWorking();
        appointmentWorking();
        distributorStockWorking();

        int checkDataNotSync = dbengine.CheckUserDoneGetStoreOrNot();
        if (checkDataNotSync == 1) {

        } else {
            noVisitWorking();
        }
        dayEndWorking();


        try {
            getReasonDetail();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            getDSRDetail();
            //report alert
            getDistribtrList();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        imgVw_logout = (ImageView) findViewById(R.id.imgVw_logout);

        imgVw_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fnLogoutfunctionality();

            }
        });

        ll_SoProfile = (LinearLayout) findViewById(R.id.ll_SoProfile);
        ll_SoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllButtonActivity.this, RegistrationActivity.class);
                i.putExtra("IntentFrom", "AllButtonActivity");
                i.putExtra("Button", "DSMRegistration");
                startActivity(i);
                finish();
            }
        });


    }

    void marketVisitWorking() {
        ll_marketVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_marketVisit.isSelected())
                    ll_marketVisit.setSelected(false);
                else
                    ll_marketVisit.setSelected(true);


                int checkDataNotSync = dbengine.CheckUserDoneGetStoreOrNot();
                Date date1 = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                fDate = sdf.format(date1).toString().trim();
                if (checkDataNotSync == 1) {
                    dbengine.open();
                    String rID = dbengine.GetActiveRouteID();
                    dbengine.close();

                    // Date date=new Date();
                    sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    String fDateNew = sdf.format(date1).toString();
                    //fDate = passDate.trim().toString();


                    // In Splash Screen SP, we are sending this Format "dd-MMM-yyyy"
                    // But InLauncher Screen SP, we are sending this Format "dd-MM-yyyy"


                    Intent storeIntent = new Intent(AllButtonActivity.this, StoreSelection.class);
                    storeIntent.putExtra("imei", imei);
                    storeIntent.putExtra("userDate", fDate);
                    storeIntent.putExtra("pickerDate", fDateNew);
                    storeIntent.putExtra("rID", rID);
                    startActivity(storeIntent);
                    finish();

                } else {
                    GetStoresForDay task = new GetStoresForDay(AllButtonActivity.this);
                    task.execute();
                }


            }
        });
    }

    void reportsWorking() {
        ll_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(ll_reports.isSelected())
                    ll_reports.setSelected(false);
                else
                    ll_reports.setSelected(true);*/
                //openReportAlert();
                Intent i = new Intent(AllButtonActivity.this, DetailReportSummaryActivityForAll.class);
                startActivity(i);
                // finish();
            }
        });
    }


    void outstandingTaskWorking() {

        ll_outstandingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AllButtonActivity.this, ViewOutStandingTask.class);
                startActivity(i);

            }
        });

    }

    void visitPlanWorking() {
        ll_visitPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AllButtonActivity.this, ViewVisitPlan.class);
                startActivity(i);

            }
        });
    }

    void appointmentWorking() {
        ll_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AllButtonActivity.this, ViewAppointment.class);
                startActivity(i);
                // finish();

            }
        });
    }

    private void getDSRDetail() throws IOException {

        int check = dbengine.countDataIntblNoVisitReasonMaster();

        hmapdsrIdAndDescr_details = dbengine.fetch_DSRCoverage_List();

        int index = 0;
        if (hmapdsrIdAndDescr_details != null) {
            drsNames = new String[hmapdsrIdAndDescr_details.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapdsrIdAndDescr_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while (iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry) iterator.next();
                drsNames[index] = me2.getKey().toString();
                index = index + 1;
            }
        }


    }

    public static boolean deleteNon_EmptyDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteNon_EmptyDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void dialogLogout() {

        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(LauncherActivity.this, R.style.Dialog));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);

        alertDialog.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialog.setMessage(R.string.LogoutMsg);
        alertDialog.setPositiveButton(R.string.AlertDialogYesButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                CommonInfo.AnyVisit = 0;

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();

                File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                if (!OrderXMLFolder.exists()) {
                    OrderXMLFolder.mkdirs();
                }

                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                deleteNon_EmptyDir(del);

                File ImagesFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.ImagesFolder);
                if (!ImagesFolder.exists()) {
                    ImagesFolder.mkdirs();
                }

                File del1 = new File(Environment.getExternalStorageDirectory(), CommonInfo.ImagesFolder);
                deleteNon_EmptyDir(del1);

                File TextFileFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.TextFileFolder);
                if (!ImagesFolder.exists()) {
                    ImagesFolder.mkdirs();
                }

                File del2 = new File(Environment.getExternalStorageDirectory(), CommonInfo.TextFileFolder);
                deleteNon_EmptyDir(del2);
                try {
                    dbengine.deleteViewAddedStore();
                    dbengine.deletetblStoreList();
                } catch (Exception e) {

                }


                dialog.dismiss();
                finishAffinity();
            }
        });

        alertDialog.setNegativeButton(R.string.AlertDialogNoButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    public void fnLogoutfunctionality() {

        File ImagesFileInFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.ImagesFolder);

        // check number of files in folder
        String[] PendingImagesInFolder = checkNumberOfFiles(ImagesFileInFolder);

        File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
        if (!OrderXMLFolder.exists()) {
            OrderXMLFolder.mkdirs();
        }

        imgVw_logout.setImageResource(R.drawable.logout_hover);

        File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);


        // check number of files in folder
        final String[] AllFilesNameNotSync = checkNumberOfFiles(del);

        String xmlfileNames = dbengine.fnGetXMLFile("3");
        // String xmlfileNamesStrMap=dbengineSo.fnGetXMLFile("3");

        dbengine.open();
        String[] SaveStoreList = dbengine.ProcessStoreReq();
        dbengine.close();

        if (SaveStoreList.length != 0) {
            showAlertForEveryOne("Please Day End Before Logging Out.");

        } else if (xmlfileNames.length() > 0) {
            showAlertForEveryOne("Please Day End Before Logging Out.");
        } else if (PendingImagesInFolder != null && PendingImagesInFolder.length > 0) {
            showAlertForEveryOne("Please Day End Before Logging Out.");
        } else {
            dialogLogout();

        }
                /*else if(AllFilesNameNotSync.length>0)
                {
                    showAlertForEveryOne("Please Day End Before Logging Out.");
                }*/
    }

    void getDistribtrList() {
        dbengine.open();

        Distribtr_list = dbengine.getDistributorDataMstr();
        dbengine.close();
        for (int i = 0; i < Distribtr_list.length; i++) {
            String value = Distribtr_list[i];
            DbrNodeId = value.split(Pattern.quote("^"))[0];
            DbrNodeType = value.split(Pattern.quote("^"))[1];
            DbrName = value.split(Pattern.quote("^"))[2];
            //flgReMap=Integer.parseInt(value.split(Pattern.quote("^"))[3]);

            hmapDistrbtrList.put(DbrName, DbrNodeId + "^" + DbrNodeType);
            DbrArray.add(DbrName);
        }

    }

    public void midPart() {
        String tempSID;
        String tempSNAME;

        stIDs = new ArrayList<String>(StoreList2Procs.length);
        stNames = new ArrayList<String>(StoreList2Procs.length);

        for (int x = 0; x < (StoreList2Procs.length); x++) {
            StringTokenizer tokens = new StringTokenizer(String.valueOf(StoreList2Procs[x]), "%");
            tempSID = tokens.nextToken().trim();
            tempSNAME = tokens.nextToken().trim();

            stIDs.add(x, tempSID);
            stNames.add(x, tempSNAME);
        }
    }

    public void confirmationForSubmission(String number) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AllButtonActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Cancel..");

        // Setting Dialog Message
        if (1 < Integer.valueOf(number)) {
            alertDialog.setMessage("Are you sure you want " + number + " orders are to be cancelled ?");
        } else {
            alertDialog.setMessage("Are you sure you want " + number + " order to be cancelled ?");
        }


        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.cancel_hover);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                whatTask = 2;
                // -- Route Info Exec()
                try {

                    new bgTasker().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //System.out.println(e);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    //System.out.println(e);
                }
                // --


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void dayEndCustomAlert(int flagWhichButtonClicked) {
        final Dialog dialog = new Dialog(AllButtonActivity.this, R.style.AlertDialogDayEndTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.day_end_custom_alert);
        if (flagWhichButtonClicked == 1) {
            dialog.setTitle(R.string.genTermSelectStoresPendingToCompleteDayEnd);
        } else if (flagWhichButtonClicked == 2) {
            dialog.setTitle(R.string.genTermSelectStoresPendingToCompleteChangeRoute);
        }


        LinearLayout ll_product_not_submitted = (LinearLayout) dialog.findViewById(R.id.ll_product_not_submitted);
        mSelectedItems.clear();
        final String[] stNames4List = new String[stNames.size()];
        checks = new boolean[stNames.size()];
        stNames.toArray(stNames4List);

        for (int cntPendingList = 0; cntPendingList < stNames4List.length; cntPendingList++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View viewAlertProduct = inflater.inflate(R.layout.day_end_alrt, null);
            final TextView txtVw_product_name = (TextView) viewAlertProduct.findViewById(R.id.txtVw_product_name);
            txtVw_product_name.setText(stNames4List[cntPendingList]);
            txtVw_product_name.setTextColor(this.getResources().getColor(R.color.green_submitted));
            final ImageView img_to_be_submit = (ImageView) viewAlertProduct.findViewById(R.id.img_to_be_submit);
            img_to_be_submit.setTag(cntPendingList);

            final ImageView img_to_be_cancel = (ImageView) viewAlertProduct.findViewById(R.id.img_to_be_cancel);
            img_to_be_cancel.setTag(cntPendingList);
            img_to_be_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    if (!checks[Integer.valueOf(img_to_be_submit.getTag().toString())]) {
                        img_to_be_submit.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_hover));
                        img_to_be_cancel.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel_normal));
                        txtVw_product_name.setTextColor(getResources().getColor(R.color.green_submitted));
                        mSelectedItems.add(stNames4List[Integer.valueOf(img_to_be_submit.getTag().toString())]);
                        checks[Integer.valueOf(img_to_be_submit.getTag().toString())] = true;
                    }


                }
            });

            img_to_be_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mSelectedItems.contains(stNames4List[Integer.valueOf(img_to_be_cancel.getTag().toString())])) {
                        if (checks[Integer.valueOf(img_to_be_cancel.getTag().toString())]) {

                            img_to_be_submit.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_normal));
                            img_to_be_cancel.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel_hover));
                            txtVw_product_name.setTextColor(Color.RED);
                            mSelectedItems.remove(stNames4List[Integer.valueOf(img_to_be_cancel.getTag().toString())]);
                            checks[Integer.valueOf(img_to_be_cancel.getTag().toString())] = false;
                        }

                    }

                }
            });
            mSelectedItems.add(stNames4List[cntPendingList]);
            checks[cntPendingList] = true;
            ll_product_not_submitted.addView(viewAlertProduct);
        }


        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        Button btn_cancel_Back = (Button) dialog.findViewById(R.id.btn_cancel_Back);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSelectedItems.size() == 0) {

                    DayEnd();


                } else {

                    int countOfOrderNonSelected = 0;
                    for (int countForFalse = 0; countForFalse < checks.length; countForFalse++) {
                        if (checks[countForFalse] == false) {
                            countOfOrderNonSelected++;
                        }

                    }
                    if (countOfOrderNonSelected > 0) {
                        confirmationForSubmission(String.valueOf(countOfOrderNonSelected));
                    } else {


                        whatTask = 2;
                        // -- Route Info Exec()
                        try {

                            new bgTasker().execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            //System.out.println(e);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            //System.out.println(e);
                        }
                        // --
                    }

                }

            }
        });

        btn_cancel_Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valDayEndOrChangeRoute = 0;
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);


        dialog.show();


    }

    public void DayEnd() {


        android.app.AlertDialog.Builder alertDialogSubmitConfirm = new android.app.AlertDialog.Builder(AllButtonActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.titlebar, null);
        alertDialogSubmitConfirm.setCustomTitle(view);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText(getText(R.string.PleaseConformMsg));


        View view1 = inflater.inflate(R.layout.custom_alert_dialog, null);
        view1.setBackgroundColor(Color.parseColor("#1D2E3C"));
        TextView msg_txt = (TextView) view1.findViewById(R.id.msg_txt);
        msg_txt.setText(getText(R.string.genTermDayEndAlert));
        alertDialogSubmitConfirm.setView(view1);
        alertDialogSubmitConfirm.setInverseBackgroundForced(true);


        alertDialogSubmitConfirm.setNeutralButton(R.string.AlertDialogYesButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //System.out.println("Abhinav store Selection  Step 9");
                // Location_Getting_Service.closeFlag = 1;
                //enableGPSifNot();

                // run bgTasker()!

                // if(!scheduler.isTerminated()){
                // scheduler.shutdownNow();
                // }
                dbengine.open();
                //System.out.println("Day end before");
                if (dbengine.GetLeftStoresChk() == true) {
                    //System.out.println("Abhinav store Selection  Step 10");
                    //System.out.println("Day end after");
                    // run bgTasker()!

                    // Location_Getting_Service.closeFlag = 1;
                    // scheduler.shutdownNow();

                    //enableGPSifNot();
                    // scheduler.shutdownNow();

                    dbengine.close();

                    whatTask = 3;
                    // -- Route Info Exec()
                    try {

                        new bgTasker().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    }
                    // --
                } else {
                    //System.out.println("Abhinav store Selection  Step 11");
                    // show dialog for clear..clear + tranx to launcher

                    // -- Route Info Exec()
                    try {
                        dbengine.close();
                        //System.out.println("Day end before whatTask");
                        whatTask = 1;
                        new bgTasker().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    }
                    // --

							/*dbengine.open();
							String rID=dbengine.GetActiveRouteID();
							//dbengine.updateActiveRoute(rID, 0);
							dbengine.close();
							 Intent revupOldFriend = new Intent(StoreSelection.this,LauncherActivity.class);
							 revupOldFriend.putExtra("imei", imei);
							  startActivity(revupOldFriend);
							  finish();*/

                }

            }
        });

        alertDialogSubmitConfirm.setNegativeButton(R.string.AlertDialogNoButton, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
        android.app.AlertDialog alert = alertDialogSubmitConfirm.create();
        alert.show();

    }

    public void DayEndWithoutalert() {

        dbengine.open();
        String rID = dbengine.GetActiveRouteID();

        dbengine.UpdateTblDayStartEndDetails(Integer.parseInt(rID), valDayEndOrChangeRoute);
        dbengine.close();

        SyncNow();

    }

    public void SyncNow() {

        syncTIMESTAMP = System.currentTimeMillis();
        Date dateobj = new Date(syncTIMESTAMP);


        dbengine.open();
        String presentRoute = dbengine.GetActiveRouteID();
        dbengine.close();
        //syncTIMESTAMP = System.currentTimeMillis();
        //Date dateobj = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss", Locale.ENGLISH);
        //fullFileName1 = df.format(dateobj);
        String newfullFileName = imei + "." + presentRoute + "." + df.format(dateobj);


        try {

            File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

            if (!OrderXMLFolder.exists()) {
                OrderXMLFolder.mkdirs();
            }

            String routeID = dbengine.GetActiveRouteIDSunil();

            DASFA.open();
            DASFA.export(dbengine.DATABASE_NAME, newfullFileName, routeID);


            DASFA.close();

            dbengine.savetbl_XMLfiles(newfullFileName, "3", "1");
            dbengine.open();
            for (int nosSelected = 0; nosSelected <= mSelectedItems.size() - 1; nosSelected++) {
                String valSN = (String) mSelectedItems.get(nosSelected);
                int valID = stNames.indexOf(valSN);
                String stIDneeded = stIDs.get(valID);

                dbengine.UpdateStoreFlagAtDayEndOrChangeRoute(stIDneeded, 4);
                dbengine.UpdateStoreMaterialphotoFlag(stIDneeded.trim(), 5);
                dbengine.UpdateStoreReturnphotoFlag(stIDneeded.trim(), 5);
                dbengine.updateflgFromWhereSubmitStatusAgainstStore(stIDneeded, 1);

                if (dbengine.fnchkIfStoreHasInvoiceEntry(stIDneeded) == 1) {
                    dbengine.updateStoreQuoteSubmitFlgInStoreMstrInChangeRouteCase(stIDneeded, 0);
                }


            }
            dbengine.close();

            flgChangeRouteOrDayEnd = valDayEndOrChangeRoute;

            Intent syncIntent = new Intent(AllButtonActivity.this, SyncMaster.class);
            //syncIntent.putExtra("xmlPathForSync",Environment.getExternalStorageDirectory() + "/TJUKIndirectSFAxml/" + newfullFileName + ".xml");
            syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml");
            syncIntent.putExtra("OrigZipFileName", newfullFileName);
            syncIntent.putExtra("whereTo", whereTo);
            startActivity(syncIntent);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        flgChangeRouteOrDayEnd = 0;
        if (isDayEndClicked) {
            flgChangeRouteOrDayEnd = 1;
            DayEndCode();
        }
    }

    void dayEndWorking() {
        ll_DayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(AllButtonActivity.this, DialogDayEndSummaryActivity.class);
                startActivity(in);
            }
        });

    }

    public void DayEndCode() {

        isDayEndClicked = false;

        File ImagesFileInFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.ImagesFolder);

        // check number of files in folder
        String[] PendingImagesInFolder = checkNumberOfFiles(ImagesFileInFolder);


        File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);


        // check number of files in folder
        final String[] AllFilesNameNotSync = checkNumberOfFiles(del);

        String xmlfileNames = dbengine.fnGetXMLFile("3");
        // String xmlfileNamesStrMap=dbengineSo.fnGetXMLFile("3");

        dbengine.open();
        String[] SaveStoreList = dbengine.SaveStoreList();
        dbengine.close();
        // if(xmlfileNames.length()>0 || !TextUtils.isEmpty(xmlfileNamesStrMap) || SaveStoreList.length != 0)
        if (xmlfileNames.length() > 0 || SaveStoreList.length != 0) {
            if (isOnline()) {
                whereTo = "11";
                dbengine.open();

                StoreList2Procs = dbengine.ProcessStoreReq();
                if (StoreList2Procs.length != 0) {
                    midPart();
                    dayEndCustomAlert(1);

                    dbengine.close();

                } else if (dbengine.GetLeftStoresChk() == true) {
                    DayEnd();
                    dbengine.close();
                } else {
                    DayEndWithoutalert();
                }
            } else {
                showNoConnAlert();


            }
        } else if (PendingImagesInFolder != null && PendingImagesInFolder.length > 0) {
            funSendingAllPendingImages();
        } else {

            //showAlertSingleButtonInfo(getResources().getString(R.string.NoPendingDataMsg));
            if (isOnline()) {
                whereTo = "11";
                DayEndWithoutalert();
            } else {
                showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
            }


            // showAlertForEveryOne("There is no Pending data for upload.");
                   /* if(isOnline())
                    {
                        DayEndWithoutalert();

                    }
                    else
                    {
                        showAlertForEveryOne("Internet connection required for the day End process.");
                    }*/

        }


    }


    public void funSendingAllPendingImages() {
        new SyncImgTasker().execute();
    }

    private class SyncImgTasker extends AsyncTask<Void, Void, Void> {

        int responseCode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(AllButtonActivity.this);
            mProgressDialog.setTitle("Please Wait");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {


                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.ImagesFolder);

                // check number of files in folder
                String[] AllFilesName = checkNumberOfFiles(del);


                if (AllFilesName != null && AllFilesName.length > 0) {


                    for (int vdo = 0; vdo < AllFilesName.length; vdo++) {
                        String fileUri = AllFilesName[vdo];


                        String f1 = Environment.getExternalStorageDirectory().getPath() + "/" + CommonInfo.ImagesFolder + "/" + fileUri;
                        // System.out.println("Sunil Again each file full path"+f1);
                        try {
                            responseCode = upLoad2Server(f1, fileUri);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        //if(responseCode!=200)
                        if (!the_string_response.equalsIgnoreCase("success")) {
                            break;
                        }

                    }

                } else {
                    responseCode = 200;
                }


            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            showAlertSingleButtonInfo(getResources().getString(R.string.syncDataAlertOKMsg));


        }
    }

    public int upLoad2Server(String sourceFileUri, String fileUri) {
        String currentImageFileName = fileUri;

        Bitmap bmp = BitmapFactory.decodeFile(sourceFileUri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        String image_str = BitMapToString(bmp);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        try {
            stream.flush();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        long syncTIMESTAMP = System.currentTimeMillis();
        Date datefromat = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        String onlyDate = df.format(datefromat);


        nameValuePairs.add(new BasicNameValuePair("image", image_str));
        nameValuePairs.add(new BasicNameValuePair("FileName", currentImageFileName));
        nameValuePairs.add(new BasicNameValuePair("comment", "NA"));
        nameValuePairs.add(new BasicNameValuePair("storeID", "0"));
        nameValuePairs.add(new BasicNameValuePair("date", onlyDate));
        nameValuePairs.add(new BasicNameValuePair("routeID", "0"));

        try {

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(httpParams, 0);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
//            HttpPost httppost = new HttpPost(CommonInfo.ImageSyncPath.trim());
            HttpPost httppost = new HttpPost(CommonInfo.COMMON_SYNC_PATH_URL.trim() + CommonInfo.ClientFileNameImageSyncPath);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            the_string_response = "0";
            the_string_response = convertResponseToString(response);

            System.out.println("Sunil Doing Testing Response after sending Image" + the_string_response);

            //  if(serverResponseCode == 200)
            if (the_string_response.equalsIgnoreCase("success")) {

                String file_dj_path = Environment.getExternalStorageDirectory() + "/" + CommonInfo.ImagesFolder + "/" + currentImageFileName.toString().trim();

                File fdelete = new File(file_dj_path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        callBroadCast();
                    }

                }

            }

        } catch (Exception e) {
            IMGsyOK = 1;

        }
        return serverResponseCode;
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }


    }

    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException {

        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content length�..
        //System.out.println("contentLength : " + contentLength);
        //Toast.makeText(MainActivity.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
        if (contentLength < 0) {
        } else {
            byte[] data = new byte[512];
            int len = 0;
            try {
                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer�..
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close(); // closing the stream�..
            } catch (IOException e) {
                e.printStackTrace();
            }
            res = buffer.toString();     // converting stringbuffer to string�..

            //System.out.println("Result : " + res);
            //Toast.makeText(MainActivity.this, "Result : " + res, Toast.LENGTH_LONG).show();
            ////System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }


    public String BitMapToString(Bitmap bitmap) {
        int h1 = bitmap.getHeight();
        int w1 = bitmap.getWidth();

        if (w1 > 768 || h1 > 1024) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 768, true);

        } else {

            bitmap = Bitmap.createScaledBitmap(bitmap, w1, h1, true);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] arr = baos.toByteArray();
        String result = Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }


    void dsrTrackerWorking() {
        ll_dsrTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(ll_dsrTracker.isSelected())
                    ll_dsrTracker.setSelected(false);
                else
                    ll_dsrTracker.setSelected(true);*/
                /*Intent intent=new Intent(AllButtonActivity.this,WebViewDSRTrackerActivity.class);
                startActivity(intent);*/
                openDSRTrackerAlert();

            }
        });


    }

    void distributorMapWorking() {
        ll_distrbtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(ll_distrbtnMap.isSelected())
                    ll_distrbtnMap.setSelected(false);
                else
                    ll_distrbtnMap.setSelected(true);*/

                Intent intent = new Intent(AllButtonActivity.this, DistributorMapActivity.class);
                startActivity(intent);


            }
        });
    }


    void noVisitWorking() {
        int submitFlag = CommonInfo.AnyVisit;
        noVisit_tv = (TextView) findViewById(R.id.noVisit_tv);

        int check = dbengine.fetchflgHasVisitFromtblNoVisitStoreDetails("" + 4);
        if (check == 0 && submitFlag == 0) // 0 means user did not do any visit or getStore
        {
            ll_noVisit.setEnabled(true);
        } else {
            ll_noVisit.setEnabled(false);
            String aab = dbengine.fetchReasonDescr();
            if (ReasonText.equals("") || ReasonText.equals("NA") || ReasonText.equals(null)) {

                noVisit_tv.setText("No Store Visit today");
            } else {
                noVisit_tv.setText("Reason :" + ReasonText);
            }
        }

        ll_noVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlertDialog();
            }
        });

    }

    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_nostore, null);
        final EditText et_Reason = (EditText) alertLayout.findViewById(R.id.et_Reason);
        et_Reason.setVisibility(View.INVISIBLE);

        final Spinner spinner_reason = (Spinner) alertLayout.findViewById(R.id.spinner_reason);

        ArrayAdapter adapterCategory = new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item, reasonNames);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_reason.setAdapter(adapterCategory);

        spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String spinnerReasonSelected = spinner_reason.getSelectedItem().toString();
                ReasonText = spinnerReasonSelected;
                int check = dbengine.fetchFlgToShowTextBox(spinnerReasonSelected);
                ReasonId = dbengine.fetchReasonIdBasedOnReasonDescr(spinnerReasonSelected);
                if (check == 0) {
                    et_Reason.setVisibility(View.INVISIBLE);
                } else {
                    et_Reason.setVisibility(View.VISIBLE);
                }


                //ReasonId,ReasonText
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.AlertDialogHeaderMsg);
        alert.setView(alertLayout);
        //alert.setIcon(R.drawable.info_ico);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                noVisit_tv.setEnabled(false);

                if (ReasonText.equals("") || ReasonText.equals("Select Reason")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please select the reason first.");
                    alertDialog.setIcon(R.drawable.error);
                    alertDialog.setCancelable(false);

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            displayAlertDialog();

                        }
                    });
                    alertDialog.show();
                } else {

                    // code for matching password
                    String reason;
                    TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    imei = tManager.getDeviceId();
                    if (CommonInfo.imei == null || CommonInfo.imei.trim().equals("")) {
                        imei = tManager.getDeviceId();
                        CommonInfo.imei = imei;
                    } else {
                        imei = CommonInfo.imei.trim();
                    }


                    Date pdaDate = new Date();
                    SimpleDateFormat sdfPDaDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    String CurDate = sdfPDaDate.format(pdaDate).toString().trim();

                    if (et_Reason.isShown()) {

                        if (TextUtils.isEmpty(et_Reason.getText().toString().trim())) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Please enter the reason.");
                            alertDialog.setIcon(R.drawable.error);
                            alertDialog.setCancelable(false);

                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    displayAlertDialog();

                                }
                            });
                            alertDialog.show();
                        } else {
                            ReasonText = et_Reason.getText().toString();
                            if (isOnline()) {
                                GetNoStoreVisitForDay task = new GetNoStoreVisitForDay(AllButtonActivity.this);
                                task.execute();
                            } else {
                                dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId, ReasonText);
                                dbengine.updateCurDatetblNoVisitStoreDetails(fDate);
                                dbengine.updateSstattblNoVisitStoreDetails(3);

                                //String[] aa= dbengine.fnGetALLDataInfo();
                                String aab = dbengine.fetchReasonDescr();
                                noVisit_tv.setText("Reason :" + ReasonText);


                                showNoConnAlert();

                            }


                        }
                    } else {
                        if (isOnline()) {
                            GetNoStoreVisitForDay task = new GetNoStoreVisitForDay(AllButtonActivity.this);
                            task.execute();
                        } else {
                            dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId, ReasonText);
                            dbengine.updateCurDatetblNoVisitStoreDetails(fDate);
                            dbengine.updateSstattblNoVisitStoreDetails(3);
                            showNoConnAlert();

                        }


                    }


                }

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void showNoConnAlert() {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AllButtonActivity.this);
        alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialogNoConn.setMessage(R.string.NoDataConnectionFullMsg);
        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // finish();
                    }
                });
        alertDialogNoConn.setIcon(R.drawable.error_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        try {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private class GetNoStoreVisitForDay extends AsyncTask<Void, Void, Void> {
        ServiceWorker newservice = new ServiceWorker();

        ProgressDialog pDialogGetNoStoreVisitForDay;

        public GetNoStoreVisitForDay(AllButtonActivity activity) {
            pDialogGetNoStoreVisitForDay = new ProgressDialog(activity);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialogGetNoStoreVisitForDay.setTitle(getText(R.string.PleaseWaitMsg));
            pDialogGetNoStoreVisitForDay.setMessage(getText(R.string.UploadingDataMsg));
            pDialogGetNoStoreVisitForDay.setIndeterminate(false);
            pDialogGetNoStoreVisitForDay.setCancelable(false);
            pDialogGetNoStoreVisitForDay.setCanceledOnTouchOutside(false);
            pDialogGetNoStoreVisitForDay.show();


        }

        @Override
        protected Void doInBackground(Void... params) {

            try {


                for (int mm = 1; mm < 2; mm++) {
                    if (mm == 1) {
                        newservice = newservice.getCallspSaveReasonForNoVisit(getApplicationContext(), fDate, imei, ReasonId, ReasonText);

                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                            }

                        }

                    }


                }


            } catch (Exception e) {
                Log.i("SvcMgr", "Service Execution Failed!", e);
            } finally {
                Log.i("SvcMgr", "Service Execution Completed...");
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            Log.i("SvcMgr", "Service Execution Cancelled");
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (pDialogGetNoStoreVisitForDay.isShowing()) {
                pDialogGetNoStoreVisitForDay.dismiss();
            }

            if (chkFlgForErrorToCloseApp == 0) {
                dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId, ReasonText);
                dbengine.updateCurDatetblNoVisitStoreDetails(fDate);

                dbengine.updateSstattblNoVisitStoreDetails(3);
                String aab = dbengine.fetchReasonDescr();
                noVisit_tv.setText("Reason :" + ReasonText);
            } else {


                if (RowId == 0) {
                    dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId, ReasonText);
                    dbengine.updateCurDatetblNoVisitStoreDetails(fDate);
                    dbengine.updateSstattblNoVisitStoreDetails(3);
                    String aab = dbengine.fetchReasonDescr();
                    noVisit_tv.setText("Reason :" + ReasonText);
                    ll_noVisit.setEnabled(false);

                } else {
                    dbengine.updateSstattblNoVisitStoreDetailsAfterSync(4);
                    String aab = dbengine.fetchReasonDescr();
                    noVisit_tv.setText("Reason :" + ReasonText);
                    ll_noVisit.setEnabled(false);

                }
                finishAffinity();
            }
        }
    }


    public void showAlertForEveryOne(String msg) {
        //AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(new ContextThemeWrapper(LauncherActivity.this, R.style.Dialog));
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AllButtonActivity.this);

        alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialogNoConn.setMessage(msg);
        alertDialogNoConn.setCancelable(false);
        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (GPSONOFFAlert != null && GPSONOFFAlert.isShowing()) {
            GPSONOFFAlert.dismiss();
        }
    }

    void openMarketVisitAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AllButtonActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.market_visit_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        final RadioButton rb_myVisit = (RadioButton) view.findViewById(R.id.rb_myVisit);
        final RadioButton rb_dsrVisit = (RadioButton) view.findViewById(R.id.rb_dsrVisit);
        final RadioButton rb_jointWorking = (RadioButton) view.findViewById(R.id.rb_jointWorking);
        rb_jointWorking.setVisibility(View.VISIBLE);
        final Spinner spinner_dsrVisit = (Spinner) view.findViewById(R.id.spinner_dsrVisit);
        final Spinner spinner_jointWorking = (Spinner) view.findViewById(R.id.spinner_jointWorking);

        Button btn_proceed = (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = alert.create();
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dbengine.deletetblStoreList();
                if (rb_myVisit.isChecked()) {
                    /*String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    CommonInfo.PersonNodeID=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    CommonInfo.PersonNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);*/


                    String SONodeIdAndNodeType = dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    int tempSalesmanNodeId = Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    int tempSalesmanNodeType = Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                    shardPrefForSalesman(tempSalesmanNodeId, tempSalesmanNodeType);
                    flgDataScopeSharedPref(1);

                    shardPrefForCoverageArea(0, 0);
                    flgDSRSOSharedPref(1);
                    Intent i = new Intent(AllButtonActivity.this, LauncherActivity.class);
                    startActivity(i);
                    finish();
                } else if (rb_dsrVisit.isChecked()) {
                    if (!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSM") && !SelectedDSRValue.equals("No DSM")) {

                        String DSRNodeIdAndNodeType = dbengine.fnGetDSRNodeIdAndNodeType(SelectedDSRValue);
                        int tempCoverageAreaNodeID = Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempCoverageAreaNodeType = Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                        shardPrefForCoverageArea(tempCoverageAreaNodeID, tempCoverageAreaNodeType);
                        flgDSRSOSharedPref(2);


                        String DSRPersonNodeIdAndNodeType = dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
                        int tempSalesmanNodeId = Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempSalesmanNodeType = Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                        shardPrefForSalesman(tempSalesmanNodeId, tempSalesmanNodeType);
                        flgDataScopeSharedPref(2);
                        Intent i = new Intent(AllButtonActivity.this, LauncherActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        showAlertForEveryOne("Please select DSM to Proceeds.");
                    }
                } else if (rb_jointWorking.isChecked()) {
                    if (!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSM") && !SelectedDSRValue.equals("No DSM")) {
                        String DSRNodeIdAndNodeType = dbengine.fnGetDSRNodeIdAndNodeType(SelectedDSRValue);
                        int tempCoverageAreaNodeID = Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempCoverageAreaNodeType = Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                        shardPrefForCoverageArea(tempCoverageAreaNodeID, tempCoverageAreaNodeType);
                        flgDSRSOSharedPref(4);

                        // Find GPS
                        //surbhi loc code
                        boolean isGPSokCheckInResume = false;
                        boolean isNWokCheckInResume = false;
                        isGPSokCheckInResume = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        isNWokCheckInResume = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        if (!isGPSokCheckInResume && !isNWokCheckInResume) {
                            try {
                                showSettingsAlert();
                            } catch (Exception e) {

                            }
                            isGPSokCheckInResume = false;
                            isNWokCheckInResume = false;
                        } else {
                            locationRetrievingAndDistanceCalculating();
                        }
                    } else {
                        showAlertForEveryOne("Please select DSM to Proceeds.");
                    }
                } else {
                    showAlertForEveryOne("Please select atleast one option to Proceeds.");
                }
            }
        });

        rb_myVisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_myVisit.isChecked()) {
                    rb_dsrVisit.setChecked(false);
                    rb_jointWorking.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);
                    spinner_jointWorking.setVisibility(View.GONE);
                }
            }
        });

        rb_dsrVisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_dsrVisit.isChecked()) {
                    rb_myVisit.setChecked(false);
                    rb_jointWorking.setChecked(false);
                    spinner_jointWorking.setVisibility(View.GONE);

                    ArrayAdapter adapterCategory = new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item, drsNames);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_dsrVisit.setAdapter(adapterCategory);
                    spinner_dsrVisit.setVisibility(View.VISIBLE);

                    spinner_dsrVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            SelectedDSRValue = spinner_dsrVisit.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

                }
            }
        });

        rb_jointWorking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_jointWorking.isChecked()) {
                    rb_myVisit.setChecked(false);
                    rb_dsrVisit.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);

                    ArrayAdapter adapterCategory = new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item, drsNames);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_jointWorking.setAdapter(adapterCategory);
                    spinner_jointWorking.setVisibility(View.VISIBLE);

                    spinner_jointWorking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            SelectedDSRValue = spinner_jointWorking.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                }
            }
        });

        dialog.show();
    }

    public void locationRetrievingAndDistanceCalculating() {
        appLocationService = new AppLocationService();

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire();

        pDialog2STANDBY = ProgressDialog.show(AllButtonActivity.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.rtrvng_loc), true);
        pDialog2STANDBY.setIndeterminate(true);

        pDialog2STANDBY.setCancelable(false);
        pDialog2STANDBY.show();

        if (isGooglePlayServicesAvailable()) {
            createLocationRequest();

            mGoogleApiClient = new GoogleApiClient.Builder(AllButtonActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(AllButtonActivity.this)
                    .addOnConnectionFailedListener(AllButtonActivity.this)
                    .build();
            mGoogleApiClient.connect();
        }
        //startService(new Intent(DynamicActivity.this, AppLocationService.class));
        startService(new Intent(AllButtonActivity.this, AppLocationService.class));
        Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);
        Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);
        countDownTimer = new AllButtonActivity.CoundownClass(startTime, interval);
        countDownTimer.start();

    }

    public class CoundownClass extends CountDownTimer {

        public CoundownClass(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {


            /*Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm a");
                // you can get seconds by adding  "...:ss" to it
            date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

            String localTime = date.format(currentLocalTime);*/

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
            Calendar cal = Calendar.getInstance();
            String DateTime = dateFormat.format(cal.getTime());

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            String GpsLat = "0";
            String GpsLong = "0";
            String GpsAccuracy = "0";
            String GpsAddress = "0";
            if (isGPSEnabled) {
                Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);

                if (nwLocation != null) {
                    double lattitude = nwLocation.getLatitude();
                    double longitude = nwLocation.getLongitude();
                    double accuracy = nwLocation.getAccuracy();
                    GpsLat = "" + lattitude;
                    GpsLong = "" + longitude;
                    GpsAccuracy = "" + accuracy;
                    if (isOnline()) {
                        GpsAddress = getAddressOfProviders(GpsLat, GpsLong);
                    } else {
                        GpsAddress = "NA";
                    }

                    GPSLocationLatitude = "" + lattitude;
                    GPSLocationLongitude = "" + longitude;
                    GPSLocationProvider = "GPS";
                    GPSLocationAccuracy = "" + accuracy;
                    AllProvidersLocation = "GPS=Lat:" + lattitude + "Long:" + longitude + "Acc:" + accuracy;

                }
            }

            Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);
            String NetwLat = "0";
            String NetwLong = "0";
            String NetwAccuracy = "0";
            String NetwAddress = "0";
            if (gpsLocation != null) {
                double lattitude1 = gpsLocation.getLatitude();
                double longitude1 = gpsLocation.getLongitude();
                double accuracy1 = gpsLocation.getAccuracy();

                NetwLat = "" + lattitude1;
                NetwLong = "" + longitude1;
                NetwAccuracy = "" + accuracy1;
                if (isOnline()) {
                    NetwAddress = getAddressOfProviders(NetwLat, NetwLong);
                } else {
                    NetwAddress = "NA";
                }

                NetworkLocationLatitude = "" + lattitude1;
                NetworkLocationLongitude = "" + longitude1;
                NetworkLocationProvider = "Network";
                NetworkLocationAccuracy = "" + accuracy1;
                if (!AllProvidersLocation.equals("")) {
                    AllProvidersLocation = AllProvidersLocation + "$Network=Lat:" + lattitude1 + "Long:" + longitude1 + "Acc:" + accuracy1;
                } else {
                    AllProvidersLocation = "Network=Lat:" + lattitude1 + "Long:" + longitude1 + "Acc:" + accuracy1;
                }


            }

            System.out.println("LOCATION Fused" + fusedData);

            String FusedLat = "0";
            String FusedLong = "0";
            String FusedAccuracy = "0";
            String FusedAddress = "0";

            if (!FusedLocationProvider.equals("")) {
                fnAccurateProvider = "Fused";
                fnLati = FusedLocationLatitude;
                fnLongi = FusedLocationLongitude;
                fnAccuracy = Double.parseDouble(FusedLocationAccuracy);

                FusedLat = FusedLocationLatitude;
                FusedLong = FusedLocationLongitude;
                FusedAccuracy = FusedLocationAccuracy;
                FusedLocationLatitudeWithFirstAttempt = FusedLocationLatitude;
                FusedLocationLongitudeWithFirstAttempt = FusedLocationLongitude;
                FusedLocationAccuracyWithFirstAttempt = FusedLocationAccuracy;


                if (isOnline()) {
                    FusedAddress = getAddressOfProviders(FusedLat, FusedLong);
                } else {
                    FusedAddress = "NA";
                }

                if (!AllProvidersLocation.equals("")) {
                    AllProvidersLocation = AllProvidersLocation + "$Fused=Lat:" + FusedLocationLatitude + "Long:" + FusedLocationLongitude + "Acc:" + fnAccuracy;
                } else {
                    AllProvidersLocation = "Fused=Lat:" + FusedLocationLatitude + "Long:" + FusedLocationLongitude + "Acc:" + fnAccuracy;
                }
            }


            appLocationService.KillServiceLoc(appLocationService, locationManager);
            try {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                }
            } catch (Exception e) {

            }


            fnAccurateProvider = "";
            fnLati = "0";
            fnLongi = "0";
            fnAccuracy = 0.0;

            if (!FusedLocationProvider.equals("")) {
                fnAccurateProvider = "Fused";
                fnLati = FusedLocationLatitude;
                fnLongi = FusedLocationLongitude;
                fnAccuracy = Double.parseDouble(FusedLocationAccuracy);
            }

            if (!fnAccurateProvider.equals("")) {
                if (!GPSLocationProvider.equals("")) {
                    if (Double.parseDouble(GPSLocationAccuracy) < fnAccuracy) {
                        fnAccurateProvider = "Gps";
                        fnLati = GPSLocationLatitude;
                        fnLongi = GPSLocationLongitude;
                        fnAccuracy = Double.parseDouble(GPSLocationAccuracy);
                    }
                }
            } else {
                if (!GPSLocationProvider.equals("")) {
                    fnAccurateProvider = "Gps";
                    fnLati = GPSLocationLatitude;
                    fnLongi = GPSLocationLongitude;
                    fnAccuracy = Double.parseDouble(GPSLocationAccuracy);
                }
            }

            if (!fnAccurateProvider.equals("")) {
                if (!NetworkLocationProvider.equals("")) {
                    if (Double.parseDouble(NetworkLocationAccuracy) < fnAccuracy) {
                        fnAccurateProvider = "Network";
                        fnLati = NetworkLocationLatitude;
                        fnLongi = NetworkLocationLongitude;
                        fnAccuracy = Double.parseDouble(NetworkLocationAccuracy);
                    }
                }
            } else {
                if (!NetworkLocationProvider.equals("")) {
                    fnAccurateProvider = "Network";
                    fnLati = NetworkLocationLatitude;
                    fnLongi = NetworkLocationLongitude;
                    fnAccuracy = Double.parseDouble(NetworkLocationAccuracy);
                }
            }
            checkHighAccuracyLocationMode(AllButtonActivity.this);
            // fnAccurateProvider="";
            if (fnAccurateProvider.equals("")) {
                dbengine.open();
                /*dbengine.deleteLocationTable();
                dbengine.saveTblLocationDetails("NA", "NA", "NA","NA","NA","NA","NA","NA", "NA", "NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA");
                */
                dbengine.open();
                dbengine.deleteDsrLocationDetails(String.valueOf(sharedPref.getInt("CoverageAreaNodeID", 0)), String.valueOf(sharedPref.getInt("CoverageAreaNodeType", 0)));
               /* dbengine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state,
                        fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,
                        FusedLong,FusedAccuracy,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,
                        FusedLocationLatitudeWithFirstAttempt,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
*/
                //surbhi
                dbengine.savetblDsrLocationData("NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA",
                        "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA",
                        "NA", "NA", "NA", "NA", "NA", "NA");

                dbengine.close();

                if (pDialog2STANDBY.isShowing()) {
                    pDialog2STANDBY.dismiss();
                }

            /*    ll_map.setVisibility(View.VISIBLE);
                manager= getFragmentManager();
                mapFrag = (MapFragment)manager.findFragmentById(
                        R.id.map);
                mapFrag.getView().setVisibility(View.VISIBLE);
                mapFrag.getMapAsync(DistributorMapActivity.this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.show(mapFrag);*/
                countSubmitClicked = 2;
            } else {
                String FullAddress = "0";
                if (isOnline()) {
                    FullAddress = getAddressForDynamic(fnLati, fnLongi);
                } else {
                    FullAddress = "NA";
                }
                String addr = "NA";
                String zipcode = "NA";
                String city = "NA";
                String state = "NA";


                if (!FullAddress.equals("NA")) {
                    addr = FullAddress.split(Pattern.quote("^"))[0];
                    zipcode = FullAddress.split(Pattern.quote("^"))[1];
                    city = FullAddress.split(Pattern.quote("^"))[2];
                    state = FullAddress.split(Pattern.quote("^"))[3];
                }
              /*  //surbhi
                if(!addr.equals("NA"))
                {
                    etLocalArea.setText(addr);
                }
                if(!zipcode.equals("NA"))
                {
                    etPinCode.setText(zipcode);
                }
                if(!city.equals("NA"))
                {
                    etCity.setText(city);
                }
                if(!state.equals("NA"))
                {
                    etState.setText(state);
                }*/

                if (pDialog2STANDBY.isShowing()) {
                    pDialog2STANDBY.dismiss();
                }
                if (!GpsLat.equals("0")) {
                    fnCreateLastKnownGPSLoction(GpsLat, GpsLong, GpsAccuracy);
                }

                LattitudeFromLauncher = fnLati;
                LongitudeFromLauncher = fnLongi;
                AccuracyFromLauncher = String.valueOf(fnAccuracy);
                ProviderFromLauncher = fnAccurateProvider;
/*
                GpsLatFromLauncher = GpsLat;
                GpsLongFromLauncher = GpsLong;
                GpsAccuracyFromLauncher = GpsAccuracy;

                NetworkLatFromLauncher = NetwLat;
                NetworkLongFromLauncher = NetwLong;
                NetworkAccuracyFromLauncher = NetwAccuracy;

                FusedLatFromLauncher = FusedLat;
                FusedLongFromLauncher = FusedLong;
                FusedAccuracyFromLauncher =FusedAccuracy;

                AllProvidersLocationFromLauncher = AllProvidersLocation;
                GpsAddressFromLauncher = GpsAddress;
                NetwAddressFromLauncher = NetwAddress;
                FusedAddressFromLauncher = FusedAddress;

                FusedLocationLatitudeWithFirstAttemptFromLauncher = FusedLocationLatitudeWithFirstAttempt;
                FusedLocationLongitudeWithFirstAttemptFromLauncher = FusedLocationLongitudeWithFirstAttempt;
                FusedLocationAccuracyWithFirstAttemptFromLauncher = FusedLocationAccuracyWithFirstAttempt;*/
                //LLLLL


               /* ll_map.setVisibility(View.VISIBLE);
                manager= getFragmentManager();
                mapFrag = (MapFragment)manager.findFragmentById(
                        R.id.map);
                mapFrag.getView().setVisibility(View.VISIBLE);
                mapFrag.getMapAsync(DistributorMapActivity.this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.show(mapFrag);
*/

                dbengine.open();
                dbengine.deleteDsrLocationDetails(String.valueOf(sharedPref.getInt("CoverageAreaNodeID", 0)), String.valueOf(sharedPref.getInt("CoverageAreaNodeType", 0)));
               /* dbengine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state,
                        fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,
                        FusedLong,FusedAccuracy,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,
                        FusedLocationLatitudeWithFirstAttempt,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
*/
                //surbhi

                if (GpsAddress.equals("")) {
                    GpsAddress = "NA";
                }
                if (NetwAddress.equals("")) {
                    NetwAddress = "NA";
                }
                if (FusedAddress.equals("")) {
                    FusedAddress = "NA";
                }
                dbengine.savetblDsrLocationData(String.valueOf(sharedPref.getInt("CoverageAreaNodeID", 0)), String.valueOf(sharedPref.getInt("CoverageAreaNodeType", 0)),
                        DateTime, addr, zipcode, city, state, fnLati, fnLongi, String.valueOf(fnAccuracy),
                        fnAccurateProvider, AllProvidersLocation, GpsLat, GpsLong, GpsAccuracy, GpsAddress,
                        NetwLat, NetwLong, NetwAccuracy, NetwAddress, FusedLat, FusedLong, FusedAccuracy, FusedAddress,
                        FusedLocationLatitudeWithFirstAttempt, FusedLocationLongitudeWithFirstAttempt, FusedLocationAccuracyWithFirstAttempt);

             /*   System.out.println(String.valueOf(String.valueOf(sharedPref.getInt("CoverageAreaNodeID",0)))+"--"+String.valueOf(sharedPref.getInt("CoverageAreaNodeType",0)+"--"+
                        DateTime+"--"+ addr+"--"+zipcode+"--"+ city+"--"+ state+"--"+fnLati+"--"+ fnLongi+"--"+String.valueOf(fnAccuracy)
                        +"--"+fnAccurateProvider+"--"+AllProvidersLocation+"--"+GpsLat+"--"+GpsLong+"--"+GpsAccuracy+"--"+GpsAddress+"--"+
                        NetwLat+"--"+NetwLong+"--"+NetwAccuracy+"--"+NetwAddress+"--"+FusedLat+"--"+ FusedLong+"--"+FusedAccuracy+"--"+FusedAddress+"--"+
                        FusedLocationLatitudeWithFirstAttempt+"--"+FusedLocationLongitudeWithFirstAttempt+"--"+FusedLocationAccuracyWithFirstAttempt);
*/
                dbengine.close();
                //        if(!checkLastFinalLoctionIsRepeated("28.4873276","77.1045244","22.201"))
                if (!checkLastFinalLoctionIsRepeated(LattitudeFromLauncher, LongitudeFromLauncher, AccuracyFromLauncher)) {
                    fnCreateLastKnownFinalLocation(LattitudeFromLauncher, LongitudeFromLauncher, AccuracyFromLauncher);
                    countSubmitClicked = 2;
                } else {
                    if (countSubmitClicked == 1) {
                        countSubmitClicked = 2;
                    }
                    if (countSubmitClicked == 0) {
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AllButtonActivity.this);

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
                }

                GpsLat = "0";
                GpsLong = "0";
                GpsAccuracy = "0";
                GpsAddress = "0";
                NetwLat = "0";
                NetwLong = "0";
                NetwAccuracy = "0";
                NetwAddress = "0";
                FusedLat = "0";
                FusedLong = "0";
                FusedAccuracy = "0";
                FusedAddress = "0";

                //code here
            }
        }

        @Override
        public void onTick(long arg0) {

        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

    }

    private void updateUI() {
        Location loc = mCurrentLocation;
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            FusedLocationLatitude = lat;
            FusedLocationLongitude = lng;
            FusedLocationProvider = mCurrentLocation.getProvider();
            FusedLocationAccuracy = "" + mCurrentLocation.getAccuracy();
            fusedData = "At Time: " + mLastUpdateTime +
                    "Latitude: " + lat +
                    "Longitude: " + lng +
                    "Accuracy: " + mCurrentLocation.getAccuracy() +
                    "Provider: " + mCurrentLocation.getProvider();

        } else {

        }
    }

    public String getAddressForDynamic(String latti, String longi) {


        String areaToMerge = "NA";
        Address addressTemp = null;
        String addr = "NA";
        String zipcode = "NA";
        String city = "NA";
        String state = "NA";
        String fullAddress = "";
        StringBuilder FULLADDRESS3 = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(AllButtonActivity.this, Locale.ENGLISH);

           /* AddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[3];
            CityFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[4];
            PincodeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[5];
            StateFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[6];*/
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);
            if (addresses != null && addresses.size() > 0) {
                if (addresses.get(0).getAddressLine(1) != null) {
                    addr = addresses.get(0).getAddressLine(1);
                    address = addr;
                }

                if (addresses.get(0).getLocality() != null) {
                    city = addresses.get(0).getLocality();
                    this.city = city;
                }

                if (addresses.get(0).getAdminArea() != null) {
                    state = addresses.get(0).getAdminArea();
                    this.state = state;
                }


                for (int i = 0; i < addresses.size(); i++) {
                    addressTemp = addresses.get(i);
                    if (addressTemp.getPostalCode() != null) {
                        zipcode = addressTemp.getPostalCode();
                        this.pincode = zipcode;
                        break;
                    }
                }
                if (addresses.get(0).getAddressLine(0) != null && addr.equals("NA")) {
                    String countryname = "NA";
                    if (addresses.get(0).getCountryName() != null) {
                        countryname = addresses.get(0).getCountryName();
                    }

                    addr = getAddressNewWay(addresses.get(0).getAddressLine(0), city, state, zipcode, countryname);
                }
                NewStoreFormSO recFragment = (NewStoreFormSO) getFragmentManager().findFragmentByTag("NewStoreFragment");
                if (null != recFragment) {
                    recFragment.setFreshAddress();
                }
            } else {
                FULLADDRESS3.append("NA");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return fullAddress = addr + "^" + zipcode + "^" + city + "^" + state;
        }
    }

    public String getAddressOfProviders(String latti, String longi) {

        StringBuilder FULLADDRESS2 = new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(AllButtonActivity.this, Locale.ENGLISH);


        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);

            if (addresses == null || addresses.size() == 0 || addresses.get(0).getAddressLine(0) == null) {
                FULLADDRESS2 = FULLADDRESS2.append("NA");
            } else {
                FULLADDRESS2 = FULLADDRESS2.append(addresses.get(0).getAddressLine(0));
            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        return FULLADDRESS2.toString();

    }

  /*  public String getAddressOfProviders(String latti, String longi){

        StringBuilder FULLADDRESS2 =new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(AllButtonActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);

            if (addresses == null || addresses.size()  == 0)
            {
                FULLADDRESS2=  FULLADDRESS2.append("NA");
            }
            else
            {
                for(Address address : addresses) {
                    //  String outputAddress = "";
                    for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        if(i==1)
                        {
                            FULLADDRESS2.append(address.getAddressLine(i));
                        }
                        else if(i==2)
                        {
                            FULLADDRESS2.append(",").append(address.getAddressLine(i));
                        }
                    }
                }
		      *//* //String address = addresses.get(0).getAddressLine(0);
		       String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
		       String city = addresses.get(0).getLocality();
		       String state = addresses.get(0).getAdminArea();
		       String country = addresses.get(0).getCountryName();
		       String postalCode = addresses.get(0).getPostalCode();
		       String knownName = addresses.get(0).getFeatureName();
		       FULLADDRESS=address+","+city+","+state+","+country+","+postalCode;
		      Toast.makeText(contextcopy, "ADDRESS"+address+"city:"+city+"state:"+state+"country:"+country+"postalCode:"+postalCode, Toast.LENGTH_LONG).show();*//*

            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        return FULLADDRESS2.toString();

    }*/

    public void fnCreateLastKnownGPSLoction(String chekLastGPSLat, String chekLastGPSLong, String chekLastGpsAccuracy) {

        try {

            JSONArray jArray = new JSONArray();
            JSONObject jsonObjMain = new JSONObject();


            JSONObject jOnew = new JSONObject();
            jOnew.put("chekLastGPSLat", chekLastGPSLat);
            jOnew.put("chekLastGPSLong", chekLastGPSLong);
            jOnew.put("chekLastGpsAccuracy", chekLastGpsAccuracy);


            jArray.put(jOnew);
            jsonObjMain.put("GPSLastLocationDetils", jArray);

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.AppLatLngJsonFile);
            if (!jsonTxtFolder.exists()) {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew = "GPSLastLocation.txt";
            File file = new File(jsonTxtFolder, txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory() + "/" + CommonInfo.AppLatLngJsonFile + "/" + txtFileNamenew;


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
				//fileOutputStream=contextcopy.openFileOutput("GPSLastLocation.txt", Context.MODE_PRIVATE);
				fileOutputStream.write(jsonObjMain.toString().getBytes());
				fileOutputStream.close();*/
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }
    }

    public boolean checkLastFinalLoctionIsRepeated(String currentLat, String currentLong, String currentAccuracy) {
        boolean repeatedLoction = false;

        try {

            String chekLastGPSLat = "0";
            String chekLastGPSLong = "0";
            String chekLastGpsAccuracy = "0";
            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.FinalLatLngJsonFile);
            if (!jsonTxtFolder.exists()) {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew = "FinalGPSLastLocation.txt";
            File file = new File(jsonTxtFolder, txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory() + "/" + CommonInfo.FinalLatLngJsonFile + "/" + txtFileNamenew;

            // If file does not exists, then create it
            if (file.exists()) {
                StringBuffer buffer = new StringBuffer();
                String myjson_stampiGPSLastLocation = "";
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

                myjson_stampiGPSLastLocation = sb.toString();

                JSONObject jsonObjGPSLast = new JSONObject(myjson_stampiGPSLastLocation);
                JSONArray jsonObjGPSLastInneralues = jsonObjGPSLast.getJSONArray("GPSLastLocationDetils");

                String StringjsonGPSLastnew = jsonObjGPSLastInneralues.getString(0);
                JSONObject jsonObjGPSLastnewwewe = new JSONObject(StringjsonGPSLastnew);

                chekLastGPSLat = jsonObjGPSLastnewwewe.getString("chekLastGPSLat");
                chekLastGPSLong = jsonObjGPSLastnewwewe.getString("chekLastGPSLong");
                chekLastGpsAccuracy = jsonObjGPSLastnewwewe.getString("chekLastGpsAccuracy");

                if (currentLat != null) {
                    if (currentLat.equals(chekLastGPSLat) && currentLong.equals(chekLastGPSLong) && currentAccuracy.equals(chekLastGpsAccuracy)) {
                        repeatedLoction = true;
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repeatedLoction;

    }

    public void fnCreateLastKnownFinalLocation(String chekLastGPSLat, String chekLastGPSLong, String chekLastGpsAccuracy) {

        try {

            JSONArray jArray = new JSONArray();
            JSONObject jsonObjMain = new JSONObject();


            JSONObject jOnew = new JSONObject();
            jOnew.put("chekLastGPSLat", chekLastGPSLat);
            jOnew.put("chekLastGPSLong", chekLastGPSLong);
            jOnew.put("chekLastGpsAccuracy", chekLastGpsAccuracy);


            jArray.put(jOnew);
            jsonObjMain.put("GPSLastLocationDetils", jArray);

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.FinalLatLngJsonFile);
            if (!jsonTxtFolder.exists()) {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew = "FinalGPSLastLocation.txt";
            File file = new File(jsonTxtFolder, txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory() + "/" + CommonInfo.FinalLatLngJsonFile + "/" + txtFileNamenew;


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
        } finally {

        }
    }

    public void checkHighAccuracyLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        flgLocationServicesOnOff = 0;
        flgGPSOnOff = 0;
        flgNetworkOnOff = 0;
        flgFusedOnOff = 0;
        flgInternetOnOffWhileLocationTracking = 0;

        if (isGooglePlayServicesAvailable()) {
            flgFusedOnOff = 1;
        }
        if (isOnline()) {
            flgInternetOnOffWhileLocationTracking = 1;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Equal or higher than API 19/KitKat
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
                if (locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                    flgLocationServicesOnOff = 1;
                    flgGPSOnOff = 1;
                    flgNetworkOnOff = 1;
                    //flgFusedOnOff=1;
                }
                if (locationMode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING) {
                    flgLocationServicesOnOff = 1;
                    flgGPSOnOff = 0;
                    flgNetworkOnOff = 1;
                    // flgFusedOnOff=1;
                }
                if (locationMode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY) {
                    flgLocationServicesOnOff = 1;
                    flgGPSOnOff = 1;
                    flgNetworkOnOff = 0;
                    //flgFusedOnOff=0;
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //Lower than API 19
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = Settings.Secure.LOCATION_MODE_OFF;

                flgLocationServicesOnOff = 0;
                flgGPSOnOff = 0;
                flgNetworkOnOff = 0;
                // flgFusedOnOff = 0;
            }
            if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                flgLocationServicesOnOff = 1;
                flgGPSOnOff = 1;
                flgNetworkOnOff = 1;
                //flgFusedOnOff = 0;
            } else {
                if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                    flgLocationServicesOnOff = 1;
                    flgGPSOnOff = 1;
                    flgNetworkOnOff = 0;
                    // flgFusedOnOff = 0;
                }
                if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                    flgLocationServicesOnOff = 1;
                    flgGPSOnOff = 0;
                    flgNetworkOnOff = 1;
                    //flgFusedOnOff = 0;
                }
            }
        }

    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialogGps = new android.app.AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialogGps.setTitle("Information");
        alertDialogGps.setIcon(R.drawable.error_info_ico);
        alertDialogGps.setCancelable(false);
        // Setting Dialog Message
        alertDialogGps.setMessage("GPS is not enabled. \nPlease select all settings on the next page!");

        // On pressing Settings button
        alertDialogGps.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // Showing Alert Message
        GPSONOFFAlert = alertDialogGps.create();
        GPSONOFFAlert.show();
    }


    void openReportAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AllButtonActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.report_visit_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        final RadioButton rb_myReport = (RadioButton) view.findViewById(R.id.rb_myReport);
        final RadioButton rb_dsrReport = (RadioButton) view.findViewById(R.id.rb_dsrReport);
        final RadioButton rb_WholeReport = (RadioButton) view.findViewById(R.id.rb_WholeReport);
        final Spinner spinner_dsrVisit = (Spinner) view.findViewById(R.id.spinner_dsrVisit);

        final RadioButton rb_distrbtrScope = (RadioButton) view.findViewById(R.id.rb_distrbtrScope);
        final Spinner spinner_distrbtrScope = (Spinner) view.findViewById(R.id.spinner_distrbtrScope);

        Button btn_proceed = (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = alert.create();
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (rb_myReport.isChecked()) {
                    String SONodeIdAndNodeType = dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    int tempSalesmanNodeId = Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    int tempSalesmanNodeType = Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                    shardPrefForSalesman(tempSalesmanNodeId, tempSalesmanNodeType);

                    flgDataScopeSharedPref(1);
                   /* CommonInfo.SalesmanNodeId=0;
                    CommonInfo.SalesmanNodeType=0;
                    CommonInfo.flgDataScope=1;*/
                    Intent i = new Intent(AllButtonActivity.this, DetailReportSummaryActivityForAll.class);
                    startActivity(i);
                    finish();
                } else if (rb_WholeReport.isChecked()) {
                    /*String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    CommonInfo.PersonNodeID=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    CommonInfo.PersonNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);*/

                    shardPrefForSalesman(0, 0);
                    flgDataScopeSharedPref(3);
                    Intent i = new Intent(AllButtonActivity.this, DetailReportSummaryActivityForAll.class);
                    startActivity(i);
                    finish();
                } else if (rb_dsrReport.isChecked()) {
                    if (!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSM") && !SelectedDSRValue.equals("No DSM")) {

                        String DSRNodeIdAndNodeType = dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
                        int tempSalesmanNodeId = Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempSalesmanNodeType = Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                        shardPrefForSalesman(tempSalesmanNodeId, tempSalesmanNodeType);
                        flgDataScopeSharedPref(2);

                        Intent i = new Intent(AllButtonActivity.this, DetailReportSummaryActivityForAll.class);
                        startActivity(i);
                        finish();
                    } else {
                    }
                } else if (rb_distrbtrScope.isChecked()) {
                    if (!SelectedDistrbtrName.equals("") && !SelectedDistrbtrName.equals("Select Distributor") && !SelectedDistrbtrName.equals("No Distributor")) {
                        String DbrNodeIdAndNodeType = hmapDistrbtrList.get(SelectedDistrbtrName);
                        int tempSalesmanNodeId = Integer.parseInt(DbrNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempSalesmanNodeType = Integer.parseInt(DbrNodeIdAndNodeType.split(Pattern.quote("^"))[1]);

                        shardPrefForSalesman(tempSalesmanNodeId, tempSalesmanNodeType);

                        flgDataScopeSharedPref(4);
                        Intent i = new Intent(AllButtonActivity.this, DetailReportSummaryActivityForAll.class);
                        startActivity(i);
                        finish();
                    } else {
                        showAlertForEveryOne("Please select Distributor to Proceed.");
                    }
                } else {
                    showAlertForEveryOne("Please select atleast one option to Proceed.");
                }
            }
        });

        rb_myReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_myReport.isChecked()) {
                    rb_dsrReport.setChecked(false);
                    rb_WholeReport.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);
                    rb_distrbtrScope.setChecked(false);
                    spinner_distrbtrScope.setVisibility(View.GONE);
                }
            }
        });
        rb_WholeReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_WholeReport.isChecked()) {
                    rb_dsrReport.setChecked(false);
                    rb_myReport.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);
                    rb_distrbtrScope.setChecked(false);
                    spinner_distrbtrScope.setVisibility(View.GONE);
                }
            }
        });

        rb_dsrReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_dsrReport.isChecked()) {
                    rb_myReport.setChecked(false);
                    rb_WholeReport.setChecked(false);
                    rb_distrbtrScope.setChecked(false);
                    spinner_distrbtrScope.setVisibility(View.GONE);

                    ArrayAdapter adapterCategory = new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item, drsNames);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_dsrVisit.setAdapter(adapterCategory);
                    spinner_dsrVisit.setVisibility(View.VISIBLE);

                    spinner_dsrVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            SelectedDSRValue = spinner_dsrVisit.getSelectedItem().toString();
                            /*ReasonText=spinnerReasonSelected;
                            int check=dbengine.fetchFlgToShowTextBox(spinnerReasonSelected);
                            ReasonId=dbengine.fetchReasonIdBasedOnReasonDescr(spinnerReasonSelected);
                            if(check==0)
                            {
                                et_Reason.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                et_Reason.setVisibility(View.VISIBLE);
                            }*/


                            //ReasonId,ReasonText
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                }
            }
        });

        rb_distrbtrScope.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_distrbtrScope.isChecked()) {
                    rb_myReport.setChecked(false);
                    rb_WholeReport.setChecked(false);
                    rb_dsrReport.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);

                    ArrayAdapter adapterCategory = new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item, DbrArray);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_distrbtrScope.setAdapter(adapterCategory);
                    spinner_distrbtrScope.setVisibility(View.VISIBLE);

                    spinner_distrbtrScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            SelectedDistrbtrName = spinner_distrbtrScope.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });
                }
            }
        });


        dialog.show();
    }

    void storeValidationWorking() {
        ll_storeVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetStoreAllData getStoreAllDataAsync = new GetStoreAllData(AllButtonActivity.this);
                getStoreAllDataAsync.execute();
               /* Intent i=new Intent(AllButtonActivity.this,StorelistActivity.class);
                startActivity(i);
                finish();*/


                /*if(ll_storeVal.isSelected()) {
                    ll_storeVal.setSelected(false);
                }
                else {
                    ll_storeVal.setSelected(true);
                }*/


            }
        });
    }

    void distributorStockWorking() {
        ll_distrbtrStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbengine.open();
                dbengine.maintainPDADate();
                String getPDADate = dbengine.fnGetPdaDate();
                String getServerDate = dbengine.fnGetServerDate();

                dbengine.close();


                if (!getServerDate.equals(getPDADate)) {
                    if (isOnline()) {

                        try {
                            click_but_distribtrStock = 1;
                            FullSyncDataNow task = new FullSyncDataNow(AllButtonActivity.this);
                            task.execute();
                        } catch (Exception e) {

                        }
                    } else {
                        showNoConnAlertDistributor();
                    }
                } else {
                    //changes
                    if (imei == null) {
                        imei = CommonInfo.imei;
                    }
                    if (fDate == null) {
                        Date date1 = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        fDate = sdf.format(date1).toString().trim();
                    }

                    Intent i = new Intent(AllButtonActivity.this, DistributorEntryActivity.class);
                    i.putExtra("imei", imei);
                    i.putExtra("CstmrNodeId", CstmrNodeId);
                    i.putExtra("CstomrNodeType", CstomrNodeType);
                    i.putExtra("fDate", fDate);
                    startActivity(i);
                    // finish();
                }
            }
        });
    }

    public void showNoConnAlertDistributor() {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AllButtonActivity.this);
        alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
        alertDialogNoConn.setMessage(R.string.genTermNoDataConnectionFullMsg);
        alertDialogNoConn.setNeutralButton(R.string.txtOk,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // finish();
                    }
                });
        alertDialogNoConn.setIcon(R.drawable.error_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }

    void executionWorking() {
        ll_execution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    try {
                        ll_execution.setEnabled(false);
                        GetInvoiceForDay task = new GetInvoiceForDay(AllButtonActivity.this);
                        task.execute();


                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                } else {
                    showNoConnAlert();
                }
            }
        });
    }

    private class GetInvoiceForDay extends AsyncTask<Void, Void, Void> {
        ServiceWorker newservice = new ServiceWorker();

        ProgressDialog pDialogGetInvoiceForDay;

        public GetInvoiceForDay(AllButtonActivity activity) {
            pDialogGetInvoiceForDay = new ProgressDialog(activity);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialogGetInvoiceForDay.setTitle(getText(R.string.PleaseWaitMsg));
            pDialogGetInvoiceForDay.setMessage(getText(R.string.RetrivingDataMsg));
            pDialogGetInvoiceForDay.setIndeterminate(false);
            pDialogGetInvoiceForDay.setCancelable(false);
            pDialogGetInvoiceForDay.setCanceledOnTouchOutside(false);
            pDialogGetInvoiceForDay.show();


        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                HashMap<String, String> hmapInvoiceOrderIDandStatus = new HashMap<String, String>();
                hmapInvoiceOrderIDandStatus = dbengine.fetchHmapInvoiceOrderIDandStatus();

                for (int mm = 1; mm < 5; mm++) {
                    if (mm == 1) {
                        newservice = newservice.callInvoiceButtonStoreMstr(getApplicationContext(), fDate, imei, rID, hmapInvoiceOrderIDandStatus);

                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                            }

                        }

                    }
                    if (mm == 2) {
                        newservice = newservice.callInvoiceButtonProductMstr(getApplicationContext(), fDate, imei, rID);

                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                            }

                        }

                    }
                    if (mm == 3) {
                        newservice = newservice.callInvoiceButtonStoreProductwiseOrder(getApplicationContext(), fDate, imei, rID, hmapInvoiceOrderIDandStatus);
                    }
                    if (mm == 4) {
                        dbengine.open();
                        int check1 = dbengine.counttblCatagoryMstr();
                        dbengine.close();
                        if (check1 == 0) {
                            newservice = newservice.getCategory(getApplicationContext(), imei);
                        }
                    }


                }


            } catch (Exception e) {
                Log.i("SvcMgr", "Service Execution Failed!", e);
            } finally {
                Log.i("SvcMgr", "Service Execution Completed...");
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            Log.i("SvcMgr", "Service Execution Cancelled");
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (pDialogGetInvoiceForDay.isShowing()) {
                pDialogGetInvoiceForDay.dismiss();
            }

            Intent storeIntent = new Intent(AllButtonActivity.this, InvoiceStoreSelection.class);
            storeIntent.putExtra("imei", imei);
            storeIntent.putExtra("userDate", currSysDate);
            storeIntent.putExtra("pickerDate", fDate);


            if (chkFlgForErrorToCloseApp == 0) {
                chkFlgForErrorToCloseApp = 0;
                startActivity(storeIntent);
                finish();
            } else {
                AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AllButtonActivity.this);
                alertDialogNoConn.setTitle("Information");
                alertDialogNoConn.setMessage("There is no Invoice Pending");
                alertDialogNoConn.setCancelable(false);
                alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // but_Invoice.setEnabled(true);
                                chkFlgForErrorToCloseApp = 0;
                            }
                        });
                alertDialogNoConn.setIcon(R.drawable.info_ico);
                AlertDialog alert = alertDialogNoConn.create();
                alert.show();
                return;

            }
        }
    }

    private class GetStoreAllData extends AsyncTask<Void, Void, Void> {

        public GetStoreAllData(AllButtonActivity activity) {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialogGetStores.setTitle(getText(R.string.PleaseWaitMsg));
            pDialogGetStores.setMessage(getText(R.string.RetrivingDataMsg));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);
                int DatabaseVersion = dbengine.DATABASE_VERSION;
                int ApplicationID = dbengine.Application_TypeID;

                dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);
                for (int mm = 1; mm < 5; mm++) {
                    if (mm == 1) {
                        newservice = newservice.getSOSummary(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }
                    }
                    if (mm == 2) {
                        newservice = newservice.getStoreAllDetails(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }


                    }
                    if (mm == 3) {
                        newservice = newservice.callfnSingleCallAllWebServiceSO(getApplicationContext(), ApplicationID, imei);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }

                    }
                    if (mm == 4) {
                        newservice = newservice.getQuotationDataFromServer(getApplicationContext(), fDate, imei, "0");
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }

                    }

                }


            } catch (Exception e) {
            } finally {
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (pDialogGetStores.isShowing()) {
                    pDialogGetStores.dismiss();
                }
            } catch (Exception e) {

            }

            if (chkFlgForErrorToCloseApp == 1)   // if Webservice showing exception or not excute complete properly
            {
                chkFlgForErrorToCloseApp = 0;
                SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor ed;
                if (sharedPreferences.contains("ServerDate")) {
                    ed = sharedPreferences.edit();
                    ed.putString("ServerDate", "0");
                    ed.commit();
                }
                //clear sharedpreferences
                intentPassToLauncherActivity("Internet connection is slow ,please try again.");//, "0", "0", "0"
            } else {
                dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(0);
                Intent i = new Intent(AllButtonActivity.this, StorelistActivity.class);
                startActivity(i);
                finish();

            }
        }

    }

    public void intentPassToLauncherActivity(String errorMessageFlag) {
        if (errorMessageFlag.equals("0")) {
            Intent intent = new Intent(AllButtonActivity.this, StorelistActivity.class);
            AllButtonActivity.this.startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(AllButtonActivity.this, StorelistActivity.class);
            AllButtonActivity.this.startActivity(intent);
            finish();

        }

    }


    private class FullSyncDataNow extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialogGetStores;

        public FullSyncDataNow(AllButtonActivity activity) {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage("Loading Data...");
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();


        }

        @Override

        protected Void doInBackground(Void... params) {

            int Outstat = 3;

            long syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
            String StampEndsTime = df.format(dateobj);


            dbengine.open();
            String presentRoute = dbengine.GetActiveRouteIDForDistributor();
            dbengine.close();

            SimpleDateFormat df1 = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss", Locale.ENGLISH);
            newfullFileName = imei + "." + presentRoute + "." + df1.format(dateobj);

            try {

                File MeijiDistributorEntryXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.DistributorXMLFolder);
                if (!MeijiDistributorEntryXMLFolder.exists()) {
                    MeijiDistributorEntryXMLFolder.mkdirs();
                }

                int checkNoFiles = dbengine.counttblDistributorSavedData();
                if (checkNoFiles == 1) {
                    String routeID = dbengine.GetActiveRouteIDSunil();
                    DA.open();
                    DA.export(CommonInfo.DATABASE_NAME, newfullFileName, routeID);
                    DA.close();

                }

                dbengine.open();
                dbengine.maintainPDADate();
                String getPDADate = dbengine.fnGetPdaDate();
                String getServerDate = dbengine.fnGetServerDate();

                dbengine.close();

                //dbengine.deleteDistributorStockTbles();
                if (!getServerDate.equals(getPDADate)) {
                    dbengine.deleteDistributorStockTbles();
                }


            } catch (Exception e) {

                e.printStackTrace();
                if (pDialogGetStores.isShowing()) {
                    pDialogGetStores.dismiss();
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialogGetStores.isShowing()) {
                pDialogGetStores.dismiss();
            }

            try {

                task2 = new SyncXMLfileData(AllButtonActivity.this);
                task2.execute();
            } catch (Exception e) {

            }


        }
    }


    private class SyncXMLfileData extends AsyncTask<Void, Void, Integer> {


        public SyncXMLfileData(AllButtonActivity activity) {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            File MeijiIndirectSFAxmlFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.DistributorXMLFolder);

            if (!MeijiIndirectSFAxmlFolder.exists()) {
                MeijiIndirectSFAxmlFolder.mkdirs();
            }

            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));

            pDialogGetStores.setMessage("Loading Data...");

            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();

        }

        @Override
        protected Integer doInBackground(Void... params) {


            // This method used for sending xml from Folder without taking records in DB.

            // Sending only one xml at a times

            File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.DistributorXMLFolder);

            // check number of files in folder
            String[] AllFilesName = checkNumberOfFiles(del);


            if (AllFilesName != null && AllFilesName.length > 0) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


                for (int vdo = 0; vdo < AllFilesName.length; vdo++) {
                    String fileUri = AllFilesName[vdo];


                    System.out.println("Sunil Again each file Name :" + fileUri);

                    if (fileUri.contains(".zip")) {
                        File file = new File(fileUri);
                        file.delete();
                    } else {
                        String f1 = Environment.getExternalStorageDirectory().getPath() + "/" + CommonInfo.DistributorXMLFolder + "/" + fileUri;
                        System.out.println("Sunil Again each file full path" + f1);
                        try {
                            upLoad2ServerXmlFiles(f1, fileUri);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

            } else {

            }


            // pDialogGetStores.dismiss();

            return serverResponseCode;
        }

        @Override
        protected void onCancelled() {
            //	Log.i("SyncMasterForDistributor", "Sync Cancelled");
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (!isFinishing()) {

                //	Log.i("SyncMasterForDistributor", "Sync cycle completed");


                if (pDialogGetStores.isShowing()) {
                    pDialogGetStores.dismiss();
                }


            }
            if (click_but_distribtrStock == 1) {
                click_but_distribtrStock = 0;
                //changes
                if (imei == null) {
                    imei = CommonInfo.imei;
                }
                if (fDate == null) {
                    Date date1 = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    fDate = sdf.format(date1).toString().trim();
                }
                Intent i = new Intent(AllButtonActivity.this, DistributorEntryActivity.class);
                i.putExtra("imei", imei);
                i.putExtra("CstmrNodeId", CstmrNodeId);
                i.putExtra("CstomrNodeType", CstomrNodeType);
                i.putExtra("fDate", fDate);
                startActivity(i);
                //  finish();
            }
        }


    }


    public int upLoad2ServerXmlFiles(String sourceFileUri, String fileUri) {

        fileUri = fileUri.replace(".xml", "");

        String fileName = fileUri;
        String zipFileName = fileUri;

        String newzipfile = Environment.getExternalStorageDirectory() + "/" + CommonInfo.DistributorXMLFolder + "/" + fileName + ".zip";
        ///storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.zip

        sourceFileUri = newzipfile;

        xmlForWeb[0] = Environment.getExternalStorageDirectory() + "/" + CommonInfo.DistributorXMLFolder + "/" + fileName + ".xml";
        //[/storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.xml]

        try {
            zip(xmlForWeb, newzipfile);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            //java.io.FileNotFoundException: /359648069495987.2.21.04.2016.12.44.02: open failed: EROFS (Read-only file system)
        }


        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        File file2send = new File(newzipfile);

//        String urlString = CommonInfo.DistributorSyncPath.trim()+"?CLIENTFILENAME=" + zipFileName;
        String urlString = CommonInfo.COMMON_SYNC_PATH_URL.trim() + CommonInfo.ClientFileNameDistributorSyncPath + "&CLIENTFILENAME=" + zipFileName;

        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(file2send);
            URL url = new URL(urlString);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("zipFileName", zipFileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + zipFileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {
						  /* dbengine.open();
						   dbengine.upDateXMLFileFlag(fileUri, 4);
						   dbengine.close();*/

                //new File(dir, fileUri).delete();
                syncFLAG = 1;

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                // editor.remove(xmlForWeb[0]);
                editor.putString(fileUri, "" + 4);
                editor.commit();

                String FileSyncFlag = pref.getString(fileUri, "" + 1);

                delXML(xmlForWeb[0].toString());
						   		/*dbengine.open();
					            dbengine.deleteXMLFileRow(fileUri);
					            dbengine.close();*/

            } else {
                syncFLAG = 0;
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return serverResponseCode;

    }

    public void delXML(String delPath) {
        File file = new File(delPath);
        file.delete();
        File file1 = new File(delPath.toString().replace(".xml", ".zip"));
        file1.delete();
    }

    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        final int BUFFER_SIZE = 2048;

        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        } finally {
            out.close();
        }
    }

    public static String[] checkNumberOfFiles(File dir) {
        int NoOfFiles = 0;
        String[] Totalfiles = null;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            NoOfFiles = children.length;
            Totalfiles = new String[children.length];

            for (int i = 0; i < children.length; i++) {
                Totalfiles[i] = children[i];
            }
        }
        return Totalfiles;
    }

    private class bgTasker extends AsyncTask<Void, Void, Void> {

        // obj(s) for services/sync..blah..blah

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //System.out.println("starting bgTasker Exec().....: ");


                dbengine.open();
                String rID = dbengine.GetActiveRouteID();
                dbengine.UpdateTblDayStartEndDetails(Integer.parseInt(rID), valDayEndOrChangeRoute);
                //System.out.println("TblDayStartEndDetails Background: "+ rID);
                //System.out.println("TblDayStartEndDetails Background valDayEndOrChangeRoute: "+ valDayEndOrChangeRoute);
                dbengine.close();

                //System.out.println("Induwati   whatTask :"+whatTask);

                if (whatTask == 2) {
                    whatTask = 0;
                    // stores with Sstat = 1 !
                    dbengine.open();
                    // dbengine.fnTruncateTblSelectedStoreIDinChangeRouteCase();
                    for (int nosSelected = 0; nosSelected <= mSelectedItems.size() - 1; nosSelected++) {
                        String valSN = (String) mSelectedItems.get(nosSelected);
                        int valID = stNames.indexOf(valSN);
                        String stIDneeded = stIDs.get(valID);

                        // String[] stIDs;
                        // String[] stNames;

                        dbengine.UpdateStoreFlagAtDayEndOrChangeRoute(stIDneeded, 3);
                        dbengine.UpdateStoreReturnphotoFlag(stIDneeded.trim(), 3);
                        //System.out.println("stIDneeded : " + stIDneeded);
                        dbengine.insertTblSelectedStoreIDinChangeRouteCase(stIDneeded);
                        dbengine.updateflgFromWhereSubmitStatusAgainstStore(stIDneeded, 1);
                        if (dbengine.fnchkIfStoreHasInvoiceEntry(stIDneeded) == 1) {
                            dbengine.updateStoreQuoteSubmitFlgInStoreMstrInChangeRouteCase(stIDneeded, 0);
                        }
                    }
                    // dbengine.ProcessStoreReq();
                    dbengine.close();

                    pDialog2.dismiss();
                    dbengine.open();

                    //dbengine.updateActiveRoute(rID, 0);
                    dbengine.close();
                    // sync here


                    SyncNow();


                } else if (whatTask == 3) {
                    // sync rest
                    whatTask = 0;

                    pDialog2.dismiss();
                    //dbengine.open();
                    //String rID=dbengine.GetActiveRouteID();
                    //dbengine.updateActiveRoute(rID, 0);
                    //dbengine.close();
                    // sync here

                    SyncNow();


                    /*
                     * dbengine.open(); dbengine.reCreateDB(); dbengine.close();
                     */
                } else if (whatTask == 1) {
                    // clear all
                    whatTask = 0;

                    SyncNow();

                    dbengine.open();
                    //String rID=dbengine.GetActiveRouteID();
                    //dbengine.updateActiveRoute(rID, 0);
                    dbengine.reCreateDB();

                    dbengine.close();
                }/*else if (whatTask == 0)
				{
					try {
						new FullSyncDataNow().execute().get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/


            } catch (Exception e) {
                Log.i("bgTasker", "bgTasker Execution Failed!", e);

            } finally {

                Log.i("bgTasker", "bgTasker Execution Completed...");

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog2 = ProgressDialog.show(AllButtonActivity.this, getText(R.string.PleaseWaitMsg), getText(R.string.genTermProcessingRequest), true);
            pDialog2.setIndeterminate(true);
            pDialog2.setCancelable(false);
            pDialog2.show();

        }

        @Override
        protected void onCancelled() {
            Log.i("bgTasker", "bgTasker Execution Cancelled");
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Log.i("bgTasker", "bgTasker Execution cycle completed");
            pDialog2.dismiss();
            whatTask = 0;

        }
    }


    public void shardPrefForCoverageArea(int coverageAreaNodeID, int coverageAreaNodeType) {


        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("CoverageAreaNodeID", coverageAreaNodeID);
        editor.putInt("CoverageAreaNodeType", coverageAreaNodeType);


        editor.commit();

    }


    public void shardPrefForSalesman(int salesmanNodeId, int salesmanNodeType) {


        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("SalesmanNodeId", salesmanNodeId);
        editor.putInt("SalesmanNodeType", salesmanNodeType);

        editor.commit();

    }

    public void flgDataScopeSharedPref(int _flgDataScope) {
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("flgDataScope", _flgDataScope);
        editor.commit();


    }

    public void flgDSRSOSharedPref(int _flgDSRSO) {
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("flgDSRSO", _flgDSRSO);
        editor.commit();


    }


    void openDSRTrackerAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AllButtonActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dsr_tracker_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        final RadioButton rb_dataReport = (RadioButton) view.findViewById(R.id.rb_dataReport);
        final RadioButton rb_onMap = (RadioButton) view.findViewById(R.id.rb_onMap);


        Button btn_proceed = (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = alert.create();
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (rb_dataReport.isChecked()) {
                    Intent i = new Intent(AllButtonActivity.this, WebViewDSRDataReportActivity.class);
                    startActivity(i);

                } else if (rb_onMap.isChecked()) {
                    Intent i = new Intent(AllButtonActivity.this, WebViewDSRTrackerActivity.class);
                    startActivity(i);

                } else {
                    showAlertForEveryOne("Please select atleast one option to Proceeds.");
                }
            }
        });

        rb_dataReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_dataReport.isChecked()) {
                    rb_onMap.setChecked(false);

                }
            }
        });

        rb_onMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_onMap.isChecked()) {
                    rb_dataReport.setChecked(false);

                }
            }
        });


        dialog.show();
    }


    public void showAlertBox(String msg) {
        android.app.AlertDialog.Builder alertDialogNoConn = new android.app.AlertDialog.Builder(AllButtonActivity.this);
        alertDialogNoConn.setTitle("Information");
        alertDialogNoConn.setMessage(msg);

        alertDialogNoConn.setNeutralButton(R.string.txtOk, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        android.app.AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }

    public void showAlertException(String title, String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AllButtonActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.error);
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                new GetStoresForDay(AllButtonActivity.this).execute();
                dialog.dismiss();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private class GetStoresForDay extends AsyncTask<Void, Void, Void> {


        public GetStoresForDay(AllButtonActivity activity) {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dbengine.open();
            String getPDADate = dbengine.fnGetPdaDate();
            String getServerDate = dbengine.fnGetServerDate();
            rID = dbengine.GetActiveRouteID();


            dbengine.close();

            //System.out.println("Checking  Oncreate Date PDA GetStoresForDay:"+getPDADate);
            //System.out.println("Checking  Oncreate Date Server GetStoresForDay :"+getServerDate);

            if (!getPDADate.equals(""))  // || !getPDADate.equals("NA") || !getPDADate.equals("Null") || !getPDADate.equals("NULL")
            {
                if (!getServerDate.equals(getPDADate)) {

                    showAlertBox("Your Phone  Date is not Up to date.Please Correct the Date.");
                   /* dbengine.open();
                    dbengine.maintainPDADate();
                    dbengine.reCreateDB();
                    dbengine.close();*/
                    return;
                }
            }


            dbengine.open();
            dbengine.fnSetAllRouteActiveStatus();
            dbengine.updateActiveRoute(rID, 1);

            //rID="17^18^19";

			/*StringTokenizer st = new StringTokenizer(rID, "^");

    		while (st.hasMoreElements())
    		{
    			//System.out.println("Anand StringTokenizer Output: ");
    			dbengine.updateActiveRoute(st.nextElement().toString(), 1);
    		}*/


            long syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
            String startTS = df.format(dateobj);

            int DayEndFlg = 0;
            int ChangeRouteFlg = 0;

            int DatabaseVersion = dbengine.DATABASE_VERSION;
            String AppVersionID = dbengine.AppVersionID;
            dbengine.insertTblDayStartEndDetails(imei, startTS, rID, DayEndFlg, ChangeRouteFlg, fDate, AppVersionID);//DatabaseVersion;//getVersionNumber
            dbengine.close();


            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getText(R.string.genTermRetrivingData));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            ServiceWorker newservice = new ServiceWorker();

            try {
                for (int mm = 1; mm < 41; mm++) {
                    //System.out.println("mm print..."+mm);
                    if (mm == 1) {
                        newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 1) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 2) {

                        newservice = newservice.getallProduct(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 2) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 3) {

                        newservice = newservice.getCategory(getApplicationContext(), imei);
                        if (newservice.flagExecutedServiceSuccesfully != 3) {
                            serviceException = true;
                            break;
                        }

                    }
                    if (mm == 4) {

                        Date currDateNew = new Date();
                        SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                        String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
                        newservice = newservice.getAllNewSchemeStructure(getApplicationContext(), currSysDateNew, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 4) {
                            serviceException = true;
                            break;
                        }

                    }
                    if (mm == 5) {

                        Date currDateNew = new Date();
                        SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                        String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
                        newservice = newservice.getallPDASchAppListForSecondPage(getApplicationContext(), currSysDateNew, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 5) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 6) {
					/*Date currDateNew = new Date();
					SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

					String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
					newservice = newservice.getAllPOSMaterialStructure(getApplicationContext(), currSysDateNew, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=4)
					{
						serviceException=true;
						break;
					}*/

                    }
                    if (mm == 7) {



					/*Date currDateNew = new Date();
					SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

					String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
					newservice = newservice.callGetLastVisitPOSDetails(getApplicationContext(), currSysDateNew, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=4)
					{
						serviceException=true;
						break;
					}*/


                    }
                    if (mm == 8) {

                    }
                    if (mm == 9) {
                        newservice = newservice.fnGetPDACollectionMaster(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 40) {
                            System.out.println("GRLTyre = " + mm);
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 10) {

                    }
                    if (mm == 11) {

                    }
                    if (mm == 12) {

                    }
                    if (mm == 13) {

                    }
                    if (mm == 14) {

                    }
                    if (mm == 15) {

                    }
                    if (mm == 16) {

                    }
                    if (mm == 17) {

                    }
                    if (mm == 18) {

                    }
                    if (mm == 19) {

                    }
                    if (mm == 20) {

                    }
                    if (mm == 21) {
                        newservice = newservice.GetPDAIsSchemeApplicable(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 21) {
                            serviceException = true;
                            break;
                        }

                    }

                    if (mm == 22) {
						/*newservice = newservice.getallPDAtblSyncSummuryDetails(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=22)
						{
							serviceException=true;
							break;
						}
						*/
                    }
                    if (mm == 23) {
                        //newservice = newservice.getallPDAtblSyncSummuryForProductDetails(getApplicationContext(), fDate, imei, rID);
                    }
                    if (mm == 24) {
					/*newservice = newservice.GetSchemeCoupon(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=24)
					{
						serviceException="GetSchemeCoupon";
						break;
					}*/
                    }
                    if (mm == 25) {
				/*	newservice = newservice.GetSchemeCouponSlab(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=25)
					{
						serviceException="GetSchemeCouponSlab";
						break;
					}*/
                    }
                    if (mm == 26) {
				/*	newservice = newservice.GetDaySummaryNew(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=26)
					{
						serviceException="GetDaySummaryNew";
						break;
					}*/
                    }
                    if (mm == 27) {/*
					newservice = newservice.GetOrderDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=27)
					{
						serviceException="GetOrderDetailsOnLastSalesSummary";
						break;
					}
					*/
                    }
                    if (mm == 28) {
				/*	newservice = newservice.GetVisitDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=28)
					{
						serviceException="GetVisitDetailsOnLastSalesSummary";
						break;
					}*/
                    }
                    if (mm == 29) {
                        newservice = newservice.GetLODDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 29) {
                            serviceException = true;
                            break;
                        }
                    }

                    if (mm == 31) {
                        newservice = newservice.GetCallspForPDAGetLastVisitDate(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 31) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 32) {
                        newservice = newservice.GetCallspForPDAGetLastOrderDate(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 32) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 33) {
                        newservice = newservice.GetCallspForPDAGetLastVisitDetails(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 33) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 34) {
                        newservice = newservice.GetCallspForPDAGetLastOrderDetails(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 34) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 35) {
                        newservice = newservice.GetCallspForPDAGetLastOrderDetails_TotalValues(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 35) {
                            serviceException = true;
                            break;
                        }
                    }
                    if (mm == 36) {
                        newservice = newservice.GetCallspForPDAGetExecutionSummary(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 36) {
                            serviceException = true;
                            break;
                        }
                    }

                    if (mm == 37) {
                        newservice = newservice.getQuotationDataFromServer(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 37) {
                            serviceException = true;
                            break;
                        }
                    }

				/*if(mm==38)
				{
					newservice = newservice.fnGetStoreListWithPaymentAddressMR(getApplicationContext(), fDate, imei, rID);

				}*/

                    if (mm == 38) {
                        newservice = newservice.getFSDOutletFeedbackData(getApplicationContext(), fDate, imei, rID);
                        if (newservice.flagExecutedServiceSuccesfully != 38) {
                            serviceException = true;
                            break;
                        }

                    }
                    if (mm == 39) {
                        newservice = newservice.fnGetAccountLastSummary(getApplicationContext(), fDate, imei, rID);

                    }
                    if (mm == 40) {
                        newservice = newservice.fnGetFeedbckCompetitorData(getApplicationContext(), fDate, imei, rID);

                    }

                }


            } catch (Exception e) {
                Log.i("SvcMgr", "Service Execution Failed!", e);
            } finally {
                Log.i("SvcMgr", "Service Execution Completed...");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Log.i("SvcMgr", "Service Execution cycle completed");

            try {
                if (pDialogGetStores.isShowing()) {
                    pDialogGetStores.dismiss();
                }
            } catch (Exception e) {

            }
            if (serviceException) {
                try {
                    // but_GetStore.setEnabled(true);
                    showAlertException("Error.....", "Error while Retrieving Data!\n Please Retry");
                } catch (Exception e) {

                }
                serviceException = false;
               /* dbengine.open();
                serviceException=false;
                dbengine.maintainPDADate();
                dbengine.dropRoutesTBL();
                dbengine.reCreateDB();

                dbengine.close();*/
            } else {
                Intent storeIntent = new Intent(AllButtonActivity.this, StoreSelection.class);
                storeIntent.putExtra("imei", imei);
                storeIntent.putExtra("userDate", currSysDate);
                storeIntent.putExtra("pickerDate", fDate);
                storeIntent.putExtra("rID", rID);


                startActivity(storeIntent);
                finish();

            }

        }
    }

    public String getAddressNewWay(String ZeroIndexAddress, String city, String State, String pincode, String country) {
        String editedAddress = ZeroIndexAddress;
        if (editedAddress.contains(city)) {
            editedAddress = editedAddress.replace(city, "");

        }
        if (editedAddress.contains(State)) {
            editedAddress = editedAddress.replace(State, "");

        }
        if (editedAddress.contains(pincode)) {
            editedAddress = editedAddress.replace(pincode, "");

        }
        if (editedAddress.contains(country)) {
            editedAddress = editedAddress.replace(country, "");

        }
        if (editedAddress.contains(",")) {
            editedAddress = editedAddress.replace(",", " ");

        }
        return editedAddress;
    }


}
