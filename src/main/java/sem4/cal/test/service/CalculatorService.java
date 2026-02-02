package sem4.cal.test.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService 
{

	public String calculate(String a, String b, String operator) 
	{
		// TODO Auto-generated method stub
		double x;
        double y;

        try {
            x = Double.parseDouble(a);
            y = Double.parseDouble(b);
        } 
        catch (NumberFormatException e) 
        {
            return "Error: Inputs must be valid numbers";
        }

        return switch (operator) {
            case "+" -> String.valueOf(x + y);
            case "-" -> String.valueOf(x - y);
            case "*" -> String.valueOf(x * y);
            case "/" -> {
                if (y == 0) yield "Error: Division by zero";
                yield String.valueOf(x / y);
            }
            default -> "Error: Invalid operator";
        };
	}

	
	

}
