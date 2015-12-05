import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**A customized flight selection algorithm to generate optimal routes 
 * between pairs of airports that an airline services. 
 * 
 * Finds the shortest routes between airports according to mileage, with 
 * the restriction that each route has no more than a specified no. of 
 * segments whenever possible. If this segment threshold cannot be achieved, 
 * the route is minimized by no. of segments, with mileage as tiebreaker.
 * 
 *  For the remaining pairs, you will need to find the shortest paths 
 *  based on number of flight segments, only using the mileage to break ties.
 * 
 * @author smsukardi
 */
public class RouteGenerator {
    
    /**Main method.
     * @param args arguments
     * @throws FileNotFoundException if file not found
     * @throws IOException for bad filename
     */
    public static void main(String[] args) 
            throws FileNotFoundException, IOException {
        //stores the file name to read from and the threshold inputed
        String fileName = "";
        int threshold = 0;

        //gives a message if the incorrect command-line input was inputed
        if (args.length <= 1) {
            System.out.println("Please enter a .txt file containing "
                    + "flight information followed by the max. no. of "
                    + "segments as command-line input.");
        } else {
            //stores the arguments in the variables
            fileName = args[0];
            threshold = Integer.parseInt(args[1]);
        }

        //Parses input for the first time
        //maps strings to integer indices
        HashMap<String, Integer> myMap = new HashMap<String, Integer>();
        int amtOfAirports = 0;
        
        RouteGenerator myRoute = new RouteGenerator();
        amtOfAirports = myRoute.process(fileName, myMap, amtOfAirports);

        //Constructs a new GraphMatrix
        GraphMatrix yourGraph = new GraphMatrix();
        yourGraph.init(amtOfAirports);
        //inserts the airports in the 2-D adjacency matrix
        for (String key : myMap.keySet()) {
            yourGraph.setValue(myMap.get(key), key);
        }

        //Parses input for the second time
        //Populates the adjacency matrix 
        myRoute.populateMatrix(fileName, myMap, yourGraph);

        //updates the threshold to the max possible threshold if the threshold
        //given is greater than the max possible or is 0
        if (threshold <= 0 || threshold > amtOfAirports) {
            threshold = amtOfAirports;
        } 
        
        //Applies the algorithms to the Adjacency Matrix.
        myRoute.applyDijkstra(amtOfAirports, yourGraph, threshold, myMap);
    }


    /**Processes the input for the first time.
     * 
     * @param fileName the filename
     * @param myMap the hashmap
     * @param amtOfAirports the amt of airports (set to 0)
     * @return the modified amount of airports
     * @throws FileNotFoundException the filenotfound exception
     */
    public int process(String fileName, HashMap<String, Integer> myMap, 
            int amtOfAirports) throws FileNotFoundException {
        boolean notAWord = false;
        //creates scanner
        Scanner firstRead = new Scanner(new File(fileName));

        //counts the number of airports and places them into the hashMap
        while (firstRead.hasNextLine() && !notAWord) {
            String next = firstRead.nextLine();
            String[] split = next.split("\\s+");

            if (split[0].length() != 2 + 1 || split[1].length() != 2 + 1) {
                notAWord = true;
            } else {
                if (myMap.get(split[0]) == null) {
                    myMap.put(split[0], amtOfAirports);
                    amtOfAirports++;
                }
                if (myMap.get(split[1]) == null) {
                    myMap.put(split[1], amtOfAirports);
                    amtOfAirports++;
                }
            }           
        } 
        firstRead.close(); //closes the scanner
        return amtOfAirports;
    }

