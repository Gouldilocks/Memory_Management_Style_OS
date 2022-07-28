import java.util.ArrayList;
import java.util.List;

class MemoryManager 
{
  private List<Memory> allocatedMemory;
  private int[] memory;

  /**
   * Constructor.
   * @param size the size of the memory
   */
  public MemoryManager(int size)
  {
    this.allocatedMemory = new ArrayList<Memory>();
    memory = new int[size];
    for (int i = 0; i < size; i++)
    {
      memory[i] = 0;
    }
  }

  /**
   * Requests memory from the memory manager.
   * @param size the size of the memory request
   * @param processName The name of the process requesting memory
   * @param requestStyle The style of the request (F, B, or W)
   * @return The memory object of the memory request.
   */
  public Memory requestMemory(int size, String processName, char requestStyle)
  {
    // Set variable
    int address = -1;

    // Best fit algorithm
    if (requestStyle == 'B')
    address = findMemoryBestFit(size);

    // Worst fit algorithm
    else if (requestStyle == 'W')
    address = findMemoryWorstFit(size);

    // First Fit algorithm default
    else 
    address = findMemoryFirstFit(size);

    // If there is no memory available, return null
    if (address == -1)
    {
      return null;
    }
    // Otherwise allocate that memory 
    else
    {
      Memory memory = new Memory(address, size, processName);
      allocatedMemory.add(memory);
      return memory;
    }
  }

  /**
   * Finds blocks that only have enough memory to fit the request.
   * @param openBlocks The list of open blocks.
   * @param size The size of the request.
   * @return A list of memory blocks that fit the request.
   */
  public ArrayList<Memory> findLargeEnoughBlock(ArrayList<Memory> openBlocks, int size)
  {
    ArrayList<Memory> largeEnoughBlocks = new ArrayList<Memory>();
    for (Memory block : openBlocks)
    {
      if (block.getSize() >= size)
      {
        largeEnoughBlocks.add(block);
      }
    }
    return largeEnoughBlocks;
  }

  /**
   * Finds memory in the memory manager via worst fit algorithm.
   * @param size the size of the memory request
   * @return The address of the beginning of the allocation 
     * was made. Returns -1 if no memory is available.
   */
  public int findMemoryWorstFit(int size)
  {
    // Get the openings
    ArrayList<Memory> openBlocks = getHoles();

    // Find the blocks with large enough size
    openBlocks = findLargeEnoughBlock(openBlocks, size);

    // If there are none, return -1
    if (openBlocks.size() == 0)
      return -1;

    // Sort by size
    openBlocks = sortBySize(openBlocks);

    // Find the largest block
    Memory Largest = openBlocks.get(openBlocks.size() - 1);

    // Allocate that memory
    for(int i = Largest.getAddress(); i < size; i++)
    {
      memory[i] = 1;
    }

    // Return the address of the beginning of the allocation
    return Largest.getAddress();
  }

  /**
   * Finds memory in the memory manager via best fit algorithm.
   * @param size the size of the memory request
   * @return The address of the beginning of the allocation 
     * was made. Returns -1 if no memory is available.
   */
  public int findMemoryBestFit(int size)
  {
    // Get the openings
    ArrayList<Memory> openBlocks = getHoles();

    // Find the blocks with large enough size
    openBlocks = findLargeEnoughBlock(openBlocks, size);

    // If there are none, return -1
    if (openBlocks.size() == 0)
      return -1;

    // Sort by size
    openBlocks = sortBySize(openBlocks);
    
    // Find the smallest block
    Memory Smallest = openBlocks.get(0);

    // Allocate that memory
    for(int i = Smallest.getAddress(); i < size; i++)
    {
      memory[i] = 1;
    }

    // Return the address of the beginning of the allocation
    return Smallest.getAddress();
  }

