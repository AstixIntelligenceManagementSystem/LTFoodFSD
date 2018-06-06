package project.astix.com.ltfoodfsd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

public class HealthRelationshipActivity extends BaseActivity
{

    ImageView indictr_red,indictr_green,indictr_amber,img_back;
    String globalIndctr="null";
    Button btn_done;
    DBAdapterKenya dbengine=new DBAdapterKenya(HealthRelationshipActivity.this);
    String StoreID;
    String healthReltnshpValue="0";
    public String selStoreName,imei,date,pickerDate,HealthRelationPageToRedirect;
    int bck=0;

    LinkedHashMap<String,String> hmapRelationshipColorAndId;
    LinkedHashMap<String,String> hmapRelationshipIdAndCOlorName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_relationship);

        Intent intent=getIntent();
        StoreID=intent.getStringExtra("storeID");
        selStoreName = intent.getStringExtra("SN");
        imei = intent.getStringExtra("imei");
        date = intent.getStringExtra("userdate");
        pickerDate= intent.getStringExtra("pickerDate");
        bck = intent.getIntExtra("bck", 1);
        HealthRelationPageToRedirect = intent.getStringExtra("HealthRelationPageToRedirect");//Where to Send LAst Visit Page=1, Store List =1

        getDataFromDatabase();
        indictrInitialiseAndWrking();
        doneBtnWorking();
        backBtnWorking();

    }

    void getDataFromDatabase()
    {
        healthReltnshpValue=dbengine.fetchHealthRltnshpData(StoreID);
        hmapRelationshipColorAndId=dbengine.fetch_tblHealthReltionshipData();
        hmapRelationshipIdAndCOlorName=dbengine.fetch_tblHealthReltionshipIDColor();
    }

    void backBtnWorking()
    {
        img_back= (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(HealthRelationPageToRedirect.equals("0"))
            {
                Intent intent = new Intent(HealthRelationshipActivity.this, LastVisitDetail_AllButton.class);
                intent.putExtra("storeID", StoreID);
                intent.putExtra("SN", selStoreName);
                intent.putExtra("imei", imei);
                intent.putExtra("userdate", date);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("bck", 1);

                startActivity(intent);
                finish();
            }
               /* Intent i=new Intent(HealthRelationshipActivity.this,MainActivity.class);
                startActivity(i);
                finish();*/
            }
        });
    }

    void doneBtnWorking()
    {
        btn_done= (Button) findViewById(R.id.btn_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(healthReltnshpValue!=null)
                {
                    if(!healthReltnshpValue.equals("0"))
                    {
                        AlertDialog.Builder alert=new AlertDialog.Builder(HealthRelationshipActivity.this);
                        alert.setTitle("Alert");
                        alert.setMessage("Do you want to save changes?");

                        // On pressing Settings button
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dbengine.deletetblHealthRltnshpData(StoreID);

                                dbengine.open();
                                dbengine.savetblHealthReltnshp(StoreID,healthReltnshpValue,"1");//1=Sstat
                                dbengine.close();

                                dialog.dismiss();

                                if(HealthRelationPageToRedirect.equals("0")) {
                                    Intent intent = new Intent(HealthRelationshipActivity.this, LastVisitDetail_AllButton.class);
                                    intent.putExtra("storeID", StoreID);
                                    intent.putExtra("SN", selStoreName);
                                    intent.putExtra("imei", imei);
                                    intent.putExtra("userdate", date);
                                    intent.putExtra("pickerDate", pickerDate);
                                    intent.putExtra("bck", 1);

                                    startActivity(intent);
                                    finish();
                                }


                                if(HealthRelationPageToRedirect.equals("1"))
                                {
                                    Intent storeSaveIntent = new Intent(HealthRelationshipActivity.this, LauncherActivity.class);
                                    startActivity(storeSaveIntent);
                                    finish();
                                }
                            }
                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog=alert.create();
                        dialog.show();
                    }
                    else
                    {
                        Toast.makeText(HealthRelationshipActivity.this,"Health Relationship value cannot be blank", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(HealthRelationshipActivity.this,"Health Relationship value cannot be blank", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    void indictrInitialiseAndWrking()
    {
        indictr_red = (ImageView) findViewById(R.id.indictr_red);
        indictr_green = (ImageView) findViewById(R.id.indictr_green);
        indictr_amber = (ImageView) findViewById(R.id.indictr_amber);

        if (!healthReltnshpValue.equals("0"))
        {
            if (hmapRelationshipIdAndCOlorName != null && hmapRelationshipIdAndCOlorName.size() > 0)
            {
                for(Map.Entry<String,String> entry:hmapRelationshipIdAndCOlorName.entrySet())
                {
                    if(entry.getKey().equals(healthReltnshpValue))
                    {
                        if (entry.getValue().equals("Red")) {
                            indictr_red.setBackgroundResource(R.drawable.ind_red_dark);
                        }
                        if (entry.getValue().equals("Amber")) {
                            indictr_amber.setBackgroundResource(R.drawable.ind_amber_dark);
                        }
                        if (entry.getValue().equals("Green")) {
                            indictr_green.setBackgroundResource(R.drawable.ind_green_dark);
                        }
                    }
                }

            }
        }

     /*   if(!healthReltnshpValue.equals("0"))
        {
            if(healthReltnshpValue.equals("25"))
            {
                globalIndctr="red";
                indictr_red.setBackgroundResource(R.drawable.ind_red_dark);
            }
            if(healthReltnshpValue.equals("26"))
            {
                globalIndctr="amber";
                indictr_amber.setBackgroundResource(R.drawable.ind_amber_dark);
            }
            if(healthReltnshpValue.equals("27"))
            {
                globalIndctr="green";
                indictr_green.setBackgroundResource(R.drawable.ind_green_dark);
            }
        }
*/
        indictr_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                globalIndctr="Red";
                healthReltnshpValue=hmapRelationshipColorAndId.get("Red");
                indictr_red.setBackgroundResource(R.drawable.ind_red_dark);
                indictr_green.setBackgroundResource(R.drawable.ind_green_light);
                indictr_amber.setBackgroundResource(R.drawable.ind_amber_light);
            }
        });

        indictr_amber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    globalIndctr="Amber";
                    healthReltnshpValue=hmapRelationshipColorAndId.get("Amber");
                    indictr_red.setBackgroundResource(R.drawable.ind_red_light);
                    indictr_green.setBackgroundResource(R.drawable.ind_green_light);
                    indictr_amber.setBackgroundResource(R.drawable.ind_amber_dark);
            }
        });

        indictr_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    globalIndctr="Green";
                    healthReltnshpValue=hmapRelationshipColorAndId.get("Green");
                    indictr_red.setBackgroundResource(R.drawable.ind_red_light);
                    indictr_green.setBackgroundResource(R.drawable.ind_green_dark);
                    indictr_amber.setBackgroundResource(R.drawable.ind_amber_light);
            }
        });

    }

}
