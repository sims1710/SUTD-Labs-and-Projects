package simpledb.storage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;
    private TupleDesc td;
    private RecordId recordId;
    private Field[] FieldsList;

    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td
     *            the schema of this tuple. It must be a valid TupleDesc
     *            instance with at least one field.
     */
    
    public Tuple(TupleDesc td) {
        // some code goes here
        if (td == null || td.numFields() < 1){
            throw new IllegalArgumentException("TupleDesc instance must have at least one field");
        }

        this.td = td;
        this.recordId = null;
        this.FieldsList = new Field[td.numFields()]; //declare a list of fields for this tuple
    }


    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return this.td;
        // return null;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
        // some code goes here
        // return null;
        return this.recordId;
    }

    /**
     * Set the RecordId information for this tuple.
     *
     * @param rid
     *            the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
        this.recordId = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i
     *            index of the field to change. It must be a valid index.
     * @param f
     *            new value for the field.
     */
    public void setField(int i, Field f) {
        // some code goes here
        
        // check if index i is valid
        if (i < 0 || i >= td.numFields()) {
            throw new IllegalArgumentException("Index out of bounds!!");
        }

        // check if type of field correspond to schema
        if (!td.getFieldType(i).equals(f.getType())) {
            throw new IllegalArgumentException("Field type does not match!!");
        }

        // update the value of the field at index i to f
        FieldsList[i] = f;
    }
    

    /**
     * @return the value of the ith field, or null if it has not been set.
     *
     * @param i
     *            field index to return. Must be a valid index.
     */
    public Field getField(int i) {
        // some code goes here
        // check if index i is valid
        if (i < 0 || i >= td.numFields()) {
            throw new IllegalArgumentException("Index out of bounds!!");
        }

        if (this.FieldsList[i] == null){
            return null;
        }
        return this.FieldsList[i];
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN
     *
     * where \t is any whitespace (except a newline)
     */
    public String toString() {
        // some code goes here
        //throw new UnsupportedOperationException("Implement this");
        // some code goes here
        String result = "";
        for (int i = 0; i < FieldsList.length; i++) {
            result += FieldsList[i];
            if (i < FieldsList.length - 1) {
                result += "\t";
            }
        }
        return result;
    }

    /**
     * @return
     *        An iterator which iterates over all the fields of this tuple
     * */
    public Iterator<Field> fields()
    {
        // some code goes here
        return Arrays.asList(FieldsList).iterator();
        //return null;
    }

    /**
     * reset the TupleDesc of this tuple (only affecting the TupleDesc)
     * */
    public void resetTupleDesc(TupleDesc td)
    {
        this.td = null;
    }
}
