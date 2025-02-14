package kz.idc.rs;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.rs.services.client.ClientAPIService;
import kz.idc.rs.services.client.ac.ACTools;
import kz.idc.rs.services.client.ac.requests.ACConfiguration;
import kz.idc.rs.services.client.sip.SipClient;
import kz.idc.rs.services.client.sip.requests.SipClientConfiguration;
import kz.idc.rs.services.iodevice.$IODeviceService;
import kz.idc.rs.services.iodevice.IODeviceService;

import java.util.List;


@Controller("/hardware")
public class Hardware {

    private static final String ROOT_AUDIO = "/audio";
    private static final String ROOT_CAMERA = "/camera";

    private final IODeviceService ioDeviceService = $IODeviceService.mk();
    private final ACTools acTools;
    private final SipClient sipClient;

    public Hardware(RxHttpClient rxHttpClient,
                    ACConfiguration acConfiguration,
                    SipClientConfiguration sipClientConfiguration){
        acTools = ClientAPIService.ac(rxHttpClient,
                acConfiguration);
        sipClient = ClientAPIService.sip(rxHttpClient, sipClientConfiguration);
    }

    @Get("/get/{hardwareType}")
    public Single<IODeviceDTO> getHardware(@NonNull String hardwareType) {
        return ioDeviceService.getHardware(hardwareType);
    }

    @Get( "/get")
    public Single<List<IODeviceDTO>> getHardwares() {
        return ioDeviceService.getHardwares();
    }

    @Get( ROOT_AUDIO + "/get_cards")
    public Maybe<Object> getAudioCards(@QueryValue String type) {
        return acTools.getAudioCards(type);
    }

    @Put(ROOT_AUDIO + "/set_card")
    public Maybe<Object> setAudioCard(IODeviceDTO ioDeviceDTO) {
        return acTools.setAudioCard(ioDeviceDTO);
    }

    @Post(ROOT_AUDIO + "/get_volume")
    public Maybe<Object> getVolume(IODeviceDTO ioDeviceDTO) {
        return acTools.getVolume(ioDeviceDTO);
    }

    @Put(ROOT_AUDIO + "/set_volume")
    public Maybe<HttpResponse<HttpStatus>> setVolume(@QueryValue int volume, IODeviceDTO ioDeviceDTO) {
        return acTools.setVolume(ioDeviceDTO, volume);
    }

    @Get(ROOT_CAMERA + "/get_cards")
    public Maybe<Object> getCameraCards() {
        return acTools.getCameras();
    }

    @Put("/set")
    public Maybe<IODeviceDTO> setHardware(IODeviceDTO ioDeviceDTO) {
        return ioDeviceService.setHardware(ioDeviceDTO).toMaybe()
                .flatMap(io -> sipClient.updateIO(ioDeviceDTO));
    }

    @Get(ROOT_CAMERA + "/is_available")
    public Maybe<Object> isCameraAvailable(@QueryValue String type) {
        return acTools.isAvailable(type);
    }

    @Get( ROOT_AUDIO + "/is_available")
    public Maybe<Object> isAudioAvailable(@QueryValue String type) {
        return acTools.isAvailable(type);
    }
}
