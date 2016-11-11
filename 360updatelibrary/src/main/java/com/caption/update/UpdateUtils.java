package com.caption.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;

import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;

/**
 * 360更新工具类
 * Created by deng on 2016/11/11.
 */

public class UpdateUtils {

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

    /**
     * 默认版本更新方法
     * @param mContext
     * @param checkUpdateType
     */
    public void update(Context mContext, int checkUpdateType) {
        String packageName = getPackageInfo(mContext).packageName;
        long intervalMillis = 10 * 1000L;           //第一次调用startUpdateSilent出现弹窗后，如果10秒内进行第二次调用不会查询更新
        switch (checkUpdateType) {
            case CHECK_MANUAL:
                UpdateHelper.getInstance().init(mContext, Color.parseColor("#0A93DB"));
                UpdateHelper.getInstance().setDebugMode(false);
                UpdateHelper.getInstance().manualUpdate(packageName);
                break;
            case CHECK_AUTO:
                UpdateHelper.getInstance().init(mContext, Color.parseColor("#0A93DB"));
                UpdateHelper.getInstance().setDebugMode(false);
                UpdateHelper.getInstance().autoUpdate(packageName, false, intervalMillis);
                break;
            case CHECK_FORCE:
                UpdateHelper.getInstance().init(mContext, Color.parseColor("#0A93DB"));
                UpdateHelper.getInstance().setDebugMode(false);
                UpdateHelper.getInstance().autoUpdate(packageName, true, intervalMillis);
                break;
            default:
                break;
        }
    }

    /**
     * 手动配置参数
     * @param mContext
     * @param config
     */
    public void update(Context mContext,UpdateConfig config){
        String packageName = getPackageInfo(mContext).packageName;
        long intervalMillis = 10 * 1000L;           //第一次调用startUpdateSilent出现弹窗后，如果10秒内进行第二次调用不会查询更新
        switch (config.getUpdateType()) {
            case CHECK_MANUAL:
                UpdateHelper.getInstance().init(mContext, config.getThemeColor_360());
                UpdateHelper.getInstance().setDebugMode(config.isDebug());
                UpdateHelper.getInstance().manualUpdate(packageName);
                break;
            case CHECK_AUTO:
                UpdateHelper.getInstance().init(mContext, config.getThemeColor_360());
                UpdateHelper.getInstance().setDebugMode(config.isDebug());
                UpdateHelper.getInstance().autoUpdate(packageName, false, intervalMillis);
                break;
            case CHECK_FORCE:
                UpdateHelper.getInstance().init(mContext, config.getThemeColor_360());
                UpdateHelper.getInstance().setDebugMode(config.isDebug());
                UpdateHelper.getInstance().autoUpdate(packageName, true, intervalMillis);
                break;
            default:
                break;
        }
    }

    /**
     * 获取App包 信息版本号
     *
     * @param context
     * @return
     */
    private PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}
