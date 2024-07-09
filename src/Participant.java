import java.io.IOException;

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



                }
                start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
