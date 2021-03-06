package main.window.menu;

import console.ViewLogsManager;
import console.WarningLevel;
import main.MVCCDManager;
import main.MVCCDWindow;
import messages.MessagesBuilder;
import preferences.Preferences;
import project.Project;
import repository.editingTreat.ProjectEditingTreat;
import resultat.Resultat;
import resultat.ResultatElement;
import resultat.ResultatLevel;
import utilities.Trace;
import utilities.window.DialogMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinMenuContent implements ActionListener {

    private MVCCDWindow mvccdWindow;
    private JMenuBar menuBar;
    private JMenu project;
    private JMenu edit;
    private JMenu profile;
    private JMenu help;

    private JMenuItem projectNew;
    private JMenuItem projectEdit;
    private JMenuItem projectOpen;
    private JMenu projectOpenRecents;
    private JMenuItem[] projectOpenRecentsItems;


    private JMenuItem projectClose;
    private JMenuItem projectSave;
    private JMenuItem projectSaveAs;
    /*
    private JMenu projectPreferences;
    private JMenuItem projectPreferencesDeveloper;
    private JMenuItem projectPreferencesGeneral;
    private JMenuItem projectPreferencesMCD;
    private JMenuItem projectProfile;

    private JMenuItem profileCreate;
    */


    public WinMenuContent(MVCCDWindow mvccdWindow, JMenuBar menuBar) {
        this.mvccdWindow = mvccdWindow;
        this.menuBar = menuBar;

        project = new JMenu("Projet");
        menuBar.add(project);
        edit = new JMenu("Edition");
        menuBar.add(edit);
        /*
        profile = new JMenu("Profil");
        menuBar.add(profile);
        */

        help = new JMenu("Aide");
        menuBar.add(help);

        projectNew = new JMenuItem("Nouveau");
        projectNew.addActionListener(this);
        project.add(projectNew);
        projectEdit = new JMenuItem("Propriétés");
        projectEdit.addActionListener(this);
        projectEdit.setEnabled(false);
        project.add(projectEdit);
        projectOpen = new JMenuItem("Ouvrir...");
        projectOpen.addActionListener(this);
        project.add(projectOpen);
        projectOpenRecents = new JMenu("Ouvrir récents");
        projectOpenRecents.addActionListener(this);
        project.add(projectOpenRecents);

        projectOpenRecentsItems = new JMenuItem[Preferences.FILES_RECENTS_AUTHORIZED];
        for (int i=1 ; i<= Preferences.FILES_RECENTS_AUTHORIZED; i++){
            projectOpenRecentsItems[i-1] = new JMenuItem();
            projectOpenRecentsItems[i-1].addActionListener(this);
            projectOpenRecentsItems[i-1].setVisible(false);
            projectOpenRecents.add(projectOpenRecentsItems[i-1]);
        }


        projectClose = new JMenuItem("Fermer");
        projectClose.addActionListener(this);
        projectClose.setEnabled(true);
        project.add(projectClose);

        project.addSeparator();
        projectSave = new JMenuItem("Sauver");
        projectSave.addActionListener(this);
        projectSave.setEnabled(false);
        project.add(projectSave);
        projectSaveAs = new JMenuItem("Sauver comme copie");
        projectSaveAs.addActionListener(this);
        projectSaveAs.setEnabled(false);
        project.add(projectSaveAs);

        project.addSeparator();

        /*
        projectPreferences = new JMenu("Préférences");
        projectPreferences.addActionListener(this);
        projectPreferencesDeveloper = new JMenuItem("Développeur");
        projectPreferencesDeveloper.addActionListener(this);
        projectPreferences.add(projectPreferencesDeveloper);
        projectPreferencesGeneral = new JMenuItem("Général");
        projectPreferencesGeneral.addActionListener(this);
        projectPreferences.add(projectPreferencesGeneral);
        projectPreferencesMCD = new JMenuItem("MCD");
        projectPreferencesMCD.addActionListener(this);
        projectPreferences.add(projectPreferencesMCD);
        project.add(projectPreferences);

        projectProfile = new JMenuItem("Profil");
        project.add(projectProfile);

        profileCreate = new JMenuItem("Créer/Modifier");
        profile.add(profileCreate);
*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Resultat resultat = new Resultat() ;
        String messageExceptionTarget = "";
        Boolean dialogQuittance = false;
        try {
            Object source = e.getSource();
            if (source == projectNew) {
                messageExceptionTarget = MessagesBuilder.getMessagesProperty("project.new.exception");
                resultat = newProject();
            }
            if (source == projectEdit) {
                messageExceptionTarget = MessagesBuilder.getMessagesProperty("project.edit.exception");
                resultat = ProjectEditingTreat.treatUpdate(mvccdWindow);
            }
            if (source == projectOpen) {
                messageExceptionTarget = MessagesBuilder.getMessagesProperty("project.open.exception");
                resultat = openProject();
             }
            for (int i = 0; i < Preferences.FILES_RECENTS_AUTHORIZED; i++) {
                messageExceptionTarget = MessagesBuilder.getMessagesProperty("project.recent.open.exception");
                if (source == projectOpenRecentsItems[i]) {
                    resultat = openProjectRecent(projectOpenRecentsItems[i].getText());
                }
            }

            if (source == projectClose) {
                messageExceptionTarget = MessagesBuilder.getMessagesProperty("project.close.exception");
                boolean confirmClose = true;
                if (MVCCDManager.instance().isDatasProjectChanged()) {
                    String message = MessagesBuilder.getMessagesProperty("project.close.change.not.saved");
                    confirmClose = DialogMessage.showConfirmYesNo_No(mvccdWindow, message) == JOptionPane.YES_OPTION;
                }
                if (confirmClose) {
                    resultat = MVCCDManager.instance().closeProject();
                }
            }

            if (source == projectSave) {
                messageExceptionTarget = MessagesBuilder.getMessagesProperty("project.save.exception");
                dialogQuittance = true;
                resultat = MVCCDManager.instance().saveProject();
            }
            if (source == projectSaveAs) {
                messageExceptionTarget = MessagesBuilder.getMessagesProperty("project.save.as.exception");
                dialogQuittance = true;
                resultat = MVCCDManager.instance().saveAsProject(false);
            }

            // Quittance ok
            ViewLogsManager.printResultat(resultat);
            if (dialogQuittance) {
                ViewLogsManager.dialogQuittance(mvccdWindow, resultat);
            }

        } catch(Exception exception){
            String messageException = MessagesBuilder.getMessagesProperty("bar.menu.exception",
                    messageExceptionTarget);
            ViewLogsManager.catchException(exception, mvccdWindow, messageException);
        }
    }

    private Resultat newProject() {
        Resultat resultat = new Resultat();
        if (MVCCDManager.instance().getProject() == null) {
            Project project = ProjectEditingTreat.treatNew(mvccdWindow);
            if (project != null){
                // Quittance de création d'un nouveau projet
                String message = MessagesBuilder.getMessagesProperty ("project.new", project.getName());
                resultat.add(new ResultatElement(message, ResultatLevel.INFO));
            }
        } else {
            String message = MessagesBuilder.getMessagesProperty ("project.new.not.close");
            DialogMessage.showOk(mvccdWindow,message);
        }
        return resultat;
    }

    private Resultat openProject() {
        if (MVCCDManager.instance().getProject() == null) {
            return MVCCDManager.instance().openProject();
        } else {
            String message = MessagesBuilder.getMessagesProperty ("project.open.not.close");
            DialogMessage.showOk(mvccdWindow,message);
            return new Resultat();
        }
    }

    private Resultat openProjectRecent(String fileName) {
        if (MVCCDManager.instance().getProject() == null) {
            return MVCCDManager.instance().openProjectRecent(fileName);
        } else {
            String message = MessagesBuilder.getMessagesProperty ("project.open.not.close");
            DialogMessage.showOk(mvccdWindow,message);
            return new Resultat();
        }

    }

    public void activateProjectOpenRecentsItem(int i, String text) {
        projectOpenRecentsItems[i].setText(text);
        projectOpenRecentsItems[i].setVisible(true);

        projectOpenRecents.setEnabled(true);
    }

    public void desActivateProjectOpenRecentsItems() {
        for (int i = 0; i < Preferences.FILES_RECENTS_AUTHORIZED; i++) {
            projectOpenRecentsItems[i].setVisible(false);
        }
        projectOpenRecents.setEnabled(false);
    }

    public JMenuItem getProjectEdit() {
        return projectEdit;
    }

    public JMenuItem getProjectSaveAs() {
        return projectSaveAs;

    }public JMenuItem getProjectSave() {
        return projectSave;
    }

    public JMenuItem getProjectClose() {
        return projectClose;
    }
}
