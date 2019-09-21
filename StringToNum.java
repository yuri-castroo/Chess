int[] convertMoveToCoordinates(String message)
	{
		int coordList[] = new int[4]; // Arraylist containing coordinates (origX, origY, finalX, finalY)
		String initPos = message.substring(0, 2); // Original position 
		String finalPos = message.substring(3, 5); // Final position 

        Character origYStr = initPos.charAt(0); // Get y-coordinate of original string 
        int origXStr = Character.getNumericValue(initPos.charAt(1)); // Get x-coordinate 
		int origY = ((origYStr - 'A'))*75; // Convert from letter -> coordinate
		int origX = 650-(75*origXStr); // Convert from number -> coordinate
		coordList[0] = origX; // Add coordinate to list
		coordList[1] = origY; 
		
        Character finalYStr = finalPos.charAt(0); // Get y-coordinate of final string   
        int finalXStr = Character.getNumericValue(finalPos.charAt(1)); // Get x-coordinate of final string
		int finalY = ((finalYStr - 'A'))*75;  // Convert from letter -> coordinate
		int finalX = 650-(75*finalXStr);  // Convert from number -> coordinate
 		coordList[2] = finalX; // Add coordinate to list 
 		coordList[3] = finalY; 
		
		return coordList; 
		
	}