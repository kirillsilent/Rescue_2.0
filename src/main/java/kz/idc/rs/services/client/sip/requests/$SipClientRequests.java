package kz.idc.rs.services.client.sip.requests;


public class $SipClientRequests {

    private  $SipClientRequests(){}

    private static SipClientRequests sipClientRequests;

    public static SipClientRequests mk(SipClientConfiguration configuration){
        if(sipClientRequests == null){
            sipClientRequests = new SipClientRequestsImpl(configuration);
        }
        return sipClientRequests;
    }

}
