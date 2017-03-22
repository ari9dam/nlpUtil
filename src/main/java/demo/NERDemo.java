/**NERDemo.java
 * 5:08:49 PM @author Arindam
 */
package demo;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import edu.asu.nlu.common.parsing.StanfordBerkeleyNlpPipeline;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Arindam
 * This class shows how to use standard or custom regex based Named Entity Tagger
 */
public class NERDemo {

	public static void main(String args[]){
		/*Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse,");
		//props.put("regexner.mapping", "src/main/resources/puzzle-regxner.txt");
		props.put("pos.model","edu/stanford/nlp/models/pos-tagger/english-bidirectional/english-bidirectional-distsim.tagger");
		props.put("parse.type","charniak");
		props.put("parse.executable","C:/Users/Arindam/Downloads/berkeley.bat");
		props.put("parse.model","");*/

		//StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		StanfordCoreNLP pipeline = (new StanfordBerkeleyNlpPipeline(false)).getPipeline();
		Scanner in =new Scanner(System.in);
		while(true){
			System.out.print("Enter :");
			// read some text in the text variable
			String text = in.nextLine();

			if(text.equalsIgnoreCase("-1"))
				break;

			// create an empty Annotation just with the given text
			Annotation document = new Annotation(text);

			// run all Annotators on this text
			pipeline.annotate(document);

			// these are all the sentences in this document
			// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);

			for(CoreMap sentence: sentences) {
				// traversing the words in the current sentence
				// a CoreLabel is a CoreMap with additional token-specific methods
				for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
					// this is the text of the token
					String word = token.get(TextAnnotation.class);
					// this is the POS tag of the token
					String pos = token.get(PartOfSpeechAnnotation.class);
					// this is the NER label of the token
					String ne = token.get(NamedEntityTagAnnotation.class);
					String lemma = token.lemma();

					System.out.println("<"+word+"-"+token.index()+","+pos+","+ne+","+lemma+">");
				}
				TreebankLanguagePack tlp = new PennTreebankLanguagePack();
				GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();


				// this is the parse tree of the current sentence
				Tree tree = sentence.get(TreeAnnotation.class);
				GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
				Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

				// this is the Stanford dependency graph of the current sentence
				SemanticGraph dependencies = new SemanticGraph(tdl);
				System.out.println(dependencies.toList());
				System.out.println("###typedDependenciesCollapsed(true)");
				System.out.println(gs.typedDependenciesCollapsed(true));
				/*
				System.out.println("##typedDependencies()");
				System.out.println(gs.typedDependencies());
				
				System.out.println("###typedDependenciesCCprocessed(true)");
				System.out.println(gs.typedDependenciesCCprocessed(true));
				
				System.out.println("###typedDependenciesCollapsedTree()");
				System.out.println(gs.typedDependenciesCollapsedTree());
				
				System.out.println("###typedDependencies(true)");
				System.out.println(gs.typedDependencies(true));
				*/
			}

			/* 
	    MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
	    DependencyParser parser = DependencyParser.loadFromModelFile("edu/stanford/nlp/models/parser/nndep/PTB_Stanford_params.txt.gz");

	    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
	    for (List<HasWord> sentence : tokenizer) {
	      List<TaggedWord> tagged = tagger.tagSentence(sentence);
	      GrammaticalStructure gs = parser.predict(tagged);

	      // Print typed dependencies
	      System.err.println(gs);
	    }
	*/
			// This is the coreference link graph
			// Each chain stores a set of mentions that link to each other,
			// along with a method for getting the most representative mention
			// Both sentence and token offsets start at 1!
			/*Map<Integer, CorefChain> graph = 
					document.get(CorefChainAnnotation.class);
			System.out.println(graph); 
			*/
		}
		in.close();
	}
}
