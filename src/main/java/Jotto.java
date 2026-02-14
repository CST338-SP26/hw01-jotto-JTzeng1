/**
 * @author Justin Tzeng
 * @version 0.1.0
 * @Since 02/09/2026
 **/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;


public class Jotto {
    private static final int WORD_SIZE = 5;
    private  String currentWord;
    private  int score;
    private final ArrayList<String> playGuesses = new ArrayList<>();
    private final ArrayList<String> playWords = new ArrayList<>();
    private String filename;
    private final ArrayList<String> wordList =new ArrayList<>();
    private static final boolean DEBUG = true;

    public Jotto(String filename) {
        this.filename = filename;
        readWords();
    }

    public boolean pickWord(){
        Random rand = new Random();
        if(playWords.size() == wordList.size()){
            System.out.println("You've guessed them all!");
            return false;
        }


        int RandomIndex = rand.nextInt(wordList.size());
        currentWord = wordList.get(RandomIndex);


        if(playWords.contains(currentWord)){
            return pickWord();
        }
        playWords.add(currentWord);

        if(DEBUG){
            System.out.println("DEBUG word: " + currentWord);
        }
        return true;



    }


    public String showWordList() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current word list:\n");

        for(int i = 0; i < wordList.size(); i++) {
            sb.append(wordList.get(i)).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<String> showPlayerGuesses() {
        Scanner scanner = new Scanner(System.in);
        if(playGuesses.isEmpty()){
            System.out.println("No guesses yet");
            return playGuesses;

        } else{
            System.out.println("Current player guesses: ");
            for(int i = 0; i < playGuesses.size(); i++) {
                System.out.println(playGuesses.get(i));
            }
            System.out.println("Would you like to add the words to the word list? (y/n)");
            String input = scanner.nextLine().trim().toLowerCase();

            if(input.equals("y")){
                System.out.println("Updating word list.");
                updateWordList();
                System.out.println(showWordList());
            }


        }
        return playGuesses;
    }


    public void playerGuessScores(ArrayList<String> guesses) {
        System.out.println("Guess\t\tScore");
        for (String guess : guesses) {
            System.out.println(guess + "\t\t" + getLetterCount(guess));
        }
        System.out.println();
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public ArrayList<String> readWords() {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String words = scanner.nextLine().trim().toLowerCase();

                if(!wordList.contains(words)) {
                    wordList.add(words);
                }

            }
            scanner.close();


        } catch(FileNotFoundException e) {
            System.out.println("Couldn't open " + filename);
        }
        return wordList;
    }


    public void play() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the game.");


        while(true) {
            System.out.println("Current Score: " + score);
            System.out.println("=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("Choose one of the following:");
            System.out.println("1:\t Start the game");
            System.out.println("2:\t See the word list");
            System.out.println("3:\t See the chosen words");
            System.out.println("4:\t Show Player guesses");
            System.out.println("zz to exit");
            System.out.println("=-=-=-=-=-=-=-=-=-=-=");
            System.out.print("What is your choice: ");

            String userInput = scanner.nextLine().toLowerCase().trim();
            if (userInput.equals("zz")) {
                System.out.println("Final score: " + score);
                System.out.println("Thank you for playing");
                break;
            } else if (userInput.equals("1") || userInput.equals("one")) {
                if (pickWord()) {
                    score += guess();
                    System.out.println("Your score is " + score);
                } else {
                    showPlayerGuesses();
                }
            } else if (userInput.equals("2") || userInput.equals("two")) {
                showWordList();

            } else if (userInput.equals("3") || userInput.equals("three")) {
                System.out.println(showPlayedWords());

            } else if (userInput.equals("4") || userInput.equals("four")) {
                showPlayerGuesses();
            } else {
                System.out.println("I don't know what \"" + userInput + "\" is.");
            }
            System.out.println("Press enter to continue");
            scanner.nextLine();
        }
    }

    public int guess(){
        ArrayList<String> currentGuesses  = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int letterCount = 0;
        int score = WORD_SIZE + 1;
        String wordGuess;

        while(true) {
            System.out.println("Current Score: " + score);
            System.out.print("What is your guess (q to quit):");
            wordGuess = scan.nextLine().trim().toLowerCase();

            if(wordGuess.equals("q")) {
                score = 0;
                break;
            }
            if (wordGuess.length() != WORD_SIZE) {
                System.out.println("Word must be 5 characters (" + wordGuess + " is " + wordGuess.length() + ")");
                continue;
            }

            if (currentGuesses.contains(wordGuess)) {
                System.out.println(wordGuess + " was already entered.");
                continue;
            }

            addPlayerGuess(wordGuess);
            currentGuesses.add(wordGuess);

            if(wordGuess.equals(currentWord)) {
                System.out.println("DINGDINGDING!!! the word was " + currentWord);
                playerGuessScores(currentGuesses);
                return score;
            }
            letterCount = getLetterCount(wordGuess);
            if(letterCount == WORD_SIZE) {
                System.out.println("That word is an anagram");
            } else {
                System.out.println(wordGuess + " has a Jotto score of " + letterCount);
            }
            score--;
            playerGuessScores(currentGuesses);

        }
        return score;

    }

    public ArrayList<String> getPlayedWords(){
        return playWords;
    }

    public String getCurrentWord(){
        return currentWord;
    }

    public int getLetterCount(String wordGuess){
        if(wordGuess.equals(currentWord)) {
            return WORD_SIZE;
        }
        int count = 0;
        ArrayList<Character> letters = new ArrayList<>();

        for (int i = 0; i < currentWord.length(); i++) {
            char c = currentWord.charAt(i);
            if (!letters.contains(c)) {
                letters.add(c);
            }
        }

        for (char c : wordGuess.toCharArray()) {
            if (letters.contains(c)) {
                count++;
                letters.remove((Character) c);
            }
        }
        return count;




    }


    public String showPlayedWords() {
        if(playWords.isEmpty()){
            return "No words have been played.";
        }


        StringBuilder sb = new StringBuilder();
        sb.append("Current list of played words:\n");
        for (String words : playWords) {
            sb.append(words).append('\n');
        }
        return sb.toString();
    }

    public boolean addPlayerGuess(String wordGuess) {
        if(!playGuesses.contains(wordGuess)) {
            playGuesses.add(wordGuess);
            return true;
        }
        return false;
    }


    public void updateWordList() {
        for(String updateWord : playGuesses) {
            if(!wordList.contains(updateWord)) {
                wordList.add(updateWord);
            }


        } try {
            FileWriter fw = new FileWriter(filename);


            for (String word : wordList) {
                fw.write(word + "\n");
            }
            fw.close();

        } catch(IOException e) {
            System.out.println("Error writing to file");
        }
    }



}

