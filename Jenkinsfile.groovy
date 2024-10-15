pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'ayhem' // Your branch name
                    checkout([$class: 'GitSCM', 
                        branches: [[name: "*/${branch}"]], 
                        userRemoteConfigs: [[url: repoUrl]]
                    ])
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
                    // Assurez-vous que les variables d'environnement nécessaires sont définies
                    withCredentials([usernamePassword(credentialsId: '71c996e3-a2eb-47ae-b081-f6d349669f6d', usernameVariable: 'admin', passwordVariable: 'Magrouch123!')]) {
                        sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=DevOps_Tp_Foyer \
                          -Dsonar.host.url=http://192.168.33.10:9000 \
                          -Dsonar.login=$SONAR_USER \
                          -Dsonar.password=$SONAR_PASSWORD
                        '''
                    }
                }
            }
        }

        // Ajoutez d'autres stages ici si nécessaire
    }
}
