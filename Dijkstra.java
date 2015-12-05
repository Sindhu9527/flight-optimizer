import java.util.ArrayList;

/**A class to run Dijkstra's algorithm.
 */
public class Dijkstra {

    /**Constant for infinity; 
     * The circumference of the earth is 24901 miles so this
     * should be ample buffer. */
    private static final int INFINITY = 999999999;
    
    /** Stores the amount of airports. */
    int amtOfAirports;

    /**Constructor.
     * @param airports the amount of airports
     */
    public Dijkstra(int airports) {
        this.amtOfAirports = airports;
    }

    /**Implementation of Dijkstra's algorithm.
     * @param yourGraph your graph
     * @param sourceNode your source node
     * @param distances an array of distances
     * @return An array of ArrayList of airports, with each ArrayList
     * containing the airports passed to go from the source airport to the 
     * destination airport.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String>[] dijkstra(Graph yourGraph, 
            int sourceNode, int[] distances) {
        //Stores whether the minimum path has been found yet
        ArrayList<String>[] paths = new ArrayList[this.amtOfAirports];
        boolean[] pathFound = new boolean[this.amtOfAirports];
        
        //initializes each arraylist in the array of arraylists
        for (int i = 0; i < paths.length; i++) {
            paths[i] = new ArrayList<String>();
        }
       
        //Sets distances to "infinity" (a very large number) and paths to false
        for (int i = 0; i < this.amtOfAirports; i++) {
            pathFound[i] = false;
            distances[i] = INFINITY;
        }
        //The distance from the source to itself is 0
        distances[sourceNode] = 0;

        //Finds the shortest path by applying Dijkstra's algorithm
        for (int count = 0; count < this.amtOfAirports - 1; count++) {
          //Finds the next-closest vertex
            int u = this.minVertex(distances, pathFound);
            pathFound[u] = true;            
            //Adds the path length and the path information
            for (int v = 0; v < this.amtOfAirports; v++) {
                this.addPaths(pathFound, yourGraph, distances, paths, u, v);
            }
        }
        //adds the starting point onto all paths
        for (int i = 0; i < paths.length; i++) {
            paths[i].add(0, yourGraph.getValue(sourceNode));
        }        
        return paths;
    }

    /**Updates distance if the path has not been found already,
     * an edge exists from u to v, and the total weight of the 
     * path from the source node to v through u is smaller 
     * than the current value of distances[v].
     * 
     * @param pathFound whether the min path has been found for a node
     * @param yourGraph the adjacency matrix
     * @param distances the distance from one node to another
     * @param paths an array of arraylists containing airports stopped at
     * @param i x-coord
     * @param j y-coord
     */
    private void addPaths(boolean[] pathFound, Graph yourGraph, int[] distances,
            ArrayList<String>[] paths, int i, int j) {
        //Checks if updating distance criteria is passed
        if (!pathFound[j] && yourGraph.getWeight(i, j) != 0 
                && distances[i] != INFINITY && distances[i] 
                + yourGraph.getWeight(i, j) < distances[j]) {
            //updates distances
            distances[j] = distances[i] + yourGraph.getWeight(i, j);
            //updates the path to be returned
            paths[j].clear();       
            for (int n = 0; n < paths[i].size(); n++) {
                paths[j].add(paths[i].get(n));
            }
            paths[j].add(yourGraph.getValue(j)); //adds the endpoint to the path
        }        
    }

    /**Modified Dijkstra's algorithm, for when optimal flight lengths 
     * are greater than the threshold.
     * @param newGraph The graph containing unweighted nodes.
     * @param yourGraph The original graph.
     * @param sourceNode Your source node. 
     * @param distances An array of path lengths.
     * @return An ArrayList containing airports
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String>[] modifiedDijkstra(GraphMatrix newGraph, 
            GraphMatrix yourGraph, int sourceNode, int[] distances) {
        //Stores whether the minimum path has been found
        boolean[] pathFound = new boolean[this.amtOfAirports];
        int[] ghostDistances = new int[this.amtOfAirports];
        ArrayList<String>[] paths = new ArrayList[this.amtOfAirports];
        
        //initializes each arraylist in the array of arraylists
        for (int i = 0; i < paths.length; i++) {
            paths[i] = new ArrayList<String>();
        }
        
        //Sets all distances to "infinity" and all paths to false
        for (int i = 0; i < this.amtOfAirports; i++) {
            pathFound[i] = false;
            distances[i] = INFINITY;
            ghostDistances[i] = INFINITY;
        }
        
        //Distance from the source to itself is 0
        distances[sourceNode] = 0;
        ghostDistances[sourceNode] = 0;

        //Finds the shortest path by applying Dijkstra's algorithm
        for (int count = 0; count < this.amtOfAirports - 1; count++) {
            //Finds the next-closest vertex
            int u = this.minVertex(distances, pathFound);
            pathFound[u] = true;
            
            //Adds the path length and the path information
            for (int v = 0; v < this.amtOfAirports; v++) {
                if (!pathFound[v] && newGraph.getWeight(u, v) != 0
                        && distances[u] != INFINITY && distances[u] 
                        + newGraph.getWeight(u, v) <= distances[v]) {
                    this.modAddPaths(yourGraph, newGraph, distances,
                            ghostDistances, paths, u, v);
                }
            }
        }        
        //adds the starting point onto all paths
        for (int i = 0; i < paths.length; i++) {
            paths[i].add(0, newGraph.getValue(sourceNode));
        }                
        return paths;
    }
    
    /**A modified add paths for the modified dijkstra's algorithm.
     * If two paths have the same length, chooses the one with the 
     * lower mileage as the "tiebreaker."
     * @param yourGraph the original adjacency matrix.
     * @param newGraph the modified adjacency matrix with edge weights 1.
     * @param distances the new distances.
     * @param ghostDistances the original distances, for mileage comparison.
     * @param paths an array of arraylists containing airports stopped at
     * @param i x-coord
     * @param j y-coord
     */
    private void modAddPaths(GraphMatrix yourGraph, GraphMatrix newGraph,
            int[] distances, int[] ghostDistances, ArrayList<String>[] paths,
            int i, int j) {
        if (distances[i] + newGraph.getWeight(i, j) == distances[j]) {
            //apply tiebreaker
            if (ghostDistances[i] + yourGraph.getWeight(i, j) 
                <= ghostDistances[j]) {
                //updates distances
                distances[j] = distances[i] + newGraph.getWeight(i, j);
                //updates the path to be returned
                paths[j].clear();                    
                for (int n = 0; n < paths[i].size(); n++) {
                    paths[j].add(paths[i].get(n));
                }
                paths[j].add(yourGraph.getValue(j)); //adds endpoint
            }            
        } else {                    
            //updates distances
            distances[j] = distances[i] + newGraph.getWeight(i, j);
            //updates the path to be returned
            paths[j].clear();                    
            for (int n = 0; n < paths[i].size(); n++) {
                paths[j].add(paths[i].get(n));
            }
            paths[j].add(yourGraph.getValue(j)); //adds the endpoint
        }        
    }

    /**Calculates an index where a path exists.
     * @param distances an array of distances
     * @param pathFound the path found
     * @return The index with the minimum distance, or -1 if not found.
     */
    public int minVertex(int[] distances, boolean[] pathFound) {
        //sets the smallest number as infinity
        int min = INFINITY;
        int smallestIndex = -1;
        //searches for the next smallest index
        for (int i = 0; i < this.amtOfAirports; i++) {
            if (!pathFound[i] && distances[i] <= min) {
                min = distances[i];
                smallestIndex = i;
            }
        }    
        return smallestIndex;
    }   
}