  /**
   * Finds memory in the memory manager via first fit
   * @param size the size of the memory request
   * @return the address the beginning of the allocation was made.
   * Returns -1 if there is no memory available.
   */
  public int findMemoryFirstFit(int size)
  {
    for(int i = 0; i < memory.length; i++)
    {
      // Check the first available memory
      if (memory[i] == 0)
      {
        boolean allClear = true;
        // Check if all the memory is available after
        for(Memory memory : allocatedMemory)
        {
          int beginMemory = memory.getAddress();
          int endMemory = beginMemory + memory.getSize();
          int beginRequest = i;
          int endRequest = i + size;
          // if the memory is already allocated, it is not clear
          if (beginMemory < endRequest && endMemory > beginRequest
              || beginMemory > endRequest && endMemory < beginRequest
              || beginMemory < endRequest && endMemory > endRequest)
          {
            allClear = false;
            break;
          }
          {
            continue;
          }
        }
        if (allClear)
        {
          // Allocate the memory
          for(int x = 0; x < size; x++)
          {
            memory[i + x] = 1;
          }
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Searches for the Memory object of a given process name
   * @param processName the name of the process to search for
   * @return the Memory object of the process
   */
  public Memory searchForProcess(String processName)
  {
    for(Memory memory : allocatedMemory)
    {
      if (memory.getProcess().equals(processName))
      {
        return memory;
      }
    }
    return null;
  }

  /**
   * Releases memory from the memory manager.
   * @param id the id of the memory request
   * @throws Exception
   */
  public void releaseMemory(String processName) throws Exception
  {

    // Search for the process
    Memory toRelease = searchForProcess(processName);
    if (toRelease == null)
    {
      throw new Exception("Process not found");
    }

    int id = toRelease.getAddress();
    int end = id + toRelease.getSize();
    for (int i = id; i < end; i++)
    {
      memory[i] = 0;
    }
    allocatedMemory.remove(toRelease);
  }

  /**
   * Compacts small blocks of memory from 
   * the memory manager into one large block.
   */
  public void compactMemory()
  {
    sortMemory();

    // Make a new memory space
    int[] newMemory = new int[memory.length];
    for (int i = 0; i < memory.length; i++) {
      newMemory[i] = 0;
    }

    // Iterate through allocated blocks and add those
    int currMemory = 0;
    ArrayList<Memory> newAllocatedMemory = new ArrayList<Memory>();
    for(Memory memory : allocatedMemory)
    {
      int size = memory.getSize();
      memory.setAddress(currMemory);
      for (int i = currMemory; i < size; i++)
      {
        newMemory[i] = 1;
      }
      newAllocatedMemory.add(memory);
      currMemory += size;
    }

    // Set the new memory and allocated memory
    allocatedMemory = newAllocatedMemory;
    memory = newMemory;
  }

  /**
   * Prints the amount of memory currently available.
   */
  public void reportMemory()
  {
    sortMemory();

    int previousEnd = 0;
    for(Memory memory : allocatedMemory)
    {
      int beginMemory = memory.getAddress();
      int endMemory = beginMemory + memory.getSize();
      if (beginMemory > previousEnd)
      {
        System.out.println("Memory from " + previousEnd + " to " + (beginMemory - 1) + " is available.");
      }
      System.out.println("Addresses: [" + (beginMemory) + "," + (endMemory-1) + "]" + " Process " + memory.getProcess());
      previousEnd = endMemory;
    }
      if (previousEnd < memory.length)
      {
        System.out.println("Memory from " + previousEnd + " to " + (memory.length-1) + " is available.");
      }
  }

  /**
   * Returns a list of all the holes in the memory manager.
   * @return an Arraylist of all the holes in Memory format
   * {@link Memory}
   */
  public ArrayList<Memory> getHoles()
  {
    ArrayList<Memory> holes = new ArrayList<Memory>();
    sortMemory();

    // Get the holes from each of the already allocated spaces
    int startAddress = 0;
    for ( Memory memory : allocatedMemory)
    {
      int begin = memory.getAddress();
      int end = begin + memory.getSize();
      if (startAddress < begin)
      {
        Memory hole = new Memory(startAddress, begin - startAddress, " ");
        holes.add(hole);
      }
      startAddress = end;
    }

    // Add the last hole
    if (startAddress < memory.length)
    {
      Memory hole = new Memory(startAddress, memory.length - startAddress, " ");
      holes.add(hole);
    }
    return holes;
  }

  /**
   * Sorts the memory array in ascending order.
   * Bases the sort on the beginning address of the memory.
   */
  public void sortMemory()
  {
    // Sort the memory addresses
    allocatedMemory.sort((a, b) -> {
      return a.getAddress() - b.getAddress();
    });
  }

  /**
   * Sorts the memory array in ascending order.
   * @param toSort the array to sort
   * @return the sorted array
   */
  public ArrayList<Memory> sortBySize(ArrayList<Memory> toSort)
  {
    toSort.sort((a, b) -> {
      return a.getSize() - b.getSize();
    });
    return toSort;
  }
}

