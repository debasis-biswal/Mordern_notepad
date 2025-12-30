import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StylishNotepad extends JFrame implements ActionListener {
    JTextArea textArea;
    JFileChooser fileChooser;
    File currentFile;
    JComboBox<String> fontBox, sizeBox;
    JButton colorButton;

    public StylishNotepad() {
        // Frame settings
        setTitle("Stylish Notepad");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Text area setup
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Menu bar setup
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        String[] fileItems = {"New", "Open", "Save", "Exit"};
        for (String item : fileItems) {
            JMenuItem menuItem = new JMenuItem(item);
            menuItem.addActionListener(this);
            fileMenu.add(menuItem);
        }
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Styling panel (top)
        JPanel stylePanel = new JPanel();
        stylePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        stylePanel.setBackground(new Color(230, 230, 250));
        stylePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Font style dropdown
        fontBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontBox.setSelectedItem("Arial");
        fontBox.addActionListener(e -> updateStyle());
        stylePanel.add(new JLabel("Font:"));
        stylePanel.add(fontBox);

        // Font size dropdown
        String[] sizes = {"12", "14", "16", "18", "20", "24", "28", "32", "36","40","44"};
        sizeBox = new JComboBox<>(sizes);
        sizeBox.setSelectedItem("18");
        sizeBox.addActionListener(e -> updateStyle());
        stylePanel.add(new JLabel("Size:")); 
        stylePanel.add(sizeBox);

        // Color chooser button
        colorButton = new JButton("Text Color");
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose Text Color", textArea.getForeground());
            if (color != null) {
                textArea.setForeground(color);
            }
        });
        stylePanel.add(colorButton);

        // Add styling panel to top
        add(stylePanel, BorderLayout.NORTH);

        // File chooser
        fileChooser = new JFileChooser();
    }

    // File Menu Action
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                textArea.setText("");
                currentFile = null;
                setTitle("Stylish Notepad - New File");
                break;

            case "Open":
                int openOption = fileChooser.showOpenDialog(this);
                if (openOption == JFileChooser.APPROVE_OPTION) {
                    currentFile = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                        textArea.read(reader, null);
                        setTitle("Stylish Notepad - " + currentFile.getName());
                    } catch (IOException ex) {
                        showError("Failed to open file");
                    }
                }
                break;

            case "Save":
                if (currentFile == null) {
                    int saveOption = fileChooser.showSaveDialog(this);
                    if (saveOption == JFileChooser.APPROVE_OPTION) {
                        currentFile = fileChooser.getSelectedFile();
                    } else {
                        return;
                    }
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                    textArea.write(writer);
                    setTitle("Stylish Notepad - " + currentFile.getName());
                } catch (IOException ex) {
                    showError("Failed to save file");
                }
                break;

            case "Exit":
                System.exit(0);
                break;
        }
    }

    // Styling update
    private void updateStyle() {
        String selectedFont = (String) fontBox.getSelectedItem();
        int selectedSize = Integer.parseInt((String) sizeBox.getSelectedItem());
        textArea.setFont(new Font(selectedFont, Font.PLAIN, selectedSize));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StylishNotepad().setVisible(true));
    }
}
