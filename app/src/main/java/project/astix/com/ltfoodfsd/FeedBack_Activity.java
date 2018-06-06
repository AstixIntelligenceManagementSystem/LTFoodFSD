package project.astix.com.ltfoodfsd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astix.Common.CommonInfo;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class FeedBack_Activity extends BaseActivity implements OnFocusChangeListener, DatePickerDialog.OnDateSetListener
{
	String GLOBAL_TEXT_OVERALL="";

	//calendar
	TextView GlobalCalendarTextview;
	Calendar calendar;
	DatePickerDialog datePickerDialog;
	int Year, Month, Day;
	//
	//Camera
	Dialog dialog;
	ImageView flashImage;
	float mDist=0;
	private boolean isLighOn = false;
	ArrayList<Object> arrImageData=new ArrayList<Object>();
	private Camera mCamera;
	private CameraPreview mPreview;
	private Camera.PictureCallback mPicture;
	private Button capture,cancelCam, switchCamera;
	private Context myContext;
	private LinearLayout cameraPreview;
	private boolean cameraFront = false;
	String userName,imageName,imagButtonTag;
	Uri uriSavedImage;
	String clickedTagPhoto;
	//
	public int chkIfStoreAllowedQuote=1;
	TextView spinnerQualification,tv_selectDate,tv_showImage,tv_showVideo,textView6;
	ImageView imageCam_overall,imageVideoCam_overall;
	AlertDialog.Builder alertDialog;
	AlertDialog ad;
	View convertView;
	ListView listDistributor;
	String[] merchantList= new String[3];
	ArrayAdapter<String> adapterDistributor;

	LinearLayout parentOfAllDynamicData, lLayout_ForcheckedData,parentofFeedbackNotAvailSection,ll_parentofOptionSatisfied,ll_parentofOptionDisatisfied;
	CheckBox chckBoxMain_ForFeedbck;
	RadioButton rb_chefNotAvailble,rb_prdctNotDelivery,rb_prdctNotUsed,othersStatic,rb_ShopClosed,rb_Willtakefeebacklater;
	CheckBox rb_Positives,rb_Supply,rb_Quality,rb_Pricing,rb_Qualitybeforecooking,rb_QualityAppearanceaftercooking,rb_Yield,rb_Supplyexistingcustomers,rb_Others;
	RadioGroup radiogrp_topLayout;
	LinkedHashMap<String, String> hmapProductdataFromDataBase;
	DBAdapterKenya dbEngine=new DBAdapterKenya(FeedBack_Activity.this);
	EditText edtBoxs;
	EditText	CommentOfStaticSection,CommentOfOverAll,feedbackby;
	String storeID;
	Button button,buttonMakeQuotation,button_order,btn_NextSteps;

	ImageView bck_img,no_feedback_overall,dislike_overall,like_overall;
	String productRemainingForFeedback="";
	LinkedHashMap<String, ArrayList<String>> hmapFeedbackDataForSaving=new LinkedHashMap<String, ArrayList<String>>();
	
	LinkedHashMap<String, String> hmapRadioBtnIDAndText=new LinkedHashMap<String,String>();
	LinkedHashMap<String, ArrayList<String>> hmapTakeQuoteAndTakeOrder=new LinkedHashMap<String,ArrayList<String>>();
	LinkedHashMap<String, String> hmapImageData=new LinkedHashMap<String,String>();
	LinkedHashMap<String, String> hmapOverallOptionData=new LinkedHashMap<String,String>();
	LinkedHashMap<String, String> hmapProductwiseDate=new LinkedHashMap<String,String>();
	LinkedHashMap<String, String> hmapRetrieveSavedData;
	LinkedHashMap<String, String> hmapRetrieveOverallData;
	LinkedHashMap<String, String> hmapRetrieveImageData;
	
	int flgChckSavedData=0;
	Boolean flgSetDynamicData=false;
	public String selStoreName,imei,date,pickerDate;
	int bck=0;


	int txtBatLevel;
	Button butBattery;

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent intent) {

			txtBatLevel = intent.getIntExtra("level", 0);

		}
	};







	@Override
	protected void onStop()
	{
		super.onStop();
		try
		{
			this.unregisterReceiver(this.mBatInfoReceiver);

		}catch(Exception e)
		{

		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity);


		ArrayList<String> valueOfHmap=new ArrayList<String>();
		  valueOfHmap.add(0,"0");
		  valueOfHmap.add(1,"0");
		hmapFeedbackDataForSaving.put("0", valueOfHmap);
		
		saveAllData();
		setOptionId();
		initializeAllview();

		
		Intent intent=getIntent();
		storeID=intent.getStringExtra("storeID");
		selStoreName = intent.getStringExtra("SN");
		imei = intent.getStringExtra("imei");
		date = intent.getStringExtra("userdate");
		pickerDate= intent.getStringExtra("pickerDate");
		bck = intent.getIntExtra("bck", 1);

		chkIfStoreAllowedQuote=dbEngine.fnchkIfStoreAllowQuotation(storeID);
		hmapProductdataFromDataBase=dbEngine.fetchDatatblFSDfeedProducts(storeID);
		cameraAndVideoCameraInitialization();
		//saved data also set to layout by below functions
		checkRadioButtonOfStaticData();
		addViewIntoTable();
		spinner_Initialize();//not used now
		ImageViewOfOverAllSectionInitialice();
		retrieveOverallDataAndSettoLayout();
		retrieveImagedata();


	}
	public void ImageViewOfOverAllSectionInitialice() {


		like_overall=(ImageView) findViewById(R.id.like_overall);
		like_overall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

						/*//removing feedback data
							ArrayList<String> valueOfHmap=new ArrayList<String>();
							valueOfHmap.add(0,"0");
							valueOfHmap.add(1,"0");
							hmapFeedbackDataForSaving.put("0", valueOfHmap);
							//end here*/
				//make satisfied option visible

				GLOBAL_TEXT_OVERALL="Satisfied";//id=1
				like_overall.setImageResource(R.drawable.like_enable);
				dislike_overall.setImageResource(R.drawable.dislike);
				no_feedback_overall.setImageResource(R.drawable.feedbacknot_disable);

				CommentOfOverAll.setVisibility(View.VISIBLE);
				textView6.setVisibility(View.VISIBLE);
				ll_parentofOptionSatisfied.setVisibility(View.VISIBLE);
				ll_parentofOptionDisatisfied.setVisibility(View.GONE);
				//

				button_order.setVisibility(View.VISIBLE);
				if(chkIfStoreAllowedQuote==0)
				{
					buttonMakeQuotation.setVisibility(View.GONE);
				}
				else {
					buttonMakeQuotation.setVisibility(View.VISIBLE);
				}
				parentOfAllDynamicData.setVisibility(View.VISIBLE);
				lLayout_ForcheckedData.setVisibility(View.GONE);

				if(parentOfAllDynamicData.getChildCount()!=0){
					for(int i=0;i<parentOfAllDynamicData.getChildCount();i++){

						LinearLayout dynamicview=(LinearLayout) 	parentOfAllDynamicData.getChildAt(i);
						ImageView likeImage=(ImageView) 	dynamicview.findViewById(R.id.like);
						ImageView dislikeImage=(ImageView) 	dynamicview.findViewById(R.id.dislike);
						//TextView noFeedback=(TextView) 	dynamicview.findViewById(R.id.no_feedback);
						ImageView noFeedback=(ImageView) 	dynamicview.findViewById(R.id.no_feedback);

						LinearLayout ll_dependentRadiobtnlayout=(LinearLayout) 	dynamicview.findViewById(R.id.ll_dependentRadiobtn);
						CheckBox chckbox_feedbckLater=(CheckBox) dynamicview.findViewById(R.id.chckbox_feedbckLater);
						TextView tv_selectDateProductwise=(TextView) dynamicview.findViewById(R.id.tv_selectDateProductwise);
						CheckBox cb_take_quotationn=(CheckBox) dynamicview.findViewById(R.id.cb_take_quotation);
						CheckBox cb_take_orderr=(CheckBox) dynamicview.findViewById(R.id.cb_take_order);
						LinearLayout	ParentOfAllOtherViewsDynamicSection=(LinearLayout) dynamicview.findViewById(R.id.ParentOfAllOtherViewsDynamicSection);
						String productID=	likeImage.getTag().toString().trim();


						ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
						valueFromHmapData.set(0, "4");
						valueFromHmapData.set(1, valueFromHmapData.get(1));
						hmapFeedbackDataForSaving.put(productID, valueFromHmapData);

						likeImage.setImageResource(R.drawable.like_enable);
						dislikeImage.setImageResource(R.drawable.dislike);
						noFeedback.setImageResource(R.drawable.feedbacknot_disable);

											/*String text=	noFeedback.getText().toString();
											SpannableString spannableString = new SpannableString(text);
											ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.GRAY);
											spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
											noFeedback.setText(spannableString);*/

						ll_dependentRadiobtnlayout.setVisibility(View.GONE);
						chckbox_feedbckLater.setVisibility(View.GONE);
						tv_selectDateProductwise.setVisibility(View.GONE);

						//hiding FeedbackNotAvailSection
						chckBoxMain_ForFeedbck.setChecked(false);
						chckBoxMain_ForFeedbck.setEnabled(true);
						chckBoxMain_ForFeedbck.setVisibility(View.GONE);
						parentofFeedbackNotAvailSection.setVisibility(View.GONE);
						//showing bottom two checkbox
						if(chkIfStoreAllowedQuote==0)//If Store Level qutation allowed at store level
						{
							cb_take_quotationn.setVisibility(View.GONE);
						}
						else {
							cb_take_quotationn.setVisibility(View.VISIBLE);
						}
						cb_take_orderr.setVisibility(View.VISIBLE);
						ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
						TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
						TakeQuoteAndTakeOrder.add(1,"0");//1index for order
						hmapTakeQuoteAndTakeOrder.put(productID,TakeQuoteAndTakeOrder);
						//visibility hide otherview of dynamic section
						ParentOfAllOtherViewsDynamicSection.setVisibility(View.GONE);
						//hiding calendar
						tv_selectDate.setVisibility(View.GONE);


					}
				}

			}
		});
		dislike_overall=(ImageView) findViewById(R.id.dislike_overall);
		dislike_overall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


							/*//removing feedback data
							ArrayList<String> valueOfHmap=new ArrayList<String>();
							valueOfHmap.add(0,"0");
							valueOfHmap.add(1,"0");
							hmapFeedbackDataForSaving.put("0", valueOfHmap);

							//end here

                               */
				//make dissatisfied option visible

				GLOBAL_TEXT_OVERALL="Not Satisfied";//id=2

				dislike_overall.setImageResource(R.drawable.dislike_enable);
				like_overall.setImageResource(R.drawable.like);
				no_feedback_overall.setImageResource(R.drawable.feedbacknot_disable);

				CommentOfOverAll.setVisibility(View.VISIBLE);
				textView6.setVisibility(View.VISIBLE);
				ll_parentofOptionSatisfied.setVisibility(View.GONE);
				ll_parentofOptionDisatisfied.setVisibility(View.VISIBLE);
				//
				button_order.setVisibility(View.GONE);

				buttonMakeQuotation.setVisibility(View.GONE);


				parentOfAllDynamicData.setVisibility(View.VISIBLE);
				lLayout_ForcheckedData.setVisibility(View.GONE);
				if(parentOfAllDynamicData.getChildCount()!=0){
					for(int i=0;i<parentOfAllDynamicData.getChildCount();i++){
						LinearLayout dynamicview=(LinearLayout)	parentOfAllDynamicData.getChildAt(i);

						ImageView likeImage=(ImageView) 	dynamicview.findViewById(R.id.like);
						ImageView dislikeImage=(ImageView) 	dynamicview.findViewById(R.id.dislike);
						//TextView noFeedback=(TextView) 	dynamicview.findViewById(R.id.no_feedback);
						ImageView noFeedback=(ImageView) 	dynamicview.findViewById(R.id.no_feedback);
						LinearLayout ll_dependentRadiobtnlayout=(LinearLayout) 	dynamicview.findViewById(R.id.ll_dependentRadiobtn);
						CheckBox chckbox_feedbckLaterr=(CheckBox) dynamicview.findViewById(R.id.chckbox_feedbckLater);
						CheckBox cb_take_quotationn=(CheckBox) dynamicview.findViewById(R.id.cb_take_quotation);
						CheckBox cb_take_orderr=(CheckBox) dynamicview.findViewById(R.id.cb_take_order);
						LinearLayout	ParentOfAllOtherViewsDynamicSection=(LinearLayout) dynamicview.findViewById(R.id.ParentOfAllOtherViewsDynamicSection);

						String productID=	dislikeImage.getTag().toString().trim();

						ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
						valueFromHmapData.set(0, "5");
						valueFromHmapData.set(1, valueFromHmapData.get(1));
						hmapFeedbackDataForSaving.put(productID, valueFromHmapData);

						dislikeImage.setImageResource(R.drawable.dislike_enable);
						likeImage.setImageResource(R.drawable.like);
						noFeedback.setImageResource(R.drawable.feedbacknot_disable);
									/*String text=	noFeedback.getText().toString();
									SpannableString spannableString = new SpannableString(text);
									ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.GRAY);
									spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
									noFeedback.setText(spannableString);*/
						ll_dependentRadiobtnlayout.setVisibility(View.GONE);
						chckbox_feedbckLaterr.setVisibility(View.GONE);
						//hiding FeedbackNotAvailSection
						chckBoxMain_ForFeedbck.setChecked(false);
						chckBoxMain_ForFeedbck.setEnabled(true);
						chckBoxMain_ForFeedbck.setVisibility(View.GONE);
						parentofFeedbackNotAvailSection.setVisibility(View.GONE);

						//hiding bottom two checkboxes

						cb_take_quotationn.setVisibility(View.GONE);
						cb_take_orderr.setVisibility(View.GONE);
						ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
						TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
						TakeQuoteAndTakeOrder.add(1,"0");//1index for order
						hmapTakeQuoteAndTakeOrder.put(productID,TakeQuoteAndTakeOrder);
						//visibility hide otherview of dynamic section

						ParentOfAllOtherViewsDynamicSection.setVisibility(View.GONE);
						//hiding calendar
						tv_selectDate.setVisibility(View.GONE);




					}

				}

			}
		});
		no_feedback_overall=(ImageView) findViewById(R.id.no_feedback_overall);
		no_feedback_overall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {



//make dissatisfied option visible

				GLOBAL_TEXT_OVERALL="Feedback not available";//id=3

				dislike_overall.setImageResource(R.drawable.dislike);
				like_overall.setImageResource(R.drawable.like);
				no_feedback_overall.setImageResource(R.drawable.feedbacknot_enable);

				CommentOfOverAll.setVisibility(View.GONE);
				textView6.setVisibility(View.GONE);
				ll_parentofOptionSatisfied.setVisibility(View.GONE);
				ll_parentofOptionDisatisfied.setVisibility(View.GONE);
				//
				button_order.setVisibility(View.GONE);
				buttonMakeQuotation.setVisibility(View.GONE);
				if(rb_Willtakefeebacklater.isChecked()){
					//Visible calendar
					tv_selectDate.setVisibility(View.VISIBLE);
				}
				parentOfAllDynamicData.setVisibility(View.GONE);
				lLayout_ForcheckedData.setVisibility(View.VISIBLE);
				chckBoxMain_ForFeedbck.setChecked(true);
				chckBoxMain_ForFeedbck.setEnabled(false);
				chckBoxMain_ForFeedbck.setVisibility(View.VISIBLE);
				parentofFeedbackNotAvailSection.setVisibility(View.VISIBLE);








			}
		});



	}
	public void retrieveOverallDataAndSettoLayout() {
		hmapRetrieveOverallData = dbEngine.fetchTblFeedbackOverallData(storeID);
		if (hmapRetrieveOverallData != null && !hmapRetrieveOverallData.isEmpty()) {


		String overallDataFromServer = hmapRetrieveOverallData.get(storeID);
		String overallFeedbackID = overallDataFromServer.split(Pattern.quote("$"))[0];
			String overallOptionID = overallDataFromServer.split(Pattern.quote("$"))[4];

		if (overallFeedbackID.equals("1")) {
			//spinnerQualification.setText("Satisfied");
			GLOBAL_TEXT_OVERALL="Satisfied";//id=1
			like_overall.setImageResource(R.drawable.like_enable);
			dislike_overall.setImageResource(R.drawable.dislike);
			no_feedback_overall.setImageResource(R.drawable.feedbacknot_disable);

			parentOfAllDynamicData.setVisibility(View.VISIBLE);
			button_order.setVisibility(View.VISIBLE);
			if(chkIfStoreAllowedQuote==0)
			{
				buttonMakeQuotation.setVisibility(View.GONE);
			}
			else {
				buttonMakeQuotation.setVisibility(View.VISIBLE);
			}
			ll_parentofOptionSatisfied.setVisibility(View.VISIBLE);
			//hmapOverallOptionData.put("Satisfied",overallOptionID);
		}
		if (overallFeedbackID.equals("2")) {
			//spinnerQualification.setText("Not Satisfied");
			GLOBAL_TEXT_OVERALL="Not Satisfied";//id=2

			dislike_overall.setImageResource(R.drawable.dislike_enable);
			like_overall.setImageResource(R.drawable.like);
			no_feedback_overall.setImageResource(R.drawable.feedbacknot_disable);

			parentOfAllDynamicData.setVisibility(View.VISIBLE);
			ll_parentofOptionDisatisfied.setVisibility(View.VISIBLE);
			//hmapOverallOptionData.put("Not Satisfied",overallOptionID);
		}
		if (overallFeedbackID.equals("3")) {
			//spinnerQualification.setText("Feedback not available");
			GLOBAL_TEXT_OVERALL="Feedback not available";//id=3

			dislike_overall.setImageResource(R.drawable.dislike);
			like_overall.setImageResource(R.drawable.like);
			no_feedback_overall.setImageResource(R.drawable.feedbacknot_enable);
			CommentOfOverAll.setVisibility(View.GONE);
			textView6.setVisibility(View.GONE);

		}

		String overallFeedbackCOMMENT = overallDataFromServer.split(Pattern.quote("$"))[1];
		if (!overallFeedbackCOMMENT.equals("0")) {
			CommentOfOverAll.setText(overallFeedbackCOMMENT);
		}
			String feedbackbyStrngFrmDataBase = overallDataFromServer.split(Pattern.quote("$"))[5];
			if (!feedbackbyStrngFrmDataBase.equals("0")) {
				feedbackby.setText(feedbackbyStrngFrmDataBase);
			}
		String overallFeedbackVideoName = overallDataFromServer.split(Pattern.quote("$"))[2];
		String overallFeedbackVideoPath = overallDataFromServer.split(Pattern.quote("$"))[3];

			if(overallOptionID.contains("~")){

				String[] sharedTextArray=overallOptionID.split(Pattern.quote("~"));
				for(int i=0;i<sharedTextArray.length;i++)
				{
					/*if(sharedTextArray[i].equals("28")){
						rb_Positives.setChecked(true);
					}*/
					if(sharedTextArray[i].equals("28")){
						rb_Supply.setChecked(true);
					}
					if(sharedTextArray[i].equals("29")){
						rb_Quality.setChecked(true);
					}
					if(sharedTextArray[i].equals("13")){
						rb_Pricing.setChecked(true);
					}
					if(sharedTextArray[i].equals("14")){
						rb_Qualitybeforecooking.setChecked(true);
					}
					if(sharedTextArray[i].equals("15")){
						rb_QualityAppearanceaftercooking.setChecked(true);
					}
					if(sharedTextArray[i].equals("16")){
						rb_Yield.setChecked(true);
					}
					if(sharedTextArray[i].equals("17")){
					rb_Supplyexistingcustomers.setChecked(true);
				}
					if(sharedTextArray[i].equals("12")){
						rb_Others.setChecked(true);
					}

				}
			}
			else{
				/*if(overallOptionID.equals("28")){
					rb_Positives.setChecked(true);
				}*/
				if(overallOptionID.equals("28")){
					rb_Supply.setChecked(true);
				}
				if(overallOptionID.equals("29")){
					rb_Quality.setChecked(true);
				}
				if(overallOptionID.equals("13")){
					rb_Pricing.setChecked(true);
				}
				if(overallOptionID.equals("14")){
					rb_Qualitybeforecooking.setChecked(true);
				}
				if(overallOptionID.equals("15")){
					rb_QualityAppearanceaftercooking.setChecked(true);
				}
				if(overallOptionID.equals("16")){
					rb_Yield.setChecked(true);
				}if(overallOptionID.equals("17")){
					rb_Supplyexistingcustomers.setChecked(true);
				}
				if(overallOptionID.equals("12")){
					rb_Others.setChecked(true);
				}

			}

	}

	}
	public void retrieveImagedata(){
		hmapRetrieveImageData=dbEngine.fetchTblFeedbackImageData(storeID);
		if(hmapRetrieveImageData!=null && !hmapRetrieveImageData.isEmpty()){

			for(Entry<String, String> entry:hmapRetrieveImageData.entrySet()) {
				final String ProductID = entry.getKey().toString().trim();

				String value = entry.getValue().toString().trim();

				hmapImageData.put(ProductID,value);
			}

		}
	}
	public void cameraAndVideoCameraInitialization(){
		tv_showImage= (TextView) findViewById(R.id.tv_showImage);
		String text=tv_showImage.getText().toString().trim();
		SpannableString content1 = new SpannableString(text);
		content1.setSpan(new UnderlineSpan(), 0, text.length(), 0);
		tv_showImage.setText(content1);
		tv_showImage.setTag("OverallImage");
		tv_showImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


loadImageFromHashmapToLayout(v.getTag().toString());

			}
		});

		tv_showVideo= (TextView) findViewById(R.id.tv_showVideo);
		String text2=tv_showVideo.getText().toString().trim();
		SpannableString content2 = new SpannableString(text2);
		content2.setSpan(new UnderlineSpan(), 0, text2.length(), 0);
		tv_showVideo.setText(content2);
		tv_showVideo.setTag("OverallImage");
		tv_showVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openVideoFucntion(v.getTag().toString());


			}
		});
		imageCam_overall=(ImageView) findViewById(R.id.imageCam_overall);
		imageCam_overall.setTag("OverallImage");
		imageCam_overall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickedTagPhoto=v.getTag().toString();
				openCustomCamara();



			}
		});
		imageVideoCam_overall=(ImageView) findViewById(R.id.imageVideoCam_overall);
		imageVideoCam_overall.setTag("OverallImage");
		imageVideoCam_overall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
               // checking battery status
				if(txtBatLevel <20)
				{

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedBack_Activity.this);
					alertDialog.setTitle("Alert");

					alertDialog.setCancelable(false);
					alertDialog.setMessage("Your Phone Battery is less than 20%.You cant't record the video.");
					alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
					alertDialog.show();

				}
				else
				{
					clickedTagPhoto=v.getTag().toString();

					Intent intent=new Intent(FeedBack_Activity.this,CustomVideoCapture.class);
					intent.putExtra("VIDEOTAG",clickedTagPhoto);
					intent.putExtra("STOREID",storeID);
					startActivity(intent);

				}




			}
		});


	}
	public void spinner_Initialize(){


		spinnerQualification= (TextView) findViewById(R.id.spinnerQualification);
		spinnerQualification.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog = new AlertDialog.Builder(FeedBack_Activity.this);
				LayoutInflater inflater = getLayoutInflater();
				convertView = (View) inflater.inflate(R.layout.activity_list, null);
				EditText inputSearch=	 (EditText) convertView.findViewById(R.id.inputSearch);
				inputSearch.setVisibility(View.GONE);
				listDistributor = (ListView)convertView. findViewById(R.id.list_view);


				merchantList[0]= "Satisfied";//id=1
				merchantList[1]="Not Satisfied";//id=2
				merchantList[2]="Feedback not available";//id=3






				adapterDistributor = new ArrayAdapter<String>(FeedBack_Activity.this, R.layout.list_item, R.id.product_name, merchantList);
				listDistributor.setAdapter(adapterDistributor);
				alertDialog.setView(convertView);
				alertDialog.setTitle("Overall Feedback");
				listDistributor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String abc=listDistributor.getItemAtPosition(position).toString().trim();
						spinnerQualification.setText(abc);
						ad.dismiss();
						if(abc.equals("Satisfied")){
							/*//removing feedback data
							ArrayList<String> valueOfHmap=new ArrayList<String>();
							valueOfHmap.add(0,"0");
							valueOfHmap.add(1,"0");
							hmapFeedbackDataForSaving.put("0", valueOfHmap);
							//end here*/
							//make satisfied option visible

							CommentOfOverAll.setVisibility(View.VISIBLE);
							textView6.setVisibility(View.VISIBLE);
							ll_parentofOptionSatisfied.setVisibility(View.VISIBLE);
							ll_parentofOptionDisatisfied.setVisibility(View.GONE);
							//

							button_order.setVisibility(View.VISIBLE);
							if(chkIfStoreAllowedQuote==0)
							{
								buttonMakeQuotation.setVisibility(View.GONE);
							}
							else {
								buttonMakeQuotation.setVisibility(View.VISIBLE);
							}

							parentOfAllDynamicData.setVisibility(View.VISIBLE);
							lLayout_ForcheckedData.setVisibility(View.GONE);

									if(parentOfAllDynamicData.getChildCount()!=0){
										for(int i=0;i<parentOfAllDynamicData.getChildCount();i++){

										LinearLayout dynamicview=(LinearLayout) 	parentOfAllDynamicData.getChildAt(i);
										ImageView likeImage=(ImageView) 	dynamicview.findViewById(R.id.like);
											ImageView dislikeImage=(ImageView) 	dynamicview.findViewById(R.id.dislike);
											//TextView noFeedback=(TextView) 	dynamicview.findViewById(R.id.no_feedback);
											ImageView noFeedback=(ImageView) 	dynamicview.findViewById(R.id.no_feedback);

											LinearLayout ll_dependentRadiobtnlayout=(LinearLayout) 	dynamicview.findViewById(R.id.ll_dependentRadiobtn);
											CheckBox chckbox_feedbckLater=(CheckBox) dynamicview.findViewById(R.id.chckbox_feedbckLater);
											TextView tv_selectDateProductwise=(TextView) dynamicview.findViewById(R.id.tv_selectDateProductwise);
											CheckBox cb_take_quotationn=(CheckBox) dynamicview.findViewById(R.id.cb_take_quotation);
											CheckBox cb_take_orderr=(CheckBox) dynamicview.findViewById(R.id.cb_take_order);
										 LinearLayout	ParentOfAllOtherViewsDynamicSection=(LinearLayout) dynamicview.findViewById(R.id.ParentOfAllOtherViewsDynamicSection);
											String productID=	likeImage.getTag().toString().trim();


											ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
											valueFromHmapData.set(0, "4");
											valueFromHmapData.set(1, valueFromHmapData.get(1));
											hmapFeedbackDataForSaving.put(productID, valueFromHmapData);

											likeImage.setImageResource(R.drawable.like_enable);
											dislikeImage.setImageResource(R.drawable.dislike);
											noFeedback.setImageResource(R.drawable.feedbacknot_disable);

											/*String text=	noFeedback.getText().toString();
											SpannableString spannableString = new SpannableString(text);
											ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.GRAY);
											spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
											noFeedback.setText(spannableString);*/

											ll_dependentRadiobtnlayout.setVisibility(View.GONE);
											chckbox_feedbckLater.setVisibility(View.GONE);
											tv_selectDateProductwise.setVisibility(View.GONE);

											//hiding FeedbackNotAvailSection
											chckBoxMain_ForFeedbck.setChecked(false);
											chckBoxMain_ForFeedbck.setEnabled(true);
											chckBoxMain_ForFeedbck.setVisibility(View.GONE);
											parentofFeedbackNotAvailSection.setVisibility(View.GONE);
											//showing bottom two checkbox
											cb_take_quotationn.setVisibility(View.VISIBLE);
											cb_take_orderr.setVisibility(View.VISIBLE);
											ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
											TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
											TakeQuoteAndTakeOrder.add(1,"0");//1index for order
											hmapTakeQuoteAndTakeOrder.put(productID,TakeQuoteAndTakeOrder);
											//visibility hide otherview of dynamic section
											ParentOfAllOtherViewsDynamicSection.setVisibility(View.GONE);
											//hiding calendar
											tv_selectDate.setVisibility(View.GONE);


										}
									}
						}
						if(abc.equals("Not Satisfied")){
							/*//removing feedback data
							ArrayList<String> valueOfHmap=new ArrayList<String>();
							valueOfHmap.add(0,"0");
							valueOfHmap.add(1,"0");
							hmapFeedbackDataForSaving.put("0", valueOfHmap);

							//end here

                               */
							//make dissatisfied option visible
							CommentOfOverAll.setVisibility(View.VISIBLE);
							textView6.setVisibility(View.VISIBLE);
							ll_parentofOptionSatisfied.setVisibility(View.GONE);
							ll_parentofOptionDisatisfied.setVisibility(View.VISIBLE);
							//
							button_order.setVisibility(View.GONE);
							buttonMakeQuotation.setVisibility(View.GONE);


							parentOfAllDynamicData.setVisibility(View.VISIBLE);
							lLayout_ForcheckedData.setVisibility(View.GONE);
							if(parentOfAllDynamicData.getChildCount()!=0){
								for(int i=0;i<parentOfAllDynamicData.getChildCount();i++){
									LinearLayout dynamicview=(LinearLayout)	parentOfAllDynamicData.getChildAt(i);

									ImageView likeImage=(ImageView) 	dynamicview.findViewById(R.id.like);
									ImageView dislikeImage=(ImageView) 	dynamicview.findViewById(R.id.dislike);
									//TextView noFeedback=(TextView) 	dynamicview.findViewById(R.id.no_feedback);
									ImageView noFeedback=(ImageView) 	dynamicview.findViewById(R.id.no_feedback);
									LinearLayout ll_dependentRadiobtnlayout=(LinearLayout) 	dynamicview.findViewById(R.id.ll_dependentRadiobtn);
									CheckBox chckbox_feedbckLaterr=(CheckBox) dynamicview.findViewById(R.id.chckbox_feedbckLater);
									CheckBox cb_take_quotationn=(CheckBox) dynamicview.findViewById(R.id.cb_take_quotation);
									CheckBox cb_take_orderr=(CheckBox) dynamicview.findViewById(R.id.cb_take_order);
									LinearLayout	ParentOfAllOtherViewsDynamicSection=(LinearLayout) dynamicview.findViewById(R.id.ParentOfAllOtherViewsDynamicSection);

									String productID=	dislikeImage.getTag().toString().trim();

									ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
									valueFromHmapData.set(0, "5");
									valueFromHmapData.set(1, valueFromHmapData.get(1));
									hmapFeedbackDataForSaving.put(productID, valueFromHmapData);

									dislikeImage.setImageResource(R.drawable.dislike_enable);
									likeImage.setImageResource(R.drawable.like);
									noFeedback.setImageResource(R.drawable.feedbacknot_disable);
									/*String text=	noFeedback.getText().toString();
									SpannableString spannableString = new SpannableString(text);
									ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.GRAY);
									spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
									noFeedback.setText(spannableString);*/
									ll_dependentRadiobtnlayout.setVisibility(View.GONE);
									chckbox_feedbckLaterr.setVisibility(View.GONE);
									//hiding FeedbackNotAvailSection
									chckBoxMain_ForFeedbck.setChecked(false);
									chckBoxMain_ForFeedbck.setEnabled(true);
									chckBoxMain_ForFeedbck.setVisibility(View.GONE);
									parentofFeedbackNotAvailSection.setVisibility(View.GONE);

									//hiding bottom two checkboxes

									cb_take_quotationn.setVisibility(View.GONE);
									cb_take_orderr.setVisibility(View.GONE);
									ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
									TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
									TakeQuoteAndTakeOrder.add(1,"0");//1index for order
									hmapTakeQuoteAndTakeOrder.put(productID,TakeQuoteAndTakeOrder);
									//visibility hide otherview of dynamic section

									ParentOfAllOtherViewsDynamicSection.setVisibility(View.GONE);
									//hiding calendar
									tv_selectDate.setVisibility(View.GONE);




								}

						}}
						if(abc.equals("Feedback not available")){

//make dissatisfied option visible

							CommentOfOverAll.setVisibility(View.GONE);
							textView6.setVisibility(View.GONE);
							ll_parentofOptionSatisfied.setVisibility(View.GONE);
							ll_parentofOptionDisatisfied.setVisibility(View.GONE);
							//
							button_order.setVisibility(View.GONE);
							buttonMakeQuotation.setVisibility(View.GONE);
									if(rb_Willtakefeebacklater.isChecked()){
										//Visible calendar
										tv_selectDate.setVisibility(View.VISIBLE);
									}
							parentOfAllDynamicData.setVisibility(View.GONE);
							lLayout_ForcheckedData.setVisibility(View.VISIBLE);
							chckBoxMain_ForFeedbck.setChecked(true);
							chckBoxMain_ForFeedbck.setEnabled(false);
							chckBoxMain_ForFeedbck.setVisibility(View.VISIBLE);
							parentofFeedbackNotAvailSection.setVisibility(View.VISIBLE);






						}

					}
				});
				ad=alertDialog.show();

			}
		});


	}
	public void initializeAllview()
	{
		textView6=(TextView) findViewById(R.id.textView6);
		ll_parentofOptionSatisfied=(LinearLayout) findViewById(R.id.ll_parentofOptionSatisfied);
		ll_parentofOptionDisatisfied=(LinearLayout) findViewById(R.id.ll_parentofOptionDisatisfied);

		parentofFeedbackNotAvailSection=(LinearLayout) findViewById(R.id.parentofFeedbackNotAvailSection);
parentOfAllDynamicData=(LinearLayout) findViewById(R.id.parentOfAllDynamicData);
		
		bck_img=(ImageView) findViewById(R.id.bck_img);
		bck_img.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedBack_Activity.this);
				 alertDialog.setTitle("Information");
			       
			        alertDialog.setCancelable(false);
			     alertDialog.setMessage("Have you saved your data before going back ");
			     alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			            	dialog.dismiss();
			            	
							Intent intent=new Intent(FeedBack_Activity.this,LastVisitDetail_AllButton.class);
							intent.putExtra("storeID", storeID);
							intent.putExtra("SN", selStoreName);
							intent.putExtra("imei", imei);
							intent.putExtra("userdate", date);
							intent.putExtra("pickerDate", pickerDate);
							intent.putExtra("bck", 1);
						
							startActivity(intent);
							finish();
							/*dbEngine.open();
							String rID=dbEngine.GetActiveRouteID();
							dbEngine.close();
							Intent prevP2 = new Intent(FeedBack_Activity.this, StoreSelection.class);

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
		});
		
		lLayout_ForcheckedData=(LinearLayout) findViewById(R.id.lLayout_ForcheckedData);
		lLayout_ForcheckedData.setVisibility(View.GONE);
		
		CommentOfStaticSection	=(EditText) findViewById(R.id.CommentOfStaticSection);
		CommentOfOverAll	=(EditText) findViewById(R.id.CommentOfOverAll);
		feedbackby	=(EditText) findViewById(R.id.feedbackby);

		chckBoxMain_ForFeedbck=(CheckBox) findViewById(R.id.chckBoxMain_ForFeedbck);
		chckBoxMain_ForFeedbck.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				CheckBox checkbox=(CheckBox) buttonView;
				String text=checkbox.getText().toString();
				System.out.println("CheckBox :"+text);
				
				if(chckBoxMain_ForFeedbck.isChecked() == true)
				{
					parentOfAllDynamicData.setVisibility(View.GONE);
					lLayout_ForcheckedData.setVisibility(View.VISIBLE);
					 
				}
				else
				{
					parentOfAllDynamicData.setVisibility(View.VISIBLE);
					lLayout_ForcheckedData.setVisibility(View.GONE);
				
				}
				
				
			}
		});
		
		radiogrp_topLayout=(RadioGroup) findViewById(R.id.radiogrp_topLayout);
		
		rb_chefNotAvailble=(RadioButton) findViewById(R.id.rb_chefNotAvailble);
		rb_chefNotAvailble.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				RadioButton radioButton=(RadioButton) v;
				String text=radioButton.getText().toString();
				System.out.println("RADIO BUTTON :"+text);
				//rb_chefNotAvailble=7
				//hiding calendar
				tv_selectDate.setVisibility(View.GONE);
				rb_Willtakefeebacklater.setChecked(false);
				
				 ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
				 valueFromHmapData.set(0, "7");
		         valueFromHmapData.set(1, valueFromHmapData.get(0));
	             hmapFeedbackDataForSaving.put("0", valueFromHmapData);
				 
			}
		});
		
		rb_prdctNotDelivery=(RadioButton) findViewById(R.id.rb_prdctNotDeliverd);
		rb_prdctNotDelivery.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				RadioButton radioButton=(RadioButton) v;
				String text=radioButton.getText().toString();
				System.out.println("RADIO BUTTON :"+text);
				//rb_prdctNotDeliver=8
				//hiding calendar
				tv_selectDate.setVisibility(View.GONE);
				rb_Willtakefeebacklater.setChecked(false);
				
				  
				  ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
					 valueFromHmapData.set(0, "8");
			         valueFromHmapData.set(1, valueFromHmapData.get(0));
		             hmapFeedbackDataForSaving.put("0", valueFromHmapData);
			}
		});

		//shop closed
		rb_ShopClosed=(RadioButton) findViewById(R.id.rb_ShopClosed);
		rb_ShopClosed.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				RadioButton radioButton=(RadioButton) v;
				String text=radioButton.getText().toString();
				System.out.println("RADIO BUTTON :"+text);
				//rb_ShopClosed=10
				//hiding calendar
				tv_selectDate.setVisibility(View.GONE);
				rb_Willtakefeebacklater.setChecked(false);


				ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
				valueFromHmapData.set(0, "10");
				valueFromHmapData.set(1, valueFromHmapData.get(0));
				hmapFeedbackDataForSaving.put("0", valueFromHmapData);
			}
		});

		//calendar
		tv_selectDate=(TextView) findViewById(R.id.tv_selectDate);
		tv_selectDate.setTag("FEEDBACKLATER");
		tv_selectDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GlobalCalendarTextview=(TextView) v;

				calendar = Calendar.getInstance();

				Year = calendar.get(Calendar.YEAR);
				Month = calendar.get(Calendar.MONTH);
				Day = calendar.get(Calendar.DAY_OF_MONTH);
				datePickerDialog = DatePickerDialog.newInstance(
						FeedBack_Activity.this, Year, Month, Day);

				datePickerDialog.setThemeDark(false);

				datePickerDialog.showYearPickerFirst(false);

				Calendar calendarForSetDate = Calendar.getInstance();
				calendarForSetDate.setTimeInMillis(System.currentTimeMillis());

				// calendar.setTimeInMillis(System.currentTimeMillis()+24*60*60*1000);
				// YOU can set min or max date using this code
				// datePickerDialog.setMaxDate(Calendar.getInstance());
				// datePickerDialog.setMinDate(calendar);

				datePickerDialog.setAccentColor(Color.parseColor("#544f88"));

				datePickerDialog.setTitle("Delivery Date");
				//	datePickerDialog.setMaxDate(calendarForSetDate);
				/*
				 * Calendar calendar = Calendar.getInstance();
				 * calendar.setTimeInMillis
				 * (System.currentTimeMillis()+24*60*60*1000);
				 */
				// YOU can set min or max date using this code
				// datePickerDialog.setMaxDate(Calendar.getInstance());
				// datePickerDialog.setMinDate(calendar);
				datePickerDialog.setMinDate(Calendar.getInstance());
				datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

			}
		});
		//will take feedback later
		rb_Willtakefeebacklater=(RadioButton) findViewById(R.id.rb_Willtakefeebacklater);
		rb_Willtakefeebacklater.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				RadioButton radioButton=(RadioButton) v;
				String text=radioButton.getText().toString();
				System.out.println("RADIO BUTTON :"+text);
				//rb_ShopClosed=11
				//calender icon show
				tv_selectDate.setVisibility(View.VISIBLE);
				//uncheck other radio button
				rb_chefNotAvailble.setChecked(false);
				rb_prdctNotDelivery.setChecked(false);
				rb_ShopClosed.setChecked(false);
				othersStatic.setChecked(false);
				rb_prdctNotUsed.setChecked(false);




				ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
				valueFromHmapData.set(0, "11");
				valueFromHmapData.set(1, valueFromHmapData.get(0));
				hmapFeedbackDataForSaving.put("0", valueFromHmapData);
			}
		});


		
		othersStatic=(RadioButton) findViewById(R.id.othersStatic);
		
		othersStatic.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				RadioButton radioButton=(RadioButton) v;
				String text=radioButton.getText().toString();
				System.out.println("RADIO BUTTON :"+text);
				//othersStatic=12
				//hiding calendar
				tv_selectDate.setVisibility(View.GONE);
				rb_Willtakefeebacklater.setChecked(false);

				 ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
				 valueFromHmapData.set(0, "12");
		         valueFromHmapData.set(1, valueFromHmapData.get(0));
	             hmapFeedbackDataForSaving.put("0", valueFromHmapData);
			}
		});
		
		rb_prdctNotUsed=(RadioButton) findViewById(R.id.rb_prdctNotUsed);
		rb_prdctNotUsed.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				RadioButton radioButton=(RadioButton) v;
				String text=radioButton.getText().toString();
				System.out.println("RADIO BUTTON :"+text);
				//rb_prdctNotUsed=9
				//hiding calendar
				tv_selectDate.setVisibility(View.GONE);
				rb_Willtakefeebacklater.setChecked(false);

				 ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
				 valueFromHmapData.set(0, "9");
		         valueFromHmapData.set(1, valueFromHmapData.get(0));
	             hmapFeedbackDataForSaving.put("0", valueFromHmapData);
			}
		});

		rb_Positives=(CheckBox) findViewById(R.id.rb_Positives);
		//rb_Positives 28
		rb_Positives.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Positives.isChecked()){
					rb_Positives.setChecked(false);
				}
				else{
					rb_Positives.setChecked(true);
				}
				//hmapOverallOptionData.put("Satisfied","80");


			}
		});
		rb_Supply=(CheckBox) findViewById(R.id.rb_Supply);
		//rb_Supply    29
		rb_Supply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Supply.isChecked()){
					rb_Supply.setChecked(false);
				}
				else{
					rb_Supply.setChecked(true);
				}
				//hmapOverallOptionData.put("Satisfied","81");

			}
		});
		rb_Quality=(CheckBox) findViewById(R.id.rb_Quality);
		//rb_Quality   30
		rb_Quality.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Quality.isChecked()){
					rb_Quality.setChecked(false);
				}
				else{
					rb_Quality.setChecked(true);
				}
				//hmapOverallOptionData.put("Satisfied","82");

			}
		});
		rb_Pricing=(CheckBox) findViewById(R.id.rb_Pricing);
		//rb_Pricing   13
		rb_Pricing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Pricing.isChecked()){
					rb_Pricing.setChecked(false);
				}
				else{
					rb_Pricing.setChecked(true);
				}
				//hmapOverallOptionData.put("Not Satisfied","83");

			}
		});
		rb_Qualitybeforecooking=(CheckBox) findViewById(R.id.rb_Qualitybeforecooking);
		//rb_Qualitybeforecooking    14
		rb_Qualitybeforecooking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Qualitybeforecooking.isChecked()){
					rb_Qualitybeforecooking.setChecked(false);
				}
				else{
					rb_Qualitybeforecooking.setChecked(true);
				}

				//hmapOverallOptionData.put("Not Satisfied","84");
			}
		});
		rb_QualityAppearanceaftercooking=(CheckBox) findViewById(R.id.rb_QualityAppearanceaftercooking);
		//	rb_QualityAppearanceaftercooking 15
		rb_QualityAppearanceaftercooking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_QualityAppearanceaftercooking.isChecked()){
					rb_QualityAppearanceaftercooking.setChecked(false);
				}
				else{
					rb_QualityAppearanceaftercooking.setChecked(true);
				}
				//hmapOverallOptionData.put("Not Satisfied","85");
			}
		});
		rb_Yield=(CheckBox) findViewById(R.id.rb_Yield);
		//	rb_Yield                         16
		rb_Yield.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Yield.isChecked()){
					rb_Yield.setChecked(false);
				}
				else{
					rb_Yield.setChecked(true);
				}
				//hmapOverallOptionData.put("Not Satisfied","86");
			}
		});
		rb_Others=(CheckBox) findViewById(R.id.rb_Others);
		//rb_Supplyexistingcustomers   17
		rb_Others.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Others.isChecked()){
					rb_Others.setChecked(false);
				}
				else{
					rb_Others.setChecked(true);
				}
				//hmapOverallOptionData.put("Not Satisfied","87");
			}
		});
		rb_Supplyexistingcustomers=(CheckBox) findViewById(R.id.rb_Supplyexistingcustomers);
		//rb_Supplyexistingcustomers   17
		rb_Supplyexistingcustomers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!rb_Yield.isChecked()){
					rb_Yield.setChecked(false);
				}
				else{
					rb_Yield.setChecked(true);
				}
				//hmapOverallOptionData.put("Not Satisfied","87");
			}
		});

	}
	public void setOptionId() {
		hmapRadioBtnIDAndText.put("Chef not available", "Chef not available");
		hmapRadioBtnIDAndText.put("3", "Product not delivered");
		hmapRadioBtnIDAndText.put("4", "Product not used");
		hmapRadioBtnIDAndText.put("8", "Others");
		
		hmapRadioBtnIDAndText.put("5", "Satisfied");
		hmapRadioBtnIDAndText.put("6", "Not Satisfied");
		hmapRadioBtnIDAndText.put("9", "Feedback not available");
		hmapRadioBtnIDAndText.put("11", "Product not delivered");
		hmapRadioBtnIDAndText.put("12", "Product not used");
		hmapRadioBtnIDAndText.put("13", "Others");
		hmapRadioBtnIDAndText.put("14", "Take feedback later");
		

	}
	
	public void addViewIntoTable() 
	{
		for(Entry<String, String> entry:hmapProductdataFromDataBase.entrySet())
		{
			
			 final String ProductID=entry.getKey().toString().trim();
             
             String value=entry.getValue().toString().trim();
             String ProductName=value.split(Pattern.quote("^"))[0];
             String Qty=value.split(Pattern.quote("^"))[1];
             String OrderDate=value.split(Pattern.quote("^"))[2];
             
             ArrayList<String> valueOfHmap=new ArrayList<String>();
   		  valueOfHmap.add(0,"0");
   		  valueOfHmap.add(1,"0");
   		hmapFeedbackDataForSaving.put(ProductID, valueOfHmap);
   	 String StoreID="0";
   	 String flgProductCat="0";
   	String FBAnsID="0";
    String Comments="0";
			String QuotationFlg="0";
			String OrderFlg="0";
			String dateProductwise="0";
   		if(flgChckSavedData == 0)
		{
			
		}
   	
		else if(flgChckSavedData == 1)
		{
			if(!hmapRetrieveSavedData.isEmpty() && hmapRetrieveSavedData.containsKey(ProductID)){
			
			String valuefromtable=hmapRetrieveSavedData.get(ProductID);
            
             StoreID=valuefromtable.split(Pattern.quote("$"))[0];
            flgProductCat=valuefromtable.split(Pattern.quote("$"))[1];
            FBAnsID=valuefromtable.split(Pattern.quote("$"))[2];
             Comments=valuefromtable.split(Pattern.quote("$"))[3];
				QuotationFlg=valuefromtable.split(Pattern.quote("$"))[4];
				OrderFlg=valuefromtable.split(Pattern.quote("$"))[5];
				dateProductwise=valuefromtable.split(Pattern.quote("$"))[6];
             ArrayList<String> valueFromHmapData2=	hmapFeedbackDataForSaving.get(ProductID);
             if(!FBAnsID.equals("0") ){
             valueFromHmapData2.set(0, FBAnsID);
             }
             if(!Comments.equals("0") ){
             valueFromHmapData2.set(1, Comments);
             }
             hmapFeedbackDataForSaving.put(ProductID, valueFromHmapData2);
		}
			}
			View dynamic_container=getLayoutInflater().inflate(R.layout.feedback_inflate_layout,null);
			
			
			final RadioButton rb_prdctNotDeliverd=(RadioButton) dynamic_container.findViewById(R.id.rb_prdctNotDeliverd);
			final RadioButton rb_prdctNotUsed=(RadioButton) dynamic_container.findViewById(R.id.rb_prdctNotUsed);
			final RadioButton rb_others=(RadioButton) dynamic_container.findViewById(R.id.rb_others);
			final CheckBox chckbox_feedbckLater=(CheckBox) dynamic_container.findViewById(R.id.chckbox_feedbckLater);
			final LinearLayout parentOfProductNameSection=(LinearLayout) dynamic_container.findViewById(R.id.parentOfProductNameSection);



			final LinearLayout	ParentOfAllOtherViewsDynamicSection=(LinearLayout) dynamic_container.findViewById(R.id.ParentOfAllOtherViewsDynamicSection);
			parentOfProductNameSection.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(ParentOfAllOtherViewsDynamicSection.getVisibility()==View.VISIBLE){
						ParentOfAllOtherViewsDynamicSection.setVisibility(View.GONE);
					}
					else{
						ParentOfAllOtherViewsDynamicSection.setVisibility(View.VISIBLE);
					}

				}
			});
