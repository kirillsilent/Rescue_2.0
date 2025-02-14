package kz.idc.utils.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import kz.idc.rs.Button;
import kz.idc.utils.gpio.read433.$Read433;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GPIO implements GpioPinListenerDigital {

    @Inject
    Button buttonService;

    private static GpioController gpio;
    private final GPIOConf gpioConf;
    private static GpioPinDigitalOutput led;

    public GPIO(GPIOConf gpioConf) {
        this.gpioConf = gpioConf;
        gpio = GpioFactory.getInstance();
        init();
    }

    private void init(){
        initButton();
        initRF433WithData();
        initRF433WithRelay();
        initLight();
    }

    private void initButton(){
        GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_UP);
        button.addListener(this);
    }

    private void initRF433WithData(){
        GpioPinDigitalInput rf433WithData = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
        rf433WithData.addListener(this);
    }

    private void initRF433WithRelay(){
        GpioPinDigitalInput rf433D0 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_UP);
        GpioPinDigitalInput rf433D1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);
        GpioPinDigitalInput rf433D2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_UP);
        GpioPinDigitalInput rf433D3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_UP);

        rf433D0.addListener(this);
        rf433D1.addListener(this);
        rf433D2.addListener(this);
        rf433D3.addListener(this);
    }

    private void initLight(){
        led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29,   // PIN NUMBER
                "LED",
                PinState.LOW);
    }

    public static void shutdown() {
        gpio.shutdown();
    }

    public static void stopLight() {
        led.low();
    }

    public static void startLight() {
        led.high();
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        if (event.getPin().getName().equals(RaspiPin.GPIO_01.getName())) {
            int data433 = $Read433.mk().readDataByTime();
            if (data433 > 0) {
                System.out.println("GPIO Data 433: " + data433);
                if(gpioConf.getValuesForRead().contains(data433)){
                    startLight();
                    buttonService.card().subscribe();
                    buttonService.call().subscribe();
                }
            }
        }else if(event.getPin().getName().equals(RaspiPin.GPIO_07.getName())
                || event.getPin().getName().equals(RaspiPin.GPIO_03.getName())
                || event.getPin().getName().equals(RaspiPin.GPIO_04.getName())
                || event.getPin().getName().equals(RaspiPin.GPIO_05.getName())
                || event.getPin().getName().equals(RaspiPin.GPIO_06.getName())){
            if(event.getState().isHigh()) {
                System.out.println("Event" + event.getPin());
                startLight();
                buttonService.card().subscribe();
                buttonService.call().subscribe();
            }
        }
    }
}
