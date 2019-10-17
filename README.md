# test
1、lambda测试
1、没有稳定的API可以让开发者获取到这样的设备ID，
随着项目的演进， 越来越多的地方需要用到设备ID；
然而随着Android版本的升级，获取设备ID却越来越难了。
加上Android平台碎片化的问题，获取设备ID之路，可以说是步履维艰。


DeviceId  IMEI：
IMEI本该最理想的设备ID，具备唯一性，恢复出厂设置不会变化（真正的设备相关）。然而，获取IMEI需要 READ_PHONE_STATE 权限，估计大家也知道这个权限有多麻烦了。尤其是Android 6.0以后, 这类权限要动态申请，很多用户可能会选择拒绝授权。而且，Android 10.0 将彻底禁止第三方应用获取设备的IMEI, 即使申请了 READ_PHONE_STATE 权限。所以，如果是新APP，不建议用IMEI作为设备标识；如果已经用IMEI作为标识，要赶紧做兼容工作了，尤其是做新设备标识和IMEI的映射。

设备序列号：
通过android.os.Build.SERIAL获得，由厂商提供。如果厂商比较规范的话，设备序列号+Build.MANUFACTURER应该能唯一标识设备。但现实是并非所有厂商都按规范来，尤其是早期的设备。最致命的是，Android 8.0 以上，android.os.Build.SERIAL 总返回 “unknown”；若要获取序列号，可调用Build.getSerial() ，但是需要申请 READ_PHONE_STATE 权限。

MAC地址：
获取MAC地址也是越来越困难了，Android 6.0以后通过 WifiManager 获取到的mac将是固定的：02:00:00:00:00:00 ，再后来连读取 /sys/class/net/wlan0/address 也获取不到了。目前只能通过NetworkInterface获取了


ANDROID_ID：
Android ID 是获取门槛最低的，不需要任何权限，64bit 的取值范围，唯一性算是很好的了。但是不足之处也很明显：
刷机、root、恢复出厂设置等会使得 Android ID 改变；Android 8.0之后，Android ID的规则发生了变化：
对于升级到8.0之前安装的应用，ANDROID_ID会保持不变。如果卸载后重新安装的话，ANDROID_ID将会改变。
对于安装在8.0系统的应用来说，ANDROID_ID根据应用签名和用户的不同而不同。ANDROID_ID的唯一决定于应用签名、用户和设备三者的组合。

两个规则导致的结果就是：
第一，如果用户安装APP设备是8.0以下，后来卸载了，升级到8.0之后又重装了应用，Android ID不一样；
第二，不同签名的APP，获取到的Android ID不一样。
其中第二点可能对于广告联盟之类的有所影响（如果彼此是用Android ID对比数据的话），所以Google文档中说“请使用Advertising ID”，不过大家都知道，Google的服务在国内用不了。

对Android ID做了约束，对隐私保护起到一定作用，并且用来做APP自己的活跃统计也还是没有问题的。

