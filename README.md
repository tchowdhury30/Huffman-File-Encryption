# Huffman Encoding Compression and Decompression

## Overview
This project implements the Huffman encoding algorithm to compress and decompress files. It demonstrates the use of trees, priority queues, and file I/O to efficiently reduce the size of text files using lossless compression. The project includes both the compression and decompression processes, as well as an optional feature to store and retrieve the Huffman tree for real-world application.

## Key Features
- **Lossless Compression**: Huffman encoding ensures that the original data is fully restored during decompression.
- **Efficient File Handling**: Reads and writes files using Java’s `BufferedReader`, `BufferedWriter`, and custom bit reader/writer classes.
- **Priority Queue for Tree Construction**: Utilizes a priority queue to build an optimal Huffman tree based on character frequencies.
- **Edge Case Handling**: Handles empty files, files with repeated characters, and other boundary cases.
- **Compression Efficiency**: Ensures the file size is reduced using Huffman encoding principles.

## Objectives
- **Generate a frequency map** of characters in a file.
- **Create a priority queue** of initial single-character trees.
- **Build a Huffman tree** based on the frequency of characters.
- **Generate a mapping** of characters to codewords using the Huffman tree.
- **Compress a file** by converting its characters into Huffman-encoded bits.
- **Decompress a file** by reversing the process and writing the original characters.
- **Handle edge cases** like empty files, files with single characters, etc.
- (Optional) Implement functionality to store and retrieve the Huffman tree during compression and decompression.

## Project Structure
- **Huffman.java**: Main class that runs the compression and decompression.
- **HuffmanInterface.java**: Interface defining the required methods for Huffman encoding and decoding.
- **CodeTreeElement.java**: Class used to represent each tree node (character and frequency).
- **BufferedBitReader.java** and **BufferedBitWriter.java**: Classes for reading and writing bits (not just bytes).
- **TreeComparator.java**: Comparator for comparing nodes in the priority queue based on frequency.

This project was made in Dartmouth's CS10