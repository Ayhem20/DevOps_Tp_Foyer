pipeline {
    agent any

    environment {
        GIT_HTTP_POST_BUFFER = "524288000"  // 500MB
    }

    stages {

        stage('Prepare Git Config') {
            steps {
                // Increase buffer size for handling large transfers
                sh 'git config --global http.postBuffer ${GIT_HTTP_POST_BUFFER}'
            }
        }

        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'ayhem'
                    withCredentials([usernamePassword(credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "*/${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de']],
                            extensions: [
                                [$class: 'CloneOption', depth: 1, noTags: false, shallow: true, timeout: 15], // shallow clone
                                [$class: 'Retry', retries: 3] // retry up to 3 times
                            ]
                        ])
                    }
                }
            }
        }

        stage('Maven Clean, Compile, and Test') {
            steps {
                script {
                    sh 'mvn clean compile test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: '71c996e3-a2eb-47ae-b081-f6d349669f6d', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASSWORD')]) {
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

        stage('Maven Deploy') {
            steps {
                script {
                    sh 'mvn clean deploy -DskipTests'
                }
            }
        }
    }
}
