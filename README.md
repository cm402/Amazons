# Amazons
Amazons (or Game of the Amazons) is a combinatorial game that was invented in 1988 by Walter Zamkauskas. It is <br />
played on a 10 by 10 board and combines elements of chess (queen moves specifically) with the idea of removing squares <br />
from play, until one of the two players is unable to move and must concede. My intention with this project is to develop a <br />
program to allow users to play the game of Amazons, as well as develop different AIs that will play using a variety of strategies. <br />
The AIs include a combinatorial game theory implementation, with an endgame database optimisation, a monte-carlo tree search,<br /> 
a heuristic-based implementation, as well as a random choice AI.

https://en.wikipedia.org/wiki/Game_of_the_Amazons

# Run Instructions
In order to compile and run the program, you must be within the "src" folder.

Compile with: "javac -cp ".:../lib/junit-4.13.2.jar:../lib/hamcrest-all-1.3.jar:../lib/h2-1.4.200.jar" *.java" 

Run using: "java -cp ".:../lib/junit-4.13.2.jar:../lib/hamcrest-all-1.3.jar:../lib/h2-1.4.200.jar" GameEngine"

# Command Line Argument options

"experiments": allows the user to specify 2 AI Types to run some simulation games with

"unitTests": runs the unit tests

"fillDatabase": fills the endgame database with partitions up to specified size

"resetDatabase": drops the current database table, and creates a new one

"databaseSize": checks the number of entries in the endgame database
