import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.security.*;

public class Server {
    private ServerSocket serverSocket=null;
    private Socket socket=null;
    protected BufferedReader reader=null;
    protected PrintWriter printWriter=null;
    protected FileWriter fileWriter=null;

    protected BufferedWriter bufferedFileWriter=null;


    Server(int port){
        try {
            //creating server
            serverSocket=new ServerSocket(port);
            System.out.println("server created successfully!!");
            System.out.println("Listening");

            //listening for requests
                    socket=serverSocket.accept();
            System.out.println("Connected!!");
            //configuring input
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // configuring output stream
            printWriter=new PrintWriter(socket.getOutputStream(),true);




        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void close(){
        try {
            serverSocket.close();
            printWriter.close();
            reader.close();
            socket.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
            return;
        }

    }

    public boolean register(String[] request) {
        DBO dbo=new DBO();

        dbo.connect();

        if (dbo.checkSchoolExists(request[7])){
            try {
                fileWriter=new FileWriter("src/participants.txt",true);
                bufferedFileWriter = new BufferedWriter(fileWriter);
                bufferedFileWriter.write(request[1]+" "+request[2]+" "+request[3]+" "+request[4]+" "+ hashPassword(request[5])+" "+request[6]+" "+request[7]+" "+request[8]);
                bufferedFileWriter.newLine();
                bufferedFileWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return true;
        } else return false;


    }

    public void login(String[] request) {
        DBO dbo = new DBO();
        dbo.connect();
        if (dbo.checkParticipant(request[1],hashPassword(request[2]) )){
            printWriter.println("participant "+request[1]);
            Participant participant = new Participant(dbo.getParticipantDetails(request[1]));
            participant.start();

        }
        else if (dbo.checkRepresentative(request[1],hashPassword(request[2]) )) {
            printWriter.println("representative "+request[1]);
            Representative rep = new Representative(dbo.getRepresentative(request[1]));
            rep.validateParticipant();
            dbo.close();
        }
        else printWriter.println("invalid");

    }
    //a method to hash the password
    public String hashPassword(String password){
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            hashedPassword= sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }
    //a method to check that username and email are not in the file participants.txt already using a file reader
    public boolean checkUsernameFile(String username) {
        String line;
        try {
            BufferedReader bufferedFileReader = new BufferedReader(new FileReader("src/participants.txt"));
            while ((line = bufferedFileReader.readLine()) != null) {
                if (line.split(" ")[0].equals(username)) return true;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public byte[] imageToByteArray(String imagePath) {
        try {
            BufferedImage bImage = ImageIO.read(new File(imagePath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
            return bos.toByteArray();
        } catch (IOException e) {
            System.out.println("Error converting image to byte array: " + e.getMessage());
            return null;
        }
    }
}