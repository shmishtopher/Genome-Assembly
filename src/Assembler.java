import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import debruijn.DeBruijnGraph;
import genomics.KMer;


/**
 * The assembler driver class reads in the refrence genome and tests the
 * algorithm by attemting to assembe random segments of the refrence genome.
 * The HIV-1 refrence genome is used here for testing.
 */
public class Assembler {
  public static void main(String[] args) throws IOException {

    // Read refrence genome into string buffer
    String genome = new String(Files.readAllBytes(Paths.get("refrence.genome"))).replace("\n", "").replace("\r", "");
    Random rand = new Random(73);
    List<Pair<Integer, Integer>> points = new ArrayList<Pair<Integer, Integer>>();
    
    // Run 30 trials for each genome length 10 to 4000
    for (int genomeSize = 10; genomeSize < 400; genomeSize += 1) {
      for (int trial = 0; trial < 20; trial += 1) {
        int offset = rand.nextInt(5000);
        String randomGenome = genome.substring(offset, offset + genomeSize);
        
        for (int readLength = 2; readLength < genomeSize - 1; readLength += 1) {
          List<String> reads = window(readLength, randomGenome);
          DeBruijnGraph<KMer> graph = new DeBruijnGraph<KMer>(readLength);

          for (String read : reads) {
            graph.insertSequence(KMer.from(read.toUpperCase()));
          }

          try {
            if (readGraph(graph).equals(randomGenome.toUpperCase())) {
              points.add(new Pair<Integer, Integer>(genomeSize, readLength));
              break;
            }
          }
          catch (Error e) {
            continue;
          }
        }
      }
    }


    // Write data to file to use later
    File file = new File("append.txt");
    FileWriter fr = new FileWriter(file, true);
    BufferedWriter br = new BufferedWriter(fr);

    for (Pair<Integer, Integer> point : points) {
      br.write(point.toString() + "\r\n");
    }

    br.close();
    fr.close();
  }


  /**
   * Reads the graph as a sequence by computing the eulerian path, taking the
   * first item, and then appending the last letter of each succsessive element
   * in the eulerian path.
   * 
   * @param graph the eulerian graph to read
   * @return A string representing the sequence read from the eulerian circuit
   */
  static String readGraph(DeBruijnGraph<KMer> graph) {
    List<String> eulerianPath = graph.computeEulerianPath();
    String head = eulerianPath.get(0);
    String tail = eulerianPath.stream().skip(1)
      .map(x -> x.substring(x.length() - 1))
      .reduce((acc, val) -> acc + val)
      .orElse("");

    return head + tail;
  }


  static List<String> window(int size, String source) {
    List<String> list = new ArrayList<String>();

    for (int i = 0; i < source.length() - size + 1; i += 1) {
      list.add(source.substring(i, i + size));
    }

    return list;
  }


  /**
   * A generic pair class, which joins types T and U into a single pair.
   * This is used to represent (x, y) pairs in the driver class.
   */
  private static class Pair<T, U> {
    public T lhs;
    public U rhs;

    public Pair(T lhs, U rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    public String toString() {
      return "" + lhs + "\t" + rhs;
    }
  }
}