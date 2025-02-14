package kz.idc.rs.services.client.wifi.requests;

public class $WifiToolsRequests {

    private static WifiToolsRequests wifiToolsRequests;

    public static WifiToolsRequests mk(WifiConfiguration wifiConfiguration){
        if (wifiToolsRequests == null){
            wifiToolsRequests = new WifiToolsRequestsImpl(wifiConfiguration);
        }
        return wifiToolsRequests;
    }
}
