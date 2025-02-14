package kz.idc.rs.services.client.ac;

import io.micronaut.http.client.RxHttpClient;
import kz.idc.rs.services.client.ac.requests.ACConfiguration;


public class $ACTools {

    private $ACTools(){}

    private static ACTools mACToolsImpl;

    public static ACTools mk(RxHttpClient rxHttpClient,
                             ACConfiguration configuration) {
        if (mACToolsImpl == null) {
            mACToolsImpl = new ACToolsImpl(rxHttpClient, configuration);
        }
        return mACToolsImpl;
    }
}
