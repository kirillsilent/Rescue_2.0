package kz.idc.rs.client.sip;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kz.idc.Application;
import kz.idc.error.$Error;
import kz.idc.rs.client.sip.requests.$ApiSipRequests;
import kz.idc.rs.client.sip.requests.ApiSipConfig;
import kz.idc.rs.client.sip.requests.ApiSipRequests;
import kz.idc.utils.tprocess.$TProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class ApiSipImpl implements ApiSip {

    private boolean isCalling = false;

    private final Logger log = LoggerFactory.getLogger(Application.class);
    private final RxHttpClient rxHttpClient;
    private final ApiSipRequests apiSipRequests;

    public ApiSipImpl(RxHttpClient rxHttpClient, ApiSipConfig apiSipConfig) {
        this.rxHttpClient = rxHttpClient;
        this.apiSipRequests = $ApiSipRequests.mk(apiSipConfig);
    }

    @Override
    public void started() {
        log.info(SipStatus.SIP_STARTED.STATUS);
    }

    @Override
    public Maybe<HttpResponse<HttpStatus>> call() {
        if (!isCalling) {
            log.info(SipStatus.CALLING.STATUS);
            isCalling = true;
            return rxHttpClient.exchange(apiSipRequests.call(), HttpStatus.class)
                    .firstElement()
                    .doOnTerminate(() -> {
                        // Логируем завершение вызова
                        log.info(SipStatus.CALL_END.STATUS);
                        isCalling = false; // Сбрасываем флаг isCalling после завершения
                    })
                    .onErrorResumeNext(t -> {
                        isCalling = false;
                        log.error($Error.mk().translateSip(t.getMessage()));
                        return Maybe.just(HttpResponse.serverError());
                    });
        } else {
            log.warn(SipStatus.ALREADY_CALLING.STATUS);
            // Вместо flatMapSingle используем Maybe.defer для задержки и повторного вызова
            return Maybe.defer(() -> {
                // Задержка перед повторной попыткой
                return Maybe.timer(2, TimeUnit.SECONDS)
                        .flatMap(m -> call()); // Рекурсивно повторяем вызов
            });
        }
    }

    @Override
    public void end() {
        isCalling = false;
        log.info(SipStatus.CALL_END.STATUS);
    }

    @Override
    public Disposable error() {
        isCalling = false;
        log.info(SipStatus.SIP_ERROR.STATUS);
        return Single.create((SingleEmitter<String> e) -> {
            String restartStatus = $TProcess.mk().restartSipService();
            e.onSuccess(restartStatus);
        }).doOnSuccess(status -> log.info("SIP service restarted: " + status))
          .subscribe(restartService());
    }

    private Consumer<String> restartService() {
        return log::info;
    }
}
