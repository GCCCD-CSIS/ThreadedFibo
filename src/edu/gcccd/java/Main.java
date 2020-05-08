package edu.gcccd.java;

import java.util.Stack;

public class Main {

    static Stack<MyFib> stack = new Stack<>();

    static long fib(final long f) {
        if (f <= 1)
            return f;
        else
            return fib(f - 1) + fib(f - 2);
    }

    static long multithreaded_fib(final long f) {
        if (f <= 1)
            return f;
        else if (!stack.empty()) {
            final MyFib myFib1 = stack.pop();
            final MyFib myFib2 = stack.pop();
            myFib1.set(f-1);
            myFib2.set(f-2);

            myFib1.start();
            myFib2.start();

            try {
                myFib1.join();
                myFib2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return myFib1.get() + myFib2.get();
        } else {
            return fib(f - 1) + fib(f - 2);
        }
    }

    public static void main(String[] args) {
        final long X = 45;


        // 1st time very direct

        long t0 = System.currentTimeMillis();
        System.out.println(fib(X));
        System.out.println("Time (ms) needed: " + (System.currentTimeMillis() - t0));

        // 2nd time wrapper around static fib

        t0 = System.currentTimeMillis();
        MyFib myFib = new MyFib();
        myFib.set(X);
        myFib.run();
        System.out.println(myFib.get());
        System.out.println("Time (ms) needed: " + (System.currentTimeMillis() - t0));

        // 3rd time

        for (int i = 0; i < MyFib.NumberOfInstances; i++) {
            stack.push(new MyFib());
        }

        t0 = System.currentTimeMillis();
        System.out.println(multithreaded_fib(X));
        System.out.println("Time (ms) needed: " + (System.currentTimeMillis() - t0));
    }

    static class MyFib extends Thread {
        static int NumberOfInstances = 10000;
        long f;
        long result;

        void set(long f) {
            this.f = f;
        }

        public void run() {
            result = fib(this.f);
        }

        long get() {
            return this.result;
        }
    }
}