    /**Processes the input for the second time to populate the matrix.
     * 
     * @param fileName the the file name of the .txt file to open
     * @param myMap the hashmap
     * @param yourGraph the graph to populate
     * @throws FileNotFoundException an exception
     */
    private void populateMatrix(String fileName, 
            HashMap<String, Integer> myMap, GraphMatrix yourGraph) 
                    throws FileNotFoundException {
        boolean notAWord2 = false;
        Scanner secondRead = new Scanner(new File(fileName));

        while (secondRead.hasNextLine() && !notAWord2) {
            String next = secondRead.nextLine();
            String[] split = next.split("\\s+");

            if (split[0].length() != 2 + 1 || split[1].length() != 2 + 1) {
                notAWord2 = true;
            } else {
                //adds the weights to the adjacency matrix
                yourGraph.addEdge(myMap.get(split[0]).intValue(), 
                        myMap.get(split[1]).intValue(), 
                        Integer.parseInt(split[2]));
                yourGraph.addEdge(myMap.get(split[1]).intValue(), 
                        myMap.get(split[0]).intValue(), 
                        Integer.parseInt(split[2]));
            }
        }
        secondRead.close(); //closes scanner
    }

    /**Applies Dijkstra's algorithm to the graph.
     * 
     * @param amtOfAirports the amount of airports
     * @param yourGraph the graph with the adjacency matrix
     * @param threshold the threshold
     * @param myMap the hashmap
     * @throws FileNotFoundException a filenotfoundexception
     */
    private void applyDijkstra(int amtOfAirports, GraphMatrix yourGraph,
            int threshold, HashMap<String, Integer> myMap) 
                    throws FileNotFoundException {
        
        //Creates a printwriter object to print output to a .txt file
        PrintWriter output = new PrintWriter("routes.txt");

        for (int node = 0; node < amtOfAirports; node++) {
            //Applies Dijkstra's algorithm
            Dijkstra applyAlgorithm = new Dijkstra(amtOfAirports);
            ArrayList<String>[] paths = applyAlgorithm.dijkstra(
                    yourGraph, node, new int[amtOfAirports]);       

            //Decides whether necessary to apply modified Djikstra's algorithm
            boolean applyModified = false;            
            for (int i = 0; i < paths.length; i++) {
                if (paths[i].size() > (threshold + 1)) {
                    applyModified = true;
                }
            }
            //Applies modified djikstra's algorithm to routes beyond threshold
            if (applyModified) {
                RouteGenerator myRoute = new RouteGenerator();
                myRoute.applyModifiedDijkstra(amtOfAirports,
                        yourGraph, myMap, node, paths, threshold);
            }
            //Prints output to a file
            for (int i = 0; i < amtOfAirports; i++) {
                for (int j = 0; j < paths[i].size() - 1; j++) {
                    output.print(paths[i].get(j) + "->");
                }                
                output.print(paths[i].get(paths[i].size() - 1));   
                //prints spaces as long as not the very last line
                if (node != amtOfAirports - 1 || i != amtOfAirports - 1) {
                    output.println();
                }
            }
            //prints spaces between nodes as long as not the very last line
            if (node != amtOfAirports - 1) {
                output.println();
            }
        }
        output.close();        
    }


    /**Applies the modified Dijkstra's algorithm to the graph.
     * 
     * @param amtOfAirports the amount of airports
     * @param yourGraph the graph
     * @param myMap the hashmap
     * @param node the node being traversed
     * @param paths an array of ArrayLists containing paths
     * @param threshold the threshold no. of paths
     */
    private void applyModifiedDijkstra(int amtOfAirports, GraphMatrix yourGraph,
            HashMap<String, Integer> myMap, int node, ArrayList<String>[] paths,
            int threshold) {
        GraphMatrix newGraph = new GraphMatrix(); 
        newGraph.init(amtOfAirports);
        for (String key : myMap.keySet()) {
            newGraph.setValue(myMap.get(key), key);
        }

        //Sets all edge weights to 1 so it is effectively 
        //an unweighted graph
        for (int i = 0; i < amtOfAirports; i++) {
            for (int j = 0; j < amtOfAirports; j++) {
                if (yourGraph.getWeight(i, j) > 0) {
                    newGraph.addEdge(i, j, 1);
                }
            }
        }
        
        Dijkstra applyAlgorithm = new Dijkstra(amtOfAirports);
        
        //Retrieves modified results
        ArrayList<String>[] modPaths = applyAlgorithm.modifiedDijkstra(
                newGraph, yourGraph, node, new int[amtOfAirports]);
        
        //For paths beyond the threshold, sets results to the 
        //modified results
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].size() > (threshold + 1)) {
                paths[i] = modPaths[i];
            }
        }         
    }
}


