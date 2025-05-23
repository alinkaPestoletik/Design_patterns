/**
 * The author is Alina Pestova
 * Data of creation: 02.05.2025
 */

import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.math.RoundingMode;

/**
 * Main class to execute the program
 */
public class Main {
    /**
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, Directory> directories = new HashMap<>();
        Directory root = new Directory(0, ".");
        directories.put(0, root);

        command(scanner, directories, root);
        calculateTotalSize(root);
        Tree.printTree(root);
        scanner.close();
    }

    /**
     * Processes commands to create directories and files
     *
     * @param scanner to read input
     * @param directories map of directories
     * @param root directory
     */
    private static void command(Scanner scanner, Map<Integer, Directory> directories, Directory root) {
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+", 7);
            if (parts.length == 0)
                continue;
            String command = parts[0];

            if (command.equals("DIR")) {
                int id;
                int parentId;
                String name;

                if (parts.length == 3) {
                    id = Integer.parseInt(parts[1]);
                    name = parts[2];
                    parentId = 0;
                } else if (parts.length == 4) {
                    id = Integer.parseInt(parts[1]);
                    parentId = Integer.parseInt(parts[2]);
                    name = parts[3];
                } else {
                    continue;
                }

                Directory directory = new Directory(id, name);
                Directory parentDir = directories.get(parentId);
                if (parentDir != null) {
                    parentDir.addChild(directory);
                    directories.put(id, directory);
                }

            } else if (command.equals("FILE")) {
                int parentId = Integer.parseInt(parts[1]);
                boolean readOnly = parts[2].equals("T");
                String owner = parts[3];
                String group = parts[4];
                double sizeKB = Double.parseDouble(parts[5]);
                String fullName = parts[6];

                File newFile = new File(fullName, sizeKB, readOnly, owner, group);
                Directory parentDir = directories.get(parentId);
                if (parentDir != null) {
                    parentDir.addChild(newFile);
                }
            }
        }
    }

    /**
     * Calculates the total size of the root directory
     *
     * @param root directory
     */
    private static void calculateTotalSize(Directory root) {
        SizeVisitor sizeVisitor = new SizeVisitor();
        root.accept(sizeVisitor);

        double totalSize = sizeVisitor.getSize();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        DecimalFormat df = new DecimalFormat("#.#", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        String strTotalSize = df.format(totalSize) + "KB";

        System.out.println("total: " + strTotalSize);
    }
}

/**
 * Class representing file properties
 */
class FileProperties {
    private final String extension;
    private final boolean readOnly;
    private final String owner;
    private final String group;

    /**
     * Constructor for FileProperties
     *
     * @param extension File extension
     * @param readOnly Read-only flag
     * @param owner File owner
     * @param group File group
     */
    public FileProperties(String extension, boolean readOnly, String owner, String group) {
        this.extension = extension;
        this.readOnly = readOnly;
        this.owner = owner;
        this.group = group;
    }
}

/**
 * Factory class for creating and caching FileProperties instances
 */
class FilePropertiesFactory {
    private static final Map<String, FileProperties> properties = new HashMap<>();
    /**
     * Gets a FileProperties instance, creating and caching it if necessary
     *
     * @param extension File extension
     * @param readOnly Read-only flag
     * @param owner File owner
     * @param group File group
     * @return File properties instance
     */
    public static FileProperties getFileProperties(String extension, boolean readOnly, String owner, String group) {
        String key = extension + readOnly + owner + group;
        if (!properties.containsKey(key)) {
            properties.put(key, new FileProperties(extension, readOnly, owner, group));
        }
        return properties.get(key);
    }
}

/**
 * Visitor interface for visiting files and directories
 */
interface Visitor {
    void visit(File file);
    void visit(Directory directory);
}

/**
 * Visitable interface for accepting visitors
 */
interface Element {
    void accept(Visitor visitor);
}

/**
 * Abstract class representing a node in the file system
 */
abstract class Node implements Element {
    protected String name;
    protected Directory parent;
    protected FileProperties properties;

    @Override
    public void accept(Visitor visitor) {}

    /**
     * Constructor for Node
     *
     * @param name Node name
     */
    public Node(String name) {
        this.name = name;
        this.parent = null;
    }

    /**
     * @return node name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the parent directory
     *
     * @param parent directory
     */
    public void setParent(Directory parent) {
        this.parent = parent;
    }

    /**
     * @return size in KB
     */
    public abstract double getSizeKB();

    @Override
    public String toString() {
        return getName();
    }
}

/**
 * Class representing a file in the file system
 */
class File extends Node {
    private final double sizeKB;
    private final String fullName;

