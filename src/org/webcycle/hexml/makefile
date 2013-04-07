#
# simple makefile for hexml
# doesn't understand .java -> .class dependencies, 'cause javac is fast enough.
#

SOURCE = BasicAtom.java BasicContainer.java HFAttributeTable.java HFElementVector.java HexmlComment.java HexmlElements.java HexmlObject.java HexmlString.java ParseContextImpl.java Parser.java VocabEntry.java Vocabulary.java hexml.java iCollection.java iHexmlAttributes.java iHexmlFeature.java iHexmlObject.java iParseContext.java

hexml:
	javac $(SOURCE) -d $(CLASSES_DIR)

