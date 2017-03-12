package main;

import core.Checker;
import core.KimsufiServer;

public class Main {

	public static void main(String[] args) {
		Checker checker = new Checker(new KimsufiServer("160sk1"), 30000);
		checker.start();
	}

}
