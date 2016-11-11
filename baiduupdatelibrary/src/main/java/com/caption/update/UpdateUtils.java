package com.caption.update;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.Toast;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;

/**
 * baidu更新工具类
 * Created by deng on 2016/11/11.
 */

public class UpdateUtils implements CPCheckUpdateCallback {

    /* 手动更新 ， 自动更新 ， 强制更新*/
    public static final int CHECK_MANUAL = 0, CHECK_AUTO = 1, CHECK_FORCE = 2;

    private UpdateUtils() {
    }

    private static UpdateUtils instance;

    public static synchronized UpdateUtils getInit() {
        if (instance == null)
            instance = new UpdateUtils();
        return instance;
    }

    private Context mContext;
    private int checkUpdateType;

    /**
     * 默认版本更新方法
     *
     * @param mContext
     * @param checkUpdateType
     */
    public void update(Context mContext, int checkUpdateType) {

        this.mContext = mContext;
        this.checkUpdateType = checkUpdateType;

        BDAutoUpdateSDK.cpUpdateCheck(mContext, this);

        switch (checkUpdateType) {
            case CHECK_MANUAL:
                break;
            case CHECK_AUTO:
                break;
            case CHECK_FORCE:
                break;
            default:
                break;
        }
    }

    /**
     * 手动配置参数
     *
     * @param mContext
     * @param config
     */
    public void update(Context mContext, UpdateConfig config) {
        switch (config.getUpdateType()) {
            case CHECK_MANUAL:
                break;
            case CHECK_AUTO:
                break;
            case CHECK_FORCE:
                break;
            default:
                break;
        }
    }

    /**
     * 提示更新新版本
     */
    private void tipUpdateNewVersion(final AppUpdateInfo appUpdateInfo, final Context mContext) {
        AlertDialog.Builder idb = new AlertDialog.Builder(mContext);
        idb.setTitle(appUpdateInfo.getAppVersionName() + "版本更新内容");
        String newVersionValue = appUpdateInfo.getAppChangeLog();
        newVersionValue = newVersionValue.replaceAll("<br>", "\n");
        idb.setMessage(newVersionValue);
        final String newVersionValueStrTmp = newVersionValue;
        idb.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newVersionDown(appUpdateInfo, mContext, newVersionValueStrTmp);
            }
        });
        if (checkUpdateType == CHECK_FORCE) {
            idb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            idb.setCancelable(false);
            idb.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    System.exit(0);
                }
            });
        }
        idb.create().show();
    }

    /**
     * 提示更新新版本
     */
    private void tipUpdateNewVersion(final AppUpdateInfoForInstall appUpdateInfoForInstall, final Context mContext) {
        AlertDialog.Builder idb = new AlertDialog.Builder(mContext);
        idb.setTitle(appUpdateInfoForInstall.getAppVersionName() + "版本更新内容");
        String newVersionValue = appUpdateInfoForInstall.getAppChangeLog();
        newVersionValue = newVersionValue.replaceAll("<br>", "\n");
        idb.setMessage(newVersionValue);
        // idb.setMessage(newVersionValue.replaceAll("\\n", "\n"));
        idb.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BDAutoUpdateSDK.cpUpdateInstall(mContext, appUpdateInfoForInstall.getInstallPath());
            }
        });
        if (checkUpdateType == CHECK_FORCE) {
            idb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {// 强制更新
            idb.setCancelable(false);
            idb.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    System.exit(0);
                }
            });
        }
        idb.create().show();
    }

    /**
     * 下载新版本
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void newVersionDown(final AppUpdateInfo appUpdateInfo, final Context mContext, String newVersionValue) {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("正在下载更新包...");
        if (checkUpdateType == CHECK_FORCE) {
            pd.setCancelable(false);
        } else
            pd.setButton(DialogInterface.BUTTON_NEGATIVE, "后台更新",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface diaInterface, int arg1) {
                            diaInterface.dismiss();
                        }
                    });
        pd.show();

        BDAutoUpdateSDK.cpUpdateDownload(mContext, appUpdateInfo,
                new CPUpdateDownloadCallback() {

                    @Override
                    public void onStop() {
                        // activity.displayMessage("应用停止更新...");
                    }

                    @Override
                    public void onStart() {
                        // activity.displayMessage("应用开始更新...");
                    }

                    @SuppressLint("NewApi")
                    @Override
                    public void onPercent(int percent, long rcvLen, long fileSize) {
//                        LogHelper.e("UpdateNewVersion", percent + "%  " + rcvLen + "/" + fileSize);
                        if (pd != null) {
                            pd.setMax(Integer.valueOf(Long.toString(fileSize)));
                            pd.setProgress(Integer.valueOf(Long
                                    .toString(rcvLen)));
                            if (Build.VERSION.SDK_INT >= 11) {
                                float all = fileSize / 1024f / 1024f;
                                float downLength = rcvLen / 1024f / 1024f;
                                pd.setProgressNumberFormat(String.format("%.2fM/%.2fM", downLength, all));
                            }
                        }
                    }

                    @Override
                    public void onFail(Throwable error, String content) {

                    }

                    @Override
                    public void onDownloadComplete(String apkPath) {
                        if (pd != null)
                            pd.dismiss();
                        BDAutoUpdateSDK.cpUpdateInstall(mContext, apkPath);
                    }
                });
    }

    //百度检查更新回调
    @Override
    public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {
        if (appUpdateInfo == null && appUpdateInfoForInstall == null) {// 没有新版本
            if (checkUpdateType == CHECK_MANUAL)
                Toast.makeText(mContext, "当前已经是最新版本", Toast.LENGTH_SHORT).show();
        } else if (appUpdateInfo != null && appUpdateInfoForInstall != null) {// 有新版本且已经下载到本地
            tipUpdateNewVersion(appUpdateInfoForInstall, mContext);
        } else if (appUpdateInfo != null && appUpdateInfoForInstall == null) {// 有新版本
            tipUpdateNewVersion(appUpdateInfo, mContext);
        } else if (appUpdateInfo == null && appUpdateInfoForInstall != null) {// 新版本已经下载到本地
            tipUpdateNewVersion(appUpdateInfoForInstall, mContext);
        }
    }
}
