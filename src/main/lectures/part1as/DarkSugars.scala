package main.lectures.part1as
import scala.util.Try

object DarkSugars extends App {

  // syntax 1: Methods with single para
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod {
    //write some code
    42
  }

  // This is just like the try method
  val aTryInstance = Try {
    throw new RuntimeException
  }

  // Or the following with the list map method
  List(1,2,3).map { x =>
    x + 1
  }

  //syntax sugar #2: single abstract method
  trait Action {
    def act(i: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(i: Int): Int = i + 1
  }

  val anInstanceSingleAbstraction: Action = (i: Int) => i + 1 //magic

  //example: Runnable
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello scala")
  })

  val aSweeterThread = new Thread(() => println("hello sweeter scala"))

  //Works for classes with one method not implemented
  abstract class AnAbstractType {
    def implented: Int = 23
    def f(a: Int): Unit
  }

  // Implements the f method (only method not implemented).
  val anAbstractInstance: AnAbstractType = {
    (a: Int) => println(s"$a, noice")
  }

  //Syntax #3: the :: (prepend for seq) and the #::(prepend for stream) methods are special (right associativity)
  val prependedList = 2 :: List(2,3)
  // This is NOT coverted to 2.::List(2,3)
  // Is converted to List(2,3).::(2)
  // When a method ends in ":" it is right associative.
  // Lets look at this!

  class MyStream[T]{
    def -->:(value: T): MyStream[T] = this
  }

  val myStream = 1 -->: 2 -->: 3-->: new MyStream[Int]

  //Syntax sugar #4: multiwork method namign

  class TeenGirl (name: String) {
    def `and then said` (gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("lilly")
  lilly `and then said` "Scala is so sweet!"

  //syntax sugar #5: infix types

  class Composite[A,B]
  val composite: Composite[Int, String] = ???
  //can also do infix
  val composite2: Int Composite String = ???

  class -->[A,B]

  val towards: Int --> String = ???

  //Syntax Sugar #6: The update() method (special like apply())
  val anArray = Array(1,2,3)
  anArray(2) = 7
  //Gets rewritted to an anArray.update(2,7)
  // Used in mutable collections

  // Syntax sugar #7: Setters for mutable containers
  class Mutable {
    private var internalMember:Int =0 //private for 00 encapsulation
    def member: Int = internalMember // getter
    def member_=(value: Int): Unit = {
      internalMember = value //setter
    }
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42
  // Compiler rewrites as aMutableContainer.member_=(42)
  // Only works if you declare a getter and setter called member!



}
