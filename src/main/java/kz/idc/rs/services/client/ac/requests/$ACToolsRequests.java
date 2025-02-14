package kz.idc.rs.services.client.ac.requests;

public class $ACToolsRequests {
    private static ACToolsRequestsImpl acToolsRequests;

    public static ACToolsRequests mk(ACConfiguration configuration){
        if(acToolsRequests == null){
            acToolsRequests = new ACToolsRequestsImpl(configuration);
        }
        return acToolsRequests;
    }
}
