package fi.helsinki.cs.nodes.util


/**
 * @copyright University of Helsinki
 * @author Eemil Lagerspetz
 */
trait OptMain extends ScalaGetOpt {

  /**
   * Short options in getopt format, e.g.
   * a:bcde:f
   * or a=bcde:f
   * for usage like
   * -a arg -b -c -e evalue -f
   * or
   * -d -eValueForE -a=valuefora -c
   */
  val shortOptions: String

  /**
   * Long options in getopt format, e.g.
   * one, two, three=, four=
   * one, two, three: four:
   * for usage like
   * --one --two --three tvalue --four=fvalue
   * --three tvalue --four=fvalue --one
   */
  val longOptions: Seq[String]
  
  var setOpts:Option[Map[String, String]] = None

  /**
   * Main entry point. Parses args for options specified in shortOptSpec and longOptSpec.
   * Option values can be accessed with helpers optionSet, optional, and mandatory.
   * Works in a way similar to [[https://docs.python.org/3/library/getopt.html]]
   * Allows = or : to indicate options that require an argument, see [[fi.helsinki.cs.nodes.util.OptMain#shortOptions]] and [[fi.helsinki.cs.nodes.util.OptMain#longOptions]]
   */
  def optMain()

  /**
   * Main entry point. Parses args, then passes control to [[optMain]] .
   */
  final def main(args: Array[String]) {
    setOpts = Some(getOpt(args, shortOptions, longOptions))
    optMain()
  }
  
  /**
   * Require option o to be set and return its value, or sys.exit(1) in case that fails.
   */
  def mandatoryOption(o:String) = {
    setOpts match {
      case None => error("Options have not been parsed yet.")
      case Some(optionsMap) => requireOption(optionsMap)(o) 
    }
  }

  /**
   * Return an Option[String] representing the set status and value of option o.
   */
  def optional(o:String) = {
    setOpts.getOrElse(error("Options have not been parsed yet.")).get(o)
  }
  
  /**
   * Return true of option o was set.
   */
  def optionSet(o:String) = {
    setOpts.getOrElse(error("Options have not been parsed yet.")).contains(o)
  }
}
