package kz.idc.rs.services.client.sip;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Maybe;
import kz.idc.dto.ClearUIDTO;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.sip.IncomingDTO;
import kz.idc.dto.sip.SipIgnoreAccDTO;
import kz.idc.dto.StatusDTO;
import kz.idc.dto.sip.acc.AccWithRegServerDTO;
import kz.idc.rs.services.client.sip.requests.$SipClientRequests;
import kz.idc.rs.services.client.sip.requests.SipClientConfiguration;
import kz.idc.rs.services.client.sip.requests.SipClientRequests;
import kz.idc.utils.gpio.GPIO;
import kz.idc.utils.tprocess.$TProcess;
import kz.idc.ws.WebSocket;
import kz.idc.ws.WebSocketTopics;


public class SipClientImpl implements SipClient{

    public RxHttpClient rxHttpClient;
    private final SipClientRequests sipClientRequests;

    public SipClientImpl(RxHttpClient rxHttpClient, SipClientConfiguration configuration){
        this.rxHttpClient = rxHttpClient;
        this.sipClientRequests = $SipClientRequests.mk(configuration);
    }

    @Override
    public Maybe<StatusDTO> getSipStatus() {
        return rxHttpClient.retrieve(sipClientRequests.getSipStatus(), StatusDTO.class).firstElement()
                .onErrorResumeNext(t -> response -> response.onSuccess(StatusDTO.create(false)));
    }

    @Override
    public Maybe<Object> restart() {
        return Maybe.create(s -> {
                    $TProcess.mk().restartSipService();
                    s.onSuccess(HttpResponse.ok());
                }
        );
    }

    @Override
    public Maybe<Object> stop() {
        return Maybe.create(s -> {
            $TProcess.mk().stop();
            s.onSuccess(HttpResponse.ok());
        });
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> startEmergency() {
        clearLight();
        return rxHttpClient.exchange(sipClientRequests.startEmergency(), HttpStatus.class).firstElement();
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> stopEmergency() {
        return rxHttpClient.exchange(sipClientRequests.stopEmergency(), HttpStatus.class).firstElement();
    }

    @Override
    public Maybe<Object> incomingCall(WebSocket webSocket) {
        return Maybe.create(s -> {
            GPIO.startLight();
            webSocket.onMessage(WebSocketTopics.RESCUE.TOPIC, IncomingDTO.create(true));
            s.onSuccess(HttpResponse.ok());
        });
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> call() {
        return rxHttpClient.exchange(sipClientRequests.call(), HttpStatus.class).firstElement();
    }

    @Override
    public Maybe<IODeviceDTO> updateIO(IODeviceDTO ioDeviceDTO) {
        return rxHttpClient.exchange(sipClientRequests.updateIO(ioDeviceDTO)).firstElement()
                .flatMap(resp -> result -> result.onSuccess(ioDeviceDTO));
    }

    @Override
    public Maybe<Object> end(WebSocket webSocket) {
       return Maybe.create(s -> {
            clearLight();
            webSocket.onMessage(WebSocketTopics.RESCUE.TOPIC, ClearUIDTO.create(true));
            s.onSuccess(HttpResponse.ok());
        });
    }

    private void clearLight(){
        GPIO.stopLight();
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> updateSipAccount(AccWithRegServerDTO accWithRegServerDTO) {
        return rxHttpClient.exchange(sipClientRequests.updateSipClientAccount(accWithRegServerDTO.getAccount()), HttpStatus.class).firstElement();
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> updateSipConfig(SipIgnoreAccDTO sipIgnoreAccDTO) {
        return rxHttpClient.exchange(sipClientRequests.updateSipConfig(sipIgnoreAccDTO), HttpStatus.class).firstElement();
    }
}
