Welcome to the Java-Chess project!

As you might know, we aim at creating a fully functional chess program written
in Java 2.
You can always get the latest results of our efforts at
http://www.java-chess.de
If you are interested in the topic and have any skills in the field, you should 
not hesitate to join our little project (see the mail addies at the contact-page
of the site).

How to run Java-Chess:
======================

To run this program, you'll definitely need a installed Java Runtime Environment
(JRE) or Java Development Kit (JDK). If you don't have it already, you can
get the latest version at
http://java.sun.com
or (for Linux) at
http://www.blackdown.org
. If your Java-Chess download includes a jar-file in the build directory or you 
have already compiled our application, you should be able to start it simply 
by entering something like
'java  -jar javaChess.jar'  (without the quotes!)
In some cases (depending on your settings), it might be useful to
increase the heapsize by adding a argument like
'java -Xmx64m -jar javaChess.jar'
to the call. If that doesn't work for some reason, you can also start it
by adding the jar file to the classpath and giving the main class as a
commandline argument
'java -classpath javaChess.jar:$CLASSPATH de.java_chess.javaChess.JavaChess'

How to compile Java-Chess:
==========================

To compile the project, you should have the Jakarta-Ant build system
installed, that you can get at
http://jakarta.apache.org/ant/index.html
. You should find detailed installation instructions in the package,
but one important point is to set the ANT_HOME variable to the
installation directory.
At the moment, you'll need 2 libraries in the lib/ directory to
fully compile and test Java-chess.
One is antlrall.jar , the complete Antlr lib, that we use to generate
a PGN parser from a PGN-Grammar (see the pgn.g file). Optionally, you
can also include your Antlr installation directory into the classpath.
The next lib is the junit.jar , the JUnit testing framework, which you
can download at junit.org .

If you have the libs installed, you should go to the src/ directory
and enter 'ant' to get the help screen. 
If you don't have the parser generated, you should start with
'ant generateparser' to generate the required java files in the 
pgn/ subdirectory. 
Next step should be the compilation and generation of the jar-File
by entering 'ant package'. If you don't see any error messages,
you should find a jar file now in the build directory.

At this point, you can also run the included tests to do a first
rough check, if you can expect a working app with your settings.
To run them, simply type 'ant test' in the src/ directory and
you'll hopefully see a green bar, indicating that none of the
tests failed. If you see a red bar, please report the problem
to us!


If you have any further questions, do not hesitate to contact me,

Andreas Rueckert <mail@andreas-rueckert.de>