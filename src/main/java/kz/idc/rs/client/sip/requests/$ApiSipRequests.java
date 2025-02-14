package kz.idc.rs.client.sip.requests;

public final class $ApiSipRequests {

    private $ApiSipRequests(){}

    private static ApiSipRequests apiSipRequests;

    public static ApiSipRequests mk(ApiSipConfig apiSipConfig){
        if(apiSipRequests == null){
            apiSipRequests = new ApiSipRequestsImpl(apiSipConfig);
        }
        return apiSipRequests;
    }

}
