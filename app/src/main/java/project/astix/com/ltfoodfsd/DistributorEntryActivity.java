package project.astix.com.ltfoodfsd;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.astix.Common.CommonInfo;

public class DistributorEntryActivity extends BaseActivity
{
	SharedPreferences sharedPref;
	LinkedHashMap<String, String> hmapdsrIdAndDescr_details=new LinkedHashMap<String, String>();
	String[] drsNames;
	public String	SelectedDSRValue="";
	Dialog dialog;
	public int StockPcsCaseType=0;
	RadioButton 	RB_inpieces;
	RadioButton 	RB_InCases;
	LinearLayout lLayout_main, ll_forSearchBox,ll_forTableHeaderName;
	RelativeLayout relLayout_img;
	ImageView btn_search, imgVw_back;
	EditText edittext;
	TextView txt_stockDate,txt_stockEntry_Cases;
	LinearLayout LLparentOfInPcsCs;
	Button btn_save;
	Spinner spinner_for_filter;
	String DbrNodeId,DbrNodeType,DbrName;
	int DistribtrId_Global=0;
	int Sstat=3;
	LinearLayout llayout_dialog_parentOfRows;
	TextView oldStck_BtnGlobal;
	
	DBAdapterKenya dbengine = new DBAdapterKenya(DistributorEntryActivity.this);
	public int chkFlgForErrorToCloseApp = 0;
	String imei, fDate;
	int CstomrNodeType=0;
	
	public int DistributorNodeType_Global=0;
	TextView txt_mnth1, txt_mnth2, txt_mnth3, txt_mnth4, txt_mnth5;

	LinkedHashMap<String, String> HmapDistribtrOldStockData = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> HmapDistribtrReport = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> HmapSavedData = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> HmapGetPDAdata;
	LinkedHashMap<String, String> HmapGetPDAOldStockData;
	
	ArrayList<String> DistribtrReportColumnDesc;
	ArrayList<String> ListMnthNames;
	ArrayList<String> ArrayListMnth = new ArrayList<String>();
	ArrayList<String> DbrArray=new ArrayList<String>();
	String[] Distribtr_list;
	String productNodeType_global="";
	public String[] PName=null;
	
	 public ProgressDialog pDialog2STANDBY;
	 DatabaseAssistantDistributorEntry DA = new DatabaseAssistantDistributorEntry(this);
	 public String newfullFileName;
	 
	 SyncXMLfileData task2;
	 public String[] xmlForWeb = new String[1];
	 int serverResponseCode = 0;
		public int syncFLAG = 0;
		public ProgressDialog pDialogGetStores;
		AlertDialog ad;

	//report alert
	String[] Distribtr_list_alert;
	String DbrSelNodeId,DbrSelNodeType,DbrSelName;
	ArrayList<String> DbrSelArray=new ArrayList<String>();
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

