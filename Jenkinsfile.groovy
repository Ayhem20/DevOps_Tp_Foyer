pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'ayhem'
                    withCredentials([usernamePassword(credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "*/${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de']]
                        ])
                    }
                }
            }
        }

        stage('Maven Clean and Compile') {
            steps {
                script {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // SonarQube credentials
                    def sonarUser = 'admin'
                    def sonarPassword = 'Magrouch123!'
                    
                    sh '''
                    mvn sonar:sonar \
                      -Dsonar.projectKey=DevOps_Tp_Foyer \
                      -Dsonar.host.url=http://192.168.33.10:9000 \
                      -Dsonar.login=${sonarUser} \
                      -Dsonar.password=${sonarPassword}
                    '''
                }
            }
        }
    }
}