//camera section
			TextView tv_showImageProductwise=(TextView) dynamic_container.findViewById(R.id.tv_showImageProductwise);
			String text1=tv_showImageProductwise.getText().toString().trim();
			SpannableString content1 = new SpannableString(text1);
			content1.setSpan(new UnderlineSpan(), 0, text1.length(), 0);
			tv_showImageProductwise.setText(content1);
			tv_showImageProductwise.setTag(ProductID);
			tv_showImageProductwise.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadImageFromHashmapToLayout(v.getTag().toString());

				}
			});

			TextView tv_showVideoProductwise=(TextView) dynamic_container.findViewById(R.id.tv_showVideoProductwise);
			String text2=tv_showVideoProductwise.getText().toString().trim();
			SpannableString content2 = new SpannableString(text2);
			content1.setSpan(new UnderlineSpan(), 0, text2.length(), 0);
			tv_showVideoProductwise.setText(content2);
			tv_showVideoProductwise.setTag(ProductID);
			tv_showVideoProductwise.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openVideoFucntion(v.getTag().toString());
				}
			});
			ImageView imageCameraProductwise=(ImageView) dynamic_container.findViewById(R.id.imageCameraProductwise);
			imageCameraProductwise.setTag(ProductID);
			imageCameraProductwise.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					clickedTagPhoto=v.getTag().toString();
					openCustomCamara();

				}
			});
			ImageView imageVideoProductwise=(ImageView) dynamic_container.findViewById(R.id.imageVideoProductwise);
			imageVideoProductwise.setTag(ProductID);
			imageVideoProductwise.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{

                    // check Battery Status
					if(txtBatLevel <20){

						AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedBack_Activity.this);
						alertDialog.setTitle("Alert");

						alertDialog.setCancelable(false);
						alertDialog.setMessage("Your Phone Battery is less than 20%.You cant't record the video.");
						alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();

							}
						});
						alertDialog.show();

					}
					else
					{
						clickedTagPhoto=v.getTag().toString();
						Intent intent=new Intent(FeedBack_Activity.this,CustomVideoCapture.class);
						intent.putExtra("VIDEOTAG",clickedTagPhoto);
						intent.putExtra("STOREID",storeID);
						startActivity(intent);

					}


				}
			});

			//end camera section
			//Calendar code

		final	TextView tv_selectDateProductwise=(TextView) dynamic_container.findViewById(R.id.tv_selectDateProductwise);
			tv_selectDateProductwise.setTag(ProductID);

			tv_selectDateProductwise.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				//	tv_selectDateProductwise.setText("Date");

					GlobalCalendarTextview=(TextView) v;

					calendar = Calendar.getInstance();

					Year = calendar.get(Calendar.YEAR);
					Month = calendar.get(Calendar.MONTH);
					Day = calendar.get(Calendar.DAY_OF_MONTH);
					datePickerDialog = DatePickerDialog.newInstance(
							FeedBack_Activity.this, Year, Month, Day);

					datePickerDialog.setThemeDark(false);

					datePickerDialog.showYearPickerFirst(false);

					Calendar calendarForSetDate = Calendar.getInstance();
					calendarForSetDate.setTimeInMillis(System.currentTimeMillis());

					// calendar.setTimeInMillis(System.currentTimeMillis()+24*60*60*1000);
					// YOU can set min or max date using this code
					// datePickerDialog.setMaxDate(Calendar.getInstance());
					// datePickerDialog.setMinDate(calendar);

					datePickerDialog.setAccentColor(Color.parseColor("#544f88"));

					datePickerDialog.setTitle("Delivery Date");
					//	datePickerDialog.setMaxDate(calendarForSetDate);
				/*
				 * Calendar calendar = Calendar.getInstance();
				 * calendar.setTimeInMillis
				 * (System.currentTimeMillis()+24*60*60*1000);
				 */
					// YOU can set min or max date using this code
					// datePickerDialog.setMaxDate(Calendar.getInstance());
					// datePickerDialog.setMinDate(calendar);
					datePickerDialog.setMinDate(Calendar.getInstance());
					datePickerDialog.show(getFragmentManager(), "DatePickerDialog");



				}
			});
			//
			TextView productName=(TextView) dynamic_container.findViewById(R.id.productName);
			productName.setText(ProductName);

			
			TextView prdct_Qty=(TextView) dynamic_container.findViewById(R.id.prdct_Qty);
			prdct_Qty.setText("Qty- "+Qty);
			
			TextView orderDate=(TextView) dynamic_container.findViewById(R.id.orderDate);
			orderDate.setText(OrderDate);
			

			final RadioGroup Radiogrp_ProdctData=(RadioGroup) dynamic_container.findViewById(R.id.Radiogrp_ProdctData);






			//new like image
			final LinearLayout ll_dependentRadiobtn=(LinearLayout) dynamic_container.findViewById(R.id.ll_dependentRadiobtn);
			final ImageView like=(ImageView) dynamic_container.findViewById(R.id.like);
			final ImageView dislike=(ImageView) dynamic_container.findViewById(R.id.dislike);
			//final TextView no_feedback=(TextView) dynamic_container.findViewById(R.id.no_feedback);
			final ImageView no_feedback=(ImageView) dynamic_container.findViewById(R.id.no_feedback);
			final CheckBox cb_take_quotation=(CheckBox) dynamic_container.findViewById(R.id.cb_take_quotation);
			cb_take_quotation.setTag(ProductID);
			final CheckBox cb_take_order=(CheckBox) dynamic_container.findViewById(R.id.cb_take_order);
			cb_take_order.setTag(ProductID);



			like.setTag(ProductID);
			like.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					ImageView imageView=(ImageView) v;
					String productID=	imageView.getTag().toString().trim();
					//rb_satisfied=4
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					valueFromHmapData.set(0, "4");
					valueFromHmapData.set(1, valueFromHmapData.get(1));
					hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
					//bottom checkbox visible
					if(chkIfStoreAllowedQuote==0)
					{
						cb_take_quotation.setVisibility(View.GONE);
					}
					else {
						cb_take_quotation.setVisibility(View.VISIBLE);
					}
					cb_take_order.setVisibility(View.VISIBLE);

					like.setImageResource(R.drawable.like_enable);
					dislike.setImageResource(R.drawable.dislike);
					no_feedback.setImageResource(R.drawable.feedbacknot_disable);
					/*String text=	no_feedback.getText().toString();
					SpannableString spannableString = new SpannableString(text);
					ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.GRAY);
					spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					no_feedback.setText(spannableString);*/
					ll_dependentRadiobtn.setVisibility(View.GONE);
					chckbox_feedbckLater.setVisibility(View.GONE);
					tv_selectDateProductwise.setVisibility(View.GONE);

				}
			});
