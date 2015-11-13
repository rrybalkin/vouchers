package com.romansun.localization.engine;

public class ResourceDeterminant {
	public static final String RUSSIAN = "RU";
	public static final String ENGLISH = "EN";
	
	public String currentLocale = ENGLISH;
	
	public String resolveResource(long resourceId) {
		return resolveResource(resourceId, currentLocale);
	}
	
	public String resolveResource(long resourceId, String locale) {
		return null;
	}
	
	
	
	private ResourceDeterminant() {}
	private static volatile ResourceDeterminant instance;
	public static ResourceDeterminant getInstance() {
		if (instance == null) {
			synchronized (ResourceDeterminant.class) {
				instance = new ResourceDeterminant();
			}
		}
		
		return instance;
	}
}
