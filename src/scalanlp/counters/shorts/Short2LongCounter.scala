// THIS IS AN AUTO-GENERATED CLASS. DO NOT MODIFY.    
// generated by GenCounter on Wed Jul 30 12:05:37 PDT 2008
package scalanlp.counters.shorts;

import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

trait Short2LongCounter extends LongCounter[Short] {


  abstract override def update(k : Short, v : Long) = {

    super.update(k,v);
  }

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : Short, v : Long) :Option[Long] = { val old = get(k); update(k,v); old}

  abstract override def -=(key : Short) = {

    super.-=(key);
  }
  override def default(k : Short) : Long = 0;

  override def apply(k : Short) : Long = super.apply(k);



  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): Short2LongCounter  = super.clone().asInstanceOf[Short2LongCounter]

  override  def argmax() : Short = (elements reduceLeft ((p1:(Short,Long),p2:(Short,Long)) => if (p1._2 > p2._2) p1 else p2))._1
  override  def argmin() : Short = (elements reduceLeft ((p1:(Short,Long),p2:(Short,Long)) => if (p1._2 < p2._2) p1 else p2))._1

  override  def max : Long = values reduceLeft ((p1:Long,p2:Long) => if (p1 > p2) p1 else p2)
  override  def min : Long = values reduceLeft ((p1:Long,p2:Long) => if (p1 < p2) p1 else p2)

  override  def comparator(a : Short, b :Short) = apply(a) compare apply(b);

  override  def normalized : Short2DoubleCounter = {
    val normalized = new HashMap[Short,Double]() with Short2DoubleCounter;
    val total : Double = this.total
    for (pair <- elements) {
      normalized.put(pair._1,pair._2 / total)
    }
    normalized
  }

  override  def l2norm() : Double = {
    var norm = 0.0
    for (val v <- values) {
      norm += (v * v)
    }
    return Math.sqrt(norm)
  }

  override  def topK(k : Int) = Counters.topK[(Short,Long)](k,(x,y) => (x._2-y._2).asInstanceOf[Int])(this);

  def dot(that : Short2LongCounter) : Double = {
    var total = 0.0
    for (val (k,v) <- that.elements) {
      total += get(k).asInstanceOf[Double] * v
    }
    return total
  }

  def +=(that : Short2LongCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) + v).asInstanceOf[Long]);
    }
  }

  override  def +=(that : Iterable[(Short,Long)]) {
    for(val (k,v) <- that) {
      this(k) = (this(k) + v).asInstanceOf[Long];
    }
  }


}


object Short2LongCounter {
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._


  import scala.collection.jcl.MapWrapper;
  def apply() = new MapWrapper[Short,Long] with Short2LongCounter {
    private val under = new Short2LongOpenHashMap;
    def underlying() = under.asInstanceOf[java.util.Map[Short,Long]];
    override def apply(x : Short) = under.get(x);
    override def update(x : Short, v : Long) {
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }
  }

  
}

