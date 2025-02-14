package kz.idc.rs.services.client.network.requests;

public class $NetworkToolsRequests {
    private static NetworkToolsRequestsImpl networkToolsRequests;

    public static NetworkToolsRequests mk(NetworkConfiguration configuration){
        if(networkToolsRequests == null){
            networkToolsRequests = new NetworkToolsRequestsImpl(configuration);
        }
        return networkToolsRequests;
    }
}
