package com.caption.update;

import android.content.Context;
import android.graphics.Color;

/**
 * 更新信息配置
 * Created by deng on 2016/11/11.
 */

public class UpdateConfig {

    private Context mContext;
    private int updateType ;
    private int  themeColor_360;
    private boolean isDebug;

    private UpdateConfig(Builder builder) {
        this.isDebug = builder.isDebug;
        this.mContext = builder.mContext;
        this.themeColor_360 = builder.themeColor_360;
        this.updateType = builder.updateType;
    }

    public Context getmContext() {
        return mContext;
    }

    public int getUpdateType() {
        return updateType;
    }

    public int getThemeColor_360() {
        return themeColor_360;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public static class Builder {
        public Context mContext;

        //更新类型
        public int updateType = UpdateUtils.CHECK_MANUAL;
        public boolean isDebug = true;

        //360
        public int themeColor_360 = Color.parseColor("#0A93DB");

        //baidu

        public Builder setUpdateType(int updateType){
            this.updateType = updateType;
            return this;
        }

        public Builder IsDebug(boolean isDebug){
            this.isDebug = isDebug;
            return this;
        }

        public Builder setThemeColor360(int themeColor_360){
            this.themeColor_360 = themeColor_360;
            return this;
        }

        public void Builder(Context mContext) {
            this.mContext = mContext;
        }

        public UpdateConfig build() {
            return new UpdateConfig(this);
        }
    }
}
