# 🌲 B-Tree Implementation

## 📚 Overview

This project implements a B-tree data structure, a self-balancing tree that maintains sorted data and allows searches, insertions, and deletions in logarithmic time. Unlike binary trees, B-trees are optimized for systems that read and write large blocks of data, such as databases and file systems.

*Reference: ["Baeldung B-Tree Data Structure"](https://www.baeldung.com/cs/b-tree-data-structure), ["Database Index Internals: Understanding the Data Structures"](https://blog.bytebytego.com/p/database-index-internals-understanding)*

## 🔑 Key Algorithm Points

### B-Tree Properties

- 🏢 Each node contains multiple keys and children (high branching factor)
- 🔄 All leaf nodes are at the same depth (perfectly balanced)
- 📏 Nodes have between t-1 and 2t-1 keys (where t is the minimum degree)
- 🌿 Non-leaf nodes have between t and 2t children
- 📦 All keys within a node are sorted
- 🔍 Every key serves as a separator between child subtrees

### Core Operations

- 🔎 **Search**: Navigate from root to leaf by comparing keys at each node
- ➕ **Insert**: Add key and split nodes when they become too full
- ➖ **Delete**: Remove key and rebalance by redistributing or merging nodes

## 🛠️ Core Methods

### BTree Class

```java
public class BTree<K extends Comparable<K>, V> {
    // Main methods
}
```

| Method | Description | Time Complexity |
|--------|-------------|-----------------|
| `search(K key)` | 🔍 Searches for a key and returns its value if found | O(log n) |
| `insert(K key, V value)` | ➕ Inserts a key-value pair into the tree | O(log n) |
| `delete(K key)` | ➖ Removes a key (and its value) from the tree | O(log n) |
| `print()` | 📄 Prints the B-tree structure | O(n) |

### Internal Helper Methods

| Method | Description |
|--------|-------------|
| `splitChild(Node<K, V> parent, int index)` | 🪓 Splits a full child node during insertion |
| `insertNonFull(Node<K, V> node, K key, V value)` | 📥 Inserts a key into a non-full node |
| `removeFromLeaf(Node<K, V> node, int idx)` | 🍃 Removes a key from a leaf node |
| `removeFromNonLeaf(Node<K, V> node, int idx)` | 🌳 Removes a key from an internal node |
| `getPredecessor(Node<K, V> node, int idx)` | ⬅️ Finds the predecessor of a key |
| `getSuccessor(Node<K, V> node, int idx)` | ➡️ Finds the successor of a key |
| `borrowFromPrev(Node<K, V> node, int idx)` | ◀️ Borrows a key from the previous child during deletion |
| `borrowFromNext(Node<K, V> node, int idx)` | ▶️ Borrows a key from the next child during deletion |
| `merge(Node<K, V> node, int idx)` | 🔄 Merges two child nodes during deletion |

## 📊 Performance Test Scenarios

### 🚀 B-Tree Strengths

1. **Random Access with Large Datasets** 📈
   - **Test Description**: Compares B-tree vs TreeMap for lookups in large datasets (100,000+ elements)
   - **Expected Results**: 
     * 🥇 B-tree should demonstrate comparable or slightly better performance for random lookups
     * With larger minimum degree (10+), B-tree's advantage becomes more significant
   - **Why**: B-trees minimize the number of disk/memory accesses with higher branching factor. Each node visit retrieves multiple keys at once, unlike binary trees where each node contains a single key.
   - **Real-world Impact**: In database systems where disk I/O is expensive, B-trees significantly outperform binary search trees.

2. **Range Queries** 🔢
   - **Test Description**: Evaluates sequential range data retrieval between B-tree and TreeMap
   - **Expected Results**: 
     * 🥇 B-tree should excel at retrieving keys in a specific range
     * Advantage increases with larger node degrees and range sizes
   - **Why**: Keys stored in sorted order within nodes allow efficient traversal between adjacent keys with fewer node jumps. Once a node is loaded, multiple keys in sequence can be processed.
   - **Real-world Impact**: Critical for database query optimizers that need to retrieve ranges of records efficiently.

### 🐢 B-Tree Weaknesses

1. **Memory Overhead** 💾
   - **Test Description**: Measures memory consumption with different tree structures and node degrees
   - **Expected Results**: 
     * 🚨 B-trees should show higher memory usage compared to binary tree implementations
     * Memory usage increases with node degree
   - **Why**: B-tree nodes must allocate space for the maximum possible number of keys and children, often resulting in partially filled nodes. This is a fundamental space-time tradeoff in the B-tree design.
   - **Real-world Impact**: In memory-constrained environments, B-trees may not be the optimal choice unless disk I/O savings outweigh the memory cost.

2. **Update Performance** ⚙️
   - **Test Description**: Compares update operation speed between B-tree, TreeMap, and HashMap
   - **Expected Results**: 
     * 🚨 B-tree updates should be slower than both TreeMap and significantly slower than HashMap
     * Performance gap widens as the size of the tree increases
   - **Why**: B-tree updates require complex rebalancing operations, including node splits, merges, and rotations. Each update might trigger cascading operations affecting multiple nodes.
   - **Real-world Impact**: Applications with extremely high write volumes might benefit from alternative structures or buffering strategies.

3. **Sequential Operation Patterns** 📉
   - **Test Description**: Tests sequential vs random insertion patterns in B-trees
   - **Expected Results**: 
     * 🚨 Sequential inserts should perform worse than random inserts in some cases
     * Performance degradation increases with strictly increasing or decreasing key sequences
   - **Why**: Sequential inserts can cause repeated splitting at the rightmost (or leftmost) path of the tree, leading to poor performance and potentially unbalanced structures. This is particularly problematic in disk-based implementations.
   - **Real-world Impact**: Applications with naturally sequential data (like timestamps) may need to consider key transformation strategies or bulk loading techniques.

## 🎯 Use Cases

- 💽 **Databases**: Ideal for database indexing due to efficient disk I/O
- 📁 **File Systems**: Many file systems use B-trees for directory structures
- 🔄 **External Memory Algorithms**: Where data doesn't fit entirely in memory
- 📊 **Data Warehousing**: For large dataset operations with infrequent updates

## 🧪 Running the Tests

Execute tests with Gradle:

```bash
./gradlew test
```

The test suite includes both functional tests (BTreeTest) and performance benchmarks (BTreePerformanceTest) that demonstrate both strengths and weaknesses of the B-tree implementation.