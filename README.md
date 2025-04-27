
# AVL Tree Project

## Overview

This project implements a fully functional **AVL Tree** — a self-balancing binary search tree that maintains its height balanced through rotations during insertions and deletions.

The main goal of the project is to provide an efficient data structure for sorted data storage and retrieval with **logarithmic time complexity** for operations like insert, delete, and search.

The project is implemented in **Java**, designed for clean structure, optimal performance, and ease of use.

---

## Features

- **Standard Tree Operations**:
  - `insert(int key, String info)`: Inserts a new node into the tree while maintaining balance.
  - `delete(int key)`: Removes a node and rebalances the tree if necessary.
  - `search(int key)`: Finds and returns the associated value of a given key.
- **Auxiliary Operations**:
  - `empty()`: Checks if the tree is empty.
  - `min()`, `max()`: Finds the node with the minimum or maximum key.
  - `size()`: Returns the number of nodes in the tree.
- **Array Conversions**:
  - `keysToArray()`: Returns a sorted array of all the keys in the tree.
  - `infoToArray()`: Returns a sorted array of all the info values corresponding to the keys.
- **Advanced Operations**:
  - `split(int x)`: Splits the tree into two separate AVL trees based on a given key.
  - `join(IAVLNode x, AVLTree t)`: Joins two AVL trees and a node into a single balanced tree.
- **Performance**:
  - All operations are designed for **O(log n)** time complexity in the worst case.
  - Implemented using real and virtual nodes for easier rotations and balancing.

---

## Motivation

This project was developed to better understand balanced tree data structures and to implement a high-performance solution for sorted data management.

By implementing complex operations like `split`, `join`, and balancing mechanisms, I aimed to reinforce strong software engineering practices and algorithmic thinking.

---

## How to Run

1. Make sure you have **Java 8** or higher installed.
2. Compile the project:
   ```bash
   javac AVLTree.java
   ```
3. You can build your own tester or create a main function to manipulate the AVL tree as needed.

---

## Notes

- Only standard Java libraries were used — no external libraries or dependencies.
- The codebase is modular, well-documented, and optimized for clarity and performance.

---

## Future Enhancements

- Create a visualization tool to display AVL tree operations graphically.
- Expand functionality for batch operations (bulk insertions/deletions).
- Generalize the tree to support generic types beyond integers and strings.

---

# 📚 Example of Usage

```java
AVLTree tree = new AVLTree();
tree.insert(10, "Ten");
tree.insert(20, "Twenty");
tree.insert(30, "Thirty");
System.out.println(tree.search(20)); // Output: "Twenty"
tree.delete(10);
System.out.println(tree.min()); // Output: "Twenty"
```

