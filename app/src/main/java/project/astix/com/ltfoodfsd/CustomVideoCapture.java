package project.astix.com.ltfoodfsd;

/**
 * Created by Sunil on 11/7/2017.
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.astix.Common.CommonInfo;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class CustomVideoCapture extends BaseActivity
{

    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;

    Button myButton,cancleCamera;
    SurfaceHolder surfaceHolder;
    boolean recording;

    String VideoName;

    String onlyDate;
    String photoClickedDateTime="";
    public String imei;
    public static int orientation;
    String VIDEOTAG;
    String STOREID;
    DBAdapterKenya dbEngine=new DBAdapterKenya(CustomVideoCapture.this);
    String videoPathInFolder;


    public void saveVideoName()
    {
        long syncTIMESTAMP = System.currentTimeMillis();
        Date datefromat = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        SimpleDateFormat dfClickedDateTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS",Locale.ENGLISH);
        onlyDate=df.format(datefromat);
        photoClickedDateTime=dfClickedDateTime.format(datefromat);
        onlyDate=onlyDate.replace(":","").trim().replace("-", "").replace(" ","").trim().toString();

        File VideoFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.VideoFolder);
        if (!VideoFolder.exists())
        {
            VideoFolder.mkdirs();

        }

       // VideoName=imei+"~"+storeID+"~"+productIdVideo+"~"+onlyDate+".jpg";
        VideoName=imei+"~"+onlyDate+".mp4";



    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_video_capture);
        Intent intent=getIntent();
       VIDEOTAG=  intent.getStringExtra("VIDEOTAG");
         STOREID=  intent.getStringExtra("STOREID");

        recording = false;

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



    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();

        if (!hasCamera(CustomVideoCapture.this))
        {
            Toast toast = Toast.makeText(CustomVideoCapture.this, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();

        }
        else
        {
            if(myCamera!=null)
            {
                myCamera.release();
            }

            if (findFrontFacingCamera() < 0)
            {
                Toast.makeText(this, "No front facing camera found.",Toast.LENGTH_LONG).show();
                // myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                // myCamera.setDisplayOrientation(90);
            }
            else
            {
                /*if(findBackFacingCamera() < 0)
                {
                    Toast.makeText(this, "No Back facing camera found.",Toast.LENGTH_LONG).show();
                    myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    myCamera.setDisplayOrientation(90);
                }
                else
                {
                    myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    myCamera.setDisplayOrientation(90);
                }*/
            }



            myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            myCamera.setDisplayOrientation(90);

            myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera,this);
            FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
            myCameraPreview.addView(myCameraSurfaceView);

            myButton = (Button)findViewById(R.id.mybutton);
            myButton.setVisibility(View.VISIBLE);
            myButton.setOnClickListener(myButtonOnClickListener);


            cancleCamera = (Button)findViewById(R.id.cancleCamera);
            cancleCamera.setVisibility(View.VISIBLE);
            cancleCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    releaseMediaRecorder();       // if you are using MediaRecorder, release it first
                    releaseCamera();
                    finish();
                }
            });

        }
    }

    private boolean hasCamera(Context context)
    {
        //check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private int findFrontFacingCamera()
    {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("sv", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera()
    {
        int cameraId = -1;
        //Search for the back facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
         for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                 break;
            }
        }
        return cameraId;
    }



    private Camera getCameraInstance()
    {
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    Button.OnClickListener myButtonOnClickListener= new Button.OnClickListener(){

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if(recording)
            {
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                releaseCamera();
                ImageDeleteAndSaving();
                //Exit after saved

                finish();
            }
            else
            {

                //Release Camera before MediaRecorder start
                saveVideoName();
                releaseCamera();
                cancleCamera.setVisibility(View.GONE);

                if(!prepareMediaRecorder())
                {
                    Toast.makeText(CustomVideoCapture.this,"Fail in prepareMediaRecorder()!\n - Ended -",Toast.LENGTH_LONG).show();
                    finish();
                }

                mediaRecorder.start();
                recording = true;
                myButton.setText("STOP");
            }
        }};

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {

        Camera.CameraInfo info =
                new Camera.CameraInfo();

        Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
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
        CustomVideoCapture.orientation=result;
        camera.setDisplayOrientation(result);
    }

    private boolean prepareMediaRecorder()
    {
        myCamera = getCameraInstance();
        setCameraDisplayOrientation(this,0,myCamera);
        mediaRecorder = new MediaRecorder();

       // myCamera.setDisplayOrientation(90);
       // mediaRecorder.setOrientationHint(90);

        myCamera.unlock();
      //  myCamera.setDisplayOrientation(90);
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));


        File VideoFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.VideoFolder);
        if (!VideoFolder.exists())
        {
            VideoFolder.mkdirs();

        }
        videoPathInFolder = Environment.getExternalStorageDirectory() + "/" + CommonInfo.VideoFolder + "/" +VideoName;
      //  dbEngine.open();
    /*  String VideoNameAndPath=  dbEngine.fetchTblFeedbackVideoDataFRomStoreIDAndProductID(STOREID,VIDEOTAG);
        if(!VideoNameAndPath.equals("0")){
            String VIDEO_NAME = VideoNameAndPath.split(Pattern.quote("$"))[0];
            String VIDEO_PATH = VideoNameAndPath.split(Pattern.quote("$"))[1];
            //delete from table and folder
            dbEngine.deletetblFeedbackVideoWhereVideoName(STOREID);
            File fdelete=new File(VIDEO_PATH);
            if (fdelete.exists()) {
                if (fdelete.delete()) {

                    callBroadCast();
                } else {

                }
            }
        }
        dbEngine.open();
        dbEngine.savetblFeedbackVideoData(STOREID,VIDEOTAG,VideoName,videoPathInFolder);

        dbEngine.close();*/

        mediaRecorder.setOutputFile(videoPathInFolder);

        //mediaRecorder.setOutputFile("/sdcard/myvideo.mp4");
        mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M

        //mediaRecorder.setMaxDuration(CommonInfo.MaxDuration); // Set max duration 60 sec.

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());
        mediaRecorder.setOrientationHint(CustomVideoCapture.orientation);

        try
        {
            mediaRecorder.prepare();

        }
        catch (IllegalStateException e)
        {
            releaseMediaRecorder();
            return false;
        }
        catch (IOException e)
        {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }


    @Override
    protected void onPause()
    {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();
        try {
            VideoDeleteFromFolderIfNotInDatabase();
        }
        catch(Exception e)
        {

        }
        finish();

    }

    private void releaseMediaRecorder()
    {
        if (mediaRecorder != null)
        {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera()
    {
        if (myCamera != null)
        {
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private Camera mCamera;
        private Activity mActivity;

        public MyCameraSurfaceView(Context context, Camera camera,Activity activity)
        {
            super(context);
            mCamera = camera;
            mActivity=activity;
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null)
            {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try
            {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try
            {
                setCameraDisplayOrientation(mActivity,0,mCamera);
                mCamera.setPreviewDisplay(mHolder);
              //  mCamera.setDisplayOrientation(90);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }

    public void onDestroy() {
        super.onDestroy();
        // unregister receiver
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();
    }
    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(CustomVideoCapture.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }
    public void ImageDeleteAndSaving(){
        String VideoNameAndPath=  dbEngine.fetchTblFeedbackVideoDataFRomStoreIDAndProductID(STOREID,VIDEOTAG);
        dbEngine.open();
        if(!VideoNameAndPath.equals("0")){
            String VIDEO_NAME = VideoNameAndPath.split(Pattern.quote("$"))[0];
            String VIDEO_PATH = VideoNameAndPath.split(Pattern.quote("$"))[1];
            //delete from table and folder
            dbEngine.deletetblFeedbackVideoWhereVideoName(VIDEO_NAME);
            File fdelete=new File(VIDEO_PATH);
            if (fdelete.exists()) {
                if (fdelete.delete()) {

                    callBroadCast();
                } else {

                }
            }
        }

        dbEngine.savetblFeedbackVideoData(STOREID,VIDEOTAG,VideoName,videoPathInFolder);

        dbEngine.close();
    }
    public void VideoDeleteFromFolderIfNotInDatabase(){
        String VideoNameAndPath=  dbEngine.fetchTblFeedbackVideoAvailable(VideoName);
        if(VideoNameAndPath.equals("0")){
            File fdelete=new File(videoPathInFolder);
            if (fdelete.exists()) {
                if (fdelete.delete()) {

                    callBroadCast();
                } else {

                }
            }

        }

    }
}

