package project.astix.com.ltfoodfsd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Created by Sunil on 11/20/2017.
 */

public class DistributorSelectionForSKU extends BaseActivity
{
    public int serverFlag=0;
    Spinner spinner_for_filter;
    AlertDialog.Builder alertDialog_dynamic;
    AlertDialog ad;
    View convertView;
    ListView listDistributor;
    String[] merchantList= new String[3];
    ArrayAdapter<String> adapterDistributor;

    ImageView img_back_Btn;
    public String storeID;
    public String OrderPDAID;
    public String imei;
    public String date;
    public String pickerDate;
    public String SN;
    public String ComeFrom="0";


    String[] DbrArray;
    LinkedHashMap<String,String> hmapDistributor_list;
    String DbrNodeId,DbrNodeType,DbrName;
    DBAdapterKenya dbengine = new DBAdapterKenya(DistributorSelectionForSKU.this);
    LinkedHashMap<String, String> hmapFilterProductList=new LinkedHashMap<String, String>();

    public String[] ProductID;

    int CheckIfStoreExistInStoreProdcutPurchaseDetails=0;
    int CheckIfStoreExistInStoreProdcutInvoiceDetails=0;
    String getDisId="0";
    String SelectedDistributor="Select Distributor";
    LinearLayout ll_forRow;

    private void getDataFromIntent()
    {
        Intent passedvals = getIntent();
        storeID = passedvals.getStringExtra("storeID");
        imei = passedvals.getStringExtra("imei");
        date = passedvals.getStringExtra("userdate");
        pickerDate = passedvals.getStringExtra("pickerDate");
        SN = passedvals.getStringExtra("SN");
        OrderPDAID= passedvals.getStringExtra("OrderPDAID");
        ComeFrom= passedvals.getStringExtra("ComeFrom");

        CheckIfStoreExistInStoreProdcutPurchaseDetails=dbengine.fnCheckIfStoreExistInStoreProdcutPurchaseDetails(storeID);
        CheckIfStoreExistInStoreProdcutInvoiceDetails=dbengine.fnCheckIfStoreExistInStoreProdcutInvoiceDetails(storeID);
        if(CheckIfStoreExistInStoreProdcutPurchaseDetails==1 || CheckIfStoreExistInStoreProdcutInvoiceDetails==1)
        {
            OrderPDAID=dbengine.fngetOrderIDAganistStore(storeID);
        }

    }

    public void getDistributorList()
    {
        dbengine.open();
        hmapDistributor_list=dbengine.getDistributorAllData();
        dbengine.close();
        if(hmapDistributor_list.size()>0)
        {
            DbrArray= new String[hmapDistributor_list.size()];
            DbrArray=changeHmapToArrayKey(hmapDistributor_list);
        }
        spinner_for_filter= (Spinner)findViewById(R.id.spinner_for_filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DistributorSelectionForSKU.this,R.layout.initial_spinner_text,DbrArray);
        adapter.setDropDownViewResource(R.layout.spina);


        spinner_for_filter.setAdapter(adapter);
       String MainDistributorID= dbengine.fetchMainDistributorIDFromtblStoreProdcutPurchaseDetails(storeID,OrderPDAID);
      // if(!MainDistributorID.equals(null))


        if(MainDistributorID!=null)
       {
          int selected_choice_index=0;
           int it=0;
           serverFlag=1;

           Set set2 = hmapDistributor_list.entrySet();
           Iterator iterator = set2.iterator();
           while(iterator.hasNext())
           {
               Map.Entry me2 = (Map.Entry)iterator.next();
               String abc=me2.getValue().toString();
               //String abc=itr.next().toString().trim();
               if(abc.equals(MainDistributorID))
               {
                   selected_choice_index=it;
               }
               it=it+1;
           }
            spinner_for_filter.setSelection(selected_choice_index);

       }
       else
        {
            String DefaultDistributorID=dbengine.fetchDefaultDistributorIDFromtblStoreList(storeID);
            int selected_choice_index=0;
            int it=0;

            Set set2 = hmapDistributor_list.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                String abc=me2.getValue().toString();
                //String abc=itr.next().toString().trim();
                if(abc.equals(DefaultDistributorID))
                {
                    selected_choice_index=it;
                }
                it=it+1;
            }
            spinner_for_filter.setSelection(selected_choice_index);
        }



