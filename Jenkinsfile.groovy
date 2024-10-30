pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'chaima-sassi'
                    withCredentials([usernamePassword(credentialsId: 'c38cdad7-ffdc-4cf7-a254-95950ce5c532', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${branch}"]], // Changed 'branch' to 'name'
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: 'c38cdad7-ffdc-4cf7-a254-95950ce5c532']]
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
            withCredentials([string(credentialsId: '0b0a7f3b-d5e8-40bc-8fd9-3a469de95804', variable: 'SONAR_TOKEN')]) {
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
 
 

    }
}
