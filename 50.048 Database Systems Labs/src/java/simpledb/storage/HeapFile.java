package simpledb.storage;

import simpledb.common.Database;
import simpledb.common.DbException;
import simpledb.common.Debug;
import simpledb.common.Permissions;
import simpledb.transaction.TransactionAbortedException;
import simpledb.transaction.TransactionId;

import simpledb.storage.BufferPool;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */

    private File f;
    private TupleDesc td;
    private int pageSize;
    private final ReentrantReadWriteLock fileLock;

    public HeapFile(File f, TupleDesc td) {
        // some code goes here
        this.f = f;
        this.td = td;
        this.pageSize = BufferPool.getPageSize();
        this.fileLock = new ReentrantReadWriteLock();
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return this.f;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere to ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
        return this.f.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return this.td;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) throws IllegalArgumentException{
        // some code goes here
        try {
            RandomAccessFile raf = new RandomAccessFile(this.f, "r");
            byte[] data = new byte[this.pageSize];
            raf.seek(pid.getPageNumber() * this.pageSize);
            raf.read(data, 0, this.pageSize);
            raf.close();
            return new HeapPage((HeapPageId) pid, data);
        } catch (IOException e) {
            throw new IllegalArgumentException("Page does not exist in this file");
        }
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException { 
        // some code goes here
        // not necessary for lab1
        Lock writeLock = fileLock.writeLock();
        writeLock.lock(); // Acquire an exclusive lock on the HeapFile
        try {
        RandomAccessFile raf = new RandomAccessFile(this.f, "rw");
        raf.seek(page.getId().getPageNumber() * this.pageSize);
        raf.write(page.getPageData(), 0, this.pageSize);
        raf.close();
        } finally {
            writeLock.unlock(); // Release the exclusive lock
        }
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {   
        // some code goes here
        int numberOfPages = (int) (this.f.length() / this.pageSize);
        return numberOfPages;
    }

    /**
     * Removes the specified tuple from the file on behalf of the specified
     * transaction.
     * This method will acquire a lock on the affected pages of the file, and
     * may block until the lock can be acquired.
     *
     * @param tid The transaction performing the update
     * @param t The tuple to delete.  This tuple should be updated to reflect that
     *          it is no longer stored on any page.
     * @return An ArrayList contain the pages that were modified
     * @throws DbException if the tuple cannot be deleted or is not a member
     *   of the file
     */

    // see DbFile.java for javadocs
    public List<Page> insertTuple(TransactionId tid, Tuple t) throws DbException,
            IOException, TransactionAbortedException { // Lab 2
        // some code goes here
        for (int i = 0; i < this.numPages(); i++){
            HeapPageId pid = new HeapPageId(this.getId(), i);
            HeapPage page = (HeapPage) Database.getBufferPool().getPage(tid, pid, Permissions.READ_WRITE);
            if (page.getNumEmptySlots() > 0){
                page.insertTuple(t);
                return new ArrayList<Page>(Arrays.asList(page));
            }
            else {
                Database.getBufferPool().unsafeReleasePage(tid, pid);
            }
        }
        // if no page has empty slots, create a new page
        byte[] data = new byte[this.pageSize];
        HeapPageId pid = new HeapPageId(this.getId(), this.numPages());
        HeapPage page = new HeapPage(pid, data);
        page.insertTuple(t);
        this.writePage(page);
        return new ArrayList<Page>(Arrays.asList(page));
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException { // Lab 2
        // some code goes here
        // not necessary for lab1
        if (t == null){
            throw new DbException("Tuple is null");
        }
        RecordId rid = t.getRecordId();
        PageId pid = rid.getPageId();
        HeapPage page = (HeapPage) Database.getBufferPool().getPage(tid, pid, Permissions.READ_WRITE);
        page.deleteTuple(t);
        return new ArrayList<Page>(Arrays.asList(page));
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
        
        // referring to DbFileIterator.java and AbstractDbFileIterator.java
        return new HeapFileIterator(this, tid); // can only return a DbFileIterator, can't return HeapPage's iterator
    }
}


