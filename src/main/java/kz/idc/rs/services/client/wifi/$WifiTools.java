package kz.idc.rs.services.client.wifi;

import io.micronaut.http.client.RxHttpClient;
import kz.idc.rs.services.client.wifi.requests.WifiConfiguration;


public class $WifiTools {

    private $WifiTools(){}

    private static WifiToolsImpl mWifiToolsClientImpl;

    public static WifiToolsImpl mk(RxHttpClient rxHttpClient, WifiConfiguration configuration) {
        if (mWifiToolsClientImpl == null) {
            mWifiToolsClientImpl = new WifiToolsImpl(rxHttpClient, configuration);
        }
        return mWifiToolsClientImpl;
    }
}
