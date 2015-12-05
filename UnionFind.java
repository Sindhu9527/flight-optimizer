/**This class implements the unionfind algorithm.
 * Skeleton code from OpenDSA "Union/Find and Parent Pointer Implementation" 
 * Modified so there is no separate weights array 
 * (weights given in array contents via negative numbers)
 * modified to include a find method involving two explicit parameters
 * @author smsukardi
 *
 */
public class UnionFind {
    
    /**The array which will be formed into a tree.
     */
    private int[] array;

    /**Constructor.
     * @param size the size of the array to perform unionfind on.
     */
    public UnionFind(int size) {
        this.array = new int[size]; // Create node array
        for (int i = 0; i < size; i++) {
            this.array[i] = -1;
        }
    }

    /**Merges two subtrees if they are different.
     * 
     * @param a the first node to be united.
     * @param b the second node to be united.
     * @return whether they were united or not
     */
    public boolean union(int a, int b) {
        int root1 = this.find(a);     // Find index of root of node a
        int root2 = this.find(b);     // Find index of root of node b
        if (root1 != root2) {
            //performs weighted union
            if (this.array[root2] < this.array[root1]) {
                this.array[root2] += this.array[root1];
                this.array[root1] = root2;
            } else {
                this.array[root1] += this.array[root2];
                this.array[root2] = root1;
            }
            return true;
        }
        return false;
    }

    /**This method returns the root of curr's tree.
     * 
     * @param curr the index of the node to find the root of
     * @return the root of the tree
     */
    public int find(int curr) {
        //Finds the root of the tree
        if (this.array[curr] < 0) {
            return curr; 
        } else {
            //recursively performs path compression
            this.array[curr] = this.find(this.array[curr]);
            return (this.array[curr]);
        }
    }

    /**Determines whether two indices are part of the same tree.
     *
     *@param index1 the first thing to be found
     *@param index2 the second thing to be found
     *@return true if they are in the same tree, false if not
     */
    public boolean find(int index1, int index2) {
        if (this.find(index1) != this.find(index2)) {
            return false;
        }
        return true;
    } 
}
