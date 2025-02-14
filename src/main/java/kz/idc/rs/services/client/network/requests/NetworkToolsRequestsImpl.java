package kz.idc.rs.services.client.network.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.network.NetworkConfigDTO;

public class NetworkToolsRequestsImpl implements NetworkToolsRequests{

    private final NetworkConfiguration configuration;

    public NetworkToolsRequestsImpl(NetworkConfiguration configuration){
        this.configuration = configuration;
    }

    @Override
    public HttpRequest<?> getIpFromInterface(IODeviceDTO ioDeviceDTO) {
       return HttpRequest.GET(configuration.getIpPath(ioDeviceDTO.getDevice()));
    }

    @Override
    public HttpRequest<?> getIpFromVPNInterface(IODeviceDTO ioDeviceDTO) {
        return HttpRequest.GET(configuration.getVPNIpPath(ioDeviceDTO.getDevice()));
    }

    @Override
    public HttpRequest<?> getNetworkInterfaces(String type) {
        return HttpRequest.GET(configuration.getAllNetworkInterfaces(type));
    }

    @Override
    public HttpRequest<?> setNetworkConfig(NetworkConfigDTO networkConfig) {
        return HttpRequest.PUT(configuration.setNetworkConfig(), networkConfig);
    }

    @Override
    public HttpRequest<?> setNetworkInterface(IODeviceDTO io) {
        return HttpRequest.PUT(configuration.setDefaultNetworkInterface(), io);
    }

    @Override
    public HttpRequest<?> getCurrentNetworkInterface(IODeviceDTO io) {
        return HttpRequest.POST(configuration.getDefaultNetworkInterface(), io);
    }

    @Override
    public HttpRequest<?> isDefaultRoute(IODeviceDTO io) {
        return HttpRequest.GET(configuration.isDefaultRoute(io.getDevice()));
    }

    @Override
    public HttpRequest<?> addDefaultRoute(IODeviceDTO io) {
        return HttpRequest.PUT(configuration.addDefaultRoute(), io);
    }
}
