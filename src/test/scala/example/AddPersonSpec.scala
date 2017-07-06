package example

import org.scalatest._

class AddPersonSpec extends FlatSpec with Matchers {
  val input =
    """23
      |Robert Sandwich
      |rsandw@example.net
      |222-111-3333
      |work
      |101-201-5555
      |home
      |789-1234
      |
      |414-999-8765
      |mobile
      |
    """.stripMargin

  val expectedOutput =
    "Enter person ID: " +
    "Enter name: " +
    "Enter email address (blank for none): " +
    "Enter a phone number (or leave blank to finish): " +
    "Is this a mobile, home, or work phone? " +
    "Enter a phone number (or leave blank to finish): " +
    "Is this a mobile, home, or work phone? " +
    "Enter a phone number (or leave blank to finish): " +
    "Is this a mobile, home, or work phone? " +
    "Unknown phone type.  Using default.\n" +
    "Enter a phone number (or leave blank to finish): " +
    "Is this a mobile, home, or work phone? " +
    "Enter a phone number (or leave blank to finish): "

  "The promptForAddress method" should "interact on the console" in {
    import com.example.tutorial.addressbook.Person
    import com.example.tutorial.addressbook.Person.{PhoneNumber, PhoneType}

    import java.io._
    import java.nio.charset.StandardCharsets

    val sr = new StringReader(input)
    val stdin = new BufferedReader(sr)
    val os = new ByteArrayOutputStream()
    val stdout = new PrintStream(os)
    val person = AddPerson.promptForAddress(stdin, stdout)
    val output: String = new String(os.toByteArray, StandardCharsets.UTF_8)
    stdin.close()
    sr.close()
    stdout.close()
    os.close()

    output shouldEqual expectedOutput

    person shouldEqual Person(
      id = 23,
      name = "Robert Sandwich",
      email = Some("rsandw@example.net"),
      phones = Seq(
        PhoneNumber("222-111-3333", `type` = Some(PhoneType.WORK)),
        PhoneNumber("101-201-5555", `type` = Some(PhoneType.HOME)),
        PhoneNumber("789-1234",     `type` = None),
        PhoneNumber("414-999-8765", `type` = Some(PhoneType.MOBILE))
      )
    )
  }
}
