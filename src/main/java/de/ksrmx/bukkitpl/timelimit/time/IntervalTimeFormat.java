package de.ksrmx.bukkitpl.timelimit.time;

import de.ksrmx.bukkitpl.timelimit.FormatException;
import de.ksrmx.bukkitpl.timelimit.timerules.IntervalType;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class IntervalTimeFormat {

    public enum Component{

        YEAR      ( 0x01, 'Y'),
        MONTH     ( 0x02,'M'),
        WEEK_DAY  ( 0x04, 'W'),
        DAY       ( 0x08, 'D'),
        HOUR      ( 0x10,'h'),
        MINUTE    ( 0x11,'m'),
        SECOND    ( 0x12,'s');

        public final int FLAG;
        public final char CHARACTER;

        Component(int formatFlag, char formatCharacter) {
            this.FLAG = formatFlag;
            this.CHARACTER = formatCharacter;
        }

        @Nullable
        public static Component fromChar(char c){
            return switch (c){
                case 'Y' -> YEAR;
                case 'M' -> MONTH;
                case 'W' -> WEEK_DAY;
                case 'D' -> DAY;
                case 'h' -> HOUR;
                case 'm' -> MINUTE;
                case 's' ->SECOND;
                default -> null;
            };
        }

    }

    protected record FormatFragment(int from, int to){}

    protected final String formatString;
    protected final EnumMap<Component, FormatFragment> format;



    protected IntervalTimeFormat(String formatString, EnumMap<Component, FormatFragment> format) {
        this.formatString = formatString;
        this.format = format;
    }

    public IntervalTime format(String string, IntervalType type)throws FormatException{
        int year = 0,month = 0,day = 0,hour = 0,minute = 0,second = 0;
        if(string.length()!=formatString.length())
            throw new FormatException("Given argument \""+string+"\" does not match format \""+formatString+"\".");
        for(Map.Entry<Component,FormatFragment> f : format.entrySet()){
            int i;
            FormatFragment ff = f.getValue();
            Component c = f.getKey();
            String sub = string.substring(ff.from, ff.to);
            try{
                i = Integer.parseInt(sub);
                if(i<0) throw new FormatException("Format component '"+c.CHARACTER+"', can not be less than 0!");
                int max = switch (c){
                    case YEAR -> Integer.MAX_VALUE;
                    case MONTH -> 12;
                    case DAY -> 28;
                    case WEEK_DAY -> 7;
                    case HOUR -> 24;
                    case MINUTE,SECOND -> 60;
                };
                if(i>max)throw new FormatException("Format component '"+c.CHARACTER+"', can not be less than 0!");
            }catch (NumberFormatException e){
                if(c == Component.WEEK_DAY) {
                    sub = sub.toLowerCase();
                    if        ("monday".startsWith(sub)) {
                        i = 1;
                    } else if ("tuesday".startsWith(sub)) {
                        i = 2 ;
                    } else if ("wednesday".startsWith(sub)) {
                        i = 3;
                    } else if ("thursday".startsWith(sub)) {
                        i = 4;
                    } else if ("friday".startsWith(sub)) {
                        i = 5;
                    } else if ("saturday".startsWith(sub)) {
                        i = 6;
                    } else if ("sunday".startsWith(sub)) {
                        i = 7;
                    }else throw new FormatException("Given argument \""+string+"\" does not match format \""+formatString+"\".");
                }else throw new FormatException("Given argument \""+string+"\" does not match format \""+formatString+"\".");
            }
            switch (c){
                case YEAR -> year = i;
                case MONTH -> month = i;
                case DAY, WEEK_DAY -> day = i;
                case HOUR -> hour = i;
                case MINUTE -> minute = i;
                case SECOND -> second = i;
            }
        }
        return new IntervalTime(type,year,month,day,hour,minute,second);
    }


    public static IntervalTimeFormat of(String string, int allowedFormat) throws FormatException{
        char[] chs = string.toCharArray();
        EnumMap<Component,FormatFragment> format = new EnumMap<>(Component.class);
        int from = -1;
        char prevChar=0;
        Component cmp;
        for(int i = 0; i < chs.length; i++){
            char c = chs[i];
            if(c!=prevChar){
                cmp = Component.fromChar(prevChar);
                if(cmp!=null){
                    if((allowedFormat& cmp.FLAG)==0)
                        throw new FormatException("Format \""+string+"\" contains contains component '"+prevChar+"' which is not allowed in this format!");
                    else if(format.put(cmp,new FormatFragment(from,i))!=null)
                        throw new FormatException("Format \""+string+"\" contains contains component '"+prevChar+"' more than once!");
                }
                prevChar = c;
                from = i;
            }
        }
        cmp = Component.fromChar(prevChar);
        if(cmp!=null){
            if(format.put(cmp,new FormatFragment(from,chs.length))!=null)
                throw new FormatException("Format \""+string+"\" contains contains component '"+prevChar+"' more than once!");
        }
        return new IntervalTimeFormat(string,format);
    }

    @SuppressWarnings("unused")
    public static IntervalTimeFormat of(String string) throws FormatException{
        return of(string,0xffffffff);
    }

}
