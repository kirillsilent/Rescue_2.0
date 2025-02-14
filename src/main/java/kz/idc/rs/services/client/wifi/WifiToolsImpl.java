package kz.idc.rs.services.client.wifi;

import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Maybe;
import kz.idc.dto.io.IOType;
import kz.idc.dto.wifi.*;
import kz.idc.error.$Error;
import kz.idc.rs.services.client.wifi.requests.$WifiToolsRequests;
import kz.idc.rs.services.client.wifi.requests.WifiConfiguration;
import kz.idc.rs.services.client.wifi.requests.WifiToolsRequests;
import kz.idc.utils.storage.$Storage;
import kz.idc.utils.storage.Storage;


public class WifiToolsImpl implements WifiTools {

    private final RxHttpClient httpClient;
    private final Storage storage = $Storage.mk();
    private final WifiToolsRequests wifiToolsRequests;

    public WifiToolsImpl(RxHttpClient httpClient,
                         WifiConfiguration configuration) {
        this.httpClient = httpClient;
        wifiToolsRequests = $WifiToolsRequests.mk(configuration);
    }

    @Override
    public Maybe<Object> getPoint() {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.retrieve(wifiToolsRequests.getPoint(io), PointDTO.class)
                        .firstElement()
                        .retryWhen(retry -> retry
                                .flatMap(nothing -> httpClient.retrieve(wifiToolsRequests.getPoint(io), PointDTO.class)))
                        .flatMap(point -> result -> result.onSuccess(point)))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> getPoints() {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.retrieve(wifiToolsRequests.getPoints(io), AdvancedPointsDTO.class)
                        .firstElement()
                        .flatMap(advancedPoints -> result -> result.onSuccess(advancedPoints)))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> getConnection(String point) {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.retrieve(wifiToolsRequests.getConnection(PointIsConnectedDTO
                        .create(point, io.getDevice())), PointStatusDTO.class)
                        .firstElement()
                        .flatMap(pointStatus -> result -> result.onSuccess(pointStatus)))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> connect(ConnectToPointDTO connectToPointDTO) {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.retrieve(wifiToolsRequests.connect(ConnectToPointDTO
                        .update(connectToPointDTO, io.getDevice())), PointStatusDTO.class)
                        .firstElement()
                        .flatMap(pointStatus -> result -> result.onSuccess(pointStatus)))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }
}
