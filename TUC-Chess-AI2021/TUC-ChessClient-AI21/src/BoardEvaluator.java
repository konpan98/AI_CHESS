import java.util.ArrayList;

public class BoardEvaluator {
//    public int myColor;
    public String[][] board;
    public static int rows = 7;
    public static int columns = 5;
    ArrayList<Location>  whites;
    ArrayList<Location>  blacks;
    Location whiteKing, blackKing;
    public BoardEvaluator() {

    }

    public int evaluate(String[][] board, int myColor){
        whites = new ArrayList<>();
        blacks = new ArrayList<>();
        this.board = board;
        int[] info = getInfo();
        int[] prizeDistance = distanceFromPrize();
        int[] min_distance = distanceFromEnd();
        int[] kingPoints = checkKing();
        int[] dinstanceFromKings = distanceFromKing();
        int[] underAttack = underAttack();

        int eval = 0;
        if (myColor == 0) {
            eval = 5*(Math.abs(info[0] + info[2] ) - Math.abs(info[1] + info[3]  ) )  -5*(underAttack[0]-underAttack[1]) -5*(min_distance[0]-min_distance[1])  -5*(dinstanceFromKings[0] - dinstanceFromKings[1])  + 1*(prizeDistance[0]-prizeDistance[1]) -5*( kingPoints[0] - kingPoints[1])  ;
            //eval = 1*(Math.abs(info[0] + info[2] ) - Math.abs(info[1] + info[3]  ) ) +(dinstanceFromKings[0] - dinstanceFromKings[1]);

        }
        else{
            eval = 5*(Math.abs(info[1] + info[3]) - Math.abs(info[0] + info[2]))  -5*(underAttack[1]-underAttack[0]) -5*(min_distance[1]-min_distance[0]) -5*(dinstanceFromKings[1] - dinstanceFromKings[0])  + 1*(prizeDistance[1]-prizeDistance[0])  -5*(kingPoints[1] - kingPoints[0]);
            //eval = 1*(Math.abs(info[1] + info[3]) - Math.abs(info[0] + info[2])) +(dinstanceFromKings[1] - dinstanceFromKings[0]);

        }
//        System.out.println("White: " + prizeDistance[0] + ", " + min_distance[0] + ", " + kingPoints[0] + " Black: " + prizeDistance[1] + ", " + min_distance[1] + ", " + kingPoints[1]);
        return eval;
    }

