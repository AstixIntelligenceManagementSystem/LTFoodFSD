package project.astix.com.ltfoodfsd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import android.app.*;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.astix.Common.CommonInfo;

import com.example.gcm.NotificationActivity;


//import com.astix.sfatju.R;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreSelection extends BaseActivity implements com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{
	public int chkFlgForErrorToCloseApp=0;
	LinkedHashMap<String, String> hmapdsrIdAndDescr_details=new LinkedHashMap<String, String>();
	String[] drsNames;
	public String	SelectedDSRValue="";
	SharedPreferences sharedPref;
	int slctdCoverageAreaNodeID=0,slctdCoverageAreaNodeType=0,slctdDSrSalesmanNodeId=0,slctdDSrSalesmanNodeType=0;

	int coverageId=0,coverageType=0;
	Spinner spinner_manager;
	Spinner spinner_RouteList;

	String[] Manager_names=null;
	String[] Route_names=null;
	//String[] Manager_names= { "Select Market Location", "sec-20", "sec-24", "other"};
	static String selected_manager="NA";
	static String seleted_routeIDType="0";
	RelativeLayout rl_for_other;
	EditText ed_Street;
	static int Selected_manager_Id=0;



	HashMap<String, String> hmapManagerNameManagerIdDetails=new HashMap<String, String>();

	HashMap<String, String> hmapRouteIdNameDetails=new HashMap<String, String>();



	boolean serviceException=false;
    
  public static final String DATASUBDIRECTORYForText = CommonInfo.TextFileFolder;
	
	public String passDate;
	public SimpleDateFormat sdf;
	public String fDate;
	public String userDate;
	
	public String pickerDate;
	public String imei;
	public String[] storeList;
	public String[] storeRouteIdType;
	Dialog dialog;
	
	CheckBox check1, check2;
	public TableLayout tl2;
	RelativeLayout relativeLayout1;
	int battLevel=0;
	
	public static HashMap<String, String> hmapStoreIdSstat=new HashMap<String, String>();
	public int flgDayEndOrChangeRoutenew=0;
	LinkedHashMap<String, String> hmapOutletListForNear=new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> hmapOutletListForNearUpdated=new LinkedHashMap<String, String>();

	static int flgChangeRouteOrDayEnd = 0;
	 
	ProgressDialog pDialogGetStores;
	ProgressDialog mProgressDialog;
	public String Noti_text="Null";
	public int MsgServerID=0;
	 
	public boolean[] checks;
	ServiceWorker newservice = new ServiceWorker();
	static ScheduledExecutorService scheduler;
	public static ScheduledExecutorService schPHSTATS;

	public int noLOCflag = 0;
	boolean bool = true;
    DatabaseAssistant DA = new DatabaseAssistant(this);
	public ProgressDialog pDialogSync;

	ImageView img_side_popUp;
	int closeList = 0;
	int whatTask = 0;
	String whereTo = "11";

	ArrayList mSelectedItems = new ArrayList();

	int prevSel = 0;
	int prevID;
	public long syncTIMESTAMP;
	public String fullFileName1;

	public String[] storeCode;
	public String[] storeName;

    public String[] Prescheduled;
    ArrayList<String> stIDs;
	ArrayList<String> stNames;

	public String[] storeStatus;
	
	public String[] StoreflgSubmitFromQuotation;
	
	
    public String[] storeCloseStatus;

	public String[] storeNextDayStatus;
    public ListView listView;
	public ProgressDialog pDialog2STANDBY;
    DBAdapterKenya dbengine = new DBAdapterKenya(this);
	

	public TableRow tr;
    public String selStoreID = "";
	public String selStoreName = "";
    public String prevSelStoreID;

	public Double myCurrentLon; // removed "static"
	public Double myCurrentLat;
	public Double finalLatNow;
	public Double finalLonNow;
	
	public int gotLoc = 0;
    public int locStat = 0;
    ProgressDialog pDialog2;
    String FWDCLname;
    
	String BCKCLname;
    public Location firstLoc;
	public float acc;
	//DBAdapterLtFoods dbengineSo = new DBAdapterLtFoods(this);
    
	public Location location2;
    public String[] StoreList2Procs;
    public Location finalLocation;
    standBYtask task_STANDBY = new standBYtask();

	
    private final Context mContext = this;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    double latitude; 
	double longitude; 

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 1; // 1 second

	// Declaring a Location Manager
	protected LocationManager locationManager;
	public int valDayEndOrChangeRoute=0; // 1=Clicked on Day End Button, 2=Clicked on Change Route Button
	
	
	public String[] route_name;	
	public String[] route_name_id;
	public String selected_route_id="0";
	
	private int selected = 0;
	public String temp_select_routename="NA";
	public String temp_select_routeid="NA";
	public String rID;
	public   PowerManager pm;
	public	 PowerManager.WakeLock wl;
	public Location location;
	public String AllProvidersLocation="";
	public String FusedLocationLatitudeWithFirstAttempt="0";
	public String FusedLocationLongitudeWithFirstAttempt="0";
	public String FusedLocationAccuracyWithFirstAttempt="0";
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
	public AppLocationService appLocationService;
	public CoundownClass countDownTimer;
	private final long startTime = 15000;
	private final long interval = 200;

	private static final String TAG = "LocationActivity";
	private static final long INTERVAL = 1000 * 10;
	private static final long FASTEST_INTERVAL = 1000 * 5;
	GoogleApiClient mGoogleApiClient;
	Location mCurrentLocation;
	String mLastUpdateTime;
	String fusedData;
	public String fnAccurateProvider="";
	public String fnLati="0";
	public String fnLongi="0";
	public Double fnAccuracy=0.0;

	LocationRequest mLocationRequest;
	LinkedHashMap<String,String> hmapStoreLatLongDistanceFlgRemap=new LinkedHashMap<String,String>();

    //report alert
    String[] Distribtr_list;
    String DbrNodeId,DbrNodeType,DbrName;
    ArrayList<String> DbrArray=new ArrayList<String>();
    LinkedHashMap<String,String> hmapDistrbtrList=new LinkedHashMap<>();
    public String SelectedDistrbtrName="";
	

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
	
	/*@Override
	public void onWindowFocusChanged(boolean hasFocus)   // Force PDA donot show Statusbar to the user
    {
		// TODO Auto-generated method stub
		// super.onWindowFocusChanged(hasFocus);
   
		try 
		{
			if (!hasFocus) 
			{
				Object service = getSystemService("statusbar");
				Class<?> statusbarManager = Class
						.forName("android.app.StatusBarManager");
				Method collapse = statusbarManager.getMethod("collapse");
				collapse.setAccessible(true);
				collapse.invoke(service);
			}
		} 
		catch (Exception ex) 
		{
		}

	}
	*/
	
	
	
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {

			battLevel = intent.getIntExtra("level", 0);

		}
	};
	public void locationRetrievingAndDistanceCalculating()
	{
		appLocationService = new AppLocationService();

		pm = (PowerManager) getSystemService(POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "INFO");
		wl.acquire();


		pDialog2STANDBY = ProgressDialog.show(StoreSelection.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.searchingnearbystores), true);
		pDialog2STANDBY.setIndeterminate(true);

		pDialog2STANDBY.setCancelable(false);
		pDialog2STANDBY.show();

		if (isGooglePlayServicesAvailable()) {
			createLocationRequest();

			mGoogleApiClient = new GoogleApiClient.Builder(StoreSelection.this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(StoreSelection.this)
					.addOnConnectionFailedListener(StoreSelection.this)
					.build();
			mGoogleApiClient.connect();
		}
		//startService(new Intent(DynamicActivity.this, AppLocationService.class));


		startService(new Intent(StoreSelection.this, AppLocationService.class));
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
		startLocationUpdates();
	}
	protected void startLocationUpdates() {
		PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

	}
	@Override
	public void onConnectionSuspended(int i) {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
		locationManager.removeUpdates(appLocationService);
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

	protected void stopLocationUpdates() {

		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);




	}
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
		locationManager.removeUpdates(appLocationService);
	}

	@Override
	public void onLocationChanged(Location args0) {
		mCurrentLocation = args0;
		mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
		updateUI();
	}

	public class standBYtask extends AsyncTask<Void, Void, Void> 
	{

		@Override
		protected Void doInBackground(Void... params) 
		{
			// TODO Auto-generated method stub
            return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}



	// *****SYNC******

	public void SyncNow() 
	{

		syncTIMESTAMP = System.currentTimeMillis();
		Date dateobj = new Date(syncTIMESTAMP);
		
		
		dbengine.open();
		String presentRoute=dbengine.GetActiveRouteID();
		dbengine.close();
		//syncTIMESTAMP = System.currentTimeMillis();
		//Date dateobj = new Date(syncTIMESTAMP);
		SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
		//fullFileName1 = df.format(dateobj);
		String newfullFileName=imei+"."+presentRoute+"."+ df.format(dateobj);

		

		 try 
		   {
			 
			 File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
				
			 if (!OrderXMLFolder.exists())
				{
					OrderXMLFolder.mkdirs();
				}
			 
			 String routeID=dbengine.GetActiveRouteIDSunil();
			 
				DA.open();
				DA.export(dbengine.DATABASE_NAME, newfullFileName,routeID);
				
				
				DA.close();
				
				dbengine.savetbl_XMLfiles(newfullFileName, "3","1");
				dbengine.open();
				for (int nosSelected = 0; nosSelected <= mSelectedItems.size() - 1; nosSelected++) 
				{
					String valSN = (String) mSelectedItems.get(nosSelected);
					int valID = stNames.indexOf(valSN);
					String stIDneeded = stIDs.get(valID);

					dbengine.UpdateStoreFlagAtDayEndOrChangeRoute(stIDneeded, 4);
					dbengine.UpdateStoreMaterialphotoFlag(stIDneeded.trim(), 5);
					dbengine.UpdateStoreReturnphotoFlag(stIDneeded.trim(), 5);
					dbengine.updateflgFromWhereSubmitStatusAgainstStore(stIDneeded, 1);
					
					if(dbengine.fnchkIfStoreHasInvoiceEntry(stIDneeded)==1)
					{
						dbengine.updateStoreQuoteSubmitFlgInStoreMstrInChangeRouteCase(stIDneeded, 0);
					}

					
				}
				dbengine.close();
	
				flgChangeRouteOrDayEnd=valDayEndOrChangeRoute;
				
				Intent syncIntent = new Intent(StoreSelection.this, SyncMaster.class);
				//syncIntent.putExtra("xmlPathForSync",Environment.getExternalStorageDirectory() + "/TJUKIndirectSFAxml/" + newfullFileName + ".xml");
			    syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml");
				syncIntent.putExtra("OrigZipFileName", newfullFileName);
				syncIntent.putExtra("whereTo", whereTo);
	            startActivity(syncIntent);
				finish();
		   } 
		 catch (IOException e) 
		   {
                e.printStackTrace();
		   }

	}

	/*private class FullSyncDataNow extends AsyncTask<Void, Void, Void> {

		

		@Override
		protected Void doInBackground(Void... params) {

			try {
				
			}

			catch (Exception e) {
			//	Log.i("Sync ASync", "Sync ASync Failed!", e);

			}

			finally {

			}
			return null;
		}

		@Override
		protected void onCancelled() {

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			 pDialogSync.dismiss(); 

			//finish();
		}
	}*/

	// *****SYNC******

	
	private class bgTasker extends AsyncTask<Void, Void, Void> {

		// obj(s) for services/sync..blah..blah

		@Override
		protected Void doInBackground(Void... params) {

			try {
				//System.out.println("starting bgTasker Exec().....: ");
				
				

				
					dbengine.open();
					String rID=dbengine.GetActiveRouteID();
					dbengine.UpdateTblDayStartEndDetails(Integer.parseInt(rID), valDayEndOrChangeRoute);
					//System.out.println("TblDayStartEndDetails Background: "+ rID);
					//System.out.println("TblDayStartEndDetails Background valDayEndOrChangeRoute: "+ valDayEndOrChangeRoute);
					dbengine.close();
				
				//System.out.println("Induwati   whatTask :"+whatTask);
				
				if (whatTask == 2) 
				{
					whatTask = 0;
					// stores with Sstat = 1 !
					dbengine.open();
					// dbengine.fnTruncateTblSelectedStoreIDinChangeRouteCase();
					for (int nosSelected = 0; nosSelected <= mSelectedItems.size() - 1; nosSelected++) 
					{
						String valSN = (String) mSelectedItems.get(nosSelected);
						int valID = stNames.indexOf(valSN);
						String stIDneeded = stIDs.get(valID);

						// String[] stIDs;
						// String[] stNames;

						dbengine.UpdateStoreFlagAtDayEndOrChangeRoute(stIDneeded, 3);

						//System.out.println("stIDneeded : " + stIDneeded);
						dbengine.insertTblSelectedStoreIDinChangeRouteCase(stIDneeded);
						dbengine.updateflgFromWhereSubmitStatusAgainstStore(stIDneeded, 1);
						if(dbengine.fnchkIfStoreHasInvoiceEntry(stIDneeded)==1)
						{
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
				

				}else if (whatTask == 3) {
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
				}else if (whatTask == 1) {
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

			}

			finally {

				Log.i("bgTasker", "bgTasker Execution Completed...");

			}

			return null;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			pDialog2 = ProgressDialog.show(StoreSelection.this,getText(R.string.PleaseWaitMsg),getText(R.string.genTermProcessingRequest), true);
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

	

	public void showSettingsAlert() 
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle(R.string.AlertDialogHeaderMsg);
		alertDialog.setIcon(R.drawable.error_info_ico);
		alertDialog.setCancelable(false);
		alertDialog.setMessage(R.string.genTermGPSDisablePleaseEnable);

		alertDialog.setPositiveButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
		         {
					public void onClick(DialogInterface dialog, int which)
					   {
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
					   }
				});

		alertDialog.show();
	}
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
	 public void showNoConnAlert() 
	 {
			AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(StoreSelection.this);
			alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
			alertDialogNoConn.setMessage(R.string.NoDataConnectionFullMsg);
			
			alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
			        {
						public void onClick(DialogInterface dialog, int which) 
						{
                           dialog.dismiss();
                        }
					});
			alertDialogNoConn.setIcon(R.drawable.error_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();
			
		}
	public void enableGPSifNot()
	    {
		
			boolean isGPSok = false;
			isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			if (!isGPSok) 
			  {
				showSettingsAlert();
				isGPSok = false;
			  } 
		}
	
	

	
	public void DayEnd()
	{

		
		AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(StoreSelection.this);
		
		LayoutInflater inflater = getLayoutInflater();    
	     View view=inflater.inflate(R.layout.titlebar, null);
	     alertDialogSubmitConfirm.setCustomTitle(view);
	     TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
	     title_txt.setText(getText(R.string.PleaseConformMsg));
	     

		View view1=inflater.inflate(R.layout.custom_alert_dialog, null);
		 view1.setBackgroundColor(Color.parseColor("#1D2E3C"));
		 TextView msg_txt = (TextView) view1.findViewById(R.id.msg_txt);
		 msg_txt.setText(getText(R.string.genTermDayEndAlert));
	     alertDialogSubmitConfirm.setView(view1);
	     alertDialogSubmitConfirm.setInverseBackgroundForced(true);
	    
	    

		alertDialogSubmitConfirm.setNeutralButton(R.string.AlertDialogYesButton,new DialogInterface.OnClickListener()
		      {
					public void onClick(DialogInterface dialog, int which) 
					    {
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
						} 
						else {
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

		alertDialogSubmitConfirm.setNegativeButton(R.string.AlertDialogNoButton,new DialogInterface.OnClickListener()
		         {

					@Override
					public void onClick(DialogInterface dialog, int which) 
					  {
						dialog.dismiss();
					  }
				});

		alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
		AlertDialog alert = alertDialogSubmitConfirm.create();
		alert.show();

	}

	
public void DayEndWithoutalert()
    {
	
		dbengine.open();
		String rID=dbengine.GetActiveRouteID();

		dbengine.UpdateTblDayStartEndDetails(Integer.parseInt(rID), valDayEndOrChangeRoute);
		dbengine.close();
		
		SyncNow();
	
    }

	

	public void showChangeRouteConfirm() 
	  {

		AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(StoreSelection.this);
		alertDialogSubmitConfirm.setTitle(R.string.PleaseConformMsg);
		if(flgDayEndOrChangeRoutenew==1)
		{
			alertDialogSubmitConfirm.setMessage(getText(R.string.genTermDayEndAlertWithoutStoreSubmit));	
		}
		else if(flgDayEndOrChangeRoutenew==2)
		{
			alertDialogSubmitConfirm.setMessage(getText(R.string.genTermchangeRouteAlert));
		}
		
		alertDialogSubmitConfirm.setCancelable(false);

		alertDialogSubmitConfirm.setNeutralButton(R.string.AlertDialogYesButton,new DialogInterface.OnClickListener()
		   {
					public void onClick(DialogInterface dialog, int which) 
					   {

						// Location_Getting_Service.closeFlag = 1;
						//enableGPSifNot();

						// run bgTasker()!

						// if(!scheduler.isTerminated()){
						// scheduler.shutdownNow();
						// }
						dbengine.open();

						if (dbengine.GetLeftStoresChk() == true) {
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
							// show dialog for clear..clear + tranx to launcher

							// -- Route Info Exec()
							try {
								dbengine.close();

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

							/*Intent revupOldFriend = new Intent(StoreSelection.this, LauncherActivity.class);
							 revupOldFriend.putExtra("imei", imei);
							startActivity(revupOldFriend);
							finish();*/
						}

					}
				});

		alertDialogSubmitConfirm.setNegativeButton(R.string.AlertDialogNoButton,new DialogInterface.OnClickListener()
		        {

					@Override
					public void onClick(DialogInterface dialog, int which)
					  {
						dialog.dismiss();
					  }
				});

		alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
		AlertDialog alert = alertDialogSubmitConfirm.create();
		alert.show();

	}

	
	
	
	public void showChangeRouteConfirmWhenNoStoreisLeftToSubmit() 
	  {

		AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(StoreSelection.this);
		alertDialogSubmitConfirm.setTitle(R.string.PleaseConformMsg);
		alertDialogSubmitConfirm.setMessage(getText(R.string.genTermchangeRouteAlertWhenNoStoreisLeftToSubmit));
		alertDialogSubmitConfirm.setCancelable(false);

		alertDialogSubmitConfirm.setNeutralButton(R.string.AlertDialogYesButton,new DialogInterface.OnClickListener()
		    {
					public void onClick(DialogInterface dialog, int which)
					   {

						// Location_Getting_Service.closeFlag = 1;
						//enableGPSifNot();

						// run bgTasker()!

						// if(!scheduler.isTerminated()){
						// scheduler.shutdownNow();
						// }
						dbengine.open();

						if (dbengine.GetLeftStoresChk() == true) {
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
							// show dialog for clear..clear + tranx to launcher

							// -- Route Info Exec()
							try {
								dbengine.close();

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

							/*Intent revupOldFriend = new Intent(
									StoreSelection.this, LauncherActivity.class);
							 revupOldFriend.putExtra("imei", imei);
							startActivity(revupOldFriend);
							finish();*/
						}

					}
				});

		alertDialogSubmitConfirm.setNegativeButton(R.string.AlertDialogNoButton,new DialogInterface.OnClickListener()
		        {
                    @Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});

		alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
		AlertDialog alert = alertDialogSubmitConfirm.create();
		alert.show();

	}

    void getDistribtrList()
    {
        dbengine.open();

        Distribtr_list=dbengine.getDistributorDataMstr();
        dbengine.close();
        for(int i=0;i<Distribtr_list.length;i++)
        {
            String value=Distribtr_list[i];
            DbrNodeId=value.split(Pattern.quote("^"))[0];
            DbrNodeType=value.split(Pattern.quote("^"))[1];
            DbrName=value.split(Pattern.quote("^"))[2];
            //flgReMap=Integer.parseInt(value.split(Pattern.quote("^"))[3]);

            hmapDistrbtrList.put(DbrName,DbrNodeId+"^"+DbrNodeType);
            DbrArray.add(DbrName);
        }

    }



    public void showPendingStorelist(int flgDayEndOrChangeRoute)
	  {

		// final CharSequence[] items =
		// {"cat1","cat2","cat3","cat4","cat5","cat6","cat7","cat8","cat9","cat10","cat11","cat12","cat13","cat14","cat15","cat16","cat17","cat18","cat19","cat20","cat21","cat22","cat23","cat24"
		// };
		
		//flgDayEndOrChangeRoutenew=1-DayEnd,2-Change Route
		flgDayEndOrChangeRoutenew=flgDayEndOrChangeRoute;
		ContextThemeWrapper cw = new ContextThemeWrapper(this,R.style.AlertDialogTheme);
	
		
		AlertDialog.Builder builder = new AlertDialog.Builder(cw);
		//builder.setTitle(R.string.genTermSelectStoresPendingToComplete);
		TextView content = new TextView(this);
		if(flgDayEndOrChangeRoutenew==1)
		{
			content.setText(R.string.genTermSelectStoresPendingToCompleteDayEnd);	
		}
		else if(flgDayEndOrChangeRoutenew==2)
		{
			content.setText(R.string.genTermSelectStoresPendingToCompleteChangeRoute);
		}
        //content.setText(R.string.genTermSelectStoresPendingToComplete);
        content.setTextSize(16);
        content.setTextColor(Color.WHITE);
        builder.setCustomTitle(content);
		mSelectedItems.clear();
		
		final String[] stNames4List = new String[stNames.size()];
		 checks=new boolean[stNames.size()];
		stNames.toArray(stNames4List);
		for(int cntPendingList=0;cntPendingList<stNames4List.length;cntPendingList++)
		{
			mSelectedItems.add(stNames4List[cntPendingList]);
			 checks[cntPendingList]=true;
		}

		builder.setMultiChoiceItems(stNames4List, checks,new DialogInterface.OnMultiChoiceClickListener()
		   {

					@Override
					public void onClick(DialogInterface dialog, int which,boolean isChecked)
					{

						if (isChecked) 
						  {
							mSelectedItems.add(stNames4List[which]);

						   }
						 else if (mSelectedItems.contains(stNames4List[which]))
						   {
							////System.out.println("Abhinav store Selection  Step 5");
							mSelectedItems.remove(stNames4List[which]);

						   }
					}
			});

		builder.setPositiveButton(R.string.genTermSubmitSelected,new DialogInterface.OnClickListener()
		    {

					@Override
					public void onClick(DialogInterface dialog, int which) 
					   {

						if (mSelectedItems.size() == 0) 
						  {
							Toast.makeText(getApplicationContext(),R.string.genTermNoStroeSelectedOnSubmit,Toast.LENGTH_SHORT).show();
							showPendingStorelist(flgDayEndOrChangeRoutenew);
						   }

						else 
						  {
							// TODO Auto-generated method stub
							// Toast.makeText(getApplicationContext(),
							// "User Selected : " + mSelectedItems.toString(),
							// Toast.LENGTH_SHORT).show();
							//System.out.println("User Selected : "+ mSelectedItems.toString());

							// Location_Getting_Service.closeFlag = 1;
							//enableGPSifNot();
							// doing stuff here

							// scheduler.shutdownNow();
							// if(!scheduler.isTerminated()){
							// scheduler.shutdownNow();
							// }
							// run bgTasker()!
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
				});
		builder.setNeutralButton(R.string.genTermDirectlyChangeRoute,new DialogInterface.OnClickListener()
		   {

					@Override
					public void onClick(DialogInterface arg0, int arg1) 
					  {
						// TODO Auto-generated method stub
						closeList = 1;
						// showChangeRouteConfirm();
						dbengine.open();

						if (dbengine.GetLeftStoresChk() == true) 
						  {
							// run bgTasker()!

							// Location_Getting_Service.closeFlag = 1;
							// scheduler.shutdownNow();
							// if(!scheduler.isTerminated()){
							//enableGPSifNot();
							// scheduler.shutdownNow();
							// }

							whatTask = 3;
							// -- Route Info Exec()
							try 
							   {
                                   new bgTasker().execute().get();
							   }
							catch (InterruptedException e)
							   {
								e.printStackTrace();
							   } 
							catch (ExecutionException e)
							   {
								e.printStackTrace();
							   }
							
							dbengine.close();

						 } 
						else 
						 {
							dbengine.close();
                            // show dialog for clear..clear + tranx to launcher
							showChangeRouteConfirm();
						 }
					}
				});
		builder.setNegativeButton(R.string.txtOnChangeRouteDayEndCancel,new DialogInterface.OnClickListener()
		       {
                   @Override
					public void onClick(DialogInterface arg0, int arg1)
                       {
						// TODO Auto-generated method stub
						closeList = 1;
						valDayEndOrChangeRoute=0;
                       }
			   });

		AlertDialog alert = builder.create();
		if (closeList == 1) 
		{
			closeList = 0;
			alert.dismiss();

		} 
		else
		{
			alert.show();
			alert.setCancelable(false);
		}
	}

	public void midPart() 
	{
		String tempSID;
		String tempSNAME;

		stIDs = new ArrayList<String>(StoreList2Procs.length);
		stNames = new ArrayList<String>(StoreList2Procs.length);
		
		for (int x = 0; x < (StoreList2Procs.length); x++) 
		{
			StringTokenizer tokens = new StringTokenizer(String.valueOf(StoreList2Procs[x]), "%");
			tempSID = tokens.nextToken().trim();
			tempSNAME = tokens.nextToken().trim();

			stIDs.add(x, tempSID);
			stNames.add(x, tempSNAME);
		}
	}



	public void onDestroy() 
	{
		super.onDestroy();
		// unregister receiver
		try {
			if (dbengine.isDBOpen()) {
				dbengine.close();
			}
			this.unregisterReceiver(this.mBatInfoReceiver);

		}catch(Exception e)
		{

		}
		//this.unregisterReceiver(this.KillME);
    }




	
	private class GetDataForChangeRoute extends AsyncTask<Void, Void, Void>
	{		
		 ProgressDialog pDialogGetStores;
		public GetDataForChangeRoute(StoreSelection activity) 
		{
			pDialogGetStores = new ProgressDialog(activity);
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			
			
			
			pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
			pDialogGetStores.setMessage(getText(R.string.genTermRetrivingData));
			pDialogGetStores.setIndeterminate(false);
			pDialogGetStores.setCancelable(false);
			pDialogGetStores.setCanceledOnTouchOutside(false);
			pDialogGetStores.show();
		}

		@Override
		protected Void doInBackground(Void... args) 
		{
			ServiceWorker newservice = new ServiceWorker();
			
			
		try {
			
			for(int mm = 1; mm < 2  ; mm++)
			{
			
				if(mm==1)
				{
				//	newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID);
				}
				
				
			}
			
		
		} catch (Exception e) {
				Log.i("SvcMgr", "Service Execution Failed!", e);

			}

			finally {

				Log.i("SvcMgr", "Service Execution Completed...");

			}
			//return newservice.director;
			return null;
		}
		
	
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			
			Log.i("SvcMgr", "Service Execution cycle completed");
			
			 System.out.println("on Post execute called");
		      if (pDialogGetStores.isShowing()) 
		      {
		    	   pDialogGetStores.dismiss();
			  }
		      
		      Bundle   tempBundle = new Bundle();
		      onCreate(tempBundle);
		
		}
	}
	
	
/*	private class GetStoresForDay extends AsyncTask<Void, Void, Void>
	{		
		ServiceWorker newservice = new ServiceWorker();
		
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		
			mProgressDialog.show();
			
		
	       
		}

		@Override
		protected Void doInBackground(Void... params) 
		{

		try {
			dbengine.open();
			String rID=dbengine.GetActiveRouteID();
			dbengine.close();
			for(int mm = 1; mm < 10  ; mm++)
			{
				
				if(mm==1)
				{  
					
					if(isOnline())
					{
				        newservice = newservice.getallProduct(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=2)
						{
							   serviceException=true;
								break;
						}
					}
					
				}
				
				if(mm==2)
				{  
					if(isOnline())
					{
					
						Date currDateNew = new Date();
						SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
						
						String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
						newservice = newservice.getAllNewSchemeStructure(getApplicationContext(), currSysDateNew, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=4)
						{
							   serviceException=true;
								break;
						}
					
					}
					
				}
				if(mm==3)
				{
				
						newservice = newservice.getCategory(getApplicationContext(), imei);
						if(newservice.flagExecutedServiceSuccesfully!=3)
						{
							
								serviceException=true;
								break;
						
						}
				}
				if(mm==4)
				{
						Date currDateNew = new Date();
					    SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
					
					    String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
						newservice = newservice.getallPDASchAppListForSecondPage(getApplicationContext(), currSysDateNew, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=5)
						{
							    serviceException=true;
								break;
						}
				}
				
				if(mm==5)
				{

						dbengine.open();
						hmapStoreIdSstat=dbengine.checkForStoreIdSstat();
						
						dbengine.close();
						newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=1)
						{
							    serviceException=true;
								break;
						}
					
				}
				if(mm==6)
				{
						*//*newservice = newservice.getStoreTypeMstr(getApplicationContext(), fDate, imei);
						if(newservice.flagExecutedServiceSuccesfully!=37)
						{
							
								serviceException=true;
								break;
						
						}*//*
					
				}
				if(mm==7)
				{
						newservice = newservice.gettblTradeChannelMstr(getApplicationContext(), fDate, imei);
						if(newservice.flagExecutedServiceSuccesfully!=38)
						{
							    serviceException=true;
								break;
						}
					
				}
				
				
				if(mm==8)
				{
						*//*newservice = newservice.getStoreProductClassificationTypeListMstr(getApplicationContext(), fDate, imei);
						if(newservice.flagExecutedServiceSuccesfully!=39)
						{
							    serviceException=true;
								break;
						}*//*
					
					
				}
				
				if(mm==9)
				{
					
				}
				
				
				
				
			}
			
			
		} catch (Exception e) {
				Log.i("SvcMgr", "Service Execution Failed!", e);

			}

			finally {

				Log.i("SvcMgr", "Service Execution Completed...");

			}
			//return newservice.director;
			return null;
		}

		@Override
		protected void onCancelled() {
			Log.i("SvcMgr", "Service Execution Cancelled");
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			
			Log.i("SvcMgr", "Service Execution cycle completed");

			if(mProgressDialog != null)
			{
			if(mProgressDialog.isShowing())
			{
				mProgressDialog.dismiss();
			}
			}
			
			if(serviceException)
			{
				showAlertException("Error.....","Error while Retrieving Data!\n Please Retry");
				serviceException=false;
			}
			tl2.removeAllViews();
			setStoresList();
			
		}
	}
	*/
	public void setUpVariable()
	{



		Button btn_nearStores = (Button) findViewById(R.id.btn_nearStores);
		btn_nearStores.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(pDialog2STANDBY!=null)
				{
					if(pDialog2STANDBY.isShowing())
					{


					}
					else
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
							locationRetrievingAndDistanceCalculating();
						}
					}
				}
				else
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
						locationRetrievingAndDistanceCalculating();
					}

				}
			}
		});

		ImageView image_Notification = (ImageView) findViewById(R.id.image_Notification);
		image_Notification.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
			
			    Intent intent = new Intent(StoreSelection.this, NotificationActivity.class);
			    startActivity(intent);
				finish();
				
			}
		});
		
		
		/* Button butn_refresh_data = (Button) findViewById(R.id.butn_refresh_data);
		 butn_refresh_data.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isOnline())
				{

				System.out.println("Testing abcs ");
				AlertDialog.Builder alertDialogBuilderNEw11 = new AlertDialog.Builder(StoreSelection.this);
				
					// set title
				alertDialogBuilderNEw11.setTitle("Information");
		 
					// set dialog message
				alertDialogBuilderNEw11.setMessage("Are you sure to refresh Data?");
				alertDialogBuilderNEw11.setCancelable(false);
				alertDialogBuilderNEw11.setPositiveButton("Ok",new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
								try
		    				    {	
		    						new GetStoresForDay().execute();
		    						////System.out.println("SRVC-OK: "+ new GetStoresForDay().execute().get());
		    					} catch (Exception e) {
		    						// TODO Autouuid-generated catch block
		    						e.printStackTrace();
		    						//System.out.println("onGetStoresForDayCLICK: Exec(). EX: "+e);
		    					}
								
								//onCreate(new Bundle());
							}
						  });
					
				alertDialogBuilderNEw11.setNegativeButton(R.string.txtNo,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// System.out.println("value ofwhatTask after no button pressed by sunil"+whatTask);

										dialog.dismiss();
									}
								});

				alertDialogBuilderNEw11.setIcon(R.drawable.info_ico);
				AlertDialog alert121 = alertDialogBuilderNEw11.create();
				alert121.show();
				}		
				else
				 {
					 showNoConnAlert();
					 return;

				 }
				
			}
		});
		 */
		 
		 Button add_new_store = (Button) findViewById(R.id.but_add_store);
			add_new_store.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					/*Intent intent = new Intent(StoreSelection.this, AddNewStore_DynamicSectionWise.class);
					intent.putExtra("storeID", "0");
					intent.putExtra("activityFrom", "StoreSelection");
					intent.putExtra("userdate", userDate);
					intent.putExtra("pickerDate", pickerDate);
					intent.putExtra("imei", imei);
					intent.putExtra("rID", rID);
					StoreSelection.this.startActivity(intent);
					finish();*/
					
					/*// TODO Auto-generated method stub
					Intent intent = new Intent(StoreSelection.this, Add_New_Store_DynamicSectionWise.class);
					intent.putExtra("activityFrom", "StoreSelection");
					intent.putExtra("userdate", userDate);
					intent.putExtra("pickerDate", pickerDate);
					intent.putExtra("imei", imei);
					intent.putExtra("rID", rID);
					StoreSelection.this.startActivity(intent);
					finish();
*/				}
			});
			
			/*Button but_SalesSummray = (Button) findViewById(R.id.btnSalesSummary);
			but_SalesSummray.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				
				// Intent intent = new Intent(StoreSelection.this, My_Summary.class);
				 Intent intent = new Intent(StoreSelection.this, DetailReport_Activity.class);
					intent.putExtra("imei", imei);
					intent.putExtra("userDate", userDate);
					intent.putExtra("pickerDate", pickerDate);
					StoreSelection.this.startActivity(intent);
					finish();
					
				}
			});
			
			Button but_day_end = (Button) findViewById(R.id.mainImg1);
			but_day_end.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closeList = 0;
					valDayEndOrChangeRoute=1;
					
					if(isOnline())
					{
						
					}
					 else
					 {
						 showNoConnAlert();
						 return;
		
					 }
					
					dbengine.open();
					whereTo = "11";
					////System.out.println("Abhinav store Selection  Step 1");
						////System.out.println("StoreList2Procs(before): " + StoreList2Procs.length);
					StoreList2Procs = dbengine.ProcessStoreReq();
					////System.out.println("StoreList2Procs(after): " + StoreList2Procs.length);

					if (StoreList2Procs.length != 0) {
						//whereTo = "22";
						////System.out.println("Abhinav store Selection  Step 2");
						midPart();

						showPendingStorelist();

					} else if (dbengine.GetLeftStoresChk() == true) 
					{
						////System.out.println("Abhinav store Selection  Step 7");
						//enableGPSifNot();
						// showChangeRouteConfirm();
						DayEnd();

					}

					else {
						DayEnd();
					}

					dbengine.close();

				}
			});*/
			
			
			Button btnStart = (Button) findViewById(R.id.startQues);
			btnStart.setOnClickListener(new OnClickListener()
			  {
                 @Override
				 public void onClick(View arg0)
                   {

					if (!selStoreID.isEmpty())
					  {
						
						long StartClickTime = System.currentTimeMillis();
						Date dateobj1 = new Date(StartClickTime);
						SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
						String StartClickTimeFinal = df1.format(dateobj1);
						
						CommonInfo.fileContent=imei+"_"+selStoreID+"_"+"Start Button Click on Store Selection "+StartClickTimeFinal;
						
						File dirORIGimg = new File(Environment.getExternalStorageDirectory(),DATASUBDIRECTORYForText);
						if (!dirORIGimg.exists()) 
						{
							dirORIGimg.mkdirs();
						}

						  if(Selected_manager_Id!=-99)
						  {
							  String allData=dbengine.fetchtblManagerMstr(""+Selected_manager_Id);

							  StringTokenizer token = new StringTokenizer(String.valueOf(allData), "^");
							  dbengine.open();
							  int chk=dbengine.counttblSelectedManagerDetails();
							  dbengine.close();
							  if(chk==1)
							  {
								  dbengine.deletetblSelectedManagerDetails();
								  dbengine.open();
								  dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
										  token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
										  ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),"NA");
								  dbengine.close();
							  }
							  else
							  {
								  dbengine.open();
								  dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
										  token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
										  ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),"NA");
								  dbengine.close();
							  }
						  }
						  else if(Selected_manager_Id==-99)
						  {

							  if(TextUtils.isEmpty(ed_Street.getText().toString().trim()))
							  {

								  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StoreSelection.this);
								  alertDialogBuilder.setTitle("Information");
								  alertDialogBuilder.setCancelable(false);
								  alertDialogBuilder.setIcon(R.drawable.info_ico);

								  // set dialog message
								  alertDialogBuilder
										  .setMessage("Please Enter Manager Name")
										  .setCancelable(false)
										  .setPositiveButton("Ok",new DialogInterface.OnClickListener()
										  {
											  public void onClick(DialogInterface dialog,int id)
											  {
												  dialog.cancel();
											  }
										  });


								  // create alert dialog
								  AlertDialog alertDialog = alertDialogBuilder.create();

								  alertDialog.show();
								  return;

							  }
							  else
							  {
								  String allData=dbengine.fetchtblManagerMstr(""+Selected_manager_Id);

								  StringTokenizer token = new StringTokenizer(String.valueOf(allData), "^");
								  dbengine.open();
								  int chk=dbengine.counttblSelectedManagerDetails();
								  dbengine.close();
								  if(chk==1)
								  {
									  dbengine.deletetblSelectedManagerDetails();
									  dbengine.open();
									  dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
											  token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
											  ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),ed_Street.getText().toString().trim());
									  dbengine.close();
								  }
								  else
								  {
									  dbengine.open();
									  dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
											  token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
											  ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),ed_Street.getText().toString().trim());
									  dbengine.close();
								  }
							  }

						  }

							whereTo = "11";
							
							syncTIMESTAMP = System.currentTimeMillis();
							Date dateobj = new Date(syncTIMESTAMP);
							SimpleDateFormat df = new SimpleDateFormat(
									"dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
							fullFileName1 = df.format(dateobj);
							
							dbengine.open();
							String checkClosrOrNext[] = dbengine.checkStoreCloseOrNextMethod(selStoreID);
					        dbengine.close();
		
							StringTokenizer tokensInvoice = new StringTokenizer(String.valueOf(checkClosrOrNext[0]), "_");
		
							int close = Integer.parseInt(tokensInvoice.nextToken().toString().trim());
							int next = Integer.parseInt(tokensInvoice.nextToken().toString().trim());

					
					if (close == 0 && next == 0) {

						if (!selStoreID.isEmpty()) 
						{
							
							/*if(isMyServiceRunning())
		            		{
							
		            		}
							else
							{
								startService(new Intent(StoreSelection.this,GPSTrackerService.class));
							}
							*/
							//startService(new Intent(StoreSelection.this,FusedTrackerService.class));
							 //System.out.println("Sun new Value 0");
							 int valSstatValueAgainstStore=0;
							 int ISNewStore=0;
							 int IsNewStoreDataCompleteSaved=0;
							 try
							 {
							//dbengine.open();
								 ISNewStore =dbengine.fncheckStoreIsNewOrOld(selStoreID);
								 IsNewStoreDataCompleteSaved =dbengine.fncheckStoreIsNewStoreDataCompleteSaved(selStoreID);
							valSstatValueAgainstStore=dbengine.fnGetStatValueagainstStore(selStoreID);
							 }catch(Exception e)
							 {
								 
							 }finally
							 {
							//dbengine.close();
							 }
						/*	String chID = ((dbengine
									.getChainIDBasedOnStoreID(selStoreID)) + "")
									.toString().trim();

							int pgFWDCLname2getID = dbengine
									.getFwdPgIdonNextBtnClick(selStoreID, "2", chID);
							FWDCLname = dbengine.getCustomPGid(pgFWDCLname2getID);

							int pgBCKCLname2getID = dbengine
									.getFwdPgIdonBackBtnClick(selStoreID, "2", chID);
							BCKCLname = dbengine.getCustomPGid(pgBCKCLname2getID);*/

							//System.out.println("PREV. LOC CHK sop: "+ dbengine.PrevLocChk(selStoreID.trim()));

							/*if ((dbengine.PrevLocChk(selStoreID.trim()))
									|| locStat == 1)
							{*/
							 	/*dbengine.open();
							 	System.out.println("DtateTimeNitish3");
						        dbengine.UpdateStoreStartVisit(selStoreID, fullFileName1);
						        String passdLevel = battLevel+"%";
								dbengine.UpdateStoreVisitBatt(selStoreID, passdLevel);
						        dbengine.close();*/
							 
							if(ISNewStore==0)
							{
								//Code If Starts Here
								/*if(valSstatValueAgainstStore==1)
								{
									//Intent nxtP4 = new Intent(StoreSelection.this,ProductList.class);
									//ProductOrderSearch
									Intent nxtP4 = new Intent(StoreSelection.this,ProductOrderFilterSearch.class);
									nxtP4.putExtra("storeID", selStoreID);
									nxtP4.putExtra("SN", selStoreName);
									nxtP4.putExtra("imei", imei);
									nxtP4.putExtra("userdate", userDate);
									nxtP4.putExtra("pickerDate", pickerDate);
									startActivity(nxtP4);
									finish();
								}
								else
								{*/
									
									long syncTIMESTAMP = System.currentTimeMillis();
									Date dateobjNew = new Date(syncTIMESTAMP);
									SimpleDateFormat dfnew = new SimpleDateFormat(
											"dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
									String startTS = dfnew.format(dateobjNew);
								 	dbengine.open();
									
									dbengine.UpdateStoreStartVisit(selStoreID,startTS);
									// dbengine.UpdateStoreEndVisit(selStoreID,
									// fullFileName1);

									//dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

									String passdLevel = battLevel + "%";
									dbengine.UpdateStoreVisitBatt(selStoreID,passdLevel);

									dbengine.UpdateStoreEndVisit(selStoreID,startTS);
									dbengine.close();
								    locStat = 0;



									Intent ready4GetLoc = new Intent(StoreSelection.this,LastVisitDetails.class);
									ready4GetLoc.putExtra("storeID", selStoreID);
									ready4GetLoc.putExtra("selStoreName", selStoreName);
									ready4GetLoc.putExtra("imei", imei);
									ready4GetLoc.putExtra("userDate", userDate);
									ready4GetLoc.putExtra("pickerDate", pickerDate);
									ready4GetLoc.putExtra("startTS", fullFileName1);
									ready4GetLoc.putExtra("bck", 0);
									startActivity(ready4GetLoc);
									finish();

							}
							else
							{
								//Code Else Starts Here
								
								if(IsNewStoreDataCompleteSaved==1)
								{
									// TODO Auto-generated method stub
									/*Intent intent = new Intent(StoreSelection.this, AddNewStore_DynamicSectionWise.class);
									intent.putExtra("storeID",selStoreID);
									intent.putExtra("activityFrom", "StoreSelection");
									intent.putExtra("userdate", userDate);
									intent.putExtra("pickerDate", pickerDate);
									intent.putExtra("imei", imei);
									intent.putExtra("rID", rID);
									StoreSelection.this.startActivity(intent);
									finish();*/
								}
								else if(IsNewStoreDataCompleteSaved==0)
								{
									/*if(valSstatValueAgainstStore==1)
									{
										//Intent nxtP4 = new Intent(StoreSelection.this,ProductList.class);
										//ProductOrderSearch
										Intent nxtP4 = new Intent(StoreSelection.this,ProductOrderFilterSearch.class);
										nxtP4.putExtra("storeID", selStoreID);
										nxtP4.putExtra("SN", selStoreName);
										nxtP4.putExtra("imei", imei);
										nxtP4.putExtra("userdate", userDate);
										nxtP4.putExtra("pickerDate", pickerDate);
										startActivity(nxtP4);
										finish();
									}
									else
									{*/
										
										long syncTIMESTAMP = System.currentTimeMillis();
										Date dateobjNew = new Date(syncTIMESTAMP);
										SimpleDateFormat dfnew = new SimpleDateFormat(
												"dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
										String startTS = dfnew.format(dateobjNew);
									 	dbengine.open();
										
										dbengine.UpdateStoreStartVisit(selStoreID,startTS);
										// dbengine.UpdateStoreEndVisit(selStoreID,
										// fullFileName1);

										//dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

										String passdLevel = battLevel + "%";
										dbengine.UpdateStoreVisitBatt(selStoreID,passdLevel);

										dbengine.UpdateStoreEndVisit(selStoreID,startTS);
										dbengine.close();
										Intent ready4GetLoc = new Intent(StoreSelection.this,LastVisitDetails.class);
										
										//enableGPSifNot();
										

										ready4GetLoc.putExtra("storeID", selStoreID);
										ready4GetLoc.putExtra("selStoreName", selStoreName);
										ready4GetLoc.putExtra("imei", imei);
										ready4GetLoc.putExtra("userDate", userDate);
										ready4GetLoc.putExtra("pickerDate", pickerDate);
										ready4GetLoc.putExtra("startTS", fullFileName1);
										ready4GetLoc.putExtra("bck", 0);

										locStat = 0;
										

										startActivity(ready4GetLoc);
										finish();
									//}
								}
								
								//Code Else Ends Here
							}
							
								

								

							

						}
						else 
						  {
							Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectStore, Toast.LENGTH_SHORT).show();
						  }
						// end else
					 }
					else
					   {
						Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectDiffrentStore,Toast.LENGTH_SHORT).show();
					   }
					}
					else
					  {
						Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectStore, Toast.LENGTH_SHORT).show();
					  }

				}
			});
			
			

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_selection);
		DayStartActivity.flgDaySartWorking=0;
		try
		{
			if(dbengine.isDBOpen())
			{
				dbengine.close();
			}

			tl2 = (TableLayout) findViewById(R.id.dynprodtable);
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
		Intent getStorei = getIntent();
		if(getStorei !=null)
		{
		imei = getStorei.getStringExtra("imei").trim();
        pickerDate = getStorei.getStringExtra("pickerDate").trim();
        userDate = getStorei.getStringExtra("userDate");
		//rID = getStorei.getStringExtra("rID");
		}

		locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
		
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		

		relativeLayout1=(RelativeLayout) findViewById(R.id.relativeLayout1);
		
		dbengine.open();
		rID=dbengine.GetActiveRouteID();
		dbengine.close();
		mProgressDialog = new ProgressDialog(StoreSelection.this);
		mProgressDialog.setTitle("Please Wait");
		mProgressDialog.setMessage("Refreshing Data...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = tManager.getDeviceId();
		
		if(CommonInfo.imei == null || CommonInfo.imei.trim().equals(""))
		{
			imei = tManager.getDeviceId();
			CommonInfo.imei=imei;
		}
		else
		{
			imei=CommonInfo.imei.trim();
		}
		try {
			getDSRDetail();
            //report alert
            getDistribtrList();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Date date1=new Date();
		sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
		passDate = sdf.format(date1).toString();
		
		//System.out.println("Selctd Date: "+ passDate);
		
		fDate = passDate.trim().toString();
		
		img_side_popUp=(ImageView) findViewById(R.id.img_side_popUp);
		img_side_popUp.setOnClickListener(new OnClickListener()
		  {
				@Override
				public void onClick(View v)
				{
					open_pop_up();
				}
		  });




		getManagersDetail();
		getRouteDetail();
		spinner_manager = (Spinner)findViewById(R.id.spinner_manager);
		ArrayAdapter adapterCategory=new ArrayAdapter(this, android.R.layout.simple_spinner_item,Manager_names);
		adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_manager.setAdapter(adapterCategory);

		spinner_RouteList = (Spinner)findViewById(R.id.spinner_RouteList);
		ArrayAdapter adapterRouteList=new ArrayAdapter(this, android.R.layout.simple_spinner_item,Route_names);
		adapterRouteList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_RouteList.setAdapter(adapterRouteList);

		spinner_RouteList.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				seleted_routeIDType=hmapRouteIdNameDetails.get(arg0.getItemAtPosition(arg2).toString());
				dbengine.open();
				dbengine.fnSetAllRouteActiveStatus();
				dbengine.updateActiveRoute(seleted_routeIDType.split(Pattern.quote("_"))[0], 1);
				dbengine.close();

				try {
					fnCreateStoreListOnLoad();
				}
				catch (Exception e)
				{

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		rl_for_other = (RelativeLayout) findViewById(R.id.rl_for_other);
		rl_for_other.setVisibility(RelativeLayout.GONE);

		ed_Street=(EditText)findViewById(R.id.streetid);

		spinner_manager.setOnItemSelectedListener(new OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3)
			{

				selected_manager=arg0.getItemAtPosition(arg2).toString();

				String ManagerID=hmapManagerNameManagerIdDetails.get(selected_manager);


				if(ManagerID.equals("0"))
				{
					rl_for_other.setVisibility(RelativeLayout.GONE);
					ed_Street.setText("");
					Selected_manager_Id=0;

				}
				else if(ManagerID.equals("-99"))
				{
					Selected_manager_Id=-99;
					rl_for_other.setVisibility(RelativeLayout.VISIBLE);
				}
				else
				{
					Selected_manager_Id=Integer.parseInt(ManagerID);

					ed_Street.setText("");

					rl_for_other.setVisibility(RelativeLayout.GONE);

				}



			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub


			}
		});


		dbengine.open();
		int chk=dbengine.counttblSelectedManagerDetails();
		dbengine.close();
		if(chk==1)
		{
			String abcd=dbengine.Fetch_tblSelectedManagerDetails();

			StringTokenizer tokens = new StringTokenizer(String.valueOf(abcd), "_");

			String as=tokens.nextToken().toString().trim();


			String ManagerName = tokens.nextToken().toString().trim();

			int ManagerID =  Integer.parseInt(as);

			int selected_choice_index=0;

			if(ManagerID==0)
			{
				spinner_manager.setSelection(0);

			}
			else if(ManagerID!=-99)
			{
				for(int i1=0;i1<Manager_names.length;i1++)
				{
					if(Manager_names[i1].equals(ManagerName))
					{
						selected_choice_index=i1;
					}
				}
				spinner_manager.setSelection(selected_choice_index);

			}

			else
			{
				for(int i1=0;i1<Manager_names.length;i1++)
				{
					if(Manager_names[i1].equals(ManagerName))
					{
						selected_choice_index=i1;
					}
				}
				spinner_manager.setSelection(selected_choice_index);

				dbengine.open();
				String OtherName=dbengine.fetchOtherNameBasicOfManagerID(ManagerID);
				dbengine.close();
				rl_for_other.setVisibility(RelativeLayout.VISIBLE);
				ed_Street.setText(OtherName);



			}
		}



			
		setUpVariable();


		String routeNametobeSelectedInSpinner=dbengine.GetActiveRouteDescr();
		int index=0;
		if(hmapRouteIdNameDetails!=null)
		{
			Set set2 = hmapRouteIdNameDetails.entrySet();
			Iterator iterator = set2.iterator();
			boolean isRouteSelected=false;
			while(iterator.hasNext())
			{
				Map.Entry me2 = (Map.Entry)iterator.next();
				if(routeNametobeSelectedInSpinner.equals(me2.getKey()))
				{
					isRouteSelected=true;
					break;
				}
				index=index+1;
			}
			if(isRouteSelected)
			{
				spinner_RouteList.setSelection(index);
			}
			else
			{
				spinner_RouteList.setSelection(0);
			}

		}
		/*if(hmapRouteIdNameDetails!=null)
		{
			Set set2 = hmapRouteIdNameDetails.entrySet();
			Iterator iterator = set2.iterator();

			while(iterator.hasNext())
			{
				Map.Entry me2 = (Map.Entry)iterator.next();
				if(routeNametobeSelectedInSpinner.equals(me2.getKey()))
				{
					break;
				}
				index=index+1;
			}
			spinner_RouteList.setSelection(index);
		}*/
	}
		catch(Exception e)
	{

	}

		try {
			setStoresList();
		}
		catch(Exception e)
		{
			System.out.println("error e :"+e);
		}



	}

	public void firstTimeLocationTrack()
	{
		if(pDialog2STANDBY!=null)
		{
			if(pDialog2STANDBY.isShowing())
			{


			}
			else
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
					locationRetrievingAndDistanceCalculating();
				}
			}
		}
		else
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
				locationRetrievingAndDistanceCalculating();
			}

		}
	}

