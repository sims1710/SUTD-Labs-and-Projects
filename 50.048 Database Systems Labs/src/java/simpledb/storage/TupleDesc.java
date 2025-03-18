package simpledb.storage;

import simpledb.common.Type;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */

    private List<TDItem> tdItems;  // Store the collection of TDItem objects that the iterator will iterate over

    public Iterator<TDItem> iterator() {
        // some code goes here
        Iterator<TDItem> iter = new Iterator<TDItem>() {
            private int currentIndex = 0;

            // Check if there are more elements in the list
            @Override
            public boolean hasNext() {
                return currentIndex < tdItems.size();
            }

            // Get the next TDItem in the list
            @Override
            public TDItem next() {
                return tdItems.get(currentIndex++);
            }
            
            // Remove operation is not supported
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return iter;
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        tdItems = new ArrayList<TDItem>(); // Declare the tdItems variable
        //typeAr must contain at least one entry
        if (typeAr.length < 1){
            throw new IllegalArgumentException("Must contain at least one Field Type");
        }
        if (typeAr.length < fieldAr.length) { 
            throw new IllegalArgumentException("Number of Field Types must be at least equal to number of Field Names");
        }

        for (int i = 0; i < typeAr.length; i++) {
            //If the type declared has no Field Name (i.e. fieldAr.length < typeAr.length)
            if (fieldAr[i]== null) { 
                tdItems.add(new TDItem(typeAr[i], "NULL"));
            }
            //If the type declared has a corr Field Name
            tdItems.add(new TDItem(typeAr[i], fieldAr[i]));
        }
    }
    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here   
        tdItems = new ArrayList<TDItem>(); // Declare the tdItems variable

        //typeAr must contain at least one entry
        if (typeAr.length < 1){
            throw new IllegalArgumentException("Must contain at least one Field Type");
        }

        for (int i = 0; i < typeAr.length; i++) {
            tdItems.add(new TDItem(typeAr[i], "NULL"));
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */

    public int numFields() {
        // some code goes here
        return tdItems.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        if (i < tdItems.size()) {
            return tdItems.get(i).fieldName;
        }
        else {
            throw new NoSuchElementException("i is not a valid field reference");
        }
    }
        
    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        if (i < tdItems.size()) {
            return tdItems.get(i).fieldType;
        }
        else {
            throw new NoSuchElementException("i is not a valid field reference");
        }
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        for (int i = 0; i < tdItems.size(); i++) {
            if (tdItems.get(i).fieldName.equals(name)) {
                return i;
            }
        }
        throw new NoSuchElementException("No field with a matching name found");
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int size = 0;
        for (TDItem item : tdItems) {
            size += item.fieldType.getLen();
        }
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here

        // find number of fields in new merged TupleDesc
        int numFields = td1.numFields() + td2.numFields();

        // declare new arrays to store the type and field names
        Type[] typeAr = new Type[numFields];
        String[] fieldAr = new String[numFields];

        // copy from td1
        for (int i = 0; i < td1.numFields(); i++) {
            typeAr[i] = td1.getFieldType(i);
            fieldAr[i] = td1.getFieldName(i);
        }

        // copy from td2
        for (int i = 0; i < td2.numFields(); i++) {
            typeAr[i + td1.numFields()] = td2.getFieldType(i);
            fieldAr[i + td1.numFields()] = td2.getFieldName(i);
        }
        return new TupleDesc(typeAr, fieldAr);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {
        // some code goes here

        if (o instanceof TupleDesc) { //check if o is also a TupleDesc object
            TupleDesc other = (TupleDesc) o; 
            if (other.numFields() != this.numFields()) { //check if o has same length as this TupleDesc object
                return false; 
            }

            for (int i = 0; i < this.numFields(); i++) { //compare the type for individual TDitems of both TupleDesc
                if (other.getFieldType(i) != this.getFieldType(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        String result = "";

        for (int i = 0; i < tdItems.size(); i++) {
            result += tdItems.get(i).fieldType + "(" + tdItems.get(i).fieldName + ")";
            if (i != tdItems.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }
}