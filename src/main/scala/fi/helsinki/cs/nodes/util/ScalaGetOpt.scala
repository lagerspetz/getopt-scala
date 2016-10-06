package fi.helsinki.cs.nodes.util

import scala.util.Success
import scala.util.Failure
import scala.util.Try
import scala.util.Either
import scala.reflect.ClassTag

/**
 * Parses command line arguments in a Linux/Unix program standard way.
 * Supports formats:
 * --argument value
 * -a value
 * --argument=value
 * -a=value
 *
 *
 * Always returns argument values as Strings. It is the user's responsibility to handle casting and possible errors.
 * Will return None when an arg is not supplied.
 *
 * Usage e.g.
 *
 * object MyProgram with ScalaGetOpt{
 * def main(args: Array[String]) {
 * // Call getOpt with program arg spec(s)
 * val result = getOpt(args, "f:q", Seq("file=", "boolean"))
 * val presence = result.flatMap(_.toOption)
 * val qWasSet = presence.contains("q" -> "")
 * val boolWasSet = presence.contains("boolean" -> "")
 *
 * val setArgs = presence.map{case (option, value) =>
 * option match {
 * case "file" => println(s"File was set to $value")
 * case "f" => println(s"f was set to $value")
 * case _ => // Already handled by presence checks
 * }
 * }
 *     ...
 *   }
 * }
 *
 * @author Eemil Lagerspetz
 */
trait ScalaGetOpt {

  /**
   * Main entry point. Parses args for options specified in shortOptSpec and longOptSpec.
   * Works in a way similar to https://docs.python.org/3/library/getopt.html
   * Allows = or : to indicate options that require an argument.
   *
   */
  def getOpt(args: Array[String], shortOptSpec: String = "", longOptSpec: Seq[String] = Seq.empty) = {
    val parser = parseOption(args) _

    val optsTakeValues = shortOptSpec.zipWithIndex.filter(x ⇒ !isTakesValueChar(x._1)).map { o ⇒
      val takesValue = (shortOptSpec.length > o._2 + 1 && isTakesValueChar(shortOptSpec.charAt(o._2 + 1)))
      Left(o._1) -> takesValue
    }

    val longTakeValues = longOptSpec.map { lopt ⇒
      var theOption = lopt
      val takesValue = isTakesValueChar(lopt.last)
      if (takesValue)
        theOption = lopt.dropRight(1)
      Right(theOption) -> takesValue
    }

    val allOptions = (optsTakeValues ++ longTakeValues)

    val allParsed = allOptions.flatMap {
      case (opt, takesValue) ⇒
        parser(opt, takesValue)
    }

    allParsed.toMap
  }
  
  /**
   * Simple error message and quit helper.
   */
  def error[T:ClassTag](message:String) = {
    println(message)
    sys.exit(1)
    implicitly[ClassTag[T]].runtimeClass.newInstance.asInstanceOf[T] // To cheat type inference
  }
  
  /**
   * System exit with an error if the given option is not found in the map. 
   */
  def requireOption(setOpts:Map[String, String])(o:String) = {
    setOpts.getOrElse(o, error(s"Option $o must be set."))
  }

  private val takesValueChars = ":="

  private def isTakesValueChar(c: Char) = takesValueChars.contains(c)

  private def parseOption(args: Array[String])(opt: Either[Char, String], takesValue: Boolean) = {
    val (sopt, filterFunction) = {
      if (opt.isLeft)
        ((opt.left.get + ""), shortKeyFilter(opt.left.get) _)
      else
        (opt.right.get, longKeyFilter(opt.right.get) _)
    }

    val foundKeys = args.zipWithIndex.filter { case (a, idx) ⇒ filterFunction(a) }
    val keyValues = foundKeys.flatMap {
      case (key, idx) ⇒
        if (takesValue) {
          if (key.contains("=")) {
            val value = key.substring(key.indexOf("=") + 1)
            Some(sopt -> value)
          }else if (opt.isLeft && key.length() > 1){
            Some(sopt, key.substring(2)) // -aValue
          } else {
            if (args.length > idx + 1) {
              Some(sopt -> args(idx + 1))
            } else
              None
          }
        } else
          Some(sopt -> "")
    }
    // Use last given arg instead of first.
    keyValues.lastOption
  }

  private def longKeyFilter(key: String)(a: String) = a.startsWith(s"--$key")
  private def shortKeyFilter(key: Char)(a: String) = a.startsWith(s"-$key")
}
