package kz.idc.rs.services.client.sip;

import io.micronaut.http.client.RxHttpClient;
import kz.idc.rs.services.client.sip.requests.SipClientConfiguration;

public class $SipClient {

    private static SipClient sipClient;

    public static SipClient mk(RxHttpClient rxHttpClient,
                               SipClientConfiguration sipClientConfiguration){
        if(sipClient == null){
            sipClient = new SipClientImpl(rxHttpClient, sipClientConfiguration);
        }
        return sipClient;
    }

}
