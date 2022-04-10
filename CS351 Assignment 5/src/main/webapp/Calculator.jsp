<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Calculator</title>
	<script src="script.js"></script>
	<link href="styles.css" rel="stylesheet">
</head>
<body>
	<div class="calculator-div">
		<h2>Calculator</h2>
		<form action="Helper" name="calculator" class="calculator-form" onsubmit="return validate();">
			<input type="text" id="num-input" name="number-input" value="${value}" required>
			<br/>
			<br/>
			<input type="submit" name="clear" value="clear">
			<input type="submit" name="clear-all" value="clear all">
			<input type="submit" name="add" value="+">
			<input type="submit" name="sub" value="-">
			<input type="submit" name="mul" value="*">	
			<input type="submit" name="div" value="/">
		</form>	
		<p>${trail}</p>
	</div>
</body>
</html>