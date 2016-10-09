# getopt-scala
GNU GetOpt-style command line argument parsing for Scala.

## Usage
The easiest way to use getopt-scala is through having your main class extend `fi.helsinki.cs.nodes.util.OptMain`.
You need to define `val shortOptions:String`, `val longOptions:Seq[String]`, so that OptMain knows which options your program takes. The syntax of these is inspired by the Python getopt module: https://docs.python.org/3/library/getopt.html .
You must not define a main() function. This is used by OptMain to parse the command line arguments. You need to define optMain(), which will be called from OptMain.main after parsing the command line arguments. You can then use these functions:

- `mandatoryOption(String): String`, requires the given option to be present on the command line and returns its value.
-`optional(String):Option[String]`, returns the value of the given option if present on the command line. Otherwise returns `None`.
-`optionSet(String):Boolean`,returns true if the given on/off flag was present on the command line.

See https://github.com/lagerspetz/getopt-scala/blob/master/examples/src/main/scala/fi/helsinki/cs/nodes/examples/OptMainUsageExample.scala for a complete example.

