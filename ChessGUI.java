import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.concurrent.*;
import java.lang.Object;

public class ChessGUI extends JFrame implements MouseListener, MouseMotionListener, ActionListener {

  // Define class variables
  private static final long serialVersionUID = 1L; // Used for what??? 
  JLayeredPane layeredPane;
  JPanel chessBoard;
  JLabel chessPiece;
  int xAdjust;
  int yAdjust;
  Container initParent; // Parent of initial chess piece to move
  private int init_x; // Initial x-value for piece (before moving with mouse)
  private int init_y; // Initial y-value for piece (before moving with mouse)
  private ArrayList<ChessPiece> pieceLocations;
  private ArrayList<ChessPiece> validMovesUser = new ArrayList<ChessPiece>();
  private ArrayList<ChessPiece> validMovesComputer = new ArrayList<ChessPiece>();
  // Light-switch for the turn
  boolean imDone = false;
  boolean enablePress = false;
  boolean pieceKilled = false;
  int killLocX = 0;
  int killLocY = 0;
  // Timer GUI-specific variables 
  private JLabel timeLabel = new JLabel();
  public myTimer panel;
  public myTimer panel2;
 
  // Timer-specific variables
  private static final int ONE_SECOND = 1000;
  private int count = 6000000;
  private boolean isTimerActive = false;
  public Timer tmr = new Timer(ONE_SECOND, this);
  //Format time 
  private String formatTime(int count) {
      int hours = count / 3600;
      int minutes = (count-hours*3600)/60;
      int seconds = count-minutes*60;

      return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
  }
  
  void appendValidLocU(ArrayList<ChessPiece> userMoveTmp)
  {
	  validMovesUser.addAll(userMoveTmp);
  }
  
  void appendValidLocC(ArrayList<ChessPiece> compMoveTmp)
  {
	  validMovesComputer.addAll(compMoveTmp);
  }
  
  public ArrayList<ChessPiece> getPieceLoc()
  {
	  return pieceLocations;
  }
  
  public ArrayList<ChessPiece> getValidLocU()
  {
	  return validMovesUser;
  }
  
  public ArrayList<ChessPiece> getValidLocC()
  {
	  return validMovesComputer;
  }
  
  public void setPieceLoc(ArrayList<ChessPiece> acp)
  {
	  pieceLocations = acp;
  }
  
  public void setValidLocU(ArrayList<ChessPiece> acp)
  {
	  validMovesUser = acp;
  }
  
  public void setValidLocC(ArrayList<ChessPiece> acp)
  {
	  validMovesComputer = acp;
  }
  
  public void setEnable(boolean setVal)
  {
	  enablePress = setVal;
  }
  
  public void setDone(boolean setVal)
  {
	  imDone = setVal;
  }
  
  public void addValidLocU(ChessPiece p)
  {
	  validMovesUser.add(p);
  }
  
  public void addValidLocC(ChessPiece p)
  {
	  validMovesComputer.add(p);
  }
  