        spinner_for_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
            {

                TextView tv =(TextView) view;
                SelectedDistributor=tv.getText().toString();

                if (hmapDistributor_list.containsKey(SelectedDistributor))
                {
                    getDisId=hmapDistributor_list.get(SelectedDistributor);
                }

                if(SelectedDistributor.equals("Select Distributor"))
                {
                    if(hmapFilterProductList.size()>0)
                    {
                        if(ProductID.length>0)
                        {
                            fnGetDataFromDistributorSelection(getDisId);

                            dbengine.UpdateDistributerFlagIntblStoreProdcutPurchaseDetails(storeID,OrderPDAID, "0",serverFlag);

                        }
                    }
                }
                else
                {
                    if(hmapFilterProductList.size()>0)
                    {
                        if(ProductID.length>0)
                        {

                            fnGetDataFromDistributorSelection(getDisId);
                            dbengine.UpdateDistributerFlagIntblStoreProdcutPurchaseDetails(storeID,OrderPDAID, getDisId,serverFlag);

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributorselectionfor_sku);
        getDataFromIntent();
        inititalization();
    }





    public void inititalization()
    {
        ll_forRow = (LinearLayout) findViewById(R.id.llayout_for_row);

        hmapFilterProductList = dbengine.getFileredOrderReviewProductListMap(storeID);

        if(hmapFilterProductList.size() > 0)
        {
            ProductID = changeHmapToArrayKey(hmapFilterProductList);
            getDistributorList();



            for (Map.Entry<String, String> entry : hmapFilterProductList.entrySet())
            {

                String proID = entry.getKey();
                String pdodName=entry.getValue();

                LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater1.inflate(R.layout.list_distributor_map_sku, null);

                TextView textView_Shortname = (TextView) view.findViewById(R.id.textView_Shortname);
                textView_Shortname.setText(pdodName);

               //final TextView  spinnerDistributor= (TextView) view.findViewById(R.id.spinnerDistributor);
                final Spinner  spinnerDistributor= (Spinner) view.findViewById(R.id.spinner_for_filter);
                spinnerDistributor.setTag(proID+"_spinner_distributor");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DistributorSelectionForSKU.this,R.layout.initial_spinner_text,DbrArray);
                adapter.setDropDownViewResource(R.layout.spina);


                spinnerDistributor.setAdapter(adapter);
                spinnerDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        TextView tv =(TextView) view;
                        SelectedDistributor=tv.getText().toString();
                        String gettag=spinnerDistributor.getTag().toString().trim();
                        String proID=gettag.split(Pattern.quote("_"))[0];
                        if (hmapDistributor_list.containsKey(SelectedDistributor))
                        {
                            getDisId=hmapDistributor_list.get(SelectedDistributor);
                        }
                        dbengine.UpdateDistributorIDMapProdIDIntblStoreProdcutPurchaseDetails(storeID,OrderPDAID, getDisId,proID);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

               /* spinnerDistributor.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alertDialog_dynamic = new AlertDialog.Builder(DistributorSelectionForSKU.this);
                        LayoutInflater inflater = getLayoutInflater();
                        convertView = (View) inflater.inflate(R.layout.activity_list, null);
                        EditText inputSearch=	 (EditText) convertView.findViewById(R.id.inputSearch);
                        inputSearch.setVisibility(View.GONE);

                        listDistributor = (ListView)convertView. findViewById(R.id.list_view);


                        adapterDistributor = new ArrayAdapter<String>(DistributorSelectionForSKU.this, R.layout.list_item, R.id.product_name, DbrArray);
                        listDistributor.setAdapter(adapterDistributor);
                        alertDialog_dynamic.setView(convertView);
                        alertDialog_dynamic.setTitle("Distributor");
                        listDistributor.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                String abc=listDistributor.getItemAtPosition(position).toString().trim();
                                spinnerDistributor.setText(abc);
                                ad.dismiss();
                                if (hmapDistributor_list.containsKey(abc))
                                {
                                    String DistributorIDMapProdID=hmapDistributor_list.get(abc);
                                    dbengine.UpdateDistributorIDMapProdIDIntblStoreProdcutPurchaseDetails(storeID,OrderPDAID, DistributorIDMapProdID);

                                }

                            }
                        });
                        ad=alertDialog_dynamic.show();
                    }
                });*/




                ll_forRow.addView(view);
            }
        }

        Button btn_saveExit=(Button) findViewById(R.id.btn_saveExit);
        btn_saveExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               /* if(ComeFrom.equals("1"))
                {
                    Intent storeSaveIntent = new Intent(DistributorSelectionForSKU.this, ProductOrderFilterSearch.class);
                    storeSaveIntent.putExtra("storeID", storeID);
                    storeSaveIntent.putExtra("imei", imei);
                    storeSaveIntent.putExtra("userdate", "date");
                    storeSaveIntent.putExtra("pickerDate", pickerDate);
                    storeSaveIntent.putExtra("SN", "SN");
                    startActivity(storeSaveIntent);
                    finish();
                }
                else if(ComeFrom.equals("2"))
                {
                    Intent storeSaveIntent = new Intent(DistributorSelectionForSKU.this, LauncherActivity.class);
                    storeSaveIntent.putExtra("storeID", storeID);
                    storeSaveIntent.putExtra("imei", imei);
                    storeSaveIntent.putExtra("userdate", "date");
                    storeSaveIntent.putExtra("pickerDate", pickerDate);
                    storeSaveIntent.putExtra("SN", "SN");
                    storeSaveIntent.putExtra("OrderPDAID", OrderPDAID);
                    startActivity(storeSaveIntent);
                    finish();
                }
                else
                {
                    Intent fireBackDetPg = new Intent(DistributorSelectionForSKU.this, OrderReview.class);
                    fireBackDetPg.putExtra("storeID", storeID);
                    fireBackDetPg.putExtra("SN", SN);
                    fireBackDetPg.putExtra("imei", imei);
                    fireBackDetPg.putExtra("userdate", date);
                    fireBackDetPg.putExtra("pickerDate", pickerDate);
                    startActivity(fireBackDetPg);
                    finish();
                }*/

               if(SelectedDistributor.equals("Select Distributor"))
               {
                   showAlertSingleButtonError(getResources().getString(R.string.selectDistributorProceeds));
               }
               else
               {
                   Intent AmtCollectIntent = new Intent(DistributorSelectionForSKU.this, Delivery_Details_Activity.class);
                   AmtCollectIntent.putExtra("storeID", storeID);
                   AmtCollectIntent.putExtra("imei", imei);
                   AmtCollectIntent.putExtra("userdate", date);
                   AmtCollectIntent.putExtra("pickerDate", pickerDate);
                   AmtCollectIntent.putExtra("SN", "SN");
                   AmtCollectIntent.putExtra("OrderPDAID", OrderPDAID);
                   AmtCollectIntent.putExtra("ComeFrom", ComeFrom);
                   startActivity(AmtCollectIntent);
                   finish();
               }

            }
        });
        img_back_Btn=(ImageView) findViewById(R.id.img_back_Btn);
        img_back_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0)
            {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistributorSelectionForSKU.this);
                alertDialog.setTitle("Information");

                alertDialog.setCancelable(false);
                alertDialog.setMessage("Have you saved data, before going back ?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which)
                            {
                                dialog.dismiss();
                                //LinkedHashMap<String, String> hmapFilterProductMapDistributorList=new LinkedHashMap<String, String>();
                                //hmapFilterProductMapDistributorList= dbengine.fetchDataFromtblStoreProdcutPurchaseDetails(storeID,OrderPDAID);


                                if(ComeFrom.equals("1"))
                                {
                                    Intent storeSaveIntent = new Intent(DistributorSelectionForSKU.this, LastVisitDetail_AllButton.class);
                                    storeSaveIntent.putExtra("storeID", storeID);
                                    storeSaveIntent.putExtra("imei", imei);
                                    storeSaveIntent.putExtra("userdate", date);
                                    storeSaveIntent.putExtra("pickerDate", pickerDate);
                                    storeSaveIntent.putExtra("SN", "SN");
                                    startActivity(storeSaveIntent);
                                    finish();
                                }
                                else if(ComeFrom.equals("2"))
                                {
                                    Intent storeSaveIntent = new Intent(DistributorSelectionForSKU.this, LauncherActivity.class);
                                    storeSaveIntent.putExtra("storeID", storeID);
                                    storeSaveIntent.putExtra("imei", imei);
                                    storeSaveIntent.putExtra("userdate", date);
                                    storeSaveIntent.putExtra("pickerDate", pickerDate);
                                    storeSaveIntent.putExtra("SN", "SN");
                                    storeSaveIntent.putExtra("OrderPDAID", OrderPDAID);
                                    startActivity(storeSaveIntent);
                                    finish();
                                }
                                else
                                {
                                    Intent fireBackDetPg = new Intent(DistributorSelectionForSKU.this, OrderReview.class);
                                    fireBackDetPg.putExtra("storeID", storeID);
                                    fireBackDetPg.putExtra("SN", SN);
                                    fireBackDetPg.putExtra("imei", imei);
                                    fireBackDetPg.putExtra("userdate", date);
                                    fireBackDetPg.putExtra("pickerDate", pickerDate);
                                    startActivity(fireBackDetPg);
                                    finish();
                                }






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
        });

    }




    public  void fnGetDataFromDistributorSelection(String getDisId)
    {
        Spinner spinner_for_filter_dynamic_new;
        if(serverFlag==1)
        {
            LinkedHashMap<String,String> hmapDistributor=dbengine.fetchDataFromtblStoreProdcutPurchaseDetails(storeID,OrderPDAID);

            Set set2 = hmapDistributor.entrySet();
            Set set3 = hmapDistributor_list.entrySet();
            Iterator iterator = set2.iterator();

            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                String key=me2.getKey().toString();
                String value=me2.getValue().toString();
                String SelectedDisFromBD=value.split(Pattern.quote("~"))[0];

                spinner_for_filter_dynamic_new= (Spinner) ll_forRow.findViewWithTag(key+"_spinner_distributor");

                int selected_choice_index=0;
                int it=0;
                Iterator iterator3 = set3.iterator();
                while(iterator3.hasNext())
                {
                    Map.Entry me3 = (Map.Entry)iterator3.next();
                    String abc=me3.getValue().toString();
                    //String abc=itr.next().toString().trim();
                    if(abc.equals(SelectedDisFromBD))
                    {
                        selected_choice_index=it;
                        break;
                    }
                    it=it+1;
                }
                spinner_for_filter_dynamic_new.setSelection(selected_choice_index);


            }







            serverFlag=0;
        }

         else
        {

                for(Map.Entry<String, String> entry : hmapFilterProductList.entrySet())
                {
                    String proID=entry.getKey();
                    spinner_for_filter_dynamic_new= (Spinner) ll_forRow.findViewWithTag(proID+"_spinner_distributor");

                    int selected_choice_index=0;
                    int it=0;


                    Set set2 = hmapDistributor_list.entrySet();
                    Iterator iterator = set2.iterator();
                    while(iterator.hasNext())
                    {
                        Map.Entry me2 = (Map.Entry)iterator.next();
                        String abc=me2.getValue().toString();
                        if(abc.equals(getDisId))
                        {
                            selected_choice_index=it;
                        }
                        it=it+1;
                    }
                    spinner_for_filter_dynamic_new.setSelection(selected_choice_index);
                }
           // }
        }


    }
}
