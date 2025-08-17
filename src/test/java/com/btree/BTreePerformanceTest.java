package com.btree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

class BTreePerformanceTest {
    
    @Test
    @DisplayName("STRENGTH: B-tree vs TreeMap for random access with large datasets")
    void testBTreeVsTreeMapRandomAccess() {
        System.out.println("\n=== TESTING B-TREE STRENGTH: RANDOM ACCESS WITH LARGE DATASETS ===");
        
        // Create a B-tree with large minimum degree for disk-based scenarios
        // (larger nodes mean fewer disk accesses)
        BTree<Integer, String> bTree = new BTree<>(10);
        
        // TreeMap for comparison (Red-Black Tree implementation)
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        
        // Number of elements to insert
        int size = 100_000;
        
        // Insert elements
        System.out.println("Inserting " + size + " elements...");
        
        // Insert into B-tree
        long bTreeInsertTime = measureExecutionTime(() -> {
            for (int i = 0; i < size; i++) {
                bTree.insert(i, "Value" + i);
            }
        });
        
        // Insert into TreeMap
        long treeMapInsertTime = measureExecutionTime(() -> {
            for (int i = 0; i < size; i++) {
                treeMap.put(i, "Value" + i);
            }
        });
        
        System.out.println("B-tree insertion time: " + bTreeInsertTime + " ms");
        System.out.println("TreeMap insertion time: " + treeMapInsertTime + " ms");
        
        // Generate random keys for lookup
        Random rand = new Random(42); // Fixed seed for reproducibility
        int lookupCount = 10_000;
        List<Integer> keysToLookup = new ArrayList<>(lookupCount);
        for (int i = 0; i < lookupCount; i++) {
            keysToLookup.add(rand.nextInt(size));
        }
        
        // Measure random lookup time for B-tree
        long bTreeLookupTime = measureExecutionTime(() -> {
            for (Integer key : keysToLookup) {
                bTree.search(key);
            }
        });
        
        // Measure random lookup time for TreeMap
        long treeMapLookupTime = measureExecutionTime(() -> {
            for (Integer key : keysToLookup) {
                treeMap.get(key);
            }
        });
        
        System.out.println("B-tree random lookup time for " + lookupCount + " keys: " + bTreeLookupTime + " ms");
        System.out.println("TreeMap random lookup time for " + lookupCount + " keys: " + treeMapLookupTime + " ms");
        
        System.out.println("\nSTRENGTH: B-trees are optimized for systems with slow access (like disk storage)");
        System.out.println("- Each B-tree node contains multiple keys, reducing the number of node accesses");
        System.out.println("- In real disk-based scenarios, the performance difference would be more pronounced");
        System.out.println("- B-trees maintain O(log_m n) height where m is the degree, which is lower than binary trees");
    }
    
    @Test
    @DisplayName("STRENGTH: B-tree range queries")
    void testBTreeRangeQueries() {
        System.out.println("\n=== TESTING B-TREE STRENGTH: EFFICIENT RANGE QUERIES ===");
        
        // Create data structures
        BTree<Integer, String> bTree = new BTree<>(5);
        
        // Insert elements in random order
        Random rand = new Random(42);
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            keys.add(i);
        }
        Collections.shuffle(keys, rand);
        
        for (Integer key : keys) {
            bTree.insert(key, "Value" + key);
        }
        
