import java.awt.Component;
import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.*;
import javax.swing.JLabel;
import java.util.*;

public class Game
{
	// Data members of the game
	// Constructor
	private ChessGUI myframe; 
	Game(Semaphore mySem) 
	{
		myframe = new ChessGUI(mySem);
	}
	
	private void generatePawnMoves(ChessPiece p)
	{
		ArrayList<ChessPiece> arrChessPieces = myframe.getPieceLoc();
		boolean flagInFront = false;		
		for (int i = 0; i < arrChessPieces.size(); i++)
		{
			// Checking if there is any piece in front
			if ((arrChessPieces.get(i).getX() == p.getX()) && (arrChessPieces.get(i).getY() == (p.getY() - 75)))
			{
				flagInFront = true; // There is a piece in front of the pawn!
			}
			// Determine whether any enemy pieces are diagonal to the piece.
			int diff = -75;
			for (int j = 0; j < 2; j++)
			{
				if(j == 1) diff = 75;
				if ((arrChessPieces.get(i).getX() == (p.getX() + diff)) && (arrChessPieces.get(i).getY() == (p.getY() - 75)))
				{
					if (!arrChessPieces.get(i).isType() == p.isType())
					{
						// Determine whether piece type is valid for a user or computer.
						ChessPiece p2 = new ChessPiece(p.getX() + diff, p.getY() - 75, p.getName(), p.isType());
						if (p2.isType()) myframe.addValidLocU(p2);
						else myframe.addValidLocC(p2);
					}
				}
			}
		}
		if (!flagInFront) // If no pieces in front of pawn 
		{
			ChessPiece p2 = new ChessPiece(p.getX(), p.getY() - 75, p.getName(), p.isType());
			if (p2.isType()) myframe.addValidLocU(p2);
			else myframe.addValidLocC(p2);
		}
	}
	
	private void generateKnightMoves(ChessPiece p)
	{
		ArrayList<ChessPiece> arrChessPieces = myframe.getPieceLoc(); // Get array of piece locations
		ArrayList<ChessPiece> moveArr = new ArrayList<ChessPiece>(); // Temporary array of moves
		boolean pieceType = false; // If computer piece
		if(p.isType() == true) pieceType = true; // If user piece 
		
		String nm = p.getName(); 
		
		if(p.isType() == true) // Add all possible 8 knight moves
		{ 
			moveArr.add(new ChessPiece(p.getX()+75, p.getY()-150, nm, pieceType)); 
			moveArr.add(new ChessPiece(p.getX()+150, p.getY()-75, nm, pieceType)); 
			moveArr.add(new ChessPiece(p.getX()+150, p.getY()+75, nm, pieceType)); 
			moveArr.add(new ChessPiece(p.getX()+75, p.getY()+150, nm, pieceType)); 
			moveArr.add(new ChessPiece(p.getX()-75, p.getY()+150, nm, pieceType)); 
			moveArr.add(new ChessPiece(p.getX()-150, p.getY()+75, nm, pieceType)); 
			moveArr.add(new ChessPiece(p.getX()-150, p.getY()-75, nm, pieceType)); 
			moveArr.add(new ChessPiece(p.getX()-75, p.getY()-150, nm, pieceType)); 			
		}
		
		Boolean[] boolArr = new Boolean[8]; // Boolean array to mark invalid moves 
		Arrays.fill(boolArr, Boolean.FALSE);
		for(int j = 0; j < arrChessPieces.size(); j++)
		{
			for (int i = 0; i < moveArr.size(); i++) 
			{
				if(arrChessPieces.get(j).getX() == moveArr.get(i).getX() && arrChessPieces.get(j).getY() == moveArr.get(i).getY()) // If same coordinates
				{
					if(arrChessPieces.get(j).isType() == pieceType) boolArr[i] = true; // If pieces are of same type -> Invalid user position 								
				}
			}
		}
		// Add all valid moves to user/computer list 
		for(int i = 0; i < moveArr.size(); i++)
		{
			if(!boolArr[i]) // If move is valid 
			{
				if(p.isType() == true) myframe.addValidLocU(moveArr.get(i)); // Add to list of user moves
				if(p.isType() == false) myframe.addValidLocC(moveArr.get(i)); // Add to list of computer moves
			}
		}
	}
	
