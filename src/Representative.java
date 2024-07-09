import java.io.*;
import java.util.ArrayList;


public class Representative {
    private String name;
    private String email;
    private String schoolRegNo;
    private String username;
    BufferedReader bufferedFileReader=null;

    public Representative(String[] repDetails) {
        this.name = repDetails[1]+" "+repDetails[2];
        this.email = repDetails[3];
        this.schoolRegNo = repDetails[4];
        this.username = repDetails[0];

    }

    public void validateParticipant() {

        String line;
        DBO dbo = new DBO();
        dbo.connect();
        BufferedWriter bufferedFileWriter=null;
        //create an arraylist to store the participants
        ArrayList<String> participants = new ArrayList<>();
        try( BufferedReader bufferedFileReader=new BufferedReader(new FileReader("src/participants.txt"));
) {
            System.out.println(schoolRegNo);
            String reply;
            if ((reply=Main.server.reader.readLine()).equalsIgnoreCase("yes")) {


                while ((line = bufferedFileReader.readLine()) != null) {
                    // send line to server if the school registration number matches the school registration number of the representative
                    if (line.split(" ")[6].equalsIgnoreCase(schoolRegNo)) {
                        Main.server.printWriter.println(line);
                        reply = Main.server.reader.readLine();
                        if (reply.equalsIgnoreCase("yes"))
                            dbo.insertParticipant(line.split(" "), Main.server.imageToByteArray(line.split(" ")[7]));
                        else if (reply.equalsIgnoreCase("no"))
                            dbo.insertRejectedParticipant(line.split(" "), Main.server.imageToByteArray(line.split(" ")[7]));
                    } else
                    //add to arraylist
                    {
                        System.out.println(line);
                        participants.add(line);
                    }
                }
            }
            System.out.println("clearing participants file");
            //clear the participant file
            clearFile("src/participants.txt");
            //copy the contents of the arraylist to the participants file
            for (String participant:participants){
                try (BufferedWriter bufferedFileeWriter = new BufferedWriter(new FileWriter("src/participants.txt",true));
                ){
                    bufferedFileeWriter.write(participant);
                    bufferedFileeWriter.newLine();
                }

            }

            //clear the arrayilst
            participants.clear();


            Main.server.printWriter.println("done");

            //close the file reader
            bufferedFileReader.close();

        }catch (IOException e){
                System.out.println(e.getMessage());
            }
        dbo.close();


    }
    //a method that clears a file given the file directory
    public void clearFile(String fileDirectory ){
        try {
            BufferedWriter bufferedFileWriter = new BufferedWriter(new FileWriter(fileDirectory));
            bufferedFileWriter.write("");
            bufferedFileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}