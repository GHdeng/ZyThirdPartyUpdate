# 解决Android百度自动更新SDK和360自动更新SDK兼容


----------

最近上架的一个项目相序的被百度平台和360平台下架，郁闷的同时只能询问客服，得到的回复都是‘**平台的自动更新SDK会在您系统自动安装有害系统的软件，请使用本平台提供的更新SDK’，强制使用它平台的更新外还不可以兼容其他平台，对于这种臭不要脸的行为，作为开发者只能忌于百du 360的淫威寻找其他出路。

##解决方案
- 创建两个不同的Module，分别存放360更新sdk和百度更新sdk
- 两个Module中存放着相同的包名和方法名
- 分平台创建不同的渠道包，不同渠道引用不同的Module
- 上传到Jcenter方便引用，[发布开源项目到Jcenter/Bintray](http://blog.csdn.net/yanzhenjie1003/article/details/51672530)

![创建两个Module](http://img.blog.csdn.net/20161112152923898)

1.360更新工具类
```java
/**
 * 360更新工具类
 * Created by deng on 2016/11/11.
 */
public class UpdateUtils {
    /* 手动更新 ， 自动更新 ， 强制更新*/
    public static final int CHECK_MANUAL = 0, CHECK_AUTO = 1, CHECK_FORCE = 2;
    private UpdateUtils() {}
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
```

2.百度更新工具类
```java
//.....
/**
* 默认版本更新方法
* @param mContext
* @param checkUpdateType
*/
public void update(Context mContext, int checkUpdateType) {
        this.mContext = mContext;
        this.checkUpdateType = checkUpdateType;
        BDAutoUpdateSDK.cpUpdateCheck(mContext, this);
}
//.....
```

3.写UpdateConfig是为了方便自定义传递参数的Builder，例如360的字体颜色等，到最后发现作用并不大，这里就不贴出来了。

4.最后就是在App的build.gradle文件配置
```java
android {
    productFlavors {
        _360 {}
        baidu {}
    }
    productFlavors.all { flavor ->
        flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name]
    }
}
dependencies {
    _360Compile project(':360updatelibrary')
    baiduCompile project(':baiduupdatelibrary')
}
```
最后附上[github地址](https://github.com/GHdeng/ZyThirdPartyUpdate)

##Usage
由于我上传到Jcenter上方便调用

```java
compile 'com.caption:360updatelibrary:0.0.2'
compile 'com.caption:baiduupdatelibrary:0.0.2'
```

