package com.example.gosia.weightapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gosia.weightapplication.databinding.FragmentCameraBinding;
import com.example.gosia.weightapplication.model.WeightData;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraFragment extends Fragment {

    public final String LOGGER = this.getClass().getSimpleName();
    private FragmentCameraBinding fragmentCameraBinding;
    private final int REQUEST_CAMERA_PERMISSION_ID = 1001;
    private CameraSource cameraSource;
    private SurfaceView cameraView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_ID:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, viewGroup, false);

        //binding variable
        fragmentCameraBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_camera);
        fragmentCameraBinding.setVariable(this);

        startCamera();

        return view;
    }

    void startCamera() {
        cameraView = fragmentCameraBinding.surfaceView;

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Logger.e(LOGGER, "Detector dependencies aren't available yet");
        } else {
            cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION_ID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();

                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();

                    if (items.size() != 0) {
                        fragmentCameraBinding.textWeight.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }

                                String text = stringBuilder.toString();
                                Logger.d("Text from the camera: " + text);

                                fragmentCameraBinding.textWeight.setText(text.replaceAll("\\D+", ""));
                                Logger.d("only numbers " + text.replaceAll("\\D+", ""));

                            }
                        });
                    }
                }
            });
        }
    }

    public void typeText(View view) {
        Button mButtonType = fragmentCameraBinding.buttonType;
        EditText mEditText = fragmentCameraBinding.editText;

        if (mEditText.getVisibility() == View.VISIBLE) {
            mEditText.setVisibility(View.GONE);
            mButtonType.setVisibility(View.VISIBLE);
        } else {
            mEditText.setVisibility(View.VISIBLE);
            mButtonType.setVisibility(View.GONE);
        }
    }

    public void saveData(View view) {
        String weightType = fragmentCameraBinding.editText.getText().toString();
        String weightCamera = fragmentCameraBinding.textWeight.getText().toString();

        fragmentCameraBinding.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Logger.e("Enter pressed");

                    //hide_keyboard();
                }
                return false;
            }
        });

        if (weightType.length() != 0 && isNumeric(weightType)) {
            Logger.d("TYPE " + weightType);
            saveWeightValue(weightType);
        } else if (weightCamera.length() != 0 && weightType.length() == 0 && isNumeric(weightCamera)) {
            Logger.d("CAMERA " + weightCamera);
            saveWeightValue(weightCamera);
        }
    }

    public static boolean isNumeric(String str) {
        try {
            double num = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private void saveWeightValue(String value) {
        Log.d(LOGGER, "saveWeightValue : " + value);
        String actualDate = getActualDate();

        List<WeightData> data = SQLite.select().from(WeightData.class).queryList();

        int lastDayNumber;

        if (data.size() == 0) {
            lastDayNumber = 1;
        } else {
            String previousDate = data.get(data.size() - 1).getLastDayWeightMeasurement();
            int previousDayNumber = data.get(data.size() - 1).getDayNumber();

            int differenceBetweenDays = calculateDifferenceBetweenDates(actualDate, previousDate);
            lastDayNumber = previousDayNumber + differenceBetweenDays;
        }


        WeightData weightData = new WeightData();
        weightData.setLastDayWeightMeasurement(actualDate);
        weightData.setWeight(value);
        weightData.setDayNumber(lastDayNumber);
        weightData.save();

        List<WeightData> dataList = SQLite.select().
                from(WeightData.class).queryList();

        Logger.e(LOGGER + " size: " + dataList.size());

    }

    private String getActualDate() {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        return df.format(Calendar.getInstance().getTime());
    }

    private int calculateDifferenceBetweenDates(String stringActualDate, String stringLastDate) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        Date actualDate = null;
        Date lastDate = null;
        long differenceBetweenDays = -1;

        try {
            actualDate = df.parse(stringActualDate);
            lastDate = df.parse(stringLastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((actualDate != null) && (lastDate != null)) {
            differenceBetweenDays = ((((((actualDate.getTime() - lastDate.getTime()) / 1000) / 60)) / 60) / 24);
        }
        return (int) differenceBetweenDays;
    }
}
