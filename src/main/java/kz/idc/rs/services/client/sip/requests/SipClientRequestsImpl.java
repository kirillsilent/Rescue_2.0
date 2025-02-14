package kz.idc.rs.services.client.sip.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.sip.SipIgnoreAccDTO;
import kz.idc.dto.sip.acc.AccountDTO;

public class SipClientRequestsImpl implements SipClientRequests{

    private final SipClientConfiguration configuration;

    public SipClientRequestsImpl(SipClientConfiguration sipClientConfiguration){
        this.configuration = sipClientConfiguration;
    }

    @Override
    public HttpRequest<?> call() {
        return HttpRequest.GET(configuration.call());
    }

    @Override
    public HttpRequest<?> getSipStatus() {
        return HttpRequest.GET(configuration.getSipStatus());
    }

    @Override
    public HttpRequest<?> updateSipClientAccount(AccountDTO accountDTO) {
        return HttpRequest.POST(configuration.updateSipClient(), accountDTO);
    }

    @Override
    public HttpRequest<?> updateSipConfig(SipIgnoreAccDTO sipIgnoreAccDTO) {
        return HttpRequest.POST(configuration.updateSipConfig(), sipIgnoreAccDTO);
    }


    @Override
    public HttpRequest<?> updateIO(IODeviceDTO ioDeviceDTO) {
        return HttpRequest.POST(configuration.updateIO(), ioDeviceDTO);
    }

    @Override
    public HttpRequest<?> startEmergency() {
        return HttpRequest.GET(configuration.startEmergency());
    }

    @Override
    public HttpRequest<?> stopEmergency() {
        return HttpRequest.GET(configuration.stopEmergency());
    }
}
