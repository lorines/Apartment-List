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
            if (eCount < 3) {
              System.out.println("Not enough employees. There must be at least 3.\n");
              return;
            }
            if (eCount == 3 || eCount == 4 || eCount == 5) {
              String tmp = "";
              for (int i = 0; i < eCount; i++) {
                tmp = tmp + employees.get(i) + " ";
              }
              groups.add(tmp);
            } else {
                int min = 3;
                int minTotal = eCount - (eCount % min);

                int count = 0;
                String tmp = "";
                for (int j = 0; j < minTotal; j++) {
                  count++;
                  tmp = tmp + employees.get(j) + " ";
                  if (count == min) {
                    groups.add(tmp);
                    count = 0;
                    tmp = "";
                  }
                }
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
      //load file into array
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
    }

    public void printGroups() {
      if (!groups.isEmpty()) {
        System.out.println("Generated group(s): \n");
        for (int i = 0; i < groups.size(); i++) {
          System.out.println("Group " + (i + 1) + ": " + groups.get(i));
        }
        groups.clear();
      }
    }
    // create one Frame
    public static void main(String[] args) {
        new FamilyFriday();
    }
}
