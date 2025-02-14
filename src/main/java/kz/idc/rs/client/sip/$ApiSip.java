package kz.idc.rs.client.sip;

import io.micronaut.http.client.RxHttpClient;
import kz.idc.rs.client.sip.requests.ApiSipConfig;

public class $ApiSip {

    private static ApiSip apiSip;

    public static ApiSip mk(RxHttpClient rxHttpClient, ApiSipConfig apiSipConfig){
        if(apiSip == null){
            apiSip = new ApiSipImpl(rxHttpClient, apiSipConfig);
        }
        return apiSip;
    }
}
