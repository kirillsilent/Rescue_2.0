package kz.idc.dto.settings;

import io.micronaut.core.annotation.Introspected;
import kz.idc.dto.rescue.RescueDTO;
import kz.idc.dto.ServerAddressDTO;
import kz.idc.dto.sip.SipDTO;
import lombok.Getter;
import lombok.Setter;

@Introspected
@Getter
@Setter
public class ApiDTO {
    private RescueDTO rescueId;
    private ServerAddressDTO centralServer;
    private SipDTO sip;

    public static ApiDTO create (RescueDTO rescueDTO,
                                 ServerAddressDTO centralServer,
                                 SipDTO sipDTO){
        ApiDTO apiDTO = new ApiDTO();
        apiDTO.setRescueId(rescueDTO);
        apiDTO.setCentralServer(centralServer);
        apiDTO.setSip(sipDTO);
        return apiDTO;
    }
}
