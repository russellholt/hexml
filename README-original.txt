README * hexml 1.0
July 11, 2000
russell.holt@destiny.com

hexml is a small system for parsing and streaming XML in java.  It is
released under the GNU Public License, a copy of which can be found in
COPYING.html.  The hexml documentation can be found in docs/index.html.

hexml is the package org.webcycle.hexml.  It uses David Megginson's Trie
class, which is CA.carleton.freenet.ak117.util.Trie, and which is included
in this distribution.

For convenience, I have included two jar files, one compiled with JDK 1.2,
and one compiled with JDK 1.1.7.

CONTENTS

    COPYING          - a copy of the GNU Public License
    classes          - compiled .class files
    demo             - a test app. will be more interesting next release
    docs             - javadoc generated files (jdk 1.2's javadoc)
    hexml.html       - overview of hexml
    hexml-jdk117.jar - hexml, built with JDK 1.1.7 on a Mac
    hexml-jdk12.jar  - hexml, built with JDK 1.2 in Red Hat Linux
    makefile         - Unix makefile
    src              - source files
    README           - this file


DEMO

In the 'demo' directory, try ./run.sh, which is just:

    java -classpath "./:../hexml-jdk117.jar" hexmltest

You should get:

<!-- Doubleclick Ad BEGIN --><!-- DoubleClick Ad END --><!-- DoubleClick Sponsor Label BEGIN --><!--
 DoubleClick Sponsor Tag Middle --><!-- DoubleClick Sponsor Tag END --><!-- DoubleClick Ad BEGIN -->
<!-- DoubleClick Ad END -->
---------- hexmltest.main: done -----------
Vocabulary:
!-- -> class org.webcycle.hexml.HexmlComment

This example prints the comments from an HTML file. After that, it dumps
its vocabulary, which is very small; it says that the tag !-- is mapped
to the class org.webcycle.hexml.HexmlComment.  This is not a very
extensive demo, but the destiny HTML template system which I'll be
releasing soon is a very useful application for hexml. In addition, a
simple command line tool is planned that makes such tasks as the
'extract comments' more straightforward (no custom java code needed).


BUILDING

Unix

if you have the JDK tools in your classpath, the makefile should be
sufficient.  GNU make is expected.  Unless you are developing hexml, you
probably won't need to rebuild the documentation.  Just try `make'.
If that doesn't work, it's simple enough to:

    cd src/org/webcycle/hexml
    javac -classpath <install_dir>/src -d <install_dir>/classes *.java

Windows

I don't have a clue about Windows. Yes! Thank you very much. ;-)

Mac

I actually developed this on a G3 laptop using MPW, but not enough to learn
its obscure Make syntax and wacky non-ASCII characters (having done much more
Unix development) so you're on your own.


HISTORY

Hexml was first written in 1998, simultaneously in C++ and java, as part of
a rewrite of an HTML template system at Destiny (soon to be released as well).
I intended to replace the parser with a real XML parser like Xerces, but had a
lot of trouble due to the fact that I wanted to parse incorrect XML syntax,
or rather, correct syntax *embedded* in highly incorrect stuff!

I recently began restructuring it, pulled the parser out for better
modularization, and will re-release the template system soon.


russell.holt@destiny.com

