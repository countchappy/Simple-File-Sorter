package main.point.start.handling;

import main.point.start.handling.Append;
import main.point.start.handling.ArgHandling;

public class CatchHandling {
    public static void error(Exception e) {
        if (ArgHandling.printEr) {
            Append.out(String.valueOf(e.toString()) + " happend in: " + e.getClass());
            e.printStackTrace();
        }
    }
}

