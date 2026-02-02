package sem4.cal.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import sem4.cal.test.service.CalculatorService;


class CalculatorApplicationTests 
{
	private final CalculatorService calculatorTest = new CalculatorService();
	

    @Test
    void shouldReturnTotalSum() {
        String result = calculatorTest.calculate("2", "4", "+");
        assertEquals("6.0", result);
    }

    @Test
    void shouldReturnErrorForInvalidInput() {
        String result = calculatorTest.calculate("a", "4", "+");
        assertEquals("Error: Inputs must be valid numbers", result);
    }
	
	
	

    

	

}
