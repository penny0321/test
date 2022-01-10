package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int FILTER_ALL_APP = 0; // 所有应用程序
      public static final int FILTER_SYSTEM_APP = 1; // 系统程序
      public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
      public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
      private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_all).setOnClickListener(this::onClick);
        findViewById(R.id.btn_system).setOnClickListener(this::onClick);
        findViewById(R.id.btn_third).setOnClickListener(this::onClick);
        findViewById(R.id.btn_sdcard).setOnClickListener(this::onClick);
    }
    private void filterApp(int type) {
        // 获取PackageManager对象
        pm = getPackageManager();
        // 查询已经安装的应用程序
        List<ApplicationInfo> applicationInfos = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        // 排序
        Collections.sort(applicationInfos,
                new ApplicationInfo.DisplayNameComparator(pm));

        switch (type) {
            case FILTER_ALL_APP:// 所有应用
                for (ApplicationInfo applicationInfo : applicationInfos) {
                    getAppInfo(applicationInfo);
                }
                break;
            case FILTER_SYSTEM_APP:// 系统应用
                for (ApplicationInfo applicationInfo : applicationInfos) {
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        getAppInfo(applicationInfo);
                    }
                }
            case FILTER_THIRD_APP:// 第三方应用

                for (ApplicationInfo applicationInfo : applicationInfos) {
                    // 非系统应用
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        getAppInfo(applicationInfo);
                    }
                    // 系统应用，但更新后变成不是系统应用了
                    else if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        getAppInfo(applicationInfo);
                    }
                }
            case FILTER_SDCARD_APP:// SDCard应用
                for (ApplicationInfo applicationInfo : applicationInfos) {
                    if (applicationInfo.flags == ApplicationInfo.FLAG_SYSTEM) {
                        getAppInfo(applicationInfo);
                    }
                }
            default:
                break;
        }
    }

    /**
     * 获取应用信息
     */
    private void getAppInfo(ApplicationInfo applicationInfo) {
        String appName = applicationInfo.loadLabel(pm).toString();// 应用名
        String packageName = applicationInfo.packageName;// 包名
        System.out.println("应用名：" + appName + " 包名：" + packageName);
    }


    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_all:
                System.out.println("输出所有应用信息:\n");
                filterApp(FILTER_ALL_APP);
                break;
            case R.id.btn_system:
                System.out.println("输出系统应用信息:\n");
                filterApp(FILTER_SYSTEM_APP);
                break;
            case R.id.btn_third:
                System.out.println("输出第三方应用信息:\n");
                filterApp(FILTER_THIRD_APP);
                break;
            case R.id.btn_sdcard:
                System.out.println("输出SDCard应用信息:\n");
                filterApp(FILTER_SDCARD_APP);
                break;

            default:
                break;
        }
    }

}