	// Helper function for generateRookMoves() and generateBishopMoves()
	private boolean generateRookMovesHelper(ArrayList<ChessPiece> moveArr, ChessPiece p2, int myX, int myY)  
	{
		boolean exists = false; 
		ArrayList<ChessPiece> arrChessPieces = myframe.getPieceLoc(); // Get array of piece locations
		for(int i = 0; i < arrChessPieces.size(); i++) // Search for pre-existing piece in pieceLocations 
		{
			if(arrChessPieces.get(i).getX() == myX && arrChessPieces.get(i).getY() == myY) // If coordinates match 
			{
				// 1. If enemy piece, add to list! (Can kill)
				// 2. If piece of same type, then don't add.
				// 3. Either way - a piece obstructs the rook's path and the rook cannot go further. Break. 
				if(arrChessPieces.get(i).isType() != p2.isType()) // If enemy piece 
				{
					moveArr.add(p2); // Add to list if enemy piece (can kill piece) 
				}
				exists = true; // A piece obstructs rook's path and rook cannot go further. Break. 
				break;
			}
		}
		if(!exists) // If position is not already taken, add position to list!
		{
			moveArr.add(p2); // Add position to list of possible moves
			return true; // Success! 
		}
		// Else, quit function if position is taken by another piece  
		else return false; // Failure!
		
	}
	
