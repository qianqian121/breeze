// THIS IS AN AUTO-GENERATED CLASS. DO NOT MODIFY.    
// generated by GenCounter on Wed Jul 30 12:05:37 PDT 2008
package scalanlp.counters;

import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

trait FloatCounter[T] extends Map[T,Float] {

  private var pTotal: Float = 0;

  def total() = pTotal;

  final protected def updateTotal(delta : Float) {
    pTotal += delta;
  }

  override def clear() {
    pTotal = 0;
    super.clear();
  }


  abstract override def update(k : T, v : Float) = {
    updateTotal(v - this(k))
    super.update(k,v);
  }

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : T, v : Float) :Option[Float] = { val old = get(k); update(k,v); old}

  abstract override def -=(key : T) = {

    updateTotal(-this(key))

    super.-=(key);
  }
  override def default(k : T) : Float = 0;

  override def apply(k : T) : Float = super.apply(k);



  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): FloatCounter[T]  = super.clone().asInstanceOf[FloatCounter[T]]

   def argmax() : T = (elements reduceLeft ((p1:(T,Float),p2:(T,Float)) => if (p1._2 > p2._2) p1 else p2))._1
   def argmin() : T = (elements reduceLeft ((p1:(T,Float),p2:(T,Float)) => if (p1._2 < p2._2) p1 else p2))._1

   def max : Float = values reduceLeft ((p1:Float,p2:Float) => if (p1 > p2) p1 else p2)
   def min : Float = values reduceLeft ((p1:Float,p2:Float) => if (p1 < p2) p1 else p2)

   def comparator(a : T, b :T) = apply(a) compare apply(b);

   def normalized : DoubleCounter[T] = {
    val normalized = new HashMap[T,Double]() with DoubleCounter[T];
    val total : Double = this.total
    for (pair <- elements) {
      normalized.put(pair._1,pair._2 / total)
    }
    normalized
  }

   def l2norm() : Double = {
    var norm = 0.0
    for (val v <- values) {
      norm += (v * v)
    }
    return Math.sqrt(norm)
  }

   def topK(k : Int) = Counters.topK[(T,Float)](k,(x,y) => (x._2-y._2).asInstanceOf[Int])(this);

  def dot(that : FloatCounter[T]) : Double = {
    var total = 0.0
    for (val (k,v) <- that.elements) {
      total += get(k).asInstanceOf[Double] * v
    }
    return total
  }

  def +=(that : FloatCounter[T]) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) + v).asInstanceOf[Float]);
    }
  }

   def +=(that : Iterable[(T,Float)]) {
    for(val (k,v) <- that) {
      this(k) = (this(k) + v).asInstanceOf[Float];
    }
  }


}


object FloatCounter {
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._

  import scalanlp.counters.ints._
  import scalanlp.counters.shorts._
  import scalanlp.counters.longs._
  import scalanlp.counters.floats._
  import scalanlp.counters.doubles._


  import scala.collection.jcl.MapWrapper;
  def apply[T]() = new MapWrapper[T,Float] with FloatCounter[T] {
    private val under = new Object2FloatOpenHashMap[T];
    def underlying() = under.asInstanceOf[java.util.Map[T,Float]];
    override def apply(x : T) = under.getFloat(x);
    override def update(x : T, v : Float) {
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }
  }

  
  private def runtimeClass[T](x : Any) = x.asInstanceOf[AnyRef].getClass
  def apply[T](x : T) : FloatCounter[T] = {
    val INT = runtimeClass(3);
    val LNG = runtimeClass(3l);
    val FLT = runtimeClass(3.0f);
    val SHR = runtimeClass(3.asInstanceOf[Short]);
    val DBL = runtimeClass(3.0);
    runtimeClass(x) match {
      case INT => Int2FloatCounter().asInstanceOf[FloatCounter[T]];
      case DBL => Double2FloatCounter().asInstanceOf[FloatCounter[T]];
      case FLT => Float2FloatCounter().asInstanceOf[FloatCounter[T]];
      case SHR => Short2FloatCounter().asInstanceOf[FloatCounter[T]];
      case LNG => Long2FloatCounter().asInstanceOf[FloatCounter[T]];
      case _ => FloatCounter().asInstanceOf[FloatCounter[T]];
    }
  }
      
}

