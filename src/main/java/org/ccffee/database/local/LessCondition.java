package org.ccffee.database.local;

import org.ccffee.database.comparison.Comparator;
import org.ccffee.utils.io.ByteConvertable;

class LessCondition extends Condition{

    private byte[] data;
    private Comparator comparator;

    public LessCondition(boolean and, boolean not, int column, ByteConvertable data, Comparator comparator) {
        super(and, not, column);
        this.data = data.toByteArray();
        if(this.data.length==0)this.data = new byte[]{0};
        this.comparator = comparator;
    }

    @Override
    public boolean is(byte[] c) {
        return comparator.less(c,data);
    }
}
