pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                bat 'gradle build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}