    //	0 -> whites, 1->blacks
    public int[] getInfo(){
        int[] info = new int[4];
        String firstLetter = "";
        String secondLetter = "";
        int valueWhite = 0;
        int valueBlack =0;
        int whites = 0;
        int blacks = 0;

        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){

                try {
                    firstLetter = Character.toString(board[i][j].charAt(0));
                    secondLetter = Character.toString(board[i][j].charAt(1));
                    if(firstLetter.equals("B")){
                        if(secondLetter.equals("P")){
                            valueBlack += 1;
                        }
                        else if(secondLetter.equals("R")){
                            valueBlack += 3;
                        }else if(secondLetter.equals("K")){
                            valueBlack += 7;
                        }
                        blacks++;

                    }else if(firstLetter.equals("W")){
                        if(secondLetter.equals("P")){
                            valueWhite += 1;
                        }
                        else if(secondLetter.equals("R")){
                            valueWhite += 3;
                        }else if(secondLetter.equals("K")){
                            valueWhite += 7;
                        }
                        whites++;
                    }
                }catch (StringIndexOutOfBoundsException e){

                }


            }
        }

        info[0] = whites;
        info[1] = blacks;
        info[2] = valueWhite;
        info[3] = valueBlack;

        return info;
    }

    //public int[] kingnext(){
      //  int whiteScore,blackScore;
        //oso pio konta toso perisoterous pontous
    //}

    public int[] checkKing(){

         ArrayList<Location> blackNext = new ArrayList<>();
         if(!(blackKing.x==0)) blackNext.add(new Location(blackKing.x-1,blackKing.y));
         if(!(blackKing.x==5)) blackNext.add(new Location(blackKing.x+1,blackKing.y));
         if(!(blackKing.y==6)) blackNext.add(new Location(blackKing.x,blackKing.y+1));
         blackNext.add(new Location(blackKing.x-1,blackKing.y+1));
         blackNext.add(new Location(blackKing.x+1,blackKing.y+1));
         ArrayList<Location> whiteNext = new ArrayList<>();
         whiteNext.add(new Location(whiteKing.x-1,whiteKing.y));
         whiteNext.add(new Location(whiteKing.x+1,whiteKing.y));
         whiteNext.add(new Location(whiteKing.x,whiteKing.y-1));
         whiteNext.add(new Location(whiteKing.x-1,whiteKing.y-1));
         whiteNext.add(new Location(whiteKing.x+1,whiteKing.y-1));

         int blackPoints=0,whitePoints=0;
         int[] values = new int[2];
        String firstLetter,secondLetter;
        try {
            for(Location loc: whiteNext){
                firstLetter = Character.toString(board[loc.y][loc.x].charAt(0));
                secondLetter=Character.toString(board[loc.y][loc.x].charAt(1));
                if(firstLetter.equals("W")){
                    whitePoints +=1;
                }
                if(firstLetter.equals("B")){
                    whitePoints -=10;
                }
            }
            for(Location loc: blackNext){
                firstLetter = Character.toString(board[loc.y][loc.x].charAt(0));
                secondLetter=Character.toString(board[loc.y][loc.x].charAt(1));
                if(firstLetter.equals("B")){
                    blackPoints +=1;
                }
                if(firstLetter.equals("W")){
                    blackPoints -=10;
                }
            }
        }catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e){

        }
        values[0] = whitePoints;
        values[1] = blackPoints;
        return values;



    }

    public int[] underAttack(){
        int black=0,white=0;
        int[] value = new int[2];
        for(Location loc:whites){
            if(loc.y<=2){
                black += 7;
            }
            if(loc.y<4){
                black +=1;
            }
        }
        for(Location loc:blacks){
            if(loc.y>=4){
                white +=7;
            }
            if(loc.y>2){
                white +=1;
            }
        }
        value[0]=white;
        value[1]=black;
        return value;
    }

    public int[] distanceFromKing(){
        //manhattan distance
        int values[] = new int[2];
        int blackPoints=0;
        int whitePoints=0;
        int max_distance=8;
        int temp = 0;
        if(blackKing.x==9 && blackKing.y==9){
            whitePoints +=100;
        }else if(whiteKing.x==9 && whiteKing.y==9){
            blackPoints +=100;
        }else{
            for(Location loc: whites){

                temp = Math.abs(loc.x - blackKing.x) + Math.abs(loc.y - blackKing.y);
                whitePoints += 1*(max_distance - temp);

            }
            for(Location loc: blacks){
                temp = Math.abs(loc.x - whiteKing.x) + Math.abs(loc.y - whiteKing.y);
                blackPoints += 1*(max_distance - temp);
            }
        }

        values[0] = whitePoints;
        values[1] = blackPoints;

        return values;
    }
    public int[] distanceFromEnd(){
        int values[] = new int[2];
        int min_blacks =Integer.MAX_VALUE;
        int min_whites =Integer.MAX_VALUE;
        int distance;
        for(Location loc: blacks){
            distance =  6 - loc.y;
            min_blacks = Math.min(min_blacks,distance);
        }
        for(Location loc: whites){
            distance =  loc.y;
            min_whites = Math.min(min_whites,distance);
        }

        values[0] = min_whites;
        values[1] = min_blacks;
        return values;
    }
    public int[] distanceFromPrize(){
        //Manhattan distances
        int[] returnValues = new int[2];
        ArrayList<Location> prizes, whites, blacks;
        prizes = getPrizeLoc();
        whites = getWhiteLoc();
        blacks = getBlackLoc();
        int distance_white = Integer.MAX_VALUE;
        int distance_black = Integer.MAX_VALUE;
        int temp = 0;

        for(Location loc: whites){
            for(Location loc2: prizes){
                temp = Math.abs(loc.x - loc2.x) + Math.abs(loc.y - loc2.y);
                distance_white = Math.min(temp,distance_white);
            }
        }
        for(Location loc: blacks){
            for(Location loc2: prizes){
                temp = Math.abs(loc.x - loc2.x) + Math.abs(loc.y - loc2.y);
                distance_black = Math.min(temp,distance_black);
            }
        }
        returnValues[0] = distance_white;
        returnValues[1] = distance_black;
        return returnValues;

    }
    public ArrayList<Location> getPrizeLoc(){
        String firstLetter;
        ArrayList<Location>  prizes = new ArrayList<>();

        for(int i=0; i<rows; i++) {
            for (int j = 0; j < columns; j++) {
                firstLetter = Character.toString(board[i][j].charAt(0));
                if(firstLetter.equals("P")){
                    Location loc = new Location(j,i);
                    prizes.add(loc);
                }
            }
        }
        return prizes;
    }

    public ArrayList<Location> getWhiteLoc(){
        String firstLetter,secondLetter;
        boolean kingfound=false;

        for(int i=0; i<rows; i++) {
            for (int j = 0; j < columns; j++) {
                firstLetter = Character.toString(board[i][j].charAt(0));
                if(firstLetter.equals("W")){
                    Location loc = new Location(j,i);
                    try {
                        secondLetter=Character.toString(board[i][j].charAt(1));
                        if(secondLetter.equals("K")){
                            whiteKing = new Location(j,i);
                            kingfound =true;
                        }
                    }catch (StringIndexOutOfBoundsException e){

                    }
                    whites.add(loc);
                }
            }
        }
        if(!kingfound){
            whiteKing = new Location(9,9);
        }
        return whites;
    }

    public ArrayList<Location> getBlackLoc(){
        String firstLetter,secondLetter;
        boolean kingfound=false;

        for(int i=0; i<rows; i++) {
            for (int j = 0; j < columns; j++) {
                firstLetter = Character.toString(board[i][j].charAt(0));
                if(firstLetter.equals("B")){
                    Location loc = new Location(j,i);
                    try {
                        secondLetter=Character.toString(board[i][j].charAt(1));
                        if(secondLetter.equals("K")){
                            blackKing = new Location(j,i);
                            kingfound = true;
                        }
                    }catch (StringIndexOutOfBoundsException e){

                    }
                    blacks.add(loc);
                }
            }
        }
        if(!kingfound)
            blackKing=new Location(9,9);
        return blacks;
    }

}
