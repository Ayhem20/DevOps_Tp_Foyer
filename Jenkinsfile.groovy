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
        stage('Build') {
            steps {
                echo 'Building the project...'
                // Add build commands here, e.g., `sh 'mvn clean install'` for a Maven project
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Add test commands here
            }
        }
         stage('Maven Deploy') {
            steps {
                script {
                    sh 'mvn clean deploy -DskipTests'
                }
            }
        }
    }
}


