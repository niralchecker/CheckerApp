package com.mor.sa.android.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.mor.sa.android.activities.R;

public class PreviewDemo extends Activity {
    public static boolean IsCrop;
    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private boolean inPreview = false;
    private boolean cameraConfigured = false;
    private ImageView capture;
    private ImageView swap;
    private TextView resolutions;
    private ListView resolutionsList;
    protected int selectedIndex;
    private OrientationEventListener mOrientationEventListener;
    protected int mOrientation;
    private boolean ShowResolutions;
    private static final int FOCUS_AREA_SIZE = 800;
    private ImageView crop;

    public void refresh_submit(final byte[] data) {
        class RefundTask extends AsyncTask<Void, Integer, String> {
            ProgressDialog dialogg = null;
            private String updateDate;

            @Override
            protected void onPreExecute() {
                dialogg = new ProgressDialog(PreviewDemo.this);
                dialogg.setMessage(getString(R.string.savingImage));
                dialogg.setCancelable(false);
                dialogg.show();
                this.updateDate = null;
            }

            @Override
            protected void onPostExecute(String path) {
                try {
                    dialogg.dismiss();
                } catch (Exception ex) {

                }
                Intent result = new Intent();
                result.putExtra("jpg", path);
                setResult(RESULT_OK, result);
                PreviewDemo.this.finish();
            }

            public boolean isSamsung() {
                final String AMAZON = "samsung";

                return (Build.MANUFACTURER.equals(AMAZON));
            }

            @Override
            protected String doInBackground(Void... params) {
                File file = new File(CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
                        + "/checkerimgss");
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length);

                    if (bitmap != null) {
                        int orient = mOrientation;
                        if (isSamsung())
                            orient += 90;
                        if (orient == 360)
                            orient = 0;
                        if (orient > 0) {
                            // Notice that width and height are reversed

                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();
                            // Setting post rotate to 90
                            Matrix mtx = new Matrix();
                            mtx.postRotate(orient);
                            // Rotating Bitmap
                            Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, w, h,
                                    mtx, true);
                            bitmap.recycle();
                            bitmap = bm;
                        }
                        if (!file.isDirectory()) {
                            file.mkdir();
                        }
                        Intent i = getIntent();
                        String name = "";
                        // i.putExtra("DATAID", dataid);
                        // i.putExtra("ORDERID", orderID);

                        String newPAth = "CAM_O"
                                + i.getExtras().getString("ORDERID") + "_DLAST"
                                + "_T" + System.currentTimeMillis() / 1000
                                + ".jpg";

                        String reso = "W" + sizes.get(selectedIndex).width
                                + "H" + sizes.get(selectedIndex).height;
                        if (i != null
                                && i.getExtras().getString("ORDERID") != null
                                && i.getExtras().getString("DATAID") == null) {
                            name = "CAM" + reso + "_Or"
                                    + i.getExtras().getString("ORDERID")
                                    + "_DLAST" + "_T"
                                    + System.currentTimeMillis() / 1000
                                    + ".jpg";
                            newPAth = "CAM" + reso + "_O"
                                    + i.getExtras().getString("ORDERID")
                                    + "_DLAST" + "_T"
                                    + System.currentTimeMillis() / 1000
                                    + ".jpg";
                        } else if (i != null
                                && i.getExtras().getString("ORDERID") != null
                                && i.getExtras().getString("DATAID") != null) {
                            name = "CAM" + reso + "_Or"
                                    + i.getExtras().getString("ORDERID") + "_D"
                                    + i.getExtras().getString("DATAID") + "_T"
                                    + System.currentTimeMillis() / 1000
                                    + ".jpg";
                            newPAth = "CAM" + reso + "_O"
                                    + i.getExtras().getString("ORDERID") + "_D"
                                    + i.getExtras().getString("DATAID") + "_T"
                                    + System.currentTimeMillis() / 1000
                                    + ".jpg";
                        }

                        file = new File(
                                CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
                                        + "/checkerimgss", name);

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(
                                    file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                    fileOutputStream);

                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                        if (shouldCreateBackup()) {
                            file = new File(
                                    CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
                                            + "/checkerimgss", newPAth);
                            Log.e("TAG", "doInBackground: " + newPAth);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(
                                        file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                                        fileOutputStream);

                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                        bitmap.recycle();

                    }
                }

                return file.getAbsolutePath();
            }
        }

        RefundTask refundtaskobj = new RefundTask();
        refundtaskobj.execute();
    }

    public boolean shouldCreateBackup() {
        return false;
        // myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        // return myPrefs.getBoolean(Constants.SETTINGS_BACKUP, true);
    }

    private void copy(File src, File dst) throws IOException {

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // i.putExtra("DATAID", dataid);
        // i.putExtra("ORDERID", orderID);

        setContentView(R.layout.preview_questionnaire);
        crop = (ImageView) findViewById(R.id.btnCrop);
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_CROPPING,
                false)) {
            crop.setVisibility(RelativeLayout.VISIBLE);
        } else crop.setVisibility(RelativeLayout.INVISIBLE);
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_RESOLUTION, false)) {
            this.ShowResolutions = true;
        } else
            this.ShowResolutions = false;

        dialog = (RelativeLayout) findViewById(R.id.imgdialog);
        resolutions = (TextView) findViewById(R.id.resolutions);
        resolutionsList = (ListView) findViewById(R.id.resolutionsList);
        if (ShowResolutions) {
            resolutions.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resolutionsList.getVisibility() == RelativeLayout.GONE)
                        resolutionsList.setVisibility(RelativeLayout.VISIBLE);
                    else
                        resolutionsList.setVisibility(RelativeLayout.GONE);
                }
            });
        } else {
            resolutions.setVisibility(RelativeLayout.GONE);
        }
        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        capture = (ImageView) findViewById(R.id.btnCapture);
        swap = (ImageView) findViewById(R.id.btnSwap);
        swap.setTag((R.drawable.flash_day + ""));
        if (!PreviewDemo.this.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH))
            swap.setVisibility(RelativeLayout.GONE);
        swap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    if (swap.getTag() != null
                            && ((String) swap.getTag())
                            .equals((R.drawable.flash_day + ""))) {
                        swap.setTag((R.drawable.flash_night + ""));
                        swap.setImageResource(R.drawable.flash_night);
                        Parameters params = camera.getParameters();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                        camera.setParameters(params);
                    } else {
                        swap.setTag((R.drawable.flash_day + ""));
                        swap.setImageResource(R.drawable.flash_day);
                        Parameters params = camera.getParameters();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(params);
                    }
                } catch (Exception ex) {

                }
            }
        });
        // swap.setVisibility(RelativeLayout.GONE);
        capture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PreviewDemo.IsCrop = false;
                    AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                    camera.enableShutterSound(false);

                    camera.autoFocus(new AutoFocusCallback() {

                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            takePicture();
                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(PreviewDemo.this,
                            "Error taking picture, please try again", 200);
                    finish();
                }
            }
        });
    }

    public void takePicture() {
        try {
            camera.takePicture(null, null, new PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                    swap.setVisibility(RelativeLayout.GONE);
                    capture.setVisibility(RelativeLayout.GONE);
                    setImageView(data);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(PreviewDemo.this, "Camera failed pls try again",
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_CANCELED, null);
            PreviewDemo.this.finish();
        }
    }

    private static final int ORIENTATION_PORTRAIT_NORMAL = 0;
    private static final int ORIENTATION_LANDSCAPE_NORMAL = 270;
    private static final int ORIENTATION_PORTRAIT_INVERTED = 180;
    private static final int ORIENTATION_LANDSCAPE_INVERTED = 90;

    @Override
    public void onResume() {
        super.onResume();

        camera = Camera.open();
        startPreview();

        if (mOrientationEventListener == null) {
            mOrientationEventListener = new OrientationEventListener(this,
                    SensorManager.SENSOR_DELAY_NORMAL) {

                @Override
                public void onOrientationChanged(int orientation) {

                    // determine our orientation based on sensor response
                    int lastOrientation = mOrientation;

                    if (!inPreview)
                        return;
                    if (orientation >= 315 || orientation < 45) {
                        mOrientation = ORIENTATION_PORTRAIT_NORMAL;

                    } else if (orientation < 315 && orientation >= 225) {
                        mOrientation = ORIENTATION_LANDSCAPE_NORMAL;

                    } else if (orientation < 225 && orientation >= 135) {
                        mOrientation = ORIENTATION_PORTRAIT_INVERTED;

                    } else { // orientation <135 && orientation > 45
                        mOrientation = ORIENTATION_LANDSCAPE_INVERTED;

                    }

                }
            };
        }
        if (((OrientationEventListener) mOrientationEventListener)
                .canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    @Override
    public void onPause() {
        if (inPreview) {
            camera.stopPreview();
        }

        camera.release();
        camera = null;
        inPreview = false;

        super.onPause();
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return (result);
    }

    List<Size> sizes = null;
    protected SharedPreferences myPrefs;
    protected RelativeLayout dialog;
    private ZoomControls zoomControls;
    private boolean upsideDown = false;

    private void initPreview(int width, int height) {
        if (camera != null && previewHolder.getSurface() != null) {

            preview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            focusOnTouch(event);
                        } catch (Exception ex) {

                        }
                    }
                    return true;
                }
            });

            try {
                camera.setPreviewDisplay(previewHolder);
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback",
                        "Exception in setPreviewDisplay()", t);
                Toast.makeText(PreviewDemo.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            if (!cameraConfigured) {
                Camera.Parameters parameters = camera.getParameters();
                if (parameters.getSupportedFocusModes().contains(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    parameters
                            .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                } else if (parameters.getSupportedFocusModes().contains(
                        Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                Camera.Size size = getOptimalPreviewSize(
                        parameters.getSupportedPreviewSizes(), width, height);

                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);

                }

                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Log.e("TAG", "initPreview: "+ Build.VERSION.SDK_INT);
                        parameters.set("orientation", "portrait");
                        camera.setDisplayOrientation(90);
                        parameters.setRotation(0);
                    } else {
                        parameters.set("orientation", "portrait");
                        camera.setDisplayOrientation(90);
                        parameters.setRotation(90);
                    }

                } else {
                    // This is an undocumented although widely known feature
                    parameters.set("orientation", "landscape");
                    // For Android 2.2 and above
                    camera.setDisplayOrientation(0);
                    // Uncomment for Android 2.0 and above
                    parameters.setRotation(0);
                }
                sizes = parameters.getSupportedPictureSizes();
                Camera.Size sizePicture = null;
                int tempIndex = 0;
                if (sizes != null && sizes.size() > 0) {

                    if (sizes.get(0).height > sizes.get(sizes.size() - 1).height) {
                        tempIndex = 0;
                        upsideDown = false;
                    } else {
                        tempIndex = sizes.size() - 1;
                        upsideDown = true;
                    }

                    if (sizes.size() > 3) {
                        sizes.remove(0);
                        sizes.remove(sizes.size() - 1);
                    }

                    if (sizes.size() > 10) {
                        sizes.remove(0);
                        sizes.remove(0);
                        sizes.remove(sizes.size() - 1);
                        sizes.remove(sizes.size() - 1);
                    }

                }

                cameraConfigured = true;
                ArrayAdapter adapter = new ArrayAdapter(this,
                        R.layout.custom_preview_row_night, getSizes(sizes));
                resolutionsList.setAdapter(adapter);
                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                selectedIndex = myPrefs.getInt(Constants.PREVIEW_ITEM,
                        tempIndex);
                if (selectedIndex >= sizes.size())
                    selectedIndex = sizes.size() - 1;
                sizePicture = sizes.get(selectedIndex);
                resolutions.setText("W=" + sizePicture.width + " H="
                        + sizePicture.height);

                parameters
                        .setPictureSize(sizePicture.width, sizePicture.height);
                camera.setParameters(parameters);
                resolutionsList.setItemChecked(selectedIndex, true);
                resolutionsList
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, final int position, long id) {

                                if ((position > 0 && upsideDown == false)
                                        || (position < sizes.size() - 1 && upsideDown == true)) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            PreviewDemo.this);
                                    alert.setCancelable(false);
                                    alert.setTitle("");
                                    TextView textView = new TextView(
                                            PreviewDemo.this);
                                    textView.setTextSize(UIHelper.getFontSize(
                                            PreviewDemo.this,
                                            textView.getTextSize()));

                                    String aler = "You have selected low resolution while camera highest resolution is "
                                            + sizes.get(0).width
                                            + "x"
                                            + sizes.get(0).height
                                            + ", Are you sure you want to take picture with this resolution? Important: This will become default resolution on camera while using application.";
                                    if (upsideDown)
                                        aler = "You have selected low resolution while camera highest resolution is "
                                                + sizes.get(sizes.size() - 1).width
                                                + "x"
                                                + sizes.get(sizes.size() - 1).height
                                                + ", Are you sure you want to take picture with this resolution? Important: This will become default resolution on camera while using application.";
                                    textView.setText(aler);
                                    alert.setView(textView);
                                    alert.setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    changePrefs(position);
                                                }
                                            });

                                    alert.setNegativeButton(
                                            "No",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    resolutionsList
                                                            .setVisibility(RelativeLayout.GONE);

                                                }
                                            });
                                    alert.show();
                                } else if (sizes != null
                                        && sizes.size() > position) {
                                    changePrefs(position);
                                }
                            }

                            private void changePrefs(int position) {
                                myPrefs = getSharedPreferences("pref",
                                        MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = myPrefs
                                        .edit();
                                prefsEditor.putInt(Constants.PREVIEW_ITEM,
                                        position);
                                prefsEditor.commit();

                                Camera.Parameters cp = camera.getParameters();
                                selectedIndex = position;
                                cp.setPictureSize(sizes.get(position).width,
                                        sizes.get(position).height);
                                resolutions.setText("W="
                                        + sizes.get(position).width + " H="
                                        + sizes.get(position).height);
                                camera.setParameters(cp);
                                resolutionsList
                                        .setVisibility(RelativeLayout.GONE);
                                resolutionsList.setItemChecked(position, true);

                            }
                        });
            }
        }
    }

    /**
     * Enables zoom feature in native camera . Called from listener of the view
     * used for zoom in and zoom out.
     *
     * @param zoomInOrOut "false" for zoom in and "true" for zoom out
     */
    public void zoomCamera(boolean zoomInOrOut) {
        if (camera != null) {
            Parameters parameter = camera.getParameters();

            if (parameter.isZoomSupported()) {
                int MAX_ZOOM = parameter.getMaxZoom();
                int currnetZoom = parameter.getZoom();
                if (zoomInOrOut && (currnetZoom < MAX_ZOOM && currnetZoom >= 0)) {
                    parameter.setZoom(++currnetZoom);
                } else if (!zoomInOrOut
                        && (currnetZoom <= MAX_ZOOM && currnetZoom > 0)) {
                    parameter.setZoom(--currnetZoom);
                }
            } else
                Toast.makeText(PreviewDemo.this, "Zoom Not Available",
                        Toast.LENGTH_LONG).show();

            camera.setParameters(parameter);
        }
    }

    private void enableZoom() {
        zoomControls = new ZoomControls(this);
        zoomControls.setIsZoomInEnabled(true);
        zoomControls.setIsZoomOutEnabled(true);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                zoomCamera(true);
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                zoomCamera(false);
            }
        });
        ((RelativeLayout) (findViewById(R.id.previewLayout)))
                .addView(zoomControls);
    }

    private void setImageView(final byte[] data) {
        dialog.setVisibility(RelativeLayout.VISIBLE);
        camera.stopPreview();
        inPreview = false;
        findViewById(R.id.btnCross).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (PreviewDemo.this.getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA_FLASH))
                    swap.setVisibility(RelativeLayout.VISIBLE);
                capture.setVisibility(RelativeLayout.VISIBLE);
                camera.startPreview();
                inPreview = true;
                dialog.setVisibility(RelativeLayout.GONE);
            }
        });
        findViewById(R.id.btnTick).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PreviewDemo.IsCrop = false;
                refresh_submit(data);
                dialog.setVisibility(RelativeLayout.GONE);
            }
        });
        crop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                PreviewDemo.IsCrop = true;
                refresh_submit(data);
                dialog.setVisibility(RelativeLayout.GONE);

            }
        });
    }

    private void focusOnTouch(MotionEvent event) {
        if (camera != null) {

            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                // Log.i(TAG, "fancy !");
                Rect rect = calculateFocusArea(event.getX(), event.getY());

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);

                camera.setParameters(parameters);
                camera.autoFocus(mAutoFocusTakePictureCallback);
            } else {
                camera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / preview.getWidth()) * 2000 - 1000)
                .intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / preview.getHeight()) * 2000 - 1000)
                .intValue(), FOCUS_AREA_SIZE);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top
                + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("tap_to_focus", "success!");
                camera.cancelAutoFocus();
            } else {
                // do something...
                Log.i("tap_to_focus", "fail!");
            }
        }
    };

    private String[] getSizes(List<Size> sizes) {
        if (sizes == null)
            return null;
        String[] items = new String[sizes.size()];
        for (int i = 0; i < sizes.size(); i++) {
            items[i] = "W= " + sizes.get(i).width + " H= "
                    + sizes.get(i).height;
        }
        return items;
    }

    public static int getDeviceCurrentOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        Log.d("Utils", "Current orientation = " + rotation);
        return rotation;
    }

    public void setCameraDisplayOrientation(Context context, Camera mCamera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(0, info); // Use the first

        if (info.canDisableShutterSound) {
            mCamera.enableShutterSound(false);
        }
        // rear-facing camera
       int rotation = getDeviceCurrentOrientation(context);

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    private void startPreview() {
        if (cameraConfigured && camera != null) {
            setCameraDisplayOrientation(PreviewDemo.this, camera);
            camera.startPreview();
            inPreview = true;
            enableZoom();
        }
    }

    private Camera.Size getOptimalPictureSize(List<Camera.Size> sizes, int w,
                                              int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w,
                                              int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            initPreview(width, height);
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };

    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {
        if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage,
                    projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static byte[] rotateImageIfRequired(Context context, Uri uri, byte[] fileBytes) {
        byte[] data = null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);
        ByteArrayOutputStream outputStream = null;
        try {
            bitmap = rotateImageIfRequired(bitmap, context, uri);
            outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            data = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // Intentionally blank
            }
        }

        return data;
    }
}