package fr.orgpro.api.collaborateur;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollaborateurFichier {

    private static CollaborateurFactory fac = new CollaborateurFactory();

    public static boolean ecritureFichier(List<CollaborateurInterface> listcollaborateur, String path, boolean append){
        StringBuilder s = new StringBuilder();
        for (CollaborateurInterface c : listcollaborateur) {
            s.append(c.toString()).append("\n");
        }
        FileWriter ffw = null;
        try {
            ffw = new FileWriter(path,append);
            ffw.write(s.toString());
            ffw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static List<CollaborateurInterface> lectureFichier(String path){
        List<CollaborateurInterface> listCollaborateur = new ArrayList<CollaborateurInterface>();
        String[] temp;
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while (line != null) {
                temp = line.split(";");
                if(CollaborateurType.CLASSIC.toString().equalsIgnoreCase(temp[0])){
                    listCollaborateur.add(lectureLineClassic(line));
                }
                line = br.readLine();
            }
        }catch (IOException e) {
            return null;
        }
        return listCollaborateur;
    }

    private static CollaborateurInterface lectureLineClassic(String line){
        String[] temp;
        temp = line.split(";");
        CollaborateurInterface collab = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab.setNom(temp[1]);
        return collab;
    }
}
