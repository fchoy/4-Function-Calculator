package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Helper
 */
public class Helper extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private double firstNum, secondNum; //used to hold the two numbers for calculations
	private boolean firstNumSet, secondNumSet, equals; //used to keep track if both numbers aren't invalid
	private StringBuilder build; //used to keep track of the "paper trail"
	private String operation; //used to keep track of the current operation.
    private int inARow = 0; //keeps track of number of current operation pressed, mainly used to figure out if an operation should be appended to the trail.  
    private Calculations calculate; //object of class that does the calculations.
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Helper() {
        super();
        // TODO Auto-generated constructor stub
        build = new StringBuilder();
        firstNumSet = false;
        secondNumSet = false;
        operation = "";
        calculate = new Calculations();
        equals = false;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//if the clear button is pressed, ask JSP to clear only the text box field, keep the trail. 
		if(request.getParameter("clear") != null) {
			request.getSession().setAttribute("value", 0);
			request.getSession().setAttribute("trail", build.toString());
			request.getRequestDispatcher("Calculator.jsp").forward(request, response);
		}
		
		//if the all clear button is pressed, JSP will clear everything
		else if(request.getParameter("clear-all") != null) {
			build.setLength(0); //erase the string builder
			firstNum = 0; 
			secondNum = 0;
			firstNumSet = false;
			secondNumSet = false;
			operation = "";
			inARow = 0;
			
			request.getSession().setAttribute("value", 0);
			request.getSession().setAttribute("trail", build.toString());
			request.getRequestDispatcher("Calculator.jsp").forward(request, response);
		}
		
		/**Attempted to make a equals sign button **/
		/** 
		//for only displaying the result without additional calculations (do not reset inARow or else we will be missing operations in trail)
		else if(request.getParameter("equals") != null){
			//if first value is not set, then do nothing.
			if(!firstNumSet || operation == "") {
				request.getSession().setAttribute("value", request.getParameter("number-input")); //send the same value in the text box into the session
				request.getSession().setAttribute("trail", build.toString()); 
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
			}
			//else if first value is set
			else if(firstNumSet && operation != "") {
				equals = true;
				if(equals == true && inARow < 1) {
					build.setLength(build.length()-2); //remove the last sign
					build.append(operation + " "); //if we click the equal sign multiple times, do the same operation
				}
				//get second value from text box
				secondNum = Double.parseDouble(request.getParameter("number-input"));
				//set firstNum calculate value based on current operation selected 
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				//append second num, equal sign, and result to string builder
				build.append(secondNum + " ");
				build.append("= ");
				build.append(firstNum + " ");
				//send the result to session
				request.getSession().setAttribute("value", firstNum);
				//send trail to session
				request.getSession().setAttribute("trail", build.toString()); 
				//send to jsp and display
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);	
				equals = false;
			}
		}**/
		
		//for calculations, we need to setAttribute the operation type and the values to send to the servlet. 
		else if(request.getParameter("add") != null) { 
			//case 1: someone presses the plus button a second time in a row
			if(firstNumSet == true && secondNumSet == false && operation == "+") {
				if(inARow >= 2) {
					build.append("+ "); //clicking plus button >2 times
				}				
				//place number in input into secondNum
				secondNum = Double.parseDouble(request.getParameter("number-input"));
				build.append(secondNum + " ");
				//then calculate the two values, send firstNum, secondNum, and operation
				Calculations calculate = new Calculations();
				//place the calculated value into firstNum
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				//reset secondNumSet
				secondNumSet = false;
				//add = to string builder
				build.append("= ");
				//add calculated value to string builder
				build.append(firstNum + " ");
				request.getSession().setAttribute("value", firstNum);
				request.getSession().setAttribute("trail", build.toString());	
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++;
			}
			
			//case 2: plus button pressed for first time and no other operation was before it
			else if(!firstNumSet && !secondNumSet && operation != "+") {
				//save number to firstNum
				firstNum = Double.parseDouble(request.getParameter("number-input"));
				//firstNumSet becomes true
				firstNumSet = true;
				operation = "+";
				//add first num to string builder
				build.append(firstNum + " "); 
				//add + to string builder
				build.append("+ ");
				//clear the input value, set attribute of {value} to ""
				request.getSession().setAttribute("value", "");
				//send string builder to jsp
				request.getSession().setAttribute("trail", build.toString());
				//forward to jsp
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++; 
			}
			
			//case 3 : switching operations
			else if(firstNumSet && operation != "+") { //if we already have a value saved, and we want to switch operations
				inARow = 1;
				//first calculate the previous operation saved
				secondNum = Double.parseDouble(request.getParameter("number-input")); //get value on screen
				build.append(secondNum + " "); //append the second number to builder 
				build.append("= "); //append equal to builder
				
				//next, calculate the result from the previous operation (before switching operations)
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				build.append(firstNum + " "); //append result 
				request.getSession().setAttribute("value", firstNum); //add result to session 
				
				//now, we switch operations and add operation to end of trail
				secondNumSet = false; //reset secondNumSet so that if we do the same operation again in a row, we can run the first if statement.
				operation = "+";
				build.append("+ ");
				request.getSession().setAttribute("trail", build.toString()); //add trail to session 
				
				//display it
				request.getRequestDispatcher("Calculator.jsp").forward(request, response); 
				
				//do not increment inARow if we are switching operations, leave at 1. Prevents missing operations in trail.
				//Also because we add the operation to the end of the trail, it will prevent an additional one from showing up.
			}
			
		}
		
		else if(request.getParameter("sub") != null){
			//case 1: someone presses the plus button a second time in a row
			if(firstNumSet == true && secondNumSet == false && operation == "-") {
				if(inARow >= 2) {
					build.append("- "); //clicking plus button >2 times
				}				
				//place number in input into secondNum
				secondNum = Double.parseDouble(request.getParameter("number-input"));
				build.append(secondNum + " ");
				//then calculate the two values, send firstNum, secondNum, and operation
				//place the calculated value into firstNum
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				//reset secondNumSet
				secondNumSet = false;
				//add = to string builder
				build.append("= ");
				//add calculated value to string builder
				build.append(firstNum + " ");
				request.getSession().setAttribute("value", "");
				request.getSession().setAttribute("trail", build.toString());	
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++;
			}
			
			//case 2: plus button pressed for first time and no other operation was before it
			else if(!firstNumSet && !secondNumSet && operation != "-") {
				//save number to firstNum
				firstNum = Double.parseDouble(request.getParameter("number-input"));
				//firstNumSet becomes true
				firstNumSet = true;
				operation = "-";
				//add first num to string builder
				build.append(firstNum + " "); 
				//add + to string builder
				build.append("- ");
				//clear the input value, set attribute of {value} to ""
				request.getSession().setAttribute("value", "");
				//send string builder to jsp
				request.getSession().setAttribute("trail", build.toString());
				//forward to jsp
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++; 
			}
			
			//case 3 : switching operations
			else if(firstNumSet && operation != "-") { //if we already have a value saved, and we want to switch operations
				inARow = 1;
				//first calculate the previous operation saved
				secondNum = Double.parseDouble(request.getParameter("number-input")); //get value on screen
				build.append(secondNum + " "); //append the second number to builder 
				build.append("= "); //append equal to builder
				
				//next, calculate the result from the previous operation (before switching operations)
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				build.append(firstNum + " "); //append result 
				request.getSession().setAttribute("value", firstNum); //add result to session 
				
				//now, we switch operations and add operation to end of trail
				secondNumSet = false; //reset secondNumSet so that if we do the same operation again in a row, we can run the first if statement.
				operation = "-";
				build.append("- ");
				request.getSession().setAttribute("trail", build.toString()); //add trail to session 
				
				//display it
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				
				//do not increment inARow if we are switching operations, leave at 1. Prevents missing operations in trail.
				//Also because we add the operation to the end of the trail, it will prevent an additional one from showing up.			
				}
		}
		
		//for calculations, we need to setAttribute the operation type and the values to send to the servlet. 
		else if(request.getParameter("mul") != null) { 
			//case 1: someone presses the plus button a second time in a row
			if(firstNumSet == true && secondNumSet == false && operation == "*") {
				if(inARow >= 2) {
					build.append("* "); //clicking plus button >2 times
				}				
				//place number in input into secondNum
				secondNum = Double.parseDouble(request.getParameter("number-input"));
				build.append(secondNum + " ");
				//then calculate the two values, send firstNum, secondNum, and operation
				Calculations calculate = new Calculations();
				//place the calculated value into firstNum
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				//reset secondNumSet
				secondNumSet = false;
				//add = to string builder
				build.append("= ");
				//add calculated value to string builder
				build.append(firstNum + " ");
				request.getSession().setAttribute("value", firstNum);
				request.getSession().setAttribute("trail", build.toString());	
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++;
			}
			
			//case 2: plus button pressed for first time and no other operation was before it
			else if(!firstNumSet && !secondNumSet && operation != "*") {
				//save number to firstNum
				firstNum = Double.parseDouble(request.getParameter("number-input"));
				//firstNumSet becomes true
				firstNumSet = true;
				operation = "*";
				//add first num to string builder
				build.append(firstNum + " "); 
				//add + to string builder
				build.append("* ");
				//clear the input value, set attribute of {value} to ""
				request.getSession().setAttribute("value", "");
				//send string builder to jsp
				request.getSession().setAttribute("trail", build.toString());
				//forward to jsp
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++; 
			}
			
			//case 3 : switching operations
			else if(firstNumSet && operation != "*") { //if we already have a value saved, and we want to switch operations
				inARow = 1;
				//first calculate the previous operation saved
				secondNum = Double.parseDouble(request.getParameter("number-input")); //get value on screen
				build.append(secondNum + " "); //append the second number to builder 
				build.append("= "); //append equal to builder
				
				//next, calculate the result from the previous operation (before switching operations)
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				build.append(firstNum + " "); //append result 
				request.getSession().setAttribute("value", firstNum); //add result to session 
				
				//now, we switch operations and add operation to end of trail
				secondNumSet = false; //reset secondNumSet so that if we do the same operation again in a row, we can run the first if statement.
				operation = "*";
				build.append("* ");
				request.getSession().setAttribute("trail", build.toString()); //add trail to session 
				
				//display it
				request.getRequestDispatcher("Calculator.jsp").forward(request, response); 
				
				//do not increment inARow if we are switching operations, leave at 1. Prevents missing operations in trail.
				//Also because we add the operation to the end of the trail, it will prevent an additional one from showing up.		
			}
			
		}
		
		else if(request.getParameter("div") != null){
			
			//case 1: someone presses the plus button a second time in a row
			if(firstNumSet == true && secondNumSet == false && operation == "/") {
				if(inARow >= 2) {
					build.append("/ "); //clicking plus button >2 times
				}				
				//place number in input into secondNum
				secondNum = Double.parseDouble(request.getParameter("number-input"));
				build.append(secondNum + " ");
				//then calculate the two values, send firstNum, secondNum, and operation
				//place the calculated value into firstNum
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				//reset secondNumSet
				secondNumSet = false;
				//add = to string builder
				build.append("= ");
				//add calculated value to string builder
				build.append(firstNum + " ");
				request.getSession().setAttribute("value", "");
				request.getSession().setAttribute("trail", build.toString());	
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++;
			}
			
			//case 2: plus button pressed for first time and no other operation was before it
			else if(!firstNumSet && !secondNumSet && operation != "/") {
				//save number to firstNum
				firstNum = Double.parseDouble(request.getParameter("number-input"));
				//firstNumSet becomes true
				firstNumSet = true;
				operation = "/";
				//add first num to string builder
				build.append(firstNum + " "); 
				//add + to string builder
				build.append("/ ");
				//clear the input value, set attribute of {value} to ""
				request.getSession().setAttribute("value", "");
				//send string builder to jsp
				request.getSession().setAttribute("trail", build.toString());
				//forward to jsp
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				inARow++; 
			}
			
			//case 3 : switching operations
			else if(firstNumSet && operation != "/") { //if we already have a value saved, and we want to switch operations
				inARow = 1;
				//first calculate the previous operation saved
				secondNum = Double.parseDouble(request.getParameter("number-input")); //get value on screen
				build.append(secondNum + " "); //append the second number to builder 
				build.append("= "); //append equal to builder
				
				//next, calculate the result from the previous operation (before switching operations)
				firstNum = calculate.calculate(firstNum, secondNum, operation);
				build.append(firstNum + " "); //append result 
				request.getSession().setAttribute("value", firstNum); //add result to session 
				
				//now, we switch operations and add operation to end of trail
				secondNumSet = false; //reset secondNumSet so that if we do the same operation again in a row, we can run the first if statement.
				operation = "/";
				build.append("/ ");
				request.getSession().setAttribute("trail", build.toString()); //add trail to session 
				
				//display it
				request.getRequestDispatcher("Calculator.jsp").forward(request, response);
				
				//do not increment inARow if we are switching operations, leave at 1. Prevents missing operations in trail.
				//Also because we add the operation to the end of the trail, it will prevent an additional one from showing up.		
			}
			
		}
		
	}
		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
