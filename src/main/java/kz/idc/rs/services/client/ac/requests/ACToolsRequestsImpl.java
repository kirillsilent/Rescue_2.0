package kz.idc.rs.services.client.ac.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.audio.PlayDTO;
import kz.idc.dto.io.IODeviceDTO;

public class ACToolsRequestsImpl implements ACToolsRequests {

    private final ACConfiguration configuration;

    public ACToolsRequestsImpl(ACConfiguration configuration){
        this.configuration = configuration;
    }


    @Override
    public HttpRequest<?> getAudioCards(String type) {
        return HttpRequest.GET(configuration.getAudioCards(type));
    }

    @Override
    public HttpRequest<?> getCameras() {
        return HttpRequest.GET(configuration.getCameraCards());
    }

    @Override
    public HttpRequest<?> setVolume(int volume, IODeviceDTO ioDeviceDTO) {
        return HttpRequest.PUT(configuration.setVolume(volume), ioDeviceDTO);
    }

    @Override
    public HttpRequest<?> getVolume(IODeviceDTO ioDeviceDTO) {
        return HttpRequest.POST(configuration.getVolume(), ioDeviceDTO);
    }

    @Override
    public HttpRequest<?> audioIsAvailable(IODeviceDTO io) {
        return HttpRequest.POST(configuration.isAvailableAudio(), io);
    }

    @Override
    public HttpRequest<?> cameraIsAvailable(IODeviceDTO io) {
        return HttpRequest.POST(configuration.isAvailableCamera(), io);
    }

    @Override
    public HttpRequest<?> play(PlayDTO playDTO) {
        return HttpRequest.POST(configuration.play(), playDTO);
    }

    @Override
    public HttpRequest<?> stop() {
        return HttpRequest.GET(configuration.stop());
    }
}
