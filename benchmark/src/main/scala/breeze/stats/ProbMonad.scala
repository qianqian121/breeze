package breeze.linalg

import breeze.benchmark._

import breeze.linalg._
import breeze.stats.distributions._
import spire.implicits.cfor

object ProbMonadRunner extends MyRunner(classOf[ProbMonadBenchmark])

class ProbMonadBenchmark extends BreezeBenchmark {

  val f: Double => Double = x => math.exp(-x*x)
  val fm: Double => Rand[Double] = (x => Uniform(min(x,2*x), max(x,2*x)))
  val gaussian = Gaussian(0,1)

  val size = 1024*1024

  def timeMap(reps: Int) = run(reps) {
    val mg = gaussian.map(f)
    mg.samplesVector(size)
  }

  def timeFlatMap(reps: Int) = run(reps) {
    val mg = gaussian.flatMap(fm)
    mg.samplesVector(size)
  }

  def timeCondition(reps: Int) = run(reps) {
    val mg = gaussian.condition(x => x > 0)
    mg.samplesVector(size)
  }

  def timeRepeatCondition(reps: Int) = run(reps) {
    val mg = gaussian.condition(x => x > 0).condition(x => x < 1).condition(x => x > -1)
    mg.samplesVector(size)
  }

}
