package Lab1Level2Task3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RW {

    public static void main(String[] args) throws Exception {
        Data d = new Data();

        ExecutorService es = Executors.newFixedThreadPool(5); // ExecutorService ex - исполнитель задач
        for (int i = 0; i < 5; i++)
            es.submit(new WorkWData(d));

        TimeUnit.SECONDS.sleep(3);
        es.shutdown(); // shutdown() в Java используется для завершения выполнения исполнителя задач (ExecutorService)
        es.awaitTermination(10000, TimeUnit.MILLISECONDS); // awaitTermination() используется для блокировки текущего потока до тех пор, пока все задачи, отправленные исполнителю задач (ExecutorService), не завершат свое выполнение, или пока не истечет указанный интервал времени
    }
}

class WorkWData implements Runnable {
    Data obj;

    WorkWData(Data d) {
        obj = d;
    }

    public void run() {
        int n;
        n = obj.read();
        System.out.println("First read" + " " + Thread.currentThread().getName() + " " + new Integer(n).toString());
        obj.write();
        System.out.println("Write ... " + " " + Thread.currentThread().getName() + " " + new Integer(n).toString());
        n = obj.read();
        System.out.println("Second read" + " " + Thread.currentThread().getName() + " " + new Integer(n).toString());
    }
}

class Data {
    private int count = 0;

    // ReadWriteLock позволят множеству потоков одновременно читать общий ресурс, но только одному потоку писать в него
    // ReentrantReadWriteLock позволяет множеству потоков читать данные, пока нет потока, который пытается записать данные
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock rl = lock.readLock(); // lock.readLock() возвращает блокировку для чтения из объекта lock
    private Lock wl = lock.writeLock(); // lock.writeLock() возвращает блокировку для записи из объекта lock


    //    synchronized int read() { // synchronized сильно замедляет работу метода write()
    int read() {
        rl.lock();
        try {
            int n = count;
            TimeUnit.MILLISECONDS.sleep(400);
            count = n;
        } catch (InterruptedException ex) {
        } finally {
            rl.unlock();
        }
        return count;
    }
        //}


//        synchronized void write() { // synchronized сильно замедляет работу метода write()
    void write() {
        wl.lock();
        try {
            int n = count;
            TimeUnit.MILLISECONDS.sleep(400);
            n++;
            count = n;
        } catch (InterruptedException ex) {
        } finally {
            wl.unlock();
        }
    }
        //}
}