  // Constructor 
  public ChessGUI(Semaphore sem)
  {
	  Dimension boardSize = new Dimension(600, 700);
	  panel = new myTimer(true);
	  panel2 = new myTimer(false);
	  // Use Layered Pane for Chess GUI  
	  layeredPane = new JLayeredPane();
	  getContentPane().add(layeredPane);
	  layeredPane.setPreferredSize(boardSize);
	  layeredPane.addMouseListener(this);
	  layeredPane.addMouseMotionListener(this);

  	  timeLabel.setText(formatTime(count));
  	  timeLabel.setText("         ");

	  
	  // Timer GUI 
	  timeLabel = new JLabel();
	  timeLabel.setBorder(BorderFactory.createRaisedBevelBorder());
      layeredPane.add(panel.getPanel(), BorderLayout.SOUTH);
      panel.getPanel().setBounds(150, 10, 300, 150);
      layeredPane.add(panel2.getPanel(), BorderLayout.SOUTH);
      panel2.getPanel().setBounds(125, 650, 350, 150);
      //panel.setLayout(new BorderLayout());
      //panel.setBounds(0, 0, 5, 5);
      
      // Begin timer 
      count = 0; 
      isTimerActive = true;
	 
	  //Add a chess board to the Layered Pane 
	  chessBoard = new JPanel();
	  layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
	  chessBoard.setLayout( new GridLayout(8, 8) );
	  chessBoard.setPreferredSize( boardSize );
	  chessBoard.setBounds(0, 50, 600, 600);
	 
	  // Create initial board 
	  for (int i = 0; i < 64; i++) 
	  {
		  JPanel square = new JPanel( new BorderLayout()); // Create board square
		  chessBoard.add( square ); // Add square to board 
		  int row = (i / 8) % 2; // Get row 
		  if (row == 0) square.setBackground( i % 2 == 0 ? Color.GRAY : Color.CYAN );
		  else square.setBackground( i % 2 == 0 ? Color.CYAN  : Color.GRAY );
	  }
	  
	  //Add a few pieces to the board 
	  ArrayList<JLabel> pieceList = new ArrayList<>();
	  String s = System.getProperty("user.dir") + "\\src\\"; // Main file path 
	  /////////// INITIALIZE BASED ON THE USER DECISION ///////////
	  String s1 = s + "b"; // File path for all black strings
	  String s2 = s + "w"; // File path for all white strings
	  
	  // Put chess pieces in a list 
	  for(int i = 1; i <= 64; i++)
	  {
		  JLabel piece = new JLabel(); 
		  if(i <= 8) piece = new JLabel( new ImageIcon( s1 + i + ".png") ); // Valued black pieces
		  else if(i > 8 && i <= 16) piece = new JLabel( new ImageIcon( s1 + ".png") ); // Black pawns
		  else if(i >= 49 && i <= 56) piece = new JLabel( new ImageIcon( s2 + ".png") ); // White pawns
		  else if(i > 56 && i <= 64) piece = new JLabel( new ImageIcon( s2 + (65-i) + ".png") ); // Valued white pieces
		  pieceList.add(piece);
	  }
	  
	  // Create list of panels
	  ArrayList<JPanel> jpanelList = new ArrayList<>(); 
	  for(int i = 0; i < 64; i++)
	  {
		  JPanel myPanel = (JPanel)chessBoard.getComponent(i);
		  jpanelList.add(myPanel); 
	  }
	  
	  // Add chess piece to panels
	  for(int i = 0; i < pieceList.size(); i++)
	  {
		  jpanelList.get(i).add(pieceList.get(i));
	  }
	  
	    // Add all initial positions of chess pieces to pieceLocations! 
		pieceLocations = new ArrayList<ChessPiece>();
		for (int i = 0; i < 8; i++)
		{
			ChessPiece p1 = null; 
			for (int j = 0; j < 2; j++) // Computer's Pieces  
			{
				if (j == 0) // Delineate between the back line and the front line.
				{
					if (i == 0) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_rook0", false);						
					if (i == 7) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_rook1", false);						
					if (i == 1) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_knight0", false);						
					if (i == 6) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_knight1", false);						
					if (i == 2) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_bishop0", false);						 
					if (i == 5) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_bishop1", false);						 
					if (i == 4) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_king0", false);						 
					if (i == 3) p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_queen0", false); 
				}
				else p1 = new ChessPiece(i * 75, 75 * j + 50, "comp_pawn" + i, false); 
				pieceLocations.add(p1);
			}
			for (int j = 0; j < 2; j++) // User's Pieces
			{
				if (j == 1) // Delineate between the back line and the front line.
				{
					if (i == 0) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_rook0", true);						
					if (i == 7) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_rook1", true);						 
					if (i == 1) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_knight0", true);						 
					if (i == 6) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_knight1", true);						 
					if (i == 2) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_bishop0", true);						 
					if (i == 5) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_bishop1", true);						 
					if (i == 4) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_queen0", true);						
					if (i == 3) p1 = new ChessPiece(i * 75, 75 * j + 500, "user_king0", true);
				}
				else p1 = new ChessPiece(i * 75, 75 * j + 500, "user_pawn" + i, true);
				pieceLocations.add(p1);
			}
		}
  }
  
