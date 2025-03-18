package simpledb.storage;

import simpledb.common.Database;
import simpledb.common.DbException;
import simpledb.common.Permissions;
import simpledb.transaction.TransactionAbortedException;
import simpledb.transaction.TransactionId;

import java.util.NoSuchElementException;
import java.util.Iterator;

/** Helper for implementing DbFileIterators. Handles hasNext()/next() logic. */
public class HeapFileIterator implements DbFileIterator {
    private HeapFile heapFile;
    private TransactionId tid;
    private int currentPage;
    private Iterator<Tuple> pageTupleIterator;

    public HeapFileIterator(HeapFile heapFile, TransactionId tid){
        this.heapFile = heapFile;
        this.tid = tid;
    }
    
    /**
     * Opens the iterator
     * @throws DbException when there are problems opening/accessing the database.
     */
    public void open() throws DbException, TransactionAbortedException{
        currentPage = 0;
        pageTupleIterator = getPageTupleIterator(currentPage);  // loads the page
    }

    /** @return true if there are more tuples available, false if no more tuples or iterator isn't open. */
    public boolean hasNext() throws DbException, TransactionAbortedException {
        if (pageTupleIterator != null && pageTupleIterator.hasNext()) {
            return true; 
        }
        
        // Try to find the next non-empty page
        while (currentPage < heapFile.numPages() - 1) {
            currentPage++; 
            pageTupleIterator = getPageTupleIterator(currentPage); 
            if (pageTupleIterator.hasNext()) {
                return true; 
            }
        }
        return false;
    }

    /**
     * Gets the next tuple from the operator (typically implementing by reading
     * from a child operator or an access method).
     *
     * @return The next tuple in the iterator.
     * @throws NoSuchElementException if there are no more tuples
     */
    public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
        if ((pageTupleIterator == null) || (pageTupleIterator.hasNext() == false)){
            throw new NoSuchElementException();
        }
        return pageTupleIterator.next();  // next tuple in the page
    }

    /**
     * Resets the iterator to the start.
     * @throws DbException When rewind is unsupported.
     */
    public void rewind() throws DbException, TransactionAbortedException{
        currentPage = 0;
        pageTupleIterator = getPageTupleIterator(currentPage);
    }

    /**
     * Closes the iterator.
     */
    public void close() {
        pageTupleIterator = null;
    }

    // added function to get the iterator for tuples in a HeapPage
    public Iterator<Tuple> getPageTupleIterator(int currentPage) throws TransactionAbortedException, DbException{
        PageId pid = new HeapPageId(heapFile.getId(), currentPage);
        BufferPool bufferPool = Database.getBufferPool();
        HeapPage page = (HeapPage) bufferPool.getPage(tid, pid, Permissions.READ_ONLY);
        return page.iterator(); // function from HeapPage which iterates over all tuples on this page
    }
}