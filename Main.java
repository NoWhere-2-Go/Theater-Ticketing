
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public final static String seats = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static void main(String[] args)
    {

        // file that contains all the usernames and passwords
        File users = new File("userdb.dat");
        HashMap<String, User> userMap = new HashMap<>();

        Scanner scnr = null;
        try {
            scnr = new Scanner(users);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // read the userdb file and parse the data and populate the hashmap
        while (scnr.hasNextLine()){
            String username = scnr.next();
            String password = scnr.next();

            // create a temporary user to put in hashmap
            User newUser = new User(username, password);
            userMap.put(newUser.getUserName(), newUser);
        }
        scnr.close();

        // create the 3 auditoriums
        Auditorium<Seat> auditorium1 = new Auditorium<>("A1.txt",1, scnr);
        Auditorium<Seat> auditorium2 = new Auditorium<>("A2.txt",2, scnr);
        Auditorium<Seat> auditorium3 = new Auditorium<>("A3.txt",3, scnr);
        scnr = new Scanner(System.in);

         // display the starting point and login menu to user
        boolean usermatch = false, passwordMatch = false, adminExit = false;
        String password;
        do {
            while (!usermatch && !passwordMatch) {
                System.out.print("Enter username: ");
                String username = scnr.nextLine();

                // check if username is inside the hashmap
                for (String str : userMap.keySet()) {
                    // check if you find a matching username
                    if (username.equals(str)) {
                        usermatch = true;

                        // get password input & validate it
                        int attemps = 1;
                        while (!passwordMatch && attemps <= 3) {
                            System.out.print("Enter password: ");
                            password = scnr.nextLine();

                            for (String s : userMap.keySet()) {
                                String tempPW = userMap.get(s).getPassWord();
                                if (password.equals(tempPW)) {
                                    passwordMatch = true;

                                    // check if the user is an admin or regular user
                                    if (username.equals("admin")) {
                                        boolean loggedOut = false;
                                        while (!loggedOut) {
                                            // display admin menu and get input
                                            System.out.println(
                                                    "\n1. Print Report"
                                                            + "\n2. Logout"
                                                            + "\n3. Exit");
                                            String adminInput = scnr.nextLine();

                                            // validate what they typed
                                            boolean valid = adminMenuInput(adminInput);
                                            while (!valid) {
                                                adminInput = scnr.nextLine();
                                                valid = adminMenuInput(adminInput);
                                            }
                                            int adminMenuSel = Integer.parseInt(adminInput);

                                            // check what menu selection was made
                                            if (adminMenuSel == 1) {
                                                // print each auditorium report & total report
                                                displayReport(auditorium1, auditorium2, auditorium3);
                                            } else if (adminMenuSel == 2) {
                                                passwordMatch = false;
                                                usermatch = false;
                                                attemps = 4;
                                                loggedOut = true;
                                            } else {
                                                adminExit = true;
                                                loggedOut = true;
                                            }
                                        }
                                    } else {
                                        boolean loggedOut = false;
                                        while (!loggedOut) {
                                            // display the customer menu and get input
                                            System.out.println("\n1. Reserve Seats"
                                                    + "\n2. View Orders"
                                                    + "\n3. Update Order"
                                                    + "\n4. Display Receipt"
                                                    + "\n5. Log Out");

                                            // validate what was entered for menu selection
                                            String tempSelection = scnr.nextLine();
                                            boolean validMenuSel = userMenuInput(tempSelection, 1);
                                            while (!validMenuSel) {
                                                tempSelection = scnr.nextLine();
                                                validMenuSel = userMenuInput(tempSelection, 1);
                                            }
                                            int userMenuSelection = Integer.parseInt(tempSelection);

                                            // check which menu operation to run
                                            if (userMenuSelection == 1) {
                                                // display auditorium submenu & get input
                                                System.out.println(
                                                                "1. Auditorium 1\n" +
                                                                "2. Auditorium 2\n" +
                                                                "3. Auditorium 3");

                                                // validate input
                                                String tempAudSelection = scnr.nextLine();
                                                boolean validAud = validateAuditorium(tempAudSelection);
                                                while (!validAud) {
                                                    tempAudSelection = scnr.nextLine();
                                                    validAud = validateAuditorium(tempAudSelection);
                                                }

                                                int auditoriumNum = Integer.parseInt(tempAudSelection);

                                                // check which auditorium was selected
                                                if (auditoriumNum == 1) {
                                                    // display auditorium 1
                                                    System.out.print("  ");
                                                    for (int i = 0; i < getSeats(auditorium1); i++)
                                                    {
                                                        System.out.print(seats.charAt(i));
                                                    }
                                                    System.out.println();
                                                    auditorium1.printAuditorium();

                                                    // ask user for row
                                                    System.out.print("\nEnter a row: ");
                                                    String input = scnr.nextLine();

                                                    boolean valid = validRowSelection(input, auditorium1);
                                                    // while user doesn't enter a valid row
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validRowSelection(input, auditorium1);
                                                    }
                                                    int rowSelection = Integer.parseInt(input);

                                                    System.out.print("\nEnter a seat: ");
                                                    char seatSelection = scnr.next().charAt(0);
                                                    seatSelection = Character.toUpperCase(seatSelection);
                                                    valid = validSeatSelection(seatSelection, auditorium1);
                                                    // while user doesn't enter a valid seat
                                                    while (!valid)
                                                    {
                                                        seatSelection = scnr.next().charAt(0);
                                                        seatSelection = Character.toUpperCase(seatSelection);
                                                        valid = validSeatSelection(seatSelection, auditorium1);
                                                    }

                                                    // ask user for adult tickets
                                                    System.out.print("\nEnter number of adult tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium1);
                                                    // while user doesn't enter a valid number for tickets
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium1);
                                                    }
                                                    // once loop breaks and input is valid parse it
                                                    int adultTix = Integer.parseInt(input);

                                                    // ask user for child tickets
                                                    System.out.print("\nEnter number of child tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium1);
                                                    // while user doesn't enter a valid number for tickets
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium1);
                                                    }
                                                    int childTix = Integer.parseInt(input);

                                                    // ask user for senior tickets
                                                    System.out.print("\nEnter number of senior tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium1);

                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium1);
                                                    }
                                                    int seniorTix = Integer.parseInt(input);

                                                    // check if those seats are available
                                                    int totalTickets = adultTix+childTix+seniorTix;
                                                    boolean bestAvail = false;
                                                    if (!checkAvailability(auditorium1, rowSelection, seatSelection, totalTickets))    //if those seats aren't available tell user and exit function
                                                    {
                                                        Node<Seat> newNode = bestAvailable(auditorium1, totalTickets);
                                                        if (newNode != null)
                                                        {
                                                            // ask user if they want the best seats available
                                                            System.out.println("\nDo you want best seats available: Y/N");
                                                            String selection = scnr.nextLine().toUpperCase();
                                                            //if they do, change the row and beginning seat
                                                            if (Objects.equals(selection, "Y")) {
                                                                rowSelection = newNode.getPayload().getRow();        // change the row
                                                                seatSelection = newNode.getPayload().getSeat();      // change the seat
                                                                bestAvail = true;
                                                            }
                                                        }
                                                    }

                                                    // check if the seats are available
                                                    if (checkAvailability(auditorium1, rowSelection, seatSelection, totalTickets)) {
                                                        // create an order to add to the user
                                                        Order tempOrder = new Order(adultTix, childTix, seniorTix, 0, 1);
                                                        User tempUser = userMap.get(username);
                                                        tempOrder.setOrderNumber(tempUser);
                                                        tempOrder.setAuditoriumNum(1);
                                                        // create a node, so we can append the node to the order list
                                                        Node<Order> curr = new Node<>();
                                                        curr.setPayload(tempOrder);
                                                        // add the seats to this orders seat list
                                                        tempUser.appendOrder(curr);

                                                        // update that orders seat list
                                                        // and replace that username value with the new one updated one
                                                        int orderNum = tempOrder.getOrderNumber();
                                                        tempUser.updateSeats(adultTix, childTix, seniorTix, rowSelection, seatSelection,
                                                                auditorium1, orderNum, 1);
                                                        userMap.replace(username, tempUser);

                                                        // call the reservation function and return to the user menu
                                                        reserveSeats(auditorium1, rowSelection, seatSelection, adultTix, childTix, seniorTix);
                                                    } else
                                                        System.out.println("no seats available");
                                                }
                                                else if (auditoriumNum == 2) {
                                                    // display auditorium 2
                                                    System.out.print("  ");
                                                    for (int i = 0; i < getSeats(auditorium2); i++)
                                                    {
                                                        System.out.print(seats.charAt(i));
                                                    }
                                                    System.out.println();
                                                    auditorium2.printAuditorium();

                                                    //ask user for row
                                                    System.out.print("\nEnter a row: ");
                                                    String input = scnr.nextLine();

                                                    boolean valid = validRowSelection(input, auditorium2);
                                                    // while user doesn't enter a valid row
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validRowSelection(input, auditorium2);
                                                    }
                                                    int rowSelection = Integer.parseInt(input);

                                                    System.out.print("\nEnter a seat: ");
                                                    char seatSelection = scnr.next().charAt(0);
                                                    seatSelection = Character.toUpperCase(seatSelection);
                                                    valid = validSeatSelection(seatSelection, auditorium2);

                                                    // while user doesn't enter a valid seat
                                                    while (!valid)
                                                    {
                                                        seatSelection = scnr.next().charAt(0);
                                                        seatSelection = Character.toUpperCase(seatSelection);
                                                        valid = validSeatSelection(seatSelection, auditorium2);
                                                    }

                                                    // ask user for adult tickets
                                                    System.out.print("\nEnter number of adult tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium2);
                                                    // while user doesn't enter a valid number for tickets
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium2);
                                                    }
                                                    // once loop breaks and input is valid parse it
                                                    int adultTix = Integer.parseInt(input);

                                                    // ask user for child tickets
                                                    System.out.print("\nEnter number of child tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium2);
                                                    // while user doesn't enter a valid number for tickets
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium2);
                                                    }
                                                    int childTix = Integer.parseInt(input);

                                                    // ask user for senior tickets
                                                    System.out.print("\nEnter number of senior tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium2);

                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium2);
                                                    }
                                                    int seniorTix = Integer.parseInt(input);

                                                    //check if those seats are available
                                                    int totalTickets = adultTix+childTix+seniorTix;
                                                    if (!checkAvailability(auditorium2, rowSelection, seatSelection, totalTickets))    //if those seats aren't available tell user and exit function
                                                    {
                                                        Node<Seat> newNode = bestAvailable(auditorium2, totalTickets);
                                                        if (newNode != null)
                                                        {
                                                            //ask user if they want the best seats available
                                                            System.out.println("\nDo you want best seats available: Y/N");
                                                            String selection = scnr.nextLine().toUpperCase();
                                                            //if they do, change the row and beginning seat
                                                            if (Objects.equals(selection, "Y")) {
                                                                rowSelection = newNode.getPayload().getRow();        //change the row
                                                                seatSelection = newNode.getPayload().getSeat();      //change the seat
                                                            }
                                                        }
                                                    }

                                                    if (checkAvailability(auditorium2, rowSelection, seatSelection, totalTickets)) {
                                                        // create an order to add to the user
                                                        Order tempOrder = new Order(adultTix, childTix, seniorTix, 0, 2);
                                                        User tempUser = userMap.get(username);
                                                        tempOrder.setOrderNumber(tempUser);
                                                        tempOrder.setAuditoriumNum(2);
                                                        // create a node, so we can append the node to the order list
                                                        Node<Order> curr = new Node<>();
                                                        curr.setPayload(tempOrder);
                                                        // add the seats to this orders seat list
                                                        tempUser.appendOrder(curr);

                                                        // update that orders seat list
                                                        // and replace that username value with the new one updated one
                                                        int orderNum = tempOrder.getOrderNumber();
                                                        tempUser.updateSeats(adultTix, childTix, seniorTix, rowSelection, seatSelection,
                                                                auditorium2, orderNum, 2);
                                                        userMap.replace(username, tempUser);

                                                        // call the reservation function and return to the user menu
                                                        reserveSeats(auditorium2, rowSelection, seatSelection, adultTix, childTix, seniorTix);
                                                    } else
                                                        System.out.println("no seats available");
                                                }
                                                else {
                                                    // display auditorium 3
                                                    System.out.print("  ");
                                                    for (int i = 0; i < getSeats(auditorium3); i++)
                                                    {
                                                        System.out.print(seats.charAt(i));
                                                    }
                                                    System.out.println();
                                                    auditorium3.printAuditorium();

                                                    System.out.print("\nEnter a row: ");
                                                    String input = scnr.nextLine();

                                                    boolean valid = validRowSelection(input, auditorium3);
                                                    //while user doesn't enter a valid row
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validRowSelection(input, auditorium3);
                                                    }
                                                    int rowSelection = Integer.parseInt(input);

                                                    // get input for starting seat
                                                    System.out.print("\nEnter a seat: ");
                                                    char seatSelection = scnr.next().charAt(0);
                                                    seatSelection = Character.toUpperCase(seatSelection);
                                                    valid = validSeatSelection(seatSelection, auditorium3);

                                                    // while user doesn't enter a valid seat
                                                    while (!valid)
                                                    {
                                                        seatSelection = scnr.next().charAt(0);
                                                        seatSelection = Character.toUpperCase(seatSelection);
                                                        valid = validSeatSelection(seatSelection, auditorium3);
                                                    }

                                                    // ask user for adult tickets
                                                    System.out.print("\nEnter number of adult tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium3);
                                                    // while user doesn't enter a valid number for tickets
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium3);
                                                    }
                                                    // once loop breaks and input is valid parse it
                                                    int adultTix = Integer.parseInt(input);

                                                    // ask user for child tickets
                                                    System.out.print("\nEnter number of child tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium3);
                                                    // while user doesn't enter a valid number for tickets
                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium3);
                                                    }
                                                    int childTix = Integer.parseInt(input);

                                                    // ask user for senior tickets
                                                    System.out.print("\nEnter number of senior tickets: ");
                                                    input = scnr.nextLine();
                                                    valid = validTix(input, auditorium3);

                                                    while (!valid)
                                                    {
                                                        input = scnr.nextLine();
                                                        valid = validTix(input, auditorium3);
                                                    }
                                                    int seniorTix = Integer.parseInt(input);

                                                    // check if those seats are available
                                                    int totalTickets = adultTix+childTix+seniorTix;
                                                    if (!checkAvailability(auditorium3, rowSelection, seatSelection, totalTickets))    // if those seats aren't available tell user and exit function
                                                    {
                                                        Node<Seat> newNode = bestAvailable(auditorium3, totalTickets);
                                                        if (newNode != null)
                                                        {
                                                            // ask user if they want the best seats available
                                                            System.out.println("\nDo you want best seats available: Y/N");
                                                            String selection = scnr.nextLine().toUpperCase();
                                                            // if they do, change the row and beginning seat
                                                            if (Objects.equals(selection, "Y")) {
                                                                rowSelection = newNode.getPayload().getRow();        // change the row
                                                                seatSelection = newNode.getPayload().getSeat();      // change the seat
                                                            }
                                                        }
                                                    }

                                                    // reserve those seats if they're available
                                                    if (checkAvailability(auditorium3, rowSelection, seatSelection, totalTickets)) {
                                                        // create an order to add to the user
                                                        Order tempOrder = new Order(adultTix, childTix, seniorTix, 0, 3);
                                                        User tempUser = userMap.get(username);
                                                        tempOrder.setOrderNumber(tempUser);
                                                        tempOrder.setAuditoriumNum(3);
                                                        // create a node, so we can append the node to the order list
                                                        Node<Order> curr = new Node<>();
                                                        curr.setPayload(tempOrder);
                                                        // add the seats to this orders seat list
                                                        tempUser.appendOrder(curr);

                                                        // update that orders seat list
                                                        // and replace that username value with the new one updated one
                                                        int orderNum = tempOrder.getOrderNumber();
                                                        tempUser.updateSeats(adultTix, childTix, seniorTix, rowSelection, seatSelection,
                                                                auditorium3, orderNum, 3);
                                                        userMap.replace(username, tempUser);

                                                        // call the reservation function
                                                        reserveSeats(auditorium3, rowSelection, seatSelection, adultTix, childTix, seniorTix);
                                                    } else
                                                        System.out.println("no seats available");
                                                }
                                            } else if (userMenuSelection == 2) {
                                                User temp = userMap.get(username);
                                                // check if they have orders
                                                // else print the orders
                                                if (temp.getNumOfOrders() == 0) {
                                                    System.out.println("\nNo orders");
                                                } else
                                                    temp.printOrders(false);
                                            } else if (userMenuSelection == 3)
                                            {
                                                User tempUser = userMap.get(username);
                                                // check if they have order
                                                // else print order and ask for input
                                                if (tempUser.getNumOfOrders() == 0) {
                                                    System.out.println("\nNo orders");
                                                }
                                                else {
                                                    System.out.println("\nChoose an order number to update");
                                                    tempUser.printOrders(true);

                                                    // validate the order they chose
                                                    String temp = scnr.nextLine();
                                                    boolean valid = updateValidation(temp, tempUser);
                                                    while (!valid) {
                                                        temp = scnr.nextLine();
                                                        valid = updateValidation(temp, tempUser);
                                                    }
                                                    int orderNum = Integer.parseInt(temp);

                                                    boolean isDone = false;                 // becomes true once a user makes a valid action
                                                    while (!isDone) {
                                                        // display update action submenu
                                                        // get input and validate it
                                                        System.out.println("\n1. Add tickets to order \n2. Delete tickets from order" +
                                                                " \n3. Cancel Order");
                                                        String selection = scnr.nextLine();
                                                        boolean valid2 = userMenuInput(selection, 2);
                                                        while (!valid2) {
                                                            // loop until the input is valid
                                                            selection = scnr.nextLine();
                                                            valid2 = userMenuInput(selection, 2);
                                                        }
                                                        int updateOrderSel = Integer.parseInt(selection);

                                                        // check which menu was chosen
                                                        if (updateOrderSel == 1) {
                                                            // find the matching order number
                                                            Order updatedOrder = findOrder(orderNum, tempUser);

                                                            // display the auditorium for that order
                                                            if (updatedOrder.getAuditoriumNum() == 1) {
                                                                // display auditorium 1
                                                                System.out.print("  ");
                                                                for (int i = 0; i < getSeats(auditorium1); i++) {
                                                                    System.out.print(seats.charAt(i));
                                                                }
                                                                System.out.println();
                                                                auditorium1.printAuditorium();

                                                                // get input for seat reservation
                                                                System.out.print("\nEnter a row: ");
                                                                String input = scnr.nextLine();

                                                                valid = validRowSelection(input, auditorium1);
                                                                //while user doesn't enter a valid row
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validRowSelection(input, auditorium1);
                                                                }
                                                                int rowSelection = Integer.parseInt(input);

                                                                // get input for starting seat
                                                                System.out.print("\nEnter a seat: ");
                                                                char seatSelection = scnr.next().charAt(0);
                                                                seatSelection = Character.toUpperCase(seatSelection);
                                                                valid = validSeatSelection(seatSelection, auditorium1);

                                                                // while user doesn't enter a valid seat
                                                                while (!valid) {
                                                                    seatSelection = scnr.next().charAt(0);
                                                                    seatSelection = Character.toUpperCase(seatSelection);
                                                                    valid = validSeatSelection(seatSelection, auditorium1);
                                                                }

                                                                // ask user for adult tickets
                                                                System.out.print("\nEnter number of adult tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium1);
                                                                // while user doesn't enter a valid number for tickets
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium1);
                                                                }
                                                                // once loop breaks and input is valid parse it
                                                                int adultTix = Integer.parseInt(input);

                                                                // ask user for child tickets
                                                                System.out.print("\nEnter number of child tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium1);
                                                                // while user doesn't enter a valid number for tickets
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium1);
                                                                }
                                                                int childTix = Integer.parseInt(input);

                                                                // ask user for senior tickets
                                                                System.out.print("\nEnter number of senior tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium1);

                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium1);
                                                                }
                                                                int seniorTix = Integer.parseInt(input);

                                                                // check if seats are available
                                                                int totalTix = adultTix + childTix + seniorTix;
                                                                if (!checkAvailability(auditorium1, rowSelection, seatSelection, totalTix)) {
                                                                    System.out.println("\nnot available");
                                                                } else {
                                                                    reserveSeats(auditorium1, rowSelection,
                                                                            seatSelection, adultTix, childTix, seniorTix);

                                                                    // add those tickets to the order
                                                                    tempUser.updateSeats(adultTix, childTix, seniorTix, rowSelection, seatSelection,
                                                                            auditorium1, orderNum, 1);
                                                                    userMap.replace(username, tempUser);
                                                                    isDone = true;
                                                                }
                                                            } else if (updatedOrder.getAuditoriumNum() == 2) {
                                                                // display auditorium 2
                                                                System.out.print("  ");
                                                                for (int i = 0; i < getSeats(auditorium2); i++) {
                                                                    System.out.print(seats.charAt(i));
                                                                }
                                                                System.out.println();
                                                                auditorium2.printAuditorium();

                                                                // get input for seat reservation
                                                                System.out.print("\nEnter a row: ");
                                                                String input = scnr.nextLine();

                                                                valid = validRowSelection(input, auditorium2);
                                                                //while user doesn't enter a valid row
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validRowSelection(input, auditorium2);
                                                                }
                                                                int rowSelection = Integer.parseInt(input);

                                                                // get input for starting seat
                                                                System.out.print("\nEnter a seat: ");
                                                                char seatSelection = scnr.next().charAt(0);
                                                                seatSelection = Character.toUpperCase(seatSelection);
                                                                valid = validSeatSelection(seatSelection, auditorium2);

                                                                // while user doesn't enter a valid seat
                                                                while (!valid) {
                                                                    seatSelection = scnr.next().charAt(0);
                                                                    seatSelection = Character.toUpperCase(seatSelection);
                                                                    valid = validSeatSelection(seatSelection, auditorium2);
                                                                }

                                                                // ask user for adult tickets
                                                                System.out.print("\nEnter number of adult tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium2);
                                                                // while user doesn't enter a valid number for tickets
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium2);
                                                                }
                                                                // once loop breaks and input is valid parse it
                                                                int adultTix = Integer.parseInt(input);

                                                                // ask user for child tickets
                                                                System.out.print("\nEnter number of child tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium2);
                                                                // while user doesn't enter a valid number for tickets
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium2);
                                                                }
                                                                int childTix = Integer.parseInt(input);

                                                                // ask user for senior tickets
                                                                System.out.print("\nEnter number of senior tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium2);

                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium2);
                                                                }
                                                                int seniorTix = Integer.parseInt(input);

                                                                // check if seats are available
                                                                int totalTix = adultTix + childTix + seniorTix;
                                                                if (!checkAvailability(auditorium2, rowSelection, seatSelection, totalTix)) {
                                                                    System.out.println("\nno seats available");
                                                                } else {
                                                                    reserveSeats(auditorium2, rowSelection,
                                                                            seatSelection, adultTix, childTix, seniorTix);

                                                                    // add those tickets to the order
                                                                    tempUser.updateSeats(adultTix, childTix, seniorTix, rowSelection, seatSelection,
                                                                            auditorium2, orderNum, 2);
                                                                    userMap.replace(username, tempUser);
                                                                    isDone = true;
                                                                }
                                                            } else {
                                                                // display auditorium 3
                                                                System.out.print("  ");
                                                                for (int i = 0; i < getSeats(auditorium3); i++) {
                                                                    System.out.print(seats.charAt(i));
                                                                }
                                                                System.out.println();
                                                                auditorium3.printAuditorium();

                                                                // get input for seat reservation
                                                                System.out.print("\nEnter a row: ");
                                                                String input = scnr.nextLine();

                                                                valid = validRowSelection(input, auditorium3);
                                                                //while user doesn't enter a valid row
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validRowSelection(input, auditorium3);
                                                                }
                                                                int rowSelection = Integer.parseInt(input);

                                                                // get input for starting seat
                                                                System.out.print("\nEnter a seat: ");
                                                                char seatSelection = scnr.next().charAt(0);
                                                                seatSelection = Character.toUpperCase(seatSelection);
                                                                valid = validSeatSelection(seatSelection, auditorium3);

                                                                // while user doesn't enter a valid seat
                                                                while (!valid) {
                                                                    seatSelection = scnr.next().charAt(0);
                                                                    seatSelection = Character.toUpperCase(seatSelection);
                                                                    valid = validSeatSelection(seatSelection, auditorium3);
                                                                }

                                                                // ask user for adult tickets
                                                                System.out.print("\nEnter number of adult tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium3);
                                                                // while user doesn't enter a valid number for tickets
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium3);
                                                                }
                                                                // once loop breaks and input is valid parse it
                                                                int adultTix = Integer.parseInt(input);

                                                                // ask user for child tickets
                                                                System.out.print("\nEnter number of child tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium3);
                                                                // while user doesn't enter a valid number for tickets
                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium3);
                                                                }
                                                                int childTix = Integer.parseInt(input);

                                                                // ask user for senior tickets
                                                                System.out.print("\nEnter number of senior tickets: ");
                                                                input = scnr.nextLine();
                                                                valid = validTix(input, auditorium3);

                                                                while (!valid) {
                                                                    input = scnr.nextLine();
                                                                    valid = validTix(input, auditorium3);
                                                                }
                                                                int seniorTix = Integer.parseInt(input);

                                                                // check if seats are available
                                                                int totalTix = adultTix + childTix + seniorTix;
                                                                if (!checkAvailability(auditorium3, rowSelection, seatSelection, totalTix)) {
                                                                    System.out.println("\nno seats available");
                                                                } else {
                                                                    reserveSeats(auditorium3, rowSelection,
                                                                            seatSelection, adultTix, childTix, seniorTix);

                                                                    // add those tickets to the order
                                                                    tempUser.updateSeats(adultTix, childTix, seniorTix, rowSelection, seatSelection,
                                                                            auditorium3, orderNum, 3);
                                                                    userMap.replace(username, tempUser);
                                                                    isDone = true;
                                                                }
                                                            }
                                                        } else if (updateOrderSel == 2) {
                                                            isDone = true;
                                                            // find the matching order number
                                                            Order updatedOrder = findOrder(orderNum, tempUser);
                                                            System.out.println();

                                                            // get input for what seat they want deleted from the order
                                                            System.out.print("\nEnter row number: ");
                                                            String inputRow = scnr.nextLine();
                                                            char seatInput;
                                                            int audNum = updatedOrder.getAuditoriumNum();

                                                            // check which auditorium their order is in
                                                            if (audNum == 1) {
                                                                boolean validRow = validRowSelection(inputRow, auditorium1);

                                                                // row validation
                                                                while (!validRow) {
                                                                    inputRow = scnr.nextLine();
                                                                    validRow = validRowSelection(inputRow, auditorium1);
                                                                }

                                                                System.out.print("\nEnter seat: ");
                                                                seatInput = scnr.next().charAt(0);
                                                                seatInput = Character.toUpperCase(seatInput);

                                                                // seat validation
                                                                boolean validSeat = validSeatSelection(seatInput, auditorium1);
                                                                while (!validSeat) {
                                                                    seatInput = scnr.next().charAt(0);
                                                                    seatInput = Character.toUpperCase(seatInput);
                                                                    validSeat = validSeatSelection(seatInput, auditorium1);
                                                                }

                                                            } else if (audNum == 2) {
                                                                boolean validRow = validRowSelection(inputRow, auditorium2);

                                                                // row validation
                                                                while (!validRow) {
                                                                    inputRow = scnr.nextLine();
                                                                    validRow = validRowSelection(inputRow, auditorium2);
                                                                }

                                                                System.out.print("\nEnter seat: ");
                                                                seatInput = scnr.next().charAt(0);
                                                                seatInput = Character.toUpperCase(seatInput);

                                                                // seat validation
                                                                boolean validSeat = validSeatSelection(seatInput, auditorium2);
                                                                while (!validSeat) {
                                                                    seatInput = scnr.next().charAt(0);
                                                                    seatInput = Character.toUpperCase(seatInput);
                                                                    validSeat = validSeatSelection(seatInput, auditorium2);
                                                                }

                                                            } else {
                                                                boolean validRow = validRowSelection(inputRow, auditorium3);

                                                                // row validation
                                                                while (!validRow) {
                                                                    inputRow = scnr.nextLine();
                                                                    validRow = validRowSelection(inputRow, auditorium3);
                                                                }

                                                                System.out.print("\nEnter seat: ");
                                                                seatInput = scnr.next().charAt(0);
                                                                seatInput = Character.toUpperCase(seatInput);

                                                                // seat validation
                                                                boolean validSeat = validSeatSelection(seatInput, auditorium3);
                                                                while (!validSeat) {
                                                                    seatInput = scnr.next().charAt(0);
                                                                    seatInput = Character.toUpperCase(seatInput);
                                                                    validSeat = validSeatSelection(seatInput, auditorium3);
                                                                }

                                                            }
                                                            int deletionRow = Integer.parseInt(inputRow);

                                                            // check if that seat belongs to their order
                                                            if (!tempUser.isInOrder(deletionRow, seatInput, orderNum))
                                                                System.out.println("\nthat seat isn't in your order");
                                                            else {
                                                                // check which auditorium the order is in
                                                                if (updatedOrder.getAuditoriumNum() == 1) {
                                                                    tempUser.deleteTicket(deletionRow, seatInput, orderNum);
                                                                    changeSeat(deletionRow, seatInput, auditorium1);
                                                                } else if (updatedOrder.getAuditoriumNum() == 2) {
                                                                    tempUser.deleteTicket(deletionRow, seatInput, orderNum);
                                                                    changeSeat(deletionRow, seatInput, auditorium2);
                                                                    auditorium3.getFirst().getPayload().setTicketType('.');
                                                                } else {
                                                                    tempUser.deleteTicket(deletionRow, seatInput, orderNum);
                                                                    changeSeat(deletionRow, seatInput, auditorium3);
                                                                }
                                                            }

                                                        } else {          // cancel an order
                                                            isDone = true;
                                                            // go through their seat list and empty those seats inside the auditorium
                                                            Order deletedOrder = findOrder(orderNum, tempUser);
                                                            ArrayList<Seat> deletedSeats = deletedOrder.getSeatList();

                                                            for (Seat deletedSeat : deletedSeats) {
                                                                // check which auditorium the seat is in
                                                                if (deletedSeat.getAuditoriumNum() == 1)
                                                                    changeSeat(deletedSeat.getRow(), deletedSeat.getSeat(),
                                                                            auditorium1);
                                                                else if (deletedSeat.getAuditoriumNum() == 2)
                                                                    changeSeat(deletedSeat.getRow(), deletedSeat.getSeat(),
                                                                            auditorium2);
                                                                else
                                                                    changeSeat(deletedSeat.getRow(), deletedSeat.getSeat(),
                                                                            auditorium3);
                                                            }
                                                            tempUser.cancelOrder(orderNum);
                                                        }
                                                    }
                                                }
                                            } else if (userMenuSelection == 4) {            // display receipt
                                                User tempUser = userMap.get(username);
                                                tempUser.displayReceipt();
                                            }
                                            else {
                                                passwordMatch = false;
                                                usermatch = false;
                                                attemps = 4;
                                                loggedOut = true;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }

                            // user has entered invalid password
                            if (!passwordMatch && usermatch) {
                                System.out.println("Invalid password");
                                attemps++;
                            }

                            // if attempts == 3 return to starting point
                            if (attemps > 3) {
                                usermatch = false;
                                System.out.println("\n");
                            }
                        }
                        break;
                    }
                }
            } // end of user menu

        } while (!adminExit);

        // write each auditorium to a file
        File a1File = new File("A1Final.txt");
        File a2File = new File("A2Final.txt");
        File a3File = new File("A3Final.txt");
        int fileCount = 3;

        while (fileCount > 0) {
            // check which file we are writing to
            if (fileCount == 3) {
                // check if the file we are writing is valid
                try {
                    PrintWriter pw = new PrintWriter(a1File);

                    // points to first row
                    Node<Seat> tempRow = auditorium1.getFirst();
                    // points to first node in a row
                    Node<Seat> tempCurr = tempRow;

                    // traverse through each row and column and write to that file
                    while (tempRow != null)
                    {
                        while (tempCurr != null)
                        {
                            pw.write(tempCurr.getPayload().getTicketType());
                            tempCurr = tempCurr.getNext();
                        }
                        pw.write('\n');
                        tempRow = tempRow.getDown();
                        tempCurr = tempRow;
                    }
                    scnr.close();
                    pw.close();

                } catch (FileNotFoundException e) {
                    System.out.println("invalid file");
                    e.printStackTrace();
                }

            }
            else if (fileCount == 2)
            {
                // check if the file we are writing is valid
                try {
                    PrintWriter pw = new PrintWriter(a2File);

                    // points to first row
                    Node<Seat> tempRow = auditorium2.getFirst();
                    // points to first node in a row
                    Node<Seat> tempCurr = tempRow;

                    // traverse through each row and column and write to that file
                    while (tempRow != null)
                    {
                        while (tempCurr != null)
                        {
                            pw.write(tempCurr.getPayload().getTicketType());
                            tempCurr = tempCurr.getNext();
                        }
                        pw.write('\n');
                        tempRow = tempRow.getDown();
                        tempCurr = tempRow;
                    }
                    scnr.close();
                    pw.close();
                } catch (FileNotFoundException e) {
                    System.out.println("invalid file");
                    e.printStackTrace();
                }
            }
            else {
                // check if the file we are writing to is valid
                try {
                    PrintWriter pw = new PrintWriter(a3File);

                    // points to first row
                    Node<Seat> tempRow = auditorium3.getFirst();
                    // points to first node in a row
                    Node<Seat> tempCurr = tempRow;

                    // traverse through each row and column and write to that file
                    while (tempRow != null)
                    {
                        while (tempCurr != null)
                        {
                            pw.write(tempCurr.getPayload().getTicketType());
                            tempCurr = tempCurr.getNext();
                        }
                        pw.write('\n');
                        tempRow = tempRow.getDown();
                        tempCurr = tempRow;
                    }
                    scnr.close();
                    pw.close();
                } catch (FileNotFoundException e) {
                    System.out.println("invalid file");
                    e.printStackTrace();
                }
            }
            fileCount--;
        }

    }

    // function to replace populated seats with a '.' in that auditorium
    public static void changeSeat(int row, char seat, Auditorium<Seat> aud)
    {
        Node<Seat> currRow = aud.getFirst();
        Node<Seat> curr = aud.getFirst();

        // traverse list until finding a match
        while (currRow != null)
        {
            while (curr != null)
            {
                // creating variables for the seats row and seat just for readability
                int row2 = curr.getPayload().getRow();
                char seat2 = curr.getPayload().getSeat();

                // check if seat is found
                if (row == row2 && seat2 == seat){
                    curr.getPayload().setTicketType('.');
                    return;
                }
                else
                    curr = curr.getNext();               // move to next node
            }
            currRow = currRow.getDown();                // move to next row
            curr = currRow;
        }

    }

    // function to find matching order
    public static Order findOrder(int orderNumber, User user)
    {
        Node<Order> curr = user.getFirstOrder();

        // traverse their order list until you find the matching order number
        while (curr != null && orderNumber != curr.getPayload().getOrderNumber())
            curr = curr.getNext();
        return curr.getPayload();
    }

    // function to validate what the user types in for the admin menu selection
    public static boolean adminMenuInput(String input)
    {
        // check if user typed nothing and pressed enter
        if (input.equals(""))
            return false;
        // only expecting 1 character/digit input
        if (input.length() > 1)
            return false;
        for (int i = 0; i < input.length(); i++)
        {
            if (input.charAt(i) == '\n')
                return false;
            if (input.charAt(i) != '1' && input.charAt(i) != '2' && input.charAt(i) != '3')
                return false;
        }
        return true;
    }

    // function to validate what menu option a customer entered
    public static boolean userMenuInput(String input, int menuType)
    {
        if (input.equals(""))
            return false;
        // check if user is at the base user menu
        if (menuType == 1) {
            if (input.length() > 1)
                return false;
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '\n')
                    return false;
                if (input.charAt(i) != '1' && input.charAt(i) != '2' && input.charAt(i) != '3'
                        && input.charAt(i) != '4' && input.charAt(i) != '5')
                    return false;
            }
        }
        // check if user is at update order menu
        else if (menuType == 2){
            if (input.length() > 1)
                return false;
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '\n')
                    return false;
                if (input.charAt(i) != '1' && input.charAt(i) != '2' && input.charAt(i) != '3')
                    return false;
            }
        }
        return true;
    }

    // function to check if order chosen to update is valid
    public static boolean updateValidation(String orderNum, User user) {
        if (orderNum.equals(""))
            return false;
        // check if a letter was entered
        for (int i = 0; i < orderNum.length(); i++) {
            if (!Character.isDigit(orderNum.charAt(i)))
                return false;
        }
        // parse it to an integer and check if it's a negative number
        int x = Integer.parseInt(orderNum);
        if (x <= 0)
            return false;
        // check if the input was greater than number of orders
        if (x > user.getNumOfOrders())
            return false;
        return true;    // all checks have been passed
    }

    // function to check if the seats wanting to be reserved are available
    public static boolean checkAvailability(Auditorium<Seat> auditorium, int row, char seat, int tickets)
    {
        Node<Seat> rowNode = auditorium.getFirst();
        int counter = 0;

        while (rowNode.getPayload().getRow() != row)            // move rowNode to row user wants
            rowNode = rowNode.getDown();
        Node<Seat> curr = rowNode;
        while (curr.getPayload().getSeat() != seat)             // move curr to first seat user wants
            curr = curr.getNext();

        while (curr != null && counter < tickets)               // check if each seat user wants is available
        {
            if (curr.getPayload().getTicketType() != '.')
            {
                return false;
            }
            curr = curr.getNext();
            counter++;
        }

        if (curr == null && counter < tickets)                  // check if there's too many tickets for that section
            return false;

        // if all checks are passed the seats are available
        return true;
    }

    // function to validate auditorium menu selection
    public static boolean validateAuditorium(String input)
    {
        if (input.equals(""))
            return false;
        if (input.length() > 1)
            return false;
        for (int i = 0; i < input.length(); i++)
        {
            if (input.charAt(i) != '1' && input.charAt(i) != '2' && input.charAt(i) != '3')
                return false;
        }
        return true;
    }

    // function that will validate the row the user entered
    public static boolean validRowSelection(String line, Auditorium<Seat> tempAud)
    {
        // check if user entered an integer
        for (int i = 0; i < line.length(); i++)
        {
            // if user entered anything but an integer, return false
            if (!Character.isDigit(line.charAt(i)))
                return false;
        }
        int input;
        try {
            input = Integer.parseInt(line);
        }catch (NumberFormatException E)
        {
            return false;
        }
        //check if input is greater than the amount of rows
        if (input > getRows(tempAud)){
            return false;
        }
        //check if input is less than or equal to 0
        if (input <= 0) {
            return false;
        }

        return true;
    }


    // function to display the report at end of program
    public static void displayReport(Auditorium<Seat> auditorium1, Auditorium<Seat> auditorium2, Auditorium<Seat> auditorium3)
    {
        int adults = 0, kids = 0, seniors = 0, periods = 0;
        Node<Seat> row = auditorium1.getFirst();
        Node<Seat> curr = auditorium1.getFirst();

        // traverse through every node and row and count every A, C and S
        while (row != null)
        {
            while (curr != null)
            {
                if (curr.getPayload().getTicketType() == 'A')
                    adults++;
                else if (curr.getPayload().getTicketType() == 'C')
                    kids++;
                else if (curr.getPayload().getTicketType() == 'S')
                    seniors++;
                else
                    periods++;
                curr = curr.getNext();               // move to next node
            }
            row = row.getDown();                    // move to next row
            curr = row;
        }
        float totalSales = (adults*10) + (kids*5) + (seniors*7.50f);
        System.out.print(
                String.format("%-22s", "Auditorium 1")
                        + String.format("%-12s", periods)
                        + String.format("%-12s", (adults+kids+seniors))
                        + String.format("%-12s", adults)
                        + String.format("%-12s", kids)
                        + String.format("%-12s", seniors)
                        + String.format("%2s", "$"));
        System.out.printf("%.2f\n", totalSales);

        // ticket counters for 2nd auditorium
        int adults2 = 0, kids2 = 0, seniors2 = 0, periods2 = 0;
        row = auditorium2.getFirst();
        curr = auditorium2.getFirst();

        // traverse through every node and row and count every A, C and S
        while (row != null)
        {
            while (curr != null)
            {
                if (curr.getPayload().getTicketType() == 'A')
                    adults2++;
                else if (curr.getPayload().getTicketType() == 'C')
                    kids2++;
                else if (curr.getPayload().getTicketType() == 'S')
                    seniors2++;
                else
                    periods2++;
                curr = curr.getNext();      // move to next node
            }
            row = row.getDown();            // move to next row
            curr = row;
        }
        float totalSales2 = (adults2*10) + (kids2*5) + (seniors2*7.50f);
        System.out.print(
                String.format("%-22s", "Auditorium 2")
                        + String.format("%-12s", periods2)
                        + String.format("%-12s", (adults2+kids2+seniors2))
                        + String.format("%-12s", adults2)
                        + String.format("%-12s", kids2)
                        + String.format("%-12s", seniors2)
                        + String.format("%2s", "$"));
        System.out.printf("%.2f\n", totalSales2);


        // ticket counters for 3rd auditorium
        int adults3 = 0, kids3 = 0, seniors3 = 0, periods3 = 0;
        row = auditorium3.getFirst();
        curr = auditorium3.getFirst();

        // traverse through every node and row and count every A, C and S
        while (row != null)
        {
            while (curr != null)
            {
                if (curr.getPayload().getTicketType() == 'A')
                    adults3++;
                else if (curr.getPayload().getTicketType() == 'C')
                    kids3++;
                else if (curr.getPayload().getTicketType() == 'S')
                    seniors3++;
                else
                    periods3++;
                curr = curr.getNext();      // move to next node
            }
            row = row.getDown();            // move to next row
            curr = row;
        }
        float totalSales3 = (adults3*10) + (kids3*5) + (seniors3*7.50f);
        System.out.print(
                String.format("%-22s", "Auditorium 3")
                + String.format("%-12s", periods3)
                + String.format("%-12s", (adults3+kids3+seniors3))
                + String.format("%-12s", adults3)
                + String.format("%-12s", kids3)
                + String.format("%-12s", seniors3)
                + String.format("%2s", "$"));
        System.out.printf("%.2f\n", totalSales3);

        // print out the total report
        System.out.print(
                String.format("%-22s", "Total")
                + String.format("%-12s", (periods+periods2+periods3))                                               // all periods
                + String.format("%-12s", (adults3+adults2+adults+kids3+kids2+kids+seniors3+seniors2+seniors))       // all tickets
                + String.format("%-12s", (adults3+adults+adults2))                                                  // all adults
                + String.format("%-12s", (kids3+kids+kids2))                                                        // all children
                + String.format("%-12s", (seniors3+seniors+seniors2))                                               // all seniors
                + String.format("%2s", "$"));
        System.out.printf("%.2f\n", (totalSales+totalSales2+totalSales3));                                          // all totals

    }

    // function that will validate the tickets the user entered
    public static boolean validTix(String input, Auditorium<Seat> tempAud)
    {
        //check if the input is an integer
        for (int i = 0; i < input.length(); i++)
        {
            if (!Character.isDigit(input.charAt(i)))
                return false;
        }

        int num;
        try {
            num = Integer.parseInt(input);
        } catch (NumberFormatException E)
        {
            return false;
        }

        //if it is a digit check if it is a whole number
        if (num % 1 != 0)
            return false;
        //check if number of tickets is negative
        if (num < 0)
            return false;
        return true;
    }

    // function to find the best seat available
    public static Node<Seat> bestAvailable(Auditorium<Seat> auditorium, int tickets)
    {
        Node<Seat> newSeat = null;
        float middleRow = (float)getRows(auditorium)/2.0f, centerSeat = (float)getSeats(auditorium)/2.0f;     // holds the center and middle of the auditorium

        if (centerSeat % 2 != 0)
            centerSeat++;

        System.out.println(centerSeat);
        int seatCounter = 1;
        // make seat counter 0 if ticket == 1, if this doesn't happen
        // it will give you the incorrect best available when someone only orders 1 ticket
        if (tickets == 1)
            seatCounter = 0;
        int bestRow = 0, rowCounter = 0;
        float middleDistance = 1100;        // made an arbitrarily large impossible number for distance to be
        float centerDistance = 1000;        // made an arbitrarily large impossible number for distance to be
        String bestSeatChar = " ", endingSeat = " ";
        Node<Seat> rowNode = auditorium.getFirst();
        Node<Seat> currNode = rowNode;

        while (rowNode != null)                 // start traversing from the first seat of the first row
        {
            rowCounter++;
            while (currNode != null)
            {
                seatCounter++;
                if (currNode.getPayload().getTicketType() == '.')
                {
                    int counter = 1;
                    Node<Seat> tempNode = currNode.getNext();
                    // if there is only 1 ticket being reserved, make tempNode curr
                    if (tickets == 1) {
                        counter = 0;
                        tempNode = currNode;
                    }

                    // check if there is enough sequential seats for every ticket the customer has
                    while (counter < tickets && tempNode != null)
                    {
                        // if one of the sequential seats aren't a '.' break the loop
                        if (tempNode.getPayload().getTicketType() != '.') {
                            break;
                        }
                        else
                        {
                            counter++;
                        }
                        // create the first best available, keep out if best available is populated
                        if (centerDistance == 1000 && middleDistance == 1100 && counter == tickets && seatCounter >= tickets) {
                            // check if the section has middle seat, if it does don't subtract tickets/2 from it
                            if (Math.abs(centerSeat - seatCounter) != 0 && Math.abs(centerSeat - seatCounter - (float)(tickets/2)) != 0)
                                centerDistance = Math.abs(centerSeat - seatCounter - (float)(tickets/2));
                            else
                                centerDistance = Math.abs(centerSeat - seatCounter);
                            middleDistance = Math.abs(middleRow - rowCounter);
                            newSeat = currNode;
                            bestSeatChar = String.valueOf(currNode.getPayload().getSeat());
                            endingSeat = getCharForNumber(seatCounter + (counter-2));
                            bestRow = rowCounter;
                        }
                        // create a new best available
                        else if (counter == tickets && middleDistance != 1100 && centerDistance != 1000)
                        {
                            float newCenterDist;
                            if (Math.abs(centerSeat - seatCounter) != 0 && Math.abs(centerSeat - seatCounter - (float)(tickets/2)) != 0)
                                newCenterDist = Math.abs(centerSeat - seatCounter - (float)(tickets/2));
                            else
                                newCenterDist = Math.abs(centerSeat - seatCounter);
                            float newMidDist = Math.abs(middleRow - rowCounter);
                            //check if the new center distance is closer to the center
                            if (newCenterDist <= centerDistance)
                            {
                                // check if the distance is equal and aren't the same row
                                if (newMidDist == middleDistance && bestRow < rowCounter && centerDistance == newCenterDist)
                                    // select the smaller row number and change nothing
                                    continue;
                                    // check if the distance is equal, if so select row closest to middle
                                else if (centerDistance == newCenterDist && newMidDist <= middleDistance)
                                {
                                    bestRow = rowCounter;
                                    // if it is change center & middle distance to it and change the best available
                                    middleDistance = newMidDist;
                                    newSeat = currNode;
                                    bestSeatChar = String.valueOf(currNode.getPayload().getSeat());
                                    endingSeat = getCharForNumber(seatCounter + (counter-2));
                                }
                                else if(newCenterDist < centerDistance)
                                {
                                    bestRow = rowCounter;
                                    // if it is change center & middle distance to it and change the best available
                                    centerDistance = newCenterDist;
                                    middleDistance = newMidDist;
                                    newSeat = currNode;
                                    bestSeatChar = String.valueOf(currNode.getPayload().getSeat());
                                    endingSeat = getCharForNumber(seatCounter + (counter-2));
                                }
                            }
                        }
                        // traverse to next seat
                        tempNode = tempNode.getNext();
                    }
                }
                // move to the next seat
                currNode = currNode.getNext();
            } // end of while loop used to traverse horizontally

            // move to the next row
            rowNode = rowNode.getDown();
            currNode = rowNode;
            seatCounter = 1;
            if (tickets == 1)
                seatCounter = 0;
        } // end of while loop to traverse vertically

        // after traversing and searching for the best seat available newSeat is still null
        if (newSeat == null)
        {
            System.out.println("\nno seats available");
            return newSeat;
        }
        // if there is only 1 ticket don't display an end seat
        if (tickets == 1)
        {
            System.out.println("best seats available: " + getRow_fromIndex(auditorium, bestRow).getPayload().getRow() + bestSeatChar);
            return newSeat;
        }
        // display best seat available to user and return it
        newSeat.getPayload().setRow(bestRow);
        System.out.println("best seats available: " + getRow_fromIndex(auditorium, bestRow).getPayload().getRow() + bestSeatChar
                + " - " + getRow_fromIndex(auditorium, bestRow).getPayload().getRow() +  endingSeat);
        return newSeat;
    }

    // helper function for best available
    public static Node<Seat> getRow_fromIndex(Auditorium<Seat> audi, int num)
    {
        int count = 1;
        Node<Seat> curr = audi.getFirst();
        while (count != num && curr != null)
        {
            count++;
            curr = curr.getDown();
        }
        return curr;
    }

    //function to return the number of seats in a row
    public static int getSeats(Auditorium<Seat> tempAud)
    {
        int count = 0;
        Node<Seat> curr = tempAud.getFirst();     //create a node to traverse the list
        //traverse across the row until the node is null
        while (curr != null)
        {
            count++;
            curr = curr.getNext();
        }

        return count;                       //return the count of seats
    }

    //function to return the number of rows
    public static int getRows(Auditorium<Seat> tempAud)
    {
        Node<Seat> curr = tempAud.getFirst();     //create a node to traverse the list
        int count = 0;
        //traverse down the list until curr is null
        while (curr != null)
        {
            count++;
            curr = curr.getDown();
        }

        return count;                       //return the count of the rows
    }

    //function that will validate the column the user entered
    public static boolean validSeatSelection(char input, Auditorium<Seat> tempAud)
    {
        //create a variable to hold onto the alphabetical numerical value of input
        int seatConverted = (int)input - 64;

        //check if what the user entered is between A and Z
        if (input < 'A' || input > 'Z')
        {
            //System.out.println("outside of range");
            return false;
        }
        //check if input is greater than the last seat of the row
        if (seatConverted > getSeats(tempAud))
            return false;
        //check if the user entered a non-letter
        if (!Character.isAlphabetic(input))
            return false;

        //check if the seat is out of bounds
        if (seatConverted > getSeats(tempAud)){
            return false;
        }

        return true;        //return true if all the checks failed
    }


    public static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    // function to reserve seats
    public static void reserveSeats(Auditorium<Seat> auditorium, int row, char seat ,int adultTickets, int childTickets, int seniorTickets)
    {
        int totalTickets = adultTickets+childTickets+seniorTickets;
        int count = 0;
        Node<Seat> curr, rowNode = auditorium.getFirst();

        while (rowNode.getPayload().getRow() != row)                    // move the rowNode to the row the user wants
        {
            rowNode = rowNode.getDown();
        }
        curr = rowNode;                                                 // move curr to row that rowNode is on
        while (curr.getPayload().getSeat() != seat)                     // move curr to the first seat the user wants
        {
            curr = curr.getNext();
        }

        int adultsSeated = 0, childrenSeated = 0, eldersSeated = 0;
        while (curr != null && count < totalTickets)                    // replace the periods with the customers tickets
        {
            if (adultsSeated < adultTickets)
            {
                curr.getPayload().setTicketType('A');                  // change the ticket type to A
                adultsSeated++;
            }
            else if (childrenSeated < childTickets)
            {
                curr.getPayload().setTicketType('C');                  // change ticket type to C
                childrenSeated++;
            }
            else if(eldersSeated < seniorTickets)
            {
                curr.getPayload().setTicketType('S');                  // change ticket type to S
                eldersSeated++;
            }

            curr = curr.getNext();                                     // move to next node
            count++;                                                   // increment count
        }

    }


} // end of public class main
