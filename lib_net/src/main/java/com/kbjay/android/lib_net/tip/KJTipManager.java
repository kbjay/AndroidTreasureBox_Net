package com.kbjay.android.lib_net.tip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 全局配置，所以使用单例管理tip,可以在app初始化的时候调用
 *
 * <p>
 * 一共4类
 * 1.跟服务端约定的，比如服务端检查name不合法，返回11110，等
 * 2.ioexception,网络异常
 * 3.httpExcetion,返回非200的网络请求，服务端异常
 * 4.其他未知异常
 */
public class KJTipManager {

    List<KJTipEntity> mCustomTipEntities = new ArrayList<>();
    HashMap<Integer, String> mCustomTips = new HashMap<>();

    KJTipEntity mIoExcetionTip;
    KJTipEntity mHttpExcetionTip;
    KJTipEntity mUnknowTip;

    private KJTipManager() {
    }

    public static KJTipManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final KJTipManager INSTANCE = new KJTipManager();
    }

    public void configTips(List<KJTipEntity> customTips, KJTipEntity ioExceptionTip, KJTipEntity httpExceptionTip, KJTipEntity unknowTip) {
        if (customTips != null && customTips.size() > 0) {
            mCustomTipEntities.clear();
            mCustomTipEntities.addAll(customTips);
            convert2map();
        }

        mIoExcetionTip = ioExceptionTip;
        mHttpExcetionTip = httpExceptionTip;
        mUnknowTip = unknowTip;
    }

    private void convert2map() {
        for (int i = 0; i < mCustomTipEntities.size(); i++) {
            mCustomTips.put(mCustomTipEntities.get(i).code, mCustomTipEntities.get(i).tipContent);
        }
        mCustomTipEntities.clear();
        mCustomTipEntities = null;
    }


    public HashMap<Integer, String> getCustomTips() {
        return mCustomTips;
    }

    public KJTipEntity getIOExceptionTip() {
        return mIoExcetionTip;
    }

    public KJTipEntity getHttpExceptionTip() {
        return mHttpExcetionTip;
    }

    public KJTipEntity getUnknowTip() {
        return mUnknowTip;
    }
}
