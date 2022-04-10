/**
 * Javascript function used to validate the input. Only submits the form if the value is a number.
 */

function validate(){
	let x = document.forms["calculator"]["number-input"].value;
			
	if(isNaN(x)){ //if not a number, display alert.
		alert("The input entered is not a number, please enter a valid number.");
		return false;
	}	
}