package project.astix.com.ltfoodfsd;



        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.Html;
        import android.text.TextUtils;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.astix.Common.CommonInfo;


public class SalesValueTarget extends BaseActivity {

    TextView txt_stv;

    String imei,pickerDate,userDate;
    int intentFrom=0;



    public void customHeader()
    {


        TextView tv_heading=(TextView) findViewById(R.id.tv_heading);
        tv_heading.setText("Sales Target Value");

        ImageView imgVw_next=(ImageView) findViewById(R.id.imgVw_next);

        ImageView imgVw_back=(ImageView) findViewById(R.id.imgVw_back);
        if(intentFrom==0)
        {
            imgVw_back.setVisibility(View.GONE);
            imgVw_next.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent i=new Intent(SalesValueTarget.this,AllButtonActivity.class);
                    startActivity(i);
                    finish();

                }
            });
        }
        else
        {
            imgVw_next.setVisibility(View.GONE);
            imgVw_back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent i=new Intent(SalesValueTarget.this,StoreSelection.class);
                    i.putExtra("imei", imei);
                    i.putExtra("userDate", userDate);
                    i.putExtra("pickerDate", pickerDate);
                    startActivity(i);
                    finish();

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_value_target);
        txt_stv=(TextView) findViewById(R.id.txt_stv);

        Intent intent=getIntent();
        intentFrom= intent.getIntExtra("IntentFrom", 0);
        if(intentFrom==1)
        {
            imei = intent.getStringExtra("imei").trim();
            pickerDate = intent.getStringExtra("pickerDate").trim();
            userDate = intent.getStringExtra("userDate");
        }
        //Html.fromHtml(CommonInfo.SalesPersonTodaysTargetMsg)
        if(!TextUtils.isEmpty(CommonInfo.SalesPersonTodaysTargetMsg))
        {
            txt_stv.setText(Html.fromHtml(CommonInfo.SalesPersonTodaysTargetMsg));
        }
        else
        {
            if(intentFrom==0)
            {
                Intent i=new Intent(SalesValueTarget.this,IncentiveActivity.class);
                startActivity(i);
                finish();
            }

        }




        customHeader();
    }


}
