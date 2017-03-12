package core;

import java.util.Date;

import javax.sound.sampled.LineUnavailableException;

public class Checker {
	private int stepMillis;
	private boolean start = true;
	private KimsufiServer server;
	private Thread starter;
	
	public Checker(KimsufiServer server, int stepMillis) {
		this.server = server;
		this.starter = new Thread(getChecker());
		this.stepMillis = stepMillis;
	}
	
	public synchronized void start() {
		start = true;
		starter.start();
		System.out.println("STARTED");
	}
	
	public synchronized void stop() {
		start = false;
		starter.interrupt();
		System.out.println("STOPPED");
	}
	
	private Runnable getChecker() {
		return new Runnable() {
			
			@Override
			public void run() {
				while(start) {
					if(server.isAvailable()) {
						System.out.println(">>> AVAILABLE ! - " + new Date().toString());
						try {
							SoundUtils.laser(50);
						} catch (LineUnavailableException | InterruptedException e) {
							e.printStackTrace();
						}
						break;
					} else {
						System.out.println("> UNAVAILABLE - " + new Date().toString());
					}
					try {
						Thread.sleep(stepMillis);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		};
	}
}
