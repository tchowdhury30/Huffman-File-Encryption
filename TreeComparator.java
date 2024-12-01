import java.util.Comparator;

public class TreeComparator implements Comparator<BinaryTree<CodeTreeElement>> {

    @Override
    public int compare(BinaryTree<CodeTreeElement> tree1, BinaryTree<CodeTreeElement> tree2) {
        return tree1.getData().getFrequency().compareTo(tree2.getData().getFrequency());
    }

}
