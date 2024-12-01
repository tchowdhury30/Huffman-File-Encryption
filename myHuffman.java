
import java.io.*;
import java.util.*;

public class myHuffman implements Huffman {

    /**
     * Read file provided in pathName and count how many times each character appears
     * @param pathName - path to a file to read
     * @return - Map with a character as a key and the number of times the character appears in the file as value
     * @throws IOException
     */
    public Map<Character, Long> countFrequencies(String pathName) throws IOException {
        // creating map to store character freqs
        Map<Character, Long> freq = new HashMap<>();
        // trying to read chars in file
        try (BufferedReader reader = new BufferedReader(new FileReader(pathName))) {
            int c;
            // until the end of the file
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                // check if char is in map and increment its val in the map accordingly
                freq.put(character, freq.getOrDefault(character, 0L) + 1);
            }
        }
        return freq;
    }

    /**
     * Construct a code tree from a map of frequency counts. Note: this code should handle the special
     * cases of empty files or files with a single character.
     *
     * @param frequencies a map of Characters with their frequency counts from countFrequencies
     * @return the code tree.
     */
    public PriorityQueue<BinaryTree<CodeTreeElement>> makeCodeTree(Map<Character, Long> frequencies) {
        // Handle empty file
        if (frequencies.isEmpty()) {
            return new PriorityQueue<>();
        }

        //creating queue with codes
        PriorityQueue<BinaryTree<CodeTreeElement>> queue = new PriorityQueue<>(new TreeComparator());

        // Add all characters and their frequencies trees to the priority queue
        Iterator<Character> frqIterator = frequencies.keySet().iterator();
        while (frqIterator.hasNext()) {
            Character key = frqIterator.next();
            Long val = frequencies.get(key);
            CodeTreeElement c = new CodeTreeElement(val, key);
            BinaryTree<CodeTreeElement> tree = new BinaryTree<>(c);
            queue.add(tree);

        }

        // Combine trees until only one remains
        while (queue.size() > 1) {
            // Extract the two lowest-frequency trees T1 and T2 from the priority queue.
            BinaryTree<CodeTreeElement> tree1 = queue.poll();
            BinaryTree<CodeTreeElement> tree2 = queue.poll();

            // Create a new tree T by creating a new root node r, attaching T1 as r's left subtree, and attaching T2 as r's right subtree.
            long combinedFrequency = tree1.getData().getFrequency() + tree2.getData().getFrequency();

            //Assign to the new tree T a frequency that equals the sum of the frequencies of T1 and T2.
            CodeTreeElement combinedElement = new CodeTreeElement(combinedFrequency, null); // null character since it's an internal node

            // Insert the new tree T into the priority queue
            BinaryTree<CodeTreeElement> combinedTree = new BinaryTree<>(combinedElement, tree1, tree2);
            queue.add(combinedTree);
        }
        return queue;
    }

    /**
     * Computes the code for all characters in the tree and enters them
     * into a map where the key is a character and the value is the code of 1's and 0's representing
     * that character.
     *
     * @param codeTree the tree for encoding characters produced by makeCodeTree
     * @return the map from characters to codes
     */
    public Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree) {
        Map<Character, String> codeMap = new HashMap<>();
        // Check if the tree has only one node (i.e., it's a leaf)
        if (codeTree.isLeaf()) {
            // Assign a default code "0" to the single character
            codeMap.put(codeTree.getData().getChar(), "0");
        } else {
            computeCodesH(codeTree, "", codeMap);
        }
        return codeMap;
    }

    /**
     * Helper method to recursively traverse tree and compute codes
     * @param tree current tree
     * @param codeN code constructed so far in traversal
     * @param codeMap map to store chars to code mapping
     */
    private void computeCodesH(BinaryTree<CodeTreeElement> tree, String codeN, Map<Character, String> codeMap) {
        // base case leaf reached end of code for a letter, store in map
        if (tree.isLeaf()) {
            codeMap.put(tree.getData().getChar(), codeN);
        // add 0 or 1 to code for each path
        } else {
            if (tree.hasLeft()) {
                computeCodesH(tree.getLeft(), codeN + "0", codeMap);
            }
            if (tree.hasRight()) {
                computeCodesH(tree.getRight(), codeN + "1", codeMap);
            }
        }
    }

    /**
     * Compress the file pathName and store compressed representation in compressedPathName.
     * @param codeMap - Map of characters to codes produced by computeCodes
     * @param pathName - File to compress
     * @param compressedPathName - Store the compressed data in this file
     * @throws IOException
     */
    public void compressFile(Map<Character, String> codeMap, String pathName, String compressedPathName) throws IOException {
        // Handle empty file or single character
        if (codeMap.isEmpty()) {
            return;
        }

        // open bitwriter for output file
        BufferedBitWriter bitInput = new BufferedBitWriter(compressedPathName);
        // open text input file
        try (BufferedReader input = new BufferedReader(new FileReader(pathName))) {
            int c;
            // iterate thru each character
            while ((c = input.read()) != -1) {
                char character = (char) c;

                // System.out.println("Read character: " + character); // Debug print

                // get code for character
                String code = codeMap.get(character);

                // System.out.println("Writing code: " + code); // Debug print

                // translate char of 0 to 1 to bits and write in output file
                for (char sBit : code.toCharArray()) {
                    if (sBit == '1') {
                        bitInput.writeBit(true);
                    } else if (sBit == '0') {
                        bitInput.writeBit(false);
                    }
                }
            }
            bitInput.close(); // close compressed file
        }
    }


    /**
     * Decompress file compressedPathName and store plain text in decompressedPathName.
     * @param compressedPathName - file created by compressFile
     * @param decompressedPathName - store the decompressed text in this file, contents should match the original file before compressFile
     * @param codeTree - Tree mapping compressed data to characters
     * @throws IOException
     */
    public void decompressFile(String compressedPathName, String decompressedPathName, BinaryTree<CodeTreeElement> codeTree) throws IOException {
        // Handle empty file or single character
        if (codeTree == null || codeTree.getData() == null) {
            return;
        }

        try (BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName))) {
            BufferedBitReader bitOutput = new BufferedBitReader(compressedPathName);
            BinaryTree<CodeTreeElement> curr = codeTree;
            while (bitOutput.hasNext()) {
                boolean bit = bitOutput.readBit();

                //System.out.println("Read bit: " + (bit ? "1" : "0")); // Debug print

                // If the tree has only one node, write the character for every bit
                if (codeTree.isLeaf()) {
                    output.write(codeTree.getData().getChar());
                    continue;
                }
                if (bit) { // 1 go right
                    curr = curr.getRight();
                } else { // 0 go left
                    curr = curr.getLeft();
                }
                // Check if we've reached a leaf node and finished code for this character
                if (curr.isLeaf()) {

                    //System.out.println("Decoded character: " + curr.getData().getChar()); // Debug print

                    output.write(curr.getData().getChar()); // write the char to the file
                    curr = codeTree; // Reset to the root for the next character
                }
            }
            bitOutput.close(); // close compressed file
            output.close(); // close decompressed file
        }

    }

    public String toString(BinaryTree<CodeTreeElement> codeTree) {
        if (codeTree.size() == 1) {
            return "Empty CodeTree";
        }
        return treeToString(codeTree); // Peek to get the root without removing it
    }

    private String treeToString(BinaryTree<CodeTreeElement> tree) {
        if (tree == null) {
            return "";
        }
        if (tree.isLeaf()) {
            return "'" + tree.getData().getChar() + "'=" + tree.getData().getFrequency();
        } else {
            return "Node(" + tree.getData().getFrequency() + ")["
                    + treeToString(tree.getLeft()) + ", "
                    + treeToString(tree.getRight()) + "]";
        }
    }
}
