import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {

  public static final String PROPERTY = "FA";

  public static void main(String[] args) {

    File csvDirectory = new File("CSV/" + PROPERTY);

    // initialize bins
    ArrayList<Study> studies = new ArrayList<Study>();
    for (File path : csvDirectory.listFiles()) {
      studies.add(new Study(path.getPath()));
    }

    // read in data
    int studyIndex = 0;
    for (File study : csvDirectory.listFiles()) {
      int tractIndex = 0;
      String line = "";
      for (File tractCSV : study.listFiles()) {
        try {
          BufferedReader tractReader = new BufferedReader(new FileReader(tractCSV));
          studies.get(studyIndex).getTracts().add(new Tract(tractCSV.getName()));
          tractReader.readLine(); // throw away header line
          while (tractReader.ready()) {
            line = tractReader.readLine();
            studies.get(studyIndex).getTracts().get(tractIndex).setArcLength(line);
            studies.get(studyIndex).getTracts().get(tractIndex).setAtlasFA(line);
          }
        } catch (Exception e) {
          System.err.println(line);
          System.err.println(tractCSV.getName());
          e.printStackTrace();
          System.exit(0);
        }
        tractIndex++;
      }
      studyIndex++;
      // System.out.println(studyIndex);
    }

    // pairwise comparison
    for (int i = 0; i < csvDirectory.listFiles().length - 1; i++) {
      for (int j = (i + 1); j < csvDirectory.listFiles().length; j++) {

        // studies.get(i).getTracts().get(0).printFAs();
        // studies.get(j).getTracts().get(0).printFAs();

        // make the comparison dir
        File outputDir = new File("output/" + PROPERTY + "/Delta" + studies.get(j).getName() + "_to_" + studies.get(i).getName());
        outputDir.mkdir();
        // iterate over each tract pair and compute difference
        int tractIndex = 0;
        for (Tract tract : studies.get(i).getTracts()) {
          FileWriter outputCSV = null;
          try {
            outputCSV = new FileWriter(new File(outputDir.getPath() + "/" + tract.getName()));
            outputCSV.write("ArcLength,Delta" + PROPERTY + "\n");
            for (int k = 0; k < tract.getArcLengths().size(); k++) {
              outputCSV.write(Double.toString(tract.getArcLength(k)) + ",");
              // System.out.println("Study " + studies.get(j).getName() + " and
              // " + studies.get(i).getName());
              // lol next line
              // System.out.println(studies.get(j).getTracts().get(tractIndex).getAtlasFA(k)
              // + " - " +
              // studies.get(i).getTracts().get(tractIndex).getAtlasFA(k));
              if (studies.get(j).getTracts().get(tractIndex).getAtlasFA(k) == Double.MIN_VALUE || studies.get(i).getTracts().get(tractIndex).getAtlasFA(k) == Double.MIN_VALUE) {
                outputCSV.write("0\n"); // write out 0 where we have a nan
              } else {
                outputCSV.write(Double.toString(studies.get(j).getTracts().get(tractIndex).getAtlasFA(k) - studies.get(i).getTracts().get(tractIndex).getAtlasFA(k)) + "\n");
              }
            }

          } catch (Exception e) {
            System.err.println(studies.get(i).getTracts().get(tractIndex).getName() + " and " + studies.get(j).getTracts().get(tractIndex).getName());
            System.err.println(studies.get(i).getName() + " and " + studies.get(j).getName());
            System.err.println(tract.getName());
            e.printStackTrace();
            System.exit(0);
          }
          tractIndex++;
          try {
            outputCSV.flush();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }
  }

}
