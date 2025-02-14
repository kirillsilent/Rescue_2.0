package kz.idc.rs.services.client.sip.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.sip.SipIgnoreAccDTO;
import kz.idc.dto.sip.acc.AccountDTO;

public interface SipClientRequests {
    HttpRequest<?> call();
    HttpRequest<?> getSipStatus();
    HttpRequest<?> updateSipClientAccount(AccountDTO accountDTO);
    HttpRequest<?> updateSipConfig(SipIgnoreAccDTO sipIgnoreAccDTO);
    HttpRequest<?> updateIO(IODeviceDTO ioDeviceDTO);
    HttpRequest<?> startEmergency();
    HttpRequest<?> stopEmergency();
}
