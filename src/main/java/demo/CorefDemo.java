/**CorefDemo.java
 * 3:32:51 PM @author Arindam
 */
package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.asu.nlu.common.ds.AnnotatedSentence;
import edu.asu.nlu.common.parsing.StanfordBerkeleyNlpPipeline;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Arindam
 *
 */
public class CorefDemo {
	public static void main(String args[]){
		StanfordCoreNLP pipeline = (new StanfordBerkeleyNlpPipeline(true)).getPipeline();
		String text = "His dad gave him 39 nickels. ";
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys 
		//and has values with custom types
		List<CoreMap> sens = document.get(SentencesAnnotation.class);

		ArrayList<AnnotatedSentence> sentences = new ArrayList<AnnotatedSentence>();
		for(CoreMap sentence: sens){
			sentences.add(new AnnotatedSentence(sentence.get(TextAnnotation.class),sentence));
		}
		Map<Integer, CorefChain> corefChain = document.get(CorefChainAnnotation.class);
		document.set(CorefChainAnnotation.class, corefChain);
		for(AnnotatedSentence sen: sentences){
			System.out.println(sen.getDependencyGraph());
		}
	}
}
