package main.lectures.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)

  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
//    case head::(Nil) => println(s"The only element is $head") (this is equivalent)
    case _ => None
  }

  /*
  The things we can do matches on:
  - contants
  - wildcards
  - case classes
  - tuples
  - some special magic (like above)
   */

  class Person(val name: String, val age: Int) // for some reason can't make a case class. How can we do pattern match?
  // Make a companion object with unapply method with following pattern
  // This is what compiler looks for with pattern matching.
  // Object can actually be named whatever, but convention is as a companion object.
  object Person {
    def unapply(person: Person): Option[(String, Int)] = {
      // Won't work if person age < 21 in patter match
      if (person.age < 21) None
      else Some(person.name, person.age)
    }
    // We can OVERLOAD the unapply method with other methods
    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob  = new Person("bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi my name is $n, and I am $a years old"
    case _ => println("failed!")
  }

  println(greeting)

 // Usig the second overloaded method.
  val legalStatus = bob.age match {
    case Person(status) => s"My Legal Status is $status"
  }
  println(legalStatus)

  /* Exercise
  Create a pattern match against integer conditions like the following:
  val  n: Int = 45
  val mathProperty: String = n match {
    case x if x < 10 => "Single Digit"
    case x if x % 2 == 0 => "an even nunber"
    case _ => "no property"
   */

  // Convention is to use lowercase.
  object even {
    def unapply(arg: Int): Option[Boolean] = {
      if (arg % 2 == 0) Some(true)
      else None
    }
  }
  //Alternative way of writing without Option. Look at the match statement to see lack of "_". Still has to return boolean.
  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n: Int = 8

  val mathProperty: String = n match {
    // Doesn't need _
    case singleDigit() => "single digit"
    // Needs _ because of way object is written.
    case even(_) => "even"
    case _ => "no property"
  }

  println(mathProperty)

  // Infix patterns for pattern matching (only work with 2 things in pattern)
  case class Or[A,B](a: A, b: B)

  val either = Or[Int, String](2, "two")
  val humanDescription: String  = either match {
    // case Or(number, string) => s"$number is written as $string" (normal way to write)
    case number Or string => s"$number is written as $string" // Infix way to write
  }
  println(humanDescription)

  //decomposing sequences
  //To do this we need an unapplySeq method.
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }
  println(vararg)

//  Lets make our own class that does this.
  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    //Compiler looks for this if there is a _* in the pattern match case.
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))

  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1,2"
    case _ => "something else"
  }
  println(decomposed)

  // Custom return types for unapply. Return type only needs isEmpty: Boolean, and get: Something
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    // Shows that return type for unapply doesn't need to be Option. It can be anything with isEmpty and get implemented.
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get: String = person.name
    }
  }

  println( bob match {
    case PersonWrapper(n) => s"This persons name is $n"
    case _ => "an alien"
  })


}
