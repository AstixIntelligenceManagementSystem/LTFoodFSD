package project.astix.com.ltfoodfsd;




import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import project.astix.com.ltfoodfsd.ListAdapter.customButtonListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.astix.Common.CommonInfo;

public class InvoiceStoreSelection extends BaseActivity implements OnItemSelectedListener,customButtonListener
{
	SharedPreferences sharedPref;
	LinkedHashMap<String, String> hmapdsrIdAndDescr_details=new LinkedHashMap<String, String>();
	String[] drsNames;
	public String	SelectedDSRValue="";
	Dialog dialog;
	public EditText et_Reason;
	View alertLayout;
	public String imei;
	public String currSysDate;
	public String pickerDate; 
	public String[] distributor_name;	
	public  String[] distributor_name_id;
	public  String selected_distributor_name_id="0";
	public String[] route_name;	
	public  String[] route_name_id;
	public  String selected_route_name_id="0";
	public String[] StoreID;
	public String[] StoreIDOrderID;
	public String[] StoreSstat;
	public String[] OrderID;
	public String[] StorenameList;
	
	ProgressDialog mProgressDialog;
	public boolean[] checksCancelInvoices;
	
	public boolean[] checksConformInvoices;
	
	int spinnerRouteValue;
	 int spinnerDistributrValue;
	 ArrayAdapter<String> adapterRoute;
	 ArrayAdapter<String> adapterDisrt;
	
	 Spinner Spinner_Distributor;
	 Spinner Spinner_Route;
	 public HashMap<String, String> hmap=new HashMap<String, String>();
	 public HashMap<String, String> hmapStoreIdName=new HashMap<String, String>();
	 public HashMap<String, String> hmapStoreIdAndInvoiceOtherDetails=new HashMap<String, String>();
	public HashMap<String, String> hmapStoreIdAndInvoiceReturnRemaks=new HashMap<String, String>();
	 public HashMap<String, String> hmapConfirmCancel=new HashMap<String, String>();
	 public long syncTIMESTAMP;
		public String fullFileName1;
	 
	 String[] StorenameOther;
	 int closeList = 0;
		int whatTask = 0;
		String whereTo = "11";
		
	 public String[] StoreList2Procs;
	 
	 public String[] StoreConformInvoiceOrder;
	 
	 
	 ArrayList<String> stIDs;
		ArrayList<String> stNames;
		ArrayList mSelectedItems = new ArrayList();
		
		ArrayList mSelectedItemsConfornInvoiceOrders = new ArrayList();

	String strReason="";
		String[] branch_name;
		public EditText editText;
		public int inTest=0;
		ListAdapter adapter;  
		InvoiceDatabaseAssistant DA = new InvoiceDatabaseAssistant(this); 
	
	 DBAdapterKenya dbengine = new DBAdapterKenya(this);
	 String[] StoreName;
	 ArrayList<String> dataItems = new ArrayList<String>();  
	 public String Noti_text="Null";
		public int MsgServerID=0;

	//report alert
	String[] Distribtr_list;
	String DbrNodeId,DbrNodeType,DbrName;
	ArrayList<String> DbrArray=new ArrayList<String>();
	LinkedHashMap<String,String> hmapDistrbtrList=new LinkedHashMap<>();
	public String SelectedDistrbtrName="";

	public String fDate;

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



	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			try
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
				
				

