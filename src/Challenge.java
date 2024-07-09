import java.util.ArrayList;
import java.util.Date;

public class Challenge {


    private final String description;
    private final int duration;
    private final int questions;
    private final int wrongMark;
    private final int blankMark;
    private final String startDate;
    private final String endDate;
    private final String id;
    private  String name;

    public Challenge(String name   , String description, int duration, int questions, int wrongMark, int blankMark, String startDate, String endDate,String id) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.questions = questions;
        this.wrongMark = wrongMark;
        this.blankMark = blankMark;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id=id;
    }
    //a methods to get questions to be attempted by the participant
    public String[][] getQuestions(){
        DBO dbo = new DBO();
        dbo.connect();
        String[][]questionArray= dbo.getQuestions(id,questions);
        dbo.close();
        return questionArray;

    }

    //a method to display questions one at a time while showing time left for each attempt and storing each answer
    public void showQuestions(String[][] questionAnswers){
        Date date = new Date();
        long startTime = date.getTime();
        long endTime = startTime + duration*60000;
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> marks = new ArrayList<>();
        ArrayList<String> questionIds = new ArrayList<>();

        for(String[] questionAnswer:questionAnswers){
            questions.add(questionAnswer[0]);
            marks.add(questionAnswer[4]);
            answers.add(questionAnswer[1]);
            questionIds.add(questionAnswer[2]);
        }
        for (int i=0;i<questions.size();i++){
            System.out.println(questions.get(i));
            System.out.println("Marks: "+marks.get(i));
            System.out.println("Answer: "+answers.get(i));
            System.out.println("Question id: "+questionIds.get(i));
            System.out.println("Time left: "+(endTime-date.getTime())/60000+" minutes");

        }





    }

}
