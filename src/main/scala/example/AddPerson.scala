package example

import com.example.tutorial.addressbook.AddressBook
import com.example.tutorial.addressbook.Person
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.PrintStream

import com.example.tutorial.addressbook.Person.PhoneNumber
import com.google.protobuf.CodedInputStream

object AddPerson {

  // This function fills in a Person message based on user input.
  def promptForAddress(stdin: BufferedReader, stdout: PrintStream): Person = {

    var person: Person = Person.defaultInstance

    stdout.print("Enter person ID: ")
    person = person.withId(Integer.valueOf(stdin.readLine()))

    stdout.print("Enter name: ")
    person = person.withName(stdin.readLine())

    stdout.print("Enter email address (blank for none): ")
    val email: String = stdin.readLine()
    if (email.length > 0) {
      person = person.withEmail(email)
    }

    import scala.util.control.Breaks._
    breakable {
      while (true) {
        stdout.print("Enter a phone number (or leave blank to finish): ")
        val number: String = stdin.readLine()
        if (number.length == 0) {
          break
        }

        var phoneNumber = PhoneNumber.defaultInstance.withNumber(number)

        stdout.print("Is this a mobile, home, or work phone? ")
        val phtype = stdin.readLine()
        if (phtype == "mobile") {
          phoneNumber = phoneNumber.withType(Person.PhoneType.MOBILE)
        } else if (phtype == "home") {
          phoneNumber = phoneNumber.withType(Person.PhoneType.HOME)
        } else if (phtype == "work") {
          phoneNumber = phoneNumber.withType(Person.PhoneType.WORK)
        } else {
          stdout.println("Unknown phone type.  Using default.")
        }

        person = person.addPhones(phoneNumber)
      }
    }

    person
  }

  // Main function:  Reads the entire address book from a file,
  //   adds one person based on user input, then writes it back out to the same
  //   file.
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      System.err.println("Usage:  AddPerson ADDRESS_BOOK_FILE")
      System.exit(-1)
    }

    var addressBook = AddressBook.defaultInstance

    // Read the existing address book.
    try {
      addressBook = addressBook.mergeFrom(CodedInputStream.newInstance(new FileInputStream(args(0))))
    } catch {
      case e: FileNotFoundException =>
        System.out.println(args(0) + ": File not found.  Creating a new file.")
    }

    // Add an address.
    addressBook = addressBook.addPeople(
      promptForAddress(new BufferedReader(new InputStreamReader(System.in)),
        System.out))

    // Write the new address book back to disk.
    val output = new FileOutputStream(args(0))
    addressBook.writeTo(output)
    output.close()
  }


}
