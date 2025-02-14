package kz.idc.rs.services.client.wifi.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.wifi.ConnectToPointDTO;
import kz.idc.dto.wifi.PointIsConnectedDTO;

public class WifiToolsRequestsImpl implements WifiToolsRequests{

    private final WifiConfiguration wifiConfiguration;

    public WifiToolsRequestsImpl(WifiConfiguration configuration){
        wifiConfiguration = configuration;
    }

    @Override
    public HttpRequest<?> getPoint(IODeviceDTO io) {
        return HttpRequest.GET(wifiConfiguration.getPoint(io.getDevice()));
    }

    @Override
    public HttpRequest<?> getPoints(IODeviceDTO io) {
        return HttpRequest.GET(wifiConfiguration.getPoints(io.getDevice()));
    }

    @Override
    public HttpRequest<?> getConnection(PointIsConnectedDTO pointIsConnected) {
        return HttpRequest.POST(wifiConfiguration.getConnection(), pointIsConnected);
    }

    @Override
    public HttpRequest<?> connect(ConnectToPointDTO connectToPoint) {
        return HttpRequest.POST(wifiConfiguration.connect(), connectToPoint);
    }
}
