pipeline {
    agent any
    stages {
        stage ('code analysis') {
                    tools {
                        jdk 'JDK-11'
                    }
                    steps {
                        bat './gradlew sonar'
                    }
        }
        stage ('build') {
            steps {
                bat './gradlew build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}