  // If user presses mouse
  public void mousePressed(MouseEvent e)
  {
	  if (!enablePress)
	  {
		  return;
	  }
	  chessPiece = null; // Initialize chess piece
	  Component c =  chessBoard.findComponentAt(e.getX(), e.getY() - 50); // Get piece coordinates
	  initParent = c.getParent(); // Set initial parent! (Put chess piece back here if invalid position specified) 
	  
	  if (c instanceof JPanel) return; // If user clicks on board (not chess piece) 
	 
	  // Determine coordinates of new point 
	  Point parentLocation = c.getParent().getLocation(); 
	  xAdjust = parentLocation.x - e.getX();
	  yAdjust = parentLocation.y - e.getY() + 50;
	  chessPiece = (JLabel)c;
	  
	  init_x = e.getX() + xAdjust;
	  init_y = e.getY() + yAdjust;
	  
	  chessPiece.setLocation(e.getX() + xAdjust, e.getY() + yAdjust); // Set new location 
	  chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight()); // Set size of chess piece
	  layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER); // Add piece to layered pane
  }
 
  // Move chess piece around board
  public void mouseDragged(MouseEvent me) 
  {
	  if (!enablePress)
	  {
		  return;
	  }
	  if (chessPiece == null) return; // If chess piece is not defined, return
	  chessPiece.setLocation(me.getX() + xAdjust, me.getY() + yAdjust); // Else set location
  }
 
  // Put chess piece back onto chess board
  public void mouseReleased(MouseEvent e) 
  {
	  if (!enablePress)
	  {
		  return;
	  }
	  if(chessPiece == null) return; // If chess piece is not defined, return
	  
	  chessPiece.setVisible(false); // Hide chess piece momentarily
	  Component c =  chessBoard.findComponentAt(e.getX(), e.getY() - 50); // Find chess piece
	  
	  // Is there another piece here enemy or friend.
	  Container parent = null;
	  if (c instanceof JLabel) // If mouse clicks on pre-existing chess piece (killing the piece!)
	  { 
		  parent = c.getParent();
	  }
	  else
	  {
		  parent = (Container) c;
	  }
	  // Checking if it is valid
	  boolean notValid = true;
	  for (int i = 0; i < validMovesUser.size(); i++) // Checking  if we have selected a valid move
	  {
		  if(!(e.getX() >= 0 && e.getX() <= 600 && e.getY() >= 50 && e.getY() <= 650)) // If not out of bounds 
		  {
			  initParent.add(chessPiece); // If out of bounds, put chess piece back in original spot		  
		  }
		  else
		  {
			  if ((validMovesUser.get(i).getX() == parent.getX()) && (validMovesUser.get(i).getY() == (parent.getY() + 50))) 
			  {
				  // Now we need to check if it is a valid move for the piece.
				  String pieceName = "";
				  for (int j = 0; j < pieceLocations.size(); j++)
				  {
					  // Now we need to see if the parent locations are the same as the current piecelocation's x and y value
					  if ((initParent.getX() == pieceLocations.get(j).getX()) && ((initParent.getY() + 50) == pieceLocations.get(j).getY())) 
					  {
						  pieceName = pieceLocations.get(j).getName();
					  }
				  }
				  if (!(validMovesUser.get(i).getName().compareTo(pieceName) == 0))
				  {				  
					  // If the piece is not the same then it is actually not valid.
					  continue;
				  }
				  notValid = false;
			  }
		  }
	  }
	  if (notValid)
	  {
		  initParent.add(chessPiece);
	  }
	  
	  if (!notValid) // If the move is valid 
	  {
		  if (c instanceof JLabel) // If mouse clicks on pre-existing chess piece (killing the piece!)
		  { 
			  parent = c.getParent();
			  parent.remove(0); // Remove previous chess piece! - Might need to edit later!
			  parent.add( chessPiece ); // Add new piece 
		  }
		  else // If mouse clicks on board space (empty square)
		  {
			  parent = (Container) c;
			  if(e.getX() >= 0 && e.getX() <= 600 && e.getY() >= 50 && e.getY() <= 650) // If not out of bounds 
			  {
				  parent.add( chessPiece ); // Add chess piece to board
			  }
			  else initParent.add(chessPiece); // If out of bounds, put chess piece back in original spot 
		  }
		  for(int i = 0; i < pieceLocations.size(); i++)
		  {
			  if(pieceLocations.get(i).getX() == init_x && pieceLocations.get(i).getY() == init_y)
			  {
				  // Now we need to remove a piece if it is at this location
				  // Removing the piece off the board if there is an issue
				  for (int k = 0; k < pieceLocations.size(); k++)
				  {
					  if (((parent.getX() == pieceLocations.get(k).getX()) && (((parent.getY() + 50) == pieceLocations.get(k).getY()))) && (pieceLocations.get(k).isType() != pieceLocations.get(i).isType())) 
					  {
						  killLocX = parent.getX();
						  killLocY = 50 + parent.getY();
						  // Setting the flag to true;
						  pieceKilled = true;
					  }
				  }
				  // Chess piece has been moved, so re-set its X-Y coordinates 
				  pieceLocations.get(i).setX(parent.getX());
				  pieceLocations.get(i).setY(50 + parent.getY());
				  imDone = true;
				  break;
			  }
		  }
	  }
	  chessPiece.setVisible(true); // Display chess piece 
  }
 
  public JPanel getChessBoard()
  {
	 return chessBoard;
  }
  
  public JLayeredPane getLayeredPane()
  {
	  return layeredPane;
  }
  
// Implement Inherited Abstract methods
  public void mouseClicked(MouseEvent e) {}
  public void mouseMoved(MouseEvent e) {}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e) {}
  public void actionPerformed(ActionEvent e) {}
 
}