public void fnCreateStoreListOnLoad() throws Exception
{



	for(int i = 0; i < tl2.getChildCount(); i++)
	{
		TableRow row = (TableRow) tl2.getChildAt(i);
		if(seleted_routeIDType.equals(row.getTag()))
		{
			row.setVisibility(View.VISIBLE);
		}
		else
		{
			row.setVisibility(View.GONE);
		}
	}

}

	public void setStoresList() throws Exception
	{

		dbengine.open();

		//System.out.println("Arjun has rID :"+rID);

		storeList = dbengine.FetchStoreList(rID);
        Prescheduled = dbengine.FetchPrescheduledStoreList(rID);
		storeRouteIdType=dbengine.FetchStoreRouteIdType(rID);
		storeStatus = dbengine.FetchStoreStatus();

		storeCloseStatus = dbengine.FetchStoreStoreCloseStatus();

		storeNextDayStatus = dbengine.FetchStoreStoreNextDayStatus();
		StoreflgSubmitFromQuotation= dbengine.FetchStoreStatusflgSubmitFromQuotation();
		hmapStoreLatLongDistanceFlgRemap=dbengine.fnGeStoreList(CommonInfo.DistanceRange);
		dbengine.close();

		storeCode = new String[storeList.length];
		storeName = new String[storeList.length];
       // Prescheduled= new String[storeList.length];

		for (int splitval = 0; splitval <= (storeList.length - 1); splitval++)
		{
			StringTokenizer tokens = new StringTokenizer(String.valueOf(storeList[splitval]), "_");

			storeCode[splitval] = tokens.nextToken().trim();
			storeName[splitval] = tokens.nextToken().trim();

		}


		float density = getResources().getDisplayMetrics().density;

		LayoutParams paramRB = new LayoutParams(LayoutParams.WRAP_CONTENT,(int) (10 * density));



		LayoutInflater inflater = getLayoutInflater();

		for (int current = 0; current < storeList.length; current++)
		{

			final TableRow row = (TableRow) inflater.inflate(R.layout.table_row1, tl2, false);

			final RadioButton rb1 = (RadioButton) row.findViewById(R.id.rg1StoreName);
			final CheckBox check1 = (CheckBox) row.findViewById(R.id.check1);

			final CheckBox check2 = (CheckBox) row.findViewById(R.id.check2);

			final ImageView but_Prescheduled = (ImageView) row.findViewById(R.id.but_Prescheduled);

         try {
			 if (Prescheduled[current].equals("0"))
			 {
				 but_Prescheduled.setVisibility(View.GONE);
			 } else {
				 but_Prescheduled.setVisibility(View.VISIBLE);
			 }
		 }catch (Exception e)
		 {
			 System.out.println("print error e :"+e);
		 }


			rb1.setTag(storeCode[current]);
			rb1.setText("  " + storeName[current]);
			rb1.setTextSize(14.0f);
			rb1.setChecked(false);

			check1.setTag(storeCode[current]);
			check1.setChecked(false);
			check1.setEnabled(false);

			check2.setTag(storeCode[current]);
			check2.setChecked(false);
			check2.setEnabled(false);
			row.setTag(storeRouteIdType[current]);

			if ((storeCloseStatus[current].equals("1")))
			{
				check1.setChecked(true);
			}

			if ((storeNextDayStatus[current].equals("1")))
			{
				check2.setChecked(true);
			}

			/*if ((((storeStatus[current].split(Pattern.quote("~"))[0]).equals("3")) || ((storeStatus[current].split(Pattern.quote("~"))[0]).equals("4"))) && (StoreflgSubmitFromQuotation[current]).equals("0"))
			{*/
			/*if ((((storeStatus[current].split(Pattern.quote("~"))[0]).equals("3")) || ((storeStatus[current].split(Pattern.quote("~"))[0]).equals("4"))) && (StoreflgSubmitFromQuotation[current]).equals("0"))
			{
				//StoreflgSubmitFromQuotation
				//rb1.setEnabled(false);
				rb1.setTypeface(null, Typeface.BOLD);
				rb1.setTextColor(this.getResources().getColor(R.color.green_submitted));
			}
			else
			{
			}*/

			if ((storeStatus[current].split(Pattern.quote("~"))[0]).equals("4"))
			{
				//StoreflgSubmitFromQuotation
				rb1.setEnabled(false);
				rb1.setTypeface(null, Typeface.BOLD);
				rb1.setTextColor(this.getResources().getColor(R.color.green_submitted));
			}
			if ((storeStatus[current].split(Pattern.quote("~"))[0]).equals("3")|| (storeStatus[current].split(Pattern.quote("~"))[0]).equals("5")|| (storeStatus[current].split(Pattern.quote("~"))[0]).equals("6"))
			{
				//StoreflgSubmitFromQuotation
				//rb1.setEnabled(false);
				rb1.setTypeface(null, Typeface.BOLD);
				rb1.setTextColor(this.getResources().getColor(R.color.static_text_color));
			}

			if (((storeStatus[current].split(Pattern.quote("~"))[0]).equals("1")))
			{
				if((storeStatus[current].split(Pattern.quote("~"))[1]).equals("1"))
				{
					rb1.setTypeface(null, Typeface.BOLD);
					//rb1.setTextColor(Color.BLUE);
					rb1.setTextColor(Color.RED);
				}
				else
				{
					rb1.setTypeface(null, Typeface.BOLD);
					rb1.setTextColor(Color.RED);
				}
			}



			rb1.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {

					for (int xc = 0; xc < storeList.length; xc++)
					{
						TableRow dataRow = (TableRow) tl2.getChildAt(xc);

						RadioButton child1;
						CheckBox child2;
						CheckBox child3;

						child1 = (RadioButton)dataRow.findViewById(R.id.rg1StoreName);
						child2 = (CheckBox)dataRow.findViewById(R.id.check1);
						child3 = (CheckBox)dataRow.findViewById(R.id.check2);


						child1.setChecked(false);
						child2.setEnabled(false);
						child3.setEnabled(false);

					}

					check1.setEnabled(true);
					check2.setEnabled(true);

					selStoreID = arg0.getTag().toString();

					dbengine.open();
					selStoreName=dbengine.FetchStoreName(selStoreID);
					dbengine.close();

					RadioButton child2get12 = (RadioButton) arg0;
					child2get12.setChecked(true);
					check1.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							// TODO Auto-generated method stub
							int checkStatus = 0;
							CheckBox child2get = (CheckBox) v;
							String Sid = v.getTag().toString().trim();
							boolean ch = false;
							ch = child2get.isChecked();
							if ((ch == true))
							{
								// checkStatus=1;
								//System.out.println("1st checked  with Store ID :"+ Sid);
								long syncTIMESTAMP = System.currentTimeMillis();
								Date dateobj = new Date(syncTIMESTAMP);
								SimpleDateFormat df = new SimpleDateFormat(
										"dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
								String startTS = df.format(dateobj);

								Date currDate = new Date();
								SimpleDateFormat currDateFormat = new SimpleDateFormat(
										"dd-MMM-yyyy",Locale.ENGLISH);
								String currSysDate = currDateFormat.format(
										currDate).toString();

								if (!currSysDate.equals(pickerDate)) {
									fullFileName1 = pickerDate + " 12:00:00";
								}
								dbengine.open();
								dbengine.updateCloseflg(Sid, 1);
								System.out.println("DateTimeNitish 1");
								dbengine.UpdateStoreStartVisit(selStoreID,startTS);
								// dbengine.UpdateStoreEndVisit(selStoreID,
								// fullFileName1);

								//dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

								String passdLevel = battLevel + "%";
								dbengine.UpdateStoreVisitBatt(selStoreID,passdLevel);

								dbengine.UpdateStoreEndVisit(selStoreID,startTS);
								dbengine.close();

							} else {
								//System.out.println("1st unchecked with Store ID :"+ Sid);
								dbengine.open();
								dbengine.updateCloseflg(Sid, 0);
								//dbengine.delStoreCloseNextData(selStoreID);

								//dbengineUpdateCloseNextStoreData(Sid);

								/*dbengine.UpdateStoreStartVisit(selStoreID,"");
								dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "", "" + "", "" + "","" + "");
								dbengine.UpdateStoreVisitBatt(selStoreID,"");
								dbengine.UpdateStoreEndVisit(selStoreID,"");*/

								dbengine.close();
							}

						}
					});

					check2.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							// TODO Auto-generated method stub
							int checkStatus = 0;
							CheckBox child2get = (CheckBox) v;
							boolean ch = false;
							ch = child2get.isChecked();
							String Sid = v.getTag().toString().trim();
							if ((ch == true)) {
								// checkStatus=1;
								//System.out.println("2nd checked with Store ID :"+ Sid);
								long syncTIMESTAMP = System.currentTimeMillis();
								Date dateobj = new Date(syncTIMESTAMP);
								SimpleDateFormat df = new SimpleDateFormat(
										"dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
								String startTS = df.format(dateobj);

								Date currDate = new Date();
								SimpleDateFormat currDateFormat = new SimpleDateFormat(
										"dd-MMM-yyyy",Locale.ENGLISH);
								String currSysDate = currDateFormat.format(
										currDate).toString();

								if (!currSysDate.equals(pickerDate)) {
									fullFileName1 = pickerDate + " 12:00:00";
								}
								dbengine.open();
								System.out.println("DateTimeNitish2");
								dbengine.updateNextDayflg(Sid, 1);

								dbengine.UpdateStoreStartVisit(selStoreID,
										startTS);
								// dbengine.UpdateStoreEndVisit(selStoreID,
								// fullFileName1);

								//dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

								String passdLevel = battLevel + "%";
								dbengine.UpdateStoreVisitBatt(selStoreID,
										passdLevel);

								dbengine.UpdateStoreEndVisit(selStoreID,
										startTS);

								dbengine.close();

							} else {
								System.out
										.println("2nd unchecked with Store ID :"
												+ Sid);
								dbengine.open();
								dbengine.updateNextDayflg(Sid, 0);
								//dbengine.delStoreCloseNextData(selStoreID);

								//dbengine.UpdateCloseNextStoreData(Sid);

								/*dbengine.UpdateStoreStartVisit(selStoreID,"");
								dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "", "" + "", "" + "","" + "");
								dbengine.UpdateStoreVisitBatt(selStoreID,"");
								dbengine.UpdateStoreEndVisit(selStoreID,"");*/

								dbengine.close();
							}

						}
					});

				}
			});


			tl2.addView(row);

		}
		try {
			fnCreateStoreListOnLoad();
		}
		catch (Exception e)
		{

		}
	}
	
	private void showDialogButtonClick() {
		
		dbengine.open();
		route_name = dbengine.fnGetRouteInfoForDDL();
		route_name_id = dbengine.fnGetRouteIdsForDDL();
		dbengine.close();
        
	       
        ContextThemeWrapper cw= new ContextThemeWrapper(this,R.style.AlertDialogTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(cw);
           builder.setTitle(R.string.genTermAvailableRoute);
            
           //final CharSequence[] choiceList ={"MR-Monday", "MR-Tuesday" ,"MR-Wednesday" ,"MR-Thursday","MR-Friday","MR-Saturday","MR-Sunday" };
        
           builder.setSingleChoiceItems(
           		route_name, 
                   selected, 
                   new DialogInterface.OnClickListener() {
                
               @Override
               public void onClick(
                       DialogInterface dialog, 
                       int which) {
                   selected = which;
                   temp_select_routename=route_name[selected];
                   temp_select_routeid=route_name_id[selected];
                   rID=temp_select_routeid;
                   
               }
           })
           .setCancelable(false)
           .setPositiveButton(R.string.txtOk, 
               new DialogInterface.OnClickListener() 
               {
                   @Override
                   public void onClick(DialogInterface dialog, 
                           int which) {
                       //not the same as 'which' above
                      // Log.d(TAG,"Which value="+which);
                      // Log.d(TAG,"Selected value="+selected);
                      // Toast.makeText(LauncherActivity.this,"Select "+route_name[selected],Toast.LENGTH_SHORT).show();
                   	dialog.dismiss();
                   //	tv_Route.setText(""+temp_select_routename);
                       selected_route_id=temp_select_routeid;
                       rID=selected_route_id;
                       
                       
                       dbengine.open();
                       int checkRouteIDExistOrNot=dbengine.checkRouteIDExistInStoreListTable(Integer.parseInt(rID));
                       dbengine.close();
                       
                       if(checkRouteIDExistOrNot==0)
                       {
                    	    GetDataForChangeRoute task = new GetDataForChangeRoute(StoreSelection.this);
						    task.execute();
                       }
                       else
                       {
                    	  
                       }
                       
                      
                   }
               });
            
           AlertDialog alert = builder.create();
           alert.show();
       }



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(dbengine.isDBOpen())
		{
			dbengine.close();
		}

		/*try
		{

		dbengine.open();
		String Noti_textWithMsgServerID=dbengine.fetchNoti_textFromtblNotificationMstr();
		dbengine.close();
		System.out.println("Sunil Tty Noti_textWithMsgServerID :"+Noti_textWithMsgServerID);
		if(!Noti_textWithMsgServerID.equals("Null"))
		{
		StringTokenizer token = new StringTokenizer(String.valueOf(Noti_textWithMsgServerID), "_");
		
		MsgServerID= Integer.parseInt(token.nextToken().trim());
		Noti_text= token.nextToken().trim();
		
		
		if(Noti_text.equals("") || Noti_text.equals("Null"))
		{
			
		}
		else
		{
			
			

			 final AlertDialog builder = new AlertDialog.Builder(StoreSelection.this).create();
		       

				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        View openDialog = inflater.inflate(R.layout.custom_dialog, null);
		        openDialog.setBackgroundColor(Color.parseColor("#ffffff"));
		        
		        builder.setCancelable(false);
		     	TextView header_text=(TextView)openDialog. findViewById(R.id.txt_header);
		     	final TextView msg=(TextView)openDialog. findViewById(R.id.msg);
		     	
				final Button ok_but=(Button)openDialog. findViewById(R.id.but_yes);
				final Button cancel=(Button)openDialog. findViewById(R.id.but_no);
				
				cancel.setVisibility(View.GONE);
			    header_text.setText("Alert");
			     msg.setText(Noti_text);
			     	
			     	ok_but.setText("OK");
			     	
					builder.setView(openDialog,0,0,0,0);

			        ok_but.setOnClickListener(new OnClickListener() 
			        {
						
						@Override
						public void onClick(View arg0) 
						{
							// TODO Auto-generated method stub

							long syncTIMESTAMP = System.currentTimeMillis();
							Date dateobj = new Date(syncTIMESTAMP);
							SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
							String Noti_ReadDateTime = df.format(dateobj);
				    	
							dbengine.open();
							dbengine.updatetblNotificationMstr(MsgServerID,Noti_text,0,Noti_ReadDateTime,3);
							dbengine.close();
							
							try
							{
								dbengine.open();
							    int checkleftNoti=dbengine.countNumberOFNotificationtblNotificationMstr();
							    if(checkleftNoti>0)
							    {
								    	String Noti_textWithMsgServerID=dbengine.fetchNoti_textFromtblNotificationMstr();
										System.out.println("Sunil Tty Noti_textWithMsgServerID :"+Noti_textWithMsgServerID);
										if(!Noti_textWithMsgServerID.equals("Null"))
										{
											StringTokenizer token = new StringTokenizer(String.valueOf(Noti_textWithMsgServerID), "_");
											
											MsgServerID= Integer.parseInt(token.nextToken().trim());
											Noti_text= token.nextToken().trim();
											
											dbengine.close();
											if(Noti_text.equals("") || Noti_text.equals("Null"))
											{
												
											}
											else
											{
												  msg.setText(Noti_text);
											}
										}
							    	
							    }
								else
								{
									builder.dismiss();
								}
							
							}
							catch(Exception e)
							{
								
							}
				            finally
				            {
				            	dbengine.close();
							   
				            }
			            
						
						}
					});
			        
			       
			      
			 
			     	builder.show();
				
				
				

		
			 
		}
		}
		}
		catch(Exception e)
		{
			
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


		dbengine.open();
		String allLoctionDetails=  dbengine.getLocationDetails();
		dbengine.close();
		if(allLoctionDetails.equals("0"))
		{
			firstTimeLocationTrack();
		}
*/

		
	}

	 protected void open_pop_up() 
	 {
	        dialog = new Dialog(StoreSelection.this);
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.selection_header_custom);
	        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	        dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
	        WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
	        parms.gravity = Gravity.TOP | Gravity.LEFT;
	        parms.height=parms.MATCH_PARENT;
	        parms.dimAmount = (float) 0.5;

		 final Button btnManageDSR = (Button) dialog.findViewById(R.id.btnManageDSR);
		 btnManageDSR.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 Intent intent=new Intent(StoreSelection.this,WebViewManageDSRActivity.class);
				 startActivity(intent);
				 // finish();
			 }
		 });


		 //account census
		 final Button butn_Census_report = (Button) dialog.findViewById(R.id.butn_Census_report);

		 butn_Census_report.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 Intent intent=new Intent(StoreSelection.this,AddedOutletSummaryReportActivity.class);
				// intent.putExtra("imei",imei);
				// intent.putExtra("userDate", userDate);
				// intent.putExtra("pickerDate", pickerDate);
				 startActivity(intent);
				 //finish();
			 }
		 });

		 final   Button butn_Cost_Breakup = (Button) dialog.findViewById(R.id.butn_Cost_Breakup);
		 butn_Cost_Breakup.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 Intent intent=new Intent(StoreSelection.this,WebViewQuatationOnlineActivity.class);
				 startActivity(intent);

			 }
		 });


		 final   Button btnOutstandingtask = (Button) dialog.findViewById(R.id.btnOutstandingtask);
		 btnOutstandingtask.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 Intent intent=new Intent(StoreSelection.this,ViewOutStandingTask.class);
				 startActivity(intent);
				// finish();
			 }
		 });
		 final   Button btnVisitPlan = (Button) dialog.findViewById(R.id.btnVisitPlan);
		 btnVisitPlan.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 Intent intent=new Intent(StoreSelection.this,ViewVisitPlan.class);
				 startActivity(intent);
				 //finish();
			 }
		 });
		 final   Button btnAppointment = (Button) dialog.findViewById(R.id.btnAppointment);
		 btnAppointment.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 Intent intent=new Intent(StoreSelection.this,ViewAppointment.class);
				 startActivity(intent);
				// finish();
			 }
		 });

	        final   Button butn_refresh_data = (Button) dialog.findViewById(R.id.butn_refresh_data);
	        final  Button but_day_end = (Button) dialog.findViewById(R.id.mainImg1);
	        final  Button changeRoute = (Button) dialog.findViewById(R.id.changeRoute);
	        final  Button btnewAddedStore = (Button) dialog.findViewById(R.id.btnewAddedStore);

		 final   Button butHome = (Button) dialog.findViewById(R.id.butHome);
		 butHome.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 Intent intent=new Intent(StoreSelection.this,AllButtonActivity.class);
				 startActivity(intent);
				 finish();
			 }
		 });

		 final Button btnExecution = (Button) dialog.findViewById(R.id.btnExecution);
		 btnExecution.setOnClickListener(new OnClickListener()
		 {
			 @Override
			 public void onClick(View view)
			 {
				 GetInvoiceForDay task = new GetInvoiceForDay(StoreSelection.this);
				 task.execute();
			 }
		 });

		 final   Button butn_Change_dsr = (Button) dialog.findViewById(R.id.butn_Change_dsr);
		  butn_Change_dsr.setOnClickListener(new OnClickListener()
		 {
			 @Override
			 public void onClick(View view)
			 {
				/* dbengine.deletetblStoreList();
                 Intent intent = new Intent(StoreSelection.this, AllButtonActivity.class);
				 startActivity(intent);
				 finish();*/
				 dialog.dismiss();
				 openMarketVisitAlert();
			 }
		 });
		 int FlgDSRSO=CommonInfo.FlgDSRSO;
		 if(FlgDSRSO==1)
		 {
			 butn_Change_dsr.setVisibility(View.GONE);
		 }
		 final   Button butMarketVisit = (Button) dialog.findViewById(R.id.butMarketVisit);
		 butMarketVisit.setVisibility(View.GONE);

		 final   Button btnDistributorMap = (Button) dialog.findViewById(R.id.btnDistributorMap);
		 btnDistributorMap.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 Intent intent=new Intent(StoreSelection.this,DistributorMapActivity.class);
				 startActivity(intent);

			 }
		 });

		 final   Button btnDistributorStock = (Button) dialog.findViewById(R.id.btnDistributorStock);
		 btnDistributorStock.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				 int CstmrNodeId=0,CstomrNodeType= 0;
				 //changes
				 if(imei==null)
				 {
					 imei=CommonInfo.imei;
				 }
				 if(fDate==null)
				 {
					 Date date1 = new Date();
					 SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
					 fDate = sdf.format(date1).toString().trim();
				 }
				 Intent i=new Intent(StoreSelection.this,DistributorEntryActivity.class);
				 i.putExtra("imei", imei);
				 i.putExtra("CstmrNodeId", CstmrNodeId);
				 i.putExtra("CstomrNodeType", CstomrNodeType);
				 i.putExtra("fDate", fDate);
				 startActivity(i);
				// finish();
			 }
		 });




		 final   Button butn_store_validation = (Button) dialog.findViewById(R.id.butn_store_validation);
		 butn_store_validation.setOnClickListener(new OnClickListener()
		 {
			 @Override
			 public void onClick(View view)
			 {

				 int checkDataNotSync = dbengine.CheckCounttblQuestionMstr();
				 if (checkDataNotSync == 1)
				 {
					 Intent i=new Intent(StoreSelection.this,StorelistActivity.class);
					 startActivity(i);
					 finish();
				 }
				 else
				 {
					 GetStoreAllData getStoreAllDataAsync= new GetStoreAllData(StoreSelection.this);
					 getStoreAllDataAsync.execute();
				 }


			 }
		 });






	        
	        final Button but_SalesSummray = (Button) dialog.findViewById(R.id.btnSalesSummary);
			but_SalesSummray.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					

				
				 but_SalesSummray.setBackgroundColor(Color.GREEN);
				 dialog.dismiss();
					Intent i=new Intent(StoreSelection.this,DetailReportSummaryActivityForAll.class);

					startActivity(i);
					//finish();

					//openReportAlert();
					
				}
			});



         final Button btnCheckTodayOrder = (Button) dialog.findViewById(R.id.btnCheckTodayOrder);
         btnCheckTodayOrder.setVisibility(View.VISIBLE);
         btnCheckTodayOrder.setOnClickListener(new OnClickListener()
         {

             @Override
             public void onClick(View v) {
                 // TODO Auto-generated method stub



                 but_SalesSummray.setBackgroundColor(Color.GREEN);
                 dialog.dismiss();
                 Intent intent = new Intent(StoreSelection.this, CheckDatabaseData.class);
                 // Intent intent = new Intent(StoreSelection.this, DetailReport_Activity.class);
                 intent.putExtra("imei", imei);
                 intent.putExtra("userDate", userDate);
                 intent.putExtra("pickerDate", pickerDate);
                 intent.putExtra("rID", rID);
                 intent.putExtra("back", "0");
                 startActivity(intent);
                 finish();

             }
         });




         but_day_end.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					but_day_end.setBackgroundColor(Color.GREEN);
					closeList = 0;
					valDayEndOrChangeRoute=1;
					//checkbuttonclick=2;
				
					if(isOnline())
					{
						
					}
					 else
					 {
						 showNoConnAlert();
						 return;
		
					 }
					dbengine.open();
					whereTo = "11";
					//////System.out.println("Abhinav store Selection  Step 1");
						//////System.out.println("StoreList2Procs(before): " + StoreList2Procs.length);
					StoreList2Procs = dbengine.ProcessStoreReq();
					//////System.out.println("StoreList2Procs(after): " + StoreList2Procs.length);

					if (StoreList2Procs.length != 0) {
						//whereTo = "22";
						//////System.out.println("Abhinav store Selection  Step 2");
						midPart();
						dayEndCustomAlert(1);
						//showPendingStorelist(1);
						dbengine.close();

					} else if (dbengine.GetLeftStoresChk() == true) 
					{
						//////System.out.println("Abhinav store Selection  Step 7");
						//enableGPSifNot();
						// showChangeRouteConfirm();
						DayEnd();
						dbengine.close();
					}

					else {
						DayEndWithoutalert();
					}

					
					
					dialog.dismiss();

				}
			});


			changeRoute.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					changeRoute.setBackgroundColor(Color.GREEN);
					valDayEndOrChangeRoute=2;
					
					if(isOnline())
					{}
					 else
					 {
						 showNoConnAlert();
						 return;
		
					 }
					closeList = 0;
					whereTo = "11";
					//checkbuttonclick=1;

					// ////System.out.println("closeList: "+closeList);
					// chk if flag 2/3 found
					dbengine.open();
					 StoreList2Procs = dbengine.ProcessStoreReq();

					// int picsCHK = dbengine.getExistingPicNosOnRemStore();
					// String[] sIDs2Alert =
					// dbengine.getStoreNameExistingPicNosOnRemStore();

					if (StoreList2Procs.length != 0) {// && picsCHK <= 0

						
						midPart();
						dayEndCustomAlert(2);
						//showPendingStorelist(2);

					} else if (dbengine.GetLeftStoresChk() == true) {// && picsCHK
																		// <= 0
					
						//enableGPSifNot();

						
						showChangeRouteConfirmWhenNoStoreisLeftToSubmit();
						//showChangeRouteConfirm();

						}
					
					else {
						// show dialog for clear..clear + tranx to launcher
						//showChangeRouteConfirmWhenNoStoreisLeftToSubmit();
						DayEndWithoutalert();
						//showChangeRouteConfirm();
					}

					dbengine.close();
					dialog.dismiss();
				}
			});
			
			

			 butn_refresh_data.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					butn_refresh_data.setBackgroundColor(Color.GREEN);

					if(isOnline())
					{
					
					AlertDialog.Builder alertDialogBuilderNEw11 = new AlertDialog.Builder(StoreSelection.this);
					
						// set title
					alertDialogBuilderNEw11.setTitle("Information");
			 
						// set dialog message
					alertDialogBuilderNEw11.setMessage("Are you sure to refresh Data?");
					alertDialogBuilderNEw11.setCancelable(false);
					alertDialogBuilderNEw11.setPositiveButton("Yes",new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialogintrfc,int id) {
									// if this button is clicked, close
									// current activity
									dialogintrfc.cancel();
									try
			    				    {
										CommonInfo.flgAllRoutesData=dbengine.getflgAllRoutesDataPullFromServer();
			    						new GetStoresForDay(StoreSelection.this).execute();

			    					} 
									catch(Exception e)
									{
										
									}
									
									//onCreate(new Bundle());
								}
							  });
						
					alertDialogBuilderNEw11.setNegativeButton(R.string.txtNo,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialogintrfc, int which) {
											// //System.out.println("value ofwhatTask after no button pressed by sunil"+whatTask);

											dialogintrfc.dismiss();
										}
									});

					alertDialogBuilderNEw11.setIcon(R.drawable.info_ico);
					AlertDialog alert121 = alertDialogBuilderNEw11.create();
					alert121.show();
				}
					else
				 {
					 showNoConnAlert();
					 return;

				 }
							
				  dialog.dismiss();
					
				}
				
			});

		 final Button btnVersion = (Button) dialog.findViewById(R.id.btnVersion);
		 btnVersion.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 // TODO Auto-generated method stub



				 btnVersion.setBackgroundColor(Color.GREEN);
				 dialog.dismiss();
			 }
		 });

		 dbengine.open();
		 String ApplicationVersion=dbengine.AppVersionID;
		 dbengine.close();
		 btnVersion.setText("Version No-V"+ApplicationVersion);

		 // Version No-V12


		 final Button btnDSRTrack = (Button) dialog.findViewById(R.id.btnDSRTrack);
		 btnDSRTrack.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View view)
			 {
				/* Intent intent=new Intent(StoreSelection.this,WebViewDSRTrackerActivity.class);
				 startActivity(intent);*/
				 openDSRTrackerAlert();
			 }
		 });

	     dialog.setCanceledOnTouchOutside(true);
	        dialog.show();
	    }
	 
	 public void dayEndCustomAlert(int flagWhichButtonClicked)
	 {
		 final Dialog dialog = new Dialog(StoreSelection.this,R.style.AlertDialogDayEndTheme);

		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.day_end_custom_alert);
			if(flagWhichButtonClicked==1)
			{
				dialog.setTitle(R.string.genTermSelectStoresPendingToCompleteDayEnd);
			}
			else if(flagWhichButtonClicked==2)
			{
				dialog.setTitle(R.string.genTermSelectStoresPendingToCompleteChangeRoute);
			}
			
		
			
				LinearLayout ll_product_not_submitted=(LinearLayout) dialog.findViewById(R.id.ll_product_not_submitted);
				mSelectedItems.clear();
				final String[] stNames4List = new String[stNames.size()];
				 checks=new boolean[stNames.size()];
				stNames.toArray(stNames4List);
				
				for(int cntPendingList=0;cntPendingList<stNames4List.length;cntPendingList++)
				{
					LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View viewAlertProduct=inflater.inflate(R.layout.day_end_alrt,null);
					final TextView txtVw_product_name=(TextView) viewAlertProduct.findViewById(R.id.txtVw_product_name);
					txtVw_product_name.setText(stNames4List[cntPendingList]);
					txtVw_product_name.setTextColor(this.getResources().getColor(R.color.green_submitted));
					final ImageView img_to_be_submit=(ImageView) viewAlertProduct.findViewById(R.id.img_to_be_submit);
					img_to_be_submit.setTag(cntPendingList);
					
					final ImageView img_to_be_cancel=(ImageView) viewAlertProduct.findViewById(R.id.img_to_be_cancel);
					img_to_be_cancel.setTag(cntPendingList);
					img_to_be_submit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							
							if(!checks[Integer.valueOf(img_to_be_submit.getTag().toString())])
							{
								img_to_be_submit.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_hover) );
								img_to_be_cancel.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel_normal) );
								txtVw_product_name.setTextColor(getResources().getColor(R.color.green_submitted));
								mSelectedItems.add(stNames4List[Integer.valueOf(img_to_be_submit.getTag().toString())]);
								checks[Integer.valueOf(img_to_be_submit.getTag().toString())]=true;
							}
						
							
						}
					});
					
					img_to_be_cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (mSelectedItems.contains(stNames4List[Integer.valueOf(img_to_be_cancel.getTag().toString())]))
							{
								if(checks[Integer.valueOf(img_to_be_cancel.getTag().toString())])
								{
									
									img_to_be_submit.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_normal) );
									img_to_be_cancel.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel_hover) );
									txtVw_product_name.setTextColor(Color.RED);
									mSelectedItems.remove(stNames4List[Integer.valueOf(img_to_be_cancel.getTag().toString())]);
									checks[Integer.valueOf(img_to_be_cancel.getTag().toString())]=false;
								}
							
							}
						
						}
					});
					mSelectedItems.add(stNames4List[cntPendingList]);
					 checks[cntPendingList]=true;
					 ll_product_not_submitted.addView(viewAlertProduct);
				}
				
				
				Button btnSubmit=(Button) dialog.findViewById(R.id.btnSubmit);
				Button btn_cancel_Back=(Button) dialog.findViewById(R.id.btn_cancel_Back);
				btnSubmit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (mSelectedItems.size() == 0) {
							
							DayEnd();
						
							
						}

						else {

							int countOfOrderNonSelected=0;
							for(int countForFalse=0;countForFalse<checks.length;countForFalse++)
							{
								if(checks[countForFalse]==false)
								{
									countOfOrderNonSelected++;
								}
								
							}
							if(countOfOrderNonSelected>0)
							{
								confirmationForSubmission(String.valueOf(countOfOrderNonSelected));
							}
							
							else
							{

								
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
				
				btn_cancel_Back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						valDayEndOrChangeRoute=0;
						dialog.dismiss();
					}
				});
			
				dialog.setCanceledOnTouchOutside(false);
				
				
				dialog.show();
				
				
				
			
	 }
	 
	 
	 public void confirmationForSubmission(String number)
	 {
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(StoreSelection.this);
		 
	        // Setting Dialog Title
	        alertDialog.setTitle("Confirm Cancel..");
	 
	        // Setting Dialog Message
	        if(1<Integer.valueOf(number))
	        {
	        	  alertDialog.setMessage("Are you sure you want "+ number +" orders are to be cancelled ?");
	        }
	        else
	        {
	        	alertDialog.setMessage("Are you sure you want "+ number +" order to be cancelled ?");
	        }
	      
	 
	        // Setting Icon to Dialog
	        alertDialog.setIcon(R.drawable.cancel_hover);
	 
	        // Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	 

					
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
	 
	 
	 @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	/*	if(storeList.length>0)
		{
			
		}
		
		else
		{
			new GetStoresForDay().execute();
		}*/
	}
	 
	 public void showAlertException(String title,String msg)
	 {
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(StoreSelection.this);
		 
	        // Setting Dialog Title
	        alertDialog.setTitle(title);
	 
	        // Setting Dialog Message
	        alertDialog.setMessage(msg);
	        alertDialog.setIcon(R.drawable.error);
	        alertDialog.setCancelable(false);
	        // Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	 
	            	new GetStoresForDay(StoreSelection.this).execute();
	            	dialog.dismiss();
	            }
	        });
	 
	        // Setting Negative "NO" Button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            // Write your code here to invoke NO event
	            	dialog.dismiss();
					finish();
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
	 }
	public class CoundownClass extends CountDownTimer {

		public CoundownClass(long startTime, long interval) {
			super(startTime, interval);
			// TODO Auto-generated constructor stub
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

				Location nwLocation=appLocationService.getLocation(locationManager,LocationManager.GPS_PROVIDER,location);

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

			Location gpsLocation=appLocationService.getLocation(locationManager,LocationManager.NETWORK_PROVIDER,location);
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
				System.out.println("LOCATION(N/W)  LATTITUDE: " +lattitude1 + "LONGITUDE:" + longitude1+ "accuracy:" + accuracy1);

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
			//




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
			// fnAccurateProvider="";
			if(fnAccurateProvider.equals(""))
			{
				//because no location found so updating table with NA
				dbengine.open();
				dbengine.deleteLocationTable();
				dbengine.saveTblLocationDetails("NA", "NA", "NA","NA","NA","NA","NA","NA", "NA", "NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA");
				dbengine.close();
				if(pDialog2STANDBY.isShowing())
				{
					pDialog2STANDBY.dismiss();
				}



				int flagtoShowStorelistOrAddnewStore=dbengine.fncheckCountNearByStoreExistsOrNot(CommonInfo.DistanceRange);


				if(flagtoShowStorelistOrAddnewStore==1)
				{
					//getDataFromDatabaseToHashmap();
					//tl2.removeAllViews();

					if(tl2.getChildCount()>0){
						tl2.removeAllViews();
						// dynamcDtaContnrScrollview.removeAllViews();
						//addViewIntoTable();
						try {
							setStoresList();
						}
						catch(Exception e)
						{

						}
					}
					else
					{
						//addViewIntoTable();
						try {
							setStoresList();
						}
						catch(Exception e)
						{

						}
					}
					if(pDialog2STANDBY!=null)
					{
						if (pDialog2STANDBY.isShowing())
						{
							pDialog2STANDBY.dismiss();
						}
					}

                       /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
                        LauncherActivity.this.startActivity(intent);
                        finish();*/

				}
				else
				{
					if(pDialog2STANDBY!=null) {
						if (pDialog2STANDBY.isShowing()) {
							pDialog2STANDBY.dismiss();
						}
					}
				}
				//send direct to dynamic page-------------------------
               /* Intent intent=new Intent(StorelistActivity.this,AddNewStore_DynamicSectionWise.class);
                intent.putExtra("FLAG_NEW_UPDATE","NEW");
                StorelistActivity.this.startActivity(intent);
                finish();*/


				//commenting below error message
				// showAlertForEveryOne("Please try again, No Fused,GPS or Network found.");
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

				if(!GpsLat.equals("0") )
				{
					fnCreateLastKnownGPSLoction(GpsLat,GpsLong,GpsAccuracy);
				}
				//now Passing intent to other activity
				String addr="NA";
				String zipcode="NA";
				String city="NA";
				String state="NA";


				if(!FullAddress.equals("NA"))
				{
					addr = FullAddress.split(Pattern.quote("^"))[0];
					zipcode = FullAddress.split(Pattern.quote("^"))[1];
					city = FullAddress.split(Pattern.quote("^"))[2];
					state = FullAddress.split(Pattern.quote("^"))[3];
				}

				if(fnAccuracy>10000)
				{
					dbengine.open();
					dbengine.deleteLocationTable();
					dbengine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state,fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,FusedLong,FusedAccuracy,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,FusedLocationLatitudeWithFirstAttempt,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
					dbengine.close();
					if(pDialog2STANDBY.isShowing())
					{
						pDialog2STANDBY.dismiss();
					}

					//send to addstore Dynamic page direct-----------------------------
                   /* Intent intent=new Intent(LauncherActivity.this,AddNewStore_DynamicSectionWise.class);
                    intent.putExtra("FLAG_NEW_UPDATE","NEW");
                    LauncherActivity.this.startActivity(intent);
                    finish();*/


					//From, addr,zipcode,city,state,errorMessageFlag,username,totaltarget,todayTarget


				}
				else
				{
					dbengine.open();
					dbengine.deleteLocationTable();
					dbengine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state,fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,FusedLong,FusedAccuracy,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,FusedLocationLatitudeWithFirstAttempt,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
					dbengine.close();


					hmapOutletListForNear=dbengine.fnGetALLOutletMstr();
					System.out.println("SHIVAM"+hmapOutletListForNear);
					if(hmapOutletListForNear!=null)
					{

						for(Map.Entry<String, String> entry:hmapOutletListForNear.entrySet())
						{
							int DistanceBWPoint=1000;
							String outID=entry.getKey().toString().trim();
							//  String PrevAccuracy = entry.getValue().split(Pattern.quote("^"))[0];
							String PrevLatitude = entry.getValue().split(Pattern.quote("^"))[0];
							String PrevLongitude = entry.getValue().split(Pattern.quote("^"))[1];

							// if (!PrevAccuracy.equals("0"))
							// {
							if (!PrevLatitude.equals("0"))
							{
								try
								{
									Location locationA = new Location("point A");
									locationA.setLatitude(Double.parseDouble(fnLati));
									locationA.setLongitude(Double.parseDouble(fnLongi));

									Location locationB = new Location("point B");
									locationB.setLatitude(Double.parseDouble(PrevLatitude));
									locationB.setLongitude(Double.parseDouble(PrevLongitude));

									float distance = locationA.distanceTo(locationB) ;
									DistanceBWPoint=(int)distance;

									hmapOutletListForNearUpdated.put(outID, ""+DistanceBWPoint);
								}
								catch(Exception e)
								{

								}
							}
							// }
						}
					}

					if(hmapOutletListForNearUpdated!=null)
					{
						dbengine.open();
						for(Map.Entry<String, String> entry:hmapOutletListForNearUpdated.entrySet())
						{
							String outID=entry.getKey().toString().trim();
							String DistanceNear = entry.getValue().trim();
							if(outID.equals("853399-a1445e87daf4-NA"))
							{
								System.out.println("Shvam Distance = "+DistanceNear);
							}
							if(!DistanceNear.equals(""))
							{
								//853399-81752acdc662-NA
								if(outID.equals("853399-a1445e87daf4-NA"))
								{
									System.out.println("Shvam Distance = "+DistanceNear);
								}
								dbengine.UpdateStoreDistanceNear(outID,Integer.parseInt(DistanceNear));
							}
						}
						dbengine.close();
					}
					//send to storeListpage page
					//From, addr,zipcode,city,state,errorMessageFlag,username,totaltarget,todayTarget
					int flagtoShowStorelistOrAddnewStore=      dbengine.fncheckCountNearByStoreExistsOrNot(CommonInfo.DistanceRange);


					if(flagtoShowStorelistOrAddnewStore==1)
					{
						//getDataFromDatabaseToHashmap();
						if(tl2.getChildCount()>0){
							tl2.removeAllViews();
							// dynamcDtaContnrScrollview.removeAllViews();
							//addViewIntoTable();
							try {
								setStoresList();
							}
							catch(Exception e)
							{

							}
						}
						else
						{
							//addViewIntoTable();
							try {
								setStoresList();
							}
							catch(Exception e)
							{

							}
						}

                       /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
                        LauncherActivity.this.startActivity(intent);
                        finish();*/

					}
					else
					{
						//send to AddnewStore directly
                       /* Intent intent=new Intent(LauncherActivity.this,AddNewStore_DynamicSectionWise.class);
                        intent.putExtra("FLAG_NEW_UPDATE","NEW");
                        LauncherActivity.this.startActivity(intent);
                        finish();*/


						if(tl2.getChildCount()>0){
							tl2.removeAllViews();
							// dynamcDtaContnrScrollview.removeAllViews();
							//addViewIntoTable();
							try {
								setStoresList();
							}
							catch(Exception e)
							{

							}
						}
						else
						{
							try {
								setStoresList();
							}
							catch(Exception e)
							{

							}
						}

					}


				}
				if(pDialog2STANDBY.isShowing())
				{
					pDialog2STANDBY.dismiss();
				}
               /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
               *//* intent.putExtra("FROM","SPLASH");
                intent.putExtra("errorMessageFlag",errorMessageFlag); // 0 if no error, if error, then error message passes
                intent.putExtra("username",username);//if error then it will 0
                intent.putExtra("totaltarget",totaltarget);////if error then it will 0
                intent.putExtra("todayTarget",todayTarget);//if error then it will 0*//*
                LauncherActivity.this.startActivity(intent);
                finish();
*/
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


			//AddStoreBtn.setEnabled(true);

		}

		@Override
		public void onTick(long arg0) {
			// TODO Auto-generated method stub

		}}




	public String getAddressForDynamic(String latti,String longi){


		String areaToMerge="NA";
		Address address=null;
		String addr="NA";
		String zipcode="NA";
		String city="NA";
		String state="NA";
		String fullAddress="";
		StringBuilder FULLADDRESS3 =new StringBuilder();
		try {
			Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
			List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);
			if (addresses != null && addresses.size() > 0){
				if(addresses.get(0).getAddressLine(1)!=null){
					addr=addresses.get(0).getAddressLine(1);
				}

				if(addresses.get(0).getLocality()!=null){
					city=addresses.get(0).getLocality();
				}

				if(addresses.get(0).getAdminArea()!=null){
					state=addresses.get(0).getAdminArea();
				}


				for(int i=0 ;i<addresses.size();i++){
					address = addresses.get(i);
					if(address.getPostalCode()!=null){
						zipcode=address.getPostalCode();
						break;
					}
				}

				if(addresses.get(0).getAddressLine(0)!=null && addr.equals("NA")){
					String countryname="NA";
					if(addresses.get(0).getCountryName()!=null){
						countryname=addresses.get(0).getCountryName();
					}

					addr=  getAddressNewWay(addresses.get(0).getAddressLine(0),city,state,zipcode,countryname);
				}

			}
			else{FULLADDRESS3.append("NA");}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			return fullAddress=addr+"^"+zipcode+"^"+city+"^"+state;
		}
	}
	public void fnCreateLastKnownGPSLoction(String chekLastGPSLat,String chekLastGPSLong,String chekLastGpsAccuracy)
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

			File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(),CommonInfo.AppLatLngJsonFile);
			if (!jsonTxtFolder.exists())
			{
				jsonTxtFolder.mkdirs();

			}
			String txtFileNamenew="GPSLastLocation.txt";
			File file = new File(jsonTxtFolder,txtFileNamenew);
			String fpath = Environment.getExternalStorageDirectory()+"/"+CommonInfo.AppLatLngJsonFile+"/"+txtFileNamenew;


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

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{

		}
	}
	private void getManagersDetail()
	{

		hmapManagerNameManagerIdDetails=dbengine.fetch_Manager_List();

		int index=0;
		if(hmapManagerNameManagerIdDetails!=null)
		{
			Manager_names=new String[hmapManagerNameManagerIdDetails.size()];
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapManagerNameManagerIdDetails);
			Set set2 = map.entrySet();
			Iterator iterator = set2.iterator();
			while(iterator.hasNext())
			{
				Map.Entry me2 = (Map.Entry)iterator.next();
				Manager_names[index]=me2.getKey().toString();
				index=index+1;
			}
		}


	}


	private void getRouteDetail()
	{

		hmapRouteIdNameDetails=dbengine.fetch_Route_List();

		int index=0;
		if(hmapRouteIdNameDetails!=null)
		{
			Route_names=new String[hmapRouteIdNameDetails.size()];
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapRouteIdNameDetails);
			Set set2 = map.entrySet();
			Iterator iterator = set2.iterator();
			while(iterator.hasNext())
			{
				Map.Entry me2 = (Map.Entry)iterator.next();
				Route_names[index]=me2.getKey().toString();
				index=index+1;
			}
		}


	}

	public String getAddressOfProviders(String latti, String longi){

		StringBuilder FULLADDRESS2 =new StringBuilder();
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(StoreSelection.this, Locale.ENGLISH);



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
	/*public String getAddressOfProviders(String latti, String longi){

		StringBuilder FULLADDRESS2 =new StringBuilder();
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(this, Locale.getDefault());



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

	void openReportAlert()
	{
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(StoreSelection.this);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.report_visit_alert, null);
		alert.setView(view);

		alert.setCancelable(false);

		final RadioButton rb_myReport= (RadioButton) view.findViewById(R.id.rb_myReport);
		final RadioButton rb_dsrReport= (RadioButton) view.findViewById(R.id.rb_dsrReport);
		final RadioButton rb_WholeReport= (RadioButton) view.findViewById(R.id.rb_WholeReport);
		final Spinner spinner_dsrVisit= (Spinner) view.findViewById(R.id.spinner_dsrVisit);

        final RadioButton rb_distrbtrScope= (RadioButton) view.findViewById(R.id.rb_distrbtrScope);
        final Spinner spinner_distrbtrScope= (Spinner) view.findViewById(R.id.spinner_distrbtrScope);

        Button btn_proceed= (Button) view.findViewById(R.id.btn_proceed);
		Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

		final android.support.v7.app.AlertDialog dialog=alert.create();
		dialog.show();

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_proceed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				if(rb_myReport.isChecked())
				{
					String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

					int tempSalesmanNodeId=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
					int tempSalesmanNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);
					shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);

					flgDataScopeSharedPref(1);
                   /* CommonInfo.SalesmanNodeId=0;
                    CommonInfo.SalesmanNodeType=0;
                    CommonInfo.flgDataScope=1;*/
					Intent i=new Intent(StoreSelection.this,DetailReportSummaryActivityForAll.class);
					startActivity(i);
					finish();
				}
				else if(rb_WholeReport.isChecked())
				{
                    /*String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    CommonInfo.PersonNodeID=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    CommonInfo.PersonNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);*/

					shardPrefForSalesman(0,0);
					flgDataScopeSharedPref(3);

					Intent i=new Intent(StoreSelection.this,DetailReportSummaryActivityForAll.class);
					startActivity(i);
					finish();
				}
				else if(rb_dsrReport.isChecked())
				{
					if(!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSR") && !SelectedDSRValue.equals("No DSR") )
					{

						String DSRNodeIdAndNodeType= dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
						int slctdsrReportSalesmanNodeId =Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
						int slctdsrReportSalesmanNodeType=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
						shardPrefForSalesman(slctdsrReportSalesmanNodeId,slctdsrReportSalesmanNodeType);

						flgDataScopeSharedPref(2);
						Intent i = new Intent(StoreSelection.this, DetailReportSummaryActivityForAll.class);
						startActivity(i);
						finish();
					}
					else
					{
						showAlertForEveryOne("Please select DSR to Proceeds.");
					}
				}
                else if(rb_distrbtrScope.isChecked())
                {
                    if(!SelectedDistrbtrName.equals("") && !SelectedDistrbtrName.equals("Select Distributor") && !SelectedDistrbtrName.equals("No Distributor") )
                    {
                        String DbrNodeIdAndNodeType= hmapDistrbtrList.get(SelectedDistrbtrName);
                        int tempSalesmanNodeId=Integer.parseInt(DbrNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempSalesmanNodeType=Integer.parseInt(DbrNodeIdAndNodeType.split(Pattern.quote("^"))[1]);

                        shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);

                        flgDataScopeSharedPref(4);
                        Intent i = new Intent(StoreSelection.this, DetailReportSummaryActivityForAll.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        showAlertForEveryOne("Please select Distributor to Proceed.");
                    }
                }
				else
				{
					showAlertForEveryOne("Please select atleast one option to Proceeds.");
				}
			}
		});

		rb_myReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(rb_myReport.isChecked())
				{
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
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(rb_WholeReport.isChecked())
				{
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
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(rb_dsrReport.isChecked())
				{
					rb_myReport.setChecked(false);
					rb_WholeReport.setChecked(false);
                    rb_distrbtrScope.setChecked(false);
                    spinner_distrbtrScope.setVisibility(View.GONE);

					ArrayAdapter adapterCategory=new ArrayAdapter(StoreSelection.this, android.R.layout.simple_spinner_item,drsNames);
					adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_dsrVisit.setAdapter(adapterCategory);
					spinner_dsrVisit.setVisibility(View.VISIBLE);

					spinner_dsrVisit.setOnItemSelectedListener(new OnItemSelectedListener()
					{

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
						{
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
						public void onNothingSelected(AdapterView<?> arg0)
						{
							// TODO Auto-generated method stub

						}
					});

				}
			}
		});

        rb_distrbtrScope.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_distrbtrScope.isChecked())
                {
                    rb_myReport.setChecked(false);
                    rb_WholeReport.setChecked(false);
                    rb_dsrReport.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);

                    ArrayAdapter adapterCategory=new ArrayAdapter(StoreSelection.this, android.R.layout.simple_spinner_item,DbrArray);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_distrbtrScope.setAdapter(adapterCategory);
                    spinner_distrbtrScope.setVisibility(View.VISIBLE);

                    spinner_distrbtrScope.setOnItemSelectedListener(new OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                        {
                            SelectedDistrbtrName = spinner_distrbtrScope.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0)
                        {
                        }
                    });
                }
            }
        });

		dialog.show();
	}

	public void showAlertForEveryOne(String msg)
	{
		//AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(new ContextThemeWrapper(LauncherActivity.this, R.style.Dialog));
		android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(StoreSelection.this);

		alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
		alertDialogNoConn.setMessage(msg);
		alertDialogNoConn.setCancelable(false);
		alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				//finish();
			}
		});
		alertDialogNoConn.setIcon(R.drawable.info_ico);
		android.support.v7.app.AlertDialog alert = alertDialogNoConn.create();
		alert.show();
	}

	void openMarketVisitAlert()
	{
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(StoreSelection.this);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.market_visit_alert, null);
		alert.setView(view);

		alert.setCancelable(false);

		TextView txt_header=(TextView)view.findViewById(R.id.txt_HdrVst);
		txt_header.setText("Change DSM");
		final RadioButton rb_myVisit= (RadioButton) view.findViewById(R.id.rb_myVisit);
		rb_myVisit.setVisibility(View.GONE);
		final RadioButton rb_dsrVisit= (RadioButton) view.findViewById(R.id.rb_dsrVisit);

		final RadioButton rb_jointWorking= (RadioButton) view.findViewById(R.id.rb_jointWorking);
		rb_jointWorking.setVisibility(View.GONE);
		final Spinner spinner_dsrVisit= (Spinner) view.findViewById(R.id.spinner_dsrVisit);
		final Spinner spinner_jointWorking= (Spinner) view.findViewById(R.id.spinner_jointWorking);
		Button btn_proceed= (Button) view.findViewById(R.id.btn_proceed);
		Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

		final android.support.v7.app.AlertDialog dialog=alert.create();
		dialog.show();

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_proceed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();



					if(!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSR") && !SelectedDSRValue.equals("No DSR") )
					{

						String DSRNodeIdAndNodeType= dbengine.fnGetDSRNodeIdAndNodeType(SelectedDSRValue);
						 slctdCoverageAreaNodeID=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
						  slctdCoverageAreaNodeType=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);

						CommonInfo.CoverageAreaNodeID=slctdCoverageAreaNodeID;
						CommonInfo.CoverageAreaNodeType=slctdCoverageAreaNodeType;
						CommonInfo.FlgDSRSO=2;

						String DSRPersonNodeIdAndNodeType= dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
						slctdDSrSalesmanNodeId=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
						 slctdDSrSalesmanNodeType=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[1]);

						dbengine.open();

						rID= dbengine.GetActiveRouteIDCrntDSR(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType);
						dbengine.close();


						if(rID.equals("0"))
						{

						}

						if(dbengine.isDataAlreadyExist(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType))
						{
                            shardPrefForCoverageArea(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType);

                            shardPrefForSalesman(slctdDSrSalesmanNodeId,slctdDSrSalesmanNodeType);

                            flgDataScopeSharedPref(2);
                            flgDSRSOSharedPref(2);
							Intent intent=new Intent(StoreSelection.this,StoreSelection.class);
                            intent.putExtra("imei", imei);
                            intent.putExtra("userDate", userDate);
                            intent.putExtra("pickerDate", fDate);
                            intent.putExtra("rID", rID);
                            startActivity(intent);
                            finish();
						}
						else
						{
							if(dbengine.isDBOpen())
							{
								dbengine.close();
							}


							new GetStoresForDay(StoreSelection.this).execute();
						}



					}
					else
					{
						showAlertForEveryOne("Please select DSR to Proceeds.");
					}

			}
		});



		rb_dsrVisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(rb_dsrVisit.isChecked())
				{
					rb_myVisit.setChecked(false);
					rb_jointWorking.setChecked(false);
					ArrayAdapter adapterCategory=new ArrayAdapter(StoreSelection.this, android.R.layout.simple_spinner_item,drsNames);
					adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_dsrVisit.setAdapter(adapterCategory);
					spinner_dsrVisit.setVisibility(View.VISIBLE);

					spinner_dsrVisit.setOnItemSelectedListener(new OnItemSelectedListener()
					{

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
						{
							// TODO Auto-generated method stub
							SelectedDSRValue = spinner_dsrVisit.getSelectedItem().toString();

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0)
						{
							// TODO Auto-generated method stub

						}
					});

				}
			}
		});

		rb_dsrVisit.setChecked(true);
		dialog.show();
	}

	private class GetStoresForDay extends AsyncTask<Void, Void, Void>
	{


		public GetStoresForDay(StoreSelection activity)
		{
			pDialogGetStores = new ProgressDialog(activity);
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();


			dbengine.open();
			String getPDADate=dbengine.fnGetPdaDate();
			String getServerDate=dbengine.fnGetServerDate();



			dbengine.close();

			//System.out.println("Checking  Oncreate Date PDA GetStoresForDay:"+getPDADate);
			//System.out.println("Checking  Oncreate Date Server GetStoresForDay :"+getServerDate);

			if(!getPDADate.equals(""))  // || !getPDADate.equals("NA") || !getPDADate.equals("Null") || !getPDADate.equals("NULL")
			{
				if(!getServerDate.equals(getPDADate))
				{

					/*showAlertBox("Your Phone  Date is not Up to date.Please Correct the Date.");
					dbengine.open();
					dbengine.maintainPDADate();
					dbengine.reCreateDB();
					dbengine.close();
					return;*/
				}
			}





			dbengine.open();
			dbengine.fnSetAllRouteActiveStatus();

			//rID="17^18^19";

			StringTokenizer st = new StringTokenizer(rID, "^");

			while (st.hasMoreElements())
			{
				//System.out.println("Anand StringTokenizer Output: ");
				dbengine.updateActiveRoute(st.nextElement().toString(), 1);
			}




			long syncTIMESTAMP = System.currentTimeMillis();
			Date dateobj = new Date(syncTIMESTAMP);
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
			String startTS = df.format(dateobj);

			int DayEndFlg=0;
			int ChangeRouteFlg=0;

			int DatabaseVersion=dbengine.DATABASE_VERSION;
			String AppVersionID=dbengine.AppVersionID;
			dbengine.insertTblDayStartEndDetails(imei,startTS,rID,DayEndFlg,ChangeRouteFlg,fDate,AppVersionID);//DatabaseVersion;//getVersionNumber
			dbengine.close();


			pDialogGetStores.setTitle(getText(R.string.PleaseWaitMsg));
			pDialogGetStores.setMessage(getText(R.string.RetrivingDataMsg));
			pDialogGetStores.setIndeterminate(false);
			pDialogGetStores.setCancelable(false);
			pDialogGetStores.setCanceledOnTouchOutside(false);
			pDialogGetStores.show();
			if(dbengine.isDBOpen())
			{
				dbengine.close();
			}

		}

		@Override
		protected Void doInBackground(Void... args)
		{
			ServiceWorker newservice = new ServiceWorker();

			try
			{
				for(int mm = 1; mm < 41  ; mm++)
				{
					System.out.println("bywww "+mm);
					if(mm==1)
					{


						dbengine.open();
						hmapStoreIdSstat=dbengine.checkForStoreIdSstat();

						dbengine.close();

						newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=1)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==2)
					{

						newservice = newservice.getallProduct(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=2)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==3)
					{

						newservice = newservice.getCategory(getApplicationContext(), imei);
						if(newservice.flagExecutedServiceSuccesfully!=3)
						{
							serviceException=true;
							break;
						}

					}
					if(mm==4)
					{

						Date currDateNew = new Date();
						SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

						String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
						newservice = newservice.getAllNewSchemeStructure(getApplicationContext(), currSysDateNew, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=4)
						{
							serviceException=true;
							break;
						}

					}
					if(mm==5)
					{

						Date currDateNew = new Date();
						SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

						String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
						newservice = newservice.getallPDASchAppListForSecondPage(getApplicationContext(), currSysDateNew, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=5)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==6)
					{
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
					if(mm==7)
					{



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
					if(mm==8)
					{
						newservice = newservice.getfnGetStoreWiseTarget(getApplicationContext(), fDate, imei, rID);
					}
					if(mm==9)
					{

					}
					if(mm==10)
					{

					}
					if(mm==11)
					{

					}
					if(mm==12)
					{

					}
					if(mm==13)
					{

					}
					if(mm==14)
					{

					}
					if(mm==15)
					{

					}
					if(mm==16)
					{

					}
					if(mm==17)
					{

					}
					if(mm==18)
					{

					}
					if(mm==19)
					{

					}
					if(mm==20)
					{

					}
					if(mm==21)
					{
						newservice = newservice.GetPDAIsSchemeApplicable(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=21)
						{
							serviceException=true;
							break;
						}

					}

					if(mm==22)
					{
						/*newservice = newservice.getallPDAtblSyncSummuryDetails(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=22)
						{
							serviceException=true;
							break;
						}
						*/
					}
					if(mm==23)
					{
						//newservice = newservice.getallPDAtblSyncSummuryForProductDetails(getApplicationContext(), fDate, imei, rID);
					}
					if(mm==24)
					{
					/*newservice = newservice.GetSchemeCoupon(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=24)
					{
						serviceException="GetSchemeCoupon";
						break;
					}*/
					}
					if(mm==25)
					{
				/*	newservice = newservice.GetSchemeCouponSlab(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=25)
					{
						serviceException="GetSchemeCouponSlab";
						break;
					}*/
					}
					if(mm==26)
					{
				/*	newservice = newservice.GetDaySummaryNew(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=26)
					{
						serviceException="GetDaySummaryNew";
						break;
					}*/
					}
					if(mm==27)
					{/*
					newservice = newservice.GetOrderDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=27)
					{
						serviceException="GetOrderDetailsOnLastSalesSummary";
						break;
					}
					*/}
					if(mm==28)
					{
				/*	newservice = newservice.GetVisitDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=28)
					{
						serviceException="GetVisitDetailsOnLastSalesSummary";
						break;
					}*/
					}
					if(mm==29)
					{
						newservice = newservice.GetLODDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=29)
						{
							serviceException=true;
							break;
						}
					}

					if(mm==31)
					{
						newservice = newservice.GetCallspForPDAGetLastVisitDate(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=31)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==32)
					{
						newservice = newservice.GetCallspForPDAGetLastOrderDate(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=32)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==33)
					{
						newservice = newservice.GetCallspForPDAGetLastVisitDetails(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=33)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==34)
					{
						newservice = newservice.GetCallspForPDAGetLastOrderDetails(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=34)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==35)
					{
						newservice = newservice.GetCallspForPDAGetLastOrderDetails_TotalValues(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=35)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==36)
					{
						newservice = newservice.GetCallspForPDAGetExecutionSummary(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=36)
						{
							serviceException=true;
							break;
						}
					}

					if(mm==37)
					{
						newservice = newservice.getQuotationDataFromServer(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=37)
						{
							serviceException=true;
							break;
						}
					}

					if(mm == 38)
					{
						newservice = newservice.getFSDOutletFeedbackData(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=38)
						{
							serviceException=true;
							break;
						}

					}
                    if(mm == 39)
                    {
                        newservice = newservice.fnGetAccountLastSummary(getApplicationContext(), fDate, imei, rID);

                    }
					if(mm == 40)
					{
						newservice = newservice.fnGetFeedbckCompetitorData(getApplicationContext(), fDate, imei, rID);

					}

				}


			}
			catch (Exception e)
			{
				Log.i("SvcMgr", "Service Execution Failed!", e);
			}
			finally
			{
				Log.i("SvcMgr", "Service Execution Completed...");
			}
			return null;
		}


		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);

			Log.i("SvcMgr", "Service Execution cycle completed");

			try
			{
				if(pDialogGetStores.isShowing())
				{
					pDialogGetStores.dismiss();
				}
			}
			catch(Exception e)
			{

			}
			if(serviceException)
			{
				try
				{
					//but_GetStore.setEnabled(true);
					showAlertException("Error.....","Error while Retrieving Data!\n Please Retry");
				}
				catch(Exception e)
				{

				}
				dbengine.open();
				serviceException=false;
				dbengine.maintainPDADate();
				dbengine.dropRoutesTBL();
				dbengine.reCreateDB();

				dbengine.close();
			}
			else
			{
				//dbengine.close();
				shardPrefForCoverageArea(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType);

				shardPrefForSalesman(slctdDSrSalesmanNodeId,slctdDSrSalesmanNodeType);

				flgDataScopeSharedPref(2);
				flgDSRSOSharedPref(2);
				//onCreate(new Bundle());

				if(dbengine.isDBOpen())
				{
					dbengine.close();
				}


				Intent storeIntent = new Intent(StoreSelection.this, LauncherActivity.class);
				storeIntent.putExtra("imei", imei);
				storeIntent.putExtra("userDate", userDate);
				storeIntent.putExtra("pickerDate", fDate);
				storeIntent.putExtra("rID", rID);


				startActivity(storeIntent);
				finish();

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

	private class GetInvoiceForDay extends AsyncTask<Void, Void, Void>
	{
		ServiceWorker newservice = new ServiceWorker();
		String rID="0";
		int chkFlgForErrorToCloseApp=0;

		ProgressDialog pDialogGetInvoiceForDay;

		public GetInvoiceForDay(StoreSelection activity)
		{
			pDialogGetInvoiceForDay = new ProgressDialog(activity);
		}


		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();


			pDialogGetInvoiceForDay.setTitle(getText(R.string.PleaseWaitMsg));
			pDialogGetInvoiceForDay.setMessage(getText(R.string.RetrivingDataMsg));
			pDialogGetInvoiceForDay.setIndeterminate(false);
			pDialogGetInvoiceForDay.setCancelable(false);
			pDialogGetInvoiceForDay.setCanceledOnTouchOutside(false);
			pDialogGetInvoiceForDay.show();


		}

		@Override
		protected Void doInBackground(Void... params)
		{

			try {

				HashMap<String,String> hmapInvoiceOrderIDandStatus=new HashMap<String, String>();
				hmapInvoiceOrderIDandStatus=dbengine.fetchHmapInvoiceOrderIDandStatus();

				for(int mm = 1; mm < 5  ; mm++)
				{
					if(mm==1)
					{
						newservice = newservice.callInvoiceButtonStoreMstr(getApplicationContext(), fDate, imei, rID,hmapInvoiceOrderIDandStatus);

						if(!newservice.director.toString().trim().equals("1"))
						{
							if(chkFlgForErrorToCloseApp==0)
							{
								chkFlgForErrorToCloseApp=1;
							}

						}

					}
					if(mm==2)
					{
						newservice = newservice.callInvoiceButtonProductMstr(getApplicationContext(), fDate, imei, rID);

						if(!newservice.director.toString().trim().equals("1"))
						{
							if(chkFlgForErrorToCloseApp==0)
							{
								chkFlgForErrorToCloseApp=1;
							}

						}

					}
					if(mm==3)
					{
						newservice = newservice.callInvoiceButtonStoreProductwiseOrder(getApplicationContext(), fDate, imei, rID,hmapInvoiceOrderIDandStatus);
					}
					if(mm==4)
					{
						dbengine.open();
						int check1=dbengine.counttblCatagoryMstr();
						dbengine.close();
						if(check1==0)
						{
							newservice = newservice.getCategory(getApplicationContext(), imei);
						}
					}



				}


			} catch (Exception e)
			{
				Log.i("SvcMgr", "Service Execution Failed!", e);
			}

			finally
			{
				Log.i("SvcMgr", "Service Execution Completed...");
			}

			return null;
		}

		@Override
		protected void onCancelled()
		{
			Log.i("SvcMgr", "Service Execution Cancelled");
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);


			if(pDialogGetInvoiceForDay.isShowing())
			{
				pDialogGetInvoiceForDay.dismiss();
			}

			Date currDate = new Date();
			SimpleDateFormat currDateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

			String currSysDate = currDateFormat.format(currDate).toString();

			Intent storeIntent = new Intent(StoreSelection.this, InvoiceStoreSelection.class);
			storeIntent.putExtra("imei", imei);
			storeIntent.putExtra("userDate", currSysDate);
			storeIntent.putExtra("pickerDate", fDate);


			if(chkFlgForErrorToCloseApp==0)
			{
				chkFlgForErrorToCloseApp=0;
				startActivity(storeIntent);
				finish();
			}
			else
			{
				android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(StoreSelection.this);
				alertDialogNoConn.setTitle("Information");
				alertDialogNoConn.setMessage("There is no Invoice Pending");
				alertDialogNoConn.setCancelable(false);
				alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								// but_Invoice.setEnabled(true);
								chkFlgForErrorToCloseApp=0;
							}
						});
				alertDialogNoConn.setIcon(R.drawable.info_ico);
				android.support.v7.app.AlertDialog alert = alertDialogNoConn.create();
				alert.show();
				return;

			}
		}
	}

	void openDSRTrackerAlert()
	{
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(StoreSelection.this);
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

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_proceed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				if(rb_dataReport.isChecked())
				{
					Intent i=new Intent(StoreSelection.this,WebViewDSRDataReportActivity.class);
					startActivity(i);

				}
				else if(rb_onMap.isChecked())
				{
					Intent i = new Intent(StoreSelection.this, WebViewDSRTrackerActivity.class);
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

	private class GetStoreAllData extends AsyncTask<Void, Void, Void> {

		public GetStoreAllData(StoreSelection activity)
		{
			pDialogGetStores = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute()
		{
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
				dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);
				int DatabaseVersion = dbengine.DATABASE_VERSION;
				int ApplicationID = dbengine.Application_TypeID;
				//newservice = newservice.getAvailableAndUpdatedVersionOfApp(getApplicationContext(), imei,fDate,DatabaseVersion,ApplicationID);

				dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);
				for(int mm = 1; mm<5; mm++)
				{
					if(mm==1)
					{
						newservice = newservice.getSOSummary(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
						if (!newservice.director.toString().trim().equals("1")) {
							if (chkFlgForErrorToCloseApp == 0) {
								chkFlgForErrorToCloseApp = 1;
								break;
							}

						}
					}
					if(mm==2)
					{
						newservice = newservice.getStoreAllDetails(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
						if (!newservice.director.toString().trim().equals("1")) {
							if (chkFlgForErrorToCloseApp == 0) {
								chkFlgForErrorToCloseApp = 1;
								break;
							}

						}


					}
					if(mm==3)
					{
						newservice = newservice.callfnSingleCallAllWebServiceSO(getApplicationContext(),ApplicationID,imei);
						if (!newservice.director.toString().trim().equals("1")) {
							if (chkFlgForErrorToCloseApp == 0) {
								chkFlgForErrorToCloseApp = 1;
								break;
							}

						}

					}
					if(mm==4)
					{
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
			try
			{
				if(pDialogGetStores.isShowing())
				{
					pDialogGetStores.dismiss();
				}
			}
			catch(Exception e)
			{

			}

			if (chkFlgForErrorToCloseApp == 1)   // if Webservice showing exception or not excute complete properly
			{
				chkFlgForErrorToCloseApp = 0;
				SharedPreferences sharedPreferences=getSharedPreferences("MyPref", MODE_PRIVATE);
				SharedPreferences.Editor ed;
				if(sharedPreferences.contains("ServerDate"))
				{
					ed = sharedPreferences.edit();
					ed.putString("ServerDate", "0");
					ed.commit();
				}
				intentPassToLauncherActivity("Internet connection is slow ,please try again.");//, "0", "0", "0"
			}
			else
			{
				dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(0);
				Intent i=new Intent(StoreSelection.this,StorelistActivity.class);
				startActivity(i);
				finish();

			}
		}

	}

	public void intentPassToLauncherActivity(String errorMessageFlag)
	{
		if(errorMessageFlag.equals("0"))
		{
			Intent intent =new Intent(StoreSelection.this,StorelistActivity.class);
			StoreSelection.this.startActivity(intent);
			finish();
		}
		else {
			Intent intent =new Intent(StoreSelection.this,StorelistActivity.class);
			StoreSelection.this.startActivity(intent);
			finish();

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

}
