package simpledb.common;

import simpledb.common.Type;
import simpledb.storage.DbFile;
import simpledb.storage.HeapFile;
import simpledb.storage.TupleDesc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Catalog keeps track of all available tables in the database and their
 * associated schemas. ---> HASHMAP
 * For now, this is a stub catalog that must be populated with tables by a
 * user program before it can be used -- eventually, this should be converted
 * to a catalog that reads a catalog table from disk.
 * 
 * @Threadsafe
 */
public class Catalog {
    // Hashmaps to store the tables and their schemas
    private final Map<Integer, DbFile> tableIdToDbFile; // tableId
    private final Map<Integer, String> tableIdToName; // tableName
    private final Map<Integer, String> tableIdToPkey; // primaryKey

    /**
     * Constructor.
     * Creates a new, empty catalog.
     */
    public Catalog() {
        // some code goes here
        tableIdToDbFile = new ConcurrentHashMap<>();
        tableIdToName = new ConcurrentHashMap<>();
        tableIdToPkey = new ConcurrentHashMap<>();
    }

    /**
     * Add a new table to the catalog.
     * This table's contents are stored in the specified DbFile.
     * If there exists a table with the same name or ID, replace that old table with this one. 
     * @param file the contents of the table to add;  file.getId() is the identfier of
     *    this file/tupledesc param for the calls getTupleDesc and getFile. 
     * @param name the name of the table -- may be an empty string.  May not be null.  
     * @param pkeyField the name of the primary key field
     */
    public void addTable(DbFile file, String name, String pkeyField) throws NoSuchElementException {
        // some code goes here
        int tableId = file.getId();

        if (tableIdToName.containsValue(name)){ //duplicate name
            int oldtableID = getTableId(name);
            tableIdToName.remove(oldtableID);
            tableIdToName.put(tableId, name);
        }  

        tableIdToName.put(tableId, name); //no duplicate or contains duplicate tableid
        tableIdToDbFile.put(tableId, file);
        tableIdToPkey.put(tableId, pkeyField);
    }

    public void addTable(DbFile file, String name) {
        addTable(file, name, "");
    }

    /**
     * Add a new table to the catalog.
     * This table has tuples formatted using the specified TupleDesc and its
     * contents are stored in the specified DbFile.
     * @param file the contents of the table to add;  file.getId() is the identfier of
     *    this file/tupledesc param for the calls getTupleDesc and getFile
     */
    public void addTable(DbFile file) {
        addTable(file, (UUID.randomUUID()).toString());
    }

    /**
     * Return the id of the table with a specified name,
     * @throws NoSuchElementException if the table doesn't exist
     */
    public int getTableId(String name) throws NoSuchElementException {
        // some code goes here
        for (Map.Entry<Integer, String> entry : tableIdToName.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        throw new NoSuchElementException("Table with name " + name + " does not exist");
    }

    /**
     * Returns the tuple descriptor (schema) of the specified table
     * @param tableid The id of the table, as specified by the DbFile.getId()
     *     function passed to addTable
     * @throws NoSuchElementException if the table doesn't exist
     */
    public TupleDesc getTupleDesc(int tableid) throws NoSuchElementException {
        // some code goes here
        if (tableIdToDbFile.containsKey(tableid)) {
            return tableIdToDbFile.get(tableid).getTupleDesc();
        }
        throw new NoSuchElementException("Table with id " + tableid + " does not exist");
    }

    /**
     * Returns the DbFile that can be used to read the contents of the
     * specified table.
     * @param tableid The id of the table, as specified by the DbFile.getId()
     *     function passed to addTable
     */
    public DbFile getDatabaseFile(int tableid) throws NoSuchElementException {
        // some code goes here
        if (tableIdToDbFile.containsKey(tableid)) {
            return tableIdToDbFile.get(tableid);
        }
        throw new NoSuchElementException("Table with id " + tableid + " does not exist");
    }

    public String getPrimaryKey(int tableid) {
        // some code goes here
        if (tableIdToPkey.containsKey(tableid)) {
            return tableIdToPkey.get(tableid);
        }
        throw new NoSuchElementException("Table with id " + tableid + " does not exist");
    }

    public Iterator<Integer> tableIdIterator() {
        // some code goes here
        return tableIdToDbFile.keySet().iterator();
    }

    public String getTableName(int id) throws NoSuchElementException{
        // some code goes here
        if (tableIdToName.containsKey(id)) {
            return tableIdToName.get(id);
        }
        throw new NoSuchElementException("Table with id " + id + " does not exist");
    }
    
    /** Delete all tables from the catalog */
    public void clear() {
        tableIdToDbFile.clear();
        tableIdToName.clear();
        tableIdToPkey.clear();
    }
    
    /**
     * Reads the schema from a file and creates the appropriate tables in the database.
     * @param catalogFile
     */
    public void loadSchema(String catalogFile) {
        String line = "";
        String baseFolder=new File(new File(catalogFile).getAbsolutePath()).getParent();
        try {
            BufferedReader br = new BufferedReader(new FileReader(catalogFile));
            
            while ((line = br.readLine()) != null) {
                //assume line is of the format name (field type, field type, ...)
                String name = line.substring(0, line.indexOf("(")).trim();
                //System.out.println("TABLE NAME: " + name);
                String fields = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim();
                String[] els = fields.split(",");
                ArrayList<String> names = new ArrayList<>();
                ArrayList<Type> types = new ArrayList<>();
                String primaryKey = "";
                for (String e : els) {
                    String[] els2 = e.trim().split(" ");
                    names.add(els2[0].trim());
                    if (els2[1].trim().equalsIgnoreCase("int"))
                        types.add(Type.INT_TYPE);
                    else if (els2[1].trim().equalsIgnoreCase("string"))
                        types.add(Type.STRING_TYPE);
                    else {
                        System.out.println("Unknown type " + els2[1]);
                        System.exit(0);
                    }
                    if (els2.length == 3) {
                        if (els2[2].trim().equals("pk"))
                            primaryKey = els2[0].trim();
                        else {
                            System.out.println("Unknown annotation " + els2[2]);
                            System.exit(0);
                        }
                    }
                }
                Type[] typeAr = types.toArray(new Type[0]);
                String[] namesAr = names.toArray(new String[0]);
                TupleDesc t = new TupleDesc(typeAr, namesAr);
                HeapFile tabHf = new HeapFile(new File(baseFolder+"/"+name + ".dat"), t);
                addTable(tabHf,name,primaryKey);
                System.out.println("Added table : " + name + " with schema " + t);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println ("Invalid catalog entry : " + line);
            System.exit(0);
        }
    }
}

