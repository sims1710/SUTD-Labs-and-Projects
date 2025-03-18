package simpledb.storage;

import simpledb.common.Database;
import simpledb.common.Permissions;
import simpledb.common.DbException;
import simpledb.transaction.TransactionAbortedException;
import simpledb.transaction.TransactionId;
import simpledb.storage.LockManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BufferPool manages the reading and writing of pages into memory from
 * disk. Access methods call into it to retrieve pages, and it fetches
 * pages from the appropriate location.
 * <p>
 * The BufferPool is also responsible for locking; when a transaction fetches
 * a page, BufferPool checks that the transaction has the appropriate
 * locks to read/write the page.
 *
 * @Threadsafe, all fields are final
 */

public class BufferPool {
    /** Bytes per page, including header. */
    private static final int DEFAULT_PAGE_SIZE = 4096;

    private static int pageSize = DEFAULT_PAGE_SIZE;

    public static final int DEFAULT_PAGES = 50;
    private final ConcurrentHashMap<PageId, Page> pages;

    private final LockManager lockManager;
    private Map<TransactionId, Set<TransactionId>> waitForGraph = new HashMap<>();

    private final int numPages;

    /**
     * Creates a BufferPool that caches up to numPages pages.
     *
     * @param numPages maximum number of pages in this buffer pool.
     */
    public BufferPool(int numPages) {
        // some code goes here
        pages = new ConcurrentHashMap<>(numPages);
        //Initialise accessOrder for LRU
        lockManager = new LockManager();
        this.numPages = numPages;
    }   

    public static int getPageSize() {
        return pageSize;
    }

    // THIS FUNCTION SHOULD ONLY BE USED FOR TESTING!!
    public static void setPageSize(int pageSize) {
        BufferPool.pageSize = pageSize;
    }