//old radio button code not working now
			final RadioButton rb_satisfied=(RadioButton) dynamic_container.findViewById(R.id.rb_satisfied);
			rb_satisfied.setTag(ProductID);
			rb_satisfied.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) {
					RadioButton radioButton=(RadioButton) v;
				
					String text=radioButton.getText().toString();
					System.out.println("RADIO BUTTON :"+text);
					String productID=	radioButton.getTag().toString().trim();
					//rb_satisfied=5
					 ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "5");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
					
					
				}
			});
			//new dislike image
			dislike.setTag(ProductID);
			dislike.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					ImageView imageView=(ImageView) v;
					String productID=	imageView.getTag().toString().trim();
					//rb_NotSatisfied=5
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					valueFromHmapData.set(0, "5");
					valueFromHmapData.set(1, valueFromHmapData.get(1));
					hmapFeedbackDataForSaving.put(productID, valueFromHmapData);

					dislike.setImageResource(R.drawable.dislike_enable);
					like.setImageResource(R.drawable.like);
					no_feedback.setImageResource(R.drawable.feedbacknot_disable);

					/*String text=	no_feedback.getText().toString();
					SpannableString spannableString = new SpannableString(text);
					ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.GRAY);
					spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					no_feedback.setText(spannableString);*/
					ll_dependentRadiobtn.setVisibility(View.GONE);
					chckbox_feedbckLater.setVisibility(View.GONE);
					tv_selectDateProductwise.setVisibility(View.GONE);

					//hiding bottom two checkboxes
					cb_take_quotation.setVisibility(View.GONE);
					cb_take_order.setVisibility(View.GONE);
					ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
					TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
					TakeQuoteAndTakeOrder.add(1,"0");//1index for order
					hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);

				}
			});
			//old notsatisfied code not working
			final RadioButton rb_NotSatisfied=(RadioButton) dynamic_container.findViewById(R.id.rb_NotSatisfied);
			rb_NotSatisfied.setTag(ProductID);
			rb_NotSatisfied.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) {
					RadioButton radioButton=(RadioButton) v;
					String text=radioButton.getText().toString();
					System.out.println("RADIO BUTTON :"+text);
					String productID=	radioButton.getTag().toString().trim();
					//rb_NotSatisfied=6
					 ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "6");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
				}
			});
			//new no feedback Textview
			no_feedback.setTag(ProductID);
			no_feedback.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					//TextView textView=(TextView) view;
					ImageView textView=(ImageView) view;
					String productID=	textView.getTag().toString().trim();
					//feedback not avail=6
					//Take feedbackLater=30
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					valueFromHmapData.set(0, "6"+"^"+"30");
					rb_prdctNotDeliverd.setEnabled(false);
					rb_prdctNotUsed.setEnabled(false);
					rb_others.setEnabled(false);
					rb_prdctNotDeliverd.setChecked(false);
					rb_prdctNotUsed.setChecked(false);
					rb_others.setChecked(false);
					chckbox_feedbckLater.setChecked(true);
					valueFromHmapData.set(1, valueFromHmapData.get(1));
					hmapFeedbackDataForSaving.put(productID, valueFromHmapData);

					/*String text=	no_feedback.getText().toString();
					SpannableString spannableString = new SpannableString(text);
					ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.BLACK);
					spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					no_feedback.setText(spannableString);*/

					dislike.setImageResource(R.drawable.dislike);
					like.setImageResource(R.drawable.like);
					no_feedback.setImageResource(R.drawable.feedbacknot_enable);
					ll_dependentRadiobtn.setVisibility(View.VISIBLE);
					chckbox_feedbckLater.setVisibility(View.VISIBLE);
					tv_selectDateProductwise.setVisibility(View.VISIBLE);
					//hiding bottom two checkboxes
					cb_take_quotation.setVisibility(View.GONE);
							cb_take_order.setVisibility(View.GONE);
					ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
					TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
					TakeQuoteAndTakeOrder.add(1,"0");//1index for order
					hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);

				}
			});
			//old no feedback radio button not working
			final RadioButton rb_feedbckNotAvailble=(RadioButton) dynamic_container.findViewById(R.id.rb_feedbckNotAvailble);
			rb_feedbckNotAvailble.setTag(ProductID);
			rb_feedbckNotAvailble.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) {
					RadioButton radioButton=(RadioButton) v;
					String text=radioButton.getText().toString();
					System.out.println("RADIO BUTTON :"+text);
					String productID=	radioButton.getTag().toString().trim();
					//rb_NotSatisfied=9
					 ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "9"+"^"+"14");
					 rb_prdctNotDeliverd.setEnabled(false);
					 rb_prdctNotUsed.setEnabled(false);
					 rb_others.setEnabled(false);
					 rb_prdctNotDeliverd.setChecked(false);
					 rb_prdctNotUsed.setChecked(false);
					 rb_others.setChecked(false);
					 chckbox_feedbckLater.setChecked(true);
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
				}
			});
			
			//final RadioGroup Radiogrp_dependntRadioBtn=(RadioGroup) dynamic_container.findViewById(R.id.Radiogrp_dependntRadioBtn);
			







