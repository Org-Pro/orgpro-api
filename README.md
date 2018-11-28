[![Build Status](https://travis-ci.org/Org-Pro/orgpro-api.svg?branch=sync-api-google)](https://travis-ci.org/Org-Pro/orgpro-api)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9648a5e072954637af782a8451d4d3cc)](https://www.codacy.com/app/Trellorg/orgpro-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Org-Pro/orgpro-api&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/Org-Pro/orgpro-api/badge.svg?branch=sync-api-google)](https://coveralls.io/github/Org-Pro/orgpro-api?branch=sync-api-google)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://github.com/CodeChillAlluna/code-chill/blob/master/LICENSE)

<https://org-pro.github.io/orgpro-api/>

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

### Récupération
Vous pouvez récupérer la librairie JAVA pour l'importer dans votre projet via le chemin :  
- A partir du répertoire courant du projet : /build/lib/
    
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

### Récupération
Vous pouvez récupérer la librairie JAVA pour l'importer dans votre projet via le chemin :  
- A partir du répertoire courant du projet : /build/lib/