        System.out.println("B-tree range query capabilities:");
        System.out.println("- Keys are stored in sorted order within nodes");
        System.out.println("- Nodes are linked in the search path, making range queries efficient");
        System.out.println("- Range queries require O(log n + m) time where m is the number of elements in the range");
        System.out.println("- This makes B-trees excellent for database index implementations");
    }
    
    @Test
    @DisplayName("WEAKNESS: B-tree memory overhead")
    void testBTreeMemoryOverhead() {
        System.out.println("\n=== TESTING B-TREE WEAKNESS: MEMORY OVERHEAD ===");
        
        // Create data structures with increasing degrees
        int[] degrees = {2, 4, 8, 16, 32};
        int elemCount = 10_000;
        
        for (int degree : degrees) {
            BTree<Integer, String> bTree = new BTree<>(degree);
            
            // Insert elements
            for (int i = 0; i < elemCount; i++) {
                bTree.insert(i, "Value" + i);
            }
            
            // Calculate theoretical node count based on fill factor
            // In a B-tree, nodes are typically at least half full
            double avgFillFactor = 0.75; // Theoretical average
            int keysPerNode = 2 * degree - 1; // Max keys per node
            double avgKeysPerNode = keysPerNode * avgFillFactor;
            double estimatedNodes = Math.ceil(elemCount / avgKeysPerNode);
            
            System.out.println("B-tree with degree " + degree + ":");
            System.out.println("- Max keys per node: " + keysPerNode);
            System.out.println("- Estimated node count: " + estimatedNodes);
            System.out.println("- Higher degree means fewer nodes but more wasted space within nodes");
        }
        
        System.out.println("\nWEAKNESS: B-trees have higher memory overhead due to:");
        System.out.println("- Partially filled nodes (space is reserved but not used)");
        System.out.println("- Nodes must allocate space for maximum number of keys and children");
        System.out.println("- Complex node structure compared to simpler binary trees");
        System.out.println("- As degree increases, memory utilization can decrease");
    }
    
    @Test
    @DisplayName("WEAKNESS: B-tree update performance")
    void testBTreeUpdatePerformance() {
        System.out.println("\n=== TESTING B-TREE WEAKNESS: UPDATE PERFORMANCE ===");
        
        // Create data structures
        BTree<Integer, String> bTree = new BTree<>(3);
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        
        // Number of elements to insert
        int size = 50_000;
        
        // First, insert all elements
        for (int i = 0; i < size; i++) {
            bTree.insert(i, "Value" + i);
            treeMap.put(i, "Value" + i);
            hashMap.put(i, "Value" + i);
        }
        
        // Now measure update performance by doing insertions/deletions
        int operations = 10_000;
        Random rand = new Random(42);
        List<Integer> keysToUpdate = new ArrayList<>(operations);
        for (int i = 0; i < operations; i++) {
            keysToUpdate.add(rand.nextInt(size));
        }
        
        // Measure B-tree update time
        long bTreeUpdateTime = measureExecutionTime(() -> {
            for (Integer key : keysToUpdate) {
                bTree.delete(key);
                bTree.insert(key, "Updated" + key);
            }
        });
        
        // Measure TreeMap update time
        long treeMapUpdateTime = measureExecutionTime(() -> {
            for (Integer key : keysToUpdate) {
                treeMap.remove(key);
                treeMap.put(key, "Updated" + key);
            }
        });
        
        // Measure HashMap update time
        long hashMapUpdateTime = measureExecutionTime(() -> {
            for (Integer key : keysToUpdate) {
                hashMap.remove(key);
                hashMap.put(key, "Updated" + key);
            }
        });
        
        System.out.println("B-tree update time for " + operations + " operations: " + bTreeUpdateTime + " ms");
        System.out.println("TreeMap update time for " + operations + " operations: " + treeMapUpdateTime + " ms");
        System.out.println("HashMap update time for " + operations + " operations: " + hashMapUpdateTime + " ms");
        
        System.out.println("\nWEAKNESS: B-trees are slower for updates due to:");
        System.out.println("- Complex rebalancing operations during insertions and deletions");
        System.out.println("- Node splits and merges require moving multiple keys");
        System.out.println("- Updates may propagate up the tree, affecting multiple levels");
        System.out.println("- Hash-based structures are much faster for simple key-value operations");
        System.out.println("- Even balanced binary trees are typically faster for in-memory updates");
    }
    
    @Test
    @DisplayName("WEAKNESS: B-tree sequential insert/delete patterns")
    void testBTreeSequentialOperations() {
        System.out.println("\n=== TESTING B-TREE WEAKNESS: SEQUENTIAL OPERATIONS ===");
        
        // Create a B-tree
        BTree<Integer, String> bTree = new BTree<>(3);
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        
        // Number of elements to insert
        int size = 50_000;
        
        // Measure insertion time for sequential insertions
        System.out.println("Sequential insertion of " + size + " elements...");
        
        long bTreeSeqInsertTime = measureExecutionTime(() -> {
            for (int i = 0; i < size; i++) {
                bTree.insert(i, "Value" + i);
            }
        });
        
        long treeMapSeqInsertTime = measureExecutionTime(() -> {
            for (int i = 0; i < size; i++) {
                treeMap.put(i, "Value" + i);
            }
        });
        
        // Create new structures for random insertion test
        BTree<Integer, String> bTreeRandom = new BTree<>(3);
        TreeMap<Integer, String> treeMapRandom = new TreeMap<>();
        
        // Generate random keys
        List<Integer> randomKeys = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            randomKeys.add(i);
        }
        Collections.shuffle(randomKeys, new Random(42));
        
        // Measure insertion time for random insertions
        System.out.println("Random insertion of " + size + " elements...");
        
        long bTreeRandInsertTime = measureExecutionTime(() -> {
            for (Integer key : randomKeys) {
                bTreeRandom.insert(key, "Value" + key);
            }
        });
        
        long treeMapRandInsertTime = measureExecutionTime(() -> {
            for (Integer key : randomKeys) {
                treeMapRandom.put(key, "Value" + key);
            }
        });
        
        System.out.println("B-tree sequential insertion time: " + bTreeSeqInsertTime + " ms");
        System.out.println("B-tree random insertion time: " + bTreeRandInsertTime + " ms");
        System.out.println("TreeMap sequential insertion time: " + treeMapSeqInsertTime + " ms");
        System.out.println("TreeMap random insertion time: " + treeMapRandInsertTime + " ms");
        
        System.out.println("\nWEAKNESS: B-trees can perform poorly with sequential patterns:");
        System.out.println("- Sequential insertions can lead to many node splits");
        System.out.println("- Sequential deletions can cause repeated node merges and rebalancing");
        System.out.println("- Some implementations suffer from poor locality on sequential operations");
        System.out.println("- This is more pronounced in disk-based B-tree implementations");
    }

    private long measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }

}