	public void customHeader()
	{
		//TextView tv_heading=(TextView) findViewById(R.id.tv_heading);
		//tv_heading.setText("Distributor Stock Entry");
		//ImageView imgVw_next=(ImageView) findViewById(R.id.imgVw_next);
		//imgVw_next.setVisibility(View.GONE);


		ImageView img_side_popUp=(ImageView) findViewById(R.id.img_side_popUp);
		img_side_popUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				open_pop_up();
			}
		});
	}
	private void loadActivity() {
		{
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_distributor_entry);

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
			customHeader();
			try {
				getDSRDetail();
				//report alert
				getDistribtrList();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Intent i=getIntent();
			imei=i.getStringExtra("imei");
			fDate=i.getStringExtra("fDate");
			spinner_for_filter=(Spinner) findViewById(R.id.spinner_for_filter);



			relLayout_img=(RelativeLayout) findViewById(R.id.relLayout_img);
			relLayout_img.setTag("img");

			ll_forTableHeaderName=(LinearLayout) findViewById(R.id.ll_forTableHeaderName);
			ll_forSearchBox=(LinearLayout) findViewById(R.id.ll_forSearchBox);
			btn_search=(ImageView) findViewById(R.id.btn_search);
			edittext=(EditText) findViewById(R.id.edittext);
			lLayout_main=(LinearLayout) findViewById(R.id.lLayout_main);
			btn_save=(Button) findViewById(R.id.btn_save);

			imgVw_back=(ImageView) findViewById(R.id.img_back_Btn);
			imgVw_back.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					finish();
				}
			});





			txt_stockDate=(TextView) findViewById(R.id.txt_stockDate);
			txt_stockEntry_Cases=(TextView) findViewById(R.id.txt_stockEntry_Cases);
			LLparentOfInPcsCs=(LinearLayout) findViewById(R.id.LLparentOfInPcsCs);




			txt_mnth1=(TextView) findViewById(R.id.txt_mnth1);
			txt_mnth2=(TextView) findViewById(R.id.txt_mnth2);
			txt_mnth3=(TextView) findViewById(R.id.txt_mnth3);
			txt_mnth4=(TextView) findViewById(R.id.txt_mnth4);
			txt_mnth5=(TextView) findViewById(R.id.txt_mnth5);

			fnGetDistributorList();


			RB_inpieces=(RadioButton) findViewById(R.id.RB_inpieces);
			RB_InCases=(RadioButton) findViewById(R.id.RB_InCases);
			RB_inpieces.setButtonDrawable(getResources().getDrawable(R.drawable.radio_btn_bck));
			RB_inpieces.setChecked(true);
			RB_InCases.setButtonDrawable(getResources().getDrawable(R.drawable.radio_btn_bck));
			RB_InCases.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RB_InCases.setChecked(true);
					RB_inpieces.setChecked(false);
					txt_stockEntry_Cases.setText("Stock Entry(In Cases)");
					StockPcsCaseType=1;
				}
			});
			RB_inpieces.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RB_InCases.setChecked(false);
					RB_inpieces.setChecked(true);
					txt_stockEntry_Cases.setText("Stock Entry(In Pieces)");
					StockPcsCaseType=2;
				}
			});

			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(DistributorEntryActivity.this,android.R.layout.simple_spinner_item,DbrArray);
			//adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);





			btn_search.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					String text=edittext.getText().toString().trim();
					fnGetSavedDataFromPDA();
					fnSearchProduct(text);
				}
			});

			edittext.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
											  int after) {

				}

				@Override
				public void afterTextChanged(Editable s)
				{
					if(edittext.getText().toString().trim().equals(""))
					{
						for(int child=0;child < lLayout_main.getChildCount();child++)
						{
							View v=lLayout_main.getChildAt(child);
							if(v instanceof RelativeLayout)
							{
								v.setVisibility(View.GONE);
							}
							else
							{
								v.setVisibility(View.VISIBLE);
							}
						}
					}
				}
			});

			btn_save.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(isOnline())
					{
						Builder dialog=new Builder(DistributorEntryActivity.this);
						dialog.setTitle("Information");
						dialog.setMessage("Are you sure to submit Distributor Stock Entry. ");
						dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{



								try
								{
									saveDistributorStockInTable();
									FullSyncDataNow task = new FullSyncDataNow(DistributorEntryActivity.this);
									task.execute();
								}
								catch(Exception e)
								{

								}



							}
						});
						dialog.setNegativeButton("No", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						});

						AlertDialog alert=dialog.create();
						alert.show();
					}
					else
					{
						saveDistributorStockInTable();
						FullSyncDataNow task = new FullSyncDataNow(DistributorEntryActivity.this);
						task.execute();
						//showNoConnAlertforLocalDataSaved();
					}
				}
			});



			spinner_for_filter.setOnItemSelectedListener(new OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
				{

					TextView tv =(TextView) view;
					String text=tv.getText().toString();

					if(text.equals("Select Distributor"))
					{
						ll_forSearchBox.setVisibility(View.GONE);
						ll_forTableHeaderName.setVisibility(View.INVISIBLE);
						saveDistributorStockInTable();
						lLayout_main.removeAllViews();
						btn_save.setVisibility(View.INVISIBLE);
						txt_stockDate.setVisibility(View.INVISIBLE);
						LLparentOfInPcsCs.setVisibility(View.INVISIBLE);



					}
					else
					{

						txt_stockDate.setVisibility(View.INVISIBLE);
						LLparentOfInPcsCs.setVisibility(View.INVISIBLE);
						btn_save.setVisibility(View.VISIBLE);
						ll_forSearchBox.setVisibility(View.GONE);
						ll_forTableHeaderName.setVisibility(View.INVISIBLE);
						saveDistributorStockInTable();
						lLayout_main.removeAllViews();
						String   Distribtor_Detail=dbengine.fetchDistributorIdByName(text);

						int StrDistribtrId_Global=Integer.parseInt(Distribtor_Detail.split(Pattern.quote("^"))[0]);
						int StrDistributorNodeType_Global=Integer.parseInt(Distribtor_Detail.split(Pattern.quote("^"))[1]);

						DistribtrId_Global=StrDistribtrId_Global;
						DistributorNodeType_Global=StrDistributorNodeType_Global;


						int check=dbengine.countDataIntblDistributorSavedData(StrDistribtrId_Global,StrDistributorNodeType_Global,3);
						if(check==0)
						{
							if(isOnline())
							{
								dbengine.deleteDistributorStockTblesOnDistributorIDBasic(StrDistribtrId_Global,StrDistributorNodeType_Global);
								GetDistributorStockEntryData getData= new GetDistributorStockEntryData();
								getData.execute();
							}
							else
							{
								showNoConnAlert();
							}
						}
						else
						{

							//saveDistributorStockInTable();
							dbengine.open();
							HmapDistribtrReport = dbengine.fetchtblDistribtrReport(DistribtrId_Global,DistributorNodeType_Global);

							DistribtrReportColumnDesc = dbengine.fetchtblDistribtrReportColumnDesc(DistribtrId_Global,DistributorNodeType_Global);
							//System.out.println("SIZE 1:" + DistribtrReportColumnDesc.size());
							dbengine.close();
							fnForStaticDates();
							fnGetSavedDataFromPDA();
							ll_forSearchBox.setVisibility(View.GONE);
							ll_forTableHeaderName.setVisibility(View.VISIBLE);
							txt_stockDate.setVisibility(View.VISIBLE);
							//	LLparentOfInPcsCs.setVisibility(View.VISIBLE);
							fnToAddRows();
						}

						if(lLayout_main.getChildCount()>0)
						{
							btn_save.setVisibility(View.VISIBLE);
						}
						else
						{
							btn_save.setVisibility(View.INVISIBLE);
						}

					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

			if(lLayout_main.getChildCount()>0)
			{
				btn_save.setVisibility(View.VISIBLE);
			}
			else
			{
				btn_save.setVisibility(View.INVISIBLE);
			}

		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

			super.onCreate(savedInstanceState);
			loadActivity();
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
			//flgReMap=Integer.parseInt(value.split(Pattern.quote("^"))[3]);

			hmapDistrbtrList.put(DbrSelName,DbrSelNodeId+"^"+DbrSelNodeType);
			DbrSelArray.add(DbrSelName);
		}

	}


	public void saveDistributorStockInTable()
	{
		
		 try
		    {
			 if(lLayout_main.getChildCount()>0)
			 {
				
				 int boolCheckToDelete=0;
				 
				 for(Map.Entry<String, String> entry:HmapSavedData.entrySet())
					{
						String key=entry.getKey();
						String tag=key.split(Pattern.quote("^"))[0].toString().trim();
						//Tag=productId"+"MnthName
						String pID=tag.split(Pattern.quote("_"))[0];
						String Date=tag.split(Pattern.quote("_"))[1];
						int DistID=Integer.parseInt(tag.split(Pattern.quote("_"))[2]);
						int DistNodeType=Integer.parseInt(tag.split(Pattern.quote("_"))[3]);
						String header=key.split(Pattern.quote("^"))[1].toString().trim();
						String Short_name=key.split(Pattern.quote("^"))[2].toString().trim();
						int EntryType=Integer.parseInt(key.split(Pattern.quote("^"))[3].toString().trim());
						String Et_value=entry.getValue();
						
						String ProductNodeTypeStockDate=dbengine.fetchProductNodeTypeByID(Integer.parseInt(pID),DistID,DistNodeType);
						
						int ProductNodeType=Integer.parseInt(ProductNodeTypeStockDate.split(Pattern.quote("^"))[0].toString().trim());
						String StockDate=ProductNodeTypeStockDate.split(Pattern.quote("^"))[1].toString().trim();
						
						//int DistributorNodeType=dbengine.fetchDistributorNodeTypeByID(DistribtrId_Global);
						if(boolCheckToDelete==0)
						{
							dbengine.Delete_tblDistributorSavedData(DistID,DistNodeType);	
							fnSaveOldStockData(DistID,DistNodeType);
						}
						boolCheckToDelete++;
						dbengine.open();
						
						dbengine.savetblDistributorSavedData(header,Short_name,pID,Date, Et_value,DistID,DistNodeType,ProductNodeType,StockDate,Sstat,EntryType,StockPcsCaseType);
						//System.out.println("SAVED DATA :"+header+" "+Short_name+" "+pID+" "+Date+" "+Et_value+" "+DistribtrId_Global+" "+Sstat);
						
						dbengine.close();
					}
			 }
			
			 
			 //DistribtrId_Global=StrDistribtrId_Global;
			 //DistributorNodeType_Global=StrDistributorNodeType_Global;
			   // GetDistributorStockEntryData getData= new GetDistributorStockEntryData();
				//getData.execute();
				
		
		    }
		 
		 
			catch (Exception e) {
				// TODO Autouuid-generated catch block
				e.printStackTrace();
				//System.out.println("onGetStoresForDayCLICK: Exec(). EX: "+e);
			}
		
	}

	public void fnForStaticDates() 
	{
		for (int m = 0; m < DistribtrReportColumnDesc.size(); m++)
		{
			int count = DistribtrReportColumnDesc.size();
			if (count == 0)
			{
				txt_mnth1.setVisibility(View.GONE);
				txt_mnth2.setVisibility(View.GONE);
				txt_mnth3.setVisibility(View.GONE);
				txt_mnth4.setVisibility(View.GONE);
				txt_mnth5.setVisibility(View.GONE);
			}
			if (count == 1) 
			{
				txt_mnth2.setVisibility(View.GONE);
				txt_mnth3.setVisibility(View.GONE);
				txt_mnth4.setVisibility(View.GONE);
				txt_mnth5.setVisibility(View.GONE);
			}
			if (count == 2)
			{
				txt_mnth3.setVisibility(View.GONE);
				txt_mnth4.setVisibility(View.GONE);
				txt_mnth5.setVisibility(View.GONE);
			}
			if (count == 3)
			{
				txt_mnth4.setVisibility(View.GONE);
				txt_mnth5.setVisibility(View.GONE);
			}
			if (count == 4)
			{
				txt_mnth5.setVisibility(View.GONE);
			}
			if (count == 5) 
			{
			}

			String values = DistribtrReportColumnDesc.get(m);
			String ColumnName = values.split(Pattern.quote("^"))[0];
			String ColumnNameDesc = values.split(Pattern.quote("^"))[1];
			if (m == 0) 
			{
				txt_mnth1.setText(ColumnNameDesc);
			}
			if (m == 1) 
			{
				txt_mnth2.setText(ColumnNameDesc);
			}
			if (m == 2) 
			{
				txt_mnth3.setText(ColumnNameDesc);

			}
			if (m == 3) 
			{
				txt_mnth4.setText(ColumnNameDesc);

			}
			if (m == 4) 
			{
				txt_mnth5.setText(ColumnNameDesc);

			}
		}

	}

	public void fnSearchProduct(String item) 
	{
		int check = 0;
		for (int child = 0; child < lLayout_main.getChildCount(); child++) 
		{
			View v = lLayout_main.getChildAt(child);
			if (!v.getTag().toString().toUpperCase().trim().contains(item.toUpperCase().trim())) 
			{
				v.setVisibility(View.GONE);
			} 
			else if (v.getTag().toString().toUpperCase().trim().contains(item.toUpperCase().trim())) 
			{
				check = 1;
				v.setVisibility(View.VISIBLE);
			}
		}
		if (check == 0) 
		{
			relLayout_img.setVisibility(View.VISIBLE);
		}

	}

	public void fnToAddRows() 
	{
		 LinkedHashMap<String, String> HmapForDatesssss= dbengine.fetchtblDistribtrMnthDates(DistribtrId_Global,DistributorNodeType_Global);
		int count_for_etText_Visibility;
		HmapSavedData.clear();
		HmapDistribtrOldStockData.clear();
		HmapDistribtrOldStockData.putAll(HmapGetPDAOldStockData);
		
		dbengine.open();
		PName = dbengine.getDistinctProdctName(DistribtrId_Global,DistributorNodeType_Global);
		ListMnthNames = dbengine.fetchtblDistribtrReportColumnDesc(DistribtrId_Global,DistributorNodeType_Global);
		count_for_etText_Visibility = ListMnthNames.size();
		dbengine.close();

		for (int n = 0; n < ListMnthNames.size(); n++) 
		{
			String values = ListMnthNames.get(n);
			String ColumnName = values.split(Pattern.quote("^"))[0].toString().trim();
			String ColumnNameDesc = values.split(Pattern.quote("^"))[1].toString().trim();
			ArrayListMnth.add(n, ColumnNameDesc);
		}
		lLayout_main.removeAllViews();
		for (int i = 0; i < PName.length; i++)
		{
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.inflate_row_header, null);
			lLayout_main.addView(view);
			view.setTag(PName[i]);
			String pname=PName[i];

			LinearLayout ll_forRow = (LinearLayout) view.findViewById(R.id.llayout_for_row);
			TextView textView_PrdctHeader = (TextView) view.findViewById(R.id.textView_PrdctHeader);

			textView_PrdctHeader.setText(PName[i]);
			final String prdct_header = textView_PrdctHeader.getText().toString().trim();

			// ListPrdctNames.add(PName[i]);
			

			dbengine.open();
			String[] PIDAndShortName = dbengine.getPrdctIdAndSku(PName[i],DistribtrId_Global,DistributorNodeType_Global);
			dbengine.close();
			
			//ll_forRow.removeAllViews();
			for (int j = 0; j < PIDAndShortName.length; j++) 
			{
				String text = PIDAndShortName[j];
				final String PrdctId = text.split(Pattern.quote("^"))[0].toString().trim();
				final String SkuShortName = text.split(Pattern.quote("^"))[1].toString().trim();

				LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view1 = inflater1.inflate(R.layout.inflate_row, null);

				TextView textView_Shortname = (TextView) view1.findViewById(R.id.textView_Shortname);
				textView_Shortname.setText(SkuShortName);

				final EditText edittxt_mnth5 = (EditText) view1.findViewById(R.id.edittxt_mnth5);
				final EditText edittxt_mnth4 = (EditText) view1.findViewById(R.id.edittxt_mnth4);
				final EditText edittxt_mnth3 = (EditText) view1.findViewById(R.id.edittxt_mnth3);
				final EditText edittxt_mnth2 = (EditText) view1.findViewById(R.id.edittxt_mnth2);
				final EditText edittxt_mnth1 = (EditText) view1.findViewById(R.id.edittxt_mnth1);
				final EditText edittxt_freeStck = (EditText) view1.findViewById(R.id.edittxt_freeStck);
				final EditText edittxt_sampleStck = (EditText) view1.findViewById(R.id.edittxt_sampleStck);
				final TextView txt_forBtn_OldStck=(TextView) view1.findViewById(R.id.txt_forBtn_OldStck);
				
				
				edittxt_mnth5.setTag(PrdctId + "_" +  ArrayListMnth.get(4)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1");
				edittxt_mnth4.setTag(PrdctId + "_" +  ArrayListMnth.get(3)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1");
				edittxt_mnth3.setTag(PrdctId + "_" +  ArrayListMnth.get(2)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1");
				edittxt_mnth2.setTag(PrdctId + "_" +  ArrayListMnth.get(1)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1");
				edittxt_mnth1.setTag(PrdctId + "_" +  ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1");
				edittxt_freeStck.setTag(PrdctId + "_" +  ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_2");
				edittxt_sampleStck.setTag(PrdctId + "_" +  ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_3");
				
				/*edittxt_mnth1.setText("");
				edittxt_mnth2.setText("");
				edittxt_mnth3.setText("");
				edittxt_mnth4.setText("");
				edittxt_mnth5.setText("");
				edittxt_freeStck.setText("");
				edittxt_sampleStck.setText("");*/

				if (!HmapGetPDAdata.isEmpty() && HmapGetPDAdata != null) 
				{
					if (HmapGetPDAdata.containsKey(PrdctId + "_"+ ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1")) 
					{
						edittxt_mnth1.setText(HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
						HmapSavedData.put(edittxt_mnth1.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^1",HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
					}
					if (HmapGetPDAdata.containsKey(PrdctId + "_" + ArrayListMnth.get(1)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"))
					{
						edittxt_mnth2.setText(HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(1)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
						HmapSavedData.put(edittxt_mnth2.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^1",HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(1)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
					}
					if (HmapGetPDAdata.containsKey(PrdctId + "_"+ ArrayListMnth.get(2)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1")) {
						edittxt_mnth3.setText(HmapGetPDAdata.get(PrdctId + "_"+ ArrayListMnth.get(2)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
						HmapSavedData.put(edittxt_mnth3.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^1",HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(2)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
					}
					if (HmapGetPDAdata.containsKey(PrdctId + "_"+ ArrayListMnth.get(3)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1")) {
						edittxt_mnth4.setText(HmapGetPDAdata.get(PrdctId + "_"+ ArrayListMnth.get(3)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
						HmapSavedData.put(edittxt_mnth4.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^1",HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(3)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
					}
					if (HmapGetPDAdata.containsKey(PrdctId + "_"+ ArrayListMnth.get(4)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1")) {
						edittxt_mnth5.setText(HmapGetPDAdata.get(PrdctId + "_"+ ArrayListMnth.get(4)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
						HmapSavedData.put(edittxt_mnth5.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^1",HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(4)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1"));
					}
					if (HmapGetPDAdata.containsKey(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_2")) {
						edittxt_freeStck.setText(HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_2"));
						HmapSavedData.put(edittxt_freeStck.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^2",HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_2"));
					}
					if (HmapGetPDAdata.containsKey(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_3")) {
						edittxt_sampleStck.setText(HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_3"));
						HmapSavedData.put(edittxt_sampleStck.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^3",HmapGetPDAdata.get(PrdctId + "_" + ArrayListMnth.get(0)+ "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_3"));
					}
					
				}
				
				txt_forBtn_OldStck.setTag(SkuShortName+"_"+pname+"_"+PrdctId + "_"+DistribtrId_Global+ "_"+DistributorNodeType_Global+"_1");
				putDataToHashmapOfpopup(txt_forBtn_OldStck.getTag().toString().trim(),HmapForDatesssss);
				if( HmapDistribtrOldStockData != null)
				{
					if(HmapDistribtrOldStockData.containsKey(txt_forBtn_OldStck.getTag().toString().trim()))
					{
						
						txt_forBtn_OldStck.setText(HmapDistribtrOldStockData.get(txt_forBtn_OldStck.getTag().toString().trim()));
					}
				}
				
				SpannableString content2 = new SpannableString(txt_forBtn_OldStck.getText().toString());
				 content2.setSpan(new UnderlineSpan(), 0, String.valueOf(txt_forBtn_OldStck.getText().toString()).length(), 0);
				 txt_forBtn_OldStck.setText(content2);
				 
				txt_forBtn_OldStck.setOnClickListener(new OnClickListener()
				{	
					@Override
					public void onClick(View v) 
					{
						//v.getTag().toString().trim();
						oldStck_BtnGlobal=(TextView) v;
						fnOpenAlertForOldStock(v.getTag().toString().trim());
					}
				});

				final String mnth_date5 = ArrayListMnth.get(4);
				
				edittxt_mnth5.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void onTextChanged(CharSequence s, int start,int before, int count)
					{
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,int count, int after)
					{
					}

					@Override
					public void afterTextChanged(Editable s) 
					{
						String edit5 = edittxt_mnth5.getText().toString().trim();
						HmapSavedData.put(edittxt_mnth5.getTag().toString().trim()+ "^" + prdct_header + "^" + SkuShortName+ "^1",edit5);
					}
				});

				final String mnth_date4 = ArrayListMnth.get(3);
				
				edittxt_mnth4.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						String edit4 = edittxt_mnth4.getText().toString()
								.trim();
						HmapSavedData.put(edittxt_mnth4.getTag().toString()
								.trim()
								+ "^" + prdct_header + "^" + SkuShortName+ "^1",
								edit4);
					}
				});

				final String mnth_date3 = ArrayListMnth.get(2);
				
				edittxt_mnth3.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						String edit3 = edittxt_mnth3.getText().toString()
								.trim();
						HmapSavedData.put(edittxt_mnth3.getTag().toString()
								.trim()
								+ "^" + prdct_header + "^" + SkuShortName+ "^1",
								edit3);
					}
				});

				final String mnth_date2 = ArrayListMnth.get(1);
				
				edittxt_mnth2.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						String edit2 = edittxt_mnth2.getText().toString()
								.trim();
						HmapSavedData.put(edittxt_mnth2.getTag().toString()
								.trim()
								+ "^" + prdct_header + "^" + SkuShortName+ "^1",
								edit2);
					}
				});

				final String mnth_date1 = ArrayListMnth.get(0);
				
				edittxt_mnth1.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						String edit1 = edittxt_mnth1.getText().toString()
								.trim();
						HmapSavedData.put(edittxt_mnth1.getTag().toString()
								.trim()
								+ "^" + prdct_header + "^" + SkuShortName+ "^1",
								edit1);

					}
				});

				
				edittxt_freeStck.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						String edit_freeStck = edittxt_freeStck.getText()
								.toString().trim();
						HmapSavedData.put(edittxt_freeStck.getTag().toString()
								.trim()
								+ "^" + prdct_header + "^" + SkuShortName+ "^2",
								edit_freeStck);
					}
				});

				
				edittxt_sampleStck.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						String edit_sampleStck = edittxt_sampleStck.getText()
								.toString().trim();
						HmapSavedData.put(edittxt_sampleStck.getTag()
								.toString().trim()
								+ "^" + prdct_header + "^" + SkuShortName+ "^3",
								edit_sampleStck);

					}
				});

				if (count_for_etText_Visibility == 0) {
					edittxt_mnth1.setVisibility(View.GONE);
					edittxt_mnth2.setVisibility(View.GONE);
					edittxt_mnth3.setVisibility(View.GONE);
					edittxt_mnth4.setVisibility(View.GONE);
					edittxt_mnth5.setVisibility(View.GONE);
				}
				if (count_for_etText_Visibility == 1) {
					edittxt_mnth2.setVisibility(View.GONE);
					edittxt_mnth3.setVisibility(View.GONE);
					edittxt_mnth4.setVisibility(View.GONE);
					edittxt_mnth5.setVisibility(View.GONE);
				}
				if (count_for_etText_Visibility == 2) {
					edittxt_mnth3.setVisibility(View.GONE);
					edittxt_mnth4.setVisibility(View.GONE);
					edittxt_mnth5.setVisibility(View.GONE);
				}
				if (count_for_etText_Visibility == 3) {
					edittxt_mnth4.setVisibility(View.GONE);
					edittxt_mnth5.setVisibility(View.GONE);
				}
				if (count_for_etText_Visibility == 4) {
					edittxt_mnth5.setVisibility(View.GONE);
				}
				if (count_for_etText_Visibility == 5) {
				}
				ll_forRow.addView(view1);
			}// row loop
		}// main for loop
	}

	public void fnGetSavedDataFromPDA() 
	{
		//HmapGetPDAdata.clear();
		HmapGetPDAOldStockData= dbengine.fetchtblDistribtrOldStockData(DistribtrId_Global,DistributorNodeType_Global);
		
		HmapGetPDAdata = dbengine.fetchtblDistribtrSavedData(DistribtrId_Global,DistributorNodeType_Global);
		StockPcsCaseType=dbengine.fnGetDistributorStockPcsCaseType(DistribtrId_Global,DistributorNodeType_Global);
		if(StockPcsCaseType!=0)
		{
			RB_inpieces.setEnabled(false);
			RB_InCases.setEnabled(false);
			if(StockPcsCaseType==1)
			{
				txt_stockEntry_Cases.setText("Stock Entry(In Cases)");
				RB_InCases.setChecked(true);
				RB_inpieces.setChecked(false);
			}
			if(StockPcsCaseType==2)
			{
				txt_stockEntry_Cases.setText("Stock Entry(In Pieces)");
				RB_inpieces.setChecked(true);
				RB_InCases.setChecked(false);
			}
		}
		if(StockPcsCaseType==0)
		{
			RB_inpieces.setEnabled(true);
			RB_InCases.setEnabled(true);
			RB_inpieces.setChecked(true);
			RB_InCases.setChecked(false);
			StockPcsCaseType=2;
		}
		
	 	
	 	
		for (Map.Entry<String, String> entry : HmapGetPDAdata.entrySet()) 
		{
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println("RETRIEVE DATA :" + key + "  " + value);
		}
		
		if(HmapGetPDAdata.size() >0)
		{
			String stockDate= dbengine.getDistinctStockDate();
		    txt_stockDate.setText("Stock as on- "+stockDate);
		    
		    
		}
	
	}
	
	public void fnSaveOldStockData(int DistID,int DistNodeType)
	{
		dbengine.open();
		//HmapDistribtrOldStockData = dbengine.fetchtblDistribtrOldStockData();
		for (Map.Entry<String, String> entry : HmapDistribtrOldStockData.entrySet()) 
		{
			String key = entry.getKey();
			String value = entry.getValue();
			
			dbengine.savetblDistributorOldStockData(DistID,DistNodeType,key,value);
		}
		dbengine.close();
	}
	public void putDataToHashmapOfpopup(String tagOfButton ,LinkedHashMap<String, String> hmapforddd) {
		 final String FlvShortName=tagOfButton.split(Pattern.quote("_"))[0];
	     final String Product_name=tagOfButton.split(Pattern.quote("_"))[1];	     
	     String Product_Id=tagOfButton.split(Pattern.quote("_"))[2];
	     String Distributor_Id=tagOfButton.split(Pattern.quote("_"))[3];
	     String Distributor_NodeType=tagOfButton.split(Pattern.quote("_"))[4];
		// TODO Auto-generated method stub

	    
	     if(!hmapforddd.isEmpty() && hmapforddd != null)
	     {
	    	 for(Map.Entry<String, String> entry: hmapforddd.entrySet())
		     {
	    		 String EntryType="1";
	 	 		if(entry.getKey().equals("01-01-1900"))
	 	 		{
	 	 			EntryType="4";
	 	 		}
	    	 if (HmapGetPDAdata.containsKey(Product_Id + "_"+ entry.getKey()+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryType)) 
				{
	    		 HmapSavedData.put(Product_Id + "_" + entry.getKey()+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryType+ "^" + Product_name + "^" + FlvShortName+ "^"+EntryType,HmapGetPDAdata.get(Product_Id + "_" + entry.getKey()+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryType));
	    		 
	    		 
				}}
		    
	     }
	}
	
	public void fnGetDistributorList()
	{
		dbengine.open();
		Distribtr_list=dbengine.getDistributorData();
		dbengine.close();
		for(int i=0;i<Distribtr_list.length;i++)
		{
			//System.out.println("DISTRIBUTOR........"+Distribtr_list[i]);
			String value=Distribtr_list[i];
			DbrNodeId=value.split(Pattern.quote("^"))[0];
			DbrNodeType=value.split(Pattern.quote("^"))[1];
			DbrName=value.split(Pattern.quote("^"))[2];
			DbrArray.add(DbrName);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(DistributorEntryActivity.this,R.layout.initial_spinner_text,DbrArray);
		adapter.setDropDownViewResource(R.layout.spina);
		
	    spinner_for_filter.setAdapter(adapter);
		if(!DbrArray.isEmpty())
		{
				if(DbrArray.size() == 2)
				{
					
					
					DistribtrId_Global=Integer.parseInt(Distribtr_list[1].split(Pattern.quote("^"))[0]);
					DistributorNodeType_Global=Integer.parseInt(Distribtr_list[1].split(Pattern.quote("^"))[1]);
					
					int check=dbengine.countDataIntblDistributorSavedData(DistribtrId_Global,DistributorNodeType_Global,3); 
					if(check==0)
					{
						if(isOnline())
						 {
							dbengine.deleteDistributorStockTblesOnDistributorIDBasic(DistribtrId_Global,DistributorNodeType_Global);
						 spinner_for_filter.setSelection(1);
						   // GetDistributorStockEntryData getData= new GetDistributorStockEntryData();
						    //getData.execute();
						 }
						else
						{
							 spinner_for_filter.setSelection(0);
							showNoConnAlert();
						}
					}
					else
					{
						 spinner_for_filter.setSelection(1);
						fnGetSavedDataFromPDA();
						ll_forSearchBox.setVisibility(View.GONE);
						ll_forTableHeaderName.setVisibility(View.VISIBLE);
						fnToAddRows();
					}
					
					
					
				}
				
		}
	}
	
	public void fnOpenAlertForOldStock(String tagForBtn)
	{
		//String addValue_Global=0;
		final Dialog dialog = new Dialog(DistributorEntryActivity.this);
	     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
	  //dialog.setTitle("Calculation");
	     dialog.setCancelable(false);
	     dialog.setContentView(R.layout.popup_layout);
	     
	     final String FlvShortName=tagForBtn.split(Pattern.quote("_"))[0];
	     final String Product_name=tagForBtn.split(Pattern.quote("_"))[1];	     
	     final String Product_Id=tagForBtn.split(Pattern.quote("_"))[2];
	     final String Distributor_Id=tagForBtn.split(Pattern.quote("_"))[3];
	     final String Distributor_NodeType=tagForBtn.split(Pattern.quote("_"))[4];
	      
	     TextView textView1=(TextView) dialog.findViewById(R.id.textView1);
	     textView1.setText(Product_name+" -"+FlvShortName);
	    
	     llayout_dialog_parentOfRows=(LinearLayout) dialog.findViewById(R.id.llayout_dialog_row);
	     
	     LinkedHashMap<String, String> HmapForDates= dbengine.fetchtblDistribtrMnthDates(Integer.parseInt(Distributor_Id),Integer.parseInt(Distributor_NodeType));
	     
	     if(!HmapForDates.isEmpty() && HmapForDates != null)
	     {
		     for(Map.Entry<String, String> entry: HmapForDates.entrySet())
		     {
		    	 LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 		 View view_rows = inflater1.inflate(R.layout.inflate_dialog_row, null);
		 		 llayout_dialog_parentOfRows.addView(view_rows);
		 		 
		 		final String mnth_Date=entry.getKey().toString().trim();
		 		TextView txtView_dialog_mnthName=(TextView) view_rows.findViewById(R.id.txtView_dialog_mnthName);
		 		
		 		
		 		final EditText editTxt_dialog_mnthData=(EditText) view_rows.findViewById(R.id.editTxt_dialog_mnthData);
		 		txtView_dialog_mnthName.setText(mnth_Date);
		 		 String EntryType="1";
		 		if(mnth_Date.equals("01-01-1900"))
		 		{
		 			EntryType="4";
		 			txtView_dialog_mnthName.setText("Older Stock");
		 		}
		 		
		 		
		 		editTxt_dialog_mnthData.setTag(Product_Id + "_" + mnth_Date+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryType);
		 		
		 		
		 		if (!HmapSavedData.isEmpty() && HmapSavedData != null) 
				{
					if (HmapSavedData.containsKey(editTxt_dialog_mnthData.getTag().toString().trim()+ "^" + Product_name + "^" + FlvShortName+ "^"+EntryType)) 
					{
						editTxt_dialog_mnthData.setText(HmapSavedData.get(editTxt_dialog_mnthData.getTag().toString().trim()+ "^" + Product_name + "^" + FlvShortName+ "^"+EntryType));
					}
				}
		 		
		 		if (!HmapGetPDAdata.isEmpty() && HmapGetPDAdata != null) 
				{
					if (HmapGetPDAdata.containsKey(Product_Id + "_"+ mnth_Date+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryType)) 
					{
						
						editTxt_dialog_mnthData.setText(HmapGetPDAdata.get(Product_Id + "_" + mnth_Date+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryType));
						HmapSavedData.put(editTxt_dialog_mnthData.getTag().toString().trim()+ "^" + Product_name + "^" + FlvShortName+ "^"+EntryType,HmapGetPDAdata.get(Product_Id + "_" + mnth_Date+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryType));
					}
				}
		 		
		 		editTxt_dialog_mnthData.addTextChangedListener(new TextWatcher() 
		 		{	
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
				
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						
					}
					
					@Override
					public void afterTextChanged(Editable s) 
					{
						String edit_dialog = editTxt_dialog_mnthData.getText().toString().trim();
						String EntryTypeBox=editTxt_dialog_mnthData.getTag().toString().trim().split(Pattern.quote("_"))[4];
						HmapSavedData.put(editTxt_dialog_mnthData.getTag().toString().trim()+ "^" + Product_name + "^" + FlvShortName+ "^"+EntryTypeBox,edit_dialog);
						HmapGetPDAdata.put(Product_Id + "_"+ mnth_Date+ "_"+Distributor_Id+ "_"+Distributor_NodeType+"_"+EntryTypeBox, edit_dialog);
					}
				});
		 		
		     }
	     }
	     Button button1=(Button) dialog.findViewById(R.id.button1);
	     button1.setOnClickListener(new OnClickListener()
	     {	
			@Override
			public void onClick(View v) 
			{
				int oldStck_global=0;
				if(llayout_dialog_parentOfRows != null)
				{
					if(llayout_dialog_parentOfRows.getChildCount() > 0)
					{
						for(int i=0;i<llayout_dialog_parentOfRows.getChildCount();i++)
						{
							View view1=llayout_dialog_parentOfRows.getChildAt(i);
							if(view1 instanceof LinearLayout)
							{
								LinearLayout ll_main=(LinearLayout) view1;
								for(int j=0;j<ll_main.getChildCount();j++)
								{
									View view2=ll_main.getChildAt(j);
									
									if(view2 instanceof EditText)
									{
										EditText et=(EditText) view2;
										String oldStck_value=et.getText().toString().trim();
										if(!oldStck_value.equals(""))
										{
											oldStck_global= oldStck_global + Integer.parseInt(oldStck_value);
										}
									}
								}
							}
							
						}
						//oldStck_BtnGlobal.setText(oldStck_global);
						
						   SpannableString content1 = new SpannableString(String.valueOf(oldStck_global));
					       content1.setSpan(new UnderlineSpan(), 0, String.valueOf(oldStck_global).length(), 0);
					       oldStck_BtnGlobal.setText(content1);
					      /* if(oldStck_global==0)
					       {
					    	   SpannableString content3 = new SpannableString("Old STK");
					    	   content3.setSpan(new UnderlineSpan(), 0, "Old STK".length(), 0);
						       oldStck_BtnGlobal.setText(content3);
					    	   
					       }*/
					       
					       HmapDistribtrOldStockData.put(oldStck_BtnGlobal.getTag().toString().trim(),String.valueOf(oldStck_global));
					       
					}
				}
				dialog.dismiss();
			}
		});
	     
	     
	     Button btncncle=(Button) dialog.findViewById(R.id.btncncle);
	     btncncle.setVisibility(View.GONE);
	     btncncle.setOnClickListener(new OnClickListener()
	     {	
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	     
	     dialog.show();
	     
		/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistributorEntryActivity.this);
        //declare layout inflater
        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewStoreLocDetail=inflater.inflate(R.layout.popup_layout,null);

		LinearLayout llayout_dialog_row=(LinearLayout) viewStoreLocDetail.findViewById(R.id.llayout_dialog_row);
		Button btn_dialog_ok=(Button) viewStoreLocDetail.findViewById(R.id.btn_dialog_ok);
		
		btn_dialog_ok.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				alertDi
			}
		});
		
		LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view_rows = inflater1.inflate(R.layout.inflate_dialog_row, null);
		llayout_dialog_row.addView(view_rows);
		
        alertDialog.setView(viewStoreLocDetail);

		AlertDialog alert=alertDialog.show();
*/	}
	
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
			Builder alertDialogNoConn = new Builder(DistributorEntryActivity.this);
			alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
			alertDialogNoConn.setMessage(R.string.genTermNoDataConnectionFullMsg);
			alertDialogNoConn.setNeutralButton(R.string.txtOk,
					new DialogInterface.OnClickListener() 
			          {
							public void onClick(DialogInterface dialog, int which) 
							{
		                        dialog.dismiss();
		                        //finish();
		                    }
					  });
			alertDialogNoConn.setIcon(R.drawable.error_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();
			
		}
	 
	 
	 public void showNoConnAlertforLocalDataSaved() 
	 {
			Builder alertDialogNoConn = new Builder(DistributorEntryActivity.this);
			alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
			alertDialogNoConn.setMessage(R.string.genlocaldataMsg);
			alertDialogNoConn.setNeutralButton(R.string.txtOk,
					new DialogInterface.OnClickListener() 
			          {
							public void onClick(DialogInterface dialog, int which) 
							{
		                        dialog.dismiss();
		                        Intent i=new Intent(DistributorEntryActivity.this,LauncherActivity.class);
		        				i.putExtra("imei", imei);
		        				
		        				startActivity(i);
		        				finish();
		                    }
					  });
			alertDialogNoConn.setIcon(R.drawable.error_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();
			
		}

	public class GetDistributorStockEntryData extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog pDialogGetStores = new ProgressDialog(DistributorEntryActivity.this);

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			pDialogGetStores.setMessage("Loading data..");
			pDialogGetStores.setIndeterminate(false);
			pDialogGetStores.setCancelable(false);
			pDialogGetStores.setCanceledOnTouchOutside(false);
			pDialogGetStores.show();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			try 
			{
				int DatabaseVersion = dbengine.DATABASE_VERSION;
				int ApplicationID = dbengine.Application_TypeID;
				ServiceWorker newservice = new ServiceWorker();

				newservice = newservice.fnGetDistributorTodaysStock(getApplicationContext(), DistribtrId_Global, DistributorNodeType_Global,fDate, imei, fDate, ApplicationID);
				if (!newservice.director.toString().trim().equals("1")) 
				{
					if (chkFlgForErrorToCloseApp == 0)
					{
						chkFlgForErrorToCloseApp = 1;
					}
				}
			} catch (Exception e) {
			}

			finally {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (chkFlgForErrorToCloseApp == 1) 
			{
				chkFlgForErrorToCloseApp = 0;
				try 
				{
					if (pDialogGetStores.isShowing())
					{
						pDialogGetStores.dismiss();
					}
				} 
				catch (Exception e) 
				{

				}
			}
			else
			{
				
				try 
				{
					if (pDialogGetStores.isShowing())
					{
						pDialogGetStores.dismiss();
					}
				} 
				catch (Exception e) 
				{

				}
				dbengine.open();
				HmapDistribtrReport = dbengine.fetchtblDistribtrReport(DistribtrId_Global,DistributorNodeType_Global);
				
				DistribtrReportColumnDesc = dbengine.fetchtblDistribtrReportColumnDesc(DistribtrId_Global,DistributorNodeType_Global);
				//System.out.println("SIZE 1:" + DistribtrReportColumnDesc.size());
				dbengine.close();

				fnForStaticDates();
				
				/*int flg=dbengine.CheckIfSavedDataExist(3);
				if(flg == 1)
				{
					FullSyncDataNow task = new FullSyncDataNow(DistributorEntryActivity.this);
					 task.execute();
				}*/
				
				////fnGetSavedDataFromPDA();
				
				//fnGetDistributorList();
				fnGetSavedDataFromPDA();
				ll_forSearchBox.setVisibility(View.GONE);
				ll_forTableHeaderName.setVisibility(View.VISIBLE);
				txt_stockDate.setVisibility(View.VISIBLE);
			//	LLparentOfInPcsCs.setVisibility(View.VISIBLE);
				
				fnToAddRows();
				
				String stockDate= dbengine.getDistinctStockDate();
			    txt_stockDate.setText("Stock as on- "+stockDate);
			    
			    if(lLayout_main.getChildCount()>0)
				 {
					 btn_save.setVisibility(View.VISIBLE);
				 }
				 else
				 {
					 btn_save.setVisibility(View.INVISIBLE); 
				 }
				
			}
		}
	}// async closed
	
	
	private class FullSyncDataNow extends AsyncTask<Void, Void, Void> 
	{
		

		ProgressDialog pDialogGetStores;
		public FullSyncDataNow(DistributorEntryActivity activity) 
		{
			if(pDialog2STANDBY!=null)
			{
			 if(pDialog2STANDBY.isShowing())
				{
					pDialog2STANDBY.dismiss();
				}
			}
			pDialogGetStores = new ProgressDialog(activity);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			
			pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
			pDialogGetStores.setMessage("Submitting Distributor Entry Details...");
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
				
				
				
			dbengine.open();
			String presentRoute=dbengine.GetActiveRouteIDForDistributor();
			dbengine.close();
			
			
		
			SimpleDateFormat df1 = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
			
			newfullFileName=imei+"."+presentRoute+"."+ df1.format(dateobj);
			
			
			 
			
			try {
				
				
				 File MeijiDistributorEntryXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.DistributorXMLFolder);
					
				 if (!MeijiDistributorEntryXMLFolder.exists()) 
					{
					 MeijiDistributorEntryXMLFolder.mkdirs();
						 
					} 
				 
				
				 int checkNoFiles=dbengine.counttblDistributorSavedData();
				 if(checkNoFiles==1)
				 {
				    String routeID=dbengine.GetActiveRouteIDSunil();
				    DA.open();
				    DA.export(CommonInfo.DATABASE_NAME, newfullFileName,routeID);
				    DA.close();
					 /*dbengine.open();
					 dbengine.savetbl_XMLfiles(newfullFileName, "3","3");
					 dbengine.close();*/
				 }
				
				dbengine.open();
				dbengine.updateDistributorSstat();
				dbengine.close();
				
	          
			} catch (Exception e) {
			
				e.printStackTrace();
				if(pDialogGetStores.isShowing()) 
			      {
			    	   pDialogGetStores.dismiss();
				  }
			}
			return null;
		}

		@Override
		protected void onCancelled() 
		{
			
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			if(pDialogGetStores.isShowing()) 
		      {
		    	   pDialogGetStores.dismiss();
			  }
			if(isOnline())
			{
				try
				{
				 
				 task2 = new SyncXMLfileData(DistributorEntryActivity.this);
	 			 task2.execute();
				}
				catch(Exception e)
				{
					
				}
			}
			else
			{
				try
				{
					int checkNoFiles=dbengine.counttblDistributorSavedData();
					 if(checkNoFiles==1)
					 {
						 showNoConnAlertforLocalDataSaved();
					 }
					 else
					 {
						 Intent i=new Intent(DistributorEntryActivity.this,AllButtonActivity.class);
	     				i.putExtra("imei", imei);
	     				
	     				startActivity(i);
	     				finish();
					 }
				}
				catch(Exception e)
				{
					
				}
				
			}
			
			
			
		}
	}
	
	private class SyncXMLfileData extends AsyncTask<Void, Void, Integer> {


		public SyncXMLfileData(DistributorEntryActivity activity) {
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

			pDialogGetStores.setMessage("Submitting Distributor Details...");

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


			if (AllFilesName.length > 0) {
				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


				for (int vdo = 0; vdo < AllFilesName.length; vdo++) {
					String fileUri = AllFilesName[vdo];


					System.out.println("Sunil Again each file Name :" + fileUri);

					if (fileUri.contains(".zip")) {
						File file = new File(fileUri);
						file.delete();
					} else {
						//String f1 = Environment.getExternalStorageDirectory().getPath() + "/LTACEDistStockXml/" + fileUri;
						String f1 = Environment.getExternalStorageDirectory() + "/"+CommonInfo.DistributorXMLFolder+"/"+fileUri;

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
			//Log.i("SyncMasterForDistributor", "Sync Cancelled");
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

			//Intent i=new Intent(LauncherActivity.this,DistributorEntryActivity.class);
			dbengine.deleteDistributorStockTblesOnDistributorIDBasic(DistribtrId_Global, DistributorNodeType_Global);


			// Intent i=new Intent(DistributorEntryActivity.this,LauncherActivity.class);
			//i.putExtra("imei", imei);

			//startActivity(i);
			//finish();
			if (isOnline()) {
				AlertDialog.Builder alertDialogSyncOK = new AlertDialog.Builder(DistributorEntryActivity.this);
				alertDialogSyncOK.setTitle("Information");
				alertDialogSyncOK.setCancelable(false);


				alertDialogSyncOK.setMessage("Distributor Data Submit Successfully.");

				alertDialogSyncOK.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {


						Intent intent=new Intent(DistributorEntryActivity.this,AllButtonActivity.class);
						startActivity(intent);
						finish();


					}
				});
				alertDialogSyncOK.setIcon(R.drawable.info_ico);

				AlertDialog alert = alertDialogSyncOK.create();
				alert.show();

			} else {
				showNoConnAlert();
			}


		}
	}
	
	
	public  int upLoad2ServerXmlFiles(String sourceFileUri,String fileUri) 
	{
		 
		fileUri=fileUri.replace(".xml", "");
		
		     String fileName = fileUri;
		     String zipFileName=fileUri;
		     
		    // String newzipfile = Environment.getExternalStorageDirectory() + "/LTACEDistStockXml/" + fileName + ".zip";

		String newzipfile = Environment.getExternalStorageDirectory() + "/"+CommonInfo.DistributorXMLFolder+"/" + fileName + ".zip";

		sourceFileUri=newzipfile;
		    	
		    // xmlForWeb[0] = Environment.getExternalStorageDirectory() + "/LTACEDistStockXml/" + fileName + ".xml";
		xmlForWeb[0]=         Environment.getExternalStorageDirectory() + "/"+CommonInfo.DistributorXMLFolder+"/" + fileName + ".xml";

		try
				 {
					zip(xmlForWeb,newzipfile);
				 }
				 catch (Exception e1) 
				 {
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
			        
			        String urlString = CommonInfo.DistributorSyncPath.trim()+"?CLIENTFILENAME=" + zipFileName;
			        
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
		                 
		               while (bytesRead > 0) 
		               {
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
		               
		               if(serverResponseCode == 200)
		               {
						  /* dbengine.open();
						   dbengine.upDateXMLFileFlag(fileUri, 4);
						   dbengine.close();*/
						   
						   		//new File(dir, fileUri).delete();
		            	   syncFLAG=1;
		            	   
		            	   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
		            	   Editor editor = pref.edit();
		            	  // editor.remove(xmlForWeb[0]);
		            	   editor.putString(fileUri, ""+4); 
		            	   editor.commit();
		            	   
		            	   String FileSyncFlag=pref.getString(fileUri, ""+1); 
		            	   
						   		delXML(xmlForWeb[0].toString());
						   		/*dbengine.open();
					            dbengine.deleteXMLFileRow(fileUri);
					            dbengine.close();*/
					           
					   } 
		               else
		               {
		            	   syncFLAG=0;
		               }
		               
		               //close the streams //
		               fileInputStream.close();
		               dos.flush();
		               dos.close();
		                
		          } catch (MalformedURLException ex) 
		          {
		        	  ex.printStackTrace();
		          } catch (Exception e)
		          {
		        	 e.printStackTrace();
		           }
						
			   
		
		 
			  return serverResponseCode;  

			 }
	
	public void delXML(String delPath)
	{
		File file = new File(delPath);
	    file.delete();
	    File file1 = new File(delPath.toString().replace(".xml", ".zip"));
	    file1.delete();
	}
	
	public static void zip(String[] files, String zipFile) throws IOException
	{
	    BufferedInputStream origin = null;
	    final int BUFFER_SIZE = 2048;

	    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
	    try
	    { 
	        byte data[] = new byte[BUFFER_SIZE];

	        for (int i = 0; i < files.length; i++) 
	          {
	            FileInputStream fi = new FileInputStream(files[i]);    
	            origin = new BufferedInputStream(fi, BUFFER_SIZE);
	            try 
	              {
		                ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
		                out.putNextEntry(entry);
		                int count;
		                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) 
		                {
		                    out.write(data, 0, count);
		                }
	              }
	            finally 
	            {
	                origin.close();
	            }
	        }
	    }
	    finally 
	    {
	        out.close();
	    }
	}
	
	public static String[] checkNumberOfFiles(File dir) 
	  {
		 int NoOfFiles=0;
		 String [] Totalfiles = null;
		
	        if (dir.isDirectory()) 
	        {
	            String[] children = dir.list();
	            NoOfFiles=children.length;
	            Totalfiles=new String[children.length];
	            
	            for (int i=0; i<children.length; i++) 
	            {
	            	Totalfiles[i]=children[i];
	            }
	        }
	        return Totalfiles;
	    }
	protected void open_pop_up()
	{
		dialog = new Dialog(DistributorEntryActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.selection_header_custom);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
		WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
		parms.gravity = Gravity.TOP | Gravity.LEFT;
		parms.height=parms.MATCH_PARENT;
		parms.dimAmount = (float) 0.5;

		final Button btnDSRTrack = (Button) dialog.findViewById(R.id.btnDSRTrack);
		btnDSRTrack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				/*Intent intent=new Intent(DistributorEntryActivity.this,WebViewDSRTrackerActivity.class);
				startActivity(intent);*/
				openDSRTrackerAlert();
			}
		});

		final Button btnExecution = (Button) dialog.findViewById(R.id.btnExecution);
		btnExecution.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				GetInvoiceForDay task = new GetInvoiceForDay(DistributorEntryActivity.this);
				task.execute();
			}
		});

		final   Button butHome = (Button) dialog.findViewById(R.id.butHome);
		butHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent=new Intent(DistributorEntryActivity.this,AllButtonActivity.class);
				startActivity(intent);
				finish();
			}
		});


		final   Button butn_Change_dsr = (Button) dialog.findViewById(R.id.butn_Change_dsr);
		butn_Change_dsr.setVisibility(View.GONE);

		final   Button btnDistributorStock = (Button) dialog.findViewById(R.id.btnDistributorStock);
		btnDistributorStock.setVisibility(View.GONE);



		final   Button btnDistributorMap = (Button) dialog.findViewById(R.id.btnDistributorMap);
		btnDistributorMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent=new Intent(DistributorEntryActivity.this,DistributorMapActivity.class);
				startActivity(intent);
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
				fDate = sdf.format(date1).toString().trim();
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


					Intent storeIntent = new Intent(DistributorEntryActivity.this, StoreSelection.class);
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
				Intent intent = new Intent(DistributorEntryActivity.this, StorelistActivity.class);
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
		but_SalesSummray.setOnClickListener(new OnClickListener() {

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
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(DistributorEntryActivity.this);
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
					Intent i=new Intent(DistributorEntryActivity.this,LauncherActivity.class);
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
						Intent i = new Intent(DistributorEntryActivity.this, LauncherActivity.class);
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

					ArrayAdapter adapterCategory=new ArrayAdapter(DistributorEntryActivity.this, android.R.layout.simple_spinner_item,drsNames);
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

		rb_jointWorking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(rb_jointWorking.isChecked())
				{
					rb_myVisit.setChecked(false);
					rb_dsrVisit.setChecked(false);
					spinner_dsrVisit.setVisibility(View.GONE);

					ArrayAdapter adapterCategory=new ArrayAdapter(DistributorEntryActivity.this, android.R.layout.simple_spinner_item,drsNames);
					adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_jointWorking.setAdapter(adapterCategory);
					spinner_jointWorking.setVisibility(View.VISIBLE);

					spinner_jointWorking.setOnItemSelectedListener(new OnItemSelectedListener()
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
		android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(DistributorEntryActivity.this);

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
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(DistributorEntryActivity.this);
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
					Intent i=new Intent(DistributorEntryActivity.this,DetailReportSummaryActivityForAll.class);
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
					Intent i=new Intent(DistributorEntryActivity.this,DetailReportSummaryActivityForAll.class);
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
						Intent i = new Intent(DistributorEntryActivity.this, DetailReportSummaryActivityForAll.class);
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

						Intent i = new Intent(DistributorEntryActivity.this, DetailReportSummaryActivityForAll.class);
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

					ArrayAdapter adapterCategory=new ArrayAdapter(DistributorEntryActivity.this, android.R.layout.simple_spinner_item,drsNames);
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

					ArrayAdapter adapterCategory=new ArrayAdapter(DistributorEntryActivity.this, android.R.layout.simple_spinner_item,DbrSelArray);
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

	private class GetInvoiceForDay extends AsyncTask<Void, Void, Void>
	{
		ServiceWorker newservice = new ServiceWorker();
		String rID="0";
		int chkFlgForErrorToCloseApp=0;

		ProgressDialog pDialogGetInvoiceForDay;

		public GetInvoiceForDay(DistributorEntryActivity activity)
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

			Intent storeIntent = new Intent(DistributorEntryActivity.this, InvoiceStoreSelection.class);
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
				android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(DistributorEntryActivity.this);
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

	public void shardPrefForCoverageArea(int coverageAreaNodeID,int coverageAreaNodeType) {




		Editor editor = sharedPref.edit();

		editor.putInt("CoverageAreaNodeID", coverageAreaNodeID);
		editor.putInt("CoverageAreaNodeType", coverageAreaNodeType);


		editor.commit();

	}


	public void shardPrefForSalesman(int salesmanNodeId,int salesmanNodeType) {




		Editor editor = sharedPref.edit();


		editor.putInt("SalesmanNodeId", salesmanNodeId);
		editor.putInt("SalesmanNodeType", salesmanNodeType);

		editor.commit();

	}

	public void flgDataScopeSharedPref(int _flgDataScope)
	{
		Editor editor = sharedPref.edit();


		editor.putInt("flgDataScope", _flgDataScope);
		editor.commit();


	}
	public void flgDSRSOSharedPref(int _flgDSRSO)
	{
		Editor editor = sharedPref.edit();


		editor.putInt("flgDSRSO", _flgDSRSO);
		editor.commit();


	}

	void openDSRTrackerAlert()
	{
		final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(DistributorEntryActivity.this);
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
					Intent i=new Intent(DistributorEntryActivity.this,WebViewDSRDataReportActivity.class);
					startActivity(i);

				}
				else if(rb_onMap.isChecked())
				{
					Intent i = new Intent(DistributorEntryActivity.this, WebViewDSRTrackerActivity.class);
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
