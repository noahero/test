package com.example.test35.deviceinfo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.example.test35.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 *
 *     String PK_INetIP;//互联网IP(后端系统从request获取)
 *     String PK_LNetIP;//局域网IP
 *     String PK_NetType;//当前使用网络（移动数据流量、WIFI）
 *     String PK_DeviceIp;//设备IP
 *     String PK_DeviceId;//设备ID(例：iOS 送IDFV)
 *     String PK_PrisonFlag;//越狱情况
 *     String PK_FirstInstall;//是否首次安装启动
 *     String PK_ProxyFlag;//应用之前是否连接过代理网络
 *     String PK_DeviceInfo;//设备型号
 *     String PK_SysInfo;//设备系统版本
 *     String PK_DeviceAlias;//设备别名
 *     String PK_AppPackage;//当前应用版本
 *     String PK_LngLat;//经纬度
 *     String PK_IMEI;//IMEI(安卓)
 *     String PK_DeviceSeq;//设备序列号(安卓)
 *     String PK_LoginMobile;//社区金融登录手机号(未登录放空)
 *     String PK_LoginL;//未登录、已登录社区金融、已登录网银状态
 *     String PK_IdNo;//用户证件号码
 *     String PK_CIfNo;//客户号
 *     String PK_UserLevel;//用户层级
 *     String PK_SIMInfo;//SIM数量及对应手机号
 *     String PK_ChannelId;//设备渠道标示(包括:手机银行iOS、手机银行安卓、商户收款iOS、商户收款安卓，例: 0:手机银行iOS、1:手机银行安卓、2:商户收款iOS、3:商户收款安卓 后续新增渠道再添加)
 */
public class DeviceInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.READ_PHONE_STATE)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                        }
                    })
                    .start();
        }
        AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 现在文件


                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                    }
                })
                .start();
    }

    /**
     * 获取mac地址，亲测有效
     * @return
     */
    public  String getNewMac() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            List<NetworkInterface> all = Collections.list(networkInterfaces);
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 由蓝牙那边获取设备mac地址，但是这个6.0以后就会变成02:00:00:00:00:00，已经不能正常使用了
     * @return
     */
    public  String getNewMacFromBluetoothAdapter(){
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getAddress();
        return deviceName;
    }


    /**
     * 获取wifi内网ip
     * @return
     */
    public  String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }


        return null;
    }
    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public  String getLocalIpAddress(Context context) {
        try {

            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
        // return null;
    }
    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public  String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    //GPRS连接下的ip
    public String getGPRSIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference", ex.toString());
        }
        return null;
    }
    /**获取手机的IMEI号码*/
    public  String getPhoneIMEI() {
        TelephonyManager mTm = (TelephonyManager) DeviceInfoActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        return imei;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getMac1(View view) {
        Log.e("tag1",getNewMac());
        Log.e("tag4",getLocalIpAddress());
        Log.e("tag5",getLocalIpAddress(this));
        Log.e("tag6",getGPRSIpAddress());
        Log.e("tag7",getPhoneIMEI());
    }
}
