package spaceteroids.game_structure;

public class SimpleTypeWrapper {
	private int iValue;
	private boolean bValue;
	private double dValue;
	private long lValue;
	
	public SimpleTypeWrapper() {
		iValue = 0;
		bValue = false;
		dValue = 0;
		lValue = 0;
	}
	
	public SimpleTypeWrapper(int v) {iValue = v;}
	public SimpleTypeWrapper(boolean v) {bValue = v;}
	public SimpleTypeWrapper(double v) {dValue = v;}
	public SimpleTypeWrapper(long v) {lValue = v;}
	
	public int getIntValue() {return iValue;}
	public void setIntValue(int v) {iValue = v;}
	public boolean getBooleanValue() {return bValue;}
	public void setBooleanValue(boolean v) {bValue = v;}
	public double getDoubleValue() {return dValue;}
	public void setDoubleValue(double v) {dValue = v;}
	public long getLongValue() {return lValue;}
	public void setLongValue(long v) {lValue = v;}
}
