# Card Game
A simple card game using java multithreading
## Implementation
there is input file containing a 2d matrix 4*8 from which pile of card and cards in hand are calculated.
there are 4 players and 4 pile of cards in between each players, each player has 4 cards in hand.
The player having all the card of same preferred number in hand wins the game and the game is over
> Main Class 

In this class we read the inputdata from the inputfile and convert into a 2d matrix of 4*8
then we assign each row of a 2d matrix to each pile,
then we create player array object and create thread for each player and assign number to each player and set there preferred cards

> Player Class

It is a thread class in which each player play its turn 
while one player is drawing a card, all others are set to wait until he discards any card and notifyall
there is static variable `turn` if `playernumber` is equal to `turn` then other players are set to wait 
and gets notified when player discards any card
there a hashmap named `preferredCardsMap` which keeps the count of preferred cards each player have
,if preferred card count of a player is 4 then that player wins and game is over.

> Piles Class

this class is used to draw and discard card from the piles of card placed in between each player
card is drawn from the pile and the card which is discarded is added in the next pile
> InitialHand Class

in this class cards given in hand of each player at first are calculated from the input matrix
### Preffered Cards for players
`player1 (0,1)`
`player2 (2,3)`
`player3 (4,5)`
`player4 (6,7)`

## Requirements
1. java jdk 
2. apache maven
3. ide

## How to build and run
    open the folder in terminal
   `mvn compile` to compile the class
   
   `mvn install` to install dependencies and create a executable jar file
  
   `java -jar target/CardGame-1.0-SNAPSHOT.jar` to run the program
