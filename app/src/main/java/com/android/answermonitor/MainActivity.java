package com.android.answermonitor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends FragmentActivity{


    public static final int REQUEST_MEDIA_PROJECTION = 18;

    Button button;
    private static boolean isShow = false;

    /**
     * TessBaseAPI初始化用到的第一个参数，是个目录。
     */
    public static String DATAPATH;
    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    private static String tessdata;
    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    public static final String DEFAULT_LANGUAGE = "chi_sim";
    /**
     * assets中的文件名
     */
    private static final String DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
    /**
     * 保存到SD卡中的完整文件名
     */
    private static String LANGUAGE_PATH;

    /**
     * 权限请求值
     */
    private static final int PERMISSION_REQUEST_CODE = 10087;

    private static String DATABASE_PATH;
    private static String DATABASE_NAME = "sqlite-answer.db";
    private String openToDay = "1991-11-09";
    private int time = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashReport.initCrashReport(getApplicationContext());
        DATAPATH = this.getCacheDir().getAbsolutePath() + File.separator;
        tessdata = DATAPATH + "tessdata";
        LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;

        DATABASE_PATH = this.getDatabasePath(DATABASE_NAME).getPath();


        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.start_action);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !isToday(openToDay) && !isToday("2018-10-11") && !isToday("2018-10-10") && !isToday("2018-06-28")){
                    return;
                }
                if (!isShow) {
                    isShow = true;
                    startService();
                } else {
                    isShow = false;
                    stopService(new Intent(getApplicationContext(), FloatWindowsService.class));
                }
            }
        });
        requestCapturePermission();
    }

    public static boolean isToday(String day){
        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);
            Calendar cal = Calendar.getInstance();
            Date date = null;
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(day);
            cal.setTime(date);
            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);
                if (diffDay == 0) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void requestCapturePermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
                return;
            }
        }


    }

    private void startService(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            Toast.makeText(this, "无法使用", Toast.LENGTH_SHORT).show();
            return;
        } else {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                    getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(
                    mediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        }
        FileUtil.copyToSD(this, DATABASE_PATH, DATABASE_NAME);
        FileUtil.copyToSD(this, LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_MEDIA_PROJECTION && data != null) {
                FloatWindowsService.setResultData(data);
                startService(new Intent(getApplicationContext(), FloatWindowsService.class));
            } else if (requestCode == PERMISSION_REQUEST_CODE) {
                FileUtil.copyToSD(this, LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(time > 0){
            time--;
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            openToDay = simpleDateFormat.format(new Date());
        }
        return super.onTouchEvent(event);
    }
}
