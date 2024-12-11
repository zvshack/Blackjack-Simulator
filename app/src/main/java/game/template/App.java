/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package game.template;

import java.net.URL;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class App extends Application
{
    private Stage primaryStage;
    private Scene playScene, betScene, gameScene;
    private Board board = new Board();
    private boolean blackjack = false;
    private boolean seenYet = false;

    @Override
    public void start(Stage primaryStage) {
        
        this.primaryStage = primaryStage;

        createPlayScene();
        createBetScene();
        primaryStage.setScene(playScene);
        primaryStage.setTitle("Blackjack");
        primaryStage.show();
    }
    private void createPlayScene() {

        

        // TODO Auto-generated method stub
        //this creates the grid
        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(10); //gives the horizontal gap
        cardGrid.setVgap(10); //gives the vertical gap

        int cardsPerRow = 13; //number of cards per row

        //matches the cards to the image
        board.cardDealer();
        for (int i = 0; i < 52; i++) {
            
            String[] cards = board.getCards();
            String card = cards[i];
            Image image = new Image(getClass().getResourceAsStream("/assets/" + card + ".png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);

            int row = i / cardsPerRow;
            int col = i % cardsPerRow;
            cardGrid.add(imageView, col, row);
        }

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 40px;" +
                            "-fx-background-color: white;" +
                            "-fx-border-radius: 50px;" +
                            "-fx-background-radius: 50px;" +
                            "-fx-padding: 10px 20px;");
        
        BorderPane root = new BorderPane();
        root.setCenter(cardGrid);
        root.setBottom(playButton);
        BorderPane.setAlignment(playButton, Pos.CENTER);
        BorderPane.setMargin(playButton, new Insets(0,0,100,0));

        playScene = new Scene(root, 1000, 800);
        
        playButton.setOnAction(event -> {
            System.out.println("Game Stated!");
            
            primaryStage.setScene(betScene);
        });
    }
        private void createBetScene(){
            BorderPane root1 = new BorderPane();
            root1.setStyle("-fx-background-color: green;");

            ImageView deckBack = new ImageView(new Image(getClass().getResourceAsStream("/assets/back.png")));
            cardSetUp(deckBack);

            Text cardsLeftText = new Text("Cards Left: " + board.getDeck());
            cardsLeftText.setFill(Color.WHITE);
            cardsLeftText.setStyle("-fx-font-size: 20px;");

            HBox deckBox = new HBox(10, cardsLeftText, deckBack);
            deckBox.setAlignment(Pos.TOP_RIGHT);
            BorderPane.setAlignment(deckBox, Pos.TOP_RIGHT);

            root1.setRight(deckBox);

            Text money = new Text("You have: $" + board.getMoney());
            money.setFill(Color.WHITE);
            money.setStyle("-fx-font-size: 30px;");

            HBox monion = new HBox(10, money);
            monion.setAlignment(Pos.BOTTOM_LEFT);
            root1.setLeft(monion);

            Button betButton = new Button("Bet");
            betButton.setStyle("-fx-font-size: 40px;" +
                               "-fx-background-color: white;" +
                               "-fx-text-fill: red;" +
                               "-fx-border-color: red;" +
                               "-fx-border-radius: 50px;" +
                               "-fx-background-radius: 50px;" +
                               "-fx-padding: 10px 20px;");
            Button shuffleButton = new Button ("Shuffle");
            shuffleButton.setStyle("-fx-font-size: 40px;" +
                                   "-fx-background-color: white;" +
                                   "-fx-text-fill: red;" +
                                   "-fx-border-color: red;" +
                                   "-fx-border-radius: 50px;" +
                                   "-fx-background-radius: 50px;" +
                                   "-fx-padding: 10px 20px;");
            VBox betBox = new VBox(10, betButton, shuffleButton);
            betBox.setPadding(new Insets(20));
            betBox.setAlignment(Pos.BOTTOM_CENTER);
            root1.setBottom(betBox);
            BorderPane.setMargin(betBox, new Insets(10));
            
            betScene = new Scene(root1, 1000, 800);
            if(board.getDeck() == 52){
                shuffleButton.setDisable(true);
            }
            if(board.getMoney() == 0){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Game Over!");
                alert.setHeaderText(null);
                alert.setContentText("Game Over!");
                alert.showAndWait();
                System.exit(0);
            }
            if(board.getDeck() <= 10){
                betButton.setDisable(true);
            }

            shuffleButton.setOnAction(e -> {
                System.out.println("Shuffle Button Clicked!");
                board.cardDealer();
                Text cardsLeftText1 = new Text("Cards Left: " + board.getDeck());
                cardsLeftText1.setFill(Color.WHITE);
                cardsLeftText1.setStyle("-fx-font-size: 20px;");

                HBox deckBox1 = new HBox(10, cardsLeftText1, deckBack);
                deckBox1.setAlignment(Pos.TOP_RIGHT);
                BorderPane.setAlignment(deckBox1, Pos.TOP_RIGHT);

                root1.setRight(deckBox1);
                shuffleButton.setDisable(true);
                if(betButton.isDisabled()){
                    betButton.setDisable(false);
                }
            });

            betButton.setOnAction(e -> {
                System.out.println("Bet Button Clicked!");
                Stage betStage = new Stage();
                betStage.initModality(Modality.APPLICATION_MODAL);
                betStage.setTitle("Enter Bet Amount");

                TextField betInput = new TextField();
                betInput.setPromptText("Enter Bet Amount");

                Button confirmButton = new Button("Confirm Bet");
                confirmButton.setOnAction(e2 -> {
                    try{
                        double  betAmount = Double.parseDouble(betInput.getText());
                    } catch (NumberFormatException ex){
                        Alert alert1 = new Alert(AlertType.INFORMATION);
                        alert1.setTitle("Oops, Invalid Bet Amount!");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Invalid Bet Amount!");
                        alert1.showAndWait();
                        return;
                    }
                    double betAmount = Double.parseDouble(betInput.getText());
                    if(betAmount > board.getMoney()){
                        System.out.println("Not enough money!");
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Oops, No Money!");
                        alert.setHeaderText(null);
                        alert.setContentText("Not enough money!");
                        alert.showAndWait();
                        return;
                    }
                    String d = "" + betAmount;
                    if(d.indexOf(".") == -1){
                        d += ".0";
                        
                    }
                    if(betAmount < 10 || d.length() - d.indexOf(".") - 1 > 1){
                        System.out.println("Invalid bet amount!");
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Oops, Invalid Bet Amount! Bet Above $10 and only one decimal place!");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid Bet Amount!");
                        alert.showAndWait();
                        return;
                    }
                    board.bet(betAmount);
                    money.setText("You have: $" + board.getMoney());
                    createGameScene();
                    betStage.close();

                    primaryStage.setScene(gameScene);
                });
                
                VBox layout = new VBox(10);
                layout.getChildren().addAll(betInput, confirmButton);
                layout.setAlignment(Pos.CENTER);
                layout.setPadding(new Insets(20));

                Scene betScene = new Scene(layout, 300, 200);
                betStage.setScene(betScene);
                betStage.showAndWait();
            });
            root1.setTop(createMenuBar2());
        }
        private void createGameScene(){
            BorderPane root2 = new BorderPane();     
            board.shuffleNdeal();

            root2.setStyle("-fx-background-color: green;");
                    
            HBox myCardsBox = myDeckSetUp();
            myCardsBox.setAlignment(Pos.CENTER);

            root2.setCenter(myCardsBox);
            //make sure that the myCardsBox is centered
            BorderPane.setAlignment(myCardsBox, Pos.CENTER);
            BorderPane.setMargin(myCardsBox, new Insets(0,0,100,150));
            root2.setPadding(new Insets( 0,0,100,0));
            
           

            ImageView deckBack = new ImageView(new Image(getClass().getResourceAsStream("/assets/back.png")));
            cardSetUp(deckBack);
            ImageView deckBack2 = new ImageView(new Image(getClass().getResourceAsStream("/assets/back.png")));
            cardSetUp(deckBack2);
            ImageView dealCard1 = new ImageView(new Image(getClass().getResourceAsStream("/assets/" + board.getDealerCards()[0] + ".png")));
            cardSetUp(dealCard1);
            HBox dealCardsBox = new HBox(10, dealCard1, deckBack);
            dealCardsBox.setAlignment(Pos.CENTER);

            Text cardsLeftText = new Text("Cards Left: " + board.getDeck());
            cardsLeftText.setFill(Color.WHITE);
            cardsLeftText.setStyle("-fx-font-size: 20px;");

            HBox deckBox = new HBox(10, cardsLeftText, deckBack2);
            deckBox.setAlignment(Pos.TOP_RIGHT);

            MenuBar menu = createMenuBar(); 

            VBox topBox = new VBox(menu, dealCardsBox, deckBox);
            topBox.setAlignment(Pos.TOP_CENTER);
            VBox.setMargin(dealCardsBox,new Insets(10,0,0,0));
            VBox.setMargin(deckBox,new Insets(10,0,0,0));

            root2.setTop(topBox);
            BorderPane.setAlignment(topBox, Pos.CENTER);
            
            gameScene = new Scene(root2, 1000, 800);

            Button hitButton = new Button("Hit");
            hitButton.setStyle("-fx-font-size: 40px;" +
                               "-fx-background-color: white;" +
                               "-fx-text-fill: red;" +
                               "-fx-border-color: red;" +
                               "-fx-border-radius: 50px;" +
                               "-fx-background-radius: 50px;" +
                               "-fx-padding: 10px 20px;");
            Button standButton = new Button("Stand");
            standButton.setStyle("-fx-font-size: 40px;" +
                                 "-fx-background-color: white;" +
                                 "-fx-text-fill: red;" +
                                 "-fx-border-color: red;" +
                                 "-fx-border-radius: 50px;" +
                                 "-fx-background-radius: 50px;" +
                                 "-fx-padding: 10px 20px;");
            Button doubleButton = new Button("Double Down");
            doubleButton.setStyle("-fx-font-size: 40px;" +
                                 "-fx-background-color: white;" +
                                 "-fx-text-fill: red;" +
                                 "-fx-border-color: red;" +
                                 "-fx-border-radius: 50px;" +
                                 "-fx-background-radius: 50px;" +
                                 "-fx-padding: 10px 20px;");
            HBox buttonBox = new HBox(10, hitButton, standButton, doubleButton);
            buttonBox.setAlignment(Pos.CENTER);
            root2.setBottom(buttonBox);

            Text betText = new Text("Bet: $" + board.getBrick());
            betText.setFill(Color.WHITE);
            betText.setStyle("-fx-font-size: 30px;");
            HBox betBox1 = new HBox(10, betText);
            betBox1.setAlignment(Pos.BOTTOM_RIGHT);
            betBox1.setPadding(new Insets(20));
            root2.setRight(betBox1);
            BorderPane.setAlignment(betBox1, Pos.BOTTOM_RIGHT);
            
            if(board.getMoney() < board.getBrick()){
                doubleButton.setDisable(true);
            }
            if(board.check21(board.getMyCards()) == 21){
                hitButton.setDisable(true);
                doubleButton.setDisable(true);
                blackjack = true;
            }
            hitButton.setOnAction(e3 -> {
                System.out.println("Hit Button Clicked!");
                seenYet = false;
                board.hit();
                HBox myDeckBox = myDeckSetUp();
                myDeckBox.setAlignment(Pos.CENTER);
                root2.setCenter(myDeckBox);

                        

                if(board.check21(board.getMyCards()) > 21){
                    System.out.println("Busted!");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Busted!");
                    alert.setHeaderText(null);
                    alert.setContentText("You Busted!");
                    alert.showAndWait();
                    board.lose();
                    board.reset();
                    createBetScene();
                    primaryStage.setScene(betScene);

                }
                if (board.check21(board.getMyCards()) == 21){
                    hitButton.setDisable(true);
                    doubleButton.setDisable(true);
                }
            });
            standButton.setOnAction(e4 -> {
                System.out.println("Stand Button Clicked!");
                seenYet = false;
                HBox dealDeckBox = dealDeckSetUp();
                dealDeckBox.setAlignment(Pos.BOTTOM_CENTER);
                root2.setTop(dealDeckBox);
                hitButton.setDisable(true);
                standButton.setDisable(true);
                doubleButton.setDisable(true);
                if(blackjack == true){
                    blackjack = false;
                    board.win();
                    board.reset();
                    createBetScene();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Blackjack!");
                    alert.setHeaderText(null);
                    alert.setContentText("Blackjack!");
                    alert.showAndWait();
                    primaryStage.setScene(betScene);
                }
                else{
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e5 ->{

                        handleDealerHits(root2);
                    });
                        pause.play();//wait 2 seconds
                }   
                                
            });
                doubleButton.setOnAction(e7 -> {
                    board.hit();
                    board.doubleDown();
                    HBox myDeckBox = myDeckSetUp();
                    myDeckBox.setAlignment(Pos.CENTER);
                    root2.setCenter(myDeckBox);
                    if(board.check21(board.getMyCards()) > 21){
                        System.out.println("Busted!");
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Busted!");
                        alert.setHeaderText(null);
                        alert.setContentText("You Busted!");
                        alert.showAndWait();
                        board.lose();
                        board.reset();
                        createBetScene();
                        primaryStage.setScene(betScene);
    
                    }
                    standButton.fire();
                });
                
        }        

    public void cardSetUp(ImageView card){
        card.setFitHeight(150);
        card.setFitWidth(100);
        card.setPreserveRatio(true);
    }
    public void handleDealerHits(BorderPane root2){
        if(board.check21(board.getDealerCards()) < 17){
            board.dhit();
            HBox dealDeckBox = dealDeckSetUp();
            dealDeckBox.setAlignment(Pos.BOTTOM_CENTER);
            root2.setTop(dealDeckBox);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e6 ->{
                handleDealerHits(root2);
            });
            pause.play();
        }
        else {
            System.out.println("Dealer Stands!");
                Platform.runLater(() -> {
                if(board.check21(board.getDealerCards()) > 21){
                    System.out.println("Dealer Busted!");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Dealer Busted!");
                    alert.setHeaderText(null);
                    alert.setContentText("Dealer Busted!");
                    alert.showAndWait();
                    board.win();
                    board.reset();
                    createBetScene();
                    primaryStage.setScene(betScene);
                }
                else if(board.check21(board.getDealerCards()) > board.check21(board.getMyCards())){
                    System.out.println("Dealer Wins!");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Dealer Wins!");
                    alert.setHeaderText(null);
                    alert.setContentText("Dealer Wins!");
                    alert.showAndWait();
                    board.lose();
                    board.reset();
                    createBetScene();
                    primaryStage.setScene(betScene);
                }
                else if(board.check21(board.getDealerCards()) == board.check21(board.getMyCards())){
                    System.out.println("Tie!");
                    if(board.getNum1() < board.getNum2()){
                        System.out.println("You Win!");
                        Alert alert1 = new Alert(AlertType.INFORMATION);
                        alert1.setTitle("You Win!");
                        alert1.setHeaderText(null);
                        alert1.setContentText("You Win For Having Less Cards!");
                        alert1.showAndWait();
                        board.win();
                        board.reset();
                        createBetScene();
                        primaryStage.setScene(betScene);
                    }
                    else if (board.getNum1() > board.getNum2()){
                        System.out.println("Dealer Wins!");
                        Alert alert1 = new Alert(AlertType.INFORMATION);
                        alert1.setTitle("Dealer Wins!");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Dealer Wins For Having Less Cards!");
                        alert1.showAndWait();
                        board.lose();
                        board.reset();
                        createBetScene();
                        primaryStage.setScene(betScene);
                    }
                    else{
                        System.out.println("Tie!");
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Tie!");
                        alert.setHeaderText(null);
                        alert.setContentText("Tie!");
                        alert.showAndWait();
                        board.tie();
                        board.reset();
                        createBetScene();
                        primaryStage.setScene(betScene);
                    }
                            
                }
                else{
                    System.out.println("You Win!");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("You Win!");
                    alert.setHeaderText(null);
                    alert.setContentText("You Win!");
                    alert.showAndWait();
                    board.win();
                    board.reset();
                    createBetScene();
                    primaryStage.setScene(betScene);
                }
            });
        }
    }
    public HBox myDeckSetUp(){
        HBox myDeckBox = new HBox(10);
        myDeckBox.setAlignment(Pos.CENTER);

        for(int i = 0; i < board.getNum1(); i++){
            ImageView myCard = new ImageView(new Image(getClass().getResourceAsStream("/assets/" + board.getMyCards()[i] + ".png")));
            cardSetUp(myCard);
            myDeckBox.getChildren().add(myCard);
        }
        return myDeckBox;
    }
    public HBox dealDeckSetUp(){
        HBox dealDeckBox = new HBox(10);
        dealDeckBox.setAlignment(Pos.BOTTOM_CENTER);

        for(int i = 0; i < board.getNum2(); i++){
            ImageView myCard = new ImageView(new Image(getClass().getResourceAsStream("/assets/" + board.getDealerCards()[i] + ".png")));
            cardSetUp(myCard);
            dealDeckBox.getChildren().add(myCard);
        }
        return dealDeckBox;
    }
    private MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();
        
        Menu Count = new Menu("Counting Help");
        Menu Game = new Menu("Game");
        Menu Help = new Menu("Help");

        MenuItem Counting1 = new MenuItem("Assign High 1");
        MenuItem Counting2 = new MenuItem("Assign High 0");
        MenuItem Counting3 = new MenuItem("Card Tracker");

        Counting1.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Assign High 1");
            alert.setHeaderText(null);
            alert.setContentText("Counting cards through assigning 2-6 as +1, 7-9 as 0, and 10-A as -1. The sum for right now is: " + board.ah());
            alert.showAndWait();
        });

        Counting2.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Assign High 0");
            alert.setHeaderText(null);
            alert.setContentText("Counting cards through assigning 2-6 as -1, 7-9 as 0, and 10-A as +1. The sum for right now is: " + board.al());
            alert.showAndWait();
        });

        Counting3.setOnAction(e->{
            int i = 0;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Card Track");
            alert.setHeaderText(null);
            StringBuilder cardsPlayed = new StringBuilder("The cards that have been played are: ");
            while(board.getChistory()[i] != 0){
                cardsPlayed.append(board.getChistory()[i] + ", ");
                i += 1;
            }
            alert.setContentText(cardsPlayed.toString());
            alert.showAndWait();
        });

        Count.getItems().addAll(Counting1, Counting2, Counting3);

        MenuItem NewGame = new MenuItem("New Game");
        MenuItem Hard = new MenuItem("Hard Mode");

        NewGame.setOnAction(e->{
            Stage newGame = new Stage();
                newGame.initModality(Modality.APPLICATION_MODAL);
                newGame.setTitle("Are you sure you want to start a new game?");

                Button confirmButton = new Button("Yes");
                confirmButton.setOnAction(e2 -> {
                        board.reset();
                        board.mreset();
                        board.cardDealer();
                        createBetScene();
                        primaryStage.setScene(betScene);
                        newGame.close();
                });
                Button cancelButton = new Button("No");
                cancelButton.setOnAction(e2 -> {
                    newGame.close();
                });

                VBox layout = new VBox(10);
                layout.getChildren().addAll(cancelButton, confirmButton);
                layout.setAlignment(Pos.CENTER);
                layout.setPadding(new Insets(20));

                Scene newGameScene = new Scene(layout, 300, 200);
                newGame.setScene(newGameScene);
                newGame.showAndWait();
            
        });
        

        Hard.setOnAction(e->{
            Stage hard = new Stage();
            hard.initModality(Modality.APPLICATION_MODAL);
            hard.setTitle("Are you sure you want to start a new game in hard mode? You will start with 50$");

            Button confirmButton = new Button("Yes");
            confirmButton.setOnAction(e2 -> {
                board.reset();
                board.cardDealer();
                board.hardMode();
                createBetScene();
                primaryStage.setScene(betScene);
                hard.close();
            });
            Button cancelButton = new Button("No");
            cancelButton.setOnAction(e2 -> {
                hard.close();
            });
            VBox layout = new VBox(10);
            layout.getChildren().addAll(cancelButton, confirmButton);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));

            Scene hardScene = new Scene(layout, 300, 200);
            hard.setScene(hardScene);
            hard.showAndWait();
        });

        Game.getItems().addAll(NewGame, Hard);


        MenuItem Next = new MenuItem("Next Card");
        MenuItem hs = new MenuItem("Advice");
        MenuItem bhs = new MenuItem("Bad Advice");
        MenuItem rules = new MenuItem("Rules");

        Next.setOnAction(e->{
            Stage next = new Stage();
            next.initModality(Modality.APPLICATION_MODAL);
            next.setTitle("Do you want to see the next card?");
            Button confirmButton = new Button("Yes");
            confirmButton.setOnAction(e2 -> {
                System.out.print(seenYet);
                if(seenYet == true){
                    String j = board.getCards()[board.getNC()].substring(0, board.getCards()[board.getNC()].length() - 1);
                    if(j.equals("11") || j.equals("12") || j.equals("13")){
                        j = "10";
                    }
                    else if(j.equals("1")){
                        j = "1 or 11";
                    }
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("The Next Card");
                    alert.setHeaderText(null);
                    alert.setContentText("The next card is: " + j);
                    alert.showAndWait();
                    next.close();
                }
                else{
                    board.nextCard();
                    seenYet = true;
                    String j = board.getCards()[board.getNC()].substring(0, board.getCards()[board.getNC()].length() - 1);
                    if(j.equals("11") || j.equals("12") || j.equals("13")){
                        j = "10";
                    }
                    else if(j.equals("1")){
                        j = "1 or 11";
                    }
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("The Next Card");
                    alert.setHeaderText(null);
                    alert.setContentText("The next card is: " + j);
                    alert.showAndWait();
                    next.close();
                }
            });
            Button cancelButton = new Button("No");
            cancelButton.setOnAction(e2 -> {
                next.close();
            });
            VBox layout = new VBox(10);
            layout.getChildren().addAll(cancelButton, confirmButton);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));

            Scene nextScene = new Scene(layout, 300, 200);
            next.setScene(nextScene);
            next.showAndWait();
        });

        hs.setOnAction(e->{
            Stage advice = new Stage();
            advice.initModality(Modality.APPLICATION_MODAL);
            advice.setTitle("Do You Want Some Advice?");
            Button confirmButton = new Button("Yes");
            confirmButton.setOnAction(e2 -> {
                if(seenYet == false){
                    board.nextCard();
                    seenYet = true;
                }
                if(board.check21NC() == 21){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Advice");
                    alert.setHeaderText(null);
                    alert.setContentText("Please Please Hit!");
                    alert.showAndWait();
                    advice.close();
                }
                else if(board.check21NC() > 21){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Advice");
                    alert.setHeaderText(null);
                    alert.setContentText("Think about standing");
                    alert.showAndWait();
                    advice.close();
                }
                else{
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Advice");
                    alert.setHeaderText(null);
                    alert.setContentText("Probably Hit");
                    alert.showAndWait();
                    advice.close();
                }
            });
            Button cancelButton = new Button("No");
            cancelButton.setOnAction(e2 -> {
                advice.close();
            });
            VBox layout = new VBox(10);
                layout.getChildren().addAll(cancelButton, confirmButton);
                layout.setAlignment(Pos.CENTER);
                layout.setPadding(new Insets(20));

                Scene adviceScene = new Scene(layout, 300, 200);
                advice.setScene(adviceScene);
                advice.showAndWait();
        });

        rules.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Rules");
            alert.setHeaderText(null);
            alert.setContentText("The rules are simple. Get as close to 21 as possible without going over. If you go over, you lose. Aces are worth 1 or 11, face cards are all worth 10, and the rest are worth their face value.\n" +
            "After betting a certain amount (under the amount of money you have) you will be dealt two cards. You can choose to hit, stand, or double down.\n" +
            "If you hit, you will be dealt another card. If you stand, the dealer will hit until they reach 17 or higher.\n" +
            "If you double down, you will be dealt one more card and your bet will be doubled.\n" +
            "If you get an ace and a face as your first two cards, you win automatically.\n" +
            "In case of a tie with the dealer, the person with the least amount of cards wins.\n" +
            "Good luck!");
            alert.showAndWait();
        });

        bhs.setOnAction(e->{
            Stage badadvice = new Stage();
            badadvice.initModality(Modality.APPLICATION_MODAL);
            badadvice.setTitle("Do You Want Some Bad Advice?");
            Button confirmButton = new Button("Yes");
            confirmButton.setOnAction(e2 -> {
                if(board.Uor2() == 1){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Bad Advice");
                    alert.setHeaderText(null);
                    alert.setContentText("I don't know maybe stand");
                    alert.showAndWait();
                    badadvice.close();
                }
                else{
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Bad Advice");
                    alert.setHeaderText(null);
                    alert.setContentText("I don't know maybe hit");
                    alert.showAndWait();
                    badadvice.close();
                }
            });
            Button cancelButton = new Button("No");
            cancelButton.setOnAction(e2 -> {
                badadvice.close();
            });
            VBox layout = new VBox(10);
            layout.getChildren().addAll(cancelButton, confirmButton);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));

            Scene badadviceScene = new Scene(layout, 300, 200);
            badadvice.setScene(badadviceScene);
            badadvice.showAndWait();
        });

        Help.getItems().addAll(Next, hs, rules, bhs);

        menuBar.getMenus().addAll(Count, Game, Help);
        return menuBar;
    }
    public MenuBar createMenuBar2(){
        MenuBar menuBar = new MenuBar();

        Menu Count = new Menu("Counting Help");
        Menu Game = new Menu("Game");
        Menu Help = new Menu("Help");

        MenuItem Counting1 = new MenuItem("Assign High 1");
        MenuItem Counting2 = new MenuItem("Assign High 0");
        MenuItem Counting3 = new MenuItem("Card Tracker");

        Counting1.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Assign High 1");
            alert.setHeaderText(null);
            alert.setContentText("Counting cards through assigning 2-6 as +1, 7-9 as 0, and 10-A as -1. The sum for right now is: " + board.ah());
            alert.showAndWait();
        });

        Counting2.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Assign High 0");
            alert.setHeaderText(null);
            alert.setContentText("Counting cards through assigning 2-6 as -1, 7-9 as 0, and 10-A as +1. The sum for right now is: " + board.al());
            alert.showAndWait();
        });

        Counting3.setOnAction(e->{
            int i = 0;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Card Track");
            alert.setHeaderText(null);
            StringBuilder cardsPlayed = new StringBuilder("The cards that have been played are: ");
            while(board.getChistory()[i] != 0){
                cardsPlayed.append(board.getChistory()[i] + ", ");
                i += 1;
            }
            alert.setContentText(cardsPlayed.toString());
            alert.showAndWait();
        });

        Count.getItems().addAll(Counting1, Counting2, Counting3);

        MenuItem NewGame = new MenuItem("New Game");
        MenuItem Hard = new MenuItem("Hard Mode");

        NewGame.setOnAction(e->{
            Stage newGame = new Stage();
                newGame.initModality(Modality.APPLICATION_MODAL);
                newGame.setTitle("Are you sure you want to start a new game?");

                Button confirmButton = new Button("Yes");
                confirmButton.setOnAction(e2 -> {
                        board.reset();
                        board.mreset();
                        board.cardDealer();
                        createBetScene();
                        primaryStage.setScene(betScene);
                        newGame.close();
                });
                Button cancelButton = new Button("No");
                cancelButton.setOnAction(e2 -> {
                    newGame.close();
                });
                VBox layout = new VBox(10);
                layout.getChildren().addAll(cancelButton, confirmButton);
                layout.setAlignment(Pos.CENTER);
                layout.setPadding(new Insets(20));

                Scene newGameScene = new Scene(layout, 300, 200);
                newGame.setScene(newGameScene);
                newGame.showAndWait();
        });

        Hard.setOnAction(e->{
            Stage hard = new Stage();
            hard.initModality(Modality.APPLICATION_MODAL);
            hard.setTitle("Are you sure you want to start a new game in hard mode? You will start with 50$");

            Button confirmButton = new Button("Yes");
            confirmButton.setOnAction(e2 -> {
                board.reset();
                board.cardDealer();
                board.hardMode();
                createBetScene();
                primaryStage.setScene(betScene);
                hard.close();
            });
            Button cancelButton = new Button("No");
            cancelButton.setOnAction(e2 -> {
                hard.close();
            });
            VBox layout = new VBox(10);
                layout.getChildren().addAll(cancelButton, confirmButton);
                layout.setAlignment(Pos.CENTER);
                layout.setPadding(new Insets(20));

                Scene hardScene = new Scene(layout, 300, 200);
                hard.setScene(hardScene);
                hard.showAndWait();
        });

        Game.getItems().addAll(NewGame, Hard);

        MenuItem rules = new MenuItem("Rules");
        rules.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Rules");
            alert.setHeaderText(null);
            alert.setContentText("The rules are simple. Get as close to 21 as possible without going over. If you go over, you lose.\n" +
            "Aces are worth 1 or 11, face cards are all worth 10, and the rest are worth their face value. After betting a certain amount (under the amount of money you have) you will be dealt two cards. You can choose to hit, stand, or double down.\n" +
            "If you hit, you will be dealt another card. If you stand, the dealer will hit until they reach 17 or higher.\n" +
            "If you double down, you will be dealt one more card and your bet will be doubled.\n" +
            "If you get an ace and a face as your first two cards, you win automatically.\n" +
            "In case of a tie with the dealer, the person with the least amount of cards wins.\n" +
            "Good luck!");
            alert.showAndWait();
        });

        Help.getItems().addAll(rules);

        menuBar.getMenus().addAll(Count, Game, Help);
        return menuBar;

    }
    public static void main(String[] args) {
        launch(args);
    }

}


 