package com.btree;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic implementation of a B-tree data structure
 * 
 * @param <K> Type of keys stored in the B-tree (must implement Comparable)
 * @param <V> Type of values associated with keys
 */
public class BTree<K extends Comparable<K>, V> {
    
    // The minimum degree of the B-tree
    // Minimum number of keys in a node (except root) is t-1
    // Maximum number of keys in a node is 2t-1
    private final int t;
    
    // Root node of the B-tree
    private Node root;
    
    /**
     * Creates a new empty B-tree with the specified minimum degree
     * 
     * @param t Minimum degree of the B-tree (minimum number of children per node)
     */
    public BTree(int t) {
        if (t < 2) {
            throw new IllegalArgumentException("Minimum degree must be at least 2");
        }
        this.t = t;
        this.root = new Node(true);
    }
    
    /**
     * Searches for a key in the B-tree
     * 
     * @param key The key to search for
     * @return The value associated with the key, or null if not found
     */
    public V search(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return root.search(key);
    }
    
    /**
     * Inserts a key-value pair into the B-tree
     * 
     * @param key The key to insert
     * @param value The value associated with the key
     */
    public void insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // If root is full, create a new root
        if (root.keys.size() == 2 * t - 1) {
            Node newRoot = new Node(false);
            newRoot.children.add(root);
            root = newRoot;
            splitChild(root, 0);
        }
        
