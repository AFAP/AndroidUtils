package com.afap.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 网络相关工具类
 */
public class NetworkUtil {
    public static final int NETWORN_NONE = 0; // 未联网
    public static final int NETWORN_WIFI = 1; // 连着Wifi
    public static final int NETWORN_MOBILE = 2; // 手机移动数据
    public static final String IPADDRESS_NONE = "0.0.0.0"; //默认空地址

    /**
     * 获取手机联网方式。权限要求：ACCESS_NETWORK_STATE
     *
     * @param context 上下文
     * @return 获取手机联网状态
     */
    public static int getNetworkState(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            State state = null;
            // WIFI
            NetworkInfo wifiNetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo != null) {
                state = wifiNetworkInfo.getState();
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    return NETWORN_WIFI;
                }
            }

            // 手机数据网络
            NetworkInfo mobileNetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetworkInfo != null) {
                state = mobileNetworkInfo.getState();
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    return NETWORN_MOBILE;
                }
            }
            return NETWORN_NONE;
        } catch (Exception e) {
            return NETWORN_NONE;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        return getNetworkState(context) != NETWORN_NONE;
    }


    /**
     * 获取当前连接的WIFI的SSID,如当前未连接wifi，返回null
     *
     * @param context 上下文
     * @return 获取手机连接的wifi的SSID
     */
    public static String getConnectionSsid(Context context) {
        if (getNetworkState(context) != NETWORN_WIFI) {
            return null;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    /**
     * 获取手机IP地址，使用wifi或移动网络返回对应的地址，未联网，默认返回"0.0.0.0"
     *
     * @param context 上下文
     * @return 获取手机网络IP
     */
    public static String getMobileIp(Context context) {
        if (getNetworkState(context) == NETWORN_WIFI) {
            return getWifiIP(context);
        } else if (getNetworkState(context) == NETWORN_MOBILE) {
            return getMobileNetworkIp();
        } else {
            return IPADDRESS_NONE;
        }
    }

    /**
     * 获取手机IP，wifi下。权限要求：ACCESS_WIFI_STATE
     *
     * @param context 上下文
     * @return 获取手机网络IP
     */
    private static String getWifiIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return long2ip(wifiInfo.getIpAddress());
    }

    /**
     * 获取手机IP，移动网络。权限要求：android.permission.INTERNET
     *
     * @return 获取手机网络IP
     */
    private static String getMobileNetworkIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    // 此处一定要加上IP4
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IPADDRESS_NONE;
    }

    /**
     * 获取路由设备网关地址。权限要求：ACCESS_WIFI_STATE
     *
     * @param context 上下文
     * @return 网关地址
     */
    public static String getGateWay(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        return long2ip(dhcpInfo.gateway);
    }

    private static String long2ip(int ip) {
        int[] b = new int[4];
        b[0] = (ip >> 24) & 0xff;
        b[1] = (ip >> 16) & 0xff;
        b[2] = (ip >> 8) & 0xff;
        b[3] = ip & 0xff;

        return b[3] + "." + b[2] + "." + b[1] + "." + b[0];
    }
}