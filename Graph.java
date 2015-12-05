import java.io.PrintWriter;

/**An interface for a graph, taken and modified from AlgoViz.
 * 
 * @author smsukardi
 */
public interface Graph { 
    
    /**Initializes the graph with n vertices.
     * @param n the no. of vertices. 
     */
    void init(int n);

    /** Returns the number of vertices.
     * @return the number of vertices
     */
    int nodeCount();

    /** Return the current number of edges. 
     * @return the number of edges
     */
    int edgeCount();

    /** Get the value of node with index v.
     * @param v the node at which to get the value at
     * @return the value of the node
     */
    String getValue(int v);

    /** Set the value of node with index v.
     * @param v the index to set
     * @param val the value to set it to
     */
    void setValue(int v, String val);
  
    /** Adds a new edge from node v to node w with weight wgt.
     * @param v the x-coord
     * @param w the y-coord
     * @param wgt the weight given to the edge
     */
    void addEdge(int v, int w, int wgt);

    /** Get the weight value for an edge. 
     * @param v the x-coord
     * @param w the y-coord
     * @return the weight
     */
    int getWeight(int v, int w);

    /** Removes the edge from the graph.
     * @param v the x-coord
     * @param w the y-coord
     */
    void removeEdge(int v, int w);

    /** Returns true iff the graph has the edge.
     * @param v the x-coord
     * @param w the y-coord
     * @return if it has an edge
     */
    boolean hasEdge(int v, int w);

    /** Returns an array containing the indicies of the neighbors of v.
     * @param v the coordinate to find neighbors of 
     * @return an integer array of neighbors
     */
    int[] neighbors(int v);

    /**Prints the graph to a file.
     * @param output the file to print to
     */
    void printGraphToFile(PrintWriter output);
}