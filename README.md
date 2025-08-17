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
   - Test: Comparing B-tree vs TreeMap for large data lookups
   - Result: B-tree optimized for systems with slow access (like disk storage)
   - Benefit: Reduced number of node accesses due to multiple keys per node

2. **Range Queries** 🔢
   - Test: Evaluating sequential data retrieval
   - Result: Keys stored in sorted order within nodes enables efficient range operations
   - Benefit: Excellent for database index implementations

### 🐢 B-Tree Weaknesses

1. **Memory Overhead** 💾
   - Test: Measuring memory usage with different node degrees
   - Result: Higher memory overhead due to partially filled nodes
   - Issue: Nodes must allocate space for maximum number of keys and children

2. **Update Performance** ⚙️
   - Test: Comparing update speed with TreeMap and HashMap
   - Result: Slower for frequent updates due to complex rebalancing
   - Issue: Node splits and merges require moving multiple keys

3. **Sequential Operation Patterns** 📉
   - Test: Sequential vs random insertions
   - Result: Sequential inserts can lead to many node splits
   - Issue: Poor locality on sequential operations, especially in disk-based implementations

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