package kz.idc.rs;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.http.server.types.files.StreamedFile;
import io.reactivex.Maybe;
import kz.idc.rs.services.client.ClientAPIService;
import kz.idc.rs.services.client.ac.requests.ACConfiguration;
import kz.idc.rs.services.client.api.APIClient;
import kz.idc.rs.services.client.api.requests.APIConfiguration;
import kz.idc.rs.services.client.network.requests.NetworkConfiguration;
import kz.idc.rs.services.client.sip.requests.SipClientConfiguration;
import kz.idc.rs.services.rescue.$Streams;


@Controller("/rescue")
public class Rescue {

    private final APIClient apiClient;

    public Rescue(RxHttpClient rxHttpClient, RxStreamingHttpClient rxStreamingHttpClient,
                  APIConfiguration apiConfiguration,
                  NetworkConfiguration networkConfiguration,
                  ACConfiguration acConfiguration,
                  SipClientConfiguration sipClientConfiguration) {
        apiClient = ClientAPIService.api(rxHttpClient,
                rxStreamingHttpClient,
                apiConfiguration,
                ClientAPIService.network(rxHttpClient, networkConfiguration),
                ClientAPIService.sip(rxHttpClient, sipClientConfiguration),
                ClientAPIService.ac(rxHttpClient, acConfiguration));
    }

    @Get("/plan")
    public Maybe<StreamedFile> getPlan() {
        return $Streams.mk().getPlanStream();
    }

    public Maybe<HttpResponse<HttpStatus>> createEmergency() {
        return apiClient.createEmergency();
    }

}
