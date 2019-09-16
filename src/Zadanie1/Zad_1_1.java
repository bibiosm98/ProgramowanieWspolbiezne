package Zadanie1;
public class Zad_1_1 {
    long czas, ilosc_watkow = 100000, pewnyCzas = 10000 ,minimum = pewnyCzas, maximum = pewnyCzas;
    public Zad_1_1() throws InterruptedException {
        this.czas = 0;
        long start, stop, roznica;

        System.out.println("\n#1  --- Z pierwszym wątkiem");
        for(int i=0; i<ilosc_watkow; i++) {
            start = System.nanoTime();
            Thread t1 = new Thread(() -> {
            });
            stop = System.nanoTime();
            t1.join();
            roznica = stop-start;
//            if(roznica>1000000)System.out.println(roznica);
            czas += (roznica);
            if((roznica)<minimum) minimum=roznica;
            if(roznica>maximum) maximum=roznica;
        }
        System.out.println("Średni czas tworzenia wątków dla próby "+ilosc_watkow+" wątków = "+czas/ilosc_watkow+" nanosekund, czyli 10^-9, "+(double) (czas/ilosc_watkow)/1_000+"milisekund(y)");
        System.out.println("Min = "+minimum+" Max = "+maximum);

        czas=0;maximum=pewnyCzas;minimum=pewnyCzas;

        Thread t = new Thread(() -> {});
        for(int i=0; i<ilosc_watkow; i++) {
            start = System.nanoTime();
            Thread t1 = new Thread(() -> {
            });
            stop = System.nanoTime();
            t1.join();
            roznica = stop-start;
//            if(roznica>1000000)System.out.println(roznica);
            czas += (roznica);
            if((roznica)<minimum) minimum=roznica;
            if(roznica>maximum) maximum=roznica;
        }
        System.out.println("\n#2  --- Bez pierwszego wątku");
        System.out.println("Średni czas tworzenia wątków dla próby "+ilosc_watkow+" wątków = "+czas/ilosc_watkow+" nanosekund, czyli 10^-9, "+(double) (czas/ilosc_watkow)/1_000+"milisekund(y)");
        System.out.println("Min = "+minimum+" Max = "+maximum);
        System.out.println("\nDrukując każdy wyniki widać, że z czasem systematycznie malał czas utworzenia nowego wątku aż do około 1000 nanosekund");
        System.out.println("Prawdopodobnie jest to spowodowane zwiększaniem sie współczynnika \"cache-hit\"");
    }
}