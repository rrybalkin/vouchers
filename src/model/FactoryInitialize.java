package model;

public class FactoryInitialize {
	private static Initialize initialize = new Initialize();
	
	public static Initialize getInit(){
		return initialize;
	}
}
