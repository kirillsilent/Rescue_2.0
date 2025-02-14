package kz.idc;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.Micronaut;
import kz.idc.utils.gpio.GPIO;
import kz.idc.utils.gpio.GPIOStates;
import kz.idc.utils.storage.$Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

@Context
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static class Hook extends Thread {
        public void run() {
            GPIO.stopLight();
            GPIO.shutdown();
            log.info(GPIOStates.SHUTDOWN.STATE);
        }
    }

    public static void main(String[] args) {
        $Storage.mk().createStorage();
        Micronaut.build(args)
                .eagerInitSingletons(true)
                .mainClass(Application.class)
                .start();
        Runtime.getRuntime().addShutdownHook(new Hook());
    }

    @PostConstruct
    void init(@Value("${siphone.host:default-value}") String siphoneHost,
            @Value("${card.host:default-value}") String cardApi,
            @Value("${network.host:default-value}") String networkToolsHost,
            @Value("${gpio.rf433:default-value}") List<Integer> values,
              @Value("${ac-tools.host:default-value}") String acToolsHost)  {
        log.info("siphone host: {}", siphoneHost);
        log.info("net-tools host is: {}", networkToolsHost);
        log.info("ac-tools host is: {}", acToolsHost);
        log.info("card api host is: {}", cardApi);
        log.info("Values for read from rf433: {}", values.toString());
    }
}