//not used now
			rb_prdctNotDeliverd.setTag(ProductID);
			rb_prdctNotDeliverd.setEnabled(false);
			rb_prdctNotDeliverd.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) {
					rb_prdctNotDeliverd.setChecked(true);
					rb_prdctNotUsed.setChecked(false);
					rb_others.setChecked(false);
					RadioButton radioButton=(RadioButton) v;
					String text=radioButton.getText().toString();
					System.out.println("RADIO BUTTON :"+text);
					String productID=	radioButton.getTag().toString().trim();
					//feedback not avail=6
					//product not deliver=5
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "6"+"^"+"5");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
				}
			});


			//not used now
			rb_prdctNotUsed.setTag(ProductID);
			rb_prdctNotUsed.setEnabled(false);
			rb_prdctNotUsed.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) {
					rb_prdctNotDeliverd.setChecked(false);
					rb_prdctNotUsed.setChecked(true);
					rb_others.setChecked(false);
					RadioButton radioButton=(RadioButton) v;
					String text=radioButton.getText().toString();
					System.out.println("RADIO BUTTON :"+text);
					String productID=	radioButton.getTag().toString().trim();
					//feedback not avail=6
					//product not used=6
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "6"+"^"+"6");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
				}
			});


			//not used now
			rb_others.setTag(ProductID);
			rb_others.setEnabled(false);
			rb_others.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) 
				{
					rb_prdctNotDeliverd.setChecked(false);
					rb_prdctNotUsed.setChecked(false);
				
					RadioButton radioButton=(RadioButton) v;
					String text=radioButton.getText().toString();
					System.out.println("RADIO BUTTON :"+text);
					radioButton.setChecked(true);
					String productID=	radioButton.getTag().toString().trim();
					//feedback not avail=3
					//other=9
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "3"+"^"+"9");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
				}
			});



			ll_dependentRadiobtn.setVisibility(View.GONE);
			chckbox_feedbckLater.setVisibility(View.GONE);
			tv_selectDateProductwise.setVisibility(View.GONE);
			
			final TextView tv_comment=(TextView) dynamic_container.findViewById(R.id.tv_comment);
			
			final EditText edittxt_commnt=(EditText) dynamic_container.findViewById(R.id.edittxt_commnt);
			edittxt_commnt.setTag(ProductID+"^"+"Comment");
			if(!Comments.equals("0")){
			edittxt_commnt.setText(Comments);
			
			}
			edittxt_commnt.setOnFocusChangeListener(FeedBack_Activity.this);
			rb_feedbckNotAvailble.setTag(ProductID);
			rb_feedbckNotAvailble.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked == true)
					{
						System.out.println("HELLOO......");
						ll_dependentRadiobtn.setVisibility(View.VISIBLE);
						chckbox_feedbckLater.setVisibility(View.VISIBLE);
						tv_selectDateProductwise.setVisibility(View.VISIBLE);
						
					}
					else if(isChecked == false)
					{
						System.out.println("HELLOO......FALSE");
						ll_dependentRadiobtn.setVisibility(View.GONE);
						chckbox_feedbckLater.setVisibility(View.GONE);

					}	
				}
			});
			chckbox_feedbckLater.setTag(ProductID);
			
			chckbox_feedbckLater.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{	
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					CheckBox checkbox=(CheckBox) buttonView;
					String text=checkbox.getText().toString();
					System.out.println("Checkbox :"+text);
					
					if(isChecked == false)
					{
						rb_prdctNotDeliverd.setChecked(false);
						rb_prdctNotDeliverd.setEnabled(true);
						rb_prdctNotUsed.setChecked(false);
						rb_prdctNotUsed.setEnabled(true);
						rb_others.setChecked(false);
						rb_others.setEnabled(true);
						String productID=	checkbox.getTag().toString().trim();
						//feedback not avail=3
						//other=9
						ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
						 valueFromHmapData.set(0, "6");
				         valueFromHmapData.set(1, valueFromHmapData.get(1));
			             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
					}
					else if(isChecked == true)
					{
						rb_prdctNotDeliverd.setEnabled(false);
						rb_prdctNotUsed.setEnabled(false);
						rb_others.setEnabled(false);
						rb_prdctNotDeliverd.setChecked(false);
						rb_prdctNotUsed.setChecked(false);
						rb_others.setChecked(false);
		
						String productID=	checkbox.getTag().toString().trim();
						//feedback not avail=3
						//take feedbacklater=8
						ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
						 valueFromHmapData.set(0, "6"+"^"+"30");
				         valueFromHmapData.set(1, valueFromHmapData.get(1));
			             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
					}
				}
			});
			
			//setting checked values set data to layout
			if(!FBAnsID.equals("0"))
			{


			if(FBAnsID.equals("4"))
			{
				rb_satisfied.setChecked(true);
				like.setImageResource(R.drawable.like_enable);
			}
			else if(FBAnsID.equals("5"))
			{
				rb_NotSatisfied.setChecked(true);
				dislike.setImageResource(R.drawable.dislike_enable);

				//hide take order and take quotation because dislike
				cb_take_quotation.setVisibility(View.GONE);
				cb_take_order.setVisibility(View.GONE);
			}
			else if(FBAnsID.contains("6")&& !FBAnsID.contains("^"))
			{
				//hide take order and take quotation because feedback not taken
				cb_take_quotation.setVisibility(View.GONE);
				cb_take_order.setVisibility(View.GONE);


				//String firstValue=FBAnsID.split(Pattern.quote("^"))[0];
				//String optionValue=FBAnsID.split(Pattern.quote("^"))[1];

				rb_feedbckNotAvailble.setChecked(true);
				/*String text=	no_feedback.getText().toString();
				SpannableString spannableString = new SpannableString(text);
				ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.BLACK);
				spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				no_feedback.setText(spannableString);*/
				no_feedback.setImageResource(R.drawable.feedbacknot_enable);

				ll_dependentRadiobtn.setVisibility(View.VISIBLE);
				chckbox_feedbckLater.setVisibility(View.VISIBLE);
                chckbox_feedbckLater.setChecked(false);
				tv_selectDateProductwise.setVisibility(View.VISIBLE);
				ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(rb_feedbckNotAvailble.getTag().toString());
				valueFromHmapData.set(0, "6");
				valueFromHmapData.set(1, valueFromHmapData.get(1));
				hmapFeedbackDataForSaving.put(rb_feedbckNotAvailble.getTag().toString(), valueFromHmapData);

			}
			else if(FBAnsID.contains("6") && FBAnsID.contains("^"))
			{
				//hide take order and take quotation because feedback not taken
				cb_take_quotation.setVisibility(View.GONE);
				cb_take_order.setVisibility(View.GONE);


				String firstValue=FBAnsID.split(Pattern.quote("^"))[0];
				String optionValue=FBAnsID.split(Pattern.quote("^"))[1];
				
				rb_feedbckNotAvailble.setChecked(true);
				/*String text=	no_feedback.getText().toString();
				SpannableString spannableString = new SpannableString(text);
				ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.BLACK);
				spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				no_feedback.setText(spannableString);*/
				no_feedback.setImageResource(R.drawable.feedbacknot_enable);

				ll_dependentRadiobtn.setVisibility(View.VISIBLE);
				chckbox_feedbckLater.setVisibility(View.VISIBLE);
				tv_selectDateProductwise.setVisibility(View.VISIBLE);
				
				if(optionValue.equals("30"))
				{	String productID=	chckbox_feedbckLater.getTag().toString().trim();
					//setting Date
					if(!dateProductwise.equals("NA")){
						tv_selectDateProductwise.setText(dateProductwise);
						hmapProductwiseDate.put(productID,dateProductwise);
			       	}
					chckbox_feedbckLater.setChecked(true);
					rb_prdctNotDeliverd.setEnabled(false);
					rb_prdctNotUsed.setEnabled(false);
					rb_others.setEnabled(false);

					//rb_NotSatisfied=9
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "6"+"^"+"30");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
					//set value on date
				}
				else if(optionValue.equals("5"))
				{
					chckbox_feedbckLater.setChecked(false);
				
				 rb_prdctNotDeliverd.setChecked(true);
					
					
					
					String productID=	rb_prdctNotDeliverd.getTag().toString().trim();
					
					System.out.println("RAM"+rb_others.getTag());
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "3"+"^"+"5");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
				}
				else if(optionValue.equals("6"))
				{
					
					chckbox_feedbckLater.setChecked(false);
					rb_prdctNotUsed.setChecked(true);
					String productID=	rb_prdctNotUsed.getTag().toString().trim();
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "3"+"^"+"6");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
		
				}
				else if(optionValue.equals("9"))
				{
					chckbox_feedbckLater.setChecked(false);
					rb_others.setChecked(true);
					String productID=	rb_others.getTag().toString().trim();
					
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productID);
					 valueFromHmapData.set(0, "3"+"^"+"9");
			         valueFromHmapData.set(1, valueFromHmapData.get(1));
		             hmapFeedbackDataForSaving.put(productID, valueFromHmapData);
		
				}
				
			}

			
			}


			/*//nitika start



			final ImageView dislike=(ImageView) dynamic_container.findViewById(R.id.dislike);
			final TextView no_feedback=(TextView) dynamic_container.findViewById(R.id.no_feedback);

			final CheckBox cb_take_quotation=(CheckBox) dynamic_container.findViewById(R.id.cb_take_quotation);
			final CheckBox cb_take_order=(CheckBox) dynamic_container.findViewById(R.id.cb_take_order);




			dislike.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					ImageView imageView=(ImageView) v;

					dislike.setImageResource(R.drawable.dislike_enable);
					like.setImageResource(R.drawable.like);

					String text=	no_feedback.getText().toString();
					SpannableString spannableString = new SpannableString(text);
					ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.GRAY);
					spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					no_feedback.setText(spannableString);

				}
			});
			no_feedback.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

				    String text=	no_feedback.getText().toString();
					SpannableString spannableString = new SpannableString(text);
					ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.BLACK);
					spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					no_feedback.setText(spannableString);

					dislike.setImageResource(R.drawable.dislike);
					like.setImageResource(R.drawable.like);
				}
			});


			//nitika end*/

			if(QuotationFlg.equals("1")){
				ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
				TakeQuoteAndTakeOrder.add(0,"1");//0index for quotation
				TakeQuoteAndTakeOrder.add(1,"0");
				hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);
				cb_take_quotation.setChecked(true);


			}
			if(OrderFlg.equals("1")){
				ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
				TakeQuoteAndTakeOrder.add(0,"0");
				TakeQuoteAndTakeOrder.add(1,"1");//1 index for Order
				hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);
				cb_take_order.setChecked(true);

			}

			cb_take_quotation.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
				String productIDTAG=	cb_take_quotation.getTag().toString();
					cb_take_order.setChecked(false);
					//1=take quotation true
					//0=take quotation false
					ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
					TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
					TakeQuoteAndTakeOrder.add(1,"0");//1index for order
					if(cb_take_quotation.isChecked()){
						TakeQuoteAndTakeOrder.add(0,"1");
						hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);
					}
					else{
						TakeQuoteAndTakeOrder.add(0,"0");
						hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);

					}
				}
			});

			cb_take_order.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					String productIDTAG=	cb_take_order.getTag().toString();
					cb_take_quotation.setChecked(false);
					//1=take Order true
					//0=take Order false
					ArrayList<String> TakeQuoteAndTakeOrder=new ArrayList<String>();
					TakeQuoteAndTakeOrder.add(0,"0");//0index for quotation
					TakeQuoteAndTakeOrder.add(1,"0");//1index for order
					if(cb_take_order.isChecked()){
						TakeQuoteAndTakeOrder.add(1,"1");
						hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);

					}
					else{
						TakeQuoteAndTakeOrder.add(1,"0");
						hmapTakeQuoteAndTakeOrder.put(ProductID,TakeQuoteAndTakeOrder);

					}
				}
			});



			parentOfAllDynamicData.addView(dynamic_container);
		}
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(!hasFocus)
		{
			String tag=	v.getTag().toString();
			String productIdCmnt=tag.split(Pattern.quote("^"))[0];
			//String minDlv_qty=valueOnHashMap.split(Pattern.quote("^"))[1];
			
			EditText edittext = (EditText) v;
			if(!edittext.getText().toString().trim().equals(""))
			{
	         	String commentText=	edittext.getText().toString().trim();
		         ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);
			
			         valueFromHmapData.set(0, valueFromHmapData.get(0));
			         valueFromHmapData.set(1, commentText);
		            hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);
			}
			else
			{
				   ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);
				         valueFromHmapData.set(0, valueFromHmapData.get(0));
				         valueFromHmapData.set(1, "0");
			           hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);
				
			}
			
		}
		else
		{
			 if(v instanceof EditText)
			   {edtBoxs=(EditText) v;
			   }
		}
		
	}
	public void saveAllData() 
	{
		buttonMakeQuotation=(Button) findViewById(R.id.buttonMakeQuotation);
		if(chkIfStoreAllowedQuote==0)
		{
			buttonMakeQuotation.setVisibility(View.GONE);
		}
		buttonMakeQuotation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SavingandProceedToNextSteps(1);//1=For Quotation,2=For Order,3=ForSpecial Remarks



			}
		});
		button_order=(Button) findViewById(R.id.button_order);
		button_order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SavingandProceedToNextSteps(2);//1=For Quotation,2=For Order,3=ForSpecial Remarks


			}
		});

		btn_NextSteps=(Button) findViewById(R.id.btn_NextSteps) ;
		btn_NextSteps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SavingandProceedToNextSteps(3);//1=For Quotation,2=For Order,3=ForSpecial Remarks
	}

});

		button=(Button) findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			SavingDataAndValidateForAllButtons();
			
			/*
			if(chckBoxMain_ForFeedbck.isChecked()){
				 ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
				if(CommentOfStaticSection.getText().toString().trim().equals(""))
				{
					
						
			         valueFromHmapData.set(0, valueFromHmapData.get(0));
			         valueFromHmapData.set(1, "0");
				}
				else
				{
					 
						
			         valueFromHmapData.set(0, valueFromHmapData.get(0));
			         valueFromHmapData.set(1, CommentOfStaticSection.getText().toString().trim());
					
				}
				hmapFeedbackDataForSaving.put("0", valueFromHmapData);
			}
			else{
				if(edtBoxs!=null){
				
				
				String tag=	edtBoxs.getTag().toString();
				String productIdCmnt=tag.split(Pattern.quote("^"))[0];
				
				if(!edtBoxs.getText().toString().trim().equals(""))
				{
		         	String commentText=	edtBoxs.getText().toString().trim();
			         ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);
				
				         valueFromHmapData.set(0, valueFromHmapData.get(0));
				         valueFromHmapData.set(1, commentText);
			            hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);
				}
				else
				{
					   ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);
					   valueFromHmapData.set(0, valueFromHmapData.get(0));
					   valueFromHmapData.set(1, "0");
					        
					        
					         
				           hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);
					
				}
			}}
			System.out.println("SSSSS"+hmapFeedbackDataForSaving);
			
			if(validate())
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						FeedBack_Activity.this);
				alertDialog.setTitle("Information");

				alertDialog.setCancelable(false);
				alertDialog.setMessage("Do you want to save data ");
				alertDialog.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								
								savingDataToDataBase();

							
								

								

							}
						});
				alertDialog.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				// Showing Alert Message
				alertDialog.show();
				
				
			}*/
		}
		
	});
	}
	
	public boolean validate() 
	{
		/*if(spinnerQualification.getText().toString().trim().equals("Select"))
		{
			allMessageAlert("Please select overall feedback");
			return false;

		}*/
		if(GLOBAL_TEXT_OVERALL.trim().equals(""))
		{
			allMessageAlert("Please select overall feedback");
			return false;

		}
		else  if(ll_parentofOptionSatisfied.getVisibility() == View.VISIBLE && !RadioButtonChechedOrNot("Satisfied"))
		{
			allMessageAlert("Please select Overall option ");
			return false;

		}
		else  if(ll_parentofOptionDisatisfied.getVisibility() == View.VISIBLE &&  !RadioButtonChechedOrNot("Not Satisfied"))
		{
			allMessageAlert("Please select Overall option ");
			return false;

		}
	else  if(lLayout_ForcheckedData.getVisibility() == View.VISIBLE && hmapFeedbackDataForSaving.get("0").get(0).equals("0"))
		{
		  allMessageAlert("Please select an option ");
		  return false;
			
		}
	  else if(checkValidationOfDynamicsection().equals("6")&& lLayout_ForcheckedData.getVisibility() == View.GONE || checkValidationOfDynamicsection().equals("0") && lLayout_ForcheckedData.getVisibility() == View.GONE )
	  {
		  allMessageAlert(productRemainingForFeedback+"  Feedback Date Required");
		  return false;
	  }
	  else if(tv_selectDate.getVisibility()==View.VISIBLE && tv_selectDate.getText().toString().equals("")){
		  allMessageAlert("Please select date");
		  return false;
	  }
	  else{
		  return true;
	  }
		
	}
	
	public String checkValidationOfDynamicsection() 
	{
		String vaidationFlag="1";
		
		for(Entry<String, ArrayList<String>> entry:hmapFeedbackDataForSaving.entrySet())
		{
			
			 String ProductID=entry.getKey().toString().trim();
             if(!ProductID.equals("0"))
             {productRemainingForFeedback="";
            	 String value=hmapFeedbackDataForSaving.get(ProductID).get(0);
            	 if(value.equals("0")){
            		 vaidationFlag="0";
            		
            		 if(!hmapProductdataFromDataBase.isEmpty() && hmapProductdataFromDataBase.containsKey(ProductID)){
            		String hmapValue	= hmapProductdataFromDataBase.get(ProductID);
            			 String ProductName=hmapValue.split(Pattern.quote("^"))[0];
            			 productRemainingForFeedback=ProductName;
            		 }
            		 break;
            	 }
            	 else if(value.equals("6"+"^"+"30") && !hmapProductwiseDate.containsKey(ProductID))
            	 {
            		 vaidationFlag="6";
            		 if(!hmapProductdataFromDataBase.isEmpty() && hmapProductdataFromDataBase.containsKey(ProductID)){
                 		String hmapValue	= hmapProductdataFromDataBase.get(ProductID);
                 			 String ProductName=hmapValue.split(Pattern.quote("^"))[0];
                 			productRemainingForFeedback=ProductName;
                 		 }
            		 break;
            		 
            	 }
            	 
             }
            
		}
		return vaidationFlag;
		
		

	}
	
	private void allMessageAlert(String message) 
	{
		 AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(FeedBack_Activity.this);
			alertDialogNoConn.setTitle("Information");
			alertDialogNoConn.setMessage(message);
			//alertDialogNoConn.setMessage(getText(R.string.connAlertErrMsg));
			alertDialogNoConn.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) 
						{
                   dialog.dismiss();
                   /*if(isMyServiceRunning())
             		{
                   stopService(new Intent(DynamicActivity.this,GPSTrackerService.class));
             		}
                   finish();*/
                   //finish();
						}
					});
			alertDialogNoConn.setIcon(R.drawable.error_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();

	}
	
	private void MessageAlert(String message) 
	{
		 AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(FeedBack_Activity.this);
			alertDialogNoConn.setTitle("Information");
			alertDialogNoConn.setMessage(message);
			//alertDialogNoConn.setMessage(getText(R.string.connAlertErrMsg));
			alertDialogNoConn.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) 
						{
							
                   dialog.dismiss();
               	Intent intent=new Intent(FeedBack_Activity.this,LastVisitDetails.class);
				intent.putExtra("storeID", storeID);
				intent.putExtra("SN", selStoreName);
				intent.putExtra("imei", imei);
				intent.putExtra("userdate", date);
				intent.putExtra("pickerDate", pickerDate);
				intent.putExtra("bck", 1);
			
				startActivity(intent);
				finish();
           
                   
						}
					});
			alertDialogNoConn.setIcon(R.drawable.info_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();

	}
	
	public void savingDataToDataBase(int flgpageToRedirect) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		String dateOverall="NA";
		String OptionIDOverall="";
		String commentsOverall="";
		 if(lLayout_ForcheckedData.getVisibility() == View.VISIBLE )
		 {
			 String date="NA";
			 if(tv_selectDate.getVisibility()==View.VISIBLE){
				 date=tv_selectDate.getText().toString().trim();
				 dateOverall=date;

			 }
			 OptionIDOverall=hmapFeedbackDataForSaving.get("0").get(0);
			 commentsOverall= hmapFeedbackDataForSaving.get("0").get(1);
			 dbEngine.open();
			 dbEngine.deletetblFeedbackDataWhereStoreId(storeID);
			 dbEngine.deletetblFeedbackImageAndVideoWhereProductID_is_Not_OverallImage("OverallImage");
			 dbEngine. savetblFeedbackData(storeID, "0", "0", hmapFeedbackDataForSaving.get("0").get(0), hmapFeedbackDataForSaving.get("0").get(1),"0", "0", date, "NA", "NA");
			 dbEngine.close();
			 
			
			 System.out.println("Shivam"+ hmapFeedbackDataForSaving.get("0"));
		 }
		 else
		 {
			 dbEngine.open();
			 dbEngine.deletetblFeedbackDataWhereStoreId(storeID);
			 for(Entry<String, ArrayList<String>> entry:hmapFeedbackDataForSaving.entrySet())
				{

				 String ProductID=entry.getKey().toString().trim();
					String QuoteFlg="0";
					String OrderFlg="0";
					String date="NA";
				 if(!ProductID.equals("0"))
				 {

					if(hmapTakeQuoteAndTakeOrder.containsKey(ProductID)){
						QuoteFlg=hmapTakeQuoteAndTakeOrder.get(ProductID).get(0);
						OrderFlg=hmapTakeQuoteAndTakeOrder.get(ProductID).get(1);
					}
					if(hmapFeedbackDataForSaving.get(ProductID).get(0).equals("6"+"^"+"30")){
						date=hmapProductwiseDate.get(ProductID);
					}
					 dbEngine. savetblFeedbackData(storeID, "1", ProductID, hmapFeedbackDataForSaving.get(ProductID).get(0), hmapFeedbackDataForSaving.get(ProductID).get(1),QuoteFlg,OrderFlg, date, "NA", "NA");
				 }
				
				 System.out.println("Shivam"+ hmapFeedbackDataForSaving.get(ProductID));
				 
				}
			 dbEngine.close();
			 
			 
		 }
		 //saving overall data
		String overallFeedbackString="0";
		String overallCommentString="0";
		String videoNameString="0";
		String videoPathString="0";
		String OptionIDString="0";
		String DateString="NA";
		String feedbackbyString="0";
		//overallFeedbackString=spinnerQualification.getText().toString().trim();
		overallFeedbackString=GLOBAL_TEXT_OVERALL.trim();
		if(overallFeedbackString.equals("Satisfied")){
			overallFeedbackString="1";
		}
		if(overallFeedbackString.equals("Not Satisfied")){
			overallFeedbackString="2";
		}
		if(overallFeedbackString.equals("Feedback not available")){
			overallFeedbackString="3";
			OptionIDString= OptionIDOverall;
					overallCommentString=commentsOverall;
			DateString=dateOverall;
		}
		if(!CommentOfOverAll.getText().toString().trim().equals("")){
			overallCommentString=CommentOfOverAll.getText().toString().trim();
		}
		if(!feedbackby.getText().toString().trim().equals("")){
			feedbackbyString=feedbackby.getText().toString().trim();
		}
			if(ll_parentofOptionSatisfied.getVisibility() == View.VISIBLE ){
				String allOptions="";


				if(rb_Positives.isChecked()){
					allOptions="28";

				}
				if(rb_Supply.isChecked()){
					if(allOptions.equals("")){
						allOptions="28";

					}
					else{
						allOptions=allOptions+"~"+"28";

					}

				}
				if(rb_Quality.isChecked()){
					if(allOptions.equals("")){
						allOptions="29";
					}
					else{
						allOptions=allOptions+"~"+"29";

					}

				}
				//OptionIDString=hmapOverallOptionData.get("Satisfied");
				OptionIDString=allOptions;
			}
		if(ll_parentofOptionDisatisfied.getVisibility() == View.VISIBLE ){

			String allOptions="";


			if(rb_Pricing.isChecked()){
				allOptions="13";

			}
			if(rb_Qualitybeforecooking.isChecked()){
				if(allOptions.equals("")){
					allOptions="14";

				}
				else{
					allOptions=allOptions+"~"+"14";

				}

			}
			if(rb_QualityAppearanceaftercooking.isChecked()){
				if(allOptions.equals("")){
					allOptions="15";
				}
				else{
					allOptions=allOptions+"~"+"15";

				}

			}
			if(rb_Yield.isChecked()){
				if(allOptions.equals("")){
					allOptions="16";
				}
				else{
					allOptions=allOptions+"~"+"16";

				}

			}
			if(rb_Supplyexistingcustomers.isChecked()){
				if(allOptions.equals("")){
					allOptions="17";
				}
				else{
					allOptions=allOptions+"~"+"17";

				}
			}
			if(rb_Others.isChecked()){
				if(allOptions.equals("")){
					allOptions="12";
				}
				else{
					allOptions=allOptions+"~"+"12";

				}

			}

			OptionIDString=allOptions;
			//OptionIDString=hmapOverallOptionData.get("Not Satisfied");
		}

		dbEngine.open();
		dbEngine.deletetblFeedbackOverallDataWhereStoreId(storeID);
		dbEngine.savetblFeedbackOverAllData(storeID,overallFeedbackString,overallCommentString,videoNameString,videoPathString,OptionIDString,DateString,feedbackbyString);
		dbEngine.close();
		 getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		 //MessageAlert("Data saved successfully!");
