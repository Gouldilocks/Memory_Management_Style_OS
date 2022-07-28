import java.util.ArrayList;

/**
 * Sorts the memory array in ascending order.
 * 
 * @param toSort the array to sort
 * @return the sorted array
 */
public class HelperFunctions {
  public static ArrayList<Memory> sortBySize(ArrayList<Memory> toSort)
  {
    toSort.sort((a, b) -> {
      return a.getSize() - b.getSize();
    });
    return toSort;
  }

  /**
   * Finds blocks that only have enough memory to fit the request.
   * 
   * @param openBlocks The list of open blocks.
   * @param size       The size of the request.
   * @return A list of memory blocks that fit the request.
   */
  public static ArrayList<Memory> findLargeEnoughBlock(ArrayList<Memory> openBlocks, int size) {
    ArrayList<Memory> largeEnoughBlocks = new ArrayList<Memory>();
    for (Memory block : openBlocks) {
      if (block.getSize() >= size) {
        largeEnoughBlocks.add(block);
      }
    }
    return largeEnoughBlocks;
  }
}
