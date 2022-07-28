public class Memory {

  private int address;
  private int size;
  private String process;

  public Memory(int address, int size, String process) {
    this.address = address;
    this.size = size;
    this.process = process;
  }

  public int getAddress() {
    return address;
  }

  public int getSize() {
    return size;
  }

  public String getProcess() {
    return process;
  }

  public void setAddress(int address) {
    this.address = address;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setProcess(String process) {
    this.process = process;
  }

}