//1=For Quotation,2=For Order,3=ForSpecial Remarks
		if(flgpageToRedirect==1)
		{
			if(AddQuotationCheckBoxclickedOrNot()){
				//intent pass to order page




				Intent intents=new Intent(FeedBack_Activity.this,QuotationActivity.class);
				intents.putExtra("quatationFlag", "NEW");  //Update
				intents.putExtra("SalesQuoteId", "Null");
				intents.putExtra("storeID", storeID);
				intents.putExtra("SN", selStoreName);
				intents.putExtra("imei", imei);
				intents.putExtra("userdate", date);
				intents.putExtra("pickerDate", pickerDate);
				intents.putExtra("FROM", "FEEDBACK");
				intents.putExtra("prcID", "NULL");
				CommonInfo.SalesQuoteId="BLANK";
				CommonInfo.prcID="NULL";
				CommonInfo.newQuottionID="NULL";
				CommonInfo.globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";


				startActivity(intents);
				finish();

			}
			else{
				allMessageAlert("Please Check Add to Quotation ");

			}
		}
		if(flgpageToRedirect==2)
		{
			if(MakeOrderCheckBoxclickedOrNot()){
				//intent pass to order page
				//SavingDataAndValidateForAllButtons();
				Intent nxtP4 = new Intent(FeedBack_Activity.this,ProductOrderFilterSearch.class);
				//Intent nxtP4 = new Intent(LastVisitDetails.this,POSMaterialActivity.class);
				nxtP4.putExtra("storeID", storeID);
				nxtP4.putExtra("SN", selStoreName);
				nxtP4.putExtra("imei", imei);
				nxtP4.putExtra("userdate", date);
				nxtP4.putExtra("pickerDate", pickerDate);
				nxtP4.putExtra("RedirectedFrom", "FeedBack");
				startActivity(nxtP4);
				finish();
			}
			else{
				allMessageAlert("Please Check Add to Order");

			}

		}
		if(flgpageToRedirect==3)
		{
			Intent intents=new Intent(FeedBack_Activity.this,SpecialRemarksActivity.class);
			intents.putExtra("storeID", storeID);
			intents.putExtra("SN", selStoreName);
			intents.putExtra("imei", imei);
			intents.putExtra("userdate", date);
			intents.putExtra("pickerDate", pickerDate);
			intents.putExtra("prcID", "NULL");
			intents.putExtra("flgFromFeedBackOrLastVisit", "1");
			CommonInfo.SalesQuoteId="BLANK";
			CommonInfo.prcID="NULL";
			CommonInfo.newQuottionID="NULL";
			CommonInfo.globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";


			startActivity(intents);
			finish();
		}

	}
	
	public void checkRadioButtonOfStaticData()
	{
		
		flgChckSavedData=dbEngine.ChckTblFeedbackDataExist();
		if(flgChckSavedData == 0)
		{
			
		}
		else if(flgChckSavedData == 1)
		{
			hmapRetrieveSavedData=dbEngine.fetchTblFeedbackData(storeID);
			for(Entry<String, String> entry:hmapRetrieveSavedData.entrySet())
			{
				 String ProductID=entry.getKey().toString().trim();
	             String value=entry.getValue().toString().trim();
	             
	             String StoreID=value.split(Pattern.quote("$"))[0];
	             String flgProductCat=value.split(Pattern.quote("$"))[1];
	             String FBAnsID=value.split(Pattern.quote("$"))[2];
	             String Comments=value.split(Pattern.quote("$"))[3];
				String date=value.split(Pattern.quote("$"))[6];
	             
	             if(ProductID.equals("0"))
	             {
					 //feedback naot avail section visible
					 parentofFeedbackNotAvailSection.setVisibility(View.VISIBLE);
					 //feedback naot avail checkbox disable
					 chckBoxMain_ForFeedbck.setEnabled(false);

	            	 ArrayList<String> valueFromHmapData2=	hmapFeedbackDataForSaving.get("0");
	                 if(!FBAnsID.equals("0") ){
	                 valueFromHmapData2.set(0, FBAnsID);
	                 }
	                 if(!Comments.equals("0") ){
	                 valueFromHmapData2.set(1, Comments);
	                 }
	                 hmapFeedbackDataForSaving.put("0", valueFromHmapData2);
	            	 
	            	 chckBoxMain_ForFeedbck.setChecked(true);
	            	 if(!Comments.equals("0") ){
	            		 CommentOfStaticSection.setText(Comments);
		                 }
	            	
	            	 if(FBAnsID.equals("7"))
	            	 {
	            		 rb_chefNotAvailble.setChecked(true);
	            	 }
	            	 else if(FBAnsID.equals("8"))
	            	 {
	            		 rb_prdctNotDelivery.setChecked(true);
	            	 }
	            	 else if(FBAnsID.equals("9"))
	            	 {
	            		 rb_prdctNotUsed.setChecked(true);
	            	 }
	            	 else if(FBAnsID.equals("12"))
	            	 {
	            		 othersStatic.setChecked(true);
	            	 }
					 else if(FBAnsID.equals("10"))
					 {
						 rb_ShopClosed.setChecked(true);
					 }
					 else if(FBAnsID.equals("11"))
					 {
						 rb_Willtakefeebacklater.setChecked(true);
						 tv_selectDate.setVisibility(View.VISIBLE);
						 if(!date.equals("NA")){
							 tv_selectDate.setText(date);
						 }


					 }
	     	         
	             }
	             else
	             {
	            	// flgSetDynamicData=true;
	             }
			}
		}
	}


	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

		String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		String mon = MONTHS[monthOfYear];
		GlobalCalendarTextview.setText(dayOfMonth + "-" + mon + "-" + year);
		if(!GlobalCalendarTextview.getTag().toString().equals("FEEDBACKLATER")){
			hmapProductwiseDate.put(GlobalCalendarTextview.getTag().toString(),dayOfMonth + "-" + mon + "-" + year);
		}


	}

	public void   openCustomCamara()
	{
		if(dialog!=null)
		{
			if(!dialog.isShowing())
			{
				openCamera();


			}

		}
		else
		{
			openCamera();

		}

	}

	public void openCamera()
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		//arrImageData.clear();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		dialog = new Dialog(FeedBack_Activity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		//dialog.setTitle("Calculation");
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.camera_layout);
		WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();


		parms.height=parms.MATCH_PARENT;
		parms.width=parms.MATCH_PARENT;
		cameraPreview = (LinearLayout)dialog. findViewById(R.id.camera_preview);

		mPreview = new CameraPreview(FeedBack_Activity.this, mCamera);
		cameraPreview.addView(mPreview);
		//onResume code
		if (!hasCamera(FeedBack_Activity.this)) {
			Toast toast = Toast.makeText(FeedBack_Activity.this, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
			toast.show();

		}
		if (mCamera == null) {
			//if the front facing camera does not exist
			if (findFrontFacingCamera() < 0) {
				Toast.makeText(FeedBack_Activity.this, "No front facing camera found.", Toast.LENGTH_LONG).show();
				switchCamera.setVisibility(View.GONE);
			}

			//mCamera = Camera.open(findBackFacingCamera());

			/*if(mCamera!=null){
				mCamera.release();
				mCamera=null;
			}*/
			mCamera= Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			/*if(mCamera==null){
				mCamera=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			}*/

			boolean isParameterSet=false;
			try {
				Camera.Parameters params= mCamera.getParameters();


				List<Camera.Size> sizes = params.getSupportedPictureSizes();
				Camera.Size size = sizes.get(0);
//Camera.Size size1 = sizes.get(0);
				for(int i=0;i<sizes.size();i++)
				{

					if(sizes.get(i).width > size.width)
						size = sizes.get(i);


				}

//System.out.println(size.width + "mm" + size.height);

				params.setPictureSize(size.width, size.height);
				params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				//	params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
				params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);

				//	params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

				isLighOn = false;
				int minExpCom=params.getMinExposureCompensation();
				int maxExpCom=params.getMaxExposureCompensation();

				if( maxExpCom > 4 && minExpCom < 4)
				{
					params.setExposureCompensation(4);
				}
				else
				{
					params.setExposureCompensation(0);
				}
				params.setAutoExposureLock(false);
				params.setAutoWhiteBalanceLock(false);
				//String supportedIsoValues = params.get("iso-values");
				// String newVAlue = params.get("iso");
				//  params.set("iso","1600");
				params.setColorEffect("none");
				params.set("scene-mode","auto");


				params.setPictureFormat(ImageFormat.JPEG);
				params.setJpegQuality(70);
				params.setRotation(90);


				mCamera.setParameters(params);
				isParameterSet=true;
			}
			catch (Exception e)
			{

			}
			if(!isParameterSet)
			{
				Camera.Parameters params2= mCamera.getParameters();
				params2.setPictureFormat(ImageFormat.JPEG);
				params2.setJpegQuality(70);
				params2.setRotation(90);

				mCamera.setParameters(params2);
			}






			setCameraDisplayOrientation(FeedBack_Activity.this, Camera.CameraInfo.CAMERA_FACING_BACK,mCamera);
			mPicture = getPictureCallback();
			mPreview.refreshCamera(mCamera);
		}


		capture = (Button)dialog.  findViewById(R.id.button_capture);

		flashImage= (ImageView)dialog.  findViewById(R.id.flashImage);
		flashImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLighOn) {
					// turn off flash
					Camera.Parameters params = mCamera.getParameters();

					if (mCamera == null || params == null) {
						return;
					}


					params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(params);
					flashImage.setImageResource(R.drawable.flash_off);
					isLighOn=false;
				} else {

					// turn on flash
					Camera.Parameters params = mCamera.getParameters();

					if (mCamera == null || params == null) {
						return;
					}

					params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

					flashImage.setImageResource(R.drawable.flash_on);
					mCamera.setParameters(params);

					isLighOn=true;
				}
			}
		});

		final Button cancleCamera= (Button)dialog.  findViewById(R.id.cancleCamera);
		cancelCam=cancleCamera;
		cancleCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				v.setEnabled(false);
				capture.setEnabled(false);
				cameraPreview.setEnabled(false);
				flashImage.setEnabled(false);

				Camera.Parameters params = mCamera.getParameters();
				params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(params);

				isLighOn = false;
				if(mCamera!=null){
					mCamera.release();
					mCamera=null;
				}

				dialog.dismiss();
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			}
		});
		capture.setOnClickListener(captrureListener);

		cameraPreview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Get the pointer ID
				Camera.Parameters params = mCamera.getParameters();
				int action = event.getAction();

				if (event.getPointerCount() > 1) {
					// handle multi-touch events
					if (action == MotionEvent.ACTION_POINTER_DOWN) {
						mDist = getFingerSpacing(event);
					} else if (action == MotionEvent.ACTION_MOVE
							&& params.isZoomSupported()) {
						mCamera.cancelAutoFocus();
						handleZoom(event, params);
					}
				} else {
					// handle single touch events
					if (action == MotionEvent.ACTION_UP) {
						handleFocus(event, params);
					}
				}
				return true;
			}
		});

		dialog.show();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


	}
	private boolean hasCamera(Context context) {
		//check if the device has camera
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}
	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}
	private int findBackFacingCamera() {
		int cameraId = -1;
		//Search for the back facing camera
		//get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		//for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				break;
			}
		}
		return cameraId;
	}
	private void setCameraDisplayOrientation(Activity activity,
											 int cameraId, Camera camera) {
		Camera.CameraInfo info =
				new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0: degrees = 0; break;
			case Surface.ROTATION_90: degrees = 90; break;
			case Surface.ROTATION_180: degrees = 180; break;
			case Surface.ROTATION_270: degrees = 270; break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	private Camera.PictureCallback getPictureCallback() {
		Camera.PictureCallback picture = new Camera.PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				//make a new picture file
				File pictureFile = getOutputMediaFile();

				Camera.Parameters params = mCamera.getParameters();
				params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(params);
				isLighOn = false;

				if (pictureFile == null) {
					return;
				}
				try {
					//write the file
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();

					//Toast toast = Toast.makeText(getActivity(), "Picture saved: " + pictureFile.getName(), Toast.LENGTH_LONG);
					//toast.show();
					//put data here

					arrImageData.add(0,pictureFile);
					arrImageData.add(1,pictureFile.getName());
					dialog.dismiss();
					if(pictureFile!=null)
					{
						File file=pictureFile;
						System.out.println("File +++"+pictureFile);
						imageName=pictureFile.getName();
						Bitmap bmp = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1024, 768);

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						uriSavedImage = Uri.fromFile(pictureFile);
						bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
						byte[] byteArray = stream.toByteArray();

						// Convert ByteArray to Bitmap::\
						//
						long syncTIMESTAMP = System.currentTimeMillis();
						Date dateobj = new Date(syncTIMESTAMP);
						SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
						String clkdTime = df.format(dateobj);
						//	String valueOfKey=imagButtonTag+"~"+tempId+"~"+file.getAbsolutePath()+"~"+clkdTime+"~"+"2";
						String valueOfKey=clickedTagPhoto+"~"+storeID+"~"+uriSavedImage.toString()+"~"+clkdTime+"~"+"1";
						//   helperDb.insertImageInfo(tempId,imagButtonTag, imageName, file.getAbsolutePath(), 2);
						Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
								byteArray.length);

						//
						//setSavedImageToScrollView(bitmap, imageName,valueOfKey,clickedTagPhoto);
						setImageDataToHashmapAndDatabase();
						/*if(clickedTagPhoto.equals("OverallImage")){
							clickedTagPhoto=clickedTagPhoto+imageName;
						}
						clickedTagPhoto=clickedTagPhoto+imageName;*/
						dbEngine.open();
						dbEngine.savetblFeedbackImageData(storeID,clickedTagPhoto,imageName,uriSavedImage.toString());
						dbEngine.close();
						hmapImageData.put(clickedTagPhoto+"~"+imageName,uriSavedImage.toString()+"~"+imageName);
					}



//Show dialog here
//...
//Hide dialog here

				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}

				//refresh camera to continue preview--------------------------------------------------------------
				//	mPreview.refreshCamera(mCamera);
				//if want to release camera
				if(mCamera!=null){
					mCamera.release();
					mCamera=null;
				}
			}
		};
		return picture;
	}
	private static File getOutputMediaFile() {
		//make a new file directory inside the "sdcard" folder
		File mediaStorageDir = new File("/sdcard/", CommonInfo.ImagesnFolder);

		//if this "JCGCamera folder does not exist
		if (!mediaStorageDir.exists()) {
			//if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		//take the current timeStamp
		String timeStamp = new SimpleDateFormat("yyyyMMMdd_HHmmss",Locale.ENGLISH).format(new Date());
		File mediaFile;
		//and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" +CommonInfo.imei+ timeStamp + ".jpg");

		return mediaFile;
	}
	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setEnabled(false);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			cancelCam.setEnabled(false);
			flashImage.setEnabled(false);
			if(cameraPreview!=null)
			{
				cameraPreview.setEnabled(false);
			}

			if(mCamera!=null)
			{
				mCamera.takePicture(null, null, mPicture);
			}
			else
			{
				dialog.dismiss();
			}
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

		}
	};

	private float getFingerSpacing(MotionEvent event) {
		// ...
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float)Math.sqrt(x * x + y * y);
	}
	private void handleZoom(MotionEvent event, Camera.Parameters params) {
		int maxZoom = params.getMaxZoom();
		int zoom = params.getZoom();
		float newDist = getFingerSpacing(event);
		if (newDist > mDist) {
			// zoom in
			if (zoom < maxZoom)
				zoom++;
		} else if (newDist < mDist) {
			// zoom out
			if (zoom > 0)
				zoom--;
		}
		mDist = newDist;
		params.setZoom(zoom);
		mCamera.setParameters(params);
	}
	public void handleFocus(MotionEvent event, Camera.Parameters params) {
		int pointerId = event.getPointerId(0);
		int pointerIndex = event.findPointerIndex(pointerId);
		// Get the pointer's current position
		float x = event.getX(pointerIndex);
		float y = event.getY(pointerIndex);

		List<String> supportedFocusModes = params.getSupportedFocusModes();
		if (supportedFocusModes != null
				&& supportedFocusModes
				.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
			mCamera.autoFocus(new Camera.AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean b, Camera camera) {
					// currently set to auto-focus on single touch
				}
			});
		}
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
	{ // BEST QUALITY MATCH

		//First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize, Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight)
		{
			inSampleSize = Math.round((float)height / (float)reqHeight);
		}
		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth)
		{
			//if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
			inSampleSize = Math.round((float)width / (float)reqWidth);
		}

		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	  public void setImageDataToHashmapAndDatabase(){

	  }
