package com.astix.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import project.astix.com.ltfoodfsd.DBAdapterKenya;
import project.astix.com.ltfoodfsd.R;

public class CommonFunction {



    private static String globalIndctr,healthReltnshpValue="";
    public static void getHealthRelationShip(final Activity activity, final String storeId, final DBAdapterKenya dbengine)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        healthReltnshpValue="";
        globalIndctr="";
        dialog.setContentView(R.layout.activity_health_relationship);
        // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
        WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
        parms.gravity = Gravity.CENTER;

        Button btn_done= (Button) dialog.findViewById(R.id.btn_done);
        final ImageView indictr_red= (ImageView) dialog.findViewById(R.id.indictr_red);
        final ImageView indictr_green= (ImageView) dialog.findViewById(R.id.indictr_green);
        final ImageView indictr_amber= (ImageView) dialog.findViewById(R.id.indictr_amber);

        indictr_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!globalIndctr.equals("red") ) //initial
                {
                    globalIndctr="red";
                    healthReltnshpValue="25";
                    indictr_red.setBackgroundResource(R.drawable.ind_red_dark);
                    indictr_green.setBackgroundResource(R.drawable.ind_green_light);
                    indictr_amber.setBackgroundResource(R.drawable.ind_amber_light);
                }
            }
        });

        indictr_amber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!globalIndctr.equals("amber") ) //initial
                {
                    globalIndctr="amber";
                    healthReltnshpValue="26";
                    indictr_red.setBackgroundResource(R.drawable.ind_red_light);
                    indictr_green.setBackgroundResource(R.drawable.ind_green_light);
                    indictr_amber.setBackgroundResource(R.drawable.ind_amber_dark);
                }
            }
        });

        indictr_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!globalIndctr.equals("green") ) //initial
                {
                    globalIndctr="green";
                    healthReltnshpValue="27";
                    indictr_red.setBackgroundResource(R.drawable.ind_red_light);
                    indictr_green.setBackgroundResource(R.drawable.ind_green_dark);
                    indictr_amber.setBackgroundResource(R.drawable.ind_amber_light);
                }
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(healthReltnshpValue))
                {
                    // saving function;
                    //=new DBAdapterLtFoods(act);
                    dbengine.deletetblHealthRltnshpData(storeId);

                    dbengine.open();
                    dbengine.savetblHealthReltnshp(storeId,healthReltnshpValue,"1");//1=Sstat
                    //  System.out.println("SAVING..."+StoreID+"--"+healthReltnshpValue+"--"+globalIndctr);
                    dbengine.close();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(activity,"Please select health relationship to proceed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    public Bitmap normalizeImageForUri(Context context, Uri uri)
    {
        Bitmap rotatedBitmap = null;

        try {

            ExifInterface exif = new ExifInterface(uri.getPath());

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            rotatedBitmap = rotateBitmap(bitmap, orientation);
            if (!bitmap.equals(rotatedBitmap))
            {
                saveBitmapToFile(context, rotatedBitmap, uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    private  Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();

            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private  void saveBitmapToFile(Context context, Bitmap croppedImage, Uri saveUri) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException e) {

            } finally {
                closeSilently(outputStream);
                croppedImage.recycle();
            }
        }
    }

    private  void closeSilently(@Nullable Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }
}
