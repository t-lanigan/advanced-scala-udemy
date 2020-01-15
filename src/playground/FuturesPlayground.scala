package playground

import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random



object FuturesPlayground extends App {

  def sleep(time: Long) { Thread.sleep(time) }
  println("starting calculation ...")

  val f1 = Future {
    sleep(100)
    42
  }

  val f2 = Future {
    sleep(300)
    8
  }

  println("before onComplete f1")
  f1.onComplete {
    case Success(value) => println(s"Got the callback, meaning = $value")
    case Failure(e) => e.printStackTrace
  }

  println("before onComplete f2")
  f2.onComplete {
    case Success(value) => println(s"Got the callback, meaning = $value")
    case Failure(e) => e.printStackTrace
  }
  // do the rest of your work
  println("A ..."); sleep(100)
  println("B ..."); sleep(100)
  println("C ..."); sleep(100)
  println("D ..."); sleep(100)
  println("E ..."); sleep(100)
  println("F ..."); sleep(100)
  sleep(2000)
}