public void	loadImageFromHashmapToLayout(String tagOfTextView){
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
	//arrImageData.clear();
	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	dialog = new Dialog(FeedBack_Activity.this);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

	//dialog.setTitle("Calculation");
	dialog.setCancelable(false);
	dialog.setContentView(R.layout.image_display_layout);
	WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();


	parms.height=parms.MATCH_PARENT;
	parms.width=parms.MATCH_PARENT;
	Button btn_exit= (Button) dialog.findViewById(R.id.btn_exit);
	btn_exit.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	});
	LinearLayout ll_imageView= (LinearLayout) dialog.findViewById(R.id.ll_imageView);

	if(hmapImageData!=null && !hmapImageData.isEmpty()){
	for(Entry<String, String> entry:hmapImageData.entrySet()) {
		final String ProductID = entry.getKey().toString().trim();

		String value = entry.getValue().toString().trim();
		final String imageURI = value.split(Pattern.quote("~"))[0];
		String ImageNAME = value.split(Pattern.quote("~"))[1];
		String checkProductwiseOrOverAll = tagOfTextView +"~"+ImageNAME;
		if (checkProductwiseOrOverAll.equals(ProductID)) {

		Uri imagURi = Uri.parse(imageURI);
		File file = new File(imagURi.getPath());
		if (file != null) {
			Bitmap bmp = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1024, 768);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
			byte[] byteArray = stream.toByteArray();
			Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View viewStoreLocDetail = inflater.inflate(
					R.layout.store_loc_display, null);

			final RelativeLayout rl_photo = (RelativeLayout) viewStoreLocDetail
					.findViewById(R.id.rl_photo);
			final ImageView img_thumbnail = (ImageView) viewStoreLocDetail
					.findViewById(R.id.img_thumbnail);
			final ImageView imgCncl = (ImageView) viewStoreLocDetail
					.findViewById(R.id.imgCncl);
			img_thumbnail.setImageBitmap(bitmap);
			imgCncl.setTag(ProductID);
			img_thumbnail.setTag(imageURI);
			img_thumbnail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// view.getTag().toString().trim()

					/*
					 * String
					 * imageName=view.getTag().toString().trim().split(Pattern
					 * .quote("^"))[0]; String
					 * flagforView=view.getTag().toString
					 * ().trim().split(Pattern.quote("^"))[1];
					 */
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
						// Do something for lollipop and above versions
					} else {
						Uri intentUri = Uri.parse(img_thumbnail.getTag().toString());

						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(intentUri, "image/*");
						startActivity(intent);
					}

				}
			});

			imgCncl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String tagOfImageCancel=imgCncl.getTag().toString();
					String imageURI = tagOfImageCancel.split(Pattern.quote("~"))[0];
					String ImageNAME = tagOfImageCancel.split(Pattern.quote("~"))[1];

					hmapImageData.remove(imgCncl.getTag());
					dbEngine.open();
					dbEngine.deletetblFeedbackImageWhereImageName(ImageNAME);
					dbEngine.close();


					File fdelete = new File(Uri.parse(img_thumbnail.getTag().toString()).getPath());
					if (fdelete.exists()) {
						if (fdelete.delete()) {

							callBroadCast();
						} else {

						}
					}
					final ViewGroup parent = (ViewGroup) rl_photo.getParent();
					parent.removeView(rl_photo);
					//	System.out.println("Hashmap:" + hmapImageName_ImgPath);
				}
			});


			ll_imageView.addView(viewStoreLocDetail);

		}
	}

	}

	}
	else {Toast.makeText(FeedBack_Activity.this,"No image to show",Toast.LENGTH_SHORT).show();}


	dialog.show();
	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}
	public void callBroadCast() {
		if (Build.VERSION.SDK_INT >= 14) {
			Log.e("-->", " >= 14");
			MediaScannerConnection.scanFile(FeedBack_Activity.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

				public void onScanCompleted(String path, Uri uri) {

				}
			});
		} else {
			Log.e("-->", " < 14");
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		}
	}
	public boolean AddQuotationCheckBoxclickedOrNot(){
		boolean flag=false;
		for(Entry<String, ArrayList<String>> entry:hmapTakeQuoteAndTakeOrder.entrySet())
		{
			String ProductID=entry.getKey().toString().trim();
			if(hmapTakeQuoteAndTakeOrder.get(ProductID).get(0).equals("1")){
				flag=true;
				break;
			}

		}
		return flag;

	}

	public boolean MakeOrderCheckBoxclickedOrNot(){
		boolean flag=false;

		for(Entry<String, ArrayList<String>> entry:hmapTakeQuoteAndTakeOrder.entrySet())
		{
			String ProductID=entry.getKey().toString().trim();
			if(hmapTakeQuoteAndTakeOrder.get(ProductID).get(1).equals("1")){
				flag=true;
				break;
			}

		}
		return flag;

	}
	public void openVideoFucntion(String productID){
		String VideoNameAndPath= dbEngine.fetchTblFeedbackVideoDataFRomStoreIDAndProductID(storeID,productID);
		if(!VideoNameAndPath.equals("0")){

		String VIDEO_NAME = VideoNameAndPath.split(Pattern.quote("$"))[0];
		String VIDEO_PATH = VideoNameAndPath.split(Pattern.quote("$"))[1];

		Uri intentUri = Uri.parse(VIDEO_PATH);

			if(intentUri!=null) {
				Intent intent = new Intent(FeedBack_Activity.this,VideoPlayerActivity.class);
				intent.putExtra("STRINGPATH",VIDEO_PATH);
				/*intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(intentUri, "video*//**//**//**//*");*/
				startActivity(intent);

			}
	}
	else{
			Toast.makeText(FeedBack_Activity.this, "No video Found", Toast.LENGTH_LONG).show();

		}
	}
	/*public void addOrRemoveOptionID(String hashmapKey,String FlagRemoveOrAdd,String optionID){
		if(FlagRemoveOrAdd.equals("ADD") && hmapOverallOptionData!=null ){
			if(hmapOverallOptionData.containsKey(hashmapKey)){
			String allOptionsString=	hmapOverallOptionData.get(hashmapKey);
				if(allOptionsString.contains("~")){

				}
				else {
					String[] sharedTextArray=allOptionsString.split(Pattern.quote("~"));
					for(int i=0;i<sharedTextArray.length;i++)
					{
						if(sharedTextArray[i].equals("optionID")){

						}

					}

				}


			}
			else{
				hmapOverallOptionData.put(hashmapKey,optionID);

			}

		}
		if(FlagRemoveOrAdd.equals("REMOVE")){

		}
		String[] sharedTextArray=sharedText.split(Pattern.quote("$"));
		for(int i=0;i<sharedTextArray.length;i++)
		{}
	}*/


	public boolean RadioButtonChechedOrNot(String flag){
		boolean flagCheck=false;
		if(flag.equals("Satisfied")){
			if(rb_Positives.isChecked()){
				flagCheck=true;
			}
			if(rb_Supply.isChecked()){
				flagCheck=true;
			}
			if(rb_Quality.isChecked()){
				flagCheck=true;
			}

		}
		if(flag.equals("Not Satisfied")){
			if(rb_Pricing.isChecked()){
				flagCheck=true;
			}
			if(rb_Qualitybeforecooking.isChecked()){
				flagCheck=true;
			}
			if(rb_QualityAppearanceaftercooking.isChecked()){
				flagCheck=true;
			}
			if(rb_Yield.isChecked()){
				flagCheck=true;
			}
			if(rb_Supplyexistingcustomers.isChecked()){
				flagCheck=true;
			}
			if(rb_Others.isChecked()){
				flagCheck=true;
			}


		}
		return flagCheck;


	}
