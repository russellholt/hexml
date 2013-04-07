import org.webcycle.hexml.*;

/**
 * A demo that parses ppy.xml, prints what it parses, and prints the Vocabulary.
 */
public class hexmltest
{

	public final static void main(String argv[])
	{
		try {
			
			Vocabulary vocab = new Vocabulary();
			vocab.insertEntry("!--", "org.webcycle.hexml.HexmlComment");

			HFElementVector entities = new HFElementVector();
			entities.acceptUnrecognizedText = false;

			ParseContextImpl pcontext = new ParseContextImpl(entities, vocab);

			Parser p = new Parser();

			// do the parse
			p.load(pcontext, "ppy.xml");

			// print the resulting entities
			System.err.println(entities.toString());

			
			System.err.println("---------- hexmltest.main: done -----------");
			System.err.println("Vocabulary:");
			
			System.err.println(vocab.toString());
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}