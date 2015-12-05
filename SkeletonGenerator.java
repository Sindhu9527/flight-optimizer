import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**Creates a skeleton subset of flight segments that
 * have the highest frequencies, providing only one route
 * between each airport pair.
 * 
 * @author smsukardi
 */
public final class SkeletonGenerator {
    
    /**Empty constructor.
     */
    private SkeletonGenerator() {
    }

    /**Main method which generates two files: one with the most connected 
     * flights with no cycles, and one with flight frequencies.
     * 
     * @param args arguments
     * @throws FileNotFoundException the exception
     */
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "";

        //Ensures correct argument length
        if (args.length != 1) {
            System.out.println("Please enter a .txt file containing "
                    + "flight information.");
            return;
        }

        //stores the arguments in the variables
        fileName = args[0];

        //initializes the first scanner 
        Scanner firstRead = new Scanner(new File(fileName));
                
        //Retrieves the first line containing airport information
        String[] airports = firstRead.nextLine().trim().split("\\s+");
        int amtOfAirports = airports.length;

        //constructs and initializes the graph
        GraphMatrix yourGraph = new GraphMatrix();
        yourGraph.init(amtOfAirports);
        
        //constructs and initializes a graph which will illustrate connectivity
        //for outputCtable.txt
        GraphMatrix skeleton = new GraphMatrix();
        skeleton.init(amtOfAirports);
        
        //constructs an array list of paths
        ArrayList<Pair> pathPairs = new ArrayList<Pair>();
        //stores in a hashmap all airport-pairs
        HashMap<String, Integer> airportIndex = new HashMap<String, Integer>();
        
        //initializes the airport names in the two graphs
        for (int i = 0; i < amtOfAirports; i++) {
            yourGraph.setValue(i, airports[i]);
            skeleton.setValue(i, airports[i]);
            airportIndex.put(airports[i], i);
        }
        
        //reads the file and places the information into the two graphs
        readFile(firstRead, yourGraph, pathPairs);
        firstRead.close(); //closes the scanner

        //sorts all the paths by largest-first
        sort(pathPairs);

        //Constructs a unionfind to generate the skeleton
        UnionFind rootsaver = new UnionFind(amtOfAirports);

        //Goes through all pairs, which are sorted largest-first
        while (!pathPairs.isEmpty()) {
            //takes the top pair off
            Pair max = pathPairs.remove(0);            
            //Retrieves the start and end coordinates
            int connectedX = airportIndex.get(max.getStart());
            int connectedY = airportIndex.get(max.getDest());
            //If a union can be performed, adds the edge to the adjacency matrix
            if (rootsaver.union(connectedX, connectedY)) {
                skeleton.addEdge(connectedX, connectedY, 1);
                skeleton.addEdge(connectedY, connectedX, 1);
            }
        }
        //Removes paths in the old graph for those not in the skeleton
        for (int i = 0; i < amtOfAirports; i++) {
            for (int j = 0; j < amtOfAirports; j++) {
                if (skeleton.getWeight(i, j) == 0) {
                    yourGraph.removeEdge(i, j);
                }
            }
        }
        //Passes these two graphs into a method which prints the 
        //skeleton and flight freq information to a file.
        printToFile(skeleton, yourGraph);       
    }
    
    /**Reads in the file to a GraphMatrix and an array of flights.
     * 
     * @param firstRead the scanner
     * @param yourGraph the unpopulated GraphMatrix
     * @param pathPairs an array of flights
     */
    private static void readFile(Scanner firstRead, GraphMatrix yourGraph,
            ArrayList<Pair> pathPairs) {
        int counter = 0;
        boolean emptyLine = false;
        //reads the file once to get the name of airports, as well as 
        //specific flight information and their frequency
        while (firstRead.hasNextLine() && !emptyLine) {
            String next = firstRead.nextLine();            
            String[] split = next.split("\\s+");
            
            if (next.length() == 0) {
                emptyLine = true;
            } else {                
                for (int i = 1; i < split.length; i++) {
                    int x = counter / yourGraph.nodeCount();
                    int y = counter % yourGraph.nodeCount();
                    yourGraph.addEdge(x, y, Integer.parseInt(split[i]));
                    counter++;
                    
                    if (Integer.parseInt(split[i]) > 0) {
                        pathPairs.add(new Pair(yourGraph.getValue(x), 
                                yourGraph.getValue(y), 
                                Integer.parseInt(split[i])));
                    }
                }
            }       
        }         
    }

    /**Prints the information associated with the graphs to two files.
     * 
     * @param skeleton the skeleton graph
     * @param yourGraph the graph
     * @throws FileNotFoundException the exception
     */
    private static void printToFile(GraphMatrix skeleton,
            GraphMatrix yourGraph) throws FileNotFoundException {
        
        //prints the skeleton to a file
        PrintWriter output1 = new PrintWriter("skeleton.txt");
        skeleton.printGraphToFile(output1);
        output1.close();
        
        //prints the airport frequencies to a file
        PrintWriter output2 = new PrintWriter("airportFrequencies.txt");
        //Constructs and array of pairs to store frequencies & airport names
        ArrayList<Pair> airportPairs = new ArrayList<Pair>();       
        //Stores the frequencies in an integer array
        int[] frequency = new int[yourGraph.nodeCount()]; 
        
        //Computes the frequencies
        for (int n = 0; n < frequency.length; n++) {
            for (int i = 0; i < frequency.length; i++) {
                frequency[n] += yourGraph.getWeight(n, i) 
                        + yourGraph.getWeight(i, n);
            }
        }

        //Stores the airport-frequency pairs into an array of pairs
        for (int i = 0; i < frequency.length; i++) {
            airportPairs.add(i, new Pair(yourGraph.getValue(i), 
                    null, frequency[i]));
        }
        
        //sorts the array
        sort(airportPairs);
        
        //prints to a file
        for (int i = 0; i < frequency.length; i++) {
            output2.print(airportPairs.get(i).getStart()
                    + " - " + airportPairs.get(i).getWeight());
            
            if (i < frequency.length - 1) {
                output2.println();
            }
        }
        output2.close();        
    }
    
    /**We need a STABLE sort to sort these values.
     * This is a quick, simple, and dirty bubble sort, with highest first,
     * modified from AlgoViz's bubble sort.
     * @param arr pair array to be sorted.
     */
    public static void sort(ArrayList<Pair> arr) {
        for (int i = 0; i < arr.size() - 1; i++) {
            for (int j = 1; j < arr.size() - i; j++) {
                //if the previous value is less than the adjacent one, swaps
                if (arr.get(j - 1).getWeight() < arr.get(j).getWeight()) {
                    //swaps
                    Pair temp = arr.get(j);
                    arr.set(j, arr.get(j - 1));
                    arr.set(j - 1, temp);
                }
            }
        }
    }    
}

