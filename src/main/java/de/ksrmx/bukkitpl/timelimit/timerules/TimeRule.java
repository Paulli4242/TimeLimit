package de.ksrmx.bukkitpl.timelimit.timerules;

import de.ksrmx.bukkitpl.timelimit.main.PlayerData;
import org.ccffee.utils.io.ByteConverter;
import org.ccffee.utils.io.ByteUtils;

public abstract class TimeRule<T extends TimeRule<T>> implements ITimeRule, ByteConverter<TimeRuleData<T>> {
    protected static final TimeRuleData EMPTY_DATA = new TimeRuleData() {};
    protected final String name;
    protected TimeRuleData<T> data;

    public TimeRule(String name) {
        this.name = name;
    }

    public final void initData(byte[] data){
        byte[][] bm = ByteUtils.unpack(data);
        if(bm.length!=2){
            throw new IllegalArgumentException("unknown format");
        }
        this.data = fromBytes(bm[1]);
    }
    public final void initDefaultData(){
        this.data=getDefaultData();
        if(data==null)data = EMPTY_DATA;
    }
    protected abstract TimeRuleData<T> getDefaultData();

    public boolean isInitialized(){
        return this.data != null;
    }

    public final byte[] saveData(){
        if(data==null)throw new IllegalStateException("data not initialized");
        else if(data==EMPTY_DATA)return new byte[0];
        else return ByteUtils.pack(saveHeader(name),toBytes(data));
    }
    public String getHeader(){
        return name;
    }
    public static String loadHeader(byte[] header){
        return ByteConverter.STRING.fromBytes(header);
    }
    private static byte[] saveHeader(String header){
        return PlayerData.ByteConverter.STRING.toBytes(header);
    }

}
