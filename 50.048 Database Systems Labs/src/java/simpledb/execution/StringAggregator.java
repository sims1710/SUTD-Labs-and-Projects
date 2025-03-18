package simpledb.execution;

import simpledb.common.Type;
import simpledb.storage.Tuple;
import simpledb.storage.TupleDesc;

// additional imports
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import simpledb.storage.Field;
import simpledb.storage.IntField;

/**
 * Knows how to compute some aggregate over a set of StringFields.
 */
public class StringAggregator implements Aggregator {

    private static final long serialVersionUID = 1L;

    private int gbfield;
    private Type gbfieldtype;
    private int afield;
    private Op what;

    private Map<Field, Integer> count;
    private String gbFieldName; // Group by field name for labeling

    /**
     * Aggregate constructor
     * @param gbfield the 0-based index of the group-by field in the tuple, or NO_GROUPING if there is no grouping
     * @param gbfieldtype the type of the group by field (e.g., Type.INT_TYPE), or null if there is no grouping
     * @param afield the 0-based index of the aggregate field in the tuple
     * @param what aggregation operator to use -- only supports COUNT
     * @throws IllegalArgumentException if what != COUNT
     */

    public StringAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
        // some code goes here
        this.gbfield = gbfield;
        this.gbfieldtype = gbfieldtype;
        this.afield = afield;
        this.what = what;

        if (what != Op.COUNT) {
            throw new IllegalArgumentException("StringAggregator only supports COUNT");
        }

        this.count = new HashMap<>();
    }

    /**
     * Merge a new tuple into the aggregate, grouping as indicated in the constructor
     * @param tup the Tuple containing an aggregate field and a group-by field
     */

    public void mergeTupleIntoGroup(Tuple tup) {
        // some code goes here
        Field groupField = this.gbfield == NO_GROUPING ? null : tup.getField(this.gbfield);

        if (groupField != null)
            this.gbFieldName = groupField.toString();

        // Increment count if groupField exists, else add with count 1.
        if (count.containsKey(groupField)) {
            count.put(groupField, count.get(groupField) + 1);
        } else {
            count.put(groupField, 1);
        }
    }

    /**
     * Create a OpIterator over group aggregate results.
     *
     * @return a OpIterator whose tuples are the pair (groupVal,
     *   aggregateVal) if using group, or a single (aggregateVal) if no
     *   grouping. The aggregateVal is determined by the type of
     *   aggregate specified in the constructor.
     */
    public OpIterator iterator() {
        return new OpIterator() {
            private Iterator<Map.Entry<Field, Integer>> iterator = count.entrySet().iterator();

            @Override
            public void open() {
                iterator = count.entrySet().iterator();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Tuple next() {
                Map.Entry<Field, Integer> entry = iterator.next();
                Tuple tuple = new Tuple(getTupleDesc());

                // Set fields based on grouping
                if (gbfield == Aggregator.NO_GROUPING)
                    tuple.setField(0, new IntField(entry.getValue()));
                else {
                    tuple.setField(0, entry.getKey());
                    tuple.setField(1, new IntField(entry.getValue()));
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
