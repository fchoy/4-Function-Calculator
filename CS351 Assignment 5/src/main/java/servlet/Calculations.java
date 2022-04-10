package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class Calculations
 */
public class Calculations {
	private static final long serialVersionUID = 1L;
	int firstNum, secondNum;
	String operation;
       
	public double calculate(double firstNum, double secondNum, String operation) {
		if(operation == "+") {
			return firstNum + secondNum;
		}

		else if(operation == "-") {
			return firstNum - secondNum;
		}
		
		else if(operation == "*") {
			return firstNum * secondNum;
		}
		
		else if(operation == "/") {
			return firstNum / secondNum;
		}
		
		else return 0;
		
	}

}
