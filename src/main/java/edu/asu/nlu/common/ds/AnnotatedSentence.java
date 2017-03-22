package edu.asu.nlu.common.ds;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.asu.nlu.common.util.POSUtil;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Arindam
 * A POJO that stores a structurally annotated sentence
 */
public class AnnotatedSentence {
	private String sentence;
	private Map<Integer,CoreLabel> tokens;
	private List<CoreLabel> tokenSequence;
	private CoreMap tSentence;
	private Tree tree;
	private SemanticGraph dependencyGraph;

	public AnnotatedSentence(String sentence, CoreMap tsentence){
		this.sentence = sentence;
		this.tokenSequence = tsentence.get(TokensAnnotation.class);
		this.tSentence = tsentence;

		this.tokens = new HashMap<Integer,CoreLabel>();
		for(CoreLabel label: this.tokenSequence){
			this.tokens.put(label.index(), label);
		}

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

		// this is the parse tree of the current sentence
		this.tree = tsentence.get(TreeAnnotation.class);
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

		// this is the Stanford dependency graph of the current sentence
		this.dependencyGraph = new SemanticGraph(tdl);
		//System.out.println(this.dependencyGraph);
	}

	public String getLemma(int index){
		CoreLabel token = this.tokens.get(index);
		if(token==null)
			throw new IllegalArgumentException(
					String.format("Token with Id %s is missing",index));
		return token.lemma();
	}

	public String getPOS(int index){
		CoreLabel token = this.tokens.get(index);
		if(token==null)
			throw new IllegalArgumentException(
					String.format("Token with Id %s is missing",index));
		return token.get(PartOfSpeechAnnotation.class);
	}

	public String getNER(int index){
		CoreLabel token = this.tokens.get(index);
		if(token==null)
			throw new IllegalArgumentException(
					String.format("Token with Id %s is missing",index));
		return token.get(NamedEntityTagAnnotation.class);
	}

	public String getWord(int index){
		CoreLabel token = this.tokens.get(index);
		if(token==null)
			throw new IllegalArgumentException(
					String.format("Token with Id %s is missing",index));
		return token.get(TextAnnotation.class);
	}

	public String getWord(CoreLabel token){
		return token.get(TextAnnotation.class);
	}

	public String getRawSentence(){
		return this.sentence;
	}

	public CoreMap getTaggedSentence(){
		return this.tSentence;
	}

	public Tense getTense(int index){
		String pos = this.getPOS(index);
		// return Tense.Null for non-verb words
		if(!pos.toLowerCase().startsWith("v"))
			return Tense.NULL;
		
		Set<IndexedWord> childs = dependencyGraph.getChildrenWithReln(dependencyGraph.getNodeByIndex(index), 
				GrammaticalRelation .valueOf("auxpass"));
		for( IndexedWord id: childs){
			if(this.getWord(id.index()).equalsIgnoreCase("are")||
					this.getWord(id.index()).equalsIgnoreCase("is")||
					this.getWord(id.index()).equalsIgnoreCase("has")
					||this.getWord(id.index()).equalsIgnoreCase("have")){
				return Tense.PRESENT;
			}
		}
		
		if(pos.equalsIgnoreCase("VBD")||pos.equalsIgnoreCase("VBN"))
			return Tense.PAST;

		Set<IndexedWord> child = dependencyGraph.getChildrenWithReln(dependencyGraph.getNodeByIndex(index), 
				GrammaticalRelation .valueOf("aux"));
		for( IndexedWord id: child){
			if(this.getWord(id.index()).equalsIgnoreCase("will")){
				return Tense.FUTURE;
			}
		}

		return Tense.PRESENT;
	}

	/**
	 * @return the tokenSequence
	 */
	public List<CoreLabel> getTokenSequence() {
		return tokenSequence;
	}

	/**
	 * @param token
	 * @return POS tag
	 */
	public String getPOS(CoreLabel token) {
		return token.get(PartOfSpeechAnnotation.class);
	}

	/**
	 * @param token
	 * @return Lemma
	 */
	public String getLemma(CoreLabel token) {
		return this.getLemma(token.index());
	}

	/**
	 * @param tId
	 * @return
	 */
	public CoreLabel getToken(int tId) {		
		return this.tokens.get(tId);
	}

	public boolean hasToken(int id){
		return this.tokens.containsKey(id);
	}

	/**
	 * @return the dependencyGraph
	 */
	public SemanticGraph getDependencyGraph() {
		return dependencyGraph;
	}

	/**
	 * @param dependencyGraph the dependencyGraph to set
	 */
	public void setDependencyGraph(SemanticGraph dependencyGraph) {
		this.dependencyGraph = dependencyGraph;
	}

	/**
	 * @param label
	 * @return
	 */
	public String getFullLemma(CoreLabel label) {
		if(!POSUtil.isVerb(this.getPOS(label)))
			return this.getLemma(label);
		String lemma = label.lemma();
		try{
			Set<IndexedWord> child = this.getDependencyGraph().
					getChildrenWithReln(this.getDependencyGraph()
							.getNodeByIndex(label.index()),
							GrammaticalRelation.valueOf("prt"));

			for(IndexedWord id: child){
				lemma+=" "+ this.getLemma(id.index());
				
			}
			
			
		}catch(Exception e){}
		return lemma;
	}
}
