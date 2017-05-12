import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

public class FamilyFriday implements ActionListener {
    private int eCount = 0;
    private JLabel label = new JLabel();
    private JTextField nameField = new JTextField();
    private JFrame frame = new JFrame();
    private ArrayList<String> employees = new ArrayList<String>();
    private ArrayList<String> groups = new ArrayList<String>();
    private ArrayList<String> history = new ArrayList<String>();

    public FamilyFriday() {
        loadEmployees();

        label.setText("Employees: " + eCount);

        // the clickable button
        JButton addName = new JButton("Add Name");
        JButton groupEm = new JButton("Group Em");
        addName.addActionListener(this);
        groupEm.addActionListener(this);

        // the panel with the button and text
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(nameField);
        panel.add(addName);
        panel.add(groupEm);
        panel.add(label);

        // set up the frame and display it
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Family Friday");
        frame.pack();
        frame.setVisible(true);
    }

    // process the button clicks
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Name")) {
          String newName = nameField.getText().replaceAll("\\s+","");
          if (!newName.equals("")) {
            try {
              String filename= "Employees.txt";
              FileWriter fw = new FileWriter(filename,true);
              fw.write(newName + "\n");//appends the string to the file
              fw.close();
            } catch(IOException ioe) {
                System.err.println("IOException: " + ioe.getMessage());
            }
            employees.add(newName);
            eCount++;
            label.setText("Employees: " + eCount);
            groups.clear();
          }
          nameField.setText("");
        } else if (e.getActionCommand().equals("Group Em")) {
            if (!employees.isEmpty()) {
              //randomize array
              long seed = System.nanoTime();
              Collections.shuffle(employees, new Random(seed));
            }
            //if not enough employees
            if (eCount < 3) {
              System.out.println("Not enough employees. There must be at least 3.\n");
              return;
            }
            // minimum cases
            if (eCount == 3 || eCount == 4 || eCount == 5) {
              String tmp = "";
              for (int i = 0; i < eCount; i++) {
                tmp = tmp + employees.get(i) + " ";
              }
              groups.add(tmp);
            } else {
                int min = 3;
                int minTotal = eCount - (eCount % min);

                // group names in 3's
                int count = 0;
                String tmp = "";
                int addFlag = 1;
                for (int j = 0; j < minTotal; j++) {
                  count++;
                  tmp = tmp + employees.get(j) + " ";
                  if (count == min) {
                    String tmp2 = tmp;
                    char[] chars = tmp2.toCharArray();
                    Arrays.sort(chars);
                    String sorted = new String(chars);
                    for (int k = 0; k < history.size(); k++) {
                      String hist = history.get(k);
                      chars = hist.toCharArray();
                      Arrays.sort(chars);
                      String sortedHist = new String(chars);
                      if (tmp2.equals(sortedHist)) {
                        addFlag = 0;
                      }
                    }
                    if (addFlag == 1) {
                      groups.add(tmp);
                      count = 0;
                      tmp = "";
                    }
                    addFlag = 1;
                  }
                }
                // add anyone left out to the first couple groups available
                if (minTotal < eCount) {
                  int i = minTotal;
                  for (int j = minTotal; j < eCount; j++) {
                    groups.set(j - i, groups.get(j - i) + " " + employees.get(j) +  " ");
                  }
                }
            }
            printGroups();
        }
    }

    public void loadEmployees() {
      //load names into array
      File file = new File("./Employees.txt");
      FileInputStream fis = null;
      BufferedReader reader = null;

      try {
        fis = new FileInputStream(file);
        reader = new BufferedReader(new InputStreamReader(fis));

        String line = reader.readLine();
          while(line != null){
            eCount++;
            employees.add(line);
            line = reader.readLine();
          }
      } catch (FileNotFoundException ex) {
          Logger.getLogger(FamilyFriday.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
          Logger.getLogger(FamilyFriday.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          try {
              reader.close();
              fis.close();
          } catch (IOException ex) {
              Logger.getLogger(FamilyFriday.class.getName()).log(Level.SEVERE, null, ex);
          }
      }

      // load history
      File file2 = new File("./GroupHistory.txt");
      try {
        fis = new FileInputStream(file2);
        reader = new BufferedReader(new InputStreamReader(fis));

        String line = reader.readLine();
          while(line != null){
            history.add(line);
            line = reader.readLine();
          }
      } catch (FileNotFoundException ex) {
          Logger.getLogger(FamilyFriday.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
          Logger.getLogger(FamilyFriday.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          try {
              reader.close();
              fis.close();
          } catch (IOException ex) {
              Logger.getLogger(FamilyFriday.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
    }

    public void printGroups() {
      if (!groups.isEmpty()) {
        System.out.println("Generated group(s): \n");
        String groupNames = "";
        for (int i = 0; i < groups.size(); i++) {
          groupNames = groups.get(i);
          System.out.println("Group " + (i + 1) + ": " + groupNames);
          try {
            String filename= "GroupHistory.txt";
            FileWriter fw = new FileWriter(filename,true);
            fw.write(groupNames + "\n");//appends the string to the file
            fw.close();
          } catch(IOException ioe) {
              System.err.println("IOException: " + ioe.getMessage());
          }
        }
        groups.clear();
      }
    }
    // create one Frame
    public static void main(String[] args) {
        new FamilyFriday();
    }
}
