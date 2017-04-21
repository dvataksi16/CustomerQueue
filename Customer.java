
/**
 * @author denisavataksi
 * Customer acts as the Node for the QueueLinkedList class
 */
public class Customer {

	private int idNumber;
	private int arrivalTime; //in seconds
	private int startingTime; // the time the customer starts being served--starts off as their arrival time and gets changed as necessary 
	private int waitingTime; //in seconds
	private Customer next;
	
	
	Customer(){}
	Customer(int idNumber, int arrivalTime){
		this.idNumber = idNumber;
		this.arrivalTime = arrivalTime;
	}
	/*
	 * getters and setters
	 */

	public void setNext(Customer next){
		this.next=next;
	}
	public Customer getNext(){
		return next;
	}
	public void setID(int idNumber){
		this.idNumber = idNumber;
	}
	public int getID(){
		return idNumber;
	}
	public void setArrivalTime(int arrivalTime){
		this.arrivalTime = arrivalTime;
	}
	public int getArrivalTime(){
		return arrivalTime;
	}
	public void setStartingTime(int startingTime){
		this.startingTime = startingTime;
	}
	public int getStartingTime(){
		return startingTime;
	}
	public void setWaitingTime(int waitingTime){
		this.waitingTime = waitingTime;
	}
	public void setWaitingTime(int arrivalTime, int startingTime){
		waitingTime = startingTime - arrivalTime;
	}
	public int getWaitingTime(){
		return waitingTime;
	}
	public void displayCustomer(){
		System.out.println("ID- NUMBER: " + idNumber + "\nARRIVAL TIME: " + arrivalTime);
	}
}
