package kz.idc.rs.services.client;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.RxStreamingHttpClient;
import kz.idc.rs.services.client.ac.$ACTools;
import kz.idc.rs.services.client.ac.ACTools;
import kz.idc.rs.services.client.ac.requests.ACConfiguration;
import kz.idc.rs.services.client.api.$APIClient;
import kz.idc.rs.services.client.api.APIClient;
import kz.idc.rs.services.client.api.requests.APIConfiguration;
import kz.idc.rs.services.client.network.$NetworkTools;
import kz.idc.rs.services.client.network.NetworkTools;
import kz.idc.rs.services.client.network.requests.NetworkConfiguration;
import kz.idc.rs.services.client.sip.$SipClient;
import kz.idc.rs.services.client.sip.SipClient;
import kz.idc.rs.services.client.sip.requests.SipClientConfiguration;
import kz.idc.rs.services.client.wifi.$WifiTools;
import kz.idc.rs.services.client.wifi.requests.WifiConfiguration;
import kz.idc.rs.services.client.wifi.WifiTools;

import javax.inject.Singleton;

@Singleton
public class ClientAPIService {

    public static NetworkTools network(RxHttpClient rxHttpClient, NetworkConfiguration networkConfiguration) {
        return $NetworkTools.mk(rxHttpClient, networkConfiguration);
    }

    public static WifiTools wifi(RxHttpClient rxHttpClient, WifiConfiguration wifiConfiguration) {
        return $WifiTools.mk(rxHttpClient, wifiConfiguration);
    }

    public static ACTools ac(RxHttpClient rxHttpClient,
                             ACConfiguration acConfiguration) {
        return $ACTools.mk(rxHttpClient, acConfiguration);
    }

    public static SipClient sip(RxHttpClient rxHttpClient,
                                SipClientConfiguration sipClientConfiguration) {
        return $SipClient.mk(rxHttpClient, sipClientConfiguration);
    }

    public static APIClient api(RxHttpClient rxHttpClient,
                                RxStreamingHttpClient rxStreamingHttpClient,
                                APIConfiguration apiConfiguration,
                                NetworkTools networkTools,
                                SipClient sipClient,
                                ACTools acTools) {
        return $APIClient.mk(rxHttpClient,
                rxStreamingHttpClient,
                apiConfiguration,
                networkTools,
                sipClient,
                acTools);
    }

}
