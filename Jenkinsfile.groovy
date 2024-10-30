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
                            branches: [[name: "${branch}"]],
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

        stage('Maven Package') {
            steps {
                script {
                    sh 'mvn package'
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
stage('Deploy to Nexus') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: '774b0f42-75e2-4ee5-8c86-e421249c4010', usernameVariable: 'admin', passwordVariable: 'Sassii260994.')]) {
                sh '''
                mvn deploy:deploy-file \
                  -Durl=http://192.168.56.4:8081/repository/maven-releases/ \
                  -DrepositoryId=deploymentRepo \
                  -Dfile=target/tp-foyer-5.0.0.jar \  # Specify the path to your jar file here
                  -DgroupId=tn.esprit \
                  -DartifactId=tp-foyer \
                  -Dversion=5.0.0 \
                  -Dpackaging=jar \
                  -DgeneratePom=true \
                  -Drepository.username=admin \
                  -Drepository.password=Sassii260994 \
                  -Dmaven.test.skip=true
                '''
            }
        }
    }
}
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