	private void generateRookMoves(ChessPiece p)
	{
		ArrayList<ChessPiece> moveArr = new ArrayList<ChessPiece>(); // Temporary array of moves
		boolean pieceType = false; // If computer piece
		if(p.isType() == true) pieceType = true; // If user piece 
		
		int myX = p.getX();
		int myY = p.getY();
		while(myY >= 0) // Go vertically upwards
		{
			myY -= 75;
			ChessPiece p2 = new ChessPiece(p.getX(), myY, p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, p2.getX(), myY)) break;  
		}
		myY = p.getY();
		while(myY <= 650) // Go vertically downwards
		{
			myY += 75;
			ChessPiece p2 = new ChessPiece(p.getX(), myY, p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, p2.getX(), myY)) break;  
		}
		while(myX >= 0) // Go horizontally upwards
		{
			myX -= 75;
			ChessPiece p2 = new ChessPiece(myX, p.getY(), p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, myX, p2.getY())) break;  
		}
		myX = p.getX();
		while(myX <= 600) // Go horizontally downwards
		{
			myX += 75;
			ChessPiece p2 = new ChessPiece(myX, p.getY(), p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, myX, p2.getY())) break;  
		}
		
		if(pieceType == true) myframe.appendValidLocU(moveArr); // Add to list of user moves
		if(pieceType == false) myframe.appendValidLocC(moveArr); // Add to list of computer moves
	}
	
	private void generateBishopMoves(ChessPiece p)
	{
		ArrayList<ChessPiece> moveArr = new ArrayList<ChessPiece>(); // Temporary array of moves
		boolean pieceType = false; // If computer piece
		if(p.isType() == true) pieceType = true; // If user piece 
		
		int myX = p.getX();
		int myY = p.getY();
		while(myY >= 0 && myX >= 0) // Go NorthWest
		{
			myY -= 75;
			myX -= 75;
			ChessPiece p2 = new ChessPiece(myX, myY, p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, myX, myY)) break;  
		}
		myX = p.getX();
		myY = p.getY();
		while(myY <= 650 && myX >= 0) // Go SouthWest
		{
			myY += 75;
			myX -= 75;
			ChessPiece p2 = new ChessPiece(myX, myY, p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, myX, myY)) break;  
		}
		myX = p.getX();
		myY = p.getY();
		while(myY <= 650 && myX <= 600) // Go SouthEast
		{
			myY += 75;
			myX += 75;
			ChessPiece p2 = new ChessPiece(myX, myY, p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, myX, myY)) break;  
		}
		myX = p.getX();
		myY = p.getY();
		while(myY >= 0 && myX <= 600) // Go NorthEast
		{
			myY -= 75;
			myX += 75;
			ChessPiece p2 = new ChessPiece(myX, myY, p.getName(), pieceType);
			if(!generateRookMovesHelper(moveArr, p2, myX, myY)) break;  
		}
		
		if(pieceType == true) myframe.appendValidLocU(moveArr); // Add to list of user moves
		if(pieceType == false) myframe.appendValidLocC(moveArr); // Add to list of computer moves
	}
	
	private void generateQueenMoves(ChessPiece p)
	{
		generateRookMoves(p); // Vertical + Horizontal
		generateBishopMoves(p); // Diagonal 
	}
	
	private void generateKingMoves(ChessPiece p)
	{
		
	}
	
	// Generate moves for a given piece.
	private ArrayList<ChessPiece> genValidMoves(ChessPiece p)
	{
		// given a piece to work with.
		System.out.println("Name: " + p.getName());
		
		if ((p.getName().compareTo("user_pawn0") == 0) || (p.getName().compareTo("comp_pawn0")) == 0) generatePawnMoves(p);
		if ((p.getName().compareTo("user_pawn1") == 0) || (p.getName().compareTo("comp_pawn1")) == 0) generatePawnMoves(p);
		if ((p.getName().compareTo("user_pawn2") == 0) || (p.getName().compareTo("comp_pawn2")) == 0) generatePawnMoves(p);
		if ((p.getName().compareTo("user_pawn3") == 0) || (p.getName().compareTo("comp_pawn3")) == 0) generatePawnMoves(p);
		if ((p.getName().compareTo("user_pawn4") == 0) || (p.getName().compareTo("comp_pawn4")) == 0) generatePawnMoves(p);
		if ((p.getName().compareTo("user_pawn5") == 0) || (p.getName().compareTo("comp_pawn5")) == 0) generatePawnMoves(p);
		if ((p.getName().compareTo("user_pawn6") == 0) || (p.getName().compareTo("comp_pawn6")) == 0) generatePawnMoves(p);
		if ((p.getName().compareTo("user_pawn7") == 0) || (p.getName().compareTo("comp_pawn7")) == 0) generatePawnMoves(p);
		if (p.getName().compareTo("user_knight0") == 0 || p.getName().compareTo("comp_knight0") == 0) generateKnightMoves(p);
		if (p.getName().compareTo("user_knight1") == 0 || p.getName().compareTo("comp_knight1") == 0) generateKnightMoves(p);
		if (p.getName().compareTo("user_rook0") == 0 || p.getName().compareTo("comp_rook0") == 0) generateRookMoves(p);
		if (p.getName().compareTo("user_rook1") == 0 || p.getName().compareTo("comp_rook1") == 0) generateRookMoves(p);
		if (p.getName().compareTo("user_bishop0") == 0 || p.getName().compareTo("comp_bishop0") == 0) generateBishopMoves(p);
		if (p.getName().compareTo("user_bishop1") == 0 || p.getName().compareTo("comp_bishop1") == 0) generateBishopMoves(p);
		if (p.getName() == "user_queen0" || p.getName() == "user_queen1") generateQueenMoves(p);
		if (p.getName() == "user_king0" || p.getName() == "user_king1") generateKingMoves(p);
		
		return null;
	}
	
	// This function handles all of the user moves that can be done, generating a list of valid
	// moves for the GUI to allow that the player move.
	// Note: Synchronized function!
	public synchronized void userMove(Semaphore mySem)
	{
		myframe.panel2.tmr.start();
		ArrayList<ChessPiece> arrChessPiece = myframe.getPieceLoc();
		ArrayList<ChessPiece> emptyPieces1 = new ArrayList<ChessPiece>();
		myframe.setValidLocU(emptyPieces1);
		for (int i = 0; i < arrChessPiece.size(); i++)
		{
			this.genValidMoves(arrChessPiece.get(i));
		}
		// Wait for piece locations to change.
		myframe.setEnable(true);
		while(!myframe.imDone) 
		{
			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		myframe.setEnable(false);
		ArrayList<ChessPiece> emptyPieces2 = new ArrayList<ChessPiece>();
		myframe.setValidLocU(emptyPieces2);
		myframe.panel2.tmr.stop();
	}
	// This function handles all of the computer moves, it generates a list of valid moves for the
	// AI to use as input and handles moving the piece visually.
	public void computerMove()
	{
		myframe.panel.tmr.start();
		// Check if a piece was killed and remove it.
		if (myframe.pieceKilled)
		{
			// Now we set the piece at the given location to be either removed or not visible.
			Component c1 = myframe.getChessBoard().findComponentAt(myframe.killLocX, myframe.killLocY);
			// Now we can remove the component at the correct location
			myframe.layeredPane.remove(c1);
			// Now removing the killed piece from the board.
			ArrayList<ChessPiece> arrPieces = myframe.getPieceLoc();
			// now removing the c1 value.
			for (int i = 0; i < arrPieces.size(); i++)
			{
				// Now checking for the piece that has the same location as the kill locations 
				// It also needs to be a black piece killed
				if (((myframe.killLocX == arrPieces.get(i).getX()) && (myframe.killLocY == arrPieces.get(i).getY())) && (arrPieces.get(i).isType() == false))
				{
					// Now we can remove from the list.
					arrPieces.remove(i);
					// Setting the new set of pieces
					myframe.setPieceLoc(arrPieces);
					// We are finished now, worst case O(n) best case O(1).
					break;
				}
			}
			myframe.pieceKilled = false;
		}
		// Simulating a computer turn.
		try
		{
			Thread.sleep(20);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myframe.setDone(false);
		myframe.panel.tmr.stop();
	}
	
	// Main function 
	public static void main(String[] args) 
	{
		// Creating a Semaphore to use.
		Semaphore mySem = new Semaphore(0);		
		// Figure out whose turn it is - Computer or User
		System.out.println("Would you like to play white or black? (Enter 0 for white or 1 for black)");
		int decision = 0;
		try
		{
			decision = System.in.read(); // FIXME: Insert error-checking!
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Creating a new Game
		Game myGame = new Game(mySem);
		// Instantiate module 
		myGame.myframe.pack();
		myGame.myframe.setResizable(true);
		myGame.myframe.setLocationRelativeTo( null );
		myGame.myframe.setVisible(true); // Display board
		  
		// Computer Turn - lock all computer pieces from player interaction.
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				JLabel chessPiece = null;
				Component c1 = myGame.myframe.getChessBoard().findComponentAt(75 * i,  75 * j + 50);
				Point parentLocation1 = c1.getParent().getLocation(); 
				int xAdjust1 = parentLocation1.x;
				int yAdjust1 = parentLocation1.y;
				chessPiece = (JLabel)c1;
				// Set the location of the piece rending it immovable.
				chessPiece.setLocation(xAdjust1, 50 + yAdjust1);
				chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight()); // Set size of chess piece
				// myGame.myframe.getLayeredPane().add(chessPiece, JLayeredPane.DRAG_LAYER);
			}
		}
		// need to loop while the game isn't over
		boolean gameOver = false;
		decision = decision - 48;		
		while(!gameOver)
		{
			if (decision == 0)
			{
				myGame.userMove(mySem);
			}
			else
			{
				myGame.computerMove();				
			}
			decision = 1 - decision;
		}
	  }

}
