package de.ksrmx.bukkitpl.timelimit.main;

import de.ksrmx.bukkitpl.timelimit.FormatException;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.ccffee.utils.io.ByteConverter;
import org.ccffee.utils.io.ByteUtils;

import java.util.HashMap;
import java.util.Map;

public class OfflinePlayerData {

    protected HashMap<String, Boolean> permissions;

    public OfflinePlayerData(){
        this(new HashMap<>());
    }
    protected OfflinePlayerData(HashMap<String,Boolean> permissions){
        this.permissions = permissions;
    }

    public boolean isPermissionSet(String name) {
        return permissions.containsKey(name);
    }

    public boolean hasPermission(String name) {
        return permissions.getOrDefault(name,false);
    }

    public void updatePermissions(Permissible p){
        permissions.clear();
        for(PermissionAttachmentInfo pa : p.getEffectivePermissions()){
            permissions.put(pa.getPermission(),pa.getValue());
        }
    }


    @SuppressWarnings("unused")
    public static ByteConverter<OfflinePlayerData> serializer(){
        return ByteConverter.from(OfflinePlayerData::fromBytes, OfflinePlayerData::toBytes);
    }

    public static OfflinePlayerData fromBytes(byte[] bytes){
        byte[][] opdBytes = ByteUtils.unpack(bytes);
        if(opdBytes.length!=1) throw new RuntimeException(new FormatException("OfflinePlayerData contains "+opdBytes.length+" fields. Should be 1"));
        byte[][] permMap = ByteUtils.unpack(opdBytes[0]);
        HashMap<String, Boolean> perms = new HashMap<>(permMap.length);
        for(byte[] bs : permMap){
            byte[][] bss = ByteUtils.unpack(bs);
            if(bss.length!=2) throw new RuntimeException(new FormatException("PermMap contains "+bss.length+" fields. Should be 2"));
            perms.put(ByteConverter.STRING.fromBytes(bss[0]),ByteConverter.BOOLEAN.fromBytes(bss[1]));
        }
        return new OfflinePlayerData(perms);
    }

    public static byte[] toBytes(OfflinePlayerData opd) {
        //permissions
        byte[][] permissionsMap = new byte[opd.permissions.size()][2];
        int i = 0;
        for(Map.Entry<String,Boolean> e : opd.permissions.entrySet()){
            permissionsMap[i++] = ByteUtils.pack(
                    ByteConverter.STRING.toBytes(e.getKey()),
                    ByteConverter.BOOLEAN.toBytes(e.getValue())
            );
        }


        byte[][] opdBytes = new byte[][]{
                ByteUtils.pack(permissionsMap)
        };
        return ByteUtils.pack(opdBytes);
    }
}
