package kdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings({"unused", "resource"})
public class InputData {
  private int support;
  private int confidence;
  private String dataFilePath, decisionFromVal, decisionAttribute, decisionToVal;
  private List<String> attributes = new ArrayList<String>();
  private List<String> stableAttrs = new ArrayList<String>();
  private List<String> flexibleAttrs = new ArrayList<String>();
  private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

  public void SetDataFilePath(String dataFilePath) {
      this.dataFilePath = dataFilePath;
      ReadData();
  }

  public void ReadData() {
      String[] row;
      for (String attribute:attributes) {
          data.add(new ArrayList<String>());
      }
      
      try {
		Scanner input = new Scanner(new File(dataFilePath));
          
          while (input.hasNextLine()) {
              row = input.nextLine().split(",");
              for (int i=0; i<row.length; i++) {
            	  data.get(i).add(row[i].trim());
              }
          }
      } catch (FileNotFoundException e) {
          printMessage("File Not Found");
          e.printStackTrace();
      }
  }

  public void SetSupport(int support) {
      this.support = support;
  }

  public void SetConfidence(int confidence) {
      this.confidence = confidence;
  }

  public void SetAttributeNames(String[] attributeNames) {
      this.attributes = new ArrayList<String>(Arrays.asList(attributeNames));
      this.flexibleAttrs = new ArrayList<String>(Arrays.asList(attributeNames));
  }
  
  public void SetDecisionAttribute(String decisionAttribute) {
      this.decisionAttribute = decisionAttribute;
      this.flexibleAttrs.remove(decisionAttribute);
  }

  public void SetStableAttribute(int[] stableAttributesIndex) {
      for (int attributeIndex:stableAttributesIndex) {
          this.stableAttrs.add(this.attributes.get(attributeIndex));
      }

      for (String attribute:this.stableAttrs) {
          if(this.flexibleAttrs.contains(attribute)) {
              this.flexibleAttrs.remove(attribute);
          }
      }
  }

	public void SetDecisionFromValue(String decisionFrom)
  {
      int decisionAttributeIndex = attributes.indexOf(decisionAttribute);
      if (data.get(decisionAttributeIndex).contains(decisionFrom)) {
          this.decisionFromVal = decisionFrom;
      }
		else {
			printMessage("Invalid value.");
		}
	}

  public void SetDecisionToValue(String decisionTo)
  {
      int decisionAttributeIndex = attributes.indexOf(decisionAttribute);
              
      if (data.get(decisionAttributeIndex).contains(decisionTo)) {
          this.decisionToVal = decisionTo;
      }
      else {
          printMessage("Invalid value.");
      }
  }

  public ArrayList<ArrayList<String>> GetData() { return data; }
  public int GetSupport() { return support; }
  public int GetConfidence() { return confidence; }
  public List<String> GetAttributeNames() { return attributes; }
  public String GetDecisionAttribute() { return decisionAttribute; }
  public List<String> GetStableAttributes() { return stableAttrs; }
  public List<String> GetFlexibleAttributes() { return flexibleAttrs; }
  public String GetDecisionFromValue() { return decisionFromVal; }
  public String GetDecisionToValue() { return decisionToVal; }

  public void printMessage(String content) {
      System.out.println(content);
  }
             
  public void printList(List<String> list) {
      Iterator<String> iterate = list.iterator();
              
      while(iterate.hasNext()) {
          printMessage(iterate.next().toString());
      }
  }
}

