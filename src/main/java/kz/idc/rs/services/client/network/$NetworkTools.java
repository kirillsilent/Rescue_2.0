package kz.idc.rs.services.client.network;

import io.micronaut.http.client.RxHttpClient;
import kz.idc.rs.services.client.network.requests.NetworkConfiguration;


public class $NetworkTools {

    private $NetworkTools(){}

    private static NetworkToolsImpl mNetworkToolsClientImpl;

    public static NetworkToolsImpl mk(RxHttpClient rxHttpClient, NetworkConfiguration configuration) {
        if (mNetworkToolsClientImpl == null) {
            mNetworkToolsClientImpl = new NetworkToolsImpl(rxHttpClient, configuration);
        }
        return mNetworkToolsClientImpl;
    }
}
