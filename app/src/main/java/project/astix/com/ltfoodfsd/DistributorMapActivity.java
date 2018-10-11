package project.astix.com.ltfoodfsd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;


public class DistributorMapActivity extends BaseActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback,CategoryCommunicatorCityState,DatePickerDialog.OnDateSetListener
{

    Calendar calendar ;
    int Year, Month, Day ;
    DatePickerDialog datePickerDialog ;
    boolean dob_Bool=false;
    boolean dom_Bool=false;
    TextView txt_Dob_credential,Text_Dob,Text_mrgAnnvrsry;
    boolean credential_dob_Bool=false;
    LinkedHashMap<String,String> hmapCityAgainstState;
    String defaultCity="";
    boolean isSelectedSearch=false;


    LinkedHashMap<String, String> hmapState_details=new LinkedHashMap<String, String>();
    String previousSlctdState="Select";
    String previousSlctdCity="Select";
    LinkedHashMap<String, String> hmapCity_details=new LinkedHashMap<String, String>();
    List<String> stateNames;
    List<String> cityNames;
    EditText etLocalArea,etPinCode,etOtherCity;
    TextView etCity,etState;
    TextView  txt_city,txt_state,txt_pincode;

    SharedPreferences sharedPref;
    DBAdapterKenya dbengine = new DBAdapterKenya(DistributorMapActivity.this);
    LinkedHashMap<String, String> hmapdsrIdAndDescr_details=new LinkedHashMap<String, String>();
    String[] drsNames;
    public String	SelectedDSRValue="";
    Dialog dialog;
    LinkedHashMap<String,String> hmapDistributorListWithRemapFlg=new LinkedHashMap<String,String>();
    String UniqueDistribtrMapId;
    public LocationManager locationManager;
    public AppLocationService appLocationService;
    public PowerManager pm;
    public	 PowerManager.WakeLock wl;
    public ProgressDialog pDialog2STANDBY;

    public CoundownClass countDownTimer;
    DatabaseAssistantDistributorMap DA=new DatabaseAssistantDistributorMap(this);
    public String newfullFileName;
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

    public String FusedLocationLatitudeWithFirstAttempt="0";
    public String FusedLocationLongitudeWithFirstAttempt="0";
    public String FusedLocationAccuracyWithFirstAttempt="0";
    public String AllProvidersLocation="";
    public String FusedLocationLatitude="0";
    public String FusedLocationLongitude="0";
    public String FusedLocationProvider="";
    public String FusedLocationAccuracy="0";

    public String GPSLocationLatitude="0";
    public String GPSLocationLongitude="0";
    public String GPSLocationProvider="";
    public String GPSLocationAccuracy="0";

    public String NetworkLocationLatitude="0";
    public String NetworkLocationLongitude="0";
    public String NetworkLocationProvider="";
    public String NetworkLocationAccuracy="0";

    String fusedData;
    public boolean isGPSEnabled = false;
    public   boolean isNetworkEnabled = false;

    public String fnAccurateProvider="";
    public String fnLati="0";
    public String fnLongi="0";
    public Double fnAccuracy=0.0;

    DBAdapterKenya helperDb;

    public String AccuracyFromLauncher="NA";
    String AddressFromLauncher="NA";
    String CityFromLauncher="NA";
    String PincodeFromLauncher="NA";
    String StateFromLauncher="NA";

    public String ProviderFromLauncher="NA";
    public String GpsLatFromLauncher="NA";
    public String GpsLongFromLauncher="NA";
    public String GpsAccuracyFromLauncher="NA";
    public String NetworkLatFromLauncher="NA";
    public String NetworkLongFromLauncher="NA";
    public String NetworkAccuracyFromLauncher="NA";
    public String FusedLatFromLauncher="NA";
    public String FusedLongFromLauncher="NA";
    public String FusedAccuracyFromLauncher="NA";

    public String fnAddressFromLauncher="NA";
    public String AllProvidersLocationFromLauncher="NA";
    public String GpsAddressFromLauncher="NA";
    public String NetwAddressFromLauncher="NA";
    public String FusedAddressFromLauncher="NA";
    public String FusedLocationLatitudeWithFirstAttemptFromLauncher="NA";
    public String FusedLocationLongitudeWithFirstAttemptFromLauncher="NA";
    public String FusedLocationAccuracyWithFirstAttemptFromLauncher="NA";

    public static int flgLocationServicesOnOff=0;
    public static int flgGPSOnOff=0;
    public static int flgNetworkOnOff=0;
    public static int flgFusedOnOff=0;
    public static int flgInternetOnOffWhileLocationTracking=0;
    public static int flgRestart=0;
    public static int flgStoreOrder=0;

    public static String address,pincode,city,state,latitudeToSave,longitudeToSave,accuracyToSave;
    int countSubmitClicked=0;

    String imei, fDate;
    LinearLayout ll_locationDetails,ll_map,ll_parentLayout,ll_showMap;
    EditText edit_gstYesVal;
    Button btn_submit;
    ImageView img_back_Btn;
    Spinner spinner_for_filter;
    RadioButton rb_gstYes,rb_gstNo,rb_gstPending;
    FragmentManager manager;
    FragmentTransaction fragTrans;
    MapFragment mapFrag;

    String LattitudeFromLauncher="NA";
    String LongitudeFromLauncher="NA";
    String StoreName="NA";

    String[] Distribtr_list;

   /* LinearLayout ll_address_section,ll_local_area,ll_gstDetails;
    EditText etLocalArea, etPinCode, etCity, etState;
*/

    LinearLayout ll_gstDetails;
    TextView tvLocalArea,tvPinCode,tvCity,tvState,txt_gst;

    String DbrNodeId,DbrNodeType,DbrName;
    int flgReMap=0;
    int DistribtrId_Global=0;
    ArrayList<String> DbrArray=new ArrayList<String>();
    public int DistributorNodeType_Global=0;

    public static String VisitStartTS="NA";
    String flgGSTCapture="NA";
    AlertDialog GPSONOFFAlert=null;

    String globalSelectedDistrbtr="";

    //report alert
    String[] Distribtr_list_alert;
    String DbrSelNodeId,DbrSelNodeType,DbrSelName;
    ArrayList<String> DbrArrayAlert=new ArrayList<String>();
    LinkedHashMap<String,String> hmapDistrbtrList=new LinkedHashMap<>();
    public String SelectedDistrbtrName="";

    //refresh
    int refreshCount=0;
    RadioGroup rg_yes_no;
    RadioButton rb_yes,rb_no;
    Button btn_refresh;
    TextView txt_rfrshCmnt;
    LinearLayout ll_refresh;

    //new added on 27 march, 2018
    EditText etPhoneNo,etMail,ed_FirstName,ed_LastName;
    TextView txt_phone,txt_FirstName,txt_LastName,txt_Dob;



    LinkedHashMap<String,String> hmapDbrContactInfo=new LinkedHashMap<>();

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String mon=MONTHS[monthOfYear];

        String dayOfMnth;
        if(mon.length()<2){

            mon = "0" + mon;
        }
        if(dayOfMonth<10){

            if((String.valueOf(dayOfMonth)).length()<2)
            {
                dayOfMnth = "0" + dayOfMonth;
            }
            else
            {
                dayOfMnth=String.valueOf(dayOfMonth);
            }

        }
        else
        {
            dayOfMnth=String.valueOf(dayOfMonth);
        }
        if(credential_dob_Bool)
        {
            txt_Dob_credential.setText(dayOfMnth+"/"+mon+"/"+year);
        }
        if(dob_Bool)
        {
            Text_Dob.setText(dayOfMnth+"/"+mon+"/"+year);
        }
        if(dom_Bool)
        {
            Text_mrgAnnvrsry.setText(dayOfMnth+"/"+mon+"/"+year);
        }

