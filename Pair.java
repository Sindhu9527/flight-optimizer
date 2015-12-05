public class Pair {
    
    /** the first airport in the pair. */
    private String airport1;
    /** the second airport in the pair. */
    private String airport2;
    /** The weight given to the path. */
    private int weight;
    
    /**A pair constructor.
     * @param port1 Start airport
     * @param port2 Destination airport
     * @param weight1 Amt. of miles of the flight.
     */
    public Pair(String port1, String port2, int weight1) {
        this.airport1 = port1;
        this.airport2 = port2;
        this.weight = weight1;
    }    

    /** Gets start airport. 
     * @return the start airport
     */
    public String getStart() {
        return this.airport1;
    }
    
    /** Gets destination airport. 
     * @return the destination airport
     */
    public String getDest() {
        return this.airport2;
    }
    
    /** Gets flight length. 
     * @return the flight length (edge weight).
     */
    public int getWeight() {
        return this.weight;
    }
    
    
}