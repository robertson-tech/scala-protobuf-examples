import com.example.tutorial.addressbook.AddressBook
import com.example.tutorial.addressbook.Person
import java.io.FileInputStream

import com.example.tutorial.addressbook.Person.PhoneType._

object ListPeople {
  // Iterates though all people in the AddressBook and prints info about them.
  def print(addressBook: AddressBook): Unit = {
    for (person: Person <- addressBook.people) {
      System.out.println("Person ID: " + person.id)
      System.out.println("  Name: " + person.name)
      for (email <- person.email) {
        System.out.println("  E-mail address: " + email)
      }

      for (phoneNumber <- person.phones) {
        phoneNumber.getType match {
          case MOBILE =>
            System.out.print("  Mobile phone #: ")
          case HOME =>
            System.out.print("  Home phone #: ")
          case WORK =>
            System.out.print("  Work phone #: ")
          case _ => // shouldn't happen
            System.out.print("  Unknown type phone #: ")
        }
        System.out.println(phoneNumber.number)
      }
    }
  }

  // Main function:  Reads the entire address book from a file and prints all
  //   the information inside.
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      System.err.println("Usage:  ListPeople ADDRESS_BOOK_FILE")
      System.exit(-1)
    }

    // Read the existing address book.
    val addressBook: AddressBook =
      AddressBook.parseFrom(new FileInputStream(args(0)))

    print(addressBook)
  }
}
