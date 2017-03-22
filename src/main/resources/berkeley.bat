@ECHO OFF
pushd "%~dp0"
cmd /c java -jar berkeleymy.jar -gr eng_sm6.gr  -inputFile %4  -maxLength 399 -sentence_likelihood -kbest 2