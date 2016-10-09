package fi.helsinki.cs.nodes.examples

import fi.helsinki.cs.nodes.util.OptMain

/**
 * Example of OptMain usage.
 * Usage of this class:
 * fi.helsinki.cs.nodes.examples.OptMainUsageExample -t10000 --regs someregs --samples somesamples --target sometarget --rates pathy --load
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
  def optMainUsageExample(regs: String, singlesamples: String, rates:Option[String], target: String, tasks: Int, load: Boolean) {
    // TODO: Do your work here.
    }
}
