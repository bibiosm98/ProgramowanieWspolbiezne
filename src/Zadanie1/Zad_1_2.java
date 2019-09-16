package Zadanie1;

import java.util.ArrayList;
import java.util.List;

public class Zad_1_2 {
    public Zad_1_2() {
        System.out.println("Tworzenie pustych wątków przy udziale wyrażenia lambda jest bezcelowe, pomieważ są automatycznie zwalniane z pamieci, \ndlatego nowo utworzone wątki dodaję do tablicy ArrayList. Na dostępne 16GB pamięci RAM udaje się zapełnić do 5,5GB co przekłada się na ponad 8,2mln pustych wątków, \nkompilator rzuca wyjątek przepełniena pamięci.");
        List<Thread> watki = new ArrayList<>();
        int i = 0;
        while(true){
            i++;
            Thread t = new Thread();
            watki.add(t);
            if(i%100000==0) System.out.println(i);
            if(i>8200000)break;
        }
    }
}