				 final AlertDialog builder = new AlertDialog.Builder(InvoiceStoreSelection.this).create();
			       

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
								SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
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
			
		}
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice_storeselection);
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
		try {
			getDSRDetail();
			//report alert
			getDistribtrList();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Intent passedvals = getIntent();
		imei = passedvals.getStringExtra("imei");
		currSysDate = passedvals.getStringExtra("currSysDate");
		pickerDate = passedvals.getStringExtra("pickerDate");
		Bundle extras = passedvals.getExtras();



		if (extras != null) {
			
			 if (extras.containsKey("spnrDistSlctd")) {
			        spinnerDistributrValue= extras.getInt("spnrDistSlctd", 0);
			       
			    }
			    
			    else
			    {
			    	spinnerDistributrValue=0;
			    }
		    if (extras.containsKey("spinnerSlctd")) {
		        spinnerRouteValue= extras.getInt("spinnerSlctd", 0);
		       
		    }
		    
		    else
		    {
		    	spinnerRouteValue=0;
		    }
		}
		dbengine.open();
		distributor_name = dbengine.fnGetDistinctDistributorsName();
		distributor_name_id = dbengine.fnGetDistinctDistributorsID();
		dbengine.close();
		setUpVariable();
		showInputDialog();
	}
	
	
	protected void showInputDialog() {
		System.out.println("inTest :"+ inTest);
		inTest=inTest+1;
		dbengine.open();
		StoreName = new String[0];
		StorenameOther = new String[0];
		StoreID = new String[0];
		StoreSstat = new String[0];
		StorenameList= new String[0];
		StoreName = dbengine.fnGetStoreListForInvoice(selected_distributor_name_id,selected_route_name_id,"");
		dbengine.close();
		StorenameList=new String[StoreName.length];
		StorenameOther=new String[StoreName.length];
		StoreID=new String[StoreName.length];
		StoreSstat=new String[StoreName.length];
		OrderID=new String[StoreName.length];
		StoreIDOrderID=new String[StoreName.length];
		hmap.clear();
		int listRouteValue;
		int listDistValue;
		if(Spinner_Distributor.getSelectedItem()!=null)
		{
			
			listDistValue= adapterDisrt.getPosition(Spinner_Distributor.getSelectedItem().toString());
			
		}
		else
		{
			listDistValue=0;
			
		}
		
		if(Spinner_Route.getSelectedItem()!=null)
		{
			
			listRouteValue= adapterRoute.getPosition(Spinner_Route.getSelectedItem().toString());
			
		}
		else
		{
			listRouteValue=0;
			
		}
		for(int i=0;i<StoreName.length;i++)
		{
			String[] parts = StoreName[i].split("\\^");
			StorenameList[i] = parts[0]; 
			StorenameOther[i]= parts[1]; 
			int OdrStat= Integer.parseInt(parts[2]); 
			//hmap.put(StorenameList[i], StorenameOther[i]);
			StoreSstat[i]= parts[2];
			StringTokenizer abc = new StringTokenizer(String.valueOf(StorenameOther[i]), "_");
			//String getvalue=abc.nextToken().trim();
			
			String currentStoreId="";
			String currentStoreOrderId="";
			currentStoreId=abc.nextToken().trim();
			currentStoreOrderId=abc.nextToken().trim();
			StoreID[i]= currentStoreId;
			OrderID[i]=currentStoreOrderId;
			hmap.put(StorenameList[i], StorenameOther[i]);
			hmapStoreIdName.put(currentStoreOrderId, StorenameList[i]);
			StoreIDOrderID[i]=currentStoreOrderId;
			hmapStoreIdAndInvoiceOtherDetails.put(currentStoreOrderId, StorenameOther[i]);
			if(OdrStat==10)
			{
			hmapConfirmCancel.put(OrderID[i], "10");
				//hmapStoreIdAndInvoiceReturnRemaks.put(currentStoreOrderId,  parts[2]);

			}
		}
		
		
		
		/*try 
		{
		*/	
		
		editText = (EditText)findViewById(R.id.inputSearch);
		dataItems.clear();
		List<String> dataTemp = Arrays.asList(StoreIDOrderID);  
	    dataItems.addAll(dataTemp); 
	    final ListView areaLV = (ListView)findViewById(R.id.list_view);
		 adapter = new ListAdapter(InvoiceStoreSelection.this, dataItems,StoreIDOrderID,StoreSstat,StorenameOther,pickerDate,hmap,imei,currSysDate,StorenameList,listRouteValue,listDistValue,hmapStoreIdName,hmapStoreIdAndInvoiceOtherDetails);  
	     adapter.setCustomButtonListner(InvoiceStoreSelection.this);  
	     areaLV.setAdapter(adapter);
	   editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					areaLV.clearFocus();
					adapter.getFilter().filter(cs.toString());
				}
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
				}
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub	
				}
			});
		/*}
		catch(Exception e)
		{
			
		}*/
	
	
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

	public void setUpVariable()
	{
		ImageView img_side_popUp=(ImageView) findViewById(R.id.img_side_popUp);
		img_side_popUp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				open_pop_up();
			}
		});

		adapterDisrt = new ArrayAdapter<String>(InvoiceStoreSelection.this,
	    android.R.layout.simple_spinner_item, distributor_name);
		adapterDisrt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner_Distributor = (Spinner) findViewById(R.id.Spin_Distributor);  
		Spinner_Distributor.setAdapter(adapterDisrt);
		
		Spinner_Distributor.setOnItemSelectedListener(this);
		Spinner_Route = (Spinner) findViewById(R.id.Spin_Route);  
		Spinner_Route.setEnabled(false);
		
		if(distributor_name.length==2)
		{
			Spinner_Distributor.setSelection(distributor_name.length-1);
			Spinner_Route.setEnabled(true);
		}
		
		else
		{
			Spinner_Distributor.setSelection(spinnerDistributrValue);
			
		}
		
		Button But_Submit =(Button) findViewById(R.id.But_Submit);
		But_Submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				
				dbengine.open();
				int check=dbengine.checkRouteIDExistInStoreListTable();
				dbengine.close();
				if(check==0)
				{
					  AlertDialog.Builder alertDialog = new AlertDialog.Builder(InvoiceStoreSelection.this);
					  alertDialog.setTitle("Information");
				      alertDialog.setMessage("There is no Store for Submission");
				      alertDialog.setIcon(R.drawable.error);
				      alertDialog.setCancelable(false);
				        // Setting Positive "Yes" Button
				      alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() 
				        {
				            public void onClick(DialogInterface dialog,int which)
				            {
				 
				            	dialog.dismiss();
				            }
				        });
				 
				     alertDialog.show();
				}
				else
				{
				new GetData().execute();
				}
				
			}
		});
		
		Button But_Conform_Select = (Button) findViewById(R.id.But_Conform_Select_Invoice);//Change Id to Conform Selected Button
		But_Conform_Select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strReason="";
				if(isOnline())
				{
					
				}
				else
				{
					 showNoConnAlert();
					 return;
	
				}
				whereTo = "11";
				dbengine.open();
				StoreConformInvoiceOrder = dbengine.ProcessConformStoreReq();
				dbengine.close();
				if (hmapConfirmCancel.size() != 0)
				{
					midPartConformInvoices();
					showPendingStorelistConformInvoices();
				}
				else 
				{
	                AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(InvoiceStoreSelection.this);
					alertDialogNoConn.setTitle("Information");
					alertDialogNoConn.setMessage("No Store Order is selected for confirmation.");
					alertDialogNoConn.setPositiveButton(R.string.txtOk,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) 
								{
		                      dialog.dismiss();
		                     
								}
							});
					
					alertDialogNoConn.setIcon(R.drawable.info_ico);
					AlertDialog alert = alertDialogNoConn.create();
					alert.show();
					return;
				}
				
			}
			
		});
		
		Button But_Cancel_Select = (Button) findViewById(R.id.But_Cancel_Select);
		But_Cancel_Select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isOnline())
				{}
				 else
				 {
					 showNoConnAlert();
					 return;
	
				 }
				
				whereTo = "11";
				dbengine.open();
				 StoreList2Procs = dbengine.ProcessCancelStoreReq();
				 dbengine.close();
				if (hmapConfirmCancel.size()!=0)
				{
					midPartCancelInvoices();
					showPendingStorelistCancelInvoices();
				}
				
				else {
	                AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(InvoiceStoreSelection.this);
					alertDialogNoConn.setTitle("Information");
					alertDialogNoConn.setMessage("No Store order is selected for cancellation.");
					alertDialogNoConn.setPositiveButton(R.string.txtOk,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) 
								{
		                      dialog.dismiss();
		                     
								}
							});
					
					alertDialogNoConn.setIcon(R.drawable.info_ico);
					AlertDialog alert = alertDialogNoConn.create();
					alert.show();
					return;
				}
				
			}
		});
		ImageView ButBack = (ImageView) findViewById(R.id.btn_bck);
		ButBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				
				dbengine.open();
				 int check = dbengine.CheckNonSubmitDataIntblInvoiceButtonStoreMstr();
				 dbengine.close();
				 
				 System.out.println("Check Data in NonSubmitDataIntblInvoiceButtonStoreMstr"+check);
				 if(check==1)
				 {
				AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(
						InvoiceStoreSelection.this);
				alertDialogSubmitConfirm.setTitle("Information");
				alertDialogSubmitConfirm.setMessage("You have marked orders for execution / deletion ,but not submited, do you wish to continue?");
				alertDialogSubmitConfirm.setCancelable(false);

				alertDialogSubmitConfirm.setNeutralButton(R.string.txtYes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
								dbengine.fnAllServerOrdersFlgWith0();
								Intent intent = new Intent(InvoiceStoreSelection.this, LauncherActivity.class);
						    	intent.putExtra("imei", imei);
						         startActivity(intent);
						         finish();
							}
						});

				alertDialogSubmitConfirm.setNegativeButton(R.string.txtNo,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							}
						});

				alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
				AlertDialog alert = alertDialogSubmitConfirm.create();
				alert.show();
			}
				 else
					{
					 Intent intent = new Intent(InvoiceStoreSelection.this, LauncherActivity.class);
				    	intent.putExtra("imei", imei);
				         startActivity(intent);
				         finish();
					}
			}
			
		});
		
		
		
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch(parent.getId())
		{
        case R.id.Spin_Distributor:   
        	int index=Spinner_Distributor.getSelectedItemPosition();
            selected_distributor_name_id=distributor_name_id[index];
           
            
            System.out.println("Dangi new testing selected_distributor_name_id:"+selected_distributor_name_id);
           // selected_route_name_id="0";
            if(index==0)
              {
             	   Spinner_Route.setEnabled(false); 
             	   Spinner_Route.setSelection(0);
             	   selected_distributor_name_id="0";
             	   selected_route_name_id="0";
              }
            else
              {
             	   Spinner_Route.setEnabled(true);
             	   dbengine.open();
             	   route_name = dbengine.fnGetDistinctRouteName(selected_distributor_name_id);
             	   route_name_id = dbengine.fnGetDistinctRouteId(selected_distributor_name_id);
            		   dbengine.close();
             	   adapterRoute = new ArrayAdapter<String>(InvoiceStoreSelection.this,
            	            android.R.layout.simple_spinner_item, route_name);
             	  adapterRoute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             	   Spinner_Route.setAdapter(adapterRoute);
             	   Spinner_Route.setOnItemSelectedListener(this);
             	  Spinner_Route.setSelection(spinnerRouteValue);
             	   
              }
            
            showInputDialog();
            break;
        	
        	                      /* int index=Spinner_Distributor.getSelectedItemPosition();
        	                       selected_distributor_name_id=distributor_name_id[index];
        	                       Spinner_Distributor.setEnabled(false);
        	                  
	        	                    	   Spinner_Route.setEnabled(true);
	        	                    	   dbengine.open();
	        	                    	   route_name = dbengine.fnGetDistinctRouteName(selected_distributor_name_id);
	        	                    	   route_name_id = dbengine.fnGetDistinctRouteId(selected_distributor_name_id);
	        	                   		   dbengine.close();
	        	                    	   ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(InvoiceStoreSelection.this,
	        	                   	            android.R.layout.simple_spinner_item, route_name);
	        	                   		   adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	                    	   Spinner_Route.setAdapter(adapter2);
	        	                    	   Spinner_Route.setOnItemSelectedListener(this);
        	                       showInputDialog();
        	                       break;*/
        case R.id.Spin_Route: 
                                   int index1=Spinner_Route.getSelectedItemPosition();
                                   selected_route_name_id=route_name_id[index1];
                                  if(index1==0)
	        	                     {
                                	  selected_route_name_id="0";   
	        	                     }
      	                     
                                   showInputDialog();
                                   break;
       default:
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
			
	}


	@Override
	public void onButtonClickListner(int position, final String value,final TextView txtvw,final Button btninv,final CheckBox checkConformExecutionStore,final int clkdView,String OrderIdOnClickedChekBoxNew)
	{
		if(clkdView==1)
		{/*
			 dbengine.open();
            final StringTokenizer ad = new StringTokenizer(String.valueOf(hmap.get(value)), "_");	                      
    		String TagStoreID= ad.nextToken().trim();
    		String TagOrderID= ad.nextToken().trim();
    		String TagRouteID= ad.nextToken().trim();
    		String TagDistID= ad.nextToken().trim();
    		String TagDate= ad.nextToken().trim();
    		String TagSstat= ad.nextToken().trim();
    		if(Integer.parseInt(TagSstat)==0)
    		{
    			hmap.remove(value);
        		hmap.put(value, TagStoreID+"_"+TagOrderID+"_"+TagRouteID+"_"+TagDistID+"_"+TagDate+"_"+7);
        	
                dbengine.UpdateInvoiceButtonStoreFlag(TagStoreID.trim(), 7,1,TagOrderID.trim());
                String abc1= dbengine.FetchInvoiceButtonSstat1(TagStoreID,TagOrderID);
            
               String abc= dbengine.FetchInvoiceButtonSstat(TagStoreID,TagOrderID);
               if(Integer.parseInt(abc)==0)
               {
              	 dbengine.saveInvoiceButtonStoreTransac("NA",pickerDate,TagStoreID,"NA","NA",0.0, 0,
              			 0, 0,TagOrderID,"","7",1);
              	 dbengine.saveInvoiceButtonStoreTransac("NA",TagDate,TagStoreID,"NA","NA",0.0, 0,
                           0, 0,TagOrderID,"","7",1,0.0,TagRouteID,"0");
               }
               else
               {
              	 dbengine.UpdatetblInvoiceButtonTransac(TagStoreID, 7,1,TagOrderID);
               }
              
                dbengine.close();
                txtvw.setTextColor(Color.RED);
                btninv.setTextColor(Color.RED); 
                btnCan.setTextColor(Color.RED); 
                txtvw.setEnabled(false);
                btninv.setEnabled(false);
                btnCan.setEnabled(true);
                btnCan.setBackground(getResources().getDrawable(R.drawable.delete_button_selected_red));
                btninv.setVisibility(View.VISIBLE);
                btnCan.setVisibility(View.VISIBLE);
                checkConformExecutionStore.setEnabled(false);
                checkConformExecutionStore.setButtonDrawable(getResources().getDrawable(R.drawable.checknew_red1));
               
    		}
    		else
    		{
    			hmap.remove(value);
          		hmap.put(value, TagStoreID+"_"+TagOrderID+"_"+TagRouteID+"_"+TagDistID+"_"+TagDate+"_"+0);
          	
                 dbengine.UpdateInvoiceButtonStoreFlag(TagStoreID.trim(), 0,0,TagOrderID.trim());
               //  dbengine.deleteInvoiceRelatedTableEtrySavedData(TagStoreID,TagOrderID);
              
                 dbengine.UpdatetblInvoiceButtonTransac(TagStoreID, 0,0,TagOrderID);
                
                  dbengine.close();
                  txtvw.setTextColor(Color.BLACK);
                  btninv.setTextColor(Color.BLACK); 
                  btnCan.setTextColor(Color.BLACK); 
                  txtvw.setEnabled(true);
                  btninv.setEnabled(true);
                  btnCan.setEnabled(true);
                 btnCan.setBackground(getResources().getDrawable(R.drawable.delete_button_unselected));
                  btninv.setVisibility(View.VISIBLE);
                  btnCan.setVisibility(View.VISIBLE);
                  checkConformExecutionStore.setVisibility(View.VISIBLE);
                  checkConformExecutionStore.setEnabled(true);
                  checkConformExecutionStore.setChecked(false);
                  checkConformExecutionStore.setButtonDrawable(getResources().getDrawable(R.drawable.unchecknew));
    		}
    		
            
         
				
			//return;	
		*/}
		if(clkdView==2)
		{
			if(checkConformExecutionStore.isChecked())
			{
					
                dbengine.open();
                final StringTokenizer ad = new StringTokenizer(String.valueOf(hmapStoreIdAndInvoiceOtherDetails.get(OrderIdOnClickedChekBoxNew)), "_");	                      
        		String TagStoreID= ad.nextToken().trim();
        		String TagOrderID= ad.nextToken().trim();
        		String TagRouteID= ad.nextToken().trim();
        		String TagDistID= ad.nextToken().trim();
        		String TagDate= ad.nextToken().trim();
        		String TagSstat= ad.nextToken().trim();
        		String isCancelConfirm=ad.nextToken().trim();
        		hmapStoreIdAndInvoiceOtherDetails.remove(OrderIdOnClickedChekBoxNew);
        		hmapStoreIdAndInvoiceOtherDetails.put(OrderIdOnClickedChekBoxNew, TagStoreID+"_"+TagOrderID+"_"+TagRouteID+"_"+TagDistID+"_"+TagDate+"_"+10+"_"+isCancelConfirm);
        		hmapConfirmCancel.put(TagOrderID, "10");
                dbengine.UpdateInvoiceButtonStoreFlag(TagStoreID.trim(), 10,0,TagOrderID.trim());
                String abc1= dbengine.FetchInvoiceButtonSstat1(TagStoreID,TagOrderID);
            
               String abc= dbengine.FetchInvoiceButtonSstat(TagStoreID,TagOrderID);
               if(Integer.parseInt(abc)==0)
               {
              	/* dbengine.saveInvoiceButtonStoreTransac("NA",pickerDate,TagStoreID,"NA","NA",0.0, 0,
              			 0, 0,TagOrderID,"","7",1);*/
					if(!strReason.equals(""))
					{
						dbengine.saveInvoiceButtonStoreTransac("NA",TagDate,TagStoreID,"0","0",0.0, 0,
								0, 0,TagOrderID,"","10",0,0.0,TagRouteID,"0",strReason);
					}
					else
					{
						dbengine.saveInvoiceButtonStoreTransac("NA",TagDate,TagStoreID,"0","0",0.0, 0,
								0, 0,TagOrderID,"","10",0,0.0,TagRouteID,"0","");
					}

               }
               else
               {
              	 dbengine.UpdatetblInvoiceButtonTransac(TagStoreID, 10,0,TagOrderID,strReason);
               }
              
                dbengine.close();
                txtvw.setTextColor(Color.parseColor("#9C27B0"));
                btninv.setTextColor(Color.BLUE); 
              //  btnCan.setTextColor(Color.BLUE); 
                txtvw.setEnabled(false);
                btninv.setEnabled(false);
              //  btnCan.setEnabled(false);
               
                //getResources().getDrawable(R.drawable.ready)
                btninv.setVisibility(View.GONE);
               // btnCan.setVisibility(View.GONE);
                checkConformExecutionStore.setChecked(true);
                checkConformExecutionStore.setButtonDrawable(getResources().getDrawable(R.drawable.checked));
                
             
					
				//return;	
			
			}
			else
			{


               // dialog.dismiss();
                dbengine.open();
                final StringTokenizer ad = new StringTokenizer(String.valueOf(hmapStoreIdAndInvoiceOtherDetails.get(OrderIdOnClickedChekBoxNew)), "_");	                      
        		String TagStoreID= ad.nextToken().trim();
        		String TagOrderID= ad.nextToken().trim();
        		String TagRouteID= ad.nextToken().trim();
        		String TagDistID= ad.nextToken().trim();
        		String TagDate= ad.nextToken().trim();
        		String TagSstat= ad.nextToken().trim();
        		String isCancelConfirm=ad.nextToken().trim();
        		hmapStoreIdAndInvoiceOtherDetails.remove(OrderIdOnClickedChekBoxNew);
        		hmapStoreIdAndInvoiceOtherDetails.put(OrderIdOnClickedChekBoxNew, TagStoreID+"_"+TagOrderID+"_"+TagRouteID+"_"+TagDistID+"_"+TagDate+"_"+0+"_"+isCancelConfirm);
        		hmapConfirmCancel.remove(TagOrderID);
              dbengine.UpdateInvoiceButtonStoreFlag(TagStoreID.trim(), 0,0,TagOrderID.trim());
              // dbengine.deleteInvoiceRelatedTableEtrySavedData(TagStoreID,TagOrderID);
            
               dbengine.UpdatetblInvoiceButtonTransac(TagStoreID, 0,0,TagOrderID,strReason);
              
                dbengine.close();
                txtvw.setTextColor(Color.BLACK);
                btninv.setTextColor(Color.BLACK); 
               // btnCan.setTextColor(Color.BLACK); 
                txtvw.setEnabled(true);
                btninv.setEnabled(true);
              //  btnCan.setEnabled(true);
               
                btninv.setVisibility(View.VISIBLE);
             //   btnCan.setVisibility(View.VISIBLE);
                checkConformExecutionStore.setChecked(false);
                checkConformExecutionStore.setEnabled(true);
                checkConformExecutionStore.setButtonDrawable(getResources().getDrawable(R.drawable.unchecked));
					
				/*AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(InvoiceStoreSelection.this);
				alertDialogNoConn.setTitle("Information");
				alertDialogNoConn.setMessage("Are you sure you want to unmark the store Order from the conform store conform order :"+value);
				
				alertDialogNoConn.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) 
							{}
						});
				alertDialogNoConn.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) 
							{
	                      dialog.dismiss();
	                     
							}
						});
				alertDialogNoConn.setIcon(R.drawable.info_ico);
				AlertDialog alert = alertDialogNoConn.create();
				alert.show();*/
				//return;	
			
			
			}
		}
	}
	


	 public void showNoConnAlert() 
	 {
			AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(InvoiceStoreSelection.this);
			alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
			alertDialogNoConn.setMessage(R.string.genTermNoDataConnectionFullMsg);
			alertDialogNoConn.setNeutralButton(R.string.txtOk,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) 
						{
                     dialog.dismiss();
						}
					});
			alertDialogNoConn.setIcon(R.drawable.error_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();
		}
	 
	 public void midPartConformInvoices() {
			String tempSID;
			String tempSNAME;
//StoreConformInvoiceOrder
			stIDs = new ArrayList<String>(StoreConformInvoiceOrder.length);
			stNames = new ArrayList<String>(StoreConformInvoiceOrder.length);
			for (int x = 0; x < (StoreConformInvoiceOrder.length); x++) 
			{
				StringTokenizer tokens = new StringTokenizer(String.valueOf(StoreConformInvoiceOrder[x]), "%");
				tempSID = tokens.nextToken().trim();
				tempSNAME = tokens.nextToken().trim();
				stIDs.add(x, tempSID);
				stNames.add(x, tempSNAME);
			}
		}
	 
	 
	 public void showPendingStorelistCancelInvoices()
	 {

		 LayoutInflater inflater = getLayoutInflater();
		 alertLayout = inflater.inflate(R.layout.layout_custom_dialog_cancelationremarks, null);
		 et_Reason = (EditText) alertLayout.findViewById(R.id.et_CancelationReason);
		 et_Reason.setVisibility(View.INVISIBLE);
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvoiceStoreSelection.this);

		 TextView tv = new TextView(InvoiceStoreSelection.this);
		 tv.setText("Please provide cancelation reason:");
		 tv.setPadding(40, 10, 40, 10);
		 tv.setBackgroundColor(Color.parseColor("#486FA8"));
		 tv.setGravity(Gravity.CENTER);
		 tv.setTextSize(20);
		 tv.setTextColor(Color.parseColor("#ffffff"));
		 alertDialogBuilder.setView(alertLayout);
		 alertDialogBuilder.setCustomTitle(tv);
		 alertDialogBuilder.setIcon(R.drawable.info_ico);
		 alertDialogBuilder.setCancelable(false);
		 et_Reason.setVisibility(View.VISIBLE);


		 alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int which) {
				 if(et_Reason.getText().toString().trim() !="" && !TextUtils.isEmpty(et_Reason.getText().toString()))
				 {
					 dialog.dismiss();
strReason=	et_Reason.getText().toString().trim();
					 fnInvoiceWithCancelReason();
				 }




			 }
		 });

		 AlertDialog alertDialog = alertDialogBuilder.create();


		 try {
			 alertDialog.show();
		 } catch (Exception e) {
			 // WindowManager$BadTokenException will be caught and the app would
			 // not display the 'Force Close' message
			 e.printStackTrace();
		 }



	 }
	 public  void fnInvoiceWithCancelReason()
	 {
		 ContextThemeWrapper cw = new ContextThemeWrapper(this,R.style.AlertDialogTheme);

		 System.out.println("Nitish showPendingStorelistCancelInvoices");
		 AlertDialog.Builder builder = new AlertDialog.Builder(cw);
		 builder.setTitle(R.string.genTermSelectStoresPendingToCancel);
		 mSelectedItems.clear();

		 final String[] stNames4List = new String[stNames.size()];
		 checksCancelInvoices=new boolean[stNames.size()];
		 stNames.toArray(stNames4List);
		 for(int cntPendingList=0;cntPendingList<stNames4List.length;cntPendingList++)
		 {
			 mSelectedItems.add(stNames4List[cntPendingList]);
			 checksCancelInvoices[cntPendingList]=true;
		 }

		 builder.setPositiveButton(R.string.genTermSubmitCancelInvoiceList,
				 new DialogInterface.OnClickListener() {

					 @Override
					 public void onClick(DialogInterface dialog, int which) {

						 if (mSelectedItems.size() == 0) {

							 showPendingStorelistCancelInvoices();
						 }

						 else {
							 whatTask = 2;

							 try {


								 new bgTasker().execute().get();
							 } catch (InterruptedException e) {
								 e.printStackTrace();
								 ////System.out.println(e);
							 } catch (ExecutionException e) {
								 e.printStackTrace();
							 }
							 // --

						 }

					 }
				 });
			/*builder.setNeutralButton(R.string.genTermDirectlyChangeRouteInvoice,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(InvoiceStoreSelection.this, LauncherActivity.class);
					    	intent.putExtra("imei", imei);
					         startActivity(intent);
					         finish();

						}
					});*/
		 builder.setNegativeButton(R.string.txtCancle,
				 new DialogInterface.OnClickListener() {

					 @Override
					 public void onClick(DialogInterface arg0, int arg1) {
						 // TODO Auto-generated method stub
						 closeList = 1;
						 arg0.dismiss();
					 }
				 });

		 AlertDialog alert = builder.create();
		 //AlertDialog alert = builder.create();
		 alert.show();
			/*if (closeList == 1) {
				closeList = 0;
				alert.dismiss();


			} else {
				alert.show();
				alert.setCancelable(false);
			}*/

	 }
	 
	 public void midPartCancelInvoices() {
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
			
			//System.out.println("Nitish midPartCancelInvoices :"+stIDs+"="+stNames);
			
		}
	 
	 public void showPendingStorelistConformInvoices() {
			ContextThemeWrapper cw = new ContextThemeWrapper(this,R.style.AlertDialogTheme);
			System.out.println("Nitish showPendingStorelistConformInvoices");
			AlertDialog.Builder builder = new AlertDialog.Builder(cw);
			//StoreConformInvoiceOrder
			builder.setTitle(R.string.genTermSelectStoresPendingToComplete);
			mSelectedItemsConfornInvoiceOrders.clear();

			final String[] StoreConformInvoiceOrderList = new String[stNames.size()];
			checksConformInvoices=new boolean[stNames.size()];
			stNames.toArray(StoreConformInvoiceOrderList);
			for(int cntPendingList=0;cntPendingList<StoreConformInvoiceOrderList.length;cntPendingList++)
			{
				mSelectedItemsConfornInvoiceOrders.add(StoreConformInvoiceOrderList[cntPendingList]);
				checksConformInvoices[cntPendingList]=true;
			}
			/*builder.setMultiChoiceItems(StoreConformInvoiceOrderList, checksConformInvoices,
					new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {

							if (isChecked) {
								mSelectedItemsConfornInvoiceOrders.add(StoreConformInvoiceOrderList[which]);

							} else if (mSelectedItemsConfornInvoiceOrders.contains(StoreConformInvoiceOrderList[which])) {
								//////System.out.println("Abhinav store Selection  Step 5");
								mSelectedItemsConfornInvoiceOrders.remove(StoreConformInvoiceOrderList[which]);

							}
						}
					});
*/
			
			builder.setPositiveButton(R.string.genTermSubmitSelectedInvoice,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							if (mSelectedItemsConfornInvoiceOrders.size() == 0) {
								/*Toast.makeText(
										getApplicationContext(),
										R.string.genTermNoStroeSelectedOnSubmit,
										Toast.LENGTH_SHORT).show();*/
								showPendingStorelistConformInvoices();
							}

							else {
								whatTask = 2;
								// -- Route Info Exec()
								try {
									
									new bgTaskerConformInvoices().execute().get();
								} catch (InterruptedException e) {
									e.printStackTrace();
									////System.out.println(e);
								} catch (ExecutionException e) {
									e.printStackTrace();
								}
								// --

							}

						}
					});
			/*builder.setNeutralButton(R.string.genTermDirectlyChangeRouteInvoice,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(InvoiceStoreSelection.this, LauncherActivity.class);
					    	intent.putExtra("imei", imei);
					         startActivity(intent);
					         finish();
							
						}
					});*/
			builder.setNegativeButton(R.string.txtCancle,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							
							arg0.cancel();
							
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
			//return;
		}
	 
	 
	 private class bgTaskerConformInvoices extends AsyncTask<Void, Void, Void> {
			@Override
			protected Void doInBackground(Void... params) {

				try {
						dbengine.open();
						String rID=dbengine.GetActiveRouteID();
						dbengine.close();
					if (whatTask == 2) 
					{
						whatTask = 0;
						dbengine.open();
						for (int nosSelected = 0; nosSelected <= mSelectedItemsConfornInvoiceOrders.size() - 1; nosSelected++) 
						{
							String valSN = (String) mSelectedItemsConfornInvoiceOrders.get(nosSelected);
							int valID = stNames.indexOf(valSN);
							String stIDneeded = stIDs.get(valID);
						}
						dbengine.close();
						dbengine.UpdateCancelStoreFlag(hmapConfirmCancel,0,strReason);

					}else if (whatTask == 3) 
					{
						whatTask = 0;
					}
					else if (whatTask == 1) 
					{
						// clear all
						whatTask = 0;
						
					}

				} catch (Exception e) {
					Log.i("bgTasker", "bgTasker Execution Failed!", e);

				}

				finally {

					Log.i("bgTasker", "bgTasker Execution Completed...");

				}

				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onCancelled() {
				Log.i("bgTasker", "bgTasker Execution Cancelled");
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

				Log.i("bgTasker", "bgTasker Execution cycle completed");
				//pDialog2.dismiss();
				hmapConfirmCancel.clear();
				showInputDialog();
				whatTask = 0;

			}
		}
	 
	
	 
	 private class bgTasker extends AsyncTask<Void, Void, Void> {
			@Override
			protected Void doInBackground(Void... params) {

				try {
						dbengine.open();
						String rID=dbengine.GetActiveRouteID();

						dbengine.close();
					if (whatTask == 2) 
					{
						whatTask = 0;
						dbengine.open();
						for (int nosSelected = 0; nosSelected <= mSelectedItems.size() - 1; nosSelected++) 
						{
							String valSN = (String) mSelectedItems.get(nosSelected);
							int valID = stNames.indexOf(valSN);
							String stIDneeded = stIDs.get(valID);
						}
						dbengine.close();
						
						//9,1=cancel
						dbengine.UpdateCancelStoreFlag(hmapConfirmCancel,1,strReason);
						

					}else if (whatTask == 3) 
					{
						whatTask = 0;
						
					}else if (whatTask == 1) 
					{
						// clear all
						whatTask = 0;
						
					}

				} catch (Exception e) {
					Log.i("bgTasker", "bgTasker Execution Failed!", e);

				}

				finally {

					Log.i("bgTasker", "bgTasker Execution Completed...");

				}

				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onCancelled() {
				Log.i("bgTasker", " Execution Cancelled");
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				hmapConfirmCancel.clear();
				showInputDialog();
				Log.i("bgTasker", "bgTasker Execution cycle completed");
				//pDialog2.dismiss();
				whatTask = 0;

			}
		}
	 
	
	

	
	 
	 
	 public void submitDataToServer()
	 {


			syncTIMESTAMP = System.currentTimeMillis();
			Date dateobj = new Date(syncTIMESTAMP);
			SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
			fullFileName1 = imei +df.format(dateobj).toString().trim();
			try {
				
				 File InvoiceXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.InvoiceXMLFolder);
					
				 if (!InvoiceXMLFolder.exists())
					{
						InvoiceXMLFolder.mkdirs();
						 
					} 
				String[] OrderListing=dbengine.fngetDistictOrderIdsForSubmission();
				DA.open();
				DA.export(dbengine.DATABASE_NAME, fullFileName1,9,OrderListing);
				DA.close();
				

			} catch (IOException e) {

				e.printStackTrace();
			}

		
	 }
	 
	 public class GetData extends AsyncTask<Void, Void, Void>
		{

			 @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            
		            mProgressDialog = new ProgressDialog(InvoiceStoreSelection.this);
		            mProgressDialog.setTitle("Plase Wait");
		            mProgressDialog.setMessage("While we submit your Data...");
		            mProgressDialog.setIndeterminate(true);
		            mProgressDialog.setCancelable(false);
		           mProgressDialog.show();
		        }
			 
			@Override
			protected Void doInBackground(Void... params)
			{
				
				submitDataToServer();
				return null;
			}
			 @Override
		        protected void onPostExecute(Void args) {
				 mProgressDialog.dismiss();
				
				 Intent syncIntent = new Intent(InvoiceStoreSelection.this, InvoiceSyncMaster.class);
					//syncIntent.putExtra("xmlPathForSync",Environment.getExternalStorageDirectory() + "/TJUKSFAInvoicexml/" + fullFileName1 + ".xml");
				    syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.InvoiceXMLFolder + "/" + fullFileName1 + ".xml");

				    syncIntent.putExtra("OrigZipFileName", fullFileName1);
					syncIntent.putExtra("whereTo", "9");
					syncIntent.putStringArrayListExtra("mSelectedItems", mSelectedItemsConfornInvoiceOrders);
					syncIntent.putExtra("imei", imei);
					syncIntent.putExtra("currSysDate", currSysDate);
					syncIntent.putExtra("pickerDate", pickerDate);

					startActivity(syncIntent);
					finish();
			 }
			
		}
	protected void open_pop_up()
	{
		dialog = new Dialog(InvoiceStoreSelection.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.selection_header_custom);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
		WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
		parms.gravity = Gravity.TOP | Gravity.LEFT;
		parms.height=parms.MATCH_PARENT;
		parms.dimAmount = (float) 0.5;

		final Button btnDSRTrack = (Button) dialog.findViewById(R.id.btnDSRTrack);
		btnDSRTrack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				/*Intent intent=new Intent(InvoiceStoreSelection.this,WebViewDSRTrackerActivity.class);
				startActivity(intent);*/
				openDSRTrackerAlert();
			}
		});


		final   Button butn_Change_dsr = (Button) dialog.findViewById(R.id.butn_Change_dsr);
		butn_Change_dsr.setVisibility(View.GONE);


		final   Button btnExecution = (Button) dialog.findViewById(R.id.btnExecution);
		btnExecution.setVisibility(View.GONE);

		final   Button btnDistributorMap = (Button) dialog.findViewById(R.id.btnDistributorMap);
		btnDistributorMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent=new Intent(InvoiceStoreSelection.this,DistributorMapActivity.class);
				startActivity(intent);
				finish();
			}
		});

		final   Button butHome = (Button) dialog.findViewById(R.id.butHome);
		butHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent=new Intent(InvoiceStoreSelection.this,AllButtonActivity.class);
				startActivity(intent);
				finish();
			}
		});

		final   Button btnDistributorStock = (Button) dialog.findViewById(R.id.btnDistributorStock);
		btnDistributorStock.setOnClickListener(new View.OnClickListener() {
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
				Intent i=new Intent(InvoiceStoreSelection.this,DistributorEntryActivity.class);
				i.putExtra("imei", imei);
				i.putExtra("CstmrNodeId", CstmrNodeId);
				i.putExtra("CstomrNodeType", CstomrNodeType);
				i.putExtra("fDate", fDate);
				startActivity(i);
				finish();
			}
		});

		final   Button butMarketVisit = (Button) dialog.findViewById(R.id.butMarketVisit);
		butMarketVisit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
               /* Intent intent = new Intent(DetailReportSummaryActivityForAll.this, AllButtonActivity.class);
                startActivity(intent);
                finish();*/
				int checkDataNotSync = dbengine.CheckUserDoneGetStoreOrNot();
				Date date1 = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
				String fDate = sdf.format(date1).toString().trim();
				if (checkDataNotSync == 1)
				{
					dbengine.open();
					String rID = dbengine.GetActiveRouteID();
					dbengine.close();

					// Date date=new Date();
					sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
					String fDateNew = sdf.format(date1).toString();
					//fDate = passDate.trim().toString();


					// In Splash Screen SP, we are sending this Format "dd-MMM-yyyy"
					// But InLauncher Screen SP, we are sending this Format "dd-MM-yyyy"


					Intent storeIntent = new Intent(InvoiceStoreSelection.this, StoreSelection.class);
					storeIntent.putExtra("imei", imei);
					storeIntent.putExtra("userDate", fDate);
					storeIntent.putExtra("pickerDate", fDateNew);
					storeIntent.putExtra("rID", rID);
					startActivity(storeIntent);
					finish();

				}
				else
				{
                   /* Intent i=new Intent(DetailReportSummaryActivityForAll.this,AllButtonActivity.class);
                    startActivity(i);
                    finish();*/

					openMarketVisitAlert();
				}
			}
		});




		final   Button butn_store_validation = (Button) dialog.findViewById(R.id.butn_store_validation);
		butn_store_validation.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(InvoiceStoreSelection.this, StorelistActivity.class);
				startActivity(intent);
				finish();
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

		final Button but_SalesSummray = (Button) dialog.findViewById(R.id.btnSalesSummary);
		but_SalesSummray.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				String rID="0";
				but_SalesSummray.setBackgroundColor(Color.GREEN);
				dialog.dismiss();
				// Intent intent = new Intent(StoreSelection.this, My_Summary.class);
				//  Intent intent = new Intent(StoreSelection.this, DetailReport_Activity.class);
                /*Intent intent = new Intent(StorelistActivity.this, DetailReportSummaryActivityForAll.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", CommonInfo.userDate);
                intent.putExtra("pickerDate", CommonInfo.pickerDate);
                intent.putExtra("rID", rID);
                intent.putExtra("back", "0");
                startActivity(intent);
                finish();*/
				openReportAlert();

			}
		});















		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	void openMarketVisitAlert()
	{
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(InvoiceStoreSelection.this);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.market_visit_alert, null);
		alert.setView(view);

		alert.setCancelable(false);

		final RadioButton rb_myVisit= (RadioButton) view.findViewById(R.id.rb_myVisit);
		final RadioButton rb_dsrVisit= (RadioButton) view.findViewById(R.id.rb_dsrVisit);
		final RadioButton rb_jointWorking= (RadioButton) view.findViewById(R.id.rb_jointWorking);
		final Spinner spinner_dsrVisit= (Spinner) view.findViewById(R.id.spinner_dsrVisit);
		final Spinner spinner_jointWorking= (Spinner) view.findViewById(R.id.spinner_jointWorking);
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
				if(rb_myVisit.isChecked())
				{
                    /*String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    CommonInfo.PersonNodeID=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    CommonInfo.PersonNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);*/


					String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

					int tempSalesmanNodeId=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
					int tempSalesmanNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);
					shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);
					flgDataScopeSharedPref(1);

					shardPrefForCoverageArea(0,0);
					flgDSRSOSharedPref(1);
					Intent i=new Intent(InvoiceStoreSelection.this,LauncherActivity.class);
					startActivity(i);
					finish();
				}
				else if(rb_dsrVisit.isChecked())
				{
					if(!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSM") && !SelectedDSRValue.equals("No DSM") )
					{

						String DSRNodeIdAndNodeType= dbengine.fnGetDSRNodeIdAndNodeType(SelectedDSRValue);
						int tempCoverageAreaNodeID=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
						int tempCoverageAreaNodeType=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
						shardPrefForCoverageArea(tempCoverageAreaNodeID,tempCoverageAreaNodeType);
						flgDSRSOSharedPref(2);


						String DSRPersonNodeIdAndNodeType= dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
						int tempSalesmanNodeId=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
						int tempSalesmanNodeType=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
						shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);
						flgDataScopeSharedPref(2);
						Intent i = new Intent(InvoiceStoreSelection.this, LauncherActivity.class);
						startActivity(i);
						finish();
					}
					else
					{
						showAlertForEveryOne("Please select DSM to Proceeds.");
					}
				}
				else if(rb_jointWorking.isChecked())
				{
					if(!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSM") && !SelectedDSRValue.equals("No DSM") )
					{
						// Find GPS
                       /* String DSRNodeIdAndNodeType= dbengine.fnGetDSRNodeIdAndNodeType(SelectedDSRValue);
                        CommonInfo.CoverageAreaNodeID=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        CommonInfo.CoverageAreaNodeType=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                        CommonInfo.FlgDSRSO=2;


                        String DSRPersonNodeIdAndNodeType= dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
                        CommonInfo.SalesmanNodeId=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        CommonInfo.SalesmanNodeType=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                        CommonInfo.flgDataScope=2;
                        Intent i = new Intent(AllButtonActivity.this, LauncherActivity.class);
                        startActivity(i);
                        finish();*/
					}
					else
					{
						showAlertForEveryOne("Please select DSM to Proceeds.");
					}
				}
				else
				{
					showAlertForEveryOne("Please select atleast one option to Proceeds.");
				}
			}
		});

		rb_myVisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(rb_myVisit.isChecked())
				{
					rb_dsrVisit.setChecked(false);
					rb_jointWorking.setChecked(false);
					spinner_dsrVisit.setVisibility(View.GONE);
					spinner_jointWorking.setVisibility(View.GONE);
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
					spinner_jointWorking.setVisibility(View.GONE);

					ArrayAdapter adapterCategory=new ArrayAdapter(InvoiceStoreSelection.this, android.R.layout.simple_spinner_item,drsNames);
					adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_dsrVisit.setAdapter(adapterCategory);
					spinner_dsrVisit.setVisibility(View.VISIBLE);

					spinner_dsrVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

		rb_jointWorking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(rb_jointWorking.isChecked())
				{
					rb_myVisit.setChecked(false);
					rb_dsrVisit.setChecked(false);
					spinner_dsrVisit.setVisibility(View.GONE);

					ArrayAdapter adapterCategory=new ArrayAdapter(InvoiceStoreSelection.this, android.R.layout.simple_spinner_item,drsNames);
					adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_jointWorking.setAdapter(adapterCategory);
					spinner_jointWorking.setVisibility(View.VISIBLE);

					spinner_jointWorking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
					{

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
						{
							// TODO Auto-generated method stub
							SelectedDSRValue = spinner_jointWorking.getSelectedItem().toString();

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

		dialog.show();
	}

	public void showAlertForEveryOne(String msg)
	{
		//AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(new ContextThemeWrapper(LauncherActivity.this, R.style.Dialog));
		android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(InvoiceStoreSelection.this);

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
	void openReportAlert()
	{
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(InvoiceStoreSelection.this);
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
				if(rb_myReport.isChecked())
				{ String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

					int tempSalesmanNodeId=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
					int tempSalesmanNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);
					shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);

					flgDataScopeSharedPref(1);
                   /* CommonInfo.SalesmanNodeId=0;
                    CommonInfo.SalesmanNodeType=0;
                    CommonInfo.flgDataScope=1;*/
					Intent i=new Intent(InvoiceStoreSelection.this,DetailReportSummaryActivityForAll.class);
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
					Intent i=new Intent(InvoiceStoreSelection.this,DetailReportSummaryActivityForAll.class);
					startActivity(i);
					finish();
				}
				else if(rb_dsrReport.isChecked())
				{
					if(!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSM") && !SelectedDSRValue.equals("No DSM") )
					{

						String DSRNodeIdAndNodeType= dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
						int tempSalesmanNodeId=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
						int tempSalesmanNodeType=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
						shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);
						flgDataScopeSharedPref(2);
						Intent i = new Intent(InvoiceStoreSelection.this, DetailReportSummaryActivityForAll.class);
						startActivity(i);
						finish();
					}
					else
					{
						showAlertForEveryOne("Please select DSM to Proceed.");
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

						Intent i = new Intent(InvoiceStoreSelection.this, DetailReportSummaryActivityForAll.class);
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
					showAlertForEveryOne("Please select atleast one option to Proceed.");
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
					ArrayAdapter adapterCategory=new ArrayAdapter(InvoiceStoreSelection.this, android.R.layout.simple_spinner_item,drsNames);
					adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_dsrVisit.setAdapter(adapterCategory);
					spinner_dsrVisit.setVisibility(View.VISIBLE);

					spinner_dsrVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

					ArrayAdapter adapterCategory=new ArrayAdapter(InvoiceStoreSelection.this, android.R.layout.simple_spinner_item,DbrArray);
					adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_distrbtrScope.setAdapter(adapterCategory);
					spinner_distrbtrScope.setVisibility(View.VISIBLE);

					spinner_distrbtrScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(InvoiceStoreSelection.this);
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
					Intent i=new Intent(InvoiceStoreSelection.this,WebViewDSRDataReportActivity.class);
					startActivity(i);

				}
				else if(rb_onMap.isChecked())
				{
					Intent i = new Intent(InvoiceStoreSelection.this, WebViewDSRTrackerActivity.class);
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
}
