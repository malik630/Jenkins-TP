pipeline {
    agent any
    stages {
        stage ('test') {
            steps{
                bat './gradlew  test jacocoTestReport'
                junit 'build/test-results/test/*.xml'
                cucumber buildStatus: 'UNSTABLE',
                                    fileIncludePattern: '**/*.json',
                                    jsonReportDirectory: 'build/reports/cucumber'
            }
        }
        stage ('code analysis') {
            tools {
                jdk 'JDK-11'
            }
            steps {
                bat './gradlew sonar'
            }
        }
        stage('Code Quality') {
            steps {
                script {
                    echo "code quality"
                    echo "Vérification du Quality Gate..."

                    timeout(time: 5, unit: 'MINUTES') {
                        def qaulityGate = waitForQualityGate()
                        if (qualityGate.status != 'OK') {
                            error "Le Quality Gate a échoué: ${qualityGate.status}"
                        }
                        echo "Quality Gate validé avec succès!"
                    }
                }
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
