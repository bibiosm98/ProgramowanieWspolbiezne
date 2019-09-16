package Zadanie2;
import java.util.Random;

public class Zad_2_1 {
    int rozmiar = 10_000_0000;
    int[] tablica = new int[rozmiar];
    public Zad_2_1() {
        losujWartosci();
        zliczSekwencyjnie();
        zliczWspółbieżnie();
    }
    public void losujWartosci(){
        Random los = new Random();
        for(int i=0; i<rozmiar; i++){
            tablica[i] = los.nextInt(90)+10;
        }
        System.out.println("Wylosowano "+rozmiar+" wartości dla tablicy\n\n");
    }
    public void zliczSekwencyjnie(){
        long suma =0;
        long start, stop;

        start = System.nanoTime();
        for(int i=0; i<rozmiar; i++){
            suma += tablica[i];
        }
        stop = System.nanoTime();
        System.out.println("Wynik = "+suma+" Uzyskany w czasie "+(stop-start));
    }
    public void zliczWspółbieżnie(){
        Counter licznik = new Counter();
        int dzielnik = 4, skala = (int) rozmiar/dzielnik;
        long start, stop;

        start = System.nanoTime();
        for(int i=0; i<dzielnik; i++){
            Runnable t = Podlicz(licznik, i, skala);
            (new Thread(t)).start();
        }

        while(licznik.getIncrement()<dzielnik){licznik.waitUntil();}
        stop = System.nanoTime();
        System.out.println("Wynik = "+licznik.getScore()+" Uzyskany w czasie "+(stop-start));
    }
    class Counter{
        int increment = 0;
        long suma = 0;
        public Counter() {
            System.out.println("Sumator-sekcja krytyczna");
        }
        public synchronized void add(long x){
            suma += x;
            increment++;
            notify();
        }
        public synchronized boolean waitUntil(){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        public long getScore(){
            return suma;
        }
        public int getIncrement(){
            return increment;
        }
    }

    public Runnable Podlicz(Counter licznik, int x, int skala){
        long suma = 0;
        int y = x*skala, z =(x+1)*skala;
        for(int i = y; i<z; i++){
            suma += tablica[i];
        }
        licznik.add(suma);
        return null;
    }
}
