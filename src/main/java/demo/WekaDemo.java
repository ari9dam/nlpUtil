/**WekaDemo.java
 * 5:16:22 PM @author Arindam
 */
package demo;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Arindam
 *
 */
public class WekaDemo {
	public static void main (String args[]) throws Exception{
		// Declare two numeric attributes
		 Attribute Attribute1 = new Attribute("firstNumeric");
		 Attribute Attribute2 = new Attribute("secondNumeric");
		 
		 // Declare a nominal attribute along with its values
		 FastVector fvNominalVal = new FastVector (3);
		 fvNominalVal.addElement("blue");
		 fvNominalVal.addElement("gray");
		 fvNominalVal.addElement("black");
		 Attribute Attribute3 = new Attribute("aNominal", fvNominalVal);
		 
		 // Declare the class attribute along with its values
		 FastVector fvClassVal = new FastVector(2);
		 fvClassVal.addElement("positive");
		 fvClassVal.addElement("negative");
		 Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
		 
		 // Declare the feature vector
		 FastVector fvWekaAttributes = new FastVector(4);
		 fvWekaAttributes.addElement(Attribute1);
		 fvWekaAttributes.addElement(Attribute2);
		 fvWekaAttributes.addElement(Attribute3);
		 fvWekaAttributes.addElement(ClassAttribute);
		 
		// Create an empty training set
		 Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
		 // Set class index
		 isTrainingSet.setClassIndex(3);
		 
		// Create the instance
		 Instance iExample = new DenseInstance(4);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), 1.0);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), 0.5);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), "gray");
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), "positive");
		 
		 // add the instance
		 isTrainingSet.add(iExample);
		 
		// Create a naive bayes classifier
		 Classifier cModel = (Classifier)new NaiveBayes();
		 cModel.buildClassifier(isTrainingSet);
		 
		// Test the model
		 Evaluation eTest = new Evaluation(isTrainingSet);
		 eTest.evaluateModel(cModel, isTrainingSet);
		
		 // Print the result à la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		 
		 // Get the confusion matrix
		 double[][] cmMatrix = eTest.confusionMatrix();
		 
	}}