    /**
     * Constructor for File
     *
     * @param fullName Full name of the file
     * @param sizeKB Size of the file in KB
     * @param readOnly Read-only flag
     * @param owner File owner
     * @param group File group
     */
    public File(String fullName, double sizeKB, boolean readOnly, String owner, String group) {
        super(name(fullName));
        this.fullName = fullName;
        this.sizeKB = sizeKB;
        String extension = getExtension(fullName);
        this.properties = FilePropertiesFactory.getFileProperties(extension, readOnly, owner, group);
    }

    private static String name(String fullName) {
        int ind = fullName.lastIndexOf('.');
        if (ind > 0 && ind < fullName.length() - 1) {
            return fullName.substring(0, ind);
        } else {
            return fullName;
        }
    }

    private static String getExtension(String fullName) {
        int ind = fullName.lastIndexOf('.');
        if (ind > 0 && ind < fullName.length() - 1) {
            return fullName.substring(ind + 1);
        } else {
            return "";
        }
    }

    /**
     * @return full name of the file
     */
    public String getFullName() {
        return fullName;
    }

    @Override
    public double getSizeKB() {
        return sizeKB;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        df.setRoundingMode(java.math.RoundingMode.HALF_UP);
        String sizeStr = df.format(sizeKB) + "KB";
        return getFullName() + " (" + sizeStr + ")";
    }
}

/**
 * Class representing the state of traversal in the file system
 */
class TransitionState {
    public Node node;
    public String prefix;
    public boolean isSibling;

    /**
     * Constructor for TraversalState
     *
     * @param node Node being traversed
     * @param prefix Prefix for the node
     * @param isSibling Flag indicating if the node is the last sibling
     */
    public TransitionState(Node node, String prefix, boolean isSibling) {
        this.node = node;
        this.prefix = prefix;
        this.isSibling = isSibling;
    }
}

/**
 * Iterator interface for traversing nodes
 *
 * @param <T>
 */
interface Iterator<T> {
    boolean hasNext();
    T next();
}

/**
 * Class representing a directory in the file system
 */
class Directory extends Node {
    private final ArrayList<Node> children = new ArrayList<>();

    /**
     * @return iterator for traversing the directory tree
     */
    public Iterator<TransitionState> createIterator() {
        return new TreeDFS(this);
    }

    /**
     * Constructor for Directory
     *
     * @param id
     * @param name
     */
    public Directory(int id, String name) {
        super(name);
    }

    /**
     * Adds a child node to the directory
     *
     * @param child node to be added
     */
    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    /**
     * @return List of child nodes
     */
    public ArrayList<Node> getChildren() {
        return children;
    }

    @Override
    public double getSizeKB() {
        return 0;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Node child : children) {
            child.accept(visitor);
        }
    }
}

/**
 * Depth-First iterator for traversing the directory tree
 */
class TreeDFS implements Iterator<TransitionState> {
    private final Deque<TransitionState> nodeDeque = new LinkedList<>();

    /**
     * Constructor for TreeDFS
     *
     * @param startingDirectory for traversal
     */
    public TreeDFS(Directory startingDirectory) {
        if (startingDirectory != null) {
            ArrayList<Node> initialChildren = startingDirectory.getChildren();
            for (int i = initialChildren.size() - 1; i >= 0; i--) {
                Node childNode = initialChildren.get(i);
                boolean isLastChild = (i == initialChildren.size() - 1);
                nodeDeque.push(new TransitionState(childNode, "", isLastChild));
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !nodeDeque.isEmpty();
    }

    @Override
    public TransitionState next() {
        TransitionState transitionState = nodeDeque.pop();
        Node currentNode = transitionState.node;

        if (currentNode instanceof Directory) {
            Directory currentDirectory = (Directory) currentNode;
            ArrayList<Node> children = currentDirectory.getChildren();
            String prefixForChildren = transitionState.prefix + (transitionState.isSibling ? "    " : "│   ");

            for (int i = children.size() - 1; i >= 0; i--) {
                Node childNode = children.get(i);
                boolean isLastChild = (i == children.size() - 1);
                nodeDeque.push(new TransitionState(childNode, prefixForChildren, isLastChild));
            }
        }

        return transitionState;
    }
}

/**
 * Visitor implementation for calculating the total size of files
 */
class SizeVisitor implements Visitor {
    private double size = 0;

    @Override
    public void visit(File file) {
        size += file.getSizeKB();
    }

    @Override
    public void visit(Directory directory) {}

    /**
     * @return total size in KB
     */
    public double getSize() {
        return size;
    }
}

/**
 * Class for printing the directory tree
 */
class Tree {
    /**
     * @param root directory
     */
    public static void printTree(Directory root) {
        System.out.println(".");
        Iterator<TransitionState> iterator = root.createIterator();

        while (iterator.hasNext()) {
            TransitionState state = iterator.next();
            Node node = state.node;
            String prefix = state.prefix;
            boolean isLast = state.isSibling;

            System.out.print(prefix);
            System.out.print(isLast ? "└── " : "├── ");
            System.out.println(node.toString());
        }
    }
}