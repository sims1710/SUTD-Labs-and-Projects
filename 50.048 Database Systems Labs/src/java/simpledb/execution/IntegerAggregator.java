package simpledb.execution;

import simpledb.common.Type;
import simpledb.execution.Aggregator;
import simpledb.storage.Field;
import simpledb.storage.IntField;
import simpledb.storage.Tuple;
import simpledb.storage.TupleDesc;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IntegerAggregator implements Aggregator {

    private static final long serialVersionUID = 1L;

    private int gbfield;
    private Type gbfieldtype;
    private int afield;
    private Op what;

    private Map<Field, Integer> aggregateValues;
    private Map<Field, Integer> aggregateCounts;
    private String gbFieldName;

    public IntegerAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
        this.gbfield = gbfield;
        this.gbfieldtype = gbfieldtype;
        this.afield = afield;
        this.what = what;

        this.aggregateValues = new HashMap<>();
        this.aggregateCounts = new HashMap<>();

        if (gbfield == Aggregator.NO_GROUPING)
            this.gbFieldName = null;
        else
            this.gbFieldName = "";
    }

    public void mergeTupleIntoGroup(Tuple tup) {
        Field groupByField = (this.gbfield == Aggregator.NO_GROUPING) ? null : tup.getField(this.gbfield);
        int value = ((IntField) tup.getField(this.afield)).getValue();

        if (!this.aggregateValues.containsKey(groupByField)) {
            this.aggregateCounts.put(groupByField, 1);

            if (this.what == Op.COUNT)
                this.aggregateValues.put(groupByField, 1);
            else
                this.aggregateValues.put(groupByField, value);
        } else {
            int aggregateValue = this.aggregateValues.get(groupByField);
            int count = this.aggregateCounts.get(groupByField);

            switch (this.what) {
                case SUM:
                    aggregateValue += value;
                    break;
                case AVG:
                    aggregateValue += value;
                    break;
                case MAX:
                    aggregateValue = Math.max(aggregateValue, value);
                    break;
                case MIN:
                    aggregateValue = Math.min(aggregateValue, value);
                    break;
                case COUNT:
                    aggregateValue++;
                    break;
                default:
                    break;
            }
            this.aggregateValues.put(groupByField, aggregateValue);
            this.aggregateCounts.put(groupByField, (count + 1));
        }
    }

    public OpIterator iterator() {
        return new OpIterator() {
            private Iterator<Map.Entry<Field, Integer>> iterator = aggregateValues.entrySet().iterator();

            @Override
            public void open() {
                iterator = aggregateValues.entrySet().iterator();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Tuple next() {
                Map.Entry<Field, Integer> entry = iterator.next();
                Tuple tuple = new Tuple(getTupleDesc());

                if (gbfield == Aggregator.NO_GROUPING) {
                    switch (what) {
                        case COUNT:
                        case SUM:
                        case MAX:
                        case MIN:
                            tuple.setField(0, new IntField(entry.getValue()));
                            break;
                        case AVG:
                            tuple.setField(0, new IntField(entry.getValue() / aggregateCounts.get(null)));
                            break;
                        default:
                            break;
                    }
                } else {
                    tuple.setField(0, entry.getKey());
                    switch (what) {
                        case COUNT:
                            tuple.setField(1, new IntField(entry.getValue()));
                            break;
                        case AVG:
                            tuple.setField(1, new IntField(entry.getValue() / aggregateCounts.get(entry.getKey())));
                            break;
                        default:
                            tuple.setField(1, new IntField(entry.getValue()));
                            break;
                    }
                }
                return tuple;
            }
            
            @Override
            public void rewind() {
                close();
                open();
            }

            @Override
            public TupleDesc getTupleDesc() {
                if (gbfield == Aggregator.NO_GROUPING)
                    return new TupleDesc(new Type[]{Type.INT_TYPE}, new String[]{what.toString()});
                else
                    return new TupleDesc(new Type[]{gbfieldtype, Type.INT_TYPE}, new String[]{gbFieldName, what.toString()});
            }

            @Override
            public void close() {
                iterator = null;
            }
        };
    }
}
