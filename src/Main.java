import java.io.IOException;
import java.util.Arrays;


public class Main {
    static Server server=null;
    static String[] request;
   static  String[] initialCommands;
    //handles the

    public static void main(String[] args) {
        Main main = new Main();

        server=new Server(3333);


        initialCommands = new String[]{"register", "login"};



        try {
            while(true) {
//                System.out.println(server.hashPassword("arthur"));
                request = server.reader.readLine().strip().split(" ");


                System.out.println(Arrays.toString(request));
                if (Arrays.asList(initialCommands).contains(request[0])) {
                    DBO dbo = new DBO();
                    dbo.connect();


                    switch (request[0]) {
                        case "register":
                            if ( request.length==9) {
                                if (dbo.checkUsername(request[1])){
                                    server.printWriter.println("invalid username");

                                }else if (dbo.checkRejectedParticipant(request[1])){
                                    server.printWriter.println("invalid rejected");
                                } else if (dbo.checkParticipant(request[1],request[5] )) {
                                    server.printWriter.println("invalid participant");

                                } else if (server.checkUsernameFile(request[1])) {
                                    server.printWriter.println("invalid username");
                                } else if ( server.imageToByteArray(request[8])==null) {
                                    server.printWriter.println("invalid image");

                                } else if(server.register(request)){
                                    server.printWriter.println("valid register");
                                }else {
                                    server.printWriter.println("invalid school");
                                }
                            }else {
                                server.printWriter.println("invalid values");
                            }
                            break;
                        case "login":
                            if (request.length == 3) {
                                server.printWriter.println("valid login");

                                server.login(request);
                            }else {
                                server.printWriter.println("Incomplete command. please enter all the required fields");
                            }
                            break;
                        case "exit":
                            server.close();
                            break;
                        default:
                            server.printWriter.println("invalid");
                    }
                } else {
                    System.out.println("invalid");

                    server.printWriter.println("invalid");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
