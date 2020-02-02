package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  /*
  Exercise: Implement a functional Set
   */

  def apply(elem: A): Boolean = {
    contains(elem)
  }

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  /*
  Excersise 2:
  1. Remove an element
  2. Intersection with another set
  3. Difference with another set.
   */

  def -(elem: A): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A]

  /*
  Excersise 3:
  1. Implement a unary_! method.
   */

  def unary_! : MySet[A]

}

// Companion object to build the object.
object MySet {
  def apply[A](values: A*): MySet[A] = {

    /*
    How does this work?
    val s = MySet(1,2,3) = buildSet(Seq(1,2,3), [])
    = buildSet(Seq(2,3), [1])
    = buildSet(Seq(3), [1,2])
    = buildSet(Seq(), [1,2,3])
    = [1,2,3]
     */
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A]= {
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    }
    buildSet(values.toSeq, new EmptySet[A])
  }
}

class EmptySet[A] extends MySet[A] {

  def contains(elem: A): Boolean =  false

  def +(elem: A): MySet[A] = new NonEmptySet(elem, this)

  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  def filter(predicate: A => Boolean): MySet[A] = this

  def foreach(f: A => Unit): Unit = ()

  def -(elem: A): MySet[A] = this

  def &(anotherSet: MySet[A]): MySet[A] = this

  def --(anotherSet: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] = new AllInclusiveSet[A]
}

class AllInclusiveSet[A] extends MySet[A] {
  def contains(elem: A): Boolean =  true
  def +(elem: A): MySet[A] = this
  def ++(anotherSet: MySet[A]): MySet[A] = this
  def map[B](f: A => B): MySet[B] = new EmptySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  def filter(predicate: A => Boolean): MySet[A] = this

  def foreach(f: A => Unit): Unit = ()

  def -(elem: A): MySet[A] = this

  def &(anotherSet: MySet[A]): MySet[A] = this

  def --(anotherSet: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] = new AllInclusiveSet[A]

}



class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean =
    elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] =
    if(this contains elem) this
    else new NonEmptySet[A](elem, this)

  /* steps for the ++ function
  [1,2,3] ++ [4,5]
  [2,3] ++ [4,5] + 1
  [3] ++ [4,5] + 1 + 2
  [] ++ [4,5] + 1 + 2 + 3
  [4,5] + 1 + 2 + 3
   */
  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] = (tail map f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail

  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  def -(elem: A): MySet[A] = {
    if (head == elem) tail
    else tail - elem + head
  }

  /*
  This can be this.filter or just filter. Also filter(x => anotherSet.contains(x)) can
  be reduced to filter(anotherSet) meaning intersecting and filtering is the same thing.
  */
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  /*
  This can be this.filter or just filter. Also filter(x => !anotherSet.contains(x)) can
  be reduced to filter(!anotherSet) if a unary_! (unary underscore bang) is implemented.
  */
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  def unary_! : MySet[A] = filter(!anotherSet)
}

object MySetPlayground extends App {
  val s = MySet(1,2,3)

  s + 5 ++ MySet(10,11) + 3 map (x => x*10) filter (_ % 3 == 0)foreach println
}
