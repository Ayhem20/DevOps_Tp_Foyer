pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository' // Set Maven local repository to a cached location
    }

    options {
        skipStagesAfterUnstable() // Skip remaining stages if any stage fails
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'chaima-sassi'
                    withCredentials([usernamePassword(credentialsId: 'c38cdad7-ffdc-4cf7-a254-95950ce5c532', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: 'c38cdad7-ffdc-4cf7-a254-95950ce5c532']]
                        ])
                    }
                }
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the project...'
                // Wrap Maven build with timeout
                timeout(time: 20, unit: 'MINUTES') {
                    sh 'mvn clean install'
                }
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Add test commands or scripts here, with optional timeout
                timeout(time: 10, unit: 'MINUTES') {
                    sh 'mvn test' // Assuming the project has tests configured
                }
            }
        }
        
        stage('Maven Deploy') {
            steps {
                script {
                    // Wrap Maven deploy with timeout
                    timeout(time: 20, unit: 'MINUTES') {
                        sh 'mvn clean deploy -DskipTests'
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            deleteDir() // Clean workspace after build
        }
        failure {
            echo 'Build failed!'
        }
    }
}
