package net.kimleo.rec.accessor;

import net.kimleo.rec.Pair;
import net.kimleo.rec.accessor.lexer.Lexer;
import net.kimleo.rec.concept.Indexible;

import java.util.Map;

public class Accessor<T> {

    private final Map<String, Integer> fieldMap;
    private final Integer leastCapacity;

    public Accessor(String[] fields) {
        Pair<Map<String, Integer>, Integer> fieldMapPair = Lexer.buildFieldMapPair(fields);
        fieldMap = fieldMapPair.getFirst();
        leastCapacity = fieldMapPair.getSecond();
    }

    public Map<String, Integer> getFieldMap() {
        return fieldMap;
    }

    public Integer getLeastCapacity() {
        return leastCapacity;
    }

    public RecordWrapper<T> create(Indexible<T> record) {
        assert (record.getSize() >= leastCapacity);

        return new RecordWrapper<>(fieldMap, record);
    }

    public RecordWrapper<T> of(Indexible<T> record) {
        return create(record);
    }
}
