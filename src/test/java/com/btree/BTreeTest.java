package com.btree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BTreeTest {
    
    private BTree<Integer, String> tree;
    
    @BeforeEach
    void setUp() {
        tree = new BTree<>(3);
    }
    
    @Test
    @DisplayName("Test creating B-tree")
    void testCreation() {
        assertThat(tree).isNotNull();
    }
    
    @Test
    @DisplayName("Test creating B-tree with invalid degree")
    void testInvalidDegree() {
        assertThatThrownBy(() -> new BTree<>(1))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("Test inserting and searching elements")
    void testInsertAndSearch() {
        tree.insert(10, "Ten");
        tree.insert(20, "Twenty");
        tree.insert(5, "Five");
        
        assertThat(tree.search(10)).isEqualTo("Ten");
        assertThat(tree.search(20)).isEqualTo("Twenty");
        assertThat(tree.search(5)).isEqualTo("Five");
        assertThat(tree.search(15)).isNull();
    }
    
    @Test
    @DisplayName("Test updating existing key")
    void testUpdate() {
        tree.insert(10, "Ten");
        assertThat(tree.search(10)).isEqualTo("Ten");
        
        tree.insert(10, "New Ten");
        assertThat(tree.search(10)).isEqualTo("New Ten");
    }
    
    @Test
    @DisplayName("Test searching with null key")
    void testSearchNull() {
        assertThatThrownBy(() -> tree.search(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("Test inserting with null key")
    void testInsertNull() {
        assertThatThrownBy(() -> tree.insert(null, "Value"))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("Test deleting elements")
    void testDelete() {
        tree.insert(10, "Ten");
        tree.insert(20, "Twenty");
        tree.insert(5, "Five");
        
        assertThat(tree.search(10)).isEqualTo("Ten");
        
        tree.delete(10);
        assertThat(tree.search(10)).isNull();
        assertThat(tree.search(20)).isEqualTo("Twenty");
        assertThat(tree.search(5)).isEqualTo("Five");
    }
    
    @Test
    @DisplayName("Test deleting non-existent element")
    void testDeleteNonExistent() {
        tree.insert(10, "Ten");
        
        // This should not throw an exception
        tree.delete(20);
        
        assertThat(tree.search(10)).isEqualTo("Ten");
    }
    
    @Test
    @DisplayName("Test deleting with null key")
    void testDeleteNull() {
        assertThatThrownBy(() -> tree.delete(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("Test B-tree node splitting")
    void testNodeSplitting() {
        for (int i = 1; i <= 10; i++) {
            tree.insert(i, "Value" + i);
        }
        
        for (int i = 1; i <= 10; i++) {
            assertThat(tree.search(i)).isEqualTo("Value" + i);
        }
    }
    
    @Test
    @DisplayName("Test complex operations - mix of insert and delete")
    void testComplexOperations() {
        // Insert multiple values
        for (int i = 1; i <= 20; i++) {
            tree.insert(i, "Value" + i);
        }
        
        // Verify all values
        for (int i = 1; i <= 20; i++) {
            assertThat(tree.search(i)).isEqualTo("Value" + i);
        }
        
        // Delete some values
        tree.delete(5);
        tree.delete(10);
        tree.delete(15);
        
        // Verify deleted values
        assertThat(tree.search(5)).isNull();
        assertThat(tree.search(10)).isNull();
        assertThat(tree.search(15)).isNull();
        
        // Verify remaining values
        for (int i = 1; i <= 20; i++) {
            if (i != 5 && i != 10 && i != 15) {
                assertThat(tree.search(i)).isEqualTo("Value" + i);
            }
        }
        
        // Insert the deleted values again
        tree.insert(5, "NewValue5");
        tree.insert(10, "NewValue10");
        tree.insert(15, "NewValue15");
        
        // Verify the new values
        assertThat(tree.search(5)).isEqualTo("NewValue5");
        assertThat(tree.search(10)).isEqualTo("NewValue10");
        assertThat(tree.search(15)).isEqualTo("NewValue15");
    }
    
    @Test
    @DisplayName("Test deleting from empty tree")
    void testDeleteFromEmptyTree() {
        tree.delete(10);
        assertThat(tree.search(10)).isNull();
    }

}
