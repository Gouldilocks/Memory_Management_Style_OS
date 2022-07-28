public class Driver {
  public static void main(String[] args)
  {
    // Request a size 10 memory allocation
    MemoryManager memoryManager = new MemoryManager(10);
    Memory process1 = memoryManager.requestMemory(5, "Process1", 'F');
    Memory process2 = memoryManager.requestMemory(2, "process2", 'F');
    Memory process3 = memoryManager.requestMemory(3, "process3", 'F');

    // Print the original allocation
    System.out.println();
    memoryManager.reportMemory();
    System.out.println();

    // Release the middle memory allocation
    try
    {
    memoryManager.releaseMemory(process2.getProcess());
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }

    // Print the new allocation
    System.out.println();
    memoryManager.reportMemory();
    System.out.println();

    // Compact the memory
    memoryManager.compactMemory();

    // Print the new allocation
    System.out.println();
    memoryManager.reportMemory();
    System.out.println();
  }
}
