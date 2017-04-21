
/**
 * @author denisavataksi
 */
import java.util.Scanner; 
import java.io.File;
import java.util.ArrayList;
import java.io.PrintWriter;

/* 
 * [0]customersfile.txt
 * [1]queriesfile.txt
 */

public class Program {

	public static void main(String[] args) throws Exception{
		/**
		 * reading in the customer file
		 */
		File file1 = new File(args[0]);
		
		Scanner input = new Scanner(file1);
		
		QueueLinkedList customers = new QueueLinkedList();
		
		final int CONSTANT_TIME = input.nextInt();
		int id = 0;
		
		while (input.hasNext()){
			String idInfo = input.nextLine();
			if(idInfo.contains("ID-NUMBER")){
				String[] idData = idInfo.split(":");
				//deleting white spaces and isolating the id number
				String number = "";
				for(int i = 0; i < idData[1].length(); i ++){
					if(Character.isDigit(idData[1].charAt(i))){
						number += idData[1].charAt(i);
					}
				}
				id = Integer.parseInt(number);
			}if(idInfo.contains("ARRIVAL-TIME")){	
				String[] timeInfo = idInfo.split(" ");
				String timeData = timeInfo[1];
				String[] timeArray = timeData.split(":");
				String time = "";
				int arrivalTime = 0;
				for (int i = 0; i<timeArray.length; i++){
					//assuming no one comes at 6 am or after 6 pm
					if(i == 0 && Integer.parseInt(timeArray[i]) < 6){
						int hour = Integer.parseInt(timeArray[i]) + 12;
						//converting to military time
						time += hour;
					}else
						time =  time + timeArray[i];
				}
				arrivalTime = militaryToSeconds(time);	
				Customer customer = new Customer(id,arrivalTime);
				customers.enqueue(customer);
			}	
		}
			
			/**
			 * serving the customers/ running through the customer queue
			 */
			int customerCount = 0; final int OPENING = 32400; final int CLOSING = 61200;
			int clock = OPENING; //time is kept track of as the day passes--starts at 9 am
			//keeps track of the number of people in queue
			ArrayList<Integer> waitingRoom = new ArrayList<Integer>();
			//keeps track of the break times
			ArrayList<Integer> breakRoom = new ArrayList<Integer>();
			//array list to keep a report of all the customers served-- such as their waiting time to be served
			ArrayList<Customer> customerReports = new ArrayList<Customer>(); 
			int queue = 0;
			Customer current = customers.dequeue();
			while(current != null){
				customerCount ++;
				//note break time-- will add 0 sec if no break 
				int breakTime = breakRoom(current, clock);breakRoom.add(breakTime);
				//if first customer is in before 9 am
				if(current.getArrivalTime() < OPENING && clock == OPENING){
					current.setStartingTime(clock);
					current.setWaitingTime(current.getArrivalTime(), current.getStartingTime());
					clock += CONSTANT_TIME;
				}//if it is too late to be served
				else if(clock > CLOSING){
					customerCount--; //not served
					if(current.getArrivalTime() < CLOSING){
						current.setWaitingTime(current.getArrivalTime(), CLOSING); //if customer comes in before 5 pm but is not served they still wait until 5pm
					}else{
						current.setWaitingTime(0);
					}
				}
				//the customers to follow that come early or at 9 am or if they come in during a busy time later on the day
				else if(current.getArrivalTime() <= clock){
					current.setStartingTime(clock);
					current.setWaitingTime(current.getArrivalTime(), current.getStartingTime());
					clock +=CONSTANT_TIME;
				}//customers come in during a slow time
				else if(current.getArrivalTime() > clock){
					current.setStartingTime(current.getArrivalTime());
					current.setWaitingTime(current.getArrivalTime(), current.getStartingTime());
					clock = (current.getArrivalTime() + CONSTANT_TIME);
				}
				//takes a count of the number of customers waiting on line at a given time
				queue =waitingRoom(current, clock); waitingRoom.add(queue);
				customerReports.add(current);
				//next
				current = customers.dequeue();
			}
			/**
			 * answering queries
			 */
			File file2 = new File(args[1]);
			input = new Scanner(file2);
			
			//writing query answers into a new output file
			PrintWriter output = new PrintWriter("output.txt");
			
			while(input.hasNext()){
				String query = input.nextLine();
				//WAITING-TIME-OF
				if(query.contains("WAITING-TIME-OF")){
					String[] waitingTimeOf = query.split(" ");
					int customerID = Integer.parseInt(waitingTimeOf[1]);
					output.println(query + ": " + getWaitingTime(customerID,customerReports));	
				}
				//NUMBER-OF-CUSTOMERS-SERVED
				if(query.contains("NUMBER-OF-CUSTOMERS-SERVED")){
					output.println(query + ": " + customerCount);
				}
				//LONGEST-BREAK-LENGTH
				if(query.contains("LONGEST-BREAK-LENGTH")){
					output.println(query +": "+ max(breakRoom));
				}
				//TOTAL-IDLE-TIME 
				if(query.contains("TOTAL-IDLE-TIME")){
					output.println(query + ": "+ total(breakRoom));
				}
				//MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME
				if(query.contains("MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME")){
					output.println(query + ": " + max(waitingRoom));
				}
			}
			input.close(); output.close();		
	}
	/**
	 * converts a given military time into seconds--everything is to be measured using seconds 
	 * @param militaryTime in the form HHMMSS or HMMSS
	 * @return the time in seconds 
	 */
	public static int militaryToSeconds(String militaryTime){
		int seconds = 0;
		int secondsPlace = Integer.parseInt(militaryTime.substring(militaryTime.length()-2));
		seconds += secondsPlace;
		int minutesPlace = Integer.parseInt(militaryTime.substring(militaryTime.length()-4, militaryTime.length()-2));
		seconds += (minutesPlace * 60);
		if(militaryTime.length() == 6){
			int hoursPlace = Integer.parseInt(militaryTime.substring(0,2));
			seconds += (hoursPlace * 3600);
		}else{
			int hoursPlace = Integer.parseInt(militaryTime.substring(0,1));
			seconds += (hoursPlace *3600);
		}
		return seconds;	
	}
	/**
	 * returns the amount of customers waiting on line to be served
	 * @param customer
	 * @param clock
	 * @return the amount of people waiting to be served at a given time
	 */
	public static int waitingRoom(Customer customer, int clock){
		int queue = 0;
		Customer current = customer.getNext();
		while(current != null){
			if(current.getArrivalTime() <= clock){
				queue++;
				current = current.getNext();
			}else
				break;
		}return queue;
	}
	/**
	 * calculates the break time if there are no customers at a given time
	 * @param customer
	 * @param clock
	 * @return time in seconds of a break taken
	 * @return 0 seconds if no break takes place
	 */
	public static int breakRoom(Customer customer, int clock){
		if(customer.getArrivalTime() < clock)
			return 0;
		int breakTime = customer.getArrivalTime() - clock;
		return breakTime;
	}
	/**
	 * takes the sum of all the ints in an array
	 * (adds up all the breaks to give the total time in seconds of idling) 
	 * @param breaks
	 * @return sum of all the ints in an array
	 */
	public static int total(ArrayList<Integer> arr){
		int total = 0;
		for(int a: arr){
			total += a;
		}return total;
	}
	/**
	 * gets the amount of time any customer waited
	 * @param id
	 * @param customerReports
	 * @return how long a customer waited to be served in seconds
	 */
	public static int getWaitingTime(int id, ArrayList<Customer> customerReports){
		int waitingTime = 0;
		for(int i = 0; i < customerReports.size(); i ++){
			if(customerReports.get(i).getID() == id){
				waitingTime = customerReports.get(i).getWaitingTime();
				break;
			}				
		}return waitingTime;
		
	}
	/**
	 * takes an array and returns the max/ the max amount of people waiting at any time/
	 * the longest break 
	 * @param waitingRoom
	 * @return the max int in a given array
	 */
	public static int max(ArrayList<Integer> arr){
		int max = 0;
		for(int i = 0; i < arr.size(); i++){
			if(arr.get(i) > max)
				max = arr.get(i);
		}return max;
	}
}






