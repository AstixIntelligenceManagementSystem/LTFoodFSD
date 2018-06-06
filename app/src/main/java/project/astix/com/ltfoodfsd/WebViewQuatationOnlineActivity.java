package project.astix.com.ltfoodfsd;

/**
 * Created by Sunil on 12/14/2017.
 */

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.astix.Common.CommonInfo;

public class WebViewQuatationOnlineActivity extends BaseActivity
{
    String ImageUrl;
    String distId,dsrId,outletId;
    int nid=22;
    int ntype=150;

    ImageView btn_bck;
    DBAdapterKenya dbengine= new DBAdapterKenya(this);

    ProgressDialog progressDialog;

    public	Timer timer;
    public	MyTimerTask myTimerTask;

    public int Retry=0;

    public WebView webView;

    public TextView txtVw_name;
    public TextView txtVw_contact;
    public String imei;

    public void customHeader()
    {
        TextView tv_heading=(TextView) findViewById(R.id.tv_heading);
        tv_heading.setText("Cost Breakup");
        ImageView imgVw_next=(ImageView) findViewById(R.id.imgVw_next);
        imgVw_next.setVisibility(View.GONE);
        ImageView imgVw_back=(ImageView) findViewById(R.id.imgVw_back);
        imgVw_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

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

        customHeader();

        if(Retry==0);
        {

        }

        if(isOnline())
        {
            if (timer!=null)
            {
                timer.cancel();
                timer = null;
            }

            timer = new Timer();
            myTimerTask = new MyTimerTask();

            timer.schedule(myTimerTask,30000);

            try
            {
                progressDialog = new ProgressDialog(WebViewQuatationOnlineActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);

                int ApplicationID= CommonInfo.Application_TypeID;

                ImageUrl=CommonInfo.WebPageUrlQuatation.trim();
               // ImageUrl=ImageUrl+"?nid="+nid+"&ntype="+ntype;

                ImageUrl=ImageUrl+"?IMEI="+imei;

                webView=(WebView) findViewById(R.id.webView);
               // webView.setRawInputType(Configuration.KEYBOARD_12KEY);

                webView.setWebViewClient(new MyBrowser(progressDialog));
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setSupportZoom(true);
                webView.loadUrl(ImageUrl);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            showNoConnAlert();
        }
    }

    public void showNoConnAlert()
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(WebViewQuatationOnlineActivity.this);
        alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
        alertDialogNoConn.setMessage(R.string.genTermNoDataConnectionFullMsg);
        alertDialogNoConn.setCancelable(false);
        alertDialogNoConn.setNeutralButton(R.string.txtOk,
                new DialogInterface.OnClickListener()
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


    class MyTimerTask extends TimerTask
    {

        @Override
        public void run()
        {

            runOnUiThread(new Runnable()
            {

                @Override
                public void run()
                {

                    if(progressDialog.isShowing())
                    {
                        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(WebViewQuatationOnlineActivity.this);
                        alertDialogNoConn.setTitle("Internet issue");
                        //alertDialogNoConn.setMessage(getText(R.string.syncAlertErrMsggg));
                        alertDialogNoConn.setMessage(getText(R.string.internetslowMsggg));
                        alertDialogNoConn.setCancelable(false);
                        alertDialogNoConn.setPositiveButton("OK",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();

                                    }
                                });
                        alertDialogNoConn.setNegativeButton("Abort/Cancle",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        progressDialog.cancel();
                                        dialog.dismiss();
                                        Retry=1;
                                        Bundle bundle=new Bundle();
                                        onCreate(bundle);

                                    }
                                });
                        alertDialogNoConn.setIcon(R.drawable.error_info_ico);
                        AlertDialog alert = alertDialogNoConn.create();
                        alert.show();
                    }
                }});
        }

    }


}
