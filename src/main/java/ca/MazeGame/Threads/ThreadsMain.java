package ca.MazeGame.Threads;

import ca.MazeGame.model.MazeGame;

public class ThreadsMain {
    private static MazeGame mazeGame;
    private static MoveCatTask moveCatTask;
    private static DUPListener dupListenerTask;

    private static Thread moveCatThread;
    private static Thread udpThread;

    boolean general_isShuttingDown = false;


    public static void processGame(MazeGame mazeGame) {
        ThreadsMain.mazeGame = mazeGame;
        startCatThread();
        //        while (!general_isShuttingDown) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        catStopMoving();
    }

    public static void startUPD() {
        dupListenerTask = new DUPListener();
        udpThread = new Thread(dupListenerTask);
        udpThread.start();
    }

    private static void startCatThread() {
        moveCatTask = new MoveCatTask(mazeGame);
        moveCatThread = new Thread(moveCatTask);
        moveCatThread.start();
    }

    public static void increaseCatSpeed(MazeGame game) {
        moveCatTask = new MoveCatTask(game);
        moveCatTask.decreaseTimeInterval();
    }

    public static void decreaseCatSpeed(MazeGame game) {
        moveCatTask = new MoveCatTask(game);
        moveCatTask.increaseTimeInterval();
    }

    public void stopCatThread() {
        moveCatTask.setThreadStop(true);
    }
}
