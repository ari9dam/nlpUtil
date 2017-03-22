/**NER.java
 * 3:55:22 PM @author Arindam
 */
package edu.asu.nlu.common.parsing;

import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * @author Arindam
 *
 */
public class NER {
	private AbstractSequenceClassifier<CoreLabel> classifier;
	private String serializedClassifier = "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz"; 
	public NER() throws ClassCastException, ClassNotFoundException, IOException{
		this.classifier = CRFClassifier.getClassifier(serializedClassifier);
	}

	public List<CoreLabel> classify(String str) throws IOException{

		List<List<CoreLabel>> out = classifier.classify(str);
		return out.get(0);
	}
}