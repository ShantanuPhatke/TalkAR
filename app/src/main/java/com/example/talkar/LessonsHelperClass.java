package com.example.talkar;

class LessonsHelperClass {

    String username;
    int alphabets, numbers, shapes, colors, words, sentences, quiz;



    public LessonsHelperClass(String username, int alphabets, int numbers, int shapes, int colors, int words, int sentences, int quiz) {

        this.username = username;
        this.alphabets = alphabets;
        this.numbers = numbers;
        this.shapes = shapes;
        this.colors = colors;
        this.words = words;
        this.sentences = sentences;
        this.quiz = quiz;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getAlphabets() {
        return alphabets;
    }
    public void setAlphabets(int alphabets) {
        this.alphabets = alphabets;
    }

    public int getNumbers() {
        return numbers;
    }
    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public int getShapes() {
        return shapes;
    }
    public void setShapes(int shapes) {
        this.shapes = shapes;
    }

    public int getColors() {
        return colors;
    }
    public void setColors(int shapes) { this.colors = colors; }

    public int getWords() {
        return words;
    }
    public void setWords(int words) {
        this.words = words;
    }

    public int getSentences() {
        return sentences;
    }
    public void setSentences(int sentences) {
        this.sentences = sentences;
    }

    public int getQuiz() {
        return quiz;
    }
    public void setQuiz(int quiz) {
        this.quiz = quiz;
    }
}
