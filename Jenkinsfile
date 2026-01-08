pipeline {
    agent any

    tools {
        jdk 'JDK-21'
    }

    environment {
        MAVEN_REPO_URL = credentials('MAVEN_REPO_URL')
        MAVEN_USER = credentials('MAVEN_USER')
        MAVEN_PASS = credentials('MAVEN_PASS')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
        SLACK_WEBHOOK = credentials('SLACK_WEBHOOK')
        GMAIL_USER = credentials('GMAIL_USER')
        GMAIL_PASS = credentials('GMAIL_PASS')
    }

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
                script {
                    withSonarQubeEnv('sonar') {
                                            bat """
                                                ./gradlew sonar \
                                                -Dsonar.projectKey=TP7 \
                                                -Dsonar.projectName=TP7 \
                                                -Dsonar.host.url=http://localhost:9000 \
                                                -Dsonar.token=%SONAR_TOKEN%
                                            """
                                        }
                }
            }
        }
        stage('Code Quality') {
            steps {
                script {
                    echo "code quality"
                    echo "Vérification du Quality Gate..."

                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Le Quality Gate a échoué: ${qg.status}"
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
