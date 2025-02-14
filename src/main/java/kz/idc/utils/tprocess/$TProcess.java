package kz.idc.utils.tprocess;

public class $TProcess {
    private static TProcessImpl tProcess;

    public static TProcessImpl mk(){
        if(tProcess == null){
            tProcess = new TProcessImpl();
        }
        return tProcess;
    }
}
