import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Auditorium<T> {
    private Node<T> first;

    // constructor
    public Auditorium(String fileName, int audNum, Scanner scan)
    {
        Node<T> curr = new Node<>();
        Node<T> row = new Node<>();
        String line;
        File readFile;
        boolean fileExists = false;

        // use a try and catch block to prevent any errors
        try
        {
            readFile = new File(fileName);                      // create a file to read from
            scan = new Scanner(readFile);                       // move the scanner to read the file
            fileExists = readFile.exists();                     // set fileExists to the status of the files existence

        }catch (FileNotFoundException E){
            System.out.println("file not found");
            E.printStackTrace();
        }

        int rowCounter = 1;
        while (scan.hasNext() && fileExists)
        {
            char seatCounter = 'A';
            line = scan.nextLine();
            for (int i = 0; i < line.length(); i++)
            {
                boolean endOfLine = false;
                if (rowCounter > 1 && seatCounter == 'A')                                                   // check if at a new line
                    endOfLine = true;

                Seat newSeat = new Seat(rowCounter, audNum, seatCounter, line.charAt(i));                    // create a new seat to populate
                Node<T> newNode = new Node<T>(null, null,null, (T)newSeat);    // create a new node to insert

                if (this.getFirst() == null)                       // if auditorium is null, make first the new node
                {
                    this.setFirst(newNode);
                    row = this.getFirst();
                    curr = this.getFirst();
                }
                else if (this.getFirst().getNext() == null)        // else if firsts next is null append the newNode to it
                {
                    this.getFirst().setNext(newNode);
                    curr = this.getFirst().getNext();
                }
                // else if rowCounter > 1 assign firsts down pointer to newNode and at the end of line
                else if (rowCounter > 1 && endOfLine)
                {
                    // hold onto the previous node
                    Node<T> temp = row;
                    // move the row node
                    row.setDown(newNode);
                    row = row.getDown();
                    row.setUp(temp);
                    // move curr to row
                    curr = row;
                }
                else                                     // else append the node
                {
                    // traverse to the end of the list
                    while (curr.getNext() != null)
                        curr = curr.getNext();
                    // append the new node to the list
                    curr.setNext(newNode);
                }
                seatCounter+=1;
            } // end of for loop for line of file
            rowCounter++;
        }
        scan.close();
    }

    // mutator
    public void setFirst(Node<T> first) {
        this.first = first;
    }

    // accessor
    public Node<T> getFirst() {
        return first;
    }

    // method to print current ticketTypes
    public void printAuditorium()
    {
        int rowCount = 1;
        Node<T> curr = this.getFirst();
        Node<T> rowNode = this.getFirst();

        while (rowNode != null)
        {
            System.out.print(rowCount + " ");
            while (curr != null)
            {
                // if seat is reserved print a # instead
                if (!Objects.equals(curr.getPayload().toString(), "."))
                    System.out.print('#');
                else
                    System.out.print(curr.getPayload().toString());
                curr = curr.getNext();
            }
            rowCount++;
            System.out.println();
            rowNode = rowNode.getDown();
            curr = rowNode;
        }
    }

}
