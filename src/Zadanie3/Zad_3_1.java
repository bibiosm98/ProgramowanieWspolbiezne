package Zadanie3;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Zad_3_1 {
    boolean sprawdzaj = true;
    public Zad_3_1() throws InterruptedException {
        Bufor bufor = new Bufor();
        Producent producent = new Producent(bufor);
        Konsument konsument = new Konsument(bufor,"aaz");

        Thread producentThread = new Thread(producent);
        Thread konsumentThread = new Thread(konsument);

        producentThread.start();
        konsumentThread.start();

        producentThread.join();
        konsumentThread.join();

    }
    class Konsument implements Runnable{
        Bufor bufor;
        String hasło;
        public Konsument(Bufor bufor, String hasło) {
            this.bufor = bufor;
            this.hasło = hasło;
        }
        @Override
        public void run() {
            while(sprawdzaj){
                if(hasło.equals(bufor.getPassword())){
                    sprawdzaj = false;
                    System.out.println("Znaleziono hasło: " + hasło);
                 //   bufor.obudz();
                    break;
                }
                synchronized (bufor){
                    bufor.czekaj();
                }
            ///    System.out.println("Konsument");
            }
            System.out.println("Konsument STOP");
        }
    }



    class Producent implements Runnable{
        Random ran = new Random();
        Bufor bufor;
        List<String> alfabet = new ArrayList<>(Arrays.asList((new String[]{
                "!", "0","1","2","3","4","5","6","7","8","9",
                "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
                "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
        })));
        int size = alfabet.size(), hasłoSize = 1;
        List<Integer> wyraz = new ArrayList<>();
        String hasło = "";
        public Producent(Bufor bufor) {
            this.bufor = bufor;
            wyraz.add(0);
        }

        @Override
        public void run() {
            while(sprawdzaj){
                if(bufor.password==null){
                    losujHasło();
                    bufor.putPassword(hasło);
                }
                synchronized (bufor){
                    bufor.czekaj();
                }
         //       System.out.println("Producent");
            }
            System.out.println("Producent STOP");
        }
        public String losujHasło(){
            losujTabliceInt();
            hasło = "";
            for(int i=0; i<wyraz.size(); i++){
            //    System.out.println(alfabet.get(wyraz.get(i)));
                hasło += alfabet.get(wyraz.get(i));
            }
            System.out.println(hasło);
            return  hasło;
        }
        public void losujTabliceInt() {
            // wydlużanie hasła o pushFront jedna literę
            if(wyraz.get(0).equals(alfabet.size()-1)){
                List<Integer> wyraz2 = new ArrayList<>();
                wyraz2.add(0);
                wyraz2.addAll(wyraz);
                wyraz = wyraz2;
                wyraz.set(wyraz.size()-1, 0);
                System.out.println("Rozmiar hasła = "+wyraz.size());
            }

            int wyrazSize = wyraz.size()-1, alfabetSize = alfabet.size()-1;
            boolean a = false;
            while (true) {
                if (wyraz.get(wyrazSize).equals(alfabetSize)) {
                    wyraz.set(wyrazSize, 0);
                    wyrazSize--;
                }else{
                    wyraz.set(wyrazSize, wyraz.get(wyrazSize)+1);
                    a = true;
                }
                if(wyrazSize<0 || a){break;}

            }
        }
    }
}

class Bufor{
    String password;
    public Bufor() {}

    public synchronized  boolean putPassword(String pass){
        if(password==null){
            password = pass;
        }else{
            return false;
        }
        notify();
        return true;
    }
    public synchronized String getPassword(){
        String a = password;
        password = null;
        notify();
        return a;
    }
    public void czekaj(){
        notify();
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void obudz(){
        notify();
    }
}