class Order
{
    private ArrayList<Seat> seatList = new ArrayList<>(0);
    private int adultTickets, childTickets, seniorTickets, auditoriumNum;
    private int orderNumber;


    // overloaded constructor
    public Order(int adultTix, int childTix, int senTix, int orderNum, int audNum)
    {
        this.adultTickets = adultTix;
        this.childTickets = childTix;
        this.seniorTickets = senTix;
        this.orderNumber = orderNum;
        this.auditoriumNum = audNum;
    }

    // getter method for all ticket types.
    // Will return the amount for the ticket type depending on what the argument is
    public int getTickets (String type){
        if (type.equals("adult"))
            return adultTickets;
        else if (type.equals("child"))
            return childTickets;
        else if (type.equals("senior"))
            return seniorTickets;
        return 0;
    }

    public void setAdultTickets(int adultTickets) { this.adultTickets = adultTickets; }
    public void setChildTickets(int childTickets) { this.childTickets = childTickets; }
    public void setSeniorTickets(int seniorTickets) { this.seniorTickets = seniorTickets; }
    public void setAuditoriumNum(int auditoriumNum) { this.auditoriumNum = auditoriumNum; }
    public void setSeatList(ArrayList<Seat> seatList) { this.seatList = seatList; }

    public void setOrderNumber(User user1)
    {
        int count = 1;
        // check if the user has no previous order
        if (user1.getFirstOrder() == null)
        {
            this.orderNumber = count;
        } else {
            // traverse to end of their order list and update the count
            Node<Order> curr = user1.getFirstOrder();
            while (curr != null) {
                count++;
                curr = curr.getNext();
            }
        }
        this.orderNumber = count;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public ArrayList<Seat> getSeatList() {
        return seatList;
    }

    public int getAuditoriumNum() {
        return auditoriumNum;
    }

    // method to add seats to the seatList of an order
    public void addToSeatList(Seat temp) {
        this.seatList.add(temp);
    }

    // method to print out the seats the user reserved for that order
    public void printSeats()
    {
        // sort the list when printing it
        this.seatList.sort(Comparator
                .comparing(Seat::getRow)
                .thenComparing(Seat::getSeat));

        System.out.print("Auditorium " + this.auditoriumNum + ", ");
        for (int i = 0; i < this.seatList.size(); i++){
            int row = this.seatList.get(i).getRow();
            String seat = Character.toString(this.seatList.get(i).getSeat());
            // check if at last index of array list
            if (i != this.seatList.size()-1)
                System.out.print(row + seat + ", ");
            else
                System.out.println(row + seat);
        }
    }


} // end of Order class
