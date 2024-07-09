import java.io.IOException;

import java.time.LocalDateTime;


public class Participant {
    private int participantId;

    public Participant(String[] details) {

        String username = details[0];
        String email = details[3];
        String fullName= details[1].toUpperCase()+" "+details[2].toUpperCase();

    }


    public void viewChallenges(){
        DBO dbo = new DBO();
        dbo.connect();
        //view all challenges
        String[] challenges= dbo.getChallenges();
        System.out.println(challenges.length);
        //if challenges is empty return no challenges

        if (challenges.length==0) Main.server.printWriter.println("no challenges");
        else {
            for (String challenge : challenges) {
                Main.server.printWriter.println(challenge);
            }
            Main.server.printWriter.println("done");
            System.out.println("done");
        }
        dbo.close();

    }
    public void attemptChallenge()  {
            viewChallenges();





    };

    public void start() {
        String command;
        try {
        command=Main.server.reader.readLine().strip();
            System.out.println(command);


            if (command.equalsIgnoreCase("view challenges")) {
                viewChallenges();
                start();
            }
            else if (command.equalsIgnoreCase("attempt challenge")){

                attemptChallenge();
                command=Main.server.reader.readLine().strip();
                DBO dbo = new DBO();
                dbo.connect();
                if(dbo.checkChallengeExists(command)){

                String[] challengeDetails=dbo.getChallengeDetails(command);
                String[] details={"Challenge name: "+challengeDetails[0],"Challenge description: "+challengeDetails[1],"Challenge duration(minutes): "+challengeDetails[2],"Questions to answer: "+challengeDetails[3],"Marks for wrong answer: "+challengeDetails[4],"Marks for correct answer: 1","Marks for unanswered question: "+challengeDetails[5]};
                Challenge challenge = new Challenge(challengeDetails[0],challengeDetails[1],Integer.parseInt(challengeDetails[2]),Integer.parseInt(challengeDetails[3]),Integer.parseInt(challengeDetails[4]),Integer.parseInt(challengeDetails[5]), challengeDetails[6],challengeDetails[7],challengeDetails[8]);
                    for (String detail:details){
                        Main.server.printWriter.println(detail);
                    }
                    //send done to the client
                    Main.server.printWriter.println("done");
                    //receive the response from the client
                    String response=Main.server.reader.readLine().strip();
                    //if the response is start, send the questions to the client
                    if(response.equalsIgnoreCase("start")){
                        challenge.showQuestions(challenge.getQuestions());
                    }


//                Main.server.printWriter.println(details);
                }else {
                    Main.server.printWriter.println("invalid challenge");
                }
   start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
