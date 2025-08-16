package com.btree;

public class Main {

    public static void main(String[] args) {
        // Create a B-tree with minimum degree 3
        System.out.println("Creating a B-tree with minimum degree 3");
        BTree<Integer, String> tree = new BTree<>(3);
        
        // Insert some key-value pairs
        System.out.println("\nInserting key-value pairs...");
        tree.insert(10, "Ten");
        tree.insert(20, "Twenty");
        tree.insert(5, "Five");
        tree.insert(6, "Six");
        tree.insert(12, "Twelve");
        tree.insert(30, "Thirty");
        tree.insert(7, "Seven");
        tree.insert(17, "Seventeen");
        
        System.out.println("\nB-tree after insertions:");
        tree.print();
        
        // Search for keys
        System.out.println("\nSearching for keys...");
        String value = tree.search(6);
        System.out.println("Value for key 6: " + value);
        
        value = tree.search(15);
        System.out.println("Value for key 15: " + (value != null ? value : "Not found"));
        
        // Delete a key
        System.out.println("\nDeleting key 6...");
        tree.delete(6);
        
        System.out.println("\nB-tree after deletion:");
        tree.print();
        
        // Verify deletion
        value = tree.search(6);
        System.out.println("\nValue for key 6 after deletion: " + (value != null ? value : "Not found"));
        
        // Insert more keys to demonstrate tree balancing
        System.out.println("\nInserting more keys to demonstrate tree balancing...");
        for (int i = 40; i <= 50; i += 2) {
            tree.insert(i, "Value" + i);
        }
        
        System.out.println("\nB-tree after more insertions:");
        tree.print();
        
        // Delete multiple keys to demonstrate rebalancing
        System.out.println("\nDeleting keys 20, 30, 40...");
        tree.delete(20);
        tree.delete(30);
        tree.delete(40);
        
        System.out.println("\nB-tree after multiple deletions:");
        tree.print();
    }

}
