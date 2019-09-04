package com.kbjay.android.lib_net.tip;

/**
 * 根据code码不同弹出不同的客户端提示
 */
public class KJTipEntity {
    public int code;
    public String tipContent;

    public KJTipEntity(int code, String tipContent) {
        this.code = code;
        this.tipContent = tipContent;
    }
}
