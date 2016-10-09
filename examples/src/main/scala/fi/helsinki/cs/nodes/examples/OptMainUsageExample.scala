package fi.helsinki.cs.nodes.examples

import fi.helsinki.cs.nodes.util.OptMain

/**
 * Example of OptMain usage.
 * Usage of this class with maven:
 mvn scala:run -DmainClass=fi.helsinki.cs.nodes.examples.OptMainUsageExample -DaddArgs="-t10000|--regs|someregs|--samples|somesamples|--target|sometarget|--rates|pathy|--load"
 With scala, e.g.:
 scala -cp target/classes:$HOME/.m2/repository/fi/helsinki/cs/nodes/getopt-scala/1.1.0/getopt-scala-1.1.0.jar fi.helsinki.cs.nodes.examples.OptMainUsageExample -t10000 --regs someregs --samples somesamples --target sometarget --rates pathy --load
 With scala repl:
   val args = "-t10000 --regs someregs --samples somesamples --target sometarget --rates pathy --load".split(" ")
   args: Array[String] = Array(-t10000, --regs, someregs, --samples, somesamples, --target, sometarget, --rates, pathy, --load)
   scala> fi.helsinki.cs.nodes.examples.OptMainUsageExample.main(args)
   regs was set to someregs
   samples was set to somesamples
   Optional option rates was set to pathy
   target was set to sometarget
   tasks was set to 10000
   load was given on the command line
   
 *  
 * @copyright University of Helsinki
 * @author Eemil Lagerspetz
 */
object OptMainUsageExample extends OptMain {
  // short option spec, required. use "" for none.
  val shortOptions = "t:"
  
  // Long option spec, required. Use Seq.empty[String] for none.
  val longOptions = Seq("regs=", "samples=", "rates=", "target=", "load")

  /**
   * Required by OptMain. OptMain parses args in actual main() then gives control to this function.
   */
  def optMain() {
    // These are all mandatory, so:
    val regs = mandatoryOption("regs")
    val samples = mandatoryOption("samples")
    val target = mandatoryOption("target")
    // Short option parsing example
    val tasks = mandatoryOption("t").toInt
    
    // Optional arg
    val rates = optional("rates")
    // Boolean arg
    val load = optionSet("load")
    
    optMainUsageExample(regs, samples, rates, target, tasks, load)
  }

  /**
   * Main work function.
   */
  def optMainUsageExample(regs: String, samples: String, rates:Option[String], target: String, tasks: Int, load: Boolean) {
      // TODO: Do your work here.
      println(s"regs was set to $regs")
      println(s"samples was set to $samples")
      rates.map{rv =>
        println(s"Optional option rates was set to $rv")
      }.getOrElse(println(s"rates was not set."))
      println(s"target was set to $target")
      println(s"tasks was set to $tasks")
      if (load)
        println(s"load was given on the command line")
    }
}
