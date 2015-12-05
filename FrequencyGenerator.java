import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**Determines how many flights per day an airline will 
 * schedule for a particular route.
 * 
 * @author smsukardi
 */
public class FrequencyGenerator {
    
    /**Main method.
     * 
     * @param args arguments (the .txt file containing information).
     * @throws FileNotFoundException a filenotfound exception
     */
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "";
        
        if (args.length != 1) {
            System.out.println("Please enter a .txt file containing "
                    + "flight information.");
        } else {
            //stores the arguments in the variables
            fileName = args[0];
        }
        
        HashMap<String, Integer> myMap = new HashMap<String, Integer>();
        int amtOfAirports = 0;
        boolean emptyLine = false;
        
        Scanner firstRead = new Scanner(new File(fileName));
        
        //reads the first time to get the no. and name of airports
        //counts the number of airports and places them into the hashMap
        while (firstRead.hasNextLine() && !emptyLine) {
            String next = firstRead.nextLine();            
            String[] split = next.split("->");
            
            if (next.length() == 0) {
                emptyLine = true;
            } else {
                myMap.put(split[split.length - 1], amtOfAirports);
                amtOfAirports++;
            }       
        } 
        firstRead.close(); //closes the scanner
        
        //Constructs a new GraphMatrix
        GraphMatrix yourGraph = new GraphMatrix();
        yourGraph.init(amtOfAirports);
        //inserts the airports in the 2-D adjacency matrix
        for (String key : myMap.keySet()) {
            yourGraph.setValue(myMap.get(key), key);
        }
        
        //Reads again, populating with flights
        Scanner secondRead = new Scanner(new File(fileName));
        while (secondRead.hasNextLine()) {
            String next = secondRead.nextLine();            
            String[] split = next.split("->");            
            
            if (next.length() != 0) {
                for (int i = 0; i < split.length - 1; i++) {                  
                    yourGraph.incrementEdge(myMap.get(split[i]), 
                            myMap.get(split[i + 1]));
                }
            }
        }
        secondRead.close();
        
        //Prints to a .txt file
        FrequencyGenerator myGenerator = new FrequencyGenerator();
        PrintWriter output = new PrintWriter("frequencies.txt");
        myGenerator.printGraphToFile(yourGraph, output);
        output.close();        
    }

    /**Prints the graph to a file.
     * 
     * @param yourGraph the adjacency matrix.
     * @param output the file to be printed to.
     */
    private void printGraphToFile(Graph yourGraph, PrintWriter output) {
        yourGraph.printGraphToFile(output);
    }
}
