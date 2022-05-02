public class Seat {
    //member variables
    private int row;
    private int auditoriumNum;
    private char seat;
    private char ticketType;

    // default constructor
    public Seat() {

    }
    // overloaded constructor
    public Seat(int rowInput, int audNum, char seatInput, char ticket)
    {
        this.row = rowInput;
        this.auditoriumNum = audNum;
        this.seat = seatInput;
        this.ticketType = ticket;
    }

    // mutators
    public void setSeat(char seat) {
        this.seat = seat;
    }

    public void setAuditoriumNum(int auditoriumNum) { this.auditoriumNum = auditoriumNum; }

    public void setRow(int row) {
        this.row = row;
    }

    public void setTicketType(char ticketType) {
        this.ticketType = ticketType;
    }

    // accessors
    public char getSeat() {
        return seat;
    }

    public char getTicketType() {
        return ticketType;
    }

    public int getRow() {
        return row;
    }

    public int getAuditoriumNum() { return auditoriumNum; }

    @Override
    public String toString()
    {
        return String.valueOf(ticketType);
    }

}

