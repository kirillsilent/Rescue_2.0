package kz.idc.rs.services.client.ac;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Maybe;
import kz.idc.dto.VolumeDTO;
import kz.idc.dto.audio.PlayDTO;
import kz.idc.dto.io.IODeviceAvailableDTO;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.io.IODevicesDTO;
import kz.idc.dto.io.IOType;
import kz.idc.error.$Error;
import kz.idc.error.Errors;
import kz.idc.rs.services.client.ac.requests.$ACToolsRequests;
import kz.idc.rs.services.client.ac.requests.ACConfiguration;
import kz.idc.rs.services.client.ac.requests.ACToolsRequests;
import kz.idc.utils.storage.$Storage;
import kz.idc.utils.storage.Storage;

import java.util.List;


public class ACToolsImpl implements ACTools {

    private final RxHttpClient httpClient;
    private final ACToolsRequests acToolsRequests;
    private final Storage storage = $Storage.mk();

    public ACToolsImpl(RxHttpClient httpClient, ACConfiguration configuration) {
        this.httpClient = httpClient;
        acToolsRequests = $ACToolsRequests.mk(configuration);
    }

    @Override
    public Maybe<Object> getAudioCards(String type) {
        return httpClient.retrieve(acToolsRequests.getAudioCards(type), IODevicesDTO.class)
                .firstElement()
                .flatMap(ioDevices -> result -> result.onSuccess(ioDevices.getDevices()))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorACModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> setAudioCard(IODeviceDTO ioDeviceDTO) {
        return storage.setIO(ioDeviceDTO).toMaybe()
                .flatMap(input -> result -> result.onSuccess(input));
    }

    @Override
    public Maybe<Object> getCameras() {
        return httpClient.retrieve(acToolsRequests.getCameras(), IODevicesDTO.class)
                .firstElement()
                .flatMap(ioDevices -> result -> result.onSuccess(ioDevices.getDevices()))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorACModule(t.getMessage())));
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> setVolume(IODeviceDTO ioDeviceDTO, int volume) {
        return httpClient.exchange(acToolsRequests.setVolume(volume, ioDeviceDTO), HttpStatus.class)
                .firstElement()
                .onErrorResumeNext(t -> result -> result.onSuccess(HttpResponse.serverError()));
    }

    @Override
    public Maybe<Object> getVolume(IODeviceDTO ioDeviceDTO) {
        return httpClient.retrieve(acToolsRequests.getVolume(ioDeviceDTO), VolumeDTO.class)
                .firstElement()
                .flatMap(volume -> result -> result.onSuccess(volume))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorACModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> isAvailable(String type) {
        Maybe<IODeviceDTO> maybe = storage.getIO(type).toMaybe();
        return maybe.flatMap(io -> Maybe.create(check -> {
            if (io.getDevice() != null) {
                check.onSuccess(io);
            } else {
                check.onError(new Throwable(Errors.NULL_POINTER_AC_DEVICE.EXCEPTION));
            }
        }).flatMap(d -> createRequestAvailable(type, io))
                .flatMap(ioDevicesDTO -> Maybe.create(check -> {
                    List<IODeviceDTO> devices = ioDevicesDTO.getDevices();
                    IODeviceAvailableDTO ioDeviceAvailableDTO;
                    if (devices.isEmpty()) {
                        ioDeviceAvailableDTO = IODeviceAvailableDTO.create(type, false);
                    } else {
                        ioDeviceAvailableDTO = IODeviceAvailableDTO.create(type, devices.get(0).getDevice().equals(io.getDevice()));
                    }
                    check.onSuccess(ioDeviceAvailableDTO);
                }))).onErrorResumeNext(t -> result -> {
            if (t.getMessage().equals(Errors.NULL_POINTER_AC_DEVICE.EXCEPTION)) {
                result.onSuccess(IODeviceAvailableDTO.create(type, false));
            } else {
                result.onSuccess($Error.mk().createErrorACModule(t.getMessage()));
            }
        });
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> play(String track) {
        return storage.getIO(IOType.AUDIO_OUTPUT.DEVICE).toMaybe()
                .flatMap(io -> httpClient.exchange(acToolsRequests.play(PlayDTO.create(io, track)), HttpStatus.class)
                        .firstElement());
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> stop() {
        return httpClient.exchange(acToolsRequests.stop(), HttpStatus.class)
                        .firstElement();
    }

    private Maybe<IODevicesDTO> createRequestAvailable(String type, IODeviceDTO io) {
        if (type.equals(IOType.CAMERA.DEVICE)) {
            return httpClient.retrieve(acToolsRequests.cameraIsAvailable(io), IODevicesDTO.class).firstElement();
        } else {
            return httpClient.retrieve(acToolsRequests.audioIsAvailable(io), IODevicesDTO.class).firstElement();
        }
    }
}
