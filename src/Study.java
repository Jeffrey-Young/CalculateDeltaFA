import java.util.ArrayList;

public class Study {

  private String           _name;
  private String           _path;
  private ArrayList<Tract> _tracts;

  public Study(String name) {
    System.out.println(name);
    _path = name;
    _name = name.split("\\\\")[2]; // we want the tail as the name, thanks stack
                                   // overflow for \\\\
    _tracts = new ArrayList<Tract>();
  }

  public ArrayList<Tract> getTracts() {
    return _tracts;
  }

  public String getName() {
    return _name;
  }

}
