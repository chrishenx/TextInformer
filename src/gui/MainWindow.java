package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import model.TextInformation;
import model.TextProcessor;

/**
 * GUI para mostrar informacion sobre un texto.
 * @author Christian González León
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {
  public static final String APPLICATION_NAME = "Información del texto";
  public static final Dimension windowSize;

  // Top controls
  private JButton selectFileButton;
  private JTextField pathTextField;
  
  // Bottom controls
  private JButton informationButton;
  private JTextField informationTextField;
  
  // Central controls
  private JTextArea textContainer;
  private Container mainContainer;  
  
  static {
    windowSize = new Dimension(680, 540);
  }
  
  public static void main(String[] args) {

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }
  
  public MainWindow() {
    JPanel pathPanel = new JPanel();
    pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    selectFileButton = new JButton("Seleccionar arhivo");
    pathPanel.add(selectFileButton);
    
    pathTextField = new JTextField(50);
    pathTextField.setEditable(false);
    pathTextField.setHorizontalAlignment(JTextField.CENTER);
    pathPanel.add(pathTextField);
    
    mainContainer = getContentPane();
    
    mainContainer.add(pathPanel, BorderLayout.PAGE_START);
    
    textContainer = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textContainer);
    mainContainer.add(scrollPane, BorderLayout.CENTER);
    
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    informationButton = new JButton("Información");
    bottomPanel.add(informationButton);
    
    informationTextField = new JTextField(54);
    informationTextField.setEditable(false);
    informationTextField.setHorizontalAlignment(JTextField.CENTER);
    bottomPanel.add(informationTextField);
    
    mainContainer.add(bottomPanel, BorderLayout.PAGE_END);
    
    configureEventListeners();
  }
  
  private void configureEventListeners() {
    selectFileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Archivo de texto plano", "txt", "cpp", "c", "java", "py", "js", "css", "html");
        fileChooser.setFileFilter(filter);
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showOpenDialog(MainWindow.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String textfilePath = fileChooser.getSelectedFile().getAbsolutePath();
          String textRead = readTextFile(textfilePath);
          textContainer.setText(textRead);
          pathTextField.setText(textfilePath);
        }
      }
    });
    
    informationButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String inputText = textContainer.getText();
        TextProcessor textProcessor = new TextProcessor(inputText);
        TextInformation textInformation = textProcessor.calculateInformation();
        informationTextField.setText(
            "Cantidad de información: " + textInformation.getInformation() + " bits     |||     " +
            "Entropia: " + textInformation.getEntropy()
            );
      }
    });
  }
  
  public static void createAndShowGUI() {
    MainWindow mainWindow = new MainWindow();
    mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
    mainWindow.setTitle(APPLICATION_NAME);
    mainWindow.setMinimumSize(windowSize);
    mainWindow.setResizable(false);
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      System.out.println(e.getMessage());
    }
    SwingUtilities.updateComponentTreeUI(mainWindow);
    mainWindow.pack();
    mainWindow.setLocationRelativeTo(null);
    mainWindow.setVisible(true);
  }
  
  private String readTextFile(String filename) {
    try {
      FileInputStream inputStream = new FileInputStream(filename);
      InputStreamReader streamReader = new InputStreamReader(inputStream);
      BufferedReader reader = new BufferedReader(streamReader);
      String line;
      String wholeText = "";
      do {
        wholeText += line = reader.readLine();
        wholeText += "\n";
      } while (line != null);
      reader.close();
      return wholeText;
    } catch (IOException e) {
      System.out.println("Error cargando archivo.");
    }
    return "";
  }
}
