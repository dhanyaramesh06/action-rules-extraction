package kdd;

import java.awt.Color;
import java.io.*;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import java.util.ArrayList;



@SuppressWarnings({ "rawtypes", "serial","unused" })
public class UI extends JPanel {

    InputVerifier verifier = new VerifyNumber();

    private JLabel selectStable, decisionAttrLabel, decisionValFromLabel, decisionValToLabel,attrFilePath, dataFilePath;    
	private JComboBox decisionComboBox;
    private JPanel decisionPanel, stablePanel;
    private JTextField indices, support, confidence, decisionValFrom, decisionValTo;
    private JButton openAttr, openDataset, loadData, submitButton;
    private JFileChooser fileChooser;
	private ButtonGroup decisionGroup, ruleGroup;
    private JLabel stableAttrLabel, supportLabel, confidenceLabel, dataFileLabel, attrFilePathLabel, compLabel;
    private JScrollPane jScrollPane1;
    private JList<?> stableColumns;
    private String[] attributes, allAttrsExceptDecision, allAttributes;
    private LinkedList<String> finalData;
	
    public UI() {        
        initialize();
    }

    private void initialize() {

        selectStable = new JLabel();    
        indices = new JTextField();
        decisionPanel = new JPanel();
        decisionValToLabel = new JLabel();
        decisionValFromLabel = new JLabel();
        decisionAttrLabel = new JLabel();
        decisionValTo = new JTextField();
        decisionValFrom = new JTextField();
        support = new JTextField();
        confidence = new JTextField();
        decisionComboBox = new JComboBox();
        stablePanel = new JPanel();
        decisionGroup = new ButtonGroup();
        stableAttrLabel = new JLabel();
        jScrollPane1 = new JScrollPane();
        stableColumns = new JList();
        stableColumns.setEnabled(false);
        submitButton = new JButton();
        loadData = new JButton();
        openAttr = new JButton();
        openDataset = new JButton();
        fileChooser = new JFileChooser();
        attrFilePath = new JLabel();
        dataFilePath = new JLabel();
        compLabel = new JLabel();
        attrFilePathLabel = new JLabel();
        dataFileLabel = new JLabel();
        supportLabel = new JLabel();
        confidenceLabel = new JLabel();
        finalData = new LinkedList<String>();



        decisionPanel.setBorder(BorderFactory.createTitledBorder(null, "Decision Attribute Details", 0, 0, new java.awt.Font("Times New Roman", 1, 20)));
        decisionPanel.setBackground(Color.white);
        decisionValFromLabel.setText("Value From");
        decisionValToLabel.setText("Value To");

        decisionAttrLabel.setText("Decision Attribute");

        decisionValFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decisionValueFromActionPerformed(evt);
            }
        });

        decisionValTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decisionValueToActionPerformed(evt);
            }
        });
        


        GroupLayout decisionPanelLayout = new GroupLayout(decisionPanel);
        
        decisionPanel.setLayout(decisionPanelLayout);
        decisionPanelLayout.setHorizontalGroup(
            decisionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(decisionPanelLayout.createSequentialGroup()
            	.addComponent(decisionValFromLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
            	.addComponent(decisionValFrom, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
            	.addGap(50, 50, 50)
            	.addComponent(decisionValToLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
            	.addComponent(decisionValTo, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
            	.addGroup(decisionPanelLayout.createSequentialGroup()
                .addComponent(decisionAttrLabel, GroupLayout.DEFAULT_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addComponent(decisionComboBox, GroupLayout.DEFAULT_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        );
        decisionPanelLayout.setVerticalGroup(
            decisionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(decisionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(decisionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                .addComponent(decisionComboBox, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
	                .addComponent(decisionAttrLabel, GroupLayout.DEFAULT_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(decisionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                		.addComponent(decisionValFromLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addComponent(decisionValFrom, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
	                    .addComponent(decisionValToLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addComponent(decisionValTo, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        ));

        stablePanel.setBorder(BorderFactory.createTitledBorder(null, "Stable Attribute Details", 0, 0, new java.awt.Font("Times New Roman", 1, 20)));
        stablePanel.setBackground(Color.white);
        stableAttrLabel.setText("Stable Attributes");
        selectStable.setText("Enter Indices");

        GroupLayout stablePanelLayout = new GroupLayout(stablePanel);
        stablePanel.setLayout(stablePanelLayout);
        stablePanelLayout.setHorizontalGroup(
            stablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(stablePanelLayout.createSequentialGroup()
                .addComponent(stableAttrLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(20,20,20)
                .addComponent(selectStable, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addComponent(indices, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
        );
        stablePanelLayout.setVerticalGroup(
            stablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(stablePanelLayout.createSequentialGroup()
                .addGroup(stablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(stableAttrLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectStable, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(indices, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
        );

        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					submitActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });

        openAttr.setText("Open Attribute File");
        openAttr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathActionPerformed(evt);  
            }
        });
        
        openDataset.setText("Open Data File");
        openDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathActionPerformed1(evt);  
            }
        });

        loadData.setText("Load");
        loadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
           				dataLoadActionPerformed(evt);
           				} catch (IOException e) {
           					System.out.println("Cannot load the Data");
           					e.printStackTrace();
           				}  
            }
        });
        
        
        
        attrFilePath.setText("Attribute File");
        dataFilePath.setText("Data File");
        supportLabel.setText("Support");
        confidenceLabel.setText("Confidence");
        

        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(decisionPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(stablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(500, 500, 500)
                                .addComponent(submitButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(attrFilePath, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                .addGap(1)
                                .addComponent(openAttr, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                            	.addGap(250)	
                            	.addComponent(dataFilePath, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addGap(1)
                                .addComponent(openDataset, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                            	.addGap(30)	)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                .addGap(50)
                            	.addComponent(supportLabel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            	.addGap(1)
                            	.addComponent(support, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            	.addGap(30)
                            	.addComponent(confidenceLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            	.addGap(1)
                            	.addComponent(confidence, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            	.addGap(10)
                            	.addComponent(attrFilePathLabel, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
                            	.addComponent(dataFileLabel, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE))))
                        		.addGap(10)
                        		.addGroup(layout.createSequentialGroup()
                        				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        						.addGroup(layout.createSequentialGroup()   
                        		.addGap(500)
                                .addComponent(loadData, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(207, 207, 207)
                                .addComponent(compLabel, GroupLayout.PREFERRED_SIZE, 419, GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 66, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(attrFilePath, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(openAttr, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataFilePath, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(openDataset, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(supportLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                	.addComponent(support, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                	.addComponent(confidenceLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                	.addComponent(confidence, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                	.addComponent(attrFilePathLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                	.addComponent(dataFileLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                	.addComponent(loadData, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(decisionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(stablePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submitButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

  private void decisionValueFromActionPerformed(java.awt.event.ActionEvent evt) {
      // TODO add your handling code here:
  }

    private void decisionValueToActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    // When Load button is clicked this function gets invoked
    @SuppressWarnings("unchecked")
	private void dataLoadActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        InputVerifier verifier = new VerifyNumber();
        support.setInputVerifier(verifier);
        InputVerifier verifier1 = new VerifyNumber();
        confidence.setInputVerifier(verifier1);
  
        
    	String fileName = attrFilePathLabel.getText();
        String line = null;
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                attributes = line.split("\\s+");
                allAttributes = line.split("\\s+");
            }   
            for(int i =0; i<attributes.length;i++){
            	attributes[i] = Integer.toString(i).concat("-").concat(attributes[i]);
            }
            bufferedReader.close();         
            stableColumns.setModel(new AbstractListModel() {
                public int getSize() { return attributes.length; }
                public Object getElementAt(int i) { return attributes[i]; }
            });
            jScrollPane1.setViewportView(stableColumns);
            decisionComboBox.setModel(new DefaultComboBoxModel(attributes));

    }
    // When submit is clicked this function gets triggered.
    private void submitActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        // Indices of Stable Attributes
        int i = 0;
        
        int[] stableAttributes;
        if(indices.getText().toString().length()!=0){
        	String[] stableAttributeIndices = indices.getText().toString().split(",");
        	stableAttributes = new int[stableAttributeIndices.length];
            for (String attributeIndex:stableAttributeIndices) {
            	stableAttributes[i] = Integer.parseInt(attributeIndex);
            	i++;
            }
        }
        else{
        	stableAttributes = new int[0];
        }
        
        ActionRules.SetAttributeNames(allAttributes);
        ActionRules.SetDataFilePath(dataFileLabel.getText().toString());
        ActionRules.SetSupportThreshold(Integer.parseInt(support.getText().toString()));
        ActionRules.SetConfidenceThreshold(Integer.parseInt(confidence.getText().toString()));
        ActionRules.SetDecisionAttribute(decisionComboBox.getSelectedItem().toString().split("-")[1]);
        ActionRules.SetDecisionFromValue(decisionValFrom.getText().toString());
        ActionRules.SetDecisionToValue(decisionValTo.getText().toString());
        ActionRules.SetStableAttribute(stableAttributes);
        
        ActionRules.aras();
    }
    
// This method gets the Attribute File by selecting the file from file chooser.
    private void filePathActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	int returnVal = fileChooser.showOpenDialog(this);
    	if(returnVal == JFileChooser.APPROVE_OPTION){
    		attrFilePathLabel.setText(fileChooser.getSelectedFile().getPath());
    	}
    }
// This method gets the Data File by selecting the file from file chooser.
    private void filePathActionPerformed1(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	int returnVal = fileChooser.showOpenDialog(this);
    	if(returnVal == JFileChooser.APPROVE_OPTION){
    		dataFileLabel.setText(fileChooser.getSelectedFile().getPath());
    	}
    }
    
    public class VerifyNumber extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
           String text = null;

           if (input instanceof JTextField) {
             text = ((JTextField) input).getText();
           }
           try {
              Integer.parseInt(text);
           } catch (NumberFormatException e) {
               JOptionPane.showMessageDialog(null, "Invalid data");
        	   return false;
           }
           return true;
        }
        @Override
        public boolean shouldYieldFocus(JComponent input) {
           boolean valid = verify(input);
           if (!valid) {
              JOptionPane.showMessageDialog(null, "Invalid data");
           }
           return valid;
       }
    }
}