
public class ChessPiece
{
	private int x;
	private int y;
	private String name;
	private boolean type;
	
	public ChessPiece(int x, int y, String name, boolean type)
	{
		super();
		this.x = x;
		this.y = y;
		this.name = name;
		this.type = type;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public String getName()
	{
		return name;
	}
	public boolean isType()
	{
		return type;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setType(boolean type)
	{
		this.type = type;
	}
	
}
