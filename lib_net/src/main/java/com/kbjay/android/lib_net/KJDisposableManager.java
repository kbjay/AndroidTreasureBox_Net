package com.kbjay.android.lib_net;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 取消注册
 *
 * @author v-zewan
 * @time 2019/7/3 8:10
 **/
public class KJDisposableManager {

    private KJDisposableManager() {
    }

    private static class Holder {
        private static final KJDisposableManager INSTANCE = new KJDisposableManager();
    }

    public static KJDisposableManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 用于释放网络请求，在Activity 或者fragment destroy的时候
     */
    public HashMap<String, ArrayList<Subscription>> mMap = new HashMap<>();

    public void put(String tag, Subscription subscription) {
        if (mMap.containsKey(tag)) {
            ArrayList<Subscription> subscriptions = mMap.get(tag);
            subscriptions.add(subscription);
        } else {
            ArrayList<Subscription> subscriptions = new ArrayList<>();
            subscriptions.add(subscription);
            mMap.put(tag, subscriptions);
        }
    }

    public void clear(String tag) {
        if (mMap.containsKey(tag)) {
            ArrayList<Subscription> subscriptions = mMap.get(tag);
            for (int i = 0; i < subscriptions.size(); i++) {
                subscriptions.get(i).cancel();
            }
        }
        mMap.remove(tag);
    }
}
