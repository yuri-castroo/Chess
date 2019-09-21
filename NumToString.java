// Input: Array of integers (origX, origY, finalX, finalY) 
	// Output: Valid coordinate string (Ex: E4 E5) 
	String convertCoordinatesToMove(int[] coordinates)
	{
        String message = ""; // Message to output 
        
		Character origY = (char)(coordinates[0]/75 + 65); 
		int origX = (650-coordinates[1])/75; 
		
		Character finalY = (char)(coordinates[2]/75 + 65); 
		int finalX = (650-coordinates[3])/75; 
		
		message += origY + "" + origX + " " + finalY + "" + finalX; 
		return message; 
			
	}