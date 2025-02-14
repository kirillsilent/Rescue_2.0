package kz.idc.rs;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.reactivex.Maybe;
import kz.idc.rs.services.client.ClientAPIService;
import kz.idc.rs.services.client.ac.requests.ACConfiguration;
import kz.idc.rs.services.client.api.APIClient;
import kz.idc.rs.services.client.api.requests.APIConfiguration;
import kz.idc.rs.services.client.network.requests.NetworkConfiguration;
import kz.idc.rs.services.client.sip.requests.SipClientConfiguration;
import kz.idc.ws.WebSocket;

@Controller("/marquee")
public class Marquee {
    private final APIClient apiClient;
    private final WebSocket webSocket;

    public Marquee(RxHttpClient rxHttpClient, RxStreamingHttpClient rxStreamingHttpClient,
                   APIConfiguration apiConfiguration,
                   NetworkConfiguration networkConfiguration,
                   SipClientConfiguration sipClientConfiguration,
                   ACConfiguration acConfiguration,
                   WebSocket webSocket) {
        this.webSocket = webSocket;
        apiClient = ClientAPIService.api(rxHttpClient,
                rxStreamingHttpClient,
                apiConfiguration,
                ClientAPIService.network(rxHttpClient, networkConfiguration),
                ClientAPIService.sip(rxHttpClient, sipClientConfiguration),
                ClientAPIService.ac(rxHttpClient, acConfiguration));
    }


    @Get("/get")
    public Maybe<Object> getMarquee(){
        return apiClient.getMarquees(webSocket);
    }
}