public void	SavingDataAndValidateForAllButtons(){
	if(chckBoxMain_ForFeedbck.isChecked()){
		ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
		if(CommentOfStaticSection.getText().toString().trim().equals(""))
		{


			valueFromHmapData.set(0, valueFromHmapData.get(0));
			valueFromHmapData.set(1, "0");
		}
		else
		{


			valueFromHmapData.set(0, valueFromHmapData.get(0));
			valueFromHmapData.set(1, CommentOfStaticSection.getText().toString().trim());

		}
		hmapFeedbackDataForSaving.put("0", valueFromHmapData);
	}
	else{
		if(edtBoxs!=null){


			String tag=	edtBoxs.getTag().toString();
			String productIdCmnt=tag.split(Pattern.quote("^"))[0];

			if(!edtBoxs.getText().toString().trim().equals(""))
			{
				String commentText=	edtBoxs.getText().toString().trim();
				ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);

				valueFromHmapData.set(0, valueFromHmapData.get(0));
				valueFromHmapData.set(1, commentText);
				hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);
			}
			else
			{
				ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);
				valueFromHmapData.set(0, valueFromHmapData.get(0));
				valueFromHmapData.set(1, "0");



				hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);

			}
		}}
	//System.out.println("SSSSS"+hmapFeedbackDataForSaving);

	if(validate())
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				FeedBack_Activity.this);
		alertDialog.setTitle("Information");

		alertDialog.setCancelable(false);
		alertDialog.setMessage("Do you want to save data ");
		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int which) {
						dialog.dismiss();

						//savingDataToDataBase();






					}
				});
		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int which) {
						dialog.dismiss();
					}
				});

		// Showing Alert Message
		alertDialog.show();


	}
}


	public void	SavingandProceedToNextSteps(int flgpageToRedirect){
		if(chckBoxMain_ForFeedbck.isChecked()){
			ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get("0");
			if(CommentOfStaticSection.getText().toString().trim().equals(""))
			{


				valueFromHmapData.set(0, valueFromHmapData.get(0));
				valueFromHmapData.set(1, "0");
			}
			else
			{


				valueFromHmapData.set(0, valueFromHmapData.get(0));
				valueFromHmapData.set(1, CommentOfStaticSection.getText().toString().trim());

			}
			hmapFeedbackDataForSaving.put("0", valueFromHmapData);
		}
		else{
			if(edtBoxs!=null){


				String tag=	edtBoxs.getTag().toString();
				String productIdCmnt=tag.split(Pattern.quote("^"))[0];

				if(!edtBoxs.getText().toString().trim().equals(""))
				{
					String commentText=	edtBoxs.getText().toString().trim();
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);

					valueFromHmapData.set(0, valueFromHmapData.get(0));
					valueFromHmapData.set(1, commentText);
					hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);
				}
				else
				{
					ArrayList<String> valueFromHmapData=	hmapFeedbackDataForSaving.get(productIdCmnt);
					valueFromHmapData.set(0, valueFromHmapData.get(0));
					valueFromHmapData.set(1, "0");



					hmapFeedbackDataForSaving.put(productIdCmnt, valueFromHmapData);

				}
			}}
		//System.out.println("SSSSS"+hmapFeedbackDataForSaving);

		if(validate())
		{
			savingDataToDataBase(flgpageToRedirect);
		}
	}


}



