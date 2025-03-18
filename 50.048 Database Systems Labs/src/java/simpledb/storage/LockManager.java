package simpledb.storage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import simpledb.common.Permissions;
import simpledb.storage.PageId;
import simpledb.transaction.TransactionAbortedException;
import simpledb.transaction.TransactionId;

public class LockManager {
    private final Map<PageId, Set<TransactionId>> sharedLocks = new ConcurrentHashMap<>();
    private final Map<PageId, TransactionId> exclusiveLocks = new ConcurrentHashMap<>();
    private Map<TransactionId, Set<TransactionId>> waitForGraph = new ConcurrentHashMap<>();

    public synchronized boolean acquireLock(PageId pageId, TransactionId transactionId, Permissions permissions)
            throws TransactionAbortedException {
        try {
            while (true) {
                // Check if the transaction can acquire the lock
                if ((exclusiveLocks.containsKey(pageId) && !exclusiveLocks.get(pageId).equals(transactionId)) ||
                    (permissions.equals(Permissions.READ_WRITE) && sharedLocks.containsKey(pageId) &&
                    (sharedLocks.get(pageId).size() != 1 || !sharedLocks.get(pageId).contains(transactionId)))) {
                
                        // Add all transactions currently holding a lock on this page into the WaitForGraph
                        Set<TransactionId> currentLockHolders = getLockHolders(pageId);
                        for (TransactionId holder : currentLockHolders) {
                            addToWaitForGraph(transactionId, holder);
                        }
                        // Check whether this transaction will cause a deadlock
                        if (checkForCycle(transactionId)) {
                            // If there is a deadlock, we will remove all these transactions from the WaitForGraph
                            for (TransactionId holder : currentLockHolders) {
                                removeFromWaitForGraph(transactionId, holder);
                            }

                            // Abort the transaction by throwing an error
                            throw new TransactionAbortedException();
                        }
                        // Wait for all locks to be released and updated
                        wait();

                    } else {
                        break;
                }
            }
        // If the transaction is interrupted while waiting, return false to indicate that the lock was not acquired
        } catch (InterruptedException e) {
            return false;
        }

        // If the transaction is able to acquire the lock, update the lock tables and return true
        if (permissions.equals(Permissions.READ_ONLY)) {
            sharedLocks.computeIfAbsent(pageId, k -> new HashSet<>()).add(transactionId);
        } else {
            exclusiveLocks.put(pageId, transactionId);
        }
        return true;
    }

    public synchronized void releaseLock(TransactionId transactionId, PageId pageId) {
        sharedLocks.getOrDefault(pageId, Collections.emptySet()).remove(transactionId);
        if (sharedLocks.get(pageId) != null && sharedLocks.get(pageId).isEmpty()) {
            sharedLocks.remove(pageId);
        }
        if (transactionId.equals(exclusiveLocks.get(pageId))) {
            exclusiveLocks.remove(pageId);
        }
        notifyAll();
    }

    public synchronized void releaseAllLocks(TransactionId transactionId) {
        // Release all Write locks
        Iterator<Map.Entry<PageId, TransactionId>> exclusiveIterator = exclusiveLocks.entrySet().iterator();
        while (exclusiveIterator.hasNext()) {
            Map.Entry<PageId, TransactionId> entry = exclusiveIterator.next();
            if (entry.getValue().equals(transactionId)) {
                exclusiveIterator.remove();
            }
        }

        // Relese all Read locks
        Iterator<Map.Entry<PageId, Set<TransactionId>>> sharedIterator = sharedLocks.entrySet().iterator();
        while (sharedIterator.hasNext()) {
            Map.Entry<PageId, Set<TransactionId>> entry = sharedIterator.next();
            entry.getValue().remove(transactionId);
            if (entry.getValue().isEmpty()) {
                sharedIterator.remove();
            }
        }
    }

    public synchronized boolean holdsLock(TransactionId transactionId, PageId pageId) {
        return sharedLocks.getOrDefault(pageId, Collections.emptySet()).contains(transactionId) || transactionId.equals(exclusiveLocks.get(pageId));
    }

    // Obtain all transactions that holds a lock on a page (be it read or write)
    private Set<TransactionId> getLockHolders(PageId pageId) {
        Set<TransactionId> lockHolders = new HashSet<>();
        TransactionId exclusiveHolder = exclusiveLocks.get(pageId);
        if (exclusiveHolder != null) {
            lockHolders.add(exclusiveHolder);
        }
        Set<TransactionId> sharedHolders = sharedLocks.get(pageId);
        if (sharedHolders != null) {
            lockHolders.addAll(sharedHolders);
        }
        return lockHolders;
    }
    
    public boolean hasLock(PageId pid){
        for (PageId PageId : sharedLocks.keySet()){
            if (PageId == pid){
                return true;
            } 
        }
        for (PageId PageID : exclusiveLocks.keySet()){
            if (PageID == pid){
                return true;
            }
        }

        return false;
    }


    //----Deadlock
    private void addToWaitForGraph(TransactionId waiter, TransactionId holder) {
        waitForGraph.computeIfAbsent(waiter, k -> new HashSet<>()).add(holder);
    }

    private void removeFromWaitForGraph(TransactionId waiter, TransactionId holder) {
        if (waitForGraph.containsKey(waiter)) {
            waitForGraph.get(waiter).remove(holder);
            if (waitForGraph.get(waiter).isEmpty()) {
                waitForGraph.remove(waiter);
            }
        }
    }

    private boolean checkForCycle(TransactionId start) {
        Set<TransactionId> visited = new HashSet<>();
        return isCyclicUtil(start, visited, new HashSet<>());
    }

    private boolean isCyclicUtil(TransactionId current, Set<TransactionId> visited, Set<TransactionId> recStack) {
        if (recStack.contains(current)) {
            return true;
        }
        if (visited.contains(current)) {
            return false;
        }
        visited.add(current);
        recStack.add(current);
        Set<TransactionId> children = waitForGraph.getOrDefault(current, Collections.emptySet());
        for (TransactionId child : children) {
            if (isCyclicUtil(child, visited, recStack)) {
                return true;
            }
        }
        recStack.remove(current);
        return false;
    }

    // Non-Homocidal approach to handling Deadlock, abort this transaction and release all locks associated with it
    public void handleTransactionAbort(TransactionId transactionId) {
        this.releaseAllLocks(transactionId);
    }
}
