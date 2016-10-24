import java.util.ArrayList;

public class Tract {

  private String            _name;
  private ArrayList<Double> _arcLengths;
  private ArrayList<Double> _averageFAs;

  public Tract(String name) {
    _name = name;
    _arcLengths = new ArrayList<Double>();
    _averageFAs = new ArrayList<Double>();
  }

  // throw away first index (label) and parse the rest into doubles
  public void setArcLength(String line) {
    _arcLengths.add(Double.parseDouble(line.split(",")[0]));
  }

  public void setAtlasFA(String line) {
    double sum = 0;
    double size = 0;
    // want to skip last column (atlas FA)
    String FAArray[] = line.split(",");
    for (int i = 1; i < FAArray.length - 1; i++) {
      if (FAArray[i].toLowerCase().contains("nan")) {
        continue;
      }
      sum += Double.parseDouble(FAArray[i]);
      size++;
    }
    if (sum / size > 1) {
      System.out.println(sum / size + _name);
    }
    _averageFAs.add(sum / size);
  }

  public String getName() {
    return _name;
  }

  public ArrayList<Double> getArcLengths() {
    return _arcLengths;
  }

  public double getArcLength(int i) {
    return _arcLengths.get(i);
  }

  public double getAtlasFA(int i) {
    return _averageFAs.get(i);
  }

  public void printFAs() {
    for (int i = 0; i < _averageFAs.size(); i++) {
      System.out.print(_averageFAs.get(i) + ",");
    }
    System.out.print("\n");
  }
}
