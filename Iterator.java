import java.util.*;

class TreeNode {
    int value;
    List<TreeNode> neighbors;

    public TreeNode(int value) {
        this.value = value;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(TreeNode neighbor) {
        neighbors.add(neighbor);
    }
}

class DFSIterator implements Iterator<TreeNode> {
    private Stack<TreeNode> stack;

    public DFSIterator(TreeNode root) {
        stack = new Stack<>();
        stack.push(root);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public TreeNode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        TreeNode node = stack.pop();
        for (TreeNode neighbor : node.neighbors) {
            stack.push(neighbor);
        }
        return node;
    }
}

public class Iterator {
    public static void main(String[] args) {
        
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);

        node1.addNeighbor(node2);
        node1.addNeighbor(node3);
        node2.addNeighbor(node4);
        node3.addNeighbor(node5);

        DFSIterator dfsIterator = new DFSIterator(node1);

        System.out.println("DFS Traversal:");
        while (dfsIterator.hasNext()) {
            TreeNode node = dfsIterator.next();
            System.out.print(node.value + " ");
        }
    }
}
