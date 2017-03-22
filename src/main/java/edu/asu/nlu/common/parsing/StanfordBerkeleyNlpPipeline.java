/**StanfordBerkeleyNlpPipeline.java
 * 5:58:28 AM @author Arindam
 */
package edu.asu.nlu.common.parsing;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Arindam
 *
 */
public class StanfordBerkeleyNlpPipeline {
	private StanfordCoreNLP pipeline;
	public StanfordBerkeleyNlpPipeline(){
		this(false);
	}

	public StanfordBerkeleyNlpPipeline(List<String> annotators ){
		String an = "";
		for(String t: annotators){
			an+=t+",";
		}
		Properties props = new Properties();
		props.setProperty("annotators", an.substring(0, an.lastIndexOf(",")));
		props.put("pos.model","edu/stanford/nlp/models/pos-tagger/english-bidirectional/english-bidirectional-distsim.tagger");
		props.put("parse.type","charniak");
		props.put("parse.executable","src/main/resources/berkeley.bat");
		props.put("parse.model","");
		pipeline = new StanfordCoreNLP(props);
	}

	public StanfordBerkeleyNlpPipeline(Boolean isStanfordOnly){
		Properties props = new Properties();
		if(!isStanfordOnly){
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
			props.put("pos.model","edu/stanford/nlp/models/pos-tagger/english-bidirectional/english-bidirectional-distsim.tagger");
			props.put("parse.type","charniak");
			props.put("parse.executable","src/main/resources/berkeley.bat");
			props.put("parse.model","");
			pipeline = new StanfordCoreNLP(props);
		}else{
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
			pipeline = new StanfordCoreNLP(props);
		}
	}
	public StanfordCoreNLP getPipeline(){
		return this.pipeline;
	}
}