        credential_dob_Bool=false;
        dob_Bool=false;
        dom_Bool=false;



    }



    private void getDSRDetail() throws IOException
    {

        int check=dbengine.countDataIntblNoVisitReasonMaster();

        hmapdsrIdAndDescr_details=dbengine.fetch_DSRCoverage_List();

        int index=0;
        if(hmapdsrIdAndDescr_details!=null)
        {
            drsNames=new String[hmapdsrIdAndDescr_details.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapdsrIdAndDescr_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry)iterator.next();
                drsNames[index]=me2.getKey().toString();
                index=index+1;
            }
        }


    }

    public void customHeader()
    {
       // TextView tv_heading=(TextView) findViewById(R.id.tv_heading);
       // tv_heading.setText("Map Distributor");
       // ImageView imgVw_next=(ImageView) findViewById(R.id.imgVw_next);
       // imgVw_next.setVisibility(View.GONE);

        ImageView imgVw_back=(ImageView) findViewById(R.id.img_back_Btn);
        imgVw_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               finish();
            }
        });
       /* ImageView img_side_popUp=(ImageView) findViewById(R.id.img_side_popUp);
        img_side_popUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                open_pop_up();
            }
        });*/

    }

    public void getDateHandling()
    {
        Text_Dob= (TextView) findViewById(R.id.Text_Dob);
        Text_mrgAnnvrsry= (TextView) findViewById(R.id.Text_mrgAnnvrsry);
        Text_Dob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dob_Bool=true;
                calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR) ;
                Month = calendar.get(Calendar.MONTH);
                Day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = DatePickerDialog.newInstance(DistributorMapActivity.this, Year-18, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                Calendar calendarForSetDate = Calendar.getInstance();
                calendarForSetDate.setTimeInMillis(System.currentTimeMillis());

                // calendar.setTimeInMillis(System.currentTimeMillis()+24*60*60*1000);
                //YOU can set min or max date using this code
                // datePickerDialog.setMaxDate(Calendar.getInstance());
                // datePickerDialog.setMinDate(calendar);

                //  datePickerDialog.setMinDate(calendarForSetDate);
                calendarForSetDate.set(Year - 18, Month, Day);
                datePickerDialog.setMaxDate(calendarForSetDate);
                datePickerDialog.setAccentColor(Color.parseColor("#544f88"));

                datePickerDialog.setTitle(getResources().getString(R.string.txtSELECTDOB));
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });


        Text_mrgAnnvrsry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dom_Bool=true;
                calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR) ;
                Month = calendar.get(Calendar.MONTH);
                Day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = DatePickerDialog.newInstance(DistributorMapActivity.this, Year, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                Calendar calendarForSetDate = Calendar.getInstance();
                calendarForSetDate.setTimeInMillis(System.currentTimeMillis());

                // calendar.setTimeInMillis(System.currentTimeMillis()+24*60*60*1000);
                //YOU can set min or max date using this code
                // datePickerDialog.setMaxDate(Calendar.getInstance());
                // datePickerDialog.setMinDate(calendar);

                //  datePickerDialog.setMinDate(calendarForSetDate);
                calendarForSetDate.set(Year, Month, Day);
                datePickerDialog.setMaxDate(calendarForSetDate);
                datePickerDialog.setAccentColor(Color.parseColor("#544f88"));

                datePickerDialog.setTitle(getResources().getString(R.string.txtSELECTDOB));
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_map);

        sharedPref = getSharedPreferences(CommonInfo.Preference, MODE_PRIVATE);
        if(sharedPref.contains("CoverageAreaNodeID"))
        {
            if(sharedPref.getInt("CoverageAreaNodeID",0)!=0)
            {
                CommonInfo.CoverageAreaNodeID=sharedPref.getInt("CoverageAreaNodeID",0);
                CommonInfo.CoverageAreaNodeType=sharedPref.getInt("CoverageAreaNodeType",0);
            }
        }
        if(sharedPref.contains("SalesmanNodeId"))
        {
            if(sharedPref.getInt("SalesmanNodeId",0)!=0)
            {
                CommonInfo.SalesmanNodeId=sharedPref.getInt("SalesmanNodeId",0);
                CommonInfo.SalesmanNodeType=sharedPref.getInt("SalesmanNodeType",0);
            }
        }
        if(sharedPref.contains("flgDataScope"))
        {
            if(sharedPref.getInt("flgDataScope",0)!=0)
            {
                CommonInfo.flgDataScope=sharedPref.getInt("flgDataScope",0);

            }
        }
        if(sharedPref.contains("flgDSRSO"))
        {
            if(sharedPref.getInt("flgDSRSO",0)!=0)
            {
                CommonInfo.FlgDSRSO=sharedPref.getInt("flgDSRSO",0);

            }
        }

        helperDb=new DBAdapterKenya(DistributorMapActivity.this);

        customHeader();

        try {
            getDateHandling();
            getDSRDetail();
            //report alert
            getDistribtrList();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        locationManager=(LocationManager) this.getSystemService(LOCATION_SERVICE);
        ll_locationDetails= (LinearLayout) findViewById(R.id.ll_locationDetails);
        ll_parentLayout= (LinearLayout) findViewById(R.id.ll_parentLayout);
        ll_map= (LinearLayout) findViewById(R.id.ll_map);
        edit_gstYesVal= (EditText) findViewById(R.id.edit_gstYesVal);
        rb_gstYes= (RadioButton) findViewById(R.id.rb_gstYes);
        rb_gstNo= (RadioButton) findViewById(R.id.rb_gstNo);
        rb_gstPending= (RadioButton) findViewById(R.id.rb_gstPending);
        spinner_for_filter= (Spinner) findViewById(R.id.spinner_for_filter);
        btn_submit= (Button) findViewById(R.id.btn_submit);
        ll_gstDetails= (LinearLayout) findViewById(R.id.ll_gstDetails);

        txt_gst= (TextView) findViewById(R.id.txt_gst);

        ll_showMap= (LinearLayout) findViewById(R.id.ll_showMap);

        //refresh
        refreshCount=0;
        rg_yes_no= (RadioGroup) findViewById(R.id.rg_yes_no);
        rb_yes= (RadioButton) findViewById(R.id.rb_yes);
        rb_no=(RadioButton)findViewById(R.id.rb_no);
        btn_refresh= (Button) findViewById(R.id.btn_refresh);
        txt_rfrshCmnt= (TextView) findViewById(R.id.txt_rfrshCmnt);
        ll_refresh= (LinearLayout) findViewById(R.id.ll_refresh);

        getStateDetails();
        getCityDetails();
        defaultCity=helperDb.getDefaultCity();
        hmapCityAgainstState=helperDb.getCityAgainstState();

        address="";
        pincode="";
        city="";
        state="";

        Intent i=getIntent();
        imei=i.getStringExtra("imei");
        fDate=i.getStringExtra("fDate");

        if(imei==null)
        {
            imei= CommonInfo.imei;
        }
        if(fDate==null)
        {
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            fDate = sdf.format(date1).toString().trim();
        }

        SpannableStringBuilder text_Value3=textWithMandatory(txt_gst.getText().toString());
        txt_gst.setText(text_Value3);

        inflateAddressLayout();
        GstRadioBtns();
        fnGetDistributorList();
        SubmitBtnWorking();
        BackBtnWorking();
       // locationRetreivingAndSetToMap();
        //refresh
        refreshMapWorking();
        boolean isGPSok = false;
        boolean isNWok=false;
        isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNWok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

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

    }

    //refresh
    void refreshMapWorking()
    {
        rg_yes_no.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i!=-1)
                {
                    RadioButton radioButtonVal = (RadioButton) radioGroup.findViewById(i);
                    if(radioButtonVal.getId()==R.id.rb_yes)
                    {
                        ll_refresh.setVisibility(View.GONE);

                    }
                    else if(radioButtonVal.getId()==R.id.rb_no)
                    {
                        ll_refresh.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                boolean isGPSok = false;
                boolean isNWok=false;
                isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNWok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

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
                    manager= getFragmentManager();
                    mapFrag = (MapFragment)manager.findFragmentById(R.id.map);

                    mapFrag.getView().setVisibility(View.GONE);

                    locationRetrievingAndDistanceCalculating();
                }

                refreshCount++;
                if(refreshCount==1)
                {
                    txt_rfrshCmnt.setText(getString(R.string.second_msg_for_map));
                }
                else if(refreshCount==2)
                {
                    txt_rfrshCmnt.setText(getString(R.string.third_msg_for_map));
                    btn_refresh.setVisibility(View.GONE);
                }
                rg_yes_no.clearCheck();
                ll_refresh.setVisibility(View.GONE);

            }
        });

    }

    void getDistribtrList()
    {
        dbengine.open();

        Distribtr_list_alert=dbengine.getDistributorDataMstr();
        dbengine.close();
        for(int i=0;i<Distribtr_list_alert.length;i++)
        {
            String value=Distribtr_list_alert[i];
            DbrSelNodeId=value.split(Pattern.quote("^"))[0];
            DbrSelNodeType=value.split(Pattern.quote("^"))[1];
            DbrSelName=value.split(Pattern.quote("^"))[2];
            flgReMap=Integer.parseInt(value.split(Pattern.quote("^"))[3]);

            hmapDistrbtrList.put(DbrSelName,DbrSelNodeId+"^"+DbrSelNodeType);
            DbrArrayAlert.add(DbrSelName);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(GPSONOFFAlert!=null && GPSONOFFAlert.isShowing())
        {
            GPSONOFFAlert.dismiss();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //changes
        if(pDialog2STANDBY!=null )
        {
            if(pDialog2STANDBY.isShowing() )
             {
             }
            else
            {
                if(DbrArray.size() == 2)
                {
                    if(flgReMap == 1)
                    {
                        if(countSubmitClicked==1)
                        {
                            locationRetreivingAndSetToMap();
                        }
                        if(countSubmitClicked==0)
                        {
                            locationRetreivingAndSetToMap();
                        }
                    }
                }
            }
        }
        else
        {
            if(DbrArray.size() == 2)
            {
                if(flgReMap == 1) {
                    if (countSubmitClicked == 1) {
                        locationRetreivingAndSetToMap();
                    }
                    if (countSubmitClicked == 0) {
                        locationRetreivingAndSetToMap();
                    }
                }
            }

        }

    }

    public void GstRadioBtns()
    {
        rb_gstYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    edit_gstYesVal.setVisibility(View.VISIBLE);
                }
            }
        });

        rb_gstNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    edit_gstYesVal.setVisibility(View.GONE);
                }
            }
        });

        rb_gstPending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    edit_gstYesVal.setVisibility(View.GONE);
                }
            }
        });
    }

  /*  void inflateAddressLayout()
    {
        View viewLoc=getLayoutInflater().inflate(R.layout.location_details_layout_distributor, null);
        ll_locationDetails.addView(viewLoc);

        ll_address_section=(LinearLayout) viewLoc.findViewById(R.id.ll_address_section);
        ll_address_section.setTag("CompleteAddress");

        ll_local_area= (LinearLayout) viewLoc.findViewById(R.id.ll_local_area);
        etLocalArea= (EditText) viewLoc.findViewById(R.id.etLocalArea);
        etPinCode= (EditText) viewLoc.findViewById(R.id.etPinCode);
        etCity= (EditText) viewLoc.findViewById(R.id.etCity);
        etState= (EditText) viewLoc.findViewById(R.id.etState);

        tvLocalArea= (TextView) viewLoc.findViewById(R.id.tvLocalArea);
        tvPinCode= (TextView) viewLoc.findViewById(R.id.tvPinCode);
        tvCity= (TextView) viewLoc.findViewById(R.id.tvCity);
        tvState= (TextView) viewLoc.findViewById(R.id.tvState);

        SpannableStringBuilder text_Value=textWithMandatory(tvLocalArea.getText().toString());
        tvLocalArea.setText(text_Value);

        SpannableStringBuilder text_Value1=textWithMandatory(tvPinCode.getText().toString());
        tvPinCode.setText(text_Value1);

        SpannableStringBuilder text_Value2=textWithMandatory(tvCity.getText().toString());
        tvCity.setText(text_Value2);

        SpannableStringBuilder text_Value3=textWithMandatory(tvState.getText().toString());
        tvState.setText(text_Value3);

    }*/

    void inflateAddressLayout()
    {
       // View viewLoc=getLayoutInflater().inflate(R.layout.location_details_layoutso, null);
        View viewLoc=getLayoutInflater().inflate(R.layout.location_details_layout_with_phone, null);
        ll_locationDetails.addView(viewLoc);

        etMail= (EditText) viewLoc.findViewById(R.id.etMail);
        etPhoneNo= (EditText) viewLoc.findViewById(R.id.etPhoneNo);

        ed_FirstName= (EditText) findViewById(R.id.ed_FirstName);
        ed_LastName= (EditText) findViewById(R.id.ed_LastName);

        etLocalArea= (EditText) viewLoc.findViewById(R.id.etLocalArea);

        etPinCode= (EditText) viewLoc.findViewById(R.id.etPinCode);
        etCity= (TextView) viewLoc.findViewById(R.id.etCity);
        etState= (TextView) viewLoc.findViewById(R.id.etState);
        etState.setEnabled(false);
        etOtherCity= (EditText) viewLoc.findViewById(R.id.etOtherCity);
        txt_city= (TextView) viewLoc.findViewById(R.id.txt_city);
        txt_state= (TextView) viewLoc.findViewById(R.id.txt_state);
        txt_pincode= (TextView) viewLoc.findViewById(R.id.txt_pincode);

        SpannableStringBuilder text_Value=textWithMandatory(txt_city.getText().toString());
        txt_city.setText(text_Value);


        SpannableStringBuilder Strtxt_pincode=textWithMandatory(txt_pincode.getText().toString());
        txt_pincode.setText(Strtxt_pincode);
        SpannableStringBuilder Strtxt_state=textWithMandatory(txt_state.getText().toString());
        txt_state.setText(Strtxt_state);

        txt_phone= (TextView) viewLoc.findViewById(R.id.txt_phone);

        SpannableStringBuilder Strtxt_phone=textWithMandatory(txt_phone.getText().toString());
        txt_phone.setText(Strtxt_phone);


        txt_FirstName= (TextView) findViewById(R.id.txt_FirstName);
        SpannableStringBuilder Strtxt_FirstName=textWithMandatory(txt_FirstName.getText().toString());
        txt_FirstName.setText(Strtxt_FirstName);

        txt_LastName= (TextView) findViewById(R.id.txt_LastName);
        SpannableStringBuilder Strtxt_LastName=textWithMandatory(txt_LastName.getText().toString());
        txt_LastName.setText(Strtxt_LastName);


        txt_Dob= (TextView) findViewById(R.id.txt_Dob);
        SpannableStringBuilder Strtxt_Dob=textWithMandatory(txt_Dob.getText().toString());
        txt_Dob.setText(Strtxt_Dob);




    /*    if(!address.equals("NA"))
        {
            etLocalArea.setText(address);
        }
        if(!pincode.equals("NA"))
        {
            etPinCode.setText(pincode);
        }
        if(!state.equals("NA"))
        {
            etState.setText(state);
        }
        boolean isCityFilled = false;
        if(!city.equals("NA")) {


            for (Map.Entry<String, String> entryCity : hmapCity_details.entrySet()) {
                if (entryCity.getKey().equalsIgnoreCase(city.trim())) {
                    etCity.setText(city);
                    isCityFilled = true;
                    defaultCity = entryCity.getKey();
                    break;
                }
            }

            if(isCityFilled)
            {
                if(!TextUtils.isEmpty(defaultCity))
                {
                    etCity.setText(defaultCity);
                    if(defaultCity.equalsIgnoreCase("Others") || defaultCity.equalsIgnoreCase("Other"))
                    {
                        etState.setEnabled(true);
                        etState.setText("Select");
                    }
                    else
                    {
                        if(hmapCityAgainstState!=null && hmapCityAgainstState.containsKey(defaultCity))
                        {
                            etState.setText(hmapCityAgainstState.get(defaultCity));
                        }
                        else
                        {
                            etState.setText("Select");
                        }
                    }

                }
                else
                {
                    etState.setText("Select");
                }
            }
            else
            {
                etState.setText("Select");
            }
            //
        }

        else
        {
            if(!TextUtils.isEmpty(defaultCity))
            {
                etCity.setText(defaultCity);
                if(defaultCity.equalsIgnoreCase("Others") || defaultCity.equalsIgnoreCase("Other"))
                {
                    etState.setEnabled(true);
                }
                else
                {
                    if(hmapCityAgainstState!=null && hmapCityAgainstState.containsKey(defaultCity))
                    {
                        etState.setText(hmapCityAgainstState.get(defaultCity));
                    }
                }

            }
        }*/

        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cityNames!=null && cityNames.size()>0)
                {
                    customAlertStateCityList(1,cityNames,"Select City",previousSlctdCity);
                }
                else
                {
                    showErrorAlert("There is no city mapped to this phone");
                }

            }
        });
        etState.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //img_State.setEnabled(false);
                if(stateNames!=null && stateNames.size()>0)
                {
                    customAlertStateCityList(0,stateNames,"Select State",previousSlctdState);
                }
                else {
                    showErrorAlert("There is no State mapped to this phone");
                }
            }
        });

    }

    public SpannableStringBuilder textWithMandatory(String text_Value)
    {
        String simple = text_Value;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //text.setText(builder);

        return builder;

    }

    public void fnGetDistributorList()
    {
        helperDb.open();

        //Distribtr_list=helperDb.getDistributorDataMstr();

        Distribtr_list=helperDb.getDistributorDataWithPhoneAndEmial();

        helperDb.close();
        for(int i=0;i<Distribtr_list.length;i++)
        {
            String value=Distribtr_list[i];
            DbrNodeId=value.split(Pattern.quote("^"))[0];
            DbrNodeType=value.split(Pattern.quote("^"))[1];
            DbrName=value.split(Pattern.quote("^"))[2];
            flgReMap=Integer.parseInt(value.split(Pattern.quote("^"))[3]);
            String phoneNo=value.split(Pattern.quote("^"))[4];
            String emailID=value.split(Pattern.quote("^"))[5];
            hmapDbrContactInfo.put(DbrName,phoneNo+"^"+emailID);
            hmapDistributorListWithRemapFlg.put(DbrName,""+flgReMap+"^"+DbrNodeId+"^"+DbrNodeType);
            //hmapDistributorListWithRemapFlg.put(DbrName,""+"1"+"^"+DbrNodeId+"^"+DbrNodeType);
            System.out.println("TEMP FETCHED DATA...."+DbrNodeId+"--"+DbrNodeType+"--"+DbrName+"--"+flgReMap);
            DbrArray.add(DbrName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DistributorMapActivity.this,R.layout.initial_spinner_text,DbrArray);
        adapter.setDropDownViewResource(R.layout.spina);
        spinner_for_filter.setAdapter(adapter);

        if(!DbrArray.isEmpty())
        {
            if(DbrArray.size() == 2) //means only 1 distributor is their
            {
                DistribtrId_Global= Integer.parseInt(Distribtr_list[1].split(Pattern.quote("^"))[0]);
                DistributorNodeType_Global= Integer.parseInt(Distribtr_list[1].split(Pattern.quote("^"))[1]);

                spinner_for_filter.setSelection(1);
                ll_parentLayout.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);

            }
            else
            {
                    spinner_for_filter.setSelection(0);
                    ll_parentLayout.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
            }
        }

        spinner_for_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv =(TextView) view;
                String text=tv.getText().toString();

                if(text.equals("Select Distributor"))
                {
                    ll_parentLayout.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);
                }
                else
                {
                    if(text.equals(globalSelectedDistrbtr))
                    {

                    }
                    else
                    {
                        if(hmapDistributorListWithRemapFlg.size()>0)
                        {
                            if (hmapDistributorListWithRemapFlg.containsKey(text))
                            {
                                String flgRemap=hmapDistributorListWithRemapFlg.get(text).split(Pattern.quote("^"))[0];
                                DbrNodeId=hmapDistributorListWithRemapFlg.get(text).split(Pattern.quote("^"))[1];
                                DbrNodeType=hmapDistributorListWithRemapFlg.get(text).split(Pattern.quote("^"))[2];

                                if (Integer.parseInt(flgRemap) == 0)
                                {}
                                else
                                {
                                    if(DbrArray.size()>2)
                                    {
                                        locationRetreivingAndSetToMap();
                                    }

                                }
                            }
                        }
                    }

                    globalSelectedDistrbtr=tv.getText().toString();
                    ll_parentLayout.setVisibility(View.VISIBLE);

                    if(hmapDistributorListWithRemapFlg.size()>0)
                    {
                        if(hmapDistributorListWithRemapFlg.containsKey(text))
                        {
                            String flgRemap=hmapDistributorListWithRemapFlg.get(text).split(Pattern.quote("^"))[0];
                            DbrNodeId=hmapDistributorListWithRemapFlg.get(text).split(Pattern.quote("^"))[1];
                            DbrNodeType=hmapDistributorListWithRemapFlg.get(text).split(Pattern.quote("^"))[2];

                            if (Integer.parseInt(flgRemap) == 0)
                            {
                                btn_submit.setVisibility(View.GONE);

                                if(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[0].equals("0"))
                                {
                                    etPhoneNo.setText("");
                                }
                                else
                                {
                                    etPhoneNo.setText(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[0]);
                                }
                                etPhoneNo.setEnabled(false);

                                if(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[1].equals("NA"))
                                {
                                    etMail.setText("");
                                }
                                else
                                {
                                    etMail.setText(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[1]);
                                }
                                etMail.setEnabled(false);

                                etLocalArea.setEnabled(false);
                                etPinCode.setEnabled(false);
                                etCity.setEnabled(false);
                                etState.setEnabled(false);


                                //Show Alert Distributor is Already Mapped then close

                                AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(DistributorMapActivity.this);
                                alertDialogSubmitConfirm.setTitle("Information");
                                alertDialogSubmitConfirm.setMessage("Distributor is Already Mapped");
                                alertDialogSubmitConfirm.setCancelable(false);

                                alertDialogSubmitConfirm.setNeutralButton("OK", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                        spinner_for_filter.setSelection(0);
                                    }
                                });

                            alertDialogSubmitConfirm.show();


                            }
                            else
                            {
                                btn_submit.setVisibility(View.VISIBLE);

                                if(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[0].equals("0"))
                                {
                                    etPhoneNo.setText("");
                                }
                                else
                                {
                                    etPhoneNo.setText(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[0]);
                                }
                                etPhoneNo.setEnabled(true);

                                if(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[1].equals("NA"))
                                {
                                    etMail.setText("");
                                }
                                else
                                {
                                    etMail.setText(hmapDbrContactInfo.get(text).split(Pattern.quote("^"))[1]);
                                }
                                etMail.setEnabled(true);

                                etLocalArea.setEnabled(true);
                                etPinCode.setEnabled(true);
                                etCity.setEnabled(true);
                                etState.setEnabled(true);
                            }
                        }
                    }

                   // btn_submit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if(!LattitudeFromLauncher.equals("NA") && !LattitudeFromLauncher.equals("0.0"))
        {
            googleMap.clear();
            try {
                googleMap.setMyLocationEnabled(false);
            }
            catch(SecurityException e)
            {

            }

            MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(LattitudeFromLauncher), Double.parseDouble(LongitudeFromLauncher)));
            Marker locationMarker=googleMap.addMarker(marker);
            locationMarker.showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(LattitudeFromLauncher), Double.parseDouble(LongitudeFromLauncher)), 15));

        }
        else
        {
            if(refreshCount==2)
            {
                txt_rfrshCmnt.setText(getString(R.string.loc_not_found));
                btn_refresh.setVisibility(View.GONE);
            }

            try {
                googleMap.setMyLocationEnabled(false);
            }
            catch(SecurityException e)
            {

            }
            googleMap.moveCamera(CameraUpdateFactory.zoomIn());
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    marker.setTitle(StoreName);
                }
            });

        }

    }

    public void locationRetreivingAndSetToMap()
    {
        boolean isGPSokCheckInResume = false;
        boolean isNWokCheckInResume=false;
        isGPSokCheckInResume = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNWokCheckInResume = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSokCheckInResume && !isNWokCheckInResume)
        {
            try
            {
                showSettingsAlert();
            }
            catch(Exception e)
            {

            }
            isGPSokCheckInResume = false;
            isNWokCheckInResume=false;
        }
        else
        {

            locationRetrievingAndDistanceCalculating();
        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialogGps = new AlertDialog.Builder(this);

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
        GPSONOFFAlert=alertDialogGps.create();
        GPSONOFFAlert.show();
    }

    public void locationRetrievingAndDistanceCalculating()
    {

        appLocationService = new AppLocationService();

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire();


        pDialog2STANDBY = ProgressDialog.show(DistributorMapActivity.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.rtrvng_loc), true);
        pDialog2STANDBY.setIndeterminate(true);

        pDialog2STANDBY.setCancelable(false);
        pDialog2STANDBY.show();

        if (isGooglePlayServicesAvailable()) {
            createLocationRequest();

            mGoogleApiClient = new GoogleApiClient.Builder(DistributorMapActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(DistributorMapActivity.this)
                    .addOnConnectionFailedListener(DistributorMapActivity.this)
                    .build();
            mGoogleApiClient.connect();
        }
        //startService(new Intent(DynamicActivity.this, AppLocationService.class));
        startService(new Intent(DistributorMapActivity.this, AppLocationService.class));
        Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);
        Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);
        countDownTimer = new CoundownClass(startTime, interval);
        countDownTimer.start();

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
        startLocationUpdates();
    }

    protected void startLocationUpdates()
    {
        try {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates( mGoogleApiClient, mLocationRequest, this);
        }
        catch(SecurityException e)
        {

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
    public void onLocationChanged(Location args0) {
        mCurrentLocation = args0;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

    }

    private void updateUI() {
        Location loc =mCurrentLocation;
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            FusedLocationLatitude=lat;
            FusedLocationLongitude=lng;
            FusedLocationProvider=mCurrentLocation.getProvider();
            FusedLocationAccuracy=""+mCurrentLocation.getAccuracy();
            fusedData="At Time: " + mLastUpdateTime  +
                    "Latitude: " + lat  +
                    "Longitude: " + lng  +
                    "Accuracy: " + mCurrentLocation.getAccuracy() +
                    "Provider: " + mCurrentLocation.getProvider();

        } else {

        }
    }

    public class CoundownClass extends CountDownTimer
    {

        public CoundownClass(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            AllProvidersLocation="";
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            String GpsLat="0";
            String GpsLong="0";
            String GpsAccuracy="0";
            String GpsAddress="0";
            if(isGPSEnabled)
            {
                Location nwLocation=appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER,location);

                if(nwLocation!=null){
                    double lattitude=nwLocation.getLatitude();
                    double longitude=nwLocation.getLongitude();
                    double accuracy= nwLocation.getAccuracy();
                    GpsLat=""+lattitude;
                    GpsLong=""+longitude;
                    GpsAccuracy=""+accuracy;
                    if(isOnline())
                    {
                        GpsAddress=getAddressOfProviders(GpsLat, GpsLong);
                    }
                    else
                    {
                        GpsAddress="NA";
                    }

                    GPSLocationLatitude=""+lattitude;
                    GPSLocationLongitude=""+longitude;
                    GPSLocationProvider="GPS";
                    GPSLocationAccuracy=""+accuracy;
                    AllProvidersLocation="GPS=Lat:"+lattitude+"Long:"+longitude+"Acc:"+accuracy;

                }
            }

            Location gpsLocation=appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER,location);
            String NetwLat="0";
            String NetwLong="0";
            String NetwAccuracy="0";
            String NetwAddress="0";
            if(gpsLocation!=null){
                double lattitude1=gpsLocation.getLatitude();
                double longitude1=gpsLocation.getLongitude();
                double accuracy1= gpsLocation.getAccuracy();

                NetwLat=""+lattitude1;
                NetwLong=""+longitude1;
                NetwAccuracy=""+accuracy1;
                if(isOnline())
                {
                    NetwAddress=getAddressOfProviders(NetwLat, NetwLong);
                }
                else
                {
                    NetwAddress="NA";
                }

                NetworkLocationLatitude=""+lattitude1;
                NetworkLocationLongitude=""+longitude1;
                NetworkLocationProvider="Network";
                NetworkLocationAccuracy=""+accuracy1;
                if(!AllProvidersLocation.equals(""))
                {
                    AllProvidersLocation=AllProvidersLocation+"$Network=Lat:"+lattitude1+"Long:"+longitude1+"Acc:"+accuracy1;
                }
                else
                {
                    AllProvidersLocation="Network=Lat:"+lattitude1+"Long:"+longitude1+"Acc:"+accuracy1;
                }


            }
									 /* TextView accurcy=(TextView) findViewById(R.id.Acuracy);
									  accurcy.setText("GPS:"+GPSLocationAccuracy+"\n"+"NETWORK"+NetworkLocationAccuracy+"\n"+"FUSED"+fusedData);*/

            System.out.println("LOCATION Fused"+fusedData);

            String FusedLat="0";
            String FusedLong="0";
            String FusedAccuracy="0";
            String FusedAddress="0";

            if(!FusedLocationProvider.equals(""))
            {
                fnAccurateProvider="Fused";
                fnLati=FusedLocationLatitude;
                fnLongi=FusedLocationLongitude;
                fnAccuracy= Double.parseDouble(FusedLocationAccuracy);

                FusedLat=FusedLocationLatitude;
                FusedLong=FusedLocationLongitude;
                FusedAccuracy=FusedLocationAccuracy;
                FusedLocationLatitudeWithFirstAttempt=FusedLocationLatitude;
                FusedLocationLongitudeWithFirstAttempt=FusedLocationLongitude;
                FusedLocationAccuracyWithFirstAttempt=FusedLocationAccuracy;


                if(isOnline())
                {
                    FusedAddress=getAddressOfProviders(FusedLat, FusedLong);
                }
                else
                {
                    FusedAddress="NA";
                }

                if(!AllProvidersLocation.equals(""))
                {
                    AllProvidersLocation=AllProvidersLocation+"$Fused=Lat:"+FusedLocationLatitude+"Long:"+FusedLocationLongitude+"Acc:"+fnAccuracy;
                }
                else
                {
                    AllProvidersLocation="Fused=Lat:"+FusedLocationLatitude+"Long:"+FusedLocationLongitude+"Acc:"+fnAccuracy;
                }
            }


            appLocationService.KillServiceLoc(appLocationService, locationManager);
            try {
                if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
                {
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                }
            }
            catch (Exception e){

            }




            fnAccurateProvider="";
            fnLati="0";
            fnLongi="0";
            fnAccuracy=0.0;

            if(!FusedLocationProvider.equals(""))
            {
                fnAccurateProvider="Fused";
                fnLati=FusedLocationLatitude;
                fnLongi=FusedLocationLongitude;
                fnAccuracy= Double.parseDouble(FusedLocationAccuracy);
            }

            if(!fnAccurateProvider.equals(""))
            {
                if(!GPSLocationProvider.equals(""))
                {
                    if(Double.parseDouble(GPSLocationAccuracy)<fnAccuracy)
                    {
                        fnAccurateProvider="Gps";
                        fnLati=GPSLocationLatitude;
                        fnLongi=GPSLocationLongitude;
                        fnAccuracy= Double.parseDouble(GPSLocationAccuracy);
                    }
                }
            }
            else
            {
                if(!GPSLocationProvider.equals(""))
                {
                    fnAccurateProvider="Gps";
                    fnLati=GPSLocationLatitude;
                    fnLongi=GPSLocationLongitude;
                    fnAccuracy= Double.parseDouble(GPSLocationAccuracy);
                }
            }

            if(!fnAccurateProvider.equals(""))
            {
                if(!NetworkLocationProvider.equals(""))
                {
                    if(Double.parseDouble(NetworkLocationAccuracy)<fnAccuracy)
                    {
                        fnAccurateProvider="Network";
                        fnLati=NetworkLocationLatitude;
                        fnLongi=NetworkLocationLongitude;
                        fnAccuracy= Double.parseDouble(NetworkLocationAccuracy);
                    }
                }
            }
            else
            {
                if(!NetworkLocationProvider.equals(""))
                {
                    fnAccurateProvider="Network";
                    fnLati=NetworkLocationLatitude;
                    fnLongi=NetworkLocationLongitude;
                    fnAccuracy= Double.parseDouble(NetworkLocationAccuracy);
                }
            }
            checkHighAccuracyLocationMode(DistributorMapActivity.this);
            // fnAccurateProvider="";
            if(fnAccurateProvider.equals(""))
            {
                helperDb.open();
                helperDb.deleteLocationTable();
                helperDb.saveTblLocationDetails("NA", "NA", "NA","NA","NA","NA","NA","NA", "NA", "NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA");
                helperDb.close();

                if(pDialog2STANDBY.isShowing())
                {
                    pDialog2STANDBY.dismiss();
                }

                ll_map.setVisibility(View.VISIBLE);
                manager= getFragmentManager();
                mapFrag = (MapFragment)manager.findFragmentById(R.id.map);
                mapFrag.getView().setVisibility(View.VISIBLE);
                mapFrag.getMapAsync(DistributorMapActivity.this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.show(mapFrag);
                countSubmitClicked=2;
            }
            else
            {
                String FullAddress="0";
                if(isOnline())
                {
                    FullAddress=   getAddressForDynamic(fnLati, fnLongi);
                }
                else
                {
                    FullAddress="NA";
                }
                String addr="NA";
                String zipcode="NA";
                String tempcity="NA";
                String tempstate="NA";


                if(!FullAddress.equals("NA"))
                {
                    addr = FullAddress.split(Pattern.quote("^"))[0];
                    zipcode = FullAddress.split(Pattern.quote("^"))[1];
                    tempcity = FullAddress.split(Pattern.quote("^"))[2];
                    tempstate = FullAddress.split(Pattern.quote("^"))[3];
                }
                //surbhi
                if(!addr.equals("NA"))
                {
                    etLocalArea.setText(addr);
                    address=addr;
                }
                if(!zipcode.equals("NA"))
                {
                    etPinCode.setText(zipcode);
                    pincode=zipcode;
                }

              /*  if(!city.equals("NA"))
                {
                    etCity.setText(city);
                }
                if(!state.equals("NA"))
                {
                    etState.setText(state);
                }*/
                boolean isCityFilled = false;
                if(!tempcity.equals("NA")) {

                    city=tempcity;
                    state=tempstate;
                    for (Map.Entry<String, String> entryCity : hmapCity_details.entrySet()) {
                        if (entryCity.getKey().equalsIgnoreCase(city.trim())) {
                            etCity.setText(city);
                            isCityFilled = true;
                            defaultCity = entryCity.getKey();
                            break;
                        }
                    }

                    if(isCityFilled)
                    {
                        if(!TextUtils.isEmpty(defaultCity))
                        {
                            etCity.setText(defaultCity);
                            if(defaultCity.equalsIgnoreCase("Others") || defaultCity.equalsIgnoreCase("Other"))
                            {
                                etState.setEnabled(true);
                                etState.setText("Select");
                            }
                            else
                            {
                                if(hmapCityAgainstState!=null && hmapCityAgainstState.containsKey(defaultCity))
                                {
                                    etState.setText(hmapCityAgainstState.get(defaultCity));
                                }
                                else
                                {
                                    etState.setText("Select");
                                }
                            }

                        }
                        else
                        {
                            etState.setText("Select");
                        }
                    }
                    else
                    {
                        etState.setText("Select");
                    }
                    //
                }

                else
                {
                    if(!TextUtils.isEmpty(defaultCity))
                    {
                        etCity.setText(defaultCity);
                        if(defaultCity.equalsIgnoreCase("Others") || defaultCity.equalsIgnoreCase("Other"))
                        {
                            etState.setEnabled(true);
                        }
                        else
                        {
                            if(hmapCityAgainstState!=null && hmapCityAgainstState.containsKey(defaultCity))
                            {
                                etState.setText(hmapCityAgainstState.get(defaultCity));
                            }
                        }

                    }
                }

                if(pDialog2STANDBY.isShowing())
                {
                    pDialog2STANDBY.dismiss();
                }
                if(!GpsLat.equals("0") )
                {
                    fnCreateLastKnownGPSLoction(GpsLat,GpsLong,GpsAccuracy);
                }

                LattitudeFromLauncher=fnLati;
                LongitudeFromLauncher=fnLongi;
                AccuracyFromLauncher= String.valueOf(fnAccuracy);
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


                ll_map.setVisibility(View.VISIBLE);
                manager= getFragmentManager();
                mapFrag = (MapFragment)manager.findFragmentById(
                        R.id.map);
                mapFrag.getView().setVisibility(View.VISIBLE);
                mapFrag.getMapAsync(DistributorMapActivity.this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.show(mapFrag);


                helperDb.open();
                helperDb.deleteLocationTable();
                helperDb.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state,fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,FusedLong,FusedAccuracy,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,FusedLocationLatitudeWithFirstAttempt,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
                helperDb.close();
        //        if(!checkLastFinalLoctionIsRepeated("28.4873276","77.1045244","22.201"))
                if(!checkLastFinalLoctionIsRepeated(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher))
                {
                    fnCreateLastKnownFinalLocation(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher);
                    countSubmitClicked=2;

                }
                else
                {
                    if(countSubmitClicked == 1)
                    {
                        countSubmitClicked=2;
                    }
                    if(countSubmitClicked == 0)
                    {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistributorMapActivity.this);

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

               /* if(countSubmitClicked>0)
                {
                    if(!checkLastFinalLoctionIsRepeated(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher))
                    {
                        fnCreateLastKnownFinalLocation(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher);
                    }

                    showSubmitConfirm();
                }*/
                GpsLat="0";
                GpsLong="0";
                GpsAccuracy="0";
                GpsAddress="0";
                NetwLat="0";
                NetwLong="0";
                NetwAccuracy="0";
                NetwAddress="0";
                FusedLat="0";
                FusedLong="0";
                FusedAccuracy="0";
                FusedAddress="0";

                //code here
            }
        }

        @Override
        public void onTick(long arg0) {
            // TODO Auto-generated method stub

        }}

    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    public boolean checkLastFinalLoctionIsRepeated(String currentLat, String currentLong, String currentAccuracy){
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

    public void fnCreateLastKnownGPSLoction(String chekLastGPSLat, String chekLastGPSLong, String chekLastGpsAccuracy)
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

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.AppLatLngJsonFile);
            if (!jsonTxtFolder.exists())
            {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew="GPSLastLocation.txt";
            File file = new File(jsonTxtFolder,txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory()+"/"+ CommonInfo.AppLatLngJsonFile+"/"+txtFileNamenew;


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
        }
        finally{

        }
    }


    public void fnCreateLastKnownFinalLocation(String chekLastGPSLat, String chekLastGPSLong, String chekLastGpsAccuracy)
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

   /* public String getAddressOfProviders(String latti, String longi){

        StringBuilder FULLADDRESS2 =new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(DistributorMapActivity.this, Locale.getDefault());



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

    public String getAddressOfProviders(String latti, String longi){

        StringBuilder FULLADDRESS2 =new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(DistributorMapActivity.this, Locale.ENGLISH);



        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);

            if (addresses == null || addresses.size()  == 0 || addresses.get(0).getAddressLine(0)==null)
            {
                FULLADDRESS2=  FULLADDRESS2.append("NA");
            }
            else
            {
                FULLADDRESS2 =FULLADDRESS2.append(addresses.get(0).getAddressLine(0));
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

    public void checkHighAccuracyLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        flgLocationServicesOnOff=0;
        flgGPSOnOff=0;
        flgNetworkOnOff=0;
        flgFusedOnOff=0;
        flgInternetOnOffWhileLocationTracking=0;

        if(isGooglePlayServicesAvailable())
        {
            flgFusedOnOff=1;
        }
        if(isOnline())
        {
            flgInternetOnOffWhileLocationTracking=1;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            //Equal or higher than API 19/KitKat
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
                if (locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY){
                    flgLocationServicesOnOff=1;
                    flgGPSOnOff=1;
                    flgNetworkOnOff=1;
                    //flgFusedOnOff=1;
                }
                if (locationMode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING){
                    flgLocationServicesOnOff=1;
                    flgGPSOnOff=0;
                    flgNetworkOnOff=1;
                    // flgFusedOnOff=1;
                }
                if (locationMode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY){
                    flgLocationServicesOnOff=1;
                    flgGPSOnOff=1;
                    flgNetworkOnOff=0;
                    //flgFusedOnOff=0;
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
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

    public String getAddressForDynamic(String latti, String longi){


        String areaToMerge="NA";
        Address addressTemp=null;
        String addr="NA";
        String zipcode="NA";
        String city="NA";
        String state="NA";
        String fullAddress="";
        StringBuilder FULLADDRESS3 =new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(DistributorMapActivity.this, Locale.ENGLISH);

           /* AddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[3];
            CityFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[4];
            PincodeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[5];
            StateFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[6];*/
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);
            if (addresses != null && addresses.size() > 0){
               /* if(addresses.get(0).getAddressLine(0)!=null){
                    addr=addresses.get(0).getAddressLine(0);
                    address=addr;
                }*/

                if(addresses.get(0).getAddressLine(1)!=null){
                    addr=addresses.get(0).getAddressLine(1);
                    address=addr;
                }

                if(addresses.get(0).getLocality()!=null){
                    city=addresses.get(0).getLocality();
                    this.city=city;
                }

                if(addresses.get(0).getAdminArea()!=null){
                    state=addresses.get(0).getAdminArea();
                    this.state=state;
                }


                for(int i=0 ;i<addresses.size();i++){
                    addressTemp = addresses.get(i);
                    if(addressTemp.getPostalCode()!=null){
                        zipcode=addressTemp.getPostalCode();
                        this.pincode=zipcode;
                        break;
                    }
                }
                if(addresses.get(0).getAddressLine(0)!=null && addr.equals("NA")){
                    String countryname="NA";
                    if(addresses.get(0).getCountryName()!=null){
                        countryname=addresses.get(0).getCountryName();
                    }

                    address=  getAddressNewWay(addresses.get(0).getAddressLine(0),city,state,zipcode,countryname);
                }

                /*NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                if(null != recFragment)
                {
                    recFragment.setFreshAddress();
                }*/
                setFreshAddress();
            }
            else{FULLADDRESS3.append("NA");}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            return fullAddress=address+"^"+zipcode+"^"+city+"^"+state;
        }
    }



    public String getAddressNewWay(String ZeroIndexAddress,String city,String State,String pincode,String country){
        String editedAddress=ZeroIndexAddress;
        if(editedAddress.contains(city)){
            editedAddress= editedAddress.replace(city,"");

        }
        if(editedAddress.contains(State)){
            editedAddress=editedAddress.replace(State,"");

        }
        if(editedAddress.contains(pincode)){
            editedAddress= editedAddress.replace(pincode,"");

        }
        if(editedAddress.contains(country)){
            editedAddress=editedAddress.replace(country,"");

        }
        if(editedAddress.contains(",")){
            editedAddress=editedAddress.replace(","," ");

        }
        return editedAddress;
    }

    void SubmitBtnWorking()
    {
        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validate())
                {
                    AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(DistributorMapActivity.this);
                    alertDialogSubmitConfirm.setTitle("Information");
                    alertDialogSubmitConfirm.setMessage("Do you want to submit distributor's information? \n");
                    alertDialogSubmitConfirm.setCancelable(false);

                    alertDialogSubmitConfirm.setNeutralButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            saveDataToDatabase();
                            dialog.dismiss();
                           /* Intent i=new Intent(DistributorMapActivity.this,StorelistActivity.class);
                            startActivity(i);
                            finish();*/
                        }
                    });

                    alertDialogSubmitConfirm.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);

                    AlertDialog alert = alertDialogSubmitConfirm.create();

                    alert.show();
                }
            }
        });
    }


    private boolean validate()
    {
        String spinner_text=spinner_for_filter.getSelectedItem().toString().trim();

        if(spinner_text.equals("Select Distributor"))
        {
            showAlertForEveryOne("Please Select Distributor.");
            return false;
        }
        else if(TextUtils.isEmpty(ed_FirstName.getText().toString()))
        {
            showAlertForEveryOne("Please fill First Name.");
            return false;
        }
        else if(TextUtils.isEmpty(ed_LastName.getText().toString()))
        {
            showAlertForEveryOne("Please fill Last name.");
            return false;
        }
        else if(Text_Dob.getText().toString().trim().equals("Select Date")  )
        {
            showAlertForEveryOne("Please Select DOB ");
            //Toast.makeText(getApplicationContext(), "Please Select DOB", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(etPhoneNo.getText().toString().trim().length()<10)
        {
            showAlertForEveryOne("Please fill proper phone number.");
            return false;
        }
        else if(etMail.getText().toString().trim().length()>0 && (!validEmailAddress(etMail.getText().toString().trim())))
        {
            showAlertForEveryOne("Please fill proper email address.");
            return false;
        }
        else if(TextUtils.isEmpty(etLocalArea.getText().toString()) ||
                etLocalArea.getText().toString().trim().equals(null) ||
                etLocalArea.getText().toString().trim().equals("NA")||
                etLocalArea.getText().toString().trim().equals("0")||
                etLocalArea.getText().toString().trim().equals(""))
        {
            showAlertForEveryOne("Please Fill Address Value.");
            return false;
        }
        else if(etLocalArea.getText().toString().trim().length()<5)
        {
            showAlertForEveryOne("Please fill proper address.");
            return false;
        }
        else if(TextUtils.isEmpty(etPinCode.getText().toString()) ||
                etPinCode.getText().toString().trim().equals(null) ||
                etPinCode.getText().toString().trim().equals("0")||
                etPinCode.getText().toString().trim().equals(""))
        {
            showAlertForEveryOne("Please Fill pincode Value.");
            return false;
        }
        else if(etPinCode.getText().toString().trim().length()<6)
        {
            showAlertForEveryOne("Pin Code value cannot be less than 6 digits.");
            return false;
        }
       /* else if(TextUtils.isEmpty(etCity.getText().toString()) ||
                etCity.getText().toString().trim().equals(null) ||
                etCity.getText().toString().trim().equals("NA")||
                etCity.getText().toString().trim().equals("0")||
                etCity.getText().toString().trim().equals(""))
        {
            showAlertForEveryOne("Please Fill City Value.");
            return false;
        }
        else if(TextUtils.isEmpty(etState.getText().toString()) ||
                etState.getText().toString().trim().equals(null) ||
                etState.getText().toString().trim().equals("NA")||
                etState.getText().toString().trim().equals("0")||
                etState.getText().toString().trim().equals(""))
        {
            showAlertForEveryOne("Please Fill State Value.");
            return false;
        }*/
        else if(!fnValidateStateCity())
        {
            return  false;
        }
        else
        {
            return true;
        }
    }

    public boolean validEmailAddress(String emailID)
    {
        Boolean value;
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (emailID.matches(emailPattern) && emailID.length() > 0)
        {
            value=true;
        }
        else
        {
            value=false;
        }
        return value;
    }

    public void showAlertForEveryOne(String msg)
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(DistributorMapActivity.this);
        alertDialogNoConn.setTitle("Information");
        alertDialogNoConn.setMessage(msg);
        alertDialogNoConn.setCancelable(false);
        alertDialogNoConn.setNeutralButton("Ok",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();
    }

    public String genTempID()
    {
        //store ID generation <x>

        String cxz;
        cxz = UUID.randomUUID().toString();

        StringTokenizer tokens = new StringTokenizer(String.valueOf(cxz), "-");

        String val1 = tokens.nextToken().trim();
        String val2 = tokens.nextToken().trim();
        String val3 = tokens.nextToken().trim();
        String val4 = tokens.nextToken().trim();
        cxz = tokens.nextToken().trim();
        String imei=null;
        try
        {
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = tManager.getDeviceId();
        }
        catch (SecurityException e)
        {

        }

        String IMEIid =  imei.substring(9);

        cxz = IMEIid +"-"+cxz+"-"+VisitStartTS.replace(" ", "").replace(":", "").trim();

        return cxz;
        //-_
    }

    public void saveDataToDatabase()
    {
         UniqueDistribtrMapId=genTempID();
        System.out.println("TEMP ID...."+UniqueDistribtrMapId);

        String flgGSTCompliance = "NA";
        String GSTNumber = "NA";

       /* if (ll_gstDetails.getVisibility() == View.VISIBLE)
        {
            flgGSTCapture = "1";
        }
        else if (ll_gstDetails.getVisibility() == View.GONE)
        {
            flgGSTCapture = "0";
        }

        if (rb_gstYes.isChecked())
        {
            flgGSTCompliance = "1";
            if (!edit_gstYesVal.getText().toString().trim().equals(null) ||
                    !edit_gstYesVal.getText().toString().trim().equals(""))
            {
                GSTNumber = edit_gstYesVal.getText().toString().trim();
            }
        }
        else if (rb_gstNo.isChecked())
        {
            flgGSTCompliance = "0";
        }
        else if (rb_gstPending.isChecked())
        {
            flgGSTCompliance = "2";
        }*/

        String allLoctionDetails=  helperDb.getLocationDetails();

        String SOLattitudeFromLauncher="NA";
        String SOLongitudeFromLauncher="NA";
        String SOAccuracyFromLauncer="NA";

        String SOProviderFromLauncher="NA";

        String SOGpsLatFromLauncher="NA";
        String SOGpsLongFromLauncher="NA";
        String SOGpsAccuracyFromLauncher="NA";

        String SONetworkLatFromLauncher="NA";
        String SONetworkLongFromLauncher="NA";
        String SONetworkAccuracyFromLauncher="NA";

        String SOFusedLatFromLauncher="NA";
        String SOFusedLongFromLauncher="NA";
        String SOFusedAccuracyFromLauncher="NA";

        String SOAllProvidersLocationFromLauncher="NA";

        String SOGpsAddressFromLauncher="NA";
        String SONetwAddressFromLauncher="NA";
        String SOFusedAddressFromLauncher="NA";

        String SOFusedLocationLatitudeWithFirstAttemptFromLauncher="NA";
        String SOFusedLocationLongitudeWithFirstAttemptFromLauncher="NA";
        String SOFusedLocationAccuracyWithFirstAttemptFromLauncher="NA";

        if(!allLoctionDetails.equals("0"))
        {
            SOLattitudeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[0];
            SOLongitudeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[1];
            SOAccuracyFromLauncer = allLoctionDetails.split(Pattern.quote("^"))[2];

            AddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[3];
            CityFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[4];
            PincodeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[5];
            StateFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[6];

            SOProviderFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[7]; //finalProvider

            SOGpsLatFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[8];
            SOGpsLongFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[9];
            SOGpsAccuracyFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[10];

            SONetworkLatFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[11];
            SONetworkLongFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[12];
            SONetworkAccuracyFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[13];

            SOFusedLatFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[14];
            SOFusedLongFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[15];
            SOFusedAccuracyFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[16];

            SOAllProvidersLocationFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[17];

            SOGpsAddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[18];
            SONetwAddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[19];
            SOFusedAddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[20];

            SOFusedLocationLatitudeWithFirstAttemptFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[21];
            SOFusedLocationLongitudeWithFirstAttemptFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[22];
            SOFusedLocationAccuracyWithFirstAttemptFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[23];
        }
        if(SOProviderFromLauncher.equals("Fused"))
        {
            fnAddressFromLauncher=SOFusedAddressFromLauncher;
        }
        else if(SOProviderFromLauncher.equals("Gps"))
        {
            fnAddressFromLauncher=SOGpsAddressFromLauncher;
        }
        else if(SOProviderFromLauncher.equals("Network"))
        {
            fnAddressFromLauncher=SONetwAddressFromLauncher;
        }

        String finalAddress=etLocalArea.getText().toString().trim();
        String finalPinCode=etPinCode.getText().toString().trim();
        String finalCity=etCity.getText().toString().trim();
        String finalState=etState.getText().toString().trim();

        if((!etCity.getText().toString().trim().equalsIgnoreCase("Other")) && (!etCity.getText().toString().trim().equalsIgnoreCase("Others")) )
        {
            finalCity=etCity.getText().toString().trim();

        }
        else
        {
            if(etOtherCity.getVisibility()==View.VISIBLE)
            {
                finalCity=etOtherCity.getText().toString().trim();

            }

        }

        String cityID=hmapCity_details.get(etCity.getText().toString().trim());
        String StateID=hmapState_details.get(etState.getText().toString().trim());
        String MapAddress=address;
        String MapPincode=pincode;
        String MapCity=city;
        String MapState=state;

        String phoneNo=etPhoneNo.getText().toString();
        String emailID="NA";
        if(TextUtils.isEmpty(etMail.getText().toString().trim()) || etMail.getText().toString().equals(null))
        {
            emailID="NA";
        }
        else
        {
            emailID=etMail.getText().toString().trim();
        }

        String FirstName=ed_FirstName.getText().toString().trim();
        String LastName=ed_LastName.getText().toString().trim();
        String DOB=Text_Dob.getText().toString().trim();
        String DOM="";
        if(!Text_mrgAnnvrsry.getText().toString().trim().equals("Select Date"))
        {
            DOM=Text_mrgAnnvrsry.getText().toString().trim();
        }

        helperDb.open();
       /* helperDb.savetblDistributorMappingData(UniqueDistribtrMapId,DbrNodeId,DbrNodeType,flgGSTCapture,flgGSTCompliance,
                GSTNumber,finalAddress,finalPinCode,finalCity,finalState,SOLattitudeFromLauncher,SOLongitudeFromLauncher,
                SOAccuracyFromLauncer,"0",SOProviderFromLauncher,SOAllProvidersLocationFromLauncher,fnAddressFromLauncher,
                SOGpsLatFromLauncher,SOGpsLongFromLauncher,SOGpsAccuracyFromLauncher,SOGpsAddressFromLauncher,
                SONetworkLatFromLauncher,SONetworkLongFromLauncher,SONetworkAccuracyFromLauncher,SONetwAddressFromLauncher,
                SOFusedLatFromLauncher,SOFusedLongFromLauncher,SOFusedAccuracyFromLauncher,SOFusedAddressFromLauncher,
                SOFusedLocationLatitudeWithFirstAttemptFromLauncher,SOFusedLocationLongitudeWithFirstAttemptFromLauncher,
                SOFusedLocationAccuracyWithFirstAttemptFromLauncher,3,flgLocationServicesOnOff,flgGPSOnOff,flgNetworkOnOff,flgFusedOnOff,flgInternetOnOffWhileLocationTracking,flgRestart);
*/
        helperDb.savetblDistributorMappingData(UniqueDistribtrMapId,DbrNodeId,DbrNodeType,flgGSTCapture,flgGSTCompliance,
                GSTNumber,finalAddress,finalPinCode,finalCity,finalState,SOLattitudeFromLauncher,SOLongitudeFromLauncher,
                SOAccuracyFromLauncer,"0",SOProviderFromLauncher,SOAllProvidersLocationFromLauncher,fnAddressFromLauncher,
                SOGpsLatFromLauncher,SOGpsLongFromLauncher,SOGpsAccuracyFromLauncher,SOGpsAddressFromLauncher,
                SONetworkLatFromLauncher,SONetworkLongFromLauncher,SONetworkAccuracyFromLauncher,SONetwAddressFromLauncher,
                SOFusedLatFromLauncher,SOFusedLongFromLauncher,SOFusedAccuracyFromLauncher,SOFusedAddressFromLauncher,
                SOFusedLocationLatitudeWithFirstAttemptFromLauncher,SOFusedLocationLongitudeWithFirstAttemptFromLauncher,
                SOFusedLocationAccuracyWithFirstAttemptFromLauncher,3,flgLocationServicesOnOff,flgGPSOnOff,
                flgNetworkOnOff,flgFusedOnOff,flgInternetOnOffWhileLocationTracking,flgRestart, cityID, StateID,
                MapAddress, MapCity, MapPincode, MapState,phoneNo,emailID,FirstName,LastName,DOB,DOM);

        //System.out.println("TEMP SAVE DATA...."+UniqueDistribtrMapId+"--"+DbrNodeId+"--"+DbrNodeType);

        helperDb.close();
        try
        {
           FullSyncDataNow task = new FullSyncDataNow(DistributorMapActivity.this);
            task.execute();
        }
        catch (Exception e)
        {
            // TODO Autouuid-generated catch block
            e.printStackTrace();
            //System.out.println("onGetStoresForDayCLICK: Exec(). EX: "+e);
        }
    }

    public void BackBtnWorking()
    {

    }

    private class FullSyncDataNow extends AsyncTask<Void, Void, Void>
    {


        ProgressDialog pDialogGetStores;
        public FullSyncDataNow(DistributorMapActivity activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage("Submitting  Details...");
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();


        }

        @Override

        protected Void doInBackground(Void... params)
        {

            int Outstat=3;

            long  syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
            String StampEndsTime = df.format(dateobj);
            SimpleDateFormat df1 = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
            newfullFileName= CommonInfo.imei+"."+ df1.format(dateobj);
            LinkedHashMap<String,String> hmapstlist=new LinkedHashMap<String, String>();



            try {
                File LTFoodxmlFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                if (!LTFoodxmlFolder.exists())
                {
                    LTFoodxmlFolder.mkdirs();

                }

                DA.open();
                DA.export(CommonInfo.DATABASE_NAME, newfullFileName);
                DA.close();

                helperDb.savetbl_XMLfiles(newfullFileName, "3","2");
                helperDb.open();

                helperDb.UpdateDistributerFlag(UniqueDistribtrMapId, 4);


                helperDb.close();

                helperDb.fnupdateDisributorMstrLocationtrackRemapFlg(UniqueDistribtrMapId);

            } catch (Exception e)
            {

                e.printStackTrace();
                if(pDialogGetStores.isShowing())
                {
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
            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            try
            {
                if(isOnline())
                {
                    Intent syncIntent = new Intent(DistributorMapActivity.this, SyncMasterDistributor.class);
                    syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml");
                    syncIntent.putExtra("OrigZipFileName", newfullFileName);
                    syncIntent.putExtra("whereTo", "Regular");
                    syncIntent.putExtra("whereTo", "Regular");
                    startActivity(syncIntent);
                    finish();
                }
                else {


                    /*Intent i=new Intent(DistributorMapActivity.this,StorelistActivity.class);
                    startActivity(i);
                    finish();*/
                }


            } catch (Exception e) {

                e.printStackTrace();
            }


        }
    }

    public void shardPrefForCoverageArea(int coverageAreaNodeID,int coverageAreaNodeType) {




        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("CoverageAreaNodeID", coverageAreaNodeID);
        editor.putInt("CoverageAreaNodeType", coverageAreaNodeType);


        editor.commit();

    }


    public void shardPrefForSalesman(int salesmanNodeId,int salesmanNodeType) {




        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("SalesmanNodeId", salesmanNodeId);
        editor.putInt("SalesmanNodeType", salesmanNodeType);

        editor.commit();

    }

    public void flgDataScopeSharedPref(int _flgDataScope)
    {
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("flgDataScope", _flgDataScope);
        editor.commit();


    }
    public void flgDSRSOSharedPref(int _flgDSRSO)
    {
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("flgDSRSO", _flgDSRSO);
        editor.commit();


    }
    void openDSRTrackerAlert()
    {
        final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(DistributorMapActivity.this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dsr_tracker_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        final RadioButton rb_dataReport= (RadioButton) view.findViewById(R.id.rb_dataReport);
        final RadioButton rb_onMap= (RadioButton) view.findViewById(R.id.rb_onMap);


        Button btn_proceed= (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

        final android.support.v7.app.AlertDialog dialog=alert.create();
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if(rb_dataReport.isChecked())
                {
                    Intent i=new Intent(DistributorMapActivity.this,WebViewDSRDataReportActivity.class);
                    startActivity(i);

                }
                else if(rb_onMap.isChecked())
                {
                    Intent i = new Intent(DistributorMapActivity.this, WebViewDSRTrackerActivity.class);
                    startActivity(i);

                }

                else
                {
                    showAlertForEveryOne("Please select atleast one option to Proceeds.");
                }
            }
        });

        rb_dataReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_dataReport.isChecked())
                {
                    rb_onMap.setChecked(false);

                }
            }
        });

        rb_onMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_onMap.isChecked())
                {
                    rb_dataReport.setChecked(false);

                }
            }
        });



        dialog.show();
    }

    private void getStateDetails()
    {

        hmapState_details=helperDb.fngetDistinctState();

        int index=0;
        if(hmapState_details!=null && hmapState_details.size()>0)
        {
            stateNames=new ArrayList<String>();
            for(Map.Entry<String, String> entry:hmapState_details.entrySet())
            {
                stateNames.add(entry.getKey().toString());
            }

        }


    }

    private void getCityDetails()
    {

        hmapCity_details=helperDb.fngetCityList();

        int index=0;
        if(hmapCity_details!=null && hmapCity_details.size()>0)
        {
            cityNames=new ArrayList<String>();
            for(Map.Entry<String, String> entry:hmapCity_details.entrySet())
            {
                cityNames.add(entry.getKey().toString());
            }
        }


    }


    public void customAlertStateCityList(int flgCityState,final List<String> listOption, String sectionHeader,String StateCityName)
    {

        final Dialog listDialog = new Dialog(DistributorMapActivity.this);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listDialog.setContentView(R.layout.search_list);
        listDialog.setCanceledOnTouchOutside(false);
        listDialog.setCancelable(false);
        WindowManager.LayoutParams parms = listDialog.getWindow().getAttributes();
        parms.gravity = Gravity.CENTER;
        //there are a lot of settings, for dialog, check them all out!
        parms.dimAmount = (float) 0.5;
        isSelectedSearch=false;



        TextView txt_section=(TextView) listDialog.findViewById(R.id.txt_section);
        txt_section.setText(sectionHeader);
        TextView txtVwCncl=(TextView) listDialog.findViewById(R.id.txtVwCncl);
        //    TextView txtVwSubmit=(TextView) listDialog.findViewById(R.id.txtVwSubmit);

        final EditText ed_search=(AutoCompleteTextView) listDialog.findViewById(R.id.ed_search);
        ed_search.setVisibility(View.GONE);
        final ListView list_store=(ListView) listDialog.findViewById(R.id.list_store);
        final CardArrayAdapterCityState cardArrayAdapter = new CardArrayAdapterCityState(DistributorMapActivity.this,flgCityState,listOption,listDialog,StateCityName);







        list_store.setAdapter(cardArrayAdapter);
        //	editText.setBackgroundResource(R.drawable.et_boundary);


        boolean isSingleLine=true;

        txtVwCncl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                isSelectedSearch=false;

            }
        });




        //now that the dialog is set up, it's time to show it
        listDialog.show();

    }

    @Override
    public void selectedCityState(String selectedCategory, Dialog dialog, int flgCityState)
    {
        dialog.dismiss();
        if(flgCityState==1)
        {
            etCity.setText(selectedCategory);
            helperDb.updateAllDefaultCity(hmapCity_details.get(selectedCategory));
            previousSlctdCity=selectedCategory;
            if(selectedCategory.equalsIgnoreCase("Others") || selectedCategory.equalsIgnoreCase("Other"))
            {
                if(etOtherCity.getVisibility()==View.GONE)
                {
                    etOtherCity.setVisibility(View.VISIBLE);

                }
                etState.setText("Select");
                etState.setEnabled(true);
            }
            else
            {
                if(etOtherCity.getVisibility()==View.VISIBLE)
                {
                    etOtherCity.setVisibility(View.GONE);
                }

                if(hmapCityAgainstState!=null && hmapCityAgainstState.containsKey(selectedCategory))
                {
                    etState.setText(hmapCityAgainstState.get(selectedCategory));
                }
                etState.setEnabled(false);
            }
        }
        else
        {
            previousSlctdCity=selectedCategory;
            etState.setText(selectedCategory);
        }

    }

    public boolean fnValidateStateCity()
    {
        boolean boolValidateStateCity=true;
        if((!(etCity.getText().toString()).equalsIgnoreCase("Select")) && (!(etState.getText().toString()).equalsIgnoreCase("Select")) && (!(etCity.getText().toString()).equalsIgnoreCase("Others"))&& (!(etCity.getText().toString()).equalsIgnoreCase("Other")))
        {
            boolValidateStateCity=true;
        }
        else if(((etCity.getText().toString()).equalsIgnoreCase("Others")) || ((etCity.getText().toString()).equalsIgnoreCase("Other")))
        {
            if(TextUtils.isEmpty(etOtherCity.getText().toString().trim()))
            {
                boolValidateStateCity= false;
                showErrorAlert("Please Fill Other City Name");
            }
            else
            {

                if((etCity.getText().toString()).equalsIgnoreCase("Select"))
                {
                    showErrorAlert("Please Select City Name");
                    boolValidateStateCity= false;

                }
                else if((etState.getText().toString()).equalsIgnoreCase("Select"))
                {
                    showErrorAlert("Please Select State Name");
                    boolValidateStateCity= false;

                }


            }
        }
        else
        {

            if((etCity.getText().toString()).equalsIgnoreCase("Select"))
            {
                showErrorAlert("Please Select City Name");
                boolValidateStateCity= false;

            }
            else if((etState.getText().toString()).equalsIgnoreCase("Select"))
            {
                showErrorAlert("Please Select State Name");
                boolValidateStateCity= false;

            }


        }
        return boolValidateStateCity;
    }


    public void showErrorAlert(String message)
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(DistributorMapActivity.this);
        alertDialogNoConn.setTitle("Error");
        alertDialogNoConn.setMessage(message);
        alertDialogNoConn.setNeutralButton("OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        // finish();
                    }
                });

        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }

    public  void setFreshAddress()
    {

        if(!address.equals("NA"))
        {
            EditText edAddressFinal=null;


            if(edAddressFinal!=null)
            {
                edAddressFinal.setText("\n\n"+address);
            }

        }
        if(!pincode.equals("NA"))
        {
            etPinCode.setText(pincode);
        }
        if(!state.equals("NA"))
        {
            etState.setText(state);
        }
        boolean isCityFilled = false;
        etCity.setText("Select");
        previousSlctdCity="Select";

        if(!AddNewStore_DynamicSectionWiseSO.city.equals("NA")) {

            for (Map.Entry<String, String> entryCity : hmapCity_details.entrySet()) {
                if (entryCity.getKey().equalsIgnoreCase(AddNewStore_DynamicSectionWiseSO.city.trim())) {
                    etCity.setText(AddNewStore_DynamicSectionWiseSO.city);
                    isCityFilled = true;
                    defaultCity = entryCity.getKey();
                    break;
                }
            }

            if(isCityFilled)
            {
                if(!TextUtils.isEmpty(defaultCity))
                {
                    etCity.setText(defaultCity);
                    if(defaultCity.equalsIgnoreCase("Others") || defaultCity.equalsIgnoreCase("Other"))
                    {
                        etState.setEnabled(true);
                        etState.setText("Select");
                    }
                    else
                    {
                        if(hmapCityAgainstState!=null && hmapCityAgainstState.containsKey(defaultCity))
                        {
                            etState.setText(hmapCityAgainstState.get(defaultCity));
                        }
                        else
                        {
                            etState.setText("Select");
                        }
                    }

                }
                else
                {
                    etState.setText("Select");
                }
            }
            else
            {
                etState.setText("Select");
            }
            //
        }

        else
        {
            if(!TextUtils.isEmpty(defaultCity))
            {
                etCity.setText(defaultCity);
                if(defaultCity.equalsIgnoreCase("Others") || defaultCity.equalsIgnoreCase("Other"))
                {
                    etState.setEnabled(true);
                }
                else
                {
                    if(hmapCityAgainstState!=null && hmapCityAgainstState.containsKey(defaultCity))
                    {
                        etState.setText(hmapCityAgainstState.get(defaultCity));
                    }
                }

            }
        }

		/*if(!AddNewStore_DynamicSectionWise.city.equals("NA"))
		{
			etCity.setText(AddNewStore_DynamicSectionWise.city);
		}
		if(!AddNewStore_DynamicSectionWise.state.equals("NA"))
		{
			etState.setText(AddNewStore_DynamicSectionWise.state);
		}*/
    }
}
