pipeline {
    agent any
 
    stages {
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'manar'
                    withCredentials([usernamePassword(credentialsId: 'git', usernameVariable: 'manarbouallagui', passwordVariable: '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')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "*/${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: 'git']]
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
                    withCredentials([usernamePassword(credentialsId: 'sonar', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASSWORD')]) {
                        sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=DevOps_Tp_Foyer \
                          -Dsonar.host.url=http://localhost:9001  \
                          -Dsonar.login=$SONAR_USER \
                          -Dsonar.password=$SONAR_PASSWORD
                        '''
                    }
                }
            }
        }
    }
}
 