    // THIS FUNCTION SHOULD ONLY BE USED FOR TESTING!!
    public static void resetPageSize() {
        BufferPool.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * Retrieve the specified page with the associated permissions.
     * Will acquire a lock and may block if that lock is held by another
     * transaction.
     * <p>
     * The retrieved page should be looked up in the buffer pool. If it
     * is present, it should be returned. If it is not present, it should
     * be added to the buffer pool and returned. If there is insufficient
     * space in the buffer pool, a page should be evicted.
     *
     * @param tid  the ID of the transaction requesting the page
     * @param pid  the ID of the requested page
     * @param perm the requested permissions on the page
     * @throws TransactionAbortedException if transaction is aborted
     * @throws DbException                  if an error occurs in the database
     */

    // Modify getPage() to block and acquire the desired lock before returning a page.
    public Page getPage(TransactionId tid, PageId pid, Permissions perm)
            throws TransactionAbortedException, DbException {

        DetectAndHandleDeadlock(tid);
        if (!lockManager.acquireLock(pid, tid, perm)){
            throw new TransactionAbortedException();
        }

        synchronized (this) {
            // Check if the requested page is in pages
            if (this.pages.containsKey(pid)) {
                Page page = this.pages.get(pid);

                // for LRU implementation, the LRU cache
                this.pages.remove(pid);
                this.pages.put(pid, page);
                return page;
            }

            Page newPage = Database.getCatalog().getDatabaseFile(pid.getTableId()).readPage(pid);

            if (this.numPages <= this.pages.size()) {
                this.evictPage();
            }

            if (perm == Permissions.READ_WRITE) {
                newPage.markDirty(true, tid);
            }

            this.pages.put(pid, newPage);
            return newPage;
        }
    }

    /**
     * Releases the lock on a page.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param pid the ID of the page to unlock
     */
    public void unsafeReleasePage(TransactionId tid, PageId pid) {
        // some code goes here
        // not necessary for lab1|lab2
        this.lockManager.releaseLock(tid, pid);
    }

    /**
     * Release all locks associated with a given transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     */
    public void transactionComplete(TransactionId tid) {
        // some code goes here
        // not necessary for lab1|lab2
        this.transactionComplete(tid,true);
    }

    /** Return true if the specified transaction has a lock on the specified page */
    public boolean holdsLock(TransactionId tid, PageId p) {
        // some code goes here
        // not necessary for lab1|lab2
        return this.lockManager.holdsLock(tid, p);
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction.
     *
     * @param tid    the ID of the transaction requesting the unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    public void transactionComplete(TransactionId tid, boolean commit) {
        // some code goes here
        // not necessary for lab1|lab2
        for (PageId pid : pages.keySet()) {
            lockManager.releaseLock(tid, pid);
        }

        if (commit) {
            for (PageId pid : pages.keySet()) {
                Page page = pages.get(pid);
                if (page.isDirty() != null && page.isDirty().equals(tid)){
                    try {
                        flushPage(pid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        else {
            for (PageId pid : pages.keySet()){
                Page page = pages.get(pid);
                if (page.isDirty() != null && page.isDirty().equals(tid)){
                    DbFile file = Database.getCatalog().getDatabaseFile(pid.getTableId());
                    Page diskPage = file.readPage(pid);

                    // Update paes in BufferPool with disk page
                    pages.put(pid,diskPage);
                    // Clear dirty page
                    page.markDirty(false, tid);
                }
            }
        }

    }

    /**
     * Add a tuple to the specified table on behalf of transaction tid. Will
     * acquire a write lock on the page the tuple is added to.
     *
     * @param tid     the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t       the tuple to add
     */
    public void insertTuple(TransactionId tid, int tableId, Tuple t)
        throws DbException, IOException, TransactionAbortedException {

    DbFile file = Database.getCatalog().getDatabaseFile(tableId);
    ArrayList<Page> pageArray = (ArrayList<Page>) file.insertTuple(tid, t);

    for (Page p : pageArray) {
        p.markDirty(true, tid);

        // If the buffer pool is full and does not already contain the page, evict a page
        if (!this.pages.containsKey(p.getId()) && this.pages.size() >= this.numPages) {
            this.evictPage();
        }

        // Remove the page from the buffer pool to update its position in the LRU cache
        this.pages.remove(p.getId());

        // Re-insert the page into the buffer pool, which also updates its position in the LRU cache
        this.pages.put(p.getId(), p);
    }
}

    /**
     * Remove the specified tuple from the buffer pool.
     * Will acquire a write lock on the page the tuple is removed from.
     *
     * @param tid the transaction deleting the tuple.
     * @param t   the tuple to delete
     */
    public void deleteTuple(TransactionId tid, Tuple t)
        throws DbException, IOException, TransactionAbortedException {
        RecordId rid = t.getRecordId();
        DbFile file = Database.getCatalog().getDatabaseFile(rid.getPageId().getTableId());
        List<Page> modifiedPages = file.deleteTuple(tid, t);
        
        if (this.pages.size() >= this.numPages) {
            this.evictPage();
        }
        
        for (Page p : modifiedPages) {
            p.markDirty(true, tid);
            this.pages.remove(p.getId());
            this.pages.put(p.getId(), p);
        }
    }


    /**
     * Flush all dirty pages to disk.
     */
    public synchronized void flushAllPages() throws IOException { // Lab 2
        // some code goes here
        // not necessary for lab1
        for (PageId pid : pages.keySet()) {
            flushPage(pid);
        }
    }

    /**
     * Remove the specific page id from the buffer pool.
     * Needed by the recovery manager to ensure that the
     * buffer pool doesn't keep a rolled back page in its
     * cache.
     */
    public synchronized void discardPage(PageId pid) { // Lab 2
        // some code goes here
        // not necessary for lab1
        pages.remove(pid);
        
    }

    /**
     * Flushes a certain page to disk
     *
     * @param pid an ID indicating the page to flush
     */
    private synchronized void flushPage(PageId pid) throws IOException { // Lab 2
        // some code goes here
        // not necessary for lab1
        Page page = pages.get(pid);
        if (page.isDirty() != null) {
            Database.getCatalog().getDatabaseFile(pid.getTableId()).writePage(page);
            page.markDirty(false, null);
        }
    }

    /**
     * Write all pages of the specified transaction to disk.
     */
    public synchronized void flushPages(TransactionId tid) throws IOException {
        // not necessary for lab1|lab2
        for (PageId pid : pages.keySet()) {
            Page page = pages.get(pid);
            if (page.isDirty() != null && page.isDirty().equals(tid)) {
                flushPage(pid);
            }
        }
    }

    /**
     * Discards a page from the buffer pool.
     * Flushes the page to disk to ensure dirty pages are updated on disk.
     */
    private synchronized void evictPage() throws DbException {
        Iterator<PageId> pageIdIterator = this.pages.keySet().iterator();
    
        Page leastRecentlyUsedPage = null;
    
        while (pageIdIterator.hasNext()) {
            Page currentPage = this.pages.get(pageIdIterator.next());
            // Skip dirty pages for "NO STEAL" policy
            if (currentPage.isDirty() == null) {
                leastRecentlyUsedPage = currentPage;
            }
        }
    
        if (leastRecentlyUsedPage == null) {
            throw new DbException("There are no pages to evict in the buffer pool.");
        }
    
        try {
            this.flushPage(leastRecentlyUsedPage.getId());
        } catch (IOException e) {
            throw new DbException("Page could not be flushed.");
        }
        this.pages.remove(leastRecentlyUsedPage.getId());
    }

    //------Deadlock Management
    private synchronized void DetectAndHandleDeadlock(TransactionId requestingTid) throws TransactionAbortedException {
        //check if waitforgraph map contains a key equal to requestingTid
        if (!waitForGraph.containsKey(requestingTid)) {
            waitForGraph.put(requestingTid, new HashSet<>());
        }
        Set<TransactionId> havevisited = new HashSet<>();
        Stack<TransactionId> stack = new Stack<>();
        stack.push(requestingTid);

        //while the stack is not empty, pop the current transactionid
        //havevisited keeps track of the ids that have been visited
        while (!stack.isEmpty()) {
            TransactionId curr = stack.pop();
            havevisited.add(curr);

            Set<TransactionId> neighbors = waitForGraph.getOrDefault(curr, new HashSet<>());
            for (TransactionId neighbor : neighbors) {
                if (neighbor.equals(requestingTid)) {
                    AbortTransaction(requestingTid);
                    throw new TransactionAbortedException();
                    }
                if (!havevisited.contains(neighbor)) {
                    stack.push(neighbor);
                    }
            }
        }
    }

     //lab 3 ex 5
    private synchronized void AbortTransaction(TransactionId tid) {
        lockManager.releaseAllLocks(tid);
        waitForGraph.remove(tid);
    }
}