        root.insertNonFull(key, value);
    }
    
    /**
     * Deletes a key and its associated value from the B-tree
     * 
     * @param key The key to delete
     */
    public void delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        root.delete(key);
        
        // If the root has no keys and has a child, make the child the new root
        if (root.keys.isEmpty() && !root.isLeaf) {
            root = root.children.get(0);
        }
    }
    
    /**
     * Prints the B-tree structure (for debugging)
     */
    public void print() {
        printNode(root, 0);
    }
    
    private void printNode(Node node, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("  ");
        }
        
        System.out.print(indent + "Keys: ");
        for (int i = 0; i < node.keys.size(); i++) {
            System.out.print(node.keys.get(i) + " ");
        }
        System.out.println();
        
        if (!node.isLeaf) {
            for (Node child : node.children) {
                printNode(child, level + 1);
            }
        }
    }
    
    /**
     * Splits a full child of a node
     * 
     * @param parent The parent node
     * @param index Index of the full child in parent's children list
     */
    private void splitChild(Node parent, int index) {
        Node fullChild = parent.children.get(index);
        Node newNode = new Node(fullChild.isLeaf);
        
        // Move the t to 2t-1 keys and values from fullChild to newNode
        for (int j = 0; j < t - 1; j++) {
            newNode.keys.add(fullChild.keys.get(j + t));
            newNode.values.add(fullChild.values.get(j + t));
        }
        
        // Move children if not a leaf
        if (!fullChild.isLeaf) {
            for (int j = 0; j < t; j++) {
                newNode.children.add(fullChild.children.get(j + t));
            }
            // Remove moved children from fullChild
            for (int j = 0; j < t; j++) {
                fullChild.children.remove(fullChild.children.size() - 1);
            }
        }
        
        // Remove moved keys from fullChild
        for (int j = 0; j < t - 1; j++) {
            fullChild.keys.remove(fullChild.keys.size() - 1);
            fullChild.values.remove(fullChild.values.size() - 1);
        }
        
        // Insert a new child into parent
        parent.children.add(index + 1, newNode);
        
        // Move the middle key to the parent
        K middleKey = fullChild.keys.get(t - 1);
        V middleValue = fullChild.values.get(t - 1);
        parent.keys.add(index, middleKey);
        parent.values.add(index, middleValue);
        
        // Remove middle key from the full child
        fullChild.keys.remove(t - 1);
        fullChild.values.remove(t - 1);
    }
    
    /**
     * Node class for the B-tree
     */
    private class Node {
        // Whether this node is a leaf
        boolean isLeaf;
        
        // Keys in the node (sorted)
        List<K> keys;
        
        // Values associated with keys
        List<V> values;
        
        // Child pointers
        List<Node> children;
        
        /**
         * Creates a new empty node
         * 
         * @param isLeaf Whether the node is a leaf
         */
        Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
            this.children = new ArrayList<>();
        }
        
        /**
         * Searches for a key in this node and its subtree
         * 
         * @param key The key to search for
         * @return The value associated with the key, or null if not found
         */
        V search(K key) {
            int i = 0;
            
            // Find the first key greater than or equal to k
            while (i < keys.size() && key.compareTo(keys.get(i)) > 0) {
                i++;
            }
            
            // If the key is found
            if (i < keys.size() && key.compareTo(keys.get(i)) == 0) {
                return values.get(i);
            }
            
            // If this is a leaf node, the key is not in the tree
            if (isLeaf) {
                return null;
            }
            
            // Recursively search in the appropriate subtree
            return children.get(i).search(key);
        }
        
        /**
         * Inserts a key-value pair into this node (assuming the node is not full)
         * 
         * @param key The key to insert
         * @param value The value associated with the key
         */
        void insertNonFull(K key, V value) {
            int i = keys.size() - 1;
            
            // If this is a leaf node, insert the key-value pair in the correct position
            if (isLeaf) {
                // Find the correct position for the key
                while (i >= 0 && key.compareTo(keys.get(i)) < 0) {
                    i--;
                }
                
                // If the key already exists, update the value
                if (i >= 0 && key.compareTo(keys.get(i)) == 0) {
                    values.set(i, value);
                    return;
                }
                
                // Insert the key-value pair
                keys.add(i + 1, key);
                values.add(i + 1, value);
            } else {
                // Find the child which is going to have the new key
                while (i >= 0 && key.compareTo(keys.get(i)) < 0) {
                    i--;
                }
                
                // If the key already exists, update the value
                if (i >= 0 && key.compareTo(keys.get(i)) == 0) {
                    values.set(i, value);
                    return;
                }
                
                i++;
                
                // If the child is full, split it
                if (children.get(i).keys.size() == 2 * t - 1) {
                    splitChild(this, i);
                    
                    // The middle key from the child moved up to this node
                    // Determine which child to go down to
                    if (key.compareTo(keys.get(i)) > 0) {
                        i++;
                    }
                }
                
                // Recursively insert into the appropriate child
                children.get(i).insertNonFull(key, value);
            }
        }
        
        /**
         * Deletes a key from this node and its subtree
         * 
         * @param key The key to delete
         */
        void delete(K key) {
            int idx = findKeyIndex(key);
            
            // Case 1: The key is present in this node
            if (idx < keys.size() && keys.get(idx).compareTo(key) == 0) {
                // Case 1a: If the node is a leaf, simply remove the key
                if (isLeaf) {
                    removeKeyAtIndex(idx);
                } else {
                    // Case 1b: If the node is an internal node
                    // Case 1b-1: Predecessor has at least t keys
                    Node pred = children.get(idx);
                    if (pred.keys.size() >= t) {
                        // Replace key with its predecessor
                        K predKey = getPredecessor(idx);
                        V predValue = getPredecessorValue(idx);
                        keys.set(idx, predKey);
                        values.set(idx, predValue);
                        // Delete the predecessor from the child
                        pred.delete(predKey);
                    }
                    // Case 1b-2: Successor has at least t keys
                    else {
                        Node succ = children.get(idx + 1);
                        if (succ.keys.size() >= t) {
                            // Replace key with its successor
                            K succKey = getSuccessor(idx);
                            V succValue = getSuccessorValue(idx);
                            keys.set(idx, succKey);
                            values.set(idx, succValue);
                            // Delete the successor from the child
                            succ.delete(succKey);
                        }
                        // Case 1b-3: Both predecessor and successor have t-1 keys
                        else {
                            // Merge key and successor into predecessor
                            mergeNodes(idx);
                            // Delete key from the merged node
                            pred.delete(key);
                        }
                    }
                }
            }
            // Case 2: The key is not present in this node
            else {
                // If this is a leaf, the key is not in the tree
                if (isLeaf) {
                    return;
                }
                
                // Flag to indicate if we're at the last child
                boolean isLast = (idx == keys.size());
                
                // If the child has only t-1 keys, fill it
                if (children.get(idx).keys.size() < t) {
                    fillChild(idx);
                }
                
                // If the last child has been merged, go to the previous child
                if (isLast && idx > keys.size()) {
                    children.get(idx - 1).delete(key);
                } else {
                    children.get(idx).delete(key);
                }
            }
        }
        
        /**
         * Finds the index of a key in this node
         * 
         * @param key The key to find
         * @return The index of the key or the index where it should be inserted
         */
        private int findKeyIndex(K key) {
            int idx = 0;
            while (idx < keys.size() && key.compareTo(keys.get(idx)) > 0) {
                idx++;
            }
            return idx;
        }
        
        /**
         * Removes a key and its associated value at the given index
         * 
         * @param idx The index to remove
         */
        private void removeKeyAtIndex(int idx) {
            keys.remove(idx);
            values.remove(idx);
        }
        
        /**
         * Gets the predecessor key of the key at the given index
         * 
         * @param idx The index of the key
         * @return The predecessor key
         */
        private K getPredecessor(int idx) {
            // Go to the rightmost node in the left subtree
            Node curr = children.get(idx);
            while (!curr.isLeaf) {
                curr = curr.children.get(curr.children.size() - 1);
            }
            // Return the last key
            return curr.keys.get(curr.keys.size() - 1);
        }
        
        /**
         * Gets the predecessor value of the key at the given index
         * 
         * @param idx The index of the key
         * @return The predecessor value
         */
        private V getPredecessorValue(int idx) {
            // Go to the rightmost node in the left subtree
            Node curr = children.get(idx);
            while (!curr.isLeaf) {
                curr = curr.children.get(curr.children.size() - 1);
            }
            // Return the last value
            return curr.values.get(curr.values.size() - 1);
        }
        
        /**
         * Gets the successor key of the key at the given index
         * 
         * @param idx The index of the key
         * @return The successor key
         */
        private K getSuccessor(int idx) {
            // Go to the leftmost node in the right subtree
            Node curr = children.get(idx + 1);
            while (!curr.isLeaf) {
                curr = curr.children.get(0);
            }
            // Return the first key
            return curr.keys.get(0);
        }
        
        /**
         * Gets the successor value of the key at the given index
         * 
         * @param idx The index of the key
         * @return The successor value
         */
        private V getSuccessorValue(int idx) {
            // Go to the leftmost node in the right subtree
            Node curr = children.get(idx + 1);
            while (!curr.isLeaf) {
                curr = curr.children.get(0);
            }
            // Return the first value
            return curr.values.get(0);
        }
        
        /**
         * Merges the child at idx with the child at idx+1
         * 
         * @param idx The index of the first child
         */
        private void mergeNodes(int idx) {
            Node child = children.get(idx);
            Node sibling = children.get(idx + 1);
            
            // Add the key from this node to the child
            child.keys.add(keys.get(idx));
            child.values.add(values.get(idx));
            
            // Copy all keys and values from sibling to child
            child.keys.addAll(sibling.keys);
            child.values.addAll(sibling.values);
            
            // Copy all children from sibling to child if not leaf
            if (!child.isLeaf) {
                child.children.addAll(sibling.children);
            }
            
            // Remove the key and the sibling from this node
            keys.remove(idx);
            values.remove(idx);
            children.remove(idx + 1);
        }
        
        /**
         * Fills the child at idx which has fewer than t-1 keys
         * 
         * @param idx The index of the child to fill
         */
        private void fillChild(int idx) {
            // If previous child has at least t keys, borrow from it
            if (idx > 0 && children.get(idx - 1).keys.size() >= t) {
                borrowFromPrev(idx);
            }
            // If next child has at least t keys, borrow from it
            else if (idx < keys.size() && children.get(idx + 1).keys.size() >= t) {
                borrowFromNext(idx);
            }
            // Merge with sibling
            else {
                if (idx < keys.size()) {
                    mergeNodes(idx);
                } else {
                    mergeNodes(idx - 1);
                }
            }
        }
        
        /**
         * Borrows a key from the previous child
         * 
         * @param idx The index of the child that needs a key
         */
        private void borrowFromPrev(int idx) {
            Node child = children.get(idx);
            Node sibling = children.get(idx - 1);
            
            // Move all keys and values in child one step ahead
            child.keys.add(0, keys.get(idx - 1));
            child.values.add(0, values.get(idx - 1));
            
            // If not leaf, move the last child of sibling to child
            if (!child.isLeaf) {
                child.children.add(0, sibling.children.get(sibling.children.size() - 1));
                sibling.children.remove(sibling.children.size() - 1);
            }
            
            // Move the last key of sibling to this node
            keys.set(idx - 1, sibling.keys.get(sibling.keys.size() - 1));
            values.set(idx - 1, sibling.values.get(sibling.values.size() - 1));
            
            // Remove the last key from sibling
            sibling.keys.remove(sibling.keys.size() - 1);
            sibling.values.remove(sibling.values.size() - 1);
        }
        
        /**
         * Borrows a key from the next child
         * 
         * @param idx The index of the child that needs a key
         */
        private void borrowFromNext(int idx) {
            Node child = children.get(idx);
            Node sibling = children.get(idx + 1);
            
            // Move key from this node to child
            child.keys.add(keys.get(idx));
            child.values.add(values.get(idx));
            
            // If not leaf, move the first child of sibling to child
            if (!child.isLeaf) {
                child.children.add(sibling.children.get(0));
                sibling.children.remove(0);
            }
            
            // Move the first key of sibling to this node
            keys.set(idx, sibling.keys.get(0));
            values.set(idx, sibling.values.get(0));
            
            // Remove the first key from sibling
            sibling.keys.remove(0);
            sibling.values.remove(0);
        }
    }
}
