@echo off
title Ählichkeitssuche in Multimediadaten - Testschleife
 
set JAVA_HOME = "C:\Program Files\Java\jre1.8.0_191"
set PATH="C:\Program Files\Java\jdk1.8.0_191\bin"
set ARGUMENTS=-Xmx6000M
set JAVA=%JAVA_BIN%java
set JAVA_DIR = "C:\Users\ACER\Git Repositories\Projekt Master Branch2\Masterprojekt-SoSe19\Implementation"
set neighbours = 4
set dens = 0.1
set penalty = -1
set ALGORITHM = "EMD (Euclid)"


:: Parameter setzen
:: Bildpfad
set img_path="C:\Users\ACER\Git Repositories\Projekt Master Branch2\Masterprojekt-SoSe19\Implementation\resources\images"
:: Vergleichsbild setzen
set dateiname  = "Auto01.png"

set img= "C:\Users\ACER\Git Repositories\Projekt Master Branch2\Masterprojekt-SoSe19\Implementation\resources\images\Auto01.png"

::call java -jar Masterprojekt.jar "C:\Users\ACER\Git Repositories\Projekt Master Branch2\Masterprojekt-SoSe19\Implementation\resources\images\Auto01.png" "C:\Users\ACER\Git Repositories\Projekt Master Branch2\Masterprojekt-SoSe19\Implementation\resources\images" 4 0.1 -1 "EMD (Euclid)"
cd %JAVA_DIR%
::call java %JAVA_OPTIONS% -jar Masterprojekt.jar %img% %img_path% %neighbours% %dens% %penalty% %ALGORITHM%
call java -jar Masterprojekt.jar "C:\Users\ACER\Git Repositories\Projekt Master Branch2\Masterprojekt-SoSe19\Implementation\resources\images\Auto01.png" "C:\Users\ACER\Git Repositories\Projekt Master Branch2\Masterprojekt-SoSe19\Implementation\resources\images" 4 0.1 -1 "EMD (Euclid)"
pause