
pipeline {
    agent any


    stages {
        // Stage to check out the code from GitHub
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'chamsdine_madouri'
                    withCredentials([usernamePassword(credentialsId: '87706fb2-7071-42b7-94df-fb3b77774c16', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: '87706fb2-7071-42b7-94df-fb3b77774c16']]
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
                    withCredentials([string(credentialsId: '88c62664-596c-422b-9324-f7664ad46b5f', variable: 'SONAR_TOKEN')]) {
                        sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=DevOps_Tp_Foyer \
                          -Dsonar.host.url=http://192.168.56.4:9000 \
                          -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }

        // Stage to deploy to Nexus
        stage('Deploy to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: '01ea8ff3-c09c-4246-a03e-a9063f041a51', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh '''
                          mvn clean deploy -DskipTests
                        '''
                    }
                }
            }
        }
}
}