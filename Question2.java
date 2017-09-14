import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Question2 implements Runnable{

    private static Lock lock = new ReentrantLock();
    @Override
    public void run() {
        doSomething();
    }

    public static void doSomething() {
        if (Thread.currentThread().getName().equals("Thread1")) {
            lock.lock();
            for (int i =0; i <10; i++) {
                System.out.print(Integer.toString(i) + " ");
            }
            System.out.println();
            lock.unlock();
        }
        else {
            lock.lock();
            for (int i =0; i <10; i++) {
                System.out.print(Integer.toString(i + 10) + " ");
            }
            System.out.println();
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(new Question2(), "Thread1");
        Thread t2 = new Thread(new Question2(), "Thread2");

        t1.start();
        t2.start();

    }
}
