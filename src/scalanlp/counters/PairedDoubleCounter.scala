package scalanlp.counters;
import scala.collection.mutable.HashMap;

@serializable
class PairedDoubleCounter[K1,K2] extends HashMap[K1,DoubleCounter[K2]] with Function2[K1,K2,Double] {
  // may be overridden with other counters
  override def default(k1 : K1) :DoubleCounter[K2] = DoubleCounter[K2]();

  override def apply(k1 : K1) = getOrElseUpdate(k1,default(k1));
  def get(k1 : K1, k2 : K2) : Option[Double] = get(k1).flatMap(_.get(k2))
  def apply(k1 : K1, k2: K2) : Double= apply(k1)(k2);
  def update(k1 : K1, k2: K2, v : Double) = apply(k1)(k2) = v;

  def total = map(_._2.total).foldLeft(0.0)(_+_)

  override def toString = {
    val b = new StringBuilder;
    b append "["
    foreach {  x=>
      b append x
      b append ",\n"
    }
    b append "]"
    b.toString

  }

  def transform(f : (K1,K2,Double)=>Double) = foreach {  case (k1,c) => c.transform{ case (k2,v) => f(k1,k2,v)}}

  def +=(that : PairedDoubleCounter[K1,K2]) {
    for(val (k1,c) <- that.elements;
      val (k2,v) <- c.elements) {
      this(k1)(k2) += v;
    }
  }
}
