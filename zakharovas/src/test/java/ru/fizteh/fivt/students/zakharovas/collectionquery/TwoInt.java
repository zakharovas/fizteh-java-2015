package ru.fizteh.fivt.students.zakharovas.collectionquery;

/**
 * Created by alexander on 14.12.15.
 */
public class TwoInt {
    private Integer f;
    private Integer s;

    public Integer getF() {
        return f;
    }

    public Integer getS() {
        return s;
    }

    public TwoInt(Integer f, Integer s) {
        this.f = f;
        this.s = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TwoInt)) {
            return false;
        }
        TwoInt another = (TwoInt) obj;
        return f.equals(another.f) && s.equals(another.s);
    }

    @Override
    public int hashCode() {
        return (f.hashCode() + s.hashCode());
    }
}
