[![Build Status](https://travis-ci.org/Org-Pro/trellorg-api.svg?branch=feature-ajouter-tache-correction)](https://travis-ci.org/Trellorg/trellorg-api)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9648a5e072954637af782a8451d4d3cc)](https://www.codacy.com/app/Trellorg/trellorg-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Trellorg/trellorg-api&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/Trellorg/trellorg-api/badge.svg?branch=master)](https://coveralls.io/github/Trellorg/trellorg-api?branch=master)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://github.com/CodeChillAlluna/code-chill/blob/master/LICENSE)

<https://trellorg.github.io/trellorg-api/>

# Guide d'installation

## Pour Linux

### Pré-Requis
Installer JAVA 8  
Les commandes :  
- sudo add-apt-repository ppa:webupd8team/java  
- sudo apt-get update  
- sudo apt-get install oracle-java8-installer  
 
Puis décompresser le projet
    
### Compilation
A partir du répertoire courant du projet :  
Puis :  
- chmod +x gradlew  
- ./gradlew build  
    
### Lancer l'application
A partir du répertoire courant du projet :  
Les commandes : (Remplacer xxx.jar par le nom du fichier)  
- cd build/libs/  
- java -classpath xxx.jar fr.trellorg.api.project.Main
    
## Pour Windows

### Pré-Requis
Installer JAVA 8 (Version 8)  
Via le site : <https://www.java.com/fr/download/>  

Puis décompresser le projet

### Compilation
A partir du répertoire courant du projet :  
Lancer le fichier "build.bat"  

Ou depuis un terminal :  
Les commandes :  
- gradlew.bat build

###  Lancer l'application
A partir du répertoire courant du projet :  
Lancer le fichier "run.bat"  

Ou depuis un terminal :  
Les commandes : (Remplacer xxx.jar par le nom du fichier)  
- cd build/libs/  
- java -classpath xxx.jar fr.trellorg.api.project.Main
