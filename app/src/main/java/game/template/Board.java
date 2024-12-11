package game.template;

import java.util.Random;


public class Board {
	private double brick = 0;
	private double money = 500;
	private String[] cards = new String[52];
    private String[] myCards = new String[52];
    private String[] dealerCards = new String[52];
    private String[] chistory = new String[52];
    private int deck = 0;
    private int num1 = 0;
    private int num2 = 0;
    private int num3 = 0;
    private int nc = -1;
	public Board() {
    }

    public void hardMode(){
        money = 50;
    }
    public void cardDealer() {
        int count = 0;
        String temp = "";
        for (int i = 0; i < 52; i++) {
            if(i%4 == 0){
                count += 1;
            }
            temp = Integer.toString(count);

            if(i%4 == 0){
                temp = temp + "c";
            }
            else if(i%4 == 1){
                temp = temp + "d";
            }
            else if(i%4 == 2){
                temp = temp + "h";
            }
            else {
                temp = temp + "s";
            }
            cards[i] = temp;
            
        }
        deck = 52;
        for(int i = 0; i < 52; i++){
            chistory[i] = null;
        }
        num3 = 0;

    }
    public String[] getCards() {
        return cards;
    }
    public void win() {
        if(num1 == 2 && check21(myCards) == 21){
            money = money + (2.5 * brick);
            brick = 0;
        }
        else{
            money = money + (2 * brick);
            brick = 0;
        }
        
        
    }
    public void shuffleNdeal(){
        Random rand = new Random();

        while(myCards[1] == null){
            int x = rand.nextInt(52);
            if(cards[x] != null){
                myCards[num1] = cards[x];
                chistory[num3] = cards[x];
                cards[x] = null;
                num1 += 1;
                num3 += 1;
            }
        }
        while(dealerCards[1] == null){
            int x = rand.nextInt(52);
            if(cards[x] != null){
                dealerCards[num2] = cards[x];
                
                cards[x] = null;
                num2 += 1;
                
            }
        }
        chistory[num3] = dealerCards[0];
        num3 += 1;
        deck -= 4;
    }
    public void hit(){
        Random rand = new Random();
        if(nc == -1 || cards[nc] == null){
            int x = rand.nextInt(52);
            while(cards[x] == null){
                x = rand.nextInt(52);
            }
            myCards[num1] = cards[x];
            chistory[num3] = cards[x];
            cards[x] = null;
            deck -= 1;
            num1 += 1;
            num3 += 1;
        }
        else{
            myCards[num1] = cards[nc];
            chistory[num3] = cards[nc];
            cards[nc] = null;
            deck -= 1;
            num1 += 1;
            num3 += 1;
            nc = -1;
        }
    }
    public void dhit(){
        if(nc == -1 || cards[nc] == null){
            Random rand = new Random();
            int x = rand.nextInt(52);
            while(cards[x] == null){
                x = rand.nextInt(52);
            }
            dealerCards[num2] = cards[x];
            chistory[num3] = cards[x];
            cards[x] = null;
            deck -= 1;
            num2 += 1;
            num3 += 1;
        }
        else{
            dealerCards[num2] = cards[nc];
            chistory[num3] = cards[nc];
            cards[nc] = null;
            deck -= 1;
            num2 += 1;
            num3 += 1;
            nc = -1;
        }
    }
    public void bet(double x){
        brick = x;
        money -= brick;
    }
    public void lose(){
        brick = 0;
    }
    public void reset(){
        chistory[num3] = dealerCards[1];
        num3 += 1;
        for(int i = 0; i < 52; i++){
            myCards[i] = null;
            dealerCards[i] = null;
            
        }
        num1 = 0;
        num2 = 0;

    }
    public String[] getMyCards(){
        return myCards;
    }
    public String[] getDealerCards(){
        return dealerCards;
    }
    public int check21(String[] checkcards){
        int sum = 0;
        int aces = 0;
        boolean haveaces = false;
        int j = 0;
        int sumholder = 0;
        String[] tempdeck = new String[52];
        for(int i = 0; i < 52; i++){
            tempdeck[i] = checkcards[i];
        }
        while(checkcards[j] != null){
            tempdeck[j] = tempdeck[j].substring(0, tempdeck[j].length() - 1);
            if(tempdeck[j].equals("11") || tempdeck[j].equals("12") || tempdeck[j].equals("13")){
                sum += 10;
                sumholder += 10;
            }
            else if (tempdeck[j].equals("1")){
                if(sum + 11 > 21){
                    sum += 1;
                    aces += 1;
                }
                else{
                    aces += 1;
                }
            }
            else{
                sum += Integer.parseInt(tempdeck[j]);
                sumholder += Integer.parseInt(tempdeck[j]);
            }
            j += 1;
            
        }
        if (aces > 1){
            haveaces = true;

        }
        else
            for(int i = 0; i < aces; i++){
                if(sum + 11 > 21){
                    sum += 1;
                }
                else{
                    sum += 11;
                }
            }
        if(haveaces && sum > 21){
            sum = sumholder + aces;

        }
        return sum;
    }
    public int getDeck(){
        return deck;
    }
    public double getMoney(){
        return money;
    }
    public double getBrick(){
        return brick;
    }
    public int getNum1(){
        return num1;
    }
    public int getNum2(){
        return num2;
    }
    public void tie(){
        money = money + brick;
        brick = 0;
    }
    public void doubleDown(){
        money = money - brick;
        brick = brick * 2;
    }
    public int ah(){
        int sum = 0;
        int i = 0;
        int[] x = getChistory();
        while (x[i] != 0){
            if(x[i] < 7 && x[i] != 1){
                sum += 1;
            }
            else if(x[i] == 1 || x[i] > 9){
                sum -= 1;
            }
            i++;
        }
        return sum;
    }
    public int al(){
        int sum = 0;
        int i = 0;
        int[] x = getChistory();
        while(x[i] != 0){
            if(x[i] < 7 && x[i] != 1){
                sum -= 1;
            }
            else if(x[i] == 1 || x[i] > 9){
                sum += 1;
            }
            i++;
        }
        return sum;
    }
    public int getNum3(){
        return num3;
    }
    public int[] getChistory(){
        int[] temp = new int[52];
        int i = 0;
        while(chistory[i] != null){
            temp[i] = Integer.parseInt(chistory[i].substring(0, chistory[i].length() - 1));
            if(temp[i] > 10){
                temp[i] = 10;
            }
            i += 1;
        }
        return temp;
    }
    public void nextCard(){
        Random rand = new Random();
        int x = rand.nextInt(52);
        while(cards[x] == null){
            x = rand.nextInt(52);
        }
        nc = x; 

    }
    public int getNC(){
        return nc;
    } 
    public int check21NC(){
        return check21(myCards) + nc;
         
    }
    public int Uor2(){
        Random rand = new Random();
        int x = rand.nextInt(2);
        return x;
    }
    public void mreset(){
        money = 500;
    }
}

