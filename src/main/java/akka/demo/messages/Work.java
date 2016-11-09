package akka.demo;

public class Work {
  private final int start;
  private final int numElements;
 
  public Work(int start, int numElements) {
    this.start = start;
    this.numElements = numElements;
  }
 
  public int getStart() {
    return start;
  }
 
  public int getNumElements() {
    return numElements;
  }
}