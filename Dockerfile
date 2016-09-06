FROM java:8
ADD server.jar .
EXPOSE 2222
CMD java -jar server.jar


