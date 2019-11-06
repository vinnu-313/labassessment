/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.view;

import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import las.listener.AddQuestionViewListener;

/**
 *
 * @author megha
 */
public class AddQuestionView extends JFrame {

    private JPanel mainPanel = null;
    private JLabel questionLabel = null;
    public JTextField questionField = null;
    public JLabel messageLabel = null;
    private JButton applyButton = null;
    private JButton exitButton = null;
    private JButton resetButton = null;

    public AddQuestionView(String title) throws HeadlessException {
        super(title);
    }

    public AddQuestionView() throws HeadlessException {
    }

    public void showAddQuestionView() {
        mainPanel = new JPanel();
        questionLabel = new JLabel("Question : ");
        mainPanel.add(questionLabel);
        questionField = new JTextField(50);
        mainPanel.add(questionField);
        applyButton = new JButton("Apply");
        applyButton.addActionListener(new AddQuestionViewListener());
        mainPanel.add(applyButton);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new AddQuestionViewListener());
        mainPanel.add(exitButton);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new AddQuestionViewListener());
        mainPanel.add(resetButton);
        messageLabel = new JLabel();
        mainPanel.add(messageLabel);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        this.pack();
        this.getRootPane().setDefaultButton(applyButton);
        this.setVisible(true);
        this.addWindowListener(new AddQuestionViewListener());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
