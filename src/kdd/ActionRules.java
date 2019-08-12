package kdd;

//Import statements
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ActionRules {
	public static PrintWriter writer;
	public static Scanner input;
	public static int support, confidence;
	public static String dataFilePath;
	public static List<String> attributes = new ArrayList<String>();
	public static List<String> stableAttrs = new ArrayList<String>();
	public static List<String> flexibleAttrs = new ArrayList<String>();
	public static String decisionAttr, decisionFromVal, decisionToVal;
	public static ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>(); 
	static Map<String, HashSet<String>> distinctAttributeVals = new HashMap<String, HashSet<String>>();
	static Map<HashSet<String>, HashSet<String>> attributeVals = new HashMap<HashSet<String>, HashSet<String>>();
	static Map<HashSet<String>, HashSet<String>> oneItemsets = new HashMap<HashSet<String>, HashSet<String>>();
	static Map<HashSet<String>, HashSet<String>> reducedAttributeVals = new HashMap<HashSet<String>, HashSet<String>>();
	static Map<String, HashSet<String>> decisionVals = new HashMap<String, HashSet<String>>();
	static Map<ArrayList<String>, HashSet<String>> markedVals = new HashMap<ArrayList<String>, HashSet<String>>();
	public static Map<ArrayList<String>,String> certainRules = new HashMap<ArrayList<String>,String>();
	public static Map<ArrayList<String>,String> certainRulesForAras = new HashMap<ArrayList<String>,String>();
	public static Map<ArrayList<String>,HashSet<String>> possibleRules = new HashMap<ArrayList<String>,HashSet<String>>();

    //mapping the support threshold from Input class to the local variable
	public static void SetSupportThreshold(int supportThreshold) {
	    ActionRules.support = supportThreshold;
	}
	
	//mapping the confidence threshold from Input class to the local variable
	public static void SetConfidenceThreshold(int confidenceThreshold) {
	    ActionRules.confidence = confidenceThreshold;
	}

    //mapping the file path from Input class to the local variable
	public static void SetDataFilePath(String dataFilePath) {
	    ActionRules.dataFilePath = dataFilePath;
		readData();
	}

    //mapping the flexible attributes from Input class to the local variable
	public static void SetAttributeNames(String[] attributeNames) {
	    ActionRules.attributes = new ArrayList<String>(Arrays.asList(attributeNames));
	    flexibleAttrs = new ArrayList<String>(Arrays.asList(attributeNames));
	}
	
	//mapping the decision attributes from Input class to the local variable
	public static void SetDecisionAttribute(String decisionAttribute) {
	    ActionRules.decisionAttr = decisionAttribute;
	    flexibleAttrs.remove(decisionAttribute);
		HashSet<String> listOfDecisionValues = distinctAttributeVals.get(decisionAttribute);
		removeDecisionValueFromAttributes(listOfDecisionValues);
	
	}

    //mapping the stable attributes from Input class to the local variable
	public static void SetStableAttribute(int[] stableAttributesIndex) {
	    for (int attributeIndex:stableAttributesIndex) {
	        stableAttrs.add(attributes.get(attributeIndex));
	    }

	    for (String attribute:stableAttrs) {
	        if(flexibleAttrs.contains(attribute)) {
	            flexibleAttrs.remove(attribute);
	        }
	    }
	}

    //fetch decision from value from InputRead class to the local variable
	public static void SetDecisionFromValue(String inputDecisionFrom)
	{
		inputDecisionFrom = decisionAttr + inputDecisionFrom;
		
		if (inputDecisionFrom != null && !inputDecisionFrom.isEmpty()){
			if(decisionVals.keySet().contains(inputDecisionFrom)){
				ActionRules.decisionFromVal = inputDecisionFrom;
			}
		}
	    else {
	        printMessage("Invalid value.");
	    }
	}

    //fetch decision to value from InputRead to the local variable
	public static void SetDecisionToValue(String inputDecisionTo){
		
		inputDecisionTo = decisionAttr + inputDecisionTo;
		
		if (inputDecisionTo != null && !inputDecisionTo.isEmpty()){
			if(decisionVals.keySet().contains(inputDecisionTo)){
				ActionRules.decisionToVal = inputDecisionTo;
			}
		}
	    else {
	        printMessage("Invalid value.");
	    }
	}

	//Printing String, List and Map
	public static void printMessage(String content){
		System.out.println(content);
		writer.println(content);
	}
	
	public static void printList(List<String> list){
		Iterator<String> iterate = list.iterator();
		
		while(iterate.hasNext()){
			printMessage(iterate.next().toString());
		}
	}
	
	private static void printCertainRulesMap(Map<ArrayList<String>, String> value) {
		printMessage("\nCertain Rules:");
		if (value == null || value.isEmpty()){
			printMessage("None");
		}
		for(Map.Entry<ArrayList<String>,String> set : value.entrySet()){
			if (set.getValue().equals(decisionToVal)){
				System.out.println("set key: "+set.getKey().toString());
				System.out.println("decision to: "+decisionToVal.toString());
				certainRulesForAras.put(set.getKey(), decisionToVal.toString());
				printMessage(set.getKey().toString() + " -> "+decisionToVal);
			}
		}
	}
	
    //Print all possible rule maps using decision to value,support,confidence taken from the user
	private static void printPossibleRulesMap(Map<ArrayList<String>, HashSet<String>> value) {
		if(!value.isEmpty()){
			printMessage("\nPossible Rules:");
			for(Map.Entry<ArrayList<String>,HashSet<String>> set : value.entrySet()){
				for(String possibleValue:set.getValue()){
					if (possibleValue.equals(decisionToVal)){
						int support = calculateSupportLERS(set.getKey(),possibleValue);
						int confidence = calculateConfidence(set.getKey(),possibleValue);
						printMessage(set.getKey().toString() + " -> " + possibleValue + " [Support: " + support + ", Confidence: " + confidence +"%]");
					}
				}
			}
		}
	}
	
	
    //Print all marked values using the "markedvalues" hashmap
	private static void printMarkedValues() {
		printMessage("\nMarked Values:");
		for(Map.Entry<ArrayList<String>, HashSet<String>> markedSet : markedVals.entrySet()){
			attributeVals.remove(new HashSet<String>(markedSet.getKey()));
		
			printMessage(markedSet.toString());
		}
	}

	private static int findLERSSupport(ArrayList<String> tempList) {
		int count = 0;
		
		for(ArrayList<String> data : data){	
			if(data.containsAll(tempList))
				count++;
		}
		
		return count;
	}
	
	private static int calculateSupportLERS(ArrayList<String> key, String value) {
		ArrayList<String> tempList = new ArrayList<String>();
		
		for(String val : key){
			tempList.add(val);
		}
		
		if(!value.isEmpty())
			tempList.add(value);
	
		return findLERSSupport(tempList);
		
	}
// calculate the confidence 
	private static int calculateConfidence(ArrayList<String> key,
			String value) {
		int num = calculateSupportLERS(key, value);
		int den = calculateSupportLERS(key, "");
		int confidence = 0;
		if (den != 0){
			confidence = (num * 100)/den;
		} 
		return confidence;
	}
	
	private static void readData() {
		try {
			input = new Scanner(new File(dataFilePath));
			int lineNo = 0;
			
			while(input.hasNextLine()){
				String[] lineData = input.nextLine().split(",");
				String key;
				
				lineNo++;
				ArrayList<String> tempList = new ArrayList<String>();
				HashSet<String> set;
				
				for (int i=0;i<lineData.length;i++) {
					String currentAttributeValue = lineData[i];
					String attributeName = attributes.get(i);
					key = attributeName + currentAttributeValue;
					
					tempList.add(key);

					HashSet<String> mapKey = new HashSet<String>();
					mapKey.add(key);
					setMap(attributeVals,lineData[i],mapKey,lineNo);
					setMap(oneItemsets,lineData[i],mapKey,lineNo);
					
					if (distinctAttributeVals.containsKey(attributeName)) {
						set = distinctAttributeVals.get(attributeName);
						set.add(key);
						
					}else{
						set = new HashSet<String>();
					}
					
					set.add(key);
					distinctAttributeVals.put(attributeName, set);
				}
		
				data.add(tempList);
			}
		} catch (FileNotFoundException e) {
			printMessage("File Not Found");
			e.printStackTrace();
		}
	}

	private static void setMap(Map<HashSet<String>, HashSet<String>> values,
			String string, HashSet<String> key, int lineNo) {
		HashSet<String> tempSet;
		
		if (values.containsKey(key)) {
			tempSet = values.get(key);						
		}else{
			tempSet = new HashSet<String>();
		}
		
		tempSet.add("x"+lineNo);
		values.put(key, tempSet);
	}

	private static void removeDecisionValueFromAttributes(HashSet<String> listOfDecisionValues) {
		
		for(String value : listOfDecisionValues){
			HashSet<String> newHash = new HashSet<String>();
			newHash.add(value);
			ActionRules.decisionVals.put(value, attributeVals.get(newHash));
			attributeVals.remove(newHash);
		}
	}
    
	//printing the rules generated 
	private static void findRules() {
		int loopCount = 0;
		printMessage("\n***********************************");
		printMessage("\nGenerating Certain Rules Using LERS");
		printMessage("\n***********************************");
		
		while(!attributeVals.isEmpty()){
			printMessage("\nLoop " + (++loopCount) +":");
			printMessage("***********************************");
			
			for (Map.Entry<HashSet<String>, HashSet<String>> set : attributeVals.entrySet()) {
				ArrayList<String> setKey = new ArrayList<String>();
				setKey.addAll(set.getKey());
				
				if (set.getValue().isEmpty()) {
					continue;
				}else{
					if(!includesMarked(setKey)){
						for(Map.Entry<String, HashSet<String>> decisionSet : decisionVals.entrySet()){
							if(decisionSet.getValue().containsAll(set.getValue())){
								certainRules.put(setKey, decisionSet.getKey());
								markedVals.put(setKey, set.getValue());
								break;
							}
						}
					}
				}
				
				if(!includesMarked(setKey)){
					HashSet<String> possibleRulesSet = new HashSet<String>();
					for(Map.Entry<String, HashSet<String>> decisionSet : decisionVals.entrySet()){
						
						possibleRulesSet.add(decisionSet.getKey());
					}
					possibleRules.put(setKey, possibleRulesSet);
				}
				
			}
			printMarkedValues();
			removeMarkedValues();
			
			printCertainRulesMap(certainRules);
			printPossibleRulesMap(possibleRules);
			
			combinePossibleRules();
		}
	}

	private static boolean includesMarked(ArrayList<String> setKey) {
		for(Map.Entry<ArrayList<String>, HashSet<String>> markedSet : markedVals.entrySet()){
			if(setKey.containsAll(markedSet.getKey())){
				return true;
			}
		}
		return false;
	}	
	
	private static void removeMarkedValues() {
		for(Map.Entry<ArrayList<String>, HashSet<String>> markedSet : markedVals.entrySet()){
			attributeVals.remove(new HashSet<String>(markedSet.getKey()));
		}
	}
	
	private static void combinePossibleRules() {
		Set<ArrayList<String>> keySet = possibleRules.keySet();
		ArrayList<ArrayList<String>> keyList = new ArrayList<ArrayList<String>>();
		keyList.addAll(keySet);
		HashSet<String> possibleRule;
		for(int i = 0;i<possibleRules.size();i++){
			for(int j = (i+1);j<possibleRules.size();j++){
				possibleRule = new HashSet<String>(keyList.get(j));
				Iterator<String> iter = possibleRule.iterator();
				HashSet<String> combinedKeys = null;
				while (iter.hasNext()) {
				    combinedKeys = new HashSet<String>(keyList.get(i));
				    if (combinedKeys.add((String)iter.next())){
				    	if(!checkSameGroup(combinedKeys)){
				    		combineAttributeValues(combinedKeys);
				    	}
				    }
				}
			}
		}
		
		certainRules.clear();
		possibleRules.clear();
		
		removeRedundantValues();
		clearAttributeValues();
	}

	private static boolean checkSameGroup(HashSet<String> combinedKeys) {
		List<String> list = new ArrayList<String>(combinedKeys);
		HashSet<String> pair = new HashSet<String>();
		if (combinedKeys.size()==2){
			if (isPairSameGroup(combinedKeys))
				return true;
		} else {
			for(int i = 0;i<list.size()-1;i++){
				for(int j = i+1;j<list.size();j++){
					pair.add(list.get(i));
					pair.add(list.get(j));
					if (isPairSameGroup(pair))
						return true;
					pair.clear();
				}
			}
		}
		return false;
	}

	private static boolean isPairSameGroup(HashSet<String> pair) {
		for(Map.Entry<String, HashSet<String>> singleAttribute : distinctAttributeVals.entrySet()){
			if(singleAttribute.getValue().containsAll(pair)){
				return true;
			}
		}
		return false;
	}
	
    private static HashSet<String> findSupportActionSchema(ArrayList<String> key, String value) {
        // Find support of generalized action schema
        ArrayList<String> tempList = new ArrayList<String>();
        
        for(String val : key){
            if (stableAttrs.contains(Character.toString(val.charAt(0)))) {
                tempList.add(val);
            }
        }
        
        if(null!=value && !value.isEmpty())
            tempList.add(value);
        
        HashSet<String> supportActionSchema = findARASSupport(tempList);
        
        // Find support of bad rule leading wrong decision
        tempList.clear();
        
        for(String val : key){
            tempList.add(val);
        }
        
        if(null!=value && !value.isEmpty())
            tempList.add(value);
        
        HashSet<String> supportBadRule = findARASSupport(tempList);
        
        // Correcting support for generalized action schema
        supportActionSchema.removeAll(supportBadRule);
        
        return supportActionSchema;
    }
	
	private static void combineAttributeValues(HashSet<String> combinedKeys) {
		HashSet<String> combinedValues = new HashSet<String>();
			
		for(Map.Entry<HashSet<String>, HashSet<String>> attributeValue : attributeVals.entrySet()){
			if(combinedKeys.containsAll(attributeValue.getKey())){
				if(combinedValues.isEmpty()){
					combinedValues.addAll(attributeValue.getValue());
				}else{
					combinedValues.retainAll(attributeValue.getValue());
				}
			}
		}
		
		if (!checkSameGroup(combinedKeys))
			reducedAttributeVals.put(combinedKeys, combinedValues);
	}

	private static void removeRedundantValues() {
		HashSet<String> mark = new HashSet<String>();
		
		for(Map.Entry<HashSet<String>, HashSet<String>> reducedAttributeValue : reducedAttributeVals.entrySet()){
			for(Map.Entry<HashSet<String>, HashSet<String>> attributeValue : attributeVals.entrySet()){
				
				if(attributeValue.getValue().containsAll(reducedAttributeValue.getValue()) || reducedAttributeValue.getValue().isEmpty()){
					mark.addAll(reducedAttributeValue.getKey());
				}
			}
		}
		
		reducedAttributeVals.remove(mark);
	}
	
    private static HashSet<String> findARASSupport(ArrayList<String> tempList) {
        HashSet<String> tempSet = new HashSet<String>();
        
        int count = 1;
        for(ArrayList<String> data : data){
            if(data.containsAll(tempList))
                tempSet.add("x"+count);
                count++;
        }
        
        return tempSet;
    }
    
    private static void clearAttributeValues() {
		 attributeVals.clear();
		 for(Map.Entry<HashSet<String>, HashSet<String>> reducedAttributeValue : reducedAttributeVals.entrySet()){
			 if (!checkSameGroup(reducedAttributeValue.getKey()))
				 attributeVals.put(reducedAttributeValue.getKey(), reducedAttributeValue.getValue());
		 }
		 reducedAttributeVals.clear();
	}

	
	private static void generateActionRules() {
        String ActionRulesSchema;
        String ActionRules;
        String attributeName = "";
        String attributeValueTo = "";
        String attributeValueFrom = "";
		
		printMessage("\n***********************************");
		printMessage("\nGenerating Action Rules Using ActionRules");
		printMessage("\n***********************************");
		
        // Run PrintActionRules for every Certain Rule
		for(Map.Entry<ArrayList<String>, String> certainRule : certainRulesForAras.entrySet()){
            ArrayList<String> attrInRule = new ArrayList<String>();
            for (String attr : certainRule.getKey()) {
                    attrInRule.add(Character.toString(attr.charAt(0)));
            }

            // Find header of stable attributes that are in given certain rule
            ArrayList<String> header = new ArrayList<String>();
            for (String attr : certainRule.getKey()) {
                if (stableAttrs.contains(Character.toString(attr.charAt(0)))) {
                    header.add(attr);
                }
            }

            // Find Action Rule Schema
    		printMessage(" ");
    		printMessage(" ");
			printMessage("Action Rule Schema: ");
            ActionRulesSchema = "";
            for (String attr: certainRule.getKey()) {
                attributeName = Character.toString(attr.charAt(0));
                attributeValueFrom = "";
                attributeValueTo = Character.toString(attr.charAt(1));
                
                if (stableAttrs.contains(attributeName))
                	ActionRulesSchema = formRule(ActionRulesSchema, attributeName, "", attributeValueTo, true);
                
                if (flexibleAttrs.contains(attributeName))
                	ActionRulesSchema = formRule(ActionRulesSchema, attributeName, "", attributeValueTo, false);
            }
            printMessage("[" + ActionRulesSchema + "] -> (" + decisionAttr + ", " + decisionFromVal + "->" + decisionToVal + ")");

            // Find support for Action Rule Schema
    		printMessage(" ");
            HashSet<String> supportActionSchema = findSupportActionSchema(certainRule.getKey(), decisionFromVal);
            printMessage("Support for Action Schema: "+supportActionSchema.toString());
        
            // Find list of attribute values, which are not considered in Action Rule Schema
            ArrayList<String> remainingVals = new ArrayList<String>();
            for (String sAttr : stableAttrs) {
                if (!attrInRule.contains(sAttr)) {
                    remainingVals.addAll(new ArrayList<String>(distinctAttributeVals.get(sAttr)));
                }
            }
            for (String fAttr : flexibleAttrs) {
                if (attrInRule.contains(fAttr)) {
                    for (String fAttrVal: new ArrayList<String>(distinctAttributeVals.get(fAttr))) {
                        if (!certainRule.getKey().contains(fAttrVal)) {
                            remainingVals.add(fAttrVal);
                        }
                    }
                }
            }
        
            // Find Marked Sets
    		printMessage(" ");
            printMessage("Marked Sets: ");
            ArrayList<ArrayList<String>> markedRules = new ArrayList<ArrayList<String>>();
            for (String attrVal: remainingVals) {
                ArrayList<String> tempRule = new ArrayList<String>();
                tempRule.addAll(header);
                tempRule.add(attrVal);
                
                HashSet<String> tempSupport = findARASSupport(tempRule);
                
                if (!tempSupport.isEmpty()) {
	                if (supportActionSchema.containsAll(tempSupport)) {
	                    printMessage(tempRule.toString()+": "+tempSupport.toString());
	                    markedRules.add(tempRule);
	                }
                }
            }
            if (markedRules.isEmpty()) {
            	printMessage("None");
            }
        
            // Print Action Rules
    		printMessage(" ");
            printMessage("Action Rules: ");
            for (ArrayList<String> markedRule: markedRules) {
                ActionRules = "";

                for (String attr: certainRule.getKey()) {
                    attributeName = Character.toString(attr.charAt(0));
                    attributeValueFrom = "";
                    attributeValueTo = Character.toString(attr.charAt(1));
                    
                    if (stableAttrs.contains(attributeName)) {
                        markedRule.remove(attributeName+attributeValueTo);
                        ActionRules = formRule(ActionRules, attributeName, "", attributeValueTo, true);
                    }
                    
                    if (flexibleAttrs.contains(attributeName)) {
                    	String removeAttr = "";
                        for (String markedAttr: markedRule) {
                            if (Character.toString(markedAttr.charAt(0)).equals(attributeName)) {
                                attributeValueFrom = Character.toString(markedAttr.charAt(1));
                                removeAttr = markedAttr;
                                break;
                            }
                        }
                        
                        if (!attributeValueFrom.isEmpty()) {
                            markedRule.remove(removeAttr);
                            ActionRules = formRule(ActionRules, attributeName, attributeValueFrom, attributeValueTo, false);
                        }
                        else {
                            ActionRules = formRule(ActionRules, attributeName, "", attributeValueTo, false);
                        }
                    }
                }
                
                printMessage("[" + ActionRules + "] -> (" + decisionAttr + ", " + decisionFromVal + "->" + decisionToVal + ")");
            }
            if (markedRules.isEmpty()) {
            	printMessage("None");
            }
        }
	}
	
	private static String formRule(String rule, String attributeName, String attributeValueFrom, String attributeValueTo, boolean isStable) {
        if(!rule.isEmpty())
            rule += "^";

        if (isStable) {
            rule += attributeName + attributeValueTo;
        }
        else {
            rule += "(" + attributeName + ", ";
            if (!attributeValueFrom.isEmpty())
                rule += attributeName + attributeValueFrom;
            rule += "->" + attributeName + attributeValueTo + ")";
        }

        return rule;
    }

	public static void aras() throws IOException{
		try {
			//Find Certain and Possible rules
			writer = new PrintWriter("ActionRules_output.txt", "UTF-8");
			findRules();
			generateActionRules();
			writer.close();
			
		} catch (Throwable t){
			System.out.println("Fatal error: "+ t.toString());
			t.printStackTrace();
		}
		
		ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "ActionRules_output.txt");
		pb.start();
		
	}
}