FROM ubuntu

RUN apt-get update                     &&\
    apt-get install -y openjdk-8-jdk   &&\
    apt-get install -y maven           &&\
    apt-get clean

VOLUME /home/christian/IdeaProjects
ADD . /home/christian/IdeaProjects
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "home/christian/IdeaProjects/MathTrainerWebApp/target/mathtrainer.jar"]
CMD ["/bin/bash"]
#RUN cd /home/christian/IdeaProjects/MathTrainerWebApp/ &&\
#        mvn clean install
CMD cd /home/christian/IdeaProjects/MathTrainerWebApp/ && mvn clean install
