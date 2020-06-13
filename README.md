# Exhibition Calendar

Существует список Выставочных залов в каждом из которых есть перечень Экспозиций. 
Посетитель покупает Билеты оформив Платёж и выбрав Тему выставки.

## Installation instruction
git clone https://github.com/OlesiiukAnna/ExhibitionCalendarServlet.git

## Application launch instructions
1. Install database and change your access properties in a 
[file](.src/main/resources/dao.properties) 
with example data (for real project never push this file to github)
2. Build the project with maven ```mvn package``` 
3. Download Tomcat copy the WAR file into $CATALINA_HOME\webapps directory
4. Go to root folder $CATALINA_HOME\bin and in terminal run the command 
   ```./catalina.sh start``` to run Tomcat 
5. Go to url http://localhost:8080/ExhibitionCalendarSpring