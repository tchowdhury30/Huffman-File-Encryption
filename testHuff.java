import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;

public class testHuff {
    public static void main(String[] args) {
        myHuffman huffman = new myHuffman();

        //String pathName = "input/WarAndPeace.txt";
        //String pathCompressed = "input/WarAndPeace_compressed.txt";
        //String pathDecompressed = "input/WarAndPeace_decompressed.txt";

        String pathName = "input/test2.txt";
        String pathCompressed = "input/test2_compressed.txt";
        String pathDecompressed = "input/test2_decompressed.txt";


        // Step 1: Count frequencies
        Map<Character, Long> frequencies = null;
        try {
            frequencies = huffman.countFrequencies(pathName);
            // Debug print
            //System.out.println("Frequencies: " + frequencies);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return; // Exit if there's an error
        }

        if (frequencies == null || frequencies.isEmpty()) {
            System.err.println("Error: Frequencies not calculated.");
            return;
        }

        // Step 2: Make code tree
        PriorityQueue<BinaryTree<CodeTreeElement>> codeTreeQueue = huffman.makeCodeTree(frequencies);
        BinaryTree<CodeTreeElement> codeTree = codeTreeQueue.poll();
        // Debug print
        //System.out.println("Code Tree: " + huffman.toString(codeTree));

        if (codeTree == null) {
            System.err.println("Error: Code tree not generated.");
            return;
        }

        // Step 3: Compute codes
        Map<Character, String> codeMap = huffman.computeCodes(codeTree);
        // Debug print
        //System.out.println("Code Map: " + codeMap);

        // Step 4: Compress file
        try {
            huffman.compressFile(codeMap, pathName, pathCompressed);
            // Debug print
            System.out.println("File compressed to " + pathCompressed);
        } catch (IOException e) {
            System.err.println("Error compressing the file: " + e.getMessage());
        }

        // Step 5: Decompress file
        try {
            huffman.decompressFile(pathCompressed, pathDecompressed, codeTree);
            // Debug print
            System.out.println("File decompressed to " + pathDecompressed);
        } catch (IOException e) {
            System.err.println("Error decompressing the file: " + e.getMessage());
        }
    }
}
