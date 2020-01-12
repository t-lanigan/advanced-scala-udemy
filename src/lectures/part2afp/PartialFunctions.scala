package lectures.part2afp

object PartialFunctions extends App {
  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] or Int => Int
  // Any Int can be passed and return a result.
  // What happens if we want to restrict what is passed into the function?

  class FunctionNotApplicableException extends RuntimeException
  val aFussyFunction = (x: Int) => {
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException
  }
  // This is clunky. We could instead use pattern matching.

  val aNicerFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
      //Throw a match error for anything not matched.
  }
  // {1,2,5} => Int This is a basically partial function.

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } //Partial function value equivalent to what is above. But also has other methods.

  println(aPartialFunction(2))
  //println(aPartialFunction(4)) // Crashes with a match error.

  /*
  So what are these good for?
   */

  //You can use the isDefined at method.
  println(aPartialFunction.isDefinedAt(10))

  //Can be "lifted" to total function with return type as Option.
  val lifted = aPartialFunction.lift // Int => Option[Int]

  println(lifted(2))
  println(lifted(54))

  // You can use orElse to chain PFs
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  //Partial functions extend total functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  //SIDEEFFECT: Higher order functions accept functions as well
  val aSmallPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 78
    case 3 => 100
  }
  val aMappedList = List(1,2,3).map {aSmallPartialFunction}

  println(aMappedList)

  /*
  Note: A partial function can only have one parameter Type
   */

  /**
   *
   * Exercises
   *
   * 1 - contruct a PF instance (anonymous class)
   * 2 - Make a small dumb chatbot as a partial function
   *
   * You can use this to make a input:
   * scala.io.Source.stdin.getLines().foreach(line => println(s"you said: $line"))
   *
   */

////My answer:
//  val pfChatbot = {
//    case "hello" => "what's up dawg"
//    case _ => "I don't understand"
//  }
//
//  scala.io.Source.stdin.getLines().foreach(line => println(pfChatbot(line)))

  // The answer question 1:


  // Needs to define apply and isEmpty.
  val aManualFussyFunction = new PartialFunction[Int, Int] {
      override def apply(x: Int): Int = x match {
        case 1 => 42
        case 2 => 56
        case 5 => 999
      }
      override def isDefinedAt(x: Int): Boolean = {
        x == 1 || x == 2 || x == 5
      }
   }

  // Question 2
  val chatbot: PartialFunction[String, String] = {
    case "hello" => "sup dude"
    case "what is your name" => "my name is chatius botius"
    case _ => "I don't understand"
  }

//  scala.io.Source.stdin.getLines().foreach(line => println(chatbot(line)))

  //Could also write like this:
  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)

}

