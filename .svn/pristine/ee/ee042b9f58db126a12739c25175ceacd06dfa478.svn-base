/**Lemmatizer.java
 * 4:19:27 PM @author Arindam
 */
package edu.asu.nlu.common.parsing;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Arindam
 *
 */
public class Lemmatizer {
	private StanfordCoreNLP lemmatizer = null;
	
	public Lemmatizer(){
		Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        this.lemmatizer = new StanfordCoreNLP(props, false);   
	}
	
	public List<CoreMap> run(String text){
        Annotation document = this.lemmatizer.process(text);  
        return document.get(SentencesAnnotation.class);
        /*
        for(CoreMap sentence: document.get(SentencesAnnotation.class))
        {    
            for(CoreLabel token: sentence.get(TokensAnnotation.class))
            {       
                String word = token.get(TextAnnotation.class);      
                String lemma = token.get(LemmaAnnotation.class); 
                System.out.println("lemmatized version :" + lemma);
            }
        }
        */
	}
}
