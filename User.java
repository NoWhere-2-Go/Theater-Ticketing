class User {
    private final String userName;
    private final String passWord;
    private Node<Order> firstOrder;
    private int numOfOrders;

    public User(String user, String pass){
        this.userName = user;
        this.passWord = pass;
        this.firstOrder = null;
    }

    // accessors
    public String getUserName() { return userName; }
    public String getPassWord() { return passWord; }
    public Node<Order> getFirstOrder() { return firstOrder; }
    public int getNumOfOrders() { return numOfOrders; }

    // setters
    public void setFirstOrder(Node<Order> firstOrder) { this.firstOrder = firstOrder; }

    // method to add a new order to their list
    public void appendOrder(Node<Order> newOrder)
    {
        // check if this the users first order
        if (this.firstOrder == null)
            this.firstOrder = newOrder;
        else {
            // traverse to the end and append the new order to the linked list
            Node<Order> curr = this.firstOrder;
            while (curr.getNext() != null)
                curr = curr.getNext();
            curr.setNext(newOrder);
        }
        this.numOfOrders++;
    }

    // method to add seats to an orders seat list
    public void updateSeats(int adultTickets, int childTickets, int seniorTickets,
                                   int row, char firstSeat, Auditorium<Seat> aud, int orderNum, int audNum)
    {
        Order currOrder = findOrder(orderNum);
        int totalTickets = adultTickets+childTickets+seniorTickets;
        Node<Seat> beginning = findNode(row, firstSeat, aud);               // get the first node of section being reserved

        while (beginning != null && totalTickets > 0)
        {
            char currSeat = beginning.getPayload().getSeat();
            if (adultTickets > 0){
                Seat tempSeat = new Seat(row, audNum, currSeat, 'A');
                currOrder.addToSeatList(tempSeat);
                adultTickets--;
            } else if (childTickets > 0){
                Seat tempSeat = new Seat(row, audNum, currSeat, 'C');
                currOrder.addToSeatList(tempSeat);
                childTickets--;
            }
            else if (seniorTickets > 0){
                Seat tempSeat = new Seat(row, audNum, currSeat, 'S');
                currOrder.addToSeatList(tempSeat);
                seniorTickets--;
            }
            totalTickets--;
            beginning = beginning.getNext();
        }

    }

    // helper method to find an order
    private Order findOrder(int num)
    {
        Node<Order> temp = this.firstOrder;
        Order order = temp.getPayload();
        // traverse list until you find matching order number
        while (temp != null)
        {
            if (temp.getPayload().getOrderNumber() == num) {
                order = temp.getPayload();
                temp = temp.getNext();
            }
            else
                temp = temp.getNext();
        }
        return order;
    }

    // helper method to find the node that matches the beginning seat
    private Node<Seat> findNode(int row, char seat, Auditorium<Seat> audi)
    {
        Node<Seat> rowNode = audi.getFirst();
        Node<Seat> currNode = rowNode;
        // traverse whole auditorium until you find the matching node
        while (rowNode != null)
        {
            while (currNode != null)
            {
                int tempRow = currNode.getPayload().getRow();
                char tempSeat = currNode.getPayload().getSeat();
                if (tempRow == row && tempSeat == seat)
                    return currNode;
                currNode = currNode.getNext();
            }
            rowNode = rowNode.getDown();
            currNode = rowNode;
        }
        // match not found
        return null;
    }

    // method to display orders to user
    public void printOrders(boolean isUpdating)
    {
        Node<Order> curr = this.firstOrder;
        // check if user is updating their order
        if (isUpdating) {
            int count = 1;
            // traverse the linked list and print each order
            while (curr != null) {
                System.out.print(count + ".  ");
                curr.getPayload().printSeats();
                curr = curr.getNext();
                count++;
            }
        } else {
            // traverse the linked list and print each order
            while (curr != null) {
                curr.getPayload().printSeats();
                int adults = 0, kids = 0, elders = 0;                   // counters for ticket types per order

                // check the ticket types of the order
                for (Seat currSeat : curr.getPayload().getSeatList())
                {
                    if (currSeat.getTicketType() == 'A')
                        adults++;
                    else if (currSeat.getTicketType() == 'C')
                        kids++;
                    else
                        elders++;
                }

                System.out.println(adults + " adult, " + kids + " child, " + elders + " senior");
                curr = curr.getNext();
            }
        }
    }

    public void displayReceipt()
    {
        float userTotal = 0.0f;
        // traverse the order linked list and print each order and their sum
        Node<Order> curr = this.firstOrder;
        while (curr != null) {
            curr.getPayload().printSeats();
            float orderSum = 0.0f;
            int adults = 0, kids = 0, elders = 0;                   // counters for ticket types per order

            // check the ticket types of the order
            for (Seat currSeat : curr.getPayload().getSeatList())
            {
                if (currSeat.getTicketType() == 'A') {
                    adults++;
                    orderSum += 10;
                }
                else if (currSeat.getTicketType() == 'C') {
                    kids++;
                    orderSum += 5;
                }
                else {
                    elders++;
                    orderSum += 7.5f;
                }
            }

            System.out.println(adults + " adult, " + kids + " child, " + elders + " senior");
            userTotal += orderSum;
            System.out.print("Order Total: $");
            System.out.printf("%.2f\n\n", orderSum);
            curr = curr.getNext();
        }

        // print out the users total after traversal ends
        System.out.print("Customer Total: $");
        System.out.printf("%.2f\n\n", userTotal);
    }

    // method to delete ticket from order
    public void deleteTicket(int row, char seat, int orderNum)
    {
        int i;
        // find the order
        Order currOrder = findOrder(orderNum);
        // traverse the seat list and find the seat reserved
        ArrayList<Seat> tempList = currOrder.getSeatList();
        Seat tempSeat = tempList.get(0);
        for (i = 0; i < tempList.size(); i++) {
            tempSeat = tempList.get(i);
            if (tempSeat.getSeat() == seat && tempSeat.getRow() == row) {
                break;
            }
        }

        // check what kind of ticket it was and decrement it's counter
        if (tempSeat.getTicketType() == 'A')
            currOrder.setAdultTickets(currOrder.getTickets("adult")-1);
        else if (tempSeat.getTicketType() == 'C')
            currOrder.setChildTickets(currOrder.getTickets("child")-1);
        else
            currOrder.setSeniorTickets(currOrder.getTickets("senior")-1);

        // remove that seat from the seatList
        // and replace the seatList and order so changes are saved
        tempList.remove(i);
        currOrder.setSeatList(tempList);
        replaceOrder(currOrder, currOrder.getOrderNumber());

        // check if removing the ticket will make the order empty
        if (findOrder(orderNum).getSeatList().isEmpty())
            this.cancelOrder(orderNum);                                 // call cancel order method if true
    }

    // method to check if seats to be deleted are in the
    // users order
    public boolean isInOrder(int row, char seat, int orderNum)
    {
        ArrayList<Seat> currList = findOrder(orderNum).getSeatList();

        // traverse seat list and check if it matches row and seat
        for (int i = 0; i < currList.size(); i++)
        {
            Seat temp = currList.get(i);
            if (temp.getSeat() == seat && temp.getRow() == row)
                return true;
        }
        return false;
    }

    // method to replace an order after seat deletion
    private void replaceOrder(Order newOrder, int oldOrderNum)
    {
        // traverse the linked list and find the order to be replaced
        Node<Order> curr = this.firstOrder, prev = curr;
        while (curr != null && curr.getPayload().getOrderNumber() != oldOrderNum) {
            prev = curr;
            curr = curr.getNext();
        }

        // replace order
        Node<Order> repOrder = new Node<>();
        repOrder.setPayload(newOrder);

        // check if the head is the old order
        if (oldOrderNum == this.firstOrder.getPayload().getOrderNumber())
        {
            this.firstOrder = repOrder;
        }

        repOrder.setNext(curr.getNext());
        prev.setNext(repOrder);
    }

    public void cancelOrder(int orderNum)
    {
        // check if the cancelled order is the first order
        if (this.firstOrder.getPayload().getOrderNumber() == orderNum)
            this.firstOrder = null;
        else {
            // traverse the linked list and find the order to be replaced
            Node<Order> curr = this.firstOrder, prev = curr;
            while (curr != null && curr.getPayload().getOrderNumber() != orderNum) {
                prev = curr;
                curr = curr.getNext();
            }

            // cancel the order
            prev.setNext(curr.getNext());                   // move the previous node next
        }
        this.numOfOrders--;     // decrement that users number of orders
    }

}
