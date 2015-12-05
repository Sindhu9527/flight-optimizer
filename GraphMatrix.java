import java.io.PrintWriter;

/**Graph adjacency matrix implementation.
 * 
 * @author smsukardi
 */
public class GraphMatrix implements Graph {
    
    /** The adjacency matrix. */
    private int[][] matrix;
    /** The values of the nodes. */
    private String[] airportNames;
    /** The number of edges in the graph. */
    private int numEdges;

    /**Empty constructor.
     */
    public GraphMatrix() {       
    }

    /**Initializes the graph with n vertices.
     * @param n the no. of vertices. 
     */
    public void init(int n) {
        this.matrix = new int[n][n];
        this.airportNames = new String[n];
        this.numEdges = 0;
    }

    /** Returns the number of vertices.
     * @return the number of vertices
     */
    public int nodeCount() { 
        return this.airportNames.length; 
    }

    /** Return the current number of edges. 
     * @return the number of edges
     */
    public int edgeCount() { 
        return this.numEdges; 
    }
  
    /** Get the value of node with index v.
     * @param v the node at which to get the value at
     * @return the value of the node
     */
    public String getValue(int v) {
        return this.airportNames[v]; 
    }
  
    /** Set the value of node with index v.
     * @param v the index to set
     * @param val the value to set it to
     */
    public void setValue(int v, String val) { 
        this.airportNames[v] = val; 
    }

    /** Adds a new edge from node v to node w with weight wgt.
     * @param v the x-coord
     * @param w the y-coord
     * @param weight the weight given to the edge
     */
    public void addEdge(int v, int w, int weight) {
        if (weight == 0) {
            return; // Can't store weight of 0
        }
        this.matrix[v][w] = weight;
        this.numEdges++;
    }

    /** Get the weight value for an edge. 
     * @param v the x-coord
     * @param w the y-coord
     * @return the weight
     */
    public int getWeight(int v, int w) { 
        return this.matrix[v][w]; 
    }

    /** Removes the edge from the graph.
     * @param v the x-coord
     * @param w the y-coord
     */
    public void removeEdge(int v, int w) {
        this.matrix[v][w] = 0;
        this.numEdges--;
    }
  
    /** Returns true iff the graph has the edge.
     * @param v the x-coord
     * @param w the y-coord
     * @return if it has an edge
     */
    public boolean hasEdge(int v, int w) { 
        return this.matrix[v][w] != 0; 
    }
    
    /**Increments the edge by 1.
     * @param v x-coord
     * @param w y-coord
     */
    public void incrementEdge(int v, int w) {
        if (!this.hasEdge(v, w)) {
            this.numEdges++;
        }
        this.matrix[v][w]++;
    }

    /** Returns an array containing the indicies of the neighbors of v.
     * @param v the coordinate to find neighbors of 
     * @return an integer array of neighbors
     */
    public int[] neighbors(int v) {
        int i;
        int count = 0;
        int[] temp;

        for (i = 0; i < this.airportNames.length; i++) {
            if (this.matrix[v][i] != 0) {
                count++;
            }
        }
        temp = new int[count];
        for (i = 0, count = 0; i < this.airportNames.length; i++) {
            if (this.matrix[v][i] != 0) {
                temp[count++] = i;
            }
        }
        return temp;
    }
    
    /**A toString method to visualize the adjacency matrix representation
     * of the graph.
     * @return a string representation of the graph.
     */
    public String toString() {
        String toReturn = "";
        toReturn += "\t";
        for (int i = 0; i < this.airportNames.length; i++) {
            toReturn += this.airportNames[i] + "\t";
        }
        toReturn += "\n";
        for (int i = 0; i < this.matrix.length; i++) {
            toReturn += this.airportNames[i] + "\t";
            for (int j = 0; j < this.matrix[0].length; j++) {
                toReturn += this.matrix[i][j] + "\t";
            }
            toReturn += "\n";
        }
        return toReturn;
    }
    
    /**Prints the graph to a file.
     * @param outputFile the file to print to
     */
    public void printGraphToFile(PrintWriter outputFile) {
        //4 spaces for each row and column; right justified
        outputFile.print("    ");
        for (int i = 0; i < this.airportNames.length; i++) {
            outputFile.printf("%-4s", this.airportNames[i]);
        }
        outputFile.println();
        for (int i = 0; i < this.matrix.length; i++) {
            outputFile.printf("%-4s", this.airportNames[i]);
            for (int j = 0; j < this.matrix.length; j++) {
                outputFile.printf("%-4d", this.matrix[i][j]);
            }
            if (i != this.matrix.length - 1) {
                outputFile.println();
            }
        }
        
    }
}