package Zadanie1;

import java.util.Random;

public class Zad_1_3 {
    private static boolean[][] macierz;
    private static Counter counter = new Counter();
    final static boolean Nano = true;
    int n = 20000;
    public Zad_1_3() {
        System.out.println("Generuje macierz grafu o rozmiarze = "+n+"\n\n");
        macierz = new boolean[n][n];
        macierz = generujMacierzBool(macierz, n);
//        pokazMacierz(macierz, n);
        liczKrawedzieSekwencyjnie(n);
//        liczeKrawedzieWspolbierznie(n);
        liczeKrawedzieWspolbierznie2(n);
    }
    public static boolean[][] generujMacierzBool(boolean[][] macierz, int n){
        Random random = new Random();
        int i,j;
//        for (i=0;i<n; i++) {
//            for (j=0; j<n; j++) {
//                macierz[i][j] = false;
//                if(i==j) macierz[i][j] = false;
//            }
//        }
        for (i=0;i<n-1; i++) {
            for (j = i + 1; j < n; j++) {
                macierz[i][j] = random.nextBoolean();
            }
        }
        return macierz;
    }
    public static void pokazMacierz(boolean[][] macierz, int n){
        int i,j,k=0;
        for (i=0;i<n; i++) {
            for (j = 0; j < n; j++) {
                System.out.print(macierz[i][j]);
                if(!(j==n-1)) System.out.print("|");
            }
            System.out.println();
        }
    }

    public static void liczKrawedzieSekwencyjnie(int n){
        int ile = 0,i,j;
        long start,startNano, stop, stopNano;
        Counter counter = new Counter();
        System.out.println("Licze krawędzie Sekwencyjnie");
        startNano = System.nanoTime();
        for (i=0;i<n-1; i++) {
            for (j = i + 1; j < n; j++) {
                if(macierz[i][j])ile++;
            }
            counter.add(ile, 1);
            ile=0;
        }
        stopNano = System.nanoTime();
        if(Nano) System.out.println("Czas nanos: "+(stopNano-startNano)+" Ilosc krawedzi: "+counter.getScore());
    }

    public static void liczeKrawedzieWspolbierznie(int n){
        int i;
        long startNano,stopNano;
        System.out.println("Licze krawędzie Wielowątkowo");
        startNano = System.nanoTime();
        for (i=0;i<n; i++) {
            (new Thread(new WatekLiczeniaMacierzy(i, n))).start();
        }
        waitUntilThreadsEnd(counter, n);
        stopNano = System.nanoTime();
        if(Nano) System.out.println("Czas nanos: "+(stopNano-startNano));
        System.out.println("Ilosc krawedzi: "+counter.getScore());
    }
    private static class WatekLiczeniaMacierzy implements Runnable{
        private int i, n, ile = 0;
        public WatekLiczeniaMacierzy(int i, int n) {
            this.i = i;
            this.n = n;
        }
        public void run(){
            for (int j = i + 1; j < n; j++) {
                if(macierz[i][j]) ile++;
            }
            counter.add(ile, 1);
        }
    }

    public static void liczeKrawedzieWspolbierznie2(int n){
        System.out.println("\nLicze krawędzie Wielowątkowo2");

        int i, skok = 500, najlepszySkok = 1, ostatniSkok = 1, licz = 0;
//        if(n>100)ostaniSkok = 100;
//        if(n>1000)ostatniSkok = 1000;
//        if(n>10000)ostatniSkok = 2000;

        long startNano,stopNano, czas = Long.MAX_VALUE;
        while(true) {
            licz = 0;

            startNano = System.nanoTime();
            for (i = 0; i < n; i += skok) {
                (new Thread(new WatekLiczeniaMacierzy2(i, i + skok, n))).start();
            }
//            for(i = najlepszySkok; i<n; i++){
//                for(int j = i+1; j<n; j++){
//                    if(macierz[i][j])licz++;
//                }
//            }
//            counter.add(licz);
            waitUntilThreadsEnd(counter, n);
            stopNano = System.nanoTime();
//            System.out.println("Czas nanos: " + (stopNano - startNano)+" Ilosc krawedzi: " + counter.getScore());

            counter.zeruj();
            System.out.print(skok+",");
            skok++;

            if(czas>(stopNano-startNano)){
                czas = stopNano-startNano;
                najlepszySkok = skok;
            }
            skok = 10000;
            if(skok == 10000) break;
        }
        System.out.println("\n\nNajlepszy czas = " + czas+" Dla skoku = "+najlepszySkok+" Przy macierzy o rozmiarze "+n+"x"+n);
        counter.zeruj();

//        Czas nanos: 389866576 Ilosc krawedzi: 99992138
//        Najlepszy czas = 212158533 Dla skoku = 1223 Przy macierzy o rozmiarze 20000x20000
//        Przy około 500 działa najskuteczniej

    }

    private static class WatekLiczeniaMacierzy2 implements Runnable{
        private int i, y, n, ile = 0;
        public WatekLiczeniaMacierzy2(int i, int y, int n) {
            this.i = i;
            this.n = n;
            this.y = y;
        }
        public void run(){
            int x = y-i;
            for(i = i; i<y; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (macierz[i][j]) ile++;
                }
            }
            counter.add(ile, x);
        }
    }

    public static boolean waitUntilThreadsEnd(Counter counter, int n){
        synchronized (counter){
            while(counter.getDodania()<n){
                try {
                    counter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }return false;}
}

class Counter{
    private int licznik = 0, dodania = 0;
    public synchronized void add(int x, int y){
        licznik+=x;
        dodania+=y;
        notify();
    }
    public synchronized int getScore(){
        return licznik;
    }
    public synchronized int getDodania() {
        return dodania;
    }
    public synchronized void zeruj(){
        dodania = 0;
        licznik = 0;
    }
}

//    public static char[][] generujMacierzChar(char[][] macierz, int n){
//        Random random = new Random();
//        int i,j;
//        for (i=0;i<n; i++) {
//            for (j=0; j<n; j++) {
//                macierz[i][j] = 48;
//                if(i==j) macierz[i][j] = 35;
//            }
//        }
//
//        for (i=0;i<n-1; i++) {
//            for (j = i + 1; j < n; j++) {
//                macierz[i][j] = (char) (Math.round(random.nextInt(2))+48);
//            }
//        }
//        return macierz;
//    }