import java.util.Scanner;

public class Allocator {

  private static Scanner scan = new Scanner(System.in);
  private static MemoryManager memoryManager;

  public static String[] getPromptFromUser()
  {
    System.out.print("allocator> ");
    String line = scan.nextLine();
    String[] tokens = line.split(" ");
    return tokens;
  }

  public static void executeCommands(String[] commands)
  {
    if (commands.length > 0)
    {
      // If the user wants to Compact the memory
      if (commands[0].equals("C"))
      {
        memoryManager.compactMemory();
        System.out.println("Memory compacted\n");
      }

      // If the user wants to display the memory
      else if (commands[0].equals("STAT"))
      {
        memoryManager.reportMemory();
      }

      // If the user wants to release memory
      else if (commands[0].equals("RL"))
      {
        String processName = commands[1];
        try
        {
          memoryManager.releaseMemory(processName);
          System.out.println("Memory released\n");
        }
        catch (Exception e)
        {
          System.out.println("Could not release that memory. Not found");
        }
      }

      // If the user wants to request memory
      else if (commands[0].equals("RQ"))
      {
        String processName = commands[1];
        int requestSize = Integer.parseInt(commands[2]);

        // Can be: F, B, or W
        char requestType = commands[3].charAt(0);

        // Request the memory
        Memory memory = memoryManager.requestMemory(requestSize, processName, requestType);

        if (memory != null)
        {
          System.out.println("Memory allocated: " + memory.getAddress() + " " + memory.getSize() + " " + memory.getProcess());
        }
        else
        {
          System.out.println("Could not allocate that memory");
        }
      }

      // If the user wants to quit the program
      else if (commands[0].equals("X"))
      {
        System.out.println("Goodbye!");
        System.exit(0);
      }

      else
      {
        System.out.println("Invalid command");
      }
    }
  }

  public static void main(String[] args)
  {
    // int allocationSize = Integer.parseInt(args[0]);
    int allocationSize = 10;
    memoryManager = new MemoryManager(allocationSize);

    //Run the program
    while(true)
    {
      String[] commands = getPromptFromUser();
      executeCommands(commands);
    }
